package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 随身获取邮件列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mtype.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mtype</td><td>byte[]</td><td>mtype.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>status.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>status</td><td>byte[]</td><td>status.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titles.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titles[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titles[0]</td><td>String</td><td>titles[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titles[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titles[1]</td><td>String</td><td>titles[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titles[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titles[2]</td><td>String</td><td>titles[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>senderNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>senderNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>senderNames[0]</td><td>String</td><td>senderNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>senderNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>senderNames[1]</td><td>String</td><td>senderNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>senderNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>senderNames[2]</td><td>String</td><td>senderNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>expiredate.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>expiredate</td><td>long[]</td><td>expiredate.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pages</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nowpage</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>price.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>price</td><td>long[]</td><td>price.length</td><td>*</td></tr>
 * </table>
 */
public class MAIL_LIST_CARRY_NEW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] ids;
	byte[] mtype;
	byte[] status;
	String[] titles;
	String[] senderNames;
	long[] expiredate;
	int pages;
	int nowpage;
	long[] price;

	public MAIL_LIST_CARRY_NEW_RES(){
	}

	public MAIL_LIST_CARRY_NEW_RES(long seqNum,long[] ids,byte[] mtype,byte[] status,String[] titles,String[] senderNames,long[] expiredate,int pages,int nowpage,long[] price){
		this.seqNum = seqNum;
		this.ids = ids;
		this.mtype = mtype;
		this.status = status;
		this.titles = titles;
		this.senderNames = senderNames;
		this.expiredate = expiredate;
		this.pages = pages;
		this.nowpage = nowpage;
		this.price = price;
	}

	public MAIL_LIST_CARRY_NEW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids = new long[len];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		mtype = new byte[len];
		System.arraycopy(content,offset,mtype,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		status = new byte[len];
		System.arraycopy(content,offset,status,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		titles = new String[len];
		for(int i = 0 ; i < titles.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			titles[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		senderNames = new String[len];
		for(int i = 0 ; i < senderNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			senderNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		expiredate = new long[len];
		for(int i = 0 ; i < expiredate.length ; i++){
			expiredate[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		pages = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		nowpage = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		price = new long[len];
		for(int i = 0 ; i < price.length ; i++){
			price[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x7000C022;
	}

	public String getTypeDescription() {
		return "MAIL_LIST_CARRY_NEW_RES";
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
		len += ids.length * 8;
		len += 4;
		len += mtype.length;
		len += 4;
		len += status.length;
		len += 4;
		for(int i = 0 ; i < titles.length; i++){
			len += 2;
			try{
				len += titles[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < senderNames.length; i++){
			len += 2;
			try{
				len += senderNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += expiredate.length * 8;
		len += 4;
		len += 4;
		len += 4;
		len += price.length * 8;
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

			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(mtype.length);
			buffer.put(mtype);
			buffer.putInt(status.length);
			buffer.put(status);
			buffer.putInt(titles.length);
			for(int i = 0 ; i < titles.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = titles[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(senderNames.length);
			for(int i = 0 ; i < senderNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = senderNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(expiredate.length);
			for(int i = 0 ; i < expiredate.length; i++){
				buffer.putLong(expiredate[i]);
			}
			buffer.putInt(pages);
			buffer.putInt(nowpage);
			buffer.putInt(price.length);
			for(int i = 0 ; i < price.length; i++){
				buffer.putLong(price[i]);
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
	 *	邮件的id
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	邮件的id
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	邮件的类型（0:系统,1:玩家）
	 */
	public byte[] getMtype(){
		return mtype;
	}

	/**
	 * 设置属性：
	 *	邮件的类型（0:系统,1:玩家）
	 */
	public void setMtype(byte[] mtype){
		this.mtype = mtype;
	}

	/**
	 * 获取属性：
	 *	邮件的状态（0:未打开,1:打开, 2:已删除）
	 */
	public byte[] getStatus(){
		return status;
	}

	/**
	 * 设置属性：
	 *	邮件的状态（0:未打开,1:打开, 2:已删除）
	 */
	public void setStatus(byte[] status){
		this.status = status;
	}

	/**
	 * 获取属性：
	 *	邮件的标题
	 */
	public String[] getTitles(){
		return titles;
	}

	/**
	 * 设置属性：
	 *	邮件的标题
	 */
	public void setTitles(String[] titles){
		this.titles = titles;
	}

	/**
	 * 获取属性：
	 *	邮件的标题
	 */
	public String[] getSenderNames(){
		return senderNames;
	}

	/**
	 * 设置属性：
	 *	邮件的标题
	 */
	public void setSenderNames(String[] senderNames){
		this.senderNames = senderNames;
	}

	/**
	 * 获取属性：
	 *	过期时间
	 */
	public long[] getExpiredate(){
		return expiredate;
	}

	/**
	 * 设置属性：
	 *	过期时间
	 */
	public void setExpiredate(long[] expiredate){
		this.expiredate = expiredate;
	}

	/**
	 * 获取属性：
	 *	总页数
	 */
	public int getPages(){
		return pages;
	}

	/**
	 * 设置属性：
	 *	总页数
	 */
	public void setPages(int pages){
		this.pages = pages;
	}

	/**
	 * 获取属性：
	 *	当前页
	 */
	public int getNowpage(){
		return nowpage;
	}

	/**
	 * 设置属性：
	 *	当前页
	 */
	public void setNowpage(int nowpage){
		this.nowpage = nowpage;
	}

	/**
	 * 获取属性：
	 *	付费
	 */
	public long[] getPrice(){
		return price;
	}

	/**
	 * 设置属性：
	 *	付费
	 */
	public void setPrice(long[] price){
		this.price = price;
	}

}