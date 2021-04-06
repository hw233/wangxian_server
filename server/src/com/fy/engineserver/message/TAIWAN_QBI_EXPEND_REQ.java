package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * Q币消费<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isNeedCharge</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>expendAmount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tsi.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tsi</td><td>String</td><td>tsi.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smbody.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>smbody</td><td>String</td><td>smbody.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class TAIWAN_QBI_EXPEND_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean isNeedCharge;
	int expendAmount;
	String tsi;
	String smbody;

	public TAIWAN_QBI_EXPEND_REQ(long seqNum,boolean isNeedCharge,int expendAmount,String tsi,String smbody){
		this.seqNum = seqNum;
		this.isNeedCharge = isNeedCharge;
		this.expendAmount = expendAmount;
		this.tsi = tsi;
		this.smbody = smbody;
	}

	public TAIWAN_QBI_EXPEND_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		isNeedCharge = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		expendAmount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		tsi = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		smbody = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x0002A007;
	}

	public String getTypeDescription() {
		return "TAIWAN_QBI_EXPEND_REQ";
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
		len += 4;
		len += 2;
		len +=tsi.getBytes().length;
		len += 2;
		len +=smbody.getBytes().length;
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

			buffer.put((byte)(isNeedCharge==false?0:1));
			buffer.putInt(expendAmount);
			byte[] tmpBytes1;
			tmpBytes1 = tsi.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = smbody.getBytes();
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
	 *	是否需要充值
	 */
	public boolean getIsNeedCharge(){
		return isNeedCharge;
	}

	/**
	 * 设置属性：
	 *	是否需要充值
	 */
	public void setIsNeedCharge(boolean isNeedCharge){
		this.isNeedCharge = isNeedCharge;
	}

	/**
	 * 获取属性：
	 *	消费的Q币金额
	 */
	public int getExpendAmount(){
		return expendAmount;
	}

	/**
	 * 设置属性：
	 *	消费的Q币金额
	 */
	public void setExpendAmount(int expendAmount){
		this.expendAmount = expendAmount;
	}

	/**
	 * 获取属性：
	 *	tsi
	 */
	public String getTsi(){
		return tsi;
	}

	/**
	 * 设置属性：
	 *	tsi
	 */
	public void setTsi(String tsi){
		this.tsi = tsi;
	}

	/**
	 * 获取属性：
	 *	smbody
	 */
	public String getSmbody(){
		return smbody;
	}

	/**
	 * 设置属性：
	 *	smbody
	 */
	public void setSmbody(String smbody){
		this.smbody = smbody;
	}

}
