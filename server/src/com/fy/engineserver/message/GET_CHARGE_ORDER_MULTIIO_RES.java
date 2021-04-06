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
 * <tr bgcolor="#FFFFFF" align="center"><td>mess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mess</td><td>String</td><td>mess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>denomination</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeOrder.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chargeOrder</td><td>String</td><td>chargeOrder.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>muitiResults.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>muitiResults[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>muitiResults[0]</td><td>String</td><td>muitiResults[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>muitiResults[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>muitiResults[1]</td><td>String</td><td>muitiResults[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>muitiResults[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>muitiResults[2]</td><td>String</td><td>muitiResults[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GET_CHARGE_ORDER_MULTIIO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int result;
	String mess;
	long denomination;
	String chargeOrder;
	String[] muitiResults;

	public GET_CHARGE_ORDER_MULTIIO_RES(){
	}

	public GET_CHARGE_ORDER_MULTIIO_RES(long seqNum,int result,String mess,long denomination,String chargeOrder,String[] muitiResults){
		this.seqNum = seqNum;
		this.result = result;
		this.mess = mess;
		this.denomination = denomination;
		this.chargeOrder = chargeOrder;
		this.muitiResults = muitiResults;
	}

	public GET_CHARGE_ORDER_MULTIIO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		mess = new String(content,offset,len);
		offset += len;
		denomination = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chargeOrder = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		muitiResults = new String[len];
		for(int i = 0 ; i < muitiResults.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			muitiResults[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x70EEEEE1;
	}

	public String getTypeDescription() {
		return "GET_CHARGE_ORDER_MULTIIO_RES";
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
		len +=mess.getBytes().length;
		len += 8;
		len += 2;
		try{
			len +=chargeOrder.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < muitiResults.length; i++){
			len += 2;
			try{
				len += muitiResults[i].getBytes("UTF-8").length;
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

			buffer.putInt(result);
			byte[] tmpBytes1;
			tmpBytes1 = mess.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(denomination);
				try{
			tmpBytes1 = chargeOrder.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(muitiResults.length);
			for(int i = 0 ; i < muitiResults.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = muitiResults[i].getBytes("UTF-8");
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
	 *	结果,0-成功 1失败
	 */
	public int getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果,0-成功 1失败
	 */
	public void setResult(int result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	计费点的描述
	 */
	public String getMess(){
		return mess;
	}

	/**
	 * 设置属性：
	 *	计费点的描述
	 */
	public void setMess(String mess){
		this.mess = mess;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public long getDenomination(){
		return denomination;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDenomination(long denomination){
		this.denomination = denomination;
	}

	/**
	 * 获取属性：
	 *	充值ID
	 */
	public String getChargeOrder(){
		return chargeOrder;
	}

	/**
	 * 设置属性：
	 *	充值ID
	 */
	public void setChargeOrder(String chargeOrder){
		this.chargeOrder = chargeOrder;
	}

	/**
	 * 获取属性：
	 *	其他返回参数(C-S约定)
	 */
	public String[] getMuitiResults(){
		return muitiResults;
	}

	/**
	 * 设置属性：
	 *	其他返回参数(C-S约定)
	 */
	public void setMuitiResults(String[] muitiResults){
		this.muitiResults = muitiResults;
	}

}