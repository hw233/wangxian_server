package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 领取限时任务奖励<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>state</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>takePrize</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>score</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class XL_TIMEDTASK_PRIZE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int taskId;
	byte state;
	boolean takePrize;
	int score;

	public XL_TIMEDTASK_PRIZE_RES(){
	}

	public XL_TIMEDTASK_PRIZE_RES(long seqNum,int taskId,byte state,boolean takePrize,int score){
		this.seqNum = seqNum;
		this.taskId = taskId;
		this.state = state;
		this.takePrize = takePrize;
		this.score = score;
	}

	public XL_TIMEDTASK_PRIZE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		taskId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		state = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		takePrize = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		score = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70FFF077;
	}

	public String getTypeDescription() {
		return "XL_TIMEDTASK_PRIZE_RES";
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
		len += 1;
		len += 1;
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

			buffer.putInt(taskId);
			buffer.put(state);
			buffer.put((byte)(takePrize==false?0:1));
			buffer.putInt(score);
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
	 *	限时任务id
	 */
	public int getTaskId(){
		return taskId;
	}

	/**
	 * 设置属性：
	 *	限时任务id
	 */
	public void setTaskId(int taskId){
		this.taskId = taskId;
	}

	/**
	 * 获取属性：
	 *	任务状态:0-未接取；1-已接取未完成；2-已完成；3-超时失效
	 */
	public byte getState(){
		return state;
	}

	/**
	 * 设置属性：
	 *	任务状态:0-未接取；1-已接取未完成；2-已完成；3-超时失效
	 */
	public void setState(byte state){
		this.state = state;
	}

	/**
	 * 获取属性：
	 *	是否已领奖
	 */
	public boolean getTakePrize(){
		return takePrize;
	}

	/**
	 * 设置属性：
	 *	是否已领奖
	 */
	public void setTakePrize(boolean takePrize){
		this.takePrize = takePrize;
	}

	/**
	 * 获取属性：
	 *	当前玩家积分
	 */
	public int getScore(){
		return score;
	}

	/**
	 * 设置属性：
	 *	当前玩家积分
	 */
	public void setScore(int score){
		this.score = score;
	}

}