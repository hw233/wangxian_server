package com.fy.boss.deploy;

import java.io.File;

import com.fy.boss.game.model.Server;
import com.fy.boss.deploy.ModuleParser;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class ServerPropertiesParser implements ModuleParser {
	
	public void parse(File file, Server server) throws Exception {
		// TODO Auto-generated method stub
		String name = file.getPath().toLowerCase();
		if(!name.endsWith("jar")) {
			String serverContent = FileUtils.readFile(file.getPath());
			serverContent = serverContent.replaceAll("@serverName", StringUtil.native2ascii(server.getName()));
			serverContent = serverContent.replaceAll("@serverPort", String.valueOf(server.getGameport()));
			serverContent = serverContent.replaceAll("@resinHome", server.getResinhome());
			FileUtils.writeFile(serverContent, file.getPath());
		}
	}

}
