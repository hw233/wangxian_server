package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 镶嵌挖取请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshiType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshiId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>opType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class HUNSHI_PUTON_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long horseId;
	int hunshiType;
	long hunshiId;
	int index;
	int opType;

	public HUNSHI_PUTON_REQ(){
	}

	public HUNSHI_PUTON_REQ(long seqNum,long horseId,int hunshiType,long hunshiId,int index,int opType){
		this.seqNum = seqNum;
		this.horseId = horseId;
		this.hunshiType = hunshiType;
		this.hunshiId = hunshiId;
		this.index = index;
		this.opType = opType;
	}

	public HUNSHI_PUTON_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		horseId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		hunshiType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		hunshiId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		opType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00FFF026;
	}

	public String getTypeDescription() {
		return "HUNSHI_PUTON_REQ";
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
		len += 4;
		len += 8;
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

			buffer.putLong(horseId);
			buffer.putInt(hunshiType);
			buffer.putLong(hunshiId);
			buffer.putInt(index);
			buffer.putInt(opType);
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
	 *	坐骑id
	 */
	public long getHorseId(){
		return horseId;
	}

	/**
	 * 设置属性：
	 *	坐骑id
	 */
	public void setHorseId(long horseId){
		this.horseId = horseId;
	}

	/**
	 * 获取属性：
	 *	魂石类型：0-魂石；1套装石
	 */
	public int getHunshiType(){
		return hunshiType;
	}

	/**
	 * 设置属性：
	 *	魂石类型：0-魂石；1套装石
	 */
	public void setHunshiType(int hunshiType){
		this.hunshiType = hunshiType;
	}

	/**
	 * 获取属性：
	 *	要镶嵌或挖取的魂石id
	 */
	public long getHunshiId(){
		return hunshiId;
	}

	/**
	 * 设置属性：
	 *	要镶嵌或挖取的魂石id
	 */
	public void setHunshiId(long hunshiId){
		this.hunshiId = hunshiId;
	}

	/**
	 * 获取属性：
	 *	要镶嵌或挖取的位置
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	要镶嵌或挖取的位置
	 */
	public void setIndex(int index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	操作类型：0-镶嵌；-1-挖取
	 */
	public int getOpType(){
		return opType;
	}

	/**
	 * 设置属性：
	 *	操作类型：0-镶嵌；-1-挖取
	 */
	public void setOpType(int opType){
		this.opType = opType;
	}

}