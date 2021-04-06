package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 我的仙兽房<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>openCell.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>openCell</td><td>int[]</td><td>openCell.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>storeTimes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>storeTimes</td><td>long[]</td><td>storeTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>blessCounts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>blessCounts</td><td>int[]</td><td>blessCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>addExps.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>addExps</td><td>long[]</td><td>addExps.length</td><td>*</td></tr>
 * </table>
 */
public class PLAYER_PET_HOUSE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] openCell;
	long[] ids;
	long[] storeTimes;
	int[] blessCounts;
	long[] addExps;

	public PLAYER_PET_HOUSE_RES(){
	}

	public PLAYER_PET_HOUSE_RES(long seqNum,int[] openCell,long[] ids,long[] storeTimes,int[] blessCounts,long[] addExps){
		this.seqNum = seqNum;
		this.openCell = openCell;
		this.ids = ids;
		this.storeTimes = storeTimes;
		this.blessCounts = blessCounts;
		this.addExps = addExps;
	}

	public PLAYER_PET_HOUSE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		openCell = new int[len];
		for(int i = 0 ; i < openCell.length ; i++){
			openCell[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids = new long[len];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		storeTimes = new long[len];
		for(int i = 0 ; i < storeTimes.length ; i++){
			storeTimes[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		blessCounts = new int[len];
		for(int i = 0 ; i < blessCounts.length ; i++){
			blessCounts[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		addExps = new long[len];
		for(int i = 0 ; i < addExps.length ; i++){
			addExps[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70FFF131;
	}

	public String getTypeDescription() {
		return "PLAYER_PET_HOUSE_RES";
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
		len += openCell.length * 4;
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += storeTimes.length * 8;
		len += 4;
		len += blessCounts.length * 4;
		len += 4;
		len += addExps.length * 8;
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

			buffer.putInt(openCell.length);
			for(int i = 0 ; i < openCell.length; i++){
				buffer.putInt(openCell[i]);
			}
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(storeTimes.length);
			for(int i = 0 ; i < storeTimes.length; i++){
				buffer.putLong(storeTimes[i]);
			}
			buffer.putInt(blessCounts.length);
			for(int i = 0 ; i < blessCounts.length; i++){
				buffer.putInt(blessCounts[i]);
			}
			buffer.putInt(addExps.length);
			for(int i = 0 ; i < addExps.length; i++){
				buffer.putLong(addExps[i]);
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
	 *	是否开启,0：默认没开启，1：开启，开启也可以为空
	 */
	public int[] getOpenCell(){
		return openCell;
	}

	/**
	 * 设置属性：
	 *	是否开启,0：默认没开启，1：开启，开启也可以为空
	 */
	public void setOpenCell(int[] openCell){
		this.openCell = openCell;
	}

	/**
	 * 获取属性：
	 *	挂机中的宠物id集
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	挂机中的宠物id集
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	挂机时长，单位毫秒
	 */
	public long[] getStoreTimes(){
		return storeTimes;
	}

	/**
	 * 设置属性：
	 *	挂机时长，单位毫秒
	 */
	public void setStoreTimes(long[] storeTimes){
		this.storeTimes = storeTimes;
	}

	/**
	 * 获取属性：
	 *	祝福次数
	 */
	public int[] getBlessCounts(){
		return blessCounts;
	}

	/**
	 * 设置属性：
	 *	祝福次数
	 */
	public void setBlessCounts(int[] blessCounts){
		this.blessCounts = blessCounts;
	}

	/**
	 * 获取属性：
	 *	获得经验
	 */
	public long[] getAddExps(){
		return addExps;
	}

	/**
	 * 设置属性：
	 *	获得经验
	 */
	public void setAddExps(long[] addExps){
		this.addExps = addExps;
	}

}