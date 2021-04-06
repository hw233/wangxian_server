package com.fy.boss.message;

import com.fy.boss.message.BossMessageFactory;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 注册一个通行证<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>registerClientId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>registerClientId</td><td>String</td><td>registerClientId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>passwd.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>passwd</td><td>String</td><td>passwd.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nickName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nickName</td><td>String</td><td>nickName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fromWhere.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fromWhere</td><td>String</td><td>fromWhere.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>registerChannel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>registerChannel</td><td>String</td><td>registerChannel.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>registerMobile.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>registerMobile</td><td>String</td><td>registerMobile.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>registerMobileOs.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>registerMobileOs</td><td>String</td><td>registerMobileOs.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>registerMobileType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>registerMobileType</td><td>String</td><td>registerMobileType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>registerNetworkMode.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>registerNetworkMode</td><td>String</td><td>registerNetworkMode.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PASSPORT_REGISTER_REQ implements RequestMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	String registerClientId;
	String username;
	String passwd;
	String nickName;
	String fromWhere;
	String registerChannel;
	String registerMobile;
	String registerMobileOs;
	String registerMobileType;
	String registerNetworkMode;

	public PASSPORT_REGISTER_REQ(){
	}

	public PASSPORT_REGISTER_REQ(long seqNum,String registerClientId,String username,String passwd,String nickName,String fromWhere,String registerChannel,String registerMobile,String registerMobileOs,String registerMobileType,String registerNetworkMode){
		this.seqNum = seqNum;
		this.registerClientId = registerClientId;
		this.username = username;
		this.passwd = passwd;
		this.nickName = nickName;
		this.fromWhere = fromWhere;
		this.registerChannel = registerChannel;
		this.registerMobile = registerMobile;
		this.registerMobileOs = registerMobileOs;
		this.registerMobileType = registerMobileType;
		this.registerNetworkMode = registerNetworkMode;
	}

	public PASSPORT_REGISTER_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		registerClientId = new String(content,offset,len,"UTF-8");
		offset += len;
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
		nickName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		fromWhere = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		registerChannel = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		registerMobile = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		registerMobileOs = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		registerMobileType = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		registerNetworkMode = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x0000E000;
	}

	public String getTypeDescription() {
		return "PASSPORT_REGISTER_REQ";
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
			len +=registerClientId.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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
		try{
			len +=nickName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=fromWhere.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=registerChannel.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		len +=registerMobile.getBytes().length;
		len += 2;
		len +=registerMobileOs.getBytes().length;
		len += 2;
		len +=registerMobileType.getBytes().length;
		len += 2;
		try{
			len +=registerNetworkMode.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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
			 tmpBytes1 = registerClientId.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
				try{
			tmpBytes1 = nickName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = fromWhere.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = registerChannel.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = registerMobile.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = registerMobileOs.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = registerMobileType.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = registerNetworkMode.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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
	 *	注册clientId
	 */
	public String getRegisterClientId(){
		return registerClientId;
	}

	/**
	 * 设置属性：
	 *	注册clientId
	 */
	public void setRegisterClientId(String registerClientId){
		this.registerClientId = registerClientId;
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
	 *	账户别名
	 */
	public String getNickName(){
		return nickName;
	}

	/**
	 * 设置属性：
	 *	账户别名
	 */
	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	/**
	 * 获取属性：
	 *	账号来源
	 */
	public String getFromWhere(){
		return fromWhere;
	}

	/**
	 * 设置属性：
	 *	账号来源
	 */
	public void setFromWhere(String fromWhere){
		this.fromWhere = fromWhere;
	}

	/**
	 * 获取属性：
	 *	注册渠道
	 */
	public String getRegisterChannel(){
		return registerChannel;
	}

	/**
	 * 设置属性：
	 *	注册渠道
	 */
	public void setRegisterChannel(String registerChannel){
		this.registerChannel = registerChannel;
	}

	/**
	 * 获取属性：
	 *	注册手机号码
	 */
	public String getRegisterMobile(){
		return registerMobile;
	}

	/**
	 * 设置属性：
	 *	注册手机号码
	 */
	public void setRegisterMobile(String registerMobile){
		this.registerMobile = registerMobile;
	}

	/**
	 * 获取属性：
	 *	注册手机平台
	 */
	public String getRegisterMobileOs(){
		return registerMobileOs;
	}

	/**
	 * 设置属性：
	 *	注册手机平台
	 */
	public void setRegisterMobileOs(String registerMobileOs){
		this.registerMobileOs = registerMobileOs;
	}

	/**
	 * 获取属性：
	 *	注册手机机型
	 */
	public String getRegisterMobileType(){
		return registerMobileType;
	}

	/**
	 * 设置属性：
	 *	注册手机机型
	 */
	public void setRegisterMobileType(String registerMobileType){
		this.registerMobileType = registerMobileType;
	}

	/**
	 * 获取属性：
	 *	注册时联网方式
	 */
	public String getRegisterNetworkMode(){
		return registerNetworkMode;
	}

	/**
	 * 设置属性：
	 *	注册时联网方式
	 */
	public void setRegisterNetworkMode(String registerNetworkMode){
		this.registerNetworkMode = registerNetworkMode;
	}

}