package com.xuanzhi.tools.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

public class FileSystemTestCase extends TestCase{

	
	public void testA(){
		String dir = System.getProperty("user.dir");
		System.out.println("==========="+dir+"===========");
		
		dir = "D:/workspace/hg/final_ios_res";
		ArrayList<File> al = new ArrayList<File>();
		File root = new File(dir);
		listFile(root,al);
		
		DefaultVirtualFileSystem fs = new DefaultVirtualFileSystem(new File("d:/test3.vfs"),"test");
		
		long start = System.currentTimeMillis();
		for(int i = 0 ; i < al.size() ; i++){
			File f = al.get(i);
			String path = f.getPath();
			path = path.substring(dir.length());
			fs.put(path, 1, getFileContent(f));
		}
		System.out.println("testA,vfs:" + fs+",cost "+(System.currentTimeMillis() - start)+" ms");
		fs.close();
	}
	
	public void testB(){
		String dir = System.getProperty("user.dir");
		System.out.println("==========="+dir+"===========");
		dir = "D:/workspace/hg/final_ios_res";
		
		long start = System.currentTimeMillis();
		DefaultVirtualFileSystem fs = new DefaultVirtualFileSystem(new File("d:/test3.vfs"),"test");
		
		String fns[] = fs.getFileNames();
		for(int i = 0 ; i < fns.length ; i++){
			String fn = fns[i];
			System.out.println("fn["+i+"]="+fn);
			int version = fs.getFileVersion(fn);
			assertEquals(1, version);
		}
		fs.close();
		
		System.out.println("testA,vfs:" + fs+",cost "+(System.currentTimeMillis() - start)+" ms");
		
	}
	
	
	
	
	public void testC(){
		long start = System.currentTimeMillis();
		
		DefaultVirtualFileSystem fs = new DefaultVirtualFileSystem(new File("d:/test.vfs"),"test");
		DefaultVirtualFileSystem fs2 = new DefaultVirtualFileSystem(new File("d:/test2.vfs"),"test");
		fs.merge(fs2);
		
		System.out.println("testA,vfs:" + fs+",cost "+(System.currentTimeMillis() - start)+" ms");
		fs.close();
	}
	
	
	
	
	
	
	
	
	
	protected void listFile(File f, ArrayList<File> al){
		if(f.isFile()){
			al.add(f);
		}else{
			File fs[] = f.listFiles();
			for(int i = 0 ; i < fs.length ; i++){
				listFile(fs[i],al);
			}
		}
	}
	protected byte[] getFileContent(File f){
		if(f.isFile() == false) return new byte[0];
		int len = (int)f.length();
		byte[] data = new byte[len];
		try{
			FileInputStream input = new FileInputStream(f);
			input.read(data);
			input.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return data;
	}
}
