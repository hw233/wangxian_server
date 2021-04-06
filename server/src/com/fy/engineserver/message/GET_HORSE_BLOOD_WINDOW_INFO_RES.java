package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.horse2.model.HorseAttrModel;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开坐骑血脉升星界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentXuemai</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needXuemai</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxBloodStar</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.phyDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.magicDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.breakDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.critical</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.blizzAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.fireDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.blizzDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.windDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.thunderDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.fireBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.blizzBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr.windBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.thunderBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class GET_HORSE_BLOOD_WINDOW_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long horseId;
	long currentXuemai;
	long needXuemai;
	int maxBloodStar;
	HorseAttrModel changeHorseAttr;

	public GET_HORSE_BLOOD_WINDOW_INFO_RES(){
	}

	public GET_HORSE_BLOOD_WINDOW_INFO_RES(long seqNum,long horseId,long currentXuemai,long needXuemai,int maxBloodStar,HorseAttrModel changeHorseAttr){
		this.seqNum = seqNum;
		this.horseId = horseId;
		this.currentXuemai = currentXuemai;
		this.needXuemai = needXuemai;
		this.maxBloodStar = maxBloodStar;
		this.changeHorseAttr = changeHorseAttr;
	}

	public GET_HORSE_BLOOD_WINDOW_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		horseId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		currentXuemai = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		needXuemai = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		maxBloodStar = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		changeHorseAttr = new HorseAttrModel();
		changeHorseAttr.setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setPhyDefance((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setMagicDefance((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setHp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setMp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setBreakDefance((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setAccurate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setCriticalDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setCritical((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setDodge((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setFireAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setBlizzAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setWindAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setThunderAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setFireDefance((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setBlizzDefance((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setWindDefance((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setThunderDefance((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setFireBreak((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setBlizzBreak((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setWindBreak((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		changeHorseAttr.setThunderBreak((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
	}

	public int getType() {
		return 0x70F0EF63;
	}

	public String getTypeDescription() {
		return "GET_HORSE_BLOOD_WINDOW_INFO_RES";
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
		len += 8;
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

			buffer.putLong(horseId);
			buffer.putLong(currentXuemai);
			buffer.putLong(needXuemai);
			buffer.putInt(maxBloodStar);
			buffer.putInt((int)changeHorseAttr.getPhyAttack());
			buffer.putInt((int)changeHorseAttr.getMagicAttack());
			buffer.putInt((int)changeHorseAttr.getPhyDefance());
			buffer.putInt((int)changeHorseAttr.getMagicDefance());
			buffer.putInt((int)changeHorseAttr.getHp());
			buffer.putInt((int)changeHorseAttr.getMp());
			buffer.putInt((int)changeHorseAttr.getBreakDefance());
			buffer.putInt((int)changeHorseAttr.getAccurate());
			buffer.putInt((int)changeHorseAttr.getCriticalDefence());
			buffer.putInt((int)changeHorseAttr.getCritical());
			buffer.putInt((int)changeHorseAttr.getDodge());
			buffer.putInt((int)changeHorseAttr.getHit());
			buffer.putInt((int)changeHorseAttr.getFireAttack());
			buffer.putInt((int)changeHorseAttr.getBlizzAttack());
			buffer.putInt((int)changeHorseAttr.getWindAttack());
			buffer.putInt((int)changeHorseAttr.getThunderAttack());
			buffer.putInt((int)changeHorseAttr.getFireDefance());
			buffer.putInt((int)changeHorseAttr.getBlizzDefance());
			buffer.putInt((int)changeHorseAttr.getWindDefance());
			buffer.putInt((int)changeHorseAttr.getThunderDefance());
			buffer.putInt((int)changeHorseAttr.getFireBreak());
			buffer.putInt((int)changeHorseAttr.getBlizzBreak());
			buffer.putInt((int)changeHorseAttr.getWindBreak());
			buffer.putInt((int)changeHorseAttr.getThunderBreak());
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
	 *	坐骑id
	 */
	public long getHorseId(){
		return horseId;
	}

	/**
	 * 设置属性：
	 *	坐骑id
	 */
	public void setHorseId(long horseId){
		this.horseId = horseId;
	}

	/**
	 * 获取属性：
	 *	当前拥有血脉值
	 */
	public long getCurrentXuemai(){
		return currentXuemai;
	}

	/**
	 * 设置属性：
	 *	当前拥有血脉值
	 */
	public void setCurrentXuemai(long currentXuemai){
		this.currentXuemai = currentXuemai;
	}

	/**
	 * 获取属性：
	 *	升级到下一星级需要的血脉值
	 */
	public long getNeedXuemai(){
		return needXuemai;
	}

	/**
	 * 设置属性：
	 *	升级到下一星级需要的血脉值
	 */
	public void setNeedXuemai(long needXuemai){
		this.needXuemai = needXuemai;
	}

	/**
	 * 获取属性：
	 *	最大可达到的血脉星级
	 */
	public int getMaxBloodStar(){
		return maxBloodStar;
	}

	/**
	 * 设置属性：
	 *	最大可达到的血脉星级
	 */
	public void setMaxBloodStar(int maxBloodStar){
		this.maxBloodStar = maxBloodStar;
	}

	/**
	 * 获取属性：
	 *	坐骑属性
	 */
	public HorseAttrModel getChangeHorseAttr(){
		return changeHorseAttr;
	}

	/**
	 * 设置属性：
	 *	坐骑属性
	 */
	public void setChangeHorseAttr(HorseAttrModel changeHorseAttr){
		this.changeHorseAttr = changeHorseAttr;
	}

}