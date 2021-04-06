package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 刷新坐骑技能<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillName</td><td>String</td><td>skillName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIcon</td><td>String</td><td>skillIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>free4Skill</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>particleIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNum1</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNum2</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class REFRESH_HORSE_SKILL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long horseId;
	int skillId;
	String skillName;
	String skillIcon;
	int free4Skill;
	int particleIndex;
	int articleNum1;
	int articleNum2;

	public REFRESH_HORSE_SKILL_RES(){
	}

	public REFRESH_HORSE_SKILL_RES(long seqNum,long horseId,int skillId,String skillName,String skillIcon,int free4Skill,int particleIndex,int articleNum1,int articleNum2){
		this.seqNum = seqNum;
		this.horseId = horseId;
		this.skillId = skillId;
		this.skillName = skillName;
		this.skillIcon = skillIcon;
		this.free4Skill = free4Skill;
		this.particleIndex = particleIndex;
		this.articleNum1 = articleNum1;
		this.articleNum2 = articleNum2;
	}

	public REFRESH_HORSE_SKILL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		horseId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		skillId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		skillName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		skillIcon = new String(content,offset,len,"UTF-8");
		offset += len;
		free4Skill = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		particleIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		articleNum1 = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		articleNum2 = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F0EF56;
	}

	public String getTypeDescription() {
		return "REFRESH_HORSE_SKILL_RES";
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
		len += 4;
		len += 2;
		try{
			len +=skillName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=skillIcon.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
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

			buffer.putLong(horseId);
			buffer.putInt(skillId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = skillName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = skillIcon.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(free4Skill);
			buffer.putInt(particleIndex);
			buffer.putInt(articleNum1);
			buffer.putInt(articleNum2);
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
	 *	坐骑id
	 */
	public long getHorseId(){
		return horseId;
	}

	/**
	 * 设置属性：
	 *	坐骑id
	 */
	public void setHorseId(long horseId){
		this.horseId = horseId;
	}

	/**
	 * 获取属性：
	 *	刷新出来的技能id
	 */
	public int getSkillId(){
		return skillId;
	}

	/**
	 * 设置属性：
	 *	刷新出来的技能id
	 */
	public void setSkillId(int skillId){
		this.skillId = skillId;
	}

	/**
	 * 获取属性：
	 *	刷新出来的技能名
	 */
	public String getSkillName(){
		return skillName;
	}

	/**
	 * 设置属性：
	 *	刷新出来的技能名
	 */
	public void setSkillName(String skillName){
		this.skillName = skillName;
	}

	/**
	 * 获取属性：
	 *	刷新出来的技能icon
	 */
	public String getSkillIcon(){
		return skillIcon;
	}

	/**
	 * 设置属性：
	 *	刷新出来的技能icon
	 */
	public void setSkillIcon(String skillIcon){
		this.skillIcon = skillIcon;
	}

	/**
	 * 获取属性：
	 *	免费刷新剩余次数
	 */
	public int getFree4Skill(){
		return free4Skill;
	}

	/**
	 * 设置属性：
	 *	免费刷新剩余次数
	 */
	public void setFree4Skill(int free4Skill){
		this.free4Skill = free4Skill;
	}

	/**
	 * 获取属性：
	 *	粒子播放位置 
	 */
	public int getParticleIndex(){
		return particleIndex;
	}

	/**
	 * 设置属性：
	 *	粒子播放位置 
	 */
	public void setParticleIndex(int particleIndex){
		this.particleIndex = particleIndex;
	}

	/**
	 * 获取属性：
	 *	普通领悟物品数量 
	 */
	public int getArticleNum1(){
		return articleNum1;
	}

	/**
	 * 设置属性：
	 *	普通领悟物品数量 
	 */
	public void setArticleNum1(int articleNum1){
		this.articleNum1 = articleNum1;
	}

	/**
	 * 获取属性：
	 *	高级领悟物品数量
	 */
	public int getArticleNum2(){
		return articleNum2;
	}

	/**
	 * 设置属性：
	 *	高级领悟物品数量
	 */
	public void setArticleNum2(int articleNum2){
		this.articleNum2 = articleNum2;
	}

}