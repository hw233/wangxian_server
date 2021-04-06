package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求短信充值信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gateway.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gateway[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gateway[0]</td><td>String</td><td>gateway[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gateway[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gateway[1]</td><td>String</td><td>gateway[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gateway[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gateway[2]</td><td>String</td><td>gateway[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info[0]</td><td>String</td><td>info[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info[1]</td><td>String</td><td>info[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info[2]</td><td>String</td><td>info[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exchangeRate.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exchangeRate</td><td>int[]</td><td>exchangeRate.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>facevalues.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>facevalues[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>facevalues[0]</td><td>String</td><td>facevalues[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>facevalues[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>facevalues[1]</td><td>String</td><td>facevalues[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>facevalues[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>facevalues[2]</td><td>String</td><td>facevalues[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>msg.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>msg[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>msg[0]</td><td>String</td><td>msg[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>msg[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>msg[1]</td><td>String</td><td>msg[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>msg[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>msg[2]</td><td>String</td><td>msg[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smsNum.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>smsNum[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smsNum[0]</td><td>String</td><td>smsNum[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>smsNum[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smsNum[1]</td><td>String</td><td>smsNum[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>smsNum[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smsNum[2]</td><td>String</td><td>smsNum[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>unitPrice.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>unitPrice</td><td>int[]</td><td>unitPrice.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeQuota</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>userChargeMonthlyAmount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>userChargeDailyAmount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindingMsg.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindingMsg[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindingMsg[0]</td><td>String</td><td>bindingMsg[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindingMsg[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindingMsg[1]</td><td>String</td><td>bindingMsg[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindingMsg[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindingMsg[2]</td><td>String</td><td>bindingMsg[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindingSmsNum.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindingSmsNum[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindingSmsNum[0]</td><td>String</td><td>bindingSmsNum[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindingSmsNum[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindingSmsNum[1]</td><td>String</td><td>bindingSmsNum[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindingSmsNum[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindingSmsNum[2]</td><td>String</td><td>bindingSmsNum[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GET_SMS_CHARGE_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] gateway;
	String[] info;
	int[] exchangeRate;
	String[] facevalues;
	String[] msg;
	String[] smsNum;
	int[] unitPrice;
	int chargeQuota;
	int userChargeMonthlyAmount;
	int userChargeDailyAmount;
	String[] bindingMsg;
	String[] bindingSmsNum;

	public GET_SMS_CHARGE_INFO_RES(){
	}

	public GET_SMS_CHARGE_INFO_RES(long seqNum,String[] gateway,String[] info,int[] exchangeRate,String[] facevalues,String[] msg,String[] smsNum,int[] unitPrice,int chargeQuota,int userChargeMonthlyAmount,int userChargeDailyAmount,String[] bindingMsg,String[] bindingSmsNum){
		this.seqNum = seqNum;
		this.gateway = gateway;
		this.info = info;
		this.exchangeRate = exchangeRate;
		this.facevalues = facevalues;
		this.msg = msg;
		this.smsNum = smsNum;
		this.unitPrice = unitPrice;
		this.chargeQuota = chargeQuota;
		this.userChargeMonthlyAmount = userChargeMonthlyAmount;
		this.userChargeDailyAmount = userChargeDailyAmount;
		this.bindingMsg = bindingMsg;
		this.bindingSmsNum = bindingSmsNum;
	}

	public GET_SMS_CHARGE_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		gateway = new String[len];
		for(int i = 0 ; i < gateway.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			gateway[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		info = new String[len];
		for(int i = 0 ; i < info.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			info[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		exchangeRate = new int[len];
		for(int i = 0 ; i < exchangeRate.length ; i++){
			exchangeRate[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		facevalues = new String[len];
		for(int i = 0 ; i < facevalues.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			facevalues[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		msg = new String[len];
		for(int i = 0 ; i < msg.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			msg[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		smsNum = new String[len];
		for(int i = 0 ; i < smsNum.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			smsNum[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		unitPrice = new int[len];
		for(int i = 0 ; i < unitPrice.length ; i++){
			unitPrice[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		chargeQuota = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		userChargeMonthlyAmount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		userChargeDailyAmount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bindingMsg = new String[len];
		for(int i = 0 ; i < bindingMsg.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			bindingMsg[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bindingSmsNum = new String[len];
		for(int i = 0 ; i < bindingSmsNum.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			bindingSmsNum[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x7000EF14;
	}

	public String getTypeDescription() {
		return "GET_SMS_CHARGE_INFO_RES";
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
		for(int i = 0 ; i < gateway.length; i++){
			len += 2;
			try{
				len += gateway[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < info.length; i++){
			len += 2;
			try{
				len += info[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += exchangeRate.length * 4;
		len += 4;
		for(int i = 0 ; i < facevalues.length; i++){
			len += 2;
			try{
				len += facevalues[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < msg.length; i++){
			len += 2;
			try{
				len += msg[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < smsNum.length; i++){
			len += 2;
			try{
				len += smsNum[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += unitPrice.length * 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < bindingMsg.length; i++){
			len += 2;
			try{
				len += bindingMsg[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < bindingSmsNum.length; i++){
			len += 2;
			try{
				len += bindingSmsNum[i].getBytes("UTF-8").length;
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

			buffer.putInt(gateway.length);
			for(int i = 0 ; i < gateway.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = gateway[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(info.length);
			for(int i = 0 ; i < info.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = info[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(exchangeRate.length);
			for(int i = 0 ; i < exchangeRate.length; i++){
				buffer.putInt(exchangeRate[i]);
			}
			buffer.putInt(facevalues.length);
			for(int i = 0 ; i < facevalues.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = facevalues[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(msg.length);
			for(int i = 0 ; i < msg.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = msg[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(smsNum.length);
			for(int i = 0 ; i < smsNum.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = smsNum[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(unitPrice.length);
			for(int i = 0 ; i < unitPrice.length; i++){
				buffer.putInt(unitPrice[i]);
			}
			buffer.putInt(chargeQuota);
			buffer.putInt(userChargeMonthlyAmount);
			buffer.putInt(userChargeDailyAmount);
			buffer.putInt(bindingMsg.length);
			for(int i = 0 ; i < bindingMsg.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = bindingMsg[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(bindingSmsNum.length);
			for(int i = 0 ; i < bindingSmsNum.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = bindingSmsNum[i].getBytes("UTF-8");
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
	 *	发送短信的途径，移动 联通等
	 */
	public String[] getGateway(){
		return gateway;
	}

	/**
	 * 设置属性：
	 *	发送短信的途径，移动 联通等
	 */
	public void setGateway(String[] gateway){
		this.gateway = gateway;
	}

	/**
	 * 获取属性：
	 *	信息提示
	 */
	public String[] getInfo(){
		return info;
	}

	/**
	 * 设置属性：
	 *	信息提示
	 */
	public void setInfo(String[] info){
		this.info = info;
	}

	/**
	 * 获取属性：
	 *	兑换率，一元短信兑换多少元宝
	 */
	public int[] getExchangeRate(){
		return exchangeRate;
	}

	/**
	 * 设置属性：
	 *	兑换率，一元短信兑换多少元宝
	 */
	public void setExchangeRate(int[] exchangeRate){
		this.exchangeRate = exchangeRate;
	}

	/**
	 * 获取属性：
	 *	发送短信的金额，所有金额用一个字符串，用分隔号隔开，例如10：20：30
	 */
	public String[] getFacevalues(){
		return facevalues;
	}

	/**
	 * 设置属性：
	 *	发送短信的金额，所有金额用一个字符串，用分隔号隔开，例如10：20：30
	 */
	public void setFacevalues(String[] facevalues){
		this.facevalues = facevalues;
	}

	/**
	 * 获取属性：
	 *	发送的信息
	 */
	public String[] getMsg(){
		return msg;
	}

	/**
	 * 设置属性：
	 *	发送的信息
	 */
	public void setMsg(String[] msg){
		this.msg = msg;
	}

	/**
	 * 获取属性：
	 *	短信号码
	 */
	public String[] getSmsNum(){
		return smsNum;
	}

	/**
	 * 设置属性：
	 *	短信号码
	 */
	public void setSmsNum(String[] smsNum){
		this.smsNum = smsNum;
	}

	/**
	 * 获取属性：
	 *	每条短信的收费金额
	 */
	public int[] getUnitPrice(){
		return unitPrice;
	}

	/**
	 * 设置属性：
	 *	每条短信的收费金额
	 */
	public void setUnitPrice(int[] unitPrice){
		this.unitPrice = unitPrice;
	}

	/**
	 * 获取属性：
	 *	用户可发送的金额
	 */
	public int getChargeQuota(){
		return chargeQuota;
	}

	/**
	 * 设置属性：
	 *	用户可发送的金额
	 */
	public void setChargeQuota(int chargeQuota){
		this.chargeQuota = chargeQuota;
	}

	/**
	 * 获取属性：
	 *	用户本月已经充值的金额
	 */
	public int getUserChargeMonthlyAmount(){
		return userChargeMonthlyAmount;
	}

	/**
	 * 设置属性：
	 *	用户本月已经充值的金额
	 */
	public void setUserChargeMonthlyAmount(int userChargeMonthlyAmount){
		this.userChargeMonthlyAmount = userChargeMonthlyAmount;
	}

	/**
	 * 获取属性：
	 *	用户本日已经充值的金额
	 */
	public int getUserChargeDailyAmount(){
		return userChargeDailyAmount;
	}

	/**
	 * 设置属性：
	 *	用户本日已经充值的金额
	 */
	public void setUserChargeDailyAmount(int userChargeDailyAmount){
		this.userChargeDailyAmount = userChargeDailyAmount;
	}

	/**
	 * 获取属性：
	 *	绑定短信发送的信息
	 */
	public String[] getBindingMsg(){
		return bindingMsg;
	}

	/**
	 * 设置属性：
	 *	绑定短信发送的信息
	 */
	public void setBindingMsg(String[] bindingMsg){
		this.bindingMsg = bindingMsg;
	}

	/**
	 * 获取属性：
	 *	绑定短信号码
	 */
	public String[] getBindingSmsNum(){
		return bindingSmsNum;
	}

	/**
	 * 设置属性：
	 *	绑定短信号码
	 */
	public void setBindingSmsNum(String[] bindingSmsNum){
		this.bindingSmsNum = bindingSmsNum;
	}

}