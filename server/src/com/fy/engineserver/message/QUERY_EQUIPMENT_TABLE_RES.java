package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.SoulEquipment4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询主玩家的装备栏<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client.length</td><td>int</td><td>4个字节</td><td>SoulEquipment4Client数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[0].soulTyp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[0].equipment.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[0].equipment</td><td>long[]</td><td>soulEquipment4Client[0].equipment.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[1].soulTyp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[1].equipment.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[1].equipment</td><td>long[]</td><td>soulEquipment4Client[1].equipment.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[2].soulTyp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[2].equipment.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[2].equipment</td><td>long[]</td><td>soulEquipment4Client[2].equipment.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_EQUIPMENT_TABLE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	SoulEquipment4Client[] soulEquipment4Client;

	public QUERY_EQUIPMENT_TABLE_RES(){
	}

	public QUERY_EQUIPMENT_TABLE_RES(long seqNum,SoulEquipment4Client[] soulEquipment4Client){
		this.seqNum = seqNum;
		this.soulEquipment4Client = soulEquipment4Client;
	}

	public QUERY_EQUIPMENT_TABLE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		soulEquipment4Client = new SoulEquipment4Client[len];
		for(int i = 0 ; i < soulEquipment4Client.length ; i++){
			soulEquipment4Client[i] = new SoulEquipment4Client();
			soulEquipment4Client[i].setSoulTyp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] equipment_0001 = new long[len];
			for(int j = 0 ; j < equipment_0001.length ; j++){
				equipment_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			soulEquipment4Client[i].setEquipment(equipment_0001);
		}
	}

	public int getType() {
		return 0x700000F4;
	}

	public String getTypeDescription() {
		return "QUERY_EQUIPMENT_TABLE_RES";
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
		for(int i = 0 ; i < soulEquipment4Client.length ; i++){
			len += 4;
			len += 4;
			len += soulEquipment4Client[i].getEquipment().length * 8;
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

			buffer.putInt(soulEquipment4Client.length);
			for(int i = 0 ; i < soulEquipment4Client.length ; i++){
				buffer.putInt((int)soulEquipment4Client[i].getSoulTyp());
				buffer.putInt(soulEquipment4Client[i].getEquipment().length);
				long[] equipment_0002 = soulEquipment4Client[i].getEquipment();
				for(int j = 0 ; j < equipment_0002.length ; j++){
					buffer.putLong(equipment_0002[j]);
				}
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
	 *	玩家元神的装备信息
	 */
	public SoulEquipment4Client[] getSoulEquipment4Client(){
		return soulEquipment4Client;
	}

	/**
	 * 设置属性：
	 *	玩家元神的装备信息
	 */
	public void setSoulEquipment4Client(SoulEquipment4Client[] soulEquipment4Client){
		this.soulEquipment4Client = soulEquipment4Client;
	}

}