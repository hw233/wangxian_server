package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.newChongZhiActivity.RmbRewardClientData;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端取首充和累计充值活动数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showPlayerLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rmbToMoneyBiLi</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chongzhiMoney</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouchong.dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouchong.needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouchong.entityID.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouchong.entityID</td><td>long[]</td><td>shouchong.entityID.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouchong.entityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouchong.entityNums</td><td>int[]</td><td>shouchong.entityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouchong.rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouchong.rewardRare</td><td>boolean[]</td><td>shouchong.rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouchong.isGetBefore</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouchong.showType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouchong.showMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis.length</td><td>int</td><td>4个字节</td><td>RmbRewardClientData数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[0].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[0].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[0].entityID.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[0].entityID</td><td>long[]</td><td>leijis[0].entityID.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[0].entityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[0].entityNums</td><td>int[]</td><td>leijis[0].entityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[0].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[0].rewardRare</td><td>boolean[]</td><td>leijis[0].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[0].isGetBefore</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[0].showType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[0].showMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[1].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[1].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[1].entityID.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[1].entityID</td><td>long[]</td><td>leijis[1].entityID.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[1].entityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[1].entityNums</td><td>int[]</td><td>leijis[1].entityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[1].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[1].rewardRare</td><td>boolean[]</td><td>leijis[1].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[1].isGetBefore</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[1].showType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[1].showMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[2].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[2].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[2].entityID.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[2].entityID</td><td>long[]</td><td>leijis[2].entityID.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[2].entityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[2].entityNums</td><td>int[]</td><td>leijis[2].entityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[2].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[2].rewardRare</td><td>boolean[]</td><td>leijis[2].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[2].isGetBefore</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leijis[2].showType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leijis[2].showMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GET_RMB_REWARDMSG_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int showPlayerLevel;
	long rmbToMoneyBiLi;
	long chongzhiMoney;
	RmbRewardClientData shouchong;
	RmbRewardClientData[] leijis;

	public GET_RMB_REWARDMSG_RES(){
	}

	public GET_RMB_REWARDMSG_RES(long seqNum,int showPlayerLevel,long rmbToMoneyBiLi,long chongzhiMoney,RmbRewardClientData shouchong,RmbRewardClientData[] leijis){
		this.seqNum = seqNum;
		this.showPlayerLevel = showPlayerLevel;
		this.rmbToMoneyBiLi = rmbToMoneyBiLi;
		this.chongzhiMoney = chongzhiMoney;
		this.shouchong = shouchong;
		this.leijis = leijis;
	}

	public GET_RMB_REWARDMSG_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		showPlayerLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		rmbToMoneyBiLi = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		chongzhiMoney = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		shouchong = new RmbRewardClientData();
		shouchong.setDataID((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		shouchong.setNeedMoney((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		long[] entityID_0001 = new long[len];
		for(int j = 0 ; j < entityID_0001.length ; j++){
			entityID_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		shouchong.setEntityID(entityID_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] entityNums_0002 = new int[len];
		for(int j = 0 ; j < entityNums_0002.length ; j++){
			entityNums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		shouchong.setEntityNums(entityNums_0002);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		boolean[] rewardRare_0003 = new boolean[len];
		for(int j = 0 ; j < rewardRare_0003.length ; j++){
			rewardRare_0003[j] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		shouchong.setRewardRare(rewardRare_0003);
		shouchong.setIsGetBefore(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		shouchong.setShowType((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		shouchong.setShowMoney((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		leijis = new RmbRewardClientData[len];
		for(int i = 0 ; i < leijis.length ; i++){
			leijis[i] = new RmbRewardClientData();
			leijis[i].setDataID((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			leijis[i].setNeedMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] entityID_0004 = new long[len];
			for(int j = 0 ; j < entityID_0004.length ; j++){
				entityID_0004[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			leijis[i].setEntityID(entityID_0004);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] entityNums_0005 = new int[len];
			for(int j = 0 ; j < entityNums_0005.length ; j++){
				entityNums_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			leijis[i].setEntityNums(entityNums_0005);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			boolean[] rewardRare_0006 = new boolean[len];
			for(int j = 0 ; j < rewardRare_0006.length ; j++){
				rewardRare_0006[j] = mf.byteArrayToNumber(content,offset,1) != 0;
				offset += 1;
			}
			leijis[i].setRewardRare(rewardRare_0006);
			leijis[i].setIsGetBefore(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			leijis[i].setShowType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			leijis[i].setShowMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
	}

	public int getType() {
		return 0x700EB101;
	}

	public String getTypeDescription() {
		return "GET_RMB_REWARDMSG_RES";
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
		len += 8;
		len += 8;
		len += 4;
		len += 8;
		len += 4;
		len += shouchong.getEntityID().length * 8;
		len += 4;
		len += shouchong.getEntityNums().length * 4;
		len += 4;
		len += shouchong.getRewardRare().length * 1;
		len += 1;
		len += 4;
		len += 8;
		len += 4;
		for(int i = 0 ; i < leijis.length ; i++){
			len += 4;
			len += 8;
			len += 4;
			len += leijis[i].getEntityID().length * 8;
			len += 4;
			len += leijis[i].getEntityNums().length * 4;
			len += 4;
			len += leijis[i].getRewardRare().length * 1;
			len += 1;
			len += 4;
			len += 8;
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

			buffer.putInt(showPlayerLevel);
			buffer.putLong(rmbToMoneyBiLi);
			buffer.putLong(chongzhiMoney);
			buffer.putInt((int)shouchong.getDataID());
			buffer.putLong(shouchong.getNeedMoney());
			buffer.putInt(shouchong.getEntityID().length);
			long[] entityID_0007 = shouchong.getEntityID();
			for(int j = 0 ; j < entityID_0007.length ; j++){
				buffer.putLong(entityID_0007[j]);
			}
			buffer.putInt(shouchong.getEntityNums().length);
			int[] entityNums_0008 = shouchong.getEntityNums();
			for(int j = 0 ; j < entityNums_0008.length ; j++){
				buffer.putInt(entityNums_0008[j]);
			}
			buffer.putInt(shouchong.getRewardRare().length);
			boolean[] rewardRare_0009 = shouchong.getRewardRare();
			for(int j = 0 ; j < rewardRare_0009.length ; j++){
				buffer.put((byte)(rewardRare_0009[j]?1:0));
			}
			buffer.put((byte)(shouchong.isIsGetBefore()==false?0:1));
			buffer.putInt((int)shouchong.getShowType());
			buffer.putLong(shouchong.getShowMoney());
			buffer.putInt(leijis.length);
			for(int i = 0 ; i < leijis.length ; i++){
				buffer.putInt((int)leijis[i].getDataID());
				buffer.putLong(leijis[i].getNeedMoney());
				buffer.putInt(leijis[i].getEntityID().length);
				long[] entityID_0010 = leijis[i].getEntityID();
				for(int j = 0 ; j < entityID_0010.length ; j++){
					buffer.putLong(entityID_0010[j]);
				}
				buffer.putInt(leijis[i].getEntityNums().length);
				int[] entityNums_0011 = leijis[i].getEntityNums();
				for(int j = 0 ; j < entityNums_0011.length ; j++){
					buffer.putInt(entityNums_0011[j]);
				}
				buffer.putInt(leijis[i].getRewardRare().length);
				boolean[] rewardRare_0012 = leijis[i].getRewardRare();
				for(int j = 0 ; j < rewardRare_0012.length ; j++){
					buffer.put((byte)(rewardRare_0012[j]?1:0));
				}
				buffer.put((byte)(leijis[i].isIsGetBefore()==false?0:1));
				buffer.putInt((int)leijis[i].getShowType());
				buffer.putLong(leijis[i].getShowMoney());
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
	 *	多少级显示
	 */
	public int getShowPlayerLevel(){
		return showPlayerLevel;
	}

	/**
	 * 设置属性：
	 *	多少级显示
	 */
	public void setShowPlayerLevel(int showPlayerLevel){
		this.showPlayerLevel = showPlayerLevel;
	}

	/**
	 * 获取属性：
	 *	1元人民币对应的银子比例
	 */
	public long getRmbToMoneyBiLi(){
		return rmbToMoneyBiLi;
	}

	/**
	 * 设置属性：
	 *	1元人民币对应的银子比例
	 */
	public void setRmbToMoneyBiLi(long rmbToMoneyBiLi){
		this.rmbToMoneyBiLi = rmbToMoneyBiLi;
	}

	/**
	 * 获取属性：
	 *	累计充值金额
	 */
	public long getChongzhiMoney(){
		return chongzhiMoney;
	}

	/**
	 * 设置属性：
	 *	累计充值金额
	 */
	public void setChongzhiMoney(long chongzhiMoney){
		this.chongzhiMoney = chongzhiMoney;
	}

	/**
	 * 获取属性：
	 *	首充活动
	 */
	public RmbRewardClientData getShouchong(){
		return shouchong;
	}

	/**
	 * 设置属性：
	 *	首充活动
	 */
	public void setShouchong(RmbRewardClientData shouchong){
		this.shouchong = shouchong;
	}

	/**
	 * 获取属性：
	 *	累计活动
	 */
	public RmbRewardClientData[] getLeijis(){
		return leijis;
	}

	/**
	 * 设置属性：
	 *	累计活动
	 */
	public void setLeijis(RmbRewardClientData[] leijis){
		this.leijis = leijis;
	}

}