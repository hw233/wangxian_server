package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 隐藏属性<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hiddlenames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hiddlenames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hiddlenames[0]</td><td>String</td><td>hiddlenames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hiddlenames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hiddlenames[1]</td><td>String</td><td>hiddlenames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hiddlenames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hiddlenames[2]</td><td>String</td><td>hiddlenames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hiddlenums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hiddlenums</td><td>long[]</td><td>hiddlenums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hiddlemaxnums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hiddlemaxnums</td><td>long[]</td><td>hiddlemaxnums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialnames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialnames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialnames[0]</td><td>String</td><td>materialnames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialnames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialnames[1]</td><td>String</td><td>materialnames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialnames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialnames[2]</td><td>String</td><td>materialnames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costmess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costmess</td><td>String</td><td>costmess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiejimess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiejimess</td><td>String</td><td>jiejimess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxhiddlenum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_MAGICWEAPON_HIDDLE_PROP_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] hiddlenames;
	long[] hiddlenums;
	long[] hiddlemaxnums;
	String[] materialnames;
	String costmess;
	String jiejimess;
	int maxhiddlenum;

	public QUERY_MAGICWEAPON_HIDDLE_PROP_RES(){
	}

	public QUERY_MAGICWEAPON_HIDDLE_PROP_RES(long seqNum,String[] hiddlenames,long[] hiddlenums,long[] hiddlemaxnums,String[] materialnames,String costmess,String jiejimess,int maxhiddlenum){
		this.seqNum = seqNum;
		this.hiddlenames = hiddlenames;
		this.hiddlenums = hiddlenums;
		this.hiddlemaxnums = hiddlemaxnums;
		this.materialnames = materialnames;
		this.costmess = costmess;
		this.jiejimess = jiejimess;
		this.maxhiddlenum = maxhiddlenum;
	}

	public QUERY_MAGICWEAPON_HIDDLE_PROP_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		hiddlenames = new String[len];
		for(int i = 0 ; i < hiddlenames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			hiddlenames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		hiddlenums = new long[len];
		for(int i = 0 ; i < hiddlenums.length ; i++){
			hiddlenums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		hiddlemaxnums = new long[len];
		for(int i = 0 ; i < hiddlemaxnums.length ; i++){
			hiddlemaxnums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		materialnames = new String[len];
		for(int i = 0 ; i < materialnames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			materialnames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		costmess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		jiejimess = new String(content,offset,len,"UTF-8");
		offset += len;
		maxhiddlenum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F0EF18;
	}

	public String getTypeDescription() {
		return "QUERY_MAGICWEAPON_HIDDLE_PROP_RES";
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
		for(int i = 0 ; i < hiddlenames.length; i++){
			len += 2;
			try{
				len += hiddlenames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += hiddlenums.length * 8;
		len += 4;
		len += hiddlemaxnums.length * 8;
		len += 4;
		for(int i = 0 ; i < materialnames.length; i++){
			len += 2;
			try{
				len += materialnames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		try{
			len +=costmess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=jiejimess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
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

			buffer.putInt(hiddlenames.length);
			for(int i = 0 ; i < hiddlenames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = hiddlenames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(hiddlenums.length);
			for(int i = 0 ; i < hiddlenums.length; i++){
				buffer.putLong(hiddlenums[i]);
			}
			buffer.putInt(hiddlemaxnums.length);
			for(int i = 0 ; i < hiddlemaxnums.length; i++){
				buffer.putLong(hiddlemaxnums[i]);
			}
			buffer.putInt(materialnames.length);
			for(int i = 0 ; i < materialnames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = materialnames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = costmess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = jiejimess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(maxhiddlenum);
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
	 *	已经开启的隐藏属性名
	 */
	public String[] getHiddlenames(){
		return hiddlenames;
	}

	/**
	 * 设置属性：
	 *	已经开启的隐藏属性名
	 */
	public void setHiddlenames(String[] hiddlenames){
		this.hiddlenames = hiddlenames;
	}

	/**
	 * 获取属性：
	 *	已经开启的隐藏属性值
	 */
	public long[] getHiddlenums(){
		return hiddlenums;
	}

	/**
	 * 设置属性：
	 *	已经开启的隐藏属性值
	 */
	public void setHiddlenums(long[] hiddlenums){
		this.hiddlenums = hiddlenums;
	}

	/**
	 * 获取属性：
	 *	已经开启的隐藏属性最大值
	 */
	public long[] getHiddlemaxnums(){
		return hiddlemaxnums;
	}

	/**
	 * 设置属性：
	 *	已经开启的隐藏属性最大值
	 */
	public void setHiddlemaxnums(long[] hiddlemaxnums){
		this.hiddlemaxnums = hiddlemaxnums;
	}

	/**
	 * 获取属性：
	 *	刷新隐藏属性的材料
	 */
	public String[] getMaterialnames(){
		return materialnames;
	}

	/**
	 * 设置属性：
	 *	刷新隐藏属性的材料
	 */
	public void setMaterialnames(String[] materialnames){
		this.materialnames = materialnames;
	}

	/**
	 * 获取属性：
	 *	消费信息
	 */
	public String getCostmess(){
		return costmess;
	}

	/**
	 * 设置属性：
	 *	消费信息
	 */
	public void setCostmess(String costmess){
		this.costmess = costmess;
	}

	/**
	 * 获取属性：
	 *	阶级描述
	 */
	public String getJiejimess(){
		return jiejimess;
	}

	/**
	 * 设置属性：
	 *	阶级描述
	 */
	public void setJiejimess(String jiejimess){
		this.jiejimess = jiejimess;
	}

	/**
	 * 获取属性：
	 *	该法宝隐藏属性个数上限
	 */
	public int getMaxhiddlenum(){
		return maxhiddlenum;
	}

	/**
	 * 设置属性：
	 *	该法宝隐藏属性个数上限
	 */
	public void setMaxhiddlenum(int maxhiddlenum){
		this.maxhiddlenum = maxhiddlenum;
	}

}