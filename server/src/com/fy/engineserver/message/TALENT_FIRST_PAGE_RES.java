package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 仙婴首界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fullLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMess</td><td>String</td><td>helpMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>level.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>String</td><td>level.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>phyAttack</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>phyDefence</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>magicAttack</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>magicDefence</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentAddMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentAddMess</td><td>String</td><td>talentAddMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currExp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>upgradeExp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNames[0]</td><td>String</td><td>articleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNames[1]</td><td>String</td><td>articleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNames[2]</td><td>String</td><td>articleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cdMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cdMess</td><td>String</td><td>cdMess.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class TALENT_FIRST_PAGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int fullLevel;
	long playerId;
	String helpMess;
	String level;
	int phyAttack;
	int phyDefence;
	int magicAttack;
	int magicDefence;
	int hp;
	String talentAddMess;
	long currExp;
	long upgradeExp;
	String[] articleNames;
	String cdMess;

	public TALENT_FIRST_PAGE_RES(){
	}

	public TALENT_FIRST_PAGE_RES(long seqNum,int fullLevel,long playerId,String helpMess,String level,int phyAttack,int phyDefence,int magicAttack,int magicDefence,int hp,String talentAddMess,long currExp,long upgradeExp,String[] articleNames,String cdMess){
		this.seqNum = seqNum;
		this.fullLevel = fullLevel;
		this.playerId = playerId;
		this.helpMess = helpMess;
		this.level = level;
		this.phyAttack = phyAttack;
		this.phyDefence = phyDefence;
		this.magicAttack = magicAttack;
		this.magicDefence = magicDefence;
		this.hp = hp;
		this.talentAddMess = talentAddMess;
		this.currExp = currExp;
		this.upgradeExp = upgradeExp;
		this.articleNames = articleNames;
		this.cdMess = cdMess;
	}

	public TALENT_FIRST_PAGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		fullLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		helpMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		level = new String(content,offset,len,"UTF-8");
		offset += len;
		phyAttack = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		phyDefence = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		magicAttack = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		magicDefence = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		hp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		talentAddMess = new String(content,offset,len,"UTF-8");
		offset += len;
		currExp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		upgradeExp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleNames = new String[len];
		for(int i = 0 ; i < articleNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			articleNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		cdMess = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x70FF0127;
	}

	public String getTypeDescription() {
		return "TALENT_FIRST_PAGE_RES";
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
		len += 8;
		len += 2;
		try{
			len +=helpMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=level.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=talentAddMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 8;
		len += 4;
		for(int i = 0 ; i < articleNames.length; i++){
			len += 2;
			try{
				len += articleNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		len +=cdMess.getBytes().length;
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

			buffer.putInt(fullLevel);
			buffer.putLong(playerId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = helpMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = level.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(phyAttack);
			buffer.putInt(phyDefence);
			buffer.putInt(magicAttack);
			buffer.putInt(magicDefence);
			buffer.putInt(hp);
				try{
			tmpBytes1 = talentAddMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(currExp);
			buffer.putLong(upgradeExp);
			buffer.putInt(articleNames.length);
			for(int i = 0 ; i < articleNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = articleNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			tmpBytes1 = cdMess.getBytes();
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
	 *	满级(0:默认，1:满级)
	 */
	public int getFullLevel(){
		return fullLevel;
	}

	/**
	 * 设置属性：
	 *	满级(0:默认，1:满级)
	 */
	public void setFullLevel(int fullLevel){
		this.fullLevel = fullLevel;
	}

	/**
	 * 获取属性：
	 *	角色id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	帮助信息
	 */
	public String getHelpMess(){
		return helpMess;
	}

	/**
	 * 设置属性：
	 *	帮助信息
	 */
	public void setHelpMess(String helpMess){
		this.helpMess = helpMess;
	}

	/**
	 * 获取属性：
	 *	仙婴等级
	 */
	public String getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	仙婴等级
	 */
	public void setLevel(String level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	外功
	 */
	public int getPhyAttack(){
		return phyAttack;
	}

	/**
	 * 设置属性：
	 *	外功
	 */
	public void setPhyAttack(int phyAttack){
		this.phyAttack = phyAttack;
	}

	/**
	 * 获取属性：
	 *	外防
	 */
	public int getPhyDefence(){
		return phyDefence;
	}

	/**
	 * 设置属性：
	 *	外防
	 */
	public void setPhyDefence(int phyDefence){
		this.phyDefence = phyDefence;
	}

	/**
	 * 获取属性：
	 *	内功
	 */
	public int getMagicAttack(){
		return magicAttack;
	}

	/**
	 * 设置属性：
	 *	内功
	 */
	public void setMagicAttack(int magicAttack){
		this.magicAttack = magicAttack;
	}

	/**
	 * 获取属性：
	 *	内防
	 */
	public int getMagicDefence(){
		return magicDefence;
	}

	/**
	 * 设置属性：
	 *	内防
	 */
	public void setMagicDefence(int magicDefence){
		this.magicDefence = magicDefence;
	}

	/**
	 * 获取属性：
	 *	气血
	 */
	public int getHp(){
		return hp;
	}

	/**
	 * 设置属性：
	 *	气血
	 */
	public void setHp(int hp){
		this.hp = hp;
	}

	/**
	 * 获取属性：
	 *	天赋加成
	 */
	public String getTalentAddMess(){
		return talentAddMess;
	}

	/**
	 * 设置属性：
	 *	天赋加成
	 */
	public void setTalentAddMess(String talentAddMess){
		this.talentAddMess = talentAddMess;
	}

	/**
	 * 获取属性：
	 *	当前经验
	 */
	public long getCurrExp(){
		return currExp;
	}

	/**
	 * 设置属性：
	 *	当前经验
	 */
	public void setCurrExp(long currExp){
		this.currExp = currExp;
	}

	/**
	 * 获取属性：
	 *	升级经验
	 */
	public long getUpgradeExp(){
		return upgradeExp;
	}

	/**
	 * 设置属性：
	 *	升级经验
	 */
	public void setUpgradeExp(long upgradeExp){
		this.upgradeExp = upgradeExp;
	}

	/**
	 * 获取属性：
	 *	可以兑换经验的道具
	 */
	public String[] getArticleNames(){
		return articleNames;
	}

	/**
	 * 设置属性：
	 *	可以兑换经验的道具
	 */
	public void setArticleNames(String[] articleNames){
		this.articleNames = articleNames;
	}

	/**
	 * 获取属性：
	 *	cd信息
	 */
	public String getCdMess(){
		return cdMess;
	}

	/**
	 * 设置属性：
	 *	cd信息
	 */
	public void setCdMess(String cdMess){
		this.cdMess = cdMess;
	}

}