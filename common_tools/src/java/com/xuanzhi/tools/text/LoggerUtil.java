package com.xuanzhi.tools.text;

/**
 * 在所有debug,info,warn级别的日之前签上...enabled判断！
 */
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class LoggerUtil {
	
	public static final String logMethodTags[] = new String[]{".debug(", ".info(", ".warn("};
	public static final String judgeTags[] = new String[]{"isDebugEnabled","isInfoEnabled","isWarnEnabled"};
	
	public static boolean addJudge(String filepath) {
		List<String> lineList  = new ArrayList<String>();
		String content = FileUtils.readFile(filepath);
		String lines[] = content.split("\n");
		int placeNum = 0;
		boolean replaced = false;
		for(String line : lines) {
			boolean loggerLine = false;
			int tagIndex = 0;
			for(int i=0; i<logMethodTags.length; i++) {
				if(!line.trim().startsWith("//") && line.indexOf(logMethodTags[i]) != -1) {
					loggerLine = true;
					tagIndex = i;
					break;
				}
			}
			if(loggerLine) {
				//检查前一行，是否包含判断
				boolean judged = false;
				if(lineList.size() > 0) {
					String lastLine = lineList.get(lineList.size()-1);
					if(lastLine.indexOf("Enabled()") != -1) {
						judged = true;
					}
				}
				if(!judged) {
					//开始拼装
					String logger = getLogger(line, logMethodTags[tagIndex]);
					String judgeLine = wrapperJudge(line, logger, judgeTags[tagIndex]);
					String newline = refactLoggerLine(line);
					lineList.add(judgeLine+"\r");
					lineList.add(newline);
					placeNum++;
					replaced = true;
				} else {
					lineList.add(line);
				}
			} else {
				lineList.add(line);
			}
		}
		StringBuffer sb = new StringBuffer();
		for(String line : lineList) {
			sb.append(line+"\n");
		}
		FileUtils.writeFile(sb.toString(), filepath);
		System.out.println("[finish replace "+placeNum+"] ["+filepath+"]");
		return replaced;
	}
	
	/**
	 * 得到logger
	 * @param line
	 * @param method
	 * @return
	 */
	public static String getLogger(String line, String method) {
		String s = line.substring(0, line.indexOf(method));
		return s.trim();
	}
	
	/**
	 * 包装判断那一行
	 * @param line
	 * @param logger
	 * @return
	 */
	public static String wrapperJudge(String line, String logger, String judge) {
		String prefixChar = line.substring(0, line.indexOf(logger));
		return prefixChar + "if(" + logger+ "."+judge + "())";
	}
	
	/**
	 * 重新生成当前打日志这行，前面加一个制表符
	 * @param line
	 * @return
	 */
	public static String refactLoggerLine(String line) {
		return "\t" + line;
	}
	
	public static void refractFolderFiles(String path) {
		long start = System.currentTimeMillis();
		List<String> files = FileUtils.listAllFiles(path);
		int num = 0;
		for(String f : files) {
			boolean replaced = addJudge(f);
			if(replaced) {
				num++;
			}
		}
		System.out.println("["+files.size()+" total] ["+num+" replaced] ["+(System.currentTimeMillis()-start)+"ms]");
	}
	
	public static void main(String args[]) {
		//String file = args[0];
//		String file = "d:/Officer.java";
//		addJudge(file);
		String folder = "D:/gamepj/sanguo/webgame_server/src";
		refractFolderFiles(folder);
	}
}
