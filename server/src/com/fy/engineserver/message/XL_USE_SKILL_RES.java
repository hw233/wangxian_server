package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 使用技能<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cdTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>num</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class XL_USE_SKILL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int skillId;
	int cdTime;
	int num;

	public XL_USE_SKILL_RES(){
	}

	public XL_USE_SKILL_RES(long seqNum,int skillId,int cdTime,int num){
		this.seqNum = seqNum;
		this.skillId = skillId;
		this.cdTime = cdTime;
		this.num = num;
	}

	public XL_USE_SKILL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		skillId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		cdTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		num = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70FFF085;
	}

	public String getTypeDescription() {
		return "XL_USE_SKILL_RES";
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

			buffer.putInt(skillId);
			buffer.putInt(cdTime);
			buffer.putInt(num);
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
	 *	使用技能id
	 */
	public int getSkillId(){
		return skillId;
	}

	/**
	 * 设置属性：
	 *	使用技能id
	 */
	public void setSkillId(int skillId){
		this.skillId = skillId;
	}

	/**
	 * 获取属性：
	 *	cd时间
	 */
	public int getCdTime(){
		return cdTime;
	}

	/**
	 * 设置属性：
	 *	cd时间
	 */
	public void setCdTime(int cdTime){
		this.cdTime = cdTime;
	}

	/**
	 * 获取属性：
	 *	剩余技能数量
	 */
	public int getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	剩余技能数量
	 */
	public void setNum(int num){
		this.num = num;
	}

}