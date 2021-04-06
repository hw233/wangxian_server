package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.dice.DiceBillboardData;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 骰子副本排行<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>myRank</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterNextLayerTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterMess</td><td>String</td><td>enterMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exps</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas.length</td><td>int</td><td>4个字节</td><td>DiceBillboardData数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].rank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].playerName</td><td>String</td><td>datas[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].point</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].rank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].playerName</td><td>String</td><td>datas[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].point</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].rank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].playerName</td><td>String</td><td>datas[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].point</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class DICE_BILLBOARD_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int myRank;
	int enterNextLayerTime;
	String enterMess;
	long exps;
	DiceBillboardData[] datas;

	public DICE_BILLBOARD_RES(){
	}

	public DICE_BILLBOARD_RES(long seqNum,int myRank,int enterNextLayerTime,String enterMess,long exps,DiceBillboardData[] datas){
		this.seqNum = seqNum;
		this.myRank = myRank;
		this.enterNextLayerTime = enterNextLayerTime;
		this.enterMess = enterMess;
		this.exps = exps;
		this.datas = datas;
	}

	public DICE_BILLBOARD_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		myRank = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		enterNextLayerTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		enterMess = new String(content,offset,len,"UTF-8");
		offset += len;
		exps = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		datas = new DiceBillboardData[len];
		for(int i = 0 ; i < datas.length ; i++){
			datas[i] = new DiceBillboardData();
			datas[i].setRank((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			datas[i].setPlayerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			datas[i].setPoint((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0160;
	}

	public String getTypeDescription() {
		return "DICE_BILLBOARD_RES";
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
		len += 4;
		len += 2;
		try{
			len +=enterMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 4;
		for(int i = 0 ; i < datas.length ; i++){
			len += 4;
			len += 2;
			if(datas[i].getPlayerName() != null){
				try{
				len += datas[i].getPlayerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
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

			buffer.putInt(myRank);
			buffer.putInt(enterNextLayerTime);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = enterMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(exps);
			buffer.putInt(datas.length);
			for(int i = 0 ; i < datas.length ; i++){
				buffer.putInt((int)datas[i].getRank());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = datas[i].getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)datas[i].getPoint());
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
	 *	我的排行
	 */
	public int getMyRank(){
		return myRank;
	}

	/**
	 * 设置属性：
	 *	我的排行
	 */
	public void setMyRank(int myRank){
		this.myRank = myRank;
	}

	/**
	 * 获取属性：
	 *	进入下一层需要的时间
	 */
	public int getEnterNextLayerTime(){
		return enterNextLayerTime;
	}

	/**
	 * 设置属性：
	 *	进入下一层需要的时间
	 */
	public void setEnterNextLayerTime(int enterNextLayerTime){
		this.enterNextLayerTime = enterNextLayerTime;
	}

	/**
	 * 获取属性：
	 *	进入下一层信息
	 */
	public String getEnterMess(){
		return enterMess;
	}

	/**
	 * 设置属性：
	 *	进入下一层信息
	 */
	public void setEnterMess(String enterMess){
		this.enterMess = enterMess;
	}

	/**
	 * 获取属性：
	 *	经验
	 */
	public long getExps(){
		return exps;
	}

	/**
	 * 设置属性：
	 *	经验
	 */
	public void setExps(long exps){
		this.exps = exps;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public DiceBillboardData[] getDatas(){
		return datas;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDatas(DiceBillboardData[] datas){
		this.datas = datas;
	}

}