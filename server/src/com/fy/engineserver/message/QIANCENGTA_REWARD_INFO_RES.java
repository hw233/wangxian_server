package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.qiancengta.info.RewardMsg;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 取千层塔某道的奖励<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards.length</td><td>int</td><td>4个字节</td><td>RewardMsg数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[0].cengIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[0].rewardid.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[0].rewardid</td><td>long[]</td><td>rewards[0].rewardid.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[0].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[0].nums</td><td>int[]</td><td>rewards[0].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[1].cengIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[1].rewardid.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[1].rewardid</td><td>long[]</td><td>rewards[1].rewardid.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[1].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[1].nums</td><td>int[]</td><td>rewards[1].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[2].cengIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[2].rewardid.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[2].rewardid</td><td>long[]</td><td>rewards[2].rewardid.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[2].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[2].nums</td><td>int[]</td><td>rewards[2].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QIANCENGTA_REWARD_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	RewardMsg[] rewards;

	public QIANCENGTA_REWARD_INFO_RES(){
	}

	public QIANCENGTA_REWARD_INFO_RES(long seqNum,RewardMsg[] rewards){
		this.seqNum = seqNum;
		this.rewards = rewards;
	}

	public QIANCENGTA_REWARD_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		rewards = new RewardMsg[len];
		for(int i = 0 ; i < rewards.length ; i++){
			rewards[i] = new RewardMsg();
			rewards[i].setCengIndex((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] rewardid_0001 = new long[len];
			for(int j = 0 ; j < rewardid_0001.length ; j++){
				rewardid_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			rewards[i].setRewardid(rewardid_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] nums_0002 = new int[len];
			for(int j = 0 ; j < nums_0002.length ; j++){
				nums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			rewards[i].setNums(nums_0002);
		}
	}

	public int getType() {
		return 0x8F700007;
	}

	public String getTypeDescription() {
		return "QIANCENGTA_REWARD_INFO_RES";
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
		for(int i = 0 ; i < rewards.length ; i++){
			len += 4;
			len += 4;
			len += rewards[i].getRewardid().length * 8;
			len += 4;
			len += rewards[i].getNums().length * 4;
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

			buffer.putInt(rewards.length);
			for(int i = 0 ; i < rewards.length ; i++){
				buffer.putInt((int)rewards[i].getCengIndex());
				buffer.putInt(rewards[i].getRewardid().length);
				long[] rewardid_0003 = rewards[i].getRewardid();
				for(int j = 0 ; j < rewardid_0003.length ; j++){
					buffer.putLong(rewardid_0003[j]);
				}
				buffer.putInt(rewards[i].getNums().length);
				int[] nums_0004 = rewards[i].getNums();
				for(int j = 0 ; j < nums_0004.length ; j++){
					buffer.putInt(nums_0004[j]);
				}
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
	 *	奖励
	 */
	public RewardMsg[] getRewards(){
		return rewards;
	}

	/**
	 * 设置属性：
	 *	奖励
	 */
	public void setRewards(RewardMsg[] rewards){
		this.rewards = rewards;
	}

}