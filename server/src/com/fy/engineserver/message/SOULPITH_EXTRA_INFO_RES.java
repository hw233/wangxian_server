package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.soulpith.module.SoulPithExtraAttrModule;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 灵根激活属性<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.length</td><td>int</td><td>4个字节</td><td>SoulPithExtraAttrModule数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].name</td><td>String</td><td>datas[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].soulPithTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].soulPithTypes</td><td>int[]</td><td>datas[0].soulPithTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].needSoulNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].needSoulNum</td><td>int[]</td><td>datas[0].needSoulNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].attrDes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].attrDes[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].attrDes[0]</td><td>String</td><td>datas[0].attrDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].attrDes[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].attrDes[1]</td><td>String</td><td>datas[0].attrDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].attrDes[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].attrDes[2]</td><td>String</td><td>datas[0].attrDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].name</td><td>String</td><td>datas[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].soulPithTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].soulPithTypes</td><td>int[]</td><td>datas[1].soulPithTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].needSoulNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].needSoulNum</td><td>int[]</td><td>datas[1].needSoulNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].attrDes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].attrDes[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].attrDes[0]</td><td>String</td><td>datas[1].attrDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].attrDes[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].attrDes[1]</td><td>String</td><td>datas[1].attrDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].attrDes[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].attrDes[2]</td><td>String</td><td>datas[1].attrDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].name</td><td>String</td><td>datas[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].soulPithTypes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].soulPithTypes</td><td>int[]</td><td>datas[2].soulPithTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].needSoulNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].needSoulNum</td><td>int[]</td><td>datas[2].needSoulNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].attrDes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].attrDes[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].attrDes[0]</td><td>String</td><td>datas[2].attrDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].attrDes[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].attrDes[1]</td><td>String</td><td>datas[2].attrDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].attrDes[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].attrDes[2]</td><td>String</td><td>datas[2].attrDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SOULPITH_EXTRA_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	SoulPithExtraAttrModule[] datas;

	public SOULPITH_EXTRA_INFO_RES(){
	}

	public SOULPITH_EXTRA_INFO_RES(long seqNum,SoulPithExtraAttrModule[] datas){
		this.seqNum = seqNum;
		this.datas = datas;
	}

	public SOULPITH_EXTRA_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		datas = new SoulPithExtraAttrModule[len];
		for(int i = 0 ; i < datas.length ; i++){
			datas[i] = new SoulPithExtraAttrModule();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			datas[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] soulPithTypes_0001 = new int[len];
			for(int j = 0 ; j < soulPithTypes_0001.length ; j++){
				soulPithTypes_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			datas[i].setSoulPithTypes(soulPithTypes_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needSoulNum_0002 = new int[len];
			for(int j = 0 ; j < needSoulNum_0002.length ; j++){
				needSoulNum_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			datas[i].setNeedSoulNum(needSoulNum_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] attrDes_0003 = new String[len];
			for(int j = 0 ; j < attrDes_0003.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				attrDes_0003[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			datas[i].setAttrDes(attrDes_0003);
		}
	}

	public int getType() {
		return 0x70FF0171;
	}

	public String getTypeDescription() {
		return "SOULPITH_EXTRA_INFO_RES";
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
		for(int i = 0 ; i < datas.length ; i++){
			len += 2;
			if(datas[i].getName() != null){
				try{
				len += datas[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += datas[i].getSoulPithTypes().length * 4;
			len += 4;
			len += datas[i].getNeedSoulNum().length * 4;
			len += 4;
			String[] attrDes = datas[i].getAttrDes();
			for(int j = 0 ; j < attrDes.length; j++){
				len += 2;
				try{
					len += attrDes[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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

			buffer.putInt(datas.length);
			for(int i = 0 ; i < datas.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = datas[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(datas[i].getSoulPithTypes().length);
				int[] soulPithTypes_0004 = datas[i].getSoulPithTypes();
				for(int j = 0 ; j < soulPithTypes_0004.length ; j++){
					buffer.putInt(soulPithTypes_0004[j]);
				}
				buffer.putInt(datas[i].getNeedSoulNum().length);
				int[] needSoulNum_0005 = datas[i].getNeedSoulNum();
				for(int j = 0 ; j < needSoulNum_0005.length ; j++){
					buffer.putInt(needSoulNum_0005[j]);
				}
				buffer.putInt(datas[i].getAttrDes().length);
				String[] attrDes_0006 = datas[i].getAttrDes();
				for(int j = 0 ; j < attrDes_0006.length ; j++){
				try{
					buffer.putShort((short)attrDes_0006[j].getBytes("UTF-8").length);
					buffer.put(attrDes_0006[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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
	public SoulPithExtraAttrModule[] getDatas(){
		return datas;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDatas(SoulPithExtraAttrModule[] datas){
		this.datas = datas;
	}

}