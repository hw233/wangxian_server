package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开挖坟活动界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fenmuType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fenMuNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fenMuNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fenMuNames[0]</td><td>String</td><td>fenMuNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fenMuNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fenMuNames[1]</td><td>String</td><td>fenMuNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fenMuNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fenMuNames[2]</td><td>String</td><td>fenMuNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentFenMuIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentFenMu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentFenMu</td><td>String</td><td>currentFenMu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lineWeigth</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lineHeigth</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description</td><td>String</td><td>description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftYinChanzi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftJinChanzi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftLiuliChanzi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chanziType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chanziType</td><td>int[]</td><td>chanziType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fendiModel.length</td><td>int</td><td>4个字节</td><td>FenDiModel4Client数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fendiModel[0].fendiIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fendiModel[0].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fendiModel[0].receiveType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fendiModel[1].fendiIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fendiModel[1].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fendiModel[1].receiveType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fendiModel[2].fendiIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fendiModel[2].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fendiModel[2].receiveType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class OPEN_WAFEN_ACTIVITY_WINDOW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte fenmuType;
	String[] fenMuNames;
	int currentFenMuIndex;
	String currentFenMu;
	int lineWeigth;
	int lineHeigth;
	String description;
	int leftYinChanzi;
	int leftJinChanzi;
	int leftLiuliChanzi;
	int[] chanziType;
	FenDiModel4Client[] fendiModel;

	public OPEN_WAFEN_ACTIVITY_WINDOW_RES(){
	}

	public OPEN_WAFEN_ACTIVITY_WINDOW_RES(long seqNum,byte fenmuType,String[] fenMuNames,int currentFenMuIndex,String currentFenMu,int lineWeigth,int lineHeigth,String description,int leftYinChanzi,int leftJinChanzi,int leftLiuliChanzi,int[] chanziType,FenDiModel4Client[] fendiModel){
		this.seqNum = seqNum;
		this.fenmuType = fenmuType;
		this.fenMuNames = fenMuNames;
		this.currentFenMuIndex = currentFenMuIndex;
		this.currentFenMu = currentFenMu;
		this.lineWeigth = lineWeigth;
		this.lineHeigth = lineHeigth;
		this.description = description;
		this.leftYinChanzi = leftYinChanzi;
		this.leftJinChanzi = leftJinChanzi;
		this.leftLiuliChanzi = leftLiuliChanzi;
		this.chanziType = chanziType;
		this.fendiModel = fendiModel;
	}

	public OPEN_WAFEN_ACTIVITY_WINDOW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		fenmuType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		fenMuNames = new String[len];
		for(int i = 0 ; i < fenMuNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			fenMuNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		currentFenMuIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		currentFenMu = new String(content,offset,len,"UTF-8");
		offset += len;
		lineWeigth = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		lineHeigth = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		description = new String(content,offset,len,"UTF-8");
		offset += len;
		leftYinChanzi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		leftJinChanzi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		leftLiuliChanzi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		chanziType = new int[len];
		for(int i = 0 ; i < chanziType.length ; i++){
			chanziType[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		fendiModel = new FenDiModel4Client[len];
		for(int i = 0 ; i < fendiModel.length ; i++){
			fendiModel[i] = new FenDiModel4Client();
			fendiModel[i].setFendiIndex((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			fendiModel[i].setArticleId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			fendiModel[i].setReceiveType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
	}

	public int getType() {
		return 0x70F0EF78;
	}

	public String getTypeDescription() {
		return "OPEN_WAFEN_ACTIVITY_WINDOW_RES";
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
		len += 1;
		len += 4;
		for(int i = 0 ; i < fenMuNames.length; i++){
			len += 2;
			try{
				len += fenMuNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 2;
		try{
			len +=currentFenMu.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=description.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += chanziType.length * 4;
		len += 4;
		for(int i = 0 ; i < fendiModel.length ; i++){
			len += 4;
			len += 8;
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

			buffer.put(fenmuType);
			buffer.putInt(fenMuNames.length);
			for(int i = 0 ; i < fenMuNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = fenMuNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(currentFenMuIndex);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = currentFenMu.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(lineWeigth);
			buffer.putInt(lineHeigth);
				try{
			tmpBytes1 = description.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(leftYinChanzi);
			buffer.putInt(leftJinChanzi);
			buffer.putInt(leftLiuliChanzi);
			buffer.putInt(chanziType.length);
			for(int i = 0 ; i < chanziType.length; i++){
				buffer.putInt(chanziType[i]);
			}
			buffer.putInt(fendiModel.length);
			for(int i = 0 ; i < fendiModel.length ; i++){
				buffer.putInt((int)fendiModel[i].getFendiIndex());
				buffer.putLong(fendiModel[i].getArticleId());
				buffer.put((byte)fendiModel[i].getReceiveType());
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
	 *	0为私有  1为全服共有
	 */
	public byte getFenmuType(){
		return fenmuType;
	}

	/**
	 * 设置属性：
	 *	0为私有  1为全服共有
	 */
	public void setFenmuType(byte fenmuType){
		this.fenmuType = fenmuType;
	}

	/**
	 * 获取属性：
	 *	所有坟墓名(顺序)
	 */
	public String[] getFenMuNames(){
		return fenMuNames;
	}

	/**
	 * 设置属性：
	 *	所有坟墓名(顺序)
	 */
	public void setFenMuNames(String[] fenMuNames){
		this.fenMuNames = fenMuNames;
	}

	/**
	 * 获取属性：
	 *	当前坟墓索引
	 */
	public int getCurrentFenMuIndex(){
		return currentFenMuIndex;
	}

	/**
	 * 设置属性：
	 *	当前坟墓索引
	 */
	public void setCurrentFenMuIndex(int currentFenMuIndex){
		this.currentFenMuIndex = currentFenMuIndex;
	}

	/**
	 * 获取属性：
	 *	当前应该挖取的坟墓
	 */
	public String getCurrentFenMu(){
		return currentFenMu;
	}

	/**
	 * 设置属性：
	 *	当前应该挖取的坟墓
	 */
	public void setCurrentFenMu(String currentFenMu){
		this.currentFenMu = currentFenMu;
	}

	/**
	 * 获取属性：
	 *	行宽
	 */
	public int getLineWeigth(){
		return lineWeigth;
	}

	/**
	 * 设置属性：
	 *	行宽
	 */
	public void setLineWeigth(int lineWeigth){
		this.lineWeigth = lineWeigth;
	}

	/**
	 * 获取属性：
	 *	列高
	 */
	public int getLineHeigth(){
		return lineHeigth;
	}

	/**
	 * 设置属性：
	 *	列高
	 */
	public void setLineHeigth(int lineHeigth){
		this.lineHeigth = lineHeigth;
	}

	/**
	 * 获取属性：
	 *	描述--用不用再看
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	描述--用不用再看
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	银铲子剩余
	 */
	public int getLeftYinChanzi(){
		return leftYinChanzi;
	}

	/**
	 * 设置属性：
	 *	银铲子剩余
	 */
	public void setLeftYinChanzi(int leftYinChanzi){
		this.leftYinChanzi = leftYinChanzi;
	}

	/**
	 * 获取属性：
	 *	金铲子剩余
	 */
	public int getLeftJinChanzi(){
		return leftJinChanzi;
	}

	/**
	 * 设置属性：
	 *	金铲子剩余
	 */
	public void setLeftJinChanzi(int leftJinChanzi){
		this.leftJinChanzi = leftJinChanzi;
	}

	/**
	 * 获取属性：
	 *	琉璃铲子剩余
	 */
	public int getLeftLiuliChanzi(){
		return leftLiuliChanzi;
	}

	/**
	 * 设置属性：
	 *	琉璃铲子剩余
	 */
	public void setLeftLiuliChanzi(int leftLiuliChanzi){
		this.leftLiuliChanzi = leftLiuliChanzi;
	}

	/**
	 * 获取属性：
	 *	当前坟墓可使用的铲子类型
	 */
	public int[] getChanziType(){
		return chanziType;
	}

	/**
	 * 设置属性：
	 *	当前坟墓可使用的铲子类型
	 */
	public void setChanziType(int[] chanziType){
		this.chanziType = chanziType;
	}

	/**
	 * 获取属性：
	 *	当前坟墓相关信息
	 */
	public FenDiModel4Client[] getFendiModel(){
		return fendiModel;
	}

	/**
	 * 设置属性：
	 *	当前坟墓相关信息
	 */
	public void setFendiModel(FenDiModel4Client[] fendiModel){
		this.fendiModel = fendiModel;
	}

}