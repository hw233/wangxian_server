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
 * <tr bgcolor="#FAFAFA" align="center"><td>totalGrade</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>optResult</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initData.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initData[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initData[0]</td><td>String</td><td>initData[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initData[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initData[1]</td><td>String</td><td>initData[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initData[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initData[2]</td><td>String</td><td>initData[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class HANDLE_MINIGAME_ACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String contentMess;
	int totalGrade;
	int currentScore;
	byte optResult;
	String[] initData;

	public HANDLE_MINIGAME_ACTIVITY_RES(){
	}

	public HANDLE_MINIGAME_ACTIVITY_RES(long seqNum,String contentMess,int totalGrade,int currentScore,byte optResult,String[] initData){
		this.seqNum = seqNum;
		this.contentMess = contentMess;
		this.totalGrade = totalGrade;
		this.currentScore = currentScore;
		this.optResult = optResult;
		this.initData = initData;
	}

	public HANDLE_MINIGAME_ACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		contentMess = new String(content,offset,len,"UTF-8");
		offset += len;
		totalGrade = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currentScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		optResult = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		initData = new String[len];
		for(int i = 0 ; i < initData.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			initData[i] = new String(content,offset,len);
		offset += len;
		}
	}

	public int getType() {
		return 0x8E0EAA31;
	}

	public String getTypeDescription() {
		return "HANDLE_MINIGAME_ACTIVITY_RES";
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
		len += 4;
		len += 4;
		len += 1;
		len += 4;
		for(int i = 0 ; i < initData.length; i++){
			len += 2;
			len += initData[i].getBytes().length;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = contentMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(totalGrade);
			buffer.putInt(currentScore);
			buffer.put(optResult);
			buffer.putInt(initData.length);
			for(int i = 0 ; i < initData.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = initData[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	玩家当前总积分--每关过后获得累加
	 */
	public int getTotalGrade(){
		return totalGrade;
	}

	/**
	 * 设置属性：
	 *	玩家当前总积分--每关过后获得累加
	 */
	public void setTotalGrade(int totalGrade){
		this.totalGrade = totalGrade;
	}

	/**
	 * 获取属性：
	 *	当前小游戏分数
	 */
	public int getCurrentScore(){
		return currentScore;
	}

	/**
	 * 设置属性：
	 *	当前小游戏分数
	 */
	public void setCurrentScore(int currentScore){
		this.currentScore = currentScore;
	}

	/**
	 * 获取属性：
	 *	操作结果标志位：1代表操作成功，0代表操作不合法，2代表达到满分可以提前结束,-1代表操作超过规定时间  -2传过来的参数错误
	 */
	public byte getOptResult(){
		return optResult;
	}

	/**
	 * 设置属性：
	 *	操作结果标志位：1代表操作成功，0代表操作不合法，2代表达到满分可以提前结束,-1代表操作超过规定时间  -2传过来的参数错误
	 */
	public void setOptResult(byte optResult){
		this.optResult = optResult;
	}

	/**
	 * 获取属性：
	 *	当前需要同步的数组--目前只有管道游戏使用，每次返回新生成的管道数组（整体返回）
	 */
	public String[] getInitData(){
		return initData;
	}

	/**
	 * 设置属性：
	 *	当前需要同步的数组--目前只有管道游戏使用，每次返回新生成的管道数组（整体返回）
	 */
	public void setInitData(String[] initData){
		this.initData = initData;
	}

}