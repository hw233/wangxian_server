package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求某个技能的详细信息，此信息包括当前级别和下一个级别的信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_SKILLINFO_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	short id;
	short level;
	byte descType;

	public QUERY_SKILLINFO_REQ(){
	}

	public QUERY_SKILLINFO_REQ(long seqNum,short id,short level,byte descType){
		this.seqNum = seqNum;
		this.id = id;
		this.level = level;
		this.descType = descType;
	}

	public QUERY_SKILLINFO_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		level = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		descType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x000000E1;
	}

	public String getTypeDescription() {
		return "QUERY_SKILLINFO_REQ";
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
		len += 2;
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

			buffer.putShort(id);
			buffer.putShort(level);
			buffer.put(descType);
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
	 *	技能编号
	 */
	public short getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	技能编号
	 */
	public void setId(short id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	技能等级
	 */
	public short getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	技能等级
	 */
	public void setLevel(short level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	描述的类型，0标识技能面板的描述，包含下一级信息，1标识快捷键面板的描述，不包含下一级信息
	 */
	public byte getDescType(){
		return descType;
	}

	/**
	 * 设置属性：
	 *	描述的类型，0标识技能面板的描述，包含下一级信息，1标识快捷键面板的描述，不包含下一级信息
	 */
	public void setDescType(byte descType){
		this.descType = descType;
	}

}