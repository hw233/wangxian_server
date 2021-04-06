package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端弹出圆盘<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resultId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allIds</td><td>int[]</td><td>allIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allNames[0]</td><td>String</td><td>allNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allNames[1]</td><td>String</td><td>allNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allNames[2]</td><td>String</td><td>allNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>taskIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>runTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class ACTIVITY_FARMING_PLATE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int resultId;
	int[] allIds;
	String[] allNames;
	int taskIndex;
	long runTime;

	public ACTIVITY_FARMING_PLATE_REQ(){
	}

	public ACTIVITY_FARMING_PLATE_REQ(long seqNum,int resultId,int[] allIds,String[] allNames,int taskIndex,long runTime){
		this.seqNum = seqNum;
		this.resultId = resultId;
		this.allIds = allIds;
		this.allNames = allNames;
		this.taskIndex = taskIndex;
		this.runTime = runTime;
	}

	public ACTIVITY_FARMING_PLATE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		resultId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		allIds = new int[len];
		for(int i = 0 ; i < allIds.length ; i++){
			allIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		allNames = new String[len];
		for(int i = 0 ; i < allNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			allNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		taskIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		runTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x0F300014;
	}

	public String getTypeDescription() {
		return "ACTIVITY_FARMING_PLATE_REQ";
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
		len += allIds.length * 4;
		len += 4;
		for(int i = 0 ; i < allNames.length; i++){
			len += 2;
			try{
				len += allNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 8;
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

			buffer.putInt(resultId);
			buffer.putInt(allIds.length);
			for(int i = 0 ; i < allIds.length; i++){
				buffer.putInt(allIds[i]);
			}
			buffer.putInt(allNames.length);
			for(int i = 0 ; i < allNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = allNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(taskIndex);
			buffer.putLong(runTime);
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
	 *	转盘的结果Id
	 */
	public int getResultId(){
		return resultId;
	}

	/**
	 * 设置属性：
	 *	转盘的结果Id
	 */
	public void setResultId(int resultId){
		this.resultId = resultId;
	}

	/**
	 * 获取属性：
	 *	盘子内所有物品ID
	 */
	public int[] getAllIds(){
		return allIds;
	}

	/**
	 * 设置属性：
	 *	盘子内所有物品ID
	 */
	public void setAllIds(int[] allIds){
		this.allIds = allIds;
	}

	/**
	 * 获取属性：
	 *	盘子内所有物品名称
	 */
	public String[] getAllNames(){
		return allNames;
	}

	/**
	 * 设置属性：
	 *	盘子内所有物品名称
	 */
	public void setAllNames(String[] allNames){
		this.allNames = allNames;
	}

	/**
	 * 获取属性：
	 *	任务索引-要接的下一个任务
	 */
	public int getTaskIndex(){
		return taskIndex;
	}

	/**
	 * 设置属性：
	 *	任务索引-要接的下一个任务
	 */
	public void setTaskIndex(int taskIndex){
		this.taskIndex = taskIndex;
	}

	/**
	 * 获取属性：
	 *	盘子转的时间
	 */
	public long getRunTime(){
		return runTime;
	}

	/**
	 * 设置属性：
	 *	盘子转的时间
	 */
	public void setRunTime(long runTime){
		this.runTime = runTime;
	}

}