package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 举报玩家<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName</td><td>String</td><td>playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiBaoType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>juBaoMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>juBaoMess</td><td>String</td><td>juBaoMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerSelfRecordMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerSelfRecordMess</td><td>String</td><td>playerSelfRecordMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readyMess.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>readyMess[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readyMess[0]</td><td>String</td><td>readyMess[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>readyMess[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readyMess[1]</td><td>String</td><td>readyMess[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>readyMess[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readyMess[2]</td><td>String</td><td>readyMess[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class JUBAO_PLAYER_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	String playerName;
	int jiBaoType;
	int chatType;
	String juBaoMess;
	String playerSelfRecordMess;
	long chatTime;
	String[] readyMess;

	public JUBAO_PLAYER_REQ(){
	}

	public JUBAO_PLAYER_REQ(long seqNum,long playerId,String playerName,int jiBaoType,int chatType,String juBaoMess,String playerSelfRecordMess,long chatTime,String[] readyMess){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.playerName = playerName;
		this.jiBaoType = jiBaoType;
		this.chatType = chatType;
		this.juBaoMess = juBaoMess;
		this.playerSelfRecordMess = playerSelfRecordMess;
		this.chatTime = chatTime;
		this.readyMess = readyMess;
	}

	public JUBAO_PLAYER_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		playerName = new String(content,offset,len,"UTF-8");
		offset += len;
		jiBaoType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		chatType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		juBaoMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		playerSelfRecordMess = new String(content,offset,len,"UTF-8");
		offset += len;
		chatTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		readyMess = new String[len];
		for(int i = 0 ; i < readyMess.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			readyMess[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x00FF0097;
	}

	public String getTypeDescription() {
		return "JUBAO_PLAYER_REQ";
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
		len += 2;
		try{
			len +=playerName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=juBaoMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=playerSelfRecordMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 4;
		for(int i = 0 ; i < readyMess.length; i++){
			len += 2;
			try{
				len += readyMess[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = playerName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(jiBaoType);
			buffer.putInt(chatType);
				try{
			tmpBytes1 = juBaoMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = playerSelfRecordMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(chatTime);
			buffer.putInt(readyMess.length);
			for(int i = 0 ; i < readyMess.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = readyMess[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	被举报人id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	被举报人id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	被举报人name
	 */
	public String getPlayerName(){
		return playerName;
	}

	/**
	 * 设置属性：
	 *	被举报人name
	 */
	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}

	/**
	 * 获取属性：
	 *	type;0:聊天,1:邮件,2:私聊
	 */
	public int getJiBaoType(){
		return jiBaoType;
	}

	/**
	 * 设置属性：
	 *	type;0:聊天,1:邮件,2:私聊
	 */
	public void setJiBaoType(int jiBaoType){
		this.jiBaoType = jiBaoType;
	}

	/**
	 * 获取属性：
	 *	type
	 */
	public int getChatType(){
		return chatType;
	}

	/**
	 * 设置属性：
	 *	type
	 */
	public void setChatType(int chatType){
		this.chatType = chatType;
	}

	/**
	 * 获取属性：
	 *	举报信息
	 */
	public String getJuBaoMess(){
		return juBaoMess;
	}

	/**
	 * 设置属性：
	 *	举报信息
	 */
	public void setJuBaoMess(String juBaoMess){
		this.juBaoMess = juBaoMess;
	}

	/**
	 * 获取属性：
	 *	玩家自己写的原因
	 */
	public String getPlayerSelfRecordMess(){
		return playerSelfRecordMess;
	}

	/**
	 * 设置属性：
	 *	玩家自己写的原因
	 */
	public void setPlayerSelfRecordMess(String playerSelfRecordMess){
		this.playerSelfRecordMess = playerSelfRecordMess;
	}

	/**
	 * 获取属性：
	 *	说话时间
	 */
	public long getChatTime(){
		return chatTime;
	}

	/**
	 * 设置属性：
	 *	说话时间
	 */
	public void setChatTime(long chatTime){
		this.chatTime = chatTime;
	}

	/**
	 * 获取属性：
	 *	预留信息
	 */
	public String[] getReadyMess(){
		return readyMess;
	}

	/**
	 * 设置属性：
	 *	预留信息
	 */
	public void setReadyMess(String[] readyMess){
		this.readyMess = readyMess;
	}

}