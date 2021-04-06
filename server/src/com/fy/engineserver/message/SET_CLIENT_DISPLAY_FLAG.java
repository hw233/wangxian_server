package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端设置显示屏蔽<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showPlayer</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showSameCountryPlayer</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showGreenNamePlayer</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showNpc</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMonster</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showOtherPlayerSkill</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showChat</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class SET_CLIENT_DISPLAY_FLAG implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean showPlayer;
	boolean showSameCountryPlayer;
	boolean showGreenNamePlayer;
	boolean showNpc;
	boolean showMonster;
	boolean showOtherPlayerSkill;
	boolean showChat;

	public SET_CLIENT_DISPLAY_FLAG(){
	}

	public SET_CLIENT_DISPLAY_FLAG(long seqNum,boolean showPlayer,boolean showSameCountryPlayer,boolean showGreenNamePlayer,boolean showNpc,boolean showMonster,boolean showOtherPlayerSkill,boolean showChat){
		this.seqNum = seqNum;
		this.showPlayer = showPlayer;
		this.showSameCountryPlayer = showSameCountryPlayer;
		this.showGreenNamePlayer = showGreenNamePlayer;
		this.showNpc = showNpc;
		this.showMonster = showMonster;
		this.showOtherPlayerSkill = showOtherPlayerSkill;
		this.showChat = showChat;
	}

	public SET_CLIENT_DISPLAY_FLAG(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		showPlayer = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		showSameCountryPlayer = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		showGreenNamePlayer = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		showNpc = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		showMonster = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		showOtherPlayerSkill = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		showChat = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x00EEEE0B;
	}

	public String getTypeDescription() {
		return "SET_CLIENT_DISPLAY_FLAG";
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
		len += 1;
		len += 1;
		len += 1;
		len += 1;
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

			buffer.put((byte)(showPlayer==false?0:1));
			buffer.put((byte)(showSameCountryPlayer==false?0:1));
			buffer.put((byte)(showGreenNamePlayer==false?0:1));
			buffer.put((byte)(showNpc==false?0:1));
			buffer.put((byte)(showMonster==false?0:1));
			buffer.put((byte)(showOtherPlayerSkill==false?0:1));
			buffer.put((byte)(showChat==false?0:1));
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
	 *	显示玩家
	 */
	public boolean getShowPlayer(){
		return showPlayer;
	}

	/**
	 * 设置属性：
	 *	显示玩家
	 */
	public void setShowPlayer(boolean showPlayer){
		this.showPlayer = showPlayer;
	}

	/**
	 * 获取属性：
	 *	显示本国玩家
	 */
	public boolean getShowSameCountryPlayer(){
		return showSameCountryPlayer;
	}

	/**
	 * 设置属性：
	 *	显示本国玩家
	 */
	public void setShowSameCountryPlayer(boolean showSameCountryPlayer){
		this.showSameCountryPlayer = showSameCountryPlayer;
	}

	/**
	 * 获取属性：
	 *	显示绿名玩家
	 */
	public boolean getShowGreenNamePlayer(){
		return showGreenNamePlayer;
	}

	/**
	 * 设置属性：
	 *	显示绿名玩家
	 */
	public void setShowGreenNamePlayer(boolean showGreenNamePlayer){
		this.showGreenNamePlayer = showGreenNamePlayer;
	}

	/**
	 * 获取属性：
	 *	显示NPC
	 */
	public boolean getShowNpc(){
		return showNpc;
	}

	/**
	 * 设置属性：
	 *	显示NPC
	 */
	public void setShowNpc(boolean showNpc){
		this.showNpc = showNpc;
	}

	/**
	 * 获取属性：
	 *	显示怪
	 */
	public boolean getShowMonster(){
		return showMonster;
	}

	/**
	 * 设置属性：
	 *	显示怪
	 */
	public void setShowMonster(boolean showMonster){
		this.showMonster = showMonster;
	}

	/**
	 * 获取属性：
	 *	显示其他玩家技能
	 */
	public boolean getShowOtherPlayerSkill(){
		return showOtherPlayerSkill;
	}

	/**
	 * 设置属性：
	 *	显示其他玩家技能
	 */
	public void setShowOtherPlayerSkill(boolean showOtherPlayerSkill){
		this.showOtherPlayerSkill = showOtherPlayerSkill;
	}

	/**
	 * 获取属性：
	 *	显示聊天
	 */
	public boolean getShowChat(){
		return showChat;
	}

	/**
	 * 设置属性：
	 *	显示聊天
	 */
	public void setShowChat(boolean showChat){
		this.showChat = showChat;
	}

}