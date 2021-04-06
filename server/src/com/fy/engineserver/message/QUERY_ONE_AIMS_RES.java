package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求单个目标信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aimId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>levelLimit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipLimit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipMul</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description</td><td>String</td><td>description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardArticles.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardArticles</td><td>long[]</td><td>rewardArticles.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardNums</td><td>long[]</td><td>rewardNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>receiveType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_ONE_AIMS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	int aimId;
	int levelLimit;
	int vipLimit;
	int vipMul;
	String description;
	long[] rewardArticles;
	long[] rewardNums;
	byte receiveType;

	public QUERY_ONE_AIMS_RES(){
	}

	public QUERY_ONE_AIMS_RES(long seqNum,long playerId,int aimId,int levelLimit,int vipLimit,int vipMul,String description,long[] rewardArticles,long[] rewardNums,byte receiveType){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.aimId = aimId;
		this.levelLimit = levelLimit;
		this.vipLimit = vipLimit;
		this.vipMul = vipMul;
		this.description = description;
		this.rewardArticles = rewardArticles;
		this.rewardNums = rewardNums;
		this.receiveType = receiveType;
	}

	public QUERY_ONE_AIMS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		aimId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		levelLimit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		vipLimit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		vipMul = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		description = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardArticles = new long[len];
		for(int i = 0 ; i < rewardArticles.length ; i++){
			rewardArticles[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardNums = new long[len];
		for(int i = 0 ; i < rewardNums.length ; i++){
			rewardNums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		receiveType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x70F0EF42;
	}

	public String getTypeDescription() {
		return "QUERY_ONE_AIMS_RES";
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
		len += 8;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=description.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += rewardArticles.length * 8;
		len += 4;
		len += rewardNums.length * 8;
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

			buffer.putLong(playerId);
			buffer.putInt(aimId);
			buffer.putInt(levelLimit);
			buffer.putInt(vipLimit);
			buffer.putInt(vipMul);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = description.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(rewardArticles.length);
			for(int i = 0 ; i < rewardArticles.length; i++){
				buffer.putLong(rewardArticles[i]);
			}
			buffer.putInt(rewardNums.length);
			for(int i = 0 ; i < rewardNums.length; i++){
				buffer.putLong(rewardNums[i]);
			}
			buffer.put(receiveType);
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
	 *	角色id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	目标id
	 */
	public int getAimId(){
		return aimId;
	}

	/**
	 * 设置属性：
	 *	目标id
	 */
	public void setAimId(int aimId){
		this.aimId = aimId;
	}

	/**
	 * 获取属性：
	 *	领取奖励最低等级
	 */
	public int getLevelLimit(){
		return levelLimit;
	}

	/**
	 * 设置属性：
	 *	领取奖励最低等级
	 */
	public void setLevelLimit(int levelLimit){
		this.levelLimit = levelLimit;
	}

	/**
	 * 获取属性：
	 *	领取vip奖励最低vip等级
	 */
	public int getVipLimit(){
		return vipLimit;
	}

	/**
	 * 设置属性：
	 *	领取vip奖励最低vip等级
	 */
	public void setVipLimit(int vipLimit){
		this.vipLimit = vipLimit;
	}

	/**
	 * 获取属性：
	 *	vip额外奖励倍数
	 */
	public int getVipMul(){
		return vipMul;
	}

	/**
	 * 设置属性：
	 *	vip额外奖励倍数
	 */
	public void setVipMul(int vipMul){
		this.vipMul = vipMul;
	}

	/**
	 * 获取属性：
	 *	引导描述
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	引导描述
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	章节奖励物品id列表
	 */
	public long[] getRewardArticles(){
		return rewardArticles;
	}

	/**
	 * 设置属性：
	 *	章节奖励物品id列表
	 */
	public void setRewardArticles(long[] rewardArticles){
		this.rewardArticles = rewardArticles;
	}

	/**
	 * 获取属性：
	 *	对应奖励物品数量
	 */
	public long[] getRewardNums(){
		return rewardNums;
	}

	/**
	 * 设置属性：
	 *	对应奖励物品数量
	 */
	public void setRewardNums(long[] rewardNums){
		this.rewardNums = rewardNums;
	}

	/**
	 * 获取属性：
	 *	章节奖励领取状态（-1未达成条件  0可领取  1已普通领取  2已vip领取  3vip+普通都已领取）
	 */
	public byte getReceiveType(){
		return receiveType;
	}

	/**
	 * 设置属性：
	 *	章节奖励领取状态（-1未达成条件  0可领取  1已普通领取  2已vip领取  3vip+普通都已领取）
	 */
	public void setReceiveType(byte receiveType){
		this.receiveType = receiveType;
	}

}