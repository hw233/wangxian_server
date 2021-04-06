package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 点关卡图标<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterName</td><td>String</td><td>monsterName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[0]</td><td>String</td><td>avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[1]</td><td>String</td><td>avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[2]</td><td>String</td><td>avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>scale</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>x</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>y</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterColor</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isBoss</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categoryId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>energy</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class XL_CHALLENGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int level;
	String name;
	String monsterName;
	String[] avata;
	int scale;
	int x;
	int y;
	int monsterColor;
	boolean isBoss;
	long monsterId;
	int categoryId;
	int energy;
	int maxLevel;

	public XL_CHALLENGE_RES(){
	}

	public XL_CHALLENGE_RES(long seqNum,int level,String name,String monsterName,String[] avata,int scale,int x,int y,int monsterColor,boolean isBoss,long monsterId,int categoryId,int energy,int maxLevel){
		this.seqNum = seqNum;
		this.level = level;
		this.name = name;
		this.monsterName = monsterName;
		this.avata = avata;
		this.scale = scale;
		this.x = x;
		this.y = y;
		this.monsterColor = monsterColor;
		this.isBoss = isBoss;
		this.monsterId = monsterId;
		this.categoryId = categoryId;
		this.energy = energy;
		this.maxLevel = maxLevel;
	}

	public XL_CHALLENGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		monsterName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avata = new String[len];
		for(int i = 0 ; i < avata.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			avata[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		scale = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		x = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		y = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		monsterColor = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		isBoss = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		monsterId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		categoryId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		energy = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70FFF070;
	}

	public String getTypeDescription() {
		return "XL_CHALLENGE_RES";
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
		try{
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=monsterName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < avata.length; i++){
			len += 2;
			try{
				len += avata[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 1;
		len += 8;
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

			buffer.putInt(level);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = monsterName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(avata.length);
			for(int i = 0 ; i < avata.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = avata[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(scale);
			buffer.putInt(x);
			buffer.putInt(y);
			buffer.putInt(monsterColor);
			buffer.put((byte)(isBoss==false?0:1));
			buffer.putLong(monsterId);
			buffer.putInt(categoryId);
			buffer.putInt(energy);
			buffer.putInt(maxLevel);
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
	 *	关卡等级
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	关卡等级
	 */
	public void setLevel(int level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	关卡名
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	关卡名
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	怪物名
	 */
	public String getMonsterName(){
		return monsterName;
	}

	/**
	 * 设置属性：
	 *	怪物名
	 */
	public void setMonsterName(String monsterName){
		this.monsterName = monsterName;
	}

	/**
	 * 获取属性：
	 *	怪物avata
	 */
	public String[] getAvata(){
		return avata;
	}

	/**
	 * 设置属性：
	 *	怪物avata
	 */
	public void setAvata(String[] avata){
		this.avata = avata;
	}

	/**
	 * 获取属性：
	 *	怪物形象比例
	 */
	public int getScale(){
		return scale;
	}

	/**
	 * 设置属性：
	 *	怪物形象比例
	 */
	public void setScale(int scale){
		this.scale = scale;
	}

	/**
	 * 获取属性：
	 *	怪物坐标x
	 */
	public int getX(){
		return x;
	}

	/**
	 * 设置属性：
	 *	怪物坐标x
	 */
	public void setX(int x){
		this.x = x;
	}

	/**
	 * 获取属性：
	 *	怪物坐标y
	 */
	public int getY(){
		return y;
	}

	/**
	 * 设置属性：
	 *	怪物坐标y
	 */
	public void setY(int y){
		this.y = y;
	}

	/**
	 * 获取属性：
	 *	怪物品质
	 */
	public int getMonsterColor(){
		return monsterColor;
	}

	/**
	 * 设置属性：
	 *	怪物品质
	 */
	public void setMonsterColor(int monsterColor){
		this.monsterColor = monsterColor;
	}

	/**
	 * 获取属性：
	 *	是否boss
	 */
	public boolean getIsBoss(){
		return isBoss;
	}

	/**
	 * 设置属性：
	 *	是否boss
	 */
	public void setIsBoss(boolean isBoss){
		this.isBoss = isBoss;
	}

	/**
	 * 获取属性：
	 *	怪物id(为确认挑战而传的)
	 */
	public long getMonsterId(){
		return monsterId;
	}

	/**
	 * 设置属性：
	 *	怪物id(为确认挑战而传的)
	 */
	public void setMonsterId(long monsterId){
		this.monsterId = monsterId;
	}

	/**
	 * 获取属性：
	 *	categoryId(为确认挑战而传的)
	 */
	public int getCategoryId(){
		return categoryId;
	}

	/**
	 * 设置属性：
	 *	categoryId(为确认挑战而传的)
	 */
	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	/**
	 * 获取属性：
	 *	当前真气值
	 */
	public int getEnergy(){
		return energy;
	}

	/**
	 * 设置属性：
	 *	当前真气值
	 */
	public void setEnergy(int energy){
		this.energy = energy;
	}

	/**
	 * 获取属性：
	 *	玩家已挑战过的最大关卡
	 */
	public int getMaxLevel(){
		return maxLevel;
	}

	/**
	 * 设置属性：
	 *	玩家已挑战过的最大关卡
	 */
	public void setMaxLevel(int maxLevel){
		this.maxLevel = maxLevel;
	}

}