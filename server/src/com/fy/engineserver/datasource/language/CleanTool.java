package com.fy.engineserver.datasource.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xuanzhi.tools.text.FileUtils;

/**
 *
 * 
 * @version 创建时间：Jan 23, 2013 6:18:55 PM
 * 
 */
public class CleanTool {
	
	public String objFile = "D:/gamepj/mieshi_server/game_mieshi_server/src/com/mieshi/engineserver/datasource/language/Translate.java";
	
	public String srcFolder = "D:/gamepj/mieshi_server/game_mieshi_server/src";
	
	public String outfile = "D:/Trans.java";
	
	public void clean() throws Exception {
		long start = System.currentTimeMillis();
		//搜集变量
		List<String> set = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(objFile)));
		String line = null;
		while((line = br.readLine()) != null) {
			if(line.indexOf("public static String") != -1) {
				String str = line.split("=")[0].trim();
				String param = str.split(" ")[str.split(" ").length-1];
				set.add(param);
			}
		}
		
		//进行代码搜索
		List<String> files = FileUtils.listAllFiles(srcFolder);
		HashMap<String,Boolean> existsMap = new HashMap<String,Boolean>();
		int num = 0;
		for(String f : files) {
			File file = new File(f);
			if(file.isFile() && !file.getName().equals("Translate.java")) {
				if(++num % 100 == 0) {
					System.out.println("*");
				} else {
					System.out.print("*");
				}
				String content = FileUtils.readFile(f);
				for(String param : set) {
					if(existsMap.get(param) != null) {
						continue;
					}
					if(content.indexOf(param) != -1) {
						existsMap.put(param, Boolean.TRUE);
					}
				}
			}
		}
		
		//重新组装Translate.java
		StringBuffer sb = new StringBuffer();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(objFile)));
		while((line = br.readLine()) != null) {
			if(line.indexOf("public static String") != -1 && line.indexOf("=") != -1) {
				String str = line.split("=")[0].trim();
				String param = str.split(" ")[str.split(" ").length-1];
				if(existsMap.get(param) != null) {
					sb.append(line + "\n");
				}
			} else {
				sb.append(line + "\n");
			}
		}
		FileUtils.writeFile(sb.toString(), outfile);
		System.out.println("========================================================================");
		System.out.println(sb.toString());
		System.out.println("========================================================================");
		System.out.println("变量总数:"+set.size()+", 清理后数量："+existsMap.size()+", 消耗时常: " + (System.currentTimeMillis()-start) + "ms");
	}
	
	public static void main(String args[]) {
		CleanTool tool = new CleanTool();
		try {
			tool.clean();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
