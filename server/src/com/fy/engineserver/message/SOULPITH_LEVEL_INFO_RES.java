package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.soulpith.module.SoulInfo4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 灵髓宝石等级信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulpithType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulpithType</td><td>String</td><td>soulpithType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.length</td><td>int</td><td>4个字节</td><td>SoulInfo4Client数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].propTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].propTypes</td><td>int[]</td><td>datas[0].propTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].propNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].propNums</td><td>int[]</td><td>datas[0].propNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].soulNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].soulNums</td><td>int[]</td><td>datas[0].soulNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].propTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].propTypes</td><td>int[]</td><td>datas[1].propTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].propNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].propNums</td><td>int[]</td><td>datas[1].propNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].soulNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].soulNums</td><td>int[]</td><td>datas[1].soulNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].propTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].propTypes</td><td>int[]</td><td>datas[2].propTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].propNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].propNums</td><td>int[]</td><td>datas[2].propNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].soulNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].soulNums</td><td>int[]</td><td>datas[2].soulNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SOULPITH_LEVEL_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String soulpithType;
	SoulInfo4Client[] datas;

	public SOULPITH_LEVEL_INFO_RES(){
	}

	public SOULPITH_LEVEL_INFO_RES(long seqNum,String soulpithType,SoulInfo4Client[] datas){
		this.seqNum = seqNum;
		this.soulpithType = soulpithType;
		this.datas = datas;
	}

	public SOULPITH_LEVEL_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		soulpithType = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		datas = new SoulInfo4Client[len];
		for(int i = 0 ; i < datas.length ; i++){
			datas[i] = new SoulInfo4Client();
			datas[i].setSoulLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] propTypes_0001 = new int[len];
			for(int j = 0 ; j < propTypes_0001.length ; j++){
				propTypes_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			datas[i].setPropTypes(propTypes_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] propNums_0002 = new int[len];
			for(int j = 0 ; j < propNums_0002.length ; j++){
				propNums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			datas[i].setPropNums(propNums_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] soulNums_0003 = new int[len];
			for(int j = 0 ; j < soulNums_0003.length ; j++){
				soulNums_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			datas[i].setSoulNums(soulNums_0003);
		}
	}

	public int getType() {
		return 0x70FFF024;
	}

	public String getTypeDescription() {
		return "SOULPITH_LEVEL_INFO_RES";
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
			len +=soulpithType.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < datas.length ; i++){
			len += 4;
			len += 4;
			len += datas[i].getPropTypes().length * 4;
			len += 4;
			len += datas[i].getPropNums().length * 4;
			len += 4;
			len += datas[i].getSoulNums().length * 4;
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
			 tmpBytes1 = soulpithType.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(datas.length);
			for(int i = 0 ; i < datas.length ; i++){
				buffer.putInt((int)datas[i].getSoulLevel());
				buffer.putInt(datas[i].getPropTypes().length);
				int[] propTypes_0004 = datas[i].getPropTypes();
				for(int j = 0 ; j < propTypes_0004.length ; j++){
					buffer.putInt(propTypes_0004[j]);
				}
				buffer.putInt(datas[i].getPropNums().length);
				int[] propNums_0005 = datas[i].getPropNums();
				for(int j = 0 ; j < propNums_0005.length ; j++){
					buffer.putInt(propNums_0005[j]);
				}
				buffer.putInt(datas[i].getSoulNums().length);
				int[] soulNums_0006 = datas[i].getSoulNums();
				for(int j = 0 ; j < soulNums_0006.length ; j++){
					buffer.putInt(soulNums_0006[j]);
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
	 *	灵髓类型
	 */
	public String getSoulpithType(){
		return soulpithType;
	}

	/**
	 * 设置属性：
	 *	灵髓类型
	 */
	public void setSoulpithType(String soulpithType){
		this.soulpithType = soulpithType;
	}

	/**
	 * 获取属性：
	 *	详细信息
	 */
	public SoulInfo4Client[] getDatas(){
		return datas;
	}

	/**
	 * 设置属性：
	 *	详细信息
	 */
	public void setDatas(SoulInfo4Client[] datas){
		this.datas = datas;
	}

}