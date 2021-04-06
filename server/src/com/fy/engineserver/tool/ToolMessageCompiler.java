package com.fy.engineserver.tool;

import com.xuanzhi.tools.transport.MessageComplier;

public class ToolMessageCompiler {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String xml = "D:\\Project\\game_mieshi_server\\src\\com\\mieshi\\engineserver\\tool\\tool_messages.xml";
		String out = "D:\\Project\\game_mieshi_server\\src\\com\\mieshi\\engineserver\\tool\\message";
		MessageComplier.main(new String[] { 
				"-package","com.fy.engineserver.tool.message",
				"-file",xml,
				"-output.dir", out,
				"-imports" , "java.nio.*;com.xuanzhi.tools.transport.*;" +
						"com.fy.engineserver.datasource.article.data.articles.*;" +
						"com.fy.engineserver.datasource.article.data.props.*;" +
						"com.fy.engineserver.datasource.article.data.equipments.*;" +
						"com.fy.engineserver.core.*;" +
						"com.fy.engineserver.core.res.*;" +
						"com.fy.engineserver.sprite.npc.*;" +
						"com.fy.engineserver.datasource.skill.*;" +
						"com.fy.engineserver.sprite.monster.*;" +
						"com.fy.engineserver.datasource.repute.*;" +
						"com.fy.engineserver.task.*",
						
						"-encoding","UTF-8",
						
		});		
	}

}
