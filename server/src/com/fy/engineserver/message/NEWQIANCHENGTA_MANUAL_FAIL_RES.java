package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 手动爬塔失败<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nanduType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>daoIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>daoName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>daoName</td><td>String</td><td>daoName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cengIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shibaiMsg.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shibaiMsg</td><td>String</td><td>shibaiMsg.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class NEWQIANCHENGTA_MANUAL_FAIL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int nanduType;
	int daoIndex;
	String daoName;
	int cengIndex;
	String shibaiMsg;

	public NEWQIANCHENGTA_MANUAL_FAIL_RES(){
	}

	public NEWQIANCHENGTA_MANUAL_FAIL_RES(long seqNum,int nanduType,int daoIndex,String daoName,int cengIndex,String shibaiMsg){
		this.seqNum = seqNum;
		this.nanduType = nanduType;
		this.daoIndex = daoIndex;
		this.daoName = daoName;
		this.cengIndex = cengIndex;
		this.shibaiMsg = shibaiMsg;
	}

	public NEWQIANCHENGTA_MANUAL_FAIL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		nanduType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		daoIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		daoName = new String(content,offset,len,"UTF-8");
		offset += len;
		cengIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		shibaiMsg = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8F710013;
	}

	public String getTypeDescription() {
		return "NEWQIANCHENGTA_MANUAL_FAIL_RES";
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
		len += 4;
		len += 2;
		try{
			len +=daoName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 2;
		try{
			len +=shibaiMsg.getBytes("UTF-8").length;
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

			buffer.putInt(nanduType);
			buffer.putInt(daoIndex);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = daoName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(cengIndex);
				try{
			tmpBytes1 = shibaiMsg.getBytes("UTF-8");
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
	 *	难度Type 0是普通 1是困难 2是深渊
	 */
	public int getNanduType(){
		return nanduType;
	}

	/**
	 * 设置属性：
	 *	难度Type 0是普通 1是困难 2是深渊
	 */
	public void setNanduType(int nanduType){
		this.nanduType = nanduType;
	}

	/**
	 * 获取属性：
	 *	道的下标，从0开始
	 */
	public int getDaoIndex(){
		return daoIndex;
	}

	/**
	 * 设置属性：
	 *	道的下标，从0开始
	 */
	public void setDaoIndex(int daoIndex){
		this.daoIndex = daoIndex;
	}

	/**
	 * 获取属性：
	 *	道的名字
	 */
	public String getDaoName(){
		return daoName;
	}

	/**
	 * 设置属性：
	 *	道的名字
	 */
	public void setDaoName(String daoName){
		this.daoName = daoName;
	}

	/**
	 * 获取属性：
	 *	层数 0 开始
	 */
	public int getCengIndex(){
		return cengIndex;
	}

	/**
	 * 设置属性：
	 *	层数 0 开始
	 */
	public void setCengIndex(int cengIndex){
		this.cengIndex = cengIndex;
	}

	/**
	 * 获取属性：
	 *	失败说明
	 */
	public String getShibaiMsg(){
		return shibaiMsg;
	}

	/**
	 * 设置属性：
	 *	失败说明
	 */
	public void setShibaiMsg(String shibaiMsg){
		this.shibaiMsg = shibaiMsg;
	}

}