package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.gamegateway.gmaction.GmAction;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * GM操作补发页面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.serverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.serverName</td><td>String</td><td>gmAction.serverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.playerName</td><td>String</td><td>gmAction.playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.userName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.userName</td><td>String</td><td>gmAction.userName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.actionType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.reason.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.reason</td><td>String</td><td>gmAction.reason.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.operator.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.operator</td><td>String</td><td>gmAction.operator.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.actionTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.amount</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.articleInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.articleInfo</td><td>String</td><td>gmAction.articleInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.petInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.petInfo</td><td>String</td><td>gmAction.petInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.otherInfos.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.otherInfos[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.otherInfos[0]</td><td>String</td><td>gmAction.otherInfos[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.otherInfos[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.otherInfos[1]</td><td>String</td><td>gmAction.otherInfos[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmAction.otherInfos[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmAction.otherInfos[2]</td><td>String</td><td>gmAction.otherInfos[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GM_ACTION_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	GmAction gmAction;

	public GM_ACTION_REQ(){
	}

	public GM_ACTION_REQ(long seqNum,GmAction gmAction){
		this.seqNum = seqNum;
		this.gmAction = gmAction;
	}

	public GM_ACTION_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		gmAction = new GmAction();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gmAction.setServerName(new String(content,offset,len,"UTF-8"));
		offset += len;
		gmAction.setPlayerId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gmAction.setPlayerName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gmAction.setUserName(new String(content,offset,len,"UTF-8"));
		offset += len;
		gmAction.setActionType((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gmAction.setReason(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gmAction.setOperator(new String(content,offset,len,"UTF-8"));
		offset += len;
		gmAction.setActionTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		gmAction.setAmount((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gmAction.setArticleInfo(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gmAction.setPetInfo(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] otherInfos_0001 = new String[len];
		for(int j = 0 ; j < otherInfos_0001.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			otherInfos_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		gmAction.setOtherInfos(otherInfos_0001);
	}

	public int getType() {
		return 0x002EE109;
	}

	public String getTypeDescription() {
		return "GM_ACTION_REQ";
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
		if(gmAction.getServerName() != null){
			try{
			len += gmAction.getServerName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 2;
		if(gmAction.getPlayerName() != null){
			try{
			len += gmAction.getPlayerName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(gmAction.getUserName() != null){
			try{
			len += gmAction.getUserName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 2;
		if(gmAction.getReason() != null){
			try{
			len += gmAction.getReason().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(gmAction.getOperator() != null){
			try{
			len += gmAction.getOperator().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 8;
		len += 2;
		if(gmAction.getArticleInfo() != null){
			try{
			len += gmAction.getArticleInfo().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(gmAction.getPetInfo() != null){
			try{
			len += gmAction.getPetInfo().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		String[] otherInfos = gmAction.getOtherInfos();
		for(int j = 0 ; j < otherInfos.length; j++){
			len += 2;
			try{
				len += otherInfos[j].getBytes("UTF-8").length;
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

			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = gmAction.getServerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(gmAction.getPlayerId());
				try{
				tmpBytes1 = gmAction.getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = gmAction.getUserName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)gmAction.getActionType());
				try{
				tmpBytes1 = gmAction.getReason().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = gmAction.getOperator().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(gmAction.getActionTime());
			buffer.putLong(gmAction.getAmount());
				try{
				tmpBytes1 = gmAction.getArticleInfo().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = gmAction.getPetInfo().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(gmAction.getOtherInfos().length);
			String[] otherInfos_0002 = gmAction.getOtherInfos();
			for(int j = 0 ; j < otherInfos_0002.length ; j++){
				try{
				buffer.putShort((short)otherInfos_0002[j].getBytes("UTF-8").length);
				buffer.put(otherInfos_0002[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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
	 *	gm后台操作信息
	 */
	public GmAction getGmAction(){
		return gmAction;
	}

	/**
	 * 设置属性：
	 *	gm后台操作信息
	 */
	public void setGmAction(GmAction gmAction){
		this.gmAction = gmAction;
	}

}