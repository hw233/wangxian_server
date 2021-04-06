package com.fy.boss.message;

import com.fy.boss.message.BossMessageFactory;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 登陆<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>passwd.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>passwd</td><td>String</td><td>passwd.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastLoginClientId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastLoginClientId</td><td>String</td><td>lastLoginClientId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastLoginChannel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastLoginChannel</td><td>String</td><td>lastLoginChannel.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastLoginMobileOs.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastLoginMobileOs</td><td>String</td><td>lastLoginMobileOs.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastLoginMobileType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastLoginMobileType</td><td>String</td><td>lastLoginMobileType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastLoginNetworkMode.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastLoginNetworkMode</td><td>String</td><td>lastLoginNetworkMode.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PASSPORT_LOGIN_REQ implements RequestMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	String username;
	String passwd;
	String lastLoginClientId;
	String lastLoginChannel;
	String lastLoginMobileOs;
	String lastLoginMobileType;
	String lastLoginNetworkMode;

	public PASSPORT_LOGIN_REQ(){
	}

	public PASSPORT_LOGIN_REQ(long seqNum,String username,String passwd,String lastLoginClientId,String lastLoginChannel,String lastLoginMobileOs,String lastLoginMobileType,String lastLoginNetworkMode){
		this.seqNum = seqNum;
		this.username = username;
		this.passwd = passwd;
		this.lastLoginClientId = lastLoginClientId;
		this.lastLoginChannel = lastLoginChannel;
		this.lastLoginMobileOs = lastLoginMobileOs;
		this.lastLoginMobileType = lastLoginMobileType;
		this.lastLoginNetworkMode = lastLoginNetworkMode;
	}

	public PASSPORT_LOGIN_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		username = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		passwd = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		lastLoginClientId = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		lastLoginChannel = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		lastLoginMobileOs = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		lastLoginMobileType = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		lastLoginNetworkMode = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x0000E001;
	}

	public String getTypeDescription() {
		return "PASSPORT_LOGIN_REQ";
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
			len +=username.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=passwd.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		len +=lastLoginClientId.getBytes().length;
		len += 2;
		try{
			len +=lastLoginChannel.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		len +=lastLoginMobileOs.getBytes().length;
		len += 2;
		len +=lastLoginMobileType.getBytes().length;
		len += 2;
		len +=lastLoginNetworkMode.getBytes().length;
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
			 tmpBytes1 = username.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = passwd.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = lastLoginClientId.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = lastLoginChannel.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = lastLoginMobileOs.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = lastLoginMobileType.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = lastLoginNetworkMode.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	账户名
	 */
	public String getUsername(){
		return username;
	}

	/**
	 * 设置属性：
	 *	账户名
	 */
	public void setUsername(String username){
		this.username = username;
	}

	/**
	 * 获取属性：
	 *	密码
	 */
	public String getPasswd(){
		return passwd;
	}

	/**
	 * 设置属性：
	 *	密码
	 */
	public void setPasswd(String passwd){
		this.passwd = passwd;
	}

	/**
	 * 获取属性：
	 *	最后登录的clientid
	 */
	public String getLastLoginClientId(){
		return lastLoginClientId;
	}

	/**
	 * 设置属性：
	 *	最后登录的clientid
	 */
	public void setLastLoginClientId(String lastLoginClientId){
		this.lastLoginClientId = lastLoginClientId;
	}

	/**
	 * 获取属性：
	 *	最后登录的渠道
	 */
	public String getLastLoginChannel(){
		return lastLoginChannel;
	}

	/**
	 * 设置属性：
	 *	最后登录的渠道
	 */
	public void setLastLoginChannel(String lastLoginChannel){
		this.lastLoginChannel = lastLoginChannel;
	}

	/**
	 * 获取属性：
	 *	最后登录手机平台
	 */
	public String getLastLoginMobileOs(){
		return lastLoginMobileOs;
	}

	/**
	 * 设置属性：
	 *	最后登录手机平台
	 */
	public void setLastLoginMobileOs(String lastLoginMobileOs){
		this.lastLoginMobileOs = lastLoginMobileOs;
	}

	/**
	 * 获取属性：
	 *	最后登录机型
	 */
	public String getLastLoginMobileType(){
		return lastLoginMobileType;
	}

	/**
	 * 设置属性：
	 *	最后登录机型
	 */
	public void setLastLoginMobileType(String lastLoginMobileType){
		this.lastLoginMobileType = lastLoginMobileType;
	}

	/**
	 * 获取属性：
	 *	最后登录联网方式
	 */
	public String getLastLoginNetworkMode(){
		return lastLoginNetworkMode;
	}

	/**
	 * 设置属性：
	 *	最后登录联网方式
	 */
	public void setLastLoginNetworkMode(String lastLoginNetworkMode){
		this.lastLoginNetworkMode = lastLoginNetworkMode;
	}

}