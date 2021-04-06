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
 * <tr bgcolor="#FAFAFA" align="center"><td>passportid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>passwd.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>passwd</td><td>String</td><td>passwd.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nickName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nickName</td><td>String</td><td>nickName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>secretQuestion.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>secretQuestion</td><td>String</td><td>secretQuestion.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>secretAnswer.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>secretAnswer</td><td>String</td><td>secretAnswer.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastQuestionSetDate</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isSetSecretQuestion</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>status</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastUpdateStatusDate</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ucPassword.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ucPassword</td><td>String</td><td>ucPassword.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PASSPORT_UPDATE_REQ implements RequestMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	long passportid;
	String username;
	String passwd;
	String nickName;
	String secretQuestion;
	String secretAnswer;
	long lastQuestionSetDate;
	boolean isSetSecretQuestion;
	int status;
	long lastUpdateStatusDate;
	String ucPassword;

	public PASSPORT_UPDATE_REQ(){
	}

	public PASSPORT_UPDATE_REQ(long seqNum,long passportid,String username,String passwd,String nickName,String secretQuestion,String secretAnswer,long lastQuestionSetDate,boolean isSetSecretQuestion,int status,long lastUpdateStatusDate,String ucPassword){
		this.seqNum = seqNum;
		this.passportid = passportid;
		this.username = username;
		this.passwd = passwd;
		this.nickName = nickName;
		this.secretQuestion = secretQuestion;
		this.secretAnswer = secretAnswer;
		this.lastQuestionSetDate = lastQuestionSetDate;
		this.isSetSecretQuestion = isSetSecretQuestion;
		this.status = status;
		this.lastUpdateStatusDate = lastUpdateStatusDate;
		this.ucPassword = ucPassword;
	}

	public PASSPORT_UPDATE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		passportid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
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
		nickName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		secretQuestion = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		secretAnswer = new String(content,offset,len);
		offset += len;
		lastQuestionSetDate = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		isSetSecretQuestion = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		status = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		lastUpdateStatusDate = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		ucPassword = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x0000E004;
	}

	public String getTypeDescription() {
		return "PASSPORT_UPDATE_REQ";
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
		len +=secretQuestion.getBytes().length;
		len += 2;
		len +=secretAnswer.getBytes().length;
		len += 8;
		len += 1;
		len += 4;
		len += 8;
		len += 2;
		len +=ucPassword.getBytes().length;
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

			buffer.putLong(passportid);
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
				try{
			tmpBytes1 = nickName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = secretQuestion.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = secretAnswer.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(lastQuestionSetDate);
			buffer.put((byte)(isSetSecretQuestion==false?0:1));
			buffer.putInt(status);
			buffer.putLong(lastUpdateStatusDate);
			tmpBytes1 = ucPassword.getBytes();
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
	 *	通行证id
	 */
	public long getPassportid(){
		return passportid;
	}

	/**
	 * 设置属性：
	 *	通行证id
	 */
	public void setPassportid(long passportid){
		this.passportid = passportid;
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
	 *	密保问题
	 */
	public String getSecretQuestion(){
		return secretQuestion;
	}

	/**
	 * 设置属性：
	 *	密保问题
	 */
	public void setSecretQuestion(String secretQuestion){
		this.secretQuestion = secretQuestion;
	}

	/**
	 * 获取属性：
	 *	密保答案
	 */
	public String getSecretAnswer(){
		return secretAnswer;
	}

	/**
	 * 设置属性：
	 *	密保答案
	 */
	public void setSecretAnswer(String secretAnswer){
		this.secretAnswer = secretAnswer;
	}

	/**
	 * 获取属性：
	 *	最后一次设置密保时间
	 */
	public long getLastQuestionSetDate(){
		return lastQuestionSetDate;
	}

	/**
	 * 设置属性：
	 *	最后一次设置密保时间
	 */
	public void setLastQuestionSetDate(long lastQuestionSetDate){
		this.lastQuestionSetDate = lastQuestionSetDate;
	}

	/**
	 * 获取属性：
	 *	是否设置密保问题
	 */
	public boolean getIsSetSecretQuestion(){
		return isSetSecretQuestion;
	}

	/**
	 * 设置属性：
	 *	是否设置密保问题
	 */
	public void setIsSetSecretQuestion(boolean isSetSecretQuestion){
		this.isSetSecretQuestion = isSetSecretQuestion;
	}

	/**
	 * 获取属性：
	 *	用户状态
	 */
	public int getStatus(){
		return status;
	}

	/**
	 * 设置属性：
	 *	用户状态
	 */
	public void setStatus(int status){
		this.status = status;
	}

	/**
	 * 获取属性：
	 *	最后一次设置用户状态时间
	 */
	public long getLastUpdateStatusDate(){
		return lastUpdateStatusDate;
	}

	/**
	 * 设置属性：
	 *	最后一次设置用户状态时间
	 */
	public void setLastUpdateStatusDate(long lastUpdateStatusDate){
		this.lastUpdateStatusDate = lastUpdateStatusDate;
	}

	/**
	 * 获取属性：
	 *	uc密码
	 */
	public String getUcPassword(){
		return ucPassword;
	}

	/**
	 * 设置属性：
	 *	uc密码
	 */
	public void setUcPassword(String ucPassword){
		this.ucPassword = ucPassword;
	}

}