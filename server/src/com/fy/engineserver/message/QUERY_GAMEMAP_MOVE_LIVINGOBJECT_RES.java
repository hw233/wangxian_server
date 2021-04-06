package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询某一地图的动态位置数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapname.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapname</td><td>String</td><td>mapname.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0]</td><td>String</td><td>players[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1]</td><td>String</td><td>players[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2]</td><td>String</td><td>players[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playersIcon.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playersIcon[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playersIcon[0]</td><td>String</td><td>playersIcon[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playersIcon[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playersIcon[1]</td><td>String</td><td>playersIcon[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playersIcon[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playersIcon[2]</td><td>String</td><td>playersIcon[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playersx.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playersx</td><td>short[]</td><td>playersx.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playersy.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playersy</td><td>short[]</td><td>playersy.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerCountryx.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerCountryx</td><td>short[]</td><td>ownerCountryx.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerCountryy.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerCountryy</td><td>short[]</td><td>ownerCountryy.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enemyCountryx.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enemyCountryx</td><td>short[]</td><td>enemyCountryx.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enemyCountryy.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enemyCountryy</td><td>short[]</td><td>enemyCountryy.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String mapname;
	String[] players;
	String[] playersIcon;
	short[] playersx;
	short[] playersy;
	short[] ownerCountryx;
	short[] ownerCountryy;
	short[] enemyCountryx;
	short[] enemyCountryy;

	public QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES(){
	}

	public QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES(long seqNum,String mapname,String[] players,String[] playersIcon,short[] playersx,short[] playersy,short[] ownerCountryx,short[] ownerCountryy,short[] enemyCountryx,short[] enemyCountryy){
		this.seqNum = seqNum;
		this.mapname = mapname;
		this.players = players;
		this.playersIcon = playersIcon;
		this.playersx = playersx;
		this.playersy = playersy;
		this.ownerCountryx = ownerCountryx;
		this.ownerCountryy = ownerCountryy;
		this.enemyCountryx = enemyCountryx;
		this.enemyCountryy = enemyCountryy;
	}

	public QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		mapname = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		players = new String[len];
		for(int i = 0 ; i < players.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playersIcon = new String[len];
		for(int i = 0 ; i < playersIcon.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			playersIcon[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playersx = new short[len];
		for(int i = 0 ; i < playersx.length ; i++){
			playersx[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playersy = new short[len];
		for(int i = 0 ; i < playersy.length ; i++){
			playersy[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ownerCountryx = new short[len];
		for(int i = 0 ; i < ownerCountryx.length ; i++){
			ownerCountryx[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ownerCountryy = new short[len];
		for(int i = 0 ; i < ownerCountryy.length ; i++){
			ownerCountryy[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		enemyCountryx = new short[len];
		for(int i = 0 ; i < enemyCountryx.length ; i++){
			enemyCountryx[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		enemyCountryy = new short[len];
		for(int i = 0 ; i < enemyCountryy.length ; i++){
			enemyCountryy[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
	}

	public int getType() {
		return 0x7000F043;
	}

	public String getTypeDescription() {
		return "QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES";
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
			len +=mapname.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < players.length; i++){
			len += 2;
			try{
				len += players[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < playersIcon.length; i++){
			len += 2;
			try{
				len += playersIcon[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += playersx.length * 2;
		len += 4;
		len += playersy.length * 2;
		len += 4;
		len += ownerCountryx.length * 2;
		len += 4;
		len += ownerCountryy.length * 2;
		len += 4;
		len += enemyCountryx.length * 2;
		len += 4;
		len += enemyCountryy.length * 2;
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
			 tmpBytes1 = mapname.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(players.length);
			for(int i = 0 ; i < players.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = players[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(playersIcon.length);
			for(int i = 0 ; i < playersIcon.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = playersIcon[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(playersx.length);
			for(int i = 0 ; i < playersx.length; i++){
				buffer.putShort(playersx[i]);
			}
			buffer.putInt(playersy.length);
			for(int i = 0 ; i < playersy.length; i++){
				buffer.putShort(playersy[i]);
			}
			buffer.putInt(ownerCountryx.length);
			for(int i = 0 ; i < ownerCountryx.length; i++){
				buffer.putShort(ownerCountryx[i]);
			}
			buffer.putInt(ownerCountryy.length);
			for(int i = 0 ; i < ownerCountryy.length; i++){
				buffer.putShort(ownerCountryy[i]);
			}
			buffer.putInt(enemyCountryx.length);
			for(int i = 0 ; i < enemyCountryx.length; i++){
				buffer.putShort(enemyCountryx[i]);
			}
			buffer.putInt(enemyCountryy.length);
			for(int i = 0 ; i < enemyCountryy.length; i++){
				buffer.putShort(enemyCountryy[i]);
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
	 *	地图名
	 */
	public String getMapname(){
		return mapname;
	}

	/**
	 * 设置属性：
	 *	地图名
	 */
	public void setMapname(String mapname){
		this.mapname = mapname;
	}

	/**
	 * 获取属性：
	 *	人物
	 */
	public String[] getPlayers(){
		return players;
	}

	/**
	 * 设置属性：
	 *	人物
	 */
	public void setPlayers(String[] players){
		this.players = players;
	}

	/**
	 * 获取属性：
	 *	人物显示图标名
	 */
	public String[] getPlayersIcon(){
		return playersIcon;
	}

	/**
	 * 设置属性：
	 *	人物显示图标名
	 */
	public void setPlayersIcon(String[] playersIcon){
		this.playersIcon = playersIcon;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public short[] getPlayersx(){
		return playersx;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPlayersx(short[] playersx){
		this.playersx = playersx;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public short[] getPlayersy(){
		return playersy;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPlayersy(short[] playersy){
		this.playersy = playersy;
	}

	/**
	 * 获取属性：
	 *	本国玩家
	 */
	public short[] getOwnerCountryx(){
		return ownerCountryx;
	}

	/**
	 * 设置属性：
	 *	本国玩家
	 */
	public void setOwnerCountryx(short[] ownerCountryx){
		this.ownerCountryx = ownerCountryx;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public short[] getOwnerCountryy(){
		return ownerCountryy;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setOwnerCountryy(short[] ownerCountryy){
		this.ownerCountryy = ownerCountryy;
	}

	/**
	 * 获取属性：
	 *	敌国玩家
	 */
	public short[] getEnemyCountryx(){
		return enemyCountryx;
	}

	/**
	 * 设置属性：
	 *	敌国玩家
	 */
	public void setEnemyCountryx(short[] enemyCountryx){
		this.enemyCountryx = enemyCountryx;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public short[] getEnemyCountryy(){
		return enemyCountryy;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setEnemyCountryy(short[] enemyCountryy){
		this.enemyCountryy = enemyCountryy;
	}

}