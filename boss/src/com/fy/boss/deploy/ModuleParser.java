package com.fy.boss.deploy;

import java.io.File;

import com.fy.boss.game.model.Server;

public interface ModuleParser {
	
	public void parse(File file, Server server) throws Exception;
	
}
