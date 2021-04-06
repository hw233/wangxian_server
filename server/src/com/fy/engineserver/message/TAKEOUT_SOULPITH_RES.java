package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.soulpith.module.SoulpithInfo4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 取出灵髓<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soultype</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.soulNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.soulNums</td><td>int[]</td><td>datas.soulNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.inlayInfos.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.inlayInfos</td><td>long[]</td><td>datas.inlayInfos.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.description.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.description[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.description[0]</td><td>String</td><td>datas.description[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.description[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.description[1]</td><td>String</td><td>datas.description[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.description[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.description[2]</td><td>String</td><td>datas.description[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.basicsoulNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.basicsoulNums</td><td>int[]</td><td>datas.basicsoulNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.description2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.description2[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.description2[0]</td><td>String</td><td>datas.description2[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.description2[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.description2[1]</td><td>String</td><td>datas.description2[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.description2[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.description2[2]</td><td>String</td><td>datas.description2[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class TAKEOUT_SOULPITH_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int soultype;
	SoulpithInfo4Client datas;

	public TAKEOUT_SOULPITH_RES(){
	}

	public TAKEOUT_SOULPITH_RES(long seqNum,int soultype,SoulpithInfo4Client datas){
		this.seqNum = seqNum;
		this.soultype = soultype;
		this.datas = datas;
	}

	public TAKEOUT_SOULPITH_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		soultype = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		datas = new SoulpithInfo4Client();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] soulNums_0001 = new int[len];
		for(int j = 0 ; j < soulNums_0001.length ; j++){
			soulNums_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		datas.setSoulNums(soulNums_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		long[] inlayInfos_0002 = new long[len];
		for(int j = 0 ; j < inlayInfos_0002.length ; j++){
			inlayInfos_0002[j] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		datas.setInlayInfos(inlayInfos_0002);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] description_0003 = new String[len];
		for(int j = 0 ; j < description_0003.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			description_0003[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		datas.setDescription(description_0003);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] basicsoulNums_0004 = new int[len];
		for(int j = 0 ; j < basicsoulNums_0004.length ; j++){
			basicsoulNums_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		datas.setBasicsoulNums(basicsoulNums_0004);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] description2_0005 = new String[len];
		for(int j = 0 ; j < description2_0005.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			description2_0005[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		datas.setDescription2(description2_0005);
	}

	public int getType() {
		return 0x70FF0167;
	}

	public String getTypeDescription() {
		return "TAKEOUT_SOULPITH_RES";
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
		len += datas.getSoulNums().length * 4;
		len += 4;
		len += datas.getInlayInfos().length * 8;
		len += 4;
		String[] description = datas.getDescription();
		for(int j = 0 ; j < description.length; j++){
			len += 2;
			try{
				len += description[j].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += datas.getBasicsoulNums().length * 4;
		len += 4;
		String[] description2 = datas.getDescription2();
		for(int j = 0 ; j < description2.length; j++){
			len += 2;
			try{
				len += description2[j].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
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

			buffer.putInt(soultype);
			buffer.putInt(datas.getSoulNums().length);
			int[] soulNums_0006 = datas.getSoulNums();
			for(int j = 0 ; j < soulNums_0006.length ; j++){
				buffer.putInt(soulNums_0006[j]);
			}
			buffer.putInt(datas.getInlayInfos().length);
			long[] inlayInfos_0007 = datas.getInlayInfos();
			for(int j = 0 ; j < inlayInfos_0007.length ; j++){
				buffer.putLong(inlayInfos_0007[j]);
			}
			buffer.putInt(datas.getDescription().length);
			String[] description_0008 = datas.getDescription();
			for(int j = 0 ; j < description_0008.length ; j++){
				try{
				buffer.putShort((short)description_0008[j].getBytes("UTF-8").length);
				buffer.put(description_0008[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			buffer.putInt(datas.getBasicsoulNums().length);
			int[] basicsoulNums_0009 = datas.getBasicsoulNums();
			for(int j = 0 ; j < basicsoulNums_0009.length ; j++){
				buffer.putInt(basicsoulNums_0009[j]);
			}
			buffer.putInt(datas.getDescription2().length);
			String[] description2_0010 = datas.getDescription2();
			for(int j = 0 ; j < description2_0010.length ; j++){
				try{
				buffer.putShort((short)description2_0010[j].getBytes("UTF-8").length);
				buffer.put(description2_0010[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
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
	 *	元神类型
	 */
	public int getSoultype(){
		return soultype;
	}

	/**
	 * 设置属性：
	 *	元神类型
	 */
	public void setSoultype(int soultype){
		this.soultype = soultype;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public SoulpithInfo4Client getDatas(){
		return datas;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDatas(SoulpithInfo4Client datas){
		this.datas = datas;
	}

}