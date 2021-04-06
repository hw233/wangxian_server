package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.pet.Pet;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 给宠物分配属性点<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>points.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>points</td><td>int[]</td><td>points.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.dingli</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showPhyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showMagicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.hitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.dodgeRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.criticalHitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.physicalDecrease</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.spellDecrease</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.unAllocatedPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class PET_ALLOCATE_POINTS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] points;
	Pet pet;

	public PET_ALLOCATE_POINTS_RES(){
	}

	public PET_ALLOCATE_POINTS_RES(long seqNum,int[] points,Pet pet){
		this.seqNum = seqNum;
		this.points = points;
		this.pet = pet;
	}

	public PET_ALLOCATE_POINTS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		points = new int[len];
		for(int i = 0 ; i < points.length ; i++){
			points[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		pet = new Pet();
		pet.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		pet.setStrength((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setDexterity((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setSpellpower((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setConstitution((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setDingli((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setHp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setDodge((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setHitRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setDodgeRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setCriticalHitRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setPhysicalDecrease((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setSpellDecrease((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setUnAllocatedPoints((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
	}

	public int getType() {
		return 0x7000FF33;
	}

	public String getTypeDescription() {
		return "PET_ALLOCATE_POINTS_RES";
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
		len += points.length * 4;
		len += 8;
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

			buffer.putInt(points.length);
			for(int i = 0 ; i < points.length; i++){
				buffer.putInt(points[i]);
			}
			buffer.putLong(pet.getId());
			buffer.putInt((int)pet.getStrength());
			buffer.putInt((int)pet.getDexterity());
			buffer.putInt((int)pet.getSpellpower());
			buffer.putInt((int)pet.getConstitution());
			buffer.putInt((int)pet.getDingli());
			buffer.putInt((int)pet.getShowPhyAttack());
			buffer.putInt((int)pet.getPhyDefence());
			buffer.putInt((int)pet.getShowMagicAttack());
			buffer.putInt((int)pet.getMagicDefence());
			buffer.putInt((int)pet.getMaxHP());
			buffer.putInt((int)pet.getHp());
			buffer.putInt((int)pet.getCriticalHit());
			buffer.putInt((int)pet.getHit());
			buffer.putInt((int)pet.getDodge());
			buffer.putInt((int)pet.getHitRate());
			buffer.putInt((int)pet.getDodgeRate());
			buffer.putInt((int)pet.getCriticalHitRate());
			buffer.putInt((int)pet.getPhysicalDecrease());
			buffer.putInt((int)pet.getSpellDecrease());
			buffer.putInt((int)pet.getUnAllocatedPoints());
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
	 *	累计分配的点：0力量1灵力2身法3耐力4定力
	 */
	public int[] getPoints(){
		return points;
	}

	/**
	 * 设置属性：
	 *	累计分配的点：0力量1灵力2身法3耐力4定力
	 */
	public void setPoints(int[] points){
		this.points = points;
	}

	/**
	 * 获取属性：
	 *	加点后的宠物
	 */
	public Pet getPet(){
		return pet;
	}

	/**
	 * 设置属性：
	 *	加点后的宠物
	 */
	public void setPet(Pet pet){
		this.pet = pet;
	}

}