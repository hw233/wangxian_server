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
 * <tr bgcolor="#FAFAFA" align="center"><td>userName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>userName</td><td>String</td><td>userName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName</td><td>String</td><td>playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerCareer</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>action</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class MIESHI_UPDATE_PLAYER_INFO implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String serverName;
	String userName;
	String playerName;
	int playerLevel;
	int playerCareer;
	int action;

	public MIESHI_UPDATE_PLAYER_INFO(){
	}

	public MIESHI_UPDATE_PLAYER_INFO(long seqNum,String serverName,String userName,String playerName,int playerLevel,int playerCareer,int action){
		this.seqNum = seqNum;
		this.serverName = serverName;
		this.userName = userName;
		this.playerName = playerName;
		this.playerLevel = playerLevel;
		this.playerCareer = playerCareer;
		this.action = action;
	}

	public MIESHI_UPDATE_PLAYER_INFO(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		serverName = new String(content,offset,len,"utf-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		userName = new String(content,offset,len,"utf-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		playerName = new String(content,offset,len,"utf-8");
		offset += len;
		playerLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		playerCareer = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		action = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0002A025;
	}

	public String getTypeDescription() {
		return "MIESHI_UPDATE_PLAYER_INFO";
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
		len += 2;
		try{
			len +=userName.getBytes("utf-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [utf-8]",e);
		}
		len += 2;
		try{
			len +=playerName.getBytes("utf-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [utf-8]",e);
		}
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
				try{
			tmpBytes1 = userName.getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = playerName.getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(playerLevel);
			buffer.putInt(playerCareer);
			buffer.putInt(action);
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
	 *	无帮助说明
	 */
	public String getUserName(){
		return userName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setUserName(String userName){
		this.userName = userName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String getPlayerName(){
		return playerName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getPlayerLevel(){
		return playerLevel;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPlayerLevel(int playerLevel){
		this.playerLevel = playerLevel;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getPlayerCareer(){
		return playerCareer;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPlayerCareer(int playerCareer){
		this.playerCareer = playerCareer;
	}

	/**
	 * 获取属性：
	 *	0=创建角色，1=进入游戏，2=离开游戏，3=删除角色
	 */
	public int getAction(){
		return action;
	}

	/**
	 * 设置属性：
	 *	0=创建角色，1=进入游戏，2=离开游戏，3=删除角色
	 */
	public void setAction(int action){
		this.action = action;
	}

}