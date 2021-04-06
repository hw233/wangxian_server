package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.SoulData;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>action</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showButton</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result</td><td>String</td><td>result.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currGrade</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currCostExp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>upgraderesult.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgraderesult</td><td>String</td><td>upgraderesult.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas.length</td><td>int</td><td>4个字节</td><td>SoulData数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[0].color.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[0].color</td><td>String</td><td>souldatas[0].color.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[0].title</td><td>String</td><td>souldatas[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[0].percent.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[0].percent</td><td>int[]</td><td>souldatas[0].percent.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[1].color.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[1].color</td><td>String</td><td>souldatas[1].color.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[1].title</td><td>String</td><td>souldatas[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[1].percent.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[1].percent</td><td>int[]</td><td>souldatas[1].percent.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[2].color.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[2].color</td><td>String</td><td>souldatas[2].color.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[2].title</td><td>String</td><td>souldatas[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souldatas[2].percent.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souldatas[2].percent</td><td>int[]</td><td>souldatas[2].percent.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SOUL_MESSAGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int action;
	int showButton;
	String result;
	int currGrade;
	long currCostExp;
	String upgraderesult;
	SoulData[] souldatas;

	public SOUL_MESSAGE_RES(){
	}

	public SOUL_MESSAGE_RES(long seqNum,int action,int showButton,String result,int currGrade,long currCostExp,String upgraderesult,SoulData[] souldatas){
		this.seqNum = seqNum;
		this.action = action;
		this.showButton = showButton;
		this.result = result;
		this.currGrade = currGrade;
		this.currCostExp = currCostExp;
		this.upgraderesult = upgraderesult;
		this.souldatas = souldatas;
	}

	public SOUL_MESSAGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		action = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showButton = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		result = new String(content,offset,len,"UTF-8");
		offset += len;
		currGrade = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currCostExp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		upgraderesult = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		souldatas = new SoulData[len];
		for(int i = 0 ; i < souldatas.length ; i++){
			souldatas[i] = new SoulData();
			souldatas[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			souldatas[i].setColor(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			souldatas[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] percent_0001 = new int[len];
			for(int j = 0 ; j < percent_0001.length ; j++){
				percent_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			souldatas[i].setPercent(percent_0001);
		}
	}

	public int getType() {
		return 0x8E0EAE03;
	}

	public String getTypeDescription() {
		return "SOUL_MESSAGE_RES";
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
		len += 2;
		try{
			len +=result.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 8;
		len += 2;
		try{
			len +=upgraderesult.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < souldatas.length ; i++){
			len += 4;
			len += 2;
			if(souldatas[i].getColor() != null){
				try{
				len += souldatas[i].getColor().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(souldatas[i].getTitle() != null){
				try{
				len += souldatas[i].getTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += souldatas[i].getPercent().length * 4;
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

			buffer.putInt(action);
			buffer.putInt(showButton);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = result.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(currGrade);
			buffer.putLong(currCostExp);
				try{
			tmpBytes1 = upgraderesult.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(souldatas.length);
			for(int i = 0 ; i < souldatas.length ; i++){
				buffer.putInt((int)souldatas[i].getLevel());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = souldatas[i].getColor().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = souldatas[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(souldatas[i].getPercent().length);
				int[] percent_0002 = souldatas[i].getPercent();
				for(int j = 0 ; j < percent_0002.length ; j++){
					buffer.putInt(percent_0002[j]);
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
	 *	0成功 1失败
	 */
	public int getAction(){
		return action;
	}

	/**
	 * 设置属性：
	 *	0成功 1失败
	 */
	public void setAction(int action){
		this.action = action;
	}

	/**
	 * 获取属性：
	 *	是否展示升级按钮，0：是 ，1：否
	 */
	public int getShowButton(){
		return showButton;
	}

	/**
	 * 设置属性：
	 *	是否展示升级按钮，0：是 ，1：否
	 */
	public void setShowButton(int showButton){
		this.showButton = showButton;
	}

	/**
	 * 获取属性：
	 *	成功失败原因,用户界面信息显示
	 */
	public String getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	成功失败原因,用户界面信息显示
	 */
	public void setResult(String result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	当前属性等级
	 */
	public int getCurrGrade(){
		return currGrade;
	}

	/**
	 * 设置属性：
	 *	当前属性等级
	 */
	public void setCurrGrade(int currGrade){
		this.currGrade = currGrade;
	}

	/**
	 * 获取属性：
	 *	下一级升级所需经验
	 */
	public long getCurrCostExp(){
		return currCostExp;
	}

	/**
	 * 设置属性：
	 *	下一级升级所需经验
	 */
	public void setCurrCostExp(long currCostExp){
		this.currCostExp = currCostExp;
	}

	/**
	 * 获取属性：
	 *	不能升级原因
	 */
	public String getUpgraderesult(){
		return upgraderesult;
	}

	/**
	 * 设置属性：
	 *	不能升级原因
	 */
	public void setUpgraderesult(String upgraderesult){
		this.upgraderesult = upgraderesult;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public SoulData[] getSouldatas(){
		return souldatas;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setSouldatas(SoulData[] souldatas){
		this.souldatas = souldatas;
	}

}