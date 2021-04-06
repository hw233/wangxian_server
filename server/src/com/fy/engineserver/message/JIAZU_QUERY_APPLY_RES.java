package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询申请加入家族的列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playername.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playername[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playername[0]</td><td>String</td><td>playername[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playername[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playername[1]</td><td>String</td><td>playername[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playername[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playername[2]</td><td>String</td><td>playername[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerid.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerid</td><td>long[]</td><td>playerid.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerLevel.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerLevel</td><td>int[]</td><td>playerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>career.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>career</td><td>int[]</td><td>career.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>applyTime.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>applyTime[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>applyTime[0]</td><td>String</td><td>applyTime[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>applyTime[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>applyTime[1]</td><td>String</td><td>applyTime[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>applyTime[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>applyTime[2]</td><td>String</td><td>applyTime[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class JIAZU_QUERY_APPLY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] playername;
	long[] playerid;
	int[] playerLevel;
	int[] career;
	String[] applyTime;

	public JIAZU_QUERY_APPLY_RES(){
	}

	public JIAZU_QUERY_APPLY_RES(long seqNum,String[] playername,long[] playerid,int[] playerLevel,int[] career,String[] applyTime){
		this.seqNum = seqNum;
		this.playername = playername;
		this.playerid = playerid;
		this.playerLevel = playerLevel;
		this.career = career;
		this.applyTime = applyTime;
	}

	public JIAZU_QUERY_APPLY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playername = new String[len];
		for(int i = 0 ; i < playername.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			playername[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerid = new long[len];
		for(int i = 0 ; i < playerid.length ; i++){
			playerid[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerLevel = new int[len];
		for(int i = 0 ; i < playerLevel.length ; i++){
			playerLevel[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		career = new int[len];
		for(int i = 0 ; i < career.length ; i++){
			career[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		applyTime = new String[len];
		for(int i = 0 ; i < applyTime.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			applyTime[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x700AEE04;
	}

	public String getTypeDescription() {
		return "JIAZU_QUERY_APPLY_RES";
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
		for(int i = 0 ; i < playername.length; i++){
			len += 2;
			try{
				len += playername[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += playerid.length * 8;
		len += 4;
		len += playerLevel.length * 4;
		len += 4;
		len += career.length * 4;
		len += 4;
		for(int i = 0 ; i < applyTime.length; i++){
			len += 2;
			try{
				len += applyTime[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
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

			buffer.putInt(playername.length);
			for(int i = 0 ; i < playername.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = playername[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(playerid.length);
			for(int i = 0 ; i < playerid.length; i++){
				buffer.putLong(playerid[i]);
			}
			buffer.putInt(playerLevel.length);
			for(int i = 0 ; i < playerLevel.length; i++){
				buffer.putInt(playerLevel[i]);
			}
			buffer.putInt(career.length);
			for(int i = 0 ; i < career.length; i++){
				buffer.putInt(career[i]);
			}
			buffer.putInt(applyTime.length);
			for(int i = 0 ; i < applyTime.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = applyTime[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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
	 *	角色的名称
	 */
	public String[] getPlayername(){
		return playername;
	}

	/**
	 * 设置属性：
	 *	角色的名称
	 */
	public void setPlayername(String[] playername){
		this.playername = playername;
	}

	/**
	 * 获取属性：
	 *	角色的ID
	 */
	public long[] getPlayerid(){
		return playerid;
	}

	/**
	 * 设置属性：
	 *	角色的ID
	 */
	public void setPlayerid(long[] playerid){
		this.playerid = playerid;
	}

	/**
	 * 获取属性：
	 *	角色等级
	 */
	public int[] getPlayerLevel(){
		return playerLevel;
	}

	/**
	 * 设置属性：
	 *	角色等级
	 */
	public void setPlayerLevel(int[] playerLevel){
		this.playerLevel = playerLevel;
	}

	/**
	 * 获取属性：
	 *	角色职业
	 */
	public int[] getCareer(){
		return career;
	}

	/**
	 * 设置属性：
	 *	角色职业
	 */
	public void setCareer(int[] career){
		this.career = career;
	}

	/**
	 * 获取属性：
	 *	申请时间
	 */
	public String[] getApplyTime(){
		return applyTime;
	}

	/**
	 * 设置属性：
	 *	申请时间
	 */
	public void setApplyTime(String[] applyTime){
		this.applyTime = applyTime;
	}

}