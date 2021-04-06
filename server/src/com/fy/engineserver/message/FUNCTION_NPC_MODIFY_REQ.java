package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.newtask.service.FunctionNpcModify;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 当前地图NPC可接任务变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs.length</td><td>int</td><td>4个字节</td><td>FunctionNpcModify数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[0].NPCId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[0].modifyType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[0].modifyNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[0].modifyNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[0].modifyNames[0]</td><td>String</td><td>modifyNPCs[0].modifyNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[0].modifyNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[0].modifyNames[1]</td><td>String</td><td>modifyNPCs[0].modifyNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[0].modifyNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[0].modifyNames[2]</td><td>String</td><td>modifyNPCs[0].modifyNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[0].modifyIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[0].modifyIds</td><td>long[]</td><td>modifyNPCs[0].modifyIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[0].modifyTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[0].modifyTypes</td><td>byte[]</td><td>modifyNPCs[0].modifyTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[0].modifyGrades.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[0].modifyGrades</td><td>int[]</td><td>modifyNPCs[0].modifyGrades.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[1].NPCId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[1].modifyType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[1].modifyNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[1].modifyNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[1].modifyNames[0]</td><td>String</td><td>modifyNPCs[1].modifyNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[1].modifyNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[1].modifyNames[1]</td><td>String</td><td>modifyNPCs[1].modifyNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[1].modifyNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[1].modifyNames[2]</td><td>String</td><td>modifyNPCs[1].modifyNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[1].modifyIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[1].modifyIds</td><td>long[]</td><td>modifyNPCs[1].modifyIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[1].modifyTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[1].modifyTypes</td><td>byte[]</td><td>modifyNPCs[1].modifyTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[1].modifyGrades.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[1].modifyGrades</td><td>int[]</td><td>modifyNPCs[1].modifyGrades.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[2].NPCId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[2].modifyType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[2].modifyNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[2].modifyNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[2].modifyNames[0]</td><td>String</td><td>modifyNPCs[2].modifyNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[2].modifyNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[2].modifyNames[1]</td><td>String</td><td>modifyNPCs[2].modifyNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[2].modifyNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[2].modifyNames[2]</td><td>String</td><td>modifyNPCs[2].modifyNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[2].modifyIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[2].modifyIds</td><td>long[]</td><td>modifyNPCs[2].modifyIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[2].modifyTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[2].modifyTypes</td><td>byte[]</td><td>modifyNPCs[2].modifyTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modifyNPCs[2].modifyGrades.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modifyNPCs[2].modifyGrades</td><td>int[]</td><td>modifyNPCs[2].modifyGrades.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class FUNCTION_NPC_MODIFY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	FunctionNpcModify[] modifyNPCs;

	public FUNCTION_NPC_MODIFY_REQ(){
	}

	public FUNCTION_NPC_MODIFY_REQ(long seqNum,FunctionNpcModify[] modifyNPCs){
		this.seqNum = seqNum;
		this.modifyNPCs = modifyNPCs;
	}

	public FUNCTION_NPC_MODIFY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		modifyNPCs = new FunctionNpcModify[len];
		for(int i = 0 ; i < modifyNPCs.length ; i++){
			modifyNPCs[i] = new FunctionNpcModify();
			modifyNPCs[i].setNPCId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			modifyNPCs[i].setModifyType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] modifyNames_0001 = new String[len];
			for(int j = 0 ; j < modifyNames_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				modifyNames_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			modifyNPCs[i].setModifyNames(modifyNames_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] modifyIds_0002 = new long[len];
			for(int j = 0 ; j < modifyIds_0002.length ; j++){
				modifyIds_0002[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			modifyNPCs[i].setModifyIds(modifyIds_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] modifyTypes_0003 = new byte[len];
			System.arraycopy(content,offset,modifyTypes_0003,0,len);
			offset += len;
			modifyNPCs[i].setModifyTypes(modifyTypes_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] modifyGrades_0004 = new int[len];
			for(int j = 0 ; j < modifyGrades_0004.length ; j++){
				modifyGrades_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			modifyNPCs[i].setModifyGrades(modifyGrades_0004);
		}
	}

	public int getType() {
		return 0x00000FA5;
	}

	public String getTypeDescription() {
		return "FUNCTION_NPC_MODIFY_REQ";
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
		for(int i = 0 ; i < modifyNPCs.length ; i++){
			len += 8;
			len += 1;
			len += 4;
			String[] modifyNames = modifyNPCs[i].getModifyNames();
			for(int j = 0 ; j < modifyNames.length; j++){
				len += 2;
				try{
					len += modifyNames[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += modifyNPCs[i].getModifyIds().length * 8;
			len += 4;
			len += modifyNPCs[i].getModifyTypes().length * 1;
			len += 4;
			len += modifyNPCs[i].getModifyGrades().length * 4;
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

			buffer.putInt(modifyNPCs.length);
			for(int i = 0 ; i < modifyNPCs.length ; i++){
				buffer.putLong(modifyNPCs[i].getNPCId());
				buffer.put((byte)modifyNPCs[i].getModifyType());
				buffer.putInt(modifyNPCs[i].getModifyNames().length);
				String[] modifyNames_0005 = modifyNPCs[i].getModifyNames();
				for(int j = 0 ; j < modifyNames_0005.length ; j++){
				try{
					buffer.putShort((short)modifyNames_0005[j].getBytes("UTF-8").length);
					buffer.put(modifyNames_0005[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(modifyNPCs[i].getModifyIds().length);
				long[] modifyIds_0006 = modifyNPCs[i].getModifyIds();
				for(int j = 0 ; j < modifyIds_0006.length ; j++){
					buffer.putLong(modifyIds_0006[j]);
				}
				buffer.putInt(modifyNPCs[i].getModifyTypes().length);
				buffer.put(modifyNPCs[i].getModifyTypes());
				buffer.putInt(modifyNPCs[i].getModifyGrades().length);
				int[] modifyGrades_0007 = modifyNPCs[i].getModifyGrades();
				for(int j = 0 ; j < modifyGrades_0007.length ; j++){
					buffer.putInt(modifyGrades_0007[j]);
				}
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
	 *	无帮助说明
	 */
	public FunctionNpcModify[] getModifyNPCs(){
		return modifyNPCs;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setModifyNPCs(FunctionNpcModify[] modifyNPCs){
		this.modifyNPCs = modifyNPCs;
	}

}