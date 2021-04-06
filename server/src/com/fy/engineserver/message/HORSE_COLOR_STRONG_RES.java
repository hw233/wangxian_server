package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.horse2.model.HorseAttrModel;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 升级坐骑颜色<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr.length</td><td>int</td><td>4个字节</td><td>HorseAttrModel数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].phyDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].magicDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].breakDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].critical</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].blizzAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].fireDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].blizzDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].windDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].thunderDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].fireBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].blizzBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[0].windBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[0].thunderBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].phyDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].magicDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].breakDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].critical</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].blizzAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].fireDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].blizzDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].windDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].thunderDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].fireBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].blizzBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[1].windBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[1].thunderBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].phyDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].magicDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].breakDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].critical</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].blizzAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].fireDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].blizzDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].windDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].thunderDefance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].fireBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].blizzBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeHorseAttr[2].windBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeHorseAttr[2].thunderBreak</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class HORSE_COLOR_STRONG_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	HorseAttrModel[] changeHorseAttr;

	public HORSE_COLOR_STRONG_RES(){
	}

	public HORSE_COLOR_STRONG_RES(long seqNum,byte result,HorseAttrModel[] changeHorseAttr){
		this.seqNum = seqNum;
		this.result = result;
		this.changeHorseAttr = changeHorseAttr;
	}

	public HORSE_COLOR_STRONG_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		changeHorseAttr = new HorseAttrModel[len];
		for(int i = 0 ; i < changeHorseAttr.length ; i++){
			changeHorseAttr[i] = new HorseAttrModel();
			changeHorseAttr[i].setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setPhyDefance((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setMagicDefance((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setMp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setBreakDefance((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setAccurate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setCriticalDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setCritical((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setDodge((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setFireAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setBlizzAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setWindAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setThunderAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setFireDefance((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setBlizzDefance((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setWindDefance((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setThunderDefance((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setFireBreak((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setBlizzBreak((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setWindBreak((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			changeHorseAttr[i].setThunderBreak((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70F0EF58;
	}

	public String getTypeDescription() {
		return "HORSE_COLOR_STRONG_RES";
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
		len += 4;
		for(int i = 0 ; i < changeHorseAttr.length ; i++){
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
		}
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

			buffer.put(result);
			buffer.putInt(changeHorseAttr.length);
			for(int i = 0 ; i < changeHorseAttr.length ; i++){
				buffer.putInt((int)changeHorseAttr[i].getPhyAttack());
				buffer.putInt((int)changeHorseAttr[i].getMagicAttack());
				buffer.putInt((int)changeHorseAttr[i].getPhyDefance());
				buffer.putInt((int)changeHorseAttr[i].getMagicDefance());
				buffer.putInt((int)changeHorseAttr[i].getHp());
				buffer.putInt((int)changeHorseAttr[i].getMp());
				buffer.putInt((int)changeHorseAttr[i].getBreakDefance());
				buffer.putInt((int)changeHorseAttr[i].getAccurate());
				buffer.putInt((int)changeHorseAttr[i].getCriticalDefence());
				buffer.putInt((int)changeHorseAttr[i].getCritical());
				buffer.putInt((int)changeHorseAttr[i].getDodge());
				buffer.putInt((int)changeHorseAttr[i].getHit());
				buffer.putInt((int)changeHorseAttr[i].getFireAttack());
				buffer.putInt((int)changeHorseAttr[i].getBlizzAttack());
				buffer.putInt((int)changeHorseAttr[i].getWindAttack());
				buffer.putInt((int)changeHorseAttr[i].getThunderAttack());
				buffer.putInt((int)changeHorseAttr[i].getFireDefance());
				buffer.putInt((int)changeHorseAttr[i].getBlizzDefance());
				buffer.putInt((int)changeHorseAttr[i].getWindDefance());
				buffer.putInt((int)changeHorseAttr[i].getThunderDefance());
				buffer.putInt((int)changeHorseAttr[i].getFireBreak());
				buffer.putInt((int)changeHorseAttr[i].getBlizzBreak());
				buffer.putInt((int)changeHorseAttr[i].getWindBreak());
				buffer.putInt((int)changeHorseAttr[i].getThunderBreak());
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
	 *	结果 1成功
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果 1成功
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	坐骑属性
	 */
	public HorseAttrModel[] getChangeHorseAttr(){
		return changeHorseAttr;
	}

	/**
	 * 设置属性：
	 *	坐骑属性
	 */
	public void setChangeHorseAttr(HorseAttrModel[] changeHorseAttr){
		this.changeHorseAttr = changeHorseAttr;
	}

}