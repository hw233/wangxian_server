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
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialID.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialID</td><td>long[]</td><td>strongMaterialID.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherStrongMaterialID.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherStrongMaterialID</td><td>long[]</td><td>otherStrongMaterialID.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherStrongMaterialNum.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherStrongMaterialNum</td><td>int[]</td><td>otherStrongMaterialNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class MAGICWEAPON_STRONG_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long magicweaponId;
	long[] strongMaterialID;
	long[] otherStrongMaterialID;
	int[] otherStrongMaterialNum;
	byte strongType;

	public MAGICWEAPON_STRONG_REQ(){
	}

	public MAGICWEAPON_STRONG_REQ(long seqNum,long magicweaponId,long[] strongMaterialID,long[] otherStrongMaterialID,int[] otherStrongMaterialNum,byte strongType){
		this.seqNum = seqNum;
		this.magicweaponId = magicweaponId;
		this.strongMaterialID = strongMaterialID;
		this.otherStrongMaterialID = otherStrongMaterialID;
		this.otherStrongMaterialNum = otherStrongMaterialNum;
		this.strongType = strongType;
	}

	public MAGICWEAPON_STRONG_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		magicweaponId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		strongMaterialID = new long[len];
		for(int i = 0 ; i < strongMaterialID.length ; i++){
			strongMaterialID[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		otherStrongMaterialID = new long[len];
		for(int i = 0 ; i < otherStrongMaterialID.length ; i++){
			otherStrongMaterialID[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		otherStrongMaterialNum = new int[len];
		for(int i = 0 ; i < otherStrongMaterialNum.length ; i++){
			otherStrongMaterialNum[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		strongType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x00F0EF06;
	}

	public String getTypeDescription() {
		return "MAGICWEAPON_STRONG_REQ";
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
		len += strongMaterialID.length * 8;
		len += 4;
		len += otherStrongMaterialID.length * 8;
		len += 4;
		len += otherStrongMaterialNum.length * 4;
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
			buffer.putInt(strongMaterialID.length);
			for(int i = 0 ; i < strongMaterialID.length; i++){
				buffer.putLong(strongMaterialID[i]);
			}
			buffer.putInt(otherStrongMaterialID.length);
			for(int i = 0 ; i < otherStrongMaterialID.length; i++){
				buffer.putLong(otherStrongMaterialID[i]);
			}
			buffer.putInt(otherStrongMaterialNum.length);
			for(int i = 0 ; i < otherStrongMaterialNum.length; i++){
				buffer.putInt(otherStrongMaterialNum[i]);
			}
			buffer.put(strongType);
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
	 *	强化符ID
	 */
	public long[] getStrongMaterialID(){
		return strongMaterialID;
	}

	/**
	 * 设置属性：
	 *	强化符ID
	 */
	public void setStrongMaterialID(long[] strongMaterialID){
		this.strongMaterialID = strongMaterialID;
	}

	/**
	 * 获取属性：
	 *	额外的强化需求物品ID
	 */
	public long[] getOtherStrongMaterialID(){
		return otherStrongMaterialID;
	}

	/**
	 * 设置属性：
	 *	额外的强化需求物品ID
	 */
	public void setOtherStrongMaterialID(long[] otherStrongMaterialID){
		this.otherStrongMaterialID = otherStrongMaterialID;
	}

	/**
	 * 获取属性：
	 *	额外的强化需求物品ID
	 */
	public int[] getOtherStrongMaterialNum(){
		return otherStrongMaterialNum;
	}

	/**
	 * 设置属性：
	 *	额外的强化需求物品ID
	 */
	public void setOtherStrongMaterialNum(int[] otherStrongMaterialNum){
		this.otherStrongMaterialNum = otherStrongMaterialNum;
	}

	/**
	 * 获取属性：
	 *	强化类型，0表示使用金币强化，1表示使用元宝强化
	 */
	public byte getStrongType(){
		return strongType;
	}

	/**
	 * 设置属性：
	 *	强化类型，0表示使用金币强化，1表示使用元宝强化
	 */
	public void setStrongType(byte strongType){
		this.strongType = strongType;
	}

}