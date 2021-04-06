package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器发送给客户端，通知客户端技能CD状态，防止冷却时间长的技能在服务端释放失败，导致无谓的长时间冷却。<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillid</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillstate</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>startTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SKILL_CD_MODIFY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerid;
	short skillid;
	byte skillstate;
	long startTime;

	public SKILL_CD_MODIFY_REQ(){
	}

	public SKILL_CD_MODIFY_REQ(long seqNum,long playerid,short skillid,byte skillstate,long startTime){
		this.seqNum = seqNum;
		this.playerid = playerid;
		this.skillid = skillid;
		this.skillstate = skillstate;
		this.startTime = startTime;
	}

	public SKILL_CD_MODIFY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		skillid = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		skillstate = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		startTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x0000EF09;
	}

	public String getTypeDescription() {
		return "SKILL_CD_MODIFY_REQ";
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
		len += 2;
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

			buffer.putLong(playerid);
			buffer.putShort(skillid);
			buffer.put(skillstate);
			buffer.putLong(startTime);
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
	 *	玩家ID
	 */
	public long getPlayerid(){
		return playerid;
	}

	/**
	 * 设置属性：
	 *	玩家ID
	 */
	public void setPlayerid(long playerid){
		this.playerid = playerid;
	}

	/**
	 * 获取属性：
	 *	技能ID
	 */
	public short getSkillid(){
		return skillid;
	}

	/**
	 * 设置属性：
	 *	技能ID
	 */
	public void setSkillid(short skillid){
		this.skillid = skillid;
	}

	/**
	 * 获取属性：
	 *	技能状态,0开始，1-吟唱，2-攻击，3-冷却
	 */
	public byte getSkillstate(){
		return skillstate;
	}

	/**
	 * 设置属性：
	 *	技能状态,0开始，1-吟唱，2-攻击，3-冷却
	 */
	public void setSkillstate(byte skillstate){
		this.skillstate = skillstate;
	}

	/**
	 * 获取属性：
	 *	技能开始执行的时间
	 */
	public long getStartTime(){
		return startTime;
	}

	/**
	 * 设置属性：
	 *	技能开始执行的时间
	 */
	public void setStartTime(long startTime){
		this.startTime = startTime;
	}

}