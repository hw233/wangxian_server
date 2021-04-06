package com.fy.boss.gm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import com.fy.boss.gm.Module;
import com.fy.boss.gm.XmlRole;
import com.fy.boss.gm.XmlRoleUtil;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class XmlRoleUtil {
	public static List<Module> moduless = new ArrayList<Module>();
	public static List<Module> getModuless() {
		return moduless;
	}

	public static void setModuless(List<Module> moduless) {
		XmlRoleUtil.moduless = moduless;
	}

	public static List<XmlRole> loadPage(String xmlname){
		List<XmlRole> roles = new ArrayList<XmlRole>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder()
					.buildFromFile(xmlname);
			Configuration rolesConf[] = rootConf.getChildren();
			for (int i = 0; i < rolesConf.length; i++) {
				XmlRole role = new XmlRole();
				role.setName(rolesConf[i].getAttribute("name"));
				role.setId(rolesConf[i].getAttribute("id"));
				Configuration serverConf = rolesConf[i].getChild("servers");
				Configuration serversConf[] = serverConf.getChildren();
				List<String> servers = new ArrayList<String>();
				for (int j = 0; j < serversConf.length; j++) {
					servers.add(serversConf[j].getAttribute("id"));
				}
				role.setServers(servers);
				Configuration modeleConf = rolesConf[i].getChild("modules");
				Configuration modelesConf[] = modeleConf.getChildren();
				List<Module> modules = new ArrayList<Module>();
				for (int k = 0; k < modelesConf.length; k++) {
					Module module = new Module();
					module.setId(modelesConf[k].getAttribute("id"));
					module.setPermission(modelesConf[k]
							.getAttribute("permission"));
					modules.add(module);
					if (role.getName().equals("admin")) {
						moduless.add(module);
					}
				}
				role.setModules(modules);
				
				roles.add(role);
			}
			return roles;
		} catch (Exception e) {
			return new ArrayList<XmlRole>();
		}
	}

	public static void savePage(List<XmlRole> roles, String saveFile)
			throws IllegalArgumentException, IllegalAccessException,
			SAXException, IOException, ConfigurationException,
			InvocationTargetException {
		Configuration rootConf = new DefaultConfiguration("roles", "-");
		for (XmlRole role : roles) {
			Configuration roleConf = new DefaultConfiguration("role", "-");
			rootConf.addChild(roleConf);
			roleConf.setAttribute("id", role.getId());
			roleConf.setAttribute("name", role.getName());
			Configuration serversConf = new DefaultConfiguration("servers", "-");
			roleConf.addChild(serversConf);
			List<String> servers = role.getServers();
			for (int i = 0; i < servers.size(); i++) {
				Configuration serverConf = new DefaultConfiguration("server",
						"-");
				serverConf.setAttribute("id", servers.get(i));
				serversConf.addChild(serverConf);
			}
			Configuration modulesConf = new DefaultConfiguration("modules", "-");
			roleConf.addChild(modulesConf);
			List<Module> modules = role.getModules();
			for (int j = 0; j < modules.size(); j++) {
				Configuration moduleConf = new DefaultConfiguration("module",
						"-");
				moduleConf.setAttribute("id", modules.get(j).getId());
				moduleConf.setAttribute("permission", modules.get(j)
						.getPermission());
				modulesConf.addChild(moduleConf);
			}
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(
				new File(saveFile), rootConf);
	}
}
