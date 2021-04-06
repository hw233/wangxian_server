package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求充值卡充值信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardTypeNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeNames[0]</td><td>String</td><td>cardTypeNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardTypeNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeNames[1]</td><td>String</td><td>cardTypeNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardTypeNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeNames[2]</td><td>String</td><td>cardTypeNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardTypeIds</td><td>byte[]</td><td>cardTypeIds.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeInfos.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardTypeInfos[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeInfos[0]</td><td>String</td><td>cardTypeInfos[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardTypeInfos[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeInfos[1]</td><td>String</td><td>cardTypeInfos[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardTypeInfos[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardTypeInfos[2]</td><td>String</td><td>cardTypeInfos[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>facevalues.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>facevalues[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>facevalues[0]</td><td>String</td><td>facevalues[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>facevalues[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>facevalues[1]</td><td>String</td><td>facevalues[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>facevalues[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>facevalues[2]</td><td>String</td><td>facevalues[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exchange.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exchange[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exchange[0]</td><td>String</td><td>exchange[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exchange[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exchange[1]</td><td>String</td><td>exchange[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exchange[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exchange[2]</td><td>String</td><td>exchange[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GET_CARD_CHARGE_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] cardTypeNames;
	byte[] cardTypeIds;
	String[] cardTypeInfos;
	String[] facevalues;
	String[] exchange;

	public GET_CARD_CHARGE_INFO_RES(){
	}

	public GET_CARD_CHARGE_INFO_RES(long seqNum,String[] cardTypeNames,byte[] cardTypeIds,String[] cardTypeInfos,String[] facevalues,String[] exchange){
		this.seqNum = seqNum;
		this.cardTypeNames = cardTypeNames;
		this.cardTypeIds = cardTypeIds;
		this.cardTypeInfos = cardTypeInfos;
		this.facevalues = facevalues;
		this.exchange = exchange;
	}

	public GET_CARD_CHARGE_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		cardTypeNames = new String[len];
		for(int i = 0 ; i < cardTypeNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			cardTypeNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		cardTypeIds = new byte[len];
		System.arraycopy(content,offset,cardTypeIds,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		cardTypeInfos = new String[len];
		for(int i = 0 ; i < cardTypeInfos.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			cardTypeInfos[i] = new String(content,offset,len,"UTF-8");
		offset += len;
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
		exchange = new String[len];
		for(int i = 0 ; i < exchange.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			exchange[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x7000EF13;
	}

	public String getTypeDescription() {
		return "GET_CARD_CHARGE_INFO_RES";
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
		for(int i = 0 ; i < cardTypeNames.length; i++){
			len += 2;
			try{
				len += cardTypeNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += cardTypeIds.length;
		len += 4;
		for(int i = 0 ; i < cardTypeInfos.length; i++){
			len += 2;
			try{
				len += cardTypeInfos[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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
		for(int i = 0 ; i < exchange.length; i++){
			len += 2;
			try{
				len += exchange[i].getBytes("UTF-8").length;
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

			buffer.putInt(cardTypeNames.length);
			for(int i = 0 ; i < cardTypeNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = cardTypeNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(cardTypeIds.length);
			buffer.put(cardTypeIds);
			buffer.putInt(cardTypeInfos.length);
			for(int i = 0 ; i < cardTypeInfos.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = cardTypeInfos[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
			buffer.putInt(exchange.length);
			for(int i = 0 ; i < exchange.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = exchange[i].getBytes("UTF-8");
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
	 *	充值卡种类名称
	 */
	public String[] getCardTypeNames(){
		return cardTypeNames;
	}

	/**
	 * 设置属性：
	 *	充值卡种类名称
	 */
	public void setCardTypeNames(String[] cardTypeNames){
		this.cardTypeNames = cardTypeNames;
	}

	/**
	 * 获取属性：
	 *	充值卡种类ID
	 */
	public byte[] getCardTypeIds(){
		return cardTypeIds;
	}

	/**
	 * 设置属性：
	 *	充值卡种类ID
	 */
	public void setCardTypeIds(byte[] cardTypeIds){
		this.cardTypeIds = cardTypeIds;
	}

	/**
	 * 获取属性：
	 *	每种充值卡的说明名字
	 */
	public String[] getCardTypeInfos(){
		return cardTypeInfos;
	}

	/**
	 * 设置属性：
	 *	每种充值卡的说明名字
	 */
	public void setCardTypeInfos(String[] cardTypeInfos){
		this.cardTypeInfos = cardTypeInfos;
	}

	/**
	 * 获取属性：
	 *	每种充值卡的面值，所有面额用一个字符串，用分隔号隔开，例如10：20：30
	 */
	public String[] getFacevalues(){
		return facevalues;
	}

	/**
	 * 设置属性：
	 *	每种充值卡的面值，所有面额用一个字符串，用分隔号隔开，例如10：20：30
	 */
	public void setFacevalues(String[] facevalues){
		this.facevalues = facevalues;
	}

	/**
	 * 获取属性：
	 *	兑换元宝的数量，所有兑换的数量用一个字符串，用分隔号隔开，例如120：240：360
	 */
	public String[] getExchange(){
		return exchange;
	}

	/**
	 * 设置属性：
	 *	兑换元宝的数量，所有兑换的数量用一个字符串，用分隔号隔开，例如120：240：360
	 */
	public void setExchange(String[] exchange){
		this.exchange = exchange;
	}

}