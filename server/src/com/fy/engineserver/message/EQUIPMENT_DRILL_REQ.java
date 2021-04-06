package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求服务器，装备打孔的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>confirmType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialAmount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class EQUIPMENT_DRILL_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long equipmentId;
	byte confirmType;
	int materialAmount;
	int index;

	public EQUIPMENT_DRILL_REQ(){
	}

	public EQUIPMENT_DRILL_REQ(long seqNum,long equipmentId,byte confirmType,int materialAmount,int index){
		this.seqNum = seqNum;
		this.equipmentId = equipmentId;
		this.confirmType = confirmType;
		this.materialAmount = materialAmount;
		this.index = index;
	}

	public EQUIPMENT_DRILL_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		equipmentId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		confirmType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		materialAmount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00F1EEF2;
	}

	public String getTypeDescription() {
		return "EQUIPMENT_DRILL_REQ";
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
		len += 1;
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

			buffer.putLong(equipmentId);
			buffer.put(confirmType);
			buffer.putInt(materialAmount);
			buffer.putInt(index);
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
	 *	要升级的装备，玩家的背包中哦你必须有此装备
	 */
	public long getEquipmentId(){
		return equipmentId;
	}

	/**
	 * 设置属性：
	 *	要升级的装备，玩家的背包中哦你必须有此装备
	 */
	public void setEquipmentId(long equipmentId){
		this.equipmentId = equipmentId;
	}

	/**
	 * 获取属性：
	 *	确认标记，0标识不打孔，只是查询装备的打孔数据,1标识要打孔。2代表给服务器发消息，想要重置，需要服务器传回重置所需物品的信息。3代表确认重置
	 */
	public byte getConfirmType(){
		return confirmType;
	}

	/**
	 * 设置属性：
	 *	确认标记，0标识不打孔，只是查询装备的打孔数据,1标识要打孔。2代表给服务器发消息，想要重置，需要服务器传回重置所需物品的信息。3代表确认重置
	 */
	public void setConfirmType(byte confirmType){
		this.confirmType = confirmType;
	}

	/**
	 * 获取属性：
	 *	用几个材料打孔
	 */
	public int getMaterialAmount(){
		return materialAmount;
	}

	/**
	 * 设置属性：
	 *	用几个材料打孔
	 */
	public void setMaterialAmount(int materialAmount){
		this.materialAmount = materialAmount;
	}

	/**
	 * 获取属性：
	 *	装备在背包中的位置
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	装备在背包中的位置
	 */
	public void setIndex(int index){
		this.index = index;
	}

}