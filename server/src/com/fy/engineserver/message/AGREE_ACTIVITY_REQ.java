package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 被邀请方同意活动<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>successNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class AGREE_ACTIVITY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long activityId;
	byte activityType;
	int successNum;
	int level;

	public AGREE_ACTIVITY_REQ(){
	}

	public AGREE_ACTIVITY_REQ(long seqNum,long activityId,byte activityType,int successNum,int level){
		this.seqNum = seqNum;
		this.activityId = activityId;
		this.activityType = activityType;
		this.successNum = successNum;
		this.level = level;
	}

	public AGREE_ACTIVITY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		activityId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		activityType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		successNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x000F0004;
	}

	public String getTypeDescription() {
		return "AGREE_ACTIVITY_REQ";
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

			buffer.putLong(activityId);
			buffer.put(activityType);
			buffer.putInt(successNum);
			buffer.putInt(level);
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
	 *	活动id
	 */
	public long getActivityId(){
		return activityId;
	}

	/**
	 * 设置属性：
	 *	活动id
	 */
	public void setActivityId(long activityId){
		this.activityId = activityId;
	}

	/**
	 * 获取属性：
	 *	国内仙缘(0),国外仙缘(1),国内论道(2),国外论道(3)
	 */
	public byte getActivityType(){
		return activityType;
	}

	/**
	 * 设置属性：
	 *	国内仙缘(0),国外仙缘(1),国内论道(2),国外论道(3)
	 */
	public void setActivityType(byte activityType){
		this.activityType = activityType;
	}

	/**
	 * 获取属性：
	 *	接受成功次数
	 */
	public int getSuccessNum(){
		return successNum;
	}

	/**
	 * 设置属性：
	 *	接受成功次数
	 */
	public void setSuccessNum(int successNum){
		this.successNum = successNum;
	}

	/**
	 * 获取属性：
	 *	邀请活动的级别
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	邀请活动的级别
	 */
	public void setLevel(int level){
		this.level = level;
	}

}