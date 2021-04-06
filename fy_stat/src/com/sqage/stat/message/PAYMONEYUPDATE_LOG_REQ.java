package com.sqage.stat.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.sqage.stat.model.PayMoneyUpGradeFlow;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 充值日志<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.userName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.userName</td><td>String</td><td>PayMoneyUpGradeFlow.userName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.date</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.game.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.game</td><td>String</td><td>PayMoneyUpGradeFlow.game.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.fenQu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.fenQu</td><td>String</td><td>PayMoneyUpGradeFlow.fenQu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.level.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.level</td><td>String</td><td>PayMoneyUpGradeFlow.level.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.payMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.type.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.type</td><td>String</td><td>PayMoneyUpGradeFlow.type.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.quDao.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.quDao</td><td>String</td><td>PayMoneyUpGradeFlow.quDao.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.cost.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.cost</td><td>String</td><td>PayMoneyUpGradeFlow.cost.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.cardType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.cardType</td><td>String</td><td>PayMoneyUpGradeFlow.cardType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.jixing.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.jixing</td><td>String</td><td>PayMoneyUpGradeFlow.jixing.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.modelType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.modelType</td><td>String</td><td>PayMoneyUpGradeFlow.modelType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.vip.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.vip</td><td>String</td><td>PayMoneyUpGradeFlow.vip.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.registDate</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.playName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.playName</td><td>String</td><td>PayMoneyUpGradeFlow.playName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>PayMoneyUpGradeFlow.colum1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>PayMoneyUpGradeFlow.colum1</td><td>String</td><td>PayMoneyUpGradeFlow.colum1.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PAYMONEYUPDATE_LOG_REQ implements RequestMessage{

	static StatMessageFactory mf = StatMessageFactory.getInstance();

	long seqNum;
	PayMoneyUpGradeFlow PayMoneyUpGradeFlow;

	public PAYMONEYUPDATE_LOG_REQ(long seqNum,PayMoneyUpGradeFlow PayMoneyUpGradeFlow){
		this.seqNum = seqNum;
		this.PayMoneyUpGradeFlow = PayMoneyUpGradeFlow;
	}

	public PAYMONEYUPDATE_LOG_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		PayMoneyUpGradeFlow = new PayMoneyUpGradeFlow();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setUserName(new String(content,offset,len,"UTF-8"));
		offset += len;
		PayMoneyUpGradeFlow.setDate((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setGame(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setFenQu(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setLevel(new String(content,offset,len,"UTF-8"));
		offset += len;
		PayMoneyUpGradeFlow.setPayMoney((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setType(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setQuDao(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setCost(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setCardType(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setJixing(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setModelType(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setVip(new String(content,offset,len,"UTF-8"));
		offset += len;
		PayMoneyUpGradeFlow.setRegistDate((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setPlayName(new String(content,offset,len));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		PayMoneyUpGradeFlow.setColum1(new String(content,offset,len));
		offset += len;
	}

	public int getType() {
		return 0x00000018;
	}

	public String getTypeDescription() {
		return "PAYMONEYUPDATE_LOG_REQ";
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
		if(PayMoneyUpGradeFlow.getUserName() != null){
			try{
			len += PayMoneyUpGradeFlow.getUserName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 2;
		if(PayMoneyUpGradeFlow.getGame() != null){
			try{
			len += PayMoneyUpGradeFlow.getGame().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getFenQu() != null){
			try{
			len += PayMoneyUpGradeFlow.getFenQu().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getLevel() != null){
			try{
			len += PayMoneyUpGradeFlow.getLevel().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 2;
		if(PayMoneyUpGradeFlow.getType() != null){
			try{
			len += PayMoneyUpGradeFlow.getType().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getQuDao() != null){
			try{
			len += PayMoneyUpGradeFlow.getQuDao().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getCost() != null){
			try{
			len += PayMoneyUpGradeFlow.getCost().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getCardType() != null){
			try{
			len += PayMoneyUpGradeFlow.getCardType().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getJixing() != null){
			try{
			len += PayMoneyUpGradeFlow.getJixing().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getModelType() != null){
			try{
			len += PayMoneyUpGradeFlow.getModelType().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getVip() != null){
			try{
			len += PayMoneyUpGradeFlow.getVip().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 2;
		if(PayMoneyUpGradeFlow.getPlayName() != null){
			len += PayMoneyUpGradeFlow.getPlayName().getBytes().length;
		}
		len += 2;
		if(PayMoneyUpGradeFlow.getColum1() != null){
			len += PayMoneyUpGradeFlow.getColum1().getBytes().length;
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

			byte[] tmpBytes1 = PayMoneyUpGradeFlow.getUserName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(PayMoneyUpGradeFlow.getDate());
			tmpBytes1 = PayMoneyUpGradeFlow.getGame().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getFenQu().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getLevel().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(PayMoneyUpGradeFlow.getPayMoney());
			tmpBytes1 = PayMoneyUpGradeFlow.getType().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getQuDao().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getCost().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getCardType().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getJixing().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getModelType().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getVip().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(PayMoneyUpGradeFlow.getRegistDate());
			tmpBytes1 = PayMoneyUpGradeFlow.getPlayName().getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = PayMoneyUpGradeFlow.getColum1().getBytes();
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
	 *	充值日志
	 */
	public PayMoneyUpGradeFlow getPayMoneyUpGradeFlow(){
		return PayMoneyUpGradeFlow;
	}

	/**
	 * 设置属性：
	 *	充值日志
	 */
	public void setPayMoneyUpGradeFlow(PayMoneyUpGradeFlow PayMoneyUpGradeFlow){
		this.PayMoneyUpGradeFlow = PayMoneyUpGradeFlow;
	}

}