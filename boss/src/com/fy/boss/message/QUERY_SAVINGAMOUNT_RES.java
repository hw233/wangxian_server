package com.fy.boss.message;

import com.fy.boss.message.BossMessageFactory;
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
 * <tr bgcolor="#FAFAFA" align="center"><td>amount</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherResults.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherResults[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherResults[0]</td><td>String</td><td>otherResults[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherResults[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherResults[1]</td><td>String</td><td>otherResults[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherResults[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherResults[2]</td><td>String</td><td>otherResults[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_SAVINGAMOUNT_RES implements ResponseMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	long amount;
	String[] otherResults;

	public QUERY_SAVINGAMOUNT_RES(){
	}

	public QUERY_SAVINGAMOUNT_RES(long seqNum,long amount,String[] otherResults){
		this.seqNum = seqNum;
		this.amount = amount;
		this.otherResults = otherResults;
	}

	public QUERY_SAVINGAMOUNT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		amount = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 5024000) throw new Exception("array length ["+len+"] big than the max length [5024000]");
		otherResults = new String[len];
		for(int i = 0 ; i < otherResults.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
			otherResults[i] = new String(content,offset,len);
		offset += len;
		}
	}

	public int getType() {
		return 0x8000F038;
	}

	public String getTypeDescription() {
		return "QUERY_SAVINGAMOUNT_RES";
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
		for(int i = 0 ; i < otherResults.length; i++){
			len += 2;
			len += otherResults[i].getBytes().length;
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

			buffer.putLong(amount);
			buffer.putInt(otherResults.length);
			for(int i = 0 ; i < otherResults.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = otherResults[i].getBytes();
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
	 *	充值总额
	 */
	public long getAmount(){
		return amount;
	}

	/**
	 * 设置属性：
	 *	充值总额
	 */
	public void setAmount(long amount){
		this.amount = amount;
	}

	/**
	 * 获取属性：
	 *	留着扩展用
	 */
	public String[] getOtherResults(){
		return otherResults;
	}

	/**
	 * 设置属性：
	 *	留着扩展用
	 */
	public void setOtherResults(String[] otherResults){
		this.otherResults = otherResults;
	}

}