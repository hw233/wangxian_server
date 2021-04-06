package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.vip.data.VIPData2;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询VIP信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas.length</td><td>int</td><td>4个字节</td><td>VIPData2数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[0].vipLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[0].needCost</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[0].vipIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[0].vipIcon</td><td>String</td><td>vipDatas[0].vipIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[0].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[0].description</td><td>String</td><td>vipDatas[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[1].vipLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[1].needCost</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[1].vipIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[1].vipIcon</td><td>String</td><td>vipDatas[1].vipIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[1].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[1].description</td><td>String</td><td>vipDatas[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[2].vipLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[2].needCost</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[2].vipIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[2].vipIcon</td><td>String</td><td>vipDatas[2].vipIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[2].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipDatas[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipDatas[2].description</td><td>String</td><td>vipDatas[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yinziPerRMB</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipRewardFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class VIP_2_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	VIPData2[] vipDatas;
	int yinziPerRMB;
	boolean vipRewardFlag;

	public VIP_2_RES(){
	}

	public VIP_2_RES(long seqNum,VIPData2[] vipDatas,int yinziPerRMB,boolean vipRewardFlag){
		this.seqNum = seqNum;
		this.vipDatas = vipDatas;
		this.yinziPerRMB = yinziPerRMB;
		this.vipRewardFlag = vipRewardFlag;
	}

	public VIP_2_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		vipDatas = new VIPData2[len];
		for(int i = 0 ; i < vipDatas.length ; i++){
			vipDatas[i] = new VIPData2();
			vipDatas[i].setVipLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			vipDatas[i].setNeedCost((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			vipDatas[i].setVipIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			vipDatas[i].setArticleId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			vipDatas[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		yinziPerRMB = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		vipRewardFlag = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x70F0EF69;
	}

	public String getTypeDescription() {
		return "VIP_2_RES";
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
		for(int i = 0 ; i < vipDatas.length ; i++){
			len += 4;
			len += 8;
			len += 2;
			if(vipDatas[i].getVipIcon() != null){
				try{
				len += vipDatas[i].getVipIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 2;
			if(vipDatas[i].getDescription() != null){
				try{
				len += vipDatas[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		len += 1;
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

			buffer.putInt(vipDatas.length);
			for(int i = 0 ; i < vipDatas.length ; i++){
				buffer.putInt((int)vipDatas[i].getVipLevel());
				buffer.putLong(vipDatas[i].getNeedCost());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = vipDatas[i].getVipIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(vipDatas[i].getArticleId());
				try{
				tmpBytes2 = vipDatas[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(yinziPerRMB);
			buffer.put((byte)(vipRewardFlag==false?0:1));
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
	 *	vip信息
	 */
	public VIPData2[] getVipDatas(){
		return vipDatas;
	}

	/**
	 * 设置属性：
	 *	vip信息
	 */
	public void setVipDatas(VIPData2[] vipDatas){
		this.vipDatas = vipDatas;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getYinziPerRMB(){
		return yinziPerRMB;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setYinziPerRMB(int yinziPerRMB){
		this.yinziPerRMB = yinziPerRMB;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean getVipRewardFlag(){
		return vipRewardFlag;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setVipRewardFlag(boolean vipRewardFlag){
		this.vipRewardFlag = vipRewardFlag;
	}

}