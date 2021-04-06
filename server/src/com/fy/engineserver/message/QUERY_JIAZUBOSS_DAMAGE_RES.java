package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.septstation.JiazuBossDamageRecord;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询伤害列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossMaxHp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfDamage</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBossDamageRecords.length</td><td>int</td><td>4个字节</td><td>JiazuBossDamageRecord数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBossDamageRecords[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBossDamageRecords[0].playerName</td><td>String</td><td>jiazuBossDamageRecords[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBossDamageRecords[0].damage</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBossDamageRecords[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBossDamageRecords[1].playerName</td><td>String</td><td>jiazuBossDamageRecords[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBossDamageRecords[1].damage</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBossDamageRecords[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBossDamageRecords[2].playerName</td><td>String</td><td>jiazuBossDamageRecords[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBossDamageRecords[2].damage</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_JIAZUBOSS_DAMAGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	long bossMaxHp;
	long selfDamage;
	JiazuBossDamageRecord[] jiazuBossDamageRecords;

	public QUERY_JIAZUBOSS_DAMAGE_RES(){
	}

	public QUERY_JIAZUBOSS_DAMAGE_RES(long seqNum,byte result,long bossMaxHp,long selfDamage,JiazuBossDamageRecord[] jiazuBossDamageRecords){
		this.seqNum = seqNum;
		this.result = result;
		this.bossMaxHp = bossMaxHp;
		this.selfDamage = selfDamage;
		this.jiazuBossDamageRecords = jiazuBossDamageRecords;
	}

	public QUERY_JIAZUBOSS_DAMAGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		bossMaxHp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		selfDamage = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		jiazuBossDamageRecords = new JiazuBossDamageRecord[len];
		for(int i = 0 ; i < jiazuBossDamageRecords.length ; i++){
			jiazuBossDamageRecords[i] = new JiazuBossDamageRecord();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuBossDamageRecords[i].setPlayerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			jiazuBossDamageRecords[i].setDamage((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
	}

	public int getType() {
		return 0x700EEE0D;
	}

	public String getTypeDescription() {
		return "QUERY_JIAZUBOSS_DAMAGE_RES";
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
		len += 1;
		len += 8;
		len += 8;
		len += 4;
		for(int i = 0 ; i < jiazuBossDamageRecords.length ; i++){
			len += 2;
			if(jiazuBossDamageRecords[i].getPlayerName() != null){
				try{
				len += jiazuBossDamageRecords[i].getPlayerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
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

			buffer.put(result);
			buffer.putLong(bossMaxHp);
			buffer.putLong(selfDamage);
			buffer.putInt(jiazuBossDamageRecords.length);
			for(int i = 0 ; i < jiazuBossDamageRecords.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = jiazuBossDamageRecords[i].getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(jiazuBossDamageRecords[i].getDamage());
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
	 *	查询是否成功
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	查询是否成功
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	boss最大血
	 */
	public long getBossMaxHp(){
		return bossMaxHp;
	}

	/**
	 * 设置属性：
	 *	boss最大血
	 */
	public void setBossMaxHp(long bossMaxHp){
		this.bossMaxHp = bossMaxHp;
	}

	/**
	 * 获取属性：
	 *	自己的伤害
	 */
	public long getSelfDamage(){
		return selfDamage;
	}

	/**
	 * 设置属性：
	 *	自己的伤害
	 */
	public void setSelfDamage(long selfDamage){
		this.selfDamage = selfDamage;
	}

	/**
	 * 获取属性：
	 *	伤害列表
	 */
	public JiazuBossDamageRecord[] getJiazuBossDamageRecords(){
		return jiazuBossDamageRecords;
	}

	/**
	 * 设置属性：
	 *	伤害列表
	 */
	public void setJiazuBossDamageRecords(JiazuBossDamageRecord[] jiazuBossDamageRecords){
		this.jiazuBossDamageRecords = jiazuBossDamageRecords;
	}

}