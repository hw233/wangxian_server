package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 汇报服务器的超时心跳<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverName</td><td>String</td><td>serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>game.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>game</td><td>String</td><td>game.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>heartbeatTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class REPORT_LONG_HEARTBEAT_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String serverName;
	String game;
	long heartbeatTime;

	public REPORT_LONG_HEARTBEAT_REQ(){
	}

	public REPORT_LONG_HEARTBEAT_REQ(long seqNum,String serverName,String game,long heartbeatTime){
		this.seqNum = seqNum;
		this.serverName = serverName;
		this.game = game;
		this.heartbeatTime = heartbeatTime;
	}

	public REPORT_LONG_HEARTBEAT_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		serverName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		game = new String(content,offset,len,"UTF-8");
		offset += len;
		heartbeatTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x002EE078;
	}

	public String getTypeDescription() {
		return "REPORT_LONG_HEARTBEAT_REQ";
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
		try{
			len +=serverName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=game.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = serverName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = game.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(heartbeatTime);
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
	 *	无帮助说明
	 */
	public String getServerName(){
		return serverName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setServerName(String serverName){
		this.serverName = serverName;
	}

	/**
	 * 获取属性：
	 *	地图名
	 */
	public String getGame(){
		return game;
	}

	/**
	 * 设置属性：
	 *	地图名
	 */
	public void setGame(String game){
		this.game = game;
	}

	/**
	 * 获取属性：
	 *	心跳时长
	 */
	public long getHeartbeatTime(){
		return heartbeatTime;
	}

	/**
	 * 设置属性：
	 *	心跳时长
	 */
	public void setHeartbeatTime(long heartbeatTime){
		this.heartbeatTime = heartbeatTime;
	}

}