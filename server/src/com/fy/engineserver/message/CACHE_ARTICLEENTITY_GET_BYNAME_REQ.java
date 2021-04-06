package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取一个物品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleName</td><td>String</td><td>articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class CACHE_ARTICLEENTITY_GET_BYNAME_REQ implements RequestMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	String articleName;
	boolean binded;

	public CACHE_ARTICLEENTITY_GET_BYNAME_REQ(long seqNum,String articleName,boolean binded){
		this.seqNum = seqNum;
		this.articleName = articleName;
		this.binded = binded;
	}

	public CACHE_ARTICLEENTITY_GET_BYNAME_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		articleName = new String(content,offset,len,"UTF-8");
		offset += len;
		binded = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x00000022;
	}

	public String getTypeDescription() {
		return "CACHE_ARTICLEENTITY_GET_BYNAME_REQ";
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
			len +=articleName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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

			byte[] tmpBytes1 = articleName.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(binded==false?0:1));
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	articleName
	 */
	public String getArticleName(){
		return articleName;
	}

	/**
	 * 设置属性：
	 *	articleName
	 */
	public void setArticleName(String articleName){
		this.articleName = articleName;
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

}
