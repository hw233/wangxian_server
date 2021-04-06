package com.fy.boss.message;

import com.fy.boss.message.BossMessageFactory;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * QQ充值<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>uid.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>uid</td><td>String</td><td>uid.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goodcount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingType</td><td>String</td><td>savingType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverName</td><td>String</td><td>serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>userChannel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>userChannel</td><td>String</td><td>userChannel.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class QQ_SAVING_NEW_REQ implements RequestMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	String uid;
	int goodcount;
	String savingType;
	String serverName;
	String userChannel;

	public QQ_SAVING_NEW_REQ(){
	}

	public QQ_SAVING_NEW_REQ(long seqNum,String uid,int goodcount,String savingType,String serverName,String userChannel){
		this.seqNum = seqNum;
		this.uid = uid;
		this.goodcount = goodcount;
		this.savingType = savingType;
		this.serverName = serverName;
		this.userChannel = userChannel;
	}

	public QQ_SAVING_NEW_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		uid = new String(content,offset,len,"UTF-8");
		offset += len;
		goodcount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		savingType = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		serverName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		userChannel = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x0000A006;
	}

	public String getTypeDescription() {
		return "QQ_SAVING_NEW_REQ";
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
			len +=uid.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 2;
		len +=savingType.getBytes().length;
		len += 2;
		try{
			len +=serverName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=userChannel.getBytes("UTF-8").length;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = uid.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(goodcount);
			tmpBytes1 = savingType.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = serverName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = userChannel.getBytes("UTF-8");
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
	 *	腾讯获取的UID
	 */
	public String getUid(){
		return uid;
	}

	/**
	 * 设置属性：
	 *	腾讯获取的UID
	 */
	public void setUid(String uid){
		this.uid = uid;
	}

	/**
	 * 获取属性：
	 *	商品数量
	 */
	public int getGoodcount(){
		return goodcount;
	}

	/**
	 * 设置属性：
	 *	商品数量
	 */
	public void setGoodcount(int goodcount){
		this.goodcount = goodcount;
	}

	/**
	 * 获取属性：
	 *	充值类别
	 */
	public String getSavingType(){
		return savingType;
	}

	/**
	 * 设置属性：
	 *	充值类别
	 */
	public void setSavingType(String savingType){
		this.savingType = savingType;
	}

	/**
	 * 获取属性：
	 *	游戏服务器名称
	 */
	public String getServerName(){
		return serverName;
	}

	/**
	 * 设置属性：
	 *	游戏服务器名称
	 */
	public void setServerName(String serverName){
		this.serverName = serverName;
	}

	/**
	 * 获取属性：
	 *	渠道名称
	 */
	public String getUserChannel(){
		return userChannel;
	}

	/**
	 * 设置属性：
	 *	渠道名称
	 */
	public void setUserChannel(String userChannel){
		this.userChannel = userChannel;
	}

}