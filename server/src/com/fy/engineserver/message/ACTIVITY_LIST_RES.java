package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.ActivityIntroduce;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 活动列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces.length</td><td>int</td><td>4个字节</td><td>ActivityIntroduce数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].icon</td><td>String</td><td>activityIntroduces[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].showType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].openTimeDistance.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].openTimeDistance</td><td>String</td><td>activityIntroduces[0].openTimeDistance.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].name</td><td>String</td><td>activityIntroduces[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].startGame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].startGame</td><td>String</td><td>activityIntroduces[0].startGame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].startGameCnName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].startGameCnName</td><td>String</td><td>activityIntroduces[0].startGameCnName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].startNpc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].startNpc</td><td>String</td><td>activityIntroduces[0].startNpc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].startX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].startY</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].des</td><td>String</td><td>activityIntroduces[0].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].activityAdd</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].lables.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].lables[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].lables[0]</td><td>String</td><td>activityIntroduces[0].lables[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].lables[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].lables[1]</td><td>String</td><td>activityIntroduces[0].lables[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].lables[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].lables[2]</td><td>String</td><td>activityIntroduces[0].lables[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].star</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].activityType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[0].activityState</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[0].doType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].icon</td><td>String</td><td>activityIntroduces[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].showType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].openTimeDistance.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].openTimeDistance</td><td>String</td><td>activityIntroduces[1].openTimeDistance.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].name</td><td>String</td><td>activityIntroduces[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].startGame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].startGame</td><td>String</td><td>activityIntroduces[1].startGame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].startGameCnName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].startGameCnName</td><td>String</td><td>activityIntroduces[1].startGameCnName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].startNpc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].startNpc</td><td>String</td><td>activityIntroduces[1].startNpc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].startX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].startY</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].des</td><td>String</td><td>activityIntroduces[1].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].activityAdd</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].lables.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].lables[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].lables[0]</td><td>String</td><td>activityIntroduces[1].lables[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].lables[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].lables[1]</td><td>String</td><td>activityIntroduces[1].lables[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].lables[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].lables[2]</td><td>String</td><td>activityIntroduces[1].lables[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].star</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].activityType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[1].activityState</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[1].doType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].icon</td><td>String</td><td>activityIntroduces[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].showType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].openTimeDistance.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].openTimeDistance</td><td>String</td><td>activityIntroduces[2].openTimeDistance.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].name</td><td>String</td><td>activityIntroduces[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].startGame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].startGame</td><td>String</td><td>activityIntroduces[2].startGame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].startGameCnName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].startGameCnName</td><td>String</td><td>activityIntroduces[2].startGameCnName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].startNpc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].startNpc</td><td>String</td><td>activityIntroduces[2].startNpc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].startX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].startY</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].des</td><td>String</td><td>activityIntroduces[2].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].activityAdd</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].lables.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].lables[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].lables[0]</td><td>String</td><td>activityIntroduces[2].lables[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].lables[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].lables[1]</td><td>String</td><td>activityIntroduces[2].lables[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].lables[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].lables[2]</td><td>String</td><td>activityIntroduces[2].lables[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].star</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].activityType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIntroduces[2].activityState</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIntroduces[2].doType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showColor.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showColor</td><td>int[]</td><td>showColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>doneDes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>doneDes[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>doneDes[0]</td><td>String</td><td>doneDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>doneDes[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>doneDes[1]</td><td>String</td><td>doneDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>doneDes[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>doneDes[2]</td><td>String</td><td>doneDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class ACTIVITY_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ActivityIntroduce[] activityIntroduces;
	int[] showColor;
	String[] doneDes;

	public ACTIVITY_LIST_RES(){
	}

	public ACTIVITY_LIST_RES(long seqNum,ActivityIntroduce[] activityIntroduces,int[] showColor,String[] doneDes){
		this.seqNum = seqNum;
		this.activityIntroduces = activityIntroduces;
		this.showColor = showColor;
		this.doneDes = doneDes;
	}

	public ACTIVITY_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		activityIntroduces = new ActivityIntroduce[len];
		for(int i = 0 ; i < activityIntroduces.length ; i++){
			activityIntroduces[i] = new ActivityIntroduce();
			activityIntroduces[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityIntroduces[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			activityIntroduces[i].setShowType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityIntroduces[i].setOpenTimeDistance(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityIntroduces[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityIntroduces[i].setStartGame(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityIntroduces[i].setStartGameCnName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityIntroduces[i].setStartNpc(new String(content,offset,len,"UTF-8"));
			offset += len;
			activityIntroduces[i].setStartX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityIntroduces[i].setStartY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityIntroduces[i].setDes(new String(content,offset,len,"UTF-8"));
			offset += len;
			activityIntroduces[i].setActivityAdd((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] lables_0001 = new String[len];
			for(int j = 0 ; j < lables_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				lables_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			activityIntroduces[i].setLables(lables_0001);
			activityIntroduces[i].setLevelLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityIntroduces[i].setStar((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityIntroduces[i].setActivityType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityIntroduces[i].setActivityState((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityIntroduces[i].setDoType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		showColor = new int[len];
		for(int i = 0 ; i < showColor.length ; i++){
			showColor[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		doneDes = new String[len];
		for(int i = 0 ; i < doneDes.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			doneDes[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x70EEE001;
	}

	public String getTypeDescription() {
		return "ACTIVITY_LIST_RES";
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
		for(int i = 0 ; i < activityIntroduces.length ; i++){
			len += 4;
			len += 2;
			if(activityIntroduces[i].getIcon() != null){
				try{
				len += activityIntroduces[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 2;
			if(activityIntroduces[i].getOpenTimeDistance() != null){
				try{
				len += activityIntroduces[i].getOpenTimeDistance().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(activityIntroduces[i].getName() != null){
				try{
				len += activityIntroduces[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(activityIntroduces[i].getStartGame() != null){
				try{
				len += activityIntroduces[i].getStartGame().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(activityIntroduces[i].getStartGameCnName() != null){
				try{
				len += activityIntroduces[i].getStartGameCnName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(activityIntroduces[i].getStartNpc() != null){
				try{
				len += activityIntroduces[i].getStartNpc().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 2;
			if(activityIntroduces[i].getDes() != null){
				try{
				len += activityIntroduces[i].getDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			String[] lables = activityIntroduces[i].getLables();
			for(int j = 0 ; j < lables.length; j++){
				len += 2;
				try{
					len += lables[j].getBytes("UTF-8").length;
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
		}
		len += 4;
		len += showColor.length * 4;
		len += 4;
		for(int i = 0 ; i < doneDes.length; i++){
			len += 2;
			try{
				len += doneDes[i].getBytes("UTF-8").length;
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

			buffer.putInt(activityIntroduces.length);
			for(int i = 0 ; i < activityIntroduces.length ; i++){
				buffer.putInt((int)activityIntroduces[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = activityIntroduces[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)activityIntroduces[i].getShowType());
				try{
				tmpBytes2 = activityIntroduces[i].getOpenTimeDistance().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = activityIntroduces[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = activityIntroduces[i].getStartGame().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = activityIntroduces[i].getStartGameCnName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = activityIntroduces[i].getStartNpc().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)activityIntroduces[i].getStartX());
				buffer.putInt((int)activityIntroduces[i].getStartY());
				try{
				tmpBytes2 = activityIntroduces[i].getDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)activityIntroduces[i].getActivityAdd());
				buffer.putInt(activityIntroduces[i].getLables().length);
				String[] lables_0002 = activityIntroduces[i].getLables();
				for(int j = 0 ; j < lables_0002.length ; j++){
				try{
					buffer.putShort((short)lables_0002[j].getBytes("UTF-8").length);
					buffer.put(lables_0002[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt((int)activityIntroduces[i].getLevelLimit());
				buffer.putInt((int)activityIntroduces[i].getStar());
				buffer.putInt((int)activityIntroduces[i].getActivityType());
				buffer.putInt((int)activityIntroduces[i].getActivityState());
				buffer.putInt((int)activityIntroduces[i].getDoType());
			}
			buffer.putInt(showColor.length);
			for(int i = 0 ; i < showColor.length; i++){
				buffer.putInt(showColor[i]);
			}
			buffer.putInt(doneDes.length);
			for(int i = 0 ; i < doneDes.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = doneDes[i].getBytes("UTF-8");
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
	 *	活动介绍
	 */
	public ActivityIntroduce[] getActivityIntroduces(){
		return activityIntroduces;
	}

	/**
	 * 设置属性：
	 *	活动介绍
	 */
	public void setActivityIntroduces(ActivityIntroduce[] activityIntroduces){
		this.activityIntroduces = activityIntroduces;
	}

	/**
	 * 获取属性：
	 *	显示的颜色
	 */
	public int[] getShowColor(){
		return showColor;
	}

	/**
	 * 设置属性：
	 *	显示的颜色
	 */
	public void setShowColor(int[] showColor){
		this.showColor = showColor;
	}

	/**
	 * 获取属性：
	 *	完成度显示
	 */
	public String[] getDoneDes(){
		return doneDes;
	}

	/**
	 * 设置属性：
	 *	完成度显示
	 */
	public void setDoneDes(String[] doneDes){
		this.doneDes = doneDes;
	}

}