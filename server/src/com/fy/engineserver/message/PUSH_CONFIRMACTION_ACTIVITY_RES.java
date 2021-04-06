package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.confirm.bean.GameActivity;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 某一激活码活动数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.name</td><td>String</td><td>activitys.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.description</td><td>String</td><td>activitys.description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.startTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.endTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.eachUserExchangeTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.propNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.propNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.propNames[0]</td><td>String</td><td>activitys.propNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.propNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.propNames[1]</td><td>String</td><td>activitys.propNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.propNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.propNames[2]</td><td>String</td><td>activitys.propNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.nums</td><td>long[]</td><td>activitys.nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.colors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.colors</td><td>int[]</td><td>activitys.colors.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.binds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.binds</td><td>boolean[]</td><td>activitys.binds.length</td><td>*</td></tr>
 * </table>
 */
public class PUSH_CONFIRMACTION_ACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	GameActivity activitys;

	public PUSH_CONFIRMACTION_ACTIVITY_RES(){
	}

	public PUSH_CONFIRMACTION_ACTIVITY_RES(long seqNum,GameActivity activitys){
		this.seqNum = seqNum;
		this.activitys = activitys;
	}

	public PUSH_CONFIRMACTION_ACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		activitys = new GameActivity();
		activitys.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activitys.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activitys.setDescription(new String(content,offset,len,"UTF-8"));
		offset += len;
		activitys.setStartTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		activitys.setEndTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		activitys.setEachUserExchangeTimes((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] propNames_0001 = new String[len];
		for(int j = 0 ; j < propNames_0001.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propNames_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		activitys.setPropNames(propNames_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		long[] nums_0002 = new long[len];
		for(int j = 0 ; j < nums_0002.length ; j++){
			nums_0002[j] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		activitys.setNums(nums_0002);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] colors_0003 = new int[len];
		for(int j = 0 ; j < colors_0003.length ; j++){
			colors_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		activitys.setColors(colors_0003);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		boolean[] binds_0004 = new boolean[len];
		for(int j = 0 ; j < binds_0004.length ; j++){
			binds_0004[j] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		activitys.setBinds(binds_0004);
	}

	public int getType() {
		return 0x8F3000A5;
	}

	public String getTypeDescription() {
		return "PUSH_CONFIRMACTION_ACTIVITY_RES";
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
		if(activitys.getName() != null){
			try{
			len += activitys.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(activitys.getDescription() != null){
			try{
			len += activitys.getDescription().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 8;
		len += 4;
		len += 4;
		String[] propNames = activitys.getPropNames();
		for(int j = 0 ; j < propNames.length; j++){
			len += 2;
			try{
				len += propNames[j].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += activitys.getNums().length * 8;
		len += 4;
		len += activitys.getColors().length * 4;
		len += 4;
		len += activitys.getBinds().length * 1;
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

			buffer.putLong(activitys.getId());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = activitys.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = activitys.getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(activitys.getStartTime());
			buffer.putLong(activitys.getEndTime());
			buffer.putInt((int)activitys.getEachUserExchangeTimes());
			buffer.putInt(activitys.getPropNames().length);
			String[] propNames_0005 = activitys.getPropNames();
			for(int j = 0 ; j < propNames_0005.length ; j++){
				try{
				buffer.putShort((short)propNames_0005[j].getBytes("UTF-8").length);
				buffer.put(propNames_0005[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			buffer.putInt(activitys.getNums().length);
			long[] nums_0006 = activitys.getNums();
			for(int j = 0 ; j < nums_0006.length ; j++){
				buffer.putLong(nums_0006[j]);
			}
			buffer.putInt(activitys.getColors().length);
			int[] colors_0007 = activitys.getColors();
			for(int j = 0 ; j < colors_0007.length ; j++){
				buffer.putInt(colors_0007[j]);
			}
			buffer.putInt(activitys.getBinds().length);
			boolean[] binds_0008 = activitys.getBinds();
			for(int j = 0 ; j < binds_0008.length ; j++){
				buffer.put((byte)(binds_0008[j]?1:0));
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
	 *	所有有效的活动列表
	 */
	public GameActivity getActivitys(){
		return activitys;
	}

	/**
	 * 设置属性：
	 *	所有有效的活动列表
	 */
	public void setActivitys(GameActivity activitys){
		this.activitys = activitys;
	}

}