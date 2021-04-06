package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器向客户端发送，通知客户端发生了某些事情，比如获得经验值，钱，升级了，任务完成了等等，客户端根据不同的事情，作不同的现实<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>eventType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>eventData</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class NOTIFY_EVENT_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte targetType;
	long targetId;
	byte eventType;
	long eventData;

	public NOTIFY_EVENT_REQ(){
	}

	public NOTIFY_EVENT_REQ(long seqNum,byte targetType,long targetId,byte eventType,long eventData){
		this.seqNum = seqNum;
		this.targetType = targetType;
		this.targetId = targetId;
		this.eventType = eventType;
		this.eventData = eventData;
	}

	public NOTIFY_EVENT_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		targetType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		targetId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		eventType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		eventData = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x000000D1;
	}

	public String getTypeDescription() {
		return "NOTIFY_EVENT_REQ";
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
		len += 1;
		len += 8;
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

			buffer.put(targetType);
			buffer.putLong(targetId);
			buffer.put(eventType);
			buffer.putLong(eventData);
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
	 *	0标识玩家，1标识怪，2表示宠物
	 */
	public byte getTargetType(){
		return targetType;
	}

	/**
	 * 设置属性：
	 *	0标识玩家，1标识怪，2表示宠物
	 */
	public void setTargetType(byte targetType){
		this.targetType = targetType;
	}

	/**
	 * 获取属性：
	 *	某个玩家Id或者某个怪的Id
	 */
	public long getTargetId(){
		return targetId;
	}

	/**
	 * 设置属性：
	 *	某个玩家Id或者某个怪的Id
	 */
	public void setTargetId(long targetId){
		this.targetId = targetId;
	}

	/**
	 * 获取属性：
	 *	事件类型，查看Constants类
	 */
	public byte getEventType(){
		return eventType;
	}

	/**
	 * 设置属性：
	 *	事件类型，查看Constants类
	 */
	public void setEventType(byte eventType){
		this.eventType = eventType;
	}

	/**
	 * 获取属性：
	 *	事件对应的数据
	 */
	public long getEventData(){
		return eventData;
	}

	/**
	 * 设置属性：
	 *	事件对应的数据
	 */
	public void setEventData(long eventData){
		this.eventData = eventData;
	}

}