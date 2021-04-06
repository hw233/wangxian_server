package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 确认挑战<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categoryId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class XL_CHALLENGE_SURE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int level;
	long monsterId;
	int categoryId;

	public XL_CHALLENGE_SURE_REQ(){
	}

	public XL_CHALLENGE_SURE_REQ(long seqNum,int level,long monsterId,int categoryId){
		this.seqNum = seqNum;
		this.level = level;
		this.monsterId = monsterId;
		this.categoryId = categoryId;
	}

	public XL_CHALLENGE_SURE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		monsterId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		categoryId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00FFF071;
	}

	public String getTypeDescription() {
		return "XL_CHALLENGE_SURE_REQ";
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
		len += 8;
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

			buffer.putInt(level);
			buffer.putLong(monsterId);
			buffer.putInt(categoryId);
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
	 *	关卡等级
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	关卡等级
	 */
	public void setLevel(int level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	怪物id(为确认挑战而传的)
	 */
	public long getMonsterId(){
		return monsterId;
	}

	/**
	 * 设置属性：
	 *	怪物id(为确认挑战而传的)
	 */
	public void setMonsterId(long monsterId){
		this.monsterId = monsterId;
	}

	/**
	 * 获取属性：
	 *	categoryId(为确认挑战而传的)
	 */
	public int getCategoryId(){
		return categoryId;
	}

	/**
	 * 设置属性：
	 *	categoryId(为确认挑战而传的)
	 */
	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

}