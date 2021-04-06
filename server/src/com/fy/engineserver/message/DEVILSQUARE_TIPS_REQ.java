package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.carbon.devilSquare.model.DevelSquareBaseConf;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 恶魔广场副本介绍内容,开启副本等级等<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes.length</td><td>int</td><td>4个字节</td><td>DevelSquareBaseConf数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].plays.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].plays</td><td>String</td><td>messes[0].plays.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].bcstorys.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].bcstorys</td><td>String</td><td>messes[0].bcstorys.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].costprops</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].costNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].dropprops.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].dropprops</td><td>long[]</td><td>messes[0].dropprops.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].dropprobabbly.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].dropprobabbly[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].dropprobabbly[0]</td><td>String</td><td>messes[0].dropprobabbly[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].dropprobabbly[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].dropprobabbly[1]</td><td>String</td><td>messes[0].dropprobabbly[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].dropprobabbly[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].dropprobabbly[2]</td><td>String</td><td>messes[0].dropprobabbly[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].mapName</td><td>String</td><td>messes[0].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].plays.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].plays</td><td>String</td><td>messes[1].plays.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].bcstorys.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].bcstorys</td><td>String</td><td>messes[1].bcstorys.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].costprops</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].costNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].dropprops.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].dropprops</td><td>long[]</td><td>messes[1].dropprops.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].dropprobabbly.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].dropprobabbly[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].dropprobabbly[0]</td><td>String</td><td>messes[1].dropprobabbly[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].dropprobabbly[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].dropprobabbly[1]</td><td>String</td><td>messes[1].dropprobabbly[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].dropprobabbly[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].dropprobabbly[2]</td><td>String</td><td>messes[1].dropprobabbly[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].mapName</td><td>String</td><td>messes[1].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].plays.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].plays</td><td>String</td><td>messes[2].plays.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].bcstorys.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].bcstorys</td><td>String</td><td>messes[2].bcstorys.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].costprops</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].costNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].dropprops.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].dropprops</td><td>long[]</td><td>messes[2].dropprops.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].dropprobabbly.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].dropprobabbly[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].dropprobabbly[0]</td><td>String</td><td>messes[2].dropprobabbly[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].dropprobabbly[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].dropprobabbly[1]</td><td>String</td><td>messes[2].dropprobabbly[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].dropprobabbly[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].dropprobabbly[2]</td><td>String</td><td>messes[2].dropprobabbly[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].mapName</td><td>String</td><td>messes[2].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class DEVILSQUARE_TIPS_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	DevelSquareBaseConf[] messes;

	public DEVILSQUARE_TIPS_REQ(){
	}

	public DEVILSQUARE_TIPS_REQ(long seqNum,DevelSquareBaseConf[] messes){
		this.seqNum = seqNum;
		this.messes = messes;
	}

	public DEVILSQUARE_TIPS_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		messes = new DevelSquareBaseConf[len];
		for(int i = 0 ; i < messes.length ; i++){
			messes[i] = new DevelSquareBaseConf();
			messes[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setPlays(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setBcstorys(new String(content,offset,len,"UTF-8"));
			offset += len;
			messes[i].setCostprops((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			messes[i].setCostNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] dropprops_0001 = new long[len];
			for(int j = 0 ; j < dropprops_0001.length ; j++){
				dropprops_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			messes[i].setDropprops(dropprops_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] dropprobabbly_0002 = new String[len];
			for(int j = 0 ; j < dropprobabbly_0002.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				dropprobabbly_0002[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			messes[i].setDropprobabbly(dropprobabbly_0002);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setMapName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x0E0EAA89;
	}

	public String getTypeDescription() {
		return "DEVILSQUARE_TIPS_REQ";
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
		for(int i = 0 ; i < messes.length ; i++){
			len += 4;
			len += 2;
			if(messes[i].getPlays() != null){
				try{
				len += messes[i].getPlays().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(messes[i].getBcstorys() != null){
				try{
				len += messes[i].getBcstorys().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 4;
			len += 4;
			len += messes[i].getDropprops().length * 8;
			len += 4;
			String[] dropprobabbly = messes[i].getDropprobabbly();
			for(int j = 0 ; j < dropprobabbly.length; j++){
				len += 2;
				try{
					len += dropprobabbly[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(messes[i].getMapName() != null){
				try{
				len += messes[i].getMapName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.putInt(messes.length);
			for(int i = 0 ; i < messes.length ; i++){
				buffer.putInt((int)messes[i].getLevel());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = messes[i].getPlays().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = messes[i].getBcstorys().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(messes[i].getCostprops());
				buffer.putInt((int)messes[i].getCostNum());
				buffer.putInt(messes[i].getDropprops().length);
				long[] dropprops_0003 = messes[i].getDropprops();
				for(int j = 0 ; j < dropprops_0003.length ; j++){
					buffer.putLong(dropprops_0003[j]);
				}
				buffer.putInt(messes[i].getDropprobabbly().length);
				String[] dropprobabbly_0004 = messes[i].getDropprobabbly();
				for(int j = 0 ; j < dropprobabbly_0004.length ; j++){
				try{
					buffer.putShort((short)dropprobabbly_0004[j].getBytes("UTF-8").length);
					buffer.put(dropprobabbly_0004[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				try{
				tmpBytes2 = messes[i].getMapName().getBytes("UTF-8");
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
	 *	法宝信息
	 */
	public DevelSquareBaseConf[] getMesses(){
		return messes;
	}

	/**
	 * 设置属性：
	 *	法宝信息
	 */
	public void setMesses(DevelSquareBaseConf[] messes){
		this.messes = messes;
	}

}