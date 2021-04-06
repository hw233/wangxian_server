package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.core.client.FunctionNPC;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端向服务器发送请求包<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs.length</td><td>int</td><td>4个字节</td><td>FunctionNPC数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].avaiableTaskIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].avaiableTaskIds</td><td>long[]</td><td>functionNPCs[0].avaiableTaskIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].avaiableTaskNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].avaiableTaskNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].avaiableTaskNames[0]</td><td>String</td><td>functionNPCs[0].avaiableTaskNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].avaiableTaskNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].avaiableTaskNames[1]</td><td>String</td><td>functionNPCs[0].avaiableTaskNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].avaiableTaskNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].avaiableTaskNames[2]</td><td>String</td><td>functionNPCs[0].avaiableTaskNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].avaiableTaskGrades.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].avaiableTaskGrades</td><td>int[]</td><td>functionNPCs[0].avaiableTaskGrades.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].avaiableTaskTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].avaiableTaskTypes</td><td>byte[]</td><td>functionNPCs[0].avaiableTaskTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].name</td><td>String</td><td>functionNPCs[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[0].title</td><td>String</td><td>functionNPCs[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[0].functionType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].avaiableTaskIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].avaiableTaskIds</td><td>long[]</td><td>functionNPCs[1].avaiableTaskIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].avaiableTaskNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].avaiableTaskNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].avaiableTaskNames[0]</td><td>String</td><td>functionNPCs[1].avaiableTaskNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].avaiableTaskNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].avaiableTaskNames[1]</td><td>String</td><td>functionNPCs[1].avaiableTaskNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].avaiableTaskNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].avaiableTaskNames[2]</td><td>String</td><td>functionNPCs[1].avaiableTaskNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].avaiableTaskGrades.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].avaiableTaskGrades</td><td>int[]</td><td>functionNPCs[1].avaiableTaskGrades.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].avaiableTaskTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].avaiableTaskTypes</td><td>byte[]</td><td>functionNPCs[1].avaiableTaskTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].name</td><td>String</td><td>functionNPCs[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[1].title</td><td>String</td><td>functionNPCs[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[1].functionType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].avaiableTaskIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].avaiableTaskIds</td><td>long[]</td><td>functionNPCs[2].avaiableTaskIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].avaiableTaskNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].avaiableTaskNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].avaiableTaskNames[0]</td><td>String</td><td>functionNPCs[2].avaiableTaskNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].avaiableTaskNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].avaiableTaskNames[1]</td><td>String</td><td>functionNPCs[2].avaiableTaskNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].avaiableTaskNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].avaiableTaskNames[2]</td><td>String</td><td>functionNPCs[2].avaiableTaskNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].avaiableTaskGrades.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].avaiableTaskGrades</td><td>int[]</td><td>functionNPCs[2].avaiableTaskGrades.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].avaiableTaskTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].avaiableTaskTypes</td><td>byte[]</td><td>functionNPCs[2].avaiableTaskTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].name</td><td>String</td><td>functionNPCs[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionNPCs[2].title</td><td>String</td><td>functionNPCs[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionNPCs[2].functionType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class FUNCTION_NPC_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	FunctionNPC[] functionNPCs;

	public FUNCTION_NPC_RES(){
	}

	public FUNCTION_NPC_RES(long seqNum,FunctionNPC[] functionNPCs){
		this.seqNum = seqNum;
		this.functionNPCs = functionNPCs;
	}

	public FUNCTION_NPC_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		functionNPCs = new FunctionNPC[len];
		for(int i = 0 ; i < functionNPCs.length ; i++){
			functionNPCs[i] = new FunctionNPC();
			functionNPCs[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			functionNPCs[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] avaiableTaskIds_0001 = new long[len];
			for(int j = 0 ; j < avaiableTaskIds_0001.length ; j++){
				avaiableTaskIds_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			functionNPCs[i].setAvaiableTaskIds(avaiableTaskIds_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] avaiableTaskNames_0002 = new String[len];
			for(int j = 0 ; j < avaiableTaskNames_0002.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				avaiableTaskNames_0002[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			functionNPCs[i].setAvaiableTaskNames(avaiableTaskNames_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] avaiableTaskGrades_0003 = new int[len];
			for(int j = 0 ; j < avaiableTaskGrades_0003.length ; j++){
				avaiableTaskGrades_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			functionNPCs[i].setAvaiableTaskGrades(avaiableTaskGrades_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] avaiableTaskTypes_0004 = new byte[len];
			System.arraycopy(content,offset,avaiableTaskTypes_0004,0,len);
			offset += len;
			functionNPCs[i].setAvaiableTaskTypes(avaiableTaskTypes_0004);
			functionNPCs[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			functionNPCs[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			functionNPCs[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			functionNPCs[i].setFunctionType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
	}

	public int getType() {
		return 0x70000019;
	}

	public String getTypeDescription() {
		return "FUNCTION_NPC_RES";
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
		for(int i = 0 ; i < functionNPCs.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			len += functionNPCs[i].getAvaiableTaskIds().length * 8;
			len += 4;
			String[] avaiableTaskNames = functionNPCs[i].getAvaiableTaskNames();
			for(int j = 0 ; j < avaiableTaskNames.length; j++){
				len += 2;
				try{
					len += avaiableTaskNames[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += functionNPCs[i].getAvaiableTaskGrades().length * 4;
			len += 4;
			len += functionNPCs[i].getAvaiableTaskTypes().length * 1;
			len += 8;
			len += 2;
			if(functionNPCs[i].getName() != null){
				try{
				len += functionNPCs[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(functionNPCs[i].getTitle() != null){
				try{
				len += functionNPCs[i].getTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
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

			buffer.putInt(functionNPCs.length);
			for(int i = 0 ; i < functionNPCs.length ; i++){
				buffer.putInt((int)functionNPCs[i].getX());
				buffer.putInt((int)functionNPCs[i].getY());
				buffer.putInt(functionNPCs[i].getAvaiableTaskIds().length);
				long[] avaiableTaskIds_0005 = functionNPCs[i].getAvaiableTaskIds();
				for(int j = 0 ; j < avaiableTaskIds_0005.length ; j++){
					buffer.putLong(avaiableTaskIds_0005[j]);
				}
				buffer.putInt(functionNPCs[i].getAvaiableTaskNames().length);
				String[] avaiableTaskNames_0006 = functionNPCs[i].getAvaiableTaskNames();
				for(int j = 0 ; j < avaiableTaskNames_0006.length ; j++){
				try{
					buffer.putShort((short)avaiableTaskNames_0006[j].getBytes("UTF-8").length);
					buffer.put(avaiableTaskNames_0006[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(functionNPCs[i].getAvaiableTaskGrades().length);
				int[] avaiableTaskGrades_0007 = functionNPCs[i].getAvaiableTaskGrades();
				for(int j = 0 ; j < avaiableTaskGrades_0007.length ; j++){
					buffer.putInt(avaiableTaskGrades_0007[j]);
				}
				buffer.putInt(functionNPCs[i].getAvaiableTaskTypes().length);
				buffer.put(functionNPCs[i].getAvaiableTaskTypes());
				buffer.putLong(functionNPCs[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = functionNPCs[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = functionNPCs[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)functionNPCs[i].getFunctionType());
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
	 *	功能性NPC数组
	 */
	public FunctionNPC[] getFunctionNPCs(){
		return functionNPCs;
	}

	/**
	 * 设置属性：
	 *	功能性NPC数组
	 */
	public void setFunctionNPCs(FunctionNPC[] functionNPCs){
		this.functionNPCs = functionNPCs;
	}

}