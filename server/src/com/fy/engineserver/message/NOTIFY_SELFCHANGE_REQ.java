package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.core.FieldChangeEvent;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器向客户端发送，通知客户端玩家自身变量的改变<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents.length</td><td>int</td><td>4个字节</td><td>FieldChangeEvent数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[0].fieldId</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[0].valueType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[0].valueData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[0].valueData</td><td>byte[]</td><td>changeEvents[0].valueData.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[1].fieldId</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[1].valueType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[1].valueData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[1].valueData</td><td>byte[]</td><td>changeEvents[1].valueData.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[2].fieldId</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[2].valueType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[2].valueData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[2].valueData</td><td>byte[]</td><td>changeEvents[2].valueData.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NOTIFY_SELFCHANGE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	FieldChangeEvent[] changeEvents;

	public NOTIFY_SELFCHANGE_REQ(){
	}

	public NOTIFY_SELFCHANGE_REQ(long seqNum,FieldChangeEvent[] changeEvents){
		this.seqNum = seqNum;
		this.changeEvents = changeEvents;
	}

	public NOTIFY_SELFCHANGE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		changeEvents = new FieldChangeEvent[len];
		for(int i = 0 ; i < changeEvents.length ; i++){
			changeEvents[i] = new FieldChangeEvent();
			changeEvents[i].setFieldId((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			changeEvents[i].setValueType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] valueData_0001 = new byte[len];
			System.arraycopy(content,offset,valueData_0001,0,len);
			offset += len;
			changeEvents[i].setValueData(valueData_0001);
		}
	}

	public int getType() {
		return 0x000000D3;
	}

	public String getTypeDescription() {
		return "NOTIFY_SELFCHANGE_REQ";
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
		for(int i = 0 ; i < changeEvents.length ; i++){
			len += 2;
			len += 1;
			len += 4;
			len += changeEvents[i].getValueData().length * 1;
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

			buffer.putInt(changeEvents.length);
			for(int i = 0 ; i < changeEvents.length ; i++){
				buffer.putShort((short)changeEvents[i].getFieldId());
				buffer.put((byte)changeEvents[i].getValueType());
				buffer.putInt(changeEvents[i].getValueData().length);
				buffer.put(changeEvents[i].getValueData());
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
	 *	玩家自身发生的状态变化
	 */
	public FieldChangeEvent[] getChangeEvents(){
		return changeEvents;
	}

	/**
	 * 设置属性：
	 *	玩家自身发生的状态变化
	 */
	public void setChangeEvents(FieldChangeEvent[] changeEvents){
		this.changeEvents = changeEvents;
	}

}