package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 随身获取邮件列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mailBaseType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pageIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pageNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class MAIL_LIST_CARRY_NEW_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int mailBaseType;
	int pageIndex;
	int pageNum;

	public MAIL_LIST_CARRY_NEW_REQ(){
	}

	public MAIL_LIST_CARRY_NEW_REQ(long seqNum,int mailBaseType,int pageIndex,int pageNum){
		this.seqNum = seqNum;
		this.mailBaseType = mailBaseType;
		this.pageIndex = pageIndex;
		this.pageNum = pageNum;
	}

	public MAIL_LIST_CARRY_NEW_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		mailBaseType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		pageIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		pageNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000C022;
	}

	public String getTypeDescription() {
		return "MAIL_LIST_CARRY_NEW_REQ";
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

			buffer.putInt(mailBaseType);
			buffer.putInt(pageIndex);
			buffer.putInt(pageNum);
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
	 *	邮件类型，0是系统邮件，1是玩家邮件
	 */
	public int getMailBaseType(){
		return mailBaseType;
	}

	/**
	 * 设置属性：
	 *	邮件类型，0是系统邮件，1是玩家邮件
	 */
	public void setMailBaseType(int mailBaseType){
		this.mailBaseType = mailBaseType;
	}

	/**
	 * 获取属性：
	 *	第几页
	 */
	public int getPageIndex(){
		return pageIndex;
	}

	/**
	 * 设置属性：
	 *	第几页
	 */
	public void setPageIndex(int pageIndex){
		this.pageIndex = pageIndex;
	}

	/**
	 * 获取属性：
	 *	每页显示的数量
	 */
	public int getPageNum(){
		return pageNum;
	}

	/**
	 * 设置属性：
	 *	每页显示的数量
	 */
	public void setPageNum(int pageNum){
		this.pageNum = pageNum;
	}

}