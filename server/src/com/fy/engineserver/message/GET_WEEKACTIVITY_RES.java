package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.newChongZhiActivity.WeekClientActivity;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 新充值活动信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dataID</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>msg.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>msg</td><td>String</td><td>msg.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>time.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>time</td><td>String</td><td>time.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys.length</td><td>int</td><td>4个字节</td><td>WeekClientActivity数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].title</td><td>String</td><td>activitys[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].rewardEntityIDs</td><td>long[]</td><td>activitys[0].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].rewardEntityNums</td><td>int[]</td><td>activitys[0].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].needValue</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].title</td><td>String</td><td>activitys[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].rewardEntityIDs</td><td>long[]</td><td>activitys[1].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].rewardEntityNums</td><td>int[]</td><td>activitys[1].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].needValue</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].title</td><td>String</td><td>activitys[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].rewardEntityIDs</td><td>long[]</td><td>activitys[2].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].rewardEntityNums</td><td>int[]</td><td>activitys[2].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].needValue</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerChongZhi</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isGetRewards.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isGetRewards</td><td>boolean[]</td><td>isGetRewards.length</td><td>*</td></tr>
 * </table>
 */
public class GET_WEEKACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int dataID;
	String msg;
	String time;
	WeekClientActivity[] activitys;
	long playerChongZhi;
	int playerNum;
	boolean[] isGetRewards;

	public GET_WEEKACTIVITY_RES(){
	}

	public GET_WEEKACTIVITY_RES(long seqNum,int dataID,String msg,String time,WeekClientActivity[] activitys,long playerChongZhi,int playerNum,boolean[] isGetRewards){
		this.seqNum = seqNum;
		this.dataID = dataID;
		this.msg = msg;
		this.time = time;
		this.activitys = activitys;
		this.playerChongZhi = playerChongZhi;
		this.playerNum = playerNum;
		this.isGetRewards = isGetRewards;
	}

	public GET_WEEKACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		dataID = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		msg = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		time = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		activitys = new WeekClientActivity[len];
		for(int i = 0 ; i < activitys.length ; i++){
			activitys[i] = new WeekClientActivity();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activitys[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] rewardEntityIDs_0001 = new long[len];
			for(int j = 0 ; j < rewardEntityIDs_0001.length ; j++){
				rewardEntityIDs_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			activitys[i].setRewardEntityIDs(rewardEntityIDs_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] rewardEntityNums_0002 = new int[len];
			for(int j = 0 ; j < rewardEntityNums_0002.length ; j++){
				rewardEntityNums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			activitys[i].setRewardEntityNums(rewardEntityNums_0002);
			activitys[i].setNeedValue((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
		playerChongZhi = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		playerNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		isGetRewards = new boolean[len];
		for(int i = 0 ; i < isGetRewards.length ; i++){
			isGetRewards[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
	}

	public int getType() {
		return 0x700EB105;
	}

	public String getTypeDescription() {
		return "GET_WEEKACTIVITY_RES";
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
			len +=msg.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=time.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < activitys.length ; i++){
			len += 2;
			if(activitys[i].getTitle() != null){
				try{
				len += activitys[i].getTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += activitys[i].getRewardEntityIDs().length * 8;
			len += 4;
			len += activitys[i].getRewardEntityNums().length * 4;
			len += 8;
		}
		len += 8;
		len += 4;
		len += 4;
		len += isGetRewards.length;
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

			buffer.putInt(dataID);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = msg.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = time.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(activitys.length);
			for(int i = 0 ; i < activitys.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = activitys[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(activitys[i].getRewardEntityIDs().length);
				long[] rewardEntityIDs_0003 = activitys[i].getRewardEntityIDs();
				for(int j = 0 ; j < rewardEntityIDs_0003.length ; j++){
					buffer.putLong(rewardEntityIDs_0003[j]);
				}
				buffer.putInt(activitys[i].getRewardEntityNums().length);
				int[] rewardEntityNums_0004 = activitys[i].getRewardEntityNums();
				for(int j = 0 ; j < rewardEntityNums_0004.length ; j++){
					buffer.putInt(rewardEntityNums_0004[j]);
				}
				buffer.putLong(activitys[i].getNeedValue());
			}
			buffer.putLong(playerChongZhi);
			buffer.putInt(playerNum);
			buffer.putInt(isGetRewards.length);
			for(int i = 0 ; i < isGetRewards.length; i++){
				buffer.put((byte)(isGetRewards[i]==false?0:1));
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
	 *	活动ID
	 */
	public int getDataID(){
		return dataID;
	}

	/**
	 * 设置属性：
	 *	活动ID
	 */
	public void setDataID(int dataID){
		this.dataID = dataID;
	}

	/**
	 * 获取属性：
	 *	活动说明
	 */
	public String getMsg(){
		return msg;
	}

	/**
	 * 设置属性：
	 *	活动说明
	 */
	public void setMsg(String msg){
		this.msg = msg;
	}

	/**
	 * 获取属性：
	 *	活动时间
	 */
	public String getTime(){
		return time;
	}

	/**
	 * 设置属性：
	 *	活动时间
	 */
	public void setTime(String time){
		this.time = time;
	}

	/**
	 * 获取属性：
	 *	周充值活动
	 */
	public WeekClientActivity[] getActivitys(){
		return activitys;
	}

	/**
	 * 设置属性：
	 *	周充值活动
	 */
	public void setActivitys(WeekClientActivity[] activitys){
		this.activitys = activitys;
	}

	/**
	 * 获取属性：
	 *	玩家充值金额
	 */
	public long getPlayerChongZhi(){
		return playerChongZhi;
	}

	/**
	 * 设置属性：
	 *	玩家充值金额
	 */
	public void setPlayerChongZhi(long playerChongZhi){
		this.playerChongZhi = playerChongZhi;
	}

	/**
	 * 获取属性：
	 *	玩家满足条件次数
	 */
	public int getPlayerNum(){
		return playerNum;
	}

	/**
	 * 设置属性：
	 *	玩家满足条件次数
	 */
	public void setPlayerNum(int playerNum){
		this.playerNum = playerNum;
	}

	/**
	 * 获取属性：
	 *	是否领取过
	 */
	public boolean[] getIsGetRewards(){
		return isGetRewards;
	}

	/**
	 * 设置属性：
	 *	是否领取过
	 */
	public void setIsGetRewards(boolean[] isGetRewards){
		this.isGetRewards = isGetRewards;
	}

}