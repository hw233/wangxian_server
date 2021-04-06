package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * PLAYPHONE充值<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientTransactionId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>itmeId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>amount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>MD5.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>MD5</td><td>String</td><td>MD5.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PLAYPHONE_USER_SAVING_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long clientTransactionId;
	int itmeId;
	int amount;
	String MD5;

	public PLAYPHONE_USER_SAVING_REQ(long seqNum,long clientTransactionId,int itmeId,int amount,String MD5){
		this.seqNum = seqNum;
		this.clientTransactionId = clientTransactionId;
		this.itmeId = itmeId;
		this.amount = amount;
		this.MD5 = MD5;
	}

	public PLAYPHONE_USER_SAVING_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		clientTransactionId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		itmeId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		amount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		MD5 = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x0002A012;
	}

	public String getTypeDescription() {
		return "PLAYPHONE_USER_SAVING_REQ";
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
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=MD5.getBytes("UTF-8").length;
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

			buffer.putLong(clientTransactionId);
			buffer.putInt(itmeId);
			buffer.putInt(amount);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = MD5.getBytes("UTF-8");
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
	 *	客户端交易识别码
	 */
	public long getClientTransactionId(){
		return clientTransactionId;
	}

	/**
	 * 设置属性：
	 *	客户端交易识别码
	 */
	public void setClientTransactionId(long clientTransactionId){
		this.clientTransactionId = clientTransactionId;
	}

	/**
	 * 获取属性：
	 *	虚拟道具ID
	 */
	public int getItmeId(){
		return itmeId;
	}

	/**
	 * 设置属性：
	 *	虚拟道具ID
	 */
	public void setItmeId(int itmeId){
		this.itmeId = itmeId;
	}

	/**
	 * 获取属性：
	 *	用户名，定长16个英文字符
	 */
	public int getAmount(){
		return amount;
	}

	/**
	 * 设置属性：
	 *	用户名，定长16个英文字符
	 */
	public void setAmount(int amount){
		this.amount = amount;
	}

	/**
	 * 获取属性：
	 *	MD5验证码，格式：clientTransactionId@userName@amount@secretKey
	 */
	public String getMD5(){
		return MD5;
	}

	/**
	 * 设置属性：
	 *	MD5验证码，格式：clientTransactionId@userName@amount@secretKey
	 */
	public void setMD5(String MD5){
		this.MD5 = MD5;
	}

}
