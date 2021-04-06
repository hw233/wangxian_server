package com.fy.boss.deploy;

import java.io.File;

import com.fy.boss.game.model.Server;
import com.fy.boss.deploy.ModuleParser;
import com.xuanzhi.tools.text.FileUtils;

public class GeneralParser implements ModuleParser {
	
	public void parse(File file, Server server) throws Exception {
		// TODO Auto-generated method stub
		replace(file, server);
	}
	
	private void replace(File file, Server server) {
		if(file.isDirectory()) {
			File files[] = file.listFiles();
			for(File f : files) {
				replace(f, server);
			}
		} else {
			String name = file.getPath().toLowerCase();
			if(name.endsWith("xml") || name.endsWith("conf") || name.endsWith("properties") || name.endsWith("txt") || name.endsWith("pl")) {
				String content = FileUtils.readFile(file.getPath());
				content = content.replaceAll("@resinHome", server.getResinhome());
				FileUtils.writeFile(content, file.getPath());
			}
		}
	}

}
