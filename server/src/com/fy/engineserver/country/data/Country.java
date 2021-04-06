package com.fy.engineserver.country.data;

import java.util.Hashtable;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 国家
 */
@SimpleEntity
public class Country {
	
	@SimpleId
	@SimpleColumn
	private long id;
	
	@SimpleVersion
	protected int version2;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion2() {
		return version2;
	}

	public void setVersion2(int version2) {
		this.version2 = version2;
	}

	private byte countryId;
	
	long lastUpdateTime;
	
	private String name;

	/**
	 * 国王，混元至圣
	 */
	private long kingId;
	
	/**
	 * 创建人物时的计数
	 */
	private long count;
	
	/**
	 * 大司马，文德大帝
	 */
	private long dasimaId;
	
	/**
	 * 大将军，纯阳真圣
	 */
	private long seniorGeneralId;
	
	/**
	 * 元帅，司命天王
	 */
	private long marshalId;
	
	/**
	 * 宰相，司禄天王
	 */
	private long primeMinisterId;
	
	/**
	 * 巡捕国王任命，度厄神君
	 */
	private long policeByKingId;
	
	/**
	 * 御史大夫国王任命，禄存神君
	 */
	private long yushidafuByKingId;
	
	/**
	 * 巡捕元帅任命，贪狼神君
	 */
	private long policeByMarshalId;
	
	/**
	 * 御史大夫宰相任命，贞廉神君
	 */
	private long yushidafuByPrimeMinisterId;
	
	/**
	 * 护国公，武曲星君
	 */
	private long huguogongId;
	
	/**
	 * 辅国公，文曲星君
	 */
	private long fuguogongId;
	
	/**
	 * 御林军，九天巡守
	 */
	private long[] yulinjunIds = new long[10];
	
	/**
	 * 授勋的国家官员id
	 */
	private long[] shouxunIds = new long[2];
	
	/**
	 * 表彰的国家官员id
	 */
	private long[] biaozhangIds = new long[2];
	
	/**
	 * 这个记录了某人成为国家官员的时间点
	 * 如果这个人以前就是国家官员，那么当这个人切换了某个职位后此时间点不变
	 * 如果某人辞官，那么把此人从这个表中去掉
	 */
	@SimpleColumn(name="guanyuanshijian")
	public Hashtable<Long,Long> 成为国家官员时间Map = new Hashtable<Long,Long>();

	@SimpleColumn(name="shouxunshijian")
	public Hashtable<Long,Long> 成为授勋官员时间Map = new Hashtable<Long,Long>();

	@SimpleColumn(name="biaozhangshijian")
	public Hashtable<Long,Long> 成为表彰官员时间Map = new Hashtable<Long,Long>();

	/**
	 * 当前投票数
	 * 数组下标代表 国王,大司马,大将军,元帅,宰相,巡捕国王任命,巡捕元帅任命,御史大夫国王任命,御史大夫宰相任命这些官职
	 * 上述顺序即为下标号,每个职位的当前得票数,只记录好评票,最后和总票数比较可以得出得票率
	 */
	public int[] currentVote = new int[9];
	
	/**
	 * 昨天投票数
	 * 数组下标代表 国王,大司马,大将军,元帅,宰相,巡捕国王任命,巡捕元帅任命,御史大夫国王任命,御史大夫宰相任命这些官职
	 * 上述顺序即为下标号,每个职位的昨天得票数,只记录好评票,最后和总票数比较可以得出得票率
	 */
	public double[] yesterdayVote = new double[9];

	/**
	 * 当前总投票数
	 */
	public int currentAllVote;

	/**
	 * 官员俸禄
	 * 国王,大司马,大将军,元帅,宰相,巡捕国王任命,巡捕元帅任命,御史大夫国王任命,御史大夫宰相
	 * 按照官职序号-1的顺序安排
	 */
	@SimpleColumn(name="fenglu")
	private int[] 官员俸禄 = new int[9];

	/**
	 * 国王小金库钱
	 */
	public long kingMoney;
	
	/**
	 * 金砖
	 */
	public int goldBrick = 500;
	
	/**
	 * 国运
	 */
	public boolean guoyun;
	
	/**
	 * 国运开始时间
	 */
	public long guoyunStartTime;
	
	/**
	 * 国探
	 */
	public boolean guotan;
	
	/**
	 * 国探开始时间
	 */
	public long guotanStartTime;
	
	BiaoJu biaoju;
	/**
	 * 公告
	 */
	public String notice = "";
	
	/**
	 * 国战资源
	 */
	private long guozhanResource;
	
	/**
	 * 国家上一次国战日期
	 */
	private int lastGuozhanDay;
	
	/**
	 * 本国第一个到70的玩家id
	 */
	public long first_70_playerId;
	
	/**
	 * 本国第一个到110的玩家id
	 */
	public long first_110_playerId;
	
	/**
	 * 本国第一个到180的玩家id
	 */
	public long first_180_playerId;
	
	/**
	 * 本国第一个到180的修罗玩家id
	 */
	public long first_douluo_playerId;
	
	/**
	 * 本国第一个到180的影魅玩家id
	 */
	public long first_guisha_playerId;
	
	/**
	 * 本国第一个到180的仙心玩家id
	 */
	public long first_lingzun_playerId;
	
	/**
	 * 本国第一个到180的九黎玩家id
	 */
	public long first_wuhuang_playerId;
	
	private long first_shoukui_playerId;
	
	public long[] first_259_playerIds = new long[5];	//修罗 影魅 仙心  九黎  兽魁
	
	private long first_260_playerId;
	
	public long getFirst_70_playerId() {
		return first_70_playerId;
	}

	public void setFirst_70_playerId(long first_70PlayerId) {
		first_70_playerId = first_70PlayerId;
		this.dirty = true;
	}

	public long getFirst_110_playerId() {
		return first_110_playerId;
	}

	public void setFirst_110_playerId(long first_110PlayerId) {
		first_110_playerId = first_110PlayerId;
		this.dirty = true;
	}

	public long getFirst_180_playerId() {
		return first_180_playerId;
	}

	public void setFirst_180_playerId(long first_180PlayerId) {
		first_180_playerId = first_180PlayerId;
		this.dirty = true;
	}

	public long getFirst_douluo_playerId() {
		return first_douluo_playerId;
	}

	public void setFirst_douluo_playerId(long firstDouluoPlayerId) {
		first_douluo_playerId = firstDouluoPlayerId;
		this.dirty = true;
	}

	public long getFirst_guisha_playerId() {
		return first_guisha_playerId;
	}

	public void setFirst_guisha_playerId(long firstGuishaPlayerId) {
		first_guisha_playerId = firstGuishaPlayerId;
		this.dirty = true;
	}

	public long getFirst_lingzun_playerId() {
		return first_lingzun_playerId;
	}

	public void setFirst_lingzun_playerId(long firstLingzunPlayerId) {
		first_lingzun_playerId = firstLingzunPlayerId;
		this.dirty = true;
	}

	public long getFirst_wuhuang_playerId() {
		return first_wuhuang_playerId;
	}

	public void setFirst_wuhuang_playerId(long firstWuhuangPlayerId) {
		first_wuhuang_playerId = firstWuhuangPlayerId;
		this.dirty = true;
	}

	/**
	 * 用于数据存储
	 */
	transient boolean dirty;
	
	public long getKingId() {
		return kingId;
	}

	public void setKingId(long kingId) {
		this.kingId = kingId;
		this.dirty = true;
	}

	public long getDasimaId() {
		return dasimaId;
	}

	public void setDasimaId(long dasimaId) {
		this.dasimaId = dasimaId;
		this.dirty = true;
	}

	public long getSeniorGeneralId() {
		return seniorGeneralId;
	}

	public void setSeniorGeneralId(long seniorGeneralId) {
		this.seniorGeneralId = seniorGeneralId;
		this.dirty = true;
	}

	public long getMarshalId() {
		return marshalId;
	}

	public void setMarshalId(long marshalId) {
		this.marshalId = marshalId;
		this.dirty = true;
	}

	public long getPrimeMinisterId() {
		return primeMinisterId;
	}

	public void setPrimeMinisterId(long primeMinisterId) {
		this.primeMinisterId = primeMinisterId;
		this.dirty = true;
	}

	public long getPoliceByKingId() {
		return policeByKingId;
	}

	public void setPoliceByKingId(long policeByKingId) {
		this.policeByKingId = policeByKingId;
		this.dirty = true;
	}

	public long getYushidafuByKingId() {
		return yushidafuByKingId;
	}

	public void setYushidafuByKingId(long yushidafuByKingId) {
		this.yushidafuByKingId = yushidafuByKingId;
		this.dirty = true;
	}

	public long getPoliceByMarshalId() {
		return policeByMarshalId;
	}

	public void setPoliceByMarshalId(long policeByMarshalId) {
		this.policeByMarshalId = policeByMarshalId;
		this.dirty = true;
	}

	public long getYushidafuByPrimeMinisterId() {
		return yushidafuByPrimeMinisterId;
	}

	public void setYushidafuByPrimeMinisterId(long yushidafuByPrimeMinisterId) {
		this.yushidafuByPrimeMinisterId = yushidafuByPrimeMinisterId;
		this.dirty = true;
	}

	public long getHuguogongId() {
		return huguogongId;
	}

	public void setHuguogongId(long huguogongId) {
		this.huguogongId = huguogongId;
		this.dirty = true;
	}

	public long getFuguogongId() {
		return fuguogongId;
	}

	public void setFuguogongId(long fuguogongId) {
		this.fuguogongId = fuguogongId;
		this.dirty = true;
	}

	public long[] getYulinjunIds() {
		return yulinjunIds;
	}

	public void setYulinjunIds(long[] yulinjunIds) {
		this.yulinjunIds = yulinjunIds;
		this.dirty = true;
	}

	public long[] getShouxunIds() {
		return shouxunIds;
	}

	public void setShouxunIds(long[] shouxunIds) {
		this.shouxunIds = shouxunIds;
		this.dirty = true;
	}

	public long[] getBiaozhangIds() {
		return biaozhangIds;
	}

	public void setBiaozhangIds(long[] biaozhangIds) {
		this.biaozhangIds = biaozhangIds;
		this.dirty = true;
	}

	public int[] getCurrentVote() {
		return currentVote;
	}

	public void setCurrentVote(int[] currentVote) {
		this.currentVote = currentVote;
		this.dirty = true;
	}

	public double[] getYesterdayVote() {
		return yesterdayVote;
	}

	public void setYesterdayVote(double[] yesterdayVote) {
		this.yesterdayVote = yesterdayVote;
		this.dirty = true;
	}

	public int getCurrentAllVote() {
		return currentAllVote;
	}

	public void setCurrentAllVote(int currentAllVote) {
		this.currentAllVote = currentAllVote;
		this.dirty = true;
	}

	public long getKingMoney() {
		return kingMoney;
	}

	public void setKingMoney(long kingMoney) {
		this.kingMoney = kingMoney;
		this.dirty = true;
	}

	public int getGoldBrick() {
		return goldBrick;
	}

	public void setGoldBrick(int goldBrick) {
		this.goldBrick = goldBrick;
		this.dirty = true;
	}

	public boolean isGuoyun() {
		return guoyun;
	}

	public void setGuoyun(boolean guoyun) {
		this.guoyun = guoyun;
		this.dirty = true;
	}

	public long getGuoyunStartTime() {
		return guoyunStartTime;
	}

	public void setGuoyunStartTime(long guoyunStartTime) {
		this.guoyunStartTime = guoyunStartTime;
		this.dirty = true;
	}

	public boolean isGuotan() {
		return guotan;
	}

	public void setGuotan(boolean guotan) {
		this.guotan = guotan;
		this.dirty = true;
	}

	public long getGuotanStartTime() {
		return guotanStartTime;
	}

	public void setGuotanStartTime(long guotanStartTime) {
		this.guotanStartTime = guotanStartTime;
		this.dirty = true;
	}

	public BiaoJu getBiaoju() {
		return biaoju;
	}

	public void setBiaoju(BiaoJu biaoju) {
		this.biaoju = biaoju;
		this.dirty = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.dirty = true;
	}

	public byte getCountryId() {
		return countryId;
	}

	public void setCountryId(byte countryId) {
		this.countryId = countryId;
		this.dirty = true;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
		this.dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int[] get官员俸禄() {
		return 官员俸禄;
	}

	public void set官员俸禄(int[] 官员俸禄) {
		this.官员俸禄 = 官员俸禄;
		this.dirty = true;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
		this.dirty = true;
	}

	public long getGuozhanResource() {
		return guozhanResource;
	}

	public void setGuozhanResource(long guozhanResource) {
		this.guozhanResource = guozhanResource;
		this.dirty = true;
	}

	public int getLastGuozhanDay() {
		return lastGuozhanDay;
	}

	public void setLastGuozhanDay(int lastGuozhanDay) {
		this.lastGuozhanDay = lastGuozhanDay;
		this.dirty = true;
	}

	public long getFirst_shoukui_playerId() {
		return first_shoukui_playerId;
	}

	public void setFirst_shoukui_playerId(long first_shoukui_playerId) {
		this.first_shoukui_playerId = first_shoukui_playerId;
	}

	public long getFirst_260_playerId() {
		return first_260_playerId;
	}

	public void setFirst_260_playerId(long first_260_playerId) {
		this.first_260_playerId = first_260_playerId;
		this.dirty = true;
	}

	public long[] getFirst_259_playerIds() {
		return first_259_playerIds;
	}

	public void setFirst_259_playerIds(long[] first_239_playerIds) {
		this.first_259_playerIds = first_239_playerIds;
		this.dirty = true;
	}

	
}
