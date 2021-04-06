package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.jiazu.JiazuMember4Client;
import com.fy.engineserver.septbuilding.entity.JiazuBedge;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询家族信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuName</td><td>String</td><td>jiazuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>slogan.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>slogan</td><td>String</td><td>slogan.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>usedBedge</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>constructionConsume</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titleAlias.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titleAlias[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titleAlias[0]</td><td>String</td><td>titleAlias[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titleAlias[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titleAlias[1]</td><td>String</td><td>titleAlias[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titleAlias[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titleAlias[2]</td><td>String</td><td>titleAlias[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers.length</td><td>int</td><td>4个字节</td><td>JiazuMember4Client数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[0].playerName</td><td>String</td><td>jiazuMembers[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[0].title</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[0].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].contributionSalary</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[0].contributeMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].classLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[0].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[0].onLine</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[1].playerName</td><td>String</td><td>jiazuMembers[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[1].title</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[1].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].contributionSalary</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[1].contributeMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].classLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[1].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[1].onLine</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[2].playerName</td><td>String</td><td>jiazuMembers[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[2].title</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[2].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].contributionSalary</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[2].contributeMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].classLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMembers[2].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMembers[2].onLine</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge.length</td><td>int</td><td>4个字节</td><td>JiazuBedge数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[0].name</td><td>String</td><td>allBedge[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[0].resName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[0].resName</td><td>String</td><td>allBedge[0].resName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[0].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[0].des</td><td>String</td><td>allBedge[0].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[0].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[0].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[0].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[0].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[1].name</td><td>String</td><td>allBedge[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[1].resName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[1].resName</td><td>String</td><td>allBedge[1].resName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[1].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[1].des</td><td>String</td><td>allBedge[1].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[1].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[1].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[1].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[1].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[2].name</td><td>String</td><td>allBedge[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[2].resName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[2].resName</td><td>String</td><td>allBedge[2].resName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[2].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[2].des</td><td>String</td><td>allBedge[2].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[2].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[2].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allBedge[2].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allBedge[2].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hadBedge.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hadBedge</td><td>int[]</td><td>hadBedge.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>switchCost</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>septStationId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maintai</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMoney</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>warScore</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prosperity</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class JIAZU_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long jiazuId;
	String jiazuName;
	int level;
	String slogan;
	int usedBedge;
	int constructionConsume;
	String[] titleAlias;
	JiazuMember4Client[] jiazuMembers;
	JiazuBedge[] allBedge;
	int[] hadBedge;
	int switchCost;
	long septStationId;
	long maintai;
	int jiazuMoney;
	long warScore;
	long prosperity;

	public JIAZU_INFO_RES(){
	}

	public JIAZU_INFO_RES(long seqNum,long jiazuId,String jiazuName,int level,String slogan,int usedBedge,int constructionConsume,String[] titleAlias,JiazuMember4Client[] jiazuMembers,JiazuBedge[] allBedge,int[] hadBedge,int switchCost,long septStationId,long maintai,int jiazuMoney,long warScore,long prosperity){
		this.seqNum = seqNum;
		this.jiazuId = jiazuId;
		this.jiazuName = jiazuName;
		this.level = level;
		this.slogan = slogan;
		this.usedBedge = usedBedge;
		this.constructionConsume = constructionConsume;
		this.titleAlias = titleAlias;
		this.jiazuMembers = jiazuMembers;
		this.allBedge = allBedge;
		this.hadBedge = hadBedge;
		this.switchCost = switchCost;
		this.septStationId = septStationId;
		this.maintai = maintai;
		this.jiazuMoney = jiazuMoney;
		this.warScore = warScore;
		this.prosperity = prosperity;
	}

	public JIAZU_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		jiazuId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		jiazuName = new String(content,offset,len,"UTF-8");
		offset += len;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		slogan = new String(content,offset,len,"UTF-8");
		offset += len;
		usedBedge = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		constructionConsume = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		titleAlias = new String[len];
		for(int i = 0 ; i < titleAlias.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			titleAlias[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
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
			jiazuMembers[i].setContributionSalary((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuMembers[i].setContributeMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			jiazuMembers[i].setClassLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuMembers[i].setCareer((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuMembers[i].setOnLine(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		allBedge = new JiazuBedge[len];
		for(int i = 0 ; i < allBedge.length ; i++){
			allBedge[i] = new JiazuBedge();
			allBedge[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			allBedge[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			allBedge[i].setResName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			allBedge[i].setDes(new String(content,offset,len,"UTF-8"));
			offset += len;
			allBedge[i].setColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			allBedge[i].setPrice((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			allBedge[i].setType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			allBedge[i].setLevelLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		hadBedge = new int[len];
		for(int i = 0 ; i < hadBedge.length ; i++){
			hadBedge[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		switchCost = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		septStationId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		maintai = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		jiazuMoney = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		warScore = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		prosperity = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x700AEE21;
	}

	public String getTypeDescription() {
		return "JIAZU_INFO_RES";
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
		len += 8;
		len += 2;
		try{
			len +=jiazuName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 2;
		try{
			len +=slogan.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < titleAlias.length; i++){
			len += 2;
			try{
				len += titleAlias[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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
			len += 4;
			len += 8;
			len += 4;
			len += 4;
			len += 1;
		}
		len += 4;
		for(int i = 0 ; i < allBedge.length ; i++){
			len += 4;
			len += 2;
			if(allBedge[i].getName() != null){
				try{
				len += allBedge[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(allBedge[i].getResName() != null){
				try{
				len += allBedge[i].getResName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(allBedge[i].getDes() != null){
				try{
				len += allBedge[i].getDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += 4;
		}
		len += 4;
		len += hadBedge.length * 4;
		len += 4;
		len += 8;
		len += 8;
		len += 4;
		len += 8;
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

			buffer.putLong(jiazuId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = jiazuName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(level);
				try{
			tmpBytes1 = slogan.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(usedBedge);
			buffer.putInt(constructionConsume);
			buffer.putInt(titleAlias.length);
			for(int i = 0 ; i < titleAlias.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = titleAlias[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
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
				buffer.putInt((int)jiazuMembers[i].getContributionSalary());
				buffer.putLong(jiazuMembers[i].getContributeMoney());
				buffer.putInt((int)jiazuMembers[i].getClassLevel());
				buffer.putInt((int)jiazuMembers[i].getCareer());
				buffer.put((byte)(jiazuMembers[i].isOnLine()==false?0:1));
			}
			buffer.putInt(allBedge.length);
			for(int i = 0 ; i < allBedge.length ; i++){
				buffer.putInt((int)allBedge[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = allBedge[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = allBedge[i].getResName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = allBedge[i].getDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)allBedge[i].getColor());
				buffer.putInt((int)allBedge[i].getPrice());
				buffer.putInt((int)allBedge[i].getType());
				buffer.putInt((int)allBedge[i].getLevelLimit());
			}
			buffer.putInt(hadBedge.length);
			for(int i = 0 ; i < hadBedge.length; i++){
				buffer.putInt(hadBedge[i]);
			}
			buffer.putInt(switchCost);
			buffer.putLong(septStationId);
			buffer.putLong(maintai);
			buffer.putInt(jiazuMoney);
			buffer.putLong(warScore);
			buffer.putLong(prosperity);
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
	 *	家族ID
	 */
	public long getJiazuId(){
		return jiazuId;
	}

	/**
	 * 设置属性：
	 *	家族ID
	 */
	public void setJiazuId(long jiazuId){
		this.jiazuId = jiazuId;
	}

	/**
	 * 获取属性：
	 *	家族名字
	 */
	public String getJiazuName(){
		return jiazuName;
	}

	/**
	 * 设置属性：
	 *	家族名字
	 */
	public void setJiazuName(String jiazuName){
		this.jiazuName = jiazuName;
	}

	/**
	 * 获取属性：
	 *	家族等级
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	家族等级
	 */
	public void setLevel(int level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	家族标语
	 */
	public String getSlogan(){
		return slogan;
	}

	/**
	 * 设置属性：
	 *	家族标语
	 */
	public void setSlogan(String slogan){
		this.slogan = slogan;
	}

	/**
	 * 获取属性：
	 *	当前使用的徽章
	 */
	public int getUsedBedge(){
		return usedBedge;
	}

	/**
	 * 设置属性：
	 *	当前使用的徽章
	 */
	public void setUsedBedge(int usedBedge){
		this.usedBedge = usedBedge;
	}

	/**
	 * 获取属性：
	 *	家族灵脉值 
	 */
	public int getConstructionConsume(){
		return constructionConsume;
	}

	/**
	 * 设置属性：
	 *	家族灵脉值 
	 */
	public void setConstructionConsume(int constructionConsume){
		this.constructionConsume = constructionConsume;
	}

	/**
	 * 获取属性：
	 *	家族修改后的职位
	 */
	public String[] getTitleAlias(){
		return titleAlias;
	}

	/**
	 * 设置属性：
	 *	家族修改后的职位
	 */
	public void setTitleAlias(String[] titleAlias){
		this.titleAlias = titleAlias;
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
	 *	所有的徽章
	 */
	public JiazuBedge[] getAllBedge(){
		return allBedge;
	}

	/**
	 * 设置属性：
	 *	所有的徽章
	 */
	public void setAllBedge(JiazuBedge[] allBedge){
		this.allBedge = allBedge;
	}

	/**
	 * 获取属性：
	 *	已获得的徽章ID
	 */
	public int[] getHadBedge(){
		return hadBedge;
	}

	/**
	 * 设置属性：
	 *	已获得的徽章ID
	 */
	public void setHadBedge(int[] hadBedge){
		this.hadBedge = hadBedge;
	}

	/**
	 * 获取属性：
	 *	切换徽章消耗
	 */
	public int getSwitchCost(){
		return switchCost;
	}

	/**
	 * 设置属性：
	 *	切换徽章消耗
	 */
	public void setSwitchCost(int switchCost){
		this.switchCost = switchCost;
	}

	/**
	 * 获取属性：
	 *	驻地ID
	 */
	public long getSeptStationId(){
		return septStationId;
	}

	/**
	 * 设置属性：
	 *	驻地ID
	 */
	public void setSeptStationId(long septStationId){
		this.septStationId = septStationId;
	}

	/**
	 * 获取属性：
	 *	维护费
	 */
	public long getMaintai(){
		return maintai;
	}

	/**
	 * 设置属性：
	 *	维护费
	 */
	public void setMaintai(long maintai){
		this.maintai = maintai;
	}

	/**
	 * 获取属性：
	 *	家族资金 
	 */
	public int getJiazuMoney(){
		return jiazuMoney;
	}

	/**
	 * 设置属性：
	 *	家族资金 
	 */
	public void setJiazuMoney(int jiazuMoney){
		this.jiazuMoney = jiazuMoney;
	}

	/**
	 * 获取属性：
	 *	家族武力值 
	 */
	public long getWarScore(){
		return warScore;
	}

	/**
	 * 设置属性：
	 *	家族武力值 
	 */
	public void setWarScore(long warScore){
		this.warScore = warScore;
	}

	/**
	 * 获取属性：
	 *	家族繁荣度 
	 */
	public long getProsperity(){
		return prosperity;
	}

	/**
	 * 设置属性：
	 *	家族繁荣度 
	 */
	public void setProsperity(long prosperity){
		this.prosperity = prosperity;
	}

}