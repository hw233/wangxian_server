package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 生成混沌万灵榜广播<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentName</td><td>String</td><td>equipmentName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentShowType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rank</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CREATE_SPECIALEQUIPMENT_BROADCAST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String equipmentName;
	int equpmentShowType;
	int billType;
	int rank;

	public CREATE_SPECIALEQUIPMENT_BROADCAST_RES(){
	}

	public CREATE_SPECIALEQUIPMENT_BROADCAST_RES(long seqNum,String equipmentName,int equpmentShowType,int billType,int rank){
		this.seqNum = seqNum;
		this.equipmentName = equipmentName;
		this.equpmentShowType = equpmentShowType;
		this.billType = billType;
		this.rank = rank;
	}

	public CREATE_SPECIALEQUIPMENT_BROADCAST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		equipmentName = new String(content,offset,len,"UTF-8");
		offset += len;
		equpmentShowType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		billType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		rank = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70EEE004;
	}

	public String getTypeDescription() {
		return "CREATE_SPECIALEQUIPMENT_BROADCAST_RES";
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
		len += 2;
		try{
			len +=equipmentName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = equipmentName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(equpmentShowType);
			buffer.putInt(billType);
			buffer.putInt(rank);
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
	 *	装备名称
	 */
	public String getEquipmentName(){
		return equipmentName;
	}

	/**
	 * 设置属性：
	 *	装备名称
	 */
	public void setEquipmentName(String equipmentName){
		this.equipmentName = equipmentName;
	}

	/**
	 * 获取属性：
	 *	装备类型
	 */
	public int getEqupmentShowType(){
		return equpmentShowType;
	}

	/**
	 * 设置属性：
	 *	装备类型
	 */
	public void setEqupmentShowType(int equpmentShowType){
		this.equpmentShowType = equpmentShowType;
	}

	/**
	 * 获取属性：
	 *	榜单类型(0天榜,1地榜)
	 */
	public int getBillType(){
		return billType;
	}

	/**
	 * 设置属性：
	 *	榜单类型(0天榜,1地榜)
	 */
	public void setBillType(int billType){
		this.billType = billType;
	}

	/**
	 * 获取属性：
	 *	第几名
	 */
	public int getRank(){
		return rank;
	}

	/**
	 * 设置属性：
	 *	第几名
	 */
	public void setRank(int rank){
		this.rank = rank;
	}

}