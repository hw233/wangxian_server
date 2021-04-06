package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取玩家消息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerProtobuf.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerProtobuf</td><td>byte[]</td><td>playerProtobuf.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statid.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statid</td><td>int[]</td><td>statid.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>svalue.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>svalue</td><td>long[]</td><td>svalue.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastUpdateTime.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastUpdateTime</td><td>long[]</td><td>lastUpdateTime.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>String</td><td>result.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class CACHE_PLAYER_GET_RES implements ResponseMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	byte[] playerProtobuf;
	long playerid;
	int[] statid;
	long[] svalue;
	long[] lastUpdateTime;
	String result;

	public CACHE_PLAYER_GET_RES(long seqNum,byte[] playerProtobuf,long playerid,int[] statid,long[] svalue,long[] lastUpdateTime,String result){
		this.seqNum = seqNum;
		this.playerProtobuf = playerProtobuf;
		this.playerid = playerid;
		this.statid = statid;
		this.svalue = svalue;
		this.lastUpdateTime = lastUpdateTime;
		this.result = result;
	}

	public CACHE_PLAYER_GET_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 512000) throw new Exception("array length ["+len+"] big than the max length [512000]");
		playerProtobuf = new byte[len];
		System.arraycopy(content,offset,playerProtobuf,0,len);
		offset += len;
		playerid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		statid = new int[len];
		for(int i = 0 ; i < statid.length ; i++){
			statid[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		svalue = new long[len];
		for(int i = 0 ; i < svalue.length ; i++){
			svalue[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		lastUpdateTime = new long[len];
		for(int i = 0 ; i < lastUpdateTime.length ; i++){
			lastUpdateTime[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		result = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x80000002;
	}

	public String getTypeDescription() {
		return "CACHE_PLAYER_GET_RES";
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
		len += playerProtobuf.length;
		len += 8;
		len += 4;
		len += statid.length * 4;
		len += 4;
		len += svalue.length * 8;
		len += 4;
		len += lastUpdateTime.length * 8;
		len += 2;
		try{
			len +=result.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putInt(playerProtobuf.length);
			buffer.put(playerProtobuf);
			buffer.putLong(playerid);
			buffer.putInt(statid.length);
			for(int i = 0 ; i < statid.length; i++){
				buffer.putInt(statid[i]);
			}
			buffer.putInt(svalue.length);
			for(int i = 0 ; i < svalue.length; i++){
				buffer.putLong(svalue[i]);
			}
			buffer.putInt(lastUpdateTime.length);
			for(int i = 0 ; i < lastUpdateTime.length; i++){
				buffer.putLong(lastUpdateTime[i]);
			}
			byte[] tmpBytes1 = result.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public byte[] getPlayerProtobuf(){
		return playerProtobuf;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPlayerProtobuf(byte[] playerProtobuf){
		this.playerProtobuf = playerProtobuf;
	}

	/**
	 * 获取属性：
	 *	角色id
	 */
	public long getPlayerid(){
		return playerid;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPlayerid(long playerid){
		this.playerid = playerid;
	}

	/**
	 * 获取属性：
	 *	统计项
	 */
	public int[] getStatid(){
		return statid;
	}

	/**
	 * 设置属性：
	 *	统计项
	 */
	public void setStatid(int[] statid){
		this.statid = statid;
	}

	/**
	 * 获取属性：
	 *	统计值
	 */
	public long[] getSvalue(){
		return svalue;
	}

	/**
	 * 设置属性：
	 *	统计值
	 */
	public void setSvalue(long[] svalue){
		this.svalue = svalue;
	}

	/**
	 * 获取属性：
	 *	上次更新时间
	 */
	public long[] getLastUpdateTime(){
		return lastUpdateTime;
	}

	/**
	 * 设置属性：
	 *	上次更新时间
	 */
	public void setLastUpdateTime(long[] lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * 获取属性：
	 *	结果说明
	 */
	public String getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果说明
	 */
	public void setResult(String result){
		this.result = result;
	}

}
