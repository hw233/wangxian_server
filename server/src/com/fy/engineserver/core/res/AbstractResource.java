package com.fy.engineserver.core.res;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
abstract public class AbstractResource implements Constants{	
	static public final int RES_TYPE_PNGMTL = 1;
	static public final int RES_TYPE_PART = 2;
	static public final int RES_TYPE_APPEAR = 3;
	static public final int RES_TYPE_MAP = 4;
	static public final int RES_TYPE_PNGRES = 5;
	
	protected File dataFile;
	
	String relatePath;
	
	public File getDataFile() {
		return dataFile;
	}
	/**
     * 发送给客户端的数据 
     */
	byte[] data;
    int version;    
      
    
    public void load(File f,String relatePath)throws Exception{
    	this.relatePath = relatePath;
    	dataFile = f;
    	FileInputStream fis  = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		int len = dis.available();
		data = new byte[len];
		dis.read(data);
		fis.close();			
		
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream is = new DataInputStream(bis);
		load(is);
		is.close();
    }
    abstract public void load(DataInputStream is) throws Exception;
    
   
    public int getVersion(){
    	return version;
    }
	public byte[] getGameBinaryData() {
		return data;
	}
	public boolean isBinaryDataExists() {
		return (data != null && data.length > 0);
	}
}
