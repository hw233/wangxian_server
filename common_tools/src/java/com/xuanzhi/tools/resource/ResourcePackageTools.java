package com.xuanzhi.tools.resource;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Properties;

import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 
 * 资源包管理器
 * 
 * 用于产生各个资源包，所有的资源文件需要存储在一个目录中，
 * 资源文件的key，就是从这个根目录开始的其他目录及文件名组成的字符串，路径分隔符采用"/"
 * 
 * 运行工具，即可以产生对应版本的补丁包或者全资源包。
 * 
 *
 */

public class ResourcePackageTools {

	String packageFileSuffix = ".vfs";
	
	String packageFileZipSuffix = ".7z";
	
	String httpDownloadRoot = "";
	
	String excludesFolders = "";
	
	String excludesFileSuffix = "";
	
	String ignorePath = "false";
	
	String packageFilePrefix = "3dsword";
	//所有的资源文件所在的目录
	String resourceRootDir;
	
	//资源包文件所在的目录
	String packageRootDir;
	
	//a包对应的文件列表，存储在哪个文件中
	//格式为一行一个文件。#开头的为注释。 "/"开头的都会过滤掉"/"
	//如果指定的文件不存在，或者文件内容为空. 说明不存在B包。
	String aPackageListFile;
	
	boolean debug = false;
	
	//=========================================================================
	
	//排好序的a包
	ArrayList<ResourceFile> arpList = new ArrayList<ResourceFile>();
	
	//排好序的b包
	ArrayList<ResourceFile> brpList = new ArrayList<ResourceFile>();
	
	//a包的文件列表，为了区分哪些文件是a包的，哪些是b包的
	HashSet<String> aPackageList = new HashSet<String>();
	
	//所有的资源文件类别
	ArrayList<File> resourceFileList = new ArrayList<File>();
	
	public void init() throws Exception{
		long size = 0;
		getAllFile(new File(resourceRootDir),resourceFileList);
		
		
		if(excludesFolders != null && excludesFolders.length() > 0){
			
			ArrayList<File> al = new ArrayList<File>();
			
			String excludes[] = excludesFolders.split(";");
			String resourceRootDirPath = new File(resourceRootDir).getPath();
			for(int i = 0 ; i <resourceFileList.size() ; i++ ){
				File f = resourceFileList.get(i);
				
				String path = f.getPath();
				path = path.substring(resourceRootDirPath.length());
				path = path.replaceAll("\\\\", "/");
				if(path.charAt(0) != '/'){
					path = "/" + path;
				}
				boolean ex = false;
				for(int j = 0 ; j < excludes.length ; j++){
					if(excludes[j].length() > 0 ){
						if(path.matches(excludes[j])){
							ex = true;
							break;
						}
					}
				}
				if(ex == false){
					al.add(f);
				}
			}
			resourceFileList = al;
			
			
			al = new ArrayList<File>();
			
			excludes = this.excludesFileSuffix.split(";");
			for(int i = 0 ; i <resourceFileList.size() ; i++ ){
				File f = resourceFileList.get(i);
				String name = f.getName();
				boolean ex = false;
				for(int j = 0 ; j < excludes.length ; j++){
					if(excludes[j].length() > 0 ){
						if(name.endsWith(excludes[j])){
							ex = true;
							break;
						}
					}
				}
				if(ex == false){
					al.add(f);
				}
			}
			
			resourceFileList = al;
		}

		for(int i = 0 ; i <resourceFileList.size() ; i++ ){
			File f = resourceFileList.get(i);
			size += f.length();
		}
		System.out.println("资源的根目录为："+resourceRootDir+",未压缩大小为:"+StringUtil.addcommas(size));
		
		readAPackageList();
		
		ArrayList<File> al = new ArrayList<File>();
		System.out.println("资源包所在路径为： " + packageRootDir);
		getAllFile(new File(packageRootDir),al);
		
		for(int i = 0; i < al.size() ; i ++){
			File f = al.get(i);
			if(f.getName().toLowerCase().endsWith(packageFileSuffix)){
				ResourceFile rp = new ResourceFile(f);
				if(rp.init()){
					if(rp.aPackageFlag){
						arpList.add(rp);
					}else{
						brpList.add(rp);
					}
				}
			}
		}
		
		Comparator<ResourceFile> c = new Comparator<ResourceFile>(){
			public int compare(ResourceFile o1, ResourceFile o2) {
				if(o1.masterVersion < o2.masterVersion) return -1;
				if(o1.masterVersion > o2.masterVersion) return 1;
				if(o1.slaveVersion < o2.slaveVersion) return -1;
				if(o1.slaveVersion > o2.slaveVersion) return 1;
				if(o1.fullPackage == false && o2.fullPackage == true) return -1;
				if(o1.fullPackage == true && o2.fullPackage == false) return 1;
				return 0;
			}};
		
		Collections.sort(arpList, c);
		Collections.sort(brpList, c);

		System.out.println("资源包的信息如下： ");
		System.out.println(String.format("%1$10s %2$5s %3$5s %4$10s %5$21s %6$5s %7$5s %8$10s", "a版本","a全部","a文件","a大小","b版本","b全部","b文件","b大小"));
		for(int i = 0 ; (i < arpList.size() || i < brpList.size()) ; i++){
			String aVersion = "";
			String aFull="";
			String aNum = "";
			String aSize = "";
			
			String bVersion = "";
			String bFull="";
			String bNum = "";
			String bSize = "";
			
			if(i < arpList.size()){
				ResourceFile p = arpList.get(i);
				aVersion = p.masterVersion+"."+p.slaveVersion;
				if(p.fullPackage){
					aFull = "F";
				}else{
					aFull = "H";
				}
				aNum = "" + p.vfs.getBlockNum();
				aSize = "" + p.vfs.getCurrentDiskSize();
			}
			
			if(i < brpList.size()){
				ResourceFile p = brpList.get(i);
				bVersion = p.masterVersion+"."+p.slaveVersion;
				if(p.fullPackage){
					bFull = "F";
				}else{
					bFull = "H";
				}
				bNum = "" + p.vfs.getBlockNum();
				bSize = "" + p.vfs.getCurrentDiskSize();
			}
			
			System.out.println(String.format("%1$10s %2$8s %3$8s %4$15s %5$20s %6$5s %7$10s %8$13s", aVersion,aFull,aNum,aSize,bVersion,bFull,bNum,bSize));
		}
		
		
	}
	
	public void readAPackageList() throws Exception{
		//System.out.println("A包的文件列表文件为： " + aPackageListFile);
		if(aPackageListFile != null && aPackageListFile.trim().length() > 0){
			
			File f = new File(aPackageListFile);
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String s = null;
			while( (s = reader.readLine()) != null){
				s = s.trim();
				if(s.startsWith("#") || s.length() == 0){
					continue;
				}
				s = s.replaceAll("\\\\", "/");
				if(s.charAt(0) == '/'){
					s = s.substring(1);
				}
				aPackageList.add(s);
			}
			System.out.println("A包的文件列表文件为： " + aPackageListFile+",一共有"+aPackageList.size()+"个文件");
		}else{
			System.out.println("不存在A包的文件列表文件，默认所有的文件都在A包中！ ");
		}
	}
	
	/**
	 * <?xml version="1.0" encoding="UTF-8" ?>
	 * <resoucepackages create-time="2014-05-16 13:35:33">
	 * 
	 * 		<resoucepackage version="1.0" full-type="full|half" package-type="a|b" file-num="6399" file-size="755666777">
	 * 			<url>http://wh1.sqage.com/download/res/3dsword20140516181818_AFR_1.0.7z</url>
	 * 			<url>http://wh2.sqage.com/download/res/3dsword20140516181818_AFR_1.0.7z</url>
	 * 		</resoucepackage>
	 * 
	 * </resoucepackages>
	 * @throws Exception
	 */
	public String generateXmlConfig() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		sb.append("<resoucepackages create-time=\""+DateUtil.formatDate(new java.util.Date(), "yyyy-MM-dd HH:mm:ss")+"\">\n");
		
		String urlRoot[] = this.httpDownloadRoot.split(";");
		
		for(int i = 0 ; i < arpList.size() ; i++){
			ResourceFile rp = arpList.get(i);
			File zipFile = new File(rp.dataFile.getParentFile(),rp.dataFile.getName().replace(packageFileSuffix, packageFileZipSuffix));
			if(zipFile.exists() == false){
				System.out.println("错误：资源文件["+rp.dataFile+"]对应的压缩zip文件不存在，无法获取zip文件的大小！请先产生zip文件");
				System.exit(1);
			}
			sb.append("    <resoucepackage version=\""+rp.masterVersion+"."+rp.slaveVersion+"\" full-type=\""+(rp.fullPackage?"full":"half")+"\" package-type=\""+(rp.aPackageFlag?"a":"b")+"\" file-num=\""+rp.vfs.getBlockNum()+"\" file-size=\""+zipFile.length()+"\">\n");
			
			for(int j = 0 ; j < urlRoot.length ; j++){
				String ss = urlRoot[j].trim();
				if(ss.toLowerCase().startsWith("http://")){
					if(ss.endsWith("/") == false){
						ss = ss + "/";
					}
					String fName = rp.dataFile.getName();
					fName = fName.replace(this.packageFileSuffix, this.packageFileZipSuffix);
					sb.append("        <url>"+ss+fName+"</url>\n");
				}
			}
			sb.append("    </resoucepackage>\n");
		}
		
		for(int i = 0 ; i < brpList.size() ; i++){
			ResourceFile rp = brpList.get(i);
			File zipFile = new File(rp.dataFile.getParentFile(),rp.dataFile.getName().replace(packageFileSuffix, packageFileZipSuffix));
			if(zipFile.exists() == false){
				System.out.println("错误：资源文件["+rp.dataFile+"]对应的压缩zip文件不存在，无法获取zip文件的大小！请先产生zip文件");
				System.exit(1);
			}
			
			sb.append("    <resoucepackage version=\""+rp.masterVersion+"."+rp.slaveVersion+"\" full-type=\""+(rp.fullPackage?"full":"half")+"\" package-type=\""+(rp.aPackageFlag?"a":"b")+"\" file-num=\""+rp.vfs.getBlockNum()+"\" file-size=\""+zipFile.length()+"\">\n");
			
			for(int j = 0 ; j < urlRoot.length ; j++){
				String ss = urlRoot[j].trim();
				if(ss.toLowerCase().startsWith("http://")){
					if(ss.endsWith("/") == false){
						ss = ss + "/";
					}
					String fName = rp.dataFile.getName();
					fName = fName.replace(this.packageFileSuffix, this.packageFileZipSuffix);
					sb.append("        <url>"+ss+fName+"</url>\n");
				}
			}
			sb.append("    </resoucepackage>\n");
		}
		
		sb.append("</resoucepackages>");
		
		return sb.toString();
	}
	/**
	 * 产生一个新的资源包
	 * @param masterVersion
	 * @param slaveVersion
	 * @param fullPackage
	 */
	public void generatePackage(int masterVersion,int slaveVersion,boolean fullPackage,boolean aPackageFlag) throws Exception{
		long startTime = System.currentTimeMillis();
		ArrayList<ResourceFile> al = brpList;
		if(aPackageFlag){
			al = arpList;
		}
		//
		if(al.size() > 0){
			ResourceFile rp = al.get(al.size()-1);
			if(rp.masterVersion > masterVersion || (rp.masterVersion == masterVersion && rp.slaveVersion>slaveVersion)){
				throw new RuntimeException("输入的版本号比最后一次生成的版本还要小，请检查输入的参数！");
			}
			
			if(rp.masterVersion == masterVersion && rp.slaveVersion == slaveVersion && rp.fullPackage == fullPackage){
				if(rp.releaseFlag == true){
					throw new RuntimeException("输入的版本号与最后一次发布的版本一致，发布版本不能被覆盖，请检查输入的参数！");
				}else{
					//删除最后一次版本
					al.remove(rp);
					
					rp.deletePackage();
				}
			}
		}
		String resourcePackageDataFileName = getResourcePackageDataFileName(masterVersion,slaveVersion,fullPackage,aPackageFlag);
		ResourceFile rp = new ResourceFile(new File(this.packageRootDir,resourcePackageDataFileName));
		rp.masterVersion = masterVersion;
		rp.slaveVersion = slaveVersion;
		rp.fullPackage = fullPackage;
		rp.releaseFlag = false;
		rp.aPackageFlag = aPackageFlag;
		rp.init();
		

		String resourceRootDirPath = new File(resourceRootDir).getPath();
		for(int i = 0 ; i < this.resourceFileList.size() ; i++){
			File f = resourceFileList.get(i);
			
			String path = f.getPath();
			path = path.substring(resourceRootDirPath.length());
			path = path.replaceAll("\\\\", "/");
			if(path.charAt(0) == '/'){
				path = path.substring(1);
			}
			
			if(this.ignorePath.equals("true")){
				path = f.getName();
			}
			
			if(aPackageFlag && (this.aPackageList.contains(path) == false && aPackageList.size() > 0)){
				continue;
			}
			
			if(aPackageFlag == false && (this.aPackageList.contains(path) || aPackageList.size() == 0)){
				continue;
			}
				
			byte data[] = getFileData(f);
			
			//此文件对应的最新的版本和数据
		
			int a_version = -1;
			byte a_data2[] = null;
			
			for(int j = arpList.size() -1 ; j >= 0 ; j--){
				ResourceFile lastRP = arpList.get(j);
				a_version = lastRP.vfs.getFileVersion(path);
				if(a_version != -1){
					a_data2 = lastRP.vfs.getFileData(path);
					break;
				}
			}
			int b_version = -1;
			byte b_data2[] = null;
			
			for(int j = brpList.size() -1 ; j >= 0 ; j--){
				ResourceFile lastRP = brpList.get(j);
				b_version = lastRP.vfs.getFileVersion(path);
				if(b_version != -1){
					b_data2 = lastRP.vfs.getFileData(path);
					break;
				}
			}

			//新的文件
			if(a_version == -1 && b_version == -1){
				if(debug)
					System.out.println("添加文件["+path+"]...");
				rp.vfs.put(path, 1, data);
			}else{
				if(aPackageFlag){
					if(a_version == -1 || b_version>a_version){
						//生成a包，a包没有或者b包中更加的新
						boolean sameData = Arrays.equals(data, b_data2);
						if(sameData){
							if(debug)
								System.out.println("添加文件["+path+"]...");
							rp.vfs.put(path, b_version, data);
						}else{
							if(debug)
								System.out.println("添加文件["+path+"]...");
							rp.vfs.put(path, b_version+1, data);
						}
					}else{
						boolean sameData = Arrays.equals(data, a_data2);
						if(sameData){
							if(fullPackage){
								if(debug)
									System.out.println("添加文件["+path+"]...");
								rp.vfs.put(path, a_version, data);
							}
						}else{
							if(debug)
								System.out.println("添加文件["+path+"]...");
							rp.vfs.put(path, a_version+1, data);
						}
					}
				}else{
					if(b_version == -1 || a_version>b_version){
						//生成a包，a包没有
						boolean sameData = Arrays.equals(data, a_data2);
						if(sameData){
							if(debug)
								System.out.println("添加文件["+path+"]...");
							rp.vfs.put(path, a_version, data);
						}else{
							if(debug)
								System.out.println("添加文件["+path+"]...");
							rp.vfs.put(path, a_version+1, data);
						}
					}else{
						boolean sameData = Arrays.equals(data, b_data2);
						if(sameData){
							if(fullPackage){
								if(debug)
									System.out.println("添加文件["+path+"]...");
								rp.vfs.put(path, b_version, data);
							}
						}else{
							if(debug)
								System.out.println("添加文件["+path+"]...");
							rp.vfs.put(path, b_version+1, data);
						}
					}
				}
			}
		}
		int blockNum = rp.vfs.getDataBlockNum();
		long diskSize = rp.vfs.getCurrentDiskSize();
		
		if(blockNum > 0){
			al.add(rp);
			rp.vfs.close();
			System.out.println("资源包" + resourcePackageDataFileName + "已生成，包含个"+blockNum+"文件，大小为"+StringUtil.addcommas(diskSize)+"，耗时"+((System.currentTimeMillis()- startTime)/1000)+"秒");
		}else{
			rp.vfs.clear();
			System.out.println("资源包" + resourcePackageDataFileName + "未生成生成，没有发现任何变化，耗时"+((System.currentTimeMillis()- startTime)/1000)+"秒");
		}
	}
	
	public String getResourcePackageDataFileName(int masterVersion,int slaveVersion,boolean fullPackage,boolean aPackageFlag){
		return packageFilePrefix + DateUtil.formatDate(new java.util.Date(),"yyyyMMddHHmmss")+"_"+(aPackageFlag?"A":"B")+ (fullPackage?"F":"H") + "_" +masterVersion+"."+slaveVersion+packageFileSuffix;
	}
	
	public static void getAllFile(File f,ArrayList<File> al){
		if(f.isFile()){
			al.add(f);
		}else{
			File fs[] = f.listFiles();
			for(int i = 0 ; fs != null && i < fs.length ; i++){
				getAllFile(fs[i],al);
			}
		}
	}
	
	public static byte[] getFileData(File f) throws Exception{
		byte data[] = new byte[(int)f.length()];
		FileInputStream input = new FileInputStream(f);
		input.read(data);
		input.close();
		return data;
	}
	
	
	/**
	 * java -cp . xxxxxxx -f res.conf -g -a -f -r masterversion slaveversion
	 *   
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
		String conf = "res.conf";
		boolean generate = false;
		boolean genxml = false;
		String xmlFile = "res.xml";
		boolean aPackage = false;
		boolean fullPackage = false;
		boolean release = false;
		boolean debug = false;
		int masterVersion = 1;
		int slaveVersion = 0;
		
		for(int i = 0 ; i < args.length ; i++){
			String s = args[i];
			if(s.equals("-f")){
				conf = args[i+1];
				i++;
			}else if(s.equals("-g")){
				generate = true;
			}else if(s.equals("-x")){
				genxml = true;
				xmlFile = args[i+1];
				i++;
			}else if(s.equals("-a")){
				aPackage = true;
			}else if(s.equals("-debug")){
				debug = true;
			}
			else if(s.equals("-F")){
				fullPackage = true;
			}else if(s.equals("-r")){
				release = true;
			}else if(s.equals("-h")){
				printHelp();
				System.exit(0);
			}
		}

		if(generate){
			try{
				masterVersion = Integer.parseInt(args[args.length-2]);
				slaveVersion = Integer.parseInt(args[args.length-1]);
			}catch(Exception e){
				System.out.println("参数配置错误，请检查！");
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
		
		ResourcePackageTools drm = new ResourcePackageTools();
		
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
		
		if(genxml){
			drm.init();
			String ss = drm.generateXmlConfig();
			System.out.println(ss);
			File f = new File(System.getProperty("user.dir"),xmlFile);
			FileOutputStream output = new FileOutputStream(f);
			output.write(ss.getBytes());
			output.close();
			System.out.println("xml配置文件生成完毕，别忘了在使用此配置文件前，需要手动压缩资源包文件，建议采用7z压缩！");
			
		}else if(generate){
			drm.init();
			drm.generatePackage(masterVersion, slaveVersion, fullPackage, aPackage);
		}else{
			drm.init();
		}
	}
	
	
	public static void printHelp(){
		System.out.println("java -cp ./xuanzhi-tools-1.1.jar com.xuanzhi.tools.resource.DefaultResourceManager -f conf [-g] [-x xmlfile] [-a] [-F] [-r] [masterversion slaveversion]");
		System.out.println("Options:");
		System.out.println("        -f conf 配置文件，配置资源文件所在路径，资源包存储路径，A包文件列表所在路径");
		System.out.println("        -g 生成资源包");
		System.out.println("        -x xml 生成网关上使用的xml配置文件");
		System.out.println("        -a 配合-g使用，生成A包对应的资源包");
		System.out.println("        -F 配合-g使用，生成全资源包");
		System.out.println("        -r 配合-g使用，生成发布版本的资源包，发布版本资源包不能被覆盖");
		System.out.println("        masterversion 配合-g使用,资源包的主版本号，必须为倒数第二个参数");
		System.out.println("        slaveversion 配合-g使用,资源包的副版本号，必须为倒数第一个参数");
	}
}
