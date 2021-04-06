package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求活动<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityName</td><td>String</td><td>activityName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>backgroundName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>backgroundName</td><td>String</td><td>backgroundName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityTitle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityTitle</td><td>String</td><td>activityTitle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityContent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityContent</td><td>String</td><td>activityContent.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardIds</td><td>long[]</td><td>rewardIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardNums</td><td>int[]</td><td>rewardNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lizis.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lizis</td><td>int[]</td><td>lizis.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buttonType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buttonStat</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffonName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffonName</td><td>String</td><td>buffonName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chargeMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeMess</td><td>String</td><td>chargeMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xPoint</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yPoint</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcName</td><td>String</td><td>npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapName</td><td>String</td><td>mapName.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class GET_ACTIVITY_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String activityName;
	int tempType;
	String backgroundName;
	String activityTitle;
	String activityContent;
	long[] rewardIds;
	int[] rewardNums;
	int[] lizis;
	int buttonType;
	int buttonStat;
	String buffonName;
	String chargeMess;
	int xPoint;
	int yPoint;
	String npcName;
	String mapName;

	public GET_ACTIVITY_INFO_RES(){
	}

	public GET_ACTIVITY_INFO_RES(long seqNum,String activityName,int tempType,String backgroundName,String activityTitle,String activityContent,long[] rewardIds,int[] rewardNums,int[] lizis,int buttonType,int buttonStat,String buffonName,String chargeMess,int xPoint,int yPoint,String npcName,String mapName){
		this.seqNum = seqNum;
		this.activityName = activityName;
		this.tempType = tempType;
		this.backgroundName = backgroundName;
		this.activityTitle = activityTitle;
		this.activityContent = activityContent;
		this.rewardIds = rewardIds;
		this.rewardNums = rewardNums;
		this.lizis = lizis;
		this.buttonType = buttonType;
		this.buttonStat = buttonStat;
		this.buffonName = buffonName;
		this.chargeMess = chargeMess;
		this.xPoint = xPoint;
		this.yPoint = yPoint;
		this.npcName = npcName;
		this.mapName = mapName;
	}

	public GET_ACTIVITY_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityName = new String(content,offset,len,"UTF-8");
		offset += len;
		tempType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		backgroundName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityTitle = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityContent = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardIds = new long[len];
		for(int i = 0 ; i < rewardIds.length ; i++){
			rewardIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardNums = new int[len];
		for(int i = 0 ; i < rewardNums.length ; i++){
			rewardNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lizis = new int[len];
		for(int i = 0 ; i < lizis.length ; i++){
			lizis[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		buttonType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		buttonStat = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		buffonName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chargeMess = new String(content,offset,len,"UTF-8");
		offset += len;
		xPoint = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		yPoint = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		npcName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		mapName = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x70F0EF99;
	}

	public String getTypeDescription() {
		return "GET_ACTIVITY_INFO_RES";
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
			len +=activityName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 2;
		try{
			len +=backgroundName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=activityTitle.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=activityContent.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += rewardIds.length * 8;
		len += 4;
		len += rewardNums.length * 4;
		len += 4;
		len += lizis.length * 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=buffonName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=chargeMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=npcName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=mapName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = activityName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(tempType);
				try{
			tmpBytes1 = backgroundName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = activityTitle.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = activityContent.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(rewardIds.length);
			for(int i = 0 ; i < rewardIds.length; i++){
				buffer.putLong(rewardIds[i]);
			}
			buffer.putInt(rewardNums.length);
			for(int i = 0 ; i < rewardNums.length; i++){
				buffer.putInt(rewardNums[i]);
			}
			buffer.putInt(lizis.length);
			for(int i = 0 ; i < lizis.length; i++){
				buffer.putInt(lizis[i]);
			}
			buffer.putInt(buttonType);
			buffer.putInt(buttonStat);
				try{
			tmpBytes1 = buffonName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = chargeMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(xPoint);
			buffer.putInt(yPoint);
				try{
			tmpBytes1 = npcName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = mapName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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
	 *	请求的活动名
	 */
	public String getActivityName(){
		return activityName;
	}

	/**
	 * 设置属性：
	 *	请求的活动名
	 */
	public void setActivityName(String activityName){
		this.activityName = activityName;
	}

	/**
	 * 获取属性：
	 *	模板类型
	 */
	public int getTempType(){
		return tempType;
	}

	/**
	 * 设置属性：
	 *	模板类型
	 */
	public void setTempType(int tempType){
		this.tempType = tempType;
	}

	/**
	 * 获取属性：
	 *	背景图
	 */
	public String getBackgroundName(){
		return backgroundName;
	}

	/**
	 * 设置属性：
	 *	背景图
	 */
	public void setBackgroundName(String backgroundName){
		this.backgroundName = backgroundName;
	}

	/**
	 * 获取属性：
	 *	活动标题
	 */
	public String getActivityTitle(){
		return activityTitle;
	}

	/**
	 * 设置属性：
	 *	活动标题
	 */
	public void setActivityTitle(String activityTitle){
		this.activityTitle = activityTitle;
	}

	/**
	 * 获取属性：
	 *	活动内容
	 */
	public String getActivityContent(){
		return activityContent;
	}

	/**
	 * 设置属性：
	 *	活动内容
	 */
	public void setActivityContent(String activityContent){
		this.activityContent = activityContent;
	}

	/**
	 * 获取属性：
	 *	奖励ids
	 */
	public long[] getRewardIds(){
		return rewardIds;
	}

	/**
	 * 设置属性：
	 *	奖励ids
	 */
	public void setRewardIds(long[] rewardIds){
		this.rewardIds = rewardIds;
	}

	/**
	 * 获取属性：
	 *	奖励nums
	 */
	public int[] getRewardNums(){
		return rewardNums;
	}

	/**
	 * 设置属性：
	 *	奖励nums
	 */
	public void setRewardNums(int[] rewardNums){
		this.rewardNums = rewardNums;
	}

	/**
	 * 获取属性：
	 *	是否显示粒子
	 */
	public int[] getLizis(){
		return lizis;
	}

	/**
	 * 设置属性：
	 *	是否显示粒子
	 */
	public void setLizis(int[] lizis){
		this.lizis = lizis;
	}

	/**
	 * 获取属性：
	 *	buttonType
	 */
	public int getButtonType(){
		return buttonType;
	}

	/**
	 * 设置属性：
	 *	buttonType
	 */
	public void setButtonType(int buttonType){
		this.buttonType = buttonType;
	}

	/**
	 * 获取属性：
	 *	活动按钮状态
	 */
	public int getButtonStat(){
		return buttonStat;
	}

	/**
	 * 设置属性：
	 *	活动按钮状态
	 */
	public void setButtonStat(int buttonStat){
		this.buttonStat = buttonStat;
	}

	/**
	 * 获取属性：
	 *	buttonName
	 */
	public String getBuffonName(){
		return buffonName;
	}

	/**
	 * 设置属性：
	 *	buttonName
	 */
	public void setBuffonName(String buffonName){
		this.buffonName = buffonName;
	}

	/**
	 * 获取属性：
	 *	刷新活动内容/活动进度改变
	 */
	public String getChargeMess(){
		return chargeMess;
	}

	/**
	 * 设置属性：
	 *	刷新活动内容/活动进度改变
	 */
	public void setChargeMess(String chargeMess){
		this.chargeMess = chargeMess;
	}

	/**
	 * 获取属性：
	 *	x
	 */
	public int getXPoint(){
		return xPoint;
	}

	/**
	 * 设置属性：
	 *	x
	 */
	public void setXPoint(int xPoint){
		this.xPoint = xPoint;
	}

	/**
	 * 获取属性：
	 *	y
	 */
	public int getYPoint(){
		return yPoint;
	}

	/**
	 * 设置属性：
	 *	y
	 */
	public void setYPoint(int yPoint){
		this.yPoint = yPoint;
	}

	/**
	 * 获取属性：
	 *	npc名
	 */
	public String getNpcName(){
		return npcName;
	}

	/**
	 * 设置属性：
	 *	npc名
	 */
	public void setNpcName(String npcName){
		this.npcName = npcName;
	}

	/**
	 * 获取属性：
	 *	地图名
	 */
	public String getMapName(){
		return mapName;
	}

	/**
	 * 设置属性：
	 *	地图名
	 */
	public void setMapName(String mapName){
		this.mapName = mapName;
	}

}