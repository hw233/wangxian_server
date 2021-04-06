package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获得服务器列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[0]</td><td>String</td><td>names[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[1]</td><td>String</td><td>names[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[2]</td><td>String</td><td>names[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerCount.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerCount</td><td>int[]</td><td>playerCount.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>status.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>status</td><td>byte[]</td><td>status.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>host.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>host[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>host[0]</td><td>String</td><td>host[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>host[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>host[1]</td><td>String</td><td>host[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>host[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>host[2]</td><td>String</td><td>host[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>port.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>port</td><td>int[]</td><td>port.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientIDs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>clientIDs</td><td>byte[]</td><td>clientIDs.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverIDs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverIDs</td><td>byte[]</td><td>serverIDs.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class GET_SERVER_NEW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] names;
	int[] playerCount;
	byte[] status;
	String[] host;
	int[] port;
	byte[] clientIDs;
	byte[] serverIDs;

	public GET_SERVER_NEW_RES(long seqNum,String[] names,int[] playerCount,byte[] status,String[] host,int[] port,byte[] clientIDs,byte[] serverIDs){
		this.seqNum = seqNum;
		this.names = names;
		this.playerCount = playerCount;
		this.status = status;
		this.host = host;
		this.port = port;
		this.clientIDs = clientIDs;
		this.serverIDs = serverIDs;
	}

	public GET_SERVER_NEW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		names = new String[len];
		for(int i = 0 ; i < names.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			names[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerCount = new int[len];
		for(int i = 0 ; i < playerCount.length ; i++){
			playerCount[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		status = new byte[len];
		System.arraycopy(content,offset,status,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		host = new String[len];
		for(int i = 0 ; i < host.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			host[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		port = new int[len];
		for(int i = 0 ; i < port.length ; i++){
			port[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		clientIDs = new byte[len];
		System.arraycopy(content,offset,clientIDs,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		serverIDs = new byte[len];
		System.arraycopy(content,offset,serverIDs,0,len);
		offset += len;
	}

	public int getType() {
		return 0x8000F016;
	}

	public String getTypeDescription() {
		return "GET_SERVER_NEW_RES";
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
		for(int i = 0 ; i < names.length; i++){
			len += 2;
			try{
				len += names[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += playerCount.length * 4;
		len += 4;
		len += status.length;
		len += 4;
		for(int i = 0 ; i < host.length; i++){
			len += 2;
			len += host[i].getBytes().length;
		}
		len += 4;
		len += port.length * 4;
		len += 4;
		len += clientIDs.length;
		len += 4;
		len += serverIDs.length;
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

			buffer.putInt(names.length);
			for(int i = 0 ; i < names.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = names[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(playerCount.length);
			for(int i = 0 ; i < playerCount.length; i++){
				buffer.putInt(playerCount[i]);
			}
			buffer.putInt(status.length);
			buffer.put(status);
			buffer.putInt(host.length);
			for(int i = 0 ; i < host.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = host[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(port.length);
			for(int i = 0 ; i < port.length; i++){
				buffer.putInt(port[i]);
			}
			buffer.putInt(clientIDs.length);
			buffer.put(clientIDs);
			buffer.putInt(serverIDs.length);
			buffer.put(serverIDs);
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
	public String[] getNames(){
		return names;
	}

	/**
	 * 设置属性：
	 *	服务器名称
	 */
	public void setNames(String[] names){
		this.names = names;
	}

	/**
	 * 获取属性：
	 *	玩家数量
	 */
	public int[] getPlayerCount(){
		return playerCount;
	}

	/**
	 * 设置属性：
	 *	玩家数量
	 */
	public void setPlayerCount(int[] playerCount){
		this.playerCount = playerCount;
	}

	/**
	 * 获取属性：
	 *	服务器的状态:0-推荐,1-良好,2-繁忙,3-爆满
	 */
	public byte[] getStatus(){
		return status;
	}

	/**
	 * 设置属性：
	 *	服务器的状态:0-推荐,1-良好,2-繁忙,3-爆满
	 */
	public void setStatus(byte[] status){
		this.status = status;
	}

	/**
	 * 获取属性：
	 *	ip:port
	 */
	public String[] getHost(){
		return host;
	}

	/**
	 * 设置属性：
	 *	ip:port
	 */
	public void setHost(String[] host){
		this.host = host;
	}

	/**
	 * 获取属性：
	 *	port
	 */
	public int[] getPort(){
		return port;
	}

	/**
	 * 设置属性：
	 *	port
	 */
	public void setPort(int[] port){
		this.port = port;
	}

	/**
	 * 获取属性：
	 *	客户ID，用于建立长连接后的第一条指令
	 */
	public byte[] getClientIDs(){
		return clientIDs;
	}

	/**
	 * 设置属性：
	 *	客户ID，用于建立长连接后的第一条指令
	 */
	public void setClientIDs(byte[] clientIDs){
		this.clientIDs = clientIDs;
	}

	/**
	 * 获取属性：
	 *	服务器ID，用于建立长连接后的第一条指令
	 */
	public byte[] getServerIDs(){
		return serverIDs;
	}

	/**
	 * 设置属性：
	 *	服务器ID，用于建立长连接后的第一条指令
	 */
	public void setServerIDs(byte[] serverIDs){
		this.serverIDs = serverIDs;
	}

}