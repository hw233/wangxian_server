package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求装备重置<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resetType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class EQUIPMENT_RESET_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int index;
	byte resetType;
	long articleId;

	public EQUIPMENT_RESET_REQ(){
	}

	public EQUIPMENT_RESET_REQ(long seqNum,int index,byte resetType,long articleId){
		this.seqNum = seqNum;
		this.index = index;
		this.resetType = resetType;
		this.articleId = articleId;
	}

	public EQUIPMENT_RESET_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		resetType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		articleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x000000FE;
	}

	public String getTypeDescription() {
		return "EQUIPMENT_RESET_REQ";
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
		len += 1;
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

			buffer.putInt(index);
			buffer.put(resetType);
			buffer.putLong(articleId);
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
	 *	装备在背包的位置
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	装备在背包的位置
	 */
	public void setIndex(int index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	申请类型，0代表打开装备重置面板，1代表装备重置
	 */
	public byte getResetType(){
		return resetType;
	}

	/**
	 * 设置属性：
	 *	申请类型，0代表打开装备重置面板，1代表装备重置
	 */
	public void setResetType(byte resetType){
		this.resetType = resetType;
	}

	/**
	 * 获取属性：
	 *	物品实体的唯一标识
	 */
	public long getArticleId(){
		return articleId;
	}

	/**
	 * 设置属性：
	 *	物品实体的唯一标识
	 */
	public void setArticleId(long articleId){
		this.articleId = articleId;
	}

}