package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.newtask.TaskEntity;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端，某个任务实体发生了变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>actionType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entity.taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entity.taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entity.taskName</td><td>String</td><td>entity.taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entity.taskGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entity.status</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entity.showType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entity.taskDemand.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entity.taskDemand</td><td>int[]</td><td>entity.taskDemand.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entity.completed.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entity.completed</td><td>int[]</td><td>entity.completed.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entity.failTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class TASK_SEND_ENTITY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte actionType;
	TaskEntity entity;

	public TASK_SEND_ENTITY_REQ(){
	}

	public TASK_SEND_ENTITY_REQ(long seqNum,byte actionType,TaskEntity entity){
		this.seqNum = seqNum;
		this.actionType = actionType;
		this.entity = entity;
	}

	public TASK_SEND_ENTITY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		actionType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		entity = new TaskEntity();
		entity.setTaskId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		entity.setTaskName(new String(content,offset,len,"UTF-8"));
		offset += len;
		entity.setTaskGrade((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		entity.setStatus((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		entity.setShowType((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] taskDemand_0001 = new int[len];
		for(int j = 0 ; j < taskDemand_0001.length ; j++){
			taskDemand_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		entity.setTaskDemand(taskDemand_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] completed_0002 = new int[len];
		for(int j = 0 ; j < completed_0002.length ; j++){
			completed_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		entity.setCompleted(completed_0002);
		entity.setFailTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
	}

	public int getType() {
		return 0x00000FA2;
	}

	public String getTypeDescription() {
		return "TASK_SEND_ENTITY_REQ";
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
		len += 1;
		len += 8;
		len += 2;
		if(entity.getTaskName() != null){
			try{
			len += entity.getTaskName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 1;
		len += 4;
		len += entity.getTaskDemand().length * 4;
		len += 4;
		len += entity.getCompleted().length * 4;
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

			buffer.put(actionType);
			buffer.putLong(entity.getTaskId());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = entity.getTaskName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)entity.getTaskGrade());
			buffer.putInt((int)entity.getStatus());
			buffer.put((byte)entity.getShowType());
			buffer.putInt(entity.getTaskDemand().length);
			int[] taskDemand_0003 = entity.getTaskDemand();
			for(int j = 0 ; j < taskDemand_0003.length ; j++){
				buffer.putInt(taskDemand_0003[j]);
			}
			buffer.putInt(entity.getCompleted().length);
			int[] completed_0004 = entity.getCompleted();
			for(int j = 0 ; j < completed_0004.length ; j++){
				buffer.putInt(completed_0004[j]);
			}
			buffer.putLong(entity.getFailTime());
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
	 *	某个实体发生了变化，0标识已接任务发生了变化，1标识新接了一个任务，2标识交付了一个任务，3标识放弃了一个任务
	 */
	public byte getActionType(){
		return actionType;
	}

	/**
	 * 设置属性：
	 *	某个实体发生了变化，0标识已接任务发生了变化，1标识新接了一个任务，2标识交付了一个任务，3标识放弃了一个任务
	 */
	public void setActionType(byte actionType){
		this.actionType = actionType;
	}

	/**
	 * 获取属性：
	 *	玩家正在做的任务列表，也就是已经接了，但还没有交付的任务
	 */
	public TaskEntity getEntity(){
		return entity;
	}

	/**
	 * 设置属性：
	 *	玩家正在做的任务列表，也就是已经接了，但还没有交付的任务
	 */
	public void setEntity(TaskEntity entity){
		this.entity = entity;
	}

}