package com.xuanzhi.tools.cache.diskcache.concrete;

import java.io.*;

/**
 * 数据块：数据文件被拆分为多个数据块，每个数据块的大小任意，同时每个数据块中只存放一个(key,value)。
 * 数据块包括如下信息：
 * 		数据块在数据文件中的位置
 * 		数据块的大小
 * 		数据块是否包含数据（也就是(key,value)对）
 * 		数据块对应数据的生命结束时间
 * 		数据块对应数据的Idle超时时间
 * 		数据块对应数据key的hashcode
 * 		数据块对应数据key
 * 		数据块对应数据value
 * 		数据块对应数据的校验码
 * 
 * 数据块的存储格式：
 * 
 *  DataBlockLength(4B) + FreeFlag(1B) + LifeTime(8B) + IdleTime(8B)+hashcode(4B)+
 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
 *   
 */
public class DataBlock {
	
	public final static int MIN_BLOCK_SIZE = 512;
	
	public static int MAX_BLOCK_SIZE = 1024 * 1024 * 1024;
	
	protected final static int HEADER_SIZE = 4+1+8+8+4+4+4+16;
	
	transient AbstractDiskCache ddc = null;
	
	DataBlock(AbstractDiskCache ddc){
		this.ddc = ddc;
	}
	
	//数据块在数据文件中的位置
	public long offset = 0;
	
	//数据块的大小
	public int length = 0;
	
	//数据块是否包含数据（也就是(key,value)对）
	public boolean containsData = false;
	
	//数据块对应数据的生命结束时间
	public long lifetime = 0;
	
	//数据块对应数据的Idle超时时间
	public long idletime = 0;
	
	//数据块对应数据key的hashcode
	public int hashcode = 0;
	
	//对应的数据
	transient Object key=null;
	transient Object value=null;
	public transient int keyLength=0;
	public transient int valueLength=0;
	
	//文件中前一个数据块
	DataBlock previous;
	
	//文件中后一个数据块
	DataBlock next;
	
	void addPrevious(DataBlock db){
		db.previous = this.previous;
		db.next = this;
		this.previous.next = db;
		this.previous = db;
	}
	
	void addNext(DataBlock db){
		db.previous = this;
		db.next = this.next;
		this.next.previous = db;
		this.next = db;
	}
	
	void remove(){
		this.previous.next = this.next;
		this.next.previous = this.previous;
	}
	
	boolean checkValid(){
		if(this.length < HEADER_SIZE) return false;
		return true;
	}
	
	Object getKey(RandomAccessFile reader) throws Exception{
		if(containsData == false) return null;
		if(key != null) return key;
		readAllData(reader);
		return key;
	}
	
	Object getValue(RandomAccessFile reader) throws Exception{
		if(containsData == false) return null;
		if(value != null) return value;
		readAllData(reader);
		return value;
	}
	
	/**
	 * 读取数据块头部信息
	 * DataBlockLength(4B) + FreeFlag(1B) + LifeTime(8B) + IdleTime(8B)+hashcode(4B)+
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	void readHeader(RandomAccessFile reader)throws Exception{
		byte bytes[] = new byte[4+1+8+8+4];
		reader.seek(offset);
		reader.read(bytes);
		int of = 0;
		this.length = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+=4;
		this.containsData = ((int)ByteArrayUtils.byteArrayToNumber(bytes,of,1)!=0);
		of+=1;
		this.lifetime = ByteArrayUtils.byteArrayToNumber(bytes,of,8);
		of+=8;
		this.idletime = ByteArrayUtils.byteArrayToNumber(bytes,of,8);
		of+=8;
		this.hashcode = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+= 4;
	}
	
	/**
	 * 读取数据块
	 * DataBlockLength(4B) + FreeFlag(1B) + LifeTime(8B) + IdleTime(8B)+hashcode(4B)+
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	void readAllData(RandomAccessFile reader)throws Exception{
		if(checkValid() == false){
			throw new Exception("block is invalid.(length="+length+")");
		}
		byte bytes[] = new byte[length];
		
		reader.seek(offset);
		reader.read(bytes);
		
		int of = 0;
		int _length = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+=4;
		boolean _flag = ((int)ByteArrayUtils.byteArrayToNumber(bytes,of,1)!=0);
		of+=1;
		long _lifetime = ByteArrayUtils.byteArrayToNumber(bytes,of,8);
		of+=8;
		long _idletime = ByteArrayUtils.byteArrayToNumber(bytes,of,8);
		of+=8;
		int _hashcode = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+= 4;
		
		
		if(_length != length){
			String s = "format error block length of memory("+length+") and disk("+_length+") not same.";
			ddc.reportBadBlock(this,0);
			//free this block
			throw new Exception(s);
		}
		
		if(_flag != this.containsData){
			String s ="format error block containsData of memory("+containsData+") and disk("+_flag+") not same.";
			ddc.reportBadBlock(this,1);
			////free this block
			throw new Exception(s);
		}
		
		if(_hashcode != hashcode){
			String s = "format error block hashcode of memory("+hashcode+") and disk("+_hashcode+") not same.";
			ddc.reportBadBlock(this,2);
			//free this block
			throw new Exception(s);
		}
		
		if(_lifetime != lifetime){
			//ddc.reportBadBlock(this,3);
			//free this block
			//throw new Exception("format error block lifetime of memory("+lifetime+") and disk("+_lifetime+") not same.");
		}
		
		if(_idletime != idletime){
			//ddc.reportBadBlock(this,4);
			//free this block
			//throw new Exception("format error block idletime of memory("+idletime+") and disk("+_idletime+") not same.");
		}
		int kl = this.keyLength;
		int vl = this.valueLength;
		if(key == null) kl = 0;
		if(value == null) vl = 0;
		try{
			this.keyLength = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
			of+=4;
			key = ByteArrayUtils.byteArrayToObject(bytes,of,this.keyLength);
			of+=keyLength;
			this.valueLength = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
			of+=4;
			value = ByteArrayUtils.byteArrayToObject(bytes,of,this.valueLength);
			of+=valueLength;
			
			int memoryChange = this.keyLength + this.valueLength - kl - vl;
			ddc.addCurrentMemorySize(memoryChange);
			
		}catch(Exception e){
			this.keyLength = kl;
			this.valueLength = vl;
			ddc.reportBadBlock(this,5);
			
			throw e;
		}

		
	}
	
	
	/**
	 * 将数据块中的所有数据写入到数据文件中
	 * DataBlockLength(4B) + FreeFlag(1B) + LifeTime(8B) + IdleTime(8B)+hashcode(4B)+
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	int invalidateAllData(RandomAccessFile writer) throws Exception{
		if(checkValid()){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(ByteArrayUtils.numberToByteArray(length,4));
			out.write(ByteArrayUtils.numberToByteArray(containsData?1:0,1));
			out.write(ByteArrayUtils.numberToByteArray(lifetime,8));
			out.write(ByteArrayUtils.numberToByteArray(idletime,8));
			out.write(ByteArrayUtils.numberToByteArray(hashcode,4));
			
			byte bytes1[] = ByteArrayUtils.objectToByteArray(key);
			keyLength = bytes1.length;
			out.write(ByteArrayUtils.numberToByteArray(keyLength,4));
			out.write(bytes1);
			
			byte bytes2[] = ByteArrayUtils.objectToByteArray(value);
			valueLength =  bytes2.length;
			out.write(ByteArrayUtils.numberToByteArray(valueLength,4));
			out.write(bytes2);
			
			byte checkSum [] = ByteArrayUtils.key_value_md5(bytes1,bytes2);
			out.write(checkSum,0,16);
			
			int k = out.size();
			if(k < length){
				out.write(new byte[length-k]);
			}
			
			writer.seek(offset);
			writer.write(out.toByteArray());
			return out.size();
		}else{
			throw new Exception("block is invalid.(length="+length+")");
		}
	}
	
	/**
	 * 将数据块中的所有数据写入到数据文件中
	 * DataBlockLength(4B) + FreeFlag(1B) + LifeTime(8B) + IdleTime(8B)+hashcode(4B)+
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	int invalidateHeaderData(RandomAccessFile writer) throws Exception{
		if(checkValid()){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(ByteArrayUtils.numberToByteArray(length,4));
			out.write(ByteArrayUtils.numberToByteArray(containsData?1:0,1));
			out.write(ByteArrayUtils.numberToByteArray(lifetime,8));
			out.write(ByteArrayUtils.numberToByteArray(idletime,8));
			out.write(ByteArrayUtils.numberToByteArray(hashcode,4));
			
			writer.seek(offset);
			writer.write(out.toByteArray());
			return out.size();
		}else{
			throw new Exception("block is invalid.(length="+length+")");
		}
	}
	
	
	/**
	 * 将数据块中的所有数据写入到数据文件中
	 * DataBlockLength(4B) + FreeFlag(1B) + LifeTime(8B) + IdleTime(8B)+hashcode(4B)+
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	int invalidateAllData(RandomAccessFile writer,byte keyBytes[],byte valueBytes[]) throws Exception{
		if(checkValid()){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(ByteArrayUtils.numberToByteArray(length,4));
			out.write(ByteArrayUtils.numberToByteArray(containsData?1:0,1));
			out.write(ByteArrayUtils.numberToByteArray(lifetime,8));
			out.write(ByteArrayUtils.numberToByteArray(idletime,8));
			out.write(ByteArrayUtils.numberToByteArray(hashcode,4));
			
			keyLength = keyBytes.length;
			out.write(ByteArrayUtils.numberToByteArray(keyLength,4));
			out.write(keyBytes);
			
			valueLength =  valueBytes.length;
			out.write(ByteArrayUtils.numberToByteArray(valueLength,4));
			out.write(valueBytes);
			
			byte checkSum [] = ByteArrayUtils.key_value_md5(keyBytes,valueBytes);
			out.write(checkSum,0,16);
			
			int k = out.size();
			if(k < length){
				out.write(new byte[length-k]);
			}
			
			writer.seek(offset);
			writer.write(out.toByteArray());
			return out.size();
		}else{
			throw new Exception("block is invalid.(length="+length+")");
		}
	}
	
	/**
	 * 将数据块中的标记是否为空数据的标记写入到数据文件中
	 * DataBlockLength(4B) + FreeFlag(1B) + LifeTime(8B) + IdleTime(8B)+hashcode(4B)+
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	void invalidateContainsDataFlag(RandomAccessFile writer) throws IOException{
		writer.seek(offset+4);
		writer.write(ByteArrayUtils.numberToByteArray(containsData?1:0,1));
	}
	
	/**
	 * 将数据块中的标记是否为空数据的标记写入到数据文件中
	 * DataBlockLength(4B) + FreeFlag(1B) + LifeTime(8B) + IdleTime(8B)+hashcode(4B)+
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	void invalidateIdleTime(RandomAccessFile writer) throws IOException{
		writer.seek(offset+4+1+8);
		writer.write(ByteArrayUtils.numberToByteArray(idletime,8));
	}

	public String toString(){
		return "DB{"+offset+","+length+","+this.containsData+","+(this.lifetime-System.currentTimeMillis())+","+(this.idletime-System.currentTimeMillis())+","+this.hashcode+"}";
	}
}
