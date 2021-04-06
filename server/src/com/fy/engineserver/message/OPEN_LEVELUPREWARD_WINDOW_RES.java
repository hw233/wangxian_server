package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开冲级红利界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardIds</td><td>int[]</td><td>rewardIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardNames[0]</td><td>String</td><td>rewardNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardNames[1]</td><td>String</td><td>rewardNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardNames[2]</td><td>String</td><td>rewardNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descriptions.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descriptions[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descriptions[0]</td><td>String</td><td>descriptions[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descriptions[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descriptions[1]</td><td>String</td><td>descriptions[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descriptions[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descriptions[2]</td><td>String</td><td>descriptions[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lowLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lowLevels</td><td>int[]</td><td>lowLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>highLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>highLevels</td><td>int[]</td><td>highLevels.length</td><td>*</td></tr>
 * </table>
 */
public class OPEN_LEVELUPREWARD_WINDOW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] rewardIds;
	String[] rewardNames;
	String[] descriptions;
	int[] lowLevels;
	int[] highLevels;

	public OPEN_LEVELUPREWARD_WINDOW_RES(){
	}

	public OPEN_LEVELUPREWARD_WINDOW_RES(long seqNum,int[] rewardIds,String[] rewardNames,String[] descriptions,int[] lowLevels,int[] highLevels){
		this.seqNum = seqNum;
		this.rewardIds = rewardIds;
		this.rewardNames = rewardNames;
		this.descriptions = descriptions;
		this.lowLevels = lowLevels;
		this.highLevels = highLevels;
	}

	public OPEN_LEVELUPREWARD_WINDOW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardIds = new int[len];
		for(int i = 0 ; i < rewardIds.length ; i++){
			rewardIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardNames = new String[len];
		for(int i = 0 ; i < rewardNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			rewardNames[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		descriptions = new String[len];
		for(int i = 0 ; i < descriptions.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			descriptions[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lowLevels = new int[len];
		for(int i = 0 ; i < lowLevels.length ; i++){
			lowLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		highLevels = new int[len];
		for(int i = 0 ; i < highLevels.length ; i++){
			highLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0079;
	}

	public String getTypeDescription() {
		return "OPEN_LEVELUPREWARD_WINDOW_RES";
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
		len += rewardIds.length * 4;
		len += 4;
		for(int i = 0 ; i < rewardNames.length; i++){
			len += 2;
			len += rewardNames[i].getBytes().length;
		}
		len += 4;
		for(int i = 0 ; i < descriptions.length; i++){
			len += 2;
			len += descriptions[i].getBytes().length;
		}
		len += 4;
		len += lowLevels.length * 4;
		len += 4;
		len += highLevels.length * 4;
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

			buffer.putInt(rewardIds.length);
			for(int i = 0 ; i < rewardIds.length; i++){
				buffer.putInt(rewardIds[i]);
			}
			buffer.putInt(rewardNames.length);
			for(int i = 0 ; i < rewardNames.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = rewardNames[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(descriptions.length);
			for(int i = 0 ; i < descriptions.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = descriptions[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(lowLevels.length);
			for(int i = 0 ; i < lowLevels.length; i++){
				buffer.putInt(lowLevels[i]);
			}
			buffer.putInt(highLevels.length);
			for(int i = 0 ; i < highLevels.length; i++){
				buffer.putInt(highLevels[i]);
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
	 *	返利页签id
	 */
	public int[] getRewardIds(){
		return rewardIds;
	}

	/**
	 * 设置属性：
	 *	返利页签id
	 */
	public void setRewardIds(int[] rewardIds){
		this.rewardIds = rewardIds;
	}

	/**
	 * 获取属性：
	 *	返利页签名
	 */
	public String[] getRewardNames(){
		return rewardNames;
	}

	/**
	 * 设置属性：
	 *	返利页签名
	 */
	public void setRewardNames(String[] rewardNames){
		this.rewardNames = rewardNames;
	}

	/**
	 * 获取属性：
	 *	对应描述
	 */
	public String[] getDescriptions(){
		return descriptions;
	}

	/**
	 * 设置属性：
	 *	对应描述
	 */
	public void setDescriptions(String[] descriptions){
		this.descriptions = descriptions;
	}

	/**
	 * 获取属性：
	 *	购买最低等级
	 */
	public int[] getLowLevels(){
		return lowLevels;
	}

	/**
	 * 设置属性：
	 *	购买最低等级
	 */
	public void setLowLevels(int[] lowLevels){
		this.lowLevels = lowLevels;
	}

	/**
	 * 获取属性：
	 *	购买最高等级
	 */
	public int[] getHighLevels(){
		return highLevels;
	}

	/**
	 * 设置属性：
	 *	购买最高等级
	 */
	public void setHighLevels(int[] highLevels){
		this.highLevels = highLevels;
	}

}