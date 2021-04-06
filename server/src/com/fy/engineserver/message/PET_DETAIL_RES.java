package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.pet2.GradePet;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.progName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.progName</td><td>String</td><td>petDetail.progName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.name</td><td>String</td><td>petDetail.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.maxGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.lv4Avatar.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.lv4Avatar</td><td>String</td><td>petDetail.lv4Avatar.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.lv7Avatar.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.lv7Avatar</td><td>String</td><td>petDetail.lv7Avatar.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.jiChuJiNengDesc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.jiChuJiNengDesc</td><td>String</td><td>petDetail.jiChuJiNengDesc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.tianFuJiNengDesc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.tianFuJiNengDesc</td><td>String</td><td>petDetail.tianFuJiNengDesc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.gainFrom.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.gainFrom</td><td>String</td><td>petDetail.gainFrom.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.bornSkill.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.bornSkill</td><td>int[]</td><td>petDetail.bornSkill.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.icons.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.icons[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.icons[0]</td><td>String</td><td>petDetail.icons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.icons[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.icons[1]</td><td>String</td><td>petDetail.icons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.icons[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.icons[2]</td><td>String</td><td>petDetail.icons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.skDesc.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.skDesc[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.skDesc[0]</td><td>String</td><td>petDetail.skDesc[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.skDesc[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.skDesc[1]</td><td>String</td><td>petDetail.skDesc[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.skDesc[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.skDesc[2]</td><td>String</td><td>petDetail.skDesc[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.baseAvatar.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.baseAvatar</td><td>String</td><td>petDetail.baseAvatar.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.character</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.takeLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.rarity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.minValues.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.minValues</td><td>int[]</td><td>petDetail.minValues.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.maxValues.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.maxValues</td><td>int[]</td><td>petDetail.maxValues.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partBody.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.partBody[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partBody[0]</td><td>String</td><td>petDetail.partBody[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.partBody[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partBody[1]</td><td>String</td><td>petDetail.partBody[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.partBody[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partBody[2]</td><td>String</td><td>petDetail.partBody[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partBodyY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.partBodyY</td><td>int[]</td><td>petDetail.partBodyY.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partFoot.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.partFoot[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partFoot[0]</td><td>String</td><td>petDetail.partFoot[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.partFoot[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partFoot[1]</td><td>String</td><td>petDetail.partFoot[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.partFoot[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partFoot[2]</td><td>String</td><td>petDetail.partFoot[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petDetail.partFootY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petDetail.partFootY</td><td>int[]</td><td>petDetail.partFootY.length</td><td>*</td></tr>
 * </table>
 */
public class PET_DETAIL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	GradePet petDetail;

	public PET_DETAIL_RES(){
	}

	public PET_DETAIL_RES(long seqNum,GradePet petDetail){
		this.seqNum = seqNum;
		this.petDetail = petDetail;
	}

	public PET_DETAIL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		petDetail = new GradePet();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		petDetail.setProgName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		petDetail.setName(new String(content,offset,len));
		offset += len;
		petDetail.setMaxGrade((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		petDetail.setLv4Avatar(new String(content,offset,len));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		petDetail.setLv7Avatar(new String(content,offset,len));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		petDetail.setJiChuJiNengDesc(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		petDetail.setTianFuJiNengDesc(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		petDetail.setGainFrom(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] bornSkill_0001 = new int[len];
		for(int j = 0 ; j < bornSkill_0001.length ; j++){
			bornSkill_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		petDetail.setBornSkill(bornSkill_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] icons_0002 = new String[len];
		for(int j = 0 ; j < icons_0002.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			icons_0002[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		petDetail.setIcons(icons_0002);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] skDesc_0003 = new String[len];
		for(int j = 0 ; j < skDesc_0003.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skDesc_0003[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		petDetail.setSkDesc(skDesc_0003);
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		petDetail.setBaseAvatar(new String(content,offset,len,"UTF-8"));
		offset += len;
		petDetail.setCharacter((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		petDetail.setTakeLevel((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		petDetail.setRarity((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] minValues_0004 = new int[len];
		for(int j = 0 ; j < minValues_0004.length ; j++){
			minValues_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		petDetail.setMinValues(minValues_0004);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] maxValues_0005 = new int[len];
		for(int j = 0 ; j < maxValues_0005.length ; j++){
			maxValues_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		petDetail.setMaxValues(maxValues_0005);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] partBody_0006 = new String[len];
		for(int j = 0 ; j < partBody_0006.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			partBody_0006[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		petDetail.setPartBody(partBody_0006);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] partBodyY_0007 = new int[len];
		for(int j = 0 ; j < partBodyY_0007.length ; j++){
			partBodyY_0007[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		petDetail.setPartBodyY(partBodyY_0007);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] partFoot_0008 = new String[len];
		for(int j = 0 ; j < partFoot_0008.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			partFoot_0008[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		petDetail.setPartFoot(partFoot_0008);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] partFootY_0009 = new int[len];
		for(int j = 0 ; j < partFootY_0009.length ; j++){
			partFootY_0009[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		petDetail.setPartFootY(partFootY_0009);
	}

	public int getType() {
		return 0x8E0EAA56;
	}

	public String getTypeDescription() {
		return "PET_DETAIL_RES";
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
		if(petDetail.getProgName() != null){
			try{
			len += petDetail.getProgName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(petDetail.getName() != null){
			len += petDetail.getName().getBytes().length;
		}
		len += 4;
		len += 2;
		if(petDetail.getLv4Avatar() != null){
			len += petDetail.getLv4Avatar().getBytes().length;
		}
		len += 2;
		if(petDetail.getLv7Avatar() != null){
			len += petDetail.getLv7Avatar().getBytes().length;
		}
		len += 2;
		if(petDetail.getJiChuJiNengDesc() != null){
			try{
			len += petDetail.getJiChuJiNengDesc().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(petDetail.getTianFuJiNengDesc() != null){
			try{
			len += petDetail.getTianFuJiNengDesc().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(petDetail.getGainFrom() != null){
			try{
			len += petDetail.getGainFrom().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += petDetail.getBornSkill().length * 4;
		len += 4;
		String[] icons = petDetail.getIcons();
		for(int j = 0 ; j < icons.length; j++){
			len += 2;
			try{
				len += icons[j].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		String[] skDesc = petDetail.getSkDesc();
		for(int j = 0 ; j < skDesc.length; j++){
			len += 2;
			try{
				len += skDesc[j].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(petDetail.getBaseAvatar() != null){
			try{
			len += petDetail.getBaseAvatar().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += petDetail.getMinValues().length * 4;
		len += 4;
		len += petDetail.getMaxValues().length * 4;
		len += 4;
		String[] partBody = petDetail.getPartBody();
		for(int j = 0 ; j < partBody.length; j++){
			len += 2;
			try{
				len += partBody[j].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += petDetail.getPartBodyY().length * 4;
		len += 4;
		String[] partFoot = petDetail.getPartFoot();
		for(int j = 0 ; j < partFoot.length; j++){
			len += 2;
			try{
				len += partFoot[j].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += petDetail.getPartFootY().length * 4;
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

			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = petDetail.getProgName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = petDetail.getName().getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)petDetail.getMaxGrade());
			tmpBytes1 = petDetail.getLv4Avatar().getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = petDetail.getLv7Avatar().getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = petDetail.getJiChuJiNengDesc().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = petDetail.getTianFuJiNengDesc().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = petDetail.getGainFrom().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(petDetail.getBornSkill().length);
			int[] bornSkill_0010 = petDetail.getBornSkill();
			for(int j = 0 ; j < bornSkill_0010.length ; j++){
				buffer.putInt(bornSkill_0010[j]);
			}
			buffer.putInt(petDetail.getIcons().length);
			String[] icons_0011 = petDetail.getIcons();
			for(int j = 0 ; j < icons_0011.length ; j++){
				try{
				buffer.putShort((short)icons_0011[j].getBytes("UTF-8").length);
				buffer.put(icons_0011[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			buffer.putInt(petDetail.getSkDesc().length);
			String[] skDesc_0012 = petDetail.getSkDesc();
			for(int j = 0 ; j < skDesc_0012.length ; j++){
				try{
				buffer.putShort((short)skDesc_0012[j].getBytes("UTF-8").length);
				buffer.put(skDesc_0012[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
				try{
				tmpBytes1 = petDetail.getBaseAvatar().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)petDetail.getCharacter());
			buffer.putInt((int)petDetail.getTakeLevel());
			buffer.putInt((int)petDetail.getRarity());
			buffer.putInt(petDetail.getMinValues().length);
			int[] minValues_0013 = petDetail.getMinValues();
			for(int j = 0 ; j < minValues_0013.length ; j++){
				buffer.putInt(minValues_0013[j]);
			}
			buffer.putInt(petDetail.getMaxValues().length);
			int[] maxValues_0014 = petDetail.getMaxValues();
			for(int j = 0 ; j < maxValues_0014.length ; j++){
				buffer.putInt(maxValues_0014[j]);
			}
			buffer.putInt(petDetail.getPartBody().length);
			String[] partBody_0015 = petDetail.getPartBody();
			for(int j = 0 ; j < partBody_0015.length ; j++){
				try{
				buffer.putShort((short)partBody_0015[j].getBytes("UTF-8").length);
				buffer.put(partBody_0015[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			buffer.putInt(petDetail.getPartBodyY().length);
			int[] partBodyY_0016 = petDetail.getPartBodyY();
			for(int j = 0 ; j < partBodyY_0016.length ; j++){
				buffer.putInt(partBodyY_0016[j]);
			}
			buffer.putInt(petDetail.getPartFoot().length);
			String[] partFoot_0017 = petDetail.getPartFoot();
			for(int j = 0 ; j < partFoot_0017.length ; j++){
				try{
				buffer.putShort((short)partFoot_0017[j].getBytes("UTF-8").length);
				buffer.put(partFoot_0017[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			buffer.putInt(petDetail.getPartFootY().length);
			int[] partFootY_0018 = petDetail.getPartFootY();
			for(int j = 0 ; j < partFootY_0018.length ; j++){
				buffer.putInt(partFootY_0018[j]);
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
	public GradePet getPetDetail(){
		return petDetail;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPetDetail(GradePet petDetail){
		this.petDetail = petDetail;
	}

}