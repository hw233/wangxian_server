package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 法宝强化确认<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>magicweaponId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>star</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canStrong</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class MAGICWEAPON_STRONG_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long magicweaponId;
	short star;
	boolean canStrong;

	public MAGICWEAPON_STRONG_RES(){
	}

	public MAGICWEAPON_STRONG_RES(long seqNum,long magicweaponId,short star,boolean canStrong){
		this.seqNum = seqNum;
		this.magicweaponId = magicweaponId;
		this.star = star;
		this.canStrong = canStrong;
	}

	public MAGICWEAPON_STRONG_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		magicweaponId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		star = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		canStrong = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x70F0EF06;
	}

	public String getTypeDescription() {
		return "MAGICWEAPON_STRONG_RES";
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

			buffer.putLong(magicweaponId);
			buffer.putShort(star);
			buffer.put((byte)(canStrong==false?0:1));
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
	 *	要强化的法宝
	 */
	public long getMagicweaponId(){
		return magicweaponId;
	}

	/**
	 * 设置属性：
	 *	要强化的法宝
	 */
	public void setMagicweaponId(long magicweaponId){
		this.magicweaponId = magicweaponId;
	}

	/**
	 * 获取属性：
	 *	强化后装备的等级
	 */
	public short getStar(){
		return star;
	}

	/**
	 * 设置属性：
	 *	强化后装备的等级
	 */
	public void setStar(short star){
		this.star = star;
	}

	/**
	 * 获取属性：
	 *	是否还能强化
	 */
	public boolean getCanStrong(){
		return canStrong;
	}

	/**
	 * 设置属性：
	 *	是否还能强化
	 */
	public void setCanStrong(boolean canStrong){
		this.canStrong = canStrong;
	}

}