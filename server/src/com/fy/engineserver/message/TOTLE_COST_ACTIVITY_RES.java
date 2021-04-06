package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.chongzhiActivity.TotleCostActivity;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 累计消费<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMess</td><td>String</td><td>helpMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys.length</td><td>int</td><td>4个字节</td><td>TotleCostActivity数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[0].hasChargeRmb</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[0].needChargeRmb</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[0].ids.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[0].ids</td><td>long[]</td><td>totleCostActivitys[0].ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[0].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[0].nums</td><td>int[]</td><td>totleCostActivitys[0].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[0].colors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[0].colors</td><td>int[]</td><td>totleCostActivitys[0].colors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[0].showIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[0].showIcon</td><td>String</td><td>totleCostActivitys[0].showIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[0].endTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[1].hasChargeRmb</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[1].needChargeRmb</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[1].ids.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[1].ids</td><td>long[]</td><td>totleCostActivitys[1].ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[1].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[1].nums</td><td>int[]</td><td>totleCostActivitys[1].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[1].colors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[1].colors</td><td>int[]</td><td>totleCostActivitys[1].colors.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[1].showIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[1].showIcon</td><td>String</td><td>totleCostActivitys[1].showIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[1].endTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[2].hasChargeRmb</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[2].needChargeRmb</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[2].ids.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[2].ids</td><td>long[]</td><td>totleCostActivitys[2].ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[2].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[2].nums</td><td>int[]</td><td>totleCostActivitys[2].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[2].colors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[2].colors</td><td>int[]</td><td>totleCostActivitys[2].colors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[2].showIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totleCostActivitys[2].showIcon</td><td>String</td><td>totleCostActivitys[2].showIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleCostActivitys[2].endTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class TOTLE_COST_ACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String helpMess;
	TotleCostActivity[] totleCostActivitys;

	public TOTLE_COST_ACTIVITY_RES(){
	}

	public TOTLE_COST_ACTIVITY_RES(long seqNum,String helpMess,TotleCostActivity[] totleCostActivitys){
		this.seqNum = seqNum;
		this.helpMess = helpMess;
		this.totleCostActivitys = totleCostActivitys;
	}

	public TOTLE_COST_ACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		helpMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		totleCostActivitys = new TotleCostActivity[len];
		for(int i = 0 ; i < totleCostActivitys.length ; i++){
			totleCostActivitys[i] = new TotleCostActivity();
			totleCostActivitys[i].setHasChargeRmb((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			totleCostActivitys[i].setNeedChargeRmb((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] ids_0001 = new long[len];
			for(int j = 0 ; j < ids_0001.length ; j++){
				ids_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			totleCostActivitys[i].setIds(ids_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] nums_0002 = new int[len];
			for(int j = 0 ; j < nums_0002.length ; j++){
				nums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			totleCostActivitys[i].setNums(nums_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] colors_0003 = new int[len];
			for(int j = 0 ; j < colors_0003.length ; j++){
				colors_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			totleCostActivitys[i].setColors(colors_0003);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			totleCostActivitys[i].setShowIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			totleCostActivitys[i].setEndTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
	}

	public int getType() {
		return 0x70FFF151;
	}

	public String getTypeDescription() {
		return "TOTLE_COST_ACTIVITY_RES";
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
		len += 2;
		try{
			len +=helpMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < totleCostActivitys.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			len += totleCostActivitys[i].getIds().length * 8;
			len += 4;
			len += totleCostActivitys[i].getNums().length * 4;
			len += 4;
			len += totleCostActivitys[i].getColors().length * 4;
			len += 2;
			if(totleCostActivitys[i].getShowIcon() != null){
				try{
				len += totleCostActivitys[i].getShowIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = helpMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(totleCostActivitys.length);
			for(int i = 0 ; i < totleCostActivitys.length ; i++){
				buffer.putInt((int)totleCostActivitys[i].getHasChargeRmb());
				buffer.putInt((int)totleCostActivitys[i].getNeedChargeRmb());
				buffer.putInt(totleCostActivitys[i].getIds().length);
				long[] ids_0004 = totleCostActivitys[i].getIds();
				for(int j = 0 ; j < ids_0004.length ; j++){
					buffer.putLong(ids_0004[j]);
				}
				buffer.putInt(totleCostActivitys[i].getNums().length);
				int[] nums_0005 = totleCostActivitys[i].getNums();
				for(int j = 0 ; j < nums_0005.length ; j++){
					buffer.putInt(nums_0005[j]);
				}
				buffer.putInt(totleCostActivitys[i].getColors().length);
				int[] colors_0006 = totleCostActivitys[i].getColors();
				for(int j = 0 ; j < colors_0006.length ; j++){
					buffer.putInt(colors_0006[j]);
				}
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = totleCostActivitys[i].getShowIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(totleCostActivitys[i].getEndTime());
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
	 *	帮助信息
	 */
	public String getHelpMess(){
		return helpMess;
	}

	/**
	 * 设置属性：
	 *	帮助信息
	 */
	public void setHelpMess(String helpMess){
		this.helpMess = helpMess;
	}

	/**
	 * 获取属性：
	 *	活动列表
	 */
	public TotleCostActivity[] getTotleCostActivitys(){
		return totleCostActivitys;
	}

	/**
	 * 设置属性：
	 *	活动列表
	 */
	public void setTotleCostActivitys(TotleCostActivity[] totleCostActivitys){
		this.totleCostActivitys = totleCostActivitys;
	}

}