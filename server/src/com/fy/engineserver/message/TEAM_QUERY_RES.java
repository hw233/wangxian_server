package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.Player;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端向服务器发送请求，查询玩家当前所在的队伍<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>captainId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>assignColorType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.length</td><td>int</td><td>4个字节</td><td>Player数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].name</td><td>String</td><td>players[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].name</td><td>String</td><td>players[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].name</td><td>String</td><td>players[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class TEAM_QUERY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int teamId;
	long captainId;
	byte assignColorType;
	Player[] players;

	public TEAM_QUERY_RES(){
	}

	public TEAM_QUERY_RES(long seqNum,int teamId,long captainId,byte assignColorType,Player[] players){
		this.seqNum = seqNum;
		this.teamId = teamId;
		this.captainId = captainId;
		this.assignColorType = assignColorType;
		this.players = players;
	}

	public TEAM_QUERY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		teamId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		captainId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		assignColorType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		players = new Player[len];
		for(int i = 0 ; i < players.length ; i++){
			players[i] = new Player();
			players[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			players[i].setHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMaxMP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
	}

	public int getType() {
		return 0x70000F00;
	}

	public String getTypeDescription() {
		return "TEAM_QUERY_RES";
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
		len += 8;
		len += 1;
		len += 4;
		for(int i = 0 ; i < players.length ; i++){
			len += 8;
			len += 4;
			len += 2;
			if(players[i].getName() != null){
				try{
				len += players[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 1;
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

			buffer.putInt(teamId);
			buffer.putLong(captainId);
			buffer.put(assignColorType);
			buffer.putInt(players.length);
			for(int i = 0 ; i < players.length ; i++){
				buffer.putLong(players[i].getId());
				buffer.putInt((int)players[i].getLevel());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = players[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)players[i].getHp());
				buffer.putInt((int)players[i].getMp());
				buffer.putInt((int)players[i].getMaxHP());
				buffer.putInt((int)players[i].getMaxMP());
				buffer.put((byte)players[i].getCareer());
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
	 *	团队的ID
	 */
	public int getTeamId(){
		return teamId;
	}

	/**
	 * 设置属性：
	 *	团队的ID
	 */
	public void setTeamId(int teamId){
		this.teamId = teamId;
	}

	/**
	 * 获取属性：
	 *	队长的ID
	 */
	public long getCaptainId(){
		return captainId;
	}

	/**
	 * 设置属性：
	 *	队长的ID
	 */
	public void setCaptainId(long captainId){
		this.captainId = captainId;
	}

	/**
	 * 获取属性：
	 *	在队伍分配和队长分配的情况下，用于区分物品的好坏(现在没用)
	 */
	public byte getAssignColorType(){
		return assignColorType;
	}

	/**
	 * 设置属性：
	 *	在队伍分配和队长分配的情况下，用于区分物品的好坏(现在没用)
	 */
	public void setAssignColorType(byte assignColorType){
		this.assignColorType = assignColorType;
	}

	/**
	 * 获取属性：
	 *	队伍里的人
	 */
	public Player[] getPlayers(){
		return players;
	}

	/**
	 * 设置属性：
	 *	队伍里的人
	 */
	public void setPlayers(Player[] players){
		this.players = players;
	}

}