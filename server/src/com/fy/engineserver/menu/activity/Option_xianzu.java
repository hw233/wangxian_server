package com.fy.engineserver.menu.activity;

import java.util.Random;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.activity.silvercar.Option_Refresh_CarColor;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;

public class Option_xianzu extends Option implements NeedCheckPurview, NeedRecordNPC {

	private NPC npc;

	/******* 以下都是static的 ********/
	public static String titleStart = "<f color='" + ArticleManager.COLOR_PURPLE + "'>祭祖次数:";
	public static String titleEnt = "</f>";
	public static String TITLE = "拜先祖，得好运活动（腾讯）";

	// 各个国家祭祖的次数 1,2,3
	public static int[] countryTimes = new int[] { 0, 0, 0, 0 };
	public static Object[] locks = new Object[] { new Object(), new Object(), new Object(), new Object() };

	public static long costMoney = 50000L;

	public static Random random = new Random();

	// public static int[] prizeNums = new int[] { 100, 500, 1000, 10000 };
	public static int[] prizeNums = new int[] { 10, 20, 25, 30 };
	public static String[] prizeArticles = new String[] { "福星宝石袋(1级)", "福星宝石袋(2级)", "福星宝石袋(3级)", "福星宝石袋(4级)" };
	public static String[] prizeNotices = new String[] { "<f color='" + ArticleManager.COLOR_GREEN + "'>@STRING_1@国仙友“@STRING_2@”心诚祭拜，感动仙祖，获得宝石福袋一个。</f>", "<f color='" + ArticleManager.COLOR_BLUE + "'>@STRING_1@国仙友“@STRING_2@”在“拜仙祖，得好运”活动中，得到了仙祖的庇佑，获得宝石福袋，太幸运啦。</f>", "<f color='" + ArticleManager.COLOR_PURPLE + "'>@STRING_1@国仙友“@STRING_2@”在祭祖时中人品爆发，高级宝石福袋从天而降落入他的囊中，好让人羡慕。</f>", "<f color='" + ArticleManager.COLOR_ORANGE + "'>@STRING_1@国仙友“@STRING_2@”在祭祖过程中，诚心磕了九百九十九个响头，感动了仙祖，仙祖特赠予“@STRING_2@”4级宝石福袋和“福星高照”称号！</f>" };
	public static String[] prizeTitles = new String[] { null, null, null, "拜先祖，得好运活动（腾讯）" };

	public static String[] prizeBuffNames = new String[] { "紫薇星法", "铁布金钟", "铁布金钟", "十八罗汉" };
	public static long delayTime = 1000 * 60 * 60;

	@Override
	public void doSelect(Game game, Player player) {
		if (!player.bindSilverEnough(costMoney)) {
			player.sendError("你没有足够的银子!");
			return;
		}

		try {
			String buffName = prizeBuffNames[random.nextInt(prizeBuffNames.length)];

			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate buffTemplate = btm.getBuffTemplateByName(buffName);
			if (buffTemplate == null) {
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[祭祖活动] [" + player.getLogString() + "] [buff不存在:" + buffName + "]");
				}
				return;
			}
			BillingCenter.getInstance().playerExpense(player, costMoney, CurrencyType.BIND_YINZI, 1, "活动-祭祖");

			player.send_HINT_REQ("成功祭祖，心诚必得仙祖庇佑。花费50两。获得buff:" + buffName);

			Buff buff = buffTemplate.createBuff(1);
			buff.setStartTime(SystemTime.currentTimeMillis());
			buff.setInvalidTime(buff.getStartTime() + delayTime);
			buff.setCauser(player);
			player.placeBuff(buff);

			int currentNum = 0;
			synchronized (locks[player.getCountry()]) {
				countryTimes[player.getCountry()]++;
				currentNum = countryTimes[player.getCountry()];
			}
			for (int i = 0; i < prizeArticles.length; i++) {
				int prizeNum = prizeNums[i];
				if (currentNum == prizeNum) {// 可以奖励
					String prizeArticle = prizeArticles[i];
					Article article = ArticleManager.getInstance().getArticle(prizeArticle);
					if (article == null) {
						ActivitySubSystem.logger.error(player.getLogString() + " [祭祖活动] [奖励物品不存在:" + prizeArticle + "]");
						continue;
					} else {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, 1, player, article.getColorType(), 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, "拜仙祖，得好运", "心诚祭祖，神灵庇佑！恭喜您在“拜仙祖，得好运”中获得宝石福袋一个！请在附件中提取。", 0L, 0L, 0L, "祭祖");

						player.sendNotice("心诚祭祖，神灵庇佑！恭喜您在“拜仙祖，得好运”中获得宝石福袋一个！请在附件中提取。");

						ChatMessageService cm = ChatMessageService.getInstance();
						ChatMessage msg = new ChatMessage();
						msg.setSort(ChatChannelType.SYSTEM);
						String des = Translate.translateString(prizeNotices[i], new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.STRING_2, player.getName() } });
						msg.setMessageText(des);
						cm.sendMessageToSystem(msg);
						ActivitySubSystem.logger.warn(player.getLogString() + " [祭祖活动] [获得奖励:" + ae.getArticleName() + "] [发送世界消息:" + msg.getMessageText() + "]");
					}
					// title
					String title = prizeTitles[i];
					if (title != null) {
						String valueByType = PlayerTitlesManager.getInstance().getValueByType(TITLE);
						PlayerTitlesManager.getInstance().addTitle(player, TITLE, false);
						player.sendError("恭喜你获得称号:" + valueByType);
						ActivitySubSystem.logger.warn(player.getLogString() + " [祭祖活动] [获得称号:(" + TITLE + "," + valueByType + ")]");
					}
					break;
				}
			}
			npc.setTitle(titleStart + currentNum + titleEnt);
			Game otherGame = null;
			if (game.gi.displayName.equals("昆华古城")) {
				otherGame = GameManager.getInstance().getGameByDisplayName("昆仑圣殿", player.getCountry());
			} else {
				otherGame = GameManager.getInstance().getGameByDisplayName("昆华古城", player.getCountry());;
			}
			if (otherGame != null) {
				LivingObject[] los = otherGame.getLivingObjects();
				for (LivingObject lo : los) {
					if (lo instanceof NPC) {
						NPC tempNpc = (NPC) lo;
						if (tempNpc.getnPCCategoryId() == npc.getnPCCategoryId()) {
							tempNpc.setTitle(titleStart + currentNum + titleEnt);
							break;
						}
					}
				}
			} else {
				ActivitySubSystem.logger.warn("另一个地图不存在,当前地图:" + game.gi.displayName);
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error(player.getLogString() + " [祭祖活动] [异常]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	@Override
	public Option copy(Game game, Player p) {
		Option_xianzu o = new Option_xianzu();
		o.setOptionId(this.getOptionId());
		o.setIconId(this.getIconId());
		o.setColor(this.getColor());
		o.setText(this.getText());
		o.setOId(this.getOId());
		o.setNPC(this.npc);
		return o;
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getLevel() <= 10) {
			return false;
		}
		byte country = player.getCountry();
		int currentGameCountry = player.getCurrentGameCountry();
		return country == currentGameCountry;
	}
}
