package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 用户充值<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardno.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardno</td><td>String</td><td>cardno.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardpass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardpass</td><td>String</td><td>cardpass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mianzhi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>amount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardtype</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CARD_SAVING_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String username;
	String cardno;
	String cardpass;
	int mianzhi;
	int amount;
	int cardtype;

	public CARD_SAVING_REQ(){
	}

	public CARD_SAVING_REQ(long seqNum,String username,String cardno,String cardpass,int mianzhi,int amount,int cardtype){
		this.seqNum = seqNum;
		this.username = username;
		this.cardno = cardno;
		this.cardpass = cardpass;
		this.mianzhi = mianzhi;
		this.amount = amount;
		this.cardtype = cardtype;
	}

	public CARD_SAVING_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		username = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		cardno = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		cardpass = new String(content,offset,len,"UTF-8");
		offset += len;
		mianzhi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		amount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		cardtype = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000EF00;
	}

	public String getTypeDescription() {
		return "CARD_SAVING_REQ";
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
			len +=cardno.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=cardpass.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
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
			tmpBytes1 = cardno.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = cardpass.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(mianzhi);
			buffer.putInt(amount);
			buffer.putInt(cardtype);
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
	 *	账号名
	 */
	public String getUsername(){
		return username;
	}

	/**
	 * 设置属性：
	 *	账号名
	 */
	public void setUsername(String username){
		this.username = username;
	}

	/**
	 * 获取属性：
	 *	卡序列号
	 */
	public String getCardno(){
		return cardno;
	}

	/**
	 * 设置属性：
	 *	卡序列号
	 */
	public void setCardno(String cardno){
		this.cardno = cardno;
	}

	/**
	 * 获取属性：
	 *	卡密码
	 */
	public String getCardpass(){
		return cardpass;
	}

	/**
	 * 设置属性：
	 *	卡密码
	 */
	public void setCardpass(String cardpass){
		this.cardpass = cardpass;
	}

	/**
	 * 获取属性：
	 *	卡面值
	 */
	public int getMianzhi(){
		return mianzhi;
	}

	/**
	 * 设置属性：
	 *	卡面值
	 */
	public void setMianzhi(int mianzhi){
		this.mianzhi = mianzhi;
	}

	/**
	 * 获取属性：
	 *	充值额度，客户端可直接填面值
	 */
	public int getAmount(){
		return amount;
	}

	/**
	 * 设置属性：
	 *	充值额度，客户端可直接填面值
	 */
	public void setAmount(int amount){
		this.amount = amount;
	}

	/**
	 * 获取属性：
	 *	卡类型, 0-神州行,1-联通卡,2-电信卡
	 */
	public int getCardtype(){
		return cardtype;
	}

	/**
	 * 设置属性：
	 *	卡类型, 0-神州行,1-联通卡,2-电信卡
	 */
	public void setCardtype(int cardtype){
		this.cardtype = cardtype;
	}

}