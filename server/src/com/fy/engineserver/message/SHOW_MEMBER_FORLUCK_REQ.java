package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.jiazu.JiazuMember4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 弹出分配树上果实的菜单<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>frontDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>frontDes</td><td>String</td><td>frontDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>endDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>endDes</td><td>String</td><td>endDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers.length</td><td>int</td><td>4个字节</td><td>JiazuMember4Client数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].playerName</td><td>String</td><td>jiazuMembers[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[0].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].title</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[0].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[1].playerName</td><td>String</td><td>jiazuMembers[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[1].title</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[1].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].playerName</td><td>String</td><td>jiazuMembers[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[2].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].title</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[2].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hasPickuped.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hasPickuped</td><td>boolean[]</td><td>hasPickuped.length</td><td>*</td></tr>
 * </table>
 */
public class SHOW_MEMBER_FORLUCK_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String frontDes;
	String endDes;
	long npcId;
	JiazuMember4Client[] jiazuMembers;
	boolean[] hasPickuped;

	public SHOW_MEMBER_FORLUCK_REQ(){
	}

	public SHOW_MEMBER_FORLUCK_REQ(long seqNum,String frontDes,String endDes,long npcId,JiazuMember4Client[] jiazuMembers,boolean[] hasPickuped){
		this.seqNum = seqNum;
		this.frontDes = frontDes;
		this.endDes = endDes;
		this.npcId = npcId;
		this.jiazuMembers = jiazuMembers;
		this.hasPickuped = hasPickuped;
	}

	public SHOW_MEMBER_FORLUCK_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		frontDes = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		endDes = new String(content,offset,len,"UTF-8");
		offset += len;
		npcId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		jiazuMembers = new JiazuMember4Client[len];
		for(int i = 0 ; i < jiazuMembers.length ; i++){
			jiazuMembers[i] = new JiazuMember4Client();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuMembers[i].setPlayerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			jiazuMembers[i].setPlayerId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			jiazuMembers[i].setTitle((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuMembers[i].setPlayerLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuMembers[i].setSex((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		hasPickuped = new boolean[len];
		for(int i = 0 ; i < hasPickuped.length ; i++){
			hasPickuped[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
	}

	public int getType() {
		return 0x0F300003;
	}

	public String getTypeDescription() {
		return "SHOW_MEMBER_FORLUCK_REQ";
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
			len +=frontDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=endDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 4;
		for(int i = 0 ; i < jiazuMembers.length ; i++){
			len += 2;
			if(jiazuMembers[i].getPlayerName() != null){
				try{
				len += jiazuMembers[i].getPlayerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 4;
			len += 4;
			len += 1;
		}
		len += 4;
		len += hasPickuped.length;
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
			 tmpBytes1 = frontDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = endDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(npcId);
			buffer.putInt(jiazuMembers.length);
			for(int i = 0 ; i < jiazuMembers.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = jiazuMembers[i].getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(jiazuMembers[i].getPlayerId());
				buffer.putInt((int)jiazuMembers[i].getTitle());
				buffer.putInt((int)jiazuMembers[i].getPlayerLevel());
				buffer.put((byte)jiazuMembers[i].getSex());
			}
			buffer.putInt(hasPickuped.length);
			for(int i = 0 ; i < hasPickuped.length; i++){
				buffer.put((byte)(hasPickuped[i]==false?0:1));
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
	 *	菜单前的描述
	 */
	public String getFrontDes(){
		return frontDes;
	}

	/**
	 * 设置属性：
	 *	菜单前的描述
	 */
	public void setFrontDes(String frontDes){
		this.frontDes = frontDes;
	}

	/**
	 * 获取属性：
	 *	菜单后的描述
	 */
	public String getEndDes(){
		return endDes;
	}

	/**
	 * 设置属性：
	 *	菜单后的描述
	 */
	public void setEndDes(String endDes){
		this.endDes = endDes;
	}

	/**
	 * 获取属性：
	 *	果树NPC的ID
	 */
	public long getNpcId(){
		return npcId;
	}

	/**
	 * 设置属性：
	 *	果树NPC的ID
	 */
	public void setNpcId(long npcId){
		this.npcId = npcId;
	}

	/**
	 * 获取属性：
	 *	家族成员类表
	 */
	public JiazuMember4Client[] getJiazuMembers(){
		return jiazuMembers;
	}

	/**
	 * 设置属性：
	 *	家族成员类表
	 */
	public void setJiazuMembers(JiazuMember4Client[] jiazuMembers){
		this.jiazuMembers = jiazuMembers;
	}

	/**
	 * 获取属性：
	 *	是否已经摘取过了
	 */
	public boolean[] getHasPickuped(){
		return hasPickuped;
	}

	/**
	 * 设置属性：
	 *	是否已经摘取过了
	 */
	public void setHasPickuped(boolean[] hasPickuped){
		this.hasPickuped = hasPickuped;
	}

}