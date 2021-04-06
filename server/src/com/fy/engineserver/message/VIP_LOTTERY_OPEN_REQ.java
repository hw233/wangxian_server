package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.shop.ActivityPropHasRate;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * VIP活动弹出转盘<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fixedGiven.articleNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fixedGiven.bind</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fixedGivenId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>randomGiven.length</td><td>int</td><td>4个字节</td><td>ActivityPropHasRate数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>randomGiven[0].articleNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>randomGiven[0].bind</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>randomGiven[1].articleNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>randomGiven[1].bind</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>randomGiven[2].articleNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>randomGiven[2].bind</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>randomGivenId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>randomGivenId</td><td>long[]</td><td>randomGivenId.length</td><td>*</td></tr>
 * </table>
 */
public class VIP_LOTTERY_OPEN_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ActivityPropHasRate fixedGiven;
	long fixedGivenId;
	ActivityPropHasRate[] randomGiven;
	long[] randomGivenId;

	public VIP_LOTTERY_OPEN_REQ(){
	}

	public VIP_LOTTERY_OPEN_REQ(long seqNum,ActivityPropHasRate fixedGiven,long fixedGivenId,ActivityPropHasRate[] randomGiven,long[] randomGivenId){
		this.seqNum = seqNum;
		this.fixedGiven = fixedGiven;
		this.fixedGivenId = fixedGivenId;
		this.randomGiven = randomGiven;
		this.randomGivenId = randomGivenId;
	}

	public VIP_LOTTERY_OPEN_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		fixedGiven = new ActivityPropHasRate();
		fixedGiven.setArticleNum((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		fixedGiven.setBind(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		fixedGivenId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		randomGiven = new ActivityPropHasRate[len];
		for(int i = 0 ; i < randomGiven.length ; i++){
			randomGiven[i] = new ActivityPropHasRate();
			randomGiven[i].setArticleNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			randomGiven[i].setBind(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		randomGivenId = new long[len];
		for(int i = 0 ; i < randomGivenId.length ; i++){
			randomGivenId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x00FFF001;
	}

	public String getTypeDescription() {
		return "VIP_LOTTERY_OPEN_REQ";
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
		len += 8;
		len += 4;
		for(int i = 0 ; i < randomGiven.length ; i++){
			len += 4;
			len += 1;
		}
		len += 4;
		len += randomGivenId.length * 8;
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

			buffer.putInt((int)fixedGiven.getArticleNum());
			buffer.put((byte)(fixedGiven.isBind()==false?0:1));
			buffer.putLong(fixedGivenId);
			buffer.putInt(randomGiven.length);
			for(int i = 0 ; i < randomGiven.length ; i++){
				buffer.putInt((int)randomGiven[i].getArticleNum());
				buffer.put((byte)(randomGiven[i].isBind()==false?0:1));
			}
			buffer.putInt(randomGivenId.length);
			for(int i = 0 ; i < randomGivenId.length; i++){
				buffer.putLong(randomGivenId[i]);
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
	 *	固定奖励
	 */
	public ActivityPropHasRate getFixedGiven(){
		return fixedGiven;
	}

	/**
	 * 设置属性：
	 *	固定奖励
	 */
	public void setFixedGiven(ActivityPropHasRate fixedGiven){
		this.fixedGiven = fixedGiven;
	}

	/**
	 * 获取属性：
	 *	固定奖励的物品ID
	 */
	public long getFixedGivenId(){
		return fixedGivenId;
	}

	/**
	 * 设置属性：
	 *	固定奖励的物品ID
	 */
	public void setFixedGivenId(long fixedGivenId){
		this.fixedGivenId = fixedGivenId;
	}

	/**
	 * 获取属性：
	 *	随机奖励
	 */
	public ActivityPropHasRate[] getRandomGiven(){
		return randomGiven;
	}

	/**
	 * 设置属性：
	 *	随机奖励
	 */
	public void setRandomGiven(ActivityPropHasRate[] randomGiven){
		this.randomGiven = randomGiven;
	}

	/**
	 * 获取属性：
	 *	随机奖励的物品IDs
	 */
	public long[] getRandomGivenId(){
		return randomGivenId;
	}

	/**
	 * 设置属性：
	 *	随机奖励的物品IDs
	 */
	public void setRandomGivenId(long[] randomGivenId){
		this.randomGivenId = randomGivenId;
	}

}