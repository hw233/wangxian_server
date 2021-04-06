package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 恶魔广场中各种倒计时<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>contentmass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>contentmass</td><td>String</td><td>contentmass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class DEVILSQUARE_COUNTDOWNTIME_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String contentmass;
	int countType;
	int countTime;

	public DEVILSQUARE_COUNTDOWNTIME_REQ(){
	}

	public DEVILSQUARE_COUNTDOWNTIME_REQ(long seqNum,String contentmass,int countType,int countTime){
		this.seqNum = seqNum;
		this.contentmass = contentmass;
		this.countType = countType;
		this.countTime = countTime;
	}

	public DEVILSQUARE_COUNTDOWNTIME_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		contentmass = new String(content,offset,len,"UTF-8");
		offset += len;
		countType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		countTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0E0EAA90;
	}

	public String getTypeDescription() {
		return "DEVILSQUARE_COUNTDOWNTIME_REQ";
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
			len +=contentmass.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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
			 tmpBytes1 = contentmass.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(countType);
			buffer.putInt(countTime);
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
	 *	内容
	 */
	public String getContentmass(){
		return contentmass;
	}

	/**
	 * 设置属性：
	 *	内容
	 */
	public void setContentmass(String contentmass){
		this.contentmass = contentmass;
	}

	/**
	 * 获取属性：
	 *	类型，0:入场倒计时  1:准备倒计时   2:刷怪倒计时   3:清怪倒计时 4:刷boss倒计时  5:退出副本倒计时
	 */
	public int getCountType(){
		return countType;
	}

	/**
	 * 设置属性：
	 *	类型，0:入场倒计时  1:准备倒计时   2:刷怪倒计时   3:清怪倒计时 4:刷boss倒计时  5:退出副本倒计时
	 */
	public void setCountType(int countType){
		this.countType = countType;
	}

	/**
	 * 获取属性：
	 *	时间
	 */
	public int getCountTime(){
		return countTime;
	}

	/**
	 * 设置属性：
	 *	时间
	 */
	public void setCountTime(int countTime){
		this.countTime = countTime;
	}

}