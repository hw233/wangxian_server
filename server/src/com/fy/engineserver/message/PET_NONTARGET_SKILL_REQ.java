package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端通知服务器，宠物无目标地施放技能<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetX</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetY</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetType</td><td>byte[]</td><td>targetType.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetId</td><td>long[]</td><td>targetId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillId</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>direction</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PET_NONTARGET_SKILL_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	short targetX;
	short targetY;
	byte[] targetType;
	long[] targetId;
	short skillId;
	byte direction;

	public PET_NONTARGET_SKILL_REQ(){
	}

	public PET_NONTARGET_SKILL_REQ(long seqNum,short targetX,short targetY,byte[] targetType,long[] targetId,short skillId,byte direction){
		this.seqNum = seqNum;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetType = targetType;
		this.targetId = targetId;
		this.skillId = skillId;
		this.direction = direction;
	}

	public PET_NONTARGET_SKILL_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		targetX = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		targetY = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		targetType = new byte[len];
		System.arraycopy(content,offset,targetType,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		targetId = new long[len];
		for(int i = 0 ; i < targetId.length ; i++){
			targetId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		skillId = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		direction = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x0000FF14;
	}

	public String getTypeDescription() {
		return "PET_NONTARGET_SKILL_REQ";
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
		len += 2;
		len += 2;
		len += 4;
		len += targetType.length;
		len += 4;
		len += targetId.length * 8;
		len += 2;
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

			buffer.putShort(targetX);
			buffer.putShort(targetY);
			buffer.putInt(targetType.length);
			buffer.put(targetType);
			buffer.putInt(targetId.length);
			for(int i = 0 ; i < targetId.length; i++){
				buffer.putLong(targetId[i]);
			}
			buffer.putShort(skillId);
			buffer.put(direction);
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
	 *	技能施放目标的X坐标
	 */
	public short getTargetX(){
		return targetX;
	}

	/**
	 * 设置属性：
	 *	技能施放目标的X坐标
	 */
	public void setTargetX(short targetX){
		this.targetX = targetX;
	}

	/**
	 * 获取属性：
	 *	技能施放目标的Y坐标
	 */
	public short getTargetY(){
		return targetY;
	}

	/**
	 * 设置属性：
	 *	技能施放目标的Y坐标
	 */
	public void setTargetY(short targetY){
		this.targetY = targetY;
	}

	/**
	 * 获取属性：
	 *	为了让服务器和客户端在击中目标保持一致，因此无目标技能也可以带上目标，可以传0个目标，表示周围没有目标
	 */
	public byte[] getTargetType(){
		return targetType;
	}

	/**
	 * 设置属性：
	 *	为了让服务器和客户端在击中目标保持一致，因此无目标技能也可以带上目标，可以传0个目标，表示周围没有目标
	 */
	public void setTargetType(byte[] targetType){
		this.targetType = targetType;
	}

	/**
	 * 获取属性：
	 *	为了让服务器和客户端在击中目标保持一致，因此无目标技能也可以带上目标，可以传0个目标，表示周围没有目标
	 */
	public long[] getTargetId(){
		return targetId;
	}

	/**
	 * 设置属性：
	 *	为了让服务器和客户端在击中目标保持一致，因此无目标技能也可以带上目标，可以传0个目标，表示周围没有目标
	 */
	public void setTargetId(long[] targetId){
		this.targetId = targetId;
	}

	/**
	 * 获取属性：
	 *	技能的Id
	 */
	public short getSkillId(){
		return skillId;
	}

	/**
	 * 设置属性：
	 *	技能的Id
	 */
	public void setSkillId(short skillId){
		this.skillId = skillId;
	}

	/**
	 * 获取属性：
	 *	释放技能的生物朝向
	 */
	public byte getDirection(){
		return direction;
	}

	/**
	 * 设置属性：
	 *	释放技能的生物朝向
	 */
	public void setDirection(byte direction){
		this.direction = direction;
	}

}