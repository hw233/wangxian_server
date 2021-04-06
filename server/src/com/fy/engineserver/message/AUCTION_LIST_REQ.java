package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询拍卖<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>reqType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mainType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mainType</td><td>String</td><td>mainType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>subType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>subType</td><td>String</td><td>subType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxlevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>color</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>startNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>reqLen</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class AUCTION_LIST_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte reqType;
	String mainType;
	String subType;
	int level;
	int maxlevel;
	int color;
	String name;
	int startNum;
	int reqLen;

	public AUCTION_LIST_REQ(){
	}

	public AUCTION_LIST_REQ(long seqNum,byte reqType,String mainType,String subType,int level,int maxlevel,int color,String name,int startNum,int reqLen){
		this.seqNum = seqNum;
		this.reqType = reqType;
		this.mainType = mainType;
		this.subType = subType;
		this.level = level;
		this.maxlevel = maxlevel;
		this.color = color;
		this.name = name;
		this.startNum = startNum;
		this.reqLen = reqLen;
	}

	public AUCTION_LIST_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		reqType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		mainType = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		subType = new String(content,offset,len,"UTF-8");
		offset += len;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxlevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		color = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		startNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		reqLen = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000D001;
	}

	public String getTypeDescription() {
		return "AUCTION_LIST_REQ";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}

	public long getSequnceNum(){
		return seqNum;
	}

	private int packet_length = 0;

	public int getLength() {
		if(packet_length > 0) return packet_length;
		int len =  mf.getNumOfByteForMessageLength() + 4 + 4;
		len += 1;
		len += 2;
		try{
			len +=mainType.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=subType.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		int oldPos = buffer.position();
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.put(reqType);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = mainType.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = subType.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(level);
			buffer.putInt(maxlevel);
			buffer.putInt(color);
				try{
			tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(startNum);
			buffer.putInt(reqLen);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		int newPos = buffer.position();
		buffer.position(oldPos);
		buffer.put(mf.numberToByteArray(newPos-oldPos,mf.getNumOfByteForMessageLength()));
		buffer.position(newPos);
		return newPos-oldPos;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public byte getReqType(){
		return reqType;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setReqType(byte reqType){
		this.reqType = reqType;
	}

	/**
	 * 获取属性：
	 *	第一分类，为空字符代表不需要做条件
	 */
	public String getMainType(){
		return mainType;
	}

	/**
	 * 设置属性：
	 *	第一分类，为空字符代表不需要做条件
	 */
	public void setMainType(String mainType){
		this.mainType = mainType;
	}

	/**
	 * 获取属性：
	 *	第二分类，为空字符代表不需要做条件
	 */
	public String getSubType(){
		return subType;
	}

	/**
	 * 设置属性：
	 *	第二分类，为空字符代表不需要做条件
	 */
	public void setSubType(String subType){
		this.subType = subType;
	}

	/**
	 * 获取属性：
	 *	等级，为-1代表不需要做条件
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	等级，为-1代表不需要做条件
	 */
	public void setLevel(int level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	最高等级，为-1代表不需要做条件
	 */
	public int getMaxlevel(){
		return maxlevel;
	}

	/**
	 * 设置属性：
	 *	最高等级，为-1代表不需要做条件
	 */
	public void setMaxlevel(int maxlevel){
		this.maxlevel = maxlevel;
	}

	/**
	 * 获取属性：
	 *	品质, 为-1代表不需要做条件
	 */
	public int getColor(){
		return color;
	}

	/**
	 * 设置属性：
	 *	品质, 为-1代表不需要做条件
	 */
	public void setColor(int color){
		this.color = color;
	}

	/**
	 * 获取属性：
	 *	名称，为空字符代表不需要做条件
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	名称，为空字符代表不需要做条件
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	查询起始个数
	 */
	public int getStartNum(){
		return startNum;
	}

	/**
	 * 设置属性：
	 *	查询起始个数
	 */
	public void setStartNum(int startNum){
		this.startNum = startNum;
	}

	/**
	 * 获取属性：
	 *	查询个数
	 */
	public int getReqLen(){
		return reqLen;
	}

	/**
	 * 设置属性：
	 *	查询个数
	 */
	public void setReqLen(int reqLen){
		this.reqLen = reqLen;
	}

}