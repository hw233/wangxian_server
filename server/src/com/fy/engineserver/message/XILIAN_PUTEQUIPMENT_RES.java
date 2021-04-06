package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 洗炼放入装备<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>addPropDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>addPropDes</td><td>String</td><td>addPropDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xiLianDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xiLianDes</td><td>String</td><td>xiLianDes.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class XILIAN_PUTEQUIPMENT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long equipmentId;
	String addPropDes;
	String xiLianDes;

	public XILIAN_PUTEQUIPMENT_RES(){
	}

	public XILIAN_PUTEQUIPMENT_RES(long seqNum,long equipmentId,String addPropDes,String xiLianDes){
		this.seqNum = seqNum;
		this.equipmentId = equipmentId;
		this.addPropDes = addPropDes;
		this.xiLianDes = xiLianDes;
	}

	public XILIAN_PUTEQUIPMENT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		equipmentId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		addPropDes = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		xiLianDes = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x70FF0144;
	}

	public String getTypeDescription() {
		return "XILIAN_PUTEQUIPMENT_RES";
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
		len +=addPropDes.getBytes().length;
		len += 2;
		len +=xiLianDes.getBytes().length;
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
			byte[] tmpBytes1;
			tmpBytes1 = addPropDes.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = xiLianDes.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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

	/**
	 * 获取属性：
	 *	附加属性描述
	 */
	public String getAddPropDes(){
		return addPropDes;
	}

	/**
	 * 设置属性：
	 *	附加属性描述
	 */
	public void setAddPropDes(String addPropDes){
		this.addPropDes = addPropDes;
	}

	/**
	 * 获取属性：
	 *	洗练描述
	 */
	public String getXiLianDes(){
		return xiLianDes;
	}

	/**
	 * 设置属性：
	 *	洗练描述
	 */
	public void setXiLianDes(String xiLianDes){
		this.xiLianDes = xiLianDes;
	}

}