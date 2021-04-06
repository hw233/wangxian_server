package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 祈福开始或重新祈福<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cliffordType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cliffordIndex</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CLIFFORD_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte cliffordType;
	byte cliffordIndex;

	public CLIFFORD_REQ(){
	}

	public CLIFFORD_REQ(long seqNum,byte cliffordType,byte cliffordIndex){
		this.seqNum = seqNum;
		this.cliffordType = cliffordType;
		this.cliffordIndex = cliffordIndex;
	}

	public CLIFFORD_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		cliffordType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		cliffordIndex = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x0F300017;
	}

	public String getTypeDescription() {
		return "CLIFFORD_REQ";
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

			buffer.put(cliffordType);
			buffer.put(cliffordIndex);
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
	 *	0为单次祈福，1为全部祈福
	 */
	public byte getCliffordType(){
		return cliffordType;
	}

	/**
	 * 设置属性：
	 *	0为单次祈福，1为全部祈福
	 */
	public void setCliffordType(byte cliffordType){
		this.cliffordType = cliffordType;
	}

	/**
	 * 获取属性：
	 *	祈福位置
	 */
	public byte getCliffordIndex(){
		return cliffordIndex;
	}

	/**
	 * 设置属性：
	 *	祈福位置
	 */
	public void setCliffordIndex(byte cliffordIndex){
		this.cliffordIndex = cliffordIndex;
	}

}