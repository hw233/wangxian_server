package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.battlefield.concrete.BattleFieldStatData;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询战场详细信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>forceOpenWindow</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>winner.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>winner</td><td>String</td><td>winner.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas.length</td><td>int</td><td>4个字节</td><td>BattleFieldStatData数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[0].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[0].playerName</td><td>String</td><td>statDatas[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[0].career.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[0].career</td><td>String</td><td>statDatas[0].career.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[0].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[0].battleSide</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[0].killingNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[0].killedNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[0].honorKillingNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[0].honorPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[0].totalDamage</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[0].totalEnhenceHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[0].description</td><td>String</td><td>statDatas[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[1].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[1].playerName</td><td>String</td><td>statDatas[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[1].career.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[1].career</td><td>String</td><td>statDatas[1].career.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[1].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[1].battleSide</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[1].killingNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[1].killedNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[1].honorKillingNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[1].honorPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[1].totalDamage</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[1].totalEnhenceHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[1].description</td><td>String</td><td>statDatas[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[2].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[2].playerName</td><td>String</td><td>statDatas[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[2].career.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[2].career</td><td>String</td><td>statDatas[2].career.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[2].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[2].battleSide</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[2].killingNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[2].killedNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[2].honorKillingNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[2].honorPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[2].totalDamage</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[2].totalEnhenceHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statDatas[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statDatas[2].description</td><td>String</td><td>statDatas[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_BATTLEFIELD_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String name;
	boolean forceOpenWindow;
	String winner;
	BattleFieldStatData[] statDatas;

	public QUERY_BATTLEFIELD_INFO_RES(){
	}

	public QUERY_BATTLEFIELD_INFO_RES(long seqNum,String name,boolean forceOpenWindow,String winner,BattleFieldStatData[] statDatas){
		this.seqNum = seqNum;
		this.name = name;
		this.forceOpenWindow = forceOpenWindow;
		this.winner = winner;
		this.statDatas = statDatas;
	}

	public QUERY_BATTLEFIELD_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		forceOpenWindow = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		winner = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		statDatas = new BattleFieldStatData[len];
		for(int i = 0 ; i < statDatas.length ; i++){
			statDatas[i] = new BattleFieldStatData();
			statDatas[i].setPlayerId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			statDatas[i].setPlayerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			statDatas[i].setCareer(new String(content,offset,len,"UTF-8"));
			offset += len;
			statDatas[i].setPlayerLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			statDatas[i].setBattleSide((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			statDatas[i].setKillingNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			statDatas[i].setKilledNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			statDatas[i].setHonorKillingNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			statDatas[i].setHonorPoints((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			statDatas[i].setTotalDamage((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			statDatas[i].setTotalEnhenceHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			statDatas[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x7000AF22;
	}

	public String getTypeDescription() {
		return "QUERY_BATTLEFIELD_INFO_RES";
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
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 2;
		try{
			len +=winner.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < statDatas.length ; i++){
			len += 8;
			len += 2;
			if(statDatas[i].getPlayerName() != null){
				try{
				len += statDatas[i].getPlayerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(statDatas[i].getCareer() != null){
				try{
				len += statDatas[i].getCareer().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(statDatas[i].getDescription() != null){
				try{
				len += statDatas[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(forceOpenWindow==false?0:1));
				try{
			tmpBytes1 = winner.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(statDatas.length);
			for(int i = 0 ; i < statDatas.length ; i++){
				buffer.putLong(statDatas[i].getPlayerId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = statDatas[i].getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = statDatas[i].getCareer().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)statDatas[i].getPlayerLevel());
				buffer.putInt((int)statDatas[i].getBattleSide());
				buffer.putInt((int)statDatas[i].getKillingNum());
				buffer.putInt((int)statDatas[i].getKilledNum());
				buffer.putInt((int)statDatas[i].getHonorKillingNum());
				buffer.putInt((int)statDatas[i].getHonorPoints());
				buffer.putInt((int)statDatas[i].getTotalDamage());
				buffer.putInt((int)statDatas[i].getTotalEnhenceHp());
				try{
				tmpBytes2 = statDatas[i].getDescription().getBytes("UTF-8");
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
	 *	战场的名称
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	战场的名称
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	是否强制打开窗口
	 */
	public boolean getForceOpenWindow(){
		return forceOpenWindow;
	}

	/**
	 * 设置属性：
	 *	是否强制打开窗口
	 */
	public void setForceOpenWindow(boolean forceOpenWindow){
		this.forceOpenWindow = forceOpenWindow;
	}

	/**
	 * 获取属性：
	 *	胜利方，如果战场正在进行中，此字符串长度为0
	 */
	public String getWinner(){
		return winner;
	}

	/**
	 * 设置属性：
	 *	胜利方，如果战场正在进行中，此字符串长度为0
	 */
	public void setWinner(String winner){
		this.winner = winner;
	}

	/**
	 * 获取属性：
	 *	统计信息，按荣誉值从大到小排列
	 */
	public BattleFieldStatData[] getStatDatas(){
		return statDatas;
	}

	/**
	 * 设置属性：
	 *	统计信息，按荣誉值从大到小排列
	 */
	public void setStatDatas(BattleFieldStatData[] statDatas){
		this.statDatas = statDatas;
	}

}