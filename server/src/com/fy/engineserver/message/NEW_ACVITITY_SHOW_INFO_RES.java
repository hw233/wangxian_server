package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.datamanager.module.ActivityModule;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 新活动界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas.length</td><td>int</td><td>4个字节</td><td>ActivityModule数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].activityShowName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].activityShowName</td><td>String</td><td>datas[0].activityShowName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].des4client.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].des4client</td><td>String</td><td>datas[0].des4client.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].articleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].articleIds</td><td>long[]</td><td>datas[0].articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].npcName</td><td>String</td><td>datas[0].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].npcPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].npcPoint</td><td>int[]</td><td>datas[0].npcPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[0].mapName</td><td>String</td><td>datas[0].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[0].inActivityTime</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].activityShowName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].activityShowName</td><td>String</td><td>datas[1].activityShowName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].des4client.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].des4client</td><td>String</td><td>datas[1].des4client.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].articleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].articleIds</td><td>long[]</td><td>datas[1].articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].npcName</td><td>String</td><td>datas[1].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].npcPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].npcPoint</td><td>int[]</td><td>datas[1].npcPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[1].mapName</td><td>String</td><td>datas[1].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[1].inActivityTime</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].activityShowName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].activityShowName</td><td>String</td><td>datas[2].activityShowName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].des4client.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].des4client</td><td>String</td><td>datas[2].des4client.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].articleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].articleIds</td><td>long[]</td><td>datas[2].articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].npcName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].npcName</td><td>String</td><td>datas[2].npcName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].npcPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].npcPoint</td><td>int[]</td><td>datas[2].npcPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>datas[2].mapName</td><td>String</td><td>datas[2].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>datas[2].inActivityTime</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NEW_ACVITITY_SHOW_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ActivityModule[] datas;

	public NEW_ACVITITY_SHOW_INFO_RES(){
	}

	public NEW_ACVITITY_SHOW_INFO_RES(long seqNum,ActivityModule[] datas){
		this.seqNum = seqNum;
		this.datas = datas;
	}

	public NEW_ACVITITY_SHOW_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		datas = new ActivityModule[len];
		for(int i = 0 ; i < datas.length ; i++){
			datas[i] = new ActivityModule();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			datas[i].setActivityShowName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			datas[i].setDes4client(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] articleIds_0001 = new long[len];
			for(int j = 0 ; j < articleIds_0001.length ; j++){
				articleIds_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			datas[i].setArticleIds(articleIds_0001);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			datas[i].setNpcName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] npcPoint_0002 = new int[len];
			for(int j = 0 ; j < npcPoint_0002.length ; j++){
				npcPoint_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			datas[i].setNpcPoint(npcPoint_0002);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			datas[i].setMapName(new String(content,offset,len,"UTF-8"));
			offset += len;
			datas[i].setInActivityTime(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
		}
	}

	public int getType() {
		return 0x70FFF040;
	}

	public String getTypeDescription() {
		return "NEW_ACVITITY_SHOW_INFO_RES";
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
		for(int i = 0 ; i < datas.length ; i++){
			len += 2;
			if(datas[i].getActivityShowName() != null){
				try{
				len += datas[i].getActivityShowName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(datas[i].getDes4client() != null){
				try{
				len += datas[i].getDes4client().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += datas[i].getArticleIds().length * 8;
			len += 2;
			if(datas[i].getNpcName() != null){
				try{
				len += datas[i].getNpcName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += datas[i].getNpcPoint().length * 4;
			len += 2;
			if(datas[i].getMapName() != null){
				try{
				len += datas[i].getMapName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
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

			buffer.putInt(datas.length);
			for(int i = 0 ; i < datas.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = datas[i].getActivityShowName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = datas[i].getDes4client().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(datas[i].getArticleIds().length);
				long[] articleIds_0003 = datas[i].getArticleIds();
				for(int j = 0 ; j < articleIds_0003.length ; j++){
					buffer.putLong(articleIds_0003[j]);
				}
				try{
				tmpBytes2 = datas[i].getNpcName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(datas[i].getNpcPoint().length);
				int[] npcPoint_0004 = datas[i].getNpcPoint();
				for(int j = 0 ; j < npcPoint_0004.length ; j++){
					buffer.putInt(npcPoint_0004[j]);
				}
				try{
				tmpBytes2 = datas[i].getMapName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(datas[i].isInActivityTime()==false?0:1));
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
	 *	活动数据
	 */
	public ActivityModule[] getDatas(){
		return datas;
	}

	/**
	 * 设置属性：
	 *	活动数据
	 */
	public void setDatas(ActivityModule[] datas){
		this.datas = datas;
	}

}