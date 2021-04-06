package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 某个成就系列查询<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>achievementSeriesId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>achievementIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>achievementIds</td><td>int[]</td><td>achievementIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>achievementNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>achievementNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>achievementNames[0]</td><td>String</td><td>achievementNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>achievementNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>achievementNames[1]</td><td>String</td><td>achievementNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>achievementNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>achievementNames[2]</td><td>String</td><td>achievementNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>colors.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>colors</td><td>int[]</td><td>colors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentPage</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allPage</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_ACHIEVEMENT_SERIES_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int achievementSeriesId;
	int[] achievementIds;
	String[] achievementNames;
	int[] colors;
	int currentPage;
	int allPage;

	public QUERY_ACHIEVEMENT_SERIES_RES(){
	}

	public QUERY_ACHIEVEMENT_SERIES_RES(long seqNum,int achievementSeriesId,int[] achievementIds,String[] achievementNames,int[] colors,int currentPage,int allPage){
		this.seqNum = seqNum;
		this.achievementSeriesId = achievementSeriesId;
		this.achievementIds = achievementIds;
		this.achievementNames = achievementNames;
		this.colors = colors;
		this.currentPage = currentPage;
		this.allPage = allPage;
	}

	public QUERY_ACHIEVEMENT_SERIES_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		achievementSeriesId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		achievementIds = new int[len];
		for(int i = 0 ; i < achievementIds.length ; i++){
			achievementIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		achievementNames = new String[len];
		for(int i = 0 ; i < achievementNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			achievementNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		colors = new int[len];
		for(int i = 0 ; i < colors.length ; i++){
			colors[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		currentPage = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		allPage = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x7001FF02;
	}

	public String getTypeDescription() {
		return "QUERY_ACHIEVEMENT_SERIES_RES";
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
		len += achievementIds.length * 4;
		len += 4;
		for(int i = 0 ; i < achievementNames.length; i++){
			len += 2;
			try{
				len += achievementNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += colors.length * 4;
		len += 4;
		len += 4;
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

			buffer.putInt(achievementSeriesId);
			buffer.putInt(achievementIds.length);
			for(int i = 0 ; i < achievementIds.length; i++){
				buffer.putInt(achievementIds[i]);
			}
			buffer.putInt(achievementNames.length);
			for(int i = 0 ; i < achievementNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = achievementNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(colors.length);
			for(int i = 0 ; i < colors.length; i++){
				buffer.putInt(colors[i]);
			}
			buffer.putInt(currentPage);
			buffer.putInt(allPage);
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
	 *	成就系列id
	 */
	public int getAchievementSeriesId(){
		return achievementSeriesId;
	}

	/**
	 * 设置属性：
	 *	成就系列id
	 */
	public void setAchievementSeriesId(int achievementSeriesId){
		this.achievementSeriesId = achievementSeriesId;
	}

	/**
	 * 获取属性：
	 *	成就id数组
	 */
	public int[] getAchievementIds(){
		return achievementIds;
	}

	/**
	 * 设置属性：
	 *	成就id数组
	 */
	public void setAchievementIds(int[] achievementIds){
		this.achievementIds = achievementIds;
	}

	/**
	 * 获取属性：
	 *	成就名字数组
	 */
	public String[] getAchievementNames(){
		return achievementNames;
	}

	/**
	 * 设置属性：
	 *	成就名字数组
	 */
	public void setAchievementNames(String[] achievementNames){
		this.achievementNames = achievementNames;
	}

	/**
	 * 获取属性：
	 *	成就颜色数组
	 */
	public int[] getColors(){
		return colors;
	}

	/**
	 * 设置属性：
	 *	成就颜色数组
	 */
	public void setColors(int[] colors){
		this.colors = colors;
	}

	/**
	 * 获取属性：
	 *	当前页
	 */
	public int getCurrentPage(){
		return currentPage;
	}

	/**
	 * 设置属性：
	 *	当前页
	 */
	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}

	/**
	 * 获取属性：
	 *	所有页数
	 */
	public int getAllPage(){
		return allPage;
	}

	/**
	 * 设置属性：
	 *	所有页数
	 */
	public void setAllPage(int allPage){
		this.allPage = allPage;
	}

}