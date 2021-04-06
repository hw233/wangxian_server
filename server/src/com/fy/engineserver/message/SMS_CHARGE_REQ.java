package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 短信充值信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>unitPrice</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gateway</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>msg.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>msg</td><td>String</td><td>msg.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smsNum.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>smsNum</td><td>String</td><td>smsNum.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>userAccount.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>userAccount</td><td>String</td><td>userAccount.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smsCounts</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SMS_CHARGE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int unitPrice;
	int gateway;
	String msg;
	String smsNum;
	String userAccount;
	int smsCounts;

	public SMS_CHARGE_REQ(){
	}

	public SMS_CHARGE_REQ(long seqNum,int unitPrice,int gateway,String msg,String smsNum,String userAccount,int smsCounts){
		this.seqNum = seqNum;
		this.unitPrice = unitPrice;
		this.gateway = gateway;
		this.msg = msg;
		this.smsNum = smsNum;
		this.userAccount = userAccount;
		this.smsCounts = smsCounts;
	}

	public SMS_CHARGE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		unitPrice = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		gateway = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		msg = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		smsNum = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		userAccount = new String(content,offset,len,"UTF-8");
		offset += len;
		smsCounts = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000EF15;
	}

	public String getTypeDescription() {
		return "SMS_CHARGE_REQ";
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
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=msg.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=smsNum.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=userAccount.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putInt(unitPrice);
			buffer.putInt(gateway);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = msg.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = smsNum.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = userAccount.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(smsCounts);
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
	 *	短信计费金额
	 */
	public int getUnitPrice(){
		return unitPrice;
	}

	/**
	 * 设置属性：
	 *	短信计费金额
	 */
	public void setUnitPrice(int unitPrice){
		this.unitPrice = unitPrice;
	}

	/**
	 * 获取属性：
	 *	短信发送途径 0：移动 1：联通 2：电信
	 */
	public int getGateway(){
		return gateway;
	}

	/**
	 * 设置属性：
	 *	短信发送途径 0：移动 1：联通 2：电信
	 */
	public void setGateway(int gateway){
		this.gateway = gateway;
	}

	/**
	 * 获取属性：
	 *	短信内容
	 */
	public String getMsg(){
		return msg;
	}

	/**
	 * 设置属性：
	 *	短信内容
	 */
	public void setMsg(String msg){
		this.msg = msg;
	}

	/**
	 * 获取属性：
	 *	短信号码
	 */
	public String getSmsNum(){
		return smsNum;
	}

	/**
	 * 设置属性：
	 *	短信号码
	 */
	public void setSmsNum(String smsNum){
		this.smsNum = smsNum;
	}

	/**
	 * 获取属性：
	 *	玩家帐号
	 */
	public String getUserAccount(){
		return userAccount;
	}

	/**
	 * 设置属性：
	 *	玩家帐号
	 */
	public void setUserAccount(String userAccount){
		this.userAccount = userAccount;
	}

	/**
	 * 获取属性：
	 *	短信条数
	 */
	public int getSmsCounts(){
		return smsCounts;
	}

	/**
	 * 设置属性：
	 *	短信条数
	 */
	public void setSmsCounts(int smsCounts){
		this.smsCounts = smsCounts;
	}

}