package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 七日登录奖励<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>states.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>states</td><td>int[]</td><td>states.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[0]</td><td>String</td><td>names[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[1]</td><td>String</td><td>names[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[2]</td><td>String</td><td>names[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[0]</td><td>String</td><td>icons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[1]</td><td>String</td><td>icons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[2]</td><td>String</td><td>icons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeIds1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeIds1</td><td>long[]</td><td>aeIds1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeIds2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeIds2</td><td>long[]</td><td>aeIds2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeIds3.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeIds3</td><td>long[]</td><td>aeIds3.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeIds4.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeIds4</td><td>long[]</td><td>aeIds4.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeIds5.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeIds5</td><td>long[]</td><td>aeIds5.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeIds6.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeIds6</td><td>long[]</td><td>aeIds6.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aeIds7.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aeIds7</td><td>long[]</td><td>aeIds7.length</td><td>*</td></tr>
 * </table>
 */
public class LOGIN_REWARD_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] states;
	String[] names;
	String[] icons;
	long[] aeIds1;
	long[] aeIds2;
	long[] aeIds3;
	long[] aeIds4;
	long[] aeIds5;
	long[] aeIds6;
	long[] aeIds7;

	public LOGIN_REWARD_RES(){
	}

	public LOGIN_REWARD_RES(long seqNum,int[] states,String[] names,String[] icons,long[] aeIds1,long[] aeIds2,long[] aeIds3,long[] aeIds4,long[] aeIds5,long[] aeIds6,long[] aeIds7){
		this.seqNum = seqNum;
		this.states = states;
		this.names = names;
		this.icons = icons;
		this.aeIds1 = aeIds1;
		this.aeIds2 = aeIds2;
		this.aeIds3 = aeIds3;
		this.aeIds4 = aeIds4;
		this.aeIds5 = aeIds5;
		this.aeIds6 = aeIds6;
		this.aeIds7 = aeIds7;
	}

	public LOGIN_REWARD_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		states = new int[len];
		for(int i = 0 ; i < states.length ; i++){
			states[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		names = new String[len];
		for(int i = 0 ; i < names.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			names[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		icons = new String[len];
		for(int i = 0 ; i < icons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			icons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeIds1 = new long[len];
		for(int i = 0 ; i < aeIds1.length ; i++){
			aeIds1[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeIds2 = new long[len];
		for(int i = 0 ; i < aeIds2.length ; i++){
			aeIds2[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeIds3 = new long[len];
		for(int i = 0 ; i < aeIds3.length ; i++){
			aeIds3[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeIds4 = new long[len];
		for(int i = 0 ; i < aeIds4.length ; i++){
			aeIds4[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeIds5 = new long[len];
		for(int i = 0 ; i < aeIds5.length ; i++){
			aeIds5[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeIds6 = new long[len];
		for(int i = 0 ; i < aeIds6.length ; i++){
			aeIds6[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		aeIds7 = new long[len];
		for(int i = 0 ; i < aeIds7.length ; i++){
			aeIds7[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70FFF124;
	}

	public String getTypeDescription() {
		return "LOGIN_REWARD_RES";
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
		len += states.length * 4;
		len += 4;
		for(int i = 0 ; i < names.length; i++){
			len += 2;
			try{
				len += names[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < icons.length; i++){
			len += 2;
			try{
				len += icons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += aeIds1.length * 8;
		len += 4;
		len += aeIds2.length * 8;
		len += 4;
		len += aeIds3.length * 8;
		len += 4;
		len += aeIds4.length * 8;
		len += 4;
		len += aeIds5.length * 8;
		len += 4;
		len += aeIds6.length * 8;
		len += 4;
		len += aeIds7.length * 8;
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

			buffer.putInt(states.length);
			for(int i = 0 ; i < states.length; i++){
				buffer.putInt(states[i]);
			}
			buffer.putInt(names.length);
			for(int i = 0 ; i < names.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = names[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(icons.length);
			for(int i = 0 ; i < icons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = icons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(aeIds1.length);
			for(int i = 0 ; i < aeIds1.length; i++){
				buffer.putLong(aeIds1[i]);
			}
			buffer.putInt(aeIds2.length);
			for(int i = 0 ; i < aeIds2.length; i++){
				buffer.putLong(aeIds2[i]);
			}
			buffer.putInt(aeIds3.length);
			for(int i = 0 ; i < aeIds3.length; i++){
				buffer.putLong(aeIds3[i]);
			}
			buffer.putInt(aeIds4.length);
			for(int i = 0 ; i < aeIds4.length; i++){
				buffer.putLong(aeIds4[i]);
			}
			buffer.putInt(aeIds5.length);
			for(int i = 0 ; i < aeIds5.length; i++){
				buffer.putLong(aeIds5[i]);
			}
			buffer.putInt(aeIds6.length);
			for(int i = 0 ; i < aeIds6.length; i++){
				buffer.putLong(aeIds6[i]);
			}
			buffer.putInt(aeIds7.length);
			for(int i = 0 ; i < aeIds7.length; i++){
				buffer.putLong(aeIds7[i]);
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
	 *	领取状态,0:初始值，1:登录，2:领取过奖励
	 */
	public int[] getStates(){
		return states;
	}

	/**
	 * 设置属性：
	 *	领取状态,0:初始值，1:登录，2:领取过奖励
	 */
	public void setStates(int[] states){
		this.states = states;
	}

	/**
	 * 获取属性：
	 *	礼包名字集
	 */
	public String[] getNames(){
		return names;
	}

	/**
	 * 设置属性：
	 *	礼包名字集
	 */
	public void setNames(String[] names){
		this.names = names;
	}

	/**
	 * 获取属性：
	 *	礼包名icon
	 */
	public String[] getIcons(){
		return icons;
	}

	/**
	 * 设置属性：
	 *	礼包名icon
	 */
	public void setIcons(String[] icons){
		this.icons = icons;
	}

	/**
	 * 获取属性：
	 *	礼包开出具体物品id集
	 */
	public long[] getAeIds1(){
		return aeIds1;
	}

	/**
	 * 设置属性：
	 *	礼包开出具体物品id集
	 */
	public void setAeIds1(long[] aeIds1){
		this.aeIds1 = aeIds1;
	}

	/**
	 * 获取属性：
	 *	礼包开出具体物品id集
	 */
	public long[] getAeIds2(){
		return aeIds2;
	}

	/**
	 * 设置属性：
	 *	礼包开出具体物品id集
	 */
	public void setAeIds2(long[] aeIds2){
		this.aeIds2 = aeIds2;
	}

	/**
	 * 获取属性：
	 *	礼包开出具体物品id集
	 */
	public long[] getAeIds3(){
		return aeIds3;
	}

	/**
	 * 设置属性：
	 *	礼包开出具体物品id集
	 */
	public void setAeIds3(long[] aeIds3){
		this.aeIds3 = aeIds3;
	}

	/**
	 * 获取属性：
	 *	礼包开出具体物品id集
	 */
	public long[] getAeIds4(){
		return aeIds4;
	}

	/**
	 * 设置属性：
	 *	礼包开出具体物品id集
	 */
	public void setAeIds4(long[] aeIds4){
		this.aeIds4 = aeIds4;
	}

	/**
	 * 获取属性：
	 *	礼包开出具体物品id集
	 */
	public long[] getAeIds5(){
		return aeIds5;
	}

	/**
	 * 设置属性：
	 *	礼包开出具体物品id集
	 */
	public void setAeIds5(long[] aeIds5){
		this.aeIds5 = aeIds5;
	}

	/**
	 * 获取属性：
	 *	礼包开出具体物品id集
	 */
	public long[] getAeIds6(){
		return aeIds6;
	}

	/**
	 * 设置属性：
	 *	礼包开出具体物品id集
	 */
	public void setAeIds6(long[] aeIds6){
		this.aeIds6 = aeIds6;
	}

	/**
	 * 获取属性：
	 *	礼包开出具体物品id集
	 */
	public long[] getAeIds7(){
		return aeIds7;
	}

	/**
	 * 设置属性：
	 *	礼包开出具体物品id集
	 */
	public void setAeIds7(long[] aeIds7){
		this.aeIds7 = aeIds7;
	}

}