package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.trade.boothsale.BoothInfo4Client;
import com.fy.engineserver.trade.boothsale.MessageBoardInfo4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询玩家的摊位<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>salerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>salername.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>salername</td><td>String</td><td>salername.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>advertising.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>advertising</td><td>String</td><td>advertising.length</td><td>字符串对应的byte数组</td></tr>
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
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client.length</td><td>int</td><td>4个字节</td><td>MessageBoardInfo4Client数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[0].name</td><td>String</td><td>messageBoardInfo4Client[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[0].content.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[0].content</td><td>String</td><td>messageBoardInfo4Client[0].content.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[0].color.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[0].color</td><td>String</td><td>messageBoardInfo4Client[0].color.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[1].name</td><td>String</td><td>messageBoardInfo4Client[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[1].content.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[1].content</td><td>String</td><td>messageBoardInfo4Client[1].content.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[1].color.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[1].color</td><td>String</td><td>messageBoardInfo4Client[1].color.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[2].name</td><td>String</td><td>messageBoardInfo4Client[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[2].content.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[2].content</td><td>String</td><td>messageBoardInfo4Client[2].content.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messageBoardInfo4Client[2].color.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messageBoardInfo4Client[2].color</td><td>String</td><td>messageBoardInfo4Client[2].color.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buyTax</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class BOOTHSALE_LOOK_OVER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long salerId;
	String salername;
	String advertising;
	BoothInfo4Client boothInfo4Client;
	MessageBoardInfo4Client[] messageBoardInfo4Client;
	int buyTax;

	public BOOTHSALE_LOOK_OVER_RES(){
	}

	public BOOTHSALE_LOOK_OVER_RES(long seqNum,long salerId,String salername,String advertising,BoothInfo4Client boothInfo4Client,MessageBoardInfo4Client[] messageBoardInfo4Client,int buyTax){
		this.seqNum = seqNum;
		this.salerId = salerId;
		this.salername = salername;
		this.advertising = advertising;
		this.boothInfo4Client = boothInfo4Client;
		this.messageBoardInfo4Client = messageBoardInfo4Client;
		this.buyTax = buyTax;
	}

	public BOOTHSALE_LOOK_OVER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		salerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		salername = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		advertising = new String(content,offset,len,"UTF-8");
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
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		messageBoardInfo4Client = new MessageBoardInfo4Client[len];
		for(int i = 0 ; i < messageBoardInfo4Client.length ; i++){
			messageBoardInfo4Client[i] = new MessageBoardInfo4Client();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messageBoardInfo4Client[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messageBoardInfo4Client[i].setContent(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messageBoardInfo4Client[i].setColor(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		buyTax = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F00001;
	}

	public String getTypeDescription() {
		return "BOOTHSALE_LOOK_OVER_RES";
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
		len += 2;
		try{
			len +=salername.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=advertising.getBytes("UTF-8").length;
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
		len += 4;
		for(int i = 0 ; i < messageBoardInfo4Client.length ; i++){
			len += 2;
			if(messageBoardInfo4Client[i].getName() != null){
				try{
				len += messageBoardInfo4Client[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(messageBoardInfo4Client[i].getContent() != null){
				try{
				len += messageBoardInfo4Client[i].getContent().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(messageBoardInfo4Client[i].getColor() != null){
				try{
				len += messageBoardInfo4Client[i].getColor().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
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

			buffer.putLong(salerId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = salername.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = advertising.getBytes("UTF-8");
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
			buffer.putInt(messageBoardInfo4Client.length);
			for(int i = 0 ; i < messageBoardInfo4Client.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = messageBoardInfo4Client[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = messageBoardInfo4Client[i].getContent().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = messageBoardInfo4Client[i].getColor().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(buyTax);
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
	 *	摆摊人的ID
	 */
	public long getSalerId(){
		return salerId;
	}

	/**
	 * 设置属性：
	 *	摆摊人的ID
	 */
	public void setSalerId(long salerId){
		this.salerId = salerId;
	}

	/**
	 * 获取属性：
	 *	招牌
	 */
	public String getSalername(){
		return salername;
	}

	/**
	 * 设置属性：
	 *	招牌
	 */
	public void setSalername(String salername){
		this.salername = salername;
	}

	/**
	 * 获取属性：
	 *	广告语
	 */
	public String getAdvertising(){
		return advertising;
	}

	/**
	 * 设置属性：
	 *	广告语
	 */
	public void setAdvertising(String advertising){
		this.advertising = advertising;
	}

	/**
	 * 获取属性：
	 *	摊位内容
	 */
	public BoothInfo4Client getBoothInfo4Client(){
		return boothInfo4Client;
	}

	/**
	 * 设置属性：
	 *	摊位内容
	 */
	public void setBoothInfo4Client(BoothInfo4Client boothInfo4Client){
		this.boothInfo4Client = boothInfo4Client;
	}

	/**
	 * 获取属性：
	 *	摊位聊天版内容
	 */
	public MessageBoardInfo4Client[] getMessageBoardInfo4Client(){
		return messageBoardInfo4Client;
	}

	/**
	 * 设置属性：
	 *	摊位聊天版内容
	 */
	public void setMessageBoardInfo4Client(MessageBoardInfo4Client[] messageBoardInfo4Client){
		this.messageBoardInfo4Client = messageBoardInfo4Client;
	}

	/**
	 * 获取属性：
	 *	买摆摊物品收的税率,要除100
	 */
	public int getBuyTax(){
		return buyTax;
	}

	/**
	 * 设置属性：
	 *	买摆摊物品收的税率,要除100
	 */
	public void setBuyTax(int buyTax){
		this.buyTax = buyTax;
	}

}