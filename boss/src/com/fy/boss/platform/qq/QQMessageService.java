package com.fy.boss.platform.qq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.platform.qq.message.LoginNotifyREQ;
import com.fy.boss.platform.qq.message.LoginNotifyRES;
import com.fy.boss.platform.qq.message.QQMessage;
import com.fy.boss.platform.qq.message.QQMessageFactory;
import com.fy.boss.platform.qq.message.QQ_ORIGINAL_REQ;
import com.fy.boss.platform.qq.message.QQ_ORIGINAL_RES;
import com.fy.boss.platform.qq.message.QQ_SAVING_RESULT_REQ;
import com.fy.boss.platform.qq.message.QQ_SAVING_RESULT_RES;
import com.fy.boss.platform.qq.DES3Secret;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.platform.qq.QQMessageService;
import com.fy.boss.platform.qq.QQUser;
import com.fy.boss.platform.qq.QQUserCenter;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 
 */
public class QQMessageService implements ConnectionConnectedHandler, MessageHandler {

	public static Logger logger = Logger.getLogger(QQMessageService.class);

	protected QQMessageFactory mf = QQMessageFactory.getInstance();

	protected int maxWindowSize = 32;
	protected long waitingResponseMessageTimeout = 1800 * 1000L;
	protected long waitingRequestMessageTimeout = 1800 * 1000L;
	protected int maxReSendTimes = 2;
	protected long sendBufferSize = 5 * 512 * 1024L;
	protected long receiveBufferSize = 5 * 512 * 1024L;

	private String host = "";

	private int port = 9301;

	private DES3Secret des;

	private String key1 = "95379885";

	private String key2 = "85370983";

	private String key3 = "85370988";

	DefaultConnectionSelector selector;
	
	public void init() {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		des = new DES3Secret(this.key1, this.key2, this.key3);
		selector = new com.xuanzhi.tools.transport.DefaultConnectionSelector();
		selector.setEnableHeapForTimeout(true);
		selector.setMultiServerModel(false);
		selector.setName("QQAuthNotifyServiceSelector");
		selector.setClientModel(false);
		selector.setPort(port);
		selector.setHost(host);
		selector.setMaxIdelTime(24*60*60*1000);
		selector.setConnectionConnectedHandler(this);
		try {
			selector.init();
		} catch (Exception e) {
			System.out.println("=================打印异常信息:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("[腾讯消息接收服务初始化失败]", e);
		}
		if (logger.isInfoEnabled()) {
			logger.info("[腾讯消息接收服务初始化成功] [" + (System.currentTimeMillis() - start) + "ms]");
		}
		System.out.println("[腾讯消息接收服务初始化成功] [" + (System.currentTimeMillis() - start) + "ms]");
	}

	@Override
	public void connected(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		logger.info("[有新的连接] [Connection connected] [" + conn.getName() + "] [" + conn.getRemoteAddress() + "]");
		
		//需要加ip验证
		conn.setMaxWindowSize(maxWindowSize);
		conn.setMaxReSendTimes(maxReSendTimes);
		conn.setSendBufferSize((int) sendBufferSize);
		conn.setReceiveBufferSize((int) receiveBufferSize);
		conn.setWaitingRequestMessageTimeout(waitingRequestMessageTimeout);
		conn.setWaitingResponseMessageTimeout(waitingResponseMessageTimeout);
		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	private QQMessage constructReqeustMessage(byte[] data) throws Exception {
		long startTime = System.currentTimeMillis();
		int offset = 0;
		int length = (int) mf.byteArrayToNumber(data, offset, 4);
		offset += 4;
		int version = (int) mf.byteArrayToNumber(data, offset, 1);
		offset += 1;
		int seqNo = (int) mf.byteArrayToNumber(data, offset, 4);
		offset += 4;
		int cmd = (int) mf.byteArrayToNumber(data, offset, 1);
		offset += 1;
		if (cmd == QQMessage.登录通知) {
			int uidL = (int) mf.byteArrayToNumber(data, offset, 1);
			offset += 1;
			if (uidL < 0) {
				logger.warn("[腾讯消息] [解析失败：uid长度错误] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [uid长度："+uidL+"]");
				return null;
			}
			String uid = new String(data, offset, uidL, "utf-8");
			offset += uidL;
			int sidL = (int) mf.byteArrayToNumber(data, offset, 1);
			offset += 1;
			if (sidL < 0) {
				logger.warn("[腾讯消息] [解析失败：sid长度错误] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [uid长度："+uidL+"]");
				return null;
			}
			String sid = new String(data, offset, sidL, "utf-8");
			offset += sidL;
			int loginTime = (int) mf.byteArrayToNumber(data, offset, 4);
			offset += 4;
			int reserve = (int) mf.byteArrayToNumber(data, offset, 4);
			if (logger.isInfoEnabled()) {
				logger.info("[腾讯消息] [解析成功] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [uid长度："+uidL+"] [uid："+uid+"] [sid长度："+sidL+"] [sid："+sid+"] [登录时间："+loginTime+"] [reserve："+reserve+"]");

			}
			LoginNotifyREQ req = new LoginNotifyREQ(version, seqNo, cmd, uid, sid, loginTime, reserve);
			return req;
		} else if(cmd == QQMessage.Q币支付通知 ) {
			//重新置位
			offset = 10;
			int uidL = (int) mf.byteArrayToNumber(data, offset, 1);
			offset += 1;
			if (uidL < 0) {
				logger.warn("[腾讯Q币支付通知消息] [解析失败：uid长度错误] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [uid长度："+uidL+"]");
				return null;
			}
			String uid = new String(data, offset, uidL, "utf-8");
			offset += uidL;
			int linkidL = (int) mf.byteArrayToNumber(data, offset, 1);
			offset += 1;
			if (linkidL < 0) {
				logger.warn("[腾讯Q币支付通知消息] [解析失败：linkid长度错误] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [linkid长度："+linkidL+"]");
				return null;
			}
			String linkid = new String(data, offset, linkidL, "utf-8");
			offset += linkidL;
			int buyId  = (int) mf.byteArrayToNumber(data, offset, 4);
			offset += 4;
			short goodsId = (short) mf.byteArrayToNumber(data, offset, 2);
			offset += 2;
			int goodsCount = (int)mf.byteArrayToNumber(data, offset, 4);
			offset += 4;
			int buyTime = (int)mf.byteArrayToNumber(data, offset, 4);
			
			if (logger.isInfoEnabled()) {
				logger.info("[腾讯Q币支付通知消息] [解析成功] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [uid长度："+uidL+"] [uid："+uid+"] [linkId长度："+linkidL+"] [linkid："+linkid+"] [buyid："+buyId+"] [goodsId："+goodsId+"]" +
						" [goodsCount:"+goodsCount+"] [buyTime:"+buyTime+"] [costs:"+(System.currentTimeMillis() - startTime)+"]");

			}
			//生成充值结果通知请求
			QQ_SAVING_RESULT_REQ req = new QQ_SAVING_RESULT_REQ(version, seqNo, cmd, uid, linkid, buyId, goodsId, goodsCount, buyTime);
			return req;
			
		} else if(cmd == QQMessage.神州付支付通知) {
			//重新置位
			offset = 10;
			int uidL = (int) mf.byteArrayToNumber(data, offset, 1);
			offset += 1;
			if (uidL < 0) {
				logger.warn("[腾讯神州付支付通知消息] [解析失败：uid长度错误] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [uid长度："+uidL+"]");
				return null;
			}
			String uid = new String(data, offset, uidL, "utf-8");
			offset += uidL;
			int linkidL = (int) mf.byteArrayToNumber(data, offset, 1);
			offset += 1;
			if (linkidL < 0) {
				logger.warn("[腾讯神州付支付通知消息] [解析失败：linkid长度错误] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [linkid长度："+linkidL+"]");
				return null;
			}
			String linkid = new String(data, offset, linkidL, "utf-8");
			offset += linkidL;
			int buyId  = (int) mf.byteArrayToNumber(data, offset, 4);
			offset += 4;
			short goodsId = (short) mf.byteArrayToNumber(data, offset, 2);
			offset += 2;
			int goodsCount = (int)mf.byteArrayToNumber(data, offset, 4);
			offset += 4;
			int buyTime = (int)mf.byteArrayToNumber(data, offset, 4);
			
			if (logger.isInfoEnabled()) {
				logger.info("[腾讯神州付支付通知消息] [解析成功] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"] [uid长度："+uidL+"] [uid："+uid+"] [linkId长度："+linkidL+"] [linkid："+linkid+"] [buyid："+buyId+"] [goodsId："+goodsId+"]" +
						" [goodsCount:"+goodsCount+"] [buyTime:"+buyTime+"] [costs:"+(System.currentTimeMillis() - startTime)+"]");

			}
			//生成充值结果通知请求
			QQ_SAVING_RESULT_REQ req = new QQ_SAVING_RESULT_REQ(version, seqNo, cmd, uid, linkid, buyId, goodsId, goodsCount, buyTime);
			return req;
		}
		
		logger.warn("[腾讯消息] [解析失败：未知类型] [类型："+cmd+"] [版本："+version+"] [序列号："+seqNo+"]");
		return null;
	}
	
	/**
	 * @param message
	 * @return
	 */
	public ResponseMessage constructResponseMessage(QQMessage message) {
		long startTime = System.currentTimeMillis();
		if(message instanceof LoginNotifyRES) {
			LoginNotifyRES res = (LoginNotifyRES)message;
			try{
				byte[] versionData=mf.numberToByteArray(res.getVer(), 1);
				byte[] seqNoData=mf.numberToByteArray(res.getSeqNum(), 4);
				byte[] cmdData=mf.numberToByteArray(res.getCmd(), 1);
				byte[] statusData=mf.numberToByteArray(res.getState(), 1);
				int len=versionData.length+seqNoData.length+cmdData.length+statusData.length+4;
				byte[] lenData=mf.numberToByteArray(len, 4);
				byte[] data=new byte[len];
				System.arraycopy(lenData, 0, data, 0, lenData.length);
				System.arraycopy(versionData, 0, data, lenData.length, versionData.length);
				System.arraycopy(seqNoData, 0, data, lenData.length+versionData.length, seqNoData.length);
				System.arraycopy(cmdData, 0, data, lenData.length+versionData.length+seqNoData.length, cmdData.length);
				System.arraycopy(statusData, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length, statusData.length);
				byte[] encryptData=this.des.encrypt(data);
				if(encryptData!=null){
					QQ_ORIGINAL_RES ores = new QQ_ORIGINAL_RES(encryptData);
					return ores;
				}else{
					logger.warn("[组装相应包失败：加密失败] ["+res.getSeqNum()+"]");
				}
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("[组装相应包失败：异常] ["+res.getSeqNum()+"]", e);
			}
		}
		else if(message instanceof QQ_SAVING_RESULT_RES) {
			QQ_SAVING_RESULT_RES res = (QQ_SAVING_RESULT_RES)message;
			try{
				byte[] versionData=mf.numberToByteArray(res.getVer(), 1);
				byte[] seqNoData=mf.numberToByteArray(res.getSeqNum(), 4);
				byte[] cmdData=mf.numberToByteArray(res.getCmd(), 1);
				
				
				byte[] uidData = res.getUid().getBytes();
				byte[] uidL = mf.numberToByteArray(uidData.length, 1);
				byte[] linkIdData = res.getLinkId().getBytes();
				byte[] linkIdL = mf.numberToByteArray(linkIdData.length, 1);
				byte[] buyIdData = mf.numberToByteArray(res.getBuyId(), 4);
				byte[] goodsIdData = mf.numberToByteArray(res.getGoodsId(), 2);
				byte[] goodsCountData = mf.numberToByteArray(res.getGoodsCount(), 4);
				byte[] statusData = mf.numberToByteArray(res.getStatus(), 1);
				
				
				int len=versionData.length+seqNoData.length+cmdData.length+uidL.length+uidData.length+linkIdL.length+linkIdData.length
						+buyIdData.length+goodsIdData.length+goodsCountData.length+statusData.length;
				byte[] lenData = mf.numberToByteArray(len, 4);
				len += lenData.length;
				byte[] data=new byte[len];
				System.arraycopy(lenData, 0, data, 0, lenData.length);
				System.arraycopy(versionData, 0, data, lenData.length, versionData.length);
				System.arraycopy(seqNoData, 0, data, lenData.length+versionData.length, seqNoData.length);
				System.arraycopy(cmdData, 0, data, lenData.length+versionData.length+seqNoData.length, cmdData.length);
				
				System.arraycopy(uidL, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length,uidL.length);
				System.arraycopy(uidData, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length+uidL.length,uidData.length);
				System.arraycopy(linkIdL, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length+
						uidL.length+uidData.length, 
						linkIdL.length);
				System.arraycopy(linkIdData, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length+
						uidL.length+uidData.length+linkIdL.length, linkIdData.length);
				System.arraycopy(buyIdData, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length+
						uidL.length+uidData.length+linkIdL.length+linkIdData.length, buyIdData.length);
				System.arraycopy(goodsIdData, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length+
						uidL.length+uidData.length+linkIdL.length+linkIdData.length+buyIdData.length, goodsIdData.length);
				System.arraycopy(goodsCountData, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length+
						uidL.length+uidData.length+linkIdL.length+linkIdData.length+buyIdData.length+goodsIdData.length, goodsCountData.length);
				System.arraycopy(statusData, 0, data, lenData.length+versionData.length+seqNoData.length+cmdData.length+
						uidL.length+uidData.length+linkIdL.length+linkIdData.length+buyIdData.length+goodsIdData.length+goodsCountData.length, statusData.length);
				
				byte[] encryptData=this.des.encrypt(data);
				if(encryptData!=null){
					QQ_ORIGINAL_RES ores = new QQ_ORIGINAL_RES(encryptData);
					return ores;
				}else{
					logger.warn("[组装相应包失败：加密失败] ["+res.getSeqNum()+"]");
				}
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("[组装相应包失败：异常] ["+res.getSeqNum()+"]", e);
			}
		}
		logger.warn("[组装相应包失败：不可知的response] ["+message.getSeqNum()+"] ["+message.getClass().getName()+"]");
		return null;
	}

	public  synchronized ResponseMessage handleMessage(QQMessage message) {
		long startTime  = System.currentTimeMillis();
		if(message instanceof LoginNotifyREQ) {
			LoginNotifyREQ req = (LoginNotifyREQ)message;
			QQUserCenter center = QQUserCenter.getInstance();
			QQUser user = center.getUser(req.getUid());
			if (user != null) {
				String oldSid = user.getSid();
				user.setSid(req.getSessionId());
				user.setLoginTime(req.getLoginTime() * 1000l);
				if (logger.isInfoEnabled()) {
					logger.info("[处理消息:更新用户] [UID:" + user.getUid() + "] [SID:" + user.getSid() + "] [old_sid:"+oldSid+"] [" + new Date(user.getLoginTime()) + "]");
				}
				LoginNotifyRES res = new LoginNotifyRES(req.getVer(), req.getSeqNum(), req.getCmd(), 0);
				return this.constructResponseMessage(res);
			} else {
				user = new QQUser();
				user.setUid(req.getUid());
				user.setSid(req.getSessionId());
				user.setLoginTime(req.getLoginTime() * 1000l);
				QQUserCenter.getInstance().addUser(user);
				if (logger.isInfoEnabled()) {
					logger.info("[处理消息:增加用户] [UID:" + user.getUid() + "] [SID:" + user.getSid() + "] [" + new Date(user.getLoginTime()) + "]");
				}
				LoginNotifyRES res = new LoginNotifyRES(req.getVer(), req.getSeqNum(), req.getCmd(), 0);
				return this.constructResponseMessage(res);
			}
		}
		else if(message instanceof QQ_SAVING_RESULT_REQ)
		{
			QQ_SAVING_RESULT_REQ req = (QQ_SAVING_RESULT_REQ)message;
			//如果充值通知信息传入 那么开始验证
			//从数据库中读出passport
			PassportManager passportManager = PassportManager.getInstance();
			Passport passport = passportManager.getPassport("QQUSER_"+req.getUid());
			if(passport == null)
			{
				passport = passportManager.getPassport("QQOPENAPIUSER_"+req.getUid());
			}
			if(passport != null)
			{
				//先从数据库中读出订单
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(req.getLinkId());
				if(orderForm != null)
				{
					/**
					 * 先判断订单是否回调过了
					 * 如果回调过了那么再去判断腾讯的buyid是否和以前的buyid一样 如果不一样 那么重新生成一笔订单
					 * 
					 */
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK)
					{
						if((!StringUtils.isEmpty(orderForm.getChannelOrderId())) &&(req.getLinkId().equals(orderForm.getOrderId()))&& (!orderForm.getChannelOrderId().equals(req.getBuyId()+"")) )
						{
							//生成订单
							OrderForm order = new OrderForm();
							order.setCreateTime(new Date().getTime());
							//设置充值平台
							order.setSavingPlatform(orderForm.getSavingPlatform());
							//设置充值介质
							order.setSavingMedium(orderForm.getSavingMedium());
							//设置充值人
							order.setPassportId(orderForm.getPassportId());
							//设置商品数量
							order.setGoodsCount(req.getGoodsCount());
							//设置消费金额
							order.setPayMoney(QQConstants.GOODS_PRICE * req.getGoodsCount());
							order.setServerName(orderForm.getServerName());
							order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							order.setHandleResultDesp("新生成订单");
							//设置订单responseResult
							order.setResponseResult(OrderForm.RESPONSE_SUCC);
							order.setResponseTime(System.currentTimeMillis());
							orderForm.setResponseDesp("version:" + req.getVer() + "," 
									+ "sequenceNum:" + req.getSeqNum() + "," + "cmd:" + req.getCmd() +","+ "uid:" + req.getUid() 
									+"," + "linkId:" + req.getLinkId() + "," + "buyId:" + req.getBuyId() + "," + "goodsId:" + req.getGoodsId() 
									+ "," + "goodsCount:" + req.getGoodsCount() + "," + "buyTime:" + req.getBuyTime());
							//设置是否通知游戏服状态 设为false
							order.setNotified(false);
							//设置通知游戏服是否成功 设为false
							order.setNotifySucc(false);
							order.setUserChannel(orderForm.getUserChannel());
							orderForm.setChannelOrderId(req.getBuyId()+"");
							//先存入到数据库中
							order = orderFormManager.createOrderForm(order);
							order.setOrderId(DateUtil.formatDate(new Date(), "yyyy-MM-dd") + "-" + QQConstants.GOODS_ID + "-"+ order.getId());
							try {
								orderFormManager.update(order);
							} catch (Exception e) {
								logger.error("[腾讯充值结果通知] [失败] [充值] [失败:拷贝订单时 更新订单出现异常] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
										"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [作为模板的linkId："+orderForm.getOrderId()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]",e);
							}
							QQ_SAVING_RESULT_RES res = new QQ_SAVING_RESULT_RES(req.getVer(),req.getSeqNum(),req.getCmd(),req.getUid(),
									req.getLinkId(), req.getBuyId(), req.getGoodsId(), req.getGoodsCount(), 0);
							if(logger.isInfoEnabled())
								logger.info("[腾讯充值结果通知] [成功] [充值] [成功:此为拷贝一份订单 因为客户进行了连续充值] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
									"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [作为模板的linkId："+orderForm.getOrderId()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
							return this.constructResponseMessage(res);
						}
						else
						{
							QQ_SAVING_RESULT_RES res = new QQ_SAVING_RESULT_RES(req.getVer(),req.getSeqNum(),req.getCmd(),req.getUid(),
									req.getLinkId(), req.getBuyId(), req.getGoodsId(), req.getGoodsCount(), 0);
							if(logger.isInfoEnabled())
								logger.info("[腾讯充值结果通知] [失败] [充值] [失败：此订单已经回调过了] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
									"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [作为模板的linkId："+orderForm.getOrderId()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
							return this.constructResponseMessage(res);
						}
					}
					
					//开始验证
					//验证用户名是否存在 uid 暂时不用验证 因为在存入订单时已经验证过了
					//验证uid是否和订单设置的uid匹配
					List<String> fieldNames = new ArrayList<String>();
					if(orderForm.getPassportId() == passport.getId() )
					{
						//验证goodsid是否匹配
						if(req.getGoodsId() == QQConstants.GOODS_ID)
						{
							//验证goodsCount是否和订单中的goodsCount吻合
							/*if(req.getGoodsCount() == orderForm.getGoodsCount())
							{*/
								//验证购买时间 先了解购买时间具体格式后再进行比较验证 这里暂时先不验证
								//开始更新订单状态
								//更新 充值结果返回时间
							orderForm.setGoodsCount(req.getGoodsCount());
							fieldNames.add("goodsCount");
							orderForm.setPayMoney(QQConstants.GOODS_PRICE * req.getGoodsCount());
							fieldNames.add("payMoney");
							orderForm.setResponseTime(System.currentTimeMillis());
							fieldNames.add("responseTime");
							//更新 充值结果返回描述
							orderForm.setResponseDesp("version:" + req.getVer() + "," 
									+ "sequenceNum:" + req.getSeqNum() + "," + "cmd:" + req.getCmd() +","+ "uid:" + req.getUid() 
									+"," + "linkId:" + req.getLinkId() + "," + "buyId:" + req.getBuyId() + "," + "goodsId:" + req.getGoodsId() 
									+ "," + "goodsCount:" + req.getGoodsCount() + "," + "buyTime:" + req.getBuyTime());
							fieldNames.add("responseDesp");
							//更新 本订单兑换状态, -1表示未返回, 0-返回充值成功, 1-返回充值失败 代表对方回调或给出充值是否成功的确认状态
							orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
							fieldNames.add("responseResult");
							//更新 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
							orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							fieldNames.add("handleResult");
							//更新 调用充值平台描述
							orderForm.setHandleResultDesp("腾讯回调 此时更新调用充值平台结果为成功");
							fieldNames.add("handleResultDesp");
							
							orderForm.setChannelOrderId(req.getBuyId()+"");
							fieldNames.add("channelOrderId");
							orderFormManager.batch_updateField(orderForm, fieldNames);
							if(logger.isInfoEnabled())
								logger.info("[腾讯充值结果通知] [成功] [充值] [成功] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
									"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
							QQ_SAVING_RESULT_RES res = new QQ_SAVING_RESULT_RES(req.getVer(),req.getSeqNum(),req.getCmd(),req.getUid(),
									req.getLinkId(), req.getBuyId(), req.getGoodsId(), req.getGoodsCount(), 0);
							return this.constructResponseMessage(res);
							/*}
							else //goodsCount不一致
							{
								//开始更新订单状态
								//更新 充值结果返回时间
								orderForm.setResponseTime(System.currentTimeMillis());
								fieldNames.add("responseTime");
								//更新 充值结果返回描述
								orderForm.setResponseDesp("version:" + req.getVer() + "," 
										+ "sequenceNum:" + req.getSeqNum() + "," + "cmd:" + req.getCmd() +","+ "uid:" + req.getUid() 
										+"," + "linkId:" + req.getLinkId() + "," + "buyId:" + req.getBuyId() + "," + "goodsId:" + req.getGoodsId() 
										+ "," + "goodsCount:" + req.getGoodsCount() + "," + "buyTime:" + req.getBuyTime());
								fieldNames.add("responseDesp");
								//更新 本订单兑换状态, -1表示未返回, 0-返回充值成功, 1-返回充值失败 代表对方回调或给出充值是否成功的确认状态
								orderForm.setResponseResult(1);
								fieldNames.add("responseResult");
								//更新 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
								orderForm.setHandleResult(1);
								fieldNames.add("handleResult");
								orderForm.setHandleResultDesp("腾讯回调 此时更新调用充值平台结果为成功");
								fieldNames.add("handleResultDesp");
								orderFormManager.batch_updateField(orderForm, fieldNames);
								logger.error("[腾讯充值结果通知] [成功] [充值] [失败:goodsCount不一致] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
										"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
								QQ_SAVING_RESULT_RES res = new QQ_SAVING_RESULT_RES(req.getVer(),req.getSeqNum(),req.getCmd(),req.getUid(),
										req.getLinkId(), req.getBuyId(), req.getGoodsId(), req.getGoodsCount(), 3);
								return this.constructResponseMessage(res);
							}*/

						} //goodsid不匹配
						else
						{
							//开始更新订单状态
							//更新 充值结果返回时间
							orderForm.setResponseTime(System.currentTimeMillis());
							fieldNames.add("responseTime");
							//更新 充值结果返回描述
							orderForm.setResponseDesp("version:" + req.getVer() + "," 
									+ "sequenceNum:" + req.getSeqNum() + "," + "cmd:" + req.getCmd() +","+ "uid:" + req.getUid() 
									+"," + "linkId:" + req.getLinkId() + "," + "buyId:" + req.getBuyId() + "," + "goodsId:" + req.getGoodsId() 
									+ "," + "goodsCount:" + req.getGoodsCount() + "," + "buyTime:" + req.getBuyTime());
							fieldNames.add("responseDesp");
							//更新 本订单兑换状态, -1表示未返回, 0-返回充值成功, 1-返回充值失败 代表对方回调或给出充值是否成功的确认状态
							orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
							fieldNames.add("responseResult");
							//更新 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
							orderForm.setHandleResult(1);
							fieldNames.add("handleResult");
							orderForm.setHandleResultDesp("腾讯回调 此时更新调用充值平台结果为成功");
							fieldNames.add("handleResultDesp");
							orderFormManager.batch_updateField(orderForm, fieldNames);
							logger.error("[腾讯充值结果通知] [成功] [充值] [失败:goodsId不一致] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
									"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
							QQ_SAVING_RESULT_RES res = new QQ_SAVING_RESULT_RES(req.getVer(),req.getSeqNum(),req.getCmd(),req.getUid(),
									req.getLinkId(), req.getBuyId(), req.getGoodsId(), req.getGoodsCount(),2);
							return this.constructResponseMessage(res);
						}
						
					} //uid错误
					else
					{
						//开始更新订单状态
						//更新 充值结果返回时间
						orderForm.setResponseTime(System.currentTimeMillis());
						fieldNames.add("responseTime");
						//更新 充值结果返回描述
						orderForm.setResponseDesp("version:" + req.getVer() + "," 
								+ "sequenceNum:" + req.getSeqNum() + "," + "cmd:" + req.getCmd() +","+ "uid:" + req.getUid() 
								+"," + "linkId:" + req.getLinkId() + "," + "buyId:" + req.getBuyId() + "," + "goodsId:" + req.getGoodsId() 
								+ "," + "goodsCount:" + req.getGoodsCount() + "," + "buyTime:" + req.getBuyTime());
						fieldNames.add("responseDesp");
						//更新 本订单兑换状态, -1表示未返回, 0-返回充值成功, 1-返回充值失败 代表对方回调或给出充值是否成功的确认状态
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						fieldNames.add("responseResult");
						//更新 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
						orderForm.setHandleResult(1);
						fieldNames.add("handleResult");
						orderForm.setHandleResultDesp("腾讯回调 此时更新调用充值平台结果为成功");
						fieldNames.add("handleResultDesp");
						orderFormManager.batch_updateField(orderForm, fieldNames);
						logger.error("[腾讯充值结果通知] [成功] [充值] [失败:uid不一致] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
								"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
						QQ_SAVING_RESULT_RES res = new QQ_SAVING_RESULT_RES(req.getVer(),req.getSeqNum(),req.getCmd(),req.getUid(),
								req.getLinkId(), req.getBuyId(), req.getGoodsId(), req.getGoodsCount(),2);
						return this.constructResponseMessage(res);
					}
				} 
				else //数据库中不存在本订单  发货失败
				{
					logger.error("[腾讯充值结果通知] [成功] [充值] [失败:数据库中不存在此订单] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
							"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
					QQ_SAVING_RESULT_RES res = new QQ_SAVING_RESULT_RES(req.getVer(),req.getSeqNum(),req.getCmd(),req.getUid(),
							req.getLinkId(), req.getBuyId(), req.getGoodsId(), req.getGoodsCount(),1);
					return this.constructResponseMessage(res);
				}
			} //uid不在数据库中
			else
			{
				logger.error("[腾讯充值结果通知] [成功] [充值] [失败:数据库中不存在此用户] [version:"+req.getVer()+"] [sequenceNum:"+req.getSeqNum()+"] [cmd:"+req.getCmd()+"] " +
						"[uid:"+req.getUid()+"] [linkId:"+req.getLinkId()+"] [buyId:"+req.getBuyId()+"] [goodsId:"+req.getGoodsId()+"] [goodsCount:"+req.getGoodsCount()+"] [buyTime:"+req.getBuyTime()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
				QQ_SAVING_RESULT_RES res = new QQ_SAVING_RESULT_RES(req.getVer(),req.getSeqNum(),req.getCmd(),req.getUid(),
						req.getLinkId(), req.getBuyId(), req.getGoodsId(), req.getGoodsCount(),2);
				return this.constructResponseMessage(res);
			}
		}
		logger.warn("[处理消息:未知的消息类型] [" + message.getCmd() + "] [ver:" + message.getVer() + "] [seqNum:" + message.getSeqNum() + "]");
		return null;
	}

	@Override
	public void discardRequestMessage(Connection conn, RequestMessage req) throws ConnectionException {
		// TODO Auto-generated method stub
		if (logger.isInfoEnabled()) {
			logger.info("[丢弃消息] [" + req.getTypeDescription() + "]");
		}
	}

	@Override
	public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage message) throws ConnectionException {
		// TODO Auto-generated method stub
		QQ_ORIGINAL_REQ origMes = (QQ_ORIGINAL_REQ) message;
		byte data[] = origMes.getData();
		try {
			byte[] decryptData = this.des.decrypt(data);
			if (decryptData != null) {
				QQMessage mes = this.constructReqeustMessage(decryptData);
				if (mes != null) {
					return this.handleMessage(mes);
				}
			} else {
				logger.warn("[解密失败] [" + data.length + "]");
			}
		} catch(Exception e) {
			logger.error("[解密发生异常] [dataLen:"+data.length+"]", e);
		}
		return null;
	}

	@Override
	public void receiveResponseMessage(Connection conn, RequestMessage req, ResponseMessage res) throws ConnectionException {
		// TODO Auto-generated method stub

	}

	@Override
	public RequestMessage waitingTimeout(Connection conn, long timeout) throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}

}
