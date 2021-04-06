package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 保存一个物品实体<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showName</td><td>String</td><td>showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inValidTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskArticle</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>abandoned</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propertyString.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propertyString</td><td>String</td><td>propertyString.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class CACHE_ARTICLEENTITY_UPDATE_REQ implements RequestMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	long id;
	String showName;
	long inValidTime;
	boolean taskArticle;
	boolean binded;
	boolean abandoned;
	String propertyString;

	public CACHE_ARTICLEENTITY_UPDATE_REQ(long seqNum,long id,String showName,long inValidTime,boolean taskArticle,boolean binded,boolean abandoned,String propertyString){
		this.seqNum = seqNum;
		this.id = id;
		this.showName = showName;
		this.inValidTime = inValidTime;
		this.taskArticle = taskArticle;
		this.binded = binded;
		this.abandoned = abandoned;
		this.propertyString = propertyString;
	}

	public CACHE_ARTICLEENTITY_UPDATE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		showName = new String(content,offset,len,"UTF-8");
		offset += len;
		inValidTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		taskArticle = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		binded = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		abandoned = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		propertyString = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x00000014;
	}

	public String getTypeDescription() {
		return "CACHE_ARTICLEENTITY_UPDATE_REQ";
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
			len +=showName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 1;
		len += 1;
		len += 1;
		len += 2;
		try{
			len +=propertyString.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putLong(id);
			byte[] tmpBytes1 = showName.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(inValidTime);
			buffer.put((byte)(taskArticle==false?0:1));
			buffer.put((byte)(binded==false?0:1));
			buffer.put((byte)(abandoned==false?0:1));
			tmpBytes1 = propertyString.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	id
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	id
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	显示名称
	 */
	public String getShowName(){
		return showName;
	}

	/**
	 * 设置属性：
	 *	显示名称
	 */
	public void setShowName(String showName){
		this.showName = showName;
	}

	/**
	 * 获取属性：
	 *	有效期
	 */
	public long getInValidTime(){
		return inValidTime;
	}

	/**
	 * 设置属性：
	 *	有效期
	 */
	public void setInValidTime(long inValidTime){
		this.inValidTime = inValidTime;
	}

	/**
	 * 获取属性：
	 *	是否任务物品
	 */
	public boolean getTaskArticle(){
		return taskArticle;
	}

	/**
	 * 设置属性：
	 *	是否任务物品
	 */
	public void setTaskArticle(boolean taskArticle){
		this.taskArticle = taskArticle;
	}

	/**
	 * 获取属性：
	 *	是否绑定
	 */
	public boolean getBinded(){
		return binded;
	}

	/**
	 * 设置属性：
	 *	是否绑定
	 */
	public void setBinded(boolean binded){
		this.binded = binded;
	}

	/**
	 * 获取属性：
	 *	是否遗弃
	 */
	public boolean getAbandoned(){
		return abandoned;
	}

	/**
	 * 设置属性：
	 *	是否遗弃
	 */
	public void setAbandoned(boolean abandoned){
		this.abandoned = abandoned;
	}

	/**
	 * 获取属性：
	 *	属性字符串
	 */
	public String getPropertyString(){
		return propertyString;
	}

	/**
	 * 设置属性：
	 *	属性字符串
	 */
	public void setPropertyString(String propertyString){
		this.propertyString = propertyString;
	}

}
