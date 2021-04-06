package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.xianling.TempPrize;
import com.fy.engineserver.activity.xianling.XianLingBillBoardData;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开跨服排行界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankDes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rankDes[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankDes[0]</td><td>String</td><td>rankDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rankDes[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankDes[1]</td><td>String</td><td>rankDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rankDes[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankDes[2]</td><td>String</td><td>rankDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize.length</td><td>int</td><td>4个字节</td><td>TempPrize数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[0].tempIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[0].tempIds</td><td>long[]</td><td>tempPrize[0].tempIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[0].articleNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[0].articleNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[0].articleNames[0]</td><td>String</td><td>tempPrize[0].articleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[0].articleNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[0].articleNames[1]</td><td>String</td><td>tempPrize[0].articleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[0].articleNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[0].articleNames[2]</td><td>String</td><td>tempPrize[0].articleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[0].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[0].nums</td><td>int[]</td><td>tempPrize[0].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[0].bind.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[0].bind</td><td>boolean[]</td><td>tempPrize[0].bind.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[1].tempIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[1].tempIds</td><td>long[]</td><td>tempPrize[1].tempIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[1].articleNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[1].articleNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[1].articleNames[0]</td><td>String</td><td>tempPrize[1].articleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[1].articleNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[1].articleNames[1]</td><td>String</td><td>tempPrize[1].articleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[1].articleNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[1].articleNames[2]</td><td>String</td><td>tempPrize[1].articleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[1].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[1].nums</td><td>int[]</td><td>tempPrize[1].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[1].bind.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[1].bind</td><td>boolean[]</td><td>tempPrize[1].bind.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[2].tempIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[2].tempIds</td><td>long[]</td><td>tempPrize[2].tempIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[2].articleNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[2].articleNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[2].articleNames[0]</td><td>String</td><td>tempPrize[2].articleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[2].articleNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[2].articleNames[1]</td><td>String</td><td>tempPrize[2].articleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[2].articleNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[2].articleNames[2]</td><td>String</td><td>tempPrize[2].articleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[2].nums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[2].nums</td><td>int[]</td><td>tempPrize[2].nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempPrize[2].bind.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempPrize[2].bind</td><td>boolean[]</td><td>tempPrize[2].bind.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData.length</td><td>int</td><td>4个字节</td><td>XianLingBillBoardData数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[0].playerName</td><td>String</td><td>boardData[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[0].serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[0].serverName</td><td>String</td><td>boardData[0].serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[0].crossServerRank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[0].score</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[1].playerName</td><td>String</td><td>boardData[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[1].serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[1].serverName</td><td>String</td><td>boardData[1].serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[1].crossServerRank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[1].score</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[2].playerName</td><td>String</td><td>boardData[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[2].serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[2].serverName</td><td>String</td><td>boardData[2].serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>boardData[2].crossServerRank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>boardData[2].score</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rank</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfBoardData.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfBoardData.playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfBoardData.playerName</td><td>String</td><td>selfBoardData.playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfBoardData.serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfBoardData.serverName</td><td>String</td><td>selfBoardData.serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfBoardData.crossServerRank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfBoardData.score</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class XL_CROSSBILLBOARD_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] rankDes;
	TempPrize[] tempPrize;
	XianLingBillBoardData[] boardData;
	int rank;
	XianLingBillBoardData selfBoardData;
	long leftTime;

	public XL_CROSSBILLBOARD_RES(){
	}

	public XL_CROSSBILLBOARD_RES(long seqNum,String[] rankDes,TempPrize[] tempPrize,XianLingBillBoardData[] boardData,int rank,XianLingBillBoardData selfBoardData,long leftTime){
		this.seqNum = seqNum;
		this.rankDes = rankDes;
		this.tempPrize = tempPrize;
		this.boardData = boardData;
		this.rank = rank;
		this.selfBoardData = selfBoardData;
		this.leftTime = leftTime;
	}

	public XL_CROSSBILLBOARD_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rankDes = new String[len];
		for(int i = 0 ; i < rankDes.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			rankDes[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		tempPrize = new TempPrize[len];
		for(int i = 0 ; i < tempPrize.length ; i++){
			tempPrize[i] = new TempPrize();
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] tempIds_0001 = new long[len];
			for(int j = 0 ; j < tempIds_0001.length ; j++){
				tempIds_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			tempPrize[i].setTempIds(tempIds_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] articleNames_0002 = new String[len];
			for(int j = 0 ; j < articleNames_0002.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				articleNames_0002[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			tempPrize[i].setArticleNames(articleNames_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] nums_0003 = new int[len];
			for(int j = 0 ; j < nums_0003.length ; j++){
				nums_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			tempPrize[i].setNums(nums_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			boolean[] bind_0004 = new boolean[len];
			for(int j = 0 ; j < bind_0004.length ; j++){
				bind_0004[j] = mf.byteArrayToNumber(content,offset,1) != 0;
				offset += 1;
			}
			tempPrize[i].setBind(bind_0004);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		boardData = new XianLingBillBoardData[len];
		for(int i = 0 ; i < boardData.length ; i++){
			boardData[i] = new XianLingBillBoardData();
			boardData[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			boardData[i].setPlayerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			boardData[i].setServerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			boardData[i].setCrossServerRank((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			boardData[i].setScore((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		rank = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		selfBoardData = new XianLingBillBoardData();
		selfBoardData.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		selfBoardData.setPlayerName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		selfBoardData.setServerName(new String(content,offset,len,"UTF-8"));
		offset += len;
		selfBoardData.setCrossServerRank((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		selfBoardData.setScore((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		leftTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x80FFF080;
	}

	public String getTypeDescription() {
		return "XL_CROSSBILLBOARD_RES";
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
		for(int i = 0 ; i < rankDes.length; i++){
			len += 2;
			try{
				len += rankDes[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < tempPrize.length ; i++){
			len += 4;
			len += tempPrize[i].getTempIds().length * 8;
			len += 4;
			String[] articleNames = tempPrize[i].getArticleNames();
			for(int j = 0 ; j < articleNames.length; j++){
				len += 2;
				try{
					len += articleNames[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += tempPrize[i].getNums().length * 4;
			len += 4;
			len += tempPrize[i].getBind().length * 1;
		}
		len += 4;
		for(int i = 0 ; i < boardData.length ; i++){
			len += 8;
			len += 2;
			if(boardData[i].getPlayerName() != null){
				try{
				len += boardData[i].getPlayerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(boardData[i].getServerName() != null){
				try{
				len += boardData[i].getServerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
		}
		len += 4;
		len += 8;
		len += 2;
		if(selfBoardData.getPlayerName() != null){
			try{
			len += selfBoardData.getPlayerName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(selfBoardData.getServerName() != null){
			try{
			len += selfBoardData.getServerName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
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

			buffer.putInt(rankDes.length);
			for(int i = 0 ; i < rankDes.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = rankDes[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(tempPrize.length);
			for(int i = 0 ; i < tempPrize.length ; i++){
				buffer.putInt(tempPrize[i].getTempIds().length);
				long[] tempIds_0005 = tempPrize[i].getTempIds();
				for(int j = 0 ; j < tempIds_0005.length ; j++){
					buffer.putLong(tempIds_0005[j]);
				}
				buffer.putInt(tempPrize[i].getArticleNames().length);
				String[] articleNames_0006 = tempPrize[i].getArticleNames();
				for(int j = 0 ; j < articleNames_0006.length ; j++){
				try{
					buffer.putShort((short)articleNames_0006[j].getBytes("UTF-8").length);
					buffer.put(articleNames_0006[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(tempPrize[i].getNums().length);
				int[] nums_0007 = tempPrize[i].getNums();
				for(int j = 0 ; j < nums_0007.length ; j++){
					buffer.putInt(nums_0007[j]);
				}
				buffer.putInt(tempPrize[i].getBind().length);
				boolean[] bind_0008 = tempPrize[i].getBind();
				for(int j = 0 ; j < bind_0008.length ; j++){
					buffer.put((byte)(bind_0008[j]?1:0));
				}
			}
			buffer.putInt(boardData.length);
			for(int i = 0 ; i < boardData.length ; i++){
				buffer.putLong(boardData[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = boardData[i].getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = boardData[i].getServerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)boardData[i].getCrossServerRank());
				buffer.putInt((int)boardData[i].getScore());
			}
			buffer.putInt(rank);
			buffer.putLong(selfBoardData.getId());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = selfBoardData.getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = selfBoardData.getServerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)selfBoardData.getCrossServerRank());
			buffer.putInt((int)selfBoardData.getScore());
			buffer.putLong(leftTime);
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
	 *	奖励名次描述
	 */
	public String[] getRankDes(){
		return rankDes;
	}

	/**
	 * 设置属性：
	 *	奖励名次描述
	 */
	public void setRankDes(String[] rankDes){
		this.rankDes = rankDes;
	}

	/**
	 * 获取属性：
	 *	奖励道具
	 */
	public TempPrize[] getTempPrize(){
		return tempPrize;
	}

	/**
	 * 设置属性：
	 *	奖励道具
	 */
	public void setTempPrize(TempPrize[] tempPrize){
		this.tempPrize = tempPrize;
	}

	/**
	 * 获取属性：
	 *	跨服排行榜数据
	 */
	public XianLingBillBoardData[] getBoardData(){
		return boardData;
	}

	/**
	 * 设置属性：
	 *	跨服排行榜数据
	 */
	public void setBoardData(XianLingBillBoardData[] boardData){
		this.boardData = boardData;
	}

	/**
	 * 获取属性：
	 *	玩家跨服名次
	 */
	public int getRank(){
		return rank;
	}

	/**
	 * 设置属性：
	 *	玩家跨服名次
	 */
	public void setRank(int rank){
		this.rank = rank;
	}

	/**
	 * 获取属性：
	 *	玩家自己的排行榜数据
	 */
	public XianLingBillBoardData getSelfBoardData(){
		return selfBoardData;
	}

	/**
	 * 设置属性：
	 *	玩家自己的排行榜数据
	 */
	public void setSelfBoardData(XianLingBillBoardData selfBoardData){
		this.selfBoardData = selfBoardData;
	}

	/**
	 * 获取属性：
	 *	倒计时
	 */
	public long getLeftTime(){
		return leftTime;
	}

	/**
	 * 设置属性：
	 *	倒计时
	 */
	public void setLeftTime(long leftTime){
		this.leftTime = leftTime;
	}

}