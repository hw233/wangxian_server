package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.core.client.AavilableTaskInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 可接任务列表变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos.length</td><td>int</td><td>4个字节</td><td>AavilableTaskInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[0].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[0].showType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[0].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[0].taskName</td><td>String</td><td>aavilableTaskInfos[0].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[0].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[0].mapName</td><td>String</td><td>aavilableTaskInfos[0].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[0].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[0].npcName</td><td>String</td><td>aavilableTaskInfos[0].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[0].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[1].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[1].showType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[1].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[1].taskName</td><td>String</td><td>aavilableTaskInfos[1].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[1].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[1].mapName</td><td>String</td><td>aavilableTaskInfos[1].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[1].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[1].npcName</td><td>String</td><td>aavilableTaskInfos[1].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[1].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[2].taskId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[2].showType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[2].taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[2].taskName</td><td>String</td><td>aavilableTaskInfos[2].taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[2].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[2].mapName</td><td>String</td><td>aavilableTaskInfos[2].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[2].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[2].npcName</td><td>String</td><td>aavilableTaskInfos[2].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[2].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aavilableTaskInfos[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aavilableTaskInfos[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CAN_ACCEPT_TASK_MODIFY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte modifyType;
	AavilableTaskInfo[] aavilableTaskInfos;

	public CAN_ACCEPT_TASK_MODIFY_REQ(){
	}

	public CAN_ACCEPT_TASK_MODIFY_REQ(long seqNum,byte modifyType,AavilableTaskInfo[] aavilableTaskInfos){
		this.seqNum = seqNum;
		this.modifyType = modifyType;
		this.aavilableTaskInfos = aavilableTaskInfos;
	}

	public CAN_ACCEPT_TASK_MODIFY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		modifyType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		aavilableTaskInfos = new AavilableTaskInfo[len];
		for(int i = 0 ; i < aavilableTaskInfos.length ; i++){
			aavilableTaskInfos[i] = new AavilableTaskInfo();
			aavilableTaskInfos[i].setTaskId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			aavilableTaskInfos[i].setShowType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			aavilableTaskInfos[i].setTaskName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			aavilableTaskInfos[i].setMapName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			aavilableTaskInfos[i].setNpcName(new String(content,offset,len,"UTF-8"));
			offset += len;
			aavilableTaskInfos[i].setGrade((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			aavilableTaskInfos[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			aavilableTaskInfos[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x00000FA6;
	}

	public String getTypeDescription() {
		return "CAN_ACCEPT_TASK_MODIFY_REQ";
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
		len += 4;
		for(int i = 0 ; i < aavilableTaskInfos.length ; i++){
			len += 8;
			len += 1;
			len += 2;
			if(aavilableTaskInfos[i].getTaskName() != null){
				try{
				len += aavilableTaskInfos[i].getTaskName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(aavilableTaskInfos[i].getMapName() != null){
				try{
				len += aavilableTaskInfos[i].getMapName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(aavilableTaskInfos[i].getNpcName() != null){
				try{
				len += aavilableTaskInfos[i].getNpcName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
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

			buffer.put(modifyType);
			buffer.putInt(aavilableTaskInfos.length);
			for(int i = 0 ; i < aavilableTaskInfos.length ; i++){
				buffer.putLong(aavilableTaskInfos[i].getTaskId());
				buffer.put((byte)aavilableTaskInfos[i].getShowType());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = aavilableTaskInfos[i].getTaskName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = aavilableTaskInfos[i].getMapName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = aavilableTaskInfos[i].getNpcName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)aavilableTaskInfos[i].getGrade());
				buffer.putInt((int)aavilableTaskInfos[i].getX());
				buffer.putInt((int)aavilableTaskInfos[i].getY());
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
	 *	修改类型:0增加 1减少
	 */
	public byte getModifyType(){
		return modifyType;
	}

	/**
	 * 设置属性：
	 *	修改类型:0增加 1减少
	 */
	public void setModifyType(byte modifyType){
		this.modifyType = modifyType;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public AavilableTaskInfo[] getAavilableTaskInfos(){
		return aavilableTaskInfos;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAavilableTaskInfos(AavilableTaskInfo[] aavilableTaskInfos){
		this.aavilableTaskInfos = aavilableTaskInfos;
	}

}