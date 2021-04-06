package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端，增加或者去除一个临时敌人<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>action</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class TRANSIENTENEMY_CHANGE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte action;
	byte targetType;
	long targetId;

	public TRANSIENTENEMY_CHANGE_REQ(){
	}

	public TRANSIENTENEMY_CHANGE_REQ(long seqNum,byte action,byte targetType,long targetId){
		this.seqNum = seqNum;
		this.action = action;
		this.targetType = targetType;
		this.targetId = targetId;
	}

	public TRANSIENTENEMY_CHANGE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		action = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		targetType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		targetId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x00F0EEFF;
	}

	public String getTypeDescription() {
		return "TRANSIENTENEMY_CHANGE_REQ";
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

			buffer.put(action);
			buffer.put(targetType);
			buffer.putLong(targetId);
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
	 *	0标识增加一个敌人，1标识删除一个敌人
	 */
	public byte getAction(){
		return action;
	}

	/**
	 * 设置属性：
	 *	0标识增加一个敌人，1标识删除一个敌人
	 */
	public void setAction(byte action){
		this.action = action;
	}

	/**
	 * 获取属性：
	 *	0标识玩家，1标识NPC
	 */
	public byte getTargetType(){
		return targetType;
	}

	/**
	 * 设置属性：
	 *	0标识玩家，1标识NPC
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

}