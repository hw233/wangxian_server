package com.fy.engineserver.datasource.language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xuanzhi.tools.text.FileUtils;

/**
 *
 * 
 * @version 创建时间：Jan 23, 2013 6:18:55 PM
 * 
 */
public class ChineseCollector {
		
	public String srcFolder = "D:/gamepj/mieshi_server/game_mieshi_server/src";
	
	public String outfile = "D:/chinese.txt";
	
	public void collect() throws Exception {
		long start = System.currentTimeMillis();
		//进行代码搜索
		List<String> files = FileUtils.listAllFiles(srcFolder);
		List<String> chineseList = new ArrayList<String>();
		int num = 0;
		for(String f : files) {
			File file = new File(f);
			if(file.isFile() && !file.getName().equals("Translate.java") && !file.getName().equals("CopyOfTranslate.java")) {
				if(++num % 100 == 0) {
					System.out.println("*");
				} else {
					System.out.print("*");
				}
				String content = FileUtils.readFile(f);
				String str[] = content.split("\n");
				for(String s : str) {
					if(s.indexOf("debug(") != -1 || s.indexOf("info(") != -1 || s.indexOf("warn(") != -1 || s.indexOf("error(") != -1
							|| s.indexOf("//") != -1 || s.indexOf("/*") != -1 || s.indexOf("Exception") != -1 || s.indexOf("//") != -1) {
						continue;
					}
					String quotContent[] = getQuotContents(s);
					boolean added = false;
					for(String c : quotContent) {
						if(c.getBytes().length != c.length()) {
							System.out.println(c);
							chineseList.add(file.getPath());
							added = true;
							break;
						}
					}
					if(added) {
						break;
					}
				}
			}
		}
		
		//重新组装Translate.java
		StringBuffer sb = new StringBuffer();
		for(String s : chineseList) {
			sb.append(s + "\n");
		}
		
		FileUtils.writeFile(sb.toString(), outfile);
		System.out.println("========================================================================");
		System.out.println(sb.toString());
		System.out.println("========================================================================");
		System.out.println("含有中文的类总数:"+chineseList.size()+", 消耗时常: " + (System.currentTimeMillis()-start) + "ms");
	}
	
	public String[] getQuotContents(String content) {
		String regex = "\"(.*?)\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		List<String> alist = new ArrayList<String>();
		while(m.find()) {
			alist.add(m.group(1));
		}
		return alist.toArray(new String[0]);
	}
	
	public static void main(String args[]) {
		ChineseCollector tool = new ChineseCollector();
		try {
			tool.collect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
