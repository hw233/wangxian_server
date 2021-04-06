package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 喂养宠物查询<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNames[0]</td><td>String</td><td>articleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNames[1]</td><td>String</td><td>articleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNames[2]</td><td>String</td><td>articleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedType</td><td>byte[]</td><td>feedType.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedValue.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedValue</td><td>int[]</td><td>feedValue.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_PET_FEED_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] articleNames;
	byte[] feedType;
	int[] feedValue;

	public QUERY_PET_FEED_RES(){
	}

	public QUERY_PET_FEED_RES(long seqNum,String[] articleNames,byte[] feedType,int[] feedValue){
		this.seqNum = seqNum;
		this.articleNames = articleNames;
		this.feedType = feedType;
		this.feedValue = feedValue;
	}

	public QUERY_PET_FEED_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleNames = new String[len];
		for(int i = 0 ; i < articleNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			articleNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		feedType = new byte[len];
		System.arraycopy(content,offset,feedType,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		feedValue = new int[len];
		for(int i = 0 ; i < feedValue.length ; i++){
			feedValue[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x7000FF38;
	}

	public String getTypeDescription() {
		return "QUERY_PET_FEED_RES";
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
		len += 4;
		for(int i = 0 ; i < articleNames.length; i++){
			len += 2;
			try{
				len += articleNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += feedType.length;
		len += 4;
		len += feedValue.length * 4;
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

			buffer.putInt(articleNames.length);
			for(int i = 0 ; i < articleNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = articleNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(feedType.length);
			buffer.put(feedType);
			buffer.putInt(feedValue.length);
			for(int i = 0 ; i < feedValue.length; i++){
				buffer.putInt(feedValue[i]);
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
	 *	宠物能够使用的物品名
	 */
	public String[] getArticleNames(){
		return articleNames;
	}

	/**
	 * 设置属性：
	 *	宠物能够使用的物品名
	 */
	public void setArticleNames(String[] articleNames){
		this.articleNames = articleNames;
	}

	/**
	 * 获取属性：
	 *	喂养物品的类型，如加血类型，加快乐值，加寿命0 1 2 3 0血 1 快乐 2寿命 3经验
	 */
	public byte[] getFeedType(){
		return feedType;
	}

	/**
	 * 设置属性：
	 *	喂养物品的类型，如加血类型，加快乐值，加寿命0 1 2 3 0血 1 快乐 2寿命 3经验
	 */
	public void setFeedType(byte[] feedType){
		this.feedType = feedType;
	}

	/**
	 * 获取属性：
	 *	喂养物品所加的具体数值
	 */
	public int[] getFeedValue(){
		return feedValue;
	}

	/**
	 * 设置属性：
	 *	喂养物品所加的具体数值
	 */
	public void setFeedValue(int[] feedValue){
		this.feedValue = feedValue;
	}

}