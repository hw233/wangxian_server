package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>continueDays</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>turnCardNums</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityHelp.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityHelp</td><td>String</td><td>activityHelp.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>stats.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>stats</td><td>int[]</td><td>stats.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>noticeMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>noticeMess</td><td>String</td><td>noticeMess.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class CONTINUE_ACTIVITY_MESS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int continueDays;
	int turnCardNums;
	String activityHelp;
	long[] ids;
	int[] stats;
	String noticeMess;

	public CONTINUE_ACTIVITY_MESS_RES(){
	}

	public CONTINUE_ACTIVITY_MESS_RES(long seqNum,int continueDays,int turnCardNums,String activityHelp,long[] ids,int[] stats,String noticeMess){
		this.seqNum = seqNum;
		this.continueDays = continueDays;
		this.turnCardNums = turnCardNums;
		this.activityHelp = activityHelp;
		this.ids = ids;
		this.stats = stats;
		this.noticeMess = noticeMess;
	}

	public CONTINUE_ACTIVITY_MESS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		continueDays = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		turnCardNums = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityHelp = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids = new long[len];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		stats = new int[len];
		for(int i = 0 ; i < stats.length ; i++){
			stats[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		noticeMess = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8E0EAE05;
	}

	public String getTypeDescription() {
		return "CONTINUE_ACTIVITY_MESS_RES";
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
		len += 4;
		len += 2;
		try{
			len +=activityHelp.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += stats.length * 4;
		len += 2;
		try{
			len +=noticeMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.putInt(continueDays);
			buffer.putInt(turnCardNums);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = activityHelp.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(stats.length);
			for(int i = 0 ; i < stats.length; i++){
				buffer.putInt(stats[i]);
			}
				try{
			tmpBytes1 = noticeMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	连续登录了几天
	 */
	public int getContinueDays(){
		return continueDays;
	}

	/**
	 * 设置属性：
	 *	连续登录了几天
	 */
	public void setContinueDays(int continueDays){
		this.continueDays = continueDays;
	}

	/**
	 * 获取属性：
	 *	翻拍次数
	 */
	public int getTurnCardNums(){
		return turnCardNums;
	}

	/**
	 * 设置属性：
	 *	翻拍次数
	 */
	public void setTurnCardNums(int turnCardNums){
		this.turnCardNums = turnCardNums;
	}

	/**
	 * 获取属性：
	 *	活动说明信息
	 */
	public String getActivityHelp(){
		return activityHelp;
	}

	/**
	 * 设置属性：
	 *	活动说明信息
	 */
	public void setActivityHelp(String activityHelp){
		this.activityHelp = activityHelp;
	}

	/**
	 * 获取属性：
	 *	九宫格物品id
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	九宫格物品id
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	九宫格物品状态(0:未翻开-1:已领取)
	 */
	public int[] getStats(){
		return stats;
	}

	/**
	 * 设置属性：
	 *	九宫格物品状态(0:未翻开-1:已领取)
	 */
	public void setStats(int[] stats){
		this.stats = stats;
	}

	/**
	 * 获取属性：
	 *	公告信息
	 */
	public String getNoticeMess(){
		return noticeMess;
	}

	/**
	 * 设置属性：
	 *	公告信息
	 */
	public void setNoticeMess(String noticeMess){
		this.noticeMess = noticeMess;
	}

}