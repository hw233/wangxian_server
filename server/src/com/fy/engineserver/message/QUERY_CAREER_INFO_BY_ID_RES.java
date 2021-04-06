package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.career.SkillInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询门派信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.name</td><td>String</td><td>querySkill.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.iconId</td><td>String</td><td>querySkill.iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.description</td><td>String</td><td>querySkill.description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.needMoney</td><td>int[]</td><td>querySkill.needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.needYuanQi</td><td>int[]</td><td>querySkill.needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.needExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.needExp</td><td>int[]</td><td>querySkill.needExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.needPoint</td><td>int[]</td><td>querySkill.needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>querySkill.needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>querySkill.needPlayerLevel</td><td>int[]</td><td>querySkill.needPlayerLevel.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_CAREER_INFO_BY_ID_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	SkillInfo querySkill;

	public QUERY_CAREER_INFO_BY_ID_RES(){
	}

	public QUERY_CAREER_INFO_BY_ID_RES(long seqNum,SkillInfo querySkill){
		this.seqNum = seqNum;
		this.querySkill = querySkill;
	}

	public QUERY_CAREER_INFO_BY_ID_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		querySkill = new SkillInfo();
		querySkill.setId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		querySkill.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		querySkill.setIconId(new String(content,offset,len,"UTF-8"));
		offset += len;
		querySkill.setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		querySkill.setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		querySkill.setDescription(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] needMoney_0001 = new int[len];
		for(int j = 0 ; j < needMoney_0001.length ; j++){
			needMoney_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		querySkill.setNeedMoney(needMoney_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] needYuanQi_0002 = new int[len];
		for(int j = 0 ; j < needYuanQi_0002.length ; j++){
			needYuanQi_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		querySkill.setNeedYuanQi(needYuanQi_0002);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] needExp_0003 = new int[len];
		for(int j = 0 ; j < needExp_0003.length ; j++){
			needExp_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		querySkill.setNeedExp(needExp_0003);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] needPoint_0004 = new int[len];
		for(int j = 0 ; j < needPoint_0004.length ; j++){
			needPoint_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		querySkill.setNeedPoint(needPoint_0004);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] needPlayerLevel_0005 = new int[len];
		for(int j = 0 ; j < needPlayerLevel_0005.length ; j++){
			needPlayerLevel_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		querySkill.setNeedPlayerLevel(needPlayerLevel_0005);
	}

	public int getType() {
		return 0x700001E6;
	}

	public String getTypeDescription() {
		return "QUERY_CAREER_INFO_BY_ID_RES";
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
		len += 2;
		if(querySkill.getName() != null){
			try{
			len += querySkill.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(querySkill.getIconId() != null){
			try{
			len += querySkill.getIconId().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 1;
		len += 4;
		len += 2;
		if(querySkill.getDescription() != null){
			try{
			len += querySkill.getDescription().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += querySkill.getNeedMoney().length * 4;
		len += 4;
		len += querySkill.getNeedYuanQi().length * 4;
		len += 4;
		len += querySkill.getNeedExp().length * 4;
		len += 4;
		len += querySkill.getNeedPoint().length * 4;
		len += 4;
		len += querySkill.getNeedPlayerLevel().length * 4;
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

			buffer.putInt((int)querySkill.getId());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = querySkill.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = querySkill.getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)querySkill.getSkillType());
			buffer.putInt((int)querySkill.getMaxLevel());
				try{
				tmpBytes1 = querySkill.getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(querySkill.getNeedMoney().length);
			int[] needMoney_0006 = querySkill.getNeedMoney();
			for(int j = 0 ; j < needMoney_0006.length ; j++){
				buffer.putInt(needMoney_0006[j]);
			}
			buffer.putInt(querySkill.getNeedYuanQi().length);
			int[] needYuanQi_0007 = querySkill.getNeedYuanQi();
			for(int j = 0 ; j < needYuanQi_0007.length ; j++){
				buffer.putInt(needYuanQi_0007[j]);
			}
			buffer.putInt(querySkill.getNeedExp().length);
			int[] needExp_0008 = querySkill.getNeedExp();
			for(int j = 0 ; j < needExp_0008.length ; j++){
				buffer.putInt(needExp_0008[j]);
			}
			buffer.putInt(querySkill.getNeedPoint().length);
			int[] needPoint_0009 = querySkill.getNeedPoint();
			for(int j = 0 ; j < needPoint_0009.length ; j++){
				buffer.putInt(needPoint_0009[j]);
			}
			buffer.putInt(querySkill.getNeedPlayerLevel().length);
			int[] needPlayerLevel_0010 = querySkill.getNeedPlayerLevel();
			for(int j = 0 ; j < needPlayerLevel_0010.length ; j++){
				buffer.putInt(needPlayerLevel_0010[j]);
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
	public SkillInfo getQuerySkill(){
		return querySkill;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setQuerySkill(SkillInfo querySkill){
		this.querySkill = querySkill;
	}

}