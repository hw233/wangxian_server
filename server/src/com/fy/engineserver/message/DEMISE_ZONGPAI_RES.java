package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 宗主禅让<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>password.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>password</td><td>String</td><td>password.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>passwordHint.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>passwordHint</td><td>String</td><td>passwordHint.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>masterName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>masterName</td><td>String</td><td>masterName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>onLineNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fanrongdu</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuId</td><td>long[]</td><td>jiazuId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuName[0]</td><td>String</td><td>jiazuName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuName[1]</td><td>String</td><td>jiazuName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuName[2]</td><td>String</td><td>jiazuName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBar.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBar[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBar[0]</td><td>String</td><td>jiazuBar[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBar[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBar[1]</td><td>String</td><td>jiazuBar[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBar[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBar[2]</td><td>String</td><td>jiazuBar[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuLevel.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuLevel</td><td>byte[]</td><td>jiazuLevel.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerId</td><td>long[]</td><td>playerId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[0]</td><td>String</td><td>playerName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[1]</td><td>String</td><td>playerName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[2]</td><td>String</td><td>playerName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class DEMISE_ZONGPAI_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String password;
	String passwordHint;
	String masterName;
	int onLineNum;
	int fanrongdu;
	long[] jiazuId;
	String[] jiazuName;
	String[] jiazuBar;
	byte[] jiazuLevel;
	long[] playerId;
	String[] playerName;

	public DEMISE_ZONGPAI_RES(){
	}

	public DEMISE_ZONGPAI_RES(long seqNum,String password,String passwordHint,String masterName,int onLineNum,int fanrongdu,long[] jiazuId,String[] jiazuName,String[] jiazuBar,byte[] jiazuLevel,long[] playerId,String[] playerName){
		this.seqNum = seqNum;
		this.password = password;
		this.passwordHint = passwordHint;
		this.masterName = masterName;
		this.onLineNum = onLineNum;
		this.fanrongdu = fanrongdu;
		this.jiazuId = jiazuId;
		this.jiazuName = jiazuName;
		this.jiazuBar = jiazuBar;
		this.jiazuLevel = jiazuLevel;
		this.playerId = playerId;
		this.playerName = playerName;
	}

	public DEMISE_ZONGPAI_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		password = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		passwordHint = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		masterName = new String(content,offset,len,"UTF-8");
		offset += len;
		onLineNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		fanrongdu = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazuId = new long[len];
		for(int i = 0 ; i < jiazuId.length ; i++){
			jiazuId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazuName = new String[len];
		for(int i = 0 ; i < jiazuName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazuBar = new String[len];
		for(int i = 0 ; i < jiazuBar.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuBar[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazuLevel = new byte[len];
		System.arraycopy(content,offset,jiazuLevel,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerId = new long[len];
		for(int i = 0 ; i < playerId.length ; i++){
			playerId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerName = new String[len];
		for(int i = 0 ; i < playerName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			playerName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x7000F116;
	}

	public String getTypeDescription() {
		return "DEMISE_ZONGPAI_RES";
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
			len +=password.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=passwordHint.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=masterName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += jiazuId.length * 8;
		len += 4;
		for(int i = 0 ; i < jiazuName.length; i++){
			len += 2;
			try{
				len += jiazuName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < jiazuBar.length; i++){
			len += 2;
			try{
				len += jiazuBar[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += jiazuLevel.length;
		len += 4;
		len += playerId.length * 8;
		len += 4;
		for(int i = 0 ; i < playerName.length; i++){
			len += 2;
			try{
				len += playerName[i].getBytes("UTF-8").length;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = password.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = passwordHint.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = masterName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(onLineNum);
			buffer.putInt(fanrongdu);
			buffer.putInt(jiazuId.length);
			for(int i = 0 ; i < jiazuId.length; i++){
				buffer.putLong(jiazuId[i]);
			}
			buffer.putInt(jiazuName.length);
			for(int i = 0 ; i < jiazuName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = jiazuName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(jiazuBar.length);
			for(int i = 0 ; i < jiazuBar.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = jiazuBar[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(jiazuLevel.length);
			buffer.put(jiazuLevel);
			buffer.putInt(playerId.length);
			for(int i = 0 ; i < playerId.length; i++){
				buffer.putLong(playerId[i]);
			}
			buffer.putInt(playerName.length);
			for(int i = 0 ; i < playerName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = playerName[i].getBytes("UTF-8");
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
	 *	无帮助说明
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * 获取属性：
	 *	宗派的密码提示
	 */
	public String getPasswordHint(){
		return passwordHint;
	}

	/**
	 * 设置属性：
	 *	宗派的密码提示
	 */
	public void setPasswordHint(String passwordHint){
		this.passwordHint = passwordHint;
	}

	/**
	 * 获取属性：
	 *	族长name
	 */
	public String getMasterName(){
		return masterName;
	}

	/**
	 * 设置属性：
	 *	族长name
	 */
	public void setMasterName(String masterName){
		this.masterName = masterName;
	}

	/**
	 * 获取属性：
	 *	在线人数
	 */
	public int getOnLineNum(){
		return onLineNum;
	}

	/**
	 * 设置属性：
	 *	在线人数
	 */
	public void setOnLineNum(int onLineNum){
		this.onLineNum = onLineNum;
	}

	/**
	 * 获取属性：
	 *	繁荣度
	 */
	public int getFanrongdu(){
		return fanrongdu;
	}

	/**
	 * 设置属性：
	 *	繁荣度
	 */
	public void setFanrongdu(int fanrongdu){
		this.fanrongdu = fanrongdu;
	}

	/**
	 * 获取属性：
	 *	家族id
	 */
	public long[] getJiazuId(){
		return jiazuId;
	}

	/**
	 * 设置属性：
	 *	家族id
	 */
	public void setJiazuId(long[] jiazuId){
		this.jiazuId = jiazuId;
	}

	/**
	 * 获取属性：
	 *	家族names
	 */
	public String[] getJiazuName(){
		return jiazuName;
	}

	/**
	 * 设置属性：
	 *	家族names
	 */
	public void setJiazuName(String[] jiazuName){
		this.jiazuName = jiazuName;
	}

	/**
	 * 获取属性：
	 *	家族徽章
	 */
	public String[] getJiazuBar(){
		return jiazuBar;
	}

	/**
	 * 设置属性：
	 *	家族徽章
	 */
	public void setJiazuBar(String[] jiazuBar){
		this.jiazuBar = jiazuBar;
	}

	/**
	 * 获取属性：
	 *	家族级别
	 */
	public byte[] getJiazuLevel(){
		return jiazuLevel;
	}

	/**
	 * 设置属性：
	 *	家族级别
	 */
	public void setJiazuLevel(byte[] jiazuLevel){
		this.jiazuLevel = jiazuLevel;
	}

	/**
	 * 获取属性：
	 *	族长id
	 */
	public long[] getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	族长id
	 */
	public void setPlayerId(long[] playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	族长names
	 */
	public String[] getPlayerName(){
		return playerName;
	}

	/**
	 * 设置属性：
	 *	族长names
	 */
	public void setPlayerName(String[] playerName){
		this.playerName = playerName;
	}

}