package com.sqage.stat.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.sqage.stat.model.Transaction_SpecialFlow;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 特殊交易日志<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.createDate</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fenQu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fenQu</td><td>String</td><td>transaction_SpecialFlow.fenQu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.transactionType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.transactionType</td><td>String</td><td>transaction_SpecialFlow.transactionType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fuuId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fuuId</td><td>String</td><td>transaction_SpecialFlow.fuuId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fuserName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fuserName</td><td>String</td><td>transaction_SpecialFlow.fuserName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fplayerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fplayerName</td><td>String</td><td>transaction_SpecialFlow.fplayerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fquDao.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fquDao</td><td>String</td><td>transaction_SpecialFlow.fquDao.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fdaoJuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fdaoJuName</td><td>String</td><td>transaction_SpecialFlow.fdaoJuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fyinzi</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fjiazhi</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fregisttime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fcreatPlayerTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.flevel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.flevel</td><td>String</td><td>transaction_SpecialFlow.flevel.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.fvip.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.fvip</td><td>String</td><td>transaction_SpecialFlow.fvip.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.ftotalMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.toRegisttime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.toCreatPlayerTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.toLevel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.toLevel</td><td>String</td><td>transaction_SpecialFlow.toLevel.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.toVip.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.toVip</td><td>String</td><td>transaction_SpecialFlow.toVip.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.toTotalMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.touuId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.touuId</td><td>String</td><td>transaction_SpecialFlow.touuId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.toUserName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.toUserName</td><td>String</td><td>transaction_SpecialFlow.toUserName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.toPlayername.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.toPlayername</td><td>String</td><td>transaction_SpecialFlow.toPlayername.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.toquDao.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.toquDao</td><td>String</td><td>transaction_SpecialFlow.toquDao.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.todaoJuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.todaoJuName</td><td>String</td><td>transaction_SpecialFlow.todaoJuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>transaction_SpecialFlow.toyinzi</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>transaction_SpecialFlow.tojiazhi</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class TRANSACTION_SPECIAL_LOG_REQ implements RequestMessage{

	static StatMessageFactory mf = StatMessageFactory.getInstance();

	long seqNum;
	Transaction_SpecialFlow transaction_SpecialFlow;

	public TRANSACTION_SPECIAL_LOG_REQ(long seqNum,Transaction_SpecialFlow transaction_SpecialFlow){
		this.seqNum = seqNum;
		this.transaction_SpecialFlow = transaction_SpecialFlow;
	}

	public TRANSACTION_SPECIAL_LOG_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		transaction_SpecialFlow = new Transaction_SpecialFlow();
		transaction_SpecialFlow.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		transaction_SpecialFlow.setCreateDate((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setFenQu(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setTransactionType(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setFuuId(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setFuserName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setFplayerName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setFquDao(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setFdaoJuName(new String(content,offset,len,"UTF-8"));
		offset += len;
		transaction_SpecialFlow.setFyinzi((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		transaction_SpecialFlow.setFjiazhi((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		transaction_SpecialFlow.setFregisttime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		transaction_SpecialFlow.setFcreatPlayerTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setFlevel(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setFvip(new String(content,offset,len,"UTF-8"));
		offset += len;
		transaction_SpecialFlow.setFtotalMoney((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		transaction_SpecialFlow.setToRegisttime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		transaction_SpecialFlow.setToCreatPlayerTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setToLevel(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setToVip(new String(content,offset,len,"UTF-8"));
		offset += len;
		transaction_SpecialFlow.setToTotalMoney((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setTouuId(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setToUserName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setToPlayername(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setToquDao(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		transaction_SpecialFlow.setTodaoJuName(new String(content,offset,len,"UTF-8"));
		offset += len;
		transaction_SpecialFlow.setToyinzi((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		transaction_SpecialFlow.setTojiazhi((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
	}

	public int getType() {
		return 0x00000016;
	}

	public String getTypeDescription() {
		return "TRANSACTION_SPECIAL_LOG_REQ";
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
		len += 8;
		len += 8;
		len += 2;
		if(transaction_SpecialFlow.getFenQu() != null){
			try{
			len += transaction_SpecialFlow.getFenQu().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getTransactionType() != null){
			try{
			len += transaction_SpecialFlow.getTransactionType().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getFuuId() != null){
			try{
			len += transaction_SpecialFlow.getFuuId().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getFuserName() != null){
			try{
			len += transaction_SpecialFlow.getFuserName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getFplayerName() != null){
			try{
			len += transaction_SpecialFlow.getFplayerName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getFquDao() != null){
			try{
			len += transaction_SpecialFlow.getFquDao().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getFdaoJuName() != null){
			try{
			len += transaction_SpecialFlow.getFdaoJuName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 8;
		len += 8;
		len += 8;
		len += 2;
		if(transaction_SpecialFlow.getFlevel() != null){
			try{
			len += transaction_SpecialFlow.getFlevel().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getFvip() != null){
			try{
			len += transaction_SpecialFlow.getFvip().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 8;
		len += 8;
		len += 2;
		if(transaction_SpecialFlow.getToLevel() != null){
			try{
			len += transaction_SpecialFlow.getToLevel().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getToVip() != null){
			try{
			len += transaction_SpecialFlow.getToVip().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 2;
		if(transaction_SpecialFlow.getTouuId() != null){
			try{
			len += transaction_SpecialFlow.getTouuId().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getToUserName() != null){
			try{
			len += transaction_SpecialFlow.getToUserName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getToPlayername() != null){
			try{
			len += transaction_SpecialFlow.getToPlayername().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getToquDao() != null){
			try{
			len += transaction_SpecialFlow.getToquDao().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(transaction_SpecialFlow.getTodaoJuName() != null){
			try{
			len += transaction_SpecialFlow.getTodaoJuName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 8;
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

			buffer.putLong(transaction_SpecialFlow.getId());
			buffer.putLong(transaction_SpecialFlow.getCreateDate());
			byte[] tmpBytes1 = transaction_SpecialFlow.getFenQu().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getTransactionType().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getFuuId().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getFuserName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getFplayerName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getFquDao().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getFdaoJuName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(transaction_SpecialFlow.getFyinzi());
			buffer.putLong(transaction_SpecialFlow.getFjiazhi());
			buffer.putLong(transaction_SpecialFlow.getFregisttime());
			buffer.putLong(transaction_SpecialFlow.getFcreatPlayerTime());
			tmpBytes1 = transaction_SpecialFlow.getFlevel().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getFvip().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(transaction_SpecialFlow.getFtotalMoney());
			buffer.putLong(transaction_SpecialFlow.getToRegisttime());
			buffer.putLong(transaction_SpecialFlow.getToCreatPlayerTime());
			tmpBytes1 = transaction_SpecialFlow.getToLevel().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getToVip().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(transaction_SpecialFlow.getToTotalMoney());
			tmpBytes1 = transaction_SpecialFlow.getTouuId().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getToUserName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getToPlayername().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getToquDao().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = transaction_SpecialFlow.getTodaoJuName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(transaction_SpecialFlow.getToyinzi());
			buffer.putLong(transaction_SpecialFlow.getTojiazhi());
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	特殊交易日志
	 */
	public Transaction_SpecialFlow getTransaction_SpecialFlow(){
		return transaction_SpecialFlow;
	}

	/**
	 * 设置属性：
	 *	特殊交易日志
	 */
	public void setTransaction_SpecialFlow(Transaction_SpecialFlow transaction_SpecialFlow){
		this.transaction_SpecialFlow = transaction_SpecialFlow;
	}

}