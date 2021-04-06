package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.TeamChangeNotice;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 队伍成员变化通知其他人 0: hp;1: maxHP;2: mp;3: maxMP;4:level<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamChangeNotice.length</td><td>int</td><td>4个字节</td><td>TeamChangeNotice数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>teamChangeNotice[0].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamChangeNotice[0].changeType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>teamChangeNotice[0].nowValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamChangeNotice[1].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>teamChangeNotice[1].changeType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamChangeNotice[1].nowValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>teamChangeNotice[2].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamChangeNotice[2].changeType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>teamChangeNotice[2].nowValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NOTICE_TEAM_CHANGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	TeamChangeNotice[] teamChangeNotice;

	public NOTICE_TEAM_CHANGE_RES(){
	}

	public NOTICE_TEAM_CHANGE_RES(long seqNum,TeamChangeNotice[] teamChangeNotice){
		this.seqNum = seqNum;
		this.teamChangeNotice = teamChangeNotice;
	}

	public NOTICE_TEAM_CHANGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		teamChangeNotice = new TeamChangeNotice[len];
		for(int i = 0 ; i < teamChangeNotice.length ; i++){
			teamChangeNotice[i] = new TeamChangeNotice();
			teamChangeNotice[i].setPlayerId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			teamChangeNotice[i].setChangeType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			teamChangeNotice[i].setNowValue((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x8F600057;
	}

	public String getTypeDescription() {
		return "NOTICE_TEAM_CHANGE_RES";
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
		for(int i = 0 ; i < teamChangeNotice.length ; i++){
			len += 8;
			len += 4;
			len += 4;
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

			buffer.putInt(teamChangeNotice.length);
			for(int i = 0 ; i < teamChangeNotice.length ; i++){
				buffer.putLong(teamChangeNotice[i].getPlayerId());
				buffer.putInt((int)teamChangeNotice[i].getChangeType());
				buffer.putInt((int)teamChangeNotice[i].getNowValue());
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
	 *	无帮助说明
	 */
	public TeamChangeNotice[] getTeamChangeNotice(){
		return teamChangeNotice;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setTeamChangeNotice(TeamChangeNotice[] teamChangeNotice){
		this.teamChangeNotice = teamChangeNotice;
	}

}