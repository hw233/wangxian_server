package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 国战的主控制面板，不同的角色具有不同的权限(新)<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description</td><td>String</td><td>description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossName[0]</td><td>String</td><td>bossName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossName[1]</td><td>String</td><td>bossName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossName[2]</td><td>String</td><td>bossName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossIcon.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossIcon[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossIcon[0]</td><td>String</td><td>bossIcon[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossIcon[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossIcon[1]</td><td>String</td><td>bossIcon[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossIcon[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossIcon[2]</td><td>String</td><td>bossIcon[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossHP.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossHP</td><td>int[]</td><td>bossHP.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossStatus.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossStatus</td><td>int[]</td><td>bossStatus.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossCanAddHP.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossCanAddHP</td><td>boolean[]</td><td>bossCanAddHP.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossAddHPLeftTimes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossAddHPLeftTimes</td><td>int[]</td><td>bossAddHPLeftTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossAddHPTotalTimes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossAddHPTotalTimes</td><td>int[]</td><td>bossAddHPTotalTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>canDelayTime</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>delayLeftTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>delayTotalTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canPullPlayer</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pullPlayerLeftTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pullPlayerTotalTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornPointName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornPointName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornPointName[0]</td><td>String</td><td>bornPointName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornPointName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornPointName[1]</td><td>String</td><td>bornPointName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornPointName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornPointName[2]</td><td>String</td><td>bornPointName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornPointBelong.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornPointBelong[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornPointBelong[0]</td><td>String</td><td>bornPointBelong[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornPointBelong[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornPointBelong[1]</td><td>String</td><td>bornPointBelong[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornPointBelong[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornPointBelong[2]</td><td>String</td><td>bornPointBelong[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderIcon.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderIcon[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderIcon[0]</td><td>String</td><td>orderIcon[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderIcon[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderIcon[1]</td><td>String</td><td>orderIcon[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderIcon[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderIcon[2]</td><td>String</td><td>orderIcon[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderName[0]</td><td>String</td><td>orderName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderName[1]</td><td>String</td><td>orderName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderName[2]</td><td>String</td><td>orderName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pubOrderName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pubOrderName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pubOrderName[0]</td><td>String</td><td>pubOrderName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pubOrderName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pubOrderName[1]</td><td>String</td><td>pubOrderName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pubOrderName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pubOrderName[2]</td><td>String</td><td>pubOrderName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pubOrderMessage.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pubOrderMessage[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pubOrderMessage[0]</td><td>String</td><td>pubOrderMessage[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pubOrderMessage[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pubOrderMessage[1]</td><td>String</td><td>pubOrderMessage[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pubOrderMessage[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pubOrderMessage[2]</td><td>String</td><td>pubOrderMessage[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>guozhanResource</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class GUOZHAN_CONTROL_PANNEL_NEW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String description;
	String[] bossName;
	String[] bossIcon;
	int[] bossHP;
	int[] bossStatus;
	boolean[] bossCanAddHP;
	int[] bossAddHPLeftTimes;
	int[] bossAddHPTotalTimes;
	int leftTime;
	boolean canDelayTime;
	int delayLeftTimes;
	int delayTotalTimes;
	boolean canPullPlayer;
	int pullPlayerLeftTimes;
	int pullPlayerTotalTimes;
	String[] bornPointName;
	String[] bornPointBelong;
	String[] orderIcon;
	String[] orderName;
	String[] pubOrderName;
	String[] pubOrderMessage;
	long guozhanResource;

	public GUOZHAN_CONTROL_PANNEL_NEW_RES(){
	}

	public GUOZHAN_CONTROL_PANNEL_NEW_RES(long seqNum,String description,String[] bossName,String[] bossIcon,int[] bossHP,int[] bossStatus,boolean[] bossCanAddHP,int[] bossAddHPLeftTimes,int[] bossAddHPTotalTimes,int leftTime,boolean canDelayTime,int delayLeftTimes,int delayTotalTimes,boolean canPullPlayer,int pullPlayerLeftTimes,int pullPlayerTotalTimes,String[] bornPointName,String[] bornPointBelong,String[] orderIcon,String[] orderName,String[] pubOrderName,String[] pubOrderMessage,long guozhanResource){
		this.seqNum = seqNum;
		this.description = description;
		this.bossName = bossName;
		this.bossIcon = bossIcon;
		this.bossHP = bossHP;
		this.bossStatus = bossStatus;
		this.bossCanAddHP = bossCanAddHP;
		this.bossAddHPLeftTimes = bossAddHPLeftTimes;
		this.bossAddHPTotalTimes = bossAddHPTotalTimes;
		this.leftTime = leftTime;
		this.canDelayTime = canDelayTime;
		this.delayLeftTimes = delayLeftTimes;
		this.delayTotalTimes = delayTotalTimes;
		this.canPullPlayer = canPullPlayer;
		this.pullPlayerLeftTimes = pullPlayerLeftTimes;
		this.pullPlayerTotalTimes = pullPlayerTotalTimes;
		this.bornPointName = bornPointName;
		this.bornPointBelong = bornPointBelong;
		this.orderIcon = orderIcon;
		this.orderName = orderName;
		this.pubOrderName = pubOrderName;
		this.pubOrderMessage = pubOrderMessage;
		this.guozhanResource = guozhanResource;
	}

	public GUOZHAN_CONTROL_PANNEL_NEW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		description = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bossName = new String[len];
		for(int i = 0 ; i < bossName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			bossName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bossIcon = new String[len];
		for(int i = 0 ; i < bossIcon.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			bossIcon[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bossHP = new int[len];
		for(int i = 0 ; i < bossHP.length ; i++){
			bossHP[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bossStatus = new int[len];
		for(int i = 0 ; i < bossStatus.length ; i++){
			bossStatus[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bossCanAddHP = new boolean[len];
		for(int i = 0 ; i < bossCanAddHP.length ; i++){
			bossCanAddHP[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bossAddHPLeftTimes = new int[len];
		for(int i = 0 ; i < bossAddHPLeftTimes.length ; i++){
			bossAddHPLeftTimes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bossAddHPTotalTimes = new int[len];
		for(int i = 0 ; i < bossAddHPTotalTimes.length ; i++){
			bossAddHPTotalTimes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		leftTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		canDelayTime = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		delayLeftTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		delayTotalTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		canPullPlayer = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		pullPlayerLeftTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		pullPlayerTotalTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bornPointName = new String[len];
		for(int i = 0 ; i < bornPointName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			bornPointName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bornPointBelong = new String[len];
		for(int i = 0 ; i < bornPointBelong.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			bornPointBelong[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		orderIcon = new String[len];
		for(int i = 0 ; i < orderIcon.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			orderIcon[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		orderName = new String[len];
		for(int i = 0 ; i < orderName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			orderName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		pubOrderName = new String[len];
		for(int i = 0 ; i < pubOrderName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			pubOrderName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		pubOrderMessage = new String[len];
		for(int i = 0 ; i < pubOrderMessage.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			pubOrderMessage[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		guozhanResource = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70EE0020;
	}

	public String getTypeDescription() {
		return "GUOZHAN_CONTROL_PANNEL_NEW_RES";
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
			len +=description.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < bossName.length; i++){
			len += 2;
			try{
				len += bossName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < bossIcon.length; i++){
			len += 2;
			try{
				len += bossIcon[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += bossHP.length * 4;
		len += 4;
		len += bossStatus.length * 4;
		len += 4;
		len += bossCanAddHP.length;
		len += 4;
		len += bossAddHPLeftTimes.length * 4;
		len += 4;
		len += bossAddHPTotalTimes.length * 4;
		len += 4;
		len += 1;
		len += 4;
		len += 4;
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < bornPointName.length; i++){
			len += 2;
			try{
				len += bornPointName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < bornPointBelong.length; i++){
			len += 2;
			try{
				len += bornPointBelong[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < orderIcon.length; i++){
			len += 2;
			try{
				len += orderIcon[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < orderName.length; i++){
			len += 2;
			try{
				len += orderName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < pubOrderName.length; i++){
			len += 2;
			try{
				len += pubOrderName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < pubOrderMessage.length; i++){
			len += 2;
			try{
				len += pubOrderMessage[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
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
			 tmpBytes1 = description.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(bossName.length);
			for(int i = 0 ; i < bossName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = bossName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(bossIcon.length);
			for(int i = 0 ; i < bossIcon.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = bossIcon[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(bossHP.length);
			for(int i = 0 ; i < bossHP.length; i++){
				buffer.putInt(bossHP[i]);
			}
			buffer.putInt(bossStatus.length);
			for(int i = 0 ; i < bossStatus.length; i++){
				buffer.putInt(bossStatus[i]);
			}
			buffer.putInt(bossCanAddHP.length);
			for(int i = 0 ; i < bossCanAddHP.length; i++){
				buffer.put((byte)(bossCanAddHP[i]==false?0:1));
			}
			buffer.putInt(bossAddHPLeftTimes.length);
			for(int i = 0 ; i < bossAddHPLeftTimes.length; i++){
				buffer.putInt(bossAddHPLeftTimes[i]);
			}
			buffer.putInt(bossAddHPTotalTimes.length);
			for(int i = 0 ; i < bossAddHPTotalTimes.length; i++){
				buffer.putInt(bossAddHPTotalTimes[i]);
			}
			buffer.putInt(leftTime);
			buffer.put((byte)(canDelayTime==false?0:1));
			buffer.putInt(delayLeftTimes);
			buffer.putInt(delayTotalTimes);
			buffer.put((byte)(canPullPlayer==false?0:1));
			buffer.putInt(pullPlayerLeftTimes);
			buffer.putInt(pullPlayerTotalTimes);
			buffer.putInt(bornPointName.length);
			for(int i = 0 ; i < bornPointName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = bornPointName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(bornPointBelong.length);
			for(int i = 0 ; i < bornPointBelong.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = bornPointBelong[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(orderIcon.length);
			for(int i = 0 ; i < orderIcon.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = orderIcon[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(orderName.length);
			for(int i = 0 ; i < orderName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = orderName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(pubOrderName.length);
			for(int i = 0 ; i < pubOrderName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = pubOrderName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(pubOrderMessage.length);
			for(int i = 0 ; i < pubOrderMessage.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = pubOrderMessage[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putLong(guozhanResource);
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
	public String getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getBossName(){
		return bossName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBossName(String[] bossName){
		this.bossName = bossName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getBossIcon(){
		return bossIcon;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBossIcon(String[] bossIcon){
		this.bossIcon = bossIcon;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getBossHP(){
		return bossHP;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBossHP(int[] bossHP){
		this.bossHP = bossHP;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getBossStatus(){
		return bossStatus;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBossStatus(int[] bossStatus){
		this.bossStatus = bossStatus;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean[] getBossCanAddHP(){
		return bossCanAddHP;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBossCanAddHP(boolean[] bossCanAddHP){
		this.bossCanAddHP = bossCanAddHP;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getBossAddHPLeftTimes(){
		return bossAddHPLeftTimes;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBossAddHPLeftTimes(int[] bossAddHPLeftTimes){
		this.bossAddHPLeftTimes = bossAddHPLeftTimes;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getBossAddHPTotalTimes(){
		return bossAddHPTotalTimes;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBossAddHPTotalTimes(int[] bossAddHPTotalTimes){
		this.bossAddHPTotalTimes = bossAddHPTotalTimes;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getLeftTime(){
		return leftTime;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setLeftTime(int leftTime){
		this.leftTime = leftTime;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean getCanDelayTime(){
		return canDelayTime;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setCanDelayTime(boolean canDelayTime){
		this.canDelayTime = canDelayTime;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getDelayLeftTimes(){
		return delayLeftTimes;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDelayLeftTimes(int delayLeftTimes){
		this.delayLeftTimes = delayLeftTimes;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getDelayTotalTimes(){
		return delayTotalTimes;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDelayTotalTimes(int delayTotalTimes){
		this.delayTotalTimes = delayTotalTimes;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean getCanPullPlayer(){
		return canPullPlayer;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setCanPullPlayer(boolean canPullPlayer){
		this.canPullPlayer = canPullPlayer;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getPullPlayerLeftTimes(){
		return pullPlayerLeftTimes;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPullPlayerLeftTimes(int pullPlayerLeftTimes){
		this.pullPlayerLeftTimes = pullPlayerLeftTimes;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getPullPlayerTotalTimes(){
		return pullPlayerTotalTimes;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPullPlayerTotalTimes(int pullPlayerTotalTimes){
		this.pullPlayerTotalTimes = pullPlayerTotalTimes;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getBornPointName(){
		return bornPointName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBornPointName(String[] bornPointName){
		this.bornPointName = bornPointName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getBornPointBelong(){
		return bornPointBelong;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBornPointBelong(String[] bornPointBelong){
		this.bornPointBelong = bornPointBelong;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getOrderIcon(){
		return orderIcon;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setOrderIcon(String[] orderIcon){
		this.orderIcon = orderIcon;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getOrderName(){
		return orderName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setOrderName(String[] orderName){
		this.orderName = orderName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getPubOrderName(){
		return pubOrderName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPubOrderName(String[] pubOrderName){
		this.pubOrderName = pubOrderName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getPubOrderMessage(){
		return pubOrderMessage;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPubOrderMessage(String[] pubOrderMessage){
		this.pubOrderMessage = pubOrderMessage;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public long getGuozhanResource(){
		return guozhanResource;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setGuozhanResource(long guozhanResource){
		this.guozhanResource = guozhanResource;
	}

}