package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.tournament.data.TournamentRewardDataClient;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 比武奖励面板<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canTakeReward</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards.length</td><td>int</td><td>4个字节</td><td>TournamentRewardDataClient数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[0].rankRange.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[0].rankRange</td><td>int[]</td><td>rewards[0].rankRange.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[0].rewardDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[0].rewardDes</td><td>String</td><td>rewards[0].rewardDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[0].articleEntityIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[0].articleEntityIds</td><td>long[]</td><td>rewards[0].articleEntityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[0].articleEntityCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[0].articleEntityCounts</td><td>int[]</td><td>rewards[0].articleEntityCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[1].rankRange.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[1].rankRange</td><td>int[]</td><td>rewards[1].rankRange.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[1].rewardDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[1].rewardDes</td><td>String</td><td>rewards[1].rewardDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[1].articleEntityIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[1].articleEntityIds</td><td>long[]</td><td>rewards[1].articleEntityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[1].articleEntityCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[1].articleEntityCounts</td><td>int[]</td><td>rewards[1].articleEntityCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[2].rankRange.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[2].rankRange</td><td>int[]</td><td>rewards[2].rankRange.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[2].rewardDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[2].rewardDes</td><td>String</td><td>rewards[2].rewardDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[2].articleEntityIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[2].articleEntityIds</td><td>long[]</td><td>rewards[2].articleEntityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewards[2].articleEntityCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewards[2].articleEntityCounts</td><td>int[]</td><td>rewards[2].articleEntityCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class TOURNAMENT_REWARD_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean canTakeReward;
	TournamentRewardDataClient[] rewards;

	public TOURNAMENT_REWARD_RES(){
	}

	public TOURNAMENT_REWARD_RES(long seqNum,boolean canTakeReward,TournamentRewardDataClient[] rewards){
		this.seqNum = seqNum;
		this.canTakeReward = canTakeReward;
		this.rewards = rewards;
	}

	public TOURNAMENT_REWARD_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		canTakeReward = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		rewards = new TournamentRewardDataClient[len];
		for(int i = 0 ; i < rewards.length ; i++){
			rewards[i] = new TournamentRewardDataClient();
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] rankRange_0001 = new int[len];
			for(int j = 0 ; j < rankRange_0001.length ; j++){
				rankRange_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			rewards[i].setRankRange(rankRange_0001);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			rewards[i].setRewardDes(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] articleEntityIds_0002 = new long[len];
			for(int j = 0 ; j < articleEntityIds_0002.length ; j++){
				articleEntityIds_0002[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			rewards[i].setArticleEntityIds(articleEntityIds_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] articleEntityCounts_0003 = new int[len];
			for(int j = 0 ; j < articleEntityCounts_0003.length ; j++){
				articleEntityCounts_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			rewards[i].setArticleEntityCounts(articleEntityCounts_0003);
		}
	}

	public int getType() {
		return 0x8F300027;
	}

	public String getTypeDescription() {
		return "TOURNAMENT_REWARD_RES";
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
		len += 4;
		for(int i = 0 ; i < rewards.length ; i++){
			len += 4;
			len += rewards[i].getRankRange().length * 4;
			len += 2;
			if(rewards[i].getRewardDes() != null){
				try{
				len += rewards[i].getRewardDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += rewards[i].getArticleEntityIds().length * 8;
			len += 4;
			len += rewards[i].getArticleEntityCounts().length * 4;
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

			buffer.put((byte)(canTakeReward==false?0:1));
			buffer.putInt(rewards.length);
			for(int i = 0 ; i < rewards.length ; i++){
				buffer.putInt(rewards[i].getRankRange().length);
				int[] rankRange_0004 = rewards[i].getRankRange();
				for(int j = 0 ; j < rankRange_0004.length ; j++){
					buffer.putInt(rankRange_0004[j]);
				}
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = rewards[i].getRewardDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(rewards[i].getArticleEntityIds().length);
				long[] articleEntityIds_0005 = rewards[i].getArticleEntityIds();
				for(int j = 0 ; j < articleEntityIds_0005.length ; j++){
					buffer.putLong(articleEntityIds_0005[j]);
				}
				buffer.putInt(rewards[i].getArticleEntityCounts().length);
				int[] articleEntityCounts_0006 = rewards[i].getArticleEntityCounts();
				for(int j = 0 ; j < articleEntityCounts_0006.length ; j++){
					buffer.putInt(articleEntityCounts_0006[j]);
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
	 *	是否可以领取比武奖励
	 */
	public boolean getCanTakeReward(){
		return canTakeReward;
	}

	/**
	 * 设置属性：
	 *	是否可以领取比武奖励
	 */
	public void setCanTakeReward(boolean canTakeReward){
		this.canTakeReward = canTakeReward;
	}

	/**
	 * 获取属性：
	 *	比武奖励数组
	 */
	public TournamentRewardDataClient[] getRewards(){
		return rewards;
	}

	/**
	 * 设置属性：
	 *	比武奖励数组
	 */
	public void setRewards(TournamentRewardDataClient[] rewards){
		this.rewards = rewards;
	}

}