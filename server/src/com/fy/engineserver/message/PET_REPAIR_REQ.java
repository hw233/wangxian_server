package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 宠物炼化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>force</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>flushLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>flushType</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class PET_REPAIR_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long petId;
	boolean force;
	int flushLevel;
	boolean flushType;

	public PET_REPAIR_REQ(){
	}

	public PET_REPAIR_REQ(long seqNum,long petId,boolean force,int flushLevel,boolean flushType){
		this.seqNum = seqNum;
		this.petId = petId;
		this.force = force;
		this.flushLevel = flushLevel;
		this.flushType = flushType;
	}

	public PET_REPAIR_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		petId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		force = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		flushLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		flushType = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x0000FF63;
	}

	public String getTypeDescription() {
		return "PET_REPAIR_REQ";
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

			buffer.putLong(petId);
			buffer.put((byte)(force==false?0:1));
			buffer.putInt(flushLevel);
			buffer.put((byte)(flushType==false?0:1));
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
	 *	宠物id
	 */
	public long getPetId(){
		return petId;
	}

	/**
	 * 设置属性：
	 *	宠物id
	 */
	public void setPetId(long petId){
		this.petId = petId;
	}

	/**
	 * 获取属性：
	 *	强制刷新(true),false为查看(下边不做处理)
	 */
	public boolean getForce(){
		return force;
	}

	/**
	 * 设置属性：
	 *	强制刷新(true),false为查看(下边不做处理)
	 */
	public void setForce(boolean force){
		this.force = force;
	}

	/**
	 * 获取属性：
	 *	刷新级别(初,中,高)
	 */
	public int getFlushLevel(){
		return flushLevel;
	}

	/**
	 * 设置属性：
	 *	刷新级别(初,中,高)
	 */
	public void setFlushLevel(int flushLevel){
		this.flushLevel = flushLevel;
	}

	/**
	 * 获取属性：
	 *	用钱还是用道具刷新(true银子，false道具)
	 */
	public boolean getFlushType(){
		return flushType;
	}

	/**
	 * 设置属性：
	 *	用钱还是用道具刷新(true银子，false道具)
	 */
	public void setFlushType(boolean flushType){
		this.flushType = flushType;
	}

}