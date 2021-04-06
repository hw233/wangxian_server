package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 国战结果(新)<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result</td><td>String</td><td>result.length</td><td>字符串对应的byte数组</td></tr>
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
 * <tr bgcolor="#FAFAFA" align="center"><td>damage</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>killNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gongxun</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>exp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attacker</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcNames[0]</td><td>String</td><td>npcNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcNames[1]</td><td>String</td><td>npcNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcNames[2]</td><td>String</td><td>npcNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcStatus.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcStatus</td><td>int[]</td><td>npcStatus.length</td><td>*</td></tr>
 * </table>
 */
public class GUOZHAN_RESULT_NEW_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String result;
	String winCountry;
	String attackCountry;
	String[] attackName;
	int[] attackKillNum;
	String defendCountry;
	String[] defendName;
	int[] defendKillNum;
	long damage;
	int killNum;
	int gongxun;
	long exp;
	boolean attacker;
	String[] npcNames;
	int[] npcStatus;

	public GUOZHAN_RESULT_NEW_REQ(){
	}

	public GUOZHAN_RESULT_NEW_REQ(long seqNum,String result,String winCountry,String attackCountry,String[] attackName,int[] attackKillNum,String defendCountry,String[] defendName,int[] defendKillNum,long damage,int killNum,int gongxun,long exp,boolean attacker,String[] npcNames,int[] npcStatus){
		this.seqNum = seqNum;
		this.result = result;
		this.winCountry = winCountry;
		this.attackCountry = attackCountry;
		this.attackName = attackName;
		this.attackKillNum = attackKillNum;
		this.defendCountry = defendCountry;
		this.defendName = defendName;
		this.defendKillNum = defendKillNum;
		this.damage = damage;
		this.killNum = killNum;
		this.gongxun = gongxun;
		this.exp = exp;
		this.attacker = attacker;
		this.npcNames = npcNames;
		this.npcStatus = npcStatus;
	}

	public GUOZHAN_RESULT_NEW_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		result = new String(content,offset,len,"UTF-8");
		offset += len;
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
		damage = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		killNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		gongxun = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		exp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		attacker = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		npcNames = new String[len];
		for(int i = 0 ; i < npcNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			npcNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		npcStatus = new int[len];
		for(int i = 0 ; i < npcStatus.length ; i++){
			npcStatus[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x00EE0019;
	}

	public String getTypeDescription() {
		return "GUOZHAN_RESULT_NEW_REQ";
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
			len +=result.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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
		len += 8;
		len += 4;
		len += 4;
		len += 8;
		len += 1;
		len += 4;
		for(int i = 0 ; i < npcNames.length; i++){
			len += 2;
			try{
				len += npcNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += npcStatus.length * 4;
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
			 tmpBytes1 = result.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
			buffer.putLong(damage);
			buffer.putInt(killNum);
			buffer.putInt(gongxun);
			buffer.putLong(exp);
			buffer.put((byte)(attacker==false?0:1));
			buffer.putInt(npcNames.length);
			for(int i = 0 ; i < npcNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = npcNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(npcStatus.length);
			for(int i = 0 ; i < npcStatus.length; i++){
				buffer.putInt(npcStatus[i]);
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
	public String getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setResult(String result){
		this.result = result;
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

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public long getDamage(){
		return damage;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDamage(long damage){
		this.damage = damage;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getKillNum(){
		return killNum;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setKillNum(int killNum){
		this.killNum = killNum;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getGongxun(){
		return gongxun;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setGongxun(int gongxun){
		this.gongxun = gongxun;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public long getExp(){
		return exp;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setExp(long exp){
		this.exp = exp;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean getAttacker(){
		return attacker;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAttacker(boolean attacker){
		this.attacker = attacker;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getNpcNames(){
		return npcNames;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setNpcNames(String[] npcNames){
		this.npcNames = npcNames;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getNpcStatus(){
		return npcStatus;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setNpcStatus(int[] npcStatus){
		this.npcStatus = npcStatus;
	}

}