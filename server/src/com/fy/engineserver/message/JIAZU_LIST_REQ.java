package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求获取家族列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderby</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isasc</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>keyword.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>keyword</td><td>String</td><td>keyword.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pagenum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pageindex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class JIAZU_LIST_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int orderby;
	boolean isasc;
	String keyword;
	int pagenum;
	int pageindex;

	public JIAZU_LIST_REQ(){
	}

	public JIAZU_LIST_REQ(long seqNum,int orderby,boolean isasc,String keyword,int pagenum,int pageindex){
		this.seqNum = seqNum;
		this.orderby = orderby;
		this.isasc = isasc;
		this.keyword = keyword;
		this.pagenum = pagenum;
		this.pageindex = pageindex;
	}

	public JIAZU_LIST_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		orderby = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		isasc = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		keyword = new String(content,offset,len,"UTF-8");
		offset += len;
		pagenum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		pageindex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x000AEE02;
	}

	public String getTypeDescription() {
		return "JIAZU_LIST_REQ";
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
		len += 1;
		len += 2;
		try{
			len +=keyword.getBytes("UTF-8").length;
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

			buffer.putInt(orderby);
			buffer.put((byte)(isasc==false?0:1));
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = keyword.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(pagenum);
			buffer.putInt(pageindex);
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
	 *	排序方式,0 创建时间 1 实力榜
	 */
	public int getOrderby(){
		return orderby;
	}

	/**
	 * 设置属性：
	 *	排序方式,0 创建时间 1 实力榜
	 */
	public void setOrderby(int orderby){
		this.orderby = orderby;
	}

	/**
	 * 获取属性：
	 *	是否升序，ture是，false为降序
	 */
	public boolean getIsasc(){
		return isasc;
	}

	/**
	 * 设置属性：
	 *	是否升序，ture是，false为降序
	 */
	public void setIsasc(boolean isasc){
		this.isasc = isasc;
	}

	/**
	 * 获取属性：
	 *	模糊查询关键字
	 */
	public String getKeyword(){
		return keyword;
	}

	/**
	 * 设置属性：
	 *	模糊查询关键字
	 */
	public void setKeyword(String keyword){
		this.keyword = keyword;
	}

	/**
	 * 获取属性：
	 *	每页的显示数量,20
	 */
	public int getPagenum(){
		return pagenum;
	}

	/**
	 * 设置属性：
	 *	每页的显示数量,20
	 */
	public void setPagenum(int pagenum){
		this.pagenum = pagenum;
	}

	/**
	 * 获取属性：
	 *	现实第几页:0,1,2,3...
	 */
	public int getPageindex(){
		return pageindex;
	}

	/**
	 * 设置属性：
	 *	现实第几页:0,1,2,3...
	 */
	public void setPageindex(int pageindex){
		this.pageindex = pageindex;
	}

}