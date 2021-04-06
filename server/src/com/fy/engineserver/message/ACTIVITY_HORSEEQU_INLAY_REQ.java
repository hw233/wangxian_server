package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 激活或者重置坐骑装备孔<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseEquId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>opt</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costIds</td><td>long[]</td><td>costIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costNums</td><td>int[]</td><td>costNums.length</td><td>*</td></tr>
 * </table>
 */
public class ACTIVITY_HORSEEQU_INLAY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long horseEquId;
	int opt;
	int inlayIndex;
	long[] costIds;
	int[] costNums;

	public ACTIVITY_HORSEEQU_INLAY_REQ(){
	}

	public ACTIVITY_HORSEEQU_INLAY_REQ(long seqNum,long horseEquId,int opt,int inlayIndex,long[] costIds,int[] costNums){
		this.seqNum = seqNum;
		this.horseEquId = horseEquId;
		this.opt = opt;
		this.inlayIndex = inlayIndex;
		this.costIds = costIds;
		this.costNums = costNums;
	}

	public ACTIVITY_HORSEEQU_INLAY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		horseEquId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		opt = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		inlayIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		costIds = new long[len];
		for(int i = 0 ; i < costIds.length ; i++){
			costIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		costNums = new int[len];
		for(int i = 0 ; i < costNums.length ; i++){
			costNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x00FFF101;
	}

	public String getTypeDescription() {
		return "ACTIVITY_HORSEEQU_INLAY_REQ";
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
		len += 4;
		len += 4;
		len += 4;
		len += costIds.length * 8;
		len += 4;
		len += costNums.length * 4;
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

			buffer.putLong(horseEquId);
			buffer.putInt(opt);
			buffer.putInt(inlayIndex);
			buffer.putInt(costIds.length);
			for(int i = 0 ; i < costIds.length; i++){
				buffer.putLong(costIds[i]);
			}
			buffer.putInt(costNums.length);
			for(int i = 0 ; i < costNums.length; i++){
				buffer.putInt(costNums[i]);
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
	 *	坐骑装备id
	 */
	public long getHorseEquId(){
		return horseEquId;
	}

	/**
	 * 设置属性：
	 *	坐骑装备id
	 */
	public void setHorseEquId(long horseEquId){
		this.horseEquId = horseEquId;
	}

	/**
	 * 获取属性：
	 *	操作类型  1激活  2重置
	 */
	public int getOpt(){
		return opt;
	}

	/**
	 * 设置属性：
	 *	操作类型  1激活  2重置
	 */
	public void setOpt(int opt){
		this.opt = opt;
	}

	/**
	 * 获取属性：
	 *	孔索引
	 */
	public int getInlayIndex(){
		return inlayIndex;
	}

	/**
	 * 设置属性：
	 *	孔索引
	 */
	public void setInlayIndex(int inlayIndex){
		this.inlayIndex = inlayIndex;
	}

	/**
	 * 获取属性：
	 *	选择消耗的物品id数组
	 */
	public long[] getCostIds(){
		return costIds;
	}

	/**
	 * 设置属性：
	 *	选择消耗的物品id数组
	 */
	public void setCostIds(long[] costIds){
		this.costIds = costIds;
	}

	/**
	 * 获取属性：
	 *	选择消耗的物品数量数组
	 */
	public int[] getCostNums(){
		return costNums;
	}

	/**
	 * 设置属性：
	 *	选择消耗的物品数量数组
	 */
	public void setCostNums(int[] costNums){
		this.costNums = costNums;
	}

}