package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 成员同意还是不同意结义;队长不同意结义<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class UNITE_AGREEORNO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	boolean result;

	public UNITE_AGREEORNO_RES(){
	}

	public UNITE_AGREEORNO_RES(long seqNum,long playerId,boolean result){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.result = result;
	}

	public UNITE_AGREEORNO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		result = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x7000F031;
	}

	public String getTypeDescription() {
		return "UNITE_AGREEORNO_RES";
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

			buffer.putLong(playerId);
			buffer.put((byte)(result==false?0:1));
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
	 *	用户id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	用户id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	成员同意还是不同意结义(同意为true)
	 */
	public boolean getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	成员同意还是不同意结义(同意为true)
	 */
	public void setResult(boolean result){
		this.result = result;
	}

}