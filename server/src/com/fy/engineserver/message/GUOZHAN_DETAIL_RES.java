package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 国战详情<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>winCountry.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>winCountry</td><td>String</td><td>winCountry.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackCountry.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>attackCountry</td><td>String</td><td>attackCountry.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>attackName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackName[0]</td><td>String</td><td>attackName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>attackName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackName[1]</td><td>String</td><td>attackName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>attackName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackName[2]</td><td>String</td><td>attackName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackKillNum.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>attackKillNum</td><td>int[]</td><td>attackKillNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defendCountry.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defendCountry</td><td>String</td><td>defendCountry.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defendName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defendName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defendName[0]</td><td>String</td><td>defendName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defendName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defendName[1]</td><td>String</td><td>defendName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defendName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defendName[2]</td><td>String</td><td>defendName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defendKillNum.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defendKillNum</td><td>int[]</td><td>defendKillNum.length</td><td>*</td></tr>
 * </table>
 */
public class GUOZHAN_DETAIL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String winCountry;
	String attackCountry;
	String[] attackName;
	int[] attackKillNum;
	String defendCountry;
	String[] defendName;
	int[] defendKillNum;

	public GUOZHAN_DETAIL_RES(){
	}

	public GUOZHAN_DETAIL_RES(long seqNum,String winCountry,String attackCountry,String[] attackName,int[] attackKillNum,String defendCountry,String[] defendName,int[] defendKillNum){
		this.seqNum = seqNum;
		this.winCountry = winCountry;
		this.attackCountry = attackCountry;
		this.attackName = attackName;
		this.attackKillNum = attackKillNum;
		this.defendCountry = defendCountry;
		this.defendName = defendName;
		this.defendKillNum = defendKillNum;
	}

	public GUOZHAN_DETAIL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		winCountry = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		attackCountry = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		attackName = new String[len];
		for(int i = 0 ; i < attackName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			attackName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		attackKillNum = new int[len];
		for(int i = 0 ; i < attackKillNum.length ; i++){
			attackKillNum[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		defendCountry = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		defendName = new String[len];
		for(int i = 0 ; i < defendName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			defendName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		defendKillNum = new int[len];
		for(int i = 0 ; i < defendKillNum.length ; i++){
			defendKillNum[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70EE0014;
	}

	public String getTypeDescription() {
		return "GUOZHAN_DETAIL_RES";
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
			len +=winCountry.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=attackCountry.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < attackName.length; i++){
			len += 2;
			try{
				len += attackName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += attackKillNum.length * 4;
		len += 2;
		try{
			len +=defendCountry.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < defendName.length; i++){
			len += 2;
			try{
				len += defendName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += defendKillNum.length * 4;
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
			 tmpBytes1 = winCountry.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = attackCountry.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(attackName.length);
			for(int i = 0 ; i < attackName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = attackName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(attackKillNum.length);
			for(int i = 0 ; i < attackKillNum.length; i++){
				buffer.putInt(attackKillNum[i]);
			}
				try{
			tmpBytes1 = defendCountry.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(defendName.length);
			for(int i = 0 ; i < defendName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = defendName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(defendKillNum.length);
			for(int i = 0 ; i < defendKillNum.length; i++){
				buffer.putInt(defendKillNum[i]);
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
	 *	无帮助说明
	 */
	public String getWinCountry(){
		return winCountry;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setWinCountry(String winCountry){
		this.winCountry = winCountry;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String getAttackCountry(){
		return attackCountry;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAttackCountry(String attackCountry){
		this.attackCountry = attackCountry;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getAttackName(){
		return attackName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAttackName(String[] attackName){
		this.attackName = attackName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getAttackKillNum(){
		return attackKillNum;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAttackKillNum(int[] attackKillNum){
		this.attackKillNum = attackKillNum;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String getDefendCountry(){
		return defendCountry;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDefendCountry(String defendCountry){
		this.defendCountry = defendCountry;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getDefendName(){
		return defendName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDefendName(String[] defendName){
		this.defendName = defendName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getDefendKillNum(){
		return defendKillNum;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDefendKillNum(int[] defendKillNum){
		this.defendKillNum = defendKillNum;
	}

}