package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.qiancengta.info.RewardMsg;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 手动爬塔胜利后<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>daoIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>daoName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>daoName</td><td>String</td><td>daoName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cengMsg.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cengMsg</td><td>String</td><td>cengMsg.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardsExp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards.cengIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards.rewardid.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards.rewardid</td><td>long[]</td><td>rewards.rewardid.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards.nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards.nums</td><td>int[]</td><td>rewards.nums.length</td><td>*</td></tr>
 * </table>
 */
public class QIANCHENGTA_MANUAL_OVER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int daoIndex;
	String daoName;
	String cengMsg;
	int rewardsExp;
	RewardMsg rewards;

	public QIANCHENGTA_MANUAL_OVER_RES(){
	}

	public QIANCHENGTA_MANUAL_OVER_RES(long seqNum,int daoIndex,String daoName,String cengMsg,int rewardsExp,RewardMsg rewards){
		this.seqNum = seqNum;
		this.daoIndex = daoIndex;
		this.daoName = daoName;
		this.cengMsg = cengMsg;
		this.rewardsExp = rewardsExp;
		this.rewards = rewards;
	}

	public QIANCHENGTA_MANUAL_OVER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		daoIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		daoName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		cengMsg = new String(content,offset,len,"UTF-8");
		offset += len;
		rewardsExp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		rewards = new RewardMsg();
		rewards.setCengIndex((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		long[] rewardid_0001 = new long[len];
		for(int j = 0 ; j < rewardid_0001.length ; j++){
			rewardid_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		rewards.setRewardid(rewardid_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] nums_0002 = new int[len];
		for(int j = 0 ; j < nums_0002.length ; j++){
			nums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		rewards.setNums(nums_0002);
	}

	public int getType() {
		return 0x8F700010;
	}

	public String getTypeDescription() {
		return "QIANCHENGTA_MANUAL_OVER_RES";
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
			len +=daoName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=cengMsg.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += rewards.getRewardid().length * 8;
		len += 4;
		len += rewards.getNums().length * 4;
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
				try{
			tmpBytes1 = cengMsg.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(rewardsExp);
			buffer.putInt((int)rewards.getCengIndex());
			buffer.putInt(rewards.getRewardid().length);
			long[] rewardid_0003 = rewards.getRewardid();
			for(int j = 0 ; j < rewardid_0003.length ; j++){
				buffer.putLong(rewardid_0003[j]);
			}
			buffer.putInt(rewards.getNums().length);
			int[] nums_0004 = rewards.getNums();
			for(int j = 0 ; j < nums_0004.length ; j++){
				buffer.putInt(nums_0004[j]);
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
	 *	道的下标，从0开始
	 */
	public int getDaoIndex(){
		return daoIndex;
	}

	/**
	 * 设置属性：
	 *	道的下标，从0开始
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
	 *	信息
	 */
	public String getCengMsg(){
		return cengMsg;
	}

	/**
	 * 设置属性：
	 *	信息
	 */
	public void setCengMsg(String cengMsg){
		this.cengMsg = cengMsg;
	}

	/**
	 * 获取属性：
	 *	奖励经验
	 */
	public int getRewardsExp(){
		return rewardsExp;
	}

	/**
	 * 设置属性：
	 *	奖励经验
	 */
	public void setRewardsExp(int rewardsExp){
		this.rewardsExp = rewardsExp;
	}

	/**
	 * 获取属性：
	 *	奖励
	 */
	public RewardMsg getRewards(){
		return rewards;
	}

	/**
	 * 设置属性：
	 *	奖励
	 */
	public void setRewards(RewardMsg rewards){
		this.rewards = rewards;
	}

}