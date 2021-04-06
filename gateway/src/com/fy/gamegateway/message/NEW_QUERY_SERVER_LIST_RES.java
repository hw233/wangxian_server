package com.fy.gamegateway.message;

import com.fy.gamegateway.mieshi.server.ServerInfoForClient;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端查询的所有服务器状态<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastLoginCategory.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastLoginCategory</td><td>String</td><td>lastLoginCategory.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[0]</td><td>String</td><td>categories[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[1]</td><td>String</td><td>categories[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[2]</td><td>String</td><td>categories[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>newservernames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>newservernames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>newservernames[0]</td><td>String</td><td>newservernames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>newservernames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>newservernames[1]</td><td>String</td><td>newservernames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>newservernames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>newservernames[2]</td><td>String</td><td>newservernames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>recommendservernames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>recommendservernames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>recommendservernames[0]</td><td>String</td><td>recommendservernames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>recommendservernames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>recommendservernames[1]</td><td>String</td><td>recommendservernames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>recommendservernames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>recommendservernames[2]</td><td>String</td><td>recommendservernames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos.length</td><td>int</td><td>4个字节</td><td>ServerInfoForClient数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].category.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].category</td><td>String</td><td>serverInfos[0].category.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].name</td><td>String</td><td>serverInfos[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].ip.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].ip</td><td>String</td><td>serverInfos[0].ip.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].port</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].clientid</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].serverid</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].status</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].statusName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].statusName</td><td>String</td><td>serverInfos[0].statusName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].description</td><td>String</td><td>serverInfos[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].onlinePlayerNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].hasPlayer</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[0].playerName</td><td>String</td><td>serverInfos[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].category.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].category</td><td>String</td><td>serverInfos[1].category.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].name</td><td>String</td><td>serverInfos[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].ip.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].ip</td><td>String</td><td>serverInfos[1].ip.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].port</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].clientid</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].serverid</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].status</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].statusName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].statusName</td><td>String</td><td>serverInfos[1].statusName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].description</td><td>String</td><td>serverInfos[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].onlinePlayerNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].hasPlayer</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[1].playerName</td><td>String</td><td>serverInfos[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].category.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].category</td><td>String</td><td>serverInfos[2].category.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].name</td><td>String</td><td>serverInfos[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].ip.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].ip</td><td>String</td><td>serverInfos[2].ip.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].port</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].clientid</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].serverid</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].status</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].statusName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].statusName</td><td>String</td><td>serverInfos[2].statusName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].description</td><td>String</td><td>serverInfos[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].onlinePlayerNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].hasPlayer</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>serverInfos[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>serverInfos[2].playerName</td><td>String</td><td>serverInfos[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NEW_QUERY_SERVER_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String lastLoginCategory;
	String[] categories;
	String[] newservernames;
	String[] recommendservernames;
	ServerInfoForClient[] serverInfos;

	public NEW_QUERY_SERVER_LIST_RES(){
	}

	public NEW_QUERY_SERVER_LIST_RES(long seqNum,String lastLoginCategory,String[] categories,String[] newservernames,String[] recommendservernames,ServerInfoForClient[] serverInfos){
		this.seqNum = seqNum;
		this.lastLoginCategory = lastLoginCategory;
		this.categories = categories;
		this.newservernames = newservernames;
		this.recommendservernames = recommendservernames;
		this.serverInfos = serverInfos;
	}

	public NEW_QUERY_SERVER_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		lastLoginCategory = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		categories = new String[len];
		for(int i = 0 ; i < categories.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			categories[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		newservernames = new String[len];
		for(int i = 0 ; i < newservernames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			newservernames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		recommendservernames = new String[len];
		for(int i = 0 ; i < recommendservernames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			recommendservernames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		serverInfos = new ServerInfoForClient[len];
		for(int i = 0 ; i < serverInfos.length ; i++){
			serverInfos[i] = new ServerInfoForClient();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			serverInfos[i].setCategory(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			serverInfos[i].setName(new String(content,offset,len,"utf-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			serverInfos[i].setIp(new String(content,offset,len,"utf-8"));
			offset += len;
			serverInfos[i].setPort((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			serverInfos[i].setClientid((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			serverInfos[i].setServerid((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			serverInfos[i].setStatus((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			serverInfos[i].setStatusName(new String(content,offset,len,"utf-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			serverInfos[i].setDescription(new String(content,offset,len,"utf-8"));
			offset += len;
			serverInfos[i].setOnlinePlayerNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			serverInfos[i].setHasPlayer(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			serverInfos[i].setCareer((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			serverInfos[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			serverInfos[i].setPlayerName(new String(content,offset,len,"utf-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x802EE113;
	}

	public String getTypeDescription() {
		return "NEW_QUERY_SERVER_LIST_RES";
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
			len +=lastLoginCategory.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < categories.length; i++){
			len += 2;
			try{
				len += categories[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < newservernames.length; i++){
			len += 2;
			try{
				len += newservernames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < recommendservernames.length; i++){
			len += 2;
			try{
				len += recommendservernames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < serverInfos.length ; i++){
			len += 2;
			if(serverInfos[i].getCategory() != null){
				try{
				len += serverInfos[i].getCategory().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(serverInfos[i].getName() != null){
				try{
				len += serverInfos[i].getName().getBytes("utf-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			}
			len += 2;
			if(serverInfos[i].getIp() != null){
				try{
				len += serverInfos[i].getIp().getBytes("utf-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			}
			len += 4;
			len += 1;
			len += 1;
			len += 4;
			len += 2;
			if(serverInfos[i].getStatusName() != null){
				try{
				len += serverInfos[i].getStatusName().getBytes("utf-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			}
			len += 2;
			if(serverInfos[i].getDescription() != null){
				try{
				len += serverInfos[i].getDescription().getBytes("utf-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			}
			len += 4;
			len += 1;
			len += 4;
			len += 4;
			len += 2;
			if(serverInfos[i].getPlayerName() != null){
				try{
				len += serverInfos[i].getPlayerName().getBytes("utf-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
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
			 tmpBytes1 = lastLoginCategory.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(categories.length);
			for(int i = 0 ; i < categories.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = categories[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(newservernames.length);
			for(int i = 0 ; i < newservernames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = newservernames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(recommendservernames.length);
			for(int i = 0 ; i < recommendservernames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = recommendservernames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(serverInfos.length);
			for(int i = 0 ; i < serverInfos.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = serverInfos[i].getCategory().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = serverInfos[i].getName().getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = serverInfos[i].getIp().getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)serverInfos[i].getPort());
				buffer.put((byte)serverInfos[i].getClientid());
				buffer.put((byte)serverInfos[i].getServerid());
				buffer.putInt((int)serverInfos[i].getStatus());
				try{
				tmpBytes2 = serverInfos[i].getStatusName().getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = serverInfos[i].getDescription().getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)serverInfos[i].getOnlinePlayerNum());
				buffer.put((byte)(serverInfos[i].isHasPlayer()==false?0:1));
				buffer.putInt((int)serverInfos[i].getCareer());
				buffer.putInt((int)serverInfos[i].getLevel());
				try{
				tmpBytes2 = serverInfos[i].getPlayerName().getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
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
	 *	最近登录的区和服务器，如果没有最近登录，服务器会制定一个区和服务器
	 */
	public String getLastLoginCategory(){
		return lastLoginCategory;
	}

	/**
	 * 设置属性：
	 *	最近登录的区和服务器，如果没有最近登录，服务器会制定一个区和服务器
	 */
	public void setLastLoginCategory(String lastLoginCategory){
		this.lastLoginCategory = lastLoginCategory;
	}

	/**
	 * 获取属性：
	 *	所有分区名字，按顺序显示
	 */
	public String[] getCategories(){
		return categories;
	}

	/**
	 * 设置属性：
	 *	所有分区名字，按顺序显示
	 */
	public void setCategories(String[] categories){
		this.categories = categories;
	}

	/**
	 * 获取属性：
	 *	新服务器名称
	 */
	public String[] getNewservernames(){
		return newservernames;
	}

	/**
	 * 设置属性：
	 *	新服务器名称
	 */
	public void setNewservernames(String[] newservernames){
		this.newservernames = newservernames;
	}

	/**
	 * 获取属性：
	 *	推荐服务器名称
	 */
	public String[] getRecommendservernames(){
		return recommendservernames;
	}

	/**
	 * 设置属性：
	 *	推荐服务器名称
	 */
	public void setRecommendservernames(String[] recommendservernames){
		this.recommendservernames = recommendservernames;
	}

	/**
	 * 获取属性：
	 *	大区里的服务器信息
	 */
	public ServerInfoForClient[] getServerInfos(){
		return serverInfos;
	}

	/**
	 * 设置属性：
	 *	大区里的服务器信息
	 */
	public void setServerInfos(ServerInfoForClient[] serverInfos){
		this.serverInfos = serverInfos;
	}

}