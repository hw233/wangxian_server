package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.newtask.TaskEntity;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器将玩家正在做的任务发送给玩家<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities.length</td><td>int</td><td>4个字节</td><td>TaskEntity数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[0].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[0].status</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[0].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[0].taskName</td><td>String</td><td>entities[0].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[0].completed.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[0].completed</td><td>int[]</td><td>entities[0].completed.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[0].taskDemand.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[0].taskDemand</td><td>int[]</td><td>entities[0].taskDemand.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[0].showType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[0].failTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[1].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[1].status</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[1].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[1].taskName</td><td>String</td><td>entities[1].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[1].completed.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[1].completed</td><td>int[]</td><td>entities[1].completed.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[1].taskDemand.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[1].taskDemand</td><td>int[]</td><td>entities[1].taskDemand.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[1].showType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[1].failTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[2].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[2].status</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[2].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[2].taskName</td><td>String</td><td>entities[2].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[2].completed.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[2].completed</td><td>int[]</td><td>entities[2].completed.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[2].taskDemand.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[2].taskDemand</td><td>int[]</td><td>entities[2].taskDemand.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entities[2].showType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entities[2].failTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>taskLevels</td><td>int[]</td><td>taskLevels.length</td><td>*</td></tr>
 * </table>
 */
public class TASK_QUERY_ENTITIES_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	TaskEntity[] entities;
	int[] taskLevels;

	public TASK_QUERY_ENTITIES_RES(){
	}

	public TASK_QUERY_ENTITIES_RES(long seqNum,TaskEntity[] entities,int[] taskLevels){
		this.seqNum = seqNum;
		this.entities = entities;
		this.taskLevels = taskLevels;
	}

	public TASK_QUERY_ENTITIES_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		entities = new TaskEntity[len];
		for(int i = 0 ; i < entities.length ; i++){
			entities[i] = new TaskEntity();
			entities[i].setTaskId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			entities[i].setStatus((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			entities[i].setTaskName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] completed_0001 = new int[len];
			for(int j = 0 ; j < completed_0001.length ; j++){
				completed_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			entities[i].setCompleted(completed_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] taskDemand_0002 = new int[len];
			for(int j = 0 ; j < taskDemand_0002.length ; j++){
				taskDemand_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			entities[i].setTaskDemand(taskDemand_0002);
			entities[i].setShowType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			entities[i].setFailTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		taskLevels = new int[len];
		for(int i = 0 ; i < taskLevels.length ; i++){
			taskLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70000FA1;
	}

	public String getTypeDescription() {
		return "TASK_QUERY_ENTITIES_RES";
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
		for(int i = 0 ; i < entities.length ; i++){
			len += 8;
			len += 4;
			len += 2;
			if(entities[i].getTaskName() != null){
				try{
				len += entities[i].getTaskName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += entities[i].getCompleted().length * 4;
			len += 4;
			len += entities[i].getTaskDemand().length * 4;
			len += 1;
			len += 8;
		}
		len += 4;
		len += taskLevels.length * 4;
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

			buffer.putInt(entities.length);
			for(int i = 0 ; i < entities.length ; i++){
				buffer.putLong(entities[i].getTaskId());
				buffer.putInt((int)entities[i].getStatus());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = entities[i].getTaskName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(entities[i].getCompleted().length);
				int[] completed_0003 = entities[i].getCompleted();
				for(int j = 0 ; j < completed_0003.length ; j++){
					buffer.putInt(completed_0003[j]);
				}
				buffer.putInt(entities[i].getTaskDemand().length);
				int[] taskDemand_0004 = entities[i].getTaskDemand();
				for(int j = 0 ; j < taskDemand_0004.length ; j++){
					buffer.putInt(taskDemand_0004[j]);
				}
				buffer.put((byte)entities[i].getShowType());
				buffer.putLong(entities[i].getFailTime());
			}
			buffer.putInt(taskLevels.length);
			for(int i = 0 ; i < taskLevels.length; i++){
				buffer.putInt(taskLevels[i]);
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
	 *	玩家正在做的任务列表，也就是已经接了，但还没有交付的任务
	 */
	public TaskEntity[] getEntities(){
		return entities;
	}

	/**
	 * 设置属性：
	 *	玩家正在做的任务列表，也就是已经接了，但还没有交付的任务
	 */
	public void setEntities(TaskEntity[] entities){
		this.entities = entities;
	}

	/**
	 * 获取属性：
	 *	对应各个实体的级别
	 */
	public int[] getTaskLevels(){
		return taskLevels;
	}

	/**
	 * 设置属性：
	 *	对应各个实体的级别
	 */
	public void setTaskLevels(int[] taskLevels){
		this.taskLevels = taskLevels;
	}

}