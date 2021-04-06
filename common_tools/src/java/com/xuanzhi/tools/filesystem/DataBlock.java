package com.xuanzhi.tools.filesystem;

import java.io.*;

/**
 * 数据块：数据文件被拆分为多个数据块，每个数据块的大小任意，同时每个数据块中只存放一个(key,value)。
 * 数据块包括如下信息：
 * 		数据块在数据文件中的位置
 * 		数据块的大小
 * 		数据块是否包含数据（也就是(key,value)对）
 * 		数据块对应数据key的reserve
 * 		数据块对应数据key
 * 		数据块对应数据value
 * 		数据块对应数据的校验码
 * 
 * 数据块的存储格式：
 * 
 *  DataBlockLength(4B) + FreeFlag(1B) + reserve(4B)+ version(4B)
 *  KeyLength(4B)+Key+
 *  ValueLenth(4B)+Value+ CheckSum(16B)+Zero
 */
public class DataBlock {
	public final static int BAD_BLOCK_FLAG = 999999;
	public final static int MIN_BLOCK_SIZE = 512;
	
	public final static int DROP_BLOCK_FLAG = -888999;
	public final static int DROP_BLOCK_VERSION = -777777777;
	
	protected final static int HEADER_SIZE = 4 + 1 + 4 + 4 + 4 + 4 + 16;

	
	//数据块在数据文件中的位置
	public long offset = 0;
	
	//数据块的大小
	public int length = 0;
	
	//数据块是否包含数据（也就是(key,value)对）
	public boolean containsData = false;
	
	//数据块对应数据key的reserve
	public int reserve = 0;
	
	public int version;
	
	//对应的数据
	public String fileName = null;
	public byte[] fileContent = null;
	public int keyLength=0;
	public int valueLength=0;
	
	//文件中前一个数据块
	DataBlock previous;
	
	//文件中后一个数据块
	DataBlock next;
	
	DefaultVirtualFileSystem ddc;
	
	public DataBlock(DefaultVirtualFileSystem ddc){
		this.ddc = ddc;
	}
	
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
	
	String getFileName(RandomAccessFile reader) throws Exception{
		if(containsData == false) return null;
		if(fileName != null) return fileName;
		readAllData(reader);
		return fileName;
	}
	
	byte[] getValue(RandomAccessFile reader) throws Exception{
		if(containsData == false) return null;
		if(fileContent != null) return fileContent;
		readAllData(reader);
		return fileContent;
	}
	
	/**
	 * 读取数据块头部信息
	 * DataBlockLength(4B) + FreeFlag(1B) + reserve(4B)+ version(4B)
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	void readHeader(RandomAccessFile reader)throws Exception{
		byte bytes[] = new byte[4 + 1 + 4 + 4 + 4];
		reader.seek(offset);
		reader.read(bytes);
		int of = 0;
		this.length = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+=4;
		this.containsData = ((int)ByteArrayUtils.byteArrayToNumber(bytes,of,1)!=0);
		of+=1;
		this.reserve = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+= 4;
		this.version = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+= 4;
		this.keyLength = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+=4;
		
		if(keyLength > 0){
			bytes = new byte[keyLength];
			reader.read(bytes);
			fileName = new String(bytes);
 		}
	}
	
	/**
	 * 读取数据块
	 * DataBlockLength(4B) + FreeFlag(1B) + reserve(4B)+ version(4b)
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
		int _reserve = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
		of+= 4;
		int _version = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
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
		
		if(_version != version){
			String s = "format error block reserve of memory("+version+") and disk("+_version+") not same.";
			ddc.reportBadBlock(this,2);
			//free this block
			throw new Exception(s);
		}
		
		
		int kl = this.keyLength;
		int vl = this.valueLength;
		if(fileName == null) kl = 0;
		if(fileContent == null) vl = 0;
		try{
			this.keyLength = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
			of+=4;
			fileName = new String(bytes,of,this.keyLength);
			of+=keyLength;
			this.valueLength = (int)ByteArrayUtils.byteArrayToNumber(bytes,of,4);
			of+=4;
			fileContent = new byte[valueLength]; 
			System.arraycopy(bytes, of, fileContent, 0, valueLength);
			of+=valueLength;
			
			int memoryChange = this.valueLength - vl;
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
	 * DataBlockLength(4B) + FreeFlag(1B) +reserve(4B)+ version(4b)
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	int invalidateAllData(RandomAccessFile writer) throws Exception{
		if(checkValid()){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(ByteArrayUtils.numberToByteArray(length,4));
			out.write(ByteArrayUtils.numberToByteArray(containsData?1:0,1));
			out.write(ByteArrayUtils.numberToByteArray(reserve,4));
			out.write(ByteArrayUtils.numberToByteArray(version,4));
			
			byte bytes1[] = null;
			if(fileName != null){
				bytes1 = fileName.getBytes();
				keyLength = bytes1.length;
			}else{
				bytes1 = new byte[0];
			}
			keyLength = bytes1.length;
			out.write(ByteArrayUtils.numberToByteArray(keyLength,4));
			out.write(bytes1);
			
			byte bytes2[] = null;
			if(fileContent != null){
				bytes2 = fileContent;
			}else{
				bytes2 = new byte[0];
			}
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
			writer.write(out.toByteArray(),0,length);
			return out.size();
		}else{
			throw new Exception("block is invalid.(length="+length+")");
		}
	}
	
	/**
	 * 将数据块中的所有数据写入到数据文件中
	 * DataBlockLength(4B) + FreeFlag(1B)+reserve(4B)+version
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	int invalidateHeaderData(RandomAccessFile writer) throws Exception{
		if(checkValid()){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(ByteArrayUtils.numberToByteArray(length,4));
			out.write(ByteArrayUtils.numberToByteArray(containsData?1:0,1));
			out.write(ByteArrayUtils.numberToByteArray(reserve,4));
			out.write(ByteArrayUtils.numberToByteArray(version,4));
			if(fileName != null){
				byte bytes[] = fileName.getBytes();
				out.write(ByteArrayUtils.numberToByteArray(bytes.length,4));
				out.write(bytes);
			}else{
				out.write(ByteArrayUtils.numberToByteArray(0,4));
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
	 * DataBlockLength(4B) + FreeFlag(1B)+reserve(4B)+ version(4b)
	 *  KeyLength(4B)+Key+ValueLenth(4B)+Value+ CheckSum(16B)+Zero
	 * @param writer
	 */
	void invalidateContainsDataFlag(RandomAccessFile writer) throws IOException{
		writer.seek(offset+4);
		writer.write(ByteArrayUtils.numberToByteArray(containsData?1:0,1));
	}


	public String toString(){
		return "DB{"+offset+","+length+","+this.containsData+","+","+this.reserve+","+fileName+",version="+version+"}";
	}
}
