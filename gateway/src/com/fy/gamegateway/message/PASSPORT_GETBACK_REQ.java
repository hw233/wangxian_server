package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 忘记密码<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>passportid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>clientId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientId</td><td>String</td><td>clientId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>channel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>channel</td><td>String</td><td>channel.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>MobileType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>MobileType</td><td>String</td><td>MobileType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areaname.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areaname</td><td>String</td><td>areaname.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>servername.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>servername</td><td>String</td><td>servername.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>registerMobile.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>registerMobile</td><td>String</td><td>registerMobile.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>secretQuestion.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>secretQuestion</td><td>String</td><td>secretQuestion.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>secretAnswer.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>secretAnswer</td><td>String</td><td>secretAnswer.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastChargeDate.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastChargeDate</td><td>String</td><td>lastChargeDate.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastChargeAmount.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastChargeAmount</td><td>String</td><td>lastChargeAmount.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastLoginDate.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastLoginDate</td><td>String</td><td>lastLoginDate.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PASSPORT_GETBACK_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long passportid;
	String clientId;
	String channel;
	String MobileType;
	String areaname;
	String name;
	String servername;
	String registerMobile;
	String username;
	String secretQuestion;
	String secretAnswer;
	String lastChargeDate;
	String lastChargeAmount;
	String lastLoginDate;

	public PASSPORT_GETBACK_REQ(){
	}

	public PASSPORT_GETBACK_REQ(long seqNum,long passportid,String clientId,String channel,String MobileType,String areaname,String name,String servername,String registerMobile,String username,String secretQuestion,String secretAnswer,String lastChargeDate,String lastChargeAmount,String lastLoginDate){
		this.seqNum = seqNum;
		this.passportid = passportid;
		this.clientId = clientId;
		this.channel = channel;
		this.MobileType = MobileType;
		this.areaname = areaname;
		this.name = name;
		this.servername = servername;
		this.registerMobile = registerMobile;
		this.username = username;
		this.secretQuestion = secretQuestion;
		this.secretAnswer = secretAnswer;
		this.lastChargeDate = lastChargeDate;
		this.lastChargeAmount = lastChargeAmount;
		this.lastLoginDate = lastLoginDate;
	}

	public PASSPORT_GETBACK_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		passportid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		clientId = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		channel = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		MobileType = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		areaname = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		servername = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		registerMobile = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		username = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		secretQuestion = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		secretAnswer = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		lastChargeDate = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		lastChargeAmount = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		lastLoginDate = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x000EE002;
	}

	public String getTypeDescription() {
		return "PASSPORT_GETBACK_REQ";
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
			len +=clientId.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		len +=channel.getBytes().length;
		len += 2;
		len +=MobileType.getBytes().length;
		len += 2;
		try{
			len +=areaname.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=servername.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		len +=registerMobile.getBytes().length;
		len += 2;
		try{
			len +=username.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		len +=secretQuestion.getBytes().length;
		len += 2;
		len +=secretAnswer.getBytes().length;
		len += 2;
		len +=lastChargeDate.getBytes().length;
		len += 2;
		len +=lastChargeAmount.getBytes().length;
		len += 2;
		len +=lastLoginDate.getBytes().length;
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
			 tmpBytes1 = clientId.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = channel.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = MobileType.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = areaname.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = servername.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = registerMobile.getBytes();
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
			tmpBytes1 = secretQuestion.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = secretAnswer.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = lastChargeDate.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = lastChargeAmount.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = lastLoginDate.getBytes();
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
	 *	客户端唯一标示,客服核实用
	 */
	public String getClientId(){
		return clientId;
	}

	/**
	 * 设置属性：
	 *	客户端唯一标示,客服核实用
	 */
	public void setClientId(String clientId){
		this.clientId = clientId;
	}

	/**
	 * 获取属性：
	 *	当前渠道
	 */
	public String getChannel(){
		return channel;
	}

	/**
	 * 设置属性：
	 *	当前渠道
	 */
	public void setChannel(String channel){
		this.channel = channel;
	}

	/**
	 * 获取属性：
	 *	手机机型
	 */
	public String getMobileType(){
		return MobileType;
	}

	/**
	 * 设置属性：
	 *	手机机型
	 */
	public void setMobileType(String MobileType){
		this.MobileType = MobileType;
	}

	/**
	 * 获取属性：
	 *	大区
	 */
	public String getAreaname(){
		return areaname;
	}

	/**
	 * 设置属性：
	 *	大区
	 */
	public void setAreaname(String areaname){
		this.areaname = areaname;
	}

	/**
	 * 获取属性：
	 *	角色名
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	角色名
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	服务器
	 */
	public String getServername(){
		return servername;
	}

	/**
	 * 设置属性：
	 *	服务器
	 */
	public void setServername(String servername){
		this.servername = servername;
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
	 *	最后一次充值日期
	 */
	public String getLastChargeDate(){
		return lastChargeDate;
	}

	/**
	 * 设置属性：
	 *	最后一次充值日期
	 */
	public void setLastChargeDate(String lastChargeDate){
		this.lastChargeDate = lastChargeDate;
	}

	/**
	 * 获取属性：
	 *	最后一次充值金额
	 */
	public String getLastChargeAmount(){
		return lastChargeAmount;
	}

	/**
	 * 设置属性：
	 *	最后一次充值金额
	 */
	public void setLastChargeAmount(String lastChargeAmount){
		this.lastChargeAmount = lastChargeAmount;
	}

	/**
	 * 获取属性：
	 *	最后一次登陆日期
	 */
	public String getLastLoginDate(){
		return lastLoginDate;
	}

	/**
	 * 设置属性：
	 *	最后一次登陆日期
	 */
	public void setLastLoginDate(String lastLoginDate){
		this.lastLoginDate = lastLoginDate;
	}

}