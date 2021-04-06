package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 灭世游戏请求资源文件列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fileVersion.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fileVersion</td><td>String</td><td>fileVersion.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fileLength</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>count</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class MIESHI_GET_RESOURCE_FILE_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int result;
	String fileVersion;
	long fileLength;
	long count;

	public MIESHI_GET_RESOURCE_FILE_LIST_RES(){
	}

	public MIESHI_GET_RESOURCE_FILE_LIST_RES(long seqNum,int result,String fileVersion,long fileLength,long count){
		this.seqNum = seqNum;
		this.result = result;
		this.fileVersion = fileVersion;
		this.fileLength = fileLength;
		this.count = count;
	}

	public MIESHI_GET_RESOURCE_FILE_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		fileVersion = new String(content,offset,len,"UTF-8");
		offset += len;
		fileLength = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		count = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x8002A017;
	}

	public String getTypeDescription() {
		return "MIESHI_GET_RESOURCE_FILE_LIST_RES";
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
		len += 2;
		try{
			len +=fileVersion.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putInt(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = fileVersion.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(fileLength);
			buffer.putLong(count);
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
	 *	结果，0标识成功，其他值表示资源文件不存在
	 */
	public int getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果，0标识成功，其他值表示资源文件不存在
	 */
	public void setResult(int result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	资源文件版本
	 */
	public String getFileVersion(){
		return fileVersion;
	}

	/**
	 * 设置属性：
	 *	资源文件版本
	 */
	public void setFileVersion(String fileVersion){
		this.fileVersion = fileVersion;
	}

	/**
	 * 获取属性：
	 *	资源文件大小，单位字节
	 */
	public long getFileLength(){
		return fileLength;
	}

	/**
	 * 设置属性：
	 *	资源文件大小，单位字节
	 */
	public void setFileLength(long fileLength){
		this.fileLength = fileLength;
	}

	/**
	 * 获取属性：
	 *	资源文件被拆分成多少个小包发送
	 */
	public long getCount(){
		return count;
	}

	/**
	 * 设置属性：
	 *	资源文件被拆分成多少个小包发送
	 */
	public void setCount(long count){
		this.count = count;
	}

}