package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开极地寻宝界面窗口<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description</td><td>String</td><td>description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>treasureNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>treasureNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>treasureNames[0]</td><td>String</td><td>treasureNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>treasureNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>treasureNames[1]</td><td>String</td><td>treasureNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>treasureNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>treasureNames[2]</td><td>String</td><td>treasureNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>treasureIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>treasureIds</td><td>int[]</td><td>treasureIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>digTimes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>digTimes</td><td>int[]</td><td>digTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costSilvers.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costSilvers</td><td>long[]</td><td>costSilvers.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defaultArticleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defaultArticleIds</td><td>long[]</td><td>defaultArticleIds.length</td><td>*</td></tr>
 * </table>
 */
public class OPEN_TREASUREACTIVITY_WINDOW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long leftTime;
	String description;
	String[] treasureNames;
	int[] treasureIds;
	int[] digTimes;
	long[] costSilvers;
	long[] defaultArticleIds;

	public OPEN_TREASUREACTIVITY_WINDOW_RES(){
	}

	public OPEN_TREASUREACTIVITY_WINDOW_RES(long seqNum,long leftTime,String description,String[] treasureNames,int[] treasureIds,int[] digTimes,long[] costSilvers,long[] defaultArticleIds){
		this.seqNum = seqNum;
		this.leftTime = leftTime;
		this.description = description;
		this.treasureNames = treasureNames;
		this.treasureIds = treasureIds;
		this.digTimes = digTimes;
		this.costSilvers = costSilvers;
		this.defaultArticleIds = defaultArticleIds;
	}

	public OPEN_TREASUREACTIVITY_WINDOW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		leftTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		description = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		treasureNames = new String[len];
		for(int i = 0 ; i < treasureNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			treasureNames[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		treasureIds = new int[len];
		for(int i = 0 ; i < treasureIds.length ; i++){
			treasureIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		digTimes = new int[len];
		for(int i = 0 ; i < digTimes.length ; i++){
			digTimes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		costSilvers = new long[len];
		for(int i = 0 ; i < costSilvers.length ; i++){
			costSilvers[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		defaultArticleIds = new long[len];
		for(int i = 0 ; i < defaultArticleIds.length ; i++){
			defaultArticleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70FF0076;
	}

	public String getTypeDescription() {
		return "OPEN_TREASUREACTIVITY_WINDOW_RES";
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
		len += 2;
		len +=description.getBytes().length;
		len += 4;
		for(int i = 0 ; i < treasureNames.length; i++){
			len += 2;
			len += treasureNames[i].getBytes().length;
		}
		len += 4;
		len += treasureIds.length * 4;
		len += 4;
		len += digTimes.length * 4;
		len += 4;
		len += costSilvers.length * 8;
		len += 4;
		len += defaultArticleIds.length * 8;
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

			buffer.putLong(leftTime);
			byte[] tmpBytes1;
			tmpBytes1 = description.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(treasureNames.length);
			for(int i = 0 ; i < treasureNames.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = treasureNames[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(treasureIds.length);
			for(int i = 0 ; i < treasureIds.length; i++){
				buffer.putInt(treasureIds[i]);
			}
			buffer.putInt(digTimes.length);
			for(int i = 0 ; i < digTimes.length; i++){
				buffer.putInt(digTimes[i]);
			}
			buffer.putInt(costSilvers.length);
			for(int i = 0 ; i < costSilvers.length; i++){
				buffer.putLong(costSilvers[i]);
			}
			buffer.putInt(defaultArticleIds.length);
			for(int i = 0 ; i < defaultArticleIds.length; i++){
				buffer.putLong(defaultArticleIds[i]);
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
	 *	活动剩余时间，单位：秒
	 */
	public long getLeftTime(){
		return leftTime;
	}

	/**
	 * 设置属性：
	 *	活动剩余时间，单位：秒
	 */
	public void setLeftTime(long leftTime){
		this.leftTime = leftTime;
	}

	/**
	 * 获取属性：
	 *	玩法说明
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	玩法说明
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	宝藏标签名  
	 */
	public String[] getTreasureNames(){
		return treasureNames;
	}

	/**
	 * 设置属性：
	 *	宝藏标签名  
	 */
	public void setTreasureNames(String[] treasureNames){
		this.treasureNames = treasureNames;
	}

	/**
	 * 获取属性：
	 *	宝藏标签id
	 */
	public int[] getTreasureIds(){
		return treasureIds;
	}

	/**
	 * 设置属性：
	 *	宝藏标签id
	 */
	public void setTreasureIds(int[] treasureIds){
		this.treasureIds = treasureIds;
	}

	/**
	 * 获取属性：
	 *	一次性挖取的次数
	 */
	public int[] getDigTimes(){
		return digTimes;
	}

	/**
	 * 设置属性：
	 *	一次性挖取的次数
	 */
	public void setDigTimes(int[] digTimes){
		this.digTimes = digTimes;
	}

	/**
	 * 获取属性：
	 *	对应次数消耗银子数量
	 */
	public long[] getCostSilvers(){
		return costSilvers;
	}

	/**
	 * 设置属性：
	 *	对应次数消耗银子数量
	 */
	public void setCostSilvers(long[] costSilvers){
		this.costSilvers = costSilvers;
	}

	/**
	 * 获取属性：
	 *	默认界面展示的物品id列表
	 */
	public long[] getDefaultArticleIds(){
		return defaultArticleIds;
	}

	/**
	 * 设置属性：
	 *	默认界面展示的物品id列表
	 */
	public void setDefaultArticleIds(long[] defaultArticleIds){
		this.defaultArticleIds = defaultArticleIds;
	}

}