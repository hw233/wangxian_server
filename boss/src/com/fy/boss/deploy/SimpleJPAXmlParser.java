package com.fy.boss.deploy;

import java.io.File;

import com.fy.boss.game.model.Server;
import com.fy.boss.deploy.ModuleParser;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class SimpleJPAXmlParser implements ModuleParser {
	
	public void parse(File file, Server server) throws Exception {
		// TODO Auto-generated method stub
		String name = file.getPath().toLowerCase();
		if(!name.endsWith("jar")) {
			String serverContent = FileUtils.readFile(file.getPath());
			serverContent = serverContent.replaceAll("@serverId", server.getServerid());
			serverContent = serverContent.replaceAll("@serverName", server.getName());
			serverContent = serverContent.replaceAll("@dbUrl", server.getDburi());
			serverContent = serverContent.replaceAll("@dbUserName", server.getDbusername());
			serverContent = serverContent.replaceAll("@dbPassword", server.getDbpassword());
			FileUtils.writeFile(serverContent, file.getPath());
		}
	}

}
