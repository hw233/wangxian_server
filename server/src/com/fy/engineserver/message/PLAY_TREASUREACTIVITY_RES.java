package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 挖取宝藏<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>treasureId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>takeArticleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>takeArticleIds</td><td>long[]</td><td>takeArticleIds.length</td><td>*</td></tr>
 * </table>
 */
public class PLAY_TREASUREACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int treasureId;
	long[] takeArticleIds;

	public PLAY_TREASUREACTIVITY_RES(){
	}

	public PLAY_TREASUREACTIVITY_RES(long seqNum,int treasureId,long[] takeArticleIds){
		this.seqNum = seqNum;
		this.treasureId = treasureId;
		this.takeArticleIds = takeArticleIds;
	}

	public PLAY_TREASUREACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		treasureId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		takeArticleIds = new long[len];
		for(int i = 0 ; i < takeArticleIds.length ; i++){
			takeArticleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70FF0078;
	}

	public String getTypeDescription() {
		return "PLAY_TREASUREACTIVITY_RES";
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
		len += 4;
		len += takeArticleIds.length * 8;
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

			buffer.putInt(treasureId);
			buffer.putInt(takeArticleIds.length);
			for(int i = 0 ; i < takeArticleIds.length; i++){
				buffer.putLong(takeArticleIds[i]);
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
	 *	宝藏id
	 */
	public int getTreasureId(){
		return treasureId;
	}

	/**
	 * 设置属性：
	 *	宝藏id
	 */
	public void setTreasureId(int treasureId){
		this.treasureId = treasureId;
	}

	/**
	 * 获取属性：
	 *	挖取到的物品id列表
	 */
	public long[] getTakeArticleIds(){
		return takeArticleIds;
	}

	/**
	 * 设置属性：
	 *	挖取到的物品id列表
	 */
	public void setTakeArticleIds(long[] takeArticleIds){
		this.takeArticleIds = takeArticleIds;
	}

}