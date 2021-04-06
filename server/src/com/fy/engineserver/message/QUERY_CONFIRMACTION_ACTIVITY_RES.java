package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.fy.confirm.bean.GameActivity;
import com.xuanzhi.tools.transport.ResponseMessage;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询所有激活码活动<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.length</td><td>int</td><td>4个字节</td><td>GameActivity数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].name</td><td>String</td><td>activitys[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].description</td><td>String</td><td>activitys[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].startTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].endTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].eachUserExchangeTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].propNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].propNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].propNames[0]</td><td>String</td><td>activitys[0].propNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].propNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].propNames[1]</td><td>String</td><td>activitys[0].propNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].propNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].propNames[2]</td><td>String</td><td>activitys[0].propNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].nums</td><td>long[]</td><td>activitys[0].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].colors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].colors</td><td>int[]</td><td>activitys[0].colors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].binds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].binds</td><td>boolean[]</td><td>activitys[0].binds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].name</td><td>String</td><td>activitys[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].description</td><td>String</td><td>activitys[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].startTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].endTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].eachUserExchangeTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].propNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].propNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].propNames[0]</td><td>String</td><td>activitys[1].propNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].propNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].propNames[1]</td><td>String</td><td>activitys[1].propNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].propNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].propNames[2]</td><td>String</td><td>activitys[1].propNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].nums</td><td>long[]</td><td>activitys[1].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].colors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].colors</td><td>int[]</td><td>activitys[1].colors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].binds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].binds</td><td>boolean[]</td><td>activitys[1].binds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].name</td><td>String</td><td>activitys[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].description</td><td>String</td><td>activitys[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].startTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].endTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].eachUserExchangeTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].propNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].propNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].propNames[0]</td><td>String</td><td>activitys[2].propNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].propNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].propNames[1]</td><td>String</td><td>activitys[2].propNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].propNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].propNames[2]</td><td>String</td><td>activitys[2].propNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].nums</td><td>long[]</td><td>activitys[2].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].colors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].colors</td><td>int[]</td><td>activitys[2].colors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].binds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].binds</td><td>boolean[]</td><td>activitys[2].binds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_CONFIRMACTION_ACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	GameActivity[] activitys;

	public QUERY_CONFIRMACTION_ACTIVITY_RES(long seqNum,GameActivity[] activitys){
		this.seqNum = seqNum;
		this.activitys = activitys;
	}

	public QUERY_CONFIRMACTION_ACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		activitys = new GameActivity[len];
		for(int i = 0 ; i < activitys.length ; i++){
			activitys[i] = new GameActivity();
			activitys[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activitys[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activitys[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			activitys[i].setStartTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			activitys[i].setEndTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			activitys[i].setEachUserExchangeTimes((int)mf.byteArrayToNumber(content,offset,4));
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
			activitys[i].setPropNames(propNames_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] nums_0002 = new long[len];
			for(int j = 0 ; j < nums_0002.length ; j++){
				nums_0002[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			activitys[i].setNums(nums_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] colors_0003 = new int[len];
			for(int j = 0 ; j < colors_0003.length ; j++){
				colors_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			activitys[i].setColors(colors_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			boolean[] binds_0004 = new boolean[len];
			for(int j = 0 ; j < binds_0004.length ; j++){
				binds_0004[j] = mf.byteArrayToNumber(content,offset,1) != 0;
				offset += 1;
			}
			activitys[i].setBinds(binds_0004);
		}
	}

	public int getType() {
		return 0x8F3000A5;
	}

	public String getTypeDescription() {
		return "QUERY_CONFIRMACTION_ACTIVITY_RES";
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
		for(int i = 0 ; i < activitys.length ; i++){
			len += 8;
			len += 2;
			if(activitys[i].getName() != null){
				try{
				len += activitys[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(activitys[i].getDescription() != null){
				try{
				len += activitys[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 8;
			len += 4;
			len += 4;
			String[] propNames = activitys[i].getPropNames();
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
			len += activitys[i].getNums().length * 8;
			len += 4;
			len += activitys[i].getColors().length * 4;
			len += 4;
			len += activitys[i].getBinds().length * 1;
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

			buffer.putInt(activitys.length);
			for(int i = 0 ; i < activitys.length ; i++){
				buffer.putLong(activitys[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = activitys[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = activitys[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(activitys[i].getStartTime());
				buffer.putLong(activitys[i].getEndTime());
				buffer.putInt((int)activitys[i].getEachUserExchangeTimes());
				buffer.putInt(activitys[i].getPropNames().length);
				String[] propNames_0005 = activitys[i].getPropNames();
				for(int j = 0 ; j < propNames_0005.length ; j++){
				try{
					buffer.putShort((short)propNames_0005[j].getBytes("UTF-8").length);
					buffer.put(propNames_0005[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(activitys[i].getNums().length);
				long[] nums_0006 = activitys[i].getNums();
				for(int j = 0 ; j < nums_0006.length ; j++){
					buffer.putLong(nums_0006[j]);
				}
				buffer.putInt(activitys[i].getColors().length);
				int[] colors_0007 = activitys[i].getColors();
				for(int j = 0 ; j < colors_0007.length ; j++){
					buffer.putInt(colors_0007[j]);
				}
				buffer.putInt(activitys[i].getBinds().length);
				boolean[] binds_0008 = activitys[i].getBinds();
				for(int j = 0 ; j < binds_0008.length ; j++){
					buffer.put((byte)(binds_0008[j]?1:0));
				}
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	所有有效的活动列表
	 */
	public GameActivity[] getActivitys(){
		return activitys;
	}

	/**
	 * 设置属性：
	 *	所有有效的活动列表
	 */
	public void setActivitys(GameActivity[] activitys){
		this.activitys = activitys;
	}

}
