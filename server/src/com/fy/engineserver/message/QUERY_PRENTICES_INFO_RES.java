package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求徒弟信息，徒弟名，等级，帮会，拜师时间，出师时间，最近上线<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[0]</td><td>String</td><td>names[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[1]</td><td>String</td><td>names[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[2]</td><td>String</td><td>names[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isOnline.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isOnline</td><td>byte[]</td><td>isOnline.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>uub.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>uub[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>uub[0]</td><td>String</td><td>uub[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>uub[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>uub[1]</td><td>String</td><td>uub[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>uub[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>uub[2]</td><td>String</td><td>uub[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isCurrent.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isCurrent</td><td>boolean[]</td><td>isCurrent.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totalPage</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentPage</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_PRENTICES_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] names;
	long[] ids;
	byte[] isOnline;
	String[] uub;
	boolean[] isCurrent;
	int totalPage;
	int currentPage;

	public QUERY_PRENTICES_INFO_RES(){
	}

	public QUERY_PRENTICES_INFO_RES(long seqNum,String[] names,long[] ids,byte[] isOnline,String[] uub,boolean[] isCurrent,int totalPage,int currentPage){
		this.seqNum = seqNum;
		this.names = names;
		this.ids = ids;
		this.isOnline = isOnline;
		this.uub = uub;
		this.isCurrent = isCurrent;
		this.totalPage = totalPage;
		this.currentPage = currentPage;
	}

	public QUERY_PRENTICES_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		names = new String[len];
		for(int i = 0 ; i < names.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			names[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
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
		isOnline = new byte[len];
		System.arraycopy(content,offset,isOnline,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		uub = new String[len];
		for(int i = 0 ; i < uub.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			uub[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		isCurrent = new boolean[len];
		for(int i = 0 ; i < isCurrent.length ; i++){
			isCurrent[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		totalPage = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currentPage = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x7000AF12;
	}

	public String getTypeDescription() {
		return "QUERY_PRENTICES_INFO_RES";
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
		for(int i = 0 ; i < names.length; i++){
			len += 2;
			try{
				len += names[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += isOnline.length;
		len += 4;
		for(int i = 0 ; i < uub.length; i++){
			len += 2;
			try{
				len += uub[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += isCurrent.length;
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

			buffer.putInt(names.length);
			for(int i = 0 ; i < names.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = names[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(isOnline.length);
			buffer.put(isOnline);
			buffer.putInt(uub.length);
			for(int i = 0 ; i < uub.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = uub[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(isCurrent.length);
			for(int i = 0 ; i < isCurrent.length; i++){
				buffer.put((byte)(isCurrent[i]==false?0:1));
			}
			buffer.putInt(totalPage);
			buffer.putInt(currentPage);
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
	 *	徒弟名字
	 */
	public String[] getNames(){
		return names;
	}

	/**
	 * 设置属性：
	 *	徒弟名字
	 */
	public void setNames(String[] names){
		this.names = names;
	}

	/**
	 * 获取属性：
	 *	玩家ID
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	玩家ID
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	是否在线 0离线 1在线
	 */
	public byte[] getIsOnline(){
		return isOnline;
	}

	/**
	 * 设置属性：
	 *	是否在线 0离线 1在线
	 */
	public void setIsOnline(byte[] isOnline){
		this.isOnline = isOnline;
	}

	/**
	 * 获取属性：
	 *	徒弟名，等级，师德，师徒积分，帮会，拜师时间，出师时间，最近上线
	 */
	public String[] getUub(){
		return uub;
	}

	/**
	 * 设置属性：
	 *	徒弟名，等级，师德，师徒积分，帮会，拜师时间，出师时间，最近上线
	 */
	public void setUub(String[] uub){
		this.uub = uub;
	}

	/**
	 * 获取属性：
	 *	是否是当前的徒弟
	 */
	public boolean[] getIsCurrent(){
		return isCurrent;
	}

	/**
	 * 设置属性：
	 *	是否是当前的徒弟
	 */
	public void setIsCurrent(boolean[] isCurrent){
		this.isCurrent = isCurrent;
	}

	/**
	 * 获取属性：
	 *	总页数
	 */
	public int getTotalPage(){
		return totalPage;
	}

	/**
	 * 设置属性：
	 *	总页数
	 */
	public void setTotalPage(int totalPage){
		this.totalPage = totalPage;
	}

	/**
	 * 获取属性：
	 *	当前页码
	 */
	public int getCurrentPage(){
		return currentPage;
	}

	/**
	 * 设置属性：
	 *	当前页码
	 */
	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}

}