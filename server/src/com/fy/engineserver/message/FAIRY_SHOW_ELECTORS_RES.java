package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看投票榜<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos.length</td><td>int</td><td>4个字节</td><td>FairyBuddhaInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[0].name</td><td>String</td><td>fairyBuddhaInfos[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[0].country</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[0].declaration.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[0].declaration</td><td>String</td><td>fairyBuddhaInfos[0].declaration.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[0].score</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[0].lastChangeTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[0].lastChangeTime</td><td>String</td><td>fairyBuddhaInfos[0].lastChangeTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[1].name</td><td>String</td><td>fairyBuddhaInfos[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[1].country</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[1].declaration.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[1].declaration</td><td>String</td><td>fairyBuddhaInfos[1].declaration.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[1].score</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[1].lastChangeTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[1].lastChangeTime</td><td>String</td><td>fairyBuddhaInfos[1].lastChangeTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[2].name</td><td>String</td><td>fairyBuddhaInfos[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[2].country</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[2].declaration.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[2].declaration</td><td>String</td><td>fairyBuddhaInfos[2].declaration.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[2].score</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fairyBuddhaInfos[2].lastChangeTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fairyBuddhaInfos[2].lastChangeTime</td><td>String</td><td>fairyBuddhaInfos[2].lastChangeTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showButton.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showButton</td><td>boolean[]</td><td>showButton.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cycle</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>career</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class FAIRY_SHOW_ELECTORS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	FairyBuddhaInfo[] fairyBuddhaInfos;
	boolean[] showButton;
	int cycle;
	byte career;

	public FAIRY_SHOW_ELECTORS_RES(){
	}

	public FAIRY_SHOW_ELECTORS_RES(long seqNum,FairyBuddhaInfo[] fairyBuddhaInfos,boolean[] showButton,int cycle,byte career){
		this.seqNum = seqNum;
		this.fairyBuddhaInfos = fairyBuddhaInfos;
		this.showButton = showButton;
		this.cycle = cycle;
		this.career = career;
	}

	public FAIRY_SHOW_ELECTORS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		fairyBuddhaInfos = new FairyBuddhaInfo[len];
		for(int i = 0 ; i < fairyBuddhaInfos.length ; i++){
			fairyBuddhaInfos[i] = new FairyBuddhaInfo();
			fairyBuddhaInfos[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			fairyBuddhaInfos[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			fairyBuddhaInfos[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			fairyBuddhaInfos[i].setCountry((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			fairyBuddhaInfos[i].setDeclaration(new String(content,offset,len,"UTF-8"));
			offset += len;
			fairyBuddhaInfos[i].setScore((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			fairyBuddhaInfos[i].setLastChangeTime(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		showButton = new boolean[len];
		for(int i = 0 ; i < showButton.length ; i++){
			showButton[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		cycle = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		career = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x70F0EF28;
	}

	public String getTypeDescription() {
		return "FAIRY_SHOW_ELECTORS_RES";
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
		for(int i = 0 ; i < fairyBuddhaInfos.length ; i++){
			len += 8;
			len += 2;
			if(fairyBuddhaInfos[i].getName() != null){
				try{
				len += fairyBuddhaInfos[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 2;
			if(fairyBuddhaInfos[i].getDeclaration() != null){
				try{
				len += fairyBuddhaInfos[i].getDeclaration().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 2;
			if(fairyBuddhaInfos[i].getLastChangeTime() != null){
				try{
				len += fairyBuddhaInfos[i].getLastChangeTime().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		len += showButton.length;
		len += 4;
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

			buffer.putInt(fairyBuddhaInfos.length);
			for(int i = 0 ; i < fairyBuddhaInfos.length ; i++){
				buffer.putLong(fairyBuddhaInfos[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = fairyBuddhaInfos[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)fairyBuddhaInfos[i].getLevel());
				buffer.putInt((int)fairyBuddhaInfos[i].getCountry());
				try{
				tmpBytes2 = fairyBuddhaInfos[i].getDeclaration().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)fairyBuddhaInfos[i].getScore());
				try{
				tmpBytes2 = fairyBuddhaInfos[i].getLastChangeTime().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(showButton.length);
			for(int i = 0 ; i < showButton.length; i++){
				buffer.put((byte)(showButton[i]==false?0:1));
			}
			buffer.putInt(cycle);
			buffer.put(career);
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
	 *	参选者信息
	 */
	public FairyBuddhaInfo[] getFairyBuddhaInfos(){
		return fairyBuddhaInfos;
	}

	/**
	 * 设置属性：
	 *	参选者信息
	 */
	public void setFairyBuddhaInfos(FairyBuddhaInfo[] fairyBuddhaInfos){
		this.fairyBuddhaInfos = fairyBuddhaInfos;
	}

	/**
	 * 获取属性：
	 *	true:显示;false:不显示
	 */
	public boolean[] getShowButton(){
		return showButton;
	}

	/**
	 * 设置属性：
	 *	true:显示;false:不显示
	 */
	public void setShowButton(boolean[] showButton){
		this.showButton = showButton;
	}

	/**
	 * 获取属性：
	 *	0:本期;-1:上期
	 */
	public int getCycle(){
		return cycle;
	}

	/**
	 * 设置属性：
	 *	0:本期;-1:上期
	 */
	public void setCycle(int cycle){
		this.cycle = cycle;
	}

	/**
	 * 获取属性：
	 *	投票榜职业
	 */
	public byte getCareer(){
		return career;
	}

	/**
	 * 设置属性：
	 *	投票榜职业
	 */
	public void setCareer(byte career){
		this.career = career;
	}

}