package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.jiazu2.manager.HuanJingInfoForClient;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 单人副本<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cityType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos.length</td><td>int</td><td>4个字节</td><td>HuanJingInfoForClient数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].bossId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].bossName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].bossName</td><td>String</td><td>infos[0].bossName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].bossIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].bossIcon</td><td>String</td><td>infos[0].bossIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].dropRate.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].dropRate</td><td>String</td><td>infos[0].dropRate.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].ids.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].ids</td><td>long[]</td><td>infos[0].ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].nums</td><td>int[]</td><td>infos[0].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].cityName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].cityName</td><td>String</td><td>infos[0].cityName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].openTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].openTime</td><td>String</td><td>infos[0].openTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].jiazuLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].enterTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].totleTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].bossId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].bossName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].bossName</td><td>String</td><td>infos[1].bossName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].bossIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].bossIcon</td><td>String</td><td>infos[1].bossIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].dropRate.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].dropRate</td><td>String</td><td>infos[1].dropRate.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].ids.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].ids</td><td>long[]</td><td>infos[1].ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].nums</td><td>int[]</td><td>infos[1].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].cityName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].cityName</td><td>String</td><td>infos[1].cityName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].openTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].openTime</td><td>String</td><td>infos[1].openTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].jiazuLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].enterTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].totleTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].bossId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].bossName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].bossName</td><td>String</td><td>infos[2].bossName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].bossIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].bossIcon</td><td>String</td><td>infos[2].bossIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].dropRate.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].dropRate</td><td>String</td><td>infos[2].dropRate.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].ids.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].ids</td><td>long[]</td><td>infos[2].ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].nums</td><td>int[]</td><td>infos[2].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].cityName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].cityName</td><td>String</td><td>infos[2].cityName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].openTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].openTime</td><td>String</td><td>infos[2].openTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].jiazuLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].enterTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].totleTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CITY_SINGLE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int cityType;
	HuanJingInfoForClient[] infos;

	public CITY_SINGLE_RES(){
	}

	public CITY_SINGLE_RES(long seqNum,int cityType,HuanJingInfoForClient[] infos){
		this.seqNum = seqNum;
		this.cityType = cityType;
		this.infos = infos;
	}

	public CITY_SINGLE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		cityType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		infos = new HuanJingInfoForClient[len];
		for(int i = 0 ; i < infos.length ; i++){
			infos[i] = new HuanJingInfoForClient();
			infos[i].setBossId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setBossName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setBossIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setDropRate(new String(content,offset,len,"UTF-8"));
			offset += len;
			infos[i].setPlayerLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] ids_0001 = new long[len];
			for(int j = 0 ; j < ids_0001.length ; j++){
				ids_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			infos[i].setIds(ids_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] nums_0002 = new int[len];
			for(int j = 0 ; j < nums_0002.length ; j++){
				nums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			infos[i].setNums(nums_0002);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setCityName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setOpenTime(new String(content,offset,len,"UTF-8"));
			offset += len;
			infos[i].setJiazuLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			infos[i].setEnterTimes((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			infos[i].setTotleTimes((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF136;
	}

	public String getTypeDescription() {
		return "CITY_SINGLE_RES";
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
		len += 4;
		for(int i = 0 ; i < infos.length ; i++){
			len += 8;
			len += 2;
			if(infos[i].getBossName() != null){
				try{
				len += infos[i].getBossName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(infos[i].getBossIcon() != null){
				try{
				len += infos[i].getBossIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(infos[i].getDropRate() != null){
				try{
				len += infos[i].getDropRate().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += infos[i].getIds().length * 8;
			len += 4;
			len += infos[i].getNums().length * 4;
			len += 2;
			if(infos[i].getCityName() != null){
				try{
				len += infos[i].getCityName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(infos[i].getOpenTime() != null){
				try{
				len += infos[i].getOpenTime().getBytes("UTF-8").length;
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

			buffer.putInt(cityType);
			buffer.putInt(infos.length);
			for(int i = 0 ; i < infos.length ; i++){
				buffer.putLong(infos[i].getBossId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = infos[i].getBossName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = infos[i].getBossIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = infos[i].getDropRate().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)infos[i].getPlayerLevel());
				buffer.putInt(infos[i].getIds().length);
				long[] ids_0003 = infos[i].getIds();
				for(int j = 0 ; j < ids_0003.length ; j++){
					buffer.putLong(ids_0003[j]);
				}
				buffer.putInt(infos[i].getNums().length);
				int[] nums_0004 = infos[i].getNums();
				for(int j = 0 ; j < nums_0004.length ; j++){
					buffer.putInt(nums_0004[j]);
				}
				try{
				tmpBytes2 = infos[i].getCityName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = infos[i].getOpenTime().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)infos[i].getJiazuLevel());
				buffer.putInt((int)infos[i].getEnterTimes());
				buffer.putInt((int)infos[i].getTotleTimes());
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
	 *	副本类型,0：组队副本，1：家族副本，2：全民boss
	 */
	public int getCityType(){
		return cityType;
	}

	/**
	 * 设置属性：
	 *	副本类型,0：组队副本，1：家族副本，2：全民boss
	 */
	public void setCityType(int cityType){
		this.cityType = cityType;
	}

	/**
	 * 获取属性：
	 *	副本信息
	 */
	public HuanJingInfoForClient[] getInfos(){
		return infos;
	}

	/**
	 * 设置属性：
	 *	副本信息
	 */
	public void setInfos(HuanJingInfoForClient[] infos){
		this.infos = infos;
	}

}