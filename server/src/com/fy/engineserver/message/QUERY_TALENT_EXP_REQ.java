package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 喂养请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inputExp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeId</td><td>long[]</td><td>aeId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeNums</td><td>int[]</td><td>aeNums.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_TALENT_EXP_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	int feedType;
	long inputExp;
	long[] aeId;
	int[] aeNums;

	public QUERY_TALENT_EXP_REQ(){
	}

	public QUERY_TALENT_EXP_REQ(long seqNum,long playerId,int feedType,long inputExp,long[] aeId,int[] aeNums){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.feedType = feedType;
		this.inputExp = inputExp;
		this.aeId = aeId;
		this.aeNums = aeNums;
	}

	public QUERY_TALENT_EXP_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		feedType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		inputExp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeId = new long[len];
		for(int i = 0 ; i < aeId.length ; i++){
			aeId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeNums = new int[len];
		for(int i = 0 ; i < aeNums.length ; i++){
			aeNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x00FF0128;
	}

	public String getTypeDescription() {
		return "QUERY_TALENT_EXP_REQ";
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
		len += 8;
		len += 4;
		len += 8;
		len += 4;
		len += aeId.length * 8;
		len += 4;
		len += aeNums.length * 4;
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

			buffer.putLong(playerId);
			buffer.putInt(feedType);
			buffer.putLong(inputExp);
			buffer.putInt(aeId.length);
			for(int i = 0 ; i < aeId.length; i++){
				buffer.putLong(aeId[i]);
			}
			buffer.putInt(aeNums.length);
			for(int i = 0 ; i < aeNums.length; i++){
				buffer.putInt(aeNums[i]);
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
	 *	角色id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	类型
	 */
	public int getFeedType(){
		return feedType;
	}

	/**
	 * 设置属性：
	 *	类型
	 */
	public void setFeedType(int feedType){
		this.feedType = feedType;
	}

	/**
	 * 获取属性：
	 *	输入的经验
	 */
	public long getInputExp(){
		return inputExp;
	}

	/**
	 * 设置属性：
	 *	输入的经验
	 */
	public void setInputExp(long inputExp){
		this.inputExp = inputExp;
	}

	/**
	 * 获取属性：
	 *	道具id
	 */
	public long[] getAeId(){
		return aeId;
	}

	/**
	 * 设置属性：
	 *	道具id
	 */
	public void setAeId(long[] aeId){
		this.aeId = aeId;
	}

	/**
	 * 获取属性：
	 *	道具数量
	 */
	public int[] getAeNums(){
		return aeNums;
	}

	/**
	 * 设置属性：
	 *	道具数量
	 */
	public void setAeNums(int[] aeNums){
		this.aeNums = aeNums;
	}

}