package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开限时任务界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>timedTaskMonster.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>timedTaskMonster</td><td>String</td><td>timedTaskMonster.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterName</td><td>String</td><td>monsterName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[0]</td><td>String</td><td>avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[1]</td><td>String</td><td>avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[2]</td><td>String</td><td>avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterColor</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>scale</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>x</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>y</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isBoss</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tip.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tip</td><td>String</td><td>tip.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des</td><td>String</td><td>des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>score</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempId</td><td>long[]</td><td>tempId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>num.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>num</td><td>int[]</td><td>num.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bind.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bind</td><td>boolean[]</td><td>bind.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>state</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>takePrize</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cost</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class XL_TIMEDTASK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int taskId;
	String timedTaskMonster;
	String monsterName;
	String[] avata;
	int monsterColor;
	int scale;
	int x;
	int y;
	boolean isBoss;
	String tip;
	String des;
	int score;
	long[] tempId;
	int[] num;
	boolean[] bind;
	long leftTime;
	byte state;
	boolean takePrize;
	long cost;

	public XL_TIMEDTASK_RES(){
	}

	public XL_TIMEDTASK_RES(long seqNum,int taskId,String timedTaskMonster,String monsterName,String[] avata,int monsterColor,int scale,int x,int y,boolean isBoss,String tip,String des,int score,long[] tempId,int[] num,boolean[] bind,long leftTime,byte state,boolean takePrize,long cost){
		this.seqNum = seqNum;
		this.taskId = taskId;
		this.timedTaskMonster = timedTaskMonster;
		this.monsterName = monsterName;
		this.avata = avata;
		this.monsterColor = monsterColor;
		this.scale = scale;
		this.x = x;
		this.y = y;
		this.isBoss = isBoss;
		this.tip = tip;
		this.des = des;
		this.score = score;
		this.tempId = tempId;
		this.num = num;
		this.bind = bind;
		this.leftTime = leftTime;
		this.state = state;
		this.takePrize = takePrize;
		this.cost = cost;
	}

	public XL_TIMEDTASK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		taskId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		timedTaskMonster = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		monsterName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avata = new String[len];
		for(int i = 0 ; i < avata.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			avata[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		monsterColor = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		scale = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		x = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		y = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		isBoss = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		tip = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		des = new String(content,offset,len,"UTF-8");
		offset += len;
		score = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		tempId = new long[len];
		for(int i = 0 ; i < tempId.length ; i++){
			tempId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		num = new int[len];
		for(int i = 0 ; i < num.length ; i++){
			num[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bind = new boolean[len];
		for(int i = 0 ; i < bind.length ; i++){
			bind[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		leftTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		state = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		takePrize = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		cost = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70FFF076;
	}

	public String getTypeDescription() {
		return "XL_TIMEDTASK_RES";
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
		len += 2;
		try{
			len +=timedTaskMonster.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=monsterName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < avata.length; i++){
			len += 2;
			try{
				len += avata[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 1;
		len += 2;
		try{
			len +=tip.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=des.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += tempId.length * 8;
		len += 4;
		len += num.length * 4;
		len += 4;
		len += bind.length;
		len += 8;
		len += 1;
		len += 1;
		len += 8;
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

			buffer.putInt(taskId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = timedTaskMonster.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = monsterName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(avata.length);
			for(int i = 0 ; i < avata.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = avata[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(monsterColor);
			buffer.putInt(scale);
			buffer.putInt(x);
			buffer.putInt(y);
			buffer.put((byte)(isBoss==false?0:1));
				try{
			tmpBytes1 = tip.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = des.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(score);
			buffer.putInt(tempId.length);
			for(int i = 0 ; i < tempId.length; i++){
				buffer.putLong(tempId[i]);
			}
			buffer.putInt(num.length);
			for(int i = 0 ; i < num.length; i++){
				buffer.putInt(num[i]);
			}
			buffer.putInt(bind.length);
			for(int i = 0 ; i < bind.length; i++){
				buffer.put((byte)(bind[i]==false?0:1));
			}
			buffer.putLong(leftTime);
			buffer.put(state);
			buffer.put((byte)(takePrize==false?0:1));
			buffer.putLong(cost);
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
	 *	限时任务id
	 */
	public int getTaskId(){
		return taskId;
	}

	/**
	 * 设置属性：
	 *	限时任务id
	 */
	public void setTaskId(int taskId){
		this.taskId = taskId;
	}

	/**
	 * 获取属性：
	 *	限时任务怪图标
	 */
	public String getTimedTaskMonster(){
		return timedTaskMonster;
	}

	/**
	 * 设置属性：
	 *	限时任务怪图标
	 */
	public void setTimedTaskMonster(String timedTaskMonster){
		this.timedTaskMonster = timedTaskMonster;
	}

	/**
	 * 获取属性：
	 *	怪物名
	 */
	public String getMonsterName(){
		return monsterName;
	}

	/**
	 * 设置属性：
	 *	怪物名
	 */
	public void setMonsterName(String monsterName){
		this.monsterName = monsterName;
	}

	/**
	 * 获取属性：
	 *	怪物avata
	 */
	public String[] getAvata(){
		return avata;
	}

	/**
	 * 设置属性：
	 *	怪物avata
	 */
	public void setAvata(String[] avata){
		this.avata = avata;
	}

	/**
	 * 获取属性：
	 *	怪物品质
	 */
	public int getMonsterColor(){
		return monsterColor;
	}

	/**
	 * 设置属性：
	 *	怪物品质
	 */
	public void setMonsterColor(int monsterColor){
		this.monsterColor = monsterColor;
	}

	/**
	 * 获取属性：
	 *	怪物形象比例
	 */
	public int getScale(){
		return scale;
	}

	/**
	 * 设置属性：
	 *	怪物形象比例
	 */
	public void setScale(int scale){
		this.scale = scale;
	}

	/**
	 * 获取属性：
	 *	怪物坐标x
	 */
	public int getX(){
		return x;
	}

	/**
	 * 设置属性：
	 *	怪物坐标x
	 */
	public void setX(int x){
		this.x = x;
	}

	/**
	 * 获取属性：
	 *	怪物坐标y
	 */
	public int getY(){
		return y;
	}

	/**
	 * 设置属性：
	 *	怪物坐标y
	 */
	public void setY(int y){
		this.y = y;
	}

	/**
	 * 获取属性：
	 *	是否boss
	 */
	public boolean getIsBoss(){
		return isBoss;
	}

	/**
	 * 设置属性：
	 *	是否boss
	 */
	public void setIsBoss(boolean isBoss){
		this.isBoss = isBoss;
	}

	/**
	 * 获取属性：
	 *	提示
	 */
	public String getTip(){
		return tip;
	}

	/**
	 * 设置属性：
	 *	提示
	 */
	public void setTip(String tip){
		this.tip = tip;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String getDes(){
		return des;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDes(String des){
		this.des = des;
	}

	/**
	 * 获取属性：
	 *	奖励积分
	 */
	public int getScore(){
		return score;
	}

	/**
	 * 设置属性：
	 *	奖励积分
	 */
	public void setScore(int score){
		this.score = score;
	}

	/**
	 * 获取属性：
	 *	奖励道具
	 */
	public long[] getTempId(){
		return tempId;
	}

	/**
	 * 设置属性：
	 *	奖励道具
	 */
	public void setTempId(long[] tempId){
		this.tempId = tempId;
	}

	/**
	 * 获取属性：
	 *	奖励道具数量
	 */
	public int[] getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	奖励道具数量
	 */
	public void setNum(int[] num){
		this.num = num;
	}

	/**
	 * 获取属性：
	 *	奖励道具是否绑定
	 */
	public boolean[] getBind(){
		return bind;
	}

	/**
	 * 设置属性：
	 *	奖励道具是否绑定
	 */
	public void setBind(boolean[] bind){
		this.bind = bind;
	}

	/**
	 * 获取属性：
	 *	倒计时
	 */
	public long getLeftTime(){
		return leftTime;
	}

	/**
	 * 设置属性：
	 *	倒计时
	 */
	public void setLeftTime(long leftTime){
		this.leftTime = leftTime;
	}

	/**
	 * 获取属性：
	 *	任务状态:0-未接取；1-已接取未完成；2-已完成；3-超时失效
	 */
	public byte getState(){
		return state;
	}

	/**
	 * 设置属性：
	 *	任务状态:0-未接取；1-已接取未完成；2-已完成；3-超时失效
	 */
	public void setState(byte state){
		this.state = state;
	}

	/**
	 * 获取属性：
	 *	是否已领奖
	 */
	public boolean getTakePrize(){
		return takePrize;
	}

	/**
	 * 设置属性：
	 *	是否已领奖
	 */
	public void setTakePrize(boolean takePrize){
		this.takePrize = takePrize;
	}

	/**
	 * 获取属性：
	 *	消耗银子
	 */
	public long getCost(){
		return cost;
	}

	/**
	 * 设置属性：
	 *	消耗银子
	 */
	public void setCost(long cost){
		this.cost = cost;
	}

}