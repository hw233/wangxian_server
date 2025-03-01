package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 链路建立后，客户端必须第一个发送此数据包登录，<br>否则发其他任何包，服务器将强制断开链接<br><br><br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resultString.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resultString</td><td>String</td><td>resultString.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>authCode.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>authCode</td><td>String</td><td>authCode.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>RecommendWelcome.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>RecommendWelcome</td><td>String</td><td>RecommendWelcome.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>userName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>userName</td><td>String</td><td>userName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>password.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>password</td><td>String</td><td>password.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class TENCENT_USER_LOGIN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	String resultString;
	String authCode;
	String RecommendWelcome;
	String userName;
	String password;

	public TENCENT_USER_LOGIN_RES(long seqNum,byte result,String resultString,String authCode,String RecommendWelcome,String userName,String password){
		this.seqNum = seqNum;
		this.result = result;
		this.resultString = resultString;
		this.authCode = authCode;
		this.RecommendWelcome = RecommendWelcome;
		this.userName = userName;
		this.password = password;
	}

	public TENCENT_USER_LOGIN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		resultString = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		authCode = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		RecommendWelcome = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		userName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		password = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8002A013;
	}

	public String getTypeDescription() {
		return "TENCENT_USER_LOGIN_RES";
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
		len += 1;
		len += 2;
		try{
			len +=resultString.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=authCode.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=RecommendWelcome.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=userName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=password.getBytes("UTF-8").length;
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
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.put(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = resultString.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = authCode.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = RecommendWelcome.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = userName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = password.getBytes("UTF-8");
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
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	登录的结果，0表示成功登陆，1表示密码不匹配，2表示用户不存在，3此帐号正在被其他人使用，6表示包格式错误
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	登录的结果，0表示成功登陆，1表示密码不匹配，2表示用户不存在，3此帐号正在被其他人使用，6表示包格式错误
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	错误信息
	 */
	public String getResultString(){
		return resultString;
	}

	/**
	 * 设置属性：
	 *	错误信息
	 */
	public void setResultString(String resultString){
		this.resultString = resultString;
	}

	/**
	 * 获取属性：
	 *	根据账户加密后的串，用于后期验证
	 */
	public String getAuthCode(){
		return authCode;
	}

	/**
	 * 设置属性：
	 *	根据账户加密后的串，用于后期验证
	 */
	public void setAuthCode(String authCode){
		this.authCode = authCode;
	}

	/**
	 * 获取属性：
	 *	推荐朋友玩游戏的欢迎语
	 */
	public String getRecommendWelcome(){
		return RecommendWelcome;
	}

	/**
	 * 设置属性：
	 *	推荐朋友玩游戏的欢迎语
	 */
	public void setRecommendWelcome(String RecommendWelcome){
		this.RecommendWelcome = RecommendWelcome;
	}

	/**
	 * 获取属性：
	 *	帐号
	 */
	public String getUserName(){
		return userName;
	}

	/**
	 * 设置属性：
	 *	帐号
	 */
	public void setUserName(String userName){
		this.userName = userName;
	}

	/**
	 * 获取属性：
	 *	密码
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * 设置属性：
	 *	密码
	 */
	public void setPassword(String password){
		this.password = password;
	}

}
