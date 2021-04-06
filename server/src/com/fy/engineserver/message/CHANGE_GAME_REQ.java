package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器发送，要求客户端离开当前地图，然后请求进入指定的下一张地图<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nextGame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nextGame</td><td>String</td><td>nextGame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapDataVersion</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterTypes</td><td>short[]</td><td>monsterTypes.length</td><td>*</td></tr>
 * </table>
 */
public class CHANGE_GAME_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String nextGame;
	int mapDataVersion;
	short[] monsterTypes;

	public CHANGE_GAME_REQ(){
	}

	public CHANGE_GAME_REQ(long seqNum,String nextGame,int mapDataVersion,short[] monsterTypes){
		this.seqNum = seqNum;
		this.nextGame = nextGame;
		this.mapDataVersion = mapDataVersion;
		this.monsterTypes = monsterTypes;
	}

	public CHANGE_GAME_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		nextGame = new String(content,offset,len,"UTF-8");
		offset += len;
		mapDataVersion = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		monsterTypes = new short[len];
		for(int i = 0 ; i < monsterTypes.length ; i++){
			monsterTypes[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
	}

	public int getType() {
		return 0x00000092;
	}

	public String getTypeDescription() {
		return "CHANGE_GAME_REQ";
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
			len +=nextGame.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += monsterTypes.length * 2;
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
			 tmpBytes1 = nextGame.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(mapDataVersion);
			buffer.putInt(monsterTypes.length);
			for(int i = 0 ; i < monsterTypes.length; i++){
				buffer.putShort(monsterTypes[i]);
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
	 *	要求前往的下一张地图
	 */
	public String getNextGame(){
		return nextGame;
	}

	/**
	 * 设置属性：
	 *	要求前往的下一张地图
	 */
	public void setNextGame(String nextGame){
		this.nextGame = nextGame;
	}

	/**
	 * 获取属性：
	 *	要进入地图的数据版本号
	 */
	public int getMapDataVersion(){
		return mapDataVersion;
	}

	/**
	 * 设置属性：
	 *	要进入地图的数据版本号
	 */
	public void setMapDataVersion(int mapDataVersion){
		this.mapDataVersion = mapDataVersion;
	}

	/**
	 * 获取属性：
	 *	精灵的类型（仅限用普通动画表达）
	 */
	public short[] getMonsterTypes(){
		return monsterTypes;
	}

	/**
	 * 设置属性：
	 *	精灵的类型（仅限用普通动画表达）
	 */
	public void setMonsterTypes(short[] monsterTypes){
		this.monsterTypes = monsterTypes;
	}

}