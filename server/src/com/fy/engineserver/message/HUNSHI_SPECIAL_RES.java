package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 魂石特有信息查询<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialId</td><td>long[]</td><td>materialId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jianDing.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jianDing</td><td>boolean[]</td><td>jianDing.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ronghezhi.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ronghezhi</td><td>int[]</td><td>ronghezhi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>index.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>index</td><td>int[]</td><td>index.length</td><td>*</td></tr>
 * </table>
 */
public class HUNSHI_SPECIAL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] materialId;
	boolean[] jianDing;
	int[] ronghezhi;
	int[] index;

	public HUNSHI_SPECIAL_RES(){
	}

	public HUNSHI_SPECIAL_RES(long seqNum,long[] materialId,boolean[] jianDing,int[] ronghezhi,int[] index){
		this.seqNum = seqNum;
		this.materialId = materialId;
		this.jianDing = jianDing;
		this.ronghezhi = ronghezhi;
		this.index = index;
	}

	public HUNSHI_SPECIAL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		materialId = new long[len];
		for(int i = 0 ; i < materialId.length ; i++){
			materialId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jianDing = new boolean[len];
		for(int i = 0 ; i < jianDing.length ; i++){
			jianDing[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ronghezhi = new int[len];
		for(int i = 0 ; i < ronghezhi.length ; i++){
			ronghezhi[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		index = new int[len];
		for(int i = 0 ; i < index.length ; i++){
			index[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF018;
	}

	public String getTypeDescription() {
		return "HUNSHI_SPECIAL_RES";
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
		len += materialId.length * 8;
		len += 4;
		len += jianDing.length;
		len += 4;
		len += ronghezhi.length * 4;
		len += 4;
		len += index.length * 4;
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

			buffer.putInt(materialId.length);
			for(int i = 0 ; i < materialId.length; i++){
				buffer.putLong(materialId[i]);
			}
			buffer.putInt(jianDing.length);
			for(int i = 0 ; i < jianDing.length; i++){
				buffer.put((byte)(jianDing[i]==false?0:1));
			}
			buffer.putInt(ronghezhi.length);
			for(int i = 0 ; i < ronghezhi.length; i++){
				buffer.putInt(ronghezhi[i]);
			}
			buffer.putInt(index.length);
			for(int i = 0 ; i < index.length; i++){
				buffer.putInt(index[i]);
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
	 *	材料ID
	 */
	public long[] getMaterialId(){
		return materialId;
	}

	/**
	 * 设置属性：
	 *	材料ID
	 */
	public void setMaterialId(long[] materialId){
		this.materialId = materialId;
	}

	/**
	 * 获取属性：
	 *	是否已鉴定
	 */
	public boolean[] getJianDing(){
		return jianDing;
	}

	/**
	 * 设置属性：
	 *	是否已鉴定
	 */
	public void setJianDing(boolean[] jianDing){
		this.jianDing = jianDing;
	}

	/**
	 * 获取属性：
	 *	总融合值
	 */
	public int[] getRonghezhi(){
		return ronghezhi;
	}

	/**
	 * 设置属性：
	 *	总融合值
	 */
	public void setRonghezhi(int[] ronghezhi){
		this.ronghezhi = ronghezhi;
	}

	/**
	 * 获取属性：
	 *	窍的位置：-1-不带窍，0~15对应1~16个窍
	 */
	public int[] getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	窍的位置：-1-不带窍，0~15对应1~16个窍
	 */
	public void setIndex(int[] index){
		this.index = index;
	}

}