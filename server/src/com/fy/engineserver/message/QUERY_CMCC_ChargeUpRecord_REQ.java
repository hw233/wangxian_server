package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询所有充值(消费)记录 游戏业务平台<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>userId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>userId</td><td>String</td><td>userId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>key.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>key</td><td>String</td><td>key.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pageindex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pagenum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>timeType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>month.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>month</td><td>String</td><td>month.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>consumeType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_CMCC_ChargeUpRecord_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String userId;
	String key;
	String username;
	int pageindex;
	int pagenum;
	byte timeType;
	String month;
	byte consumeType;

	public QUERY_CMCC_ChargeUpRecord_REQ(long seqNum,String userId,String key,String username,int pageindex,int pagenum,byte timeType,String month,byte consumeType){
		this.seqNum = seqNum;
		this.userId = userId;
		this.key = key;
		this.username = username;
		this.pageindex = pageindex;
		this.pagenum = pagenum;
		this.timeType = timeType;
		this.month = month;
		this.consumeType = consumeType;
	}

	public QUERY_CMCC_ChargeUpRecord_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		userId = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		key = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		username = new String(content,offset,len,"UTF-8");
		offset += len;
		pageindex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		pagenum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		timeType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		month = new String(content,offset,len,"UTF-8");
		offset += len;
		consumeType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x0000AF09;
	}

	public String getTypeDescription() {
		return "QUERY_CMCC_ChargeUpRecord_REQ";
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
			len +=userId.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=key.getBytes("UTF-8").length;
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
		len += 4;
		len += 4;
		len += 1;
		len += 2;
		try{
			len +=month.getBytes("UTF-8").length;
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
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = userId.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = key.getBytes("UTF-8");
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
			buffer.putInt(pageindex);
			buffer.putInt(pagenum);
			buffer.put(timeType);
				try{
			tmpBytes1 = month.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(consumeType);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	用户伪码
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * 设置属性：
	 *	用户伪码
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String getKey(){
		return key;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setKey(String key){
		this.key = key;
	}

	/**
	 * 获取属性：
	 *	账号，可以空
	 */
	public String getUsername(){
		return username;
	}

	/**
	 * 设置属性：
	 *	账号，可以空
	 */
	public void setUsername(String username){
		this.username = username;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getPageindex(){
		return pageindex;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPageindex(int pageindex){
		this.pageindex = pageindex;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getPagenum(){
		return pagenum;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPagenum(int pagenum){
		this.pagenum = pagenum;
	}

	/**
	 * 获取属性：
	 *	1-当天 2-指定月 3-最近10天
	 */
	public byte getTimeType(){
		return timeType;
	}

	/**
	 * 设置属性：
	 *	1-当天 2-指定月 3-最近10天
	 */
	public void setTimeType(byte timeType){
		this.timeType = timeType;
	}

	/**
	 * 获取属性：
	 *	yyyymm
	 */
	public String getMonth(){
		return month;
	}

	/**
	 * 设置属性：
	 *	yyyymm
	 */
	public void setMonth(String month){
		this.month = month;
	}

	/**
	 * 获取属性：
	 *	7-所有充值记录,13-潜龙Online的道具消费，3-所有客户端网游道具消费，5-所有WAP网游道具消费
	 */
	public byte getConsumeType(){
		return consumeType;
	}

	/**
	 * 设置属性：
	 *	7-所有充值记录,13-潜龙Online的道具消费，3-所有客户端网游道具消费，5-所有WAP网游道具消费
	 */
	public void setConsumeType(byte consumeType){
		this.consumeType = consumeType;
	}

}
