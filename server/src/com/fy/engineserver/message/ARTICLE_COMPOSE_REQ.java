package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求物品合成<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleIds</td><td>long[]</td><td>articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleCounts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleCounts</td><td>int[]</td><td>articleCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>composeType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class ARTICLE_COMPOSE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] articleIds;
	int[] articleCounts;
	byte composeType;

	public ARTICLE_COMPOSE_REQ(){
	}

	public ARTICLE_COMPOSE_REQ(long seqNum,long[] articleIds,int[] articleCounts,byte composeType){
		this.seqNum = seqNum;
		this.articleIds = articleIds;
		this.articleCounts = articleCounts;
		this.composeType = composeType;
	}

	public ARTICLE_COMPOSE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleIds = new long[len];
		for(int i = 0 ; i < articleIds.length ; i++){
			articleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleCounts = new int[len];
		for(int i = 0 ; i < articleCounts.length ; i++){
			articleCounts[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		composeType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x000000FF;
	}

	public String getTypeDescription() {
		return "ARTICLE_COMPOSE_REQ";
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
		len += articleIds.length * 8;
		len += 4;
		len += articleCounts.length * 4;
		len += 1;
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

			buffer.putInt(articleIds.length);
			for(int i = 0 ; i < articleIds.length; i++){
				buffer.putLong(articleIds[i]);
			}
			buffer.putInt(articleCounts.length);
			for(int i = 0 ; i < articleCounts.length; i++){
				buffer.putInt(articleCounts[i]);
			}
			buffer.put(composeType);
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
	 *	物品实体的唯一标识
	 */
	public long[] getArticleIds(){
		return articleIds;
	}

	/**
	 * 设置属性：
	 *	物品实体的唯一标识
	 */
	public void setArticleIds(long[] articleIds){
		this.articleIds = articleIds;
	}

	/**
	 * 获取属性：
	 *	物品实体的个数
	 */
	public int[] getArticleCounts(){
		return articleCounts;
	}

	/**
	 * 设置属性：
	 *	物品实体的个数
	 */
	public void setArticleCounts(int[] articleCounts){
		this.articleCounts = articleCounts;
	}

	/**
	 * 获取属性：
	 *	合成类型，0表示使用金币合成，1表示使用元宝合成
	 */
	public byte getComposeType(){
		return composeType;
	}

	/**
	 * 设置属性：
	 *	合成类型，0表示使用金币合成，1表示使用元宝合成
	 */
	public void setComposeType(byte composeType){
		this.composeType = composeType;
	}

}