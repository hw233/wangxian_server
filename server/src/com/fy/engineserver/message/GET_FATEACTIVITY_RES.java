package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.Player;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 得到进行中的仙缘活动<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerRole</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityState</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descript.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descript</td><td>String</td><td>descript.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>country</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nodoPlayer.length</td><td>int</td><td>4个字节</td><td>Player数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nodoPlayer[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nodoPlayer[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nodoPlayer[0].name</td><td>String</td><td>nodoPlayer[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nodoPlayer[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nodoPlayer[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nodoPlayer[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nodoPlayer[1].name</td><td>String</td><td>nodoPlayer[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nodoPlayer[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nodoPlayer[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nodoPlayer[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nodoPlayer[2].name</td><td>String</td><td>nodoPlayer[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nodoPlayer[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>didPlayer.length</td><td>int</td><td>4个字节</td><td>Player数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>didPlayer[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>didPlayer[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>didPlayer[0].name</td><td>String</td><td>didPlayer[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>didPlayer[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>didPlayer[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>didPlayer[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>didPlayer[1].name</td><td>String</td><td>didPlayer[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>didPlayer[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>didPlayer[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>didPlayer[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>didPlayer[2].name</td><td>String</td><td>didPlayer[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>didPlayer[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>time</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class GET_FATEACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte activityType;
	boolean playerRole;
	byte activityState;
	String descript;
	long id;
	String name;
	byte country;
	Player[] nodoPlayer;
	Player[] didPlayer;
	long time;

	public GET_FATEACTIVITY_RES(){
	}

	public GET_FATEACTIVITY_RES(long seqNum,byte activityType,boolean playerRole,byte activityState,String descript,long id,String name,byte country,Player[] nodoPlayer,Player[] didPlayer,long time){
		this.seqNum = seqNum;
		this.activityType = activityType;
		this.playerRole = playerRole;
		this.activityState = activityState;
		this.descript = descript;
		this.id = id;
		this.name = name;
		this.country = country;
		this.nodoPlayer = nodoPlayer;
		this.didPlayer = didPlayer;
		this.time = time;
	}

	public GET_FATEACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		activityType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		playerRole = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		activityState = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		descript = new String(content,offset,len,"UTF-8");
		offset += len;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		country = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		nodoPlayer = new Player[len];
		for(int i = 0 ; i < nodoPlayer.length ; i++){
			nodoPlayer[i] = new Player();
			nodoPlayer[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			nodoPlayer[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			nodoPlayer[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		didPlayer = new Player[len];
		for(int i = 0 ; i < didPlayer.length ; i++){
			didPlayer[i] = new Player();
			didPlayer[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			didPlayer[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			didPlayer[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
		time = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x700F0001;
	}

	public String getTypeDescription() {
		return "GET_FATEACTIVITY_RES";
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
		len += 1;
		len += 2;
		try{
			len +=descript.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 2;
		try{
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 4;
		for(int i = 0 ; i < nodoPlayer.length ; i++){
			len += 8;
			len += 2;
			if(nodoPlayer[i].getName() != null){
				try{
				len += nodoPlayer[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
		}
		len += 4;
		for(int i = 0 ; i < didPlayer.length ; i++){
			len += 8;
			len += 2;
			if(didPlayer[i].getName() != null){
				try{
				len += didPlayer[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
		}
		len += 8;
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

			buffer.put(activityType);
			buffer.put((byte)(playerRole==false?0:1));
			buffer.put(activityState);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = descript.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(id);
				try{
			tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(country);
			buffer.putInt(nodoPlayer.length);
			for(int i = 0 ; i < nodoPlayer.length ; i++){
				buffer.putLong(nodoPlayer[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = nodoPlayer[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)nodoPlayer[i].getCareer());
			}
			buffer.putInt(didPlayer.length);
			for(int i = 0 ; i < didPlayer.length ; i++){
				buffer.putLong(didPlayer[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = didPlayer[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)didPlayer[i].getCareer());
			}
			buffer.putLong(time);
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
	 *	国内仙缘(0),国外仙缘(1),国内论道(2),国外论道(3)
	 */
	public byte getActivityType(){
		return activityType;
	}

	/**
	 * 设置属性：
	 *	国内仙缘(0),国外仙缘(1),国内论道(2),国外论道(3)
	 */
	public void setActivityType(byte activityType){
		this.activityType = activityType;
	}

	/**
	 * 获取属性：
	 *	玩家的角色（邀请方(true)还是被邀请方）
	 */
	public boolean getPlayerRole(){
		return playerRole;
	}

	/**
	 * 设置属性：
	 *	玩家的角色（邀请方(true)还是被邀请方）
	 */
	public void setPlayerRole(boolean playerRole){
		this.playerRole = playerRole;
	}

	/**
	 * 获取属性：
	 *	活动的状态，根据状态不同显示重选1，还是刷新0
	 */
	public byte getActivityState(){
		return activityState;
	}

	/**
	 * 设置属性：
	 *	活动的状态，根据状态不同显示重选1，还是刷新0
	 */
	public void setActivityState(byte activityState){
		this.activityState = activityState;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String getDescript(){
		return descript;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDescript(String descript){
		this.descript = descript;
	}

	/**
	 * 获取属性：
	 *	玩家唯一的编号(只有显示重选，放弃有用)
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	玩家唯一的编号(只有显示重选，放弃有用)
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	玩家的昵称(只有显示重选，放弃有用)
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	玩家的昵称(只有显示重选，放弃有用)
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	国家
	 */
	public byte getCountry(){
		return country;
	}

	/**
	 * 设置属性：
	 *	国家
	 */
	public void setCountry(byte country){
		this.country = country;
	}

	/**
	 * 获取属性：
	 *	没有做过的玩家
	 */
	public Player[] getNodoPlayer(){
		return nodoPlayer;
	}

	/**
	 * 设置属性：
	 *	没有做过的玩家
	 */
	public void setNodoPlayer(Player[] nodoPlayer){
		this.nodoPlayer = nodoPlayer;
	}

	/**
	 * 获取属性：
	 *	已经做过的玩家
	 */
	public Player[] getDidPlayer(){
		return didPlayer;
	}

	/**
	 * 设置属性：
	 *	已经做过的玩家
	 */
	public void setDidPlayer(Player[] didPlayer){
		this.didPlayer = didPlayer;
	}

	/**
	 * 获取属性：
	 *	距离刷新时间
	 */
	public long getTime(){
		return time;
	}

	/**
	 * 设置属性：
	 *	距离刷新时间
	 */
	public void setTime(long time){
		this.time = time;
	}

}