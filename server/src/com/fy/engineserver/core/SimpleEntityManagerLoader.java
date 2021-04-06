package com.fy.engineserver.core;

import com.fy.engineserver.achievement.AchievementEntity;
import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.activeness.PlayerActivenessInfo;
import com.fy.engineserver.activity.chongzhiActivity.ChargeRecord;
import com.fy.engineserver.activity.chongzhiActivity.MonthCardRecord;
import com.fy.engineserver.activity.clifford.CliffordData;
import com.fy.engineserver.activity.fairyRobbery.instance.FairyRobberyEntity;
import com.fy.engineserver.activity.levelUpReward.instance.LevelUpRewardEntity;
import com.fy.engineserver.activity.village.data.VillageData;
import com.fy.engineserver.activity.xianling.PlayerXianLingData;
import com.fy.engineserver.activity.xianling.XianLingBillBoardData;
import com.fy.engineserver.articleEnchant.EnchantData;
import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.auction.Auction;
import com.fy.engineserver.buffsave.BuffSave;
import com.fy.engineserver.cityfight.citydata.CityData;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.data.CountryManagerData;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.article.data.equipextra.instance.EquipExtraData;
import com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntArticleExtraData;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecord;
import com.fy.engineserver.gm.feedback.GMRecord;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.hotspot.HotspotInfo;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.petHouse.HouseData;
import com.fy.engineserver.jiazu2.instance.BiaoCheEntity;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.marriage.MarriageBeq;
import com.fy.engineserver.newBillboard.BillboardInfo;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newtask.DeliverTask;
import com.fy.engineserver.newtask.NewDeliverTask;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.playerAims.instance.PlayerAimsEntity;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.seal.data.SealTaskInfo;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.sifang.info.SiFangInfo;
import com.fy.engineserver.soulpith.instance.SoulPithAeData;
import com.fy.engineserver.soulpith.instance.SoulPithEntity;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity;
import com.fy.engineserver.sprite.pet.PetFlyState;
import com.fy.engineserver.sprite.pet2.PetsOfPlayer;
import com.fy.engineserver.talent.TalentData;
import com.fy.engineserver.tournament.data.OneTournamentData;
import com.fy.engineserver.tournament.data.TournamentData;
import com.fy.engineserver.trade.requestbuy.RequestBuy;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class SimpleEntityManagerLoader {

	public void init() throws Exception {

		//OperationTrackerServiceHelper.config(true, 7200);
		
		long now = System.currentTimeMillis();
		SimpleEntityManagerFactory.getSimpleEntityManager(JiazuMember.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(Jiazu.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(SeptStation.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(Cave.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(Faery.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(AchievementEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(GameDataRecord.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(TaskEntity.class);

		SimpleEntityManagerFactory.getSimpleEntityManager(QianCengTa_Ta.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(SiFangInfo.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(RequestBuy.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(Auction.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(HotspotInfo.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(MarriageBeq.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.marriage.MarriageInfo.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.marriage.MarriageDownCity.class);

		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.unite.Unite.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.masterAndPrentice.MasterPrenticeInfo.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.society.Relation.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.gm.feedback.Feedback.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.notice.Notice.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.zongzu.data.ZongPai.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.sprite.horse.Horse.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.activity.fateActivity.base.FateActivity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(com.fy.engineserver.sprite.pet.Pet.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(BillboardStatDate.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(BillboardInfo.class);

		SimpleEntityManagerFactory.getSimpleEntityManager(GMRecord.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(CliffordData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(VillageData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(Country.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(CountryManagerData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(ArticleEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(Mail.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(TournamentData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(OneTournamentData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(CityData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(ArticleProtectData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(PetsOfPlayer.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(PlayerActivenessInfo.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(SkBean.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(TransitRobberyEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(NewDeliverTask.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(BuffSave.class);	
		SimpleEntityManagerFactory.getSimpleEntityManager(PlayerAimsEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(Horse2RelevantEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(JiazuMember2.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(BiaoCheEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(SaleRecord.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(ArticleTradeRecord.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(MonthCardRecord.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(LevelUpRewardEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(ChargeRecord.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(EnchantData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(HuntLifeEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(HuntArticleExtraData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(FairyRobberyEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(SoulPithEntity.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(SoulPithAeData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(TalentData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(BaoShiXiaZiData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(PetFlyState.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(EquipExtraData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(PlayerXianLingData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(XianLingBillBoardData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(HorseEquInlay.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(HouseData.class);
		SimpleEntityManagerFactory.getSimpleEntityManager(SealTaskInfo.class);
		
		
		System.out.println("******************[检查所有数据库] [成功] [耗时:" + (System.currentTimeMillis() - now) + "ms]*****************");
		ServiceStartRecord.startLog(this);
	}

	public static void main(String[] args) throws Exception {
		SimpleEntityManagerLoader entityManagerLoad = new SimpleEntityManagerLoader();
		entityManagerLoad.init();
		System.out.println("over");
	}
}
