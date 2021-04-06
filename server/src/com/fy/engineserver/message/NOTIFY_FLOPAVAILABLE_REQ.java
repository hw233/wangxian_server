package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端，那些怪身上有东西可以捡，或者采集NPC有东西可以采集<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>spriteIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>spriteIds</td><td>long[]</td><td>spriteIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avaiableFlag.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avaiableFlag</td><td>boolean[]</td><td>avaiableFlag.length</td><td>*</td></tr>
 * </table>
 */
public class NOTIFY_FLOPAVAILABLE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] spriteIds;
	boolean[] avaiableFlag;

	public NOTIFY_FLOPAVAILABLE_REQ(){
	}

	public NOTIFY_FLOPAVAILABLE_REQ(long seqNum,long[] spriteIds,boolean[] avaiableFlag){
		this.seqNum = seqNum;
		this.spriteIds = spriteIds;
		this.avaiableFlag = avaiableFlag;
	}

	public NOTIFY_FLOPAVAILABLE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		spriteIds = new long[len];
		for(int i = 0 ; i < spriteIds.length ; i++){
			spriteIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avaiableFlag = new boolean[len];
		for(int i = 0 ; i < avaiableFlag.length ; i++){
			avaiableFlag[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
	}

	public int getType() {
		return 0x000000D5;
	}

	public String getTypeDescription() {
		return "NOTIFY_FLOPAVAILABLE_REQ";
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
		len += spriteIds.length * 8;
		len += 4;
		len += avaiableFlag.length;
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

			buffer.putInt(spriteIds.length);
			for(int i = 0 ; i < spriteIds.length; i++){
				buffer.putLong(spriteIds[i]);
			}
			buffer.putInt(avaiableFlag.length);
			for(int i = 0 ; i < avaiableFlag.length; i++){
				buffer.put((byte)(avaiableFlag[i]==false?0:1));
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
	 *	怪或者采集NPC的Id列表
	 */
	public long[] getSpriteIds(){
		return spriteIds;
	}

	/**
	 * 设置属性：
	 *	怪或者采集NPC的Id列表
	 */
	public void setSpriteIds(long[] spriteIds){
		this.spriteIds = spriteIds;
	}

	/**
	 * 获取属性：
	 *	是否可以捡的标识
	 */
	public boolean[] getAvaiableFlag(){
		return avaiableFlag;
	}

	/**
	 * 设置属性：
	 *	是否可以捡的标识
	 */
	public void setAvaiableFlag(boolean[] avaiableFlag){
		this.avaiableFlag = avaiableFlag;
	}

}