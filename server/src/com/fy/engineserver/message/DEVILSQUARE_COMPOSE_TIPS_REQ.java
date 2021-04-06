package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 门票合成通知客户端<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>succeedRate.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>succeedRate</td><td>int[]</td><td>succeedRate.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>colorProps.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>colorProps</td><td>long[]</td><td>colorProps.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ticketProps.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ticketProps</td><td>long[]</td><td>ticketProps.length</td><td>*</td></tr>
 * </table>
 */
public class DEVILSQUARE_COMPOSE_TIPS_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] succeedRate;
	long[] colorProps;
	long[] ticketProps;

	public DEVILSQUARE_COMPOSE_TIPS_REQ(){
	}

	public DEVILSQUARE_COMPOSE_TIPS_REQ(long seqNum,int[] succeedRate,long[] colorProps,long[] ticketProps){
		this.seqNum = seqNum;
		this.succeedRate = succeedRate;
		this.colorProps = colorProps;
		this.ticketProps = ticketProps;
	}

	public DEVILSQUARE_COMPOSE_TIPS_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		succeedRate = new int[len];
		for(int i = 0 ; i < succeedRate.length ; i++){
			succeedRate[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		colorProps = new long[len];
		for(int i = 0 ; i < colorProps.length ; i++){
			colorProps[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ticketProps = new long[len];
		for(int i = 0 ; i < ticketProps.length ; i++){
			ticketProps[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x0E0EAA93;
	}

	public String getTypeDescription() {
		return "DEVILSQUARE_COMPOSE_TIPS_REQ";
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
		len += succeedRate.length * 4;
		len += 4;
		len += colorProps.length * 8;
		len += 4;
		len += ticketProps.length * 8;
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

			buffer.putInt(succeedRate.length);
			for(int i = 0 ; i < succeedRate.length; i++){
				buffer.putInt(succeedRate[i]);
			}
			buffer.putInt(colorProps.length);
			for(int i = 0 ; i < colorProps.length; i++){
				buffer.putLong(colorProps[i]);
			}
			buffer.putInt(ticketProps.length);
			for(int i = 0 ; i < ticketProps.length; i++){
				buffer.putLong(ticketProps[i]);
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
	 *	合成成功率
	 */
	public int[] getSucceedRate(){
		return succeedRate;
	}

	/**
	 * 设置属性：
	 *	合成成功率
	 */
	public void setSucceedRate(int[] succeedRate){
		this.succeedRate = succeedRate;
	}

	/**
	 * 获取属性：
	 *	颜色合成得到的物品列表
	 */
	public long[] getColorProps(){
		return colorProps;
	}

	/**
	 * 设置属性：
	 *	颜色合成得到的物品列表
	 */
	public void setColorProps(long[] colorProps){
		this.colorProps = colorProps;
	}

	/**
	 * 获取属性：
	 *	门票列表
	 */
	public long[] getTicketProps(){
		return ticketProps;
	}

	/**
	 * 设置属性：
	 *	门票列表
	 */
	public void setTicketProps(long[] ticketProps){
		this.ticketProps = ticketProps;
	}

}