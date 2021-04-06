package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开仙灵主面板<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>score</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icon</td><td>String</td><td>icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff_meetMongsterLeftTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff_xianlingScoreLeftTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buff_xianlingScoreLevel</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>timedTaskLeftTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>timedTaskMonster.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>timedTaskMonster</td><td>String</td><td>timedTaskMonster.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currEnergy</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxEnergy</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapName[0]</td><td>String</td><td>mapName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapName[1]</td><td>String</td><td>mapName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapName[2]</td><td>String</td><td>mapName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class XIANLING_OPEN_MAIN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int score;
	String icon;
	long buff_meetMongsterLeftTime;
	long tempId;
	long buff_xianlingScoreLeftTime;
	long buff_xianlingScoreLevel;
	long timedTaskLeftTime;
	String timedTaskMonster;
	int maxLevel;
	int currEnergy;
	int maxEnergy;
	String[] mapName;
	boolean isOpen;

	public XIANLING_OPEN_MAIN_RES(){
	}

	public XIANLING_OPEN_MAIN_RES(long seqNum,int score,String icon,long buff_meetMongsterLeftTime,long tempId,long buff_xianlingScoreLeftTime,long buff_xianlingScoreLevel,long timedTaskLeftTime,String timedTaskMonster,int maxLevel,int currEnergy,int maxEnergy,String[] mapName,boolean isOpen){
		this.seqNum = seqNum;
		this.score = score;
		this.icon = icon;
		this.buff_meetMongsterLeftTime = buff_meetMongsterLeftTime;
		this.tempId = tempId;
		this.buff_xianlingScoreLeftTime = buff_xianlingScoreLeftTime;
		this.buff_xianlingScoreLevel = buff_xianlingScoreLevel;
		this.timedTaskLeftTime = timedTaskLeftTime;
		this.timedTaskMonster = timedTaskMonster;
		this.maxLevel = maxLevel;
		this.currEnergy = currEnergy;
		this.maxEnergy = maxEnergy;
		this.mapName = mapName;
		this.isOpen = isOpen;
	}

	public XIANLING_OPEN_MAIN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		score = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		icon = new String(content,offset,len,"UTF-8");
		offset += len;
		buff_meetMongsterLeftTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		tempId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		buff_xianlingScoreLeftTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		buff_xianlingScoreLevel = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		timedTaskLeftTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		timedTaskMonster = new String(content,offset,len,"UTF-8");
		offset += len;
		maxLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currEnergy = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxEnergy = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		mapName = new String[len];
		for(int i = 0 ; i < mapName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			mapName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		isOpen = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x70FFF067;
	}

	public String getTypeDescription() {
		return "XIANLING_OPEN_MAIN_RES";
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
			len +=icon.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 8;
		len += 8;
		len += 8;
		len += 8;
		len += 2;
		try{
			len +=timedTaskMonster.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < mapName.length; i++){
			len += 2;
			try{
				len += mapName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 1;
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

			buffer.putInt(score);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = icon.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(buff_meetMongsterLeftTime);
			buffer.putLong(tempId);
			buffer.putLong(buff_xianlingScoreLeftTime);
			buffer.putLong(buff_xianlingScoreLevel);
			buffer.putLong(timedTaskLeftTime);
				try{
			tmpBytes1 = timedTaskMonster.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(maxLevel);
			buffer.putInt(currEnergy);
			buffer.putInt(maxEnergy);
			buffer.putInt(mapName.length);
			for(int i = 0 ; i < mapName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = mapName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.put((byte)(isOpen==false?0:1));
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
	 *	玩家当前活动获得的总积分
	 */
	public int getScore(){
		return score;
	}

	/**
	 * 设置属性：
	 *	玩家当前活动获得的总积分
	 */
	public void setScore(int score){
		this.score = score;
	}

	/**
	 * 获取属性：
	 *	激活buff图标
	 */
	public String getIcon(){
		return icon;
	}

	/**
	 * 设置属性：
	 *	激活buff图标
	 */
	public void setIcon(String icon){
		this.icon = icon;
	}

	/**
	 * 获取属性：
	 *	激活buff倒计时
	 */
	public long getBuff_meetMongsterLeftTime(){
		return buff_meetMongsterLeftTime;
	}

	/**
	 * 设置属性：
	 *	激活buff倒计时
	 */
	public void setBuff_meetMongsterLeftTime(long buff_meetMongsterLeftTime){
		this.buff_meetMongsterLeftTime = buff_meetMongsterLeftTime;
	}

	/**
	 * 获取属性：
	 *	积分卡临时物品id
	 */
	public long getTempId(){
		return tempId;
	}

	/**
	 * 设置属性：
	 *	积分卡临时物品id
	 */
	public void setTempId(long tempId){
		this.tempId = tempId;
	}

	/**
	 * 获取属性：
	 *	积分卡buff倒计时
	 */
	public long getBuff_xianlingScoreLeftTime(){
		return buff_xianlingScoreLeftTime;
	}

	/**
	 * 设置属性：
	 *	积分卡buff倒计时
	 */
	public void setBuff_xianlingScoreLeftTime(long buff_xianlingScoreLeftTime){
		this.buff_xianlingScoreLeftTime = buff_xianlingScoreLeftTime;
	}

	/**
	 * 获取属性：
	 *	积分卡等级，0:普通，1:高级
	 */
	public long getBuff_xianlingScoreLevel(){
		return buff_xianlingScoreLevel;
	}

	/**
	 * 设置属性：
	 *	积分卡等级，0:普通，1:高级
	 */
	public void setBuff_xianlingScoreLevel(long buff_xianlingScoreLevel){
		this.buff_xianlingScoreLevel = buff_xianlingScoreLevel;
	}

	/**
	 * 获取属性：
	 *	限时任务倒计时
	 */
	public long getTimedTaskLeftTime(){
		return timedTaskLeftTime;
	}

	/**
	 * 设置属性：
	 *	限时任务倒计时
	 */
	public void setTimedTaskLeftTime(long timedTaskLeftTime){
		this.timedTaskLeftTime = timedTaskLeftTime;
	}

	/**
	 * 获取属性：
	 *	限时任务怪图标
	 */
	public String getTimedTaskMonster(){
		return timedTaskMonster;
	}

	/**
	 * 设置属性：
	 *	限时任务怪图标
	 */
	public void setTimedTaskMonster(String timedTaskMonster){
		this.timedTaskMonster = timedTaskMonster;
	}

	/**
	 * 获取属性：
	 *	玩家当前开启过的最大关卡
	 */
	public int getMaxLevel(){
		return maxLevel;
	}

	/**
	 * 设置属性：
	 *	玩家当前开启过的最大关卡
	 */
	public void setMaxLevel(int maxLevel){
		this.maxLevel = maxLevel;
	}

	/**
	 * 获取属性：
	 *	当前真气
	 */
	public int getCurrEnergy(){
		return currEnergy;
	}

	/**
	 * 设置属性：
	 *	当前真气
	 */
	public void setCurrEnergy(int currEnergy){
		this.currEnergy = currEnergy;
	}

	/**
	 * 获取属性：
	 *	最大真气
	 */
	public int getMaxEnergy(){
		return maxEnergy;
	}

	/**
	 * 设置属性：
	 *	最大真气
	 */
	public void setMaxEnergy(int maxEnergy){
		this.maxEnergy = maxEnergy;
	}

	/**
	 * 获取属性：
	 *	关卡地图名
	 */
	public String[] getMapName(){
		return mapName;
	}

	/**
	 * 设置属性：
	 *	关卡地图名
	 */
	public void setMapName(String[] mapName){
		this.mapName = mapName;
	}

	/**
	 * 获取属性：
	 *	活动是否开启
	 */
	public boolean getIsOpen(){
		return isOpen;
	}

	/**
	 * 设置属性：
	 *	活动是否开启
	 */
	public void setIsOpen(boolean isOpen){
		this.isOpen = isOpen;
	}

}