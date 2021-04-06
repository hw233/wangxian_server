package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取技能id<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIds</td><td>int[]</td><td>skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[0]</td><td>String</td><td>icons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[1]</td><td>String</td><td>icons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[2]</td><td>String</td><td>icons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cdtime.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cdtime</td><td>int[]</td><td>cdtime.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>attackRange.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackRange</td><td>int[]</td><td>attackRange.length</td><td>*</td></tr>
 * </table>
 */
public class WOLF_SKILL_ID_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	int[] skillIds;
	String[] icons;
	int[] cdtime;
	int[] attackRange;

	public WOLF_SKILL_ID_RES(){
	}

	public WOLF_SKILL_ID_RES(long seqNum,long playerId,int[] skillIds,String[] icons,int[] cdtime,int[] attackRange){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.skillIds = skillIds;
		this.icons = icons;
		this.cdtime = cdtime;
		this.attackRange = attackRange;
	}

	public WOLF_SKILL_ID_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillIds = new int[len];
		for(int i = 0 ; i < skillIds.length ; i++){
			skillIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		icons = new String[len];
		for(int i = 0 ; i < icons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			icons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		cdtime = new int[len];
		for(int i = 0 ; i < cdtime.length ; i++){
			cdtime[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		attackRange = new int[len];
		for(int i = 0 ; i < attackRange.length ; i++){
			attackRange[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF032;
	}

	public String getTypeDescription() {
		return "WOLF_SKILL_ID_RES";
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
		len += 4;
		len += skillIds.length * 4;
		len += 4;
		for(int i = 0 ; i < icons.length; i++){
			len += 2;
			try{
				len += icons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += cdtime.length * 4;
		len += 4;
		len += attackRange.length * 4;
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

			buffer.putLong(playerId);
			buffer.putInt(skillIds.length);
			for(int i = 0 ; i < skillIds.length; i++){
				buffer.putInt(skillIds[i]);
			}
			buffer.putInt(icons.length);
			for(int i = 0 ; i < icons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = icons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(cdtime.length);
			for(int i = 0 ; i < cdtime.length; i++){
				buffer.putInt(cdtime[i]);
			}
			buffer.putInt(attackRange.length);
			for(int i = 0 ; i < attackRange.length; i++){
				buffer.putInt(attackRange[i]);
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
	 *	玩家id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	玩家id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	技能ids
	 */
	public int[] getSkillIds(){
		return skillIds;
	}

	/**
	 * 设置属性：
	 *	技能ids
	 */
	public void setSkillIds(int[] skillIds){
		this.skillIds = skillIds;
	}

	/**
	 * 获取属性：
	 *	icon
	 */
	public String[] getIcons(){
		return icons;
	}

	/**
	 * 设置属性：
	 *	icon
	 */
	public void setIcons(String[] icons){
		this.icons = icons;
	}

	/**
	 * 获取属性：
	 *	cd
	 */
	public int[] getCdtime(){
		return cdtime;
	}

	/**
	 * 设置属性：
	 *	cd
	 */
	public void setCdtime(int[] cdtime){
		this.cdtime = cdtime;
	}

	/**
	 * 获取属性：
	 *	攻击距离
	 */
	public int[] getAttackRange(){
		return attackRange;
	}

	/**
	 * 设置属性：
	 *	攻击距离
	 */
	public void setAttackRange(int[] attackRange){
		this.attackRange = attackRange;
	}

}