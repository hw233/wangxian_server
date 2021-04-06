package com.fy.boss.gm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import com.fy.boss.gm.XmlServer;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class XmlServerUtil {
	public static List<XmlServer> loadPage(String xmlname) {
		List<XmlServer> servers = new ArrayList<XmlServer>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder()
					.buildFromFile(xmlname);
			Configuration serversConf[] = rootConf.getChildren();
			for (int i = 0; i < serversConf.length; i++) {
				XmlServer server = new XmlServer();
				String id = serversConf[i].getAttribute("id", "");
				String description = serversConf[i].getAttribute("description",
						"");
				String uri = serversConf[i].getAttribute("uri", "");
				server.setId(id);
				server.setDescription(description);
				server.setUri(uri);
				servers.add(server);

			}
			return servers;
		} catch (Exception e) {
			return new ArrayList<XmlServer>();
		}
	}

	public static void savePage(List<XmlServer> servers, String saveFile)
			throws IllegalArgumentException, IllegalAccessException,
			SAXException, IOException, ConfigurationException,
			InvocationTargetException {
		Configuration rootConf = new DefaultConfiguration("servers", "-");
		for (XmlServer server : servers) {
			Configuration serverConf = new DefaultConfiguration("server", "-");
			rootConf.addChild(serverConf);
			serverConf.setAttribute("id", server.getId());
			serverConf.setAttribute("description", server.getDescription());
			serverConf.setAttribute("uri", server.getUri());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(
				new File(saveFile), rootConf);
	}
}
