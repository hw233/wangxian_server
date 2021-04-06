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
 * <tr bgcolor="#FAFAFA" align="center"><td>contentMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>contentMess</td><td>String</td><td>contentMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gameType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentlevel</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>difficulty</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initDataArr.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initDataArr[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initDataArr[0]</td><td>String</td><td>initDataArr[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initDataArr[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initDataArr[1]</td><td>String</td><td>initDataArr[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initDataArr[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initDataArr[2]</td><td>String</td><td>initDataArr[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totleScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totalLife</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>life</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>duration</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>scores.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>scores</td><td>int[]</td><td>scores.length</td><td>*</td></tr>
 * </table>
 */
public class INITDATA_MINIGAME_ACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String contentMess;
	byte gameType;
	byte currentlevel;
	byte difficulty;
	String[] initDataArr;
	int totleScore;
	int totalLife;
	int life;
	int duration;
	int[] scores;

	public INITDATA_MINIGAME_ACTIVITY_RES(){
	}

	public INITDATA_MINIGAME_ACTIVITY_RES(long seqNum,String contentMess,byte gameType,byte currentlevel,byte difficulty,String[] initDataArr,int totleScore,int totalLife,int life,int duration,int[] scores){
		this.seqNum = seqNum;
		this.contentMess = contentMess;
		this.gameType = gameType;
		this.currentlevel = currentlevel;
		this.difficulty = difficulty;
		this.initDataArr = initDataArr;
		this.totleScore = totleScore;
		this.totalLife = totalLife;
		this.life = life;
		this.duration = duration;
		this.scores = scores;
	}

	public INITDATA_MINIGAME_ACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		contentMess = new String(content,offset,len,"UTF-8");
		offset += len;
		gameType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		currentlevel = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		difficulty = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		initDataArr = new String[len];
		for(int i = 0 ; i < initDataArr.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			initDataArr[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		totleScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		totalLife = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		life = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		duration = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		scores = new int[len];
		for(int i = 0 ; i < scores.length ; i++){
			scores[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x8E0EAA30;
	}

	public String getTypeDescription() {
		return "INITDATA_MINIGAME_ACTIVITY_RES";
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
		len += 2;
		try{
			len +=contentMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 1;
		len += 1;
		len += 4;
		for(int i = 0 ; i < initDataArr.length; i++){
			len += 2;
			try{
				len += initDataArr[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += scores.length * 4;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = contentMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(gameType);
			buffer.put(currentlevel);
			buffer.put(difficulty);
			buffer.putInt(initDataArr.length);
			for(int i = 0 ; i < initDataArr.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = initDataArr[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(totleScore);
			buffer.putInt(totalLife);
			buffer.putInt(life);
			buffer.putInt(duration);
			buffer.putInt(scores.length);
			for(int i = 0 ; i < scores.length; i++){
				buffer.putInt(scores[i]);
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
	 *	提示内容---保留，用不用待确定
	 */
	public String getContentMess(){
		return contentMess;
	}

	/**
	 * 设置属性：
	 *	提示内容---保留，用不用待确定
	 */
	public void setContentMess(String contentMess){
		this.contentMess = contentMess;
	}

	/**
	 * 获取属性：
	 *	拼图游戏(0),记忆力游戏(1),对对碰(2),管道(3)
	 */
	public byte getGameType(){
		return gameType;
	}

	/**
	 * 设置属性：
	 *	拼图游戏(0),记忆力游戏(1),对对碰(2),管道(3)
	 */
	public void setGameType(byte gameType){
		this.gameType = gameType;
	}

	/**
	 * 获取属性：
	 *	当前关卡level
	 */
	public byte getCurrentlevel(){
		return currentlevel;
	}

	/**
	 * 设置属性：
	 *	当前关卡level
	 */
	public void setCurrentlevel(byte currentlevel){
		this.currentlevel = currentlevel;
	}

	/**
	 * 获取属性：
	 *	关卡难度,1简单 2中等 3困难
	 */
	public byte getDifficulty(){
		return difficulty;
	}

	/**
	 * 设置属性：
	 *	关卡难度,1简单 2中等 3困难
	 */
	public void setDifficulty(byte difficulty){
		this.difficulty = difficulty;
	}

	/**
	 * 获取属性：
	 *	(记忆游戏前两位为记忆时间和操作时间)初始化数据数组（拼图存储数字序列、其他存储icon名。所有都按照后端初始化的顺序排列）     【对于拼图游戏，initDataArr[0]为场景名称,initDataArr[1]为x坐标,initDataArr[2]为y坐标】 initDataArr[3]为切块数(横)initDataArr[4]为切块数(竖)     【对于记忆类游戏，initDataArr[0]为记忆时间,initDataArr[1]为操作时间,最后一位是操作说明;len=initDataArr[2]默认宝石数量--initDataArr[2]-[len]----initDataArr[2+[len]记忆数量，之后为随机出来的记忆宝石序列】
	 */
	public String[] getInitDataArr(){
		return initDataArr;
	}

	/**
	 * 设置属性：
	 *	(记忆游戏前两位为记忆时间和操作时间)初始化数据数组（拼图存储数字序列、其他存储icon名。所有都按照后端初始化的顺序排列）     【对于拼图游戏，initDataArr[0]为场景名称,initDataArr[1]为x坐标,initDataArr[2]为y坐标】 initDataArr[3]为切块数(横)initDataArr[4]为切块数(竖)     【对于记忆类游戏，initDataArr[0]为记忆时间,initDataArr[1]为操作时间,最后一位是操作说明;len=initDataArr[2]默认宝石数量--initDataArr[2]-[len]----initDataArr[2+[len]记忆数量，之后为随机出来的记忆宝石序列】
	 */
	public void setInitDataArr(String[] initDataArr){
		this.initDataArr = initDataArr;
	}

	/**
	 * 获取属性：
	 *	总积分
	 */
	public int getTotleScore(){
		return totleScore;
	}

	/**
	 * 设置属性：
	 *	总积分
	 */
	public void setTotleScore(int totleScore){
		this.totleScore = totleScore;
	}

	/**
	 * 获取属性：
	 *	总生命数
	 */
	public int getTotalLife(){
		return totalLife;
	}

	/**
	 * 设置属性：
	 *	总生命数
	 */
	public void setTotalLife(int totalLife){
		this.totalLife = totalLife;
	}

	/**
	 * 获取属性：
	 *	剩余游戏生命数
	 */
	public int getLife(){
		return life;
	}

	/**
	 * 设置属性：
	 *	剩余游戏生命数
	 */
	public void setLife(int life){
		this.life = life;
	}

	/**
	 * 获取属性：
	 *	持续时间
	 */
	public int getDuration(){
		return duration;
	}

	/**
	 * 设置属性：
	 *	持续时间
	 */
	public void setDuration(int duration){
		this.duration = duration;
	}

	/**
	 * 获取属性：
	 *	scores[0]为达到优的分数--scores[1]为达到良的分数
	 */
	public int[] getScores(){
		return scores;
	}

	/**
	 * 设置属性：
	 *	scores[0]为达到优的分数--scores[1]为达到良的分数
	 */
	public void setScores(int[] scores){
		this.scores = scores;
	}

}