package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.jiazu2.model.JiazuSkillModel;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 家族技能信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xiulianZhi</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buildType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills.length</td><td>int</td><td>4个字节</td><td>JiazuSkillModel数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[0].skillId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[0].skillName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[0].skillName</td><td>String</td><td>jiazuSkills[0].skillName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[0].icon</td><td>String</td><td>jiazuSkills[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[0].currentLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[0].needExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[0].currentExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[0].costType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[0].costNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[0].currentLevelDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[0].currentLevelDes</td><td>String</td><td>jiazuSkills[0].currentLevelDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[1].skillId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[1].skillName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[1].skillName</td><td>String</td><td>jiazuSkills[1].skillName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[1].icon</td><td>String</td><td>jiazuSkills[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[1].currentLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[1].needExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[1].currentExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[1].costType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[1].costNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[1].currentLevelDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[1].currentLevelDes</td><td>String</td><td>jiazuSkills[1].currentLevelDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[2].skillId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[2].skillName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[2].skillName</td><td>String</td><td>jiazuSkills[2].skillName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[2].icon</td><td>String</td><td>jiazuSkills[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[2].currentLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[2].needExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[2].currentExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[2].costType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[2].costNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuSkills[2].currentLevelDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuSkills[2].currentLevelDes</td><td>String</td><td>jiazuSkills[2].currentLevelDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costXiulianNum</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class JIAZU_JINENG_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long xiulianZhi;
	byte buildType;
	JiazuSkillModel[] jiazuSkills;
	long costXiulianNum;

	public JIAZU_JINENG_INFO_RES(){
	}

	public JIAZU_JINENG_INFO_RES(long seqNum,long xiulianZhi,byte buildType,JiazuSkillModel[] jiazuSkills,long costXiulianNum){
		this.seqNum = seqNum;
		this.xiulianZhi = xiulianZhi;
		this.buildType = buildType;
		this.jiazuSkills = jiazuSkills;
		this.costXiulianNum = costXiulianNum;
	}

	public JIAZU_JINENG_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		xiulianZhi = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		buildType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		jiazuSkills = new JiazuSkillModel[len];
		for(int i = 0 ; i < jiazuSkills.length ; i++){
			jiazuSkills[i] = new JiazuSkillModel();
			jiazuSkills[i].setSkillId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuSkills[i].setSkillName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuSkills[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			jiazuSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuSkills[i].setCurrentLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuSkills[i].setNeedExp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuSkills[i].setCurrentExp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuSkills[i].setCostType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuSkills[i].setCostNum((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuSkills[i].setCurrentLevelDes(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		costXiulianNum = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70FF0037;
	}

	public String getTypeDescription() {
		return "JIAZU_JINENG_INFO_RES";
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
		len += 8;
		len += 1;
		len += 4;
		for(int i = 0 ; i < jiazuSkills.length ; i++){
			len += 4;
			len += 2;
			if(jiazuSkills[i].getSkillName() != null){
				try{
				len += jiazuSkills[i].getSkillName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(jiazuSkills[i].getIcon() != null){
				try{
				len += jiazuSkills[i].getIcon().getBytes("UTF-8").length;
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
			len += 8;
			len += 2;
			if(jiazuSkills[i].getCurrentLevelDes() != null){
				try{
				len += jiazuSkills[i].getCurrentLevelDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 8;
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

			buffer.putLong(xiulianZhi);
			buffer.put(buildType);
			buffer.putInt(jiazuSkills.length);
			for(int i = 0 ; i < jiazuSkills.length ; i++){
				buffer.putInt((int)jiazuSkills[i].getSkillId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = jiazuSkills[i].getSkillName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = jiazuSkills[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)jiazuSkills[i].getMaxLevel());
				buffer.putInt((int)jiazuSkills[i].getCurrentLevel());
				buffer.putInt((int)jiazuSkills[i].getNeedExp());
				buffer.putInt((int)jiazuSkills[i].getCurrentExp());
				buffer.putInt((int)jiazuSkills[i].getCostType());
				buffer.putLong(jiazuSkills[i].getCostNum());
				try{
				tmpBytes2 = jiazuSkills[i].getCurrentLevelDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putLong(costXiulianNum);
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
	 *	修炼值
	 */
	public long getXiulianZhi(){
		return xiulianZhi;
	}

	/**
	 * 设置属性：
	 *	修炼值
	 */
	public void setXiulianZhi(long xiulianZhi){
		this.xiulianZhi = xiulianZhi;
	}

	/**
	 * 获取属性：
	 *	0防具坊  1武器坊
	 */
	public byte getBuildType(){
		return buildType;
	}

	/**
	 * 设置属性：
	 *	0防具坊  1武器坊
	 */
	public void setBuildType(byte buildType){
		this.buildType = buildType;
	}

	/**
	 * 获取属性：
	 *	家族技能信息
	 */
	public JiazuSkillModel[] getJiazuSkills(){
		return jiazuSkills;
	}

	/**
	 * 设置属性：
	 *	家族技能信息
	 */
	public void setJiazuSkills(JiazuSkillModel[] jiazuSkills){
		this.jiazuSkills = jiazuSkills;
	}

	/**
	 * 获取属性：
	 *	每次消耗修炼值
	 */
	public long getCostXiulianNum(){
		return costXiulianNum;
	}

	/**
	 * 设置属性：
	 *	每次消耗修炼值
	 */
	public void setCostXiulianNum(long costXiulianNum){
		this.costXiulianNum = costXiulianNum;
	}

}