package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 灭世游戏获取资源版本信息程序版本信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needToUpdateProgram</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needToForceUpdate</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverProgramVersion.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverProgramVersion</td><td>String</td><td>serverProgramVersion.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>newPackageSize</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>link.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>link</td><td>String</td><td>link.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverResourceVersion.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverResourceVersion</td><td>String</td><td>serverResourceVersion.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class MIESHI_GET_VERSION_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean needToUpdateProgram;
	boolean needToForceUpdate;
	String serverProgramVersion;
	int newPackageSize;
	String link;
	String serverResourceVersion;

	public MIESHI_GET_VERSION_INFO_RES(){
	}

	public MIESHI_GET_VERSION_INFO_RES(long seqNum,boolean needToUpdateProgram,boolean needToForceUpdate,String serverProgramVersion,int newPackageSize,String link,String serverResourceVersion){
		this.seqNum = seqNum;
		this.needToUpdateProgram = needToUpdateProgram;
		this.needToForceUpdate = needToForceUpdate;
		this.serverProgramVersion = serverProgramVersion;
		this.newPackageSize = newPackageSize;
		this.link = link;
		this.serverResourceVersion = serverResourceVersion;
	}

	public MIESHI_GET_VERSION_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		needToUpdateProgram = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		needToForceUpdate = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		serverProgramVersion = new String(content,offset,len,"UTF-8");
		offset += len;
		newPackageSize = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		link = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		serverResourceVersion = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8002A016;
	}

	public String getTypeDescription() {
		return "MIESHI_GET_VERSION_INFO_RES";
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
		len += 1;
		len += 2;
		try{
			len +=serverProgramVersion.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 2;
		try{
			len +=link.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=serverResourceVersion.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.put((byte)(needToUpdateProgram==false?0:1));
			buffer.put((byte)(needToForceUpdate==false?0:1));
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = serverProgramVersion.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(newPackageSize);
				try{
			tmpBytes1 = link.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = serverResourceVersion.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	是否有新版本要更新
	 */
	public boolean getNeedToUpdateProgram(){
		return needToUpdateProgram;
	}

	/**
	 * 设置属性：
	 *	是否有新版本要更新
	 */
	public void setNeedToUpdateProgram(boolean needToUpdateProgram){
		this.needToUpdateProgram = needToUpdateProgram;
	}

	/**
	 * 获取属性：
	 *	是否需要强制更新
	 */
	public boolean getNeedToForceUpdate(){
		return needToForceUpdate;
	}

	/**
	 * 设置属性：
	 *	是否需要强制更新
	 */
	public void setNeedToForceUpdate(boolean needToForceUpdate){
		this.needToForceUpdate = needToForceUpdate;
	}

	/**
	 * 获取属性：
	 *	服务端程序版本
	 */
	public String getServerProgramVersion(){
		return serverProgramVersion;
	}

	/**
	 * 设置属性：
	 *	服务端程序版本
	 */
	public void setServerProgramVersion(String serverProgramVersion){
		this.serverProgramVersion = serverProgramVersion;
	}

	/**
	 * 获取属性：
	 *	新程序包的大小，不需要更新返回0
	 */
	public int getNewPackageSize(){
		return newPackageSize;
	}

	/**
	 * 设置属性：
	 *	新程序包的大小，不需要更新返回0
	 */
	public void setNewPackageSize(int newPackageSize){
		this.newPackageSize = newPackageSize;
	}

	/**
	 * 获取属性：
	 *	服务器端程序版本链接，不需要更新返回空字符
	 */
	public String getLink(){
		return link;
	}

	/**
	 * 设置属性：
	 *	服务器端程序版本链接，不需要更新返回空字符
	 */
	public void setLink(String link){
		this.link = link;
	}

	/**
	 * 获取属性：
	 *	服务器端资源版本
	 */
	public String getServerResourceVersion(){
		return serverResourceVersion;
	}

	/**
	 * 设置属性：
	 *	服务器端资源版本
	 */
	public void setServerResourceVersion(String serverResourceVersion){
		this.serverResourceVersion = serverResourceVersion;
	}

}