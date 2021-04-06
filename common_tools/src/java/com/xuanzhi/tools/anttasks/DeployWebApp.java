package com.xuanzhi.tools.anttasks;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;

import com.xuanzhi.tools.text.DateUtil;

import java.io.*;
import java.util.*;

/**
 * 部署项目
 * 
 * 有如下的参数：
 * 		部署的根目录
 * 		备份的根目录
 * 		备份目录的时间格式，默认为yyyyMMddHHmmss
 * 		需要部署的文件集合
 * 		部署的类型，分为部署和回滚
 *
 * 
        
      <taskdef name="deploywebapp" classname="com.xuanzhi.tools.anttasks.DeployWebApp" classpath="lib/xuanzhi-tools-1.0.jar"/>
 *    
 *    <target name="deploy">
            <deploywebapp type="deploy" 
                    srcDir="" 
                    deployDir="/usr/local/projects/message_platform/deploy" 
                    backupDir="/usr/local/projects/message_platform/backup"
                    logFile="./deploy.log" 
                    maxBackupNum="3">
                    <backup>
                            <fileset dir="WEB-INF/lib">
                                    <include name="*.jar"/>
                            </fileset>
                            <fileset dir="WEB-INF/conf">
                                    <include name="*.xml"/>
                            </fileset>
                            <fileset dir="">
                                    <include name="*.jsp"/>
                            </fileset>
                    </backup>
                    <copy to="WEB-INF/lib">
                            <fileset dir="lib">
                                    <include name="*.jar"/>
                            </fileset>
                    </copy>
                    <copy to="WEB-INF/conf">
                            <fileset dir="conf">
                                    <include name="*.xml"/>
                            </fileset>
                    </copy>
                    <copy to="">
                            <fileset dir="web">
                                    <include name="*.jsp"/>
                            </fileset>
                    </copy>
                    <copy file="conf/mt_log4j.properties" to="WEB-INF/classes/log4j.properties"/>
            </deploywebapp>
    </target>

    <target name="rollback">
            <deploywebapp type="rollback" debug="on"
                    srcDir="" 
                    deployDir="/usr/local/projects/message_platform/deploy" 
                    backupDir="/usr/local/projects/message_platform/backup"
                    logFile="./deploy.log">
            </deploywebapp>
    </target>
 */
public class DeployWebApp extends Task{

	public static class Copy{
		String file;
		String to;
		boolean forceOverwrite = false;
		boolean debug = false;
		
		protected ArrayList<FileSet> filesets = new ArrayList<FileSet>();
		
		public void addFileset(FileSet fs){
			filesets.add(fs);
		}
		
		public String getFile() {
			return file;
		}
		public void setFile(String from) {
			this.file = from;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		
		public void setToDir(String to) {
			this.to = to;
		}
		
		public boolean isForceOverwrite() {
			return forceOverwrite;
		}
		public void setForceOverwrite(boolean forceOverwrite) {
			this.forceOverwrite = forceOverwrite;
		}
		
		public void setDebug(boolean b){
			debug = b;
		}
		
	}
	public static class Backup{
		protected ArrayList<FileSet> filesets = new ArrayList<FileSet>();
		boolean debug = false;
		public void setDebug(boolean b){
			debug = b;
		}
		
		public void addFileset(FileSet fs){
			filesets.add(fs);
		}
		
		
	}
	
	private FileUtils fileUtils;
	
	protected File srcDir;
	
	protected File deployDir;
	
	protected File backupDir;
	
	protected String type = null;
	
	protected String timeFormat = "yyyyMMddHHmmss";
	
	protected boolean debug = false;
	
	protected ArrayList<Backup> backup = new ArrayList<Backup>();
	
	protected ArrayList<Copy> copies = new ArrayList<Copy>();
	
	protected ArrayList<Replace> replaces = new ArrayList<Replace>();
	
	protected ArrayList<Mkdir> mkdirs = new ArrayList<Mkdir>();
	
	protected File logFile = null;
	protected java.io.FileOutputStream output = null;
	
	protected int maxBackupNum = 100;
	
	public void setMaxBackupNum(int n){
		maxBackupNum = n;
	}
	
	public void log(String message){
		log(message,true);
	}
	
	public void log(String message,boolean force){
		if(logFile != null && output == null){
			try {
				output = new java.io.FileOutputStream(logFile,true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(output != null){
			try {
				output.write(("["+DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "] ["+this.getOwningTarget().getName()+"] ["+ type + "] [" + message + "]\n").getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(force || debug)
			super.log(message);
	}
	
	public DeployWebApp() {
        fileUtils = FileUtils.newFileUtils();
    }
	
	public Backup createBackup(){
		Backup fss = new Backup();
		backup.add(fss);
		return fss;
	}
	
	public Replace createReplace(){
		Replace r = new Replace();
		replaces.add(r);
		return r;
	}
	
	public Mkdir createMkdir(){
		Mkdir m = new Mkdir();
		mkdirs.add(m);
		return m;
	}

	public Copy createCopy(){
		Copy c = new Copy();
		copies.add(c);
		return c;
	}

	public void setDebug(boolean b){
			debug = b;
	}
	
	public void setLogFile(File f){
		logFile = f;
	}
	public File getSrcDir() {
		return srcDir;
	}

	public void setSrcDir(File srcDir) {
		this.srcDir = srcDir;
	}

	public File getDeployDir() {
		return deployDir;
	}

	public void setDeployDir(File deployDir) {
		this.deployDir = deployDir;
	}

	public File getBackupDir() {
		return backupDir;
	}

	public void setBackupDir(File backupDir) {
		this.backupDir = backupDir;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setTimeFormat(String s){
			timeFormat = s;
	}
	
	 /**
     * Performs the copy operation.
     */
    public void execute() throws BuildException {

    	validateAttributes();   
    	if(type.equals("deploy")){
    		backup(true);
    		Hashtable table = deploy(true);
    	
    		if(table.size() == 0){
    			log("Nothing to do with this operation, all files are up to date.");
    			return;
    		}
    		log("You are preparing to deploy " + getOwningTarget().getName()+" and deploy dir is "+deployDir.getAbsolutePath()+"");
    		log("And it will do these copies listing below:");
    		Iterator it = table.keySet().iterator();
    		while(it.hasNext()){
    			String src = (String)it.next();
    			String to = (String)table.get(src);
    			log("    " + src + "    ------->   " + to);
    		}
    		log("Are you sure to do this(yes/no)?");
    		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    	String s = null;
	    	try {
				s = reader.readLine();
			} catch (IOException e) {
				throw new BuildException("deploy failed cause io error!" + e);
			}
			s = s.trim();
			
			if(s.equals("yes")){
				for(int i = 0 ; i < mkdirs.size() ; i++){
					Mkdir r = mkdirs.get(i);
					r.execute();
				}
				backup(false);
				deploy(false);
				for(int i = 0 ; i < replaces.size() ; i++){
					Replace r = replaces.get(i);
					r.execute();
				}
			}
    		
			checkBackupNum();
			
    	}else if(type.equals("rollback")){
    		rollback();
    	}
    	
    	
    	
    	if(output != null){
    		try {
				output.write(("***********************************************************\n").getBytes());
				output.write(("*                   END EXECUTE                           *\n").getBytes());
				output.write(("***********************************************************\n").getBytes());
    			output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    
    protected void backup(boolean check) throws BuildException{
    	FileNameMapper mapper = new IdentityMapper();
    	Hashtable fileCopyMap = new Hashtable();
    	
    	String toDirStr = DateUtil.formatDate(new Date(), timeFormat);
    	File toDir = new File(backupDir,toDirStr);
    	
    	for(int i = 0 ; i < backup.size() ; i++){
    		Backup fss = backup.get(i);
    		FileSet fs[] = fss.filesets.toArray(new FileSet[0]);
    		for(int j = 0 ; j < fs.length ; j++){
	    		if(check){
	    			File f = project.getBaseDir();
		    		File f2 = fs[j].getDir(project);
		    		
		    		fs[j].setDir(new File(deployDir,formatPath(f,f2)));
	    		}

	    		if(fs[j].getDir(project).exists()){
	    			DirectoryScanner ds = fs[j].getDirectoryScanner(project);
	    			String srcFiles[] = ds.getIncludedFiles();
	    			String ss = formatPath(deployDir,fs[j].getDir(project));
	    			if(ss.length()>0){
	    				if(ss.charAt(ss.length()-1) == File.separatorChar) ss = ss.substring(0,ss.length()-1);
	    				for(int k = 0 ; k < srcFiles.length ; k++){

	    					srcFiles[k] = ss + File.separatorChar +srcFiles[k]; 
	    				}
	    			}
	    			buildMap(deployDir,toDir,srcFiles,mapper,fileCopyMap,true);
	    		}
    		}
    	}
    	if(check == false && fileCopyMap.size() > 0){
    		log("preparing to backup .... ");
    		doFileCopying(fileCopyMap,toDir,true);
    		log("backup OK! the backup dir is " + toDir.getAbsolutePath()+", and "+fileCopyMap.size()+" files had been copied!");
    	}
    }
    
    protected Hashtable deploy(boolean check) throws BuildException{
    	Hashtable table = new Hashtable();
    	FileNameMapper mapper = new IdentityMapper();
    	
    	if(check == false){
    		log("preparing to deploy .... ");
    	}
    	
    	for(int i = 0 ; i < copies.size() ; i++){
    		Hashtable fileCopyMap = new Hashtable();
    		Copy c = copies.get(i);
    		if(c.file != null){
    			File f = new File(srcDir,c.file);
    			if(f.exists()){
    				if(c.to == null){
    					File toFile = new File(deployDir,f.getName());
    					if(!toFile.exists() || toFile.lastModified() < f.lastModified())
    						fileCopyMap.put(f.getAbsolutePath(), toFile.getAbsolutePath());
    				}else{
    					File d = new File(deployDir,c.to);
    					if(d.isDirectory()){
    						File toFile = new File(d,f.getName());
    						if(!toFile.exists() || toFile.lastModified() < f.lastModified())
    							fileCopyMap.put(f.getAbsolutePath(), toFile.getAbsolutePath());
    					}else if(d.isFile()){
    						if(d.lastModified() < f.lastModified())
    							fileCopyMap.put(f.getAbsolutePath(), d.getAbsolutePath());
    					}else{
    						if(c.to.indexOf(".") >=0)
    							if(d.lastModified() < f.lastModified())
    							fileCopyMap.put(f.getAbsolutePath(), d.getAbsolutePath());
    						else{
    							File toFile = new File(d,f.getName());
    							if(!toFile.exists() || toFile.lastModified() < f.lastModified())
    								fileCopyMap.put(f.getAbsolutePath(), toFile.getAbsolutePath());
    						}
    					}
    				}
    			}else{
    				throw new BuildException("file ["+f.getAbsolutePath()+"] not exist!");
    			}
    		}
    		
    		FileSet fs[] = c.filesets.toArray(new FileSet[0]);
    		for(int j = 0 ; j < fs.length ; j++){
    			if(check){
	    			File f = project.getBaseDir();
	    			File f2 = fs[j].getDir(project);
	    			
	    			fs[j].setDir(new File(srcDir,formatPath(f,f2)));
    			}
    			DirectoryScanner ds = fs[j].getDirectoryScanner(project);
    			
    			String srcFiles[] = ds.getIncludedFiles();
    			
    			File destDir = deployDir;
    			if(c.to != null){
    				destDir = new File(deployDir,c.to);
    			}
    			
    			buildMap(fs[j].getDir(project),destDir,srcFiles,mapper,fileCopyMap,c.forceOverwrite);
    			
    		}
    		
    		table.putAll(fileCopyMap);
    		
    		if(check == false){
    			doFileCopying(fileCopyMap,deployDir,c.forceOverwrite);
    		}
    	}
    	
    	if(check == false){
    		log("deploy OK! the deploy dir is " + deployDir + ", and "+table.size()+" files had been copied!");
    	}
    	
    	return table;
    	
    }
    protected void checkBackupNum(){
    	File bdir[] = backupDir.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return pathname.isDirectory();	
			}
    	});
    	if(bdir!= null && bdir.length > this.maxBackupNum){
    		String ss[] = new String[bdir.length];
    		for(int i = 0 ;i < ss.length ; i++){
    			ss[i] = bdir[i].getName();
    		}
    		Arrays.sort(ss);
    		for(int i = 0 ; i < ss.length-this.maxBackupNum ; i++){
    			File dir = new File(backupDir,ss[i]);
    			try {
					removeDir(dir);
					log("remove backup [" + ss[i] +"] cause reach max backup num ["+this.maxBackupNum+"]");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }

    private void removeDir(File dir) throws IOException {

        // check to make sure that the given dir isn't a symlink
        // the comparison of absolute path and canonical path
        // catches this
	
	//        if (dir.getCanonicalPath().equals(dir.getAbsolutePath())) {
	// (costin) It will not work if /home/costin is symlink to /da0/home/costin ( taz
	// for example )
	String[] list = dir.list();
	for (int i = 0; i < list.length; i++) {
	    String s = list[i];
	    File f = new File(dir, s);
	    if (f.isDirectory()) {
		removeDir(f);
	    } else {
		if (!f.delete()) {
        	    throw new BuildException("Unable to delete file " + f.getAbsolutePath());
	        }
	    }
	}
        if (!dir.delete()) {
	    throw new BuildException("Unable to delete directory " + dir.getAbsolutePath());
	}
    }
    
    protected void rollback() throws BuildException{
    	log("preparing to rollback .... ");
    	
    	File bdir[] = backupDir.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return pathname.isDirectory();	
			}
    	});
    	
    	System.out.println("all available backup selections list here:");
    	for(int i = 0 ; bdir != null && i <  bdir.length ; i++){
    		System.out.println("    " + (i+1) + ") " + bdir[i].getName());
    	}
    	if(bdir == null || bdir.length == 0){
    		throw new BuildException("no available backup selection to rollback!");
    	}
    	System.out.print("please input the backup selection name:");
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	String s = null;
    	try {
			s = reader.readLine();
		} catch (IOException e) {
			throw new BuildException("rollback failed cause io error!" + e);
		}
		s = s.trim();
		File dir = null;
		for(int i = 0 ; bdir != null && i <  bdir.length ; i++){
			if(bdir[i].getName().equals(s)){
				dir = bdir[i];
			}
		}
		if(dir == null){
			throw new BuildException("rollback failed cause input error! the backup["+s+"] not exist!");
		}
	
		FileNameMapper mapper = new IdentityMapper();
    	Hashtable fileCopyMap = new Hashtable();
   
    	FileSet fs = new FileSet();
    	fs.setDir(dir);
    	fs.setIncludes("**/*.*");
    	DirectoryScanner ds = fs.getDirectoryScanner(project);
    	ds.setBasedir(dir);
    	String srcFiles[] = ds.getIncludedFiles();
    	buildMap(dir,deployDir,srcFiles,mapper,fileCopyMap,true);

    	doFileCopying(fileCopyMap,deployDir,true);
    	log("rollback OK! the backup dir is " + dir.getName() +", and "+fileCopyMap.size()+" files had been copied!");
    	
    }
    
     /**
     * Actually does the file (and possibly empty directory) copies.
     * This is a good method for subclasses to override.
     */
    protected void doFileCopying(Hashtable fileCopyMap,File destDir,boolean forceOverwrite) {
        if (fileCopyMap.size() > 0) {
        	
            log("Copying " + fileCopyMap.size() + 
                " file" + (fileCopyMap.size() == 1 ? "" : "s") + 
                " to " + destDir.getAbsolutePath(),false);

            Enumeration e = fileCopyMap.keys();
            while (e.hasMoreElements()) {
                String fromFile = (String) e.nextElement();
                String toFile = (String) fileCopyMap.get(fromFile);

                if( fromFile.equals( toFile ) ) {
                    
                    log("Skipping self-copy of " + fromFile,false);
                    continue;
                }

                try {
                	
                	log("Copying " + fromFile + " to " + toFile,false);	
                    
                    FilterSetCollection executionFilters = new FilterSetCollection();
                
                    fileUtils.copyFile(fromFile, toFile, executionFilters,
                                       forceOverwrite, true);
                } catch (IOException ioe) {
                    String msg = "Failed to copy " + fromFile + " to " + toFile
                        + " due to " + ioe.getMessage();
                    throw new BuildException(msg, ioe, location);
                }
            }
        }
    }
    
    protected void buildMap(File fromDir, File toDir, String[] names,
                            FileNameMapper mapper, Hashtable map,boolean forceOverwrite) {

        String[] toCopy = null;
        if (forceOverwrite) {
            Vector v = new Vector();
            for (int i=0; i<names.length; i++) {
                if (mapper.mapFileName(names[i]) != null) {
                    v.addElement(names[i]);
                }
            }
            toCopy = new String[v.size()];
            v.copyInto(toCopy);
        } else {
            SourceFileScanner ds = new SourceFileScanner(this);
            toCopy = ds.restrict(names, fromDir, toDir, mapper);
        }
        
        for (int i = 0; i < toCopy.length; i++) {
            File src = new File(fromDir, toCopy[i]);
            File dest = new File(toDir, mapper.mapFileName(toCopy[i])[0]);
            map.put( src.getAbsolutePath(), dest.getAbsolutePath() );
        }
    }
     
    
    
    
    
    protected void validateAttributes() throws BuildException{
    	if(type == null || (!type.equals("deploy") && !type.equals("rollback"))){
    		throw new BuildException("DeployWebApp's type ["+type+"] is invalid!");
    	}
    	
    	if(srcDir == null || !srcDir.isDirectory() || !srcDir.exists()){
    		throw new BuildException("srcDir ["+srcDir+"] not exists or is file!");
    	}
    	
    	if(deployDir == null || !deployDir.isDirectory() || !deployDir.exists()){
    		throw new BuildException("deployDir ["+deployDir+"] not exists or is file!");
    	}
    	
    	if(backupDir == null || !backupDir.isDirectory() || !backupDir.exists()){
    		throw new BuildException("backupDir ["+backupDir+"] not exists or is file!");
    	}
    	
    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
    	try{
    		sdf.applyPattern(timeFormat);
    	}catch(Exception e){
    		throw new BuildException("TimeFormat ["+timeFormat+"] is invaild! " + e.getMessage());
    	}
    	
    }
    
    public static String formatPath(File baseDir,File file){
    	if(baseDir == null) return file.getAbsolutePath();
    	
    	File d = file;
    	while(d != null){
    		if(d.equals(baseDir)){
    			String s = file.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
    			if(s.length() > 0 && s.charAt(0) == File.separatorChar){
    				s = s.substring(1);
    			}
    			return s;
    		}
    		d = d.getParentFile();
    	}
    	ArrayList<String> al1 = new ArrayList<String>();
    	ArrayList<String> al2 = new ArrayList<String>();
    	d = baseDir;
    	while(d != null){
    		al1.add(0,d.getName());
    		d = d.getParentFile();
    	}
    	d = file;
    	while(d != null){
    		al2.add(0,d.getName());
    		d = d.getParentFile();
    	}
    	for(int i = 0 ; i < al2.size() ; i++){
    		String s1 = al1.get(i);
    		String s2 = al2.get(i);
    		if(s1.equals(s2) == false){
    			String s = "";
    			for(int j = i ; j < al2.size() ; j++){
    				s += al2.get(j);
    				if(j < al2.size() -1){
    					s += File.separatorChar;
    				}
    			}
    			for(int j = i ; j < al1.size() ; j++){
    				s = ".." + File.separatorChar + s;
    			}
    			return s;
    		}
    	}
    	String s = "";
    	for(int j = al2.size() ; j < al1.size() ; j++){
    		s = ".." + File.separatorChar + s;
    	}
    	return s;
    	
    }
    
}
