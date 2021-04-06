package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 更新一个玩家<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playername.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playername</td><td>String</td><td>playername.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>money</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindyuanbao</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>game.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>game</td><td>String</td><td>game.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastgame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastgame</td><td>String</td><td>lastgame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastupdatetime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerProtobuf.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerProtobuf</td><td>byte[]</td><td>playerProtobuf.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>polcamp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastLoginTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastLogoutTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statid.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statid</td><td>int[]</td><td>statid.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>svalue.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>svalue</td><td>long[]</td><td>svalue.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastUpdateTime.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastUpdateTime</td><td>long[]</td><td>lastUpdateTime.length</td><td>*</td></tr>
 * </table>
 */
public class CACHE_PLAYER_UPDATE_REQ implements RequestMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	long playerId;
	String username;
	String playername;
	int playerLevel;
	int hp;
	int mp;
	long money;
	long bindyuanbao;
	String game;
	String lastgame;
	long lastupdatetime;
	byte[] playerProtobuf;
	int exp;
	int polcamp;
	long lastLoginTime;
	long lastLogoutTime;
	int[] statid;
	long[] svalue;
	long[] lastUpdateTime;

	public CACHE_PLAYER_UPDATE_REQ(long seqNum,long playerId,String username,String playername,int playerLevel,int hp,int mp,long money,long bindyuanbao,String game,String lastgame,long lastupdatetime,byte[] playerProtobuf,int exp,int polcamp,long lastLoginTime,long lastLogoutTime,int[] statid,long[] svalue,long[] lastUpdateTime){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.username = username;
		this.playername = playername;
		this.playerLevel = playerLevel;
		this.hp = hp;
		this.mp = mp;
		this.money = money;
		this.bindyuanbao = bindyuanbao;
		this.game = game;
		this.lastgame = lastgame;
		this.lastupdatetime = lastupdatetime;
		this.playerProtobuf = playerProtobuf;
		this.exp = exp;
		this.polcamp = polcamp;
		this.lastLoginTime = lastLoginTime;
		this.lastLogoutTime = lastLogoutTime;
		this.statid = statid;
		this.svalue = svalue;
		this.lastUpdateTime = lastUpdateTime;
	}

	public CACHE_PLAYER_UPDATE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		username = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		playername = new String(content,offset,len,"UTF-8");
		offset += len;
		playerLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		hp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		mp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		money = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		bindyuanbao = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		game = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		lastgame = new String(content,offset,len,"UTF-8");
		offset += len;
		lastupdatetime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 512000) throw new Exception("array length ["+len+"] big than the max length [512000]");
		playerProtobuf = new byte[len];
		System.arraycopy(content,offset,playerProtobuf,0,len);
		offset += len;
		exp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		polcamp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		lastLoginTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		lastLogoutTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		statid = new int[len];
		for(int i = 0 ; i < statid.length ; i++){
			statid[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		svalue = new long[len];
		for(int i = 0 ; i < svalue.length ; i++){
			svalue[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		lastUpdateTime = new long[len];
		for(int i = 0 ; i < lastUpdateTime.length ; i++){
			lastUpdateTime[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x00000003;
	}

	public String getTypeDescription() {
		return "CACHE_PLAYER_UPDATE_REQ";
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
		len += 2;
		try{
			len +=username.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=playername.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 8;
		len += 8;
		len += 2;
		try{
			len +=game.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=lastgame.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 4;
		len += playerProtobuf.length;
		len += 4;
		len += 4;
		len += 8;
		len += 8;
		len += 4;
		len += statid.length * 4;
		len += 4;
		len += svalue.length * 8;
		len += 4;
		len += lastUpdateTime.length * 8;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putLong(playerId);
			byte[] tmpBytes1 = username.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = playername.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(playerLevel);
			buffer.putInt(hp);
			buffer.putInt(mp);
			buffer.putLong(money);
			buffer.putLong(bindyuanbao);
			tmpBytes1 = game.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = lastgame.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(lastupdatetime);
			buffer.putInt(playerProtobuf.length);
			buffer.put(playerProtobuf);
			buffer.putInt(exp);
			buffer.putInt(polcamp);
			buffer.putLong(lastLoginTime);
			buffer.putLong(lastLogoutTime);
			buffer.putInt(statid.length);
			for(int i = 0 ; i < statid.length; i++){
				buffer.putInt(statid[i]);
			}
			buffer.putInt(svalue.length);
			for(int i = 0 ; i < svalue.length; i++){
				buffer.putLong(svalue[i]);
			}
			buffer.putInt(lastUpdateTime.length);
			for(int i = 0 ; i < lastUpdateTime.length; i++){
				buffer.putLong(lastUpdateTime[i]);
			}
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	用户名称
	 */
	public String getUsername(){
		return username;
	}

	/**
	 * 设置属性：
	 *	用户名称
	 */
	public void setUsername(String username){
		this.username = username;
	}

	/**
	 * 获取属性：
	 *	玩家名称
	 */
	public String getPlayername(){
		return playername;
	}

	/**
	 * 设置属性：
	 *	玩家名称
	 */
	public void setPlayername(String playername){
		this.playername = playername;
	}

	/**
	 * 获取属性：
	 *	等级
	 */
	public int getPlayerLevel(){
		return playerLevel;
	}

	/**
	 * 设置属性：
	 *	等级
	 */
	public void setPlayerLevel(int playerLevel){
		this.playerLevel = playerLevel;
	}

	/**
	 * 获取属性：
	 *	hp
	 */
	public int getHp(){
		return hp;
	}

	/**
	 * 设置属性：
	 *	hp
	 */
	public void setHp(int hp){
		this.hp = hp;
	}

	/**
	 * 获取属性：
	 *	mp
	 */
	public int getMp(){
		return mp;
	}

	/**
	 * 设置属性：
	 *	mp
	 */
	public void setMp(int mp){
		this.mp = mp;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public long getMoney(){
		return money;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setMoney(long money){
		this.money = money;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public long getBindyuanbao(){
		return bindyuanbao;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBindyuanbao(long bindyuanbao){
		this.bindyuanbao = bindyuanbao;
	}

	/**
	 * 获取属性：
	 *	游戏
	 */
	public String getGame(){
		return game;
	}

	/**
	 * 设置属性：
	 *	游戏
	 */
	public void setGame(String game){
		this.game = game;
	}

	/**
	 * 获取属性：
	 *	上一个游戏
	 */
	public String getLastgame(){
		return lastgame;
	}

	/**
	 * 设置属性：
	 *	上一个游戏
	 */
	public void setLastgame(String lastgame){
		this.lastgame = lastgame;
	}

	/**
	 * 获取属性：
	 *	上次更新
	 */
	public long getLastupdatetime(){
		return lastupdatetime;
	}

	/**
	 * 设置属性：
	 *	上次更新
	 */
	public void setLastupdatetime(long lastupdatetime){
		this.lastupdatetime = lastupdatetime;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public byte[] getPlayerProtobuf(){
		return playerProtobuf;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPlayerProtobuf(byte[] playerProtobuf){
		this.playerProtobuf = playerProtobuf;
	}

	/**
	 * 获取属性：
	 *	经验值
	 */
	public int getExp(){
		return exp;
	}

	/**
	 * 设置属性：
	 *	经验值
	 */
	public void setExp(int exp){
		this.exp = exp;
	}

	/**
	 * 获取属性：
	 *	阵营
	 */
	public int getPolcamp(){
		return polcamp;
	}

	/**
	 * 设置属性：
	 *	阵营
	 */
	public void setPolcamp(int polcamp){
		this.polcamp = polcamp;
	}

	/**
	 * 获取属性：
	 *	上次登录时间
	 */
	public long getLastLoginTime(){
		return lastLoginTime;
	}

	/**
	 * 设置属性：
	 *	上次登录时间
	 */
	public void setLastLoginTime(long lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * 获取属性：
	 *	上次下线时间
	 */
	public long getLastLogoutTime(){
		return lastLogoutTime;
	}

	/**
	 * 设置属性：
	 *	上次下线时间
	 */
	public void setLastLogoutTime(long lastLogoutTime){
		this.lastLogoutTime = lastLogoutTime;
	}

	/**
	 * 获取属性：
	 *	统计项
	 */
	public int[] getStatid(){
		return statid;
	}

	/**
	 * 设置属性：
	 *	统计项
	 */
	public void setStatid(int[] statid){
		this.statid = statid;
	}

	/**
	 * 获取属性：
	 *	统计值
	 */
	public long[] getSvalue(){
		return svalue;
	}

	/**
	 * 设置属性：
	 *	统计值
	 */
	public void setSvalue(long[] svalue){
		this.svalue = svalue;
	}

	/**
	 * 获取属性：
	 *	上次更新时间
	 */
	public long[] getLastUpdateTime(){
		return lastUpdateTime;
	}

	/**
	 * 设置属性：
	 *	上次更新时间
	 */
	public void setLastUpdateTime(long[] lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

}
