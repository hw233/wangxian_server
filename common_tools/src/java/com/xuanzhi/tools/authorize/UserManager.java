package com.xuanzhi.tools.authorize;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.*;

public class UserManager {

	static Logger logger = Logger.getLogger(UserManager.class);

	private static String md5_key = "!@#$%^&*()-=";
	private static final String SIGN_KEY = "dfiG92B&32+1j^Xd9u543TY*32cF9@i34";

	protected RoleManager roleManager;

	protected ArrayList<User> userList = new ArrayList<User>();

	public User getUser(String name) {
		for (int i = 0; i < userList.size(); i++) {
			User m = userList.get(i);
			if (m.name != null && m.name.equals(name)) {
				return m;
			}
		}
		return null;
	}

	public boolean isUserExists(String name) {
		return (getUser(name) != null);
	}

	public User[] getUsers() {
		return userList.toArray(new User[0]);
	}

	public User addUser(String name, String realName, String email) {
		User m = getUser(name);
		if (m != null) {
			if (realName != null && !realName.equals(m.realName)) {
				m.setRealName(realName);
			}
			if (email != null && !email.equals(m.email)) {
				m.setEmail(email);
			}
			return m;
		} else {
			m = new User(name, realName, email);
			m.createDate = new Date();
			m.passwordExpiredTime = new Date();
			m.password = StringUtil.hash("888888" + md5_key);
			userList.add(m);

			logger.warn("[add-user] [" + name + "] [" + realName + "] [" + email + "]");

			return m;
		}
	}

	public void setUserPassword(User u, String password, int expireDays) {
		u.password = StringUtil.hash(password + md5_key);
		u.passwordExpiredTime = new Date(System.currentTimeMillis() + expireDays * 24 * 3600000L);

		logger.warn("[change-password] [" + u.name + "] [" + expireDays + "]");
	}

	public boolean matchUser(String username, String password) {
		User u = getUser(username);
		if (u != null) {
			if (u.password != null && u.password.equals(StringUtil.hash(password + md5_key))) {
				return true;
			}
		}
		return false;
	}

	public boolean matchUser2(String username, String encryptPassword) {
		User u = getUser(username);
		if (u != null) {
			if (u.password != null && u.password.equals(encryptPassword)) {
				return true;
			}
		}
		return false;
	}

	public boolean signedMatchUser(String username, String signedPassword, String ipAddress) {
		User u = getUser(username);
		if (u != null && u.getPassword() != null) {
			String signed = StringUtil.hash(u.getPassword() + ipAddress + SIGN_KEY);
			if (signed.equals(signedPassword)) {
				return true;
			}
		}
		return false;
	}

	public void removeUser(String name) {
		User m = getUser(name);
		if (m != null) {
			userList.remove(m);
			logger.warn("[remove-user] [" + name + "] [" + m.getRealName() + "] [" + m.getEmail() + "]");
		}
	}

	public String saveToElementString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<users>\n");
		User[] us = getUsers();
		for (int i = 0; i < us.length; i++) {
			User u = us[i];
			sb.append("<user name='" + StringUtil.escapeForXML(u.getName()) + "' realname='" + StringUtil.escapeForXML(u.getRealName()) + "' email='"
					+ StringUtil.escapeForXML(u.getEmail()) + "' password='" + StringUtil.escapeForXML(u.getPassword()) + "' ctime='"
					+ DateUtil.formatDate(u.getCreateDate(), "yyyyMMddHHmmss") + "' expire='"
					+ DateUtil.formatDate(u.getPasswordExpiredTime(), "yyyyMMddHHmmss") + "'>\n");
			Role rs[] = u.getRoles();
			for (int j = 0; j < rs.length; j++) {
				sb.append("<role>" + rs[j].getId() + "</role>\n");
			}
			Properties props = u.getProperties();
			String keys[] = props.keySet().toArray(new String[0]);
			for (int j = 0; j < keys.length; j++) {
				sb.append("<property name='" + StringUtil.escapeForXML(keys[j]) + "' value='" + StringUtil.escapeForXML(props.getProperty(keys[j])) + "'/>\n");
			}
			sb.append("</user>\n");
		}
		sb.append("</users>\n");
		return sb.toString();
	}

	public void loadFromElement(Element root) {
		ArrayList<User> users = new ArrayList<User>();
		Element ues[] = XmlUtil.getChildrenByName(root, "user");
		for (int i = 0; i < ues.length; i++) {
			Element e = ues[i];
			String name = XmlUtil.getAttributeAsString(e, "name", null);
			String realname = XmlUtil.getAttributeAsString(e, "realname", "", null);
			String email = XmlUtil.getAttributeAsString(e, "email", "", null);
			String password = XmlUtil.getAttributeAsString(e, "password", null);
			String ctime = XmlUtil.getAttributeAsString(e, "ctime", null);
			String expire = XmlUtil.getAttributeAsString(e, "expire", null);
			if (name != null && password != null) {
				User u = new User(name, realname, email);
				u.password = password;
				if (ctime != null) {
					u.createDate = DateUtil.parseDate(ctime, "yyyyMMddHHmmss");
				}
				if (expire != null) {
					u.passwordExpiredTime = DateUtil.parseDate(expire, "yyyyMMddHHmmss");
				} else {
					u.passwordExpiredTime = new Date();
				}

				Element res[] = XmlUtil.getChildrenByName(e, "role");
				for (int j = 0; j < res.length; j++) {
					String rid = XmlUtil.getValueAsString(res[j], null);
					if (rid != null) {
						Role r = roleManager.getRole(rid);
						if (r != null) {
							u.roleList.add(r);
						}
					}
				}
				res = XmlUtil.getChildrenByName(e, "property");
				for (int j = 0; j < res.length; j++) {
					String key = XmlUtil.getAttributeAsString(res[j], "name", null);
					String value = XmlUtil.getAttributeAsString(res[j], "value", null);
					if (key != null && value != null) {
						u.getProperties().setProperty(key, value);
					}
				}

				users.add(u);
			}
		}
		userList = users;
	}
	
	public static void main(String args[]) {
		//公司
		String signed = StringUtil.hash("734aca3b479c37f68a483e923619746c" + "106.120.222.214" + SIGN_KEY);
		//腾讯映射
//		String signed = StringUtil.hash("734aca3b479c37f68a483e923619746c" + "10.147.21.100" + SIGN_KEY);
		System.out.println("signed:" + signed );
	}
}
