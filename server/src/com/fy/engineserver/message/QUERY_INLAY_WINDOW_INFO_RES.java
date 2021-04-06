package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开坐骑装备镶嵌界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseEquId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayIndexs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayIndexs</td><td>int[]</td><td>inlayIndexs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayColors.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayColors</td><td>int[]</td><td>inlayColors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayIds</td><td>long[]</td><td>inlayIds.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_INLAY_WINDOW_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long horseEquId;
	int[] inlayIndexs;
	int[] inlayColors;
	long[] inlayIds;

	public QUERY_INLAY_WINDOW_INFO_RES(){
	}

	public QUERY_INLAY_WINDOW_INFO_RES(long seqNum,long horseEquId,int[] inlayIndexs,int[] inlayColors,long[] inlayIds){
		this.seqNum = seqNum;
		this.horseEquId = horseEquId;
		this.inlayIndexs = inlayIndexs;
		this.inlayColors = inlayColors;
		this.inlayIds = inlayIds;
	}

	public QUERY_INLAY_WINDOW_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		horseEquId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inlayIndexs = new int[len];
		for(int i = 0 ; i < inlayIndexs.length ; i++){
			inlayIndexs[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inlayColors = new int[len];
		for(int i = 0 ; i < inlayColors.length ; i++){
			inlayColors[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inlayIds = new long[len];
		for(int i = 0 ; i < inlayIds.length ; i++){
			inlayIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70FFF102;
	}

	public String getTypeDescription() {
		return "QUERY_INLAY_WINDOW_INFO_RES";
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
		len += inlayIndexs.length * 4;
		len += 4;
		len += inlayColors.length * 4;
		len += 4;
		len += inlayIds.length * 8;
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
			buffer.putInt(inlayIndexs.length);
			for(int i = 0 ; i < inlayIndexs.length; i++){
				buffer.putInt(inlayIndexs[i]);
			}
			buffer.putInt(inlayColors.length);
			for(int i = 0 ; i < inlayColors.length; i++){
				buffer.putInt(inlayColors[i]);
			}
			buffer.putInt(inlayIds.length);
			for(int i = 0 ; i < inlayIds.length; i++){
				buffer.putLong(inlayIds[i]);
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
	 *	孔索引
	 */
	public int[] getInlayIndexs(){
		return inlayIndexs;
	}

	/**
	 * 设置属性：
	 *	孔索引
	 */
	public void setInlayIndexs(int[] inlayIndexs){
		this.inlayIndexs = inlayIndexs;
	}

	/**
	 * 获取属性：
	 *	对应索引颜色
	 */
	public int[] getInlayColors(){
		return inlayColors;
	}

	/**
	 * 设置属性：
	 *	对应索引颜色
	 */
	public void setInlayColors(int[] inlayColors){
		this.inlayColors = inlayColors;
	}

	/**
	 * 获取属性：
	 *	对应索引镶嵌的神匣id
	 */
	public long[] getInlayIds(){
		return inlayIds;
	}

	/**
	 * 设置属性：
	 *	对应索引镶嵌的神匣id
	 */
	public void setInlayIds(long[] inlayIds){
		this.inlayIds = inlayIds;
	}

}