package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 回答问题<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rightOrWrong</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>answerKey</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerIds</td><td>long[]</td><td>playerIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[0]</td><td>String</td><td>names[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[1]</td><td>String</td><td>names[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[2]</td><td>String</td><td>names[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>scores.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>scores</td><td>int[]</td><td>scores.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>remainTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>personScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>personRank</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>obtainCulture</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sumCulture</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class ANSWER_QUIZ_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean rightOrWrong;
	byte answerKey;
	long[] playerIds;
	String[] names;
	int[] scores;
	int remainTime;
	int personScore;
	int personRank;
	int obtainCulture;
	int sumCulture;

	public ANSWER_QUIZ_RES(){
	}

	public ANSWER_QUIZ_RES(long seqNum,boolean rightOrWrong,byte answerKey,long[] playerIds,String[] names,int[] scores,int remainTime,int personScore,int personRank,int obtainCulture,int sumCulture){
		this.seqNum = seqNum;
		this.rightOrWrong = rightOrWrong;
		this.answerKey = answerKey;
		this.playerIds = playerIds;
		this.names = names;
		this.scores = scores;
		this.remainTime = remainTime;
		this.personScore = personScore;
		this.personRank = personRank;
		this.obtainCulture = obtainCulture;
		this.sumCulture = sumCulture;
	}

	public ANSWER_QUIZ_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		rightOrWrong = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		answerKey = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerIds = new long[len];
		for(int i = 0 ; i < playerIds.length ; i++){
			playerIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		names = new String[len];
		for(int i = 0 ; i < names.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			names[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		scores = new int[len];
		for(int i = 0 ; i < scores.length ; i++){
			scores[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		remainTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		personScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		personRank = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		obtainCulture = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		sumCulture = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x700F0101;
	}

	public String getTypeDescription() {
		return "ANSWER_QUIZ_RES";
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
		len += 1;
		len += 4;
		len += playerIds.length * 8;
		len += 4;
		for(int i = 0 ; i < names.length; i++){
			len += 2;
			try{
				len += names[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += scores.length * 4;
		len += 4;
		len += 4;
		len += 4;
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

			buffer.put((byte)(rightOrWrong==false?0:1));
			buffer.put(answerKey);
			buffer.putInt(playerIds.length);
			for(int i = 0 ; i < playerIds.length; i++){
				buffer.putLong(playerIds[i]);
			}
			buffer.putInt(names.length);
			for(int i = 0 ; i < names.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = names[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(scores.length);
			for(int i = 0 ; i < scores.length; i++){
				buffer.putInt(scores[i]);
			}
			buffer.putInt(remainTime);
			buffer.putInt(personScore);
			buffer.putInt(personRank);
			buffer.putInt(obtainCulture);
			buffer.putInt(sumCulture);
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
	 *	回答对或者错误
	 */
	public boolean getRightOrWrong(){
		return rightOrWrong;
	}

	/**
	 * 设置属性：
	 *	回答对或者错误
	 */
	public void setRightOrWrong(boolean rightOrWrong){
		this.rightOrWrong = rightOrWrong;
	}

	/**
	 * 获取属性：
	 *	正确答案
	 */
	public byte getAnswerKey(){
		return answerKey;
	}

	/**
	 * 设置属性：
	 *	正确答案
	 */
	public void setAnswerKey(byte answerKey){
		this.answerKey = answerKey;
	}

	/**
	 * 获取属性：
	 *	玩家id
	 */
	public long[] getPlayerIds(){
		return playerIds;
	}

	/**
	 * 设置属性：
	 *	玩家id
	 */
	public void setPlayerIds(long[] playerIds){
		this.playerIds = playerIds;
	}

	/**
	 * 获取属性：
	 *	玩家的名字
	 */
	public String[] getNames(){
		return names;
	}

	/**
	 * 设置属性：
	 *	玩家的名字
	 */
	public void setNames(String[] names){
		this.names = names;
	}

	/**
	 * 获取属性：
	 *	得分情况
	 */
	public int[] getScores(){
		return scores;
	}

	/**
	 * 设置属性：
	 *	得分情况
	 */
	public void setScores(int[] scores){
		this.scores = scores;
	}

	/**
	 * 获取属性：
	 *	剩余时间(秒)
	 */
	public int getRemainTime(){
		return remainTime;
	}

	/**
	 * 设置属性：
	 *	剩余时间(秒)
	 */
	public void setRemainTime(int remainTime){
		this.remainTime = remainTime;
	}

	/**
	 * 获取属性：
	 *	个人当前得分
	 */
	public int getPersonScore(){
		return personScore;
	}

	/**
	 * 设置属性：
	 *	个人当前得分
	 */
	public void setPersonScore(int personScore){
		this.personScore = personScore;
	}

	/**
	 * 获取属性：
	 *	个人排名
	 */
	public int getPersonRank(){
		return personRank;
	}

	/**
	 * 设置属性：
	 *	个人排名
	 */
	public void setPersonRank(int personRank){
		this.personRank = personRank;
	}

	/**
	 * 获取属性：
	 *	得到的文采值
	 */
	public int getObtainCulture(){
		return obtainCulture;
	}

	/**
	 * 设置属性：
	 *	得到的文采值
	 */
	public void setObtainCulture(int obtainCulture){
		this.obtainCulture = obtainCulture;
	}

	/**
	 * 获取属性：
	 *	累计文采值
	 */
	public int getSumCulture(){
		return sumCulture;
	}

	/**
	 * 设置属性：
	 *	累计文采值
	 */
	public void setSumCulture(int sumCulture){
		this.sumCulture = sumCulture;
	}

}