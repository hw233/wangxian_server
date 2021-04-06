package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端金猴天灾游戏结束<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerId</td><td>long[]</td><td>playerId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerNames[0]</td><td>String</td><td>playerNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerNames[1]</td><td>String</td><td>playerNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerNames[2]</td><td>String</td><td>playerNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>deadTimes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>deadTimes</td><td>int[]</td><td>deadTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardExp.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardExp</td><td>long[]</td><td>rewardExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardAeId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardAeId</td><td>long[]</td><td>rewardAeId.length</td><td>*</td></tr>
 * </table>
 */
public class DISASTER_END_INFO_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] playerId;
	String[] playerNames;
	int[] deadTimes;
	long[] rewardExp;
	long[] rewardAeId;

	public DISASTER_END_INFO_REQ(){
	}

	public DISASTER_END_INFO_REQ(long seqNum,long[] playerId,String[] playerNames,int[] deadTimes,long[] rewardExp,long[] rewardAeId){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.playerNames = playerNames;
		this.deadTimes = deadTimes;
		this.rewardExp = rewardExp;
		this.rewardAeId = rewardAeId;
	}

	public DISASTER_END_INFO_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerId = new long[len];
		for(int i = 0 ; i < playerId.length ; i++){
			playerId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerNames = new String[len];
		for(int i = 0 ; i < playerNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			playerNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		deadTimes = new int[len];
		for(int i = 0 ; i < deadTimes.length ; i++){
			deadTimes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardExp = new long[len];
		for(int i = 0 ; i < rewardExp.length ; i++){
			rewardExp[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardAeId = new long[len];
		for(int i = 0 ; i < rewardAeId.length ; i++){
			rewardAeId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70FF0151;
	}

	public String getTypeDescription() {
		return "DISASTER_END_INFO_REQ";
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
		len += playerId.length * 8;
		len += 4;
		for(int i = 0 ; i < playerNames.length; i++){
			len += 2;
			try{
				len += playerNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += deadTimes.length * 4;
		len += 4;
		len += rewardExp.length * 8;
		len += 4;
		len += rewardAeId.length * 8;
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

			buffer.putInt(playerId.length);
			for(int i = 0 ; i < playerId.length; i++){
				buffer.putLong(playerId[i]);
			}
			buffer.putInt(playerNames.length);
			for(int i = 0 ; i < playerNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = playerNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(deadTimes.length);
			for(int i = 0 ; i < deadTimes.length; i++){
				buffer.putInt(deadTimes[i]);
			}
			buffer.putInt(rewardExp.length);
			for(int i = 0 ; i < rewardExp.length; i++){
				buffer.putLong(rewardExp[i]);
			}
			buffer.putInt(rewardAeId.length);
			for(int i = 0 ; i < rewardAeId.length; i++){
				buffer.putLong(rewardAeId[i]);
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
	 *	角色Id列表
	 */
	public long[] getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	角色Id列表
	 */
	public void setPlayerId(long[] playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	角色名列表
	 */
	public String[] getPlayerNames(){
		return playerNames;
	}

	/**
	 * 设置属性：
	 *	角色名列表
	 */
	public void setPlayerNames(String[] playerNames){
		this.playerNames = playerNames;
	}

	/**
	 * 获取属性：
	 *	对应角色死亡次数
	 */
	public int[] getDeadTimes(){
		return deadTimes;
	}

	/**
	 * 设置属性：
	 *	对应角色死亡次数
	 */
	public void setDeadTimes(int[] deadTimes){
		this.deadTimes = deadTimes;
	}

	/**
	 * 获取属性：
	 *	对应获得的经验奖励数
	 */
	public long[] getRewardExp(){
		return rewardExp;
	}

	/**
	 * 设置属性：
	 *	对应获得的经验奖励数
	 */
	public void setRewardExp(long[] rewardExp){
		this.rewardExp = rewardExp;
	}

	/**
	 * 获取属性：
	 *	对应奖励道具Id(0代表没有物品奖励)
	 */
	public long[] getRewardAeId(){
		return rewardAeId;
	}

	/**
	 * 设置属性：
	 *	对应奖励道具Id(0代表没有物品奖励)
	 */
	public void setRewardAeId(long[] rewardAeId){
		this.rewardAeId = rewardAeId;
	}

}