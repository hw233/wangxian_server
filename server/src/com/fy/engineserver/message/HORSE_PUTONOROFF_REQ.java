package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器向客户端发送，通知客户端发生了某些事情，客户端根据不同的事情，作不同的现实<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>onOroff</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class HORSE_PUTONOROFF_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean onOroff;
	long horseId;
	long equipmentId;

	public HORSE_PUTONOROFF_REQ(){
	}

	public HORSE_PUTONOROFF_REQ(long seqNum,boolean onOroff,long horseId,long equipmentId){
		this.seqNum = seqNum;
		this.onOroff = onOroff;
		this.horseId = horseId;
		this.equipmentId = equipmentId;
	}

	public HORSE_PUTONOROFF_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		onOroff = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		horseId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		equipmentId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x00000120;
	}

	public String getTypeDescription() {
		return "HORSE_PUTONOROFF_REQ";
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
		len += 8;
		len += 8;
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

			buffer.put((byte)(onOroff==false?0:1));
			buffer.putLong(horseId);
			buffer.putLong(equipmentId);
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
	 *	穿还是脱, 穿true
	 */
	public boolean getOnOroff(){
		return onOroff;
	}

	/**
	 * 设置属性：
	 *	穿还是脱, 穿true
	 */
	public void setOnOroff(boolean onOroff){
		this.onOroff = onOroff;
	}

	/**
	 * 获取属性：
	 *	马的id
	 */
	public long getHorseId(){
		return horseId;
	}

	/**
	 * 设置属性：
	 *	马的id
	 */
	public void setHorseId(long horseId){
		this.horseId = horseId;
	}

	/**
	 * 获取属性：
	 *	装备id
	 */
	public long getEquipmentId(){
		return equipmentId;
	}

	/**
	 * 设置属性：
	 *	装备id
	 */
	public void setEquipmentId(long equipmentId){
		this.equipmentId = equipmentId;
	}

}