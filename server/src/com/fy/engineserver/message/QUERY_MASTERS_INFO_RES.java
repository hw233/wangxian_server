package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求自己的身份，师德，师徒积分，拜师时间，出师时间， 师傅的 师傅名，等级，，师德，师徒积分，帮会，拜师时间，出师时间，最近上线,是否在线<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isOnline</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>uub.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>uub</td><td>String</td><td>uub.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>status</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_MASTERS_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long id;
	String name;
	byte isOnline;
	String uub;
	byte status;

	public QUERY_MASTERS_INFO_RES(){
	}

	public QUERY_MASTERS_INFO_RES(long seqNum,long id,String name,byte isOnline,String uub,byte status){
		this.seqNum = seqNum;
		this.id = id;
		this.name = name;
		this.isOnline = isOnline;
		this.uub = uub;
		this.status = status;
	}

	public QUERY_MASTERS_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		isOnline = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		uub = new String(content,offset,len,"UTF-8");
		offset += len;
		status = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x7000AF11;
	}

	public String getTypeDescription() {
		return "QUERY_MASTERS_INFO_RES";
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
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 2;
		try{
			len +=uub.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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

			buffer.putLong(id);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(isOnline);
				try{
			tmpBytes1 = uub.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(status);
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
	 *	玩家ID
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	玩家ID
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	师傅名字
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	师傅名字
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	是否在线 0离线 1在线
	 */
	public byte getIsOnline(){
		return isOnline;
	}

	/**
	 * 设置属性：
	 *	是否在线 0离线 1在线
	 */
	public void setIsOnline(byte isOnline){
		this.isOnline = isOnline;
	}

	/**
	 * 获取属性：
	 *	自己 和 师傅信息UUB
	 */
	public String getUub(){
		return uub;
	}

	/**
	 * 设置属性：
	 *	自己 和 师傅信息UUB
	 */
	public void setUub(String uub){
		this.uub = uub;
	}

	/**
	 * 获取属性：
	 *	是否已经出师 0未出师 1已经出师
	 */
	public byte getStatus(){
		return status;
	}

	/**
	 * 设置属性：
	 *	是否已经出师 0未出师 1已经出师
	 */
	public void setStatus(byte status){
		this.status = status;
	}

}