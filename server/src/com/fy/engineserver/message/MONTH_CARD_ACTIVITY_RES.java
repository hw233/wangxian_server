package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.chongzhiActivity.MonthCardInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 月卡活动<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos.length</td><td>int</td><td>4个字节</td><td>MonthCardInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].cardName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].cardName</td><td>String</td><td>infos[0].cardName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].cardId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].cardId</td><td>String</td><td>infos[0].cardId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].chargeMoney.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].chargeMoney</td><td>String</td><td>infos[0].chargeMoney.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].chargeRewardIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].chargeRewardIds</td><td>long[]</td><td>infos[0].chargeRewardIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].chargeRewardCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].chargeRewardCounts</td><td>int[]</td><td>infos[0].chargeRewardCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].dayRewardIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].dayRewardIds</td><td>long[]</td><td>infos[0].dayRewardIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].dayRewardCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].dayRewardCounts</td><td>int[]</td><td>infos[0].dayRewardCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].dayRewardInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].dayRewardInfo</td><td>String</td><td>infos[0].dayRewardInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].hasDayMess</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].canReward</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[0].str1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[0].str1</td><td>String</td><td>infos[0].str1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].cardName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].cardName</td><td>String</td><td>infos[1].cardName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].cardId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].cardId</td><td>String</td><td>infos[1].cardId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].chargeMoney.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].chargeMoney</td><td>String</td><td>infos[1].chargeMoney.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].chargeRewardIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].chargeRewardIds</td><td>long[]</td><td>infos[1].chargeRewardIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].chargeRewardCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].chargeRewardCounts</td><td>int[]</td><td>infos[1].chargeRewardCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].dayRewardIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].dayRewardIds</td><td>long[]</td><td>infos[1].dayRewardIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].dayRewardCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].dayRewardCounts</td><td>int[]</td><td>infos[1].dayRewardCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].dayRewardInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].dayRewardInfo</td><td>String</td><td>infos[1].dayRewardInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].hasDayMess</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].canReward</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[1].str1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[1].str1</td><td>String</td><td>infos[1].str1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].cardName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].cardName</td><td>String</td><td>infos[2].cardName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].cardId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].cardId</td><td>String</td><td>infos[2].cardId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].chargeMoney.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].chargeMoney</td><td>String</td><td>infos[2].chargeMoney.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].chargeRewardIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].chargeRewardIds</td><td>long[]</td><td>infos[2].chargeRewardIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].chargeRewardCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].chargeRewardCounts</td><td>int[]</td><td>infos[2].chargeRewardCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].dayRewardIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].dayRewardIds</td><td>long[]</td><td>infos[2].dayRewardIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].dayRewardCounts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].dayRewardCounts</td><td>int[]</td><td>infos[2].dayRewardCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].dayRewardInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].dayRewardInfo</td><td>String</td><td>infos[2].dayRewardInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].hasDayMess</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].canReward</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos[2].str1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos[2].str1</td><td>String</td><td>infos[2].str1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class MONTH_CARD_ACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	MonthCardInfo[] infos;

	public MONTH_CARD_ACTIVITY_RES(){
	}

	public MONTH_CARD_ACTIVITY_RES(long seqNum,MonthCardInfo[] infos){
		this.seqNum = seqNum;
		this.infos = infos;
	}

	public MONTH_CARD_ACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		infos = new MonthCardInfo[len];
		for(int i = 0 ; i < infos.length ; i++){
			infos[i] = new MonthCardInfo();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setCardName(new String(content,offset,len));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setCardId(new String(content,offset,len));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setChargeMoney(new String(content,offset,len));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] chargeRewardIds_0001 = new long[len];
			for(int j = 0 ; j < chargeRewardIds_0001.length ; j++){
				chargeRewardIds_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			infos[i].setChargeRewardIds(chargeRewardIds_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] chargeRewardCounts_0002 = new int[len];
			for(int j = 0 ; j < chargeRewardCounts_0002.length ; j++){
				chargeRewardCounts_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			infos[i].setChargeRewardCounts(chargeRewardCounts_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] dayRewardIds_0003 = new long[len];
			for(int j = 0 ; j < dayRewardIds_0003.length ; j++){
				dayRewardIds_0003[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			infos[i].setDayRewardIds(dayRewardIds_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] dayRewardCounts_0004 = new int[len];
			for(int j = 0 ; j < dayRewardCounts_0004.length ; j++){
				dayRewardCounts_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			infos[i].setDayRewardCounts(dayRewardCounts_0004);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setDayRewardInfo(new String(content,offset,len));
			offset += len;
			infos[i].setHasDayMess((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			infos[i].setCanReward(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			infos[i].setStr1(new String(content,offset,len));
			offset += len;
		}
	}

	public int getType() {
		return 0x70FF0114;
	}

	public String getTypeDescription() {
		return "MONTH_CARD_ACTIVITY_RES";
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
		for(int i = 0 ; i < infos.length ; i++){
			len += 2;
			if(infos[i].getCardName() != null){
				len += infos[i].getCardName().getBytes().length;
			}
			len += 2;
			if(infos[i].getCardId() != null){
				len += infos[i].getCardId().getBytes().length;
			}
			len += 2;
			if(infos[i].getChargeMoney() != null){
				len += infos[i].getChargeMoney().getBytes().length;
			}
			len += 4;
			len += infos[i].getChargeRewardIds().length * 8;
			len += 4;
			len += infos[i].getChargeRewardCounts().length * 4;
			len += 4;
			len += infos[i].getDayRewardIds().length * 8;
			len += 4;
			len += infos[i].getDayRewardCounts().length * 4;
			len += 2;
			if(infos[i].getDayRewardInfo() != null){
				len += infos[i].getDayRewardInfo().getBytes().length;
			}
			len += 8;
			len += 1;
			len += 2;
			if(infos[i].getStr1() != null){
				len += infos[i].getStr1().getBytes().length;
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

			buffer.putInt(infos.length);
			for(int i = 0 ; i < infos.length ; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = infos[i].getCardName().getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				tmpBytes2 = infos[i].getCardId().getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				tmpBytes2 = infos[i].getChargeMoney().getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(infos[i].getChargeRewardIds().length);
				long[] chargeRewardIds_0005 = infos[i].getChargeRewardIds();
				for(int j = 0 ; j < chargeRewardIds_0005.length ; j++){
					buffer.putLong(chargeRewardIds_0005[j]);
				}
				buffer.putInt(infos[i].getChargeRewardCounts().length);
				int[] chargeRewardCounts_0006 = infos[i].getChargeRewardCounts();
				for(int j = 0 ; j < chargeRewardCounts_0006.length ; j++){
					buffer.putInt(chargeRewardCounts_0006[j]);
				}
				buffer.putInt(infos[i].getDayRewardIds().length);
				long[] dayRewardIds_0007 = infos[i].getDayRewardIds();
				for(int j = 0 ; j < dayRewardIds_0007.length ; j++){
					buffer.putLong(dayRewardIds_0007[j]);
				}
				buffer.putInt(infos[i].getDayRewardCounts().length);
				int[] dayRewardCounts_0008 = infos[i].getDayRewardCounts();
				for(int j = 0 ; j < dayRewardCounts_0008.length ; j++){
					buffer.putInt(dayRewardCounts_0008[j]);
				}
				tmpBytes2 = infos[i].getDayRewardInfo().getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(infos[i].getHasDayMess());
				buffer.put((byte)(infos[i].isCanReward()==false?0:1));
				tmpBytes2 = infos[i].getStr1().getBytes();
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
	 *	月卡信息
	 */
	public MonthCardInfo[] getInfos(){
		return infos;
	}

	/**
	 * 设置属性：
	 *	月卡信息
	 */
	public void setInfos(MonthCardInfo[] infos){
		this.infos = infos;
	}

}