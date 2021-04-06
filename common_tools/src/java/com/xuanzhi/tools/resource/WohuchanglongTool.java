package com.xuanzhi.tools.resource;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class WohuchanglongTool {
	/**
	 * java -cp . xxxxxxx -f res.conf -g -a -f -r masterversion slaveversion
	 *   
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{

		String conf = "res.conf";
		
		String xmlFile = "res.xml";
		boolean fullPackage = false;
		int masterVersion = 1;
		int slaveVersion = 0;
		boolean debug = true;
		
		for(int i = 0 ; i < args.length ; i++){
			String s = args[i];
			if(s.equals("-f")){
				conf = args[i+1];
				i++;
			}else if(s.equals("-x")){
				xmlFile = args[i+1];
				i++;
			}else if(s.equals("-debug")){
				debug = true;
			}
			else if(s.equals("-h")){
				printHelp();
				System.exit(0);
			}
		}

		
		File confFile = new File(conf);
		if(confFile.exists() == false){
			confFile = new File(System.getProperty("user.dir"),conf);
			if(confFile.exists() == false){
				System.out.println("找不到指定的配置文件["+conf+"]，请检查！");
				printHelp();
				System.exit(0);
			}
		}
		
		Properties props = new Properties();
		props.load(new FileInputStream(confFile));
		
		final ResourcePackageTools drm = new ResourcePackageTools();
		
		drm.packageFileSuffix = props.getProperty("packageFileSuffix",".vfs");
		drm.packageFilePrefix = props.getProperty("packageFilePrefix","3dsword");
		drm.resourceRootDir = props.getProperty("resourceRootDir");
		drm.packageRootDir = props.getProperty("packageRootDir");
		drm.aPackageListFile = props.getProperty("aPackageListFile");
		drm.httpDownloadRoot = props.getProperty("httpDownloadRoot");
		drm.packageFileZipSuffix = props.getProperty("packageFileZipSuffix");
		drm.excludesFolders=  props.getProperty("excludesFolders");
		drm.excludesFileSuffix = props.getProperty("excludesFileSuffix");
		drm.ignorePath = props.getProperty("ignorePath");
		drm.debug = debug;
		
		drm.init();
		
		if(drm.arpList.size() == 0){
			masterVersion = 1;
			slaveVersion = 0;
			fullPackage = true;
		}else{
			masterVersion = drm.arpList.get(drm.arpList.size() -1).masterVersion;
			slaveVersion = drm.arpList.get(drm.arpList.size() -1).slaveVersion + 1;
			if(slaveVersion >= 10){
				masterVersion = masterVersion + 1;
				slaveVersion = 0;
			}
			fullPackage = false;
		}
		System.out.println("正在生成A资源包["+masterVersion+"."+slaveVersion+"]......");
		drm.generatePackage(masterVersion, slaveVersion, fullPackage, true);
		
		if(drm.brpList.size() == 0){
			masterVersion = 1;
			slaveVersion = 0;
			fullPackage = true;
		}else{
			masterVersion = drm.brpList.get(drm.brpList.size() -1).masterVersion;
			slaveVersion = drm.brpList.get(drm.brpList.size() -1).slaveVersion + 1;
			if(slaveVersion >= 10){
				masterVersion = masterVersion + 1;
				slaveVersion = 0;
			}
			fullPackage = false;
		}
		System.out.println("正在生成B资源包["+masterVersion+"."+slaveVersion+"]......");
		drm.generatePackage(masterVersion, slaveVersion, fullPackage, false);
		
		File dir = new File(drm.packageRootDir);
		File vfsFiles[] = dir.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(drm.packageFileSuffix);
			}
		});
		for(int i = 0 ; i < vfsFiles.length ; i++){
			File f = vfsFiles[i];
			String s = f.getPath().substring(0, f.getPath().length() - drm.packageFileSuffix.length()) + drm.packageFileZipSuffix;
			File ff = new File(s);
			if(ff.exists() == false){
				//zip 不存在
				System.out.println("压缩资源包["+f.getName()+"]-->["+ff.getName()+"]......");
				Process process = Runtime.getRuntime().exec("lzma.exe e "+f.getPath()+" "+s+" -d25");
				InputStream input = process.getErrorStream();
				byte bytes[] = new byte[1024];
				int n = 0;
				while( (n = input.read(bytes)) > 0){
					System.out.println(new String(bytes,0,n));
				}
			}
		}
		
		String ss = drm.generateXmlConfig();
		System.out.println(ss);
		File xmlf = new File(System.getProperty("user.dir"),xmlFile);
		FileOutputStream output = new FileOutputStream(xmlf);
		output.write(ss.getBytes());
		output.close();
		
		System.out.println("xml配置文件生成完毕，别忘了将此文件传输到网关上，刷新！");
		
		
	}
	
	public static void printHelp(){
		System.out.println("java -cp ./xuanzhi-tools-1.1.jar com.xuanzhi.tools.resource.WohuchanglongTool -f conf");
		System.out.println("Options:");
		System.out.println("        -f conf 配置文件，配置资源文件所在路径，资源包存储路径，A包文件列表所在路径");
	}
}
