package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求所有目标章节名（顺序）<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterNames[0]</td><td>String</td><td>chapterNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterNames[1]</td><td>String</td><td>chapterNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterNames[2]</td><td>String</td><td>chapterNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterSubTitle.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterSubTitle[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterSubTitle[0]</td><td>String</td><td>chapterSubTitle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterSubTitle[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterSubTitle[1]</td><td>String</td><td>chapterSubTitle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterSubTitle[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterSubTitle[2]</td><td>String</td><td>chapterSubTitle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totalChapterScore.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totalChapterScore</td><td>int[]</td><td>totalChapterScore.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentChapterScore.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentChapterScore</td><td>int[]</td><td>currentChapterScore.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>canReceive.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canReceive</td><td>int[]</td><td>canReceive.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentChapterName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentChapterName</td><td>String</td><td>currentChapterName.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class QUERY_ALL_AIMS_CHAPTER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	String[] chapterNames;
	String[] chapterSubTitle;
	int[] totalChapterScore;
	int[] currentChapterScore;
	int[] canReceive;
	int allScore;
	int currentScore;
	String currentChapterName;

	public QUERY_ALL_AIMS_CHAPTER_RES(){
	}

	public QUERY_ALL_AIMS_CHAPTER_RES(long seqNum,long playerId,String[] chapterNames,String[] chapterSubTitle,int[] totalChapterScore,int[] currentChapterScore,int[] canReceive,int allScore,int currentScore,String currentChapterName){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.chapterNames = chapterNames;
		this.chapterSubTitle = chapterSubTitle;
		this.totalChapterScore = totalChapterScore;
		this.currentChapterScore = currentChapterScore;
		this.canReceive = canReceive;
		this.allScore = allScore;
		this.currentScore = currentScore;
		this.currentChapterName = currentChapterName;
	}

	public QUERY_ALL_AIMS_CHAPTER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		chapterNames = new String[len];
		for(int i = 0 ; i < chapterNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			chapterNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		chapterSubTitle = new String[len];
		for(int i = 0 ; i < chapterSubTitle.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			chapterSubTitle[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		totalChapterScore = new int[len];
		for(int i = 0 ; i < totalChapterScore.length ; i++){
			totalChapterScore[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		currentChapterScore = new int[len];
		for(int i = 0 ; i < currentChapterScore.length ; i++){
			currentChapterScore[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		canReceive = new int[len];
		for(int i = 0 ; i < canReceive.length ; i++){
			canReceive[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		allScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currentScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		currentChapterName = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x70F0EF40;
	}

	public String getTypeDescription() {
		return "QUERY_ALL_AIMS_CHAPTER_RES";
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
		for(int i = 0 ; i < chapterNames.length; i++){
			len += 2;
			try{
				len += chapterNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < chapterSubTitle.length; i++){
			len += 2;
			try{
				len += chapterSubTitle[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += totalChapterScore.length * 4;
		len += 4;
		len += currentChapterScore.length * 4;
		len += 4;
		len += canReceive.length * 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=currentChapterName.getBytes("UTF-8").length;
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

			buffer.putLong(playerId);
			buffer.putInt(chapterNames.length);
			for(int i = 0 ; i < chapterNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = chapterNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(chapterSubTitle.length);
			for(int i = 0 ; i < chapterSubTitle.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = chapterSubTitle[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(totalChapterScore.length);
			for(int i = 0 ; i < totalChapterScore.length; i++){
				buffer.putInt(totalChapterScore[i]);
			}
			buffer.putInt(currentChapterScore.length);
			for(int i = 0 ; i < currentChapterScore.length; i++){
				buffer.putInt(currentChapterScore[i]);
			}
			buffer.putInt(canReceive.length);
			for(int i = 0 ; i < canReceive.length; i++){
				buffer.putInt(canReceive[i]);
			}
			buffer.putInt(allScore);
			buffer.putInt(currentScore);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = currentChapterName.getBytes("UTF-8");
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
	 *	顺序排放(章节名)
	 */
	public String[] getChapterNames(){
		return chapterNames;
	}

	/**
	 * 设置属性：
	 *	顺序排放(章节名)
	 */
	public void setChapterNames(String[] chapterNames){
		this.chapterNames = chapterNames;
	}

	/**
	 * 获取属性：
	 *	章节副标题(章节当前分数)
	 */
	public String[] getChapterSubTitle(){
		return chapterSubTitle;
	}

	/**
	 * 设置属性：
	 *	章节副标题(章节当前分数)
	 */
	public void setChapterSubTitle(String[] chapterSubTitle){
		this.chapterSubTitle = chapterSubTitle;
	}

	/**
	 * 获取属性：
	 *	顺序排放(章节总分数)
	 */
	public int[] getTotalChapterScore(){
		return totalChapterScore;
	}

	/**
	 * 设置属性：
	 *	顺序排放(章节总分数)
	 */
	public void setTotalChapterScore(int[] totalChapterScore){
		this.totalChapterScore = totalChapterScore;
	}

	/**
	 * 获取属性：
	 *	顺序排放(章节当前分数)
	 */
	public int[] getCurrentChapterScore(){
		return currentChapterScore;
	}

	/**
	 * 设置属性：
	 *	顺序排放(章节当前分数)
	 */
	public void setCurrentChapterScore(int[] currentChapterScore){
		this.currentChapterScore = currentChapterScore;
	}

	/**
	 * 获取属性：
	 *	可领取个数(章节当前分数)
	 */
	public int[] getCanReceive(){
		return canReceive;
	}

	/**
	 * 设置属性：
	 *	可领取个数(章节当前分数)
	 */
	public void setCanReceive(int[] canReceive){
		this.canReceive = canReceive;
	}

	/**
	 * 获取属性：
	 *	所有目标加起来可获得的总积分
	 */
	public int getAllScore(){
		return allScore;
	}

	/**
	 * 设置属性：
	 *	所有目标加起来可获得的总积分
	 */
	public void setAllScore(int allScore){
		this.allScore = allScore;
	}

	/**
	 * 获取属性：
	 *	当前获得的总积分
	 */
	public int getCurrentScore(){
		return currentScore;
	}

	/**
	 * 设置属性：
	 *	当前获得的总积分
	 */
	public void setCurrentScore(int currentScore){
		this.currentScore = currentScore;
	}

	/**
	 * 获取属性：
	 *	当前所在章节名
	 */
	public String getCurrentChapterName(){
		return currentChapterName;
	}

	/**
	 * 设置属性：
	 *	当前所在章节名
	 */
	public void setCurrentChapterName(String currentChapterName){
		this.currentChapterName = currentChapterName;
	}

}