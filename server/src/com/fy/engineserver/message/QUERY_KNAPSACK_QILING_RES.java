package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>qilingCellmax</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>qilingEntityId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>qilingEntityId</td><td>long[]</td><td>qilingEntityId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>qilingCounts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>qilingCounts</td><td>short[]</td><td>qilingCounts.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_KNAPSACK_QILING_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	short qilingCellmax;
	long[] qilingEntityId;
	short[] qilingCounts;

	public QUERY_KNAPSACK_QILING_RES(){
	}

	public QUERY_KNAPSACK_QILING_RES(long seqNum,short qilingCellmax,long[] qilingEntityId,short[] qilingCounts){
		this.seqNum = seqNum;
		this.qilingCellmax = qilingCellmax;
		this.qilingEntityId = qilingEntityId;
		this.qilingCounts = qilingCounts;
	}

	public QUERY_KNAPSACK_QILING_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		qilingCellmax = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		qilingEntityId = new long[len];
		for(int i = 0 ; i < qilingEntityId.length ; i++){
			qilingEntityId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		qilingCounts = new short[len];
		for(int i = 0 ; i < qilingCounts.length ; i++){
			qilingCounts[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
	}

	public int getType() {
		return 0x70EEAABD;
	}

	public String getTypeDescription() {
		return "QUERY_KNAPSACK_QILING_RES";
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
		len += 4;
		len += qilingEntityId.length * 8;
		len += 4;
		len += qilingCounts.length * 2;
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

			buffer.putShort(qilingCellmax);
			buffer.putInt(qilingEntityId.length);
			for(int i = 0 ; i < qilingEntityId.length; i++){
				buffer.putLong(qilingEntityId[i]);
			}
			buffer.putInt(qilingCounts.length);
			for(int i = 0 ; i < qilingCounts.length; i++){
				buffer.putShort(qilingCounts[i]);
			}
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
	 *	背包最大扩充个数
	 */
	public short getQilingCellmax(){
		return qilingCellmax;
	}

	/**
	 * 设置属性：
	 *	背包最大扩充个数
	 */
	public void setQilingCellmax(short qilingCellmax){
		this.qilingCellmax = qilingCellmax;
	}

	/**
	 * 获取属性：
	 *	背包中各个单元格放置的物体唯一标识，长度就是背包格个数，内容-1标识空格子
	 */
	public long[] getQilingEntityId(){
		return qilingEntityId;
	}

	/**
	 * 设置属性：
	 *	背包中各个单元格放置的物体唯一标识，长度就是背包格个数，内容-1标识空格子
	 */
	public void setQilingEntityId(long[] qilingEntityId){
		this.qilingEntityId = qilingEntityId;
	}

	/**
	 * 获取属性：
	 *	背包中各个单元格放置的物体个数，0标识没有物品
	 */
	public short[] getQilingCounts(){
		return qilingCounts;
	}

	/**
	 * 设置属性：
	 *	背包中各个单元格放置的物体个数，0标识没有物品
	 */
	public void setQilingCounts(short[] qilingCounts){
		this.qilingCounts = qilingCounts;
	}

}