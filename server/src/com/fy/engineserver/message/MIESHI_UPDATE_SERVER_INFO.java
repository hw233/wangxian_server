package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 灭世游戏服务器给网关发送技能数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverName</td><td>String</td><td>serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerOnlineNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxMemInKBytes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totalMemInKBytes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>freeMemInKBytes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cpuUsage</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>diskUsage</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class MIESHI_UPDATE_SERVER_INFO implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String serverName;
	int playerOnlineNum;
	int maxMemInKBytes;
	int totalMemInKBytes;
	int freeMemInKBytes;
	int cpuUsage;
	int diskUsage;

	public MIESHI_UPDATE_SERVER_INFO(long seqNum,String serverName,int playerOnlineNum,int maxMemInKBytes,int totalMemInKBytes,int freeMemInKBytes,int cpuUsage,int diskUsage){
		this.seqNum = seqNum;
		this.serverName = serverName;
		this.playerOnlineNum = playerOnlineNum;
		this.maxMemInKBytes = maxMemInKBytes;
		this.totalMemInKBytes = totalMemInKBytes;
		this.freeMemInKBytes = freeMemInKBytes;
		this.cpuUsage = cpuUsage;
		this.diskUsage = diskUsage;
	}

	public MIESHI_UPDATE_SERVER_INFO(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		serverName = new String(content,offset,len,"utf-8");
		offset += len;
		playerOnlineNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxMemInKBytes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		totalMemInKBytes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		freeMemInKBytes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		cpuUsage = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		diskUsage = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0002A026;
	}

	public String getTypeDescription() {
		return "MIESHI_UPDATE_SERVER_INFO";
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
			len +=serverName.getBytes("utf-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [utf-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = serverName.getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(playerOnlineNum);
			buffer.putInt(maxMemInKBytes);
			buffer.putInt(totalMemInKBytes);
			buffer.putInt(freeMemInKBytes);
			buffer.putInt(cpuUsage);
			buffer.putInt(diskUsage);
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
	 *	在线人数
	 */
	public int getPlayerOnlineNum(){
		return playerOnlineNum;
	}

	/**
	 * 设置属性：
	 *	在线人数
	 */
	public void setPlayerOnlineNum(int playerOnlineNum){
		this.playerOnlineNum = playerOnlineNum;
	}

	/**
	 * 获取属性：
	 *	内存使用情况
	 */
	public int getMaxMemInKBytes(){
		return maxMemInKBytes;
	}

	/**
	 * 设置属性：
	 *	内存使用情况
	 */
	public void setMaxMemInKBytes(int maxMemInKBytes){
		this.maxMemInKBytes = maxMemInKBytes;
	}

	/**
	 * 获取属性：
	 *	内存使用情况
	 */
	public int getTotalMemInKBytes(){
		return totalMemInKBytes;
	}

	/**
	 * 设置属性：
	 *	内存使用情况
	 */
	public void setTotalMemInKBytes(int totalMemInKBytes){
		this.totalMemInKBytes = totalMemInKBytes;
	}

	/**
	 * 获取属性：
	 *	内存使用情况
	 */
	public int getFreeMemInKBytes(){
		return freeMemInKBytes;
	}

	/**
	 * 设置属性：
	 *	内存使用情况
	 */
	public void setFreeMemInKBytes(int freeMemInKBytes){
		this.freeMemInKBytes = freeMemInKBytes;
	}

	/**
	 * 获取属性：
	 *	cpu的百分比
	 */
	public int getCpuUsage(){
		return cpuUsage;
	}

	/**
	 * 设置属性：
	 *	cpu的百分比
	 */
	public void setCpuUsage(int cpuUsage){
		this.cpuUsage = cpuUsage;
	}

	/**
	 * 获取属性：
	 *	硬盘使用的百分比
	 */
	public int getDiskUsage(){
		return diskUsage;
	}

	/**
	 * 设置属性：
	 *	硬盘使用的百分比
	 */
	public void setDiskUsage(int diskUsage){
		this.diskUsage = diskUsage;
	}

}