package com.xuanzhi.tools.authorize;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class RoleManager {

	static Logger logger = Logger.getLogger(RoleManager.class);
	
	protected ArrayList<Role> roleList = new ArrayList<Role>();
	
	public Role getRole(String id){
		for(int i = 0 ; i < roleList.size() ; i++){
			Role m = roleList.get(i);
			if(m.id != null && m.id.equals(id)){
				return m;
			}
		}
		return null;
	}
	
	public boolean isRoleExists(String id){
		return (getRole(id) != null);
	}
	
	public Role[] getRoles(){
		return roleList.toArray(new Role[0]);
	}
	
	public Role addRole(String id,String name){
		Role m = getRole(id);
		if(m != null){
			if(name != null && !name.equals(m.name)){
				m.setName(name);
			}
			
			return m;
		}else{
			m = new Role(id,name);
			roleList.add(m);
			
			logger.warn("[add-role] ["+id+"] ["+name+"]");
			
			return m;
		}
	}
	
	public void removeRole(String name){
		Role m = getRole(name);
		if(m != null){
			roleList.remove(m);
			logger.warn("[remove-role] ["+m.getId()+"] ["+m.getName()+"]");
		}
	}
	
	public String saveToElementString(){
		StringBuffer sb = new StringBuffer();
		sb.append("<roles>\n");
		Role[] us = getRoles();
		for(int i = 0 ; i < us.length ; i++){
			Role u = us[i];
			sb.append("<role id='"+StringUtil.escapeForXML(u.getId())
					+"' name='"+StringUtil.escapeForXML(u.getName())
					+"' valid='"+u.isValid()+"'>\n");
			sb.append("</role>\n");
		}
		sb.append("</roles>\n");
		return sb.toString();
	}
	
	public void loadFromElement(Element root){
		ArrayList<Role> users = new ArrayList<Role>();
		Element ues[] = XmlUtil.getChildrenByName(root, "role");
		for(int i = 0 ; i < ues.length ; i++){
			Element e = ues[i];
			String id = XmlUtil.getAttributeAsString(e, "id", null);
			String name = XmlUtil.getAttributeAsString(e, "name", id,null);
			boolean valid = XmlUtil.getAttributeAsBoolean(e, "valid",false);
			if(id != null){
				Role u = new Role(id,name);
				u.valid = valid;
				users.add(u);
			}
		}
		roleList = users;
	}
	
}
