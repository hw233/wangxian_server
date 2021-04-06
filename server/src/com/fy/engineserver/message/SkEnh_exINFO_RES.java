package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.skill.master.ExchangeConf;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>point</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exConf.length</td><td>int</td><td>4个字节</td><td>ExchangeConf数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exConf[0].startTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exConf[0].endTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exConf[0].needExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exConf[0].desc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exConf[0].desc</td><td>String</td><td>exConf[0].desc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exConf[1].startTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exConf[1].endTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exConf[1].needExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exConf[1].desc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exConf[1].desc</td><td>String</td><td>exConf[1].desc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exConf[2].startTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exConf[2].endTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exConf[2].needExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exConf[2].desc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exConf[2].desc</td><td>String</td><td>exConf[2].desc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftTimes</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exchangeTip.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exchangeTip</td><td>String</td><td>exchangeTip.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moneyPerPoint</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SkEnh_exINFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int point;
	int exTimes;
	ExchangeConf[] exConf;
	long leftTimes;
	String exchangeTip;
	int moneyPerPoint;

	public SkEnh_exINFO_RES(){
	}

	public SkEnh_exINFO_RES(long seqNum,int point,int exTimes,ExchangeConf[] exConf,long leftTimes,String exchangeTip,int moneyPerPoint){
		this.seqNum = seqNum;
		this.point = point;
		this.exTimes = exTimes;
		this.exConf = exConf;
		this.leftTimes = leftTimes;
		this.exchangeTip = exchangeTip;
		this.moneyPerPoint = moneyPerPoint;
	}

	public SkEnh_exINFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		point = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		exTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		exConf = new ExchangeConf[len];
		for(int i = 0 ; i < exConf.length ; i++){
			exConf[i] = new ExchangeConf();
			exConf[i].setStartTimes((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			exConf[i].setEndTimes((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			exConf[i].setNeedExp((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			exConf[i].setDesc(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		leftTimes = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		exchangeTip = new String(content,offset,len,"UTF-8");
		offset += len;
		moneyPerPoint = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x8E0EAA6E;
	}

	public String getTypeDescription() {
		return "SkEnh_exINFO_RES";
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
		len += 4;
		len += 4;
		for(int i = 0 ; i < exConf.length ; i++){
			len += 4;
			len += 4;
			len += 8;
			len += 2;
			if(exConf[i].getDesc() != null){
				try{
				len += exConf[i].getDesc().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 8;
		len += 2;
		try{
			len +=exchangeTip.getBytes("UTF-8").length;
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

			buffer.putInt(point);
			buffer.putInt(exTimes);
			buffer.putInt(exConf.length);
			for(int i = 0 ; i < exConf.length ; i++){
				buffer.putInt((int)exConf[i].getStartTimes());
				buffer.putInt((int)exConf[i].getEndTimes());
				buffer.putLong(exConf[i].getNeedExp());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = exConf[i].getDesc().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putLong(leftTimes);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = exchangeTip.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(moneyPerPoint);
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
	 *	现有点数
	 */
	public int getPoint(){
		return point;
	}

	/**
	 * 设置属性：
	 *	现有点数
	 */
	public void setPoint(int point){
		this.point = point;
	}

	/**
	 * 获取属性：
	 *	已兑换次数
	 */
	public int getExTimes(){
		return exTimes;
	}

	/**
	 * 设置属性：
	 *	已兑换次数
	 */
	public void setExTimes(int exTimes){
		this.exTimes = exTimes;
	}

	/**
	 * 获取属性：
	 *	经验兑换点数配置
	 */
	public ExchangeConf[] getExConf(){
		return exConf;
	}

	/**
	 * 设置属性：
	 *	经验兑换点数配置
	 */
	public void setExConf(ExchangeConf[] exConf){
		this.exConf = exConf;
	}

	/**
	 * 获取属性：
	 *	可兑换次数
	 */
	public long getLeftTimes(){
		return leftTimes;
	}

	/**
	 * 设置属性：
	 *	可兑换次数
	 */
	public void setLeftTimes(long leftTimes){
		this.leftTimes = leftTimes;
	}

	/**
	 * 获取属性：
	 *	兑换总体描述
	 */
	public String getExchangeTip(){
		return exchangeTip;
	}

	/**
	 * 设置属性：
	 *	兑换总体描述
	 */
	public void setExchangeTip(String exchangeTip){
		this.exchangeTip = exchangeTip;
	}

	/**
	 * 获取属性：
	 *	当前兑换1点需要多少（两）银子，0表示不花钱
	 */
	public int getMoneyPerPoint(){
		return moneyPerPoint;
	}

	/**
	 * 设置属性：
	 *	当前兑换1点需要多少（两）银子，0表示不花钱
	 */
	public void setMoneyPerPoint(int moneyPerPoint){
		this.moneyPerPoint = moneyPerPoint;
	}

}