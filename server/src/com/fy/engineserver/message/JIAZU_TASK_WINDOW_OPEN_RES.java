package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.jiazu2.model.JiazuTaskModel4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开家族任务面板<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuGongzi</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>refreshDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>refreshDes</td><td>String</td><td>refreshDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>alreadyComp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totalComp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks.length</td><td>int</td><td>4个字节</td><td>JiazuTaskModel4Client数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[0].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[0].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[0].taskName</td><td>String</td><td>tasks[0].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[0].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[0].des</td><td>String</td><td>tasks[0].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[0].star</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[0].rewardDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[0].rewardDes</td><td>String</td><td>tasks[0].rewardDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[0].targetDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[0].targetDes</td><td>String</td><td>tasks[0].targetDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[0].targetLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[1].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[1].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[1].taskName</td><td>String</td><td>tasks[1].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[1].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[1].des</td><td>String</td><td>tasks[1].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[1].star</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[1].rewardDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[1].rewardDes</td><td>String</td><td>tasks[1].rewardDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[1].targetDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[1].targetDes</td><td>String</td><td>tasks[1].targetDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[1].targetLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[2].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[2].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[2].taskName</td><td>String</td><td>tasks[2].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[2].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[2].des</td><td>String</td><td>tasks[2].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[2].star</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[2].rewardDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[2].rewardDes</td><td>String</td><td>tasks[2].rewardDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[2].targetDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tasks[2].targetDes</td><td>String</td><td>tasks[2].targetDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tasks[2].targetLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class JIAZU_TASK_WINDOW_OPEN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long jiazuGongzi;
	String refreshDes;
	int alreadyComp;
	int totalComp;
	JiazuTaskModel4Client[] tasks;

	public JIAZU_TASK_WINDOW_OPEN_RES(){
	}

	public JIAZU_TASK_WINDOW_OPEN_RES(long seqNum,long jiazuGongzi,String refreshDes,int alreadyComp,int totalComp,JiazuTaskModel4Client[] tasks){
		this.seqNum = seqNum;
		this.jiazuGongzi = jiazuGongzi;
		this.refreshDes = refreshDes;
		this.alreadyComp = alreadyComp;
		this.totalComp = totalComp;
		this.tasks = tasks;
	}

	public JIAZU_TASK_WINDOW_OPEN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		jiazuGongzi = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		refreshDes = new String(content,offset,len,"UTF-8");
		offset += len;
		alreadyComp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		totalComp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		tasks = new JiazuTaskModel4Client[len];
		for(int i = 0 ; i < tasks.length ; i++){
			tasks[i] = new JiazuTaskModel4Client();
			tasks[i].setTaskId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tasks[i].setTaskName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tasks[i].setDes(new String(content,offset,len,"UTF-8"));
			offset += len;
			tasks[i].setStar((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tasks[i].setRewardDes(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tasks[i].setTargetDes(new String(content,offset,len,"UTF-8"));
			offset += len;
			tasks[i].setTargetLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0039;
	}

	public String getTypeDescription() {
		return "JIAZU_TASK_WINDOW_OPEN_RES";
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
		len += 2;
		try{
			len +=refreshDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < tasks.length ; i++){
			len += 8;
			len += 2;
			if(tasks[i].getTaskName() != null){
				try{
				len += tasks[i].getTaskName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(tasks[i].getDes() != null){
				try{
				len += tasks[i].getDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 2;
			if(tasks[i].getRewardDes() != null){
				try{
				len += tasks[i].getRewardDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(tasks[i].getTargetDes() != null){
				try{
				len += tasks[i].getTargetDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
		}
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

			buffer.putLong(jiazuGongzi);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = refreshDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(alreadyComp);
			buffer.putInt(totalComp);
			buffer.putInt(tasks.length);
			for(int i = 0 ; i < tasks.length ; i++){
				buffer.putLong(tasks[i].getTaskId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = tasks[i].getTaskName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = tasks[i].getDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)tasks[i].getStar());
				try{
				tmpBytes2 = tasks[i].getRewardDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = tasks[i].getTargetDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)tasks[i].getTargetLevel());
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
	 *	工资
	 */
	public long getJiazuGongzi(){
		return jiazuGongzi;
	}

	/**
	 * 设置属性：
	 *	工资
	 */
	public void setJiazuGongzi(long jiazuGongzi){
		this.jiazuGongzi = jiazuGongzi;
	}

	/**
	 * 获取属性：
	 *	刷新任务描述
	 */
	public String getRefreshDes(){
		return refreshDes;
	}

	/**
	 * 设置属性：
	 *	刷新任务描述
	 */
	public void setRefreshDes(String refreshDes){
		this.refreshDes = refreshDes;
	}

	/**
	 * 获取属性：
	 *	当前已经完成任务数量
	 */
	public int getAlreadyComp(){
		return alreadyComp;
	}

	/**
	 * 设置属性：
	 *	当前已经完成任务数量
	 */
	public void setAlreadyComp(int alreadyComp){
		this.alreadyComp = alreadyComp;
	}

	/**
	 * 获取属性：
	 *	每周最多可完成任务数量
	 */
	public int getTotalComp(){
		return totalComp;
	}

	/**
	 * 设置属性：
	 *	每周最多可完成任务数量
	 */
	public void setTotalComp(int totalComp){
		this.totalComp = totalComp;
	}

	/**
	 * 获取属性：
	 *	家族技能信息
	 */
	public JiazuTaskModel4Client[] getTasks(){
		return tasks;
	}

	/**
	 * 设置属性：
	 *	家族技能信息
	 */
	public void setTasks(JiazuTaskModel4Client[] tasks){
		this.tasks = tasks;
	}

}