package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器向客户端广播技能使用事件，限定为使用者广播区内的所有玩家，不包括使用者本人<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>casterType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>casterId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class TARGET_SKILL_BROADCAST_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte casterType;
	long casterId;
	byte targetType;
	long targetId;
	int skillId;
	byte level;

	public TARGET_SKILL_BROADCAST_REQ(){
	}

	public TARGET_SKILL_BROADCAST_REQ(long seqNum,byte casterType,long casterId,byte targetType,long targetId,int skillId,byte level){
		this.seqNum = seqNum;
		this.casterType = casterType;
		this.casterId = casterId;
		this.targetType = targetType;
		this.targetId = targetId;
		this.skillId = skillId;
		this.level = level;
	}

	public TARGET_SKILL_BROADCAST_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		casterType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		casterId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		targetType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		targetId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		skillId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		level = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x00000023;
	}

	public String getTypeDescription() {
		return "TARGET_SKILL_BROADCAST_REQ";
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

			buffer.put(casterType);
			buffer.putLong(casterId);
			buffer.put(targetType);
			buffer.putLong(targetId);
			buffer.putInt(skillId);
			buffer.put(level);
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
	 *	技能施放者的类型
	 */
	public byte getCasterType(){
		return casterType;
	}

	/**
	 * 设置属性：
	 *	技能施放者的类型
	 */
	public void setCasterType(byte casterType){
		this.casterType = casterType;
	}

	/**
	 * 获取属性：
	 *	技能施放者的标识
	 */
	public long getCasterId(){
		return casterId;
	}

	/**
	 * 设置属性：
	 *	技能施放者的标识
	 */
	public void setCasterId(long casterId){
		this.casterId = casterId;
	}

	/**
	 * 获取属性：
	 *	目标的类型
	 */
	public byte getTargetType(){
		return targetType;
	}

	/**
	 * 设置属性：
	 *	目标的类型
	 */
	public void setTargetType(byte targetType){
		this.targetType = targetType;
	}

	/**
	 * 获取属性：
	 *	目标的标识
	 */
	public long getTargetId(){
		return targetId;
	}

	/**
	 * 设置属性：
	 *	目标的标识
	 */
	public void setTargetId(long targetId){
		this.targetId = targetId;
	}

	/**
	 * 获取属性：
	 *	技能编号，范围是所有技能
	 */
	public int getSkillId(){
		return skillId;
	}

	/**
	 * 设置属性：
	 *	技能编号，范围是所有技能
	 */
	public void setSkillId(int skillId){
		this.skillId = skillId;
	}

	/**
	 * 获取属性：
	 *	技能的等级
	 */
	public byte getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	技能的等级
	 */
	public void setLevel(byte level){
		this.level = level;
	}

}