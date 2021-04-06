package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 使用寻宝道具，得到在那个位置:很远 ((byte)0),附近((byte)1),正上方((byte)2),右上方((byte)3),右边((byte)4),    右下方((byte)5),正下方((byte)6),左下方((byte)7),左边((byte)8),左上方((byte)9);<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>success</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>position</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class USE_EXPLOREPROPS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean success;
	byte position;

	public USE_EXPLOREPROPS_RES(){
	}

	public USE_EXPLOREPROPS_RES(long seqNum,boolean success,byte position){
		this.seqNum = seqNum;
		this.success = success;
		this.position = position;
	}

	public USE_EXPLOREPROPS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		success = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		position = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x700F0301;
	}

	public String getTypeDescription() {
		return "USE_EXPLOREPROPS_RES";
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

			buffer.put((byte)(success==false?0:1));
			buffer.put(position);
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
	 *	寻宝成功
	 */
	public boolean getSuccess(){
		return success;
	}

	/**
	 * 设置属性：
	 *	寻宝成功
	 */
	public void setSuccess(boolean success){
		this.success = success;
	}

	/**
	 * 获取属性：
	 *	位置索引，左，右
	 */
	public byte getPosition(){
		return position;
	}

	/**
	 * 设置属性：
	 *	位置索引，左，右
	 */
	public void setPosition(byte position){
		this.position = position;
	}

}