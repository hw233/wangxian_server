package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 随身修理装备<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>repairType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>money</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class REPAIR_CARRY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	byte repairType;
	long equipmentId;
	int money;

	public REPAIR_CARRY_RES(){
	}

	public REPAIR_CARRY_RES(long seqNum,byte result,byte repairType,long equipmentId,int money){
		this.seqNum = seqNum;
		this.result = result;
		this.repairType = repairType;
		this.equipmentId = equipmentId;
		this.money = money;
	}

	public REPAIR_CARRY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		repairType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		equipmentId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		money = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F0EE13;
	}

	public String getTypeDescription() {
		return "REPAIR_CARRY_RES";
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
		len += 1;
		len += 8;
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

			buffer.put(result);
			buffer.put(repairType);
			buffer.putLong(equipmentId);
			buffer.putInt(money);
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
	 *	0标识修理成功，1标识钱不够修理
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	0标识修理成功，1标识钱不够修理
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	修理的内容，0标识全部修理，1标识修理当前装备武器，2标识逐个修理
	 */
	public byte getRepairType(){
		return repairType;
	}

	/**
	 * 设置属性：
	 *	修理的内容，0标识全部修理，1标识修理当前装备武器，2标识逐个修理
	 */
	public void setRepairType(byte repairType){
		this.repairType = repairType;
	}

	/**
	 * 获取属性：
	 *	要修理的装备
	 */
	public long getEquipmentId(){
		return equipmentId;
	}

	/**
	 * 设置属性：
	 *	要修理的装备
	 */
	public void setEquipmentId(long equipmentId){
		this.equipmentId = equipmentId;
	}

	/**
	 * 获取属性：
	 *	修理成功说花掉的钱
	 */
	public int getMoney(){
		return money;
	}

	/**
	 * 设置属性：
	 *	修理成功说花掉的钱
	 */
	public void setMoney(int money){
		this.money = money;
	}

}