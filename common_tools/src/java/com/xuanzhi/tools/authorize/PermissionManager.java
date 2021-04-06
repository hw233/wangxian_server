package com.xuanzhi.tools.authorize;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class PermissionManager {
	static Logger logger = Logger.getLogger(PermissionManager.class);
	protected PlatformManager pm;
	protected RoleManager rm;
	
	protected ArrayList<PlatformPermission> ppList = new ArrayList<PlatformPermission>();
	protected ArrayList<ModulePermission> mpList = new ArrayList<ModulePermission>();
	protected ArrayList<WebPagePermission> wpList = new ArrayList<WebPagePermission>();
	
	public PlatformPermission[] getPlatformPermissions(){
		return ppList.toArray(new PlatformPermission[0]);
	}
	
	public PlatformPermission addPlatformPermission(Platform p,Role r,int permission){
		PlatformPermission pp = getPlatformPermission(p,r);
		if(pp != null){
			pp.setPermission(permission);
		}
		else{
			pp = new PlatformPermission(p,r,permission);
			ppList.add(pp);
		}
		logger.warn("[add-platformpermission] ["+p.getName()+"] ["+r.getName()+"] ["+Platform.PERMISSION_NAMES[permission]+"]");
		return pp;
	}
	
	public void removePlatformPermission(Platform p,Role r){
		PlatformPermission pp = getPlatformPermission(p,r);
		if(pp != null){
			ppList.remove(pp);
			logger.warn("[remove-platformpermission] ["+p.getName()+"] ["+r.getName()+"] [-]");
		}
	}
	
	public PlatformPermission getPlatformPermission(Platform p,Role r){
		PlatformPermission pps[] = getPlatformPermissions();
		for(int i = 0 ; i < pps.length ;i++){
			if( (pps[i].getPlatform() == p || p.getId().equals(pps[i].getPlatform().getId())) 
					&& (pps[i].getRole() == r || r.getId().equals(pps[i].getRole().getId()))){
				return pps[i];
			}
		}
		return null;
	}
	
	public PlatformPermission[] getPlatformPermissions(Platform p){
		PlatformPermission pps[] = getPlatformPermissions();
		ArrayList<PlatformPermission> al = new ArrayList<PlatformPermission>();
		for(int i = 0 ; i < pps.length ;i++){
			if(pps[i].getPlatform() == p){
				al.add(pps[i]);
			}
		}
		return al.toArray(new PlatformPermission[0]);
	}
	public PlatformPermission[] getPlatformPermissions(Role r){
		PlatformPermission pps[] = getPlatformPermissions();
		ArrayList<PlatformPermission> al = new ArrayList<PlatformPermission>();
		for(int i = 0 ; i < pps.length ;i++){
			if(pps[i].getRole() == r){
				al.add(pps[i]);
			}
		}
		return al.toArray(new PlatformPermission[0]);
	}
	
	
	public boolean isPlatformPermissionExist(Platform p,Role r){
		return getPlatformPermission(p,r) != null;
	}
	

	public ModulePermission[] getModulePermissions(){
		return mpList.toArray(new ModulePermission[0]);
	}
	
	public ModulePermission addModulePermission(Module p,Role r,int permission){
		ModulePermission pp = getModulePermission(p,r);
		if(pp != null){
			pp.setPermission(permission);
		}
		else{
			pp = new ModulePermission(p,r,permission);
			mpList.add(pp);
		}
		logger.warn("[add-modulepermission] ["+p.getPlatform().getName()+"] ["+p.getName()+"] ["+r.getName()+"] ["+Module.PERMISSION_NAMES[permission]+"]");
		return pp;
	}
	
	public void removeModulePermission(Module p,Role r){
		ModulePermission pp = getModulePermission(p,r);
		if(pp != null){
			mpList.remove(pp);
			logger.warn("[remove-modulepermission] ["+p.getPlatform().getName()+"] ["+p.getName()+"] ["+r.getName()+"] [--]");
		}
	}
	
	public ModulePermission getModulePermission(Module p,Role r){
		ModulePermission pps[] = getModulePermissions();
		for(int i = 0 ; i < pps.length ;i++){
			if(p.getId().equals(pps[i].getModule().getId()) && r.getId().equals(pps[i].getRole().getId())){
				return pps[i];
			}
		}
		return null;
	}
	
	public ModulePermission[] getModulePermissions(Module p){
		ModulePermission pps[] = getModulePermissions();
		ArrayList<ModulePermission> al = new ArrayList<ModulePermission>();
		for(int i = 0 ; i < pps.length ;i++){
			if(pps[i].getModule() == p){
				al.add(pps[i]);
			}
		}
		return al.toArray(new ModulePermission[0]);
	}
	public ModulePermission[] getModulePermissions(Role r){
		ModulePermission pps[] = getModulePermissions();
		ArrayList<ModulePermission> al = new ArrayList<ModulePermission>();
		for(int i = 0 ; i < pps.length ;i++){
			if(pps[i].getRole() == r || r.getId().equals(pps[i].getRole().getId())){
				al.add(pps[i]);
			}
		}
		return al.toArray(new ModulePermission[0]);
	}
	
	
	public boolean isModulePermissionExist(Module p,Role r){
		return getModulePermission(p,r) != null;
	}
	
	
	
	public WebPagePermission[] getWebPagePermissions(){
		return wpList.toArray(new WebPagePermission[0]);
	}
	
	public WebPagePermission addWebPagePermission(WebPage p,Role r,int permission){
		WebPagePermission pp = getWebPagePermission(p,r);
		if(pp != null){
			pp.setPermission(permission);
		}
		else{
			pp = new WebPagePermission(p,r,permission);
			wpList.add(pp);
		}
		logger.warn("[add-modulepermission] ["+p.getModule().getPlatform().getName()+"] ["+p.getModule().getName()+"] ["+p.getName()+"] ["+r.getName()+"] ["+WebPage.PERMISSION_NAMES[permission]+"]");
		return pp;
	}
	
	public void removeWebPagePermission(WebPage p,Role r){
		WebPagePermission pp = getWebPagePermission(p,r);
		if(pp != null){
			wpList.remove(pp);
			logger.warn("[add-modulepermission] ["+p.getModule().getPlatform().getName()+"] ["+p.getModule().getName()+"] ["+p.getName()+"] ["+r.getName()+"] [--]");
		}
	}
	
	public WebPagePermission getWebPagePermission(WebPage p,Role r){
		WebPagePermission pps[] = getWebPagePermissions();
		for(int i = 0 ; i < pps.length ;i++){
			if( (pps[i].getWebpage() == p || p.getId().equals(pps[i].getWebpage().getId())) 
					&& (pps[i].getRole() == r || r.getId().equals(pps[i].getRole().getId()))){
				return pps[i];
			}
		}
		return null;
	}
	
	public WebPagePermission[] getWebPagePermissions(WebPage p){
		WebPagePermission pps[] = getWebPagePermissions();
		ArrayList<WebPagePermission> al = new ArrayList<WebPagePermission>();
		for(int i = 0 ; i < pps.length ;i++){
			if(pps[i].getWebpage() == p){
				al.add(pps[i]);
			}
		}
		return al.toArray(new WebPagePermission[0]);
	}
	public WebPagePermission[] getWebPagePermissions(Role r){
		WebPagePermission pps[] = getWebPagePermissions();
		ArrayList<WebPagePermission> al = new ArrayList<WebPagePermission>();
		for(int i = 0 ; i < pps.length ;i++){
			if(pps[i].getRole() == r){
				al.add(pps[i]);
			}
		}
		return al.toArray(new WebPagePermission[0]);
	}
	
	
	public boolean isWebPagePermissionExist(WebPage p,Role r){
		return getWebPagePermission(p,r) != null;
	}
	
	public String saveToElementString(){
		StringBuffer sb = new StringBuffer();
		sb.append("<permissions>\n");
		PlatformPermission[] us = this.getPlatformPermissions();
		for(int i = 0 ; i < us.length ; i++){
			PlatformPermission u = us[i];
			sb.append("<pp platform='"+StringUtil.escapeForXML(u.getPlatform().getId())
					+"' role='"+StringUtil.escapeForXML(u.getRole().getId())
					+"' permission='"+u.getPermission()+"' accessOutOffice='"+u.isAccessOutOfficeEnable()+"'>\n");
			sb.append("</pp>\n");
		}
		ModulePermission[] ms = this.getModulePermissions();
		for(int i = 0 ; i < ms.length ; i++){
			ModulePermission u = ms[i];
			sb.append("<mp platform='"+StringUtil.escapeForXML(u.getModule().getPlatform().getId())
					+"' module='"+StringUtil.escapeForXML(u.getModule().getId())
					+"' role='"+StringUtil.escapeForXML(u.getRole().getId())
					+"' permission='"+u.getPermission()+"' accessOutOffice='"+u.isAccessOutOfficeEnable()+"'>\n");
			sb.append("</mp>\n");
		}
		WebPagePermission[] ws = this.getWebPagePermissions();
		for(int i = 0 ; i < ws.length ; i++){
			WebPagePermission u = ws[i];
			sb.append("<wp platform='"+StringUtil.escapeForXML(u.getWebpage().getModule().getPlatform().getId())
					+"' module='"+StringUtil.escapeForXML(u.getWebpage().getModule().getId())
					+"' webpage='"+StringUtil.escapeForXML(u.getWebpage().getId())
					+"' role='"+StringUtil.escapeForXML(u.getRole().getId())
					+"' permission='"+u.getPermission()+"' accessOutOffice='"+u.isAccessOutOfficeEnable()+"'>\n");
			sb.append("</wp>\n");
		}
		sb.append("</permissions>\n");
		return sb.toString();
	}
	
	public void loadFromElement(Element root){
		ArrayList<PlatformPermission> pp = new ArrayList<PlatformPermission>();
		Element ues[] = XmlUtil.getChildrenByName(root, "pp");
		for(int i = 0 ; i < ues.length ; i++){
			Element e = ues[i];
			String id = XmlUtil.getAttributeAsString(e, "platform", null);
			String rid = XmlUtil.getAttributeAsString(e, "role", null);
			int p = XmlUtil.getAttributeAsInteger(e, "permission",0);
			boolean accessOutOffice = XmlUtil.getAttributeAsBoolean(e, "accessOutOffice",true);
			if(id != null && rid != null){
				Platform platform = pm.getPlatform(id);
				Role role = rm.getRole(rid);
				if(platform != null && role != null){
					PlatformPermission o = new PlatformPermission(platform, role, p);
					o.setAccessOutOfficeEnable(accessOutOffice);
					pp.add(o);
				}
			}
		}
		ppList = pp;
		
		ArrayList<ModulePermission> mp = new ArrayList<ModulePermission>();
		ues = XmlUtil.getChildrenByName(root, "mp");
		for(int i = 0 ; i < ues.length ; i++){
			Element e = ues[i];
			String id = XmlUtil.getAttributeAsString(e, "platform", null);
			String mid = XmlUtil.getAttributeAsString(e, "module", null);
			String rid = XmlUtil.getAttributeAsString(e, "role", null);
			int p = XmlUtil.getAttributeAsInteger(e, "permission",0);
			boolean accessOutOffice = XmlUtil.getAttributeAsBoolean(e, "accessOutOffice",true);
			if(id != null && rid != null && mid != null){
				Platform platform = pm.getPlatform(id);
				Module module = null;
				if(platform != null) module = platform.getModule(mid);
				Role role = rm.getRole(rid);
				if(module != null && role != null){
					ModulePermission o = new ModulePermission(module, role, p);
					o.setAccessOutOfficeEnable(accessOutOffice);
					mp.add(o);
				}
			}
		}
		mpList = mp;
		
		ArrayList<WebPagePermission> wp = new ArrayList<WebPagePermission>();
		ues = XmlUtil.getChildrenByName(root, "wp");
		for(int i = 0 ; i < ues.length ; i++){
			Element e = ues[i];
			String id = XmlUtil.getAttributeAsString(e, "platform", null);
			String mid = XmlUtil.getAttributeAsString(e, "module", null);
			String wid = XmlUtil.getAttributeAsString(e, "webpage", null);
			String rid = XmlUtil.getAttributeAsString(e, "role", null);
			int p = XmlUtil.getAttributeAsInteger(e, "permission",0);
			boolean accessOutOffice = XmlUtil.getAttributeAsBoolean(e, "accessOutOffice",true);
			if(id != null && rid != null && mid != null && wid!= null){
				Platform platform = pm.getPlatform(id);
				Module module = null;
				if(platform != null) module = platform.getModule(mid);
				WebPage webpage = null;
				if(module != null) webpage = module.getWebPage(wid);
				Role role = rm.getRole(rid);
				if(webpage != null && role != null){
					WebPagePermission o = new WebPagePermission(webpage, role, p);
					o.setAccessOutOfficeEnable(accessOutOffice);
					wp.add(o);
				}
			}
		}
		wpList = wp;
		
	}
	
}
