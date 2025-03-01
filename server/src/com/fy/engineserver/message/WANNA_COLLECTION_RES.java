package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 想要采集物品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canCollection</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>collectionBarTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>barDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>barDes</td><td>String</td><td>barDes.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class WANNA_COLLECTION_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean canCollection;
	long collectionBarTime;
	long npcId;
	String barDes;

	public WANNA_COLLECTION_RES(){
	}

	public WANNA_COLLECTION_RES(long seqNum,boolean canCollection,long collectionBarTime,long npcId,String barDes){
		this.seqNum = seqNum;
		this.canCollection = canCollection;
		this.collectionBarTime = collectionBarTime;
		this.npcId = npcId;
		this.barDes = barDes;
	}

	public WANNA_COLLECTION_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		canCollection = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		collectionBarTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		npcId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		barDes = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x70000FAA;
	}

	public String getTypeDescription() {
		return "WANNA_COLLECTION_RES";
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
		len += 8;
		len += 8;
		len += 2;
		try{
			len +=barDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.put((byte)(canCollection==false?0:1));
			buffer.putLong(collectionBarTime);
			buffer.putLong(npcId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = barDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	是否可采集
	 */
	public boolean getCanCollection(){
		return canCollection;
	}

	/**
	 * 设置属性：
	 *	是否可采集
	 */
	public void setCanCollection(boolean canCollection){
		this.canCollection = canCollection;
	}

	/**
	 * 获取属性：
	 *	采集条时间
	 */
	public long getCollectionBarTime(){
		return collectionBarTime;
	}

	/**
	 * 设置属性：
	 *	采集条时间
	 */
	public void setCollectionBarTime(long collectionBarTime){
		this.collectionBarTime = collectionBarTime;
	}

	/**
	 * 获取属性：
	 *	要采集的NPCID
	 */
	public long getNpcId(){
		return npcId;
	}

	/**
	 * 设置属性：
	 *	要采集的NPCID
	 */
	public void setNpcId(long npcId){
		this.npcId = npcId;
	}

	/**
	 * 获取属性：
	 *	读条显示的文字
	 */
	public String getBarDes(){
		return barDes;
	}

	/**
	 * 设置属性：
	 *	读条显示的文字
	 */
	public void setBarDes(String barDes){
		this.barDes = barDes;
	}

}