package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 观察工具向服务器发送观察某个地图的请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>world.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>world</td><td>String</td><td>world.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>game.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>game</td><td>String</td><td>game.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>x</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>y</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>viewWidth</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>viewHeight</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class OBSERVER_REGISTER_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String world;
	String game;
	int x;
	int y;
	int viewWidth;
	int viewHeight;

	public OBSERVER_REGISTER_REQ(long seqNum,String world,String game,int x,int y,int viewWidth,int viewHeight){
		this.seqNum = seqNum;
		this.world = world;
		this.game = game;
		this.x = x;
		this.y = y;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	}

	public OBSERVER_REGISTER_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024) throw new Exception("string length ["+len+"] big than the max length [1024]");
		world = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024) throw new Exception("string length ["+len+"] big than the max length [1024]");
		game = new String(content,offset,len);
		offset += len;
		x = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		y = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		viewWidth = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		viewHeight = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00000113;
	}

	public String getTypeDescription() {
		return "OBSERVER_REGISTER_REQ";
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
		len +=world.getBytes().length;
		len += 2;
		len +=game.getBytes().length;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			byte[] tmpBytes1 = world.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = game.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(x);
			buffer.putInt(y);
			buffer.putInt(viewWidth);
			buffer.putInt(viewHeight);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :" + e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	分区的编号
	 */
	public String getWorld(){
		return world;
	}

	/**
	 * 设置属性：
	 *	分区的编号
	 */
	public void setWorld(String world){
		this.world = world;
	}

	/**
	 * 获取属性：
	 *	地图的编号
	 */
	public String getGame(){
		return game;
	}

	/**
	 * 设置属性：
	 *	地图的编号
	 */
	public void setGame(String game){
		this.game = game;
	}

	/**
	 * 获取属性：
	 *	观察者所在的位置
	 */
	public int getX(){
		return x;
	}

	/**
	 * 设置属性：
	 *	观察者所在的位置
	 */
	public void setX(int x){
		this.x = x;
	}

	/**
	 * 获取属性：
	 *	观察者所在的位置
	 */
	public int getY(){
		return y;
	}

	/**
	 * 设置属性：
	 *	观察者所在的位置
	 */
	public void setY(int y){
		this.y = y;
	}

	/**
	 * 获取属性：
	 *	窗口的宽度
	 */
	public int getViewWidth(){
		return viewWidth;
	}

	/**
	 * 设置属性：
	 *	窗口的宽度
	 */
	public void setViewWidth(int viewWidth){
		this.viewWidth = viewWidth;
	}

	/**
	 * 获取属性：
	 *	窗口的高度
	 */
	public int getViewHeight(){
		return viewHeight;
	}

	/**
	 * 设置属性：
	 *	窗口的高度
	 */
	public void setViewHeight(int viewHeight){
		this.viewHeight = viewHeight;
	}

}
