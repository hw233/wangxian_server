package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 家族仓库<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isFengpei</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entityIDs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityIDs</td><td>long[]</td><td>entityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entityNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityNums</td><td>int[]</td><td>entityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuPNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuPNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuPNames[0]</td><td>String</td><td>jiazuPNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuPNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuPNames[1]</td><td>String</td><td>jiazuPNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuPNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuPNames[2]</td><td>String</td><td>jiazuPNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class JIAZU_WAREHOUSE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean isFengpei;
	int maxNum;
	long[] entityIDs;
	int[] entityNums;
	String[] jiazuPNames;

	public JIAZU_WAREHOUSE_RES(){
	}

	public JIAZU_WAREHOUSE_RES(long seqNum,boolean isFengpei,int maxNum,long[] entityIDs,int[] entityNums,String[] jiazuPNames){
		this.seqNum = seqNum;
		this.isFengpei = isFengpei;
		this.maxNum = maxNum;
		this.entityIDs = entityIDs;
		this.entityNums = entityNums;
		this.jiazuPNames = jiazuPNames;
	}

	public JIAZU_WAREHOUSE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		isFengpei = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		maxNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		entityIDs = new long[len];
		for(int i = 0 ; i < entityIDs.length ; i++){
			entityIDs[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		entityNums = new int[len];
		for(int i = 0 ; i < entityNums.length ; i++){
			entityNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazuPNames = new String[len];
		for(int i = 0 ; i < jiazuPNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuPNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x700AEE2C;
	}

	public String getTypeDescription() {
		return "JIAZU_WAREHOUSE_RES";
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
		len += 4;
		len += 4;
		len += entityIDs.length * 8;
		len += 4;
		len += entityNums.length * 4;
		len += 4;
		for(int i = 0 ; i < jiazuPNames.length; i++){
			len += 2;
			try{
				len += jiazuPNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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

			buffer.put((byte)(isFengpei==false?0:1));
			buffer.putInt(maxNum);
			buffer.putInt(entityIDs.length);
			for(int i = 0 ; i < entityIDs.length; i++){
				buffer.putLong(entityIDs[i]);
			}
			buffer.putInt(entityNums.length);
			for(int i = 0 ; i < entityNums.length; i++){
				buffer.putInt(entityNums[i]);
			}
			buffer.putInt(jiazuPNames.length);
			for(int i = 0 ; i < jiazuPNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = jiazuPNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
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
	 *	是否有权限分配
	 */
	public boolean getIsFengpei(){
		return isFengpei;
	}

	/**
	 * 设置属性：
	 *	是否有权限分配
	 */
	public void setIsFengpei(boolean isFengpei){
		this.isFengpei = isFengpei;
	}

	/**
	 * 获取属性：
	 *	最大数量
	 */
	public int getMaxNum(){
		return maxNum;
	}

	/**
	 * 设置属性：
	 *	最大数量
	 */
	public void setMaxNum(int maxNum){
		this.maxNum = maxNum;
	}

	/**
	 * 获取属性：
	 *	物品ID
	 */
	public long[] getEntityIDs(){
		return entityIDs;
	}

	/**
	 * 设置属性：
	 *	物品ID
	 */
	public void setEntityIDs(long[] entityIDs){
		this.entityIDs = entityIDs;
	}

	/**
	 * 获取属性：
	 *	物品个数
	 */
	public int[] getEntityNums(){
		return entityNums;
	}

	/**
	 * 设置属性：
	 *	物品个数
	 */
	public void setEntityNums(int[] entityNums){
		this.entityNums = entityNums;
	}

	/**
	 * 获取属性：
	 *	家族成员名称
	 */
	public String[] getJiazuPNames(){
		return jiazuPNames;
	}

	/**
	 * 设置属性：
	 *	家族成员名称
	 */
	public void setJiazuPNames(String[] jiazuPNames){
		this.jiazuPNames = jiazuPNames;
	}

}