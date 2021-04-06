package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看家族建筑信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients.length</td><td>int</td><td>4个字节</td><td>SeptBuildingEntity4Client数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].npcId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].npcName</td><td>String</td><td>septBuildingEntity4Clients[0].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].inBuild</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].buildType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].index</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].des</td><td>String</td><td>septBuildingEntity4Clients[0].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].icon</td><td>String</td><td>septBuildingEntity4Clients[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].depend.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].depend</td><td>String</td><td>septBuildingEntity4Clients[0].depend.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].dependType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].dependGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].lvUpCostSpirit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].lvUpCostCoin</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].dailyMaintainCost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].lvupMaintainCost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].currAddProsper</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[0].lvUpAddProsper</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[0].releaseTaskNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].npcId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].npcName</td><td>String</td><td>septBuildingEntity4Clients[1].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].inBuild</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].buildType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].index</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].des</td><td>String</td><td>septBuildingEntity4Clients[1].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].icon</td><td>String</td><td>septBuildingEntity4Clients[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].depend.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].depend</td><td>String</td><td>septBuildingEntity4Clients[1].depend.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].dependType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].dependGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].lvUpCostSpirit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].lvUpCostCoin</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].dailyMaintainCost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].lvupMaintainCost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].currAddProsper</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[1].lvUpAddProsper</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[1].releaseTaskNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].npcId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].npcName</td><td>String</td><td>septBuildingEntity4Clients[2].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].inBuild</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].buildType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].index</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].des</td><td>String</td><td>septBuildingEntity4Clients[2].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].icon</td><td>String</td><td>septBuildingEntity4Clients[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].depend.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].depend</td><td>String</td><td>septBuildingEntity4Clients[2].depend.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].dependType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].dependGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].lvUpCostSpirit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].lvUpCostCoin</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].dailyMaintainCost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].lvupMaintainCost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].currAddProsper</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septBuildingEntity4Clients[2].lvUpAddProsper</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>septBuildingEntity4Clients[2].releaseTaskNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SEPTBUILDING_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	SeptBuildingEntity4Client[] septBuildingEntity4Clients;

	public SEPTBUILDING_INFO_RES(){
	}

	public SEPTBUILDING_INFO_RES(long seqNum,SeptBuildingEntity4Client[] septBuildingEntity4Clients){
		this.seqNum = seqNum;
		this.septBuildingEntity4Clients = septBuildingEntity4Clients;
	}

	public SEPTBUILDING_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		septBuildingEntity4Clients = new SeptBuildingEntity4Client[len];
		for(int i = 0 ; i < septBuildingEntity4Clients.length ; i++){
			septBuildingEntity4Clients[i] = new SeptBuildingEntity4Client();
			septBuildingEntity4Clients[i].setNpcId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			septBuildingEntity4Clients[i].setNpcName(new String(content,offset,len,"UTF-8"));
			offset += len;
			septBuildingEntity4Clients[i].setGrade((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setInBuild(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			septBuildingEntity4Clients[i].setBuildType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setIndex((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			septBuildingEntity4Clients[i].setDes(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			septBuildingEntity4Clients[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			septBuildingEntity4Clients[i].setDepend(new String(content,offset,len,"UTF-8"));
			offset += len;
			septBuildingEntity4Clients[i].setDependType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setDependGrade((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setLvUpCostSpirit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setLvUpCostCoin((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setDailyMaintainCost((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setLvupMaintainCost((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setCurrAddProsper((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setLvUpAddProsper((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			septBuildingEntity4Clients[i].setReleaseTaskNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x700EEE09;
	}

	public String getTypeDescription() {
		return "SEPTBUILDING_INFO_RES";
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
		for(int i = 0 ; i < septBuildingEntity4Clients.length ; i++){
			len += 8;
			len += 2;
			if(septBuildingEntity4Clients[i].getNpcName() != null){
				try{
				len += septBuildingEntity4Clients[i].getNpcName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(septBuildingEntity4Clients[i].getDes() != null){
				try{
				len += septBuildingEntity4Clients[i].getDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(septBuildingEntity4Clients[i].getIcon() != null){
				try{
				len += septBuildingEntity4Clients[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(septBuildingEntity4Clients[i].getDepend() != null){
				try{
				len += septBuildingEntity4Clients[i].getDepend().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
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

			buffer.putInt(septBuildingEntity4Clients.length);
			for(int i = 0 ; i < septBuildingEntity4Clients.length ; i++){
				buffer.putLong(septBuildingEntity4Clients[i].getNpcId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = septBuildingEntity4Clients[i].getNpcName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)septBuildingEntity4Clients[i].getGrade());
				buffer.put((byte)(septBuildingEntity4Clients[i].isInBuild()==false?0:1));
				buffer.putInt((int)septBuildingEntity4Clients[i].getBuildType());
				buffer.putInt((int)septBuildingEntity4Clients[i].getIndex());
				buffer.putInt((int)septBuildingEntity4Clients[i].getX());
				buffer.putInt((int)septBuildingEntity4Clients[i].getY());
				try{
				tmpBytes2 = septBuildingEntity4Clients[i].getDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = septBuildingEntity4Clients[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = septBuildingEntity4Clients[i].getDepend().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)septBuildingEntity4Clients[i].getDependType());
				buffer.putInt((int)septBuildingEntity4Clients[i].getDependGrade());
				buffer.putInt((int)septBuildingEntity4Clients[i].getLvUpCostSpirit());
				buffer.putInt((int)septBuildingEntity4Clients[i].getLvUpCostCoin());
				buffer.putInt((int)septBuildingEntity4Clients[i].getDailyMaintainCost());
				buffer.putInt((int)septBuildingEntity4Clients[i].getLvupMaintainCost());
				buffer.putInt((int)septBuildingEntity4Clients[i].getCurrAddProsper());
				buffer.putInt((int)septBuildingEntity4Clients[i].getLvUpAddProsper());
				buffer.putInt((int)septBuildingEntity4Clients[i].getReleaseTaskNum());
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
	 *	家族建筑信息
	 */
	public SeptBuildingEntity4Client[] getSeptBuildingEntity4Clients(){
		return septBuildingEntity4Clients;
	}

	/**
	 * 设置属性：
	 *	家族建筑信息
	 */
	public void setSeptBuildingEntity4Clients(SeptBuildingEntity4Client[] septBuildingEntity4Clients){
		this.septBuildingEntity4Clients = septBuildingEntity4Clients;
	}

}