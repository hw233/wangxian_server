package com.fy.boss.cmd.message;

import com.fy.boss.cmd.message.CMDMessageFactory;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器状态<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>installed</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>running</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class SERVER_STATUS_RES implements ResponseMessage{

	static CMDMessageFactory mf = CMDMessageFactory.getInstance();

	long seqNum;
	boolean installed;
	boolean running;

	public SERVER_STATUS_RES(long seqNum,boolean installed,boolean running){
		this.seqNum = seqNum;
		this.installed = installed;
		this.running = running;
	}

	public SERVER_STATUS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		installed = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		running = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x8000A002;
	}

	public String getTypeDescription() {
		return "SERVER_STATUS_RES";
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

			buffer.put((byte)(installed==false?0:1));
			buffer.put((byte)(running==false?0:1));
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	是否已部署
	 */
	public boolean getInstalled(){
		return installed;
	}

	/**
	 * 设置属性：
	 *	是否已部署
	 */
	public void setInstalled(boolean installed){
		this.installed = installed;
	}

	/**
	 * 获取属性：
	 *	是否已启动
	 */
	public boolean getRunning(){
		return running;
	}

	/**
	 * 设置属性：
	 *	是否已启动
	 */
	public void setRunning(boolean running){
		this.running = running;
	}

}
