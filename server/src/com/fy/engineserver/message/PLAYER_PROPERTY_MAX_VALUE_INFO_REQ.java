package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端这个人物角色的最大属性面板值<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>career</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propertyValues.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propertyValues</td><td>int[]</td><td>propertyValues.length</td><td>*</td></tr>
 * </table>
 */
public class PLAYER_PROPERTY_MAX_VALUE_INFO_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int career;
	int[] propertyValues;

	public PLAYER_PROPERTY_MAX_VALUE_INFO_REQ(){
	}

	public PLAYER_PROPERTY_MAX_VALUE_INFO_REQ(long seqNum,int career,int[] propertyValues){
		this.seqNum = seqNum;
		this.career = career;
		this.propertyValues = propertyValues;
	}

	public PLAYER_PROPERTY_MAX_VALUE_INFO_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		career = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propertyValues = new int[len];
		for(int i = 0 ; i < propertyValues.length ; i++){
			propertyValues[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x0F300035;
	}

	public String getTypeDescription() {
		return "PLAYER_PROPERTY_MAX_VALUE_INFO_REQ";
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
		len += 4;
		len += propertyValues.length * 4;
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

			buffer.putInt(career);
			buffer.putInt(propertyValues.length);
			for(int i = 0 ; i < propertyValues.length; i++){
				buffer.putInt(propertyValues[i]);
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
	 *	职业
	 */
	public int getCareer(){
		return career;
	}

	/**
	 * 设置属性：
	 *	职业
	 */
	public void setCareer(int career){
		this.career = career;
	}

	/**
	 * 获取属性：
	 *	各个属性的最大值,顺序MHP,AP,AP2,AC,AC2,MMP,DAC,HITP,DGP,ACTP,CHP,DCHP,FAP,IAP,WAP,TAP,FRT,IRT,WRT,TRT,DFRT,DIRT,DWRT,DTRT
	 */
	public int[] getPropertyValues(){
		return propertyValues;
	}

	/**
	 * 设置属性：
	 *	各个属性的最大值,顺序MHP,AP,AP2,AC,AC2,MMP,DAC,HITP,DGP,ACTP,CHP,DCHP,FAP,IAP,WAP,TAP,FRT,IRT,WRT,TRT,DFRT,DIRT,DWRT,DTRT
	 */
	public void setPropertyValues(int[] propertyValues){
		this.propertyValues = propertyValues;
	}

}