package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 腾讯好友列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ptype</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>page</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>numbers</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class GET_TYPEPLAYER_REQ2 implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte ptype;
	int page;
	int numbers;

	public GET_TYPEPLAYER_REQ2(){
	}

	public GET_TYPEPLAYER_REQ2(long seqNum,byte ptype,int page,int numbers){
		this.seqNum = seqNum;
		this.ptype = ptype;
		this.page = page;
		this.numbers = numbers;
	}

	public GET_TYPEPLAYER_REQ2(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		ptype = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		page = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		numbers = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000EC30;
	}

	public String getTypeDescription() {
		return "GET_TYPEPLAYER_REQ2";
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

			buffer.put(ptype);
			buffer.putInt(page);
			buffer.putInt(numbers);
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
	 *	类型, 0-好友,1-临时好友 2-黑名单, 3-仇人
	 */
	public byte getPtype(){
		return ptype;
	}

	/**
	 * 设置属性：
	 *	类型, 0-好友,1-临时好友 2-黑名单, 3-仇人
	 */
	public void setPtype(byte ptype){
		this.ptype = ptype;
	}

	/**
	 * 获取属性：
	 *	分页
	 */
	public int getPage(){
		return page;
	}

	/**
	 * 设置属性：
	 *	分页
	 */
	public void setPage(int page){
		this.page = page;
	}

	/**
	 * 获取属性：
	 *	每页显示几个
	 */
	public int getNumbers(){
		return numbers;
	}

	/**
	 * 设置属性：
	 *	每页显示几个
	 */
	public void setNumbers(int numbers){
		this.numbers = numbers;
	}

}