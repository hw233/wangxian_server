package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.tournament.data.TournamentRewardDataClient;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 个人比武奖励面板<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>reward.rankRange.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>reward.rankRange</td><td>int[]</td><td>reward.rankRange.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>reward.rewardDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>reward.rewardDes</td><td>String</td><td>reward.rewardDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>reward.articleEntityIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>reward.articleEntityIds</td><td>long[]</td><td>reward.articleEntityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>reward.articleEntityCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>reward.articleEntityCounts</td><td>int[]</td><td>reward.articleEntityCounts.length</td><td>*</td></tr>
 * </table>
 */
public class TOURNAMENT_REWARD_SELF_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	TournamentRewardDataClient reward;

	public TOURNAMENT_REWARD_SELF_RES(){
	}

	public TOURNAMENT_REWARD_SELF_RES(long seqNum,TournamentRewardDataClient reward){
		this.seqNum = seqNum;
		this.reward = reward;
	}

	public TOURNAMENT_REWARD_SELF_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		reward = new TournamentRewardDataClient();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] rankRange_0001 = new int[len];
		for(int j = 0 ; j < rankRange_0001.length ; j++){
			rankRange_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		reward.setRankRange(rankRange_0001);
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		reward.setRewardDes(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		long[] articleEntityIds_0002 = new long[len];
		for(int j = 0 ; j < articleEntityIds_0002.length ; j++){
			articleEntityIds_0002[j] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		reward.setArticleEntityIds(articleEntityIds_0002);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] articleEntityCounts_0003 = new int[len];
		for(int j = 0 ; j < articleEntityCounts_0003.length ; j++){
			articleEntityCounts_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		reward.setArticleEntityCounts(articleEntityCounts_0003);
	}

	public int getType() {
		return 0x8F300128;
	}

	public String getTypeDescription() {
		return "TOURNAMENT_REWARD_SELF_RES";
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
		len += reward.getRankRange().length * 4;
		len += 2;
		if(reward.getRewardDes() != null){
			try{
			len += reward.getRewardDes().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += reward.getArticleEntityIds().length * 8;
		len += 4;
		len += reward.getArticleEntityCounts().length * 4;
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

			buffer.putInt(reward.getRankRange().length);
			int[] rankRange_0004 = reward.getRankRange();
			for(int j = 0 ; j < rankRange_0004.length ; j++){
				buffer.putInt(rankRange_0004[j]);
			}
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = reward.getRewardDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(reward.getArticleEntityIds().length);
			long[] articleEntityIds_0005 = reward.getArticleEntityIds();
			for(int j = 0 ; j < articleEntityIds_0005.length ; j++){
				buffer.putLong(articleEntityIds_0005[j]);
			}
			buffer.putInt(reward.getArticleEntityCounts().length);
			int[] articleEntityCounts_0006 = reward.getArticleEntityCounts();
			for(int j = 0 ; j < articleEntityCounts_0006.length ; j++){
				buffer.putInt(articleEntityCounts_0006[j]);
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
	 *	比武奖励
	 */
	public TournamentRewardDataClient getReward(){
		return reward;
	}

	/**
	 * 设置属性：
	 *	比武奖励
	 */
	public void setReward(TournamentRewardDataClient reward){
		this.reward = reward;
	}

}