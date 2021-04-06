package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chargeOrder.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeOrder</td><td>String</td><td>chargeOrder.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>money</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>waresId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>waresId</td><td>String</td><td>waresId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargePoint</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class YINGYONGHUI_GET_CHARGE_ORDER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int result;
	String chargeOrder;
	long money;
	String waresId;
	int chargePoint;

	public YINGYONGHUI_GET_CHARGE_ORDER_RES(){
	}

	public YINGYONGHUI_GET_CHARGE_ORDER_RES(long seqNum,int result,String chargeOrder,long money,String waresId,int chargePoint){
		this.seqNum = seqNum;
		this.result = result;
		this.chargeOrder = chargeOrder;
		this.money = money;
		this.waresId = waresId;
		this.chargePoint = chargePoint;
	}

	public YINGYONGHUI_GET_CHARGE_ORDER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chargeOrder = new String(content,offset,len,"UTF-8");
		offset += len;
		money = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		waresId = new String(content,offset,len,"UTF-8");
		offset += len;
		chargePoint = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70EEEE0D;
	}

	public String getTypeDescription() {
		return "YINGYONGHUI_GET_CHARGE_ORDER_RES";
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
		len += 2;
		try{
			len +=chargeOrder.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 2;
		try{
			len +=waresId.getBytes("UTF-8").length;
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

			buffer.putInt(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = chargeOrder.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(money);
				try{
			tmpBytes1 = waresId.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(chargePoint);
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
	 *	是否成功,0-成功,1-失败
	 */
	public int getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	是否成功,0-成功,1-失败
	 */
	public void setResult(int result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	正确返回则为订单号,否则为错误提示
	 */
	public String getChargeOrder(){
		return chargeOrder;
	}

	/**
	 * 设置属性：
	 *	正确返回则为订单号,否则为错误提示
	 */
	public void setChargeOrder(String chargeOrder){
		this.chargeOrder = chargeOrder;
	}

	/**
	 * 获取属性：
	 *	钱数,分
	 */
	public long getMoney(){
		return money;
	}

	/**
	 * 设置属性：
	 *	钱数,分
	 */
	public void setMoney(long money){
		this.money = money;
	}

	/**
	 * 获取属性：
	 *	商品代码
	 */
	public String getWaresId(){
		return waresId;
	}

	/**
	 * 设置属性：
	 *	商品代码
	 */
	public void setWaresId(String waresId){
		this.waresId = waresId;
	}

	/**
	 * 获取属性：
	 *	计费点代码
	 */
	public int getChargePoint(){
		return chargePoint;
	}

	/**
	 * 设置属性：
	 *	计费点代码
	 */
	public void setChargePoint(int chargePoint){
		this.chargePoint = chargePoint;
	}

}