package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 收获<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>action</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CAVE_HARVEST_PLANT_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long npcId;
	byte action;

	public CAVE_HARVEST_PLANT_REQ(){
	}

	public CAVE_HARVEST_PLANT_REQ(long seqNum,long npcId,byte action){
		this.seqNum = seqNum;
		this.npcId = npcId;
		this.action = action;
	}

	public CAVE_HARVEST_PLANT_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		npcId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		action = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x0F000008;
	}

	public String getTypeDescription() {
		return "CAVE_HARVEST_PLANT_REQ";
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

			buffer.putLong(npcId);
			buffer.put(action);
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
	 *	要种植的土地对应的NPCID
	 */
	public long getNpcId(){
		return npcId;
	}

	/**
	 * 设置属性：
	 *	要种植的土地对应的NPCID
	 */
	public void setNpcId(long npcId){
		this.npcId = npcId;
	}

	/**
	 * 获取属性：
	 *	操作类型 0-自己摘取 ,1-偷菜
	 */
	public byte getAction(){
		return action;
	}

	/**
	 * 设置属性：
	 *	操作类型 0-自己摘取 ,1-偷菜
	 */
	public void setAction(byte action){
		this.action = action;
	}

}