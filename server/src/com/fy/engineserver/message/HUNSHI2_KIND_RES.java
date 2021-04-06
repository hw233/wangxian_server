package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.hunshi.Hunshi2Material;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 套装魂石分类请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kinds.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kinds</td><td>String</td><td>kinds.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2.length</td><td>int</td><td>4个字节</td><td>Hunshi2Material数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2[0].targetTempId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2[0].materialTempIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2[0].materialTempIds</td><td>long[]</td><td>hunshi2[0].materialTempIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2[0].mergeCost</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2[1].targetTempId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2[1].materialTempIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2[1].materialTempIds</td><td>long[]</td><td>hunshi2[1].materialTempIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2[1].mergeCost</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2[2].targetTempId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2[2].materialTempIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2[2].materialTempIds</td><td>long[]</td><td>hunshi2[2].materialTempIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2[2].mergeCost</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des</td><td>String</td><td>des.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class HUNSHI2_KIND_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String kinds;
	Hunshi2Material[] hunshi2;
	String des;

	public HUNSHI2_KIND_RES(){
	}

	public HUNSHI2_KIND_RES(long seqNum,String kinds,Hunshi2Material[] hunshi2,String des){
		this.seqNum = seqNum;
		this.kinds = kinds;
		this.hunshi2 = hunshi2;
		this.des = des;
	}

	public HUNSHI2_KIND_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		kinds = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		hunshi2 = new Hunshi2Material[len];
		for(int i = 0 ; i < hunshi2.length ; i++){
			hunshi2[i] = new Hunshi2Material();
			hunshi2[i].setTargetTempId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] materialTempIds_0001 = new long[len];
			for(int j = 0 ; j < materialTempIds_0001.length ; j++){
				materialTempIds_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			hunshi2[i].setMaterialTempIds(materialTempIds_0001);
			hunshi2[i].setMergeCost((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		des = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x70FFF021;
	}

	public String getTypeDescription() {
		return "HUNSHI2_KIND_RES";
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
			len +=kinds.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < hunshi2.length ; i++){
			len += 8;
			len += 4;
			len += hunshi2[i].getMaterialTempIds().length * 8;
			len += 8;
		}
		len += 2;
		try{
			len +=des.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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
			 tmpBytes1 = kinds.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(hunshi2.length);
			for(int i = 0 ; i < hunshi2.length ; i++){
				buffer.putLong(hunshi2[i].getTargetTempId());
				buffer.putInt(hunshi2[i].getMaterialTempIds().length);
				long[] materialTempIds_0002 = hunshi2[i].getMaterialTempIds();
				for(int j = 0 ; j < materialTempIds_0002.length ; j++){
					buffer.putLong(materialTempIds_0002[j]);
				}
				buffer.putLong(hunshi2[i].getMergeCost());
			}
				try{
			tmpBytes1 = des.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	分类名
	 */
	public String getKinds(){
		return kinds;
	}

	/**
	 * 设置属性：
	 *	分类名
	 */
	public void setKinds(String kinds){
		this.kinds = kinds;
	}

	/**
	 * 获取属性：
	 *	套装魂石材料
	 */
	public Hunshi2Material[] getHunshi2(){
		return hunshi2;
	}

	/**
	 * 设置属性：
	 *	套装魂石材料
	 */
	public void setHunshi2(Hunshi2Material[] hunshi2){
		this.hunshi2 = hunshi2;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String getDes(){
		return des;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDes(String des){
		this.des = des;
	}

}