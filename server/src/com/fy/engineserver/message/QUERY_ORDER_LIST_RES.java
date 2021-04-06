package com.fy.engineserver.message;

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
 * <tr bgcolor="#FAFAFA" align="center"><td>pageIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pageSize</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxPageIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderList.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderList[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderList[0]</td><td>String</td><td>orderList[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderList[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderList[1]</td><td>String</td><td>orderList[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderList[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderList[2]</td><td>String</td><td>orderList[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_ORDER_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int pageIndex;
	int pageSize;
	int maxPageIndex;
	String[] orderList;

	public QUERY_ORDER_LIST_RES(){
	}

	public QUERY_ORDER_LIST_RES(long seqNum,int pageIndex,int pageSize,int maxPageIndex,String[] orderList){
		this.seqNum = seqNum;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.maxPageIndex = maxPageIndex;
		this.orderList = orderList;
	}

	public QUERY_ORDER_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		pageIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		pageSize = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxPageIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		orderList = new String[len];
		for(int i = 0 ; i < orderList.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			orderList[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x70EEEE07;
	}

	public String getTypeDescription() {
		return "QUERY_ORDER_LIST_RES";
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
		len += 4;
		for(int i = 0 ; i < orderList.length; i++){
			len += 2;
			try{
				len += orderList[i].getBytes("UTF-8").length;
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

			buffer.putInt(pageIndex);
			buffer.putInt(pageSize);
			buffer.putInt(maxPageIndex);
			buffer.putInt(orderList.length);
			for(int i = 0 ; i < orderList.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = orderList[i].getBytes("UTF-8");
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
	 *	页码
	 */
	public int getPageIndex(){
		return pageIndex;
	}

	/**
	 * 设置属性：
	 *	页码
	 */
	public void setPageIndex(int pageIndex){
		this.pageIndex = pageIndex;
	}

	/**
	 * 获取属性：
	 *	每页条数
	 */
	public int getPageSize(){
		return pageSize;
	}

	/**
	 * 设置属性：
	 *	每页条数
	 */
	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	/**
	 * 获取属性：
	 *	最大页数
	 */
	public int getMaxPageIndex(){
		return maxPageIndex;
	}

	/**
	 * 设置属性：
	 *	最大页数
	 */
	public void setMaxPageIndex(int maxPageIndex){
		this.maxPageIndex = maxPageIndex;
	}

	/**
	 * 获取属性：
	 *	充值记录
	 */
	public String[] getOrderList(){
		return orderList;
	}

	/**
	 * 设置属性：
	 *	充值记录
	 */
	public void setOrderList(String[] orderList){
		this.orderList = orderList;
	}

}