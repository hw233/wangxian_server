package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端打开千层塔<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nanduType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>canAuto</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>daoOpens.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>daoOpens</td><td>boolean[]</td><td>daoOpens.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>daoIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>daoName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>daoName</td><td>String</td><td>daoName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cengIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxCeng</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hiddenCengOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxReachCengIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentMonsters.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentMonsters[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentMonsters[0]</td><td>String</td><td>currentMonsters[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentMonsters[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentMonsters[1]</td><td>String</td><td>currentMonsters[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentMonsters[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentMonsters[2]</td><td>String</td><td>currentMonsters[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cengRewardIDs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cengRewardIDs</td><td>long[]</td><td>cengRewardIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totalFlushTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>freeFlushTimesEveryDay</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxFlushTimesEveryDay</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costSilver</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isInAutoPata</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hasRewards</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardEntityIDs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardEntityIDs</td><td>long[]</td><td>rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>firstRewardIDs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>firstRewardIDs</td><td>long[]</td><td>firstRewardIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>firstRewardNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>firstRewardNums</td><td>int[]</td><td>firstRewardNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>firstIsGet</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class NEWQIANCENGTA_OPEN_INFO_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int nanduType;
	boolean canAuto;
	boolean[] daoOpens;
	int daoIndex;
	String daoName;
	int cengIndex;
	int maxCeng;
	boolean hiddenCengOpen;
	int maxReachCengIndex;
	String[] currentMonsters;
	long[] cengRewardIDs;
	int totalFlushTimes;
	int freeFlushTimesEveryDay;
	int maxFlushTimesEveryDay;
	long costSilver;
	boolean isInAutoPata;
	boolean hasRewards;
	long[] rewardEntityIDs;
	long[] firstRewardIDs;
	int[] firstRewardNums;
	boolean firstIsGet;

	public NEWQIANCENGTA_OPEN_INFO_REQ(){
	}

	public NEWQIANCENGTA_OPEN_INFO_REQ(long seqNum,int nanduType,boolean canAuto,boolean[] daoOpens,int daoIndex,String daoName,int cengIndex,int maxCeng,boolean hiddenCengOpen,int maxReachCengIndex,String[] currentMonsters,long[] cengRewardIDs,int totalFlushTimes,int freeFlushTimesEveryDay,int maxFlushTimesEveryDay,long costSilver,boolean isInAutoPata,boolean hasRewards,long[] rewardEntityIDs,long[] firstRewardIDs,int[] firstRewardNums,boolean firstIsGet){
		this.seqNum = seqNum;
		this.nanduType = nanduType;
		this.canAuto = canAuto;
		this.daoOpens = daoOpens;
		this.daoIndex = daoIndex;
		this.daoName = daoName;
		this.cengIndex = cengIndex;
		this.maxCeng = maxCeng;
		this.hiddenCengOpen = hiddenCengOpen;
		this.maxReachCengIndex = maxReachCengIndex;
		this.currentMonsters = currentMonsters;
		this.cengRewardIDs = cengRewardIDs;
		this.totalFlushTimes = totalFlushTimes;
		this.freeFlushTimesEveryDay = freeFlushTimesEveryDay;
		this.maxFlushTimesEveryDay = maxFlushTimesEveryDay;
		this.costSilver = costSilver;
		this.isInAutoPata = isInAutoPata;
		this.hasRewards = hasRewards;
		this.rewardEntityIDs = rewardEntityIDs;
		this.firstRewardIDs = firstRewardIDs;
		this.firstRewardNums = firstRewardNums;
		this.firstIsGet = firstIsGet;
	}

	public NEWQIANCENGTA_OPEN_INFO_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		nanduType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		canAuto = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		daoOpens = new boolean[len];
		for(int i = 0 ; i < daoOpens.length ; i++){
			daoOpens[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		daoIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		daoName = new String(content,offset,len,"UTF-8");
		offset += len;
		cengIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxCeng = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		hiddenCengOpen = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		maxReachCengIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		currentMonsters = new String[len];
		for(int i = 0 ; i < currentMonsters.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			currentMonsters[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		cengRewardIDs = new long[len];
		for(int i = 0 ; i < cengRewardIDs.length ; i++){
			cengRewardIDs[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		totalFlushTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		freeFlushTimesEveryDay = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxFlushTimesEveryDay = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		costSilver = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		isInAutoPata = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		hasRewards = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardEntityIDs = new long[len];
		for(int i = 0 ; i < rewardEntityIDs.length ; i++){
			rewardEntityIDs[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		firstRewardIDs = new long[len];
		for(int i = 0 ; i < firstRewardIDs.length ; i++){
			firstRewardIDs[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		firstRewardNums = new int[len];
		for(int i = 0 ; i < firstRewardNums.length ; i++){
			firstRewardNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		firstIsGet = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x0F710001;
	}

	public String getTypeDescription() {
		return "NEWQIANCENGTA_OPEN_INFO_REQ";
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
		len += 1;
		len += 4;
		len += daoOpens.length;
		len += 4;
		len += 2;
		try{
			len +=daoName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 1;
		len += 4;
		len += 4;
		for(int i = 0 ; i < currentMonsters.length; i++){
			len += 2;
			try{
				len += currentMonsters[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += cengRewardIDs.length * 8;
		len += 4;
		len += 4;
		len += 4;
		len += 8;
		len += 1;
		len += 1;
		len += 4;
		len += rewardEntityIDs.length * 8;
		len += 4;
		len += firstRewardIDs.length * 8;
		len += 4;
		len += firstRewardNums.length * 4;
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

			buffer.putInt(nanduType);
			buffer.put((byte)(canAuto==false?0:1));
			buffer.putInt(daoOpens.length);
			for(int i = 0 ; i < daoOpens.length; i++){
				buffer.put((byte)(daoOpens[i]==false?0:1));
			}
			buffer.putInt(daoIndex);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = daoName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(cengIndex);
			buffer.putInt(maxCeng);
			buffer.put((byte)(hiddenCengOpen==false?0:1));
			buffer.putInt(maxReachCengIndex);
			buffer.putInt(currentMonsters.length);
			for(int i = 0 ; i < currentMonsters.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = currentMonsters[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(cengRewardIDs.length);
			for(int i = 0 ; i < cengRewardIDs.length; i++){
				buffer.putLong(cengRewardIDs[i]);
			}
			buffer.putInt(totalFlushTimes);
			buffer.putInt(freeFlushTimesEveryDay);
			buffer.putInt(maxFlushTimesEveryDay);
			buffer.putLong(costSilver);
			buffer.put((byte)(isInAutoPata==false?0:1));
			buffer.put((byte)(hasRewards==false?0:1));
			buffer.putInt(rewardEntityIDs.length);
			for(int i = 0 ; i < rewardEntityIDs.length; i++){
				buffer.putLong(rewardEntityIDs[i]);
			}
			buffer.putInt(firstRewardIDs.length);
			for(int i = 0 ; i < firstRewardIDs.length; i++){
				buffer.putLong(firstRewardIDs[i]);
			}
			buffer.putInt(firstRewardNums.length);
			for(int i = 0 ; i < firstRewardNums.length; i++){
				buffer.putInt(firstRewardNums[i]);
			}
			buffer.put((byte)(firstIsGet==false?0:1));
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
	 *	难度Type 0是普通 1是困难 2是深渊
	 */
	public int getNanduType(){
		return nanduType;
	}

	/**
	 * 设置属性：
	 *	难度Type 0是普通 1是困难 2是深渊
	 */
	public void setNanduType(int nanduType){
		this.nanduType = nanduType;
	}

	/**
	 * 获取属性：
	 *	是否可以自动爬塔
	 */
	public boolean getCanAuto(){
		return canAuto;
	}

	/**
	 * 设置属性：
	 *	是否可以自动爬塔
	 */
	public void setCanAuto(boolean canAuto){
		this.canAuto = canAuto;
	}

	/**
	 * 获取属性：
	 *	六道开启情况 长度为6
	 */
	public boolean[] getDaoOpens(){
		return daoOpens;
	}

	/**
	 * 设置属性：
	 *	六道开启情况 长度为6
	 */
	public void setDaoOpens(boolean[] daoOpens){
		this.daoOpens = daoOpens;
	}

	/**
	 * 获取属性：
	 *	当前道的下标，从0开始
	 */
	public int getDaoIndex(){
		return daoIndex;
	}

	/**
	 * 设置属性：
	 *	当前道的下标，从0开始
	 */
	public void setDaoIndex(int daoIndex){
		this.daoIndex = daoIndex;
	}

	/**
	 * 获取属性：
	 *	道的名字
	 */
	public String getDaoName(){
		return daoName;
	}

	/**
	 * 设置属性：
	 *	道的名字
	 */
	public void setDaoName(String daoName){
		this.daoName = daoName;
	}

	/**
	 * 获取属性：
	 *	要进入的道里面的层，从0开始
	 */
	public int getCengIndex(){
		return cengIndex;
	}

	/**
	 * 设置属性：
	 *	要进入的道里面的层，从0开始
	 */
	public void setCengIndex(int cengIndex){
		this.cengIndex = cengIndex;
	}

	/**
	 * 获取属性：
	 *	此道最多多少层
	 */
	public int getMaxCeng(){
		return maxCeng;
	}

	/**
	 * 设置属性：
	 *	此道最多多少层
	 */
	public void setMaxCeng(int maxCeng){
		this.maxCeng = maxCeng;
	}

	/**
	 * 获取属性：
	 *	隐藏层是否开启
	 */
	public boolean getHiddenCengOpen(){
		return hiddenCengOpen;
	}

	/**
	 * 设置属性：
	 *	隐藏层是否开启
	 */
	public void setHiddenCengOpen(boolean hiddenCengOpen){
		this.hiddenCengOpen = hiddenCengOpen;
	}

	/**
	 * 获取属性：
	 *	此用户在本道里历史到达的最高层，开始为-1
	 */
	public int getMaxReachCengIndex(){
		return maxReachCengIndex;
	}

	/**
	 * 设置属性：
	 *	此用户在本道里历史到达的最高层，开始为-1
	 */
	public void setMaxReachCengIndex(int maxReachCengIndex){
		this.maxReachCengIndex = maxReachCengIndex;
	}

	/**
	 * 获取属性：
	 *	要进入层的怪物信息
	 */
	public String[] getCurrentMonsters(){
		return currentMonsters;
	}

	/**
	 * 设置属性：
	 *	要进入层的怪物信息
	 */
	public void setCurrentMonsters(String[] currentMonsters){
		this.currentMonsters = currentMonsters;
	}

	/**
	 * 获取属性：
	 *	本层奖励
	 */
	public long[] getCengRewardIDs(){
		return cengRewardIDs;
	}

	/**
	 * 设置属性：
	 *	本层奖励
	 */
	public void setCengRewardIDs(long[] cengRewardIDs){
		this.cengRewardIDs = cengRewardIDs;
	}

	/**
	 * 获取属性：
	 *	今日已刷新次数
	 */
	public int getTotalFlushTimes(){
		return totalFlushTimes;
	}

	/**
	 * 设置属性：
	 *	今日已刷新次数
	 */
	public void setTotalFlushTimes(int totalFlushTimes){
		this.totalFlushTimes = totalFlushTimes;
	}

	/**
	 * 获取属性：
	 *	今日可免费刷新次数，totalFlushTimes>=freeFlushTimesEveryDay标识免费次数用完
	 */
	public int getFreeFlushTimesEveryDay(){
		return freeFlushTimesEveryDay;
	}

	/**
	 * 设置属性：
	 *	今日可免费刷新次数，totalFlushTimes>=freeFlushTimesEveryDay标识免费次数用完
	 */
	public void setFreeFlushTimesEveryDay(int freeFlushTimesEveryDay){
		this.freeFlushTimesEveryDay = freeFlushTimesEveryDay;
	}

	/**
	 * 获取属性：
	 *	今日最大刷新次数
	 */
	public int getMaxFlushTimesEveryDay(){
		return maxFlushTimesEveryDay;
	}

	/**
	 * 设置属性：
	 *	今日最大刷新次数
	 */
	public void setMaxFlushTimesEveryDay(int maxFlushTimesEveryDay){
		this.maxFlushTimesEveryDay = maxFlushTimesEveryDay;
	}

	/**
	 * 获取属性：
	 *	付费刷新，每次扣多少钱，单位为文
	 */
	public long getCostSilver(){
		return costSilver;
	}

	/**
	 * 设置属性：
	 *	付费刷新，每次扣多少钱，单位为文
	 */
	public void setCostSilver(long costSilver){
		this.costSilver = costSilver;
	}

	/**
	 * 获取属性：
	 *	是否正在自动爬塔
	 */
	public boolean getIsInAutoPata(){
		return isInAutoPata;
	}

	/**
	 * 设置属性：
	 *	是否正在自动爬塔
	 */
	public void setIsInAutoPata(boolean isInAutoPata){
		this.isInAutoPata = isInAutoPata;
	}

	/**
	 * 获取属性：
	 *	本道是否有奖励为领取
	 */
	public boolean getHasRewards(){
		return hasRewards;
	}

	/**
	 * 设置属性：
	 *	本道是否有奖励为领取
	 */
	public void setHasRewards(boolean hasRewards){
		this.hasRewards = hasRewards;
	}

	/**
	 * 获取属性：
	 *	本道奖励tempEntityID
	 */
	public long[] getRewardEntityIDs(){
		return rewardEntityIDs;
	}

	/**
	 * 设置属性：
	 *	本道奖励tempEntityID
	 */
	public void setRewardEntityIDs(long[] rewardEntityIDs){
		this.rewardEntityIDs = rewardEntityIDs;
	}

	/**
	 * 获取属性：
	 *	本道首次奖励IDs
	 */
	public long[] getFirstRewardIDs(){
		return firstRewardIDs;
	}

	/**
	 * 设置属性：
	 *	本道首次奖励IDs
	 */
	public void setFirstRewardIDs(long[] firstRewardIDs){
		this.firstRewardIDs = firstRewardIDs;
	}

	/**
	 * 获取属性：
	 *	本道首次奖励Nums
	 */
	public int[] getFirstRewardNums(){
		return firstRewardNums;
	}

	/**
	 * 设置属性：
	 *	本道首次奖励Nums
	 */
	public void setFirstRewardNums(int[] firstRewardNums){
		this.firstRewardNums = firstRewardNums;
	}

	/**
	 * 获取属性：
	 *	本道首次奖励是否领取
	 */
	public boolean getFirstIsGet(){
		return firstIsGet;
	}

	/**
	 * 设置属性：
	 *	本道首次奖励是否领取
	 */
	public void setFirstIsGet(boolean firstIsGet){
		this.firstIsGet = firstIsGet;
	}

}