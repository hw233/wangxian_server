package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通过用户名获得所有角色<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerIds</td><td>long[]</td><td>playerIds.length</td><td>*</td></tr>
 * </table>
 */
public class CACHE_PLAYERS_GET_RES implements ResponseMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	long[] playerIds;

	public CACHE_PLAYERS_GET_RES(long seqNum,long[] playerIds){
		this.seqNum = seqNum;
		this.playerIds = playerIds;
	}

	public CACHE_PLAYERS_GET_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		playerIds = new long[len];
		for(int i = 0 ; i < playerIds.length ; i++){
			playerIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x80000005;
	}

	public String getTypeDescription() {
		return "CACHE_PLAYERS_GET_RES";
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
		len += playerIds.length * 8;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putInt(playerIds.length);
			for(int i = 0 ; i < playerIds.length; i++){
				buffer.putLong(playerIds[i]);
			}
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	结果
	 */
	public long[] getPlayerIds(){
		return playerIds;
	}

	/**
	 * 设置属性：
	 *	结果
	 */
	public void setPlayerIds(long[] playerIds){
		this.playerIds = playerIds;
	}

}
