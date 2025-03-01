package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 用户进入服务器<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>onlineNum</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>offlineNum</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class USER_ENTER_SERVER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	long onlineNum;
	long offlineNum;
	long leftTime;

	public USER_ENTER_SERVER_RES(){
	}

	public USER_ENTER_SERVER_RES(long seqNum,byte result,long onlineNum,long offlineNum,long leftTime){
		this.seqNum = seqNum;
		this.result = result;
		this.onlineNum = onlineNum;
		this.offlineNum = offlineNum;
		this.leftTime = leftTime;
	}

	public USER_ENTER_SERVER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		onlineNum = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		offlineNum = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		leftTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x7000F012;
	}

	public String getTypeDescription() {
		return "USER_ENTER_SERVER_RES";
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

			buffer.put(result);
			buffer.putLong(onlineNum);
			buffer.putLong(offlineNum);
			buffer.putLong(leftTime);
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
	 *	进入服务器结果，0-可以直接进入，1-需要排队并且已经排队, 2-进入失败
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	进入服务器结果，0-可以直接进入，1-需要排队并且已经排队, 2-进入失败
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	在线排队的数量
	 */
	public long getOnlineNum(){
		return onlineNum;
	}

	/**
	 * 设置属性：
	 *	在线排队的数量
	 */
	public void setOnlineNum(long onlineNum){
		this.onlineNum = onlineNum;
	}

	/**
	 * 获取属性：
	 *	离线排队的数量
	 */
	public long getOfflineNum(){
		return offlineNum;
	}

	/**
	 * 设置属性：
	 *	离线排队的数量
	 */
	public void setOfflineNum(long offlineNum){
		this.offlineNum = offlineNum;
	}

	/**
	 * 获取属性：
	 *	预计进入时长(ms)
	 */
	public long getLeftTime(){
		return leftTime;
	}

	/**
	 * 设置属性：
	 *	预计进入时长(ms)
	 */
	public void setLeftTime(long leftTime){
		this.leftTime = leftTime;
	}

}