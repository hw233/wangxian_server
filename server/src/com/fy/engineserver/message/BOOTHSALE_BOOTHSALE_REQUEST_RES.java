package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.trade.boothsale.BoothInfo4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 申请摆摊<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>failreason.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>failreason</td><td>String</td><td>failreason.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boothInfo4Client.entityId.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boothInfo4Client.entityId</td><td>long[]</td><td>boothInfo4Client.entityId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boothInfo4Client.counts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boothInfo4Client.counts</td><td>int[]</td><td>boothInfo4Client.counts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boothInfo4Client.perPrice.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boothInfo4Client.perPrice</td><td>long[]</td><td>boothInfo4Client.perPrice.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boothInfo4Client.knapType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boothInfo4Client.knapType</td><td>int[]</td><td>boothInfo4Client.knapType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boothInfo4Client.knapIndex.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boothInfo4Client.knapIndex</td><td>int[]</td><td>boothInfo4Client.knapIndex.length</td><td>*</td></tr>
 * </table>
 */
public class BOOTHSALE_BOOTHSALE_REQUEST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	String failreason;
	BoothInfo4Client boothInfo4Client;

	public BOOTHSALE_BOOTHSALE_REQUEST_RES(){
	}

	public BOOTHSALE_BOOTHSALE_REQUEST_RES(long seqNum,byte result,String failreason,BoothInfo4Client boothInfo4Client){
		this.seqNum = seqNum;
		this.result = result;
		this.failreason = failreason;
		this.boothInfo4Client = boothInfo4Client;
	}

	public BOOTHSALE_BOOTHSALE_REQUEST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		failreason = new String(content,offset,len,"UTF-8");
		offset += len;
		boothInfo4Client = new BoothInfo4Client();
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		long[] entityId_0001 = new long[len];
		for(int j = 0 ; j < entityId_0001.length ; j++){
			entityId_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		boothInfo4Client.setEntityId(entityId_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] counts_0002 = new int[len];
		for(int j = 0 ; j < counts_0002.length ; j++){
			counts_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		boothInfo4Client.setCounts(counts_0002);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		long[] perPrice_0003 = new long[len];
		for(int j = 0 ; j < perPrice_0003.length ; j++){
			perPrice_0003[j] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		boothInfo4Client.setPerPrice(perPrice_0003);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] knapType_0004 = new int[len];
		for(int j = 0 ; j < knapType_0004.length ; j++){
			knapType_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		boothInfo4Client.setKnapType(knapType_0004);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] knapIndex_0005 = new int[len];
		for(int j = 0 ; j < knapIndex_0005.length ; j++){
			knapIndex_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		boothInfo4Client.setKnapIndex(knapIndex_0005);
	}

	public int getType() {
		return 0x70F00008;
	}

	public String getTypeDescription() {
		return "BOOTHSALE_BOOTHSALE_REQUEST_RES";
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
		len += 2;
		try{
			len +=failreason.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += boothInfo4Client.getEntityId().length * 8;
		len += 4;
		len += boothInfo4Client.getCounts().length * 4;
		len += 4;
		len += boothInfo4Client.getPerPrice().length * 8;
		len += 4;
		len += boothInfo4Client.getKnapType().length * 4;
		len += 4;
		len += boothInfo4Client.getKnapIndex().length * 4;
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

			buffer.put(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = failreason.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(boothInfo4Client.getEntityId().length);
			long[] entityId_0006 = boothInfo4Client.getEntityId();
			for(int j = 0 ; j < entityId_0006.length ; j++){
				buffer.putLong(entityId_0006[j]);
			}
			buffer.putInt(boothInfo4Client.getCounts().length);
			int[] counts_0007 = boothInfo4Client.getCounts();
			for(int j = 0 ; j < counts_0007.length ; j++){
				buffer.putInt(counts_0007[j]);
			}
			buffer.putInt(boothInfo4Client.getPerPrice().length);
			long[] perPrice_0008 = boothInfo4Client.getPerPrice();
			for(int j = 0 ; j < perPrice_0008.length ; j++){
				buffer.putLong(perPrice_0008[j]);
			}
			buffer.putInt(boothInfo4Client.getKnapType().length);
			int[] knapType_0009 = boothInfo4Client.getKnapType();
			for(int j = 0 ; j < knapType_0009.length ; j++){
				buffer.putInt(knapType_0009[j]);
			}
			buffer.putInt(boothInfo4Client.getKnapIndex().length);
			int[] knapIndex_0010 = boothInfo4Client.getKnapIndex();
			for(int j = 0 ; j < knapIndex_0010.length ; j++){
				buffer.putInt(knapIndex_0010[j]);
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
	 *	结果,0-成功，1-失败
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果,0-成功，1-失败
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	失败的结果描述
	 */
	public String getFailreason(){
		return failreason;
	}

	/**
	 * 设置属性：
	 *	失败的结果描述
	 */
	public void setFailreason(String failreason){
		this.failreason = failreason;
	}

	/**
	 * 获取属性：
	 *	摊位内容,如果验证失败则传空
	 */
	public BoothInfo4Client getBoothInfo4Client(){
		return boothInfo4Client;
	}

	/**
	 * 设置属性：
	 *	摊位内容,如果验证失败则传空
	 */
	public void setBoothInfo4Client(BoothInfo4Client boothInfo4Client){
		this.boothInfo4Client = boothInfo4Client;
	}

}