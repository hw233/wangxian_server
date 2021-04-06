package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取法宝消息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>quality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>star</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIds</td><td>int[]</td><td>skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillLevels</td><td>int[]</td><td>skillLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bstrength</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bdexterity</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bspellpower</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bconstitution</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>properties.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>properties</td><td>String</td><td>properties.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>borntime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CACHE_MAGICWEAPON_GET_RES implements ResponseMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	String name;
	long playerid;
	long articleid;
	int exp;
	int plevel;
	int quality;
	int star;
	int[] skillIds;
	int[] skillLevels;
	int bstrength;
	int bdexterity;
	int bspellpower;
	int bconstitution;
	String properties;
	long borntime;

	public CACHE_MAGICWEAPON_GET_RES(long seqNum,String name,long playerid,long articleid,int exp,int plevel,int quality,int star,int[] skillIds,int[] skillLevels,int bstrength,int bdexterity,int bspellpower,int bconstitution,String properties,long borntime){
		this.seqNum = seqNum;
		this.name = name;
		this.playerid = playerid;
		this.articleid = articleid;
		this.exp = exp;
		this.plevel = plevel;
		this.quality = quality;
		this.star = star;
		this.skillIds = skillIds;
		this.skillLevels = skillLevels;
		this.bstrength = bstrength;
		this.bdexterity = bdexterity;
		this.bspellpower = bspellpower;
		this.bconstitution = bconstitution;
		this.properties = properties;
		this.borntime = borntime;
	}

	public CACHE_MAGICWEAPON_GET_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		playerid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		articleid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		exp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		plevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		quality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		star = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		skillIds = new int[len];
		for(int i = 0 ; i < skillIds.length ; i++){
			skillIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		skillLevels = new int[len];
		for(int i = 0 ; i < skillLevels.length ; i++){
			skillLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		bstrength = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		bdexterity = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		bspellpower = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		bconstitution = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		properties = new String(content,offset,len,"UTF-8");
		offset += len;
		borntime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x80000034;
	}

	public String getTypeDescription() {
		return "CACHE_MAGICWEAPON_GET_RES";
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
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 8;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += skillIds.length * 4;
		len += 4;
		len += skillLevels.length * 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=properties.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			byte[] tmpBytes1 = name.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(playerid);
			buffer.putLong(articleid);
			buffer.putInt(exp);
			buffer.putInt(plevel);
			buffer.putInt(quality);
			buffer.putInt(star);
			buffer.putInt(skillIds.length);
			for(int i = 0 ; i < skillIds.length; i++){
				buffer.putInt(skillIds[i]);
			}
			buffer.putInt(skillLevels.length);
			for(int i = 0 ; i < skillLevels.length; i++){
				buffer.putInt(skillLevels[i]);
			}
			buffer.putInt(bstrength);
			buffer.putInt(bdexterity);
			buffer.putInt(bspellpower);
			buffer.putInt(bconstitution);
			tmpBytes1 = properties.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(borntime);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	法宝名
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	法宝名
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	所属角色
	 */
	public long getPlayerid(){
		return playerid;
	}

	/**
	 * 设置属性：
	 *	所属角色
	 */
	public void setPlayerid(long playerid){
		this.playerid = playerid;
	}

	/**
	 * 获取属性：
	 *	幻化物品的id
	 */
	public long getArticleid(){
		return articleid;
	}

	/**
	 * 设置属性：
	 *	幻化物品的id
	 */
	public void setArticleid(long articleid){
		this.articleid = articleid;
	}

	/**
	 * 获取属性：
	 *	当前级别的经验值
	 */
	public int getExp(){
		return exp;
	}

	/**
	 * 设置属性：
	 *	当前级别的经验值
	 */
	public void setExp(int exp){
		this.exp = exp;
	}

	/**
	 * 获取属性：
	 *	等级
	 */
	public int getPlevel(){
		return plevel;
	}

	/**
	 * 设置属性：
	 *	等级
	 */
	public void setPlevel(int plevel){
		this.plevel = plevel;
	}

	/**
	 * 获取属性：
	 *	品阶
	 */
	public int getQuality(){
		return quality;
	}

	/**
	 * 设置属性：
	 *	品阶
	 */
	public void setQuality(int quality){
		this.quality = quality;
	}

	/**
	 * 获取属性：
	 *	星级
	 */
	public int getStar(){
		return star;
	}

	/**
	 * 设置属性：
	 *	星级
	 */
	public void setStar(int star){
		this.star = star;
	}

	/**
	 * 获取属性：
	 *	技能id
	 */
	public int[] getSkillIds(){
		return skillIds;
	}

	/**
	 * 设置属性：
	 *	技能id
	 */
	public void setSkillIds(int[] skillIds){
		this.skillIds = skillIds;
	}

	/**
	 * 获取属性：
	 *	技能等级
	 */
	public int[] getSkillLevels(){
		return skillLevels;
	}

	/**
	 * 设置属性：
	 *	技能等级
	 */
	public void setSkillLevels(int[] skillLevels){
		this.skillLevels = skillLevels;
	}

	/**
	 * 获取属性：
	 *	基本力量值
	 */
	public int getBstrength(){
		return bstrength;
	}

	/**
	 * 设置属性：
	 *	基本力量值
	 */
	public void setBstrength(int bstrength){
		this.bstrength = bstrength;
	}

	/**
	 * 获取属性：
	 *	基本敏捷值
	 */
	public int getBdexterity(){
		return bdexterity;
	}

	/**
	 * 设置属性：
	 *	基本敏捷值
	 */
	public void setBdexterity(int bdexterity){
		this.bdexterity = bdexterity;
	}

	/**
	 * 获取属性：
	 *	基本智力值
	 */
	public int getBspellpower(){
		return bspellpower;
	}

	/**
	 * 设置属性：
	 *	基本智力值
	 */
	public void setBspellpower(int bspellpower){
		this.bspellpower = bspellpower;
	}

	/**
	 * 获取属性：
	 *	基本耐力值
	 */
	public int getBconstitution(){
		return bconstitution;
	}

	/**
	 * 设置属性：
	 *	基本耐力值
	 */
	public void setBconstitution(int bconstitution){
		this.bconstitution = bconstitution;
	}

	/**
	 * 获取属性：
	 *	属性
	 */
	public String getProperties(){
		return properties;
	}

	/**
	 * 设置属性：
	 *	属性
	 */
	public void setProperties(String properties){
		this.properties = properties;
	}

	/**
	 * 获取属性：
	 *	创建时间
	 */
	public long getBorntime(){
		return borntime;
	}

	/**
	 * 设置属性：
	 *	创建时间
	 */
	public void setBorntime(long borntime){
		this.borntime = borntime;
	}

}
