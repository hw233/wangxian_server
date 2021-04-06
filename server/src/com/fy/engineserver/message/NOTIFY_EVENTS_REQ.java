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
 * <tr bgcolor="#FAFAFA" align="center"><td>targetTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetTypes</td><td>byte[]</td><td>targetTypes.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetIds</td><td>long[]</td><td>targetIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>eventTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>eventTypes</td><td>byte[]</td><td>eventTypes.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>eventDatas.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>eventDatas</td><td>long[]</td><td>eventDatas.length</td><td>*</td></tr>
 * </table>
 */
public class NOTIFY_EVENTS_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte[] targetTypes;
	long[] targetIds;
	byte[] eventTypes;
	long[] eventDatas;

	public NOTIFY_EVENTS_REQ(){
	}

	public NOTIFY_EVENTS_REQ(long seqNum,byte[] targetTypes,long[] targetIds,byte[] eventTypes,long[] eventDatas){
		this.seqNum = seqNum;
		this.targetTypes = targetTypes;
		this.targetIds = targetIds;
		this.eventTypes = eventTypes;
		this.eventDatas = eventDatas;
	}

	public NOTIFY_EVENTS_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		targetTypes = new byte[len];
		System.arraycopy(content,offset,targetTypes,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		targetIds = new long[len];
		for(int i = 0 ; i < targetIds.length ; i++){
			targetIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		eventTypes = new byte[len];
		System.arraycopy(content,offset,eventTypes,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		eventDatas = new long[len];
		for(int i = 0 ; i < eventDatas.length ; i++){
			eventDatas[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x000000DF;
	}

	public String getTypeDescription() {
		return "NOTIFY_EVENTS_REQ";
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
		len += targetTypes.length;
		len += 4;
		len += targetIds.length * 8;
		len += 4;
		len += eventTypes.length;
		len += 4;
		len += eventDatas.length * 8;
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

			buffer.putInt(targetTypes.length);
			buffer.put(targetTypes);
			buffer.putInt(targetIds.length);
			for(int i = 0 ; i < targetIds.length; i++){
				buffer.putLong(targetIds[i]);
			}
			buffer.putInt(eventTypes.length);
			buffer.put(eventTypes);
			buffer.putInt(eventDatas.length);
			for(int i = 0 ; i < eventDatas.length; i++){
				buffer.putLong(eventDatas[i]);
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
	 *	0标识玩家，1标识怪，2表示宠物
	 */
	public byte[] getTargetTypes(){
		return targetTypes;
	}

	/**
	 * 设置属性：
	 *	0标识玩家，1标识怪，2表示宠物
	 */
	public void setTargetTypes(byte[] targetTypes){
		this.targetTypes = targetTypes;
	}

	/**
	 * 获取属性：
	 *	某个玩家Id或者某个怪的Id
	 */
	public long[] getTargetIds(){
		return targetIds;
	}

	/**
	 * 设置属性：
	 *	某个玩家Id或者某个怪的Id
	 */
	public void setTargetIds(long[] targetIds){
		this.targetIds = targetIds;
	}

	/**
	 * 获取属性：
	 *	事件类型，查看Constants类
	 */
	public byte[] getEventTypes(){
		return eventTypes;
	}

	/**
	 * 设置属性：
	 *	事件类型，查看Constants类
	 */
	public void setEventTypes(byte[] eventTypes){
		this.eventTypes = eventTypes;
	}

	/**
	 * 获取属性：
	 *	事件对应的数据
	 */
	public long[] getEventDatas(){
		return eventDatas;
	}

	/**
	 * 设置属性：
	 *	事件对应的数据
	 */
	public void setEventDatas(long[] eventDatas){
		this.eventDatas = eventDatas;
	}

}