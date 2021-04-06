package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sifang.info.SiFangOverInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 四方神兽活动结果查询<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>winIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>overInfo.length</td><td>int</td><td>4个字节</td><td>SiFangOverInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>overInfo[0].JiaZuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>overInfo[0].JiaZuName</td><td>String</td><td>overInfo[0].JiaZuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>overInfo[0].leftNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>overInfo[0].killNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>overInfo[0].reward</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>overInfo[1].JiaZuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>overInfo[1].JiaZuName</td><td>String</td><td>overInfo[1].JiaZuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>overInfo[1].leftNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>overInfo[1].killNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>overInfo[1].reward</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>overInfo[2].JiaZuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>overInfo[2].JiaZuName</td><td>String</td><td>overInfo[2].JiaZuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>overInfo[2].leftNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>overInfo[2].killNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>overInfo[2].reward</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SIFANG_OVER_MSG_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int sifangType;
	int winIndex;
	int selfIndex;
	SiFangOverInfo[] overInfo;

	public SIFANG_OVER_MSG_RES(){
	}

	public SIFANG_OVER_MSG_RES(long seqNum,int sifangType,int winIndex,int selfIndex,SiFangOverInfo[] overInfo){
		this.seqNum = seqNum;
		this.sifangType = sifangType;
		this.winIndex = winIndex;
		this.selfIndex = selfIndex;
		this.overInfo = overInfo;
	}

	public SIFANG_OVER_MSG_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		sifangType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		winIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		selfIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		overInfo = new SiFangOverInfo[len];
		for(int i = 0 ; i < overInfo.length ; i++){
			overInfo[i] = new SiFangOverInfo();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			overInfo[i].setJiaZuName(new String(content,offset,len,"UTF-8"));
			offset += len;
			overInfo[i].setLeftNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			overInfo[i].setKillNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			overInfo[i].setReward((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70100003;
	}

	public String getTypeDescription() {
		return "SIFANG_OVER_MSG_RES";
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
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < overInfo.length ; i++){
			len += 2;
			if(overInfo[i].getJiaZuName() != null){
				try{
				len += overInfo[i].getJiaZuName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
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

			buffer.putInt(sifangType);
			buffer.putInt(winIndex);
			buffer.putInt(selfIndex);
			buffer.putInt(overInfo.length);
			for(int i = 0 ; i < overInfo.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = overInfo[i].getJiaZuName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)overInfo[i].getLeftNum());
				buffer.putInt((int)overInfo[i].getKillNum());
				buffer.putInt((int)overInfo[i].getReward());
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
	 *	活动类型
	 */
	public int getSifangType(){
		return sifangType;
	}

	/**
	 * 设置属性：
	 *	活动类型
	 */
	public void setSifangType(int sifangType){
		this.sifangType = sifangType;
	}

	/**
	 * 获取属性：
	 *	胜利那个
	 */
	public int getWinIndex(){
		return winIndex;
	}

	/**
	 * 设置属性：
	 *	胜利那个
	 */
	public void setWinIndex(int winIndex){
		this.winIndex = winIndex;
	}

	/**
	 * 获取属性：
	 *	自己那个
	 */
	public int getSelfIndex(){
		return selfIndex;
	}

	/**
	 * 设置属性：
	 *	自己那个
	 */
	public void setSelfIndex(int selfIndex){
		this.selfIndex = selfIndex;
	}

	/**
	 * 获取属性：
	 *	结束信息
	 */
	public SiFangOverInfo[] getOverInfo(){
		return overInfo;
	}

	/**
	 * 设置属性：
	 *	结束信息
	 */
	public void setOverInfo(SiFangOverInfo[] overInfo){
		this.overInfo = overInfo;
	}

}