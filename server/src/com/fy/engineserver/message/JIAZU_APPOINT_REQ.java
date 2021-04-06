package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 任命家族<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>appointPlayerID</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazupassword.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazupassword</td><td>String</td><td>jiazupassword.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titleID</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class JIAZU_APPOINT_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long appointPlayerID;
	String jiazupassword;
	int titleID;

	public JIAZU_APPOINT_REQ(){
	}

	public JIAZU_APPOINT_REQ(long seqNum,long appointPlayerID,String jiazupassword,int titleID){
		this.seqNum = seqNum;
		this.appointPlayerID = appointPlayerID;
		this.jiazupassword = jiazupassword;
		this.titleID = titleID;
	}

	public JIAZU_APPOINT_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		appointPlayerID = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		jiazupassword = new String(content,offset,len,"UTF-8");
		offset += len;
		titleID = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x000AEE14;
	}

	public String getTypeDescription() {
		return "JIAZU_APPOINT_REQ";
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
			len +=jiazupassword.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putLong(appointPlayerID);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = jiazupassword.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(titleID);
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
	 *	被授予职位的playerID
	 */
	public long getAppointPlayerID(){
		return appointPlayerID;
	}

	/**
	 * 设置属性：
	 *	被授予职位的playerID
	 */
	public void setAppointPlayerID(long appointPlayerID){
		this.appointPlayerID = appointPlayerID;
	}

	/**
	 * 获取属性：
	 *	家族密码
	 */
	public String getJiazupassword(){
		return jiazupassword;
	}

	/**
	 * 设置属性：
	 *	家族密码
	 */
	public void setJiazupassword(String jiazupassword){
		this.jiazupassword = jiazupassword;
	}

	/**
	 * 获取属性：
	 *	职位ID，0-族长 1－副族长 2－执法长老 3－龙血长老 4－执事长老 5－精英 6－平民
	 */
	public int getTitleID(){
		return titleID;
	}

	/**
	 * 设置属性：
	 *	职位ID，0-族长 1－副族长 2－执法长老 3－龙血长老 4－执事长老 5－精英 6－平民
	 */
	public void setTitleID(int titleID){
		this.titleID = titleID;
	}

}