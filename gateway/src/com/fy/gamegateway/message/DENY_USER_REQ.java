package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 封帐号<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>clientId</td><td>String</td><td>clientId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>reason.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>reason</td><td>String</td><td>reason.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gm.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gm</td><td>String</td><td>gm.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>denyClientId</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enableDeny</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>foroverDeny</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>days</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hours</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class DENY_USER_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String clientId;
	String username;
	String reason;
	String gm;
	boolean denyClientId;
	boolean enableDeny;
	boolean foroverDeny;
	int days;
	int hours;

	public DENY_USER_REQ(){
	}

	public DENY_USER_REQ(long seqNum,String clientId,String username,String reason,String gm,boolean denyClientId,boolean enableDeny,boolean foroverDeny,int days,int hours){
		this.seqNum = seqNum;
		this.clientId = clientId;
		this.username = username;
		this.reason = reason;
		this.gm = gm;
		this.denyClientId = denyClientId;
		this.enableDeny = enableDeny;
		this.foroverDeny = foroverDeny;
		this.days = days;
		this.hours = hours;
	}

	public DENY_USER_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		clientId = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		username = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		reason = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gm = new String(content,offset,len,"UTF-8");
		offset += len;
		denyClientId = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		enableDeny = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		foroverDeny = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		days = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		hours = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x002EE005;
	}

	public String getTypeDescription() {
		return "DENY_USER_REQ";
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
			len +=clientId.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=username.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=reason.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=gm.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 1;
		len += 1;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = clientId.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = username.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = reason.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = gm.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(denyClientId==false?0:1));
			buffer.put((byte)(enableDeny==false?0:1));
			buffer.put((byte)(foroverDeny==false?0:1));
			buffer.putInt(days);
			buffer.putInt(hours);
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
	 *	clientid
	 */
	public String getClientId(){
		return clientId;
	}

	/**
	 * 设置属性：
	 *	clientid
	 */
	public void setClientId(String clientId){
		this.clientId = clientId;
	}

	/**
	 * 获取属性：
	 *	帐号名
	 */
	public String getUsername(){
		return username;
	}

	/**
	 * 设置属性：
	 *	帐号名
	 */
	public void setUsername(String username){
		this.username = username;
	}

	/**
	 * 获取属性：
	 *	原因
	 */
	public String getReason(){
		return reason;
	}

	/**
	 * 设置属性：
	 *	原因
	 */
	public void setReason(String reason){
		this.reason = reason;
	}

	/**
	 * 获取属性：
	 *	gm
	 */
	public String getGm(){
		return gm;
	}

	/**
	 * 设置属性：
	 *	gm
	 */
	public void setGm(String gm){
		this.gm = gm;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean getDenyClientId(){
		return denyClientId;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDenyClientId(boolean denyClientId){
		this.denyClientId = denyClientId;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean getEnableDeny(){
		return enableDeny;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setEnableDeny(boolean enableDeny){
		this.enableDeny = enableDeny;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean getForoverDeny(){
		return foroverDeny;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setForoverDeny(boolean foroverDeny){
		this.foroverDeny = foroverDeny;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getDays(){
		return days;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDays(int days){
		this.days = days;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getHours(){
		return hours;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setHours(int hours){
		this.hours = hours;
	}

}