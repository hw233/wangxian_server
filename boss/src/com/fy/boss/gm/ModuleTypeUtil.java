package com.fy.boss.gm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import com.fy.boss.gm.ModuleType;
import com.fy.boss.gm.XmlModule;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class ModuleTypeUtil {
	public static List<ModuleType> loadPage(String xmlname) {
		List<ModuleType> types = new ArrayList<ModuleType>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder()
					.buildFromFile(xmlname);
			Configuration typesConf[] = rootConf.getChildren();
			for (int i = 0; i < typesConf.length; i++) {
				ModuleType type = new ModuleType();
				String tid = typesConf[i].getAttribute("id","");
				String name = typesConf[i].getAttribute("name", "");
				String memo = typesConf[i].getAttribute("memo", "");
				type.setName(name);
				type.setMemo(memo);
				type.setId(Integer.parseInt(tid));
				Configuration modulesConf[] = typesConf[i].getChildren();
				for (int j = 0; j < modulesConf.length; j++) {
					XmlModule module = new XmlModule();
					String id = modulesConf[j].getAttribute("id", "");
					String description = modulesConf[j].getAttribute(
							"description", "");
					String isShow = modulesConf[j].getAttribute("isshow","");
					String base = modulesConf[j].getAttribute("base", "false");
					String jspname = modulesConf[j].getAttribute("jspname", "");
					module.setId(id);
					module.setJspName(jspname);
					module.setBase(base);
					module.setIsShow(isShow);
					module.setDescription(description);
					type.getModules().add(module);
				}
				types.add(type);
			}
			return types;
		} catch (Exception e) {
			return new ArrayList<ModuleType>();
		}
	}

	public static void savePage(List<ModuleType> types, String saveFile)
			throws IllegalArgumentException, IllegalAccessException,
			SAXException, IOException, ConfigurationException,
			InvocationTargetException {
		Configuration rootConf = new DefaultConfiguration("modules", "-");
		for (ModuleType type : types) {
			Configuration typeConf = new DefaultConfiguration("type", "-");
			typeConf.setAttribute("name", type.getName());
			typeConf.setAttribute("memo", type.getMemo());
			typeConf.setAttribute("id", type.getId()+"");
			rootConf.addChild(typeConf);
			for (XmlModule m : type.getModules()) {
				Configuration moduleConf = new DefaultConfiguration("module",
						"-");
				moduleConf.setAttribute("id", m.getId());
				moduleConf.setAttribute("description", m.getDescription());
				moduleConf.setAttribute("base", m.getBase());
				moduleConf.setAttribute("isshow", m.getIsShow());
				moduleConf.setAttribute("jspname", m.getJspName());
				typeConf.addChild(moduleConf);
			}
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(
				new File(saveFile), rootConf);
	}

}
