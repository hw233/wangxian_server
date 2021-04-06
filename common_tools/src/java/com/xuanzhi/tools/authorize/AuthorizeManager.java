package com.xuanzhi.tools.authorize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.*;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

public class AuthorizeManager implements ConfigFileChangedListener, Runnable {

    static Logger logger = Logger.getLogger(AuthorizeManager.class);

    protected File configFile = null;
    protected URL configURL = null;
    protected boolean autoLoad = false;
    protected UserManager userManager;
    protected RoleManager roleManager;
    protected PlatformManager platformManager;
    protected PermissionManager permissionManager;
    protected String proxyHost;
    protected int proxyPort;

    public String[] m_ipAllows = new String[]{
    		"118.163.83.235",//昆仑台湾
    		"61.148.75.238",//昆仑-北京
    		"112.136.245.162",//昆仑韩国地区办公ip
    		"112.136.245.182",//昆仑韩国地区办公ip
    		"121.78.58.16",//韩服网关
    		"119.81.29.67",//马来网关
    		"211.72.246.23",//台湾网关
    		"117.135.128.177",//腾讯网关
    		"10.147.21.100",//腾讯映射服
    		"106.120.222.214",//公司IP
    		"124.248.40.5",//squid代理
    		"116.213.142.183",//国服网关
    		"116.213.192.200",//国服GM
    		"119.81.29.67" //原公司IP
    	};
    protected String authorizePlatformPreURL = "http://+116.213.142.183:8882/+game_gateway/+admin/+authorize/+.*";

    private long lastReadUrlTime = System.currentTimeMillis();
    private String urlContent = null;
    private Thread thread = null;
    private long urlCheckStep = 60 * 1000L;

    private static AuthorizeManager m_self;

    public static AuthorizeManager getInstance() {
        return m_self;
    }

    public boolean isPlatformAccessEnable(User user, String url, String ip) {
    	while(url.indexOf("//") >= 7){
    		url = url.replaceAll("//", "/");
    	}
    	
    	//权限系统禁止公司外ip访问
//    	if(url.toLowerCase().matches(authorizePlatformPreURL)){
    		if(isIpAllows(ip) == false){
    			AuthorizedServletFilter.logger.info("[IP_Not_Allow] ["+ip+"]");
    			return false;
    		}
//    	}
    	
        Platform platform = platformManager.getPlatformByUrl(url);
        if (platform == null) return false;
        boolean isOfficeAddress = isIpAllows(ip);
        Role roles[] = user.getRoles();
        boolean canAccess = false;
        for (int i = 0; i < roles.length; i++) {
            if (roles[i].isValid() == false) continue;
            PlatformPermission pp = permissionManager.getPlatformPermission(platform, roles[i]);
            if (pp != null) {
                if (pp.isAccessOutOfficeEnable() || isOfficeAddress) {
                    int permission = pp.getPermission();
                    if (permission == Platform.WHOLE_PERMISSION) {
                        canAccess = true;
                        break;
                    } else if (permission == Platform.PART_PERMISSION) {
                        Module module = platformManager.getModuleByUrl(platform, url);
                        if (module != null) {
                            canAccess = isModuleAccessEnable(roles[i], module, url, isOfficeAddress);
                            if (canAccess) break;
                        }
                    }
                }
            }
        }
        return canAccess;
    }


    public boolean isAbstractUrlAccessEnable(User user, String url, String ip) {

        Platform[] ps = platformManager.getPlatforms();
        ArrayList<Platform> al = new ArrayList<Platform>();
        for (int i = 0; i < ps.length; i++) {
            String s = ps[i].getUrl();
            if (url.startsWith(s)) {
                al.add(ps[i]);
            }
        }
        ps = al.toArray(new Platform[0]);
        int k = -1;
        int len = -1;
        for (int i = 0; i < ps.length; i++) {
            String s = ps[i].getUrl();
            if (s.length() > len) {
                len = s.length();
                k = i;
            }
        }
        Platform platform = null;
        if (k >= 0) platform = ps[k];
        if(platform == null)return false;

        boolean canAccess = false;
        boolean isOfficeAddress = isIpAllows(ip);
        Role roles[] = user.getRoles();
        for (Role role : roles) {
            if (role.isValid() == false) continue;
            PlatformPermission pp = permissionManager.getPlatformPermission(platform, role);
            if (pp != null) {
                if (pp.isAccessOutOfficeEnable() || isOfficeAddress) {
                    int permission = pp.getPermission();
                    if (permission == Platform.WHOLE_PERMISSION) {
                        canAccess = true;
                        break;
                    } else if (permission == Platform.PART_PERMISSION) {
                        Module module = getModuleByAbstractUrl(platform, url);
                        if (module != null) {
                            canAccess = isModuleAccessEnable(role, module, url, isOfficeAddress);
                            if (canAccess) break;
                        }
                    }
                }
            }
        }
        return canAccess;
    }

    private Module getModuleByAbstractUrl(Platform platform,String url){
		Module modules[] = platform.getModules();
		ArrayList<Module> al = new ArrayList<Module>();
        for (Module module : modules) {
            String platformUrl = platform.getUrl();
            if (!url.startsWith(platformUrl)) return null;
            String uri = url.substring(platformUrl.length());
            while (uri.length() > 0 && uri.charAt(0) == '/') {
                uri = uri.substring(1);
            }
            String uri2 = module.getUri();
            while (uri2.length() > 0 && uri2.charAt(0) == '/') {
                uri2 = uri2.substring(1);
            }
            if (uri.matches(uri2)) {
                al.add(module);
            }
        }

		if(al.size() == 0) return null;
		modules = al.toArray(new Module[0]);
		int k = -1;
		int len = -1;
		for(int i = 0 ; i < modules.length ; i++){
			Module module = modules[i];
			if(module.getUri().length() > len){
				len = module.getUri().length();
				k = i;
			}
		}
		return modules[k];
	}


    public boolean isModuleAccessEnable(Role role, Module module, String url, boolean isOfficeAddress) {
        ModulePermission mp = permissionManager.getModulePermission(module, role);
        if (mp != null) {
            if (mp.isAccessOutOfficeEnable() || isOfficeAddress) {
                int permission = mp.getPermission();
                if (permission == Module.WHOLE_PERMISSION) {
                    return true;
                } else if (permission == Module.PART_PERMISSION) {
                    WebPage ws[] = module.getWebPages();
                    for (int i = 0; i < ws.length; i++) {
                        WebPagePermission wp = permissionManager.getWebPagePermission(ws[i], role);
                        if (wp != null) {
                            if (wp.isAccessOutOfficeEnable() || isOfficeAddress) {
                                int p2 = wp.getPermission();
                                if (p2 == WebPage.WHOLE_PERMISSION) {
                                    if (url.matches(ws[i].pagePattern)) return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setIpAllows(String allows) {
        m_ipAllows = allows.split("[ ,;]+");
    }

    /**
     * 判断来取消息的机器，是否在允许的范围内
     *
     * @param remoteHost
     * @return
     */
    public boolean isIpAllows(String remoteHost) {

        if (m_ipAllows == null)
            return true;

        for (int i = 0; i < m_ipAllows.length; i++) {
            if (remoteHost.matches(m_ipAllows[i])) {
            	AuthorizedServletFilter.logger.info("[IP_Allow] ["+remoteHost+"] [matched:"+m_ipAllows[i]+"]");	
                return true;
            } else {
            	AuthorizedServletFilter.logger.info("[IP_Not_Allow] ["+remoteHost+"] [matched:"+m_ipAllows[i]+"]");
            }
        }
        return false;
    }

    public String getIpAllows() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; m_ipAllows != null && i < m_ipAllows.length; i++) {
            if (i < m_ipAllows.length - 1) {
                sb.append(m_ipAllows[i] + ",");
            } else {
                sb.append(m_ipAllows[i]);
            }
        }
        return sb.toString();
    }

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public boolean isAutoLoad() {
        return autoLoad;
    }

    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public PlatformManager getPlatformManager() {
        return platformManager;
    }

    public void setPlatformManager(PlatformManager platformManager) {
        this.platformManager = platformManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void init() throws Exception {

        if (configFile == null && configURL == null) {
            throw new Exception("configFile not configure!");
        }

        userManager = new UserManager();
        roleManager = new RoleManager();
        platformManager = new PlatformManager();
        permissionManager = new PermissionManager();

        userManager.roleManager = roleManager;
        permissionManager.rm = roleManager;
        permissionManager.pm = platformManager;

        if (configFile != null) {
            if (autoLoad && configFile.isFile() && configFile.exists()) {
                ConfigFileChangedAdapter c = ConfigFileChangedAdapter.getInstance();
                c.addListener(configFile, this);
            }
            if (configFile.exists() && configFile.isFile()) {
                loadConfig(configFile);
            }
        } else if (configURL != null) {
            loadConfig(configURL);
            if (autoLoad) {
                thread = new Thread(this, "AuthorizeManager_CheckURL_Thread");
                thread.start();
            }
        }
        
        for(String ip : this.m_ipAllows) {
        	AuthorizedServletFilter.logger.info("[授权的IP地址] ["+ip+"]");
        }
        
        m_self = this;
    }

    private void loadConfig(File file) {
        try {
            Document dom = XmlUtil.load(file.getAbsolutePath());
            loadFromDocument(dom);
        } catch (Exception e) {
            System.out.println("load authorize config file [" + file + "] error:");
            e.printStackTrace(System.out);
            logger.warn("load authorize config file [" + file + "] error:", e);
        }
    }

    private void loadConfig(URL url) {
        try {
        	 byte content[] = new byte[0];
        	if(proxyHost != null) {
        		content = HttpUtils.webGet(url, null, proxyHost, proxyPort, 5000, 5000);
        	} else {
        		content = HttpUtils.webGet(url, null, 5000, 5000);
        	}
            String domStr = new String(content);
            if (!domStr.equals(urlContent)) {
                Document dom = XmlUtil.loadString(domStr);
                loadFromDocument(dom);
            }
            this.lastReadUrlTime = System.currentTimeMillis();
            this.urlContent = domStr;

        } catch (Exception e) {
            System.out.println("load authorize config url [" + url + "] error:");
            e.printStackTrace(System.out);
            logger.warn("load authorize config url [" + url + "] error:", e);
        }
    }

    private void loadFromDocument(Document dom) {
        try {
            Element root = dom.getDocumentElement();

            Element roleRoot = XmlUtil.getChildByName(root, "roles");
            roleManager.loadFromElement(roleRoot);

            Element platformRoot = XmlUtil.getChildByName(root, "platforms");
            platformManager.loadFromElement(platformRoot);

            Element userRoot = XmlUtil.getChildByName(root, "users");
            userManager.loadFromElement(userRoot);

            Element permRoot = XmlUtil.getChildByName(root, "permissions");
            permissionManager.loadFromElement(permRoot);

            try {
	            String ipallows = XmlUtil.getAttributeAsString(root, "ipallows", null);
	            if (ipallows != null) {
	                this.setIpAllows(ipallows);
	            }
            } catch(IllegalArgumentException e) {
            	e.printStackTrace();
            }

            System.out.println("[INFO] [" + DateUtil.formatDate(new java.util.Date(), "yyyy-MM-dd HH:mm:ss") + "] [AuthorizeManager Load Config Success!]");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            logger.warn("", e);
        }
    }

    public void saveTo(File file) {

        if (file == null) throw new NullPointerException("saveTo error cause file is null.");

        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version='1.0' encoding='"+System.getProperty("file.encoding","utf-8")+"'?>\n");
        sb.append("<authroize ipallows='" + StringUtil.escapeForXML(this.getIpAllows()) + "'>\n");
        sb.append(roleManager.saveToElementString());
        sb.append(platformManager.saveToElementString());
        sb.append(userManager.saveToElementString());
        sb.append(permissionManager.saveToElementString());
        sb.append("</authroize>\n");

        if (file.exists() && file.isFile()) {
            File back = new File(file.getAbsolutePath() + ".bak");
            try {
                FileOutputStream output = new FileOutputStream(back);
                FileInputStream input = new FileInputStream(file);
                byte buffer[] = new byte[2048];
                int n = 0;
                while ((n = input.read(buffer)) > 0) {
                    output.write(buffer, 0, n);
                }
                input.close();
                output.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        try {
            FileOutputStream output = new FileOutputStream(file);
            output.write(sb.toString().getBytes());
            output.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void fileChanged(File file) {
        loadConfig(configFile);
    }

    public URL getConfigURL() {
        return configURL;
    }

    public void setConfigURL(URL configURL) {
        this.configURL = configURL;
    }

    public void run() {
        while (thread.isInterrupted() == false) {
            try {
                if (System.currentTimeMillis() - this.lastReadUrlTime > urlCheckStep) {
                    loadConfig(configURL);
                }
                Thread.sleep(30000L);
            } catch (Throwable e) {
                logger.warn("URL check error:", e);
            }
        }

    }

    public long getUrlCheckStep() {
        return urlCheckStep;
    }

    public void setUrlCheckStep(long urlCheckStep) {
        this.urlCheckStep = urlCheckStep;
    }

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getAuthorizePlatformPreURL() {
		return authorizePlatformPreURL;
	}

	public void setAuthorizePlatformPreURL(String authorizePlatformPreURL) {
		this.authorizePlatformPreURL = authorizePlatformPreURL;
	}
    
	public static void main(String args[]) {
	    String[] m_ipAllows = new String[]{"106.120.222.214","124.248.40.5","116.213.142.183","10.147.21.100","116.213.192.200"};
	    String remoteHost = "10.147.21.100";
		for (int i = 0; i < m_ipAllows.length; i++) {
            if (remoteHost.matches(m_ipAllows[i]))
        		System.out.println("[IP_Allow] ["+remoteHost+"] [matched:"+m_ipAllows[i]+"]");
            else
        		System.out.println("[IP_Not_Allow] ["+remoteHost+"] [matched:"+m_ipAllows[i]+"]");
        }
	}
}
