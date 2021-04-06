package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求获取家族列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazunames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazunames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazunames[0]</td><td>String</td><td>jiazunames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazunames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazunames[1]</td><td>String</td><td>jiazunames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazunames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazunames[2]</td><td>String</td><td>jiazunames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazulevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazulevels</td><td>int[]</td><td>jiazulevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMaster.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMaster[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMaster[0]</td><td>String</td><td>jiazuMaster[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMaster[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMaster[1]</td><td>String</td><td>jiazuMaster[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMaster[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuMaster[2]</td><td>String</td><td>jiazuMaster[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuFull.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuFull</td><td>boolean[]</td><td>jiazuFull.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>slogan.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>slogan[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>slogan[0]</td><td>String</td><td>slogan[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>slogan[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>slogan[1]</td><td>String</td><td>slogan[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>slogan[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>slogan[2]</td><td>String</td><td>slogan[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazumembers.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazumembers</td><td>int[]</td><td>jiazumembers.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxMember.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxMember</td><td>int[]</td><td>maxMember.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestJiazuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestJiazuName</td><td>String</td><td>requestJiazuName.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class JIAZU_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] jiazunames;
	int[] jiazulevels;
	String[] jiazuMaster;
	boolean[] jiazuFull;
	String[] slogan;
	int[] jiazumembers;
	int[] maxMember;
	String requestJiazuName;

	public JIAZU_LIST_RES(){
	}

	public JIAZU_LIST_RES(long seqNum,String[] jiazunames,int[] jiazulevels,String[] jiazuMaster,boolean[] jiazuFull,String[] slogan,int[] jiazumembers,int[] maxMember,String requestJiazuName){
		this.seqNum = seqNum;
		this.jiazunames = jiazunames;
		this.jiazulevels = jiazulevels;
		this.jiazuMaster = jiazuMaster;
		this.jiazuFull = jiazuFull;
		this.slogan = slogan;
		this.jiazumembers = jiazumembers;
		this.maxMember = maxMember;
		this.requestJiazuName = requestJiazuName;
	}

	public JIAZU_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazunames = new String[len];
		for(int i = 0 ; i < jiazunames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazunames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazulevels = new int[len];
		for(int i = 0 ; i < jiazulevels.length ; i++){
			jiazulevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazuMaster = new String[len];
		for(int i = 0 ; i < jiazuMaster.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuMaster[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazuFull = new boolean[len];
		for(int i = 0 ; i < jiazuFull.length ; i++){
			jiazuFull[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		slogan = new String[len];
		for(int i = 0 ; i < slogan.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			slogan[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazumembers = new int[len];
		for(int i = 0 ; i < jiazumembers.length ; i++){
			jiazumembers[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		maxMember = new int[len];
		for(int i = 0 ; i < maxMember.length ; i++){
			maxMember[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		requestJiazuName = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x700AEE02;
	}

	public String getTypeDescription() {
		return "JIAZU_LIST_RES";
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
		for(int i = 0 ; i < jiazunames.length; i++){
			len += 2;
			try{
				len += jiazunames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += jiazulevels.length * 4;
		len += 4;
		for(int i = 0 ; i < jiazuMaster.length; i++){
			len += 2;
			try{
				len += jiazuMaster[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += jiazuFull.length;
		len += 4;
		for(int i = 0 ; i < slogan.length; i++){
			len += 2;
			try{
				len += slogan[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += jiazumembers.length * 4;
		len += 4;
		len += maxMember.length * 4;
		len += 2;
		try{
			len +=requestJiazuName.getBytes("UTF-8").length;
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

			buffer.putInt(jiazunames.length);
			for(int i = 0 ; i < jiazunames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = jiazunames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(jiazulevels.length);
			for(int i = 0 ; i < jiazulevels.length; i++){
				buffer.putInt(jiazulevels[i]);
			}
			buffer.putInt(jiazuMaster.length);
			for(int i = 0 ; i < jiazuMaster.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = jiazuMaster[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(jiazuFull.length);
			for(int i = 0 ; i < jiazuFull.length; i++){
				buffer.put((byte)(jiazuFull[i]==false?0:1));
			}
			buffer.putInt(slogan.length);
			for(int i = 0 ; i < slogan.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = slogan[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(jiazumembers.length);
			for(int i = 0 ; i < jiazumembers.length; i++){
				buffer.putInt(jiazumembers[i]);
			}
			buffer.putInt(maxMember.length);
			for(int i = 0 ; i < maxMember.length; i++){
				buffer.putInt(maxMember[i]);
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = requestJiazuName.getBytes("UTF-8");
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
	 *	家族名称列表
	 */
	public String[] getJiazunames(){
		return jiazunames;
	}

	/**
	 * 设置属性：
	 *	家族名称列表
	 */
	public void setJiazunames(String[] jiazunames){
		this.jiazunames = jiazunames;
	}

	/**
	 * 获取属性：
	 *	对应的家族级别
	 */
	public int[] getJiazulevels(){
		return jiazulevels;
	}

	/**
	 * 设置属性：
	 *	对应的家族级别
	 */
	public void setJiazulevels(int[] jiazulevels){
		this.jiazulevels = jiazulevels;
	}

	/**
	 * 获取属性：
	 *	家族族长的名字
	 */
	public String[] getJiazuMaster(){
		return jiazuMaster;
	}

	/**
	 * 设置属性：
	 *	家族族长的名字
	 */
	public void setJiazuMaster(String[] jiazuMaster){
		this.jiazuMaster = jiazuMaster;
	}

	/**
	 * 获取属性：
	 *	是否已经满了
	 */
	public boolean[] getJiazuFull(){
		return jiazuFull;
	}

	/**
	 * 设置属性：
	 *	是否已经满了
	 */
	public void setJiazuFull(boolean[] jiazuFull){
		this.jiazuFull = jiazuFull;
	}

	/**
	 * 获取属性：
	 *	家族标语
	 */
	public String[] getSlogan(){
		return slogan;
	}

	/**
	 * 设置属性：
	 *	家族标语
	 */
	public void setSlogan(String[] slogan){
		this.slogan = slogan;
	}

	/**
	 * 获取属性：
	 *	对应的家族成员数
	 */
	public int[] getJiazumembers(){
		return jiazumembers;
	}

	/**
	 * 设置属性：
	 *	对应的家族成员数
	 */
	public void setJiazumembers(int[] jiazumembers){
		this.jiazumembers = jiazumembers;
	}

	/**
	 * 获取属性：
	 *	对应的家族最大成员数
	 */
	public int[] getMaxMember(){
		return maxMember;
	}

	/**
	 * 设置属性：
	 *	对应的家族最大成员数
	 */
	public void setMaxMember(int[] maxMember){
		this.maxMember = maxMember;
	}

	/**
	 * 获取属性：
	 *	已申请家族名称
	 */
	public String getRequestJiazuName(){
		return requestJiazuName;
	}

	/**
	 * 设置属性：
	 *	已申请家族名称
	 */
	public void setRequestJiazuName(String requestJiazuName){
		this.requestJiazuName = requestJiazuName;
	}

}