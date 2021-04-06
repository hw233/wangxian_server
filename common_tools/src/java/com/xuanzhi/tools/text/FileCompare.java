package com.xuanzhi.tools.text;

import java.io.File;

/**
 * 具有完全一样的文件目录结构的文件，进行比较，列出文件差异
 * 
 */
public class FileCompare {
	
	//源目录，通常是最新的文件
	private String sourceFolder;
	
	//目标目录，通常是较老的那份
	private String targetFolder;
	
	private FileCompare(String sourceFolder, String targetFolder) {
		this.sourceFolder = sourceFolder;
		this.targetFolder = targetFolder;
	}
	
	public void startCompare() {
		compare(sourceFolder, targetFolder);
	}
	
	public void compare(String f1, String f2) {
		File ff1[] = new File(f1).listFiles();
		for(File f : ff1) {
			if(f.isFile()) {
				String path = f.getPath().substring(f1.length());
				String t = f2  + path;
				File tf = new File(t);
				if(!tf.exists()) {
					System.out.println("文件不存在:" + f.getPath().substring(sourceFolder.length()));
				} else if(tf.isFile()) {
					String c1 = FileUtils.readFile(f.getPath());
					String c2 = FileUtils.readFile(t);
					if(!c1.equals(c2)) {
						System.out.println("文件有改动:" + f.getPath().substring(sourceFolder.length()));
					} 
				}
			} else {
				String path = f.getPath().substring(f1.length());
				String t = f2  + path;
				compare(f.getPath(), t);
			}
		}
	}
	
	public static void main(String args[]) {
		String source = "D:/gamepj/fm_engine/engine_0114";
		String target = "D:/gamepj/fm_engine/engine_0113";
		FileCompare fc = new FileCompare(source, target);
		fc.startCompare();
	}
}
