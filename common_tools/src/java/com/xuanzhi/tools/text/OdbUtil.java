package com.xuanzhi.tools.text;

/**
 * 提出所有entity和embeddable的类名
 */
import java.io.File;

/**
 *
 * 
 */
public class OdbUtil {
	
	public static final String tags[] = new String[]{"@Entity", "@Embeddable"};

	public static void getTagedClassNames(String folder) {
		File files[] = new File(folder).listFiles();
		for(File f : files) {
			if(f.isFile()) {
				tagger(f.getPath());
			} else if(f.isDirectory()) {
				getTagedClassNames(f.getPath());
			}
		}
	}
	
	/**
	 * 如果是taged，那么获得他的className
	 * @param file
	 * @return
	 */
	private static void tagger(String file) {
		String content = FileUtils.readFile(file);
		String pkgName = null;
		boolean hasTag = false;
		String className = null;
		String lines[] = content.split("\n");
		for(String line : lines) {
			line = line.trim();
			if(line.startsWith("package")) {
				pkgName = line.substring("package ".length(), line.length()-1);
			} else if(line.startsWith("public class") || line.startsWith("public interface")) { 
				className = line.split(" ")[2];
				break;
			} else {
				for(String tag : tags) {
					if(line.indexOf(tag) != -1) {
						hasTag = true;
						break;
					}
				}
			}
		}
		if(hasTag && pkgName != null && className != null) {
			System.out.println(pkgName + "." + className);
		} else if(hasTag) {
			if(pkgName == null) {
				System.out.println("Taged but pkgName is NULL ["+file+"]");
			} else if(className == null) {
				System.out.println("Taged but className is NULL ["+file+"]");
			}
		}
	}
	
	public static void main(String args[]) {
		//String file = args[0];
//		String file = "d:/Officer.java";
//		addJudge(file);
		String folder = "D:/gamepj/sanguo/webgame_server/src";
		getTagedClassNames(folder);
	}
}
