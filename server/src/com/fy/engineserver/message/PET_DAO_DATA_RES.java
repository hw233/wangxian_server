package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.petdao.PetData;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas.length</td><td>int</td><td>4个字节</td><td>PetData数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].daotype</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].petname.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].petname[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].petname[0]</td><td>String</td><td>petdatas[0].petname[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].petname[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].petname[1]</td><td>String</td><td>petdatas[0].petname[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].petname[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].petname[2]</td><td>String</td><td>petdatas[0].petname[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].petnames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].petnames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].petnames[0]</td><td>String</td><td>petdatas[0].petnames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].petnames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].petnames[1]</td><td>String</td><td>petdatas[0].petnames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].petnames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].petnames[2]</td><td>String</td><td>petdatas[0].petnames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].iconname.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].iconname[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].iconname[0]</td><td>String</td><td>petdatas[0].iconname[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].iconname[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].iconname[1]</td><td>String</td><td>petdatas[0].iconname[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].iconname[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].iconname[2]</td><td>String</td><td>petdatas[0].iconname[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].costsilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].desc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[0].desc</td><td>String</td><td>petdatas[0].desc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[0].isopen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].daotype</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].petname.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].petname[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].petname[0]</td><td>String</td><td>petdatas[1].petname[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].petname[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].petname[1]</td><td>String</td><td>petdatas[1].petname[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].petname[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].petname[2]</td><td>String</td><td>petdatas[1].petname[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].petnames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].petnames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].petnames[0]</td><td>String</td><td>petdatas[1].petnames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].petnames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].petnames[1]</td><td>String</td><td>petdatas[1].petnames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].petnames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].petnames[2]</td><td>String</td><td>petdatas[1].petnames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].iconname.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].iconname[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].iconname[0]</td><td>String</td><td>petdatas[1].iconname[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].iconname[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].iconname[1]</td><td>String</td><td>petdatas[1].iconname[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].iconname[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].iconname[2]</td><td>String</td><td>petdatas[1].iconname[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].costsilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].desc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[1].desc</td><td>String</td><td>petdatas[1].desc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[1].isopen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].daotype</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].petname.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].petname[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].petname[0]</td><td>String</td><td>petdatas[2].petname[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].petname[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].petname[1]</td><td>String</td><td>petdatas[2].petname[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].petname[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].petname[2]</td><td>String</td><td>petdatas[2].petname[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].petnames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].petnames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].petnames[0]</td><td>String</td><td>petdatas[2].petnames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].petnames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].petnames[1]</td><td>String</td><td>petdatas[2].petnames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].petnames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].petnames[2]</td><td>String</td><td>petdatas[2].petnames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].iconname.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].iconname[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].iconname[0]</td><td>String</td><td>petdatas[2].iconname[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].iconname[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].iconname[1]</td><td>String</td><td>petdatas[2].iconname[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].iconname[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].iconname[2]</td><td>String</td><td>petdatas[2].iconname[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].costsilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].desc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petdatas[2].desc</td><td>String</td><td>petdatas[2].desc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petdatas[2].isopen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class PET_DAO_DATA_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	PetData[] petdatas;

	public PET_DAO_DATA_RES(){
	}

	public PET_DAO_DATA_RES(long seqNum,PetData[] petdatas){
		this.seqNum = seqNum;
		this.petdatas = petdatas;
	}

	public PET_DAO_DATA_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		petdatas = new PetData[len];
		for(int i = 0 ; i < petdatas.length ; i++){
			petdatas[i] = new PetData();
			petdatas[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			petdatas[i].setDaotype((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] petname_0001 = new String[len];
			for(int j = 0 ; j < petname_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				petname_0001[j] = new String(content,offset,len);
				offset += len;
			}
			petdatas[i].setPetname(petname_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] petnames_0002 = new String[len];
			for(int j = 0 ; j < petnames_0002.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				petnames_0002[j] = new String(content,offset,len);
				offset += len;
			}
			petdatas[i].setPetnames(petnames_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] iconname_0003 = new String[len];
			for(int j = 0 ; j < iconname_0003.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				iconname_0003[j] = new String(content,offset,len);
				offset += len;
			}
			petdatas[i].setIconname(iconname_0003);
			petdatas[i].setCostsilver((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			petdatas[i].setDesc(new String(content,offset,len));
			offset += len;
			petdatas[i].setIsopen(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
		}
	}

	public int getType() {
		return 0x8E0EAA06;
	}

	public String getTypeDescription() {
		return "PET_DAO_DATA_RES";
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
		for(int i = 0 ; i < petdatas.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			String[] petname = petdatas[i].getPetname();
			for(int j = 0 ; j < petname.length; j++){
				len += 2;
				len += petname[j].getBytes().length;
			}
			len += 4;
			String[] petnames = petdatas[i].getPetnames();
			for(int j = 0 ; j < petnames.length; j++){
				len += 2;
				len += petnames[j].getBytes().length;
			}
			len += 4;
			String[] iconname = petdatas[i].getIconname();
			for(int j = 0 ; j < iconname.length; j++){
				len += 2;
				len += iconname[j].getBytes().length;
			}
			len += 8;
			len += 2;
			if(petdatas[i].getDesc() != null){
				len += petdatas[i].getDesc().getBytes().length;
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

			buffer.putInt(petdatas.length);
			for(int i = 0 ; i < petdatas.length ; i++){
				buffer.putInt((int)petdatas[i].getLevel());
				buffer.putInt((int)petdatas[i].getDaotype());
				buffer.putInt(petdatas[i].getPetname().length);
				String[] petname_0004 = petdatas[i].getPetname();
				for(int j = 0 ; j < petname_0004.length ; j++){
					buffer.putShort((short)petname_0004[j].getBytes().length);
					buffer.put(petname_0004[j].getBytes());
				}
				buffer.putInt(petdatas[i].getPetnames().length);
				String[] petnames_0005 = petdatas[i].getPetnames();
				for(int j = 0 ; j < petnames_0005.length ; j++){
					buffer.putShort((short)petnames_0005[j].getBytes().length);
					buffer.put(petnames_0005[j].getBytes());
				}
				buffer.putInt(petdatas[i].getIconname().length);
				String[] iconname_0006 = petdatas[i].getIconname();
				for(int j = 0 ; j < iconname_0006.length ; j++){
					buffer.putShort((short)iconname_0006[j].getBytes().length);
					buffer.put(iconname_0006[j].getBytes());
				}
				buffer.putLong(petdatas[i].getCostsilver());
				byte[] tmpBytes2 ;
				tmpBytes2 = petdatas[i].getDesc().getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(petdatas[i].isIsopen()==false?0:1));
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
	public PetData[] getPetdatas(){
		return petdatas;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPetdatas(PetData[] petdatas){
		this.petdatas = petdatas;
	}

}