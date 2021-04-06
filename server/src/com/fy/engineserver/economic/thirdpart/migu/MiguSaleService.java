package com.fy.engineserver.economic.thirdpart.migu;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.event.LeaveGameEvent;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.economic.GameServerSavingNotifyService;
import com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker.SimpleArticle;
import com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker.SimpleSaleInfo;
import com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker.SimpleSaleInfo4Role;
import com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecordManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;


//TODO 更改成自己的日志
public class MiguSaleService extends HttpServlet {

	public static Logger logger = GameServerSavingNotifyService.logger;

	public static List<String> whiteAddress;
	private String privateKey = "12345asdfg7y6ydsudkf8HHGsds44loiu";
	public static final String FAIL_DEFAULT_CHANNEL = "YOUAI_XUNXIAN";
	
	public static boolean 复制物品bug修复 = true;
//
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		if (whiteAddress == null) {
			whiteAddress = new ArrayList<String>();
		}
//		whiteAddress.add("116.213.142.183");
		whiteAddress.add("124.248.40.21");
		whiteAddress.add("106.120.222.214");
		whiteAddress.add(Game.网关地址);
		// whiteAddress.add("112.25.14.13");
		System.out.println("[初始化米谷通知接口成功] [" + MiguSaleService.class.getName() + "]");
	}


	public List<String> getWhiteAddress() {
		return whiteAddress;
	}

	public void setWhiteAddress(List<String> whiteAddress) {
		this.whiteAddress = whiteAddress;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		String remoteAddr = req.getRemoteAddr();
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		boolean validAddress = false;
		if (whiteAddress.contains(remoteAddr)) {
			validAddress = true;
		} else {
			for (String s : whiteAddress) {
				if (s.endsWith("*")) {
					if (remoteAddr.startsWith(s.substring(0, s.length() - 1))) {
						validAddress = true;
						break;
					}
				}
			}
		}
		//TODO 需要添加签名验证
		
		if (validAddress) {
			
			String op = req.getParameter("op");
			long playerId = ParamUtils.getLongParameter(req, "playerId", -1l);
			String username = req.getParameter("username");
			String servername = req.getParameter("servername");
			
			servername = URLDecoder.decode(servername, "utf-8");
			String platform = req.getParameter("platform");
			String channel = req.getParameter("channel");
			String mac = req.getParameter("mac");
			String recordType = req.getParameter("recordType");
			String selledservername = req.getParameter("selledservername");
			if ("giveprice4role".equalsIgnoreCase(op) && !MiGuTradeServiceWorker.isOpenSaleRole()) {
				res.getWriter().write(URLEncoder.encode("falereason:此服务器暂未开放角色交易系统，敬请期待！", "utf-8"));
				return ;
			}
			
			boolean isCancleRole = false;
			boolean isbuyrole = false;				//购买角色---存在跨服购买，可以跳过部分验证
			if ("buyequip".equalsIgnoreCase(op) && recordType != null && recordType.equalsIgnoreCase("3")) {
//				selledservername = 
				isbuyrole = MiGuTradeServiceWorker.validata4salerole(selledservername,platform,channel);
			} else if ("cancelsell".equalsIgnoreCase(op) && recordType != null && recordType.equalsIgnoreCase("3")) {		//下架角色
				isCancleRole = true;
			}
			
			Player player = null;
			try {
				if (!"giveprice4role".equalsIgnoreCase(op)) {
					player = PlayerManager.getInstance().getPlayer(playerId);
				} else {
					long saleroleid = ParamUtils.getLongParameter(req, "saleroleid", -1l);
					player = PlayerManager.getInstance().getPlayer(saleroleid);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.warn("[米谷通讯] [获取角色] [异常] [" + playerId + "] [" + op + "] [" + req.getParameter("saleroleid") + "]", e);;
			}
			
//			if(player.getPassport() == null)
//			{
//				logger.warn("[接收米谷通知] [玩家未登录] ["+username+"] ["+playerId+"] ["+servername+"] ["+platform+"] ["+channel+"] ["+op+"]");
//			}
			
			//验证渠道，用户，角色和平台之间的准确性
			boolean isValidRole = MiGuTradeServiceWorker.validateRole(username, playerId, servername, platform,channel);
			boolean isInvalidPlayer = MiGuTradeServiceWorker.isInvalidPlayer(player);
			if (isbuyrole || isCancleRole) {
				isInvalidPlayer = false;
			}
			if (!isValidRole && MiGuTradeServiceWorker.validata4salerole(selledservername,platform,channel) && player != null && player.getUsername().equals(username)) {
				isValidRole = true;
			}
			if (logger.isInfoEnabled()) {
				logger.info("[米谷通讯] [op:" + op + "] [isValidRole:" + isValidRole + "] [isInvalidPlayer:" + isInvalidPlayer + "] [" + username + "]");
			}
			if(isValidRole || isbuyrole || isCancleRole)
			{
				if("queryeq".equalsIgnoreCase(op) && !isInvalidPlayer)
				{
					/**
					 * 获取此角色id下的所有可出售的装备
					 * 拼装成合适的json返回给boss
					 */
					List<SimpleArticle> lst = MiGuTradeServiceWorker.queryArticles(player, 0);
					String eqsJson = "";
					try {
						if (MiGuTradeServiceWorker.是否可以上架) {
							eqsJson = JsonUtil.jsonFromObject(lst);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					res.getWriter().write(eqsJson);
					if (logger.isInfoEnabled()) {
						logger.info("[接收米谷通知] [查询角色下装备列表] [成功] ["+eqsJson+"] ["+username+"] ["+playerId+"] ["+player.getName()+"]");
					}
					return;
				}
				else if("querysilver".equalsIgnoreCase(op) && !isInvalidPlayer)
				{
					String money = "0";
					if (MiGuTradeServiceWorker.是否可以上架) {
						money = MiGuTradeServiceWorker.getSilverByPlayer(playerId)+"";
					}
					res.getWriter().write(money);
					if (logger.isInfoEnabled()) {
						logger.info("[接收米谷通知] [查询角色银两] [成功] ["+money+"] ["+username+"] ["+playerId+"] ["+player.getName()+"]");
					}
					return;
					
				}
				else if("eqdetail".equalsIgnoreCase(op) && !isInvalidPlayer)
				{
					long articleId = ParamUtils.getLongParameter(req, "articleid", -1);
					int cellIndex = ParamUtils.getIntParameter(req, "ci", -1);
					String articleInfo = MiGuTradeServiceWorker.getArticleInfo(player, articleId);
					ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(articleId);
					
					if(articleEntity != null)
					{
						Cell cell = player.getKnapsack_common().getCell(cellIndex);
						int count = 1;
						if(cell == null)
						{
//							res.getWriter().write("");
//							logger.warn("[接受米谷通知] [查询装备详情] [失败] [没有查到对应的格子] ["+articleId+"] ["+cellIndex+"] ["+username+"] ["+playerId+"] ["+player.getName()+"]");
						} else if (cell.getEntityId() == articleId) {
							count = cell.count;
						}
						
						SimpleArticle simpleArticle = MiGuTradeServiceWorker.buildSimpleArticle(cellIndex, articleEntity,count);
						String content = "";
						try {
							content = simpleArticle.type+"@@@"+JsonUtil.jsonFromObject(simpleArticle)+"@@@"+articleInfo;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						res.getWriter().write(content);
						if (logger.isInfoEnabled()) {
							logger.info("[接收米谷通知] [查询装备详情] [成功] ["+articleInfo+"] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+articleId+"]");
						}
					}
					else
					{
						res.getWriter().write("");
						logger.warn("[接受米谷通知] [查询装备详情] [失败] [没有查到道具] ["+articleId+"] ["+cellIndex+"] ["+username+"] ["+playerId+"] ["+player.getName()+"]");
					}
					
					return;
				} else if ("giveprice4role".equalsIgnoreCase(op) && !isInvalidPlayer) {	//出售角色
					try {
						String clientid = req.getParameter("clientid");
						String valid = MiGuTradeServiceWorker.validateDevice4Salerole(clientid, username,mac);
						if (valid != null) {
							logger.warn("[接受米谷通知] [上架角色] [设备不合法] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"]");
							res.getWriter().write(URLEncoder.encode("falereason:"+valid, "utf-8"));
							return ;
						}
						long price  = -1;
						String saleprice = req.getParameter("saleprice");
						price = (long) Double.parseDouble(saleprice) * 100;
						if(price < MiGuTradeServiceWorker.游戏角色上架最低金额)
						{
							logger.warn("[接受米谷通知] [上架角色] [定价小于最低单笔交易金额] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"]");
							res.getWriter().write("定价小于最低单笔交易金额");
							return;
						}
						synchronized (player) {
							String result = MiGuTradeServiceWorker.validataGivePrice4Player(player);
							if (result == null) {			//验证角色是否可出售
								if (player.getUsername().equals(username)) {	//验证角色账号是否匹配
									SaleRecord saleRecord = new SaleRecord();
									saleRecord.setArticleId(player.getId());
									saleRecord.setCellIndex(-1);
									saleRecord.setGoodsCount(1);
									saleRecord.setCreateTime(System.currentTimeMillis());
									saleRecord.setPayMoney(price);			//上架价格
									Passport p = BossClientService.getInstance().getPassportByUserName(username);
									if(p == null)
									{
										logger.warn("[接受米谷通知] [上架角色] [用户不存在] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"]");
										res.getWriter().write("");
										return;
									}
									saleRecord.setMemo2(username);				//卖家账号，下架等操作时使用
									saleRecord.setSellPassportId(p.getId());
									saleRecord.setSellPlayerId(player.getId());
									saleRecord.setUserChannel(channel);
									saleRecord.setTradeType(SaleRecord.ROLE_TRADE);
									MiGuTradeServiceWorker.doSale4Player(player, saleRecord);
									
									SimpleSaleInfo4Role simpleSaleInfo = MiGuTradeServiceWorker.buildSimpleSaleInfo4Role(saleRecord,null,"");
									if (player.isOnline() && player.getConn() != null) {
										if (player.getCurrentGame() != null) {
											player.getCurrentGame().getQueue().push(new LeaveGameEvent(player));
										}
										player.leaveServer();
										player.getConn().close();
										logger.warn("[接受米谷通知] [上架角色] [将角色踢下线] [" + player.getLogString() + "]");
									}
									String returnContent = JsonUtil.jsonFromObject(simpleSaleInfo);
									res.getWriter().write(returnContent);
									logger.warn("[接受米谷通知] [上架角色] [成功] [" + username + "] [" + playerId + "] [" + player.getLogString() + "] [" + returnContent + "]");
									return ;
								} else {
									result = "账号不匹配";
								}
							}
							res.getWriter().write(URLEncoder.encode("falereason:" + result, "utf-8"));
							logger.warn("[接受米谷通知] [上架角色] [失败] [result:"+result+"] [playerUsername:" + player.getUsername() + "] [userName:" + username + "]");
						}
					}catch (Exception e2) {
						logger.warn("[接受米谷通知] [上架角色] [失败] [" + player.getLogString() + "] ", e2);
					} 
				}
				else if("giveprice".equalsIgnoreCase(op) && !isInvalidPlayer )
				{
					String equipequiplist = req.getParameter("equiplist");
					if(SaleRecordManager.getInstance().isRoleInSale(player)) {
						logger.warn("[接受米谷通知] [装备定价] [失败] [角色正在交易中] [" + player.getLogString() + "]");
						res.getWriter().write("");
						return ;
					}
					
					/**
					 * [{"type":"1","equiponlyid":"10002220234_10","saleday":3,"salenumber":1,"saleprice":1.01},{"type":"2","equiponlyid":"10002220234_11","saleday":3,"salenumber":1,"saleprice":2.00}]
					 */
					try {
						List lst =  (List)JsonUtil.objectFromJson(equipequiplist, List.class);
						List<SaleRecord> saleRecords = new ArrayList<SaleRecord>();
						List<SimpleSaleInfo> simpleSaleInfos = new ArrayList<MiGuTradeServiceWorker.SimpleSaleInfo>();
						List<String> equipCellInfos = new ArrayList<String>();
						for(Object o : lst)
						{
							Map map = (Map)o;
							String equipid_cellindex =  (String)map.get("equiponlyid");
							long articleId = Long.decode(equipid_cellindex.split("_")[0]);
							int cellindex = Integer.decode(equipid_cellindex.split("_")[1]);
							int type = Integer.decode((String)map.get("type")); //如果是银子 默认发给我-2这个值
							long count =(long)( (Integer)map.get("salenumber"));
							long price  = -1;
							if (复制物品bug修复 && articleId != 200000l) {
								String tempStr = articleId + "_" + cellindex;
								if (equipCellInfos.contains(tempStr)) {
									logger.warn("[接受米谷通知] [装备定价] [定价物品id和格子号重复] [" + player.getLogString() + "] [articleId:" + articleId + "] [cellindex:" + cellindex + "]");
									continue;
								}
								equipCellInfos.add(tempStr);
							}
							if (map.get("saleprice") instanceof Integer)
							{
								price = (long)((Integer)map.get("saleprice") * 100);
							}
							else
							{
								price = (long)((Double)map.get("saleprice") * 100);
							}
							
							if(price < MiGuTradeServiceWorker.游戏道具最低单笔交易额)
							{
								logger.warn("[接受米谷通知] [装备定价] [定价小于最低单笔交易金额] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+articleId+"]");
								continue;
							}
							
							
							
							if(articleId != 200000l)
							{
								if(MiGuTradeServiceWorker.validateGivePriceArticleEntity(articleId, type, cellindex, player))
								{
									
									//创建销售记录
									SaleRecord saleRecord = new SaleRecord();
									saleRecord.setArticleId(articleId);
									saleRecord.setCellIndex(cellindex);
									saleRecord.setGoodsCount(count);
									saleRecord.setCreateTime(System.currentTimeMillis());
									saleRecord.setPayMoney(price);
									Passport p = BossClientService.getInstance().getPassportByUserName(username);
									if(p == null)
									{
										logger.warn("[接受米谷通知] [装备定价] [用户不存在] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+articleId+"]");
										continue;
									}
									
									saleRecord.setSellPassportId(p.getId());
									saleRecord.setSellPlayerId(playerId);
									saleRecord.setUserChannel(channel);
									saleRecord.setTradeType(SaleRecord.ARTICLE_TRADE);
									
						
									saleRecords.add(saleRecord);
									
								
								}
								else
								{
									logger.warn("[接受米谷通知] [装备定价] [道具验证失败] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+articleId+"]");
									continue;
								}
							}
							else
							{
								synchronized (player) {
									int countSale = MiGuTradeServiceWorker.getSaleRecordNum4NoSaled4Silver(playerId);
									if( countSale >= MiGuTradeServiceWorker.LIMIT_UNSALE_SILVER_ORDER_NUM)
									{
										logger.warn("[接受米谷通知] [银两定价] [银两挂单数量已经超过限制] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+articleId+"] ["+count+"] [已经挂单数量:"+countSale+"]");
										continue;
									}
									
									
									
									if(MiGuTradeServiceWorker.validateGivePrice4Silver(articleId, count, player))
									{
										//创建销售记录
										SaleRecord saleRecord = new SaleRecord();
										saleRecord.setArticleId(articleId);
										saleRecord.setCellIndex(cellindex);
										saleRecord.setGoodsCount(count);
										
										
										saleRecord.setCreateTime(System.currentTimeMillis());
										saleRecord.setPayMoney(price);
										Passport p = BossClientService.getInstance().getPassportByUserName(username);
										if(p == null)
										{
											logger.warn("[接受米谷通知] [银两定价] [用户不存在] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+articleId+"] ["+count+"]");
											continue;
										}
										
										saleRecord.setSellPassportId(p.getId());
										saleRecord.setSellPlayerId(playerId);
										saleRecord.setUserChannel(channel);
										
										saleRecord.setTradeType(SaleRecord.SILVER_TRADE);
										saleRecords.add(saleRecord);
										
										if(logger.isInfoEnabled())
										{
											logger.info("[接受米谷通知] [银两定价] [扣除玩家银两] [成功] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+articleId+"] ["+count+"]");
										}
										
									}
									else
									{
										logger.warn("[接受米谷通知] [银两定价] [道具验证失败] [跳过] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+articleId+"]");
										continue;
									}
								}
							}
							
						}
						
						MiGuTradeServiceWorker.doSale(player, saleRecords);
						
						//这里构造一些额外的信息传给MiGuTradeServiceWorker.buildSimpleSaleInfo方法，为了简化改动，所以传入了string数组
						String[] otherInfos = new String[]{player.getUsername(),mac};
						
						for(SaleRecord saleRecord : saleRecords)
						{
							if (saleRecord != null && saleRecord.getId() <= 0) {
								if (logger.isWarnEnabled()) {
									logger.warn("[接收米谷通知] [定价失败] [订单没有创建成功] [" + player.getLogString() + "] [aeId:" + saleRecord.getArticleId() + "] [num:" + saleRecord.getGoodsCount() + "]");
								}
								continue;
							}
							SimpleSaleInfo simpleSaleInfo = MiGuTradeServiceWorker.buildSimpleSaleInfo(saleRecord,otherInfos);
							simpleSaleInfos.add(simpleSaleInfo);
						}
						
						String returnContent = JsonUtil.jsonFromObject(simpleSaleInfos);
						res.getWriter().write(returnContent);
						logger.warn("[接受米谷通知] [装备定价] [成功] [ok] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+returnContent+"]");
						try {						//订单创建成功后将订单info做缓存，以防在boss访问超时时利用此缓存做订单确认
							String validExceptionTime = req.getParameter("validExceptionTime");
							String key = player.getId() + "_" + validExceptionTime;	
							SaleRecordManager.getInstance().createTempSaleInfo(key, returnContent);
							logger.warn("[boss超时异常验证] [创建验证串] [id:"+player.getId()+"] [" + username + "] [key:" + key + "] [returnContent:" + returnContent + "]");
						} catch (Exception e2) {
							logger.warn("[创建订单info缓存] [异常] [" + player.getLogString() + "]", e2);
						}
						return;
						
					} catch (Exception e) {
						logger.warn("[接受米谷通知] [装备定价] [失败] [json格式错误] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+equipequiplist+"]",e);
						res.getWriter().write("");
						return;
					}
				}
				else if("validsale".equalsIgnoreCase(op) && !isInvalidPlayer )
				{
					long saleId = ParamUtils.getLongParameter(req, "saleid", -1);
					
					SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(saleId);
					if(saleRecord != null)
					{
						res.getWriter().write(saleRecord.getId()+"");
						if(logger.isInfoEnabled())
						{
							logger.info("[接受米谷通知] [验证定价记录] [成功] [ok] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+saleId+"]");
						}
						return;
					}
					else
					{
						res.getWriter().write("");
						logger.warn("[接受米谷通知] [验证定价记录] [失败] [fail] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+saleId+"]");
						return;
					}
					
				}
				else if("buyequip".equalsIgnoreCase(op) && !isInvalidPlayer )
				{
					try {
						long saleId = ParamUtils.getLongParameter(req, "saleid", -1);
						long sellplayerId = ParamUtils.getLongParameter(req, "sellplayerId", -1);
						String tradeprice = ParamUtils.getParameter(req, "tradefee");
						String saleprice = ParamUtils.getParameter(req, "saleprice");
						String buyerusername = ParamUtils.getParameter(req, "buyerusername");
						String selledroleid = ParamUtils.getParameter(req, "selledroleid");
						String selledusername = ParamUtils.getParameter(req, "selledusername");
						if (saleprice == null || saleprice.isEmpty()) {
							saleprice = "0";			//成交价格。暂时只有角色交易使用
						}
						
						String returnContent = MiGuTradeServiceWorker.buyArticle(saleId, sellplayerId, playerId,tradeprice,saleprice,buyerusername,selledservername,isbuyrole,selledroleid,selledusername);
						res.getWriter().write(returnContent);
						if(logger.isInfoEnabled())
						{
							logger.info("[接受米谷通知] [购买商品] [成功] [ok] ["+username+"] ["+playerId+"] ["+(player == null ? "null":player.getName())+"] ["+saleId+"] ["+sellplayerId+"]");
						}
					} catch (Exception e) {
						logger.warn("[接受米谷通知] [购买商品] [异常] [" + username + "] ",e);
					}
					return;
				}
				else if("cancelsell".equalsIgnoreCase(op) )
				{
				
					
					long saleId = ParamUtils.getLongParameter(req, "saleid", -1);
					String reason = ParamUtils.getParameter(req, "reason", "");
					if( !"2".equals(reason) && isInvalidPlayer )
					{
						res.getWriter().write("");
						if(logger.isInfoEnabled())
						{
							logger.info("[接受米谷通知] [下架商品] [失败] [用户是非法用户] ["+username+"] ["+playerId+"] ["+(player==null?username:player.getName())+"] ["+saleId+"] ["+reason+"]");
						}
						return;
					}
					String returnContent = MiGuTradeServiceWorker.cancelSell(saleId, playerId, reason,username);
					res.getWriter().write(returnContent);
					if(logger.isInfoEnabled())
					{
						logger.info("[接受米谷通知] [下架商品] [成功] [ok] ["+username+"] ["+playerId+"] ["+(player==null?username:player.getName())+"] ["+saleId+"] ["+reason+"]");
					}
					return;
				}
				else if("sellshift".equalsIgnoreCase(op) && !isInvalidPlayer)
				{
					//发邮件
					/**
					 * ➢	道具寄售公示期结束后系统发给卖家的邮件：
     邮件标题：寄售物品上架成功    
     发件人：米掌柜
内容：恭喜，您寄售的宝贝：xxxxx*1已通过公示期成功上架销售。祝好运！

					 */
					long saleId = ParamUtils.getLongParameter(req, "saleid", -1);
					SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(saleId);
					
					if(saleRecord == null)
					{
						res.getWriter().write("");
						logger.warn("[接受米谷通知] [自动上架通知] [失败] [fail] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+saleId+"]");
						return;
					}
					
					ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(saleRecord.getArticleId());
					
					long EntityMailID = 0;
					try {
						String title = "寄售物品上架成功";
						String content = "恭喜，您寄售的宝贝："+articleEntity.getArticleName()+"*"+saleRecord.getGoodsCount()+"已通过公示期成功上架销售。祝好运！";
						EntityMailID = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[0]
						, new int[0], title, content, 0, 0, 0, "米谷物品上架成功");
					} catch (Exception e) {
						res.getWriter().write("");
						logger.warn("[接受米谷通知] [自动上架通知] [失败] [fail] [出现未知异常] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+saleId+"]",e);
						return;
					}
					
					res.getWriter().write(""+saleId);
					logger.warn("[接受米谷通知] [自动上架通知] [成功] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+saleId+"]");
					return;
				}
				else if ("verificationExcep".equalsIgnoreCase(op) && !isInvalidPlayer) //boss超时验证订单
				{
					try {						//订单创建成功后将订单info做缓存，以防在boss访问超时时利用此缓存做订单确认
						String validExceptionTime = req.getParameter("validExceptionTime");
						String key = player.getId() + "_" + validExceptionTime;	
						String returnContent = SaleRecordManager.getInstance().getSaleInfoByKey(key);
						logger.warn("[boss超时异常验证] [开始] [" + username + "] [key:" + key + "] [returnContent:" + returnContent + "]");
						if (returnContent != null && !returnContent.isEmpty()) {
							res.getWriter().write(returnContent);
							logger.warn("[boss超时异常验证] [成功] [ok] ["+username+"] ["+playerId+"] ["+player.getName()+"] ["+returnContent+"]");
						} else {
							res.getWriter().write("fail");
						}
					} catch (Exception e2) {
						logger.warn("[创建订单info缓存] [异常] [" + player.getLogString() + "]", e2);
					}
					return ;
				}else if ("queryRoleInfo".equalsIgnoreCase(op) && !isInvalidPlayer) {		//获取角色信息
					try {
						String contenttype = req.getParameter("contenttype");
						String result = SaleRecordManager.getInstance().getPlayerInfoShow(contenttype, player);
						res.getWriter().write(result);
						if (logger.isInfoEnabled()) {
							logger.info("[获取角色信息] [" + player.getLogString() + "] [contenttype:"+contenttype+"] [" + URLDecoder.decode(result,"utf-8") + "]");
						}
						return ;
					} catch (Exception e2) {
						logger.warn("[获取角色信息] [异常] [" + player.getLogString() + "]", e2);
					}
				}else if ("querySaleIdInfo".equalsIgnoreCase(op)) {			//验证订单是否交易成功
					try {
						long saleId = ParamUtils.getLongParameter(req, "saleid", -1);
						SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(saleId);
//						SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(saleIds);
						if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_SUCC) {
							res.getWriter().write(saleRecord.getId() + "");
							logger.warn("[获取订单状态] [成功] [订单已交易成功] [订单id:" + saleId + "]");
						} else {
							res.getWriter().write("");
							logger.warn("[获取订单状态] [失败] [订单未交易成功] [订单id:" + saleId + "] [状态:" + saleRecord.getResponseResult() + "]");
						}
					} catch (Exception e) {
						logger.warn("[获取订单状态] [异常] [" + player.getLogString() + "]", e);
					}
				} else
				{
					if (logger.isInfoEnabled()) {
						logger.info("[接收米谷通知] [" + remoteAddr + "] ["+op+"] [暂不支持的操作] ["+isInvalidPlayer+"]  [" + (System.currentTimeMillis() - start) + "ms]");
					}
				}
			}
			else
			{
				logger.info("[接收米谷通知] [角色验证不合法] ["+username+"] ["+playerId+"] ["+servername+"] ["+platform+"] ["+channel+"] ["+op+"]");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("[接收米谷通知：非法的IP地址] [" + remoteAddr + "] [" + (System.currentTimeMillis() - start) + "ms]");
			}
			res.getWriter().write("ERROR(非法IP):" + remoteAddr);
		}
	}

	private boolean checkout(String[] str) {
		String s = str[0] + str[1] + str[2] + str[3] + str[4] + privateKey;
		return StringUtil.hash(s).equals(str[5]);
	}
}
