package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 累计在线<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>onlineTimes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>onlineTimes</td><td>long[]</td><td>onlineTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>stste.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>stste</td><td>int[]</td><td>stste.length</td><td>*</td></tr>
 * </table>
 */
public class TOTLE_ONLINE_REWARD_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] ids;
	long[] onlineTimes;
	int[] stste;

	public TOTLE_ONLINE_REWARD_RES(){
	}

	public TOTLE_ONLINE_REWARD_RES(long seqNum,long[] ids,long[] onlineTimes,int[] stste){
		this.seqNum = seqNum;
		this.ids = ids;
		this.onlineTimes = onlineTimes;
		this.stste = stste;
	}

	public TOTLE_ONLINE_REWARD_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
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
		onlineTimes = new long[len];
		for(int i = 0 ; i < onlineTimes.length ; i++){
			onlineTimes[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		stste = new int[len];
		for(int i = 0 ; i < stste.length ; i++){
			stste[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF129;
	}

	public String getTypeDescription() {
		return "TOTLE_ONLINE_REWARD_RES";
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
		len += ids.length * 8;
		len += 4;
		len += onlineTimes.length * 8;
		len += 4;
		len += stste.length * 4;
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

			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(onlineTimes.length);
			for(int i = 0 ; i < onlineTimes.length; i++){
				buffer.putLong(onlineTimes[i]);
			}
			buffer.putInt(stste.length);
			for(int i = 0 ; i < stste.length; i++){
				buffer.putInt(stste[i]);
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
	 *	奖励id集
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	奖励id集
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	如果领取状态是0,显示剩余时间，单位毫秒
	 */
	public long[] getOnlineTimes(){
		return onlineTimes;
	}

	/**
	 * 设置属性：
	 *	如果领取状态是0,显示剩余时间，单位毫秒
	 */
	public void setOnlineTimes(long[] onlineTimes){
		this.onlineTimes = onlineTimes;
	}

	/**
	 * 获取属性：
	 *	0：默认，1：可领取,2：已经领取过
	 */
	public int[] getStste(){
		return stste;
	}

	/**
	 * 设置属性：
	 *	0：默认，1：可领取,2：已经领取过
	 */
	public void setStste(int[] stste){
		this.stste = stste;
	}

}