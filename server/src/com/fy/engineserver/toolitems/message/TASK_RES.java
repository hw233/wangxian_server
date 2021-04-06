package com.fy.engineserver.toolitems.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求TASK数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskdependencys.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>taskdependencys[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskdependencys[0]</td><td>String</td><td>taskdependencys[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>taskdependencys[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskdependencys[1]</td><td>String</td><td>taskdependencys[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>taskdependencys[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskdependencys[2]</td><td>String</td><td>taskdependencys[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskdependencysnamesids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>taskdependencysnamesids[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskdependencysnamesids[0]</td><td>String</td><td>taskdependencysnamesids[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>taskdependencysnamesids[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskdependencysnamesids[1]</td><td>String</td><td>taskdependencysnamesids[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>taskdependencysnamesids[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskdependencysnamesids[2]</td><td>String</td><td>taskdependencysnamesids[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class TASK_RES implements ResponseMessage{

	static ItemEditorMessageFactory mf = ItemEditorMessageFactory.getInstance();

	long seqNum;
	String[] taskdependencys;
	String[] taskdependencysnamesids;

	public TASK_RES(long seqNum,String[] taskdependencys,String[] taskdependencysnamesids){
		this.seqNum = seqNum;
		this.taskdependencys = taskdependencys;
		this.taskdependencysnamesids = taskdependencysnamesids;
	}

	public TASK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		taskdependencys = new String[len];
		for(int i = 0 ; i < taskdependencys.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 10240) throw new Exception("string length ["+len+"] big than the max length [10240]");
			taskdependencys[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		taskdependencysnamesids = new String[len];
		for(int i = 0 ; i < taskdependencysnamesids.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 10240) throw new Exception("string length ["+len+"] big than the max length [10240]");
			taskdependencysnamesids[i] = new String(content,offset,len);
		offset += len;
		}
	}

	public int getType() {
		return 0x80000002;
	}

	public String getTypeDescription() {
		return "TASK_RES";
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
		for(int i = 0 ; i < taskdependencys.length; i++){
			len += 2;
			len += taskdependencys[i].getBytes().length;
		}
		len += 4;
		for(int i = 0 ; i < taskdependencysnamesids.length; i++){
			len += 2;
			len += taskdependencysnamesids[i].getBytes().length;
		}
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putInt(taskdependencys.length);
			for(int i = 0 ; i < taskdependencys.length; i++){
				byte[] tmpBytes2 = taskdependencys[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(taskdependencysnamesids.length);
			for(int i = 0 ; i < taskdependencysnamesids.length; i++){
				byte[] tmpBytes2 = taskdependencysnamesids[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :" + e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	任务归属地名字
	 */
	public String[] getTaskdependencys(){
		return taskdependencys;
	}

	/**
	 * 设置属性：
	 *	任务归属地名字
	 */
	public void setTaskdependencys(String[] taskdependencys){
		this.taskdependencys = taskdependencys;
	}

	/**
	 * 获取属性：
	 *	任务归属地名字以及任务名以及任务ID
	 */
	public String[] getTaskdependencysnamesids(){
		return taskdependencysnamesids;
	}

	/**
	 * 设置属性：
	 *	任务归属地名字以及任务名以及任务ID
	 */
	public void setTaskdependencysnamesids(String[] taskdependencysnamesids){
		this.taskdependencysnamesids = taskdependencysnamesids;
	}

}
