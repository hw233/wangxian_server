package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.activeness.ActivityForActiveness;
import com.fy.engineserver.activity.activeness.PlayerActivenessInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 活跃度活动列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness.length</td><td>int</td><td>4个字节</td><td>ActivityForActiveness数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[0].name</td><td>String</td><td>activityForActiveness[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[0].startGame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[0].startGame</td><td>String</td><td>activityForActiveness[0].startGame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[0].startNpc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[0].startNpc</td><td>String</td><td>activityForActiveness[0].startNpc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[0].startX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[0].startY</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[0].shortdes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[0].shortdes</td><td>String</td><td>activityForActiveness[0].shortdes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[0].activityAdd</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[0].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[0].exp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[0].article</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[0].equipment</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[1].name</td><td>String</td><td>activityForActiveness[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[1].startGame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[1].startGame</td><td>String</td><td>activityForActiveness[1].startGame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[1].startNpc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[1].startNpc</td><td>String</td><td>activityForActiveness[1].startNpc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[1].startX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[1].startY</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[1].shortdes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[1].shortdes</td><td>String</td><td>activityForActiveness[1].shortdes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[1].activityAdd</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[1].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[1].exp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[1].article</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[1].equipment</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[2].name</td><td>String</td><td>activityForActiveness[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[2].startGame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[2].startGame</td><td>String</td><td>activityForActiveness[2].startGame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[2].startNpc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[2].startNpc</td><td>String</td><td>activityForActiveness[2].startNpc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[2].startX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[2].startY</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[2].shortdes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[2].shortdes</td><td>String</td><td>activityForActiveness[2].shortdes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[2].activityAdd</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[2].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[2].exp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityForActiveness[2].article</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityForActiveness[2].equipment</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
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
 * <tr bgcolor="#FAFAFA" align="center"><td>goJoin.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goJoin[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goJoin[0]</td><td>String</td><td>goJoin[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goJoin[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goJoin[1]</td><td>String</td><td>goJoin[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goJoin[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goJoin[2]</td><td>String</td><td>goJoin[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerActivenessInfo.dayActiveness</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerActivenessInfo.totalActiveness</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerActivenessInfo.gotten.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerActivenessInfo.gotten</td><td>boolean[]</td><td>playerActivenessInfo.gotten.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerActivenessInfo.hasLottery</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerActivenessInfo.canLottery</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerActivenessInfo.hasSign.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerActivenessInfo.hasSign</td><td>boolean[]</td><td>playerActivenessInfo.hasSign.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerActivenessInfo.hasGotSign.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerActivenessInfo.hasGotSign</td><td>boolean[]</td><td>playerActivenessInfo.hasGotSign.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>awardID.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>awardID</td><td>long[]</td><td>awardID.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needActiveness.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needActiveness</td><td>int[]</td><td>needActiveness.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>month</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>days</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>signdays</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>signAwardLevel.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>signAwardLevel</td><td>int[]</td><td>signAwardLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>signAwardID.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>signAwardID</td><td>long[]</td><td>signAwardID.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ranklist.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ranklist</td><td>String</td><td>ranklist.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class ACTIVENESS_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ActivityForActiveness[] activityForActiveness;
	int[] showColor;
	String[] doneDes;
	String[] goJoin;
	PlayerActivenessInfo playerActivenessInfo;
	long[] awardID;
	int[] needActiveness;
	int month;
	int days;
	int signdays;
	int[] signAwardLevel;
	long[] signAwardID;
	String ranklist;

	public ACTIVENESS_LIST_RES(){
	}

	public ACTIVENESS_LIST_RES(long seqNum,ActivityForActiveness[] activityForActiveness,int[] showColor,String[] doneDes,String[] goJoin,PlayerActivenessInfo playerActivenessInfo,long[] awardID,int[] needActiveness,int month,int days,int signdays,int[] signAwardLevel,long[] signAwardID,String ranklist){
		this.seqNum = seqNum;
		this.activityForActiveness = activityForActiveness;
		this.showColor = showColor;
		this.doneDes = doneDes;
		this.goJoin = goJoin;
		this.playerActivenessInfo = playerActivenessInfo;
		this.awardID = awardID;
		this.needActiveness = needActiveness;
		this.month = month;
		this.days = days;
		this.signdays = signdays;
		this.signAwardLevel = signAwardLevel;
		this.signAwardID = signAwardID;
		this.ranklist = ranklist;
	}

	public ACTIVENESS_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		activityForActiveness = new ActivityForActiveness[len];
		for(int i = 0 ; i < activityForActiveness.length ; i++){
			activityForActiveness[i] = new ActivityForActiveness();
			activityForActiveness[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityForActiveness[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityForActiveness[i].setStartGame(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityForActiveness[i].setStartNpc(new String(content,offset,len,"UTF-8"));
			offset += len;
			activityForActiveness[i].setStartX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityForActiveness[i].setStartY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityForActiveness[i].setShortdes(new String(content,offset,len,"UTF-8"));
			offset += len;
			activityForActiveness[i].setActivityAdd((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityForActiveness[i].setLevelLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityForActiveness[i].setExp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityForActiveness[i].setArticle((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			activityForActiveness[i].setEquipment((int)mf.byteArrayToNumber(content,offset,4));
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
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		goJoin = new String[len];
		for(int i = 0 ; i < goJoin.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			goJoin[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		playerActivenessInfo = new PlayerActivenessInfo();
		playerActivenessInfo.setDayActiveness((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		playerActivenessInfo.setTotalActiveness((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		boolean[] gotten_0001 = new boolean[len];
		for(int j = 0 ; j < gotten_0001.length ; j++){
			gotten_0001[j] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		playerActivenessInfo.setGotten(gotten_0001);
		playerActivenessInfo.setHasLottery((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		playerActivenessInfo.setCanLottery((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		boolean[] hasSign_0002 = new boolean[len];
		for(int j = 0 ; j < hasSign_0002.length ; j++){
			hasSign_0002[j] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		playerActivenessInfo.setHasSign(hasSign_0002);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		boolean[] hasGotSign_0003 = new boolean[len];
		for(int j = 0 ; j < hasGotSign_0003.length ; j++){
			hasGotSign_0003[j] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		playerActivenessInfo.setHasGotSign(hasGotSign_0003);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		awardID = new long[len];
		for(int i = 0 ; i < awardID.length ; i++){
			awardID[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		needActiveness = new int[len];
		for(int i = 0 ; i < needActiveness.length ; i++){
			needActiveness[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		month = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		days = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		signdays = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		signAwardLevel = new int[len];
		for(int i = 0 ; i < signAwardLevel.length ; i++){
			signAwardLevel[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		signAwardID = new long[len];
		for(int i = 0 ; i < signAwardID.length ; i++){
			signAwardID[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		ranklist = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8E0EAA07;
	}

	public String getTypeDescription() {
		return "ACTIVENESS_LIST_RES";
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
		for(int i = 0 ; i < activityForActiveness.length ; i++){
			len += 4;
			len += 2;
			if(activityForActiveness[i].getName() != null){
				try{
				len += activityForActiveness[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(activityForActiveness[i].getStartGame() != null){
				try{
				len += activityForActiveness[i].getStartGame().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(activityForActiveness[i].getStartNpc() != null){
				try{
				len += activityForActiveness[i].getStartNpc().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 2;
			if(activityForActiveness[i].getShortdes() != null){
				try{
				len += activityForActiveness[i].getShortdes().getBytes("UTF-8").length;
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
		len += 4;
		for(int i = 0 ; i < goJoin.length; i++){
			len += 2;
			try{
				len += goJoin[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 4;
		len += playerActivenessInfo.getGotten().length * 1;
		len += 4;
		len += 4;
		len += 4;
		len += playerActivenessInfo.getHasSign().length * 1;
		len += 4;
		len += playerActivenessInfo.getHasGotSign().length * 1;
		len += 4;
		len += awardID.length * 8;
		len += 4;
		len += needActiveness.length * 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += signAwardLevel.length * 4;
		len += 4;
		len += signAwardID.length * 8;
		len += 2;
		try{
			len +=ranklist.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.putInt(activityForActiveness.length);
			for(int i = 0 ; i < activityForActiveness.length ; i++){
				buffer.putInt((int)activityForActiveness[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = activityForActiveness[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = activityForActiveness[i].getStartGame().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = activityForActiveness[i].getStartNpc().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)activityForActiveness[i].getStartX());
				buffer.putInt((int)activityForActiveness[i].getStartY());
				try{
				tmpBytes2 = activityForActiveness[i].getShortdes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)activityForActiveness[i].getActivityAdd());
				buffer.putInt((int)activityForActiveness[i].getLevelLimit());
				buffer.putInt((int)activityForActiveness[i].getExp());
				buffer.putInt((int)activityForActiveness[i].getArticle());
				buffer.putInt((int)activityForActiveness[i].getEquipment());
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
			buffer.putInt(goJoin.length);
			for(int i = 0 ; i < goJoin.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = goJoin[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt((int)playerActivenessInfo.getDayActiveness());
			buffer.putInt((int)playerActivenessInfo.getTotalActiveness());
			buffer.putInt(playerActivenessInfo.getGotten().length);
			boolean[] gotten_0004 = playerActivenessInfo.getGotten();
			for(int j = 0 ; j < gotten_0004.length ; j++){
				buffer.put((byte)(gotten_0004[j]?1:0));
			}
			buffer.putInt((int)playerActivenessInfo.getHasLottery());
			buffer.putInt((int)playerActivenessInfo.getCanLottery());
			buffer.putInt(playerActivenessInfo.getHasSign().length);
			boolean[] hasSign_0005 = playerActivenessInfo.getHasSign();
			for(int j = 0 ; j < hasSign_0005.length ; j++){
				buffer.put((byte)(hasSign_0005[j]?1:0));
			}
			buffer.putInt(playerActivenessInfo.getHasGotSign().length);
			boolean[] hasGotSign_0006 = playerActivenessInfo.getHasGotSign();
			for(int j = 0 ; j < hasGotSign_0006.length ; j++){
				buffer.put((byte)(hasGotSign_0006[j]?1:0));
			}
			buffer.putInt(awardID.length);
			for(int i = 0 ; i < awardID.length; i++){
				buffer.putLong(awardID[i]);
			}
			buffer.putInt(needActiveness.length);
			for(int i = 0 ; i < needActiveness.length; i++){
				buffer.putInt(needActiveness[i]);
			}
			buffer.putInt(month);
			buffer.putInt(days);
			buffer.putInt(signdays);
			buffer.putInt(signAwardLevel.length);
			for(int i = 0 ; i < signAwardLevel.length; i++){
				buffer.putInt(signAwardLevel[i]);
			}
			buffer.putInt(signAwardID.length);
			for(int i = 0 ; i < signAwardID.length; i++){
				buffer.putLong(signAwardID[i]);
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = ranklist.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	活跃度活动信息
	 */
	public ActivityForActiveness[] getActivityForActiveness(){
		return activityForActiveness;
	}

	/**
	 * 设置属性：
	 *	活跃度活动信息
	 */
	public void setActivityForActiveness(ActivityForActiveness[] activityForActiveness){
		this.activityForActiveness = activityForActiveness;
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

	/**
	 * 获取属性：
	 *	前往参与
	 */
	public String[] getGoJoin(){
		return goJoin;
	}

	/**
	 * 设置属性：
	 *	前往参与
	 */
	public void setGoJoin(String[] goJoin){
		this.goJoin = goJoin;
	}

	/**
	 * 获取属性：
	 *	活跃度活动信息
	 */
	public PlayerActivenessInfo getPlayerActivenessInfo(){
		return playerActivenessInfo;
	}

	/**
	 * 设置属性：
	 *	活跃度活动信息
	 */
	public void setPlayerActivenessInfo(PlayerActivenessInfo playerActivenessInfo){
		this.playerActivenessInfo = playerActivenessInfo;
	}

	/**
	 * 获取属性：
	 *	奖励物品id
	 */
	public long[] getAwardID(){
		return awardID;
	}

	/**
	 * 设置属性：
	 *	奖励物品id
	 */
	public void setAwardID(long[] awardID){
		this.awardID = awardID;
	}

	/**
	 * 获取属性：
	 *	满足的活跃度值
	 */
	public int[] getNeedActiveness(){
		return needActiveness;
	}

	/**
	 * 设置属性：
	 *	满足的活跃度值
	 */
	public void setNeedActiveness(int[] needActiveness){
		this.needActiveness = needActiveness;
	}

	/**
	 * 获取属性：
	 *	当前月份
	 */
	public int getMonth(){
		return month;
	}

	/**
	 * 设置属性：
	 *	当前月份
	 */
	public void setMonth(int month){
		this.month = month;
	}

	/**
	 * 获取属性：
	 *	当前日期
	 */
	public int getDays(){
		return days;
	}

	/**
	 * 设置属性：
	 *	当前日期
	 */
	public void setDays(int days){
		this.days = days;
	}

	/**
	 * 获取属性：
	 *	已签到天数
	 */
	public int getSigndays(){
		return signdays;
	}

	/**
	 * 设置属性：
	 *	已签到天数
	 */
	public void setSigndays(int signdays){
		this.signdays = signdays;
	}

	/**
	 * 获取属性：
	 *	签到奖励档
	 */
	public int[] getSignAwardLevel(){
		return signAwardLevel;
	}

	/**
	 * 设置属性：
	 *	签到奖励档
	 */
	public void setSignAwardLevel(int[] signAwardLevel){
		this.signAwardLevel = signAwardLevel;
	}

	/**
	 * 获取属性：
	 *	签到奖励物品id
	 */
	public long[] getSignAwardID(){
		return signAwardID;
	}

	/**
	 * 设置属性：
	 *	签到奖励物品id
	 */
	public void setSignAwardID(long[] signAwardID){
		this.signAwardID = signAwardID;
	}

	/**
	 * 获取属性：
	 *	我的排行是
	 */
	public String getRanklist(){
		return ranklist;
	}

	/**
	 * 设置属性：
	 *	我的排行是
	 */
	public void setRanklist(String ranklist){
		this.ranklist = ranklist;
	}

}