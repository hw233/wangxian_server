package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.career.SkillInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 因为上面那条协议太长发不过去新加的只发心法协议<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].name</td><td>String</td><td>xinfaSkills[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].iconId</td><td>String</td><td>xinfaSkills[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].description</td><td>String</td><td>xinfaSkills[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needMoney</td><td>int[]</td><td>xinfaSkills[0].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needYuanQi</td><td>int[]</td><td>xinfaSkills[0].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needLongExp</td><td>long[]</td><td>xinfaSkills[0].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needPoint</td><td>int[]</td><td>xinfaSkills[0].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needPlayerLevel</td><td>int[]</td><td>xinfaSkills[0].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].name</td><td>String</td><td>xinfaSkills[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].iconId</td><td>String</td><td>xinfaSkills[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].description</td><td>String</td><td>xinfaSkills[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needMoney</td><td>int[]</td><td>xinfaSkills[1].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needYuanQi</td><td>int[]</td><td>xinfaSkills[1].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needLongExp</td><td>long[]</td><td>xinfaSkills[1].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needPoint</td><td>int[]</td><td>xinfaSkills[1].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needPlayerLevel</td><td>int[]</td><td>xinfaSkills[1].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].name</td><td>String</td><td>xinfaSkills[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].iconId</td><td>String</td><td>xinfaSkills[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].description</td><td>String</td><td>xinfaSkills[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needMoney</td><td>int[]</td><td>xinfaSkills[2].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needYuanQi</td><td>int[]</td><td>xinfaSkills[2].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needLongExp</td><td>long[]</td><td>xinfaSkills[2].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needPoint</td><td>int[]</td><td>xinfaSkills[2].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needPlayerLevel</td><td>int[]</td><td>xinfaSkills[2].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_CAREER_XINFA_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	SkillInfo[] xinfaSkills;

	public QUERY_CAREER_XINFA_INFO_RES(){
	}

	public QUERY_CAREER_XINFA_INFO_RES(long seqNum,SkillInfo[] xinfaSkills){
		this.seqNum = seqNum;
		this.xinfaSkills = xinfaSkills;
	}

	public QUERY_CAREER_XINFA_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		xinfaSkills = new SkillInfo[len];
		for(int i = 0 ; i < xinfaSkills.length ; i++){
			xinfaSkills[i] = new SkillInfo();
			xinfaSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			xinfaSkills[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			xinfaSkills[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			xinfaSkills[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			xinfaSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			xinfaSkills[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needMoney_0001 = new int[len];
			for(int j = 0 ; j < needMoney_0001.length ; j++){
				needMoney_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			xinfaSkills[i].setNeedMoney(needMoney_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needYuanQi_0002 = new int[len];
			for(int j = 0 ; j < needYuanQi_0002.length ; j++){
				needYuanQi_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			xinfaSkills[i].setNeedYuanQi(needYuanQi_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] needLongExp_0003 = new long[len];
			for(int j = 0 ; j < needLongExp_0003.length ; j++){
				needLongExp_0003[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			xinfaSkills[i].setNeedLongExp(needLongExp_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPoint_0004 = new int[len];
			for(int j = 0 ; j < needPoint_0004.length ; j++){
				needPoint_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			xinfaSkills[i].setNeedPoint(needPoint_0004);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPlayerLevel_0005 = new int[len];
			for(int j = 0 ; j < needPlayerLevel_0005.length ; j++){
				needPlayerLevel_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			xinfaSkills[i].setNeedPlayerLevel(needPlayerLevel_0005);
		}
	}

	public int getType() {
		return 0x700000EA;
	}

	public String getTypeDescription() {
		return "QUERY_CAREER_XINFA_INFO_RES";
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
		for(int i = 0 ; i < xinfaSkills.length ; i++){
			len += 4;
			len += 2;
			if(xinfaSkills[i].getName() != null){
				try{
				len += xinfaSkills[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(xinfaSkills[i].getIconId() != null){
				try{
				len += xinfaSkills[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += 2;
			if(xinfaSkills[i].getDescription() != null){
				try{
				len += xinfaSkills[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += xinfaSkills[i].getNeedMoney().length * 4;
			len += 4;
			len += xinfaSkills[i].getNeedYuanQi().length * 4;
			len += 4;
			len += xinfaSkills[i].getNeedLongExp().length * 8;
			len += 4;
			len += xinfaSkills[i].getNeedPoint().length * 4;
			len += 4;
			len += xinfaSkills[i].getNeedPlayerLevel().length * 4;
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

			buffer.putInt(xinfaSkills.length);
			for(int i = 0 ; i < xinfaSkills.length ; i++){
				buffer.putInt((int)xinfaSkills[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = xinfaSkills[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = xinfaSkills[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)xinfaSkills[i].getSkillType());
				buffer.putInt((int)xinfaSkills[i].getMaxLevel());
				try{
				tmpBytes2 = xinfaSkills[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(xinfaSkills[i].getNeedMoney().length);
				int[] needMoney_0006 = xinfaSkills[i].getNeedMoney();
				for(int j = 0 ; j < needMoney_0006.length ; j++){
					buffer.putInt(needMoney_0006[j]);
				}
				buffer.putInt(xinfaSkills[i].getNeedYuanQi().length);
				int[] needYuanQi_0007 = xinfaSkills[i].getNeedYuanQi();
				for(int j = 0 ; j < needYuanQi_0007.length ; j++){
					buffer.putInt(needYuanQi_0007[j]);
				}
				buffer.putInt(xinfaSkills[i].getNeedLongExp().length);
				long[] needLongExp_0008 = xinfaSkills[i].getNeedLongExp();
				for(int j = 0 ; j < needLongExp_0008.length ; j++){
					buffer.putLong(needLongExp_0008[j]);
				}
				buffer.putInt(xinfaSkills[i].getNeedPoint().length);
				int[] needPoint_0009 = xinfaSkills[i].getNeedPoint();
				for(int j = 0 ; j < needPoint_0009.length ; j++){
					buffer.putInt(needPoint_0009[j]);
				}
				buffer.putInt(xinfaSkills[i].getNeedPlayerLevel().length);
				int[] needPlayerLevel_0010 = xinfaSkills[i].getNeedPlayerLevel();
				for(int j = 0 ; j < needPlayerLevel_0010.length ; j++){
					buffer.putInt(needPlayerLevel_0010[j]);
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
	public SkillInfo[] getXinfaSkills(){
		return xinfaSkills;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setXinfaSkills(SkillInfo[] xinfaSkills){
		this.xinfaSkills = xinfaSkills;
	}

}