package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.Soul;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 元神属性变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>action</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.soulType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.maxHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.maxMp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soul.thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soul.thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class PLAYER_SOUL_CHANGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int action;
	Soul soul;

	public PLAYER_SOUL_CHANGE_RES(){
	}

	public PLAYER_SOUL_CHANGE_RES(long seqNum,int action,Soul soul){
		this.seqNum = seqNum;
		this.action = action;
		this.soul = soul;
	}

	public PLAYER_SOUL_CHANGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		action = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		soul = new Soul();
		soul.setSoulType((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setCareer((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		soul.setGrade((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setMaxHp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setMaxMp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setStrength((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setSpellpower((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setConstitution((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setDexterity((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setDodge((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setAccurate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setCriticalDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setBreakDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setFireAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setFireDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setFireIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setBlizzardAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setBlizzardDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setBlizzardIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setWindAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setWindDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setWindIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setThunderAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setThunderDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		soul.setThunderIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
	}

	public int getType() {
		return 0x70FA0102;
	}

	public String getTypeDescription() {
		return "PLAYER_SOUL_CHANGE_RES";
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
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
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

			buffer.putInt(action);
			buffer.putInt((int)soul.getSoulType());
			buffer.put((byte)soul.getCareer());
			buffer.putInt((int)soul.getGrade());
			buffer.putInt((int)soul.getMaxHp());
			buffer.putInt((int)soul.getMaxMp());
			buffer.putInt((int)soul.getStrength());
			buffer.putInt((int)soul.getSpellpower());
			buffer.putInt((int)soul.getConstitution());
			buffer.putInt((int)soul.getDexterity());
			buffer.putInt((int)soul.getHit());
			buffer.putInt((int)soul.getDodge());
			buffer.putInt((int)soul.getAccurate());
			buffer.putInt((int)soul.getPhyAttack());
			buffer.putInt((int)soul.getMagicAttack());
			buffer.putInt((int)soul.getCriticalHit());
			buffer.putInt((int)soul.getCriticalDefence());
			buffer.putInt((int)soul.getPhyDefence());
			buffer.putInt((int)soul.getMagicDefence());
			buffer.putInt((int)soul.getBreakDefence());
			buffer.putInt((int)soul.getFireAttack());
			buffer.putInt((int)soul.getFireDefence());
			buffer.putInt((int)soul.getFireIgnoreDefence());
			buffer.putInt((int)soul.getBlizzardAttack());
			buffer.putInt((int)soul.getBlizzardDefence());
			buffer.putInt((int)soul.getBlizzardIgnoreDefence());
			buffer.putInt((int)soul.getWindAttack());
			buffer.putInt((int)soul.getWindDefence());
			buffer.putInt((int)soul.getWindIgnoreDefence());
			buffer.putInt((int)soul.getThunderAttack());
			buffer.putInt((int)soul.getThunderDefence());
			buffer.putInt((int)soul.getThunderIgnoreDefence());
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
	 *	操作:0属性变化 1 新增
	 */
	public int getAction(){
		return action;
	}

	/**
	 * 设置属性：
	 *	操作:0属性变化 1 新增
	 */
	public void setAction(int action){
		this.action = action;
	}

	/**
	 * 获取属性：
	 *	变化的元神
	 */
	public Soul getSoul(){
		return soul;
	}

	/**
	 * 设置属性：
	 *	变化的元神
	 */
	public void setSoul(Soul soul){
		this.soul = soul;
	}

}