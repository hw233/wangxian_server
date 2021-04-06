package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.horse.Horse;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器向客户端发送，通知客户端发生了某些事情，客户端根据不同的事情，作不同的现实<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.horseId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.horseName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.horseName</td><td>String</td><td>horse.horseName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.equipmentIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.equipmentIds</td><td>long[]</td><td>horse.equipmentIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.maxEnergy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.energy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.lastEnergyIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horse.windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horse.thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class HORSE_PUTONOROFF_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Horse horse;

	public HORSE_PUTONOROFF_RES(){
	}

	public HORSE_PUTONOROFF_RES(long seqNum,Horse horse){
		this.seqNum = seqNum;
		this.horse = horse;
	}

	public HORSE_PUTONOROFF_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		horse = new Horse();
		horse.setHorseId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		horse.setHorseName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		long[] equipmentIds_0001 = new long[len];
		for(int j = 0 ; j < equipmentIds_0001.length ; j++){
			equipmentIds_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		horse.setEquipmentIds(equipmentIds_0001);
		horse.setMaxEnergy((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setEnergy((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setLastEnergyIndex((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setMaxMP((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setBreakDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setAccurate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setCriticalDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setDodge((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setBlizzardAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setBlizzardDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setFireAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setFireDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setWindAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setWindDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		horse.setThunderAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
	}

	public int getType() {
		return 0x70000120;
	}

	public String getTypeDescription() {
		return "HORSE_PUTONOROFF_RES";
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
		if(horse.getHorseName() != null){
			try{
			len += horse.getHorseName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += horse.getEquipmentIds().length * 8;
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

			buffer.putLong(horse.getHorseId());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = horse.getHorseName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(horse.getEquipmentIds().length);
			long[] equipmentIds_0002 = horse.getEquipmentIds();
			for(int j = 0 ; j < equipmentIds_0002.length ; j++){
				buffer.putLong(equipmentIds_0002[j]);
			}
			buffer.putInt((int)horse.getMaxEnergy());
			buffer.putInt((int)horse.getEnergy());
			buffer.putInt((int)horse.getLastEnergyIndex());
			buffer.putInt((int)horse.getMaxHP());
			buffer.putInt((int)horse.getPhyAttack());
			buffer.putInt((int)horse.getMagicAttack());
			buffer.putInt((int)horse.getPhyDefence());
			buffer.putInt((int)horse.getMagicDefence());
			buffer.putInt((int)horse.getMaxMP());
			buffer.putInt((int)horse.getBreakDefence());
			buffer.putInt((int)horse.getAccurate());
			buffer.putInt((int)horse.getCriticalDefence());
			buffer.putInt((int)horse.getCriticalHit());
			buffer.putInt((int)horse.getHit());
			buffer.putInt((int)horse.getDodge());
			buffer.putInt((int)horse.getBlizzardAttack());
			buffer.putInt((int)horse.getBlizzardDefence());
			buffer.putInt((int)horse.getFireAttack());
			buffer.putInt((int)horse.getFireDefence());
			buffer.putInt((int)horse.getWindAttack());
			buffer.putInt((int)horse.getWindDefence());
			buffer.putInt((int)horse.getThunderAttack());
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
	 *	返回指定马匹
	 */
	public Horse getHorse(){
		return horse;
	}

	/**
	 * 设置属性：
	 *	返回指定马匹
	 */
	public void setHorse(Horse horse){
		this.horse = horse;
	}

}