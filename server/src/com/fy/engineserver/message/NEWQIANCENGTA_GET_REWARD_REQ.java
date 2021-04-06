package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 领取奖励<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nanduType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isOver</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>daoIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cengIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class NEWQIANCENGTA_GET_REWARD_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	int nanduType;
	boolean isOver;
	int daoIndex;
	int cengIndex;

	public NEWQIANCENGTA_GET_REWARD_REQ(){
	}

	public NEWQIANCENGTA_GET_REWARD_REQ(long seqNum,long playerId,int nanduType,boolean isOver,int daoIndex,int cengIndex){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.nanduType = nanduType;
		this.isOver = isOver;
		this.daoIndex = daoIndex;
		this.cengIndex = cengIndex;
	}

	public NEWQIANCENGTA_GET_REWARD_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		nanduType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		isOver = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		daoIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		cengIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0F710006;
	}

	public String getTypeDescription() {
		return "NEWQIANCENGTA_GET_REWARD_REQ";
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
		len += 1;
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

			buffer.putLong(playerId);
			buffer.putInt(nanduType);
			buffer.put((byte)(isOver==false?0:1));
			buffer.putInt(daoIndex);
			buffer.putInt(cengIndex);
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
	 *	玩家ID
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	玩家ID
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	难度Type 0是普通 1是困难 2是深渊
	 */
	public int getNanduType(){
		return nanduType;
	}

	/**
	 * 设置属性：
	 *	难度Type 0是普通 1是困难 2是深渊
	 */
	public void setNanduType(int nanduType){
		this.nanduType = nanduType;
	}

	/**
	 * 获取属性：
	 *	是否是手动爬塔的领取，如果是就不会返回NEWQIANCENGTA_REWARD_INFO_REQ
	 */
	public boolean getIsOver(){
		return isOver;
	}

	/**
	 * 设置属性：
	 *	是否是手动爬塔的领取，如果是就不会返回NEWQIANCENGTA_REWARD_INFO_REQ
	 */
	public void setIsOver(boolean isOver){
		this.isOver = isOver;
	}

	/**
	 * 获取属性：
	 *	道的下标，从0开始
	 */
	public int getDaoIndex(){
		return daoIndex;
	}

	/**
	 * 设置属性：
	 *	道的下标，从0开始
	 */
	public void setDaoIndex(int daoIndex){
		this.daoIndex = daoIndex;
	}

	/**
	 * 获取属性：
	 *	层的下表， 从0开始， -100为全部领取
	 */
	public int getCengIndex(){
		return cengIndex;
	}

	/**
	 * 设置属性：
	 *	层的下表， 从0开始， -100为全部领取
	 */
	public void setCengIndex(int cengIndex){
		this.cengIndex = cengIndex;
	}

}