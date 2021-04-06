package com.fy.engineserver.auction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostLoad;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 拍卖品
 *
 */
@SimpleEntity
public class Auction implements Cacheable{
	
	private static SimpleEntityManager<Auction> em;
	
	public static void setEM(SimpleEntityManager<Auction> em){
		Auction.em = em;
	}
	
	public static final int STATUS_NORMAL = 0;					//正常
	public static final int STATUS_EXPIRED = 1;					//过期,或取消
	public static final int STATUS_FINISHED = 2;				//卖完

	@SimpleId
	private long id;
	
	@SimpleVersion
	private int versionA;
	
	private String name;					//物品名称
	
	private long articleEntityId;			//对应实体ID
	
	private int count;						//数量
	
	private long sellerId;					//出售者ID
	
	private long startDate;					//开始时间
	
	private long endDate;					//结束时间
	
	private long startPrice;				//起始竞标价
	
	private long nowPrice;					//当前竞标价			这个字段会变化
	
	private long buyPrice;					//一口价
	
	@SimpleColumn(name="level2")
	private int level;						//等级
	@SimpleColumn(length=1000)
	private ArrayList<Long> pricePlayers = new ArrayList<Long>();	//出价者ID
	private transient AuctionInfo4Client info4Client;
	
	private int status;						//状态			这个字段会变化
	
	private String aMaintype;				//第一分类
	
	private String aSubtype;				//第二分类
	
	private int color;						//品质
	
	public Auction() {
	}
	
	/**
	 * id
	 * @return
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 得到物品id
	 * @return
	 */
	public long getArticleEntityId() {
		return articleEntityId;
	}

	public void setArticleEntityId(long articleEntityId) {
		this.articleEntityId = articleEntityId;
	}

	private transient String sellName;
	
	public String getSellName(){
		return sellName;
	}
	
	public void setSellName(String sellName) {
		this.sellName = sellName;
	}
	
	/**
	 * 数量
	 * @return
	 */
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 得到出售者id
	 * @return
	 */
	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * 开始日期
	 * @return
	 */
	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	/**
	 * 结束日期
	 * @return
	 */
	public long getEndDate() {
		return endDate;
	}

	protected void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	/**
	 * 起价
	 * @return
	 */
	public long getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(long startPrice) {
		this.startPrice = startPrice;
	}

	/**
	 * 目前价格
	 * @return
	 */
	public long getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(long nowPrice) {
		this.nowPrice = nowPrice;
		em.notifyFieldChange(this, "nowPrice");
	}

	/**
	 * 一口价(optional)
	 * @return
	 */
	public long getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}

	/**
	 * 物品等级
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

//	/**
//	 * 最高出价人
//	 * @return
//	 */
//	
//	public void buildPricePlayers(){
//		if(pricePlayerIds==null){
//			pricePlayerIds = "";
//			return;
//		}
//		String[] strings = pricePlayerIds.split(separators);
//		for(int i = 0 ; i<strings.length; i++){
//			if(!strings[i].equals("")){
//				pricePlayers.add(Long.valueOf(strings[i]));
//			}
//		}
//	}
	public long getMaxPricePlayer(){
		if(pricePlayers.size()>0){
			return pricePlayers.get(pricePlayers.size()-1);
		}
		return 0;
	}
	
	private transient String maxPricePlayerName;
	public String getMaxPricePlayerName(){
		return maxPricePlayerName;
	}
	
	public void setMaxPricePlayerName(String maxName) {
		this.maxPricePlayerName = maxName;
	}
	
//	public String getPricePlayerIds() {
//		return pricePlayerIds;
//	}
//	
//	public void setPricePlayerIds(String maxPricePlayer) {
//		this.pricePlayerIds = maxPricePlayer;
//	}

	public ArrayList<Long> getPricePlayer(){
		return pricePlayers;
	}
	
	public void setPricePlayer(ArrayList<Long> pricePlayer){
		this.pricePlayers = pricePlayer;
	}
	
	public void addPricePlayer(long pricePlayer){
		for(int i = 0 ; i<pricePlayers.size(); i++){
			if(pricePlayers.get(i)==pricePlayer){
				pricePlayers.remove(i);
				break;
			}
		}
		pricePlayers.add(pricePlayer);
//		if(pricePlayerIds == null || pricePlayerIds.equals("")){
//			pricePlayerIds = ""+pricePlayer;
//		}else{
//			pricePlayerIds = pricePlayerIds+separators+pricePlayer;
//		}
		em.notifyFieldChange(this, "pricePlayers");
	}
	
	/**
	 * 状态
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		em.notifyFieldChange(this, "status");
	}

	/**
	 * 物品的类别,装备，道具
	 * @return
	 */
	public String getAtype() {
		return aMaintype;
	}

	public void setAtype(String atype) {
		this.aMaintype = atype;
	}

	public String getAsubtype() {
		return aSubtype;
	}

	public void setAsubtype(String asubtype) {
		this.aSubtype = asubtype;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSize() {
		return 0;
	}

	public AuctionInfo4Client createInfo(){
		if (info4Client == null) {
			postLoad();
		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		long leave = (getEndDate()-now)/1000/60/60;
		String leaveTime = null;
		if(leave<=0){
			leaveTime = Translate.text_不足1小时;
			
		}else{
			leaveTime = leave+Translate.text_小时;
		}
		info4Client.setLeaveTime(leaveTime);
		if(getNowPrice()==0){
			info4Client.setNowPrice(getStartPrice());
		}else{
			info4Client.setNowPrice(getNowPrice());
		}
		if (info4Client.getSellName() == null || info4Client.getSellName().equals("未知")) {
			info4Client.setSellName(getSellName());
		}
		if(getMaxPricePlayer()>0){
			info4Client.setBuyPlayerName(getMaxPricePlayerName());
		}
		return info4Client;
	}
	
	public static Auction createAuction(){
		Auction auction = new Auction();
		auction.setStartDate(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		auction.setEndDate(auction.getStartDate()+AuctionManager.getInstance().AUCTION_TIME);
		try {
			auction.setId(AuctionManager.getInstance().em.nextId());
		} catch (Exception e) {
			AuctionManager.logger.error("生成求购ID出错", e);
			return null;
		}
//		auction.setPricePlayerIds("");
		return auction;
	}
	
	private static final String[] stateString = new String[]{"正常", "过期或取消", "卖完"};
	public String getLogString() {
		StringBuffer perString = new StringBuffer("");
		if (AuctionManager.logger.isInfoEnabled()) {
			 for (Long id:pricePlayers) {
				 perString.append(id.longValue()).append(",");
			 }
		}
		Date date = new Date(startDate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String timeString = format.format(date);
		StringBuffer buffer = new StringBuffer("");
		buffer.append("拍卖ID=").append(id)
		.append("] [出售者ID=").append(getSellerId())
		.append("] [道具EntityID=").append(articleEntityId)
		.append("] [道具名字=").append(name)
		.append("] [品质=").append(color)
		.append("] [数量=").append(count)
		.append("] [拍卖状态=").append(stateString[getStatus()])
		.append("] [拍卖时间=").append(timeString)
		.append("] [拍卖价钱=").append(startPrice).append("-").append(buyPrice)
		.append("] [当前竞拍价=").append(nowPrice)
		.append("] [竞拍者=").append(perString);
		
		return buffer.toString();
	}
	public String getLogString4Newlog() {
		StringBuffer perString = new StringBuffer("");
		if (AuctionManager.logger.isInfoEnabled()) {
			for (Long id:pricePlayers) {
				perString.append(id.longValue()).append(",");
			}
		}
		Date date = new Date(startDate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String timeString = format.format(date);
		StringBuffer buffer = new StringBuffer("");
		buffer.append("拍卖ID:").append(id)
		.append("] [出售者ID:").append(getSellerId())
		.append("] [道具EntityID:").append(articleEntityId)
		.append("] [道具名字:").append(name)
		.append("] [品质:").append(color)
		.append("] [数量:").append(count)
		.append("] [拍卖状态:").append(stateString[getStatus()])
		.append("] [拍卖时间:").append(timeString)
		.append("] [拍卖价钱:").append(startPrice).append("-").append(buyPrice)
		.append("] [当前竞拍价:").append(nowPrice)
		.append("] [竞拍者:").append(perString);
		
		return buffer.toString();
	}

	public void setVersionA(int versionA) {
		this.versionA = versionA;
	}

	public int getVersionA() {
		return versionA;
	}
	
	@SimplePostLoad
	public void postLoad(){
		info4Client = new AuctionInfo4Client();
		info4Client.setId(getId());
		info4Client.setEntityId(getArticleEntityId());
		ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(getArticleEntityId());
		info4Client.setIcon(ArticleManager.getInstance().getArticle(entity.getArticleName()).getIconId());
		info4Client.setArticleName(entity.getArticleName());
		info4Client.setCount(getCount());
		info4Client.setBuyPrice(getBuyPrice());
		info4Client.setLevel(getLevel());
		info4Client.setColor(getColor());
	}
}
