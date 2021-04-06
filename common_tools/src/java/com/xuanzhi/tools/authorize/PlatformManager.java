package com.xuanzhi.tools.authorize;

import java.util.ArrayList;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class PlatformManager {

	protected ArrayList<Platform> platformList = new ArrayList<Platform>();
	
	public Platform getPlatformByUrl(String url){
		 Platform[] ps = getPlatforms();
		 ArrayList<Platform> al = new ArrayList<Platform>();
		 for(int i = 0 ; i < ps.length ; i++){
			 String s = ps[i].getUrl();
			 if(url.matches(s+".*")){
				 al.add(ps[i]);
			 }
		 }
		 ps = al.toArray(new Platform[0]);
		 int k = -1;
		 int len = -1;
		 for(int i = 0 ; i < ps.length ; i++){
			  String s = ps[i].getUrl();
			  if(s.length() > len){
				  len = s.length();
				  k = i;
			  }
		 }
		 if(k >= 0)
			 return ps[k];
		 return null;
	}
	
	public Module getModuleByUrl(Platform platform,String url){
		while(url.indexOf("//") >= 7){
    		url = url.replaceAll("//", "/");
    	}
		
		Module modules[] = platform.getModules();
		ArrayList<Module> al = new ArrayList<Module>();
		for(int i = 0 ; i < modules.length ; i++){
			Module module = modules[i];
			String s = module.getPlatform().getUrl();
			if(url.matches(s+".*") == false) return null;
			
			String p = "("+s+").*";
			java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(p).matcher(url);
			if(matcher.matches() == false) return null;
			String purl = matcher.group(1);
			String uri = url.substring(purl.length());
			
			while(uri.length() > 0 && uri.charAt(0) == '/'){
				uri = uri.substring(1);
			}
			String uri2 = module.getUri();
			while(uri2.length() > 0 && uri2.charAt(0) == '/'){
				uri2 = uri2.substring(1);
			}
			if(uri.matches(uri2)) {
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
	
	public Platform getPlatform(String id){
		for(int i = 0 ; i < platformList.size() ; i++){
			Platform m = platformList.get(i);
			if(m.id != null && m.id.equals(id)){
				return m;
			}
		}
		return null;
	}
	
	public boolean isPlatformExists(String id){
		return (getPlatform(id) != null);
	}
	
	public Platform[] getPlatforms(){
		return platformList.toArray(new Platform[0]);
	}
	
	public Platform addPlatform(String id,String name,String url){
		Platform m = getPlatform(id);
		if(m != null){
			if(name != null && !name.equals(m.name)){
				m.setName(name);
			}
			
			return m;
		}else{
			m = new Platform(id,name,url);
			platformList.add(m);
			return m;
		}
	}
	
	public void removePlatform(String name){
		Platform m = getPlatform(name);
		if(m != null){
			platformList.remove(m);
		}
	}
	
	public String saveToElementString(){
		StringBuffer sb = new StringBuffer();
		sb.append("<platforms>\n");
		Platform[] us = getPlatforms();
		for(int i = 0 ; i < us.length ; i++){
			Platform u = us[i];
			sb.append("<platform id='"+StringUtil.escapeForXML(u.getId())
					+"' name='"+StringUtil.escapeForXML(u.getName())
					+"' url='"+StringUtil.escapeForXML(u.getUrl())+"'>\n");
			Module rs[] = u.getModules();
			for(int j = 0 ; j < rs.length ; j++){
				sb.append("<module id='"+StringUtil.escapeForXML(rs[j].getId())
					+"' name='"+StringUtil.escapeForXML(rs[j].getName())
					+"' uri='"+StringUtil.escapeForXML(rs[j].getUri())+"'>\n");
				WebPage ps[] = rs[j].getWebPages();
				for(int k = 0 ; k < ps.length ; k++){
					sb.append("<webpage id='"+StringUtil.escapeForXML(ps[k].getId())
					+"' name='"+StringUtil.escapeForXML(ps[k].getName())
					+"' pattern='"+StringUtil.escapeForXML(ps[k].getPagePattern())+"'/>\n");
				}
				sb.append("</module>\n");
			}
			sb.append("</platform>\n");
		}
		sb.append("</platforms>\n");
		return sb.toString();
	}
	
	public void loadFromElement(Element root){
		ArrayList<Platform> users = new ArrayList<Platform>();
		Element ues[] = XmlUtil.getChildrenByName(root, "platform");
		for(int i = 0 ; i < ues.length ; i++){
			Element e = ues[i];
			String id = XmlUtil.getAttributeAsString(e, "id", null);
			String name = XmlUtil.getAttributeAsString(e, "name", id,null);
			String url = XmlUtil.getAttributeAsString(e, "url", null);
			
			if(id != null && url != null){
				Platform u = new Platform(id,name,url);
				
				Element res[] = XmlUtil.getChildrenByName(e, "module");
				for(int j = 0 ; j < res.length ; j++){
					String rid = XmlUtil.getAttributeAsString(res[j], "id",null);
					String rname = XmlUtil.getAttributeAsString(res[j], "name",null);
					String ruri = XmlUtil.getAttributeAsString(res[j], "uri",null);
					if(rid != null && ruri != null && rname != null){
						Module m = u.addModule(rid, rname, ruri);
						
						Element wes[] = XmlUtil.getChildrenByName(res[j], "webpage");
						for(int k = 0 ; k < wes.length ; k++){
							String wid = XmlUtil.getAttributeAsString(wes[k], "id",null);
							String wname = XmlUtil.getAttributeAsString(wes[k], "name",null);
							String pattern = XmlUtil.getAttributeAsString(wes[k], "pattern",null);
							if(wid != null && wname != null && pattern != null){
								m.addWebPage(wid, wname, pattern);
							}
						}
					}
				}
				
				users.add(u);
			}
		}
		platformList = users;
	}
	
}
