package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器在线通知网关<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverName</td><td>String</td><td>serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverIp.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverIp</td><td>String</td><td>serverIp.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>port</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientid</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverid</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SERVER_SYNC_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String serverName;
	int playerNum;
	String serverIp;
	int port;
	byte clientid;
	byte serverid;

	public SERVER_SYNC_REQ(long seqNum,String serverName,int playerNum,String serverIp,int port,byte clientid,byte serverid){
		this.seqNum = seqNum;
		this.serverName = serverName;
		this.playerNum = playerNum;
		this.serverIp = serverIp;
		this.port = port;
		this.clientid = clientid;
		this.serverid = serverid;
	}

	public SERVER_SYNC_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		serverName = new String(content,offset,len,"UTF-8");
		offset += len;
		playerNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		serverIp = new String(content,offset,len,"UTF-8");
		offset += len;
		port = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		clientid = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		serverid = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x0000EB02;
	}

	public String getTypeDescription() {
		return "SERVER_SYNC_REQ";
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
		len += 4;
		len += 2;
		try{
			len +=serverIp.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 1;
		len += 1;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = serverName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(playerNum);
				try{
			tmpBytes1 = serverIp.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(port);
			buffer.put(clientid);
			buffer.put(serverid);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	服务器名称
	 */
	public String getServerName(){
		return serverName;
	}

	/**
	 * 设置属性：
	 *	服务器名称
	 */
	public void setServerName(String serverName){
		this.serverName = serverName;
	}

	/**
	 * 获取属性：
	 *	在线玩家数量
	 */
	public int getPlayerNum(){
		return playerNum;
	}

	/**
	 * 设置属性：
	 *	在线玩家数量
	 */
	public void setPlayerNum(int playerNum){
		this.playerNum = playerNum;
	}

	/**
	 * 获取属性：
	 *	服务器ip
	 */
	public String getServerIp(){
		return serverIp;
	}

	/**
	 * 设置属性：
	 *	服务器ip
	 */
	public void setServerIp(String serverIp){
		this.serverIp = serverIp;
	}

	/**
	 * 获取属性：
	 *	端口
	 */
	public int getPort(){
		return port;
	}

	/**
	 * 设置属性：
	 *	端口
	 */
	public void setPort(int port){
		this.port = port;
	}

	/**
	 * 获取属性：
	 *	clientid
	 */
	public byte getClientid(){
		return clientid;
	}

	/**
	 * 设置属性：
	 *	clientid
	 */
	public void setClientid(byte clientid){
		this.clientid = clientid;
	}

	/**
	 * 获取属性：
	 *	serverid
	 */
	public byte getServerid(){
		return serverid;
	}

	/**
	 * 设置属性：
	 *	serverid
	 */
	public void setServerid(byte serverid){
		this.serverid = serverid;
	}

}