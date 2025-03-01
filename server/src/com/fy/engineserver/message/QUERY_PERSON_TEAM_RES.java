package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询自己允许进队状态，队伍分配方式，附近的队伍,如果有队伍显示队伍的分配规则<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inteamRule</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>assignRule</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>captainId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>captainId</td><td>long[]</td><td>captainId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamDes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>teamDes[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamDes[0]</td><td>String</td><td>teamDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>teamDes[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamDes[1]</td><td>String</td><td>teamDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>teamDes[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>teamDes[2]</td><td>String</td><td>teamDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_PERSON_TEAM_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte inteamRule;
	byte assignRule;
	long[] captainId;
	String[] teamDes;

	public QUERY_PERSON_TEAM_RES(){
	}

	public QUERY_PERSON_TEAM_RES(long seqNum,byte inteamRule,byte assignRule,long[] captainId,String[] teamDes){
		this.seqNum = seqNum;
		this.inteamRule = inteamRule;
		this.assignRule = assignRule;
		this.captainId = captainId;
		this.teamDes = teamDes;
	}

	public QUERY_PERSON_TEAM_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		inteamRule = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		assignRule = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		captainId = new long[len];
		for(int i = 0 ; i < captainId.length ; i++){
			captainId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		teamDes = new String[len];
		for(int i = 0 ; i < teamDes.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			teamDes[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x700000E8;
	}

	public String getTypeDescription() {
		return "QUERY_PERSON_TEAM_RES";
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
		len += 4;
		len += captainId.length * 8;
		len += 4;
		for(int i = 0 ; i < teamDes.length; i++){
			len += 2;
			try{
				len += teamDes[i].getBytes("UTF-8").length;
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

			buffer.put(inteamRule);
			buffer.put(assignRule);
			buffer.putInt(captainId.length);
			for(int i = 0 ; i < captainId.length; i++){
				buffer.putLong(captainId[i]);
			}
			buffer.putInt(teamDes.length);
			for(int i = 0 ; i < teamDes.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = teamDes[i].getBytes("UTF-8");
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
	 *	自己允许进队状态
	 */
	public byte getInteamRule(){
		return inteamRule;
	}

	/**
	 * 设置属性：
	 *	自己允许进队状态
	 */
	public void setInteamRule(byte inteamRule){
		this.inteamRule = inteamRule;
	}

	/**
	 * 获取属性：
	 *	队伍分配方式
	 */
	public byte getAssignRule(){
		return assignRule;
	}

	/**
	 * 设置属性：
	 *	队伍分配方式
	 */
	public void setAssignRule(byte assignRule){
		this.assignRule = assignRule;
	}

	/**
	 * 获取属性：
	 *	队长id
	 */
	public long[] getCaptainId(){
		return captainId;
	}

	/**
	 * 设置属性：
	 *	队长id
	 */
	public void setCaptainId(long[] captainId){
		this.captainId = captainId;
	}

	/**
	 * 获取属性：
	 *	队伍描述
	 */
	public String[] getTeamDes(){
		return teamDes;
	}

	/**
	 * 设置属性：
	 *	队伍描述
	 */
	public void setTeamDes(String[] teamDes){
		this.teamDes = teamDes;
	}

}