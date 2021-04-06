package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 是否显示活动<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>boolean[]</td><td>result.length</td><td>*</td></tr>
 * </table>
 */
public class ACTIVITY_IS_SHOW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int activityType;
	boolean[] result;

	public ACTIVITY_IS_SHOW_RES(){
	}

	public ACTIVITY_IS_SHOW_RES(long seqNum,int activityType,boolean[] result){
		this.seqNum = seqNum;
		this.activityType = activityType;
		this.result = result;
	}

	public ACTIVITY_IS_SHOW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		activityType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		result = new boolean[len];
		for(int i = 0 ; i < result.length ; i++){
			result[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
	}

	public int getType() {
		return 0x70FFF138;
	}

	public String getTypeDescription() {
		return "ACTIVITY_IS_SHOW_RES";
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
		len += result.length;
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

			buffer.putInt(activityType);
			buffer.putInt(result.length);
			for(int i = 0 ; i < result.length; i++){
				buffer.put((byte)(result[i]==false?0:1));
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
	 *	活动类型
	 */
	public int getActivityType(){
		return activityType;
	}

	/**
	 * 设置属性：
	 *	活动类型
	 */
	public void setActivityType(int activityType){
		this.activityType = activityType;
	}

	/**
	 * 获取属性：
	 *	结果
	 */
	public boolean[] getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果
	 */
	public void setResult(boolean[] result){
		this.result = result;
	}

}