package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 捐款或者捐物<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>contributionType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>amout</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cellIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CONTRIBUTION_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte contributionType;
	long amout;
	int cellIndex;

	public CONTRIBUTION_REQ(){
	}

	public CONTRIBUTION_REQ(long seqNum,byte contributionType,long amout,int cellIndex){
		this.seqNum = seqNum;
		this.contributionType = contributionType;
		this.amout = amout;
		this.cellIndex = cellIndex;
	}

	public CONTRIBUTION_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		contributionType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		amout = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		cellIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000EF06;
	}

	public String getTypeDescription() {
		return "CONTRIBUTION_REQ";
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
		len += 8;
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

			buffer.put(contributionType);
			buffer.putLong(amout);
			buffer.putInt(cellIndex);
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
	 *	捐款的类型 0帮派基金
	 */
	public byte getContributionType(){
		return contributionType;
	}

	/**
	 * 设置属性：
	 *	捐款的类型 0帮派基金
	 */
	public void setContributionType(byte contributionType){
		this.contributionType = contributionType;
	}

	/**
	 * 获取属性：
	 *	捐款的金额
	 */
	public long getAmout(){
		return amout;
	}

	/**
	 * 设置属性：
	 *	捐款的金额
	 */
	public void setAmout(long amout){
		this.amout = amout;
	}

	/**
	 * 获取属性：
	 *	格子的位置
	 */
	public int getCellIndex(){
		return cellIndex;
	}

	/**
	 * 设置属性：
	 *	格子的位置
	 */
	public void setCellIndex(int cellIndex){
		this.cellIndex = cellIndex;
	}

}