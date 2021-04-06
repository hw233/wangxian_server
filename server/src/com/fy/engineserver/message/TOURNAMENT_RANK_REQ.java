package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.tournament.data.TournamentRankDataClient;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 比武排名面板<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients.length</td><td>int</td><td>4个字节</td><td>TournamentRankDataClient数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[0].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[0].name</td><td>String</td><td>tournamentRankDataClients[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[0].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[0].point</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[0].win</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[0].lost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[1].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[1].name</td><td>String</td><td>tournamentRankDataClients[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[1].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[1].point</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[1].win</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[1].lost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[2].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[2].name</td><td>String</td><td>tournamentRankDataClients[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[2].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[2].point</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tournamentRankDataClients[2].win</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tournamentRankDataClients[2].lost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>myRankInWeek.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>myRankInWeek</td><td>String</td><td>myRankInWeek.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>myPointInWeek</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>myWinsInWeek</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>myLostsInWeek</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canTakeReward</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class TOURNAMENT_RANK_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	TournamentRankDataClient[] tournamentRankDataClients;
	String myRankInWeek;
	int myPointInWeek;
	int myWinsInWeek;
	int myLostsInWeek;
	boolean canTakeReward;

	public TOURNAMENT_RANK_REQ(){
	}

	public TOURNAMENT_RANK_REQ(long seqNum,TournamentRankDataClient[] tournamentRankDataClients,String myRankInWeek,int myPointInWeek,int myWinsInWeek,int myLostsInWeek,boolean canTakeReward){
		this.seqNum = seqNum;
		this.tournamentRankDataClients = tournamentRankDataClients;
		this.myRankInWeek = myRankInWeek;
		this.myPointInWeek = myPointInWeek;
		this.myWinsInWeek = myWinsInWeek;
		this.myLostsInWeek = myLostsInWeek;
		this.canTakeReward = canTakeReward;
	}

	public TOURNAMENT_RANK_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		tournamentRankDataClients = new TournamentRankDataClient[len];
		for(int i = 0 ; i < tournamentRankDataClients.length ; i++){
			tournamentRankDataClients[i] = new TournamentRankDataClient();
			tournamentRankDataClients[i].setCountry((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			tournamentRankDataClients[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tournamentRankDataClients[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			tournamentRankDataClients[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			tournamentRankDataClients[i].setLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			tournamentRankDataClients[i].setPoint((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			tournamentRankDataClients[i].setWin((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			tournamentRankDataClients[i].setLost((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		myRankInWeek = new String(content,offset,len,"UTF-8");
		offset += len;
		myPointInWeek = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		myWinsInWeek = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		myLostsInWeek = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		canTakeReward = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x0F300026;
	}

	public String getTypeDescription() {
		return "TOURNAMENT_RANK_REQ";
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
		for(int i = 0 ; i < tournamentRankDataClients.length ; i++){
			len += 1;
			len += 8;
			len += 2;
			if(tournamentRankDataClients[i].getName() != null){
				try{
				len += tournamentRankDataClients[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			len += 4;
			len += 4;
			len += 4;
		}
		len += 2;
		try{
			len +=myRankInWeek.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
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

			buffer.putInt(tournamentRankDataClients.length);
			for(int i = 0 ; i < tournamentRankDataClients.length ; i++){
				buffer.put((byte)tournamentRankDataClients[i].getCountry());
				buffer.putLong(tournamentRankDataClients[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = tournamentRankDataClients[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)tournamentRankDataClients[i].getCareer());
				buffer.putShort((short)tournamentRankDataClients[i].getLevel());
				buffer.putInt((int)tournamentRankDataClients[i].getPoint());
				buffer.putInt((int)tournamentRankDataClients[i].getWin());
				buffer.putInt((int)tournamentRankDataClients[i].getLost());
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = myRankInWeek.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(myPointInWeek);
			buffer.putInt(myWinsInWeek);
			buffer.putInt(myLostsInWeek);
			buffer.put((byte)(canTakeReward==false?0:1));
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
	 *	比武排名数组
	 */
	public TournamentRankDataClient[] getTournamentRankDataClients(){
		return tournamentRankDataClients;
	}

	/**
	 * 设置属性：
	 *	比武排名数组
	 */
	public void setTournamentRankDataClients(TournamentRankDataClient[] tournamentRankDataClients){
		this.tournamentRankDataClients = tournamentRankDataClients;
	}

	/**
	 * 获取属性：
	 *	我的本周排名
	 */
	public String getMyRankInWeek(){
		return myRankInWeek;
	}

	/**
	 * 设置属性：
	 *	我的本周排名
	 */
	public void setMyRankInWeek(String myRankInWeek){
		this.myRankInWeek = myRankInWeek;
	}

	/**
	 * 获取属性：
	 *	我的本周积分
	 */
	public int getMyPointInWeek(){
		return myPointInWeek;
	}

	/**
	 * 设置属性：
	 *	我的本周积分
	 */
	public void setMyPointInWeek(int myPointInWeek){
		this.myPointInWeek = myPointInWeek;
	}

	/**
	 * 获取属性：
	 *	我的本周胜场
	 */
	public int getMyWinsInWeek(){
		return myWinsInWeek;
	}

	/**
	 * 设置属性：
	 *	我的本周胜场
	 */
	public void setMyWinsInWeek(int myWinsInWeek){
		this.myWinsInWeek = myWinsInWeek;
	}

	/**
	 * 获取属性：
	 *	我的本周失败场数
	 */
	public int getMyLostsInWeek(){
		return myLostsInWeek;
	}

	/**
	 * 设置属性：
	 *	我的本周失败场数
	 */
	public void setMyLostsInWeek(int myLostsInWeek){
		this.myLostsInWeek = myLostsInWeek;
	}

	/**
	 * 获取属性：
	 *	是否可以领取比武奖励
	 */
	public boolean getCanTakeReward(){
		return canTakeReward;
	}

	/**
	 * 设置属性：
	 *	是否可以领取比武奖励
	 */
	public void setCanTakeReward(boolean canTakeReward){
		this.canTakeReward = canTakeReward;
	}

}