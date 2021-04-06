package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 创建新的推荐，当返回的所有字段都为空是，表示服务器端发生错误，推荐失败<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>friendMobile.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>friendMobile</td><td>String</td><td>friendMobile.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>friendName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>friendName</td><td>String</td><td>friendName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>url.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>url</td><td>String</td><td>url.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>friendServerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>friendServerName</td><td>String</td><td>friendServerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>friendGameUserName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>friendGameUserName</td><td>String</td><td>friendGameUserName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>friendPlayerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>friendPlayerName</td><td>String</td><td>friendPlayerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>friendPolcamp</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class NEW_RECOMMEND_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String friendMobile;
	String friendName;
	String url;
	String friendServerName;
	String friendGameUserName;
	String friendPlayerName;
	byte friendPolcamp;

	public NEW_RECOMMEND_RES(){
	}

	public NEW_RECOMMEND_RES(long seqNum,String friendMobile,String friendName,String url,String friendServerName,String friendGameUserName,String friendPlayerName,byte friendPolcamp){
		this.seqNum = seqNum;
		this.friendMobile = friendMobile;
		this.friendName = friendName;
		this.url = url;
		this.friendServerName = friendServerName;
		this.friendGameUserName = friendGameUserName;
		this.friendPlayerName = friendPlayerName;
		this.friendPolcamp = friendPolcamp;
	}

	public NEW_RECOMMEND_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		friendMobile = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		friendName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		url = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		friendServerName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		friendGameUserName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		friendPlayerName = new String(content,offset,len,"UTF-8");
		offset += len;
		friendPolcamp = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x7000AF01;
	}

	public String getTypeDescription() {
		return "NEW_RECOMMEND_RES";
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
			len +=friendMobile.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=friendName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=url.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=friendServerName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=friendGameUserName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=friendPlayerName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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
			 tmpBytes1 = friendMobile.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = friendName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = url.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = friendServerName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = friendGameUserName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = friendPlayerName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(friendPolcamp);
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
	 *	被推荐人手机号码
	 */
	public String getFriendMobile(){
		return friendMobile;
	}

	/**
	 * 设置属性：
	 *	被推荐人手机号码
	 */
	public void setFriendMobile(String friendMobile){
		this.friendMobile = friendMobile;
	}

	/**
	 * 获取属性：
	 *	被推荐人姓名
	 */
	public String getFriendName(){
		return friendName;
	}

	/**
	 * 设置属性：
	 *	被推荐人姓名
	 */
	public void setFriendName(String friendName){
		this.friendName = friendName;
	}

	/**
	 * 获取属性：
	 *	被推荐人访问的wap地址，如空则为此用户已在游戏中
	 */
	public String getUrl(){
		return url;
	}

	/**
	 * 设置属性：
	 *	被推荐人访问的wap地址，如空则为此用户已在游戏中
	 */
	public void setUrl(String url){
		this.url = url;
	}

	/**
	 * 获取属性：
	 *	被推荐人在游戏服务器，如不在游戏中为空
	 */
	public String getFriendServerName(){
		return friendServerName;
	}

	/**
	 * 设置属性：
	 *	被推荐人在游戏服务器，如不在游戏中为空
	 */
	public void setFriendServerName(String friendServerName){
		this.friendServerName = friendServerName;
	}

	/**
	 * 获取属性：
	 *	被推荐人在游戏中的账号，如不在游戏中为空
	 */
	public String getFriendGameUserName(){
		return friendGameUserName;
	}

	/**
	 * 设置属性：
	 *	被推荐人在游戏中的账号，如不在游戏中为空
	 */
	public void setFriendGameUserName(String friendGameUserName){
		this.friendGameUserName = friendGameUserName;
	}

	/**
	 * 获取属性：
	 *	被推荐人角色名称，如不在游戏中为空
	 */
	public String getFriendPlayerName(){
		return friendPlayerName;
	}

	/**
	 * 设置属性：
	 *	被推荐人角色名称，如不在游戏中为空
	 */
	public void setFriendPlayerName(String friendPlayerName){
		this.friendPlayerName = friendPlayerName;
	}

	/**
	 * 获取属性：
	 *	被推荐人所在阵营
	 */
	public byte getFriendPolcamp(){
		return friendPolcamp;
	}

	/**
	 * 设置属性：
	 *	被推荐人所在阵营
	 */
	public void setFriendPolcamp(byte friendPolcamp){
		this.friendPolcamp = friendPolcamp;
	}

}