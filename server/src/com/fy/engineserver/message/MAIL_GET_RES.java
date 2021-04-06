package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取邮件<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mtype</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>title</td><td>String</td><td>title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mcontent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mcontent</td><td>String</td><td>mcontent.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>senderId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>senderName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>senderName</td><td>String</td><td>senderName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entityIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityIds</td><td>long[]</td><td>entityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>counts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>counts</td><td>int[]</td><td>counts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>coins</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>senddate</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>expiredate</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>price</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class MAIL_GET_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte mtype;
	String title;
	String mcontent;
	long senderId;
	String senderName;
	long[] entityIds;
	int[] counts;
	long coins;
	long senddate;
	long expiredate;
	long price;

	public MAIL_GET_RES(){
	}

	public MAIL_GET_RES(long seqNum,byte mtype,String title,String mcontent,long senderId,String senderName,long[] entityIds,int[] counts,long coins,long senddate,long expiredate,long price){
		this.seqNum = seqNum;
		this.mtype = mtype;
		this.title = title;
		this.mcontent = mcontent;
		this.senderId = senderId;
		this.senderName = senderName;
		this.entityIds = entityIds;
		this.counts = counts;
		this.coins = coins;
		this.senddate = senddate;
		this.expiredate = expiredate;
		this.price = price;
	}

	public MAIL_GET_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		mtype = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		title = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		mcontent = new String(content,offset,len,"UTF-8");
		offset += len;
		senderId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		senderName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		entityIds = new long[len];
		for(int i = 0 ; i < entityIds.length ; i++){
			entityIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		counts = new int[len];
		for(int i = 0 ; i < counts.length ; i++){
			counts[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		coins = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		senddate = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		expiredate = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		price = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x7000C002;
	}

	public String getTypeDescription() {
		return "MAIL_GET_RES";
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
			len +=title.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=mcontent.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 2;
		try{
			len +=senderName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += entityIds.length * 8;
		len += 4;
		len += counts.length * 4;
		len += 8;
		len += 8;
		len += 8;
		len += 8;
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

			buffer.put(mtype);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = title.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = mcontent.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(senderId);
				try{
			tmpBytes1 = senderName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(entityIds.length);
			for(int i = 0 ; i < entityIds.length; i++){
				buffer.putLong(entityIds[i]);
			}
			buffer.putInt(counts.length);
			for(int i = 0 ; i < counts.length; i++){
				buffer.putInt(counts[i]);
			}
			buffer.putLong(coins);
			buffer.putLong(senddate);
			buffer.putLong(expiredate);
			buffer.putLong(price);
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
	 *	邮件的类型（0:系统,1:玩家）
	 */
	public byte getMtype(){
		return mtype;
	}

	/**
	 * 设置属性：
	 *	邮件的类型（0:系统,1:玩家）
	 */
	public void setMtype(byte mtype){
		this.mtype = mtype;
	}

	/**
	 * 获取属性：
	 *	邮件的标题
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * 设置属性：
	 *	邮件的标题
	 */
	public void setTitle(String title){
		this.title = title;
	}

	/**
	 * 获取属性：
	 *	邮件的内容
	 */
	public String getMcontent(){
		return mcontent;
	}

	/**
	 * 设置属性：
	 *	邮件的内容
	 */
	public void setMcontent(String mcontent){
		this.mcontent = mcontent;
	}

	/**
	 * 获取属性：
	 *	发送人id
	 */
	public long getSenderId(){
		return senderId;
	}

	/**
	 * 设置属性：
	 *	发送人id
	 */
	public void setSenderId(long senderId){
		this.senderId = senderId;
	}

	/**
	 * 获取属性：
	 *	发送人name
	 */
	public String getSenderName(){
		return senderName;
	}

	/**
	 * 设置属性：
	 *	发送人name
	 */
	public void setSenderName(String senderName){
		this.senderName = senderName;
	}

	/**
	 * 获取属性：
	 *	邮件格子中的物品
	 */
	public long[] getEntityIds(){
		return entityIds;
	}

	/**
	 * 设置属性：
	 *	邮件格子中的物品
	 */
	public void setEntityIds(long[] entityIds){
		this.entityIds = entityIds;
	}

	/**
	 * 获取属性：
	 *	对应的数量
	 */
	public int[] getCounts(){
		return counts;
	}

	/**
	 * 设置属性：
	 *	对应的数量
	 */
	public void setCounts(int[] counts){
		this.counts = counts;
	}

	/**
	 * 获取属性：
	 *	邮件中的金钱
	 */
	public long getCoins(){
		return coins;
	}

	/**
	 * 设置属性：
	 *	邮件中的金钱
	 */
	public void setCoins(long coins){
		this.coins = coins;
	}

	/**
	 * 获取属性：
	 *	邮件发送的时间(ms)
	 */
	public long getSenddate(){
		return senddate;
	}

	/**
	 * 设置属性：
	 *	邮件发送的时间(ms)
	 */
	public void setSenddate(long senddate){
		this.senddate = senddate;
	}

	/**
	 * 获取属性：
	 *	有效日期(ms)
	 */
	public long getExpiredate(){
		return expiredate;
	}

	/**
	 * 设置属性：
	 *	有效日期(ms)
	 */
	public void setExpiredate(long expiredate){
		this.expiredate = expiredate;
	}

	/**
	 * 获取属性：
	 *	价格(ms)
	 */
	public long getPrice(){
		return price;
	}

	/**
	 * 设置属性：
	 *	价格(ms)
	 */
	public void setPrice(long price){
		this.price = price;
	}

}