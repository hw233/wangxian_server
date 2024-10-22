package com.fy.engineserver.sprite;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.allMapDisplayName;
import static com.fy.engineserver.datasource.language.Translate.translateMapDisplayName;
import static com.fy.engineserver.datasource.language.Translate.translateString;
import static com.fy.engineserver.datasource.language.Translate.二级分类酒;
import static com.fy.engineserver.datasource.language.Translate.你的宠物击杀了敌对阵营的;
import static com.fy.engineserver.datasource.language.Translate.你被敌对国家的xx的宠物击杀;
import static com.fy.engineserver.datasource.language.Translate.召出了xx;
import static com.fy.engineserver.datasource.language.Translate.召回了xx;
import static com.fy.engineserver.datasource.language.Translate.吃饭去了;
import static com.fy.engineserver.datasource.language.Translate.国战活动中不能使用飞行坐骑;
import static com.fy.engineserver.datasource.language.Translate.宠物正在挂机不能出战;
import static com.fy.engineserver.datasource.language.Translate.幽冥幻域活动中不能使用飞行坐骑;
import static com.fy.engineserver.datasource.language.Translate.幽毒幼蝎;
import static com.fy.engineserver.datasource.language.Translate.忙着呢;
import static com.fy.engineserver.datasource.language.Translate.您处于冰冻状态下不能使用坐骑;
import static com.fy.engineserver.datasource.language.Translate.您处于战场中不能使用飞行坐骑;
import static com.fy.engineserver.datasource.language.Translate.挂机勿扰;
import static com.fy.engineserver.datasource.language.Translate.日曜幼虎;
import static com.fy.engineserver.datasource.language.Translate.此宠物是出战状态不能放生;
import static com.fy.engineserver.datasource.language.Translate.睡觉中;
import static com.fy.engineserver.datasource.language.Translate.离火幼狼;
import static com.fy.engineserver.datasource.language.Translate.获得称号xx;
import static com.fy.engineserver.datasource.language.Translate.躁狂野牛;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.model.Passport;
import com.fy.engineserver.achievement.AchievementEntity;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.activeness.PlayerActivenessInfo;
import com.fy.engineserver.activity.base.ExpAddManager;
import com.fy.engineserver.activity.base.TimesActivityManager;
import com.fy.engineserver.activity.buchang.EventType;
import com.fy.engineserver.activity.buchang.ServerEventManager;
import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.clifford.manager.CliffordManager;
import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.dig.DigManager;
import com.fy.engineserver.activity.dig.DigTemplate;
import com.fy.engineserver.activity.explore.ExploreEntity;
import com.fy.engineserver.activity.fairyBuddha.challenge.FairyChallengeManager;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.activity.fateActivity.FateActivityType;
import com.fy.engineserver.activity.fateActivity.FateManager;
import com.fy.engineserver.activity.furnace.FurnaceManager;
import com.fy.engineserver.activity.levelUpReward.LevelUpRewardManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.newserver.NewServerActivityManager;
import com.fy.engineserver.activity.notice.ActivityNoticeManager;
import com.fy.engineserver.activity.peekandbrick.PeekAndBrickManager;
import com.fy.engineserver.activity.peoplesearch.PeopleSearch;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.activity.peoplesearch.PeopleTemplet;
import com.fy.engineserver.activity.pickFlower.MagicWeaponNpc;
import com.fy.engineserver.activity.quiz.QuizManager;
import com.fy.engineserver.activity.refreshbox.RefreshBoxManager;
import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.activity.village.manager.VillageFightManager;
import com.fy.engineserver.activity.wolf.WolfGame;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.activity.xianling.PlayerXianLingData;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.articleEnchant.AbnormalStateBuff;
import com.fy.engineserver.articleEnchant.AddBuffTimerTask;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.articleEnchant.EnchantManager;
import com.fy.engineserver.articleEnchant.model.EnchantModel;
import com.fy.engineserver.articleEnchant.model.EnchantTempModel;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.authority.Authority;
import com.fy.engineserver.authority.AuthorityAgent;
import com.fy.engineserver.authority.AuthorityConfig;
import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.concrete.BattleFieldManager;
import com.fy.engineserver.battlefield.concrete.TournamentField;
import com.fy.engineserver.bourn.BournCfg;
import com.fy.engineserver.bourn.BournManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.cityfight.CityFightManager;
import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.constants.GameConstant;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.FieldChangeEvent;
import com.fy.engineserver.core.FightingPlace;
import com.fy.engineserver.core.FightingPlaceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.MoveTrace4Client;
import com.fy.engineserver.core.NewPlayerLeadDataManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.client.AavilableTaskInfo;
import com.fy.engineserver.core.client.FunctionNPC;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.Avata;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.core.res.GameMap;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.core.res.MapPolyArea;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.HunshiEntity;
import com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntArticleExtraData;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.article.data.props.ArticleProperty;
import com.fy.engineserver.datasource.article.data.props.AvataProps;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.HorseProps;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.Knapsack_TimeLimit;
import com.fy.engineserver.datasource.article.data.props.PackageProps;
import com.fy.engineserver.datasource.article.data.props.PetProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.data.props.PropsCategory;
import com.fy.engineserver.datasource.article.data.props.RandomPackageProps;
import com.fy.engineserver.datasource.article.entity.client.BagInfo4Client;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.UsingPropsAgent;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.article.manager.UsingPropsAgent.PropsCategoryCoolDown;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_JiangDiZhiLiao;
import com.fy.engineserver.datasource.buff.Buff_RayDamage;
import com.fy.engineserver.datasource.buff.Buff_StealFruit;
import com.fy.engineserver.datasource.buff.Buff_ZhongDu;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuFaGong;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuWithStatus;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuWuGong;
import com.fy.engineserver.datasource.buff.Buff_didangshanghai;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.career.CareerThread;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.datasource.language.MultiLanguageTranslateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.ActiveSkillParam;
import com.fy.engineserver.datasource.skill.AuraBuff;
import com.fy.engineserver.datasource.skill.AuraSkill;
import com.fy.engineserver.datasource.skill.AuraSkillAgent;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffect;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithSummonNPC;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithTargetOrPosition;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaFangPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaGongPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuFangPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuGongPassiveSkill;
import com.fy.engineserver.datasource.skill.passivetrigger.PassiveTriggerImmune;
import com.fy.engineserver.deal.service.DealCenter;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.downcity.DownCityDataForPlayer;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.downcity.downcity3.BossRoom;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.charge.CardConfig;
import com.fy.engineserver.economic.charge.CardFunction;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerLogin;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.gm.feedback.service.FeedbackManager;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.guozhan.Guozhan;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.hotspot.Hotspot;
import com.fy.engineserver.hotspot.HotspotManager;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.petHouse.PetHouseManager;
import com.fy.engineserver.jiazu.service.JiazuFighterManager;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.BossCity;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.mapsound.MapSoundManager;
import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_FightingPlaceRecurrention;
import com.fy.engineserver.menu.Option_Learn_Skill_Sure;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.ARTICLE_OPRATE_RESULT;
import com.fy.engineserver.message.CAN_ACCEPT_TASK_MODIFY_REQ;
import com.fy.engineserver.message.COLLECTION_NPC_MODIFY_REQ;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.CONTINUE_KILL_REQ;
import com.fy.engineserver.message.DENY_USER_REQ;
import com.fy.engineserver.message.DOWNCITY_PREPARE_WINDOW_REQ;
import com.fy.engineserver.message.EQUIPMENT_SKILL_REQ;
import com.fy.engineserver.message.FUNCTION_NPC_MODIFY_REQ;
import com.fy.engineserver.message.FUNCTION_NPC_RES;
import com.fy.engineserver.message.Fangbao_KNAPSACK_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HEARTBEAT_CHECK_REQ;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.HORSE_PUTONOROFF_RES;
import com.fy.engineserver.message.HORSE_RIDE_RES;
import com.fy.engineserver.message.JIAZU_TITLE_CHANGE_RES;
import com.fy.engineserver.message.LOGIN_REWARD_RES;
import com.fy.engineserver.message.MIESHI_MAP_LANG_TRANSLATE;
import com.fy.engineserver.message.MODIFY_VIP_INFO_REQ;
import com.fy.engineserver.message.MODIFY_VIP_INFO_RES;
import com.fy.engineserver.message.NEW_MIESHI_UPDATE_PLAYER_INFO;
import com.fy.engineserver.message.NEW_QUERY_CAREER_INFO_BY_ID_RES;
import com.fy.engineserver.message.NEW_QUERY_CAREER_INFO_RES;
import com.fy.engineserver.message.NEW_WAREHOUSE_GET_RES;
import com.fy.engineserver.message.NOTICE_CLIENT_BIANSHEN_CD_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_DELIVER_TASK_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAYE_CARTOON2_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.message.NOTICE_DELIVER_BOURN_TASK_NUM_REQ;
import com.fy.engineserver.message.NOTICE_PARTICLE_REQ;
import com.fy.engineserver.message.NOTICE_TEAM_CHANGE_RES;
import com.fy.engineserver.message.NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ;
import com.fy.engineserver.message.NOTIFY_EQUIPMENT_TABLECHANGE_REQ;
import com.fy.engineserver.message.NOTIFY_EVENTS_REQ;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.message.NOTIFY_KNAPSACKCHANGE_REQ;
import com.fy.engineserver.message.NOTIFY_ROBBERY_COUNTDOWN_REQ;
import com.fy.engineserver.message.NOTIFY_SHOUHUN_KNAP_CHANGE_RES;
import com.fy.engineserver.message.NOTIFY_USER_LEAVESERVER_REQ;
import com.fy.engineserver.message.PLAYER_DEAD_REQ;
import com.fy.engineserver.message.PLAYER_MOVETRACE_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.message.PLAYER_SOUL_CHANGE_RES;
import com.fy.engineserver.message.PLAYER_SWITCH_SOUL_RES;
import com.fy.engineserver.message.PLAYER_YUANBAO_CHANGED_REQ;
import com.fy.engineserver.message.PLAY_SOUND_REQ;
import com.fy.engineserver.message.PROPS_CD_MODIFY_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.message.QUERY_ARTICLE_RES;
import com.fy.engineserver.message.QUERY_CAREER_BIANSHEN_INFO_RES;
import com.fy.engineserver.message.QUERY_CAREER_INFO_BY_ID_RES;
import com.fy.engineserver.message.QUERY_CAREER_JINJIE_INFO_RES;
import com.fy.engineserver.message.QUERY_CAREER_XINFA_INFO_RES;
import com.fy.engineserver.message.QUERY_KNAPSACK_FB_RES;
import com.fy.engineserver.message.QUERY_KNAPSACK_QILING_RES;
import com.fy.engineserver.message.QUERY_KNAPSACK_RES;
import com.fy.engineserver.message.QUERY_PLAYER_HORSE_RES;
import com.fy.engineserver.message.SEND_ACTIVESKILL_REQ;
import com.fy.engineserver.message.SET_POSITION_REQ;
import com.fy.engineserver.message.SHOPS_NAME_GET_RES;
import com.fy.engineserver.message.SHOP_GET_RES;
import com.fy.engineserver.message.SHOP_OTHER_INFO_RES;
import com.fy.engineserver.message.SKILL_CD_MODIFY_REQ;
import com.fy.engineserver.message.SKILL_MSG;
import com.fy.engineserver.message.SOUL_MESSAGE_RES;
import com.fy.engineserver.message.SWITCH_BIANSHENJI_SKILLS_RES;
import com.fy.engineserver.message.TRANSIENTENEMY_CHANGE_REQ;
import com.fy.engineserver.message.USER_CLIENT_INFO_REQ;
import com.fy.engineserver.message.WAREHOUSE_GET_RES;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.newtask.DeliverTask;
import com.fy.engineserver.newtask.NewDeliverTask;
import com.fy.engineserver.newtask.NewDeliverTaskManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.TaskEntityManager;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfCollection;
import com.fy.engineserver.newtask.actions.TaskActionOfConvoyNPC;
import com.fy.engineserver.newtask.actions.TaskActionOfGetArticle;
import com.fy.engineserver.newtask.actions.TaskActionOfGetArticleAndDelete;
import com.fy.engineserver.newtask.actions.TaskActionOfGetBuff;
import com.fy.engineserver.newtask.actions.TaskActionOfKillPlayer;
import com.fy.engineserver.newtask.actions.TaskActionOfMonster;
import com.fy.engineserver.newtask.actions.TaskActionOfMonsterLv;
import com.fy.engineserver.newtask.actions.TaskActionOfMonsterLvNearPlayer;
import com.fy.engineserver.newtask.actions.TaskActionOfMonsterRandomNum;
import com.fy.engineserver.newtask.actions.TaskActionOfTalkToNPC;
import com.fy.engineserver.newtask.actions.TaskActionOfTaskDeliver;
import com.fy.engineserver.newtask.actions.TaskActionOfUseArticle;
import com.fy.engineserver.newtask.service.DeliverTaskManager;
import com.fy.engineserver.newtask.service.FunctionNpcModify;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskEventTransactCenter;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.newtask.targets.TaskTarget;
import com.fy.engineserver.newtask.targets.TaskTargetOfGetArticle;
import com.fy.engineserver.newtask.timelimit.TimeLimit;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.playerBack.OldPlayerBackManager;
import com.fy.engineserver.playerTitles.PlayerTitle;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.security.SecuritySubSystem;
import com.fy.engineserver.septbuilding.entity.JiazuBedge;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.soulpith.SoulPithEntityManager;
import com.fy.engineserver.soulpith.instance.SoulPithEntity;
import com.fy.engineserver.sound.manager.SoundManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse.RideHorseProgressBar;
import com.fy.engineserver.sprite.horse.dateUtil.DateFormat;
import com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.horse2.model.HorseRankModel;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.Monster.AttackRecord;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.FollowableNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.npc.TaskCollectionNPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetCellInfo;
import com.fy.engineserver.sprite.pet.PetFlyState;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.petdao.PetDao;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.stat.GameStatClientService;
import com.fy.engineserver.stat.StatData;
import com.fy.engineserver.stat.StatDataUpdateManager;
import com.fy.engineserver.talent.FlyTalentManager;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.trade.boothsale.BoothSale;
import com.fy.engineserver.trade.boothsale.service.BoothsaleManager;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ProbabilityUtils;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.RandomTool.RandomType;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.fy.engineserver.vip.VipManager;
import com.fy.engineserver.vip.data.VipAgent;
import com.fy.engineserver.worldmap.WorldMapManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.GameChongZhiFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.Message;

@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "name" }), @SimpleIndex(members = { "country" }, compress = 1), @SimpleIndex(members = { "level" }, compress = 1), @SimpleIndex(members = { "username" }), @SimpleIndex(name = "Player_lrt", members = { "lastRequestTime" }), @SimpleIndex(name = "Player_clrt", members = { "country", "lastRequestTime" }, compress = 1), @SimpleIndex(members = { "exp" }, compress = 1), @SimpleIndex(members = { "silver" }, compress = 1, name = "SILVER_IND") })
public class Player extends AbstractPlayer implements TaskEventListener, TaskConfig, Serializable {

	public static int gggNum = 10;
	@SimpleColumn(length = 1000)
	private String[] gameMsgs;

	public transient Object tradeKey = new Object();

	private static final long serialVersionUID = 3313716374641228095L;

	public static Logger attackRecordLogger = LoggerFactory.getLogger("player.attackrecord");

	// 被攻击的记录
	public transient ArrayList<AttackRecord> attackRecordList = new ArrayList<AttackRecord>();

	/**
	 * 表示强化用符标记
	 */
	public transient boolean usePaperFlag = false;
	
	public transient byte isnotice = 0;
	
	public transient int fateFlushNum;
	public transient int drinkFlushNum;

	private byte mainCareer;
	
	/**
	 * 该字段无效
	 */
	private List<Long> blessIds = new ArrayList<Long>();
	private int blessCount;
	private long blessDate;

	public int getBlessCount() {
		return blessCount;
	}

	public void setBlessCount(int blessCount) {
		this.blessCount = blessCount;
		setDirty(true, "blessCount");
	}

	public long getBlessDate() {
		return blessDate;
	}

	public void setBlessDate(long blessDate) {
		this.blessDate = blessDate;
		setDirty(true, "blessDate");
	}

	// 商城用银子，也是非流通银子
	private long shopSilver;
	/** 当天充值的rmb数量，小于0需要从网关重新获取值 */
	private transient long one_day_rmb = -1;
	public transient int newCareerFirstTaskId = 100827;
	/**
	 * 使用的何种符
	 */
	public transient String usePaperStr;

	public transient long usePaperTime = 0;

	// 战斗开始的时间
	private transient long fightingStartTime;

	// 荣誉值最大值
	public static final int MAX_HONOR_VALUE = 100000;

	// 离线时间
	private static final int MAX_TIME_VALUE = 40;

	// 十五天
	private static final long FIFTEENDAY = 1296000000;

	// 十天
	private static final long TENDAY = 864000000;

	// 三十天
	private static final long THIRTYDAY = 2592000000L;

	// 护盾类型
	public static final byte SHIELD_NONE = -1;

	// 每隔5分钟检查一次背包
	private static final long FIVE_MINUTES = 300000;

	// 每隔one分钟检查一次背包
	private static final long ONE_MINUTES = 60000;

	// 正常在线减少一点罪恶值所需时间
	public static final long ONE_EVIL_NEED_TIME = 600000;
	// 正常蹲监狱减少罪恶值记录分钟数
	public static final long ONE_DEVIL_RECORD_TIME = ONE_EVIL_NEED_TIME / 60000;

	// 团队标记，无团队标记
	public static final byte TEAM_MARK_NONE = 0;

	// 团队标记，是团队普通成员
	public static final byte TEAM_MARK_MEMBER = 1;

	// 团队标记，是团队的队长
	public static final byte TEAM_MARK_CAPTAIN = 2;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	// 是否第一次进入
	protected boolean firstEnter = true;

	// 玩家默认的分配规则(0自由 1顺序)
	private byte defaultAssignRule;
	// 是否每天给玩家增加体力值
	private boolean needAddVitality = false;
	// 最后一次增加体力值的时间
	private long lastAddVitalityTime = 0L;

	// 角色创建时间
	private long createTime;
	private boolean firstCharge;

	public transient Random random = new Random(System.currentTimeMillis());

	protected transient Connection conn;

	protected transient Passport passport;

	public static final int 原地复活最大银子 = 500000;

	// 当前所在地图名称
	@SimpleColumn(length = 200)
	private String game;
	// 上次心跳金钱
	public transient long lastHeatSilver;
	// 上次心跳的rmb
	public transient long lastHeatRmb;
	// 上次心跳时间
	public transient long lastHeatTime;

	public transient int optionSealStep;

	/** 客户端信息,每次登陆都要重置 */
	private transient Map<String, String> USER_CLIENT_INFO_map = null;

	public transient Map<Integer, Long> skillTouchSpace = new HashMap<Integer, Long>();

	/**
	 * 是否可以计算宠物飞升技能属性
	 * ps:骑飞升坐骑，在过图的时候同时操作下坐骑，宠物会出现2次出战
	 */
	public transient volatile boolean canCalPetFlySkill = true;

	public transient String crossMapName;
	public transient String crossIp;
	public transient String crossPort;
	public transient String crossClientPort;
	
	/**
	 * 跨服战类型，1：2v2，2：3v3，3：5v5，0：单人跨服
	 */
	public transient int crossBattleType;

	private VipAgent vipAgent = null;
	
	public transient boolean noticeShowButton;
	
	//0:初始值，1:登录，2:领取过奖励
	private long updateLoginTime;
	private int [] loginState = {0,0,0,0,0,0,0};
	private transient String [] rewardNames = {"第一日礼包","第二日礼包","第三日礼包","第四日礼包","第五日礼包","第六日礼包","第七日礼包"};
	
	private long[] lastBuyTime = {1,1,1};
	private int[] buyTimes = {0,0,0};
	
	public long[] getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(long[] lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
		setDirty(true, "lastBuyTime");
	}

	public int[] getBuyTimes() {
		return buyTimes;
	}

	public void setBuyTimes(int[] buyTimes) {
		this.buyTimes = buyTimes;
		setDirty(true, "buyTimes");
	}

	public boolean isFirstCharge() {
		return firstCharge;
	}

	public void setFirstCharge(boolean firstCharge) {
		this.firstCharge = firstCharge;
		setDirty(true, "firstCharge");
	}

	public String [] getRewardNames(){
		return rewardNames;
	}
	
	/**
	 * 记录时间
	 * 0:上次签到时间
	 */
	private long[] timeRecord = new long[10];
	
	public long[] getTimeRecord() {
		return timeRecord;
	}

	public void setTimeRecord(long[] timeRecord) {
		this.timeRecord = timeRecord;
		setDirty(true, "timeRecord");
	}

	public int[] getLoginState() {
		return loginState;
	}

	public long getUpdateLoginTime() {
		return updateLoginTime;
	}

	public void setUpdateLoginTime(long updateLoginTime) {
		this.updateLoginTime = updateLoginTime;
		setDirty(true, "updateLoginTime");
	}

	public void setLoginState(int[] loginState) {
		this.loginState = loginState;
		setDirty(true, "loginState");
	}
	
	public List<Long> getBlessIds() {
		return blessIds;
	}

	public void setBlessIds(List<Long> blessIds) {
		this.blessIds = blessIds;
		setDirty(true, "blessIds");
	}

	public void handleLoginReward(String reson){
		if(this.getLevel() < 10){
			return;
		}
		boolean result = TimeTool.instance.isSame(SystemTime.currentTimeMillis(), updateLoginTime, TimeDistance.DAY);
		if(!result){
			int index = -1;
			for(int i = 0;i < loginState.length;i++){
				if(loginState[i] == 0){
					index = i;
					break;
				}
			}
			if(index != -1){
				setUpdateLoginTime(System.currentTimeMillis());
				loginState[index] = 1;
				setLoginState(loginState);
				ActivityManagers.logger.info("[七日有礼] [处理登录] [reson:"+reson+"] [第"+(index+1)+"天登录] [登录状态:"+Arrays.toString(loginState)+"] ["+getLogString()+"]");
			}else{
				ActivityManagers.logger.info("[七日有礼] [登录已达7日上限] [reson:"+reson+"] [登录状态:"+Arrays.toString(loginState)+"] ["+getLogString()+"]");
			}
		}else{
			ActivityManagers.logger.info("[七日有礼] [同一天:"+result+"] [reson:"+reson+"] [登录状态:"+Arrays.toString(loginState)+"] ["+getLogString()+"]");
		}
	}
	
	public void getLoginReward(int day){
		try {
			if(day > loginState.length || day <= 0){
				ActivityManagers.logger.info("[七日有礼] [领取奖励] [失败:参数错误] [选择第"+(day)+"天] [登录状态:"+Arrays.toString(loginState)+"] ["+getLogString()+"]");
				return;
			}
			int state = loginState[day - 1];
			if(state == 2){
				sendError(Translate.已经领取七日奖励);
				ActivityManagers.logger.info("[七日有礼] [领取奖励] [失败:领取过] [选择第"+(day)+"天] [登录状态:"+Arrays.toString(loginState)+"] ["+getLogString()+"]");
				return;
			}
			if(state != 1){
				ActivityManagers.logger.info("[七日有礼] [领取奖励] [失败:功能错误] [选择第"+(day)+"天] [登录状态:"+Arrays.toString(loginState)+"] ["+getLogString()+"]");
				return;
			}
			String name = rewardNames[day - 1];
			Article aa = ArticleManager.getInstance().getArticle(name);
			ArticleEntity aee = ArticleEntityManager.getInstance().createEntity(aa, true, ArticleEntityManager.七日登录奖励, this, aa.getColorType(), 1, true);
			String content = Translate.translateString(Translate.七日登录奖励邮件, new String[][] { { Translate.STRING_1, day+"" } });
			MailManager.getInstance().sendMail(this.getId(), new ArticleEntity[]{aee}, content, content, 0, 0, 0, "七日登录奖励"+day);
			loginState[day - 1] = 2;
			setLoginState(loginState);
			sendError(Translate.领取七日奖励成功);
			ActivityManagers.logger.info("[七日有礼] [领取奖励] [成功] [选择第"+(day)+"天] [登录状态:"+Arrays.toString(loginState)+"] ["+getLogString()+"]");
		
		
			int [] states = this.getLoginState();
			String [] names = this.getRewardNames();
			String [] icons = new String[names.length];
			long [] aeIds1 = null;
			long [] aeIds2 = null;
			long [] aeIds3 = null;
			long [] aeIds4 = null;
			long [] aeIds5 = null;
			long [] aeIds6 = null;
			long [] aeIds7 = null;
			try {
				for(int i=0;i<names.length;i++){
					PackageProps a = (PackageProps)ArticleManager.getInstance().getArticle(names[i]);
					ArticleProperty [] articleNames = a.getArticleNames();
					icons[i] = a.getIconId();
					if(i==0){
						aeIds1 = new long[articleNames.length];
						for(int j=0;j<articleNames.length;j++){
							ArticleProperty ap = articleNames[j];
							Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
							ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
							if(ae == null){
								ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
							}
							aeIds1[j] = ae.getId();
	 					}
					}else if(i==1){
						aeIds2 = new long[articleNames.length];
						for(int j=0;j<articleNames.length;j++){
							ArticleProperty ap = articleNames[j];
							Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
							ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
							if(ae == null){
								ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
							}
							aeIds2[j] = ae.getId();
	 					}
					}else if(i==2){
						aeIds3 = new long[articleNames.length];
						for(int j=0;j<articleNames.length;j++){
							ArticleProperty ap = articleNames[j];
							Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
							ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
							if(ae == null){
								ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
							}
							aeIds3[j] = ae.getId();
	 					}
					}else if(i==3){
						aeIds4 = new long[articleNames.length];
						for(int j=0;j<articleNames.length;j++){
							ArticleProperty ap = articleNames[j];
							Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
							ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
							if(ae == null){
								ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
							}
							aeIds4[j] = ae.getId();
	 					}
					}else if(i==4){
						aeIds5 = new long[articleNames.length];
						for(int j=0;j<articleNames.length;j++){
							ArticleProperty ap = articleNames[j];
							Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
							ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
							if(ae == null){
								ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
							}
							aeIds5[j] = ae.getId();
	 					}
					}else if(i==5){
						aeIds6 = new long[articleNames.length];
						for(int j=0;j<articleNames.length;j++){
							ArticleProperty ap = articleNames[j];
							Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
							ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
							if(ae == null){
								ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
							}
							aeIds6[j] = ae.getId();
	 					}
					}else if(i==6){
						aeIds7 = new long[articleNames.length];
						for(int j=0;j<articleNames.length;j++){
							ArticleProperty ap = articleNames[j];
							Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
							ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
							if(ae == null){
								ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
							}
							aeIds7[j] = ae.getId();
	 					}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(aeIds1 == null || aeIds2 == null || aeIds3 == null || aeIds4 == null || aeIds5 == null || aeIds6 == null || aeIds7 == null){
				this.sendError(Translate.七日宝箱配置错误);
				ActivitySubSystem.logger.warn("[七日宝箱] [界面请求] [错误:配置错误] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]",
						new Object[]{aeIds1,aeIds2,aeIds3, aeIds4,aeIds5,aeIds6,aeIds7,this.getLogString()});
				return;
			}
			LOGIN_REWARD_RES res = new LOGIN_REWARD_RES(GameMessageFactory.nextSequnceNum(), states, names, icons, aeIds1, aeIds2, aeIds3, aeIds4, aeIds5, aeIds6, aeIds7);
			this.addMessageToRightBag(res);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			ActivityManagers.logger.info("[七日有礼] [领取奖励] [异常] [选择第"+(day)+"天] [登录状态:"+Arrays.toString(loginState)+"] ["+getLogString()+"]",e);
		}
	}
	
	//当天在线时长
	private long onlineTimeOfDay;
	
	//某些充值活动增长vip等级，不会给rmb
	private long viprmb;
	
	public long getViprmb() {
		return viprmb;
	}

	public void setViprmb(long viprmb) {
		this.viprmb = viprmb;
		setDirty(true, "viprmb");
		setTotalRmbyuanbao(viprmb);
		setVipLevel(getVipLevel());
		if (VipManager.getInstance() != null) {
			VipManager.getInstance().设置玩家的vip属性(this);
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(this, RecordAction.VIP等级, getVipLevel());
			}
		}
	}

	public long getOnlineTimeOfDay() {
		long time = System.currentTimeMillis() - getEnterServerTime() + onlineTimeOfDay;
		return time;
	}

	private long lastRewardTime;
	private int [] rewardState = {0,0,0,0};
	
	
	public long getLastRewardTime() {
		return lastRewardTime;
	}

	public void setLastRewardTime(long lastRewardTime) {
		this.lastRewardTime = lastRewardTime;
		setDirty(true, "lastRewardTime");
	}

	public int[] getRewardState() {
		return rewardState;
	}

	public void setRewardState(int[] rewardState) {
		this.rewardState = rewardState;
		setDirty(true, "rewardState");
	}

	public void setOnlineTimeOfDay(long onlineTimeOfDay) {
		this.onlineTimeOfDay = onlineTimeOfDay;
		setDirty(true, "onlineTimeOfDay");
	}


	/**
	 * 活动重载方法---vip经验翻倍
	 * @param rMb
	 * @param addType
	 */
	public void setRMB4Activity(long rMb, long add, int addType) {
//		long rr = add;
		// 活动判断
//		VipExpActivityManager vm = VipExpActivityManager.instance;
//		if (vm != null) {
//			rr = (long) (vm.getVipExpAddMul(this, addType) * rr);
//		}
//		rr += rMb;
		setRMB(rMb + add);
	}

	public void setRMB(long rMB) {
		super.setRMB(rMB);
		setDirty(true, "RMB");
		setVipLevel(getVipLevel());
		if (VipManager.getInstance() != null) {
			VipManager.getInstance().设置玩家的vip属性(this);
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(this, RecordAction.VIP等级, getVipLevel());
			}
		}
	}

	@SimpleColumn(length = 64)
	protected String username;

	// 上一次所在地图的名称
	private String lastGame = "";

	// 当前所在的游戏场景
	public transient Game currentGame = null;

	private transient boolean deathNotify = false;

	private transient TimerTaskAgent timerTaskAgent;

	private transient CountdownAgent countdownAgent;

	public transient String 村庄战临时复活点地图名 = "";

	public transient int 村庄战临时复活点x;

	public transient int 村庄战临时复活点y;

	public transient Object Lock_for_Ach = new Object();

	// 兽魁最近一次使用增加豆技能时间
	public transient long lastUseSkillTimeOfAddDou = 0;
	// 兽魁最近一次减豆时间
	public transient long lastMinusDouTime = 0;

	/**
	 * 一年中最近一次使用绑银的时间，一年中的某天使用了绑银 策划需求，每天使用绑银有上限，超过上限用银子代替
	 */
	@SimpleColumn(name = "useBindSilver")
	private int time_useBindSilver_yearOfDay = -1;

	// 银子的最高水位
	protected long highWaterOfSilver;

	// 玩家一共冲过多少钱，最小单位分
	protected long totalChongzhiRMB;

	// 玩家触发血少于30%的buff间隔时间
	private transient long lastBuffLength;

	public transient int sealStat = 0;

	public long getLastBuffLength() {
		return lastBuffLength;
	}

	public void setLastBuffLength(long lastBuffLength) {
		this.lastBuffLength = lastBuffLength;
	}

	public long getHighWaterOfSilver() {
		return highWaterOfSilver;
	}

	public void setHighWaterOfSilver(long highWaterOfSilver) {
		this.highWaterOfSilver = highWaterOfSilver;
		AchievementManager.getInstance().record(this, RecordAction.获得银子, highWaterOfSilver);
		this.setDirty(true, "highWaterOfSilver");
	}

	public long getTotalChongzhiRMB() {
		return totalChongzhiRMB;
	}

	public void setTotalChongzhiRMB(long totalChongzhiRMB) {
		this.totalChongzhiRMB = totalChongzhiRMB;
		this.setDirty(true, "totalChongzhiRMB");
	}

	public transient int transferGameCountry;

	public int getTransferGameCountry() {
		return transferGameCountry;
	}

	public void setTransferGameCountry(int transferGameCountry) {
		this.transferGameCountry = transferGameCountry;
	}

	public Player() {
		this.objectScale = 1000;
		this.objectColor = 0xFFFFFF;
		this.vitality = -1;
	}

	@Override
	public void setVitality(int value) {
		super.setVitality(value);
		this.setDirty(true, "vitality");
	}

	public synchronized void addVitality(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("[要增加体力值:" + value + "]");
		}
		if (vitality == -1 && value > 0) {
			vitality = 0;
		}

		if (vitality + value > getMaxVitality()) {
			setVitality(getMaxVitality());
		} else {
			setVitality(getVitality() + value);
		}
		this.checkFunctionNPCModify(ModifyType.THEW_UP);
	}

	public synchronized boolean subVitality(int value, String reson) {
		if (value < 0) {
			throw new IllegalArgumentException("[要扣除体力值:" + value + "]");
		}
		int oldVitality = getVitality();
		if (getVitality() < value) {
			return false;
		}
		setVitality(getVitality() - value);
		this.checkFunctionNPCModify(ModifyType.THEW_DOWN);
		if (GamePlayerManager.logger.isWarnEnabled()) {
			GamePlayerManager.logger.warn(this.getLogString() + "[扣除体力] [原有:" + oldVitality + "] [扣除:" + value + "] [扣除后:" + getVitality() + "] [" + reson + "]");
		}
		return true;
	}

	/**
	 * 目前召唤出来的法宝npcId
	 */
	private transient long activeMagicWeaponId = -1;
	/** 1：免疫减速 2:免疫定身 3:免疫晕眩 */
	private transient byte immuType = 0;
	/** 上次触发免疫时间 做内置cd */
	private transient long lastImmuTime = 0;

	/** 家族喝酒经验加成倍数 */
	private transient double extraHejiu;
	/** 是否同意翅膀副本 */
	public transient byte chiBangCarbonType;

	/**
	 * 目前召唤出来的宠物
	 */
	protected long activePetId;
	/** 1双攻  2雷  3风  4火  5冰 */
	public transient int attributeAddType = 1;
	
	public transient long crossDeadPetId;

	private long flyPackupPetId;
	
	private List<Long> petCell = new ArrayList<Long>();
	private int petAddPropId;
	
	public int getPetAddPropId() {
		if(getLevel() >= 150){
			if(petAddPropId == 0){
				petAddPropId = 1;
				setDirty(true, "petAddPropId");
			}
		}
		return petAddPropId;
	}
	
	
	public void handlerPetProp2Player(){
		
		phyAttackZ = 0;
		magicAttackZ = 0;
		phyDefenceZ = 0;
		magicDefenceZ = 0;
		hitZ = 0;
		dodgeZ = 0;
		criticalHitZ = 0;
		
		for(long id : getPetCell()){
			if(id <= 0){
				continue;
			}
			Pet pet = PetManager.getInstance().getPet(id);
			if(pet != null){
				phyAttackZ+=pet.getPhyAttack();
				magicAttackZ += pet.getMagicAttack();
				phyDefenceZ+=pet.getPhyDefence();
				magicDefenceZ+=pet.getMagicDefence();
				hitZ+=pet.getHit();
				dodgeZ+=pet.getDodge();
				criticalHitZ+=pet.getCriticalHit();
			}
		}
		
		List<PetCellInfo> cellInfos = PetManager.getInstance().getCellInfos();
		int mess = 0;
		for(PetCellInfo info : cellInfos){
			if(info != null && info.getId() == this.getPetAddPropId()){
				mess = info.getAddProp();
				break;
			}
		}
		phyAttackZ = (int)(phyAttackZ * mess /100);
		magicAttackZ = (int)(magicAttackZ * mess /100);
		phyDefenceZ = (int)(phyDefenceZ * mess /100);
		magicDefenceZ = (int)(magicDefenceZ *mess/100);
		hitZ = (int)(hitZ * mess/100);
		dodgeZ = (int)(dodgeZ * mess/100);
		criticalHitZ = (int)(criticalHitZ * mess/100);
		
		setPhyAttackZ(phyAttackZ);
		setMagicAttackZ(magicAttackZ);
		setPhyDefenceZ(phyDefenceZ);
		setMagicDefenceZ(magicDefenceZ);
		setHitZ(hitZ);
		setDodgeZ(dodgeZ);
		setCriticalHitZ(criticalHitZ);
		
		PetManager.logger.warn("[处理助战宠物属性] [addProp:{}] [phyAttackZ:{}] [magicAttackZ:{}] [phyDefenceZ:{}] [magicDefenceZ:{}] [hitZ:{}] [dodgeZ:{}] [criticalHitZ:{}] [ids:{}] [{}]",
				new Object[]{mess,phyAttackZ,magicAttackZ,phyDefenceZ,magicDefenceZ,hitZ,dodgeZ,criticalHitZ, getPetCell(),this.getLogString()});
	}

	public void setPetAddPropId(int petAddPropId) {
		this.petAddPropId = petAddPropId;
		setDirty(true, "petAddPropId");
	}

	public List<Long> getPetCell() {
		return petCell;
	}

	public void setPetCell(List<Long> petCell) {
		this.petCell = petCell;
		setDirty(true, "petCell");
	}

	public long getFlyPackupPetId() {
		return flyPackupPetId;
	}
	//双攻   雷 风 火 冰
		public transient int[] extraAttributeAddRates = new int[]{0, 0, 0, 0, 0};
		
		public void notifyAttributeAttackChange() {
			Arrays.fill(extraAttributeAddRates, 0);
			Pet p = PetManager.getInstance().getPet(activePetId);
			if (p != null && p.isAlive() && p.getHp() > 0 && p.attributeAddRate > 0) {
				extraAttributeAddRates[attributeAddType-1] = p.attributeAddRate;
			}
			this.setPhyAttackC(this.getPhyAttackC());
			this.setMagicAttackC(this.getMagicAttackC());
			this.setThunderAttackC(this.getThunderAttackC());
			this.setWindAttackC(this.getWindAttackC());
			this.setFireAttackC(this.getFireAttackC());
			this.setBlizzardAttackC(this.getBlizzardAttackC());
			attributeAddType++;
			if (attributeAddType > extraAttributeAddRates.length) {
				attributeAddType = 1;
			}
		}

	public void setFlyPackupPetId(long flyPackupPetId) {
		this.flyPackupPetId = flyPackupPetId;
		setDirty(true, "flyPackupPetId");
	}

	/** 1：人物经验 2:神兽碎片 3:宠物经验 4:道具 */
	private transient byte prayType = 0;

	public byte getPrayType() {
		return prayType;
	}

	public void setPrayType(byte prayType) {
		this.prayType = prayType;
	}

	/** 遇怪buffId */
	private transient int meetMonsterBuffId = -1;

	public int getMeetMonsterBuffId() {
		return meetMonsterBuffId;
	}

	public void setMeetMonsterBuffId(int meetMonsterBuffId) {
		this.meetMonsterBuffId = meetMonsterBuffId;
	}

	/** 仙灵积分卡buff */
	private transient int scoreBuff;

	public int getScoreBuff() {
		return scoreBuff;
	}

	public void setScoreBuff(int scoreBuff) {
		this.scoreBuff = scoreBuff;
	}

	/**
	 * 是否处于封印状态
	 * 
	 * @return
	 */
	public boolean getSealState() {
		SealManager sm = SealManager.getInstance();
		if (sm != null) {
			if (sm.getSealLevel() <= 0) {
				return false;
			}
			if (this.getSoulLevel() >= sm.getSealLevel()) {
				return true;
			}
		}
		return false;
	}

	public void flushSealState() {
		if (sealState != getSealState()) {
			setSealState(!sealState);
		}
	}

	/**
	 * 是否是运镖状态
	 */
	transient boolean yunbiao = false;

	/**
	 * 国王使用国王技能后技能冷却
	 */
	public transient boolean kingSkillCoolDown;

	public boolean isKingSkillCoolDown() {
		return kingSkillCoolDown;
	}

	public void setKingSkillCoolDown(boolean kingSkillCoolDown) {
		this.kingSkillCoolDown = kingSkillCoolDown;
	}

	// 上一次请求时间，此时间为客户端发送的最后一次协议请求时间，为服务器时间
	@SimpleColumn(saveInterval = 600)
	private long lastRequestTime = System.currentTimeMillis();

	/** 跟随的NPC，唯一，不存库 */
	private transient FollowableNPC followableNPC;

	public transient Map<Byte, Long> attackBiaoCheFlag = new Hashtable<Byte, Long>();
	public transient Map<Integer, Integer> taneltSkillTempAddPoints = new Hashtable<Integer, Integer>();

	/**
	 * 摆摊信息
	 */
	protected transient BoothSale boothSale;
	// 所观察的玩家摊位
	private transient long seeBoothSale;
	
	private long endBoothTime;

	/**
	 * 角色的洞府ID
	 */
	private long caveId;
	/** 洞府所在的FAERY */
	private long faeryId;

	/** 成就值 */
	private long achievementDegree;
	/** 寻人活动数据 */
	private PeopleSearch peopleSearch;
	/** 最后一次寻人时间,接取时间 */
	private long lastSearchPeopleTime;
	/** 当天寻人的次数 */
	private int dailySearchPeopleNum;
	
	public transient BossRoom room;
	public transient BossCity bcity;
	
	
	int drinkTimes = 0;
	long lastDrinkDate = 0;
	
	int tieTimes = 0;
	long lastTieDate = 0;
	
	
	
	public int getTieTimes() {
		return tieTimes;
	}

	public void setTieTimes(int tieTimes) {
		this.tieTimes = tieTimes;
		setDirty(true, "tieTimes");
	}

	public long getLastTieDate() {
		return lastTieDate;
	}

	public void setLastTieDate(long lastTieDate) {
		this.lastTieDate = lastTieDate;
		setDirty(true, "lastTieDate");
	}

	private long lastCityTime;

	public long getLastSearchPeopleTime() {
		return lastSearchPeopleTime;
	}

	
	
	public int getDrinkTimes() {
		return drinkTimes;
	}

	public void setDrinkTimes(int drinkTimes) {
		this.drinkTimes = drinkTimes;
		setDirty(true, "drinkTimes");
	}

	public long getLastDrinkDate() {
		return lastDrinkDate;
	}

	public void setLastDrinkDate(long lastDrinkDate) {
		this.lastDrinkDate = lastDrinkDate;
		setDirty(true, "lastDrinkDate");
	}

	public long getLastCityTime() {
		return lastCityTime;
	}

	public void setLastCityTime(long lastCityTime) {
		this.lastCityTime = lastCityTime;
		setDirty(true, "lastCityTime");
	}

	public void setLastSearchPeopleTime(long lastSearchPeopleTime) {
		this.lastSearchPeopleTime = lastSearchPeopleTime;
		setDirty(true, "lastSearchPeopleTime");
	}

	public int getDailySearchPeopleNum() {
		return dailySearchPeopleNum;
	}

	public void setDailySearchPeopleNum(int dailySearchPeopleNum) {
		this.dailySearchPeopleNum = dailySearchPeopleNum;
		setDirty(true, "dailySearchPeopleNum");
	}

	/**
	 * 得到当天做过的寻人次数
	 * 
	 * @return
	 */
	public int getTodaySearchePeopleNum() {
		if (!TimeTool.instance.isSame(SystemTime.currentTimeMillis(), this.getLastSearchPeopleTime(), TimeDistance.DAY)) {
			// 如果不是同一天的,当天完成次数设置成0
			setDailySearchPeopleNum(0);
			return 0;
		}
		return getDailySearchPeopleNum();
	}

	/**
	 * 增加寻人次数,一次加一次
	 */
	public synchronized void addPeopleSearchNum() {
		if (!TimeTool.instance.isSame(SystemTime.currentTimeMillis(), this.getLastSearchPeopleTime(), TimeDistance.DAY)) {
			setDailySearchPeopleNum(1);
		} else {
			setDailySearchPeopleNum(getDailySearchPeopleNum() + 1);
		}
	}
	
	public long getEndBoothTime() {
		return endBoothTime;
	}

	public void setEndBoothTime(long endBoothTime) {
		this.endBoothTime = endBoothTime;
		setDirty(true, "endBoothTime");
	}

	/**
	 * 初始化斩妖降魔
	 */
	private void initPeopleSearch() {
		try {
			if (peopleSearch != null) {
				int templetId = peopleSearch.getTempletId();
				PeopleTemplet peopleTemplet = PeopleSearchManager.getInstance().getPeopleTemplet(templetId);
				if (peopleTemplet == null) {
					this.setPeopleSearch(null);
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(this.getLogString() + "[寻人加载错误] [模板没找到,直接置空] [模板ID:" + templetId + "]");
					}
				} else {
					peopleSearch.setPeopleTemplet(peopleTemplet);
					peopleSearch.setOwner(this);
				}
			}
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(this.getLogString() + "[寻人加载完成]");
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error(this.getLogString() + "[寻人加载异常]", e);
		}
	}

	public PeopleSearch getPeopleSearch() {
		return peopleSearch;
	}

	public void setPeopleSearch(PeopleSearch peopleSearch) {
		this.peopleSearch = peopleSearch;
		setDirty(true, "peopleSearch");
	}

	public long getAchievementDegree() {
		return achievementDegree;
	}

	private void setAchievementDegree(long achievementDegree) {
		this.achievementDegree = achievementDegree;
		setDirty(true, "achievementDegree");
	}

	public synchronized void addAchievementDegree(long achievementDegree) {
		if (achievementDegree < 0) {
			throw new IllegalArgumentException("[addAchievementDegree:" + achievementDegree + "]");
		}
		if (Long.MAX_VALUE - achievementDegree < getAchievementDegree()) {
			throw new IllegalArgumentException("[addAchievementDegree:" + achievementDegree + "] [hasAchievementDegree:" + getAchievementDegree() + "]");
		}
		setAchievementDegree(getAchievementDegree() + achievementDegree);
	}

	public static final byte 和平模式 = 0;
	public static final byte 全体模式 = 1;
	public static final byte 组队模式 = 2;
	public static final byte 家族模式 = 3;
	public static final byte 宗派模式 = 4;
	public static final byte 国家模式 = 5;
	public static final byte 善恶模式 = 6;

	public static final byte 最大境界 = 11;

	public long getCaveId() {
		return caveId;
	}

	public void setCaveId(long caveId) {
		this.caveId = caveId;
		setDirty(true, "caveId");
	}

	public long getFaeryId() {
		return faeryId;
	}

	public ArrayList<Buff> getBuffs() {
		return buffs;
	}

	public void setFaeryId(long faeryId) {
		this.faeryId = faeryId;
		setDirty(true, "faeryId");
	}

	public void setJiazuId(long value) {
		super.setJiazuId(value);
		setDirty(true, "jiazuId");
	}

	/** 当前激活的元神 */

	private Soul currSoul;

	/** 闲置的元神列表 */

	private List<Soul> unusedSoul = new ArrayList<Soul>();

	/** 最后一次切换元神时间 */
	private long lastSwitchSoulTime;
	/** 最后一次获得境界经验时间 */
	private transient long lastGotBournExpTime;
	/** 当前刷新的任务的名字 */
	private String currBournTaskName;
	/** 当前刷新任务的星级 */
	private int currBournTaskStar;
	/** 最后一次增加境界任务和打坐时间的时间 */
	private long lastCountBournTime;

	public long getLastCountBournTime() {
		return lastCountBournTime;
	}

	public void setLastCountBournTime(long lastCountBournTime) {
		this.lastCountBournTime = lastCountBournTime;
		this.setDirty(true, "lastCountBournTime");
	}

	public String getCurrBournTaskName() {
		return currBournTaskName;
	}

	public void setCurrBournTaskName(String currBournTaskName) {
		this.currBournTaskName = currBournTaskName;
		this.setDirty(true, "currBournTaskName");
	}

	public int getCurrBournTaskStar() {
		return currBournTaskStar;
	}

	public void setCurrBournTaskStar(int currBournTaskStar) {
		this.currBournTaskStar = currBournTaskStar;
		this.setDirty(true, "currBournTaskStar");
	}

	public long getLastGotBournExpTime() {
		return lastGotBournExpTime;
	}

	public void setLastGotBournExpTime(long lastGotBournExpTime) {
		this.lastGotBournExpTime = lastGotBournExpTime;
	}

	public boolean isNeedAddVitality() {
		return needAddVitality;
	}

	public void setNeedAddVitality(boolean needAddVitality) {
		this.needAddVitality = needAddVitality;
		this.setDirty(true, "needAddVitality");
	}

	public long getLastAddVitalityTime() {
		return lastAddVitalityTime;
	}

	public void setLastAddVitalityTime(long lastAddVitalityTime) {
		this.lastAddVitalityTime = lastAddVitalityTime;
		setDirty(true, "lastAddVitalityTime");
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		setDirty(true, "createTime");
	}

	/** 角色上一个传送位置 */
	// private Position lastPosition;
	//
	// public Position getLastPosition() {
	// return lastPosition;
	// }
	//
	// public void setLastPosition(Position lastPosition) {
	// this.lastPosition = lastPosition;
	// setDirty(true);
	// }

	public Soul getCurrSoul() {
		return currSoul;
	}

	public void setCurrSoul(Soul currSoul) {
		this.currSoul = currSoul;
		this.setDirty(true, "currSoul");
	}

	public List<Soul> getUnusedSoul() {
		return unusedSoul;
	}

	public void setUnusedSoul(List<Soul> unusedSoul) {
		this.unusedSoul = unusedSoul;
	}

	public long getLastSwitchSoulTime() {
		return lastSwitchSoulTime;
	}

	public void setLastSwitchSoulTime(long lastSwitchSoulTime) {
		this.lastSwitchSoulTime = lastSwitchSoulTime;
		this.setDirty(true, "lastSwitchSoulTime");
	}
	
	

	// private

	public long getLastVipLoginRewardTime() {
		return lastVipLoginRewardTime;
	}

	public void setLastVipLoginRewardTime(long lastVipLoginRewardTime) {
		this.lastVipLoginRewardTime = lastVipLoginRewardTime;
		this.setDirty(true, "lastVipLoginRewardTime");
	}

	public Soul[] getSouls() {
		int length = 1;
		if (getUnusedSoul() != null) {
			length += getUnusedSoul().size();
		}
		Soul[] soul = new Soul[length];
		soul[0] = getCurrSoul();
		if (getUnusedSoul() != null) {
			for (int i = 0; i < getUnusedSoul().size(); i++) {
				soul[i + 1] = getUnusedSoul().get(i);
			}
		}
		return soul;
	}

	/******************* 活动数据记录 **********************/
	@SimpleColumn(name = "lastExchangeForluck")
	private long lastExchangeForluckFriteTime;// 最后一次兑换祝福果时间

	@SimpleColumn(name = "totalExchangeForluck")
	private int totalExchangeForluckFriteTimes;// 兑换祝福果总次数

	@SimpleColumn(name = "currentExchangeForluck")
	private int currentExchangeForluckFriteTimes;// 当前兑换祝福果总次数

	private transient int farmingPlateResult = -1;// 神农活动转盘的结果[水果的ID] 用后置为-1

	/** 最后一次刺探时间 */
	private long lastPeekTime;

	/** 最后一次救援镖车时间 */
	private transient long lastHelpSilvercarTime;
	/** 最后一次偷盗果实时间 */
	private long lastStealFruitTime;
	/** 当天偷盗果实次数 */
	private long dailyStealFruitNum;
	
	
	/**
	 * 这些字段暂时没用到
	 */
	private long lastLevelUpTime;
	private long lastUpdateFavorDegreesTime;
	
	public void setSkillPoints(int skillPoints) {
		super.setSkillPoints(skillPoints);
	}

	/** 飞升天赋附体状态 **/
	public transient int flyState;

	public transient String[] oldAvata = null;

	public transient static String[][] bianshenAvata = new String[][] { { "/part/ZJ_bianshenqiongqi.xtl", "/part/gongji01_ZJ_bianshenqiongqi.xtl" }, { "/part/ZJ_bianshenqiongqi01.xtl", "/part/gongji01_ZJ_bianshenqiongqi01.xtl" }, { "/part/ZJ_bianshenqiongqi02.xtl", "/part/gongji02_ZJ_bianshenqiongqi02.xtl" } };
	public transient static String[][] bianshenAvata4Feisheng = new String[][] { { "/part/XZJ_bianshenqiongqi.xtl", "/part/gongji01_XZJ_bianshenqiongqi.xtl" }, { "/part/XZJ_bianshenqiongqi01.xtl", "/part/gongji01_XZJ_bianshenqiongqi01.xtl" }, { "/part/XZJ_bianshenqiongqi02.xtl", "/part/gongji02_XZJ_bianshenqiongqi02.xtl" } };

	public void setShouStat(int shouStat) {
		boolean changeAvata = false;
		if (this.getShouStat() != shouStat) {
			changeAvata = true;
		}
		super.setShouStat(shouStat);
		if (changeAvata) {
			if (this.isShouStatus()) {
				oldAvata = this.getAvata();
				if (this.getAvataRace().equals(Constants.race_human_new)) {
					this.setAvata(bianshenAvata4Feisheng[this.playerRank]);
				} else {
					this.setAvata(bianshenAvata[this.playerRank]);
				}
				this.setAvataType(new byte[] { 0, 13 });
			} else {
				ResourceManager.getInstance().getAvata(this);
			}
		}
		this.setSpeedC(this.getSpeedC());
		this.setCriticalHitRate(this.getCriticalHitRate());
	}

	public void modifyShouAvata() {
		try {
			if (this.isShouStatus()) {
				oldAvata = this.getAvata();
				if (this.getAvataRace().equals(Constants.race_human_new)) {
					this.setAvata(bianshenAvata4Feisheng[this.playerRank]);
				} else {
					this.setAvata(bianshenAvata[this.playerRank]);
				}
				this.setAvataType(new byte[] { 0, 13 });
			}
		} catch (Exception e) {
			Game.logger.warn("[修正兽魁兽形态] [异常] [" + this.getLogString() + "][", e);
		}
	}

	public void setBianShenLevels(byte[] bianShenLevels) {
		super.setBianShenLevels(bianShenLevels);
		Soul s = this.getCurrSoul();
		if (s != null) {
			s.setBianShenLevels(getBianShenLevels());
			setDirty(true, "currSoul");
		}
	}

	/******************* 活动数据记录 **********************/

	public long getLastExchangeForluckFriteTime() {
		return lastExchangeForluckFriteTime;
	}

	public long getLastStealFruitTime() {
		return lastStealFruitTime;
	}

	public void setLastStealFruitTime(long lastStealFruitTime) {
		this.lastStealFruitTime = lastStealFruitTime;
		setDirty(true, "lastStealFruitTime");
	}

	public long getDailyStealFruitNum() {
		return dailyStealFruitNum;
	}

	public void setDailyStealFruitNum(long dailyStealFruitNum) {
		this.dailyStealFruitNum = dailyStealFruitNum;
		setDirty(true, "dailyStealFruitNum");
	}

	public long getLastHelpSilvercarTime() {
		return lastHelpSilvercarTime;
	}

	public void setLastHelpSilvercarTime(long lastHelpSilvercarTime) {
		this.lastHelpSilvercarTime = lastHelpSilvercarTime;
	}

	public long getLastPeekTime() {
		return lastPeekTime;
	}

	public void setLastPeekTime(long lastPeekTime) {
		this.lastPeekTime = lastPeekTime;
		setDirty(true, "lastPeekTime");
	}

	public int getFarmingPlateResult() {
		return farmingPlateResult;
	}

	public void setFarmingPlateResult(int farmingPlateResult) {
		this.farmingPlateResult = farmingPlateResult;
	}

	public void setLastExchangeForluckFriteTime(long lastExchangeForluckFriteTime) {
		this.lastExchangeForluckFriteTime = lastExchangeForluckFriteTime;
		setDirty(true, "lastExchangeForluckFriteTime");
	}

	public int getTotalExchangeForluckFriteTimes() {
		return totalExchangeForluckFriteTimes;
	}

	public void setTotalExchangeForluckFriteTimes(int totalExchangeForluckFriteTimes) {
		this.totalExchangeForluckFriteTimes = totalExchangeForluckFriteTimes;
		setDirty(true, "totalExchangeForluckFriteTimes");
	}

	public int getCurrentExchangeForluckFriteTimes() {
		return currentExchangeForluckFriteTimes;
	}

	public void setCurrentExchangeForluckFriteTimes(int currentExchangeForluckFriteTimes) {
		this.currentExchangeForluckFriteTimes = currentExchangeForluckFriteTimes;
		setDirty(true, "currentExchangeForluckFriteTimes");
	}

	/**
	 * 激活元神(只能激活一次)<BR/>
	 * 1.类型错误<BR/>
	 * 2.要激活的元神已经存在了<BR/>
	 * 3.开启元神条件未达到<BR/>
	 * 4.元神职业不符<BR/>
	 * 
	 * @param career
	 * @param siFangType
	 * @return
	 */
	public CompoundReturn activationSoul(int soulType, int career) {
		if (soulType > Soul.SOUL_TYPE_SOUL) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		Soul soul = getSoul(soulType);
		if (soul != null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		if (this.getLevel() < Soul.SOUL_OPEN_LEVEL) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		if ((career - 1) / 2 != (this.getCareer() - 1) / 2) {// 不同性别不能激活
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}
		if (ArticleManager.logger.isWarnEnabled()) {
			ArticleManager.logger.warn("[激活元神前]" + this.getPlayerPropsString() + this.getPlayerEquipment());
		}
		Soul sou = new Soul(soulType);
		sou.setGrade(Soul.SOUL_OPEN_LEVEL);
		sou.setCareer((byte) career);
		sou.setCareerBasicSkillsLevels(new byte[] { 1, 1, 1, 1, 1, 1 });
		sou.setBianShenLevels(new byte[] { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
		sou.getEc().setOwner(this);
		sou.account(this);
		if (getUnusedSoul() == null) {
			setUnusedSoul(new ArrayList<Soul>());
		}
		getUnusedSoul().add(sou);
		setDirty(true, "unusedSoul");
//		 {
//			 // 奖励坐骑
//			 String articleName = null;
//			 switch (career) {
//			 case 1:
//			 articleName = 躁狂野牛;
//			 break;
//			 case 2:
//			 articleName = 离火幼狼;
//			 break;
//			 case 3:
//			 articleName = 日曜幼虎;
//			 break;
//			 case 4:
//			 articleName = 幽毒幼蝎;
//			 break;
//			
//			 default:
//			 break;
//			 }
//			 if (articleName != null) {
//			 Article article = ArticleManager.getInstance().getArticle(articleName);
//			 if (article != null) {
//			 try {
//			 ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_LEVELUP, this, article.getColorType(), 1, true);
//			 MailManager.getInstance().sendMail(this.getId(), new ArticleEntity[] { ae }, Translate.奖励坐骑, Translate.translateString(Translate.恭喜你激活元神获得坐骑, new String[][] { {
//			 Translate.STRING_1, articleName } }), 0, 0, 0, "激活元神");
//			 this.sendError(Translate.translateString(Translate.恭喜你激活元神获得坐骑速去查看邮件, new String[][] { { Translate.STRING_1, articleName } }));
//			 CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [元神类型:" + getCurrSoul().getSoulType() + "] [元神等级:" + getCurrSoul().getGrade() + "] [奖励坐骑:" + articleName +
//			 "] [成功]");
//			 if (ArticleManager.logger.isWarnEnabled()) {
//			 ArticleManager.logger.warn(this.getPlayerPropsString() + this.getPlayerEquipment() + "[激活元神后]");
//			 }
//			 } catch (Exception e) {
//			 CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [元神类型:" + getCurrSoul().getSoulType() + "] [元神等级:" + getCurrSoul().getGrade() + "] [奖励坐骑:" + articleName +
//			 "] [异常]", e);
//			 }
//			 } else {
//			 CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [元神类型:" + getCurrSoul().getSoulType() + "] [元神等级:" + getCurrSoul().getGrade() + "] [坐骑不存在:" + articleName +
//			 "]");
//			 }
//			 } else {
//				 CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [元神类型:" + getCurrSoul().getSoulType() + "] [元神等级:" + getCurrSoul().getGrade() + "] [坐骑未配置]");
//			 }
//		 }
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	// 是否离开了服务器
	protected transient boolean leavedServer = false;

	// 角色交易状态,0正常状态1摆摊2交易
	protected transient byte tradeState;

	public byte getTradeState() {
		return tradeState;
	}

	public void setTradeState(byte tradeState) {
		this.tradeState = tradeState;
	}


	/**
	 * 进入游戏场景的时间
	 */
	private long enterGameTime = 0;


	/**
	 * 切磋开始时间
	 */
	protected long qiecuoStartTime;

	private long lastVipLoginRewardTime;

	/**
	 * 主动离开师傅的时间
	 */
	long leaveMasterTime;

	/**
	 * 驱逐徒弟的时间
	 */
	long banishPrenticeTime;

	/**
	 * 最后更新社交BUFF的时间
	 */
	long lastUpdateRelationBuffTime;

	/**
	 * 师徒积分
	 */
	long masterFunds;

	/**
	 * 充值积分
	 */
	long chargePoints;

	/**
	 * 老玩家送积分时间
	 */
	long chargePointsSendTime;

	/**
	 * 最后检测称号的时间
	 */
	long lastCheckHonorTime;

	/**
	 * 竞技场点数
	 */
	long jjcPoint;

	/**
	 * 荣誉点数,跨服pk货币
	 */
	long honorPoint;

	/**
	 * 当天奖励荣誉点数次数
	 */
	public int rewardHonorPointTimes;

	/**
	 * 跨服奖励时间
	 */
	public long rewardHonorPointDate;

	int refreshCrossShopTimes;
	long lastRefreshCrossShopDate;
	public List<Integer> goodName = new ArrayList<Integer>();

	/**
	 * 开始匹配时间
	 */
	public transient long startMatchTime;
	public long pkWinNum;
	public long pkAllNum;

	/**
	 * 上次匹配的玩家id
	 */
	public transient long lastMatchId;
	
	public boolean firstOpenCrossShop = true;

	public transient boolean noticeLostBattle;
	public transient boolean noticeLostBattle2;
	public transient boolean wineLostBattle;
	public transient boolean openRoomPage;

	/**
	 * 玩家在比武场的状态 0：未进入 1：参赛者 2：旁观者
	 */
	public transient int duelFieldState;

	/**
	 * 得到进入游戏的时间
	 * 
	 * @return
	 */
	public long getEnterGameTime() {
		return enterGameTime;
	}

	public boolean isFirstOpenCrossShop() {
		return firstOpenCrossShop;
	}

	public void setFirstOpenCrossShop(boolean firstOpenCrossShop) {
		this.firstOpenCrossShop = firstOpenCrossShop;
		setDirty(true, "firstOpenCrossShop");
	}

	public List<Integer> getGoodName() {
		return goodName;
	}

	public void setGoodName(List<Integer> goodName) {
		this.goodName = goodName;
		setDirty(true, "goodName");
	}

	public void setEnterGameTime(long enterGameTime) {
		this.enterGameTime = enterGameTime;
		setDirty(true, "enterGameTime");
	}

	public long getPkWinNum() {
		return pkWinNum;
	}

	public void setPkWinNum(long pkWinNum) {
		this.pkWinNum = pkWinNum;
		setDirty(true, "pkWinNum");
	}

	public long getPkAllNum() {
		return pkAllNum;
	}

	public void setPkAllNum(long pkAllNum) {
		this.pkAllNum = pkAllNum;
		setDirty(true, "pkAllNum");
	}

	/**
	 * 返回当前游戏
	 * 
	 * @return
	 * 
	 */
	public Game getCurrentGame() {
		return currentGame;
	}

	/**
	 * 周围通知玩家的个数
	 */
	public int aroundNotifyPlayerNum = 255;

	/**
	 * 玩家是否允许设置周围玩家个数
	 */
	private transient boolean newlySetAroundNotifyPlayerNum = false;

	public void setNewlySetAroundNotifyPlayerNum(boolean b) {
		newlySetAroundNotifyPlayerNum = b;
	}

	public boolean isNewlySetAroundNotifyPlayerNum() {
		return newlySetAroundNotifyPlayerNum;
	}

	/**
	 * 设置荣誉值
	 */
	public void setHonorValue(int value) {
		if (value > Player.MAX_HONOR_VALUE) {
			value = Player.MAX_HONOR_VALUE;
		}
		// super.setHonorValue(value);
	}

	/**
	 * 周围玩家的通知数量
	 * 
	 * @return
	 */
	public int getAroundNotifyPlayerNum() {
		return aroundNotifyPlayerNum;
	}

	/**
	 * 此方法一定要非常小心
	 * 
	 * @param num
	 */
	public void setAroundNotifyPlayerNum(int num) {
		if (aroundNotifyPlayerNum == 0 && num > 0) {
			this.setNewlyEnterGameFlag(true);
		}
		aroundNotifyPlayerNum = num;
		setNewlySetAroundNotifyPlayerNum(true);
		// Game.logger.warn("[玩家设置周围玩家数量] [" + this.getUsername() + "] [" +
		// this.getName() + "] [数量:" + aroundNotifyPlayerNum +
		// "] [数量为0将屏蔽所有的非队友和敌对玩家]");
		if (Game.logger.isWarnEnabled()) Game.logger.warn("[玩家设置周围玩家数量] [{}] [{}] [数量:{}] [数量为0将屏蔽所有的非队友和敌对玩家]", new Object[] { this.getUsername(), this.getName(), aroundNotifyPlayerNum });
	}

	/**
	 * 属性点分配方案
	 * 
	 * @see ExperienceManager#propertyPointAllocatPlans
	 * 
	 */
	int propertyPointAllocatePlan = 0;

	transient Fighter currentTargetOfClient = null;

	/**
	 * 
	 * 当前目标，-1标识没有目标
	 * 
	 * @param siFangType
	 * @param id
	 */
	public void setCurrentTarget(Fighter f) {
		currentTargetOfClient = f;
	}

	public Fighter getCurrentTarget() {
		return currentTargetOfClient;
	}

	// 怪物掉落金钱获得的百分比，缺省为0
	protected transient int flopMoneyPercent;

	public int getFlopMoneyPercent() {
		return flopMoneyPercent;
	}

	public void setFlopMoneyPercent(int flopMoneyPercent) {
		this.flopMoneyPercent = flopMoneyPercent;
	}

	// 怪物掉落获得的百分比，缺省为0
	protected transient int flopratePercent;

	public int getFlopratePercent() {
		return flopratePercent;
	}

	public void setFlopratePercent(int flopratePercent) {
		this.flopratePercent = flopratePercent;
	}

	/**
	 * 当前所在的地图区域名称
	 */
	protected transient String[] currentMapAreaNames;

	public String[] getCurrentMapAreaNames() {
		return currentMapAreaNames;
	}

	public void setCurrentMapAreaNames(String[] s) {
		this.currentMapAreaNames = s;
	}

	/**
	 * 当前所在的地图区域名称
	 */
	protected transient String currentMapAreaName;

	public String getCurrentMapAreaName() {
		return currentMapAreaName;
	}

	public void setCurrentMapAreaName(String s) {
		this.currentMapAreaName = s;
	}

	/**
	 * 人物身体上的Buff，这个数组的下标对应buffType 故，同一个buffType的buff在人物身上只能有一个
	 * 
	 * 此数据是要存盘的
	 * 
	 */
	@SimpleColumn(length = 16000, saveInterval = 600)
	private ArrayList<Buff> buffs = new ArrayList<Buff>();

	/**
	 * 下一次心跳要通知客户端去除的buff
	 */
	public transient ArrayList<Buff> removedBuffs = new ArrayList<Buff>();

	/**
	 * 下一次心跳要去通知客户端新增加的buff
	 */
	private transient ArrayList<Buff> newlyBuffs = new ArrayList<Buff>();

	// ////////////////////////////// 人物身上的装备栏相关 //////////////////////////////
	/**
	 * 装备栏，人物支持多套装备栏
	 * 
	 * 此数据是要存盘的
	 */
	private transient EquipmentColumn ecs;

	/**
	 * 玩家身上的装备栏
	 * 
	 * @return
	 */
	public EquipmentColumn getEquipmentColumns() {
		return ecs;
	}

	public void setEquipmentColumns(EquipmentColumn ecs) {
		this.ecs = ecs;
	}

	// //////////////////////////////////////////////// 人物身上的背包相关
	// //////////////////////////////////////////////////////////////////
	/**
	 * 人物身上的背包， 背包中放置了各种各样的物品 背包严格分类，装备，奇珍，异宝，任务，宠物 每种背包只允许放入该类下的物品
	 */
	public static final byte 防爆包_装备位置 = 0;
	public static final byte 防爆包_奇珍位置 = 1;
	public static final byte 防爆包_异宝位置 = 2;
	public static final byte 防爆包最大个数 = 1;
	public static final short 防爆包最大格子数 = 20;
	/**
	 * 人物身上的背包， 背包中放置了各种各样的物品 背包严格分类，装备，奇珍，异宝，任务，宠物 每种背包只允许放入该类下的物品
	 */
	@SimpleColumn(length = 20000)
	private Knapsack knapsacks_common[] = null;

	public void initKnapName() {
		try {
			if (knapsacks_common != null) {
				for (int i = 0; i < knapsacks_common.length; i++) {
					knapsacks_common[i].knapName = ArticleManager.得到背包名字(i);
				}
			}
			if (knapsacks_cangku != null) {
				if (knapsacks_cangku.knapName == null || knapsacks_cangku.knapName.isEmpty()) {
					knapsacks_cangku.knapName = ArticleManager.仓库;
				}
			}
			if (knapsacks_fangBao != null) {
				if (knapsacks_fangBao.knapName == null || knapsacks_fangBao.knapName.isEmpty()) {
					knapsacks_fangBao.knapName = ArticleManager.防爆包;
				}
			}
			if (knapsacks_QiLing != null) {
				if (knapsacks_QiLing.knapName == null || knapsacks_QiLing.knapName.isEmpty()) {
					knapsacks_QiLing.knapName = ArticleManager.器灵背包;
				}
			}
			if (knapsacks_warehouse != null) {
				if (knapsacks_warehouse.knapName == null || knapsacks_warehouse.knapName.isEmpty()) {
					knapsacks_warehouse.knapName = ArticleManager.仓库2号;
				}
			}
		} catch (Exception e) {
		}
	}

	// private List<Long> shouhunKnap = new ArrayList<Long>(); //废弃字段
	/**
	 * 专门放兽魂的背包
	 */
	private long[] shouhunKnap2 = new long[100];

	public long[] getShouhunKnap() {
		return shouhunKnap2;
	}

	public void sortShouhunKnap() {
		HuntLifeManager.sort(shouhunKnap2);
		this.setDirty(true, "shouhunKnap2");

	}

	public boolean isShouhunInKnap(long articleId) {
		for (int i = 0; i < shouhunKnap2.length; i++) {
			if (shouhunKnap2[i] == articleId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从兽魂背包中删除一个物品
	 * @param ae
	 * @return
	 */
	public boolean removeShouhun(long articleId) {
		synchronized (shouhunKnap2) {
			for (int i = 0; i < shouhunKnap2.length; i++) {
				if (shouhunKnap2[i] == articleId) {
					shouhunKnap2[i] = -1;
					this.setDirty(true, "shouhunKnap2");
					Knapsack.logger.warn("[Delete] [success] [aeId:" + articleId + "] [knapName:兽魂仓库] [" + this.getLogString() + "]");
					NOTIFY_SHOUHUN_KNAP_CHANGE_RES resp = new NOTIFY_SHOUHUN_KNAP_CHANGE_RES(GameMessageFactory.nextSequnceNum(), new int[] { i }, new long[] { shouhunKnap2[i] });
					this.addMessageToRightBag(resp);
					return true;
				}
			}
		}
		return false;
	}

	public boolean putArticle2ShouhunKnap(HuntLifeArticleEntity ae) {
		synchronized (shouhunKnap2) {
			for (int i = 0; i < shouhunKnap2.length; i++) {
				if (shouhunKnap2[i] <= 0) {
					shouhunKnap2[i] = ae.getId();
					this.setDirty(true, "shouhunKnap2");
					Knapsack.logger.warn("[Get] [success] [aeId:" + ae.getId() + "] [knapName:兽魂仓库] [aeName:" + ae.getArticleName() + "] [" + this.getLogString() + "]");
					NOTIFY_SHOUHUN_KNAP_CHANGE_RES resp = new NOTIFY_SHOUHUN_KNAP_CHANGE_RES(GameMessageFactory.nextSequnceNum(), new int[] { i }, new long[] { shouhunKnap2[i] });
					this.addMessageToRightBag(resp);
					return true;
				}
			}
		}
		return false;
	}

	public int countEmptyCell4ShouhunKnap() {
		int result = 0;
		for (long id : shouhunKnap2) {
			if (id <= 0) {
				result++;
			}
		}
		return result;
	}

	public Knapsack[] getKnapsacks_common() {
		return knapsacks_common;
	}

	public void setKnapsacks_common(Knapsack[] knapsacksCommon) {
		knapsacks_common = knapsacksCommon;
		this.setDirty(true, "knapsacks_common");
	}

	/**
	 * 人物身上的防爆背包， 背包中放置了各种各样的物品 背包严格分类，装备，奇珍，异宝 每种背包只允许放入该类下的物品
	 */
	private Knapsack_TimeLimit knapsacks_fangBao;

	/**
	 * 为了防爆包可以从新放入玩家包裹中，设置的包裹物体id
	 */
	private long knapsack_fangBao_Id = -1;

	/**
	 * 人物身上的仓库
	 */
	@SimpleColumn(length = 20000)
	private Knapsack knapsacks_cangku = new Knapsack(this, 60);
	@SimpleColumn(length = 20000)
	private Knapsack knapsacks_warehouse = new Knapsack(this, 10);

	/**
	 * 仓库密码，最少4位，最多12位
	 */
	private String cangkuPassword = "";

	/**
	 * 输入错误密码次数，如果10分钟没有输入密码就清楚次数，或输入正确密码后清楚
	 */
	public transient int pressWrongPWCount;

	/**
	 * 输入错误密码的时间点
	 */
	public transient long pressWrongPWTimePoint;

	public static final int MAX_WRONG_NUM = 3;

	public static final long 仓库密码输入错误保护持续时间 = 10 * 60 * 1000l;

	public static final String 扩展仓库物品 = Translate.扩展仓库物品;

	public static final int 每次增加格子数量 = 5;

	public static final int 仓库最大格子数量 = 80;

	/**
	 * 是否忽略眩晕和禁言状态
	 */
	public transient boolean isStunServer;

	/**
	 * 人物身上的器灵仓库
	 */
	@SimpleColumn(length = 20000)
	private Knapsack knapsacks_QiLing = new Knapsack(this, 60);

	public Knapsack getKnapsacks_cangku() {
		return knapsacks_cangku;
	}

	public void setKnapsacks_cangku(Knapsack knapsacksCangku) {
		knapsacks_cangku = knapsacksCangku;
		this.setDirty(true, "knapsacks_cangku");
	}

	public Knapsack getKnapsacks_QiLing() {
		return knapsacks_QiLing;
	}

	public void setKnapsacks_QiLing(Knapsack knapsacksQiLing) {
		knapsacks_QiLing = knapsacksQiLing;
		this.setDirty(true, "knapsacks_QiLing");
	}

	public Knapsack_TimeLimit getKnapsacks_fangBao() {
		return knapsacks_fangBao;
	}

	public void setKnapsacks_fangBao(Knapsack_TimeLimit knapsacksFangBao) {
		knapsacks_fangBao = knapsacksFangBao;
		this.setDirty(true, "knapsacks_fangBao");
	}

	public long getKnapsack_fangBao_Id() {
		return knapsack_fangBao_Id;
	}

	public void setKnapsack_fangBao_Id(long knapsackFangBaoId) {
		knapsack_fangBao_Id = knapsackFangBaoId;
		this.setDirty(true, "knapsack_fangBao_Id");
	}

	public String getCangkuPassword() {
		return cangkuPassword;
	}

	public void setCangkuPassword(String cangkuPassword) {
		this.cangkuPassword = cangkuPassword;
		setDirty(true, "cangkuPassword");
	}

	public int getPressWrongPWCount() {
		return pressWrongPWCount;
	}

	public void setPressWrongPWCount(int pressWrongPWCount) {
		this.pressWrongPWCount = pressWrongPWCount;
	}

	public long getPressWrongPWTimePoint() {
		return pressWrongPWTimePoint;
	}

	public void setPressWrongPWTimePoint(long pressWrongPWTimePoint) {
		this.pressWrongPWTimePoint = pressWrongPWTimePoint;
	}

	public void OpenCangku() {
		Knapsack knapsack = this.getKnapsacks_cangku();
		if (knapsack != null) {
			int count = knapsack.getCells().length;
			long[] entityIds = new long[count];
			int[] counts = new int[count];
			for (int j = 0; j < count; j++) {
				Cell cell = knapsack.getCells()[j];
				if (cell != null) {
					entityIds[j] = cell.entityId;
					counts[j] = cell.count;
				}
			}
			WAREHOUSE_GET_RES res = new WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(), true, entityIds, counts);
			this.addMessageToRightBag(res);
		}
	}

	public void openCanku2() {
		Knapsack knapsack = this.getKnapsacks_warehouse();
		if (knapsack != null) {
			int count = knapsack.getCells().length;
			long[] entityIds = new long[count];
			int[] counts = new int[count];
			for (int j = 0; j < count; j++) {
				Cell cell = knapsack.getCells()[j];
				if (cell != null) {
					entityIds[j] = cell.entityId;
					counts[j] = cell.count;
				}
			}
			NEW_WAREHOUSE_GET_RES res = new NEW_WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(), 6, true, entityIds, counts);
			this.addMessageToRightBag(res);
		}
	}

	// ////////////////////////////
	// 背包操作，包括各种判断和放入
	/**
	 * 玩家身上的背包(不推荐在循环中调用此方法，会产生比较多的对象) 数组为长度为2的数组 下标为0的数组为常规背包
	 * 下标为1的数组为防爆包，如果没有防爆包那么这个值为null
	 * 
	 * @return
	 */
	public Knapsack[] getKnapsacks(int type) {
		Knapsack[] ks = new Knapsack[2];
		if (type == 1) {
			ks[0] = knapsacks_common[1];
		} else {
			ks[0] = knapsacks_common[0];
		}
		if (knapsacks_fangBao != null) {
			ks[1] = knapsacks_fangBao;
		}
		// if (type == Article.KNAP_装备 && knapsacks_fangBao != null &&
		// knapsacks_fangBao.length > Article.KNAP_装备) {
		// ks[1] = knapsacks_fangBao[Article.KNAP_装备];
		// }
		// if (type == Article.KNAP_奇珍 && knapsacks_fangBao != null &&
		// knapsacks_fangBao.length > Article.KNAP_奇珍) {
		// ks[1] = knapsacks_fangBao[Article.KNAP_奇珍];
		// }
		// if (type == Article.KNAP_异宝 && knapsacks_fangBao != null &&
		// knapsacks_fangBao.length > Article.KNAP_异宝) {
		// ks[1] = knapsacks_fangBao[Article.KNAP_异宝];
		// }
		return ks;
	}

	/**
	 * 玩家身上的普通背包
	 * 
	 * @return
	 */
	public Knapsack getKnapsack_common() {
		return knapsacks_common[0];
	}

	/**
	 * 玩家身上的防爆背包
	 * 
	 * @return
	 */
	public Knapsack getKnapsack_fangbao() {
		return knapsacks_fangBao;
	}

	/**
	 * 得到宠物背包
	 * 
	 * @return
	 */
	public Knapsack getPetKnapsack() {
		// if (knapsacks_common != null && knapsacks_common.length >
		// Article.KNAP_宠物) {
		return knapsacks_common[1];
		// }
		// return null;
	}

	/**
	 * 玩家背包中放入物品
	 * 
	 * @param ae
	 * @return
	 */
	public boolean putToKnapsacks(ArticleEntity ae, String reason) {
		if (knapsacks_common == null || ae == null) {
			return false;
		}
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return false;
		}
		Article article = am.getArticle(ae.getArticleName());
		if (article == null) {
			return false;
		}

		Knapsack knapsack = null;
		if (article.getKnapsackType() == Article.KNAP_宠物) {
			knapsack = getPetKnapsack();
		} else {
			knapsack = getKnapsack_common();
		}
		if (knapsack == null) {
			return false;
		}

		if (GreenServerManager.isBindYinZiServer()) {
			if (GreenServerManager.isUseBindProp) {
				if (article.getName().equals(GreenServerManager.bindpropName)) {
					YinPiaoEntity yinpiao = getYinPiaoEntity();
					if (yinpiao == null) {
						knapsack.put(ae, reason);
					} else {
						setShopSilver(getShopSilver() + ((YinPiaoEntity) ae).getHaveMoney());
					}
					return true;
				}
			}
		}

		boolean canPutToFangBao = true;
		if (ae instanceof Special_1EquipmentEntity) {
			canPutToFangBao = false;
		}
		boolean putOk = false;

		if (!knapsack.put(ae, reason)) {
			if (canPutToFangBao) {
				knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
				if (knapsack != null) {
					putOk = knapsack.put(ae, reason);
				}
			}
		} else {
			putOk = true;
		}

		return putOk;
	}

	/**
	 * 玩家背包中找一个空格子放入一堆物品（一堆只放在一个cell里）
	 * 
	 * @param ae
	 * @return
	 */
	public boolean putToKnapsacks(ArticleEntity ae, int count, String reason) {
		if (knapsacks_common == null || ae == null) {
			return false;
		}
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return false;
		}
		Article article = am.getArticle(ae.getArticleName());
		if (article == null) {
			return false;
		}
		Knapsack knapsack = null;
		if (article.getKnapsackType() == Article.KNAP_宠物) {
			knapsack = getPetKnapsack();
		} else {
			knapsack = getKnapsack_common();
		}
		if (knapsack == null) {
			return false;
		}
		if (!knapsack.putToEmptyCell(ae.getId(), count, reason)) {
			knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
			if (knapsack != null && ((Knapsack_TimeLimit) knapsack).isValid()) {
				return knapsack.putToEmptyCell(ae.getId(), count, reason);
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 是否可以增加物品
	 * 
	 * @param ae
	 * @return
	 */
	public boolean canAddArticle(ArticleEntity ae) {
		if (knapsacks_common == null || ae == null) {
			return false;
		}
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return false;
		}
		Article article = am.getArticle(ae.getArticleName());
		if (article == null) {
			return false;
		}
		Knapsack knapsack = null;
		if (article.getKnapsackType() == Article.KNAP_宠物) {
			knapsack = getPetKnapsack();
		} else {
			knapsack = getKnapsack_common();
		}
		if (knapsack == null) {
			return false;
		}
		if (knapsack.isFull()) {
			knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
			if (knapsack != null) {
				return !knapsack.isFull();
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 按照名字得到物品在背包中的数量
	 * 
	 * @param articleName
	 * @return
	 */
	public int countArticleInKnapsacksByName(String articleName) {
		int count = 0;
		if (knapsacks_common != null) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				Article article = am.getArticle(articleName);
				if (article != null) {
					Knapsack knapsack = null;
					if (article.getKnapsackType() == Article.KNAP_宠物) {
						knapsack = getPetKnapsack();
					} else {
						knapsack = getKnapsack_common();
					}
					if (knapsack != null) {
						count += knapsack.countArticle(articleName);
					}
					knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
					if (knapsack != null) {
						count += knapsack.countArticle(articleName);
					}
				}
			}
		}
		return count;
	}

	/**
	 * 按照名字及绑定属性得到物品在背包中的数量
	 * 
	 * @param articleName
	 * @return
	 */
	public int countArticleInKnapsacksByName(String articleName, boolean flag) {
		int count = 0;
		if (knapsacks_common != null) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				Article article = am.getArticle(articleName);
				if (article != null) {
					Knapsack knapsack = null;
					if (article.getKnapsackType() == Article.KNAP_宠物) {
						knapsack = getPetKnapsack();
					} else {
						knapsack = getKnapsack_common();
					}
					if (knapsack != null) {
						count += knapsack.countArticle(articleName, flag);
					}
					knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
					if (knapsack != null) {
						count += knapsack.countArticle(articleName, flag);
					}
				}
			}
		}
		return count;
	}

	/**
	 * 删除背包中的物品实体
	 * 
	 * @param ae
	 * @return
	 */
	public ArticleEntity removeFromKnapsacks(ArticleEntity ae, String reason, boolean 从游戏中删除) {
		if (knapsacks_common == null || ae == null) {
			return null;
		}
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return null;
		}
		Article article = am.getArticle(ae.getArticleName());
		if (article == null) {
			return null;
		}
		Knapsack knapsack = null;
		if (article.getKnapsackType() == Article.KNAP_宠物) {
			knapsack = getPetKnapsack();
		} else {
			knapsack = getKnapsack_common();
		}
		if (knapsack == null) {
			return null;
		}
		ArticleEntity removeAe = knapsack.removeByArticleId(ae.getId(), reason, 从游戏中删除);
		if (removeAe == null) {
			knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
			if (knapsack != null) {
				removeAe = knapsack.removeByArticleId(ae.getId(), reason, 从游戏中删除);
			}
		}
		return removeAe;
	}

	// public CompoundReturn getArticleByNameAndColor(Srting name,int
	// colorType){
	// CompoundReturn.createCompoundReturn().setIntValue(1).setObjValue(ae).setBoolean();
	// }

	/**
	 * 删除背包中的物品实体
	 * 
	 * @param ae
	 * @return
	 */
	public ArticleEntity removeFromKnapsacks(long id, String reason, boolean 从游戏中删除) {
		if (knapsacks_common == null) {
			return null;
		}
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (am == null || aem == null) {
			return null;
		}
		ArticleEntity ae = aem.getEntity(id);
		if (ae == null) {
			return null;
		}
		Article article = am.getArticle(ae.getArticleName());
		if (article == null) {
			return null;
		}
		Knapsack knapsack = null;
		if (article.getKnapsackType() == Article.KNAP_宠物) {
			knapsack = getPetKnapsack();
		} else {
			knapsack = getKnapsack_common();
		}
		if (knapsack == null) {
			return null;
		}
		ArticleEntity removeAe = knapsack.removeByArticleId(id, reason, 从游戏中删除);
		if (removeAe == null) {
			knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
			if (knapsack != null) {
				removeAe = knapsack.removeByArticleId(id, reason, 从游戏中删除);
			}
		}
		return removeAe;
	}

	/**
	 * 得到某物种的物品数量
	 * 
	 * @param articleName
	 * @return
	 */
	public int getArticleEntityNum(String articleName) {
		return countArticleInKnapsacksByName(articleName);
	}

	/**
	 * 从背包数组中找到id为articleId的一个物品进行删除，返回删除后的物品
	 * 
	 * @param articleId
	 * @return
	 */
	public ArticleEntity removeArticleEntityFromKnapsackByArticleId(long articleId, String reason, boolean 从游戏中删除) {
		ArticleEntity ae = null;
		if (knapsacks_common != null) {
			synchronized (this.tradeKey) {
				for (Knapsack knapsack : knapsacks_common) {
					if (knapsack != null) {
						// knapsack.logger.warn("[remove] [articleId:"+articleId+"] [reason:"+reason+"] [从游戏中删除:"+从游戏中删除+"] [name:"+this.getName()+"]");
						ae = knapsack.removeByArticleId(articleId, reason, 从游戏中删除);
						ArticleManager.logger.warn("[remove] [ae:" + (ae == null ? "null" : ae.getArticleName()) + "] [articleId:" + articleId + "] [reason:" + reason + "] [从游戏中删除:" + 从游戏中删除 + "] [name:" + this.getName() + "]");
						if (ae != null) {
							return ae;
						}
					}
				}
				if (knapsacks_fangBao != null) {
					// for (Knapsack knapsack : knapsacks_fangBao) {
					// if (knapsack != null) {
					ae = knapsacks_fangBao.removeByArticleId(articleId, reason, 从游戏中删除);
					if (ae != null) {
						return ae;
					}
					// }
					// }
				}
			}
		}
		return ae;
	}

	/**
	 * 是否能够把所有物品实体都放入背包 如果普通背包中没有放下剩余继续往防爆背包中放
	 * 
	 * @param entities
	 */
	public boolean putAllOK(ArticleEntity entities[]) {
		boolean ok = false;
		ArticleManager am = ArticleManager.getInstance();
		if (entities == null || entities.length == 0) {
			return true;
		}
		if (entities != null && am != null) {
			// 把物品按照knapsackType分类
			HashMap<Integer, List<ArticleEntity>> map = new HashMap<Integer, List<ArticleEntity>>();
			for (ArticleEntity ae : entities) {
				if (ae != null) {
					Article a = am.getArticle(ae.getArticleName());
					if (a == null) {
						return false;
					}
					if (a.getKnapsackType() == Article.KNAP_宠物) {
						List<ArticleEntity> list = map.get(Article.KNAP_宠物);
						if (list == null) {
							list = new ArrayList<ArticleEntity>();
							map.put(Article.KNAP_宠物, list);
						}
						list.add(ae);
					} else {
						List<ArticleEntity> list = map.get(Article.KNAP_装备);
						if (list == null) {
							list = new ArrayList<ArticleEntity>();
							map.put(Article.KNAP_装备, list);
						}
						list.add(ae);
					}
				} else {
					return false;
				}
			}
			for (Integer knapsackType : map.keySet()) {
				List<ArticleEntity> list = map.get(knapsackType);
				Knapsack knapsack = null;
				if (knapsackType == Article.KNAP_宠物) {
					knapsack = getPetKnapsack();
				} else {
					knapsack = getKnapsack_common();
				}
				if (list == null || knapsack == null) {
					return false;
				}
				int leftCellNum = 0;
				leftCellNum = knapsack.getEmptyNum();
				Knapsack_TimeLimit knapsack_Time = (Knapsack_TimeLimit) knapsacks_fangBao;// getKnapsack_fangbao(knapsackType);
				if (knapsack_Time != null && knapsack_Time.isValid()) {
					leftCellNum = leftCellNum + knapsack_Time.getEmptyNum();
				}
				if (leftCellNum == 0) {
					return false;
				}
				Knapsack knapsackTemp = new Knapsack(this, leftCellNum);
				ok = knapsackTemp.putAllOK(list);
				if (!ok) {
					return ok;
				}
			}
		}
		return ok;
	}

	/**
	 * 判断能否把物品都放入背包 只计数 ，空位置
	 * 
	 * 
	 * @return
	 */
	public boolean canAddAll(Article[] articles) {
		if (getKnapsack_common().getEmptyNum() < articles.length) {
			if (getKnapsack_fangbao() != null) {
				if (getKnapsack_common().getEmptyNum() + getKnapsack_fangbao().getEmptyNum() < articles.length) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断能否把物品都放入背包 只计数 ，空位置 参数下标为背包的类型
	 * 
	 * @return
	 */
	public boolean canAddAll(int[] check) {
		if (check == null || check.length == 0) {
			return true;
		}

		int totalNum = 0;

		for (int i = 0; i < check.length; i++) {
			totalNum += check[i];
		}
		if (getKnapsack_common().getEmptyNum() < totalNum) {
			if (getKnapsack_fangbao() == null || getKnapsack_fangbao().getEmptyNum() < totalNum) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 把物品实体全部放入背包 因为可以把物品放入背包和防爆包所以不用背包的putAll方法，而是一个个的放入
	 * 
	 * @param entities
	 * @return
	 */
	public boolean putAll(ArticleEntity entities[], String reason) {
		boolean ok = false;
		if (entities != null) {
			if (putAllOK(entities)) {
				for (ArticleEntity ae : entities) {
					putToKnapsacks(ae, reason);
				}
				return true;
			} else {
				return false;
			}
		}
		return ok;
	}

	/**
	 * 搜集背包的变化仓库的变化并通知客户端
	 */
	protected void collectKnapsackChangeAndNotifyClient() {
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();

		Knapsack[] knapsacks = knapsacks_common;
		if (knapsacks == null) {
			return;
		}
		for (int knapsackIndex = 0; knapsackIndex < knapsacks.length; knapsackIndex++) {
			Knapsack knapsack = knapsacks[knapsackIndex];
			if (knapsack != null) {
				byte flags[] = knapsack.getCellChangeFlags();
				for (int i = 0; i < flags.length; i++) {
					if (flags[i] == Knapsack.ADD_OLD_ARTICLE) {
						Cell cell = knapsack.getCell(i);
						if (cell != null && cell.getCount() > 0) {
							NOTIFY_KNAPSACKCHANGE_REQ req = new NOTIFY_KNAPSACKCHANGE_REQ(GameMessageFactory.nextSequnceNum(), (short) knapsackIndex, (short) i, (short) cell.getCount(), cell.getEntityId(), false);
							this.addMessageToRightBag(req);
						}
					} else if (flags[i] == Knapsack.REMOVE_ARTICLE) {
						Cell cell = knapsack.getCell(i);
						if (cell != null && cell.getCount() > 0) {
							NOTIFY_KNAPSACKCHANGE_REQ req = new NOTIFY_KNAPSACKCHANGE_REQ(GameMessageFactory.nextSequnceNum(), (short) knapsackIndex, (short) i, (short) cell.getCount(), cell.getEntityId(), false);
							this.addMessageToRightBag(req);
						} else {
							NOTIFY_KNAPSACKCHANGE_REQ req = new NOTIFY_KNAPSACKCHANGE_REQ(GameMessageFactory.nextSequnceNum(), (short) knapsackIndex, (short) i, (short) 0, -1, false);
							this.addMessageToRightBag(req);
						}
					} else if (flags[i] == Knapsack.ADD_NEW_ARTICLE) {
						Cell cell = knapsack.getCell(i);
						if (cell != null && cell.getCount() > 0) {

							NOTIFY_KNAPSACKCHANGE_REQ req = new NOTIFY_KNAPSACKCHANGE_REQ(GameMessageFactory.nextSequnceNum(), (short) knapsackIndex, (short) i, (short) cell.getCount(), cell.getEntityId(), false);
							this.addMessageToRightBag(req);
						}
					}
				}
				knapsack.clearChangeFlag();
			}
		}

		Knapsack knapsack = knapsacks_fangBao;// knapsacks[knapsackIndex];
		if (knapsack != null) {
			byte flags[] = knapsack.getCellChangeFlags();
			for (int i = 0; i < flags.length; i++) {
				if (flags[i] == Knapsack.ADD_OLD_ARTICLE) {
					Cell cell = knapsack.getCell(i);
					if (cell != null && cell.getCount() > 0) {
						NOTIFY_KNAPSACKCHANGE_REQ req = new NOTIFY_KNAPSACKCHANGE_REQ(GameMessageFactory.nextSequnceNum(), (short) 0, (short) i, (short) cell.getCount(), cell.getEntityId(), true);
						this.addMessageToRightBag(req);
					}
				} else if (flags[i] == Knapsack.REMOVE_ARTICLE) {
					Cell cell = knapsack.getCell(i);
					if (cell != null && cell.getCount() > 0) {
						NOTIFY_KNAPSACKCHANGE_REQ req = new NOTIFY_KNAPSACKCHANGE_REQ(GameMessageFactory.nextSequnceNum(), (short) 0, (short) i, (short) cell.getCount(), cell.getEntityId(), true);
						this.addMessageToRightBag(req);
					} else {
						NOTIFY_KNAPSACKCHANGE_REQ req = new NOTIFY_KNAPSACKCHANGE_REQ(GameMessageFactory.nextSequnceNum(), (short) 0, (short) i, (short) 0, -1, true);
						this.addMessageToRightBag(req);
					}
				} else if (flags[i] == Knapsack.ADD_NEW_ARTICLE) {
					Cell cell = knapsack.getCell(i);
					if (cell != null && cell.getCount() > 0) {

						NOTIFY_KNAPSACKCHANGE_REQ req = new NOTIFY_KNAPSACKCHANGE_REQ(GameMessageFactory.nextSequnceNum(), (short) 0, (short) i, (short) cell.getCount(), cell.getEntityId(), true);
						this.addMessageToRightBag(req);
					}
				}
			}
			knapsack.clearChangeFlag();
		}
		// }

		// 仓库的变化
		knapsack = getKnapsacks_cangku();
		if (knapsack != null) {
			byte flags[] = knapsack.getCellChangeFlags();
			for (int i = 0; i < flags.length; i++) {
				if (flags[i] != Knapsack.NO_CHANGE) {
					int count = knapsack.getCells().length;
					long[] entityIds = new long[count];
					int[] counts = new int[count];
					for (int j = 0; j < count; j++) {
						Cell cell = knapsack.getCells()[j];
						if (cell != null) {
							entityIds[j] = cell.entityId;
							counts[j] = cell.count;
						}
					}
					WAREHOUSE_GET_RES res = new WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(), true, entityIds, counts);
					this.addMessageToRightBag(res);
					break;
				}
			}
			knapsack.clearChangeFlag();
		}

		knapsack = getKnapsacks_warehouse();
		if (knapsack != null) {
			byte flags[] = knapsack.getCellChangeFlags();
			for (int i = 0; i < flags.length; i++) {
				if (flags[i] != Knapsack.NO_CHANGE) {
					int count = knapsack.getCells().length;
					long[] entityIds = new long[count];
					int[] counts = new int[count];
					for (int j = 0; j < count; j++) {
						Cell cell = knapsack.getCells()[j];
						if (cell != null) {
							entityIds[j] = cell.entityId;
							counts[j] = cell.count;
						}
					}
					NEW_WAREHOUSE_GET_RES res = new NEW_WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(), 6, true, entityIds, counts);
					this.addMessageToRightBag(res);
					break;
				}
			}
			knapsack.clearChangeFlag();
		}

		// 器灵仓库的变化
		knapsack = getKnapsacks_QiLing();
		if (knapsack != null) {
			byte flags[] = knapsack.getCellChangeFlags();
			for (int i = 0; i < flags.length; i++) {
				if (flags[i] != Knapsack.NO_CHANGE) {
					int count = knapsack.getCells().length;
					long[] entityIds = new long[count];
					short[] counts = new short[count];
					for (int j = 0; j < count; j++) {
						Cell cell = knapsack.getCells()[j];
						if (cell != null) {
							entityIds[j] = cell.entityId;
							counts[j] = (short) cell.count;
						}
					}
					QUERY_KNAPSACK_QILING_RES res = new QUERY_KNAPSACK_QILING_RES(GameMessageFactory.nextSequnceNum(), ArticleManager.器灵仓库格子数, entityIds, counts);
					this.addMessageToRightBag(res);
					break;
				}
			}
			knapsack.clearChangeFlag();
		}
	}

	public void notifyAllKnapsack() {
		Knapsack[] sack = getKnapsacks_common();
		if (sack == null) {
			return;
		}
		BagInfo4Client[] bagInfo4Client = new BagInfo4Client[sack.length];
		for (int i = 0; i < sack.length; i++) {
			Knapsack knapsack = sack[i];
			bagInfo4Client[i] = new BagInfo4Client();
			bagInfo4Client[i].setBagtype((byte) i);
			if (knapsack != null) {
				Cell kcs[] = knapsack.getCells();
				long ids[] = new long[kcs.length];
				short counts[] = new short[kcs.length];
				for (int j = 0; j < kcs.length; j++) {
					if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
						ids[j] = kcs[j].getEntityId();
						counts[j] = (short) kcs[j].getCount();
					} else {
						ids[j] = -1;
						counts[j] = 0;
					}
				}
				bagInfo4Client[i].setEntityId(ids);
				bagInfo4Client[i].setCounts(counts);

				// 对应类型的防爆包
				Knapsack knap_fangbao = getKnapsack_fangbao();
				if (knap_fangbao != null) {
					kcs = knap_fangbao.getCells();
					ids = new long[kcs.length];
					counts = new short[kcs.length];
					for (int j = 0; j < kcs.length; j++) {
						if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
							ids[j] = kcs[j].getEntityId();
							counts[j] = (short) kcs[j].getCount();
						} else {
							ids[j] = -1;
							counts[j] = 0;
						}
					}
					bagInfo4Client[i].setFangbaoEntityId(ids);
					bagInfo4Client[i].setFangbaoCounts(counts);
				}
			}
		}

		HashMap<String, PropsCategoryCoolDown> map = this.getUsingPropsAgent().getCooldownTable();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String categoryName = it.next();
			PropsCategoryCoolDown cd = map.get(categoryName);
			if (cd != null && cd.end >= com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 5000) {
				PROPS_CD_MODIFY_REQ req2 = new PROPS_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), this.getId(), categoryName, cd.start, (byte) 0);
				this.addMessageToRightBag(req2);
			}
		}
		ArticleManager am = ArticleManager.getInstance();
		this.addMessageToRightBag(new QUERY_KNAPSACK_RES(GameMessageFactory.nextSequnceNum(), bagInfo4Client, am.getAllPropsCategory()));
		Fangbao_KNAPSACK_RES res = new Fangbao_KNAPSACK_RES(GameMessageFactory.nextSequnceNum(), this.getKnapsack_fangBao_Id());
		this.addMessageToRightBag(res);
	}

	public void notifyKnapsackFB(Player player) {
		Knapsack sack = player.getKnapsacks_fangBao();
		QUERY_KNAPSACK_FB_RES res = null;
		if (sack == null) {
			res = new QUERY_KNAPSACK_FB_RES(GameMessageFactory.nextSequnceNum(), -1, Player.防爆包最大格子数, new long[0], new short[0]);
		} else {
			Cell[] kcs = sack.getCells();
			long[] ids = new long[kcs.length];
			short[] counts = new short[kcs.length];
			for (int j = 0; j < kcs.length; j++) {
				if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
					ids[j] = kcs[j].getEntityId();
					counts[j] = (short) kcs[j].getCount();
				} else {
					ids[j] = -1;
					counts[j] = 0;
				}
			}
			res = new QUERY_KNAPSACK_FB_RES(GameMessageFactory.nextSequnceNum(), player.getKnapsack_fangBao_Id(), Player.防爆包最大格子数, ids, counts);
		}
		player.addMessageToRightBag(res);
	}

	/**
	 * 使用背包中的某个单元格中的物品
	 * 
	 * 如果物品是装备，那么就装备到人物身上，同时将人物身上同类的装备卸下来，放到这个单元格中
	 * 
	 * 如果物品是道具，
	 * 
	 * @param row
	 * @param col
	 */
	public synchronized void useItemOfKnapsack(Game game, int knapsackIndex, int index, ArticleEntity ae, int soulType) {
		Knapsack knapsack = null;
		if (knapsackIndex == 1) {
			knapsack = getPetKnapsack();
		} else {
			knapsack = getKnapsack_common();
		}
		ArticleManager am = ArticleManager.getInstance();
		Soul soul = getSoul(soulType);
		if (ae == null) {
			if (ArticleManager.logger.isInfoEnabled()) {
				ArticleManager.logger.info("[使用物品] [失败] [{}] [playerId:{}] [{}] [index:{}][物品实体为空]", new Object[] { this.getUsername(), id, getName(), index });
			}
			MagicWeaponManager.logger.debug("[使用背包物品][" + this.getLogString() + "][ 失败]");
			return;
		}
		MagicWeaponManager.logger.debug("[使用背包物品] [" + ae.getArticleName() + "] [" + this.getLogString() + "] [" + (ae instanceof NewMagicWeaponEntity) + "]");
		if (ae instanceof EquipmentEntity) {
			EquipmentEntity ee = (EquipmentEntity) ae;
			Equipment e = (Equipment) am.getArticle(ee.getArticleName());
			if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(id)) {
				this.sendError(Translate.渡劫换装失败);
				if (ArticleManager.logger.isInfoEnabled()) {
					ArticleManager.logger.info("[使用装备] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [{}]", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId(), Translate.渡劫换装失败 });
				}
				return;
			}
			FairyRobberyManager fins = FairyRobberyManager.inst;
			if (fins != null && fins.isPlayerInRobbery(this)) {
				this.sendError(Translate.渡劫换装失败);
				if (ArticleManager.logger.isInfoEnabled()) {
					ArticleManager.logger.info("[使用装备] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [{}]", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId(), Translate.渡劫换装失败 });
				}
				return;
			}
			// 坐骑的装备
			if (e.getEquipmentType() > EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER && e.getEquipmentType() != EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {

				long defaultHorseId = this.getRideHorseId();
				if (defaultHorseId > 0) {
					if (this.horseIdList.contains(defaultHorseId)) {

						Horse horse = HorseManager.getInstance().getHorseById(defaultHorseId, this);
						if (horse != null) {
							String result = e.canUse(horse);
							if (result == null) {
								try {
									horse.putOn(ee, false);
									HORSE_PUTONOROFF_RES res = new HORSE_PUTONOROFF_RES(GameMessageFactory.nextSequnceNum(), horse);
									this.addMessageToRightBag(res);
									if (ArticleManager.logger.isInfoEnabled()) {
										ArticleManager.logger.info("[使用装备] [成功] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}]", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId() });
									}
								} catch (Exception e1) {

									this.send_HINT_REQ(Translate.translateString(Translate.您没有将装备到身上服务器出现错误, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
									if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info("[使用装备] [失败] [" + this.getUsername() + "] [playerId:" + id + "] [" + getName() + "] [index:" + index + "] [" + ee.getArticleName() + "] [id:" + ee.getId() + "] [--]", e1);
									e1.printStackTrace();
								}
							} else {
								if (ArticleManager.logger.isInfoEnabled()) {
									ArticleManager.logger.info("[使用装备] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [{}]", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId(), result });
								}
								HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
								addMessageToRightBag(error);

							}
						}
					}
				} else {
					this.sendError(Translate.这是坐骑装备你还没有设置默认坐骑);
					if (ArticleManager.logger.isInfoEnabled()) {
						ArticleManager.logger.info("[使用装备] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [{}]", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId(), Translate.这是坐骑装备你还没有设置默认坐骑 });
					}
				}

			} else {

				// 人物的装备
				String result = e.canUse(this, soulType);
				if (result == null) {
					ArticleEntity entity = knapsack.remove(index, "使用装备", false);
					if (entity != null && entity == ae) {
						try {
							EquipmentEntity oldEE = soul.getEc().putOn(ee, soulType);
							if (oldEE != null) {
								knapsack.put(oldEE, "卸载装备");
							}
							if (ArticleManager.logger.isInfoEnabled()) {
								ArticleManager.logger.info("[使用装备] [成功] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [old:{}] {}", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId(), (oldEE == null ? "--" : oldEE.getArticleName()), getPlayerPropsString() });
							}

							this.send_HINT_REQ(Translate.translateString(Translate.您已经将装备装备到身上, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
						} catch (Exception ex) {
							System.out.println(ex);
							this.send_HINT_REQ(Translate.translateString(Translate.您没有将装备到身上服务器出现错误, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
							if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info("[使用装备] [失败] [" + this.getUsername() + "] [playerId:" + id + "] [" + getName() + "] [index:" + index + "] [" + ee.getArticleName() + "] [id:" + ee.getId() + "] [--] [" + ex + "]");
							ex.printStackTrace();
						}
					} else if (entity != null) {
						knapsack.put(index, entity, "使用装备错误放回背包");
						this.send_HINT_REQ(Translate.translateString(Translate.您要装备的物品与背包中装备的不匹配, new String[][] { { Translate.STRING_1, entity.getArticleName() } }));
						if (ArticleManager.logger.isInfoEnabled()) {
							ArticleManager.logger.info("[使用装备] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [您要装备的物品{}与背包中的不匹配]", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId(), entity.getArticleName() });
						}

					} else {
						this.send_HINT_REQ(Translate.translateString(Translate.您没有此物品1, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
						if (ArticleManager.logger.isInfoEnabled()) {
							ArticleManager.logger.info("[使用装备] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [在背包第{}个中没有此物品]", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId(), index });
						}
					}

				} else {
					if (ArticleManager.logger.isInfoEnabled()) {
						ArticleManager.logger.info("[使用装备] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [{}]", new Object[] { this.getUsername(), id, getName(), index, ee.getArticleName(), ee.getId(), result });
					}
					HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
					addMessageToRightBag(error);

				}
			}
		} else if (ae instanceof PropsEntity) {
			ArticleEntity entity = knapsack.getArticleEntityByCell(index);
			if (entity == null || entity != ae) {
				this.send_HINT_REQ(Translate.translateString(Translate.您没有此物品1, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
				if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[使用道具] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [在背包第{}个中没有此物品]", new Object[] { this.getUsername(), id, getName(), index, ae.getArticleName(), ae.getId(), index });
				return;
			}
			PropsEntity pe = (PropsEntity) ae;
			Props pp = (Props) am.getArticle(ae.getArticleName());
			if (pp != null) {
				String result = pp.canUse(this);
				if (result == null) {
					// TODO: 有效期或者使用次数的限制，如何实现？
					boolean success = pp.use(game, this, pe);
					if (success) {
						// 使用后就消失
						if (!pp.isUsedUndisappear()) {
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							ArticleEntity aeTemp = knapsack.getArticleEntityByCell(index);
							ArticleEntity removeAe = knapsack.remove(index, "使用道具删除", true);
							if (removeAe != null) {
								// 统计
								ArticleStatManager.addToArticleStat(this, null, removeAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "使用道具删除", null);
							}
							if (aem != null) {
								aem.recycleEntity(aeTemp, ArticleEntityManager.DELETE_USE, this);
							}
						}

						// 通知玩家，更新任务列表
						this.useOneProps(pe, this);

						if (二级分类酒.equals(pp.get物品二级分类())) {
							EnterLimitManager.setValues(this, PlayerRecordType.喝酒次数);
						}

						if (ArticleManager.logger.isInfoEnabled()) {
							ArticleManager.logger.info("[使用道具] [成功] [{}] [playerId:{}] [{}] [index:{}] [{}] [--] [{}]", new Object[] { this.getUsername(), id, getName(), index, pe.getArticleName(), pp.getClass().getName() });
						}
						if (pp instanceof PetProps) {

						} else {
							this.send_HINT_REQ(Translate.translateString(Translate.您使用了物品, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
						}
						PropsCategory pc = am.getPropsCategoryByCategoryName(pp.getCategoryName());
						if (pc != null) {
							PropsCategoryCoolDown pccd = (PropsCategoryCoolDown) this.usingPropsAgent.getCooldownTable().get(pp.getCategoryName());
							if (pccd != null && pc.getCooldownLimit() >= 5 * 60 * 1000) {
								pccd.start = heartBeatStartTime;
								pccd.end = pccd.start + (pc.getCooldownLimit() - 60 * 1000);
							} else if (pccd != null && pc.getCooldownLimit() < 5 * 60 * 1000 && pc.getCooldownLimit() / 2 > 0) {
								pccd.start = heartBeatStartTime;
								pccd.end = pccd.start + pc.getCooldownLimit() / 2;
							} else if (pc.getCooldownLimit() >= 5 * 60 * 1000) {
								pccd = new PropsCategoryCoolDown(heartBeatStartTime, heartBeatStartTime + (pc.getCooldownLimit() - 60 * 1000));
								this.usingPropsAgent.getCooldownTable().put(pp.getCategoryName(), pccd);
							} else if (pc.getCooldownLimit() < 5 * 60 * 1000 && pc.getCooldownLimit() / 2 > 0) {
								pccd = new PropsCategoryCoolDown(heartBeatStartTime, heartBeatStartTime + pc.getCooldownLimit() / 2);
								this.usingPropsAgent.getCooldownTable().put(pp.getCategoryName(), pccd);
							}
						} else {
							// System.out.println(ae.getArticleName() + "类型" +
							// pp.getCategoryName() + "没有找到类型");
						}
					} else {
						if (ArticleManager.logger.isInfoEnabled()) {
							ArticleManager.logger.info("[使用道具] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [--] [{}]", new Object[] { this.getUsername(), id, getName(), index, pe.getArticleName(), pp.getClass().getName() });
						}
						PropsCategory pc = am.getPropsCategoryByCategoryName(pp.getCategoryName());
						if (pc != null && pc.getCooldownLimit() >= 5000) {
							PROPS_CD_MODIFY_REQ req = new PROPS_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), getId(), pc.getCategoryName(), 0L, (byte) 1);
							this.addMessageToRightBag(req);
						}
					}
				} else {
					if (ArticleManager.logger.isInfoEnabled()) {
						ArticleManager.logger.info("[使用道具] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [{}]", new Object[] { this.getUsername(), id, getName(), index, pe.getArticleName(), result });
					}

					HINT_REQ error = null;
					if (pp instanceof PackageProps || pp instanceof RandomPackageProps) {
						error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
					} else {
						error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
					}
					addMessageToRightBag(error);
					PropsCategory pc = am.getPropsCategoryByCategoryName(pp.getCategoryName());
					if (pc != null && pc.getCooldownLimit() >= 5000) {
						PROPS_CD_MODIFY_REQ req = new PROPS_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), getId(), pc.getCategoryName(), 0L, (byte) 1);
						this.addMessageToRightBag(req);
					}
				}

			} else {
				if (ArticleManager.logger.isInfoEnabled()) {
					ArticleManager.logger.info("[使用道具] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [您选择的道具【{}】系统不存在，使用无效！]", new Object[] { this.getUsername(), id, getName(), index, pe.getArticleName(), pe.getId(), ae.getArticleName() });
				}
				HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.text_5835 + ae.getArticleName() + Translate.text_5836);
				addMessageToRightBag(error);
			}

		} else if (ae instanceof NewMagicWeaponEntity) { // 穿法宝
			ArticleEntity tEntity = knapsack.getArticleEntityByCell(index);
			if (tEntity == null || tEntity != ae) {
				this.send_HINT_REQ(Translate.translateString(Translate.您没有此物品1, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
				if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[穿法宝] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [在背包第{}个中没有此物品]", new Object[] { this.getUsername(), id, getName(), index, ae.getArticleName(), ae.getId(), index });
				return;
			}
			NewMagicWeaponEntity mwee = (NewMagicWeaponEntity) ae;
			MagicWeaponManager.logger.debug("[使用背包物品] [穿法宝] [" + ae.getId() + "] [" + ae.getArticleName() + "] [" + this.getLogString() + "]");
			try {
				MagicWeapon mw = (MagicWeapon) ArticleManager.getInstance().getArticle(ae.getArticleName());
				String result = mw.canuse(this, soulType);
				if (result == null) {
					ArticleEntity entity = knapsack.remove(index, "使用装备", false);
					if (entity != null && entity == ae) {
						long oldNpcid = this.getActiveMagicWeaponId();
						setBasicpropertyname(entity.getId(), "穿法宝");
						NewMagicWeaponEntity oldM = MagicWeaponManager.instance.putOn(this, (NewMagicWeaponEntity) ae, soulType);
						if (oldM != null) {
							MagicWeaponManager.instance.putOff(this, oldM, soulType, true);
							this.putToKnapsacks(oldM, "脱法宝");
							boolean isCurrent = getCurrSoul().getSoulType() == soulType;
							if (isCurrent && oldNpcid > 0) {
								NPC oldNpc = MemoryNPCManager.getNPCManager().getNPC(oldNpcid);
								this.getCurrentGame().removeSprite(oldNpc);
								if (MagicWeaponManager.logger.isDebugEnabled()) {
									MagicWeaponManager.logger.debug("[法宝移除地图][" + getLogString() + "] [" + oldNpc.getId() + "]");
								}
							}
						}
					} else if (entity != null) {
						knapsack.put(index, entity, "使用装备错误放回背包");
						this.send_HINT_REQ(Translate.translateString(Translate.您要装备的物品与背包中装备的不匹配, new String[][] { { Translate.STRING_1, entity.getArticleName() } }));
						if (ArticleManager.logger.isInfoEnabled()) {
							ArticleManager.logger.info("[使用法宝] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [您要装备的物品{}与背包中的不匹配]", new Object[] { this.getUsername(), id, getName(), index, mwee.getArticleName(), mwee.getId(), entity.getArticleName() });
						}
					}
				} else {
					this.sendError(result);
				}
			} catch (Exception e) {
				MagicWeaponManager.logger.error("[穿法宝] [失败] [" + this.getLogString() + "] [" + ae.getArticleName() + "]", e);
			}
		} else {
			if (ArticleManager.logger.isInfoEnabled()) {
				ArticleManager.logger.info("[使用道具] [失败] [{}] [playerId:{}] [{}] [index:{}] [{}] [id:{}] [您选择的是普通物品，不能被使用或者装备]", new Object[] { this.getUsername(), id, getName(), index, ae.getArticleName(), ae.getId() });
			}

			HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.text_5837);
			addMessageToRightBag(error);
		}
	}

	/**
	 * 丢弃格子内的物品
	 * 
	 * @param game
	 * @param index
	 */
	public synchronized void removeItemOfKnapsack(Game game, int bagIndex, int index) {

		Knapsack knapsack = null;
		if (bagIndex == 1) {
			knapsack = getPetKnapsack();
		} else {
			knapsack = getKnapsack_common();
		}
		if (knapsack == null) {
			// ArticleManager.logger.warn("[第" + bagIndex + "个背包为空] [" +
			// this.getUsername() + "] [" + this.getId() + "] [" +
			// this.getName() + "]");
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[第{}个背包为空] [{}] [{}] [{}]", new Object[] { bagIndex, this.getUsername(), this.getId(), this.getName() });
			return;
		}
		ArticleEntity ae = knapsack.getArticleEntityByCell(index);

		if (GreenServerManager.isBindYinZiServer()) {
			if (ae.getArticleName().equals(GreenServerManager.bindpropName)) {
				sendError(Translate.银票不能丢弃);
				return;
			}
		}

		if (!ArticleProtectManager.instance.isCanDo(this, ArticleProtectDataValues.ArticleProtect_Common, ae.getId())) {
			this.sendError(Translate.锁魂物品不能做此操作);
			return;
		}

		if (ae instanceof EquipmentEntity) {
			((EquipmentEntity) ae).destroyEntity(this);
		}

		if (ae instanceof PetPropsEntity) {
			// 如果此物品为宠物，进行特殊处理
			long petId = ((PetPropsEntity) ae).getPetId();
			Pet pet = PetManager.getInstance().getPet(petId);
			if (pet != null) {
				if (pet.getHookInfo() != null) {
					this.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
					return;
				}
			}
			
			for(long pid : this.getPetCell()){
				if(pid == pet.getId()){
					this.sendError(Translate.助战中的宠物不能封印);
					return;
				}
			}
			
			if(PetHouseManager.getInstance().petIsStore(this, pet)){
				this.sendError(Translate.挂机中的宠物不能封印);
				return; 
			}
			
			if (this.getActivePetId() == ((PetPropsEntity) ae).getPetId()) {
				this.sendError(此宠物是出战状态不能放生);
				return;
			}
		}
		int count = knapsack.clearCell(index, "丢弃", true);
		if (ae != null) {
			// 统计
			ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, count, "丢弃", null);
			if (ae instanceof PetPropsEntity) {
				// 放生
				long petId = ((PetPropsEntity) ae).getPetId();
				Pet pet = PetManager.getInstance().getPet(petId);
				if (pet != null) {
					this.send_HINT_REQ(Translate.translateString(Translate.你放生了宠物xx, new String[][] { { Translate.STRING_1, pet.getName() } }));
					PetManager.getInstance().deletePet(this, pet);
					PetManager.logger.info("Player.removeItemOfKnapsack: {} drop PetPropsEntity {}", name, petId);
				} else {
					ArticleManager.logger.error("[玩家丢弃物品错误] [" + this.getLogString() + "] [物品id:" + ae.getId() + "] [物品name:" + ae.getArticleName() + "] [index:" + index + "]");
				}
			} else if (ae instanceof PetEggPropsEntity) {
				// 丢弃宠物蛋
				PetEggPropsEntity ppe = (PetEggPropsEntity) ae;
				long petId = ppe.getPetId();
				if (petId > 0) {
					Pet pet = PetManager.getInstance().getPet(petId);
					if (pet != null) {
						PetManager.getInstance().deletePet(this, pet);
						PetManager.logger.info("Player.removeItemOfKnapsack: {} drop PetEggPropsEntity {}", name, petId);
					} else {
						ArticleManager.logger.error("[玩家丢弃物品错误] [" + this.getLogString() + "] [物品id:" + ae.getId() + "] [物品name:" + ae.getArticleName() + "] [index:" + index + "]");
					}
				}
				this.send_HINT_REQ(Translate.translateString(Translate.您失去了物品, new String[][] { { Translate.COUNT_1, count + "" }, { Translate.STRING_1, ae.getArticleName() } }));
			} else {
				String showName = ae.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(showName);
				if(a instanceof InlayArticle){
					showName = ((InlayArticle)a).getShowName();
				}
				this.send_HINT_REQ(Translate.translateString(Translate.您失去了物品, new String[][] { { Translate.COUNT_1, count + "" }, { Translate.STRING_1, showName} }));
			}
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[玩家丢弃物品] [username][{}][{}] [playerId:{}] [物品：{}] [物品id：{}][count:{}][index:{}]", new Object[] { this.getUsername(), this.getName(), this.getId(), ae.getArticleName(), ae.getId(), count, index });
		} else {
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[第{}个背包的{}个cell上没有物品] [{}] [{}] [{}]", new Object[] { bagIndex, index, this.getUsername(), this.getId(), this.getName() });
		}
		if (ae != null) {
			ArticleEntityManager.getInstance().recycleEntity(ae, ArticleEntityManager.DELETE_REASON_PLAYER_DELETE, this);
		}
	}

	/**
	 * 从玩家身上得到一件物品，范围包括所有背包包括装备栏
	 * 
	 * @param id
	 * @return
	 */
	public ArticleEntity getArticleEntity(long id) {
		// 一个装备栏中可容纳装备数
		int ecSize = EquipmentColumn.EQUIPMENT_TYPE_NAMES.length;
		for (int j = 0; j < ecSize; j++) {
			ArticleEntity articleEntity = ecs.get(j);
			if (articleEntity != null && articleEntity.getId() == id) {
				return articleEntity;
			}
		}

		for (int k = 0; k < knapsacks_common.length; k++) {
			if (knapsacks_common[k] != null) {
				loadKnapsack(knapsacks_common[k]);
				for (int i = 0, size = knapsacks_common[k].size(); i < size; i++) {
					ArticleEntity articleEntity = knapsacks_common[k].getArticleEntityByCell(i);
					if (articleEntity != null && articleEntity.getId() == id) {
						return articleEntity;
					}
				}
			}
		}
		if (knapsacks_fangBao != null) {
			loadKnapsack(knapsacks_fangBao);
			for (int i = 0, size = knapsacks_fangBao.size(); i < size; i++) {
				ArticleEntity articleEntity = knapsacks_fangBao.getArticleEntityByCell(i);
				if (articleEntity != null && articleEntity.getId() == id) {
					return articleEntity;
				}
			}
		}
		return null;
	}

	public transient static int pNumForHunshiStat;
	public transient static int aNumForHunshiStat;

	public void loadKnapsack(Knapsack knapsack) {
		if (!knapsack.loaded) {
			synchronized (knapsack) {
				knapsack.loaded = true;
				ArrayList<Long> ids = new ArrayList<Long>();
				Cell[] cells = knapsack.getCells();
				if (cells != null) {
					for (Cell cell : cells) {
						if (cell != null && cell.entityId > 0) {
							ids.add(cell.entityId);
						}
					}
				}
				if (ids.size() > 0) {
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					long[] idss = new long[ids.size()];
					for (int i = 0; i < ids.size(); i++) {
						idss[i] = ids.get(i);
					}
					aem.getEntityByIds(idss);
				}
			}
		}
	}

	/**
	 * 从玩家身上得到一件物品，范围包括所有背包包括装备栏
	 * 
	 * @param id
	 * @return
	 */
	public ArticleEntity getArticleEntity(String articleName) {
		// 一个装备栏中可容纳装备数
		int ecSize = EquipmentColumn.EQUIPMENT_TYPE_NAMES.length;
		for (int j = 0; j < ecSize; j++) {
			ArticleEntity articleEntity = ecs.get(j);
			if (articleEntity != null && articleEntity.getArticleName().equals(articleName)) {
				return articleEntity;
			}
		}

		for (int j = 0; j < ecSize; j++) {
			if (getUnusedSoul() != null) {
				for (Soul soul : getUnusedSoul()) {
					if (soul != null) {
						if (soul.getEc() != null) {
							ArticleEntity articleEntity = soul.getEc().get(j);
							if (articleEntity != null && articleEntity.getArticleName().equals(articleName)) {
								return articleEntity;
							}
						}
					}
				}
			}

		}

		for (int k = 0; k < knapsacks_common.length; k++) {
			for (int i = 0, size = knapsacks_common[k].size(); i < size; i++) {
				ArticleEntity articleEntity = knapsacks_common[k].getArticleEntityByCell(i);
				if (articleEntity != null && articleEntity.getArticleName().equals(articleName)) {
					return articleEntity;
				}
			}
		}
		if (knapsacks_fangBao != null) {
			for (int i = 0, size = knapsacks_fangBao.size(); i < size; i++) {
				ArticleEntity articleEntity = knapsacks_fangBao.getArticleEntityByCell(i);
				if (articleEntity != null && articleEntity.getArticleName().equals(articleName)) {
					return articleEntity;
				}
			}
		}
		return null;
	}

	/**
	 * 从玩家身上得到一件物品，范围包括所有背包包括装备栏
	 * 
	 * @param id
	 * @return
	 */
	public ArticleEntity getArticleEntity(String articleName, boolean isbind) {
		// 一个装备栏中可容纳装备数
		int ecSize = EquipmentColumn.EQUIPMENT_TYPE_NAMES.length;
		for (int j = 0; j < ecSize; j++) {
			ArticleEntity articleEntity = ecs.get(j);
			if (articleEntity != null && articleEntity.getArticleName().equals(articleName) && articleEntity.isBinded() == isbind) {
				return articleEntity;
			}
		}

		for (int j = 0; j < ecSize; j++) {
			if (getUnusedSoul() != null) {
				for (Soul soul : getUnusedSoul()) {
					if (soul != null) {
						if (soul.getEc() != null) {
							ArticleEntity articleEntity = soul.getEc().get(j);
							if (articleEntity != null && articleEntity.getArticleName().equals(articleName) && articleEntity.isBinded() == isbind) {
								return articleEntity;
							}
						}
					}
				}
			}

		}

		for (int k = 0; k < knapsacks_common.length; k++) {
			for (int i = 0, size = knapsacks_common[k].size(); i < size; i++) {
				ArticleEntity articleEntity = knapsacks_common[k].getArticleEntityByCell(i);
				if (articleEntity != null && articleEntity.getArticleName().equals(articleName) && articleEntity.isBinded() == isbind) {
					return articleEntity;
				}
			}
		}
		if (knapsacks_fangBao != null) {
			for (int i = 0, size = knapsacks_fangBao.size(); i < size; i++) {
				ArticleEntity articleEntity = knapsacks_fangBao.getArticleEntityByCell(i);
				if (articleEntity != null && articleEntity.getArticleName().equals(articleName) && articleEntity.isBinded() == isbind) {
					return articleEntity;
				}
			}
		}
		return null;
	}

	/**
	 * 从玩家身上得到物品id的物品数量，范围包括所有背包不包含装备栏
	 * 
	 * @param id
	 * @return
	 */
	public int getArticleEntityNum(long id) {
		// 只计算背包中的数量
		int count = 0;
		for (int k = 0; k < knapsacks_common.length; k++) {
			Knapsack knapsack = knapsacks_common[k];
			Cell[] cells = knapsack.getCells();
			if (cells != null) {
				for (Cell cell : cells) {
					if (cell != null && cell.entityId == id && cell.count > 0) {
						count += cell.count;
					}
				}
			}
		}
		if (knapsacks_fangBao != null) {
			Knapsack knapsack = knapsacks_fangBao;
			if (knapsack != null) {
				Cell[] cells = knapsack.getCells();
				if (cells != null) {
					for (Cell cell : cells) {
						if (cell != null && cell.entityId == id && cell.count > 0) {
							count += cell.count;
						}
					}
				}
			}
		}
		return count;
	}

	// //////////////////////////////////////// 人物技能相关
	// //////////////////////////////////////////

	/**
	 * 主动技能代理
	 */
	private transient ActiveSkillAgent activeSkillAgent = new ActiveSkillAgent(this);

	/**
	 * 光环技能代理
	 */
	private transient AuraSkillAgent auraSkillAgent = new AuraSkillAgent(this);

	public ActiveSkillAgent getActiveSkillAgent() {
		return activeSkillAgent;
	}

	public byte getSkillLevel(int skillId) {

		CareerManager cm = CareerManager.getInstance();
		Skill skill = cm.getSkillById(skillId);
		if (skill == null) {
			return 0;
		}
		if (skill instanceof CommonAttackSkill) {
			return 1;
		}
		Career career = cm.getCareer(this.getCareer());
		if (career != null) {
			if (career.basicSkills != null) {
				for (int i = 0; i < career.basicSkills.length; i++) {
					skill = career.basicSkills[i];
					if (skill != null && skill.getId() == skillId) {
						return careerBasicSkillsLevels[i];
					}
				}
			}
			if (career.threads != null) {
				for (int ii = 0; ii < career.threads.length; ii++) {
					CareerThread ct = career.threads[ii];
					if (ct != null) {
						Skill[] skills = ct.skills;
						for (int i = 0; i < skills.length; i++) {
							skill = skills[i];
							if (skill != null && skill.getId() == skillId) {
								return skillOneLevels[i];
							}
						}
					}
				}

			}
			if (career.nuqiSkills != null) {
				for (int i = 0; i < career.nuqiSkills.length; i++) {
					skill = career.nuqiSkills[i];
					if (skill != null && skill.getId() == skillId) {
						return nuqiSkillsLevels[i];
					}
				}
			}
			if (career.xinfaSkills != null) {
				for (int i = 0; i < career.xinfaSkills.length; i++) {
					skill = career.xinfaSkills[i];
					if (skill != null && skill.getId() == skillId) {
						return xinfaLevels[i];
					}
				}
			}
			if (career.kingSkills != null) {
				for (int i = 0; i < career.kingSkills.length; i++) {
					skill = career.kingSkills[i];
					if (skill != null && skill.getId() == skillId) {
						CountryManager cmm = CountryManager.getInstance();
						if (cmm.官职(this.getCountry(), this.getId()) == CountryManager.国王) {
							return 1;
						}
					}
				}
			}
			if (career.bianShenSkills != null) {
				for (int i = 0; i < career.bianShenSkills.length; i++) {
					skill = career.bianShenSkills[i];
					if (skill != null && skill.getId() == skillId) {
						return bianShenLevels[i];
					}
				}
			}
		}

		if (得到装备上的技能(skillId) != null) {
			return 1;
		}

		return 0;
	}

	public byte addSkillLevel(int skillId) {

		CareerManager cm = CareerManager.getInstance();
		Skill skill = cm.getSkillById(skillId);
		if (skill == null) {
			return 0;
		}
		if (skill instanceof CommonAttackSkill) {
			return 0;
		}
		Career career = cm.getCareer(this.getCareer());
		if (career != null) {
			if (career.basicSkills != null) {
				for (int i = 0; i < career.basicSkills.length; i++) {
					skill = career.basicSkills[i];
					if (skill != null && skill.getId() == skillId) {
						careerBasicSkillsLevels[i] += 1;
						this.setCareerBasicSkillsLevels(careerBasicSkillsLevels);
						return careerBasicSkillsLevels[i];
					}
				}
			}
			if (career.threads != null) {
				for (int ii = 0; ii < career.threads.length; ii++) {
					CareerThread ct = career.threads[ii];
					if (ct != null) {
						Skill[] skills = ct.skills;
						for (int i = 0; i < skills.length; i++) {
							skill = skills[i];
							if (skill != null && skill.getId() == skillId) {
								skillOneLevels[i] += 1;
								this.setSkillOneLevels(skillOneLevels);
								return skillOneLevels[i];
							}
						}
					}
				}

			}
			if (career.nuqiSkills != null) {
				for (int i = 0; i < career.nuqiSkills.length; i++) {
					skill = career.nuqiSkills[i];
					if (skill != null && skill.getId() == skillId) {
						nuqiSkillsLevels[i] += 1;
						this.setNuqiSkillsLevels(nuqiSkillsLevels);
						return nuqiSkillsLevels[i];
					}
				}
			}
			if (career.xinfaSkills != null) {
				for (int i = 0; i < career.xinfaSkills.length; i++) {
					skill = career.xinfaSkills[i];
					if (skill != null && skill.getId() == skillId) {
						xinfaLevels[i] += 1;
						this.setXinfaLevels(xinfaLevels);
						return xinfaLevels[i];
					}
				}
			}
			if (career.bianShenSkills != null) {
				for (int i = 0; i < career.bianShenSkills.length; i++) {
					skill = career.bianShenSkills[i];
					if (skill != null && skill.getId() == skillId) {
						bianShenLevels[i] += 1;
						try {
							if (skill.getId() == 306) { // 雷动技能会改变攻击距离需要单独发送给客户端
								// CareerManager cm = CareerManager.getInstance();
								int ids[] = new int[Weapon.WEAPONTYPE_NAME.length];
								for (int ii = 0; ii < ids.length; ii++) {
									CommonAttackSkill ss = cm.getCommonAttackSkill((byte) ii);
									if (ss != null) {
										ids[ii] = ss.getId();
									}
								}
								SkillWithoutEffectAndQuickMove tempSk = (SkillWithoutEffectAndQuickMove) CareerManager.getSkillInfo(306, this, new SkillWithoutEffectAndQuickMove());

								SkillWithoutEffectAndQuickMove[] tempsk = new SkillWithoutEffectAndQuickMove[] { tempSk };
								SEND_ACTIVESKILL_REQ req3 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, new CommonAttackSkill[0], new SkillWithoutEffect[0], tempsk, new SkillWithoutTraceAndWithRange[0], new SkillWithoutTraceAndWithTargetOrPosition[0], new SkillWithoutTraceAndWithSummonNPC[0], new SkillWithTraceAndDirectionOrTarget[0], new SkillWithoutTraceAndWithMatrix[0], new SkillWithoutTraceAndOnTeamMember[0]);

								this.addMessageToRightBag(req3);
							}
						} catch (Exception e) {
							Skill.logger.warn("[处理雷动单独协议] [异常] [" + this.getLogString() + "]", e);
						}
						this.setBianShenLevels(bianShenLevels);
						return bianShenLevels[i];
					}
				}
			}
		}

		return 0;
	}

	@Override
	public void setCareerBasicSkillsLevels(byte[] value) {
		super.setCareerBasicSkillsLevels(value);
		Soul s = this.getCurrSoul();
		if (s != null) {
			s.setCareerBasicSkillsLevels(getCareerBasicSkillsLevels());
			setDirty(true, "currSoul");
		}
	}

	@Override
	public void setSkillOneLevels(byte[] value) {
		super.setSkillOneLevels(value);
		Soul s = this.getCurrSoul();
		if (s != null) {
			s.setSkillOneLevels(getSkillOneLevels());
			setDirty(true, "currSoul");
		}
	}

	/**
	 * 使用道具代理
	 */
	private transient UsingPropsAgent usingPropsAgent = new UsingPropsAgent(this);

	/**
	 * 传送需要用到的数据
	 */
	private transient TransportData transportData;

	/**
	 * 上次保存时间
	 */
	private long lastUpdateTime = 0;

	/**
	 * 标记用户是否刚刚进入地图，为了是解决用户断线后，立即连接上来的情况 这种情况下，需要给用户广播广播区域内的东西
	 */
	public transient boolean newlyEnterGameFlag = false;

	/**
	 * 上一次使用复活卡复活的时间
	 * 
	 */
	private transient long lastRevivedTime = 0;

	/**
	 * 每天清空
	 */
	public transient int revivedCount = 0;

	/**
	 * 复活卡的CD
	 */
	private transient long revivedCD = 60 * 1000;

	/**
	 * 最后在切磋范围的时间， 如果用户在切磋的状态下，此变量需要不断的更新
	 */
	private transient long lastTimeInQieCuoRegion = 0;

	/**
	 * 上一次通知客户端出切磋区域的时间
	 */
	private transient long lastTimeForQieCuoOutofRegionNotify = 0;

	/**
	 * 检查邮件
	 */
	private long lastCheckMailTime = 0;

	//每天，每周，每月
	private int [] cityEnterLimitNum = new int[3];
	private long [] cityEnterLimitDate = new long[3];
	
	public int[] getCityEnterLimitNum() {
		return cityEnterLimitNum;
	}

	public void setCityEnterLimitNum(int[] cityEnterLimitNum) {
		this.cityEnterLimitNum = cityEnterLimitNum;
		this.setDirty(true, "cityEnterLimitNum");
	}

	public long[] getCityEnterLimitDate() {
		return cityEnterLimitDate;
	}

	public void setCityEnterLimitDate(long[] cityEnterLimitDate) {
		this.cityEnterLimitDate = cityEnterLimitDate;
		this.setDirty(true, "cityEnterLimitDate");
	}

	/**
	 * 开始切磋，此方法会通知客户端切磋开始
	 * 
	 * @param qiecouTarget
	 */
	public void qiecuoStart(Player qiecouTarget) {
	}

	/**
	 * 切磋结束，此方法会通知客户端，切磋结束
	 * 
	 * @param win
	 *            标识此玩家胜利或者失败
	 */
	private void qiecuoEnd(long heartBeatStartTime, boolean win) {
	}

	// ///////////////////////////////// 玩家身上的统计数据
	// /////////////////////////////////////////

	/**
	 * 玩家的统计数据
	 */
	protected transient StatData[] statDatas = new StatData[0];

	public StatData[] getStatDatas() {
		return statDatas;
	}

	public void setStatData(StatData datas[]) {
		this.statDatas = datas;
		if (this.statDatas != null) {
			for (StatData sd : this.statDatas) {
				if (sd != null) {
					sd.setDirty(true);
				}
			}
		}
	}

	public StatData getStatData(int statId) {
		for (StatData data : statDatas) {
			if (statId == data.getStatId()) {
				return data;
			}
		}
		return null;
	}

	public void addStatData(StatData data) {
		if (getStatData(data.getStatId()) == null && data.getPlayerId() == this.id) {
			StatData datas[] = new StatData[statDatas.length + 1];
			System.arraycopy(statDatas, 0, datas, 0, statDatas.length);
			data.setDirty(true);
			datas[datas.length - 1] = data;
			this.statDatas = datas;
		} else {
			// Game.logger.warn("[添加统计项失败] [统计项已存在或者角色id不匹配] [角色:" + name +
			// "] [角色id:" + id + "] [统计项:" + data.getStatId() +
			// "] [dataPlayerId:" + data.getPlayerId() + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[添加统计项失败] [统计项已存在或者角色id不匹配] [角色:{}] [角色id:{}] [统计项:{}] [dataPlayerId:{}]", new Object[] { name, id, data.getStatId(), data.getPlayerId() });
		}
	}

	// ///////////////////////////////// 战斗相关
	// /////////////////////////////////////////////////

	/**
	 * 临时的敌人列表，此列表用于存放临时的敌人，比如中立的NPC，切磋的对象， 以及竞技场中，对立的玩家或者队伍成员。
	 * 
	 * 但列表中的对象，死亡后，就会从列表中清除，以及但自己死亡后，也会将此列表清空。
	 * 
	 * 此列表会通过协议与客户端进行同步
	 */
	public transient ArrayList<Fighter> transientEnemyList = new ArrayList<Fighter>();
	public transient Hashtable<Long, Long> transientEnemyPlayerAttackTime = new Hashtable<Long, Long>();

	/**
	 * 通知玩家对象，角色进入到服务器上
	 */
	public void notifyPlayerEnterServer() {
		Boolean vipEnter = (Boolean) this.getConn().getAttachmentData("vipEnter");
		if (vipEnter != null && vipEnter.equals(Boolean.TRUE)) {
			this.getConn().setAttachmentData("vipEnter", null);
			this.send_HINT_REQ(Translate.VIP不用排队, (byte) 0);
		}
	}

	/**
	 * 为玩家增加一个临时的敌人，此敌人必须是中立的NPC， 或者 正在切磋的同阵营的玩家，其他都一律不做任何处理
	 * 
	 * @param f
	 */
	public void addOneTransientEnemy(Fighter f) {
	}

	public void removeOneTransientEnemy(Fighter f) {
	}

	public void clearTransientEnemy() {
	}

	/**
	 * 玩家所在的团队，如果玩家不在任何团队中，此变量为null
	 */
	transient Team team;

	public Team getTeam() {
		return team;
	}

	// public void setTeam(Team t) {
	// team = t;
	// }

	// /////////pk相关/////////////////

	/**
	 * 主动攻击，即灰名状态，处于该状态时，其他玩家将其杀死不会有任何惩罚
	 */
	public transient boolean activePk;

	/**
	 * 上次主动攻击时的时间，此值没有记录巡捕主动pk的时间
	 */
	private long lastActivePkTime;

	public long getLastActivePkTime() {
		return lastActivePkTime;
	}

	public void setLastActivePkTime(long lastActivePkTime) {
		this.lastActivePkTime = lastActivePkTime;
		this.setDirty(true, "lastActivePkTime");
	}

	/**
	 * 是否在蹲监狱
	 */
	public transient boolean isInPrison;

	/**
	 * 用于pk的杀怪，只有杀同等级以上的怪才会计算数量，当数量到达一定值后减少罪恶值，并且数量清零
	 */
	public transient int killMonsterCountForPk;

	public static int KILL_MONSTER = 10;

	public static int DECREASE_EVIL_FOR_KILL_MONSTER = 10;

	/**
	 * 上次随时间减少罪恶值的时间
	 */
	public transient long lastDecreaseEvilTime;

	public static final long 绿色名称时间 = 30 * 60 * 60 * 1000;

	public static final byte 名字颜色_绿色 = -1;
	public static final byte 名字颜色_白色 = 0;
	public static final byte 名字颜色_灰色 = 1;
	public static final byte 名字颜色_粉红色 = 2;
	public static final byte 名字颜色_红色 = 3;
	public static final byte 名字颜色_紫红色 = 4;
	public static final byte 名字颜色_深红色 = 5;
	public static final byte 名字颜色_黑红色 = 6;
	public static final byte 普通_类型 = 0;
	public static final byte 恶人_类型 = 1;
	public static final byte 恶霸_类型 = 2;
	public static final byte 罪大恶极_类型 = 3;
	public static final byte 十恶不赦_类型 = 4;
	public static final byte 万恶之首_类型 = 5;

	public static final int[][] 恶名等级对应表 = new int[][] { { Integer.MIN_VALUE, 0 }, { 1, 25 }, { 26, 50 }, { 51, 75 }, { 76, 100 }, { 101, Integer.MAX_VALUE } };
	public static final String[] 恶名名称 = new String[] { Translate.普通, Translate.恶人, Translate.恶霸, Translate.罪大恶极, Translate.十恶不赦, Translate.万恶之首, };
	public static final byte[] 恶名名称对应类型 = new byte[] { 普通_类型, 恶人_类型, 恶霸_类型, 罪大恶极_类型, 十恶不赦_类型, 万恶之首_类型 };
	public static final byte[] 恶名名称对应颜色类型 = new byte[] { 名字颜色_白色, 名字颜色_粉红色, 名字颜色_红色, 名字颜色_紫红色, 名字颜色_深红色, 名字颜色_黑红色 };

	public static double[] 死亡掉落个数概率 = new double[] { 0.5, 0.3, 0.2 };
	public static int[] 死亡掉落个数 = new int[] { 1, 2, 3 };

	public void killMonsterDecreaseEvil(int monsterLevel) {
		if (monsterLevel < getSoulLevel()) {
			return;
		}
		killMonsterCountForPk++;
		if (killMonsterCountForPk >= KILL_MONSTER) {
			if (evil > 0) {
				if (evil > DECREASE_EVIL_FOR_KILL_MONSTER) {
					setEvil(evil - DECREASE_EVIL_FOR_KILL_MONSTER);
				} else {
					setEvil(0);
					setLastActivePkTime(SystemTime.currentTimeMillis());
				}
				if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info(getLogString()+"[杀怪减少红名值] [减少后当前红名值:"+ getEvil() +"]");
			}
			killMonsterCountForPk = 0;
		}
	}

	// ////////////////

	// /////////////////////////////////// 反外挂处理相关
	// ///////////////////////////////

	/**
	 * 记录玩家的非法操作
	 * 
	 */
	public static class IllegalOperation {

		public static final int 非法移动路径 = 0;

		public static final int 非法设置玩家坐标位置 = 1;

		// 操作的时间
		public long operatorTime;

		// 类型
		public int type;

		// 描述
		public String description;

		public IllegalOperation(long operatorTime, int type, String description) {
			this.operatorTime = operatorTime;
			this.type = type;
			this.description = description;
		}
	}

	private transient ArrayList<IllegalOperation> illegalOperationList = new ArrayList<IllegalOperation>();

	private transient long lastCheckIllegalOperationTime = 0;

	public IllegalOperation[] getIllegalOperations() {
		return illegalOperationList.toArray(new IllegalOperation[0]);
	}

	public void addIllegalOperation(int type, String description) {
		illegalOperationList.add(new IllegalOperation(com.fy.engineserver.gametime.SystemTime.currentTimeMillis(), type, description));
		// Game.logger.warn("[非法操作] [" + this.getName() + "] [" + type + "] [" +
		// description + "] [一共:" + illegalOperationList.size() + "]");
		if (Game.logger.isWarnEnabled()) Game.logger.warn("[非法操作] [{}] [{}] [{}] [一共:{}]", new Object[] { this.getName(), type, description, illegalOperationList.size() });
	}

	/**
	 * 玩家身上的聊天频道状态 0 - 关闭 1 - 开启 下标为频道的类别 add by 
	 */
	private byte chatChannelStatus[];

	/**
	 * 聊天频道是否是开启的
	 * 
	 * @param chatChannelType
	 * @return add by 
	 */
	public boolean isChatChannelOpenning(int chatChannelType) {
		boolean openning = false;
		try {
			openning = chatChannelStatus[chatChannelType] == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openning;
	}

	/**
	 * 设置聊天频道状态
	 * 
	 * @param chatChannelType
	 * @param status
	 *            0 - 关闭, 1 - 开启 add by 
	 */
	public void setChatChannelStatus(int chatChannelType, byte status) {
		try {
			chatChannelStatus[chatChannelType] = status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		setDirty(true, "chatChannelStatus");
	}

	public byte[] getChatChannelStatus() {
		return chatChannelStatus;
	}

	public void setChatChannelStatus(byte[] chatChannelStatus) {
		this.chatChannelStatus = chatChannelStatus;
		setDirty(true, "chatChannelStatus");
	}

	/**
	 * 快捷键设置
	 */
	private byte[] shortcut = new byte[0];

	/**
	 * 获取玩家的快捷键设置
	 * 
	 * @return
	 */
	public byte[] getShortcut() {
		return shortcut;
	}

	/**
	 * 设置
	 * 
	 * @param shortcut
	 */
	public void setShortcut(byte[] shortcut) {
		this.shortcut = shortcut;
		setDirty(true, "shortcut");
	}

	@Override
	public void setSpeed(int value) {
		if (value <= 0) {
			value = 1;
		}
		if(value > 999){
			value = 999;
		}
		super.setSpeed(value);
		if (path != null) {
			path.speedChanged(speed, 0L);
		}
	}

	@Deprecated
	public void setBindSilver(long money) {
		super.setBindSilver(money);
		setDirty(true, "bindSilver");
	}

	@Deprecated
	public void setSilver(long silver) {
		long old = this.silver;
		super.setSilver(silver);
		GamePlayerManager.logger.warn("[变化2] ["+old+"/"+silver+"] ["+getId()+"] ["+getName()+"]");
		setDirty(true, "silver");
		GamePlayerManager.createPlayerMsg(this, 0, true);
		if (silver > highWaterOfSilver) {
			setHighWaterOfSilver(silver);
		}
		// 如果是银子，立即保存
		try {
			PlayerManager.getInstance().savePlayer(this);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		}
		if (silver != old) {
			try {
				String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
				if (stacktrace.indexOf("BillingCenter") == -1) {
					Game.logger.warn("[警告：直接调用setSilver()方法] [amount:" + silver + "] [player:" + this.getLogString() + "]\n" + stacktrace);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setRmbyuanbao(long rmbyuanbao) {
		super.setRmbyuanbao(rmbyuanbao);
		PLAYER_YUANBAO_CHANGED_REQ req = new PLAYER_YUANBAO_CHANGED_REQ(GameMessageFactory.nextSequnceNum(), getRmbyuanbao(), getTotalRmbyuanbao());
		addMessageToRightBag(req);
		setDirty(true, "rmbyuanbao");
	}

	// update here
	// 系统心跳中加血和蓝，即HP和MP
	private transient long lastTimeForRecoverHPAAndMPA;
	private transient long lastTimeForRecoverHPBAndMPB;
	private transient long lastTimeForBuffs;
	private transient long lastTimeForArticeValidTime;
	private transient long lastTimeForKnapsackAndEquipment;
	private transient long lastTimeForLimitTimeTask;// 心跳中检查限时交付任务
	private transient long lastTimeForNameColor;
	private transient List<String> scanedTask = new ArrayList<String>();

	private transient long lastTimeForCave;

	public static class EnemyEntity {
		Fighter enemy;
		long lastUpdateTime;
		boolean activeBreak = false;
		boolean passiveBreak = false;

		public EnemyEntity() {

		}

		@Override
		protected void finalize() throws Throwable {

		}

		public Fighter getEnemy() {
			return enemy;
		}

		public long getLastUpdateTime() {
			return lastUpdateTime;
		}

		public boolean isActiveBreak() {
			return activeBreak;
		}

		public boolean isPassiveBreak() {
			return passiveBreak;
		}

	}

	public static class EnemyList {

		ArrayList<EnemyEntity> enemyList = new ArrayList<EnemyEntity>();

		public EnemyList() {

		}

		@Override
		protected void finalize() throws Throwable {

		}

		public boolean contains(Fighter f) {
			for (EnemyEntity ee : enemyList) {
				if (ee.enemy == f) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 是否为空
		 * 
		 * @return
		 */
		public boolean isEmpty() {
			return enemyList.size() == 0;
		}

		/**
		 * 增加一个敌人
		 * 
		 * @param f
		 */
		public void add(Fighter f, boolean active, long now) {
			for (int i = 0; i < enemyList.size(); i++) {
				EnemyEntity ee = enemyList.get(i);
				if (ee != null && ee.enemy == f) {
					ee.lastUpdateTime = now;
					if (active) ee.activeBreak = false;
					else ee.passiveBreak = false;
					return;
				}
			}
			EnemyEntity ee = new EnemyEntity();
			ee.enemy = f;
			ee.lastUpdateTime = now;
			enemyList.add(ee);
		}

		/**
		 * 主动去掉一个敌人
		 * 
		 * @param f
		 */
		public void activeBreak(Fighter f) {
			for (int i = 0; i < enemyList.size(); i++) {
				EnemyEntity ee = enemyList.get(i);
				if (ee != null && ee.enemy == f) {
					ee.activeBreak = true;
					break;
				}
			}
		}

		/**
		 * 被动去掉一个敌人
		 * 
		 * @param f
		 */
		public void passiveBreak(Fighter f) {
			for (int i = 0; i < enemyList.size(); i++) {
				EnemyEntity ee = enemyList.get(i);
				if (ee != null && ee.enemy == f) {
					ee.passiveBreak = true;
					break;
				}
			}
		}

		public void flush(Game g, long now) {

			for (int i = enemyList.size() - 1; i >= 0; i--) {
				EnemyEntity ee = enemyList.get(i);
				if (ee == null) {
					enemyList.remove(i);
					continue;
				}
				if (ee.lastUpdateTime + 30000 < now) {
					enemyList.remove(i);
				} else if (ee.activeBreak || ee.passiveBreak) {
					enemyList.remove(i);
				} else if (ee.enemy.isDeath() || (ee.enemy instanceof LivingObject && ((LivingObject) ee.enemy).isAlive() == false)) {
					enemyList.remove(i);
				} else if (g.contains((LivingObject) ee.enemy) == false) {
					enemyList.remove(i);
				}
			}
		}
	}

	/**
	 * 敌人列表
	 * 
	 * 当玩家或者怪开始攻击我的时候，调用notifyPrepareToBeFighted方法，
	 * 但怪不再攻击我的时候，调用此方法notifyEndToBeFighted
	 * 当我从玩家广播区域消失的时候，调用此方法notifyEndToBeFighted
	 * 当怪或者玩家从我的广播区域消失的时候，调用此方法notifyLeaveMyBroadcastRegion
	 * 
	 * 在加上causeDamage和damageFeedback，来控制敌人列表的变化
	 * 
	 * 列表中的对象，一段时间内没有刷新，就会被清除
	 */
	public transient EnemyList enemyList = new EnemyList();

	/**
	 * 获得敌人列表
	 * 
	 * @return
	 */
	public EnemyEntity[] getEnimyList() {
		return enemyList.enemyList.toArray(new EnemyEntity[0]);
	}

	/** 护盾，吸收 */
	private transient int recoverHpHuDun;

	// 护盾
	protected int huDunDamage = 0;

	/**
	 * 护盾能吸收伤害的值
	 */
	public int getHuDunDamage() {
		return huDunDamage;
	}

	/**
	 * 设置护盾能吸收伤害的值
	 */
	public void setHuDunDamage(int hudun) {
		huDunDamage = hudun;
		if (huDunDamage > 0 && this.getEncloser() != 2) {
			this.setEncloser((byte) 2);
		} else if (huDunDamage <= 0) {
			this.setEncloser((byte) 0);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	//
	// 重载所有的A,B,C类方法
	//
	// //////////////////////////////////////////////////////////////////////////////

	// 增加仇恨的比例，也就是1表示百分之一个点
	protected int hatridRate = 0;

	public void setHatridRate(int hr) {
		hatridRate = hr;
		setDirty(true, "hatridRate");
	}

	public int getHatridRate() {
		return hatridRate;
	}

	/**
	 * 修改力量，会修改物理攻击上限4，物理攻击下限4，物理防御1
	 */
	public void setStrength(int value) {
		// super.setStrength(value);
		// int k = NumericalCalculator.根据一级属性计算二级属性基本值(strength,
		// NumericalCalculator.力量兑换外功);
		// this.setPhyAttackA(k);

	}

	/**
	 * 修改内力，会修改魔法攻击上限3，魔法攻击下限3，魔法防御2，MP上限5，加每5秒恢复MP1
	 */
	public void setSpellpower(int value) {
		// super.setSpellpower(value);
		// int k = NumericalCalculator.根据一级属性计算二级属性基本值(spellpower,
		// NumericalCalculator.灵力兑换内法);
		// this.setMagicAttackA(k);
	}

	/**
	 * 修改灵巧，会修改物理攻击上限1，物理攻击下限1，物理暴击8，回避5，命中10
	 */
	public void setDexterity(int value) {
		// super.setDexterity(value);
		// int k = NumericalCalculator.根据一级属性计算二级属性基本值(dexterity,
		// NumericalCalculator.身法兑换命中);
		// this.setHitA(k);
		// k = NumericalCalculator.根据一级属性计算二级属性基本值(dexterity,
		// NumericalCalculator.身法兑换闪躲);
		// this.setDodgeA(k);
		// k = NumericalCalculator.根据一级属性计算二级属性基本值(dexterity,
		// NumericalCalculator.身法兑换会心一击);
		// this.setCriticalHitA(k);

	}

	/**
	 * 修改体力，会修改物理防御3，魔法防御1，HP上限10，加5秒恢复HP1
	 */
	public void setConstitution(int value) {
		// super.setConstitution(value);
		// int k = NumericalCalculator.根据一级属性计算二级属性基本值(constitution,
		// NumericalCalculator.耐力兑换HP);
		// this.setMaxHPA(k);

	}
	
	public void setCriticalHit(int value) {
		super.setCriticalHit(value);
		int hitrate = CombatCaculator.CAL_会心一击率(value, level);
		this.setCriticalHitRate(hitrate);
	}

	@Override
	public int getCriticalHitRateOther() {

		if (this.getCareer() == 5 && this.isShouStatus()) {
			return this.criticalHitRateOther + 200 + bianshenSKlv * 10;
		}
		return this.criticalHitRateOther;
	}

	/**
	 * 升级，会改变血上限和蓝上限
	 */
	public void setLevel(int level) {
		super.setLevel(level);
		noticeTeamMarks[4] = true;
		setDirty(true, "level");
	}

	@Override
	public void setSoulLevel(int value) {
		super.setSoulLevel(value);
		Soul s = this.getCurrSoul();
		if (s != null) {
			s.setGrade(value);
			if (s.isMainSoul()) {
				setLevel(value);
			}
		}
		setDirty(true, "soulLevel");
		setDirty(true, "currSoul");
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 */
	public void openAura(int skillId) {
		CareerManager cm = CareerManager.getInstance();
		Career career = cm.getCareer(this.career);
		CareerThread ct = career.getCareerThreadBySkillId(skillId);
		if (ct == null) {
			// Game.logger.warn("[setOpenedAuraSkillIndex] [error] [要开启的光环技能不存在任何职业线中] [id:"
			// + skillId + "] [career:" + this.career + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[setOpenedAuraSkillIndex] [error] [要开启的光环技能不存在任何职业线中] [id:{}] [career:{}]", new Object[] { skillId, this.career });
			return;
		}
		Skill skill = ct.getSkillById(skillId);
		if (skill == null) {
			// Game.logger.warn("[setOpenedAuraSkillIndex] [error] [要开启的光环技能不存在] [id:"
			// + skillId + "] [career:" + ct.getName() + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[setOpenedAuraSkillIndex] [error] [要开启的光环技能不存在] [id:{}] [career:{}]", new Object[] { skillId, ct.getName() });
			return;
		}

		if (skill instanceof AuraSkill) {
			AuraSkill as = (AuraSkill) skill;
			int l = getSkillLevel(as.getId());
			if (l > 0) { // 可开启的情况下
				super.setOpenedAuraSkillID((short) skillId);
				this.auraSkillAgent.openAuraSkill(as);
				this.setAura(as.getAuraType());
			} else {
				// Game.logger.warn("[setOpenedAuraSkillIndex] [error] [要开启的光环技能还没有学习] [id:"
				// + as.getName() + "] [career:" + ct.getName() + "]");
				if (Game.logger.isWarnEnabled()) Game.logger.warn("[setOpenedAuraSkillIndex] [error] [要开启的光环技能还没有学习] [id:{}] [career:{}]", new Object[] { as.getName(), ct.getName() });
			}
		} else {
			// Game.logger.warn("[setOpenedAuraSkillIndex] [error] [要开启的光环技能不是光环技能] [id:"
			// + skill.getName() + "] [career:" + ct.getName() + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[setOpenedAuraSkillIndex] [error] [要开启的光环技能不是光环技能] [id:{}] [career:{}]", new Object[] { skill.getName(), ct.getName() });
			return;
		}
	}

	public void closeAura() {
		setOpenedAuraSkillID((short) -1);
		this.auraSkillAgent.openAuraSkill(null);
		this.setAura((byte) -1);
	}

	public transient boolean initializing = false;

	public void testLog(String reason){
		if(getName().equals("opo")){
			System.out.println("-----test----"+reason+"---exp:"+getExp()+"---speed:"+getSpeed());
		}
	}
	
	/**
	 * 初始化玩家数据，此方法在玩家从数据库中加载到内存中后，调用。
	 * 
	 * 数据库中只保留最基本的属性，比如 力量A，内力A，灵巧A，体力A 其他值，我们都可以通过玩家身上配置的装备和Buff来收集 收集的方法如下： 1.
	 * 先设置力量A，内力A，灵巧A，体力A 2. 将玩家身上的装备相当于重新装置一遍，以改变其他B和C的数值 3.
	 * 将玩家身上的Buff相当于重新种植一遍，以改变其他B和C的数值
	 * 
	 */
	public void init(String reason) throws Exception {
		if (initializing == true) {

			return;
		}
		if (xinfaLevels == null || xinfaLevels.length != 40) {
			byte[] newXinFaLevels = new byte[40];
			if (xinfaLevels != null) {
				for (int i = 0; i < xinfaLevels.length; i++) {
					newXinFaLevels[i] = xinfaLevels[i];
				}
			}
			setXinfaLevels(newXinFaLevels);
		}
		if (ecs != null) {
			ecs.fixChiBang();
			ecs.fixfabao();
		}
		initializing = true;

		if (vipAgent == null) {
			this.setVipAgent(new VipAgent());
		}
		vipAgent.setOwner(this);

		清空所有的BCX值();
		this.setGangName("");
		// 初始化背包
		if (knapsacks_common != null) {
			for (Knapsack k : knapsacks_common) {
				if (k != null && k.getCells() != null) {
					k.init(this, k.getCells().length);
				}
			}
		}
		if (knapsacks_fangBao != null && knapsacks_fangBao.getCells() != null) {
			knapsacks_fangBao.init(this, knapsacks_fangBao.getCells().length);
		}

		if (knapsacks_cangku != null && knapsacks_cangku.getCells() != null) {
			knapsacks_cangku.init(this, knapsacks_cangku.getCells().length);
		}
		if (knapsacks_QiLing == null || knapsacks_QiLing.getCells() == null || knapsacks_QiLing.getCells().length == 0) {
			knapsacks_QiLing = new Knapsack(this, 60);
		}
		if (knapsacks_QiLing != null && knapsacks_QiLing.getCells() != null) {
			knapsacks_QiLing.init(this, knapsacks_QiLing.getCells().length);
		}
		if (knapsacks_warehouse != null && knapsacks_warehouse.getCells() != null) {
			knapsacks_warehouse.init(this, knapsacks_warehouse.getCells().length);
		}

		// ///////////////
		filedChangeEventList = new ArrayList<FieldChangeEvent>();
		activeSkillAgent = new ActiveSkillAgent(this);

		BuffTemplateManager btm = BuffTemplateManager.getInstance();

		// 重置buff
		for (int i = buffs.size() - 1; i >= 0; i--) {
			Buff buff = buffs.get(i);
			if (buff != null) {
				String startTime = "";
				String endTime = "";
				try {
					startTime = StringUtil.formatDate(new Date(buff.getStartTime()), "yyyy-MM-dd HH:mm:ss");
					endTime = StringUtil.formatDate(new Date(buff.getInvalidTime()), "yyyy-MM-dd HH:mm:ss");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Game.logger.warn("[准备初始化buff] [{}] [{}] [开始时间:{}] [结束时间:{}]", new Object[] { getName(), buff.getTemplateName(), startTime, endTime });

			} else {

				Game.logger.warn("[准备初始化buff] [{}] [buff==null]", new Object[] { getName() });

			}

			if (buff != null && !(buff instanceof AuraBuff)) {
				String tn = buff.getTemplateName();
				BuffTemplate bt = btm.getBuffTemplateByName(tn);
				if (bt != null) {
					buff.setTemplate(bt);
					buff.setCauser(this);// 2012-12-24 15:47:14将buff的释放者设置为自己,
					boolean isSilence2 = false;
					// if (buff instanceof Buff_Silence2) {
					// Buff_Silence2 b2 = (Buff_Silence2) buff;
					// if (b2.getSilenceLevel() == 2) {
					// isSilence2 = true;
					// }
					// }
					if (isSilence2) {
						buffs.remove(i);
						setDirty(true, "buffs");
						Game.logger.warn(this.getLogString() + "[解除沉默]");
					} else {
						if (bt.getTimeType() == 0) {
							if (buff.getHeartBeatStartTime() == 0) {
								buff.setInvalidTime(buff.getInvalidTime());
								buff.setHeartBeatStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							} else {
								buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + buff.getInvalidTime() - buff.getHeartBeatStartTime());
								buff.setHeartBeatStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							}
							buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						} else if (bt.getTimeType() == 2) {
							buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						}
					}
					if (Game.logger.isDebugEnabled()) Game.logger.debug("[初始化设置BUFF] [{}] [{}] [class:{}] [timeType:{}] [剩余时间：{}ms]", new Object[] { getName(), bt.getName(), buff.getClass().getName(), bt.getTimeType(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - buff.getInvalidTime()) });

				} else {
					// error:
					buffs.remove(i);
					setDirty(true, "buffs");

					if (Game.logger.isDebugEnabled()) {
						Game.logger.debug("[初始化去除BUFF] [{}] [模板为空] [{}:{}] [time:{}]", new Object[] { getName(), buff.getClass().getName(), buff.getTemplateName(), buff.getInvalidTime() });
					}
				}

			} else {
				buffs.remove(i);
				setDirty(true, "buffs");

				if (buff != null) {
					if (Game.logger.isDebugEnabled()) {
						Game.logger.debug("[初始化去除BUFF] [{}]", new Object[] { getName() });
					}
				}
			}
		}
		// ///////////////
		// 加载元神 和元神加成
		setTotalXp(1000);
		setMaxVitality(360);
		if (vitality > getMaxVitality()) {
			setVitality(getMaxVitality());
		}
		initSoul();
		try{
			setSoulLevel(getCurrSoul().getGrade());
			initTasks();
			initPeopleSearch();
			initNextLevelExpPool();
			this.setSpeedA(this.speedA);
			this.setCommonAttackSpeed((short) 2000);
			this.aura = -1;

			// 计算一级属性
			ExperienceManager em = ExperienceManager.getInstance();
			if (this.level <= em.maxLevel) this.setNextLevelExp(em.maxExpOfLevel[this.getSoulLevel()]);
			else this.setNextLevelExp(em.maxExpOfLevel[em.maxLevel]);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("============异常："+e.toString());
		}
		this.initNextLevelExpPool();
		// 重新种植所有的buff
		for (int i = 0; i < buffs.size(); i++) {
			Buff buff = buffs.get(i);
			if (buff != null) {
				buff.start(this);
			}
		}

		// 开启光环技能
		if (openedAuraSkillID > 0) {
			openAura(openedAuraSkillID);
		}
		this.timerTaskAgent = new TimerTaskAgent(this);
		// 加载家族
		initJiazuTitleAndIcon();

		initHorse();
		try {
			Horse2EntityManager.instance.checkPlayerHorse(this);
		} catch (Exception e) {
			Horse2Manager.logger.error("[新坐骑系统] [自动转换玩家坐骑异常] [" + this.getLogString() + "]", e);
		}
		initAvta();
		initAvataProps();
		initZongPaiName();
		initPersonnalTitle();
		modifyBournTaskAndTime();
		this.countdownAgent = new CountdownAgent(this);
		CountryManager cm = CountryManager.getInstance();
		if (cm != null) {
			this.setCountryPosition(cm.官职(this.getCountry(), this.getId()));
			if (CountryManager.logger.isInfoEnabled()) CountryManager.logger.info("[人物初始化] [{}] [{}] [{}] [{}]", new Object[] { this.getCountryPosition(), this.getUsername(), this.getId(), this.getName() });
		}

		// initPropsUseRecord();
		flushSealState();
		this.handlerPetProp2Player();
		setEvil(getEvil());
		家族宣战状态();

		// 国战星级
		guozhanLevel = (int) Math.pow(2 * gongxun / 100, new Double(1) / new Double(3));
		if (guozhanLevel > 192) {
			guozhanLevel = 192;
		}
		if (guozhanLevel > 20) { // 客户端显示bug 目前设置20为上限 2014年7月21日17:17:03
			guozhanLevel = 20;
		}
		setGuozhanLevel(guozhanLevel);

		if (getMainSoul() != null) {
			if (mainCareer != getMainSoul().getCareer()) {
				setMainCareer(getMainSoul().getCareer());
			}
		}

		if (authorityMap == null) {
			authorityMap = new HashMap<Integer, Authority>();
		}
		if (authorityMap.size() > 0) {
			Authority as[] = authorityMap.values().toArray(new Authority[0]);
			for (Authority a : as) {
				if (a != null) {
					a.owner = this;
					AuthorityConfig config = AuthorityAgent.getInstance().getAuthorityConfig(a.type);
					if (config != null) {
						a.config = config;
					} else {
						GamePlayerManager.logger.warn("[初始化玩家数据时遇到非法数据] [权利类型不存在] [" + this.getLogString() + "] [权利:" + a.type + "]");
					}
				}
			}
		}
		// 修正策划改地图问题
//		if ("bianjing".equals(this.getMapName())) {
//			this.setGame("kunlunshengdian");
//			this.setLastGame("kunlunshengdian");
//			this.setRealMapName("kunlunshengdian");
//			this.setMapName("kunlunshengdian");
//		}
		try {
			Career career = CareerManager.getInstance().getCareer(this.career);
			Skill[] skills = career.getXinfaSkills();
			if (skills != null) {
				for (int i = 0; i < 17; i++) {
					Skill skill = skills[i];
					if (skill != null) {
						int level = skill.getNeedPlayerLevel()[0];
						if (this.getSoulLevel() >= level) {
							int skillLevel = getSkillLevel(skill.getId());
							if (skillLevel <= 0) {
								String result = career.isUpgradable(this, skill.getId(), true);
								if (result == null) {
									this.tryToLearnSkill(skill.getId(), false, true);
								} else {
									GameManager.logger.error("[自动学习技能失败] [{}] [{}] [{}] [{}]", new Object[] { this.getUsername(), this.getId(), this.getName(), result });
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			GameManager.logger.error("init新心法技能出错", e);
		}

		if (bianShenLevels == null) {
			bianShenLevels = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		}
		lastHeatSilver = getSilver();
		lastHeatRmb = getRMB();
		try {
			if (getGameMsgs() == null) {
				// if (getPassport() == null) {
				// Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(getUsername());
				// setPassport(passport);
				// }
				// 如果最后一次登录时间是已经发布的时间之后的认为是有问题的
				// if (getPassport().getLastLoginDate().getTime() > lastLoginTime) {
				// GamePlayerManager.sendErrorMail(this, "登录在已经发布时间后");
				// }else {
				setGameMsgs(new String[gggNum]);
				GamePlayerManager.createPlayerMsg(this, 0, false);
				GamePlayerManager.createPlayerMsg(this, 1, false);
				GamePlayerManager.createPlayerMsg(this, 2, false);
				GamePlayerManager.createPlayerMsg(this, 3, false);
				PlayerManager.getInstance().savePlayer(this);
				// }
			} else {
				GamePlayerManager.checkPlayerMsgs(this);
			}
		} catch (Exception e) {
			GamePlayerManager.logger.error("init出错:", e);
		}

		{
			// 加载角色的活跃度
			ActivenessManager am = ActivenessManager.getInstance();
			activenessInfo = am.getPlayerActivenessInfoFromDB(this);
			if (activenessInfo == null) {
				ActivitySubSystem.logger.warn(this.getLogString() + "[加载角色的活跃度activenessInfo为空]");

			}
		}
		try {
			huntLifr = HuntLifeEntityManager.instance.getHuntLifeEntity(this);
			ArticleEntity ae = this.getEquipmentColumns().get(11);
			if (ae != null && ae instanceof NewMagicWeaponEntity && ((NewMagicWeaponEntity) ae).getMdurability() > 0) {
				HuntLifeEntityManager.instance.loadAllAttr(this);
			}
		} catch (Exception e) {
			loadExcep[0] = true;
			HuntLifeManager.logger.warn("[加载猎命属性] [异常] [" + this.getLogString() + "]");
		}
		this.chuangonging = false;
		initializing = false;
		try {
			JiazuEntityManager2.instance.addPracticeAttr(this);
		} catch (Exception e) {
			loadExcep[1] = true;
			JiazuManager2.logger.error("[新家族] [增加家族技能属性] [异常] [" + this.getLogString() + "]", e);
		}

		this.initKnapName();
		totalRmbyuanbao = getRMB();
		if (Game.logger.isWarnEnabled()) Game.logger.warn("[" + this.getUsername() + "] [" + this.getId() + "] [" + this.getName() + "] [weekPoint:" + weekPoint + "] ["+totalRmbyuanbao+"] ["+reason+"] [初始化]");
	}

	public void initClientInfo() {
		if (conn != null) {
			Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
			if (USER_CLIENT_INFO_map != null) {
				USER_CLIENT_INFO_map.clear();
			} else {
				USER_CLIENT_INFO_map = new HashMap<String, String>();
			}
			if (o instanceof USER_CLIENT_INFO_REQ) {
				USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ) o;
				Field[] fields = USER_CLIENT_INFO_REQ.class.getDeclaredFields();
				for (Field f : fields) {
					try {
						f.setAccessible(true);
						Object object = f.get(req);
						USER_CLIENT_INFO_map.put(f.getName(), String.valueOf(object));

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			USER_CLIENT_INFO_map = null;
		}
	}

	public String getClientInfo(String key) {
		if (USER_CLIENT_INFO_map == null) {
			initClientInfo();
		}
		if (USER_CLIENT_INFO_map == null) {
			return null;
		}
		return USER_CLIENT_INFO_map.get(key);
	}

	public Map<String, String> getUSER_CLIENT_INFO_map() {
		return USER_CLIENT_INFO_map;
	}

	public transient boolean[] loadExcep = new boolean[] { false, false, false };

	/**
	 * 更新每天凌晨需要变更的数据,策略为,当时在线的玩家即时更改,在玩家每次进入游戏也会检测
	 */
	public void modifyDailyChangevalue() {
		modifyAddVitalityEveryday();
		modifyBournTaskAndTime();
	}

	@Override
	public int getGangContribution() {
		if (getJiazuId() <= 0) {
			return 0;
		}
		JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(getId(), this.getJiazuId());
		if (jiazuMember == null) {
			return 0;
		}
		return jiazuMember.getCurrentWeekContribution();
	}

	/**
	 * 更新最后上线时间,与这个属性相关的操作都在这
	 */
	public void updateLastOnlineTimeForCave(long nowTime) {
		try {
			{
				// 仙府
				Cave cave = FaeryManager.getInstance().getCave(this);
				if (cave != null) {
					if (cave.getStatus() == FaeryConfig.CAVE_STATUS_OPEN || cave.getStatus() == FaeryConfig.CAVE_STATUS_LOCKED) {
						cave.setOwnerLastVisitTime(nowTime);
					} else {
						if (FaeryManager.logger.isWarnEnabled()) {
							FaeryManager.logger.warn(this.getLogString() + "[更新最后上线时间,仙府已经封印了] [{}]", new Object[] { cave.getOwnerLastVisitTime() });
						}
					}
				}
			}
		} catch (Exception e) {
			CoreSubSystem.logger.error(this.getLogString() + "[leaveserver] [记录时间] [异常] ", e);
		}
	}

	public void updateLastOnlineTimeForJiazu(long nowTime) {
		try {
			{
				// 家族成员
				if (getJiazuId() > 0) {
					JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(this.getId(), this.getJiazuId());
					if (jiazuMember != null) {
						jiazuMember.setLastOffLineTime(nowTime);
					}
				}
			}
		} catch (Exception e) {
			CoreSubSystem.logger.error(this.getLogString() + "[leaveserver] [记录时间] [异常] ", e);
		}
	}

	/**
	 * 得到挂机宠物所在仙府的门NPCid列表
	 * 
	 * @return
	 */
	public List<Long> getHookPetCaveId() {
		List<Long> caveWherePetin = new ArrayList<Long>();
		com.fy.engineserver.datasource.article.data.props.Cell[] cells = this.getPetKnapsack().getCells();
		for (int i = 0; i < cells.length; i++) {
			long articleId = cells[i].getEntityId();// 宠物道具ID
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(articleId);
			if (ae instanceof PetPropsEntity) {
				long petId = ((PetPropsEntity) ae).getPetId();// 宠物ID
				Pet p = PetManager.getInstance().getPet(petId);
				if (p != null) {
					if (p.getHookInfo() != null) {
						caveWherePetin.add(p.getHookInfo().getCaveId());
					}
				}
			}
		}
		return caveWherePetin;
	}

	private synchronized void modifyAddVitalityEveryday() {
		if (needAddVitality) {
			long now = SystemTime.currentTimeMillis();
			long timeDiff = TimeTool.instance.cycleDistance(lastAddVitalityTime, now, TimeDistance.DAY);// 距离上一次增加体力值的天数
			if (timeDiff == 0) {// 如果上一次增加和现在是同一天,则不增加
				return;
			}
			setLastAddVitalityTime(now);
			// 普通与VIP不同,先做普通的,vip暂时无设计
			int oldVitality = getVitality();

			this.addVitality((int) ((PlayerManager.VITALITY_EVERYDAY_ADD + TengXunDataManager.instance.getAddVitality(this)) * timeDiff));
			GamePlayerManager.logger.info(getLogString() + getOtherSoulLogString() + "[增加体力值] [原有:{}] [增加后:{}] [timeDiff:{}]", new Object[] { oldVitality, getVitality(), timeDiff });

			// if (oldVitality >= PlayerManager.VITALITY_MAX) {
			// GamePlayerManager.logger.info(getLogString() +
			// "[增加体力值] [已经达到上限] [原有:{}] [增加后:{}]", new Object[] { oldVitality,
			// getVitality() });
			// return;
			// }
			// if (oldVitality + PlayerManager.VITALITY_EVERYDAY_ADD * timeDiff
			// >= PlayerManager.VITALITY_MAX) {
			// setVitality(PlayerManager.VITALITY_MAX);
			// if (GamePlayerManager.logger.isInfoEnabled()) {
			// GamePlayerManager.logger.info(getLogString() +
			// "[增加体力值] [增加后达到上限] [原有:{}] [增加后:{}]", new Object[] { oldVitality,
			// getVitality() });
			// }
			// return;
			// }
			// setVitality((int) (oldVitality +
			// PlayerManager.VITALITY_EVERYDAY_ADD * timeDiff));
			// GamePlayerManager.logger.info(getLogString() +
			// "[增加体力值] [原有:{}] [增加后:{}]", new Object[] { oldVitality,
			// getVitality() });
		}
	}

	public void 家族宣战状态() {
		JiazuFighterManager jfm = JiazuFighterManager.getInstance();
		if (jfm != null) {
			jfm.设置玩家宣战属性(this);
		} else {
			this.setJiazuXuanZhanData(new long[0]);
		}
	}

	public void initZongPaiName() {
		if (JiazuManager.getInstance() != null) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(getJiazuId());
			if (jiazu != null) {
				if (jiazu.getZongPaiId() > 0) {
					ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(jiazu.getZongPaiId());
					if (zp != null) setZongPaiName(zp.getZpname());
					return;
				}
			}
		}
		setZongPaiName("");

	}

	public Soul getMainSoul() {
		if (getCurrSoul().getSoulType() == Soul.SOUL_TYPE_BASE) {
			return getCurrSoul();
		}
		if (getUnusedSoul() == null) {
			return null;
		}
		for (Soul soul : getUnusedSoul()) {
			if (soul.getSoulType() == Soul.SOUL_TYPE_BASE) {
				return soul;
			}
		}
		return null;
	}

	// 得到本尊的职业
	public byte getMainCareer() {
		return mainCareer;
	}

	public void setMainCareer(byte mainCareer) {
		this.mainCareer = mainCareer;
		setDirty(true, "mainCareer");
	}

	public void 重新计算所有的被动技能() {
		CareerManager cm = CareerManager.getInstance();
		Career career = cm.getCareer(this.getCareer());
		if (career != null) {
			if (career.basicSkills != null) {
				for (int i = 0; i < career.basicSkills.length; i++) {
					Skill skill = career.basicSkills[i];
					if (skill != null && (skill instanceof PassiveSkill)) {
						if (careerBasicSkillsLevels[i] > 0) {
							PassiveSkill ps = (PassiveSkill) skill;
							ps.run(this, careerBasicSkillsLevels[i]);
						}
					}
				}
			}
			if (career.threads != null) {
				for (int ii = 0; ii < career.threads.length; ii++) {
					CareerThread ct = career.threads[ii];
					if (ct != null) {
						Skill[] skills = ct.skills;
						for (int i = 0; i < skills.length; i++) {
							Skill skill = skills[i];
							if (skill != null && (skill instanceof PassiveSkill)) {
								if (skillOneLevels[i] > 0) {
									PassiveSkill ps = (PassiveSkill) skill;
									ps.run(this, skillOneLevels[i]);
								}
							}
						}
					}
				}

			}
			if (career.nuqiSkills != null) {
				for (int i = 0; i < career.nuqiSkills.length; i++) {
					Skill skill = career.nuqiSkills[i];
					if (skill != null && (skill instanceof PassiveSkill)) {
						if (nuqiSkillsLevels != null && nuqiSkillsLevels[i] > 0) {
							PassiveSkill ps = (PassiveSkill) skill;
							ps.run(this, nuqiSkillsLevels[i]);
						}
					}
				}
			}
			if (career.xinfaSkills != null) {
				for (int i = 0; i < career.xinfaSkills.length; i++) {
					Skill skill = career.xinfaSkills[i];
					if (skill != null && (skill instanceof PassiveSkill)) {
						if (xinfaLevels != null && xinfaLevels[i] > 0) {
							PassiveSkill ps = (PassiveSkill) skill;
							ps.run(this, xinfaLevels[i]);
						}
					}
				}
			}
			if (career.bianShenSkills != null) {
				for (int i = 0; i < career.bianShenSkills.length; i++) {
					Skill skill = career.bianShenSkills[i];
					if (skill != null && (skill instanceof PassiveSkill)) {
						if (bianShenLevels != null && bianShenLevels[i] > 0) {
							PassiveSkill ps = (PassiveSkill) skill;
							ps.run(this, bianShenLevels[i]);
						}
					}
				}
			}
		}
	}

	public void initHorse() {
		try {
			// 初始化马属性
			if (isIsUpOrDown()) {
				if (this.horseIdList.contains(this.ridingHorseId)) {
					setIsUpOrDown(false);
					this.upToHorse(ridingHorseId);
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[玩家初始化上坐骑] [" + this.getLogString() + "] [坐骑id:" + ridingHorseId + "]");
					}
				} else {
					setIsUpOrDown(false);
					if (flying) setFlying(false);
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[获取坐骑异常] [" + this.getLogString() + "] [" + ridingHorseId + "]");
					}
				}
			}
		} catch (Exception e) {
			HorseManager.logger.error("[初始化角色属性] [获取坐骑异常] [" + this.getLogString() + "]", e);
		}
	}

	public void initAvta() {
		Avata av = ResourceManager.getInstance().getAvata(this);
		this.setAvata(av.avata);
		this.setAvataType(av.avataType);
		this.modifyShouAvata();
		setChatChannelStatus(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
	}

	/**
	 * 初始化时装
	 */
	public void initAvataProps() {
		if (this.getAvataPropsId() > 0) {
			try {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity aee = aem.getEntity(this.getAvataPropsId());
				if (aee != null) {
					ArticleManager am = ArticleManager.getInstance();
					Article a = am.getArticle(aee.getArticleName());
					if (a instanceof AvataProps) {
						((AvataProps) a).puton(this);
					} else {
						if (Game.logger.isWarnEnabled()) Game.logger.warn("[初始化人物] [时装道具不存在] [时装道具名:{}] [时装id:{}] [{}] [{}] [{}]", new Object[] { aee.getArticleName(), getAvataPropsId(), getUsername(), getId(), getName() });
						if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[初始化人物] [时装道具不存在] [时装道具名:{}] [时装id:{}] [{}] [{}] [{}]", new Object[] { aee.getArticleName(), getAvataPropsId(), getUsername(), getId(), getName() });
					}
				} else {
					if (Game.logger.isWarnEnabled()) Game.logger.warn("[初始化人物] [时装实体不存在] [时装id:{}] [{}] [{}] [{}]", new Object[] { getAvataPropsId(), getUsername(), getId(), getName() });
					if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[初始化人物] [时装实体不存在] [时装id:{}] [{}] [{}] [{}]", new Object[] { getAvataPropsId(), getUsername(), getId(), getName() });
				}
			} catch (Exception ex) {
				if (Game.logger.isWarnEnabled()) Game.logger.warn("[初始化人物时装出现异常]", ex);
			}

		}
	}

	/**
	 * 是否在本家族的驻地内
	 * 
	 * @return
	 */
	public boolean inSelfSeptStation() {
		if (getJiazuId() == 0) return false;
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(getJiazuId());
		if (jiazu == null) return false;
		if (jiazu.getBaseID() == 0) return false;
		SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (septStation == null) return false;
		return septStation.getGame().equals(getCurrentGame());
	}

	public void initJiazuTitleAndIcon() {
		if (getJiazuId() > 0) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(getJiazuId());
			if (jiazu != null) {
				JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(getId(), getJiazuId());
				if (jiazuMember != null) {
					setJiazuName(jiazu.getName());
					if (jiazuMember.getTitle() != null) {
						setJiazuTitle(jiazu.getTitleDes(jiazuMember.getTitle()));
					} else {
						setJiazuTitle("");
					}
					if (jiazu.getUsedBedge() > 0) {
						JiazuBedge bedge = SeptStationMapTemplet.getInstance().getBedge(jiazu.getUsedBedge());
						if (bedge != null) {
							setJiazuIcon(bedge.getResName());
						} else {
							setJiazuIcon("");
						}
					} else {
						setJiazuIcon("");
					}
				} else {
					setJiazuIcon("");
					setJiazuTitle("");
				}
			} else {
				setJiazuIcon("");
				setJiazuTitle("");
			}
		} else {
			setJiazuIcon("");
			setJiazuTitle("");
		}
	}

	/**
	 * 加载所有元神，包括所有装备以及加载激活元神的被动技能
	 */
	private void initSoul() {
		Soul currSoul = getCurrSoul();
		if (currSoul == null) {
			throw new IllegalStateException();
		}
		// 设置玩家身上的装备 技能 等
		currSoul.getEc().setOwner(this);
		setEquipmentColumns(currSoul.getEc());// TODO 和前面有重复
		int preCa = career;
		setCareer(currSoul.getCareer());

		// 计算元神属性值，会进行元神初始化，并且穿戴装备
		currSoul.account(this);

		if (getUnusedSoul() != null && getUnusedSoul().size() > 0) {
			Soul unuse = getUnusedSoul().get(0);
			unuse.getEc().setOwner(this);
			unuse.account(this);
		}

		updateSoulHorsePropertyToPlayer();

		if (preCa == currSoul.getCareer()) {
			try {
				SkEnhanceManager.getInst().syncSkillDesc(this);
			} catch (Exception e) {
				Skill.logger.error("刷新技能信息出错", e);
			}
		}
	}

	// 把元神上的坐骑属性赋值给player
	public synchronized void updateSoulHorsePropertyToPlayer() {

		this.rideHorseId = currSoul.getDefaultHorseId();
		this.ridingHorseId = currSoul.getRidingHorseId();
		this.isUpOrDown = currSoul.isUpOrDown();

		if (currSoul.getHorseArr() == null) {
			this.horseIdList = new ArrayList<Long>();
		} else {
			this.horseIdList = currSoul.getHorseArr();
		}

		this.feedNum = currSoul.getFeedNum();
		this.lastFeedTime = currSoul.getLastFeedTime();

		QUERY_PLAYER_HORSE_RES res = new QUERY_PLAYER_HORSE_RES(GameMessageFactory.nextSequnceNum(), this.getRidingHorseId(), this.getRideHorseId());
		this.addMessageToRightBag(res);
		HorseManager.logger.warn("[把元神上的坐骑属性赋值给player] [" + this.getLogString() + "]");

	}

	/**
	 * 切换元神<BR/>
	 * 1.要使用的是当前元神<BR/>
	 * 2.CD中<BR/>
	 * 3.元神不存在<BR/>
	 * 4.骑乘状态不能切换元神<BR/>
	 * 6.渡劫不能切换元神 7.元神
	 * 
	 * @param soul
	 * @return
	 */
	public synchronized CompoundReturn switchSoul(int soulType) {
		try {
			long startTime = SystemTime.currentTimeMillis();
			Soul soul = getSoul(soulType);
			long now = startTime;
			if (soul == null) {
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [不存在的元神] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6); // 这个标志位啥意思？ 怎么用的
			}
			if (this.isDeath()) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(8);
			}
			if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(id)) {
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [正处于渡劫状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
			}
			if (this.flyState == 1) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(11);
			}
			FairyRobberyManager fins = FairyRobberyManager.inst;
			if (fins != null && fins.isPlayerInRobbery(this)) {
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [正处于仙界渡劫状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
			}
			if (soul.getSoulType() == getCurrSoul().getSoulType()) {
				// 是当前的元神
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [已经是当前元神] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
			}

			if (now - getLastSwitchSoulTime() < Soul.switchCd) {
				// coldDown未结束
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [CD中] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
			}
			if (isIsUpOrDown()) {// 骑乘状态
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [在骑乘状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
			}
			DownCityManager dcm = DownCityManager.getInstance();
			if (dcm != null && dcm.isDownCityByName(this.game)) {// 副本状态
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [在副本状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
			}

			try {
				if (PetDaoManager.getInstance().isPetDao(this.getCurrentGame().gi.name)) {// 副本状态
					if (CoreSubSystem.logger.isWarnEnabled()) {
						CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [在迷城状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
					}
					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (SealManager.getInstance().isSealDownCity(this.getCurrentGame().gi.name)) {
					if (CoreSubSystem.logger.isWarnEnabled()) {
						CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [封印挑战状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
					}
					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(10);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (FairyChallengeManager.getInst().isPlayerChallenging(this)) {
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [正在挑战仙尊] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(9);
			}
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[切换元神前]" + this.getPlayerPropsString() + this.getPlayerEquipment());
			}
			synchronized (buffs) {
				for (int i = buffs.size() - 1; i >= 0; i--) {
					Buff buff = buffs.get(i);
					if (buff.getTemplate().clearSkillPointNotdisappear) {
						ActiveSkill.logger.warn("切换元神保留[buff:{}][{}][level:{}][playerId:{}][{}]", new Object[] { buff.getTemplateName(), buff.getClass(), buff.getLevel(), this.id, this.name });
						continue;
					}
					if (buff != null && !buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) { // 切换元神不能把渡劫虚弱buff踢掉。
						buff.end(this);
						if (buff.isForover() || buff.isSyncWithClient()) {
							this.removedBuffs.add(buff);
						}
						buffs.remove(i);
						setDirty(true, "buffs");
						ActiveSkill.logger.warn("[切换元神去除BUFF] [{}] [soulType:" + soulType + "] [{}:{}] [time:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime() });
					}
				}
			}

			清空所有的BCX值(true);
			if (getActiveMagicWeaponId() > 0) {
				try {
					NPC mnpc = MemoryNPCManager.getNPCManager().getNPC(this.getActiveMagicWeaponId());
					if (mnpc != null) {
						this.getCurrentGame().removeSprite(mnpc);
					}
				} catch (Exception e) {
					MagicWeaponManager.logger.error("[从场景中移除法宝npc出错] [ " + this.getLogString() + "]");
				}
				this.setActiveMagicWeaponId(-1);
			}
			this.setLastSwitchSoulTime(now);
			Soul oldSoul = getCurrSoul();
			HorseManager.getInstance().switchSoulAddHorse(this, oldSoul, soul);
			getUnusedSoul().remove(soul);
			getUnusedSoul().add(oldSoul);

			setCurrSoul(soul);
			setSoulLevel(getCurrSoul().getGrade());

			copyAttributeFromSoul(soul);

			initSoul();

			this.setCommonAttackSpeed((short) 2000);
			this.aura = -1;

			initHorse();
			initAvta();
			initAvataProps();

			initNextLevelExpPool();

			setFull();
			setDirty(true, "currSoul");
			setDirty(true, "unusedSoul");
			flushSealState();
			this.setNextLevelExp(ExperienceManager.maxExpOfLevel[getCurrSoul().getGrade()]);

//			SealManager.getInstance().noticeSealTaskStat(this);
			SkEnhanceManager.getInst().clearSkillNums(this);

			{
				// 坐骑容错,如果元神没有坐骑,则送一个
				if (getCurrSoul().getSoulType() == Soul.SOUL_TYPE_SOUL) {
					boolean hasBaseHorse = false;

					// 奖励坐骑
					String articleName = null;
					switch (career) {
					case 1:
						articleName = 躁狂野牛;
						break;
					case 2:
						articleName = 离火幼狼;
						break;
					case 3:
						articleName = 日曜幼虎;
						break;
					case 4:
						articleName = 幽毒幼蝎;
						break;
					case 5:
						articleName = Translate.独角灵兽;
						break;
					default:
						break;
					}
					for (long horseId : getCurrSoul().getHorseArr()) {
						Horse horse = HorseManager.getInstance().getHorseByHorseId(this, horseId, getCurrSoul());
						if (horse != null) {
							if (horse.getHorseName().equals(articleName)) {
								hasBaseHorse = true;
								break;
							}
						}
					}
					if (!hasBaseHorse) {
						if (articleName != null) {
							Article article = ArticleManager.getInstance().getArticle(articleName);
							if (article != null) {
								try {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_LEVELUP, this, article.getColorType(), 1, true);
									MailManager.getInstance().sendMail(this.getId(), new ArticleEntity[] { ae }, Translate.奖励坐骑2, Translate.translateString(Translate.恭喜你激活元神获得坐骑, new String[][] { { Translate.STRING_1, articleName } }), 0, 0, 0, "激活元神");
									this.sendError(Translate.translateString(Translate.恭喜你激活元神获得坐骑速去查看邮件, new String[][] { { Translate.STRING_1, articleName } }));
									CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [元神类型:" + getCurrSoul().getSoulType() + "] [元神等级:" + getCurrSoul().getGrade() + "] [奖励坐骑:" + articleName + "] [成功]");
									if (ArticleManager.logger.isWarnEnabled()) {
										ArticleManager.logger.warn(this.getPlayerPropsString() + this.getPlayerEquipment() + "[激活元神后]");
									}
								} catch (Exception e) {
									CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [元神类型:" + getCurrSoul().getSoulType() + "] [元神等级:" + getCurrSoul().getGrade() + "] [奖励坐骑:" + articleName + "] [异常]", e);
								}
							} else {
								CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [元神类型:" + getCurrSoul().getSoulType() + "] [元神等级:" + getCurrSoul().getGrade() + "] [坐骑不存在:" + articleName + "]");
							}
						} else {
							CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [元神类型:" + getCurrSoul().getSoulType() + "] [元神等级:" + getCurrSoul().getGrade() + "] [坐骑未配置]");
						}

					}
				}
			}
			try {
				noticeShouKuiInfo("切换元神");
				resetShouStat("切换元神");
				int bianshenid = 0;
				Career ca = CareerManager.getInstance().getCareer(career);
				if (ca != null) {

					CareerThread threads[] = ca.getCareerThreads();
					Skill[] skills1 = ca.getBasicSkills();
					Skill[] skills2 = threads[0].getSkills();
					SkillInfo[] basicSkills = new SkillInfo[skills1.length];
					for (int i = 0; i < basicSkills.length; i++) {
						basicSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills1.length; i++) {
						if (skills1[i] != null) {
							basicSkills[i].setInfo(this, skills1[i]);
						}
					}
					int professorNum = 14;
					if (professorNum > skills2.length) {
						professorNum = skills2.length;
					}
					SkillInfo[] professorSkills = new SkillInfo[professorNum];
					for (int i = 0; i < professorNum; i++) {
						professorSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < professorNum; i++) {
						if (skills2[i] != null) {
							professorSkills[i].setInfo(this, skills2[i]);
						}
					}

					Skill[] skills3 = ca.getNuqiSkills();
					SkillInfo[] nuqiSkills = new SkillInfo[skills3.length];
					for (int i = 0; i < nuqiSkills.length; i++) {
						nuqiSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills3.length; i++) {
						if (skills3[i] != null) {
							nuqiSkills[i].setInfo(this, skills3[i]);
						}
					}

					Skill[] skills5 = ca.getKingSkills();
					SkillInfo[] kingSkills = new SkillInfo[skills5.length];
					for (int i = 0; i < kingSkills.length; i++) {
						kingSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills5.length; i++) {
						if (skills5[i] != null) {
							kingSkills[i].setInfo(this, skills5[i]);
						}
					}

					NEW_QUERY_CAREER_INFO_RES res = new NEW_QUERY_CAREER_INFO_RES(GameMessageFactory.nextSequnceNum(), ca, basicSkills, professorSkills, nuqiSkills, new SkillInfo[0], kingSkills);
					this.addMessageToRightBag(res);

					int oneReqNum = 10; // 一条协议发送10个心法技能
					Skill[] skills4 = ca.getXinfaSkills();
					for (int a = 0; a < skills4.length / oneReqNum + 1; a++) {
						int nn = oneReqNum;
						if (oneReqNum * (a + 1) > skills4.length) {
							nn = skills4.length - oneReqNum * a;
						}
						if (nn <= 0) {
							break;
						}
						SkillInfo[] newXinfaSkills = new SkillInfo[nn];
						for (int i = 0; i < nn; i++) {
							newXinfaSkills[i] = new SkillInfo();
						}
						for (int i = 0; i < nn; i++) {
							if (skills4[oneReqNum * a + i] != null) {
								newXinfaSkills[i].setInfo(this, skills4[oneReqNum * a + i]);
							}
						}
						QUERY_CAREER_XINFA_INFO_RES res1 = new QUERY_CAREER_XINFA_INFO_RES(GameMessageFactory.nextSequnceNum(), newXinfaSkills);
						this.addMessageToRightBag(res1);
						CoreSubSystem.logger.warn("[发送心法] [协议长度:" + res1.getLength() + "] [数目:" + nn + "] [" + a + "]");
					}

					SkillInfo[] newProfessorSkills = new SkillInfo[skills2.length - professorNum];
					for (int i = 0; i < skills2.length - professorNum; i++) {
						newProfessorSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills2.length - professorNum; i++) {
						if (skills2[professorNum + i] != null) {
							newProfessorSkills[i].setInfo(this, skills2[professorNum + i]);
						}
					}
					QUERY_CAREER_JINJIE_INFO_RES res2 = new QUERY_CAREER_JINJIE_INFO_RES(GameMessageFactory.nextSequnceNum(), newProfessorSkills);
					this.addMessageToRightBag(res2);
					Skill[] skills6 = ca.getBianShenSkills();
					SkillInfo[] bianShenSkills = new SkillInfo[skills6.length];
					for (int i = 0; i < bianShenSkills.length; i++) {
						bianShenSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills6.length; i++) {
						if (skills6[i] != null) {
							if (skills6[i] instanceof ActiveSkill) {
								ActiveSkill sk = (ActiveSkill) skills6[i];
								if (sk.isBianshenBtn()) {
									bianshenid = sk.getId();
								}
							}
							bianShenSkills[i].setInfo(this, skills6[i]);
						}
					}
					QUERY_CAREER_BIANSHEN_INFO_RES res3 = new QUERY_CAREER_BIANSHEN_INFO_RES(GameMessageFactory.nextSequnceNum(), bianshenid, bianShenSkills);
					this.addMessageToRightBag(res3);
				}

			} catch (Exception e) {
				e.printStackTrace();
				CoreSubSystem.logger.error(this.getLogString() + "[激活元神] [通知元神初始化变身技能出错]");
			}
			try {
				JiazuEntityManager2.instance.addPracticeAttr(this);
				SoulPithEntityManager.getInst().initSoulPithAttr(this);
			} catch (Exception e) {
				JiazuManager2.logger.error("[新家族] [增加家族技能属性] [异常] [" + this.getLogString() + "]", e);
			}
				// 给网关服务器发送消息，更新用户信息
				NEW_MIESHI_UPDATE_PLAYER_INFO update_player_info = new NEW_MIESHI_UPDATE_PLAYER_INFO(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), username, getId(), name, this.getSoulLevel(), this.getCurrSoul().getCareer(), (int) getRMB(), getVipLevel(), 2);
				MieshiGatewayClientService.getInstance().sendMessageToGateway(update_player_info);

			if (CoreSubSystem.logger.isWarnEnabled()) {
				CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [成功] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
			}
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn(this.getPlayerPropsString() + this.getPlayerEquipment() + "[切换元神后]");
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error("切换元神异常", e);
		}

		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 强制切换元神-=--无视cd等
	 * 
	 * @param soulType
	 * @param force
	 *            true为强制
	 * @return
	 */
	public synchronized CompoundReturn switchSoul(int soulType, boolean force) {
		try {
			long startTime = SystemTime.currentTimeMillis();
			Soul soul = getSoul(soulType);
			long now = startTime;
			if (soul == null) {
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [不存在的元神] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6); // 这个标志位啥意思？ 怎么用的
			}
			if (!force && TransitRobberyEntityManager.getInstance().isPlayerInRobbery(id)) {
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [正处于渡劫状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
			}
			if (soul.getSoulType() == getCurrSoul().getSoulType()) {
				// 是当前的元神
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [已经是当前元神] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
			}

			if (!force && (now - getLastSwitchSoulTime() < Soul.switchCd)) {
				// coldDown未结束
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [CD中] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
			}
			if (isIsUpOrDown()) {// 骑乘状态
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [在骑乘状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
			}
			DownCityManager dcm = DownCityManager.getInstance();
			if (dcm != null && dcm.isDownCityByName(this.game)) {// 副本状态
				if (CoreSubSystem.logger.isWarnEnabled()) {
					CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [失败] [在副本状态] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
			}
			synchronized (buffs) {
				for (int i = buffs.size() - 1; i >= 0; i--) {
					Buff buff = buffs.get(i);
					if (buff.getTemplate().clearSkillPointNotdisappear) {
						ActiveSkill.logger.warn("切换元神保留[buff:{}][{}][level:{}][playerId:{}][{}]", new Object[] { buff.getTemplateName(), buff.getClass(), buff.getLevel(), this.id, this.name });
						continue;
					}
					if (buff != null && !buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
						buff.end(this);
						if (buff.isForover() || buff.isSyncWithClient()) {
							this.removedBuffs.add(buff);
						}
						buffs.remove(i);
						setDirty(true, "buffs");
						ActiveSkill.logger.warn("[切换元神去除BUFF] [{}] [soulType:" + soulType + "] [{}:{}] [time:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime() });
					}
				}
			}

			清空所有的BCX值(true);
			if (!force) { // 强制切换不计入cd
				this.setLastSwitchSoulTime(now);
			}
			Soul oldSoul = getCurrSoul();
			HorseManager.getInstance().switchSoulAddHorse(this, oldSoul, soul);
			getUnusedSoul().remove(soul);
			getUnusedSoul().add(oldSoul);

			setCurrSoul(soul);
			setSoulLevel(getCurrSoul().getGrade());

			copyAttributeFromSoul(soul);

			initSoul();

			this.setCommonAttackSpeed((short) 2000);
			this.aura = -1;

			initHorse();
			initAvta();
			initAvataProps();

			initNextLevelExpPool();

			setFull();
			setDirty(true, "currSoul");
			setDirty(true, "unusedSoul");
			flushSealState();
			this.setNextLevelExp(ExperienceManager.maxExpOfLevel[getCurrSoul().getGrade()]);
			try {
				JiazuEntityManager2.instance.addPracticeAttr(this);
			} catch (Exception e) {
				JiazuManager2.logger.error("[新家族] [增加家族技能属性] [异常] [" + this.getLogString() + "]", e);
			}

				// 给网关服务器发送消息，更新用户信息
				NEW_MIESHI_UPDATE_PLAYER_INFO update_player_info = new NEW_MIESHI_UPDATE_PLAYER_INFO(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), username, getId(), name, this.getSoulLevel(), this.getCurrSoul().getCareer(), (int) getRMB(), getVipLevel(), 2);
				MieshiGatewayClientService.getInstance().sendMessageToGateway(update_player_info);

			if (CoreSubSystem.logger.isWarnEnabled()) {
				CoreSubSystem.logger.warn(this.getLogString() + "[切换元神] [切换成:" + soulType + "] [成功] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error("切换元神异常", e);
		}
		PLAYER_SWITCH_SOUL_RES res = new PLAYER_SWITCH_SOUL_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, "渡劫强切", 0);
		this.addMessageToRightBag(res);
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 设置满血满蓝
	 */
	private void setFull() {
		setHp(getMaxHP());
		setMp(getMaxMP());
		getCurrSoul().setHp(getCurrSoul().getMaxHp());
		getCurrSoul().setMp(getCurrSoul().getMp());
	}

	public void copyAttributeFromSoul(Soul soul) {
		setEquipmentColumns(soul.getEc());
	}

	/**
	 * 是否重新种植不消失的buff
	 * @param flag
	 */
	private void 清空所有的BCX值(boolean flag) {
		if (flag) {
			if (buffs != null && buffs.size() > 0) {
				synchronized (buffs) { // 清空BCX值会导致需要保留的buff（切换元神等）减少或者增加的属性清除掉，再此调用end，在方法最后调用start保证buff的有效性
					for (int i = buffs.size() - 1; i >= 0; i--) {
						Buff buff = buffs.get(i);
						if (buff == null || buff.getTemplate() == null || buff.getTemplateName() == null) {
							continue;
						}
						if (buff.getTemplate().clearSkillPointNotdisappear || buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
							buff.end(this);
							ActiveSkill.logger.warn("在清空所有BCX值前调用保留buff的end方法[buff:{}][{}][level:{}][playerId:{}][{}]", new Object[] { buff.getTemplateName(), buff.getClass(), buff.getLevel(), this.id, this.name });
						}
					}
				}
			}
			清空所有的BCX值();
			if (buffs != null && buffs.size() > 0) {
				synchronized (buffs) {
					for (int i = buffs.size() - 1; i >= 0; i--) {
						Buff buff = buffs.get(i);
						if (buff == null || buff.getTemplate() == null || buff.getTemplateName() == null) {
							continue;
						}
						if (buff.getTemplate().clearSkillPointNotdisappear || buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
							buff.start(this);
							ActiveSkill.logger.warn("在清空所有BCX值后调用保留buff的start方法[buff:{}][{}][level:{}][playerId:{}][{}]", new Object[] { buff.getTemplateName(), buff.getClass(), buff.getLevel(), this.id, this.name });
						}
					}
				}
			}
		}
	}

	private void 清空所有的BCX值() {
		{
			// if(buffs != null && buffs.size() > 0){
			// synchronized (buffs) { //清空BCX值会导致需要保留的buff（切换元神等）减少或者增加的属性清除掉，再此调用end，在方法最后调用start保证buff的有效性
			// for (int i = buffs.size() - 1; i >= 0; i--) {
			// Buff buff = buffs.get(i);
			// if(buff == null || buff.getTemplate() == null || buff.getTemplateName() == null) {
			// continue;
			// }
			// if (buff.getTemplate().clearSkillPointNotdisappear || buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
			// buff.end(this);
			// ActiveSkill.logger.warn("在清空所有BCX值前调用保留buff的end方法[buff:{}][{}][level:{}][playerId:{}][{}]", new Object[] { buff.getTemplateName(), buff.getClass(), buff.getLevel(),
			// this.id, this.name });
			// }
			// }
			// }
			// }

			this.setMaxHPB(0);
			this.setMaxHPX(0);
			this.setMaxHPC(0);
			this.setMaxMPB(0);
			this.setMaxMPC(0);
			this.setPhyAttackB(0);
			this.setPhyAttackX(0);
			this.setPhyAttackC(0);
			this.setMagicAttackB(0);
			this.setMagicAttackX(0);
			this.setMagicAttackC(0);
			this.setPhyDefenceB(0);
			this.setPhyDefenceX(0);
			this.setPhyDefenceC(0);
			this.setMagicDefenceB(0);
			this.setMagicDefenceX(0);
			this.setMagicDefenceC(0);
			this.setDodgeB(0);
			this.setDodgeX(0);
			this.setDodgeC(0);
			this.setCriticalDefenceB(0);
			this.setCriticalDefenceX(0);
			this.setCriticalDefenceC(0);
			this.setHitB(0);
			this.setHitX(0);
			this.setHitC(0);
			this.setCriticalHitB(0);
			this.setCriticalHitX(0);
			this.setCriticalHitC(0);
			this.setAccurateB(0);
			this.setAccurateX(0);
			this.setAccurateC(0);
			this.setBreakDefenceB(0);
			this.setBreakDefenceX(0);
			this.setBreakDefenceC(0);
			this.setFireAttackB(0);
			this.setFireAttackX(0);
			this.setFireAttackC(0);
			this.setFireDefenceB(0);
			this.setFireDefenceX(0);
			this.setFireDefenceC(0);
			this.setFireIgnoreDefenceB(0);
			this.setFireIgnoreDefenceX(0);
			this.setFireIgnoreDefenceC(0);
			this.setBlizzardAttackB(0);
			this.setBlizzardAttackX(0);
			this.setBlizzardAttackC(0);
			this.setBlizzardDefenceB(0);
			this.setBlizzardDefenceX(0);
			this.setBlizzardDefenceC(0);
			this.setBlizzardIgnoreDefenceB(0);
			this.setBlizzardIgnoreDefenceX(0);
			this.setBlizzardIgnoreDefenceC(0);
			this.setWindAttackB(0);
			this.setWindAttackX(0);
			this.setWindAttackC(0);
			this.setWindDefenceB(0);
			this.setWindDefenceX(0);
			this.setWindDefenceC(0);
			this.setWindIgnoreDefenceB(0);
			this.setWindIgnoreDefenceX(0);
			this.setWindIgnoreDefenceC(0);
			this.setThunderAttackB(0);
			this.setThunderAttackX(0);
			this.setThunderAttackC(0);
			this.setThunderDefenceB(0);
			this.setThunderDefenceX(0);
			this.setThunderDefenceC(0);
			this.setThunderIgnoreDefenceB(0);
			this.setThunderIgnoreDefenceX(0);
			this.setThunderIgnoreDefenceC(0);

			this.phyDefenceRateOther = 0;
			this.magicDefenceRateOther = 0;
			this.dodgeRateOther = 0;
			this.criticalHitRateOther = 0;
			this.breakDefenceRateOther = 0;
			this.hitRateOther = 0;
			this.accurateRateOther = 0;
			this.fireDefenceRateOther = 0;
			this.fireIgnoreDefenceRateOther = 0;
			this.blizzardDefenceRateOther = 0;
			this.blizzardIgnoreDefenceRateOther = 0;
			this.windDefenceRateOther = 0;
			this.windIgnoreDefenceRateOther = 0;
			this.thunderDefenceRateOther = 0;
			this.thunderIgnoreDefenceRateOther = 0;
			this.criticalHitRateOther = 0;
			this.criticalDefenceRateOther = 0;
			this.magicWeaponDevourPercent = 0;
			this.expPercent = 0;
			this.petExpPercent = 0;
			medicineDiscount = 0;
			aliveHpPercent = 0;
			repairDiscount = 0;
			shouhunAttr = 0;

			this.hpRecoverBaseB = 0;
			this.hpRecoverExtend = 0;
			this.mpRecoverBaseB = 0;
			this.mpRecoverExtend = 0;

			this.speedC = 0;
			this.speedA = 290;
			this.speedE = 0;
			passiveEnchants.clear();
			this.maxHpY = 0;
			this.phyAttackY = 0;
			this.magicAttackY = 0;
			this.hitY = 0;
			this.criticalHitY = 0;
			this.accurateY = 0;
			this.breakDefenceY = 0;
			this.magicDefenceY = 0;
			this.phyDefenceY = 0;
			this.decreaseConTimeRate = 0;
			this.decreaseDmgRate = 0;
			
			this.phyAttackZ = 0;
			this.magicAttackZ = 0;
			this.phyDefenceZ = 0;
			this.magicDefenceZ = 0;
			this.hitZ = 0;
			this.dodgeZ = 0;
			this.criticalHitZ = 0;
			
			
			try {
				if (JiazuEntityManager2.instance != null) {
					JiazuEntityManager2.instance.清空BCX值(this);
				}
			} catch (Exception e) {
				JiazuManager2.logger.error("[新家族] [清除玩家原来家族技能加成属性] [异常] [" + this.getLogString() + "]", e);
			}
			try {
				if (this.getHuntLifr() != null) {
					this.getHuntLifr().setHasLoadAllAttr(false);
				}
			} catch (Exception e) {

			}
			try {
				if (this.getSoulPith() != null) {
					this.getSoulPith().setOldAddProps(null);
				}
			} catch (Exception e) {

			}

		}

		this.coolDownTimePercentB = 0;
		this.ironMaidenPercent = 0;
		this.hpStealPercent = 0;
		this.mpStealPercent = 0;

		this.toughness = 0;
		this.hold = false;
		this.immunity = false;
		this.stun = false;
		this.invulnerable = false;
		this.fighting = false;
		this.poison = false;
		this.weak = false;
		this.silence = false;

		this.shield = -1;
		this.setLastRequestTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());

		this.flying = false;
		this.huDunDamage = 0;
		this.recoverHpHuDun = 0;

		FlyTalentManager.getInstance().resetTalentProperty(this);
		// if(buffs != null && buffs.size() > 0){
		// synchronized (buffs) {
		// for (int i = buffs.size() - 1; i >= 0; i--) {
		// Buff buff = buffs.get(i);
		// if(buff == null || buff.getTemplate() == null || buff.getTemplateName() == null) {
		// continue;
		// }
		// if (buff.getTemplate().clearSkillPointNotdisappear || buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
		// buff.start(this);
		// ActiveSkill.logger.warn("在清空所有BCX值后调用保留buff的start方法[buff:{}][{}][level:{}][playerId:{}][{}]", new Object[] { buff.getTemplateName(), buff.getClass(), buff.getLevel(),
		// this.id, this.name });
		// }
		// }
		// }
		// }
	}

	/**
	 * 洗点，将用户的技能点全部洗掉
	 */
	public void resetSkillPoints() {
		// this.getDownFromHorse();
		this.downFromHorse();
		this.closeAura();

		synchronized (buffs) {
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				if (buff.getTemplate().clearSkillPointNotdisappear) {
					if (ActiveSkill.logger.isDebugEnabled()) ActiveSkill.logger.debug("使用洗髓丹保留[buff:{}][{}][level:{}][playerId:{}][{}]", new Object[] { buff.getTemplateName(), buff.getClass(), buff.getLevel(), this.id, this.name });
					continue;
				}
				if (buff != null && !buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
					buff.end(this);
					if (buff.isForover() || buff.isSyncWithClient()) {
						this.removedBuffs.add(buff);
					}
					buffs.remove(i);
					setDirty(true, "buffs");

					if (ActiveSkill.logger.isDebugEnabled()) {
						ActiveSkill.logger.debug("[洗点去除BUFF] [{}] [洗点] [{}:{}] [time:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime() });
					}

				}
			}
		}

		清空所有的BCX值(true);
		setSkillOneLevels(new byte[40]);
		if (getSoulLevel() - 40 > 0) {
			getCurrSoul().setUnallocatedSkillPoint(getSoulLevel() - 40);
		} else {
			getCurrSoul().setUnallocatedSkillPoint(0);
		}

		setUnallocatedSkillPoint(getCurrSoul().unallocatedSkillPoint);
		// 初始化背包
		if (knapsacks_common != null) {
			for (Knapsack k : knapsacks_common) {
				if (k != null) {
					k.init(this, k.getCells().length);
				}
			}
		}
		if (knapsacks_fangBao != null) {
			// for (Knapsack k : knapsacks_fangBao) {
			// if (k != null) {
			knapsacks_fangBao.init(this, knapsacks_fangBao.getCells().length);
			// }
			// }
		}
		// 加载元神 和元神加成
		this.initializing = false;
		try {
			this.init("resetSkillPoints");
		} catch (Exception e1) {
			CoreSubSystem.logger.error("");
		}
		if (false) {
			initSoul();

			setSoulLevel(getCurrSoul().getGrade());

			initNextLevelExpPool();

			this.setSpeedA(this.speedA);

			this.setCommonAttackSpeed((short) 2000);
			this.aura = -1;

			// 计算一级属性

			ExperienceManager em = ExperienceManager.getInstance();
			if (this.level <= em.maxLevel) this.setNextLevelExp(em.maxExpOfLevel[this.getSoulLevel()]);
			else this.setNextLevelExp(em.maxExpOfLevel[em.maxLevel]);

			this.initNextLevelExpPool();
		}
		try {
			JiazuEntityManager2.instance.addPracticeAttr(this);
			SoulPithEntityManager.getInst().initSoulPithAttr(this);
		} catch (Exception e) {
			JiazuManager2.logger.error("[新家族] [增加家族技能属性] [异常] [" + this.getLogString() + "]", e);
		}

		SimpleEntityManager<Player> emm = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		try {
			emm.flush(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 这个标记在玩家每次进地图的时候都会设置(当赐福buff结束后，如果玩家不过地图那么赐福还是有效果，但如果过地图赐福就会失效)
	public transient boolean 赐福标记 = false;

	/** 中兽魁毒标记，通过此标记有些技能需要特殊处理 key=playerId */
	private transient Map<Long, Byte> duFlag = new Hashtable<Long, Byte>();

	@Override
	public byte getSpriteStatus(Fighter caster) {
		if (!(caster instanceof Player) || !duFlag.containsKey(caster.getId())) {
			return (byte) 0;
		}
		return duFlag.get(caster.getId());
	}

	public void changeDuFlag(Player player, byte flag) {
		if (flag <= 0) {
			duFlag.remove(player.getId());
		} else {
			duFlag.put(player.getId(), flag);
		}
	}

	public transient int bianShenSkillId = 8260;
	// 记录上次变身为兽的时间，有5秒cd
	public transient long lastShouStatusTime = 0;

	/** 兽魁阶（不同阶需要展示不同的兽形态） 目前只有兽魁用 */
	private int playerRank;

	/**
	 * 判断玩家是否处于兽形态
	 * @return
	 */
	public boolean isShouStatus() {
		return this.getCareer() == 5 && getShouStat() == 1;
	}

	public transient int bianshenSKlv = 0;

	/**
	 * 改变兽魁形态，兽变人 人变兽 (改变形态后需要更改相应的avata以及技能排列等)
	 */
	public void changeShoukuiStatus(int skLevel) {
		long now = System.currentTimeMillis();
		if (this.isShouStatus()) {
			Skill.logger.info("[变身技能请求] [兽形态操作] [{}]", new Object[] { this.getLogString() });
			this.setShouStat(0);
			this.addMessageToRightBag(new SWITCH_BIANSHENJI_SKILLS_RES(GameMessageFactory.nextSequnceNum(), bianShenSkillId, this.getSkillPoints(), 0, new int[0], new int[0], new int[0]));
			// long left = now - lastShouStatusTime - Constants.变身CD;
			long left = (lastShouStatusTime + Constants.变身CD) - now;
			if (left < 0) {
				left = 0;
			}
			this.addMessageToRightBag(new NOTICE_CLIENT_BIANSHEN_CD_REQ(GameMessageFactory.nextSequnceNum(), left));
			this.setSpeedC(this.getSpeedC());
			this.setCriticalHitRate(this.getCriticalHitRate());
		} else {
			Career career = CareerManager.getInstance().getCareer(this.getCareer());
			if (career == null) {
				Skill.logger.info("[变身技能请求] [错误:职业{}不存在] [是否兽形态:{}] [{}]", new Object[] { this.getCareer(), this.isShouStatus(), this.getLogString() });
				this.addMessageToRightBag(new SWITCH_BIANSHENJI_SKILLS_RES(GameMessageFactory.nextSequnceNum(), bianShenSkillId, this.getSkillPoints(), 0, new int[0], new int[0], new int[0]));
			}
			Skill[] skills = career.getBianShenSkills();
			if (skills == null || skills.length == 0) {
				Skill.logger.info("[变身技能请求] [错误:变身技能配置错误] [是否兽形态:{}] [{}]", new Object[] { this.getCareer(), this.isShouStatus(), this.getLogString() });
				this.addMessageToRightBag(new SWITCH_BIANSHENJI_SKILLS_RES(GameMessageFactory.nextSequnceNum(), bianShenSkillId, this.getSkillPoints(), 0, new int[0], new int[0], new int[0]));
			}
			int bianshenid = 0;
			int skillids[] = new int[skills.length];
			int skillpoints[] = new int[skills.length];
			int skillstats[] = new int[skills.length];
			for (int i = 0; i < skills.length; i++) {
				if (skills[i] instanceof ActiveSkill) {
					ActiveSkill sk = (ActiveSkill) skills[i];
					skillids[i] = sk.getId();
					if (sk.isBianshenBtn()) {
						bianshenid = sk.getId();
					}
					int level = this.getSkillLevel(sk.getId());
					skillpoints[i] = sk.calculateDou(this, level);
					if (level == 0) {
						skillstats[i] = 1;
					}
				} else {
					continue;
				}
			}
			this.setShouStat(1);
			bianshenSKlv = skLevel;
			this.lastShouStatusTime = System.currentTimeMillis();
			this.setSpeedC(this.getSpeedC());
			this.setCriticalHitRate(this.getCriticalHitRate());
			Skill.logger.info("[变身技能请求] [人形态操作] [技能id:{}] [当前技能豆:{}] [bianshenid:{}] [技能豆:{}] [技能状态:{}] [{}]", new Object[] { Arrays.toString(skillids), this.getSkillPoints(), bianshenid, Arrays.toString(skillpoints), Arrays.toString(skillstats), this.getLogString() });
			this.addMessageToRightBag(new SWITCH_BIANSHENJI_SKILLS_RES(GameMessageFactory.nextSequnceNum(), bianshenid, this.getSkillPoints(), 1, skillids, skillpoints, skillstats));

		}
	}

	/**
	 * 需要通知玩家
	 * 上线的时候
	 * 升级的时候
	 * 玩家点变身技能的时候
	 * 任务触发
	 */
	public void noticeShouKuiInfo(String type) {
		if (this.getCareer() != 5) {
			return;
		}
		Career career = CareerManager.getInstance().getCareer(this.getCareer());
		if (career == null) {
			Skill.logger.info("[变身技能改变通知] [{}] [错误:职业{}不存在] [是否兽形态:{}] [{}]", new Object[] { type, this.getCareer(), this.isShouStatus(), this.getLogString() });
			this.addMessageToRightBag(new SWITCH_BIANSHENJI_SKILLS_RES(GameMessageFactory.nextSequnceNum(), bianShenSkillId, this.getSkillPoints(), 0, new int[0], new int[0], new int[0]));
		}
		Skill[] skills = career.getBianShenSkills();
		if (skills == null || skills.length == 0) {
			Skill.logger.info("[变身技能改变通知] [{}] [错误:变身技能配置错误] [是否兽形态:{}] [{}]", new Object[] { type, this.getCareer(), this.isShouStatus(), this.getLogString() });
			this.addMessageToRightBag(new SWITCH_BIANSHENJI_SKILLS_RES(GameMessageFactory.nextSequnceNum(), bianShenSkillId, this.getSkillPoints(), 0, new int[0], new int[0], new int[0]));
		}
		int skillids[] = new int[skills.length];
		int skillpoints[] = new int[skills.length];
		int skillstats[] = new int[skills.length];
		int bianshenid = 0;
		for (int i = 0; i < skills.length; i++) {
			if (skills[i] instanceof ActiveSkill) {
				ActiveSkill sk = (ActiveSkill) skills[i];
				int needlevel = sk.getNeedPlayerLevel()[0];
				if (sk.getId() != this.bianShenSkillId && this.getSkillLevel(sk.getId()) == 0 && this.getSoulLevel() >= needlevel) {
					byte newLevel = this.addSkillLevel(sk.getId());
					SkillInfo si = new SkillInfo();
					si.setInfo(this, sk);
					NEW_QUERY_CAREER_INFO_BY_ID_RES qciRes = new NEW_QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
					this.addMessageToRightBag(qciRes);
					Skill.logger.info("[变身技能改变通知] [学习技能] [type:{}] [skillId:{}] [skillName:{}] [newLevel:{}] [{}]", new Object[] { type, sk.getId(), sk.getName(), newLevel, this.getLogString() });
				}
				if (sk.isBianshenBtn()) {
					bianshenid = sk.getId();
				}
				skillids[i] = sk.getId();
				int level = this.getSkillLevel(sk.getId());
				skillpoints[i] = sk.calculateDou(this, level);
				if (level == 0) {
					skillstats[i] = 1;
				}
			} else {
				continue;
			}
		}
		synchAvata();
		Skill.logger.info("[变身技能改变通知] [type:{}] [当前技能豆:{}] [bianshenid:{}] [人形态操作] [技能id:{}] [技能豆:{}] [技能状态:{}] [{}]", new Object[] { type, this.getSkillPoints(), bianshenid, Arrays.toString(skillids), Arrays.toString(skillpoints), Arrays.toString(skillstats), this.getLogString() });
		this.addMessageToRightBag(new SWITCH_BIANSHENJI_SKILLS_RES(GameMessageFactory.nextSequnceNum(), bianshenid, this.getSkillPoints(), this.getShouStat(), skillids, skillpoints, skillstats));
	}

	/**
	 * 使用改变兽魁豆技能调用此方法改变兽魁豆数量
	 * @param num
	 */
	public void changeShoukuiDouNum(int num) {
		lastUseSkillTimeOfAddDou = System.currentTimeMillis();
		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("[" + this.getLogString() + "] [career:" + this.getCareer() + "] [使用技能改变豆数量] [" + this.isShouStatus() + "] [skillPoints:" + skillPoints + "] [num:" + num + "]");
		}
		if (num > 0) { // 正数为加
			if (skillPoints + num >= 5) {
				setSkillPoints(5);
			} else {
				setSkillPoints(skillPoints + num);
			}
		} else if (num < 0) { // 负数为减
			skillPoints = skillPoints + num;
			if (skillPoints < 0) {
				setSkillPoints(0);
			} else {
				setSkillPoints(skillPoints);
			}
		}
	}

	/**
	 * 获取兽魁剩余豆数量
	 * @return
	 */
	public int getShoukuiDouNum() {
		return skillPoints;
	}

	public void updateShouKuiDou() {
		if (this.getCareer() == 5) {
			long now = System.currentTimeMillis();
			if (getShoukuiDouNum() > 3) {
				if (now - lastUseSkillTimeOfAddDou >= 60 * 1000) {
					if (now - lastMinusDouTime >= 20 * 1000) {
						lastMinusDouTime = now;
						this.setSkillPoints(skillPoints - 1);
					}
				}
			}
		}
	}

	/**
	 * 附魔被动触发效果附魔id
	 * */
	public transient volatile List<EnchantTempModel> passiveEnchants = new ArrayList<EnchantTempModel>();

	public transient volatile Map<Integer, Long> passiveCds = new Hashtable<Integer, Long>();

	public transient long lastEnchantTime = 0L;
	/** 减少受到伤害比例 */
	public transient int decreaseDmgRate = 0;
	/** 减少控制类技能持续时间比率（千分比） */
	public transient int decreaseConTimeRate;
	/** 物品id */
	public transient long decreaseArticleId = -1;

	/**
	 * 检查玩家被动附魔触发
	 * @param checkType
	 * @return -1代表未触发或触发buff 返回值大于0代表减少buff持续时间百分比（100为抵抗此次）
	 */
	public long checkPassiveEnchant(byte checkType) {
		try {
			long now = System.currentTimeMillis();
			if ((lastEnchantTime + EnchantEntityManager.触发间隔) >= now) { // 所有哦被动附魔同内置cd
				return -1;
			}
			if (passiveEnchants.size() > 0) {
				int ran = random.nextInt(1000);
				int prob = 0; // 触发概率
				for (EnchantTempModel etm : passiveEnchants) {
					EnchantModel model = EnchantManager.instance.modelMap.get(etm.getId());
					if (model != null && model.getPassiveType() == checkType) {
						EquipmentEntity ee = (EquipmentEntity) this.getEquipmentColumns().get(model.getEquiptmentType());
						if (ee != null && ee.getId() == etm.getEquiptId() && !ee.getEnchantData().isLock()) {
							if (model.getProbType() == 2) {
								prob = ee.getEnchantData().getEnchants().get(0).getAttrNum();
							} else {
								prob = model.getProbabbly();
							}
							if (EnchantManager.logger.isDebugEnabled()) {
								EnchantManager.logger.debug("[checkPassiveEnchant] [ prob:" + prob + "] [ran:" + ran + "] [checkType :" + checkType + "] [" + this.getLogString() + "]");
							}
							if (ran < prob) {

								if (model.getCd() > 0 && passiveCds.containsKey(model.getId())) {
									long lastTime = passiveCds.get(model.getId());
									if ((lastTime + model.getCd()) >= now) {
										if (EnchantManager.logger.isDebugEnabled()) {
											EnchantManager.logger.debug("[checkPassiveEnchant] [触发失败] [内置cd时间未到] [lastTime:" + lastTime + "] [" + model.getId() + "] [" + this.getLogString() + "]");
										}
										return -1;
									}
								}

								passiveCds.put(model.getId(), now);

								if (model.getType() == EnchantEntityManager.附魔类型_触发buff && ee.getEnchantData() != null && ee.getEnchantData().getEnchants().size() > 0) { // 触发buff
									// 加buff
									EnchantManager.fireBuff(this, model, ee.getEnchantData().getEnchants().get(0).getAttrNum());
								} else { // 免疫此次debuff效果
									try {
										this.getTimerTaskAgent().createTimerTask(new AddBuffTimerTask(this), EnchantManager.附魔buff延迟时间, TimerTask.type_附魔特效buff);
									} catch (Exception e) {
									}
								}
								if (!ee.getEnchantData().lostDurable(this, ee, 1)) { // 附魔耐久消耗完移除附魔效果
									// EnchantEntityManager.instance.unLoadEnchantAttr(this, ee);
									try {
										String des = String.format(Translate.附魔消失邮件标题, ee.getArticleName());
										MailManager.getInstance().sendMail(this.getId(), new ArticleEntity[0], new int[0], Translate.附魔消失, des, 0L, 0L, 0L, "附魔消失");
										this.sendError(des);
									} catch (Exception e) {
										EnchantManager.logger.warn("[checkPassiveEnchant] [附魔消失邮件通知] [异常] [" + this.getLogString() + "]", e);
									}
								}
								try {
									if (ee.getEnchantData() != null && ee.getEnchantData().getEnchants().size() > 0 && ee.getEnchantData().getEnchants().get(0).getDurable() < EnchantManager.耐久) {
										this.sendError(String.format(Translate.低于10点通知, ee.getArticleName()));
									}
								} catch (Exception e) {
									EnchantManager.logger.warn("[checkPassiveEnchant] [低于10点通知] [异常] [" + this.getLogString() + "]", e);
								}
								CoreSubSystem.notifyEquipmentChange(this, new EquipmentEntity[] { ee });
								if (EnchantManager.logger.isDebugEnabled()) {
									EnchantManager.logger.debug("[checkPassiveEnchant] [触发效果] [checkType:" + checkType + "] [enchantId:" + model.getId() + "] [equiptId:" + etm.getEquiptId() + "]");
								}
								if (model.getType() == EnchantEntityManager.附魔类型_触发buff && ee.getEnchantData() != null && ee.getEnchantData().getEnchants().size() > 0) { // 触发buff
									// 加buff
									// EnchantManager.fireBuff(this, model, ee.getEnchantData().getEnchants().get(0).getAttrNum());
									return -1;
								} else { // 免疫此次debuff效果
									/*
									 * try {
									 * this.getTimerTaskAgent().createTimerTask(new AddBuffTimerTask(this), EnchantManager.附魔buff延迟时间, TimerTask.type_附魔特效buff);
									 * } catch (Exception e) {
									 * }
									 */
									return 100;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			EnchantManager.logger.warn("[checkPassiveEnchant] [异常] [" + this.getLogString() + "] [checkType:" + checkType + "]", e);
		}
		return -1;
	}

	public static String double_exp_day = "2013-04-13";
	/** 法宝吞噬经验加成 */
	private transient int magicWeaponDevourPercent = 0;
	/** 宠物打怪经验加成--法宝额外增加的，所有玩家宠物加经验时都有效 */
	private transient int petExpPercent;
	/** 购买红蓝药费用减少% */
	private transient int medicineDiscount;
	/** 回城复活血量增加% */
	private transient int aliveHpPercent;
	/** 修理装备费用减少% */
	private transient int repairDiscount;
	/** 兽魂属性增加% */
	private transient int shouhunAttr;

	/**
	 * 给用户增加经验值， reason标识因为何种原因增加经验值 0 表示
	 * 
	 * @param exp
	 */
	public void addExp(long exp, int reason) {
		exp = ExpAddManager.instance.doAddExp(this, exp, reason);
		if ((reason == ExperienceManager.ADDEXP_REASON_FIGHTING || reason == ExperienceManager.ADDEXP_REASON_FIRE_NPC) && exp > 0) {
			exp = exp * (100 + this.getExpPercent()) / 100;
		}
		// 正常加的经验
		{
			double expRateFromActivity = ActivityManager.getInstance().getExpRateFromActivity(Calendar.getInstance());
			try {
				String limitmaps[] = ActivityManager.getInstance().getLimitMaps(Calendar.getInstance());
				boolean isaddexp = false;
				if (this.getCurrentGame() != null && this.getCurrentGame().gi != null) {
					String currmapname = this.getCurrentGame().gi.name;
					if (limitmaps != null && limitmaps.length > 0) {
						for (String mapname : limitmaps) {
							if (mapname.equals(currmapname)) {
								isaddexp = true;
								break;
							}
						}
					} else {
						isaddexp = true;
					}
				}
				if (!isaddexp) {
					expRateFromActivity = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			exp *= expRateFromActivity;
		}
		long addExp = exp;
		// PK惩罚掉的经验
		long pkExp = 0;
		// 疲劳掉的经验
		long piLaoExp = 0;

		pkExp = addExp - (long) (addExp * 得到玩家获得经验的pk惩罚百分比(this));

		// 打怪获得经验受到罪恶值影响
		if (reason == ExperienceManager.ADDEXP_REASON_FIGHTING) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				if (killMonsterCount > 12000) {
					if (killMonsterCount > 12000 && killMonsterCount < 12010) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您已经进入疲劳期杀怪收益开始降低);
						this.addMessageToRightBag(hreq);
					}
					if (killMonsterCount > 14000 && killMonsterCount < 14010) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您已经进入疲劳期杀怪收益持续降低中);
						this.addMessageToRightBag(hreq);
					}
					if (killMonsterCount > 16000 && killMonsterCount < 16010) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您已经进入疲劳期杀怪收益会越来越低休息会吧);
						this.addMessageToRightBag(hreq);
					}
					if (killMonsterCount > 18000 && killMonsterCount < 18010) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您已经进入疲劳期很久了杀怪收益降低已经很多了建议您去做一些活动);
						this.addMessageToRightBag(hreq);
					}
					if (killMonsterCount > 20000 && killMonsterCount < 20010) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您已经进入疲劳期很久了杀怪快要没有收益了);
						this.addMessageToRightBag(hreq);
					}
					if (killMonsterCount > 24000 && killMonsterCount < 24010) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.别打了杀怪没经验了本游戏活动很多建议您去做一些活动);
						this.addMessageToRightBag(hreq);
					}
					// exp = exp * (24000 - killMonsterCount) / 24000;
					piLaoExp = addExp - addExp * (24000 - killMonsterCount) / 24000;
				}
			}
			int onTime = getOnLineTimePiLao();
			if (onTime > 0) {
				if (onTime == 3) {
					piLaoExp += addExp - addExp * 50 / 100;
				} else if (onTime == 4) {
					piLaoExp += addExp - addExp * 40 / 100;
				} else if (onTime == 5) {
					piLaoExp += addExp - addExp * 30 / 100;
				} else if (onTime == 6) {
					piLaoExp += addExp - addExp * 20 / 100;
				} else if (onTime == 7) {
					piLaoExp += addExp - addExp * 0 / 100;
				}
				if (exp <= 0) {
					return;
				}
			}
			exp = addExp - piLaoExp - pkExp;
			if (exp <= 0) {
				exp = 0;
				if (piLaoExp > 0 && pkExp > 0) {
					if (exp == 0) {
						send_HINT_REQ(Translate.经验到0提示_1, (byte) 7);
					} else {
						send_HINT_REQ(Translate.translateString(Translate.经验扣除说明PK疲劳, new String[][] { { Translate.COUNT_1, exp + "" }, { Translate.COUNT_2, pkExp + "" }, { Translate.COUNT_3, piLaoExp + "" } }), (byte) 7);
					}
				} else if (piLaoExp > 0) {
					if (exp == 0) {
						send_HINT_REQ(Translate.经验到0提示_2, (byte) 7);
					} else {
						send_HINT_REQ(Translate.translateString(Translate.经验扣除说明疲劳, new String[][] { { Translate.COUNT_1, exp + "" }, { Translate.COUNT_2, piLaoExp + "" } }), (byte) 7);
					}
				} else if (pkExp > 0) {
					send_HINT_REQ(Translate.translateString(Translate.经验扣除说明PK, new String[][] { { Translate.COUNT_1, addExp + "" }, { Translate.COUNT_2, pkExp + "" } }), (byte) 7);
				}
				return;
			}
			// 宠物经验
			if (this.getActivePetId() > 0) {
				Pet pet = PetManager.getInstance().getPet(this.getActivePetId());
				if (pet != null) {
					pet.addExp(exp, ExperienceManager.ADDEXP_REASON_FIGHTING);
				}
			}
		}
		exp = addExp - piLaoExp - pkExp;
		if (exp < 0) {
			exp = 0;
		}
		if (piLaoExp > 0 || pkExp > 0) {
			if (piLaoExp > 0 && pkExp > 0) {
				if (exp == 0) {
					send_HINT_REQ(Translate.经验到0提示_1, (byte) 7);
				} else {
					send_HINT_REQ(Translate.translateString(Translate.经验扣除说明PK疲劳, new String[][] { { Translate.COUNT_1, exp + "" }, { Translate.COUNT_2, pkExp + "" }, { Translate.COUNT_3, piLaoExp + "" } }), (byte) 7);
				}
			} else if (piLaoExp > 0) {
				if (exp == 0) {
					send_HINT_REQ(Translate.经验到0提示_2, (byte) 7);
				} else {
					send_HINT_REQ(Translate.translateString(Translate.经验扣除说明疲劳, new String[][] { { Translate.COUNT_1, exp + "" }, { Translate.COUNT_2, piLaoExp + "" } }), (byte) 7);
				}
			} else if (pkExp > 0) {
				send_HINT_REQ(Translate.translateString(Translate.经验扣除说明PK, new String[][] { { Translate.COUNT_1, exp + "" }, { Translate.COUNT_2, pkExp + "" } }), (byte) 7);
			} else {
				send_HINT_REQ(Translate.translateString(Translate.经验说明, new String[][] { { Translate.COUNT_1, addExp + "" } }), (byte) 7);
			}
		} else {
			send_HINT_REQ(Translate.translateString(Translate.经验说明, new String[][] { { Translate.COUNT_1, addExp + "" } }), (byte) 7);
		}
		if (exp <= 0) return;

		// 封印赐福公式：（（110-level）/110+20%）*exp
		long 赐福经验 = 0;
		{
			if (PlayerManager.开启赐福标记 && 赐福标记) {
				赐福经验 = (long) (((110 - this.getLevel()) * 1.0d / 110 + 0.2) * exp);
			}
		}

		if (赐福经验 > 0) {
			exp = exp + 赐福经验;
		}

		ExperienceManager em = ExperienceManager.getInstance();

		long oldValue = this.getExp();

		if (em.isUpgradeLevel(getSoulLevel(), this.exp, exp) && getLevel() < ExperienceManager.AUTO_LEVEL_UP_MAX) {
			int oldLevel = getSoulLevel();
			int newLevel = em.calculateLevel(getSoulLevel(), this.exp, exp);
			if (newLevel == MasterPrenticeManager.FINISH_PRENTICE) {
				// 出师
				MasterPrenticeManager.getInstance().chushi(this);
			}
			long newExp = em.calculateLeftExp(getSoulLevel(), this.exp, exp);

			if (getSoulLevel() >= 40) {
				getCurrSoul().setUnallocatedSkillPoint((getCurrSoul().unallocatedSkillPoint + newLevel - getSoulLevel()));
				setUnallocatedSkillPoint(getCurrSoul().unallocatedSkillPoint);
			}

			this.setExp(newExp);
			
			if(newLevel >= 220){
				if(petAddPropId == 0){
					petAddPropId = 1;
					setDirty(true, "petAddPropId");
				}
			}
			
			// this.setLevel((short) newLevel);
			// this.getCurrSoul().setGrade(newLevel);
			this.setSoulLevel(newLevel);
			if (this.getCurrSoul().isMainSoul()) {
				this.setLevel(this.getCurrSoul().getGrade());
			}
			playerLevelUp();
			{
				if (GamePlayerManager.sendLevelPackage && this.getCurrSoul().isMainSoul()) {
					String libao = null;
					if (newLevel == 10) {
						libao = PlayerManager.初级活动等级礼包[0];
					} else if (newLevel == 20) {
						libao = PlayerManager.初级活动等级礼包[1];
					} else if (newLevel == 30) {
						libao = PlayerManager.初级活动等级礼包[2];
					} else if (newLevel == 40) {
						libao = PlayerManager.初级活动等级礼包[3];
					} else if (newLevel == 50) {
						libao = PlayerManager.初级活动等级礼包[4];
					} else if (newLevel == 60) {
						libao = PlayerManager.初级活动等级礼包[5];
					} else if (newLevel == 70) {
						libao = PlayerManager.初级活动等级礼包[6];
					}
					if (libao != null) {
						ArticleManager am = ArticleManager.getInstance();
						if (am != null) {
							Article a = am.getArticle(libao);
							if (a != null) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								try {
									ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, this, a.getColorType(), 1, true);
									if (ae != null) {

										// 统计
										ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "升级活动礼包获得", null);

										MailManager mm = MailManager.getInstance();
										try {
											String description = Translate.translateString(Translate.恭喜您获得等级礼包, new String[][] { { Translate.STRING_1, libao } });
											mm.sendMail(this.getId(), new ArticleEntity[] { ae }, libao, description, 0, 0, 0, "活动礼包");
											String des = Translate.translateString(Translate.恭喜你到达某级, new String[][] { { Translate.COUNT_1, "" + newLevel } });
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
											this.addMessageToRightBag(hreq);
											if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [成功] [{}] [{}] [{}]", new Object[] { this.getLogString(), ae.getArticleName(), ae.getId() });
										} catch (Exception ex) {
											if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败] [{}] [{}] [{}]", new Object[] { this.getLogString(), ae.getArticleName(), ae.getId() }, ex);
										}
									} else {
										if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败，没有生成新物品] [{}] [{}]", new Object[] { this.getLogString(), a.getName() });
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} else {
								if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败，物品模板不存在] [{}] [{}]", new Object[] { this.getLogString(), libao });
							}
						}
					}
				}
			}
			{
				sendNewServerPrize(newLevel);
				sendNewServerPrizeAndNotice(newLevel);// 玩家获得礼包同时发广播
			}

			// 台服专用

			this.setNextLevelExp(em.maxExpOfLevel[getSoulLevel()]);
			this.initNextLevelExpPool();

			// 计算一级属性
			getCurrSoul().计算当前元神一级属性并给人加上(this);

			this.setHp(this.maxHP);
			this.setMp(this.maxMP);

			// TODO: 通知客户端升级
			NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, this.id, (byte) Event.LEVEL_UPGRADE, this.getSoulLevel());

			this.addMessageToRightBag(req);
			this.checkFunctionNPCModify(ModifyType.GRADE_UP);
			this.checkNextList();
			// NewPlayerLeadDataManager.getInstance().onPlayerLevelup(this);

			if (Game.logger.isInfoEnabled()) {
				Game.logger.info("[玩家升级] [{}] [{}] [ID:{}] [{}] [已通知客户端]", new Object[] { this.getUsername(), getName(), this.getId(), getSoulLevel() });
			}
			Player.sendPlayerAction(this, PlayerActionFlow.行为类型_升级, oldLevel + "->" + newLevel, 0, new Date(), GamePlayerManager.isAllowActionStat());
			HotspotManager.getInstance().openHotspot(this, Hotspot.OPENTYPE_LEVEL, "" + this.getLevel());
			HotspotManager.getInstance().overHotspot(this, Hotspot.OVERTYPE_LEVEL, "" + this.getLevel());
		} else {
			if (this.exp < this.nextLevelExpPool) {
				if (this.exp + exp < this.nextLevelExpPool) {
					this.setExp(this.exp + exp);
				} else {
					this.setExp(this.nextLevelExpPool);
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的经验已满);
					this.addMessageToRightBag(hreq);
				}
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的经验已满);
				this.addMessageToRightBag(hreq);
			}
		}

		if (exp > 0) {
			if (reason == ExperienceManager.ADDEXP_REASON_FIGHTING) {
				if (赐福经验 > 0) {
					// 通知客户端打怪获得经验值
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, this.id, (byte) Event.GAIN_EXPERIENCE_MONSTER, exp - 赐福经验);
					this.addMessageToRightBag(req);
					// 通知客户端打怪获得经验值
					NOTIFY_EVENT_REQ req1 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, this.id, (byte) Event.GAIN_EXPERIENCE_MONSTER, 赐福经验);
					this.addMessageToRightBag(req1);
				} else {
					// 通知客户端打怪获得经验值
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, this.id, (byte) Event.GAIN_EXPERIENCE_MONSTER, exp);
					this.addMessageToRightBag(req);
				}
			} else {
				if (赐福经验 > 0) {
					// 通知客户端获得经验值
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, this.id, (byte) Event.GAIN_EXPERIENCE_MONSTER, exp - 赐福经验);
					this.addMessageToRightBag(req);
					// 通知客户端获得经验值
					NOTIFY_EVENT_REQ req1 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, this.id, (byte) Event.GAIN_EXPERIENCE_MONSTER, 赐福经验);
					this.addMessageToRightBag(req1);
				} else {
					// 通知客户端获得经验值
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, this.id, (byte) Event.GAIN_EXPERIENCE_MONSTER, exp);
					this.addMessageToRightBag(req);
				}
			}
			if (!AuctionManager.使用新日志格式) { // 此修改与拍卖日志同一天修改
				if (GamePlayerManager.logger.isInfoEnabled()) {
					GamePlayerManager.logger.info("[获得经验] [{}] [{}] [ID:{}] [{}] [{}] [变化：{}-->{}] [恶名经验:{}] [疲劳经验:{}] [赐:{}]", new Object[] { this.getUsername(), getName(), this.getId(), exp, ExperienceManager.EXP_REASON_NAMES[reason], oldValue, this.getExp(), pkExp, piLaoExp, 赐福经验 });
				}
			} else {
				if (GamePlayerManager.logger.isInfoEnabled()) {
					GamePlayerManager.logger.info("[获得经验] [" + this.getLogString4Knap() + "] [exp:" + exp + "] [原经验:" + oldValue + "] [变化后经验:" + this.getExp() + "] [恶名经验:" + pkExp + "] [疲劳经验:" + piLaoExp + "] [赐福:" + 赐福经验 + "]");
				}
			}

			// 通知元神是否可以升级
			getSoulMessRes(id);
		}
	}

	public void sendNewServerPrize(int newLevel) {
		if (this.getCurrSoul().isMainSoul()) {

			// 7月16日新服“无极冰原”价值188元大礼包内容如下，发放规则为——7月4日当天在新服“炼狱绝地”达到相应等级的用户即可领取。
			String libao = null;
			GameConstants gc = GameConstants.getInstance();
			CompoundReturn cr = null;
			if (gc != null) {
				cr = NewServerActivityManager.getInstance().inNewserverActivity();
			}
			if (cr.getBooleanValue()) {
				libao = ((Map<Integer, String>) cr.getObjValue()).get(newLevel);
				if (libao != null) {
					ArticleManager am = ArticleManager.getInstance();
					if (am != null) {
						Article a = am.getArticle(libao);
						if (a != null) {
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							try {
								ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, this, a.getColorType(), 1, true);

								if (ae != null) {

									// 统计
									ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "升级活动礼包获得", null);

									MailManager mm = MailManager.getInstance();
									try {
										String description = Translate.translateString(Translate.恭喜您获得等级礼包, new String[][] { { Translate.STRING_1, libao } });
										mm.sendMail(this.getId(), new ArticleEntity[] { ae }, libao, description, 0, 0, 0, "等级礼包");
										String des = Translate.translateString(Translate.恭喜你到达某级, new String[][] { { Translate.COUNT_1, "" + newLevel } });
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
										this.addMessageToRightBag(hreq);
										if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [成功] [{}] [{}] [{}]", new Object[] { this.getLogString(), ae.getArticleName(), ae.getId() });
									} catch (Exception ex) {
										if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败] [{}] [{}] [{}]", new Object[] { this.getLogString(), ae.getArticleName(), ae.getId() }, ex);
									}
								} else {
									if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败，没有生成新物品] [{}] [{}]", new Object[] { this.getLogString(), a.getName() });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败，物品模板不存在] [{}] [{}]", new Object[] { this.getLogString(), libao });
						}
					} else {
						ArticleManager.logger.warn("[活动礼包] [ArticleManager为空]");
					}
				}
			}
		}
	}

	/**
	 * 发送等级奖励和广播
	 * 
	 * @param newLevel
	 */
	public void sendNewServerPrizeAndNotice(int newLevel) {
		if (this.getCurrSoul().isMainSoul()) {

			// 7月16日新服“无极冰原”价值188元大礼包内容如下，发放规则为——7月4日当天在新服“炼狱绝地”达到相应等级的用户即可领取。
			// String libao = null;
			GameConstants gc = GameConstants.getInstance();
			CompoundReturn cr = null;
			if (gc != null) {
				cr = NewServerActivityManager.getInstance().inServerActivity();
			} else {
				if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn(this.getLogString() + "[活动礼包2] [gc=null]");
			}
			if (cr.getBooleanValue()) {
				// libao = ((Map<Integer, String>)
				// cr.getObjValue()).get(newLevel);
				List<String> libaos = ((Map<Integer, List<String>>) cr.getObjVlues()[0]).get(newLevel);
				if (libaos != null) {
					for (String libao : libaos) {
						ArticleManager am = ArticleManager.getInstance();
						if (am != null) {
							Article a = am.getArticle(libao);
							if (a != null) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								try {
									ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, this, a.getColorType(), 1, true);

									if (ae != null) {

										// 统计
										ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "升级活动礼包获得", null);

										MailManager mm = MailManager.getInstance();
										try {
											String description = Translate.translateString(Translate.恭喜您获得等级礼包, new String[][] { { Translate.STRING_1, libao } });
											mm.sendMail(this.getId(), new ArticleEntity[] { ae }, libao, description, 0, 0, 0, "等级礼包");
											String des = Translate.translateString(Translate.恭喜你到达某级, new String[][] { { Translate.COUNT_1, "" + newLevel } });
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
											this.addMessageToRightBag(hreq);

											// 
											// 恭喜玩家xxxx到达5级并获得了《天神》最强新手礼活动的“靓装宝囊一”，内含完美紫色装备2件。到达15级将获得更多惊喜哦！
											String notice = "@STRING_1@님이 @STRING_2@레벨 달성!";// 恭喜STRING_1(玩家)到达STRING_2级
											// String mes =
											// NewServerActivityManager.getInstance().getNewServerActivityMap().get(gc.getServerName()).getNoticeMap().get(newLevel);
											String mes = ((Map<Integer, String>) cr.getObjVlues()[1]).get(newLevel);
											String msg = Translate.translateString(notice, new String[][] { { Translate.STRING_1, this.getName() }, { Translate.STRING_2, "" + newLevel } }) + mes;
											ChatMessageService.getInstance().sendMessageToSystem(msg);

											// 活动礼包2指留存活动礼包
											if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包2] [成功] [{}] [{}] [{}]", new Object[] { this.getLogString(), ae.getArticleName(), ae.getId() });
										} catch (Exception ex) {
											if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包2] [失败] [{}] [{}] [{}]", new Object[] { this.getLogString(), ae.getArticleName(), ae.getId() }, ex);
										}
									} else {
										if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包2] [失败，没有生成新物品] [{}] [{}]", new Object[] { this.getLogString(), a.getName() });
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} else {
								if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn(this.getLogString() + "[活动礼包2] [失败，物品模板不存在] [{}] [{}]", new Object[] { this.getLogString(), libao });
							}
						} else {
							ArticleManager.logger.warn(this.getLogString() + "[活动礼包2] [ArticleManager为空]");
						}
					}
				} else {
					if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn(this.getLogString() + "[活动礼包2] [没有礼包]");
				}
			}
		}
	}

	public SOUL_MESSAGE_RES getSoulMessByPlayer(Player player) {
		try {
			Soul soul = player.getSoul(Soul.SOUL_TYPE_SOUL);
			String currCostExp = player.getUpgradeExp()[player.getUpgradeNums()];
			String showMess = Translate.元神提示_元神为空;
			int showButton = 1;
			if (soul != null) {
				if (player.getCurrSoul().getSoulType() == Soul.SOUL_TYPE_BASE) {
					showMess = Translate.translateString(Translate.元神提示_元神, new String[][] { { Translate.COUNT_1, String.valueOf(player.currUpgradePoints[player.getUpgradeNums()]) }, { Translate.COUNT_2, String.valueOf(player.currUpgradePoints[player.getUpgradeNums()] + 1) } });
					if (player.getUpgradeNums() == 40) {
						showMess = Translate.translateString(Translate.元神提示_元神_满, new String[][] { { Translate.COUNT_1, String.valueOf(player.currUpgradePoints[player.getUpgradeNums()]) } });
					}
				} else {
					showMess = Translate.translateString(Translate.元神提示_本尊, new String[][] { { Translate.COUNT_1, String.valueOf(player.currUpgradePoints[player.getUpgradeNums()]) }, { Translate.COUNT_2, String.valueOf(player.currUpgradePoints[player.getUpgradeNums()] + 1) } });
					if (player.getUpgradeNums() == 40) {
						showMess = Translate.translateString(Translate.元神提示_本尊_满, new String[][] { { Translate.COUNT_1, String.valueOf(player.currUpgradePoints[player.getUpgradeNums()]) } });
					}
				}

				String upgraderesult = "";
				if (player.getLevel() >= 110) {
					if (player.upgradeCheak(Long.parseLong(currCostExp)).equals("ok")) {
						showButton = 0;
					}

				} else {
					showMess = Translate.元神提示_未开启;
					// -1表示未激活
					return new SOUL_MESSAGE_RES(GameMessageFactory.nextSequnceNum(), 0, showButton, showMess, -1, Long.parseLong(currCostExp), upgraderesult, GamePlayerManager.souldatas);// (GameMessageFactory.nextSequnceNum(),
					// 0,
					// showButton,
					// showMess,
					// -1,
					// Long.parseLong(currCostExp),
					// GamePlayerManager.souldatas);
				}

				if (upgradeNums >= 10) {
					upgraderesult = "和" + costArticleName + costNum[getIndexnum(currUpgradePoints[upgradeNums])] + Translate.个;
				}
				if (upgradeNums >= 20 && upgradeNums < 40) {
					upgraderesult = upgraderesult + "和" + costArticleName20 + costNum20[getIndexnum(currUpgradePoints[upgradeNums])] + Translate.个;
				}

				if (upgradeNums >= 40) {
					upgraderesult = Translate.已达上限;
				}

				if (CoreSubSystem.logger.isInfoEnabled()) {
					CoreSubSystem.logger.warn("[查看元神信息] [元神不为空] [是否可升级:" + (showButton == 0 ? "是" : "否") + "] [当前属性加成等级:" + player.getUpgradeNums() + "] [升级所需经验:" + currCostExp + "] [玩家信息：" + player.getLogString() + "]");
				}
				return new SOUL_MESSAGE_RES(GameMessageFactory.nextSequnceNum(), 0, showButton, showMess, player.getUpgradeNums(), Long.parseLong(currCostExp), upgraderesult, GamePlayerManager.souldatas);
			} else {
				if (CoreSubSystem.logger.isInfoEnabled()) {
					CoreSubSystem.logger.warn("[查看元神信息] [元神为空] [是否可升级:" + (showButton == 0 ? "是" : "否") + "] [当前属性加成等级:" + player.getUpgradeNums() + "] [升级所需经验:" + currCostExp + "] [玩家信息：" + player.getLogString() + "]");
				}
				return new SOUL_MESSAGE_RES(GameMessageFactory.nextSequnceNum(), 1, showButton, showMess, player.getUpgradeNums(), Long.parseLong(currCostExp), "", new SoulData[] {});
			}

		} catch (Exception e) {
			CoreSubSystem.logger.error("[查看元神信息] [异常]", e);
			return null;
		}
	}

	public SOUL_MESSAGE_RES getSoulMessRes(long pid) {
		// boolean isappserver = false;
		// String servername = GameConstants.getInstance().getServerName();
		// for (String name : ActivityManagers.getInstance().appServers) {
		// if (name.equals(servername)) {
		// isappserver = true;
		// break;
		// }
		// }
		//
		// if (ActivityManagers.getInstance().isOpenSoul && isappserver) {
		// // this.sendError("元神系统升级中.");
		// return null;
		// }

		if (id != pid) {
			Player otherplayer = null;
			try {
				otherplayer = PlayerManager.getInstance().getPlayer(pid);
				if (otherplayer != null) {
					return getSoulMessByPlayer(otherplayer);
				} else {
					CoreSubSystem.logger.warn("[查看其他玩家元神] [出错] [id:" + pid + "]");
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				CoreSubSystem.logger.warn("[查看其他玩家元神] [出错] [id:" + pid + "]", e);
				return null;
			}
		}
		return getSoulMessByPlayer(this);
	}

	/**
	 * 扣除经验
	 * 
	 * @param exp
	 * @param reason
	 */
	public boolean subExp(long exp, String reason) {
		// TODO 简单实现
		if (getExp() >= exp) {
			long oldValue = getExp();
			setExp(getExp() - exp);
			if (GamePlayerManager.logger.isInfoEnabled()) {
				GamePlayerManager.logger.info("[减少经验] [{}] [{}] [ID:{}] [{}] [{}] [变化：{}-->{}] [赐:{}]", new Object[] { this.getUsername(), getName(), this.getId(), exp, reason, oldValue, this.getExp(), 0 });
			}
			return true;
		} else {
			return false;
		}
	}

	public void playerLevelUpByClick() {

		ExperienceManager em = ExperienceManager.getInstance();

		int nowLevel = getSoulLevel();
		if (nowLevel >= em.maxLevel) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的级别已达到限制);
			this.addMessageToRightBag(hreq);
			return;
		}

		SealManager sealManager = SealManager.getInstance();
		int 封印等级 = 1000;
		if (sealManager != null && sealManager.seal != null) {
			封印等级 = sealManager.seal.sealLevel;
		}
		if (nowLevel >= 封印等级) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的级别已达到封印限制);
			this.addMessageToRightBag(hreq);
			return;
		}
		if (nowLevel >= RobberyConstant.玩家可升最高等级) {
			this.sendError(Translate.暂未开放更高等级);
			return;
		}

		if (!getCurrSoul().isMainSoul()) {
			if (nowLevel >= this.level) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的元神等级不能超过本尊等级);
				this.addMessageToRightBag(hreq);
				return;
			}
		}
		if (nowLevel >= 220) {
			TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(this.getId());
			if (entity.getFeisheng() != 1) { // 没有飞升不能升到220以上
				sendError(Translate.飞升后才能升级);
				return;
			}
		}
		try {
			if (FairyRobberyManager.inst.isPlayerInRobbery(this)) {
				sendError(Translate.渡劫中不可升级);
				return;
			}
		} catch (Exception e) {
			FairyRobberyManager.logger.warn("[仙界渡劫] [玩家升级] [异常] [" + this.getLogString() + "]", e);
		}
		if (em.isUpgradeLevel(getSoulLevel(), this.exp, 0)) {
			int oldLevel = getSoulLevel();
			int newLevel = oldLevel + 1;

			long newExp = em.calculateOneLevelLeftExp(getSoulLevel(), this.exp);

			if (getSoulLevel() >= 40) {
				getCurrSoul().setUnallocatedSkillPoint((getCurrSoul().unallocatedSkillPoint + newLevel - getSoulLevel()));
				setUnallocatedSkillPoint(getCurrSoul().unallocatedSkillPoint);
			}

			this.setExp(newExp);
			this.setSoulLevel(newLevel);
			try {
				LevelUpRewardManager.instance.autoReceiveReward(this);
			} catch (Exception e) {
				LevelUpRewardManager.logger.warn("[玩家升级发放奖励] [异常] [" + this.getLogString() + "]");
			}

			if (newLevel >= MasterPrenticeManager.FINISH_PRENTICE) {
				// 出师
				MasterPrenticeManager.getInstance().chushi(this);
			}
			if (this.getCurrSoul().isMainSoul()) {
				this.setLevel(this.getCurrSoul().getGrade());
				if (newLevel >= 封印等级) {
					封印成就(newLevel);
				}
				try {
					if (level == 259) {
						try {
							boolean has2 = false;
							for (Country country : CountryManager.getInstance().countryMap.values()) {
								if (country.first_259_playerIds[this.getMainCareer() - 1] > 0) {
									has2 = true;
									break;
								}
							}
							if (!has2) {
								Country country = CountryManager.getInstance().getCountryByCountryId(this.getCountry());
								if (country != null) {
									country.first_259_playerIds[this.getMainCareer() - 1] = this.getId();
									country.setFirst_259_playerIds(country.first_259_playerIds);
								}
								if (AchievementManager.getInstance() != null) {
									RecordAction r = PlayerAimManager.仙界第一职业等级[this.mainCareer - 1];
									AchievementManager.getInstance().record(this, r, 259);
								}
							}
						} catch (Exception e) {
							PlayerAimManager.logger.warn("[目标系统] [统计仙界最快到259的职业] [异常] [" + this.getLogString() + "]", e);
						}
					}
				} catch (Exception e) {
					PlayerAimManager.logger.warn("[目标系统] [统计仙界最快到259的职业] [异常] [" + this.getLogString() + "]", e);
				}
				if (!PlayerManager.开启赐福标记) {
					if (newLevel == 150) {
						PlayerManager.开启赐福标记 = true;
						// 世界广播
						ChatMessageService cms = ChatMessageService.getInstance();
						if (cms != null) {
							try {
								String description = Translate.translateString(Translate.伟大的到达150级开启了封印赐福, new String[][] { { Translate.STRING_1, name } });
								cms.sendMessageToSystem(description);
								ChatMessage msg = new ChatMessage();
								msg.setMessageText(description);
								cms.sendRoolMessageToSystem(msg);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				if (PlayerManager.开启赐福标记) {
					if (newLevel >= 110) {
						Buff buff = this.getBuffByName(PlayerManager.赐福buff);
						if (buff != null) {
							buff.setInvalidTime(0);
							赐福标记 = false;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.由于您到达了110级因此赐福BUFF效果消失);
							this.addMessageToRightBag(hreq);
						}
					}

				}
			} else {
				if (newLevel >= 封印等级) {
					if (sealManager != null && sealManager.seal != null) {
						try {
							if (sealManager.seal.firstPlayerIdInCountry[getCountry() - 1] <= 0) {
								sealManager.seal.firstPlayerIdInCountry[getCountry() - 1] = id;
								sealManager.saveSeal();
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(sealManager.seal.sealCanOpenTime);
								ChatMessageService cms = ChatMessageService.getInstance();
								ChatMessage msg = new ChatMessage();
								String description = Translate.translateString(Translate.某国某人为该国第一个到达封印级别的人国王可以在时间点后开启等级封印, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(getCountry()) }, { Translate.PLAYER_NAME_1, name }, { Translate.STRING_2, CountryManager.得到官职名(CountryManager.国王) }, { Translate.COUNT_1, calendar.get(Calendar.YEAR) + "" }, { Translate.COUNT_2, (calendar.get(Calendar.MONTH) + 1) + "" }, { Translate.COUNT_3, calendar.get(Calendar.DAY_OF_MONTH) + "" }, { Translate.COUNT_4, calendar.get(Calendar.HOUR_OF_DAY) + "" }, { Translate.COUNT_5, calendar.get(Calendar.MINUTE) + "" } });
								msg.setMessageText(description);
								cms.sendMessageToCountry(getCountry(), msg);
								cms.sendMessageToCountry(getCountry(), msg);
								cms.sendMessageToCountry(getCountry(), msg);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							Game.logger.error("[保存封印数据] [异常]", ex);
						}
					}
				}
			}
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[玩家升级前] " + this.getPlayerPropsString());
			}
			playerLevelUp();
			{
				if (this.getCurrSoul().isMainSoul()) {
					GameConstants gc = GameConstants.getInstance();
					// 腾讯测试用的银子发放
					if (gc.getServerName().equals("化外")) {
						if (newLevel == 10) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 50000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "10级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到10级，奖励银子50两");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						} else if (newLevel == 20) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 100000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "20级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到20级，奖励银子100两");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						} else if (newLevel == 30) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 200000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "30级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到30级，奖励银子200两");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						} else if (newLevel == 40) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 1000000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "40级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到40级，奖励银子1锭");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						} else if (newLevel == 50) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 4650000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "50级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到50级，奖励银子4锭650两");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						} else if (newLevel == 60) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 8000000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "60级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到60级，奖励银子8锭");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						} else if (newLevel == 70) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 10000000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "70级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到70级，奖励银子10锭");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						} else if (newLevel == 80) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 12000000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "80级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到80级，奖励银子12锭");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						} else if (newLevel == 90) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								// 10级自动给玩家加上5两银子
								bc.playerSaving(this, 14000000, CurrencyType.YINZI, SavingReasonType.LEVELUP, "90级奖励银子");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "恭喜您升到90级，奖励银子14锭");
								this.addMessageToRightBag(hreq);
							} catch (SavingFailedException e) {

							}
						}
					} else {
						// 升级自动发送礼包
						// 10级奖励 宝石竹青(3级)
						// 20级奖励 宝石湛天(3级)
						// 30级奖励 完美·紫色首饰*1 宝石竹青(3级)
						// 40级奖励 完美·紫色项链*1 宝石湛天(3级)
						String libao = null;
						if (newLevel == 10) {
							libao = PlayerManager.初级活动等级礼包[0];
						} else if (newLevel == 20) {
							libao = PlayerManager.初级活动等级礼包[1];
						} else if (newLevel == 30) {
							libao = PlayerManager.初级活动等级礼包[2];
						} else if (newLevel == 40) {
							libao = PlayerManager.初级活动等级礼包[3];
						} else if (newLevel == 50) {
							libao = PlayerManager.初级活动等级礼包[4];
						} else if (newLevel == 60) {
							libao = PlayerManager.初级活动等级礼包[5];
						} else if (newLevel == 70) {
							libao = PlayerManager.初级活动等级礼包[6];
						}
						if (libao != null) {
							ArticleManager am = ArticleManager.getInstance();
							if (am != null) {
								Article a = am.getArticle(libao);
								if (a != null) {
									ArticleEntityManager aem = ArticleEntityManager.getInstance();
									try {
										ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, this, a.getColorType(), 1, true);

										if (ae != null) {

											// 统计
											ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "升级活动礼包获得", null);

											MailManager mm = MailManager.getInstance();
											try {
												String description = Translate.translateString(Translate.恭喜您获得等级礼包, new String[][] { { Translate.STRING_1, libao } });
												mm.sendMail(this.getId(), new ArticleEntity[] { ae }, libao, description, 0, 0, 0, "等级礼包");
												String des = Translate.translateString(Translate.恭喜你到达某级, new String[][] { { Translate.COUNT_1, "" + newLevel } });
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
												this.addMessageToRightBag(hreq);
												if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [成功] [{}] [{}] [{}]", new Object[] { this.getLogString(), ae.getArticleName(), ae.getId() });
											} catch (Exception ex) {
												if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败] [{}] [{}] [{}]", new Object[] { this.getLogString(), ae.getArticleName(), ae.getId() }, ex);
											}
										} else {
											if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败，没有生成新物品] [{}] [{}]", new Object[] { this.getLogString(), a.getName() });
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								} else {
									if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[活动礼包] [失败，物品模板不存在] [{}] [{}]", new Object[] { this.getLogString(), libao });
								}
							}
						}
					}
				}
			}
			this.setNextLevelExp(em.maxExpOfLevel[getSoulLevel()]);
			this.initNextLevelExpPool();

			{
				// 活动
				sendNewServerPrize(newLevel);
				sendNewServerPrizeAndNotice(newLevel);// 玩家获得礼包同时发广播

			}

			// 计算一级属性
			getCurrSoul().计算当前元神一级属性并给人加上(this);

			this.setHp(this.maxHP);
			this.setMp(this.maxMP);

			// TODO: 通知客户端升级
			NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, this.id, (byte) Event.LEVEL_UPGRADE, this.getSoulLevel());

			this.addMessageToRightBag(req);
			this.checkFunctionNPCModify(ModifyType.GRADE_UP);
			this.checkNextList();

			if (Game.logger.isInfoEnabled()) {
				Game.logger.info("[玩家升级] [{}] [{}] [ID:{}] [{}] [已通知客户端]", new Object[] { this.getUsername(), getName(), this.getId(), getSoulLevel() });
			}
			Player.sendPlayerAction(this, PlayerActionFlow.行为类型_升级, oldLevel + "->" + newLevel, 0, new Date(), GamePlayerManager.isAllowActionStat());
			HotspotManager.getInstance().openHotspot(this, Hotspot.OPENTYPE_LEVEL, "" + this.getLevel());
			HotspotManager.getInstance().overHotspot(this, Hotspot.OVERTYPE_LEVEL, "" + this.getLevel());
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.恭喜您升级了);
			this.addMessageToRightBag(hreq);
			flushSealState();
			try {
				// 判断是否开启渡劫强拉人
				TransitRobberyManager.getInstance().check4NextRobberyLevel(this.id);
			} catch (Exception e) {
				TransitRobberyManager.logger.error("[渡劫][激活强制拉人出异常,e=" + e + "][" + this.getLogString() + "]");
			}
			try {
				if (this.getCurrSoul().getSoulType() == Soul.SOUL_TYPE_BASE && this.getSoulLevel() >= 60 && this.getSoul(Soul.SOUL_TYPE_SOUL) == null) {
					NOTICE_PARTICLE_REQ req11 = new NOTICE_PARTICLE_REQ(GameMessageFactory.nextSequnceNum(), 1, 2);
					this.addMessageToRightBag(req11);
				}
			} catch (Exception e) {

			}
			try {
				SoulPithEntityManager.getInst().initSoulPithAttr(this);
			} catch (Exception e) {

			}
		}
	}

	public void initNextLevelExpPool() {
		if (ExperienceManager.maxExpOfLevel != null) {
			if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				if (this.getLevel() < 190) {
					this.setNextLevelExpPool(ExperienceManager.maxExpOfLevel[getSoulLevel()] * 3);
				} else {
					this.setNextLevelExpPool((ExperienceManager.maxExpOfLevel[getSoulLevel()] * 5));
					GameManager.logger.warn("[韩服角色经验设置] [" + this.getLogString() + "] [等级:" + getSoulLevel() + "] [下级经验:" + ExperienceManager.maxExpOfLevel[getSoulLevel()] + "/" + this.getNextLevelExp() + "] [经验池经验:" + this.getNextLevelExpPool() + "]");
				}
			} else {
				this.setNextLevelExpPool(ExperienceManager.maxExpOfLevel[level] * 10);
			}
		}
	}

	/**
	 * 用户升级
	 */
	public void playerLevelUp() {

		try {
			// 升级引导
			NewPlayerLeadDataManager.getInstance().onPlayerLevelup(this);
		} catch (Exception e) {
			GameManager.logger.error("角色升级调用引导错误:" + this.getLogString(), e);
		}

		if (this.currSoul.isMainSoul()) {
			ActivityManagers.getInstance().handleReward(this);
			OldPlayerBackManager.getInstance().levelUpOldPlayerBack(this);
		}
		noticeShouKuiInfo("升级");
		CareerManager cm = CareerManager.getInstance();
		if (cm != null) {
			Career career = cm.getCareer(this.career);
			if (career != null) {
				Skill[] skills = career.getBasicSkills();
				if (skills != null) {
					for (int i = 0; i < skills.length; i++) {
						Skill skill = skills[i];
						if (skill != null) {
							int level = skill.getNeedPlayerLevel()[0];
							if (this.getSoulLevel() >= level) {
								int skillLevel = getSkillLevel(skill.getId());
								if (skillLevel <= 0) {
									String result = career.isUpgradable(this, skill.getId(), true);
									if (result == null) {
										if (getSoulLevel() == 6 || getSoulLevel() == 11) {
											SKILL_MSG res = new SKILL_MSG(GameMessageFactory.nextSequnceNum(), skill.getId(), skill.getName(), skill.getIconId(), skill.getDescription());
											addMessageToRightBag(res);
										}
										this.tryToLearnSkill(skill.getId(), false, true);
									} else {
										GameManager.logger.error("[自动学习技能失败] [{}] [{}] [{}] [{}]", new Object[] { this.getUsername(), this.getId(), this.getName(), result });
									}
								}
							}
						}
					}
				}
				skills = career.getXinfaSkills();
				if (skills != null) {
					for (int i = 0; i < 17; i++) {
						Skill skill = skills[i];
						if (skill != null) {
							int level = skill.getNeedPlayerLevel()[0];
							if (this.getSoulLevel() >= level) {
								int skillLevel = getSkillLevel(skill.getId());
								if (skillLevel <= 0) {
									String result = career.isUpgradable(this, skill.getId(), true);
									if (result == null) {
										this.tryToLearnSkill(skill.getId(), false, true);
									} else {
										GameManager.logger.error("[自动学习技能失败] [{}] [{}] [{}] [{}]", new Object[] { this.getUsername(), this.getId(), this.getName(), result });
									}
								}
							}
						}
					}
				}
			}
		}
//		{
//			// 通知仙府
//			Cave cave = FaeryManager.getInstance().getCave(this);
//			if (cave != null) {
//				cave.setOwnerGrade(this.getLevel());
//			}
//		}
		{
			// 通知元神变化
			PLAYER_SOUL_CHANGE_RES res = new PLAYER_SOUL_CHANGE_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, this.currSoul);
			this.addMessageToRightBag(res);
		}
		{
			// 统计
			if (this.getCurrSoul().getSoulType() == Soul.SOUL_TYPE_BASE) {// 是本尊
				AchievementManager.getInstance().record(this, RecordAction.等级, getCurrSoul().getGrade());
			} else {
				AchievementManager.getInstance().record(this, RecordAction.元神等级, getCurrSoul().getGrade());
			}
		}
		
		if(getLevel() == 10){
			handleLoginReward("升级");
		}
		
		if (this.getLevel() >= MasterPrenticeManager.FINISH_PRENTICE) {
			// 出师
			MasterPrenticeManager.getInstance().chushi(this);
		} else {
			MasterPrenticeManager.logger.warn("[等级不到不出师] [" + this.getLogString() + "] [level:" + this.getLevel() + "]");
		}
		CoreSubSystem.getInstance().sendInfoToLeHiHi(this,2);
		ActivityManagers.getInstance().sendPlayerInfoToUC(this,3);
			// 给网关服务器发送消息，更新用户信息
			NEW_MIESHI_UPDATE_PLAYER_INFO update_player_info = new NEW_MIESHI_UPDATE_PLAYER_INFO(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), username, getId(), name, this.getSoulLevel(), this.getCurrSoul().getCareer(), (int) getRMB(), getVipLevel(), 2);
			MieshiGatewayClientService.getInstance().sendMessageToGateway(update_player_info);
		if (GamePlayerManager.logger.isInfoEnabled()) GamePlayerManager.logger.info("[玩家升级] [{}] {}", new Object[] { this.getLogString(), this.getOtherSoulLogString() });
	}

	public void 封印成就(int level) {
		CountryManager cm = CountryManager.getInstance();
		if (cm != null) {
			// 全服第一名到达70级封印玩家
			boolean has = false;
			for (Country country : cm.countryMap.values()) {
				if (country.first_70_playerId > 0) {
					has = true;
				}
			}
			if (!has && level == 70) {
				Country country = cm.getCountryByCountryId(this.getCountry());
				if (country != null) {
					country.setFirst_70_playerId(this.getId());
				}
				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(this, RecordAction.七十不算容易, 70);
				}
			}

			// 全服第一名到达110级封印玩家
			has = false;
			for (Country country : cm.countryMap.values()) {
				if (country.first_110_playerId > 0) {
					has = true;
				}
			}
			if (!has && level == 110) {
				Country country = cm.getCountryByCountryId(this.getCountry());
				if (country != null) {
					country.setFirst_110_playerId(this.getId());
				}
				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(this, RecordAction.一百一要用点时间, 110);
				}
			}

			// 全服第一名到达190级封印玩家
			has = false;
			for (Country country : cm.countryMap.values()) {
				if (country.first_180_playerId > 0) {
					has = true;
				}
			}
			if (!has && level == 190) {
				Country country = cm.getCountryByCountryId(this.getCountry());
				if (country != null) {
					country.setFirst_180_playerId(this.getId());
				}
				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(this, RecordAction.一百八要花大功夫, 190);
				}
			}
			if (level == 260) {
				{ // 本国第一个达到260级的玩家
					Country country = cm.getCountryByCountryId(this.getCountry());
					if (country != null && country.getFirst_260_playerId() <= 0) {
						country.setFirst_260_playerId(this.getId());
						if (this.getCountry() == 1) {
							AchievementManager.getInstance().record(this, RecordAction.昆仑第一个到达仙40的玩家, 260);
						}
						if (this.getCountry() == 2) {
							AchievementManager.getInstance().record(this, RecordAction.九州第一个到达仙40的玩家, 260);
						}
						if (this.getCountry() == 3) {
							AchievementManager.getInstance().record(this, RecordAction.万法第一个到达仙40的玩家, 260);
						}
					}
				}
			}

			if (level == 190) {
				// 本国第一个到达190级封印的玩家
				{
					Country country = cm.getCountryByCountryId(this.getCountry());
					if (country != null && country.first_180_playerId <= 0) {
						country.setFirst_180_playerId(this.getId());

						if (AchievementManager.getInstance() != null) {
							if (this.getCountry() == 1) {
								AchievementManager.getInstance().record(this, RecordAction.昆仑我第一, 190);
							}
							if (this.getCountry() == 2) {
								AchievementManager.getInstance().record(this, RecordAction.九州我第一, 190);
							}
							if (this.getCountry() == 3) {
								AchievementManager.getInstance().record(this, RecordAction.万法我第一, 190);
							}
						}
					}
				}

				if (this.getMainSoul() != null) {
					if (this.getMainSoul().getCareer() == 1) {
						// 全服第一名到达190级封印修罗玩家
						has = false;
						for (Country country : cm.countryMap.values()) {
							if (country.first_douluo_playerId > 0) {
								has = true;
							}
						}
						if (!has) {
							Country country = cm.getCountryByCountryId(this.getCountry());
							if (country != null) {
								country.setFirst_douluo_playerId(this.getId());
							}
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(this, RecordAction.三界最快修罗, 190);
							}
						}
					} else if (this.getMainSoul().getCareer() == 2) {
						// 全服第一名到达190级封印影魅玩家
						has = false;
						for (Country country : cm.countryMap.values()) {
							if (country.first_guisha_playerId > 0) {
								has = true;
							}
						}
						if (!has) {
							Country country = cm.getCountryByCountryId(this.getCountry());
							if (country != null) {
								country.setFirst_guisha_playerId(this.getId());
							}
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(this, RecordAction.三界最快影魅, 190);
							}
						}
					} else if (this.getMainSoul().getCareer() == 3) {
						// 全服第一名到达190级封印仙心玩家
						has = false;
						for (Country country : cm.countryMap.values()) {
							if (country.first_lingzun_playerId > 0) {
								has = true;
							}
						}
						if (!has) {
							Country country = cm.getCountryByCountryId(this.getCountry());
							if (country != null) {
								country.setFirst_lingzun_playerId(this.getId());
							}
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(this, RecordAction.三界最快仙心, 190);
							}
						}
					} else if (this.getMainSoul().getCareer() == 4) {
						// 全服第一名到达190级封印九黎玩家
						has = false;
						for (Country country : cm.countryMap.values()) {
							if (country.first_wuhuang_playerId > 0) {
								has = true;
							}
						}
						if (!has) {
							Country country = cm.getCountryByCountryId(this.getCountry());
							if (country != null) {
								country.setFirst_wuhuang_playerId(this.getId());
							}
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(this, RecordAction.三界最快九黎, 190);
							}
						}
					} else if (this.getMainSoul().getCareer() == 5) {
						has = false;
						for (Country country : cm.countryMap.values()) {
							if (country.getFirst_shoukui_playerId() > 0) {
								has = true;
							}
						}
						if (!has) {
							Country country = cm.getCountryByCountryId(this.getCountry());
							if (country != null) {
								country.setFirst_shoukui_playerId(this.getId());
							}
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(this, RecordAction.三界最快兽魁, 190);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 判断是否死亡，此标记只是标记是否死亡，比如HP = 0 不同于LivingObject的alive标记。
	 * alive标记用于是否要将生物从游戏中清除。死亡不代表要清除。
	 * 
	 * @return
	 */
	public boolean isDeath() {
		return (this.hp <= 0 && state == STATE_DEAD);
	}

	/**
	 * 给定一个fighter，返回是敌方，友方，还是中立方。
	 * 
	 * 0 表示敌方 1 表示中立方 2 表示友方
	 * 
	 * @param fighter
	 * @return
	 */
	public int getFightingType(Fighter fighter) {

		if (fighter == this) return FIGHTING_TYPE_FRIEND;

		if (fighter instanceof Monster) {
			return FIGHTING_TYPE_ENEMY;
		}

		// 禁止PVP地图
		if (getCurrentGame() != null && this.getCurrentGame().gi.isLimitPVP()) {
			return FIGHTING_TYPE_NEUTRAL;
		}

		if (this.transientEnemyList.contains(fighter)) {
			return FIGHTING_TYPE_ENEMY;
		}
		//
		// if(this.isSameTeam(fighter)){
		// return FIGHTING_TYPE_FRIEND;
		// }
		//
		if (this.isInBattleField()) {
			if (this.getBattleFieldSide() == fighter.getBattleFieldSide()) {
				return FIGHTING_TYPE_FRIEND;
			} else if (fighter.getBattleFieldSide() == BattleField.BATTLE_SIDE_C) {
				return FIGHTING_TYPE_NEUTRAL;
			} else if (this.getBattleFieldSide() == BattleField.BATTLE_SIDE_C) {
				return FIGHTING_TYPE_NEUTRAL;
			} else {
				return FIGHTING_TYPE_ENEMY;
			}
		}


		if (fighter instanceof Sprite) {
			Sprite s = (Sprite) fighter;
			if (s.getSpriteType() == Sprite.SPRITE_TYPE_MONSTER) {
				return FIGHTING_TYPE_ENEMY;
			} else if (s.getSpriteType() == Sprite.SPRITE_TYPE_PET) {
				Pet pet = (Pet) s;
				if (pet.getMaster() != null) {
					return getFightingType(pet.getMaster());
				} else {
					return FIGHTING_TYPE_NEUTRAL;
				}
			} else if (s instanceof BiaoCheNpc) {
				BiaoCheNpc biaoche = (BiaoCheNpc) s;
				PlayerManager pm = PlayerManager.getInstance();
				if (biaoche.getGrade() < 0) {// 不让打
					return FIGHTING_TYPE_NEUTRAL;
				}
				if (pm != null) {
					try {
						Player owner = pm.getPlayer(biaoche.getOwnerId());
						return getFightingType(owner);
					} catch (Exception ex) {

					}
				}
				return FIGHTING_TYPE_NEUTRAL;

			} else if (s.getCountry() == GameConstant.中立阵营) {
				return FIGHTING_TYPE_NEUTRAL;
			} else if (s.getCountry() != this.getCountry()) {
				return FIGHTING_TYPE_NEUTRAL;
			}
		} else if (fighter instanceof Player) {

			Player p = (Player) fighter;

			if (p.country != country) {
				if (pkMode == 和平模式) {
					return FIGHTING_TYPE_NEUTRAL;
				} else {
					return FIGHTING_TYPE_ENEMY;
				}
			} else {
				if (pkMode == 全体模式) {
					return FIGHTING_TYPE_ENEMY;
				} else if (pkMode == 组队模式) {
					Player[] ps = this.getTeamMembers();
					if (ps != null) {
						for (int i = 0; i < ps.length; i++) {
							if (p == ps[i]) {
								return FIGHTING_TYPE_FRIEND;
							}
						}
					}
					return FIGHTING_TYPE_ENEMY;
				} else if (pkMode == 家族模式) {
					if (this.getJiazuId() <= 0) {
						return FIGHTING_TYPE_ENEMY;
					} else if (this.getJiazuId() != p.getJiazuId()) {
						return FIGHTING_TYPE_ENEMY;
					}
				} else if (pkMode == 宗派模式) {
					if (this.getJiazuId() <= 0 || p.getJiazuId() <= 0) {
						return FIGHTING_TYPE_ENEMY;
					}
					if (this.getJiazuId() != p.getJiazuId()) {
						if (this.getZongPaiName() == null || this.getZongPaiName().equals("") || p.getZongPaiName() == null || p.getZongPaiName().equals("") || !this.getZongPaiName().equals(p.getZongPaiName())) {
							return FIGHTING_TYPE_ENEMY;
						}
					}
				} else if (pkMode == 善恶模式) {
					if (p.getEvil() > 0) {
						return FIGHTING_TYPE_ENEMY;
					}
				} else if (pkMode == 国家模式) {

				}
			}
		}

		return FIGHTING_TYPE_FRIEND;
	}

	public static int 复活cd时间 = 300000;
	public static int 复活基本银子 = 20000;

	/**
	 * 尝试玩家是否能原地复活
	 * 
	 * 返回0标识能，并且复活 返回1标识没有复活卡 返回2标识未到时间，不能复活 返回3标识其他错误
	 */
	public int tryToRevived(String revivePropsName) {

		long now = System.currentTimeMillis();
		if (now - lastRevivedTime >= 复活cd时间) {
			revivedCount = 0;
		}
		int count = getKnapsack_common().countArticle("原地复活令");
		if(count > 0){
			removeArticle("原地复活令", "原地删除");
			sendError("消耗了原地复活令x1");
		}else{
			int cost = reviCostYinzi(revivedCount);
			BillingCenter bc = BillingCenter.getInstance();
			try {
				bc.playerExpense(this, cost, CurrencyType.BIND_YINZI, ExpenseReasonType.REVIVE, "原地复活");
				// 活跃度统计
				ActivenessManager.getInstance().record(this, ActivenessType.原地复活);
				// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),
				// (byte) 1, Translate.原地复活消耗50两绑银);
				// this.addMessageToRightBag(hreq);
			} catch (Exception ex) {
				return 1; 
			}
			revivedCount++;
			lastRevivedTime = now;
			try {
				if (getMaxCanUseBindSilver() <= 0) {
					this.sendError(String.format(Translate.剩余可使用绑银, 0 + Translate.文));
				} else {
					this.sendError(String.format(Translate.剩余可使用绑银, BillingCenter.得到带单位的银两(getMaxCanUseBindSilver())));
				}
			} catch (Exception e) {
			}
		}
		
		setHp(getMaxHP());
		setMp(getMaxMP());
		setState(STATE_STAND);
		if (AchievementManager.getInstance() != null) {
			AchievementManager.getInstance().record(this, RecordAction.原地复活次数, 1);
		}
		return 0;
	}

	public int reviCostYinzi(int count) {
		int c = 0;
		if (count <= 24) {
			c = count;
		} else {
			c = 24;
		}

		return (c + 1) * 复活基本银子;
	}

	public long getLastRevivedTime() {
		return lastRevivedTime;
	}

	/**
	 * 通知玩家从死亡状态复活
	 */
	public void notifyRevived() {

		setFighting(false);
		// clearTransientEnemy();
		// enemyList.enemyList.clear();
		deathNotify = false;
	}

	public void 原地复活数据设置() {
		lastRevivedTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		revivedCount += 1;
	}

	/**
	 * 玩家死了
	 */
	public void killed() {
		String map = this.getGame();
		if (map == null) {
			map = this.lastGame;
		}
		if (map == null) {
			map = "";
		}
		try {
			if(DownCityManager2.instance.cityMap.containsKey(new Long(getId()))){
				DownCityManager2.instance.cityMap.get(new Long(getId())).killPlayer(this);
			}
			ChestFightManager.inst.notifyPlayerDead(this);
			FairyRobberyManager.inst.notifyPlayerDeath(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendPlayerAction(this, PlayerActionFlow.行为类型_死亡, map, 1, new Date(), true);

		// 副本统计
		Game game = this.currentGame;
		if (game != null && game.getDownCity() != null) {
			DownCity dc = game.getDownCity();
			dc.statPlayerDead(this);
		}
		this.resetShouStat("玩家死亡");
		// 玩家死亡统计
		String sessionId = StringUtil.randomIntegerString(15);
		CareerManager cm = CareerManager.getInstance();
		AttackRecord ars[] = this.attackRecordList.toArray(new AttackRecord[0]);
		attackRecordList.clear();
		for (int i = 0; i < ars.length; i++) {
			AttackRecord a = ars[i];
			if (attackRecordLogger.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer();
				if (a.living instanceof Player) {
					Player p = (Player) a.living;
					sb.append("[" + p.getUsername() + "] [" + p.getName() + "] [" + cm.getCareer(p.getCareer()).getName() + "] [lv:" + p.getLevel() + "]");
				} else if (a.living instanceof Pet) {
					Pet pet = (Pet) a.living;
					Player p = pet.getMaster();
					if (p != null) {
						sb.append("[" + p.getUsername() + "] [" + p.getName() + "] [" + cm.getCareer(p.getCareer()).getName() + "] [lv:" + p.getLevel() + "]");
					}
					sb.append(" [" + pet.getName() + "] [" + pet.getId() + "]");
				}
				if (attackRecordLogger.isDebugEnabled()) attackRecordLogger.debug("[{}] [角色被攻击统计] [{}] [{}] [{}] [lv:{}] [hp:{}/{}] [ms:{}] -- [{}ms] {} [ma:{}] [sa:{}] [{}] [{}] [{}] [{}]", new Object[] { sessionId, this.getUsername(), this.getName(), cm.getCareer(getCareer()).getName(), this.getLevel(), a.hp, this.getMaxHP(), (heartBeatStartTime - this.fightingStartTime), (a.createTime - this.fightingStartTime), sb, a.meleeAttackIntensity, a.spellAttackIntensity, a.skillName, a.skillLevel, Fighter.DAMAGETYPE_NAMES[a.damageType], a.damage });
			}
		}

		if (attackRecordLogger.isWarnEnabled()) attackRecordLogger.warn("[{}] [角色死亡] [{}] [{}] [{}] [lv:{}] [hp:{}/{}] [ms:{}]", new Object[] { sessionId, this.getUsername(), this.getName(), cm.getCareer(getCareer()).getName(), this.getLevel(), this.getHp(), this.getMaxHP(), (heartBeatStartTime - this.fightingStartTime) });

		this.stopAndNotifyOthers(Game.REASON_PLAYER_BEKILLED, "");
		this.activeSkillAgent.breakExecutingByDead();

		setFighting(false);

		// clearTransientEnemy();
		this.setState(Player.STATE_DEAD);
		enemyList.enemyList.clear();

		if (ArticleManager.logger.isWarnEnabled()) {
			ArticleManager.logger.warn("[玩家死亡清buff之前] " + getPlayerPropsString());
		}

		synchronized (buffs) {
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);

				if (buff != null && buff.getTemplate() != null) {
					if (buff.getTemplate().getTimeType() == 2 || !buff.getTemplate().deadNotdisappear) {
						buff.end(this);
						if (buff.isForover() || buff.isSyncWithClient()) {
							this.removedBuffs.add(buff);
						}
						buffs.remove(i);
						setDirty(true, "buffs");

						if (ActiveSkill.logger.isDebugEnabled()) {
							ActiveSkill.logger.debug("[死亡去除BUFF] [{}] [死亡] [{}:{}] [time:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime() });
						}

					}
				}
			}
		}

		this.closeAura();

		this.downFromHorse();

		// 玩家死亡声音广播
		{
			if (game != null) {
				String soundName = SoundManager.getPlayerDeadSound(this);
				Fighter[] fs = game.getVisbleFighter(this, true);
				PLAY_SOUND_REQ psreq = new PLAY_SOUND_REQ(GameMessageFactory.nextSequnceNum(), soundName, true, false);
				if (fs != null) {
					for (Fighter f : fs) {
						if (f instanceof Player) {
							((Player) f).addMessageToRightBag(psreq);
						}
					}
				}
			}
		}
		if (this.isInBattleField()) {
			// 通知战场，玩家死亡
			battleField.playerDead(this);

		} else {
			FightingPlaceManager fpm = FightingPlaceManager.getInstance();
			if (fpm != null && fpm.isInFightingPlace(this)) {

				FightingPlace fp = fpm.getFightingPlaceByPlayer(this);

				WindowManager wm = WindowManager.getInstance();
				MenuWindow mw = wm.createTempMenuWindow(3600000);
				mw.setDescriptionInUUB("");
				Option_FightingPlaceRecurrention option = new Option_FightingPlaceRecurrention();
				option.setFightingPlace(fp);
				mw.setOptions(new Option[] { option });

			} else {

				this.ecs.beKilled();

				// 封印副本死亡不通知
				boolean notifyDead = true;
				if (game != null) {
					if (game.gi != null) {
						if (SealManager.getInstance().isSealDownCity(game.gi.name)) {
							notifyDead = false;
						}
					}
				}

				if (notifyDead) {
					PLAYER_DEAD_REQ req = new PLAYER_DEAD_REQ(GameMessageFactory.nextSequnceNum(), this.getId(), 0);
					this.addMessageToRightBag(req);
				} else {
					// 封印副本，不弹复活框
					boolean isHasBuff = false;
					List<Buff> buffs = this.getAllBuffs();
					if (buffs != null) {
						for (Buff buff : buffs) {
							if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName())) {
								isHasBuff = true;
							}
						}
					}

					if (isHasBuff == false) {
						this.setTransferGameCountry(this.getCountry());
						this.setHp(this.getMaxHP() / 2);
						this.setMp(this.getMaxMP() / 2);
						this.setState(Player.STATE_STAND);
						this.notifyRevived();
						PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_1498, this.getHp(), this.getMp());
						this.addMessageToRightBag(res);
						this.sendError(Translate.boss挑战失败);
						if (Game.logger.isWarnEnabled()) {
							Game.logger.warn("[封印副本线程] 【{}】 [{}]", new Object[] { "玩家死亡,挑战失败2", this.getLogString() });
						}
						game.transferGame(this, new TransportData(0, 0, 0, 0, this.getResurrectionMapName(), this.getResurrectionX(), this.getResurrectionY()));
					}

				}
			}
		}
		if (this.timerTaskAgent != null) {
			this.timerTaskAgent.notifyDead();
		}
	}

	/**
	 * 发送错误提示
	 * 
	 * @param content
	 */
	public void sendError(String content) {
		send_HINT_REQ(content, (byte) 5);
	}

	/**
	 * 发送提示
	 * 
	 * @param content
	 */
	public void sendNotice(String content) {
		send_HINT_REQ(content, (byte) 1);
	}

	/**
	 * 发送系统消息
	 * @param content
	 */
	public void sendWinNotice(String content) {
		send_HINT_REQ(content, (byte) 0);
	}

	/**
	 * 发送给玩家信息
	 * 
	 * @param content
	 * @param messageType
	 */
	public void send_HINT_REQ(String content, byte messageType) {
		HINT_REQ hint_REQ = new HINT_REQ(GameMessageFactory.nextSequnceNum(), messageType, content);
		this.addMessageToRightBag(hint_REQ);
	}

	/** 上次移动时间 */
	private transient long lastMoveTime = 0;

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (isStun() || isIceState()) {
			if (getMoveTrace() != null) {
				stopAndNotifyOthers(Game.REASON_WRONG_PATH, "眩晕或冰冻清除路径");
			}
		}
		super.heartbeat(heartBeatStartTime, interval, game);

		// 通知玩家第一次进入游戏，此消息在用户进入游戏2秒后触发
		if (recentlyCreatedButNotEnterGameFlag && heartBeatStartTime - recentlyCreatedButNotEnterGameTime > 2000) {
			recentlyCreatedButNotEnterGameFlag = false;

			this.firstTimeOnlineFlag = true;
			this.firstTimeOnlineFlagStartTime = heartBeatStartTime;
			for (int i = 0; i < firstTimeOnlineFlagForMinutes.length; i++) {
				firstTimeOnlineFlagForMinutes[i] = true;
			}
			this.notifyPlayerFirstEnterGame();
			String channelName = "";
			String channelKey = "";
			if (passport != null) {
				channelName = passport.getRegisterChannel();
				channelKey = passport.getRegisterChannel();
			}
			if (SpriteSubSystem.playerInfoLog.isInfoEnabled()) {
				SpriteSubSystem.playerInfoLog.info("[第一次进入游戏] [帐号：{}] [渠道：{}] [渠道ID：{}] [姓名：{}] [ID：{}] [性别：{}] [门派：{}] [等级：{}] [在线时间：{}]", new Object[] { this.getUsername(), channelName, channelKey, this.getName(), this.getId(), (this.getSex() == 0 ? "男" : "女"), CareerManager.careerNames[this.getCareer()], this.getLevel(), (this.quitGameTime - this.loginServerTime) });
			}
		}

		if (getHp() <= 0 && getState() != Player.STATE_DEAD) {
			setState(Player.STATE_DEAD);
			killed();
		}
		// 通知队伍个人的变化
		noticeTeamChange();

		if (this.getState() == Player.STATE_DEAD) {
			if (this.isFighting()) {
				setFighting(false);
			}
			return;
		}

		if (heartBeatStartTime - lastCheckIllegalOperationTime > 5000) {
			lastCheckIllegalOperationTime = heartBeatStartTime;
			check_HEARTBEAT_CHECK_REQ();

		}

		if (transientEnemyList.isEmpty() == false) {
			synchronized (transientEnemyList) {
				Iterator<Fighter> it = transientEnemyList.iterator();
				while (it.hasNext()) {
					Fighter f = it.next();
					boolean remove = false;
					if (f.isDeath()) {
						remove = true;
						it.remove();
					}
					// 如果f为人或宠物，那么死亡或时间超过30秒就清楚
					if (f instanceof Player || f instanceof Pet) {
						if (!remove) {
							Long time = transientEnemyPlayerAttackTime.get(f.getId());
							if (time != null && heartBeatStartTime - time >= 28000) {
								remove = true;
								it.remove();
							}
						}
						if (remove) {
							transientEnemyPlayerAttackTime.remove(f.getId());
						}
					}
					if (remove) {
						TRANSIENTENEMY_CHANGE_REQ req = new TRANSIENTENEMY_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, (f instanceof Player ? (byte) 0 : (byte) 1), f.getId());
						this.addMessageToRightBag(req);
					}
				}
			}
		}

		// 主动技能
		activeSkillAgent.heartbeat(game);

		// 更新敌人列表
		enemyList.flush(game, heartBeatStartTime);

		if (enemyList.isEmpty() && this.isFighting() == true) {
			this.setFighting(false);
			attackRecordList.clear();
		}

		if (!enemyList.isEmpty() && this.isFighting() == false) {
			fightingStartTime = heartBeatStartTime;
			this.setFighting(true);
		}

		// buff, 1s心跳一次
		if (heartBeatStartTime - lastTimeForBuffs > 500) {
			lastTimeForBuffs = heartBeatStartTime;
			synchronized (buffs) {
				for (int i = buffs.size() - 1; i >= 0; i--) {
					Buff buff = buffs.get(i);
					if (buff != null) {

						// 玩家不在线，不算游戏时间
						if (buff.getTemplate() != null && buff.getTemplate().getTimeType() == 0 && this.isOnline() == false) continue;

						if (buff.getInvalidTime() <= heartBeatStartTime) {
							buff.end(this);
							if (buff.isForover() || buff.isSyncWithClient()) {
								this.removedBuffs.add(buff);
							}
							buffs.remove(buff);
							setDirty(true, "buffs");
							if (VipManager.vipBuffMap.containsKey(buff.getTemplateName())) {
								this.setVipLevel(this.getVipLevel());
								if (VipManager.getInstance() != null) {
									VipManager.getInstance().设置玩家的vip属性(this);
								}
							}
							if (ActiveSkill.logger.isDebugEnabled()) {
								ActiveSkill.logger.debug("[Buff消失] [{}] [心跳前时间到了] [{}:{}] [time:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime() });
							}
						} else {
							buff.heartbeat(this, heartBeatStartTime, interval, game);
							if (buff.getInvalidTime() <= heartBeatStartTime) {
								buff.end(this);

								if (buff.isForover() || buff.isSyncWithClient()) {
									this.removedBuffs.add(buff);
								}
								buffs.remove(buff);
								setDirty(true, "buffs");
								if (VipManager.vipBuffMap.containsKey(buff.getTemplateName())) {
									this.setVipLevel(this.getVipLevel());
									if (VipManager.getInstance() != null) {
										VipManager.getInstance().设置玩家的vip属性(this);
									}
								}
								if (ActiveSkill.logger.isDebugEnabled()) {
									ActiveSkill.logger.debug("[Buff消失] [{}] [心跳后时间到了] [{}:{}] [time:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime() });
								}
							}
						}
					}
				}
			}

			// 光环技能
			auraSkillAgent.heartbeat(heartBeatStartTime, interval, game);

		}

		if (heartBeatStartTime - lastTimeForRecoverHPAAndMPA > 5000) {
			lastTimeForRecoverHPAAndMPA = heartBeatStartTime;
			int hpa = this.getHpRecoverBase();
			int mpa = this.getMpRecoverBase();
			// 是否要考虑越界的问题？
			if ((hpa > 0 && getHp() < getMaxHP()) || (hpa < 0 && getHp() > 0)) {
				if (isCanNotIncHp()) {
					if (Skill.logger.isDebugEnabled()) Skill.logger.debug("[无法回血状态] [屏蔽魂石心跳中回血] [" + getLogString() + "] [血]");
				} else {
					this.setHp(getHp() + hpa);
				}
			}
			if ((mpa > 0 && getMp() < getMaxMP()) || (mpa < 0 && getMp() > 0)) {
				this.setMp(getMp() + mpa);
			}
			try {

				if (this.getLevel() <= 220) {
					Game cg = this.getCurrentGame();
					if (cg != null && RobberyConstant.没飞升玩家不可进入的地图.contains(cg.gi.name)) {
						String mName = TransportData.getMainCityMap(this.getCountry());
						cg.transferGame(this, new TransportData(0, 0, 0, 0,mName, 3174, 1101));
						Game.logger.warn("[没有飞升的玩家进入仙界地图] [自动传送回王城] [" + this.getLogString() + "] [地图:" + cg.gi.name + "]");
					}
				}
			} catch (Exception e) {

			}
		}

		if (heartBeatStartTime - lastTimeForRecoverHPBAndMPB > 500) {
			lastTimeForRecoverHPBAndMPB = heartBeatStartTime;
			int hpa = this.getHpRecoverExtend();
			int mpa = this.getMpRecoverExtend();
			// 是否要考虑越界的问题？
			if ((hpa > 0 && getHp() < getMaxHP()) || (hpa < 0 && getHp() > 0)) {
				this.setHp(getHp() + hpa);
			}
			if ((mpa > 0 && getMp() < getMaxMP()) || (mpa < 0 && getMp() > 0)) {
				this.setMp(getMp() + mpa);
			}
		}
//		if (heartBeatStartTime - lastTimeForCave > 10000) {
//			lastTimeForCave = heartBeatStartTime;
//			updateLastOnlineTimeForCave(heartBeatStartTime);
//		}
		// 收集背包里物品，检查所有物品有效期
		if (heartBeatStartTime - lastTimeForArticeValidTime > ONE_MINUTES) {
			lastTimeForArticeValidTime = heartBeatStartTime;

			collectKnapsackArticleAndSetInvalidTimeAndNotifyClient();

			try {
				List<Byte> tempList = new ArrayList<Byte>();
				Iterator<Byte> ite = attackBiaoCheFlag.keySet().iterator();
				while (ite.hasNext()) {
					byte key1 = ite.next();
					long aa = attackBiaoCheFlag.get(key1);
					if (System.currentTimeMillis() >= (aa + ONE_MINUTES)) {
						tempList.add(key1);
					}
				}
				if (tempList.size() > 0) {
					for (Byte bb : tempList) {
						attackBiaoCheFlag.remove(bb);
					}
				}
			} catch (Exception e) {
				PlayerAimManager.logger.error("[目标系统] [清除玩家攻击镖车记录] [异常] [" + this.getLogString() + "]", e);
			}

		}

		// 收集背包和装备栏的变化
		if (heartBeatStartTime - lastTimeForKnapsackAndEquipment > 1000) {
			lastTimeForKnapsackAndEquipment = heartBeatStartTime;

			collectKnapsackChangeAndNotifyClient();

			collectEquipmentColumnChangeAndNotifyClient();

			collectEquitmemtDurabilityChangeAndNotifyClient();

			collectTaskChangeAndNotifyClient();

			try { // 检查玩家是否移动
				long nn = System.currentTimeMillis();
//				if ((nn - lastMoveTime) >= DisasterConstant.refreshBossTime) {
//					lastMoveTime = nn;
//					DisasterManager.getInst().refreshMonsterInPlayerPoint(this);
//				}
			} catch (Exception e) {

			}

		}

		// 检查人物是否已经达到绿色状态(即30小时没有主动pk)
		if (heartBeatStartTime - lastTimeForNameColor > ONE_MINUTES) {
			lastTimeForNameColor = heartBeatStartTime;
			if (nameColorType == 名字颜色_白色 && heartBeatStartTime - lastActivePkTime >= 绿色名称时间) {
				setNameColorType(名字颜色_绿色);
			}
			try {
				long currentTime = System.currentTimeMillis();
				List<PlayerTitle> playerTitles = this.getPlayerTitles();
				if (playerTitles != null && playerTitles.size() > 0) {
					for (PlayerTitle pt : playerTitles) {
						if (pt.getLastTime() > 0 && currentTime >= (pt.getStartTime() + pt.getLastTime())) {
							needRemoveTitles.add(pt);
						}
					}
				}
				if (needRemoveTitles.size() > 0) {
					for (PlayerTitle pt : needRemoveTitles) {
						boolean success = this.removePersonTitle(pt.getTitleType());
						if (success) {
							this.sendError(String.format(Translate.称号到期自动删除, pt.getTitleName()));
						}
						if (SocialManager.logger.isDebugEnabled()) {
							SocialManager.logger.debug("[称号] [删除时限称号结果] [" + success + "] [" + this.getLogString() + "] [称号:" + pt + "]");
						}
					}
					needRemoveTitles.clear();
				}
			} catch (Exception e) {
				SocialManager.logger.error("[称号] [检查称号是否超过限时] [异常] [" + this.getLogString() + "]", e);
			}

		}

		if (heartBeatStartTime - lastDecreaseEvilTime > ONE_EVIL_NEED_TIME) {
			lastDecreaseEvilTime = heartBeatStartTime;
			if (evil > 0) {
				if (isInPrison) {
					// 暂定在监狱是平时的3倍
					if (evil > 5) {
						setEvil(evil - 5);
					} else {
						setEvil(0);
						setLastActivePkTime(SystemTime.currentTimeMillis());
					}
					try {
						AchievementManager.getInstance().record(this, RecordAction.监狱中消除红名时间, ONE_DEVIL_RECORD_TIME);
					} catch (Exception e) {
						PlayerAimManager.logger.error("[目标系统] [统计玩家在监狱中消除红名时间] [异常] [" + this.getLogString() + "]", e);
					}
				} else {
					setEvil(evil - 1);
					setLastActivePkTime(SystemTime.currentTimeMillis());
				}
				if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info(getLogString()+"[自动减少红名值] [减少后当前红名值:"+ getEvil() +"]");
			}
		}

		// 心跳中检查限时交付任务
		// if (heartBeatStartTime - lastTimeForLimitTimeTask > 1000) {
		// lastTimeForLimitTimeTask = heartBeatStartTime;
		// for (TaskEntity entity : allTask) {
		// if (scanedTask.contains(entity.getTaskName())) {
		// continue;
		// } else {
		// scanedTask.add(entity.getTaskName());
		// if (entity.isDeliverTimeLimit()) {
		// entity.setLeftDeliverTime(entity.getLeftDeliverTime() - 1000);
		// if (entity.getLeftDeliverTime() <= 0) {
		// if (entity.getTask().getDeliverTimeLimit() != null) {
		// int timeType = entity.getTask().getDeliverTimeLimit().getType();
		// switch (timeType) {
		// case DEILVER_LIMIT_TYPE_OVER:
		// entity.setStatus(TASK_STATUS_COMPLETE);
		// break;
		// case DEILVER_LIMIT_TYPE_DELIVER:
		// entity.setStatus(TASK_STATUS_FAILED);
		// break;
		// case DEILVER_LIMIT_TYPE_AUTO_DELIVER:
		// // 只计算经验和金钱的奖励
		// entity.setStatus(TASK_STATUS_DEILVER);
		// List<TaskPrize> list = entity.getTask().getPrizeByType(PrizeType.EXP,
		// PrizeType.BIND_SILVER);
		// for (TaskPrize prize : list) {
		// prize.doPrize(this, null);
		// }
		// break;
		// }
		// }
		// }
		// }
		// }
		// }
		//
		// scanedTask.clear();
		// }

		// 根据离线时间和可转化为经验的离线时间，计算经验
		if (heartBeatStartTime > (lastrecordQuiteGameTime + 600000)) {
			lastrecordQuiteGameTime = heartBeatStartTime;
			setQuitGameTime(heartBeatStartTime);
		}
		// 调用坐骑的心跳
		if (isUpOrDown) {
			Horse rideHorse = HorseManager.getInstance().getHorseById(this.ridingHorseId, this);
			if (rideHorse != null) {
				rideHorse.heartBeat(this);
			}
		}

		// 处理境界
		if (isZazening()) {
			// 应该跳一次了
			if (getLeftZazenTime() <= 0) {
				// 打坐结束
				setZazening(false);
				setLeftZazenTime(0);
			} else {
				if ((now - getLastGotBournExpTime()) >= BournManager.zazenCycle) {
					setLastGotBournExpTime(now);
					setLeftZazenTime(getLeftZazenTime() - BournManager.zazenCycle);
					BournCfg bournCfg = BournManager.getInstance().getBournCfg(getClassLevel());
					int expAdd = bournCfg.getZazenOnceExp();// *
					// (BournManager.zazenCycle
					// / 1000);
					addBournExp(expAdd);
					if (BournManager.logger.isDebugEnabled()) {
						BournManager.logger.debug(getLogString() + "[打坐获得经验{}][打坐后经验{}][剩余打坐时间{}]", new Object[] { expAdd, getBournExp(), getLeftZazenTime() / 1000 });
					}
				}
			}
		}

		if (heartBeatStartTime - timerTaskAgent.getLastCheckTime() > 500) {
			timerTaskAgent.heartbeatCheck(heartBeatStartTime);
		}

		if (this.duelFieldState > 0) {
			if (!(this.battleField instanceof TournamentField)) {
				this.duelFieldState = 0;
			}
		}

		// 检查玩家身上的限时坐骑
		if (heartBeatStartTime - lastCheckLimitHorseTime >= 60000) {
			if (activeMagicWeaponId > 0) {
				try {
					if (getEquipmentColumns().get(11) != null) {
						NewMagicWeaponEntity entity = (NewMagicWeaponEntity) getEquipmentColumns().get(11);
						MagicWeaponManager.instance.addWearTime(this, entity);
					}
				} catch (Exception e) {
					MagicWeaponManager.logger.error("[扣除法宝耐久报错][" + this.getLogString() + "] ", e);
				}
			}

			lastCheckLimitHorseTime = heartBeatStartTime;
			ArrayList<Long> horseList = this.getHorseIdList();
			if (horseList != null) {
				for (int i = 0; i < horseList.size(); i++) {
					Horse horse = HorseManager.getInstance().getHorseById(horseList.get(i), this);
					if (horse != null) {
						horse.checkHorseTime(heartBeatStartTime);
					}
				}
			}
		}
		if (heartBeatStartTime - lastOnlineActivityTime >= 1000) {
			lastOnlineActivityTime = heartBeatStartTime;
			ActivityManager.getInstance().onlineActivity(this, heartBeatStartTime);
		}

		updateShouKuiDou();
		FlyTalentManager.getInstance().endTalentSkillEffect(this);
//		SealManager.getInstance().noticeSealTaskStat(this);
		HunshiManager.getInstance().dealWithBadBuff(this, null);

		// 玩家没下线，活动开始，给发邮件
		// 活动开始，玩家持续游戏没下线，给发邮件
		// if(PlatformManager.getInstance().isPlatformOf(Platform.台湾) &&
		// LoginActivityManager.getInstance().isOpen()){
		// if
		// (LoginActivityManager.notOpenServers.contains(GameConstants.getInstance().getServerName()))
		// {
		//
		// } else {
		// DefaultDiskCache ddc = LoginActivityManager.getInstance().getDdc();
		// if(ddc!=null){
		// LoginStatDate lsdd = (LoginStatDate)ddc.get(this.getId()+"login");
		// if(lsdd!=null){
		// long lastlogintime = lsdd.getLasttime();
		// if(LoginActivityManager.getContinueLoginDays(lastlogintime,System.currentTimeMillis())==1){
		// LoginActivityManager.getInstance().updateLoginContinu(this);
		// }}
		// }
		// }
		// }
//		XianLingManager.instance.resumeEnergy(this);
		try {
			if(!JiazuManager2.instance.isOpen()){
				if(this.getJiazuId() > 0){
					Jiazu jiazu = JiazuManager.getInstance().getJiazu(this.getJiazuId());
					if(jiazu.isHasKillBoss()){
						jiazu.setCity(null);
						jiazu.setHasKillBoss(false);
						Game.logger.warn("玩家清空副本:"+getLogString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private transient long lastOnlineActivityTime;
	private transient long lastCheckLimitHorseTime;

	/**
	 * 记录玩家登陆是否召唤了宠物 true(没有召唤 会心跳)
	 */
	// private transient boolean noSummonPet = true;

	public void addBournExp(int expAdd) {
		if (expAdd > 0) {
			BournCfg bournCfg = BournManager.getInstance().getBournCfg(getClassLevel());
			int expResult = getBournExp() + expAdd;
			expResult = (expResult > bournCfg.getExp()) ? bournCfg.getExp() : expResult;
			try {
				if (expResult >= bournCfg.getExp()) {
					NOTICE_PARTICLE_REQ req = new NOTICE_PARTICLE_REQ(GameMessageFactory.nextSequnceNum(), 1, 7);
					this.addMessageToRightBag(req);
				}
			} catch (Exception e) {

			}
			setBournExp(expResult);
		}
	}

	public TimerTaskAgent getTimerTaskAgent() {
		return timerTaskAgent;
	}

	public CountdownAgent getCountdownAgent() {
		return countdownAgent;
	}

	transient long lastAdvertisingTime;

	/**
	 * 离线时间转化为经验
	 */
	private void exchangeExp(long time) {
		int exp = 0;
		// TODO 需要有离线时间转化经验的公式
		exp = (int) (time / 1000);
		addExp(exp, ExperienceManager.ADDEXP_REASON_BUYING);
	}

	/**
	 * 收集任务的进度变化
	 */
	protected void collectTaskChangeAndNotifyClient() {
	}

	/**
	 * 搜集装备耐久的变化并且通知客户端
	 */
	protected void collectEquitmemtDurabilityChangeAndNotifyClient() {
	}

	/**
	 * 搜集装备栏的变化，并且通知客户端
	 */
	protected void collectEquipmentColumnChangeAndNotifyClient() {
		byte[] flags = ecs.getChangeFlags();
		for (int j = 0; j < flags.length; j++) {
			if (flags[j] != 0) {
				ArticleEntity ee = ecs.get(j);
				if (ee != null) {
					NOTIFY_EQUIPMENT_TABLECHANGE_REQ req = new NOTIFY_EQUIPMENT_TABLECHANGE_REQ(GameMessageFactory.nextSequnceNum(), getCurrSoul().getSoulType(), (short) j, ee.getId());
					this.addMessageToRightBag(req);
				} else {
					NOTIFY_EQUIPMENT_TABLECHANGE_REQ req = new NOTIFY_EQUIPMENT_TABLECHANGE_REQ(GameMessageFactory.nextSequnceNum(), getCurrSoul().getSoulType(), (short) j, -1);
					this.addMessageToRightBag(req);
				}
			}
		}
		ecs.clearChangeFlags();

		if (getUnusedSoul() != null) {
			for (Soul soul : getUnusedSoul()) {
				EquipmentColumn ecc = soul.getEc();
				byte[] flags1 = ecc.getChangeFlags();
				for (int j = 0; j < flags1.length; j++) {
					if (flags1[j] != 0) {
						ArticleEntity ee = ecc.get(j);
						if (ee != null) {
							NOTIFY_EQUIPMENT_TABLECHANGE_REQ req = new NOTIFY_EQUIPMENT_TABLECHANGE_REQ(GameMessageFactory.nextSequnceNum(), soul.getSoulType(), (short) j, ee.getId());
							this.addMessageToRightBag(req);
						} else {
							NOTIFY_EQUIPMENT_TABLECHANGE_REQ req = new NOTIFY_EQUIPMENT_TABLECHANGE_REQ(GameMessageFactory.nextSequnceNum(), soul.getSoulType(), (short) j, -1);
							this.addMessageToRightBag(req);
						}
					}
				}
				ecc.clearChangeFlags();
			}
		}

	}

	/**
	 * 2013-07-05 玩家背包是否存在某个物品
	 * 
	 * @return
	 */
	public boolean articleIsExist(String articleName) {
		Knapsack kp = this.getKnapsack_common();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (kp != null) {
			Cell[] cells = kp.getCells();
			if (cells != null && cells.length > 0) {
				for (int i = 0; i < cells.length; i++) {
					Cell cell = cells[i];
					if (cell.getEntityId() != -1) {
						ArticleEntity ae = aem.getEntityInCache(cell.getEntityId());
						if (ae != null && ae.getArticleName().equals(articleName.trim())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 2013-07-05 删除某个格子的一个物品
	 * 
	 * @param articleName
	 * @return
	 */
	public synchronized boolean removeArticle(String articleName) {
		Knapsack kp = this.getKnapsack_common();
		if (kp != null) {
			int count = kp.countArticle(articleName.trim());
			if (count >= 1) {
				ArticleEntity ae = kp.remove(kp.indexOf(articleName.trim()), "召唤仙友到凤栖梧桐下删除消耗品", true);
				if (ae != null) {
					// 统计
					ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "召唤仙友到凤栖梧桐下删除消耗品", null);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 搜集背包物品并设置失效时间，并通知客户端 包括玩家普通背包，防爆背包，仓库
	 */
	protected void collectKnapsackArticleAndSetInvalidTimeAndNotifyClient() {
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (am == null) {
			return;
		}
		Knapsack[] kps = knapsacks_common;
		if (kps != null) {
			for (int j = 0; j < kps.length; j++) {
				Knapsack kp = kps[j];
				if (kp != null) {
					Cell[] cells = kp.getCells();
					if (cells != null) {
						for (int i = 0; i < cells.length; i++) {
							Cell cell = cells[i];
							if (cell != null && cell.getEntityId() != -1) {
								ArticleEntity ae = aem.getEntityInCache(cell.getEntityId());
								if (ae != null && ae.getTimer() != null) {
									Article a = am.getArticle(ae.getArticleName());
									if (a != null && a.isHaveValidDays() && ae.getTimer().isClosed() && a.getInvalidAfterAction() == Article.INVALID_AFTER_DISAPPEAR) {
										// 如果此物品为坐骑，进行特殊处理
										int count = kp.clearCell(i, "到期删除", true);
										if (ae != null) {
											// 统计
											ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, count, "到期删除", null);
										}
										HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.某物品到期删除了, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
										addMessageToRightBag(error);
									}
								}
							}
						}
						cells = kp.getCells();
						ArrayList<Long> idList = new ArrayList<Long>();
						for (int i = 0; i < cells.length; i++) {
							Cell cell = cells[i];
							if (cell != null && cell.getEntityId() != -1) {
								ArticleEntity ae = aem.getEntityInCache(cell.getEntityId());
								if (ae != null && ae.getTimer() != null) {
									Article a = am.getArticle(ae.getArticleName());
									if (a != null && a.isHaveValidDays() && ae.getTimer().isClosed()) {
										idList.add(ae.getId());
									}
								}
							}
						}
						QUERY_ARTICLE_RES res = queryArticleRes(idList.toArray(new Long[0]));
						if (res != null) {
							addMessageToRightBag(res);
						}
					}
				}

			}

		}
		if (knapsacks_fangBao != null) {
			Knapsack kp = knapsacks_fangBao;
			if (kp != null) {
				Cell[] cells = kp.getCells();
				if (cells != null) {
					for (int i = 0; i < cells.length; i++) {
						Cell cell = cells[i];
						if (cell != null && cell.getEntityId() != -1) {
							ArticleEntity ae = aem.getEntityInCache(cell.getEntityId());
							if (ae != null && ae.getTimer() != null) {
								Article a = am.getArticle(ae.getArticleName());
								if (a != null && a.isHaveValidDays() && ae.getTimer().isClosed() && a.getInvalidAfterAction() == Article.INVALID_AFTER_DISAPPEAR) {
									// 如果此物品为坐骑，进行特殊处理
									int count = kp.clearCell(i, "到期删除", true);
									if (ae != null) {
										// 统计
										ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, count, "到期删除", null);
									}
									HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.某物品到期删除了, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
									addMessageToRightBag(error);
								}
							}
						}
					}
					cells = kp.getCells();
					ArrayList<Long> idList = new ArrayList<Long>();
					for (int i = 0; i < cells.length; i++) {
						Cell cell = cells[i];
						if (cell != null && cell.getEntityId() != -1) {
							ArticleEntity ae = aem.getEntityInCache(cell.getEntityId());
							if (ae != null && ae.getTimer() != null) {
								Article a = am.getArticle(ae.getArticleName());
								if (a != null && a.isHaveValidDays() && ae.getTimer().isClosed()) {
									idList.add(ae.getId());
								}
							}
						}
					}
					QUERY_ARTICLE_RES res = queryArticleRes(idList.toArray(new Long[0]));
					if (res != null) {
						addMessageToRightBag(res);
					}
				}
			}
		}
		EquipmentColumn ec = this.ecs;
		if (ec != null) {
			for (int i = 0; i < ec.getEquipmentIds().length; i++) {
				if (i == 11) {
					continue;
				}
				EquipmentEntity ee = (EquipmentEntity) ec.get(i);
				if (ee != null) {
					Article a = am.getArticle(ee.getArticleName());
					if (a != null && a.isHaveValidDays() && ee.getTimer() != null && ee.getTimer().isClosed() && a.getInvalidAfterAction() == Article.INVALID_AFTER_DISAPPEAR) {
						ec.takeOff(i, getCurrSoul().getSoulType());
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.某装备时间到期自动脱掉装备, new String[][] { { Translate.STRING_1, ee.getArticleName() } }));
						this.addMessageToRightBag(hreq);
						if (EquipmentColumn.logger.isWarnEnabled()) EquipmentColumn.logger.warn("[装备时间到期，自动脱掉装备] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { this.getUsername(), this.getId(), this.getName(), ee.getArticleName(), ee.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() });
					}
				}
			}
		}

		for (Soul soul : getUnusedSoul()) {
			if (soul != null) {
				ec = soul.getEc();
				for (int i = 0; i < ec.getEquipmentIds().length; i++) {
					if (i == 11) {
						continue;
					}
					EquipmentEntity ee = (EquipmentEntity) ec.get(i);
					if (ee != null) {
						Article a = am.getArticle(ee.getArticleName());
						if (a != null && a.isHaveValidDays() && ee.getTimer() != null && ee.getTimer().isClosed() && a.getInvalidAfterAction() == Article.INVALID_AFTER_DISAPPEAR) {
							ec.takeOff(i, soul.getSoulType());
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.某装备时间到期自动脱掉装备, new String[][] { { Translate.STRING_1, ee.getArticleName() } }));
							this.addMessageToRightBag(hreq);
							if (EquipmentColumn.logger.isWarnEnabled()) EquipmentColumn.logger.warn("[装备时间到期，自动脱掉装备] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { this.getUsername(), this.getId(), this.getName(), ee.getArticleName(), ee.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() });
						}
					}
				}

			}
		}

		if (this.getAvataPropsId() > 0) {
			ArticleEntity aee = aem.getEntity(this.getAvataPropsId());
			if (aee != null && aee.getTimer() != null && aee.getTimer().isClosed()) {
				this.setAvataPropsId(-1);
				Article a = am.getArticle(aee.getArticleName());
				if (a instanceof AvataProps) {
					((AvataProps) a).takeoffProperty(this);
				} else {
					if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[时装道具到期卸载] [时装道具不存在] [时装道具名:{}] [时装id:{}] [{}] [{}] [{}]", new Object[] { aee.getArticleName(), getAvataPropsId(), getUsername(), getId(), getName() });
				}
				try {
					ResourceManager rm = ResourceManager.getInstance();
					if (rm != null) {
						rm.getAvata(this);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		if (this.getHorseIdList() != null) {
			HorseManager hm = HorseManager.getInstance();
			Long[] ids = this.getHorseIdList().toArray(new Long[0]);
			for (long id : ids) {
				Horse h = hm.getHorseById(id, this);
				if (h == null) {
					continue;
				}
				if (h.getFashionId() > 0) {
					ArticleEntity ae = aem.getEntity(h.getFashionId());
					if (ae != null && ae.getTimer() != null && ae.getTimer().isClosed()) {

						Article a = am.getArticle(ae.getArticleName());
						if (a instanceof AvataProps) {
							h.takeOffFashion(true);
							h.setFashionId(-1);
							if (this.isIsUpOrDown() && this.ridingHorseId == h.getHorseId()) {
								try {
									ResourceManager rm = ResourceManager.getInstance();
									if (rm != null) {
										rm.getAvata(this);
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								if (ArticleManager.logger.isDebugEnabled()) {
									ArticleManager.logger.debug("[坐骑时装道具到期卸载] [卸载正在骑的坐骑装备] [" + this.getLogString() + "] [" + ae.getArticleName() + "]");
								}
							}
						} else {
							if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[坐骑时装道具到期卸载] [时装道具不存在] [时装道具名:{}] [时装id:{}] [{}] [{}] [{}]", new Object[] { ae.getArticleName(), getAvataPropsId(), getUsername(), getId(), getName() });
						}
					}
				}
			}
		}
	}

	/**
	 * 查询玩家身上的物品
	 * 
	 * @param ids
	 * @return
	 */
	private QUERY_ARTICLE_RES queryArticleRes(Long[] ids) {
		return null;
	}

	/**
	 * 停止并通知客户端
	 * 
	 * @param reason
	 * @param description
	 */
	public void stopAndNotifyOthers(int reason, String description) {
		SET_POSITION_REQ req = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), (byte) reason, getClassType(), id, (short) x, (short) y);
		if (reason != Game.REASON_CLIENT_STOP) {
			// 通知玩家自身，要求其停止移动
			// DefaultCrossServerAgent cross =
			// DefaultCrossServerAgent.getInstance();
			// if(cross != null && !cross.isLocalPlayerId(id)){
			// //跨服需要长id转换为短id
			// SET_POSITION_REQ req2 = new
			// SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), (byte)
			// reason, getClassType(),
			// id, (short) x, (short) y);
			// req2.setObjectId(cross.convertPlayerIdCross2Local(id));
			// addMessageToRightBag(req2, description);
			// }else{
			addMessageToRightBag(req, description);
			// }
		}
		// 通知广播区里的其他玩家

		if (currentGame == null) return;

		HashSet<Player> set = new HashSet<Player>();

		Fighter fs[] = currentGame.getVisbleFighter(this, false);
		for (int i = 0; i < fs.length; i++) {
			if (fs[i] instanceof Player) {
				set.add((Player) fs[i]);
			}
		}
		MoveTrace path = getMoveTrace();
		if (path != null) {
			Collection<LivingObject> livings = path.getLivingNotifySet();
			if (livings != null) {
				for (LivingObject living : livings) {
					if (living instanceof Player) {
						set.add((Player) living);
					}
				}
			}
			removeMoveTrace();
		}

		Iterator<Player> it = set.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (this.getDuelFieldState() == 0) {
				if (p.hiddenAllPlayer) {
				} else if (p.hiddenSameCountryPlayer) {
					if (p.getCountry() != this.getCountry()) {
						p.addMessageToRightBag(req);
					}
				} else {
					p.addMessageToRightBag(req);
				}
			} else {
				if (this.getDuelFieldState() == 1) {
					p.addMessageToRightBag(req);
				}
			}
		}
	}

	/**
	 * 给客户端发送消息
	 * 
	 * @param req
	 * @param description
	 */
	public void addMessageToRightBag(Message req, String description) {
		if (getConn() != null) {
			GameNetworkFramework.getInstance().sendMessage(getConn(), req, description);
			// if(req instanceof SHOP_OTHER_INFO_RES) {
			// Game.logger.error("[发送_SHOP_OTHER_INFO_RES] ["+this.getLogString()+"]\n"
			// +
			// StringUtil.getStackTrace(Thread.currentThread().getStackTrace()));
			// }
		} else {
			if (Game.logger.isInfoEnabled()) Game.logger.info("[discard-message] [{}] [player:{}] [玩家不在线] [{}] [{}]", new Object[] { this.getUsername(), this.getName(), req.getTypeDescription(), description });
		}
	}

	private transient List<NOTIFY_EVENT_REQ> __notifyEventList = java.util.Collections.synchronizedList(new ArrayList<NOTIFY_EVENT_REQ>());

	/**
	 * 给客户端发送消息
	 * 
	 * @param req
	 */
	public void addMessageToRightBag(Message req) {
		if (req == null) {
			return;
		}
		if (req instanceof NOTIFY_EVENT_REQ) {
			// 先不发此消息，等待game心跳结束后，统一成一个数据包发送
			NOTIFY_EVENT_REQ RR = (NOTIFY_EVENT_REQ) req;
			synchronized (__notifyEventList) {
				__notifyEventList.add(RR);
			}
		} else {
			addMessageToRightBag(req, "");
		}
	}

	/**
	 * 将此次game心跳收集到所有的NOTIFY_EVENT_REQ，组装成一个NOTIFY_EVENTS_REQ，发送给客户端
	 * 这么做，是因为，统计发现，玩家22%的流量花费在NOTIFY_EVENT_REQ协议上。 而产生此现象的原因，都是全体秒怪导致的。
	 * 可以一起发送，减少流量
	 */
	public void sendAllHoldNotifyEvents() {
		if (__notifyEventList.size() > 1) {
			int n = __notifyEventList.size();
			byte targetTypes[] = new byte[n];
			long targetIds[] = new long[n];
			byte eventTypes[] = new byte[n];
			long eventDatas[] = new long[n];
			synchronized (__notifyEventList) {
				int i = 0;
				Iterator<NOTIFY_EVENT_REQ> it = __notifyEventList.iterator();
				while (it.hasNext()) {
					NOTIFY_EVENT_REQ req = it.next();
					targetTypes[i] = req.getTargetType();
					targetIds[i] = req.getTargetId();
					eventTypes[i] = req.getEventType();
					eventDatas[i] = req.getEventData();
					i++;
				}
				__notifyEventList.clear();
			}

			NOTIFY_EVENTS_REQ req = new NOTIFY_EVENTS_REQ(GameMessageFactory.nextSequnceNum(), targetTypes, targetIds, eventTypes, eventDatas);
			addMessageToRightBag(req, "");

		} else if (__notifyEventList.size() > 0) {
			Iterator<NOTIFY_EVENT_REQ> it = __notifyEventList.iterator();
			while (it.hasNext()) {
				NOTIFY_EVENT_REQ req = it.next();
				addMessageToRightBag(req, "");
			}
			__notifyEventList.clear();
		}
	}

	/**
	 * 是否在线
	 * 
	 * @return
	 */
	public boolean isOnline() {
		if (getConn() == null || getConn().getState() == Connection.CONN_STATE_CLOSE) {
			return false;
		}
		return true;
	}

	/**
	 * 得到上次所在的地图
	 * 
	 * @return
	 */
	public String getLastGame() {
		if (lastGame == null) return "";
		return lastGame;
	}

	/**
	 * 设置上次所在的地图
	 * 
	 * @param lastGame
	 */
	public void setLastGame(String lastGame) {
		this.lastGame = lastGame;
		setDirty(true, "lastGame");
	}

	/** 过图信息--- */
	private String lastTransferGame;
	private int lastX;
	private int lastY;

	public String getLastTransferGame() {
		return lastTransferGame;
	}

	public void setLastTransferGame(String lastTransferGame) {
		this.lastTransferGame = lastTransferGame;
		setDirty(true, "lastTransferGame");
		if (lastTransferGame == null) {
			try {
				throw new Exception();
			} catch (Exception e) {
				if (FaeryManager.logger.isWarnEnabled()) FaeryManager.logger.warn(getLogString() + "[设置lastTransferGame] [异常]", e);
			}
		}
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int lastX) {
		this.lastX = lastX;
		this.setDirty(true, "lastX");
	}

	public int getLastY() {
		return lastY;
	}

	public void setLastY(int lastY) {
		this.lastY = lastY;
		setDirty(true, "lastY");
	}

	/**
	 * 获得最近一次的客户端请求时间
	 * 
	 * @return
	 */
	public long getLastRequestTime() {
		return lastRequestTime;
	}

	/**
	 * 设置最近一次客户端请求时间
	 * 
	 * @param lastRequestTime
	 */
	public void setLastRequestTime(long lastRequestTime) {

		this.lastRequestTime = lastRequestTime;
		setDirty(true, "lastRequestTime");
	}

	/**
	 * 得到游戏
	 * 
	 * @return
	 */
	public String getGame() {
		return game;
	}

	/**
	 * 设置游戏
	 * 
	 * @param game
	 */
	public void setGame(String game) {
		this.game = game;
		setDirty(true, "game");
		if (game != null && game.length() > 20) {
			try {
				throw new Exception();
			} catch (Exception e) {
				FaeryManager.logger.error(getLogString() + "[设置game超长] [game:"+game+"]", e);
			}
		}
	}

	/**
	 * 得到玩家的用户名
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置玩家的用户名
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
		setDirty(true, "username");
	}

	/**
	 * 于客户端的连接
	 * 
	 * @return
	 */
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 得到跳图数据
	 * 
	 * @return
	 */
	public TransportData getTransportData() {
		return transportData;
	}

	public void setTransportData(TransportData tp) {
		this.transportData = tp;
	}

	/**
	 * 离开游戏
	 */
	public void leaveGame() {
		long lastTime = quitGameTime - loginServerTime;

		if (getActivePetId() > 0) {

			PetManager pm = PetManager.getInstance();
			long petId = getActivePetId();
			Pet pet = pm.getPet(getActivePetId());
			Game game = getCurrentGame();
			if (pet != null && game != null && game.contains(pet)) {

				if (packupPet(false)) {
					if (PetManager.logger.isDebugEnabled()) {
						PetManager.logger.debug("[离开游戏召回宠物] [" + this.getLogString() + "]");
					}
				} else {
					game.removeSprite(pet);
					PetManager.logger.error("[离开游戏召回宠物错误] [" + this.getLogString() + "]");
				}
			}
		}

		DealCenter.getInstance().cancelDeal(DealCenter.getInstance().getDeal(this), this);

		// 刷新宠物临时加点
		Knapsack petKnapsack = this.getPetKnapsack();
		Cell[] cells = petKnapsack.getCells();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		PetManager pm = PetManager.getInstance();
		for (Cell c : cells) {
			long petPropsEntityId = c.getEntityId();
			ArticleEntity ae = aem.getEntity(petPropsEntityId);
			if (ae != null && ae instanceof PetPropsEntity) {
				PetPropsEntity ppe = (PetPropsEntity) ae;
				long petId = ppe.getPetId();
				Pet pet = pm.getPet(petId);
				if (pet != null && pet.getTempPoints() != null) {
					if (pet.getTempPoints() != null) {
						pet.cancleUnAllocate();
						// pet.initS();
						if (PetManager.logger.isInfoEnabled()) PetManager.logger.info("[玩家离开游戏清理宠物临时加点] [" + this.getLogString() + "] [" + pet.getLogString() + "] ");
					}
				}
			}
		}

		// 处理跟随NPC
		if (followableNPC != null) {
			if (followableNPC.isTransferWithOwner()) {
				followableNPC.getCurrentGame().removeSprite(followableNPC);
				if (TaskSubSystem.logger.isInfoEnabled()) {
					TaskSubSystem.logger.info(getLogString() + "[护送NPC过图] [离开地图] [NPC:{}]", new Object[] { followableNPC.getName() });
				}
			}
		}
		try {
			if (this.getActiveMagicWeaponId() > 0) { // 下线召回法宝npc
				NPC mnpc = MemoryNPCManager.getNPCManager().getNPC(getActiveMagicWeaponId());
				MagicWeaponManager.logger.debug("[用户下线] [召回法宝npc] [" + this.getLogString() + "][" + getActiveMagicWeaponId() + "][" + mnpc + "]");
				if (mnpc != null) {
					this.getCurrentGame().removeSprite(mnpc);
				}
			}
		} catch (Exception e) {

		}

		setGame(null);
		currentGame = null;
		removeMoveTrace();
		setQuitGameTime(System.currentTimeMillis());

		if (isInBattleField()) {
			this.leaveBattleField();
		}
		if (this.getTimerTaskAgent() != null) {
			this.getTimerTaskAgent().notifyUpOrDown();
		}
		this.noticeShowButton = false;
		if (GamePlayerManager.logger.isWarnEnabled()) {
			GamePlayerManager.logger.warn("[用户离开game] [" + this.getLogString() + "] [boothstate:"+isBoothState()+"] [connState:"+(getConn()!=null?getConn().getStateString(getConn().getState()):"null")+"]");
		}
	}

	/**
	 * 此方法用于预先加载数据库的数据
	 * 
	 * 用户正在进入地图，此方法的目标是防止在进入地图过程中，有消息到达，服务器无法处理
	 * 
	 * @param game
	 */
	public void prepareEnterGame(Game game) {
		currentGame = game;

		// 提前加载背包
		for (int k = 0; k < knapsacks_common.length; k++) {
			if (knapsacks_common[k] != null) {
				loadKnapsack(knapsacks_common[k]);
			}
		}
		if (knapsacks_fangBao != null) {
			loadKnapsack(knapsacks_fangBao);
		}

		// 预加载反馈
		FeedbackManager fm = FeedbackManager.getInstance();
		long[] feedbackIds = null;
		if (this.getFeedbackIds() == null) {
			feedbackIds = fm.getPlayerFeedBackIds(this);
			if (feedbackIds == null) {
				feedbackIds = new long[0];
			}
			this.setFeedbackIds(feedbackIds);
		} else {
			feedbackIds = this.getFeedbackIds();
		}
		for (int i = 0; i < feedbackIds.length; i++) {
			fm.getFeedBack(feedbackIds[i]);
		}

		// 预先加载宗派中的家族
		ZongPaiManager.getInstance().getZongPaiByPlayerId(getId());

		//
		byte[] ftypes = new byte[] { FateActivityType.国内仙缘.type, FateActivityType.国内论道.type, FateActivityType.国外仙缘.type, FateActivityType.国外论道.type };

		for (byte b : ftypes) {
			long[] idss = getInitActivityId(b);
			if (idss != null && idss.length > 1 && idss[1] > 0) {
				FateManager.getInstance().getFateActivity(idss[1]);

			}
			if (idss != null && idss.length > 0 && idss[0] > 0) {
				FateManager.getInstance().getFateActivity(idss[0]);
			}
		}
		//
		if (getActivePetId() > 0) {
			PetManager pm = PetManager.getInstance();
			pm.getPet(getActivePetId());
		}

	}

	// public boolean guideDeclare=false;
	/** 仙尊挑战标记 */
	public transient int chanllengeFlag = 0;

	/** 仙灵挑战标记 2-进入副本 */
	public transient int xl_chanllengeFlag = 0;

	public void 处理登录弹框(){
		if(this.getLevel() < 10){
			return;
		}
		long lastTime = timeRecord[0];
		boolean result = TimeTool.instance.isSame(SystemTime.currentTimeMillis(), lastTime, TimeDistance.DAY);
		if(!result){
			ActivityManager.getInstance().handPlayerEnter(this, 0);
			timeRecord[0] = System.currentTimeMillis();
			setTimeRecord(timeRecord);
		}
	}
	
	/**
	 * 进入游戏
	 * 
	 * @param game
	 */
	public void enterGame(Game game) {
		if (getUsername().equals("wtx062") && GameConstants.getInstance().getServerName().equals("客户端测试")) {
			try {
				String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
				GamePlayerManager.logger.warn("[TESTENTER] [enterGame1] [name:"+name+"\n] "+stacktrace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (game.gi.getName().contains(SeptStationManager.jiazuMapName)) {
			this.setViewWidth(3000);
			this.setViewHeight(3000);
		} else {
			this.setViewWidth(CoreSubSystem.DEFAULT_PLAYER_VIEWWIDTH);
			this.setViewHeight(CoreSubSystem.DEFAULT_PLAYER_VIEWHEIGHT);
		}
		this.removeMoveTrace();
		currentGame = game;
		setLastGame(game.gi.getName());
		
		if(game != null && game.gi != null && game.gi.getName() != null){
			int countryM = TransportData.getCountry(game.gi.getName());
			if(countryM != -1){
				//Game.logger.warn("[修正地图国家] ["+game.gi.getName()+"] [gameCountry:"+game.country+"] [countryM:"+countryM+"] ["+this.getLogString()+"]");
				if(game.country != countryM){
					Game.logger.warn("[修正地图国家22] ["+game.gi.getName()+"] [gameCountry:"+game.country+"] [countryM:"+countryM+"] ["+this.getLogString()+"]");
					game.country = (byte)countryM;
				}
			}
		}

		// enemyList.enemyList.clear();
		this.clearTransientEnemy();

		setEnterGameTime(SystemTime.currentTimeMillis());

		BattleFieldManager bm = BattleFieldManager.getInstance();
		if (bm != null && bm.isBattleFieldGame(game.getGameInfo().getName())) {
			if (battleField != null) {
				this.enterBattleField();
			}
		}

		setGame(game.gi.getName());
		设置复活点();
		setCurrentGameCountry(game.country);
		setTransferGameCountry(game.country);

		if (Game.logger.isInfoEnabled()) {
			Game.logger.info(this.getLogString() + "[进入到了地图:{}] [地图国家类型:{}]", new Object[] { game.gi.getName(), game.country });
		}
		if (ArticleManager.logger.isWarnEnabled()) {
			ArticleManager.logger.info("[过图] " + this.getLogString() + this.getPlayerPropsString());
		}

		// 处理跟随NPC
		if (followableNPC != null) {
			if (followableNPC.isTransferWithOwner()) {
				followableNPC.setCurrentGame(game);
				followableNPC.setX(this.getX());
				followableNPC.setY(this.getY());
				followableNPC.setGameNames(game.gi);
				followableNPC.removeMoveTrace();// 要重新寻路
				followableNPC.setTransferWithOwner(false);// 重置跳转状态
				game.addSprite(followableNPC);
				if (TaskSubSystem.logger.isInfoEnabled()) {
					TaskSubSystem.logger.info(getLogString() + "[护送NPC过图] [进入地图] [NPC:{}]", new Object[] { followableNPC.getName() });
				}
			}
		}
		if (getActivePetId() > 0) {
			Knapsack knapsack = getPetKnapsack();
			PetPropsEntity ppe = null;
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			for (Cell cell : knapsack.getCells()) {
				if (cell != null && cell.getEntityId() > 0 && cell.getCount() > 0) {
					try {
						PetPropsEntity ae = (PetPropsEntity) aem.getEntity(cell.getEntityId());
						if (ae != null && ae.getPetId() == getActivePetId()) {
							ppe = ae;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			if (ppe != null) {
				PetManager pm = PetManager.getInstance();
				Pet pet = pm.getPet(getActivePetId());
				if (pet != null && !game.contains(pet)) {
					setActivePetId(-1);
					this.notifyAttributeAttackChange();
					summonPet(pet, ppe, "进地图收回宠物");
					if (PetManager.logger.isInfoEnabled()) PetManager.logger.info("[{}] [{}] [{}] [进地图召唤宠物] [{}] [{}] [{},{}] [{},{}]", new Object[] { username, name, id, pet.getId(), pet.getName(), pet.getX(), pet.getY(), getX(), getY() });
				}
			} else {
				packupPet(true);
				if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[{}] [{}] [{}] [进地图收回宠物失败] [召唤宠物道具实体为空]", new Object[] { username, name, id });
			}
		}
		
		try {
			if (this.getActiveMagicWeaponId() > 0) {
				NPC mNpc = MemoryNPCManager.getNPCManager().getNPC(this.getActiveMagicWeaponId());
				NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ req = new NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ(GameMessageFactory.nextSequnceNum(), getActiveMagicWeaponId());
				addMessageToRightBag(req);
				if (MagicWeaponManager.logger.isDebugEnabled()) {
					MagicWeaponManager.logger.debug("[通知客户端法宝id][" + this.getLogString() + "] [" + mNpc.getId() + "]");
				}

				ArticleEntity ae = this.getCurrSoul().getEc().get(11);
				if (ae != null) {
					setBasicpropertyname(ae.getId(), "进入游戏");
				}

				if (MagicWeaponManager.logger.isDebugEnabled()) {
					MagicWeaponManager.logger.debug("[法宝npc] [enterGame] [法宝:" + mNpc + "] [" + (mNpc != null ? game.contains(mNpc) : "") + "]");
				}
				if (mNpc != null && !game.contains(mNpc)) {
					mNpc.setX(this.getX() + MagicWeaponConstant.offsetX);
					mNpc.setY(this.getY() + MagicWeaponConstant.offsetY);
					mNpc.setSpeedA(this.getSpeed());
					mNpc.removeMoveTrace();
					game.addSprite(mNpc);

				} else if (mNpc == null) {
					try {
						if (ae != null) {
							MagicWeapon m = (MagicWeapon) ArticleManager.getInstance().getArticle(ae.getArticleName());
							MagicWeaponNpc mmNpc = (MagicWeaponNpc) MemoryNPCManager.getNPCManager().createNPC(m.getNpcId());
							mmNpc.setSpeedA(getSpeed());
							mmNpc.removeMoveTrace();
							mmNpc.setX(getX() + MagicWeaponConstant.offsetX);
							mmNpc.setY(getY() + MagicWeaponConstant.offsetY);
							if (ae instanceof NewMagicWeaponEntity) {
								NewMagicWeaponEntity nwe = (NewMagicWeaponEntity) ae;
								if (nwe.avatarRace != null && !nwe.avatarRace.isEmpty()) {
									String[] ta = nwe.avatarRace.split("_");
									if (ta.length == 2) {
										mmNpc.setAvataRace(ta[0]);
										mmNpc.setAvataSex(ta[1]);
										Avata a1 = ResourceManager.getInstance().getAvata(mmNpc);
										mmNpc.setAvata(a1.avata);
										mmNpc.setAvataType(a1.avataType);
									} else {
										MagicWeaponManager.logger.warn("[法宝npc形象配置错误] [" + this.getLogString() + "] [" + nwe.avatarRace + "] [" + nwe.getArticleName() + "]");
									}
								}
							}
							game.addSprite(mmNpc);
							setActiveMagicWeaponId(mmNpc.getId());
							if (MagicWeaponManager.logger.isDebugEnabled()) {
								MagicWeaponManager.logger.debug("[法宝npc] [enterGame] [法宝id:" + mmNpc.getId() + "]");
							}
							// NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ req = new NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ(GameMessageFactory.nextSequnceNum(), mmNpc.getId());
							// addMessageToRightBag(req);
							// if(MagicWeaponManager.logger.isDebugEnabled()) {
							// MagicWeaponManager.logger.debug("[通知客户端法宝id][" + this.getLogString() + "] [" + mNpc.getId() + "]");
							// }
						}
					} catch (Exception e) {
						MagicWeaponManager.logger.error("[召唤法宝npc错误][" + this.getLogString() + "]", e);
					}

				}
			} else {
				ArticleEntity ae = this.getCurrSoul().getEc().get(11);
				if (ae != null) {
					setBasicpropertyname(ae.getId(), "进入游戏");
					MagicWeapon m = (MagicWeapon) ArticleManager.getInstance().getArticle(ae.getArticleName());
					MagicWeaponNpc mmNpc = (MagicWeaponNpc) MemoryNPCManager.getNPCManager().createNPC(m.getNpcId());
					mmNpc.setSpeedA(getSpeed());
					mmNpc.removeMoveTrace();
					mmNpc.setX(getX() + MagicWeaponConstant.offsetX);
					mmNpc.setY(getY() + MagicWeaponConstant.offsetY);
					if (ae instanceof NewMagicWeaponEntity) {
						NewMagicWeaponEntity nwe = (NewMagicWeaponEntity) ae;
						if (nwe.avatarRace != null && !nwe.avatarRace.isEmpty()) {
							String[] ta = nwe.avatarRace.split("_");
							if (ta.length == 2) {
								mmNpc.setAvataRace(ta[0]);
								mmNpc.setAvataSex(ta[1]);
								Avata a1 = ResourceManager.getInstance().getAvata(mmNpc);
								mmNpc.setAvata(a1.avata);
								mmNpc.setAvataType(a1.avataType);
							} else {
								MagicWeaponManager.logger.warn("[法宝npc形象配置错误] [" + this.getLogString() + "] [" + nwe.avatarRace + "] [" + nwe.getArticleName() + "]");
							}
						}
					}
					game.addSprite(mmNpc);
					setActiveMagicWeaponId(mmNpc.getId());
					if (MagicWeaponManager.logger.isDebugEnabled()) {
						MagicWeaponManager.logger.debug("[法宝npc] [enterGame2] [法宝id:" + mmNpc.getId() + "]");
					}
				}
			}
		} catch (Exception e) {

		}
		try {
			WorldMapManager wmm = WorldMapManager.getInstance();
			HotspotManager.getInstance().overHotspot(this, Hotspot.OVERTYPE_MAP, game.gi.displayName);
			wmm.setPlayerExploredMap(this.getId(), game.gi.getName());
			AchievementManager achievementManager = AchievementManager.getInstance();
			GameDataRecord gameDataRecord = achievementManager.getPlayerDataRecord(this, RecordAction.探索所有地图);

			if (gameDataRecord == null) {// 没完成成就
				boolean done = true;
				for (String name : GameManager.achievementMapnames) {
					if (!wmm.isPlayerExploredMap(this.getId(), name)) {
						done = false;
						break;
					}
				}
				if (done) {
					AchievementManager.getInstance().record(this, RecordAction.探索所有地图);
					if (AchievementManager.logger.isInfoEnabled()) {
						AchievementManager.logger.info(this.getLogString() + "[进入地图:" + game.gi.getName() + "] [之后] [探索成就已达成]");
					}
				}
			} else {
				if (AchievementManager.logger.isInfoEnabled()) {
					AchievementManager.logger.info(this.getLogString() + "[进入地图:" + game.gi.getName() + "] [之前] [探索成就已达成]");
				}
			}

		} catch (Exception ex) {

		}
		家族宣战状态();
//			QuizManager.getInstance().enterNotice(this);
			FateManager.getInstance().noticePlayerFateActivity(this);
		if (countdownAgent != null) {
			countdownAgent.通知玩家倒计时();
		}
		MapSoundManager msm = MapSoundManager.getInstance();
		if (msm != null) {
			msm.根据mapName得到地图音乐回复(this, game.gi.name);
		}
		FeedbackManager.getInstance().enterGameFeedbackNotice(this);
		设置进入区域(game);
		modifyDailyChangevalue();

		// 设置城战状态
		CityFightManager cfm = CityFightManager.getInstance();
		if (cfm != null) {
			cfm.设置玩家side(this);
		}

		// 封印
		flushSealState();
		if (GamePlayerManager.logger.isWarnEnabled()) {
			GamePlayerManager.logger.warn("[用户进入game] [{}] [性别：{}] [门派：{}] [等级：{}] [地图：{}] [country:{}] [sp:{}]", 
					new Object[] { getLogString(), (this.getSex() == 0 ? "男" : "女"), CareerManager.careerNames[this.getCareer()], this.getLevel(), game.gi.name,game.country,getSpeed() });
		}

		ZongPaiManager.getInstance().上线增加繁荣度(this);
		VipManager vm = VipManager.getInstance();
		if (vm != null) {
			this.setVipLevel(this.getVipLevel());
			vm.设置玩家的vip属性(this);
		}
		设置国运国探状态();

		if (this.needToNotifyAboutHidden) {
			needToNotifyAboutHidden = false;
			String str = Translate.温馨提示您设置了屏蔽选项包括;
			if (this.hiddenAllPlayer) {
				str += Translate.屏蔽所有玩家;
			}
			if (this.hiddenSameCountryPlayer) {
				str += Translate.屏蔽本国玩家;
			}
			if (this.hiddenChatMessage) {
				str += Translate.屏蔽聊天信息;
			}
			if (this.hiddenTransformPop) {
				str += Translate.屏蔽交易信息;
			}
			if (this.hiddenChangePop) {
				str += Translate.屏蔽交换信息;
			}
			if (this.hiddenFanzhiPop) {
				str += Translate.屏蔽繁殖信息;
			}
			if (this.hiddenTeamPop) {
				str += Translate.屏蔽组队信息;
			}
			str += Translate.您可以在设置中来修改;

			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, str);
			this.addMessageToRightBag(hreq);
		}

		通知时间地图信息(game);
		this.setWage(this.getWage());// 家族工资设置
		this.setPhysicalDecrease(this.getPhysicalDecrease());
		this.setSpellDecrease(this.getSpellDecrease());

		this.setSpeed(this.getSpeed());

		getTodayCanUseBindSilver();

		if (this.getTimerTaskAgent() != null) {
			this.getTimerTaskAgent().notifyUpOrDown();
		}
		this.chuangonging = false;
		// 由于客户端问题因此服务器补救(this);
		设置赐福状态();
		切图时对正在处于副本入口界面的玩家的特殊操作();
		this.setParaticeValue();
		this.changeBianShenLvs();
		ActivityNoticeManager.getInstance().noticePlayerCurrentActivity(this);
		if (chanllengeFlag != 0) {
			if (chanllengeFlag == 1) {
				this.sendError(Translate.已挑战过);
			} else if (chanllengeFlag < 0) {
				this.sendError(Translate.挑战失败);
			} else if (chanllengeFlag == 2) {
				this.sendError(Translate.仙尊挑战进入提示);
			} else if (chanllengeFlag == 3) {
				FurnaceManager.popwindow4enterFrun(this);
			} else if (chanllengeFlag > 100) {
				NOTIFY_ROBBERY_COUNTDOWN_REQ resp = new NOTIFY_ROBBERY_COUNTDOWN_REQ();
				resp.setCountType((byte) 2);
				resp.setLeftTime(chanllengeFlag % 100);
				resp.setContentmass("开启倒计时");
				this.addMessageToRightBag(resp);
			}
			chanllengeFlag = 0;
		}
		try {
			ServerEventManager.getInstance().doEvent(this, EventType.玩家过图);
		} catch (Exception e) {
			e.printStackTrace();
			ServerEventManager.logger.warn("[服务器补偿事件] [玩家过图] [" + this.getLogString() + "] [异常：" + e + "]");
		}
		modifyShouhun();
		try {
			
			if(!WolfManager.getInstance().isWolfGame(this)){
				if (this.isHold()) {
					setHold(false);
				}
				if (this.isStun()) {
					setStun(false);
				}
			}
			
			if (game.gi.getName().equals(WolfManager.getInstance().mapName)) {
				WolfGame g = WolfManager.getInstance().getWolfGame(this);
				if (g != null) {
					g.sendSkillIds(this);
				}
			} else {
				String[] avatas = this.getAvata();
				boolean isChange = false;
				if (avatas != null) {
					for (String s : avatas) {
						if (s!=null && (s.contains("chongwu_shihunxuelang04") || s.contains("xinchongwu_xiaoyang"))) {
							isChange = true;
						}
					}
				}
				if (isChange) {
					this.setSpeedA(150);
					ResourceManager.getInstance().getAvata(this);
					this.setHold(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ActivityManager.getInstance().handleTotleCostActivityQuery(this,"登录游戏");

		//this.deleteSameHunshi();
		if (getUsername().equals("wtx062") && GameConstants.getInstance().getServerName().equals("客户端测试")) {
			try {
				String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
				GamePlayerManager.logger.warn("[TESTENTER] [enterGame2] [name:"+name+"\n] "+stacktrace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteSameHunshi() {
		try {
			Set<Long> hasHunshiSet = new HashSet<Long>();
			ArrayList<Long> horseIdlist = this.getHorseIdList();
			for (Long horseId : horseIdlist) {
				Horse h = HorseManager.getInstance().getHorseById(horseId, this);
				if (h == null) {
					continue;
				}
				long[] hunshiArray = h.getHunshiArray();
				for (Long hunshiId : hunshiArray) {
					if (hunshiId > 0) {
						hasHunshiSet.add(hunshiId);
					}
				}
				long[] hunshi2Array = h.getHunshi2Array();
				for (Long hunshiId : hunshi2Array) {
					if (hunshiId > 0) {
						hasHunshiSet.add(hunshiId);
					}
				}
			}
			deleteFormBeibao(this.getKnapsack_common(), hasHunshiSet);
			deleteFormBeibao(this.getKnapsack_fangbao(), hasHunshiSet);
			deleteFormBeibao(this.getKnapsacks_cangku(), hasHunshiSet);
			deleteFormBeibao(this.getKnapsacks_warehouse(), hasHunshiSet);
		} catch (Exception e) {
			HorseManager.logger.warn("[刷魂石] [异常] [" + this.getLogString() + "]", e);
		}
	}

	public void deleteFormBeibao(Knapsack beibao, Set<Long> hasHunshiSet) {
		if (beibao == null) {
			return;
		}
		Cell[] cells = beibao.getCells();
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			if (cell != null && cell.getEntityId() > 0) {
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
				if (ae != null && ae instanceof HunshiEntity) {
					if (!hasHunshiSet.contains(cell.getEntityId())) {
						hasHunshiSet.add(cell.getEntityId());
					} else {
						beibao.remove(i, "刷魂石", false);
						// 统计
						ArticleStatManager.addToArticleStat(null, this, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "刷魂石", null);
						HorseManager.logger.error("[刷魂石] [删除:" + beibao.knapName + "] [" + this.getLogString() + "] [" + ae.getId() + "] [" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [totalnum:" + (++aNumForHunshiStat) + "]");
					}
				}
			}
		}
		for (int i = 0; i < RobberyConstant.需要清除的魂石Id.length; i++) {
			int count = beibao.countArticle(RobberyConstant.需要清除的魂石Id[i]);
			for (int k = 0; k < count; k++) {
				ArticleEntity ae = beibao.removeByArticleId(RobberyConstant.需要清除的魂石Id[i], "刷魂石", false);
				ArticleStatManager.addToArticleStat(null, this, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "刷魂石", null);
				HorseManager.logger.error("[刷魂石2] [删除:" + beibao.knapName + "] [" + this.getLogString() + "] [" + ae.getId() + "] [" + ae.getArticleName() + "] [color:" + ae.getColorType() + "] [totalnum:" + (++aNumForHunshiStat) + "]");
			}
		}
	}

	public void modifyShouhun() {
		try {
			if (huntLifr == null && loadExcep[0]) {
				try {
					huntLifr = HuntLifeEntityManager.instance.getHuntLifeEntity(this);
					ArticleEntity ae = this.getEquipmentColumns().get(11);
					if (ae != null && ae instanceof NewMagicWeaponEntity && ((NewMagicWeaponEntity) ae).getMdurability() > 0) {
						HuntLifeEntityManager.instance.loadAllAttr(this);
					}
					if (ae instanceof NewMagicWeaponEntity) {
						((NewMagicWeaponEntity) ae).owner = this;
					}
					loadExcep[0] = false;
					HuntLifeEntityManager.logger.warn("[重新加载玩家兽魂属性] [成功] [" + this.getLogString() + "]");
				} catch (Exception e) {

				}
			}
			if (loadExcep[1]) {
				try {
					JiazuEntityManager2.instance.addPracticeAttr(this);
					loadExcep[1] = false;
					JiazuEntityManager2.logger.warn("[重新加载玩家家族属性] [成功] [" + this.getLogString() + "]");
				} catch (Exception e) {

				}
			}
			if (loadExcep[2]) {
				try {
					SoulPithEntityManager.getInst().initSoulPithAttr(this);
					loadExcep[2] = false;
					SoulPithEntityManager.logger.warn("[重新加载玩家灵根属性] [成功] [" + this.getLogString() + "]");
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {

		}
	}

	private transient boolean hasNotice = false;

	/**
	 * 增加赐福状态，如果这个值为true代表这个人已经加过了赐福buff，以后都不会在加这个buff
	 */
	public boolean addCifuBuff = false;

	/**
	 * 设置法宝前缀统计名
	 */
	public void setBasicpropertyname(long id, String reason) {
		try {
			NewMagicWeaponEntity ne = (NewMagicWeaponEntity) ArticleEntityManager.getInstance().getEntity(id);
			if (ne != null) {
				ne.setBasicpropertyname_stat(MagicWeaponManager.nameStats.get(ne.getBasicpropertyname()));
			}
			if (MagicWeaponManager.logger.isInfoEnabled()) {
				MagicWeaponManager.logger.info("[设置法宝统计名] [成功] [id:{}] [reason:{}] [basicname:{}] [statname:{}] [player:{}]", new Object[] { id, reason, (ne == null ? "null" : ne.getBasicpropertyname()), (ne == null ? "null" : ne.getBasicpropertyname_stat()), this.getLogString() });
			}
		} catch (Exception e) {
			e.printStackTrace();
			MagicWeaponManager.logger.warn("[设置法宝统计名] [异常] [id:{}] [reason:{}] [player:{}]", new Object[] { id, reason, this.getLogString() });
		}
	}

	public boolean isAddCifuBuff() {
		return addCifuBuff;
	}

	public void setAddCifuBuff(boolean addCifuBuff) {
		this.addCifuBuff = addCifuBuff;
		setDirty(true, "addCifuBuff");
	}

	public void 设置赐福状态() {
		if (!addCifuBuff) {
			if (PlayerManager.开启赐福标记 && this.getLevel() < 110) {
				try {
					// 加赐福buff
					BuffTemplateManager btm = BuffTemplateManager.getInstance();
					BuffTemplate bt = btm.getBuffTemplateByName(PlayerManager.赐福buff);
					if (bt != null) {
						Buff buff = bt.createBuff(1);
						if (buff != null) {
							buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 10 * 1l * 24 * 3600 * 1000);
							buff.setCauser(this);
							this.placeBuff(buff);
						}
					}
					setAddCifuBuff(true);
					GamePlayerManager.logger.warn("[增加赐福buff] [成功] [" + this.getLogString() + "]");
				} catch (Exception ex) {

				}
			}
		}
		if (PlayerManager.开启赐福标记) {
			Buff buff = this.getBuffByName(PlayerManager.赐福buff);
			if (buff != null && this.getLevel() < 110) {
				赐福标记 = true;
			} else {
				赐福标记 = false;
			}
		} else {
			赐福标记 = false;
		}
	}

	public void 切图时对正在处于副本入口界面的玩家的特殊操作() {
		try {
			if (this.getTeam() == null) {
				this.prepareEnterDownCityStatus = -1;
				this.prepareEnterWingCarbonStatus = -1;
			} else {
				if (this.prepareEnterDownCityStatus != -1 && this.prepareEnterDownCityStatus != DownCityManager.PREPARE_STATUS_不去) {
					DownCityManager dcm = DownCityManager.getInstance();
					List<Player> list = team.getMembers();
					if (list != null) {
						long[] ids = new long[list.size()];
						byte[] statuss = new byte[list.size()];
						for (int i = 0; i < list.size(); i++) {
							ids[i] = list.get(i).getId();
							statuss[i] = list.get(i).prepareEnterDownCityStatus;
						}
						DOWNCITY_PREPARE_WINDOW_REQ dpwr = new DOWNCITY_PREPARE_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), dcm.通过副本英文名得到副本中文名(team.perpareEnterDownCityName), ids, statuss, Translate.当全部队员都接受后方可由队长带领进入副本请您耐心等待);
						this.addMessageToRightBag(dpwr);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void 由于客户端问题因此服务器补救(Player player) {

		String[] 元宝商城右边隐藏的商店的名字 = ShopManager.元宝商城右边隐藏的商店的名字;
		if (player.getVipLevel() > 0) {
			元宝商城右边隐藏的商店的名字 = ShopManager.元宝商城右边隐藏的商店的名字包含vip;
		}
		SHOPS_NAME_GET_RES res = new SHOPS_NAME_GET_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, ShopManager.元宝商城左边标签显示的商店的名字, ShopManager.元宝商城左边标签显示的商店的图标, 元宝商城右边隐藏的商店的名字);
		player.addMessageToRightBag(res);
		// 返回第一个商店用于客户端显示
		if (ShopManager.元宝商城左边标签显示的商店的名字 != null && ShopManager.元宝商城左边标签显示的商店的名字.length > 0) {
			String shopName = ShopManager.元宝商城左边标签显示的商店的名字[0];
			ShopManager shopManager = ShopManager.getInstance();
			if (shopManager != null) {
				Shop shop = shopManager.getShop(shopName, player);
				if (shop != null) {
					byte marketType = 0;
					if (ShopManager.元宝商城左边标签显示的商店的名字 != null) {
						for (int i = 0; i < ShopManager.元宝商城左边标签显示的商店的名字.length; i++) {
							String name = ShopManager.元宝商城左边标签显示的商店的名字[i];
							if (shopName.equals(name)) {
								marketType = ShopManager.元宝商城类型;
							}
						}
					}
					if (元宝商城右边隐藏的商店的名字 != null) {
						for (int i = 0; i < 元宝商城右边隐藏的商店的名字.length; i++) {
							String name = 元宝商城右边隐藏的商店的名字[i];
							if (shopName.equals(name)) {
								marketType = ShopManager.元宝商城类型;
							}
						}
					}
					Goods gs[] = shop.getGoods(player);
					long[] coins = null;
					if (shop.shopType == ShopManager.商店_绑银商店) {
						coins = new long[2];
						coins[0] = player.getBindSilver();
						coins[1] = player.getSilver();
					} else if (shop.shopType == ShopManager.商店_资源商店) {
						coins = new long[3];
						Cave cave = FaeryManager.getInstance().getCave(player);
						if (cave != null) {
							ResourceCollection rc = cave.getCurrRes();
							if (rc != null) {
								coins[0] = rc.getFood();
								coins[1] = rc.getWood();
								coins[2] = rc.getStone();
							}
						}

					} else if (shop.shopType == ShopManager.商店_工资商店) {
						coins = new long[1];
						coins[0] = player.getWage();
					} else if (shop.shopType == ShopManager.商店_银子商店) {
						coins = new long[1];
						coins[0] = player.getSilver();
					} else {
						coins = new long[0];
					}
					com.fy.engineserver.shop.client.Goods[] cGoods = ShopManager.translate(player, shop, gs);
					SHOP_GET_RES shopGetRes = new SHOP_GET_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.getVersion(), shop.shopType, coins, cGoods);
					player.addMessageToRightBag(shopGetRes);
					if (!shopName.equals(Translate.商店名字_历练兑换)) {
						SHOP_OTHER_INFO_RES shopOtherRes = new SHOP_OTHER_INFO_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.shopType, cGoods);
						player.addMessageToRightBag(shopOtherRes);
					}
				}
			}
		}
	}

	public void 设置进入区域(Game game) {
		game.checkMapAreaForPlayerEnterAndExit(new LivingObject[] { this });
	}

	public void 设置国运国探状态() {
		long now = System.currentTimeMillis();
		Country country = CountryManager.getInstance().countryMap.get(this.country);
		if (country != null) {
			if (country.guotan) {
				long leftTime = ((country.getGuotanStartTime() + CountryManager.国探时长) - now) / 1000;
				NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 101, (int) leftTime, Translate.国探);
				addMessageToRightBag(req);
			}

			if (country.guoyun) {
				long leftTime = ((country.getGuoyunStartTime() + CountryManager.国运时长) - now) / 1000;
				NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 100, (int) leftTime, Translate.国运);
				addMessageToRightBag(req);
			}
		}
	}

	public void 通知时间地图信息(Game game) {
		if (game.gi.时间地图类型 == GameMap.TIME_MAP_TYPE_PET) {
			long leftTime = (DITU_PET_MAX_TIME - inPetDiTuTime) * 60;
			NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 102, (int) leftTime, Translate.剩余时间);
			addMessageToRightBag(req);
		}
		if (game.gi.时间地图类型 == GameMap.TIME_MAP_TYPE_TILI) {
			long leftTime = (DITU_TILI_MAX_TIME - inTiLiDiTuTime) * 60;
			NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 103, (int) leftTime, Translate.剩余时间);
			addMessageToRightBag(req);
		}
//		if (game.gi.时间地图类型 == GameMap.TIME_MAP_TYPE_WANBAO) {
//			long leftTime = (DITU_WANBAO_MAX_TIME - inWanBaoDiTuTime) * 60;
//			NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 105, (int) leftTime, Translate.剩余时间);
//			addMessageToRightBag(req);
//		}
		if (game.gi.时间地图类型 == GameMap.TIME_MAP_TYPE_WANBAO) {
			long leftTime = (getMaxTimeOfSilverGame() - inWanBaoDiTuTime) * 60;
			NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 105, (int) leftTime, Translate.剩余时间);
			addMessageToRightBag(req);
		}
		if (game.gi.时间地图类型 == GameMap.TIME_MAP_TYPE_XIANDI) {
			long leftTime = (DITU_XIANDI_MAX_TIME - inXianDiDiTuTime) * 60;
			NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 105, (int) leftTime, Translate.剩余时间);
			addMessageToRightBag(req);
		}
		if (game.gi.时间地图类型 == GameMap.TIME_MAP_TYPE_BAGUAXIANQUE) {
			long leftTime = (DITU_BAGUAXIANQUE_MAX_TIME - inBaGuaXianQueTime) * 60;
			NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 105, (int) leftTime, Translate.剩余时间);
			addMessageToRightBag(req);
		}
		if (game.gi.时间地图类型 >= GameMap.TIME_MAP_TYPE_LIMITDITU_START && game.gi.时间地图类型 < GameMap.TIME_MAP_TYPE_LIMITDITU_END) {
			int tempIndex = game.gi.时间地图类型 - GameMap.TIME_MAP_TYPE_LIMITDITU_START;
			long leftTime = (DITU_LIMIT_MAX_TIME[tempIndex] - inLimitDituTime[tempIndex]) * 60;
			NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 105, (int) leftTime, Translate.剩余时间);
			addMessageToRightBag(req);
		}
	}
	
	public long getMaxTimeOfSilverGame(){
		int maxTime = DITU_WANBAO_MAX_TIME;
		double vipAdd = VipManager.getInstance().vip付费地图时间的百分比(this);
		maxTime += maxTime * vipAdd / 100;
		return maxTime;
	}

	private void 设置复活点() {
		String resurrectionMapName = null;
		try {
			String[][] strss = GameManager.同一复活点的多个地图名字集合;
			for (int i = 0; i < strss.length; i++) {
				for (int j = 0; j < strss[i].length; j++) {
					String str = strss[i][j];
					if (str.equals(this.getGame())) {
						resurrectionMapName = GameManager.复活地图名字集合[i];
						break;
					}
				}
				if (resurrectionMapName != null) {
					break;
				}
			}
		} catch (Exception ex) {
			Game.logger.error("设置复活点是出错[" + this.getLogString() + "]", ex);
		}
		if (resurrectionMapName != null && (resurrectionMapName = resurrectionMapName.trim()).length() != 0) {
			int a = this.getResurrectionX();
			int b = this.getResurrectionY();
			GameManager gm = GameManager.getInstance();
			if (gm != null) {
				GameInfo gi = gm.getGameInfo(resurrectionMapName);
				if (gi != null) {
					MapArea ma = gi.getMapAreaByName(Translate.出生点);
					if (ma != null) {
						a = ma.x + (int) (ma.width * Math.random());
						b = ma.y + (int) (ma.height * Math.random());
						this.setResurrectionMapName(resurrectionMapName);
						this.setResurrectionX(a);
						this.setResurrectionY(b);
					}
				}
			}
		}
	}

	public void setResurrectionMapName(String value) {
		this.resurrectionMapName = value;
		this.setDirty(true, "resurrectionMapName");
	}

	public void setResurrectionX(int value) {
		this.resurrectionX = value;
		this.setDirty(true, "resurrectionX");
	}

	public void setResurrectionY(int value) {
		this.resurrectionY = value;
		this.setDirty(true, "resurrectionY");
	}

	public void setCurrentGameCountry(int value) {
		super.setCurrentGameCountry(value);
		this.setDirty(true, "currentGameCountry");
	}

	/**
	 * 最后一次更新统计数据的时间
	 */
	private long lastUpdateStatDataTime;

	/**
	 * 用户上线
	 */
	public void enterServer() {
		if (getUsername().equals("wtx062") && GameConstants.getInstance().getServerName().equals("客户端测试")) {
			try {
				String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
				GamePlayerManager.logger.warn("[TESTENTER] [enterServer1] [name:"+name+"\n] "+stacktrace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long enterTime = SystemTime.currentTimeMillis();
		this.offlineTimeCount();
		lastDecreaseEvilTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		hasAuth = null;
		setOnlineTimeOneDay(0);
		setEnterServerTime(enterTime);
		setOnlineTimeOneDayClear(-1);
		if (System.currentTimeMillis() - this.getQuitGameTime() >= 60 * 1000) {
			// this.setShouStat(0);
			this.setSkillPoints(3);
		}
		setQuitGameTime(enterTime);
		
		try {
			if ((enterTime - this.getLoginServerTime() > ActivityManager.getInstance().oldplayerlength) && (this.getLevel() >= 150)) {
				ActivityManagers.getInstance().noticeOldPlayerBeforeLogin(this);
			}
			if(!TimeTool.instance.isSame(enterTime, this.getLoginServerTime(), TimeDistance.DAY)){
				setOnlineTimeOfDay(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setLoginServerTime(enterTime);

		SocialManager.getInstance().upOrDownNotice(this, true);
			// 给网关服务器发送消息，更新用户信息
			NEW_MIESHI_UPDATE_PLAYER_INFO info = new NEW_MIESHI_UPDATE_PLAYER_INFO(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), username, getId(), name, this.getSoulLevel(), this.getCurrSoul().getCareer(), (int) getRMB(), getVipLevel(), 1);
			MieshiGatewayClientService.getInstance().sendMessageToGateway(info);

		this.noticeBournTaskNum();

		GameStatClientService.statPlayerEnterServer(this);
		synchronized (transientEnemyList) {
			transientEnemyList.clear();
			transientEnemyPlayerAttackTime.clear();
		}
		通知装备上的技能();
		setGangContribution(getGangContribution());// 贡献度,由于enterServer没发此字段,enterServer的时候设置一下,其他set家族成员的贡献的时候都设置了这个值

		{
			// 通知客户端更改翻译后的地图显示名
			if (MultiLanguageTranslateManager.getInstance().isNeedTranslate()) {
				MIESHI_MAP_LANG_TRANSLATE req = new MIESHI_MAP_LANG_TRANSLATE(GameMessageFactory.nextSequnceNum(), allMapDisplayName, translateMapDisplayName);
				this.addMessageToRightBag(req);
				if (GamePlayerManager.logger.isWarnEnabled()) {
					GamePlayerManager.logger.warn(this.getLogString() + "[进入游戏] [通知翻译后的地图显示名字] [" + StringUtil.arrayToString(allMapDisplayName, ",") + "] [" + StringUtil.arrayToString(translateMapDisplayName, ",") + "]");
				}
			}
		}

		if (this.getActiveMagicWeaponId() > 0 && this.getCurrentGame() != null) { // 上线召唤法宝
			NPC mnpc = MemoryNPCManager.getNPCManager().getNPC(getActiveMagicWeaponId());
			if (mnpc != null && !getCurrentGame().contains(mnpc)) {
				this.getCurrentGame().addSprite(mnpc);
			}
		}

		if (GamePlayerManager.logger.isWarnEnabled()) {
			GamePlayerManager.logger.warn("[用户上线] [{}] [性别：{}] [门派：{}] [等级：{}]", new Object[] { getLogString(), (this.getSex() == 0 ? "男" : "女"), CareerManager.careerNames[this.getCareer()], this.getLevel() });
		}

		try {
			com.fy.engineserver.activity.everylogin.LoginActivityManager.getInstance().noticePlayerLogin(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventRouter.getInst().addEvent(new EventPlayerLogin(this));

		try {
			HuntLifeManager.tempIdList.remove(this.getId());
			HuntLifeManager.tempIdList2.remove(this.getId());
		} catch (Exception e) {

		}
		try {
			DigManager.getInstance().clearExtraDigInfo(this);
			TaskSubSystem.logger.error("[" + this.getLogString() + "] [清空玩家身上的挖宝信息]");
		} catch (Exception e) {
			TaskSubSystem.logger.error("[" + this.getLogString() + "] [清空玩家身上的挖宝信息出错]" + e);
			e.printStackTrace();
		}

		SpriteSubSystem.logger.warn("[角色进入游戏] [before]" + this.toString());
		try {
			UnitedServerManager.getInstance().dealwithDailyLoginActivity(this);
		} catch (Exception e) {
			UnitedServerManager.logger.error("处理合服后登录活动异常" + this.getLogString(), e);
		}
		SpriteSubSystem.logger.warn("[角色进入游戏] [end]" + this.toString());
		try {
			UnitedServerManager.getInstance().sendRiseAward(this);
		} catch (Exception e) {
			UnitedServerManager.logger.error("处理合服后崛起奖励活动异常" + this.getLogString(), e);
		}

		// SkEnhanceManager.getInst().skillPonitReturn(this);
		try {
			ArticleManager.logger.warn("[用户上线] " + this.getPlayerPropsString() + this.getPlayerEquipment());
		} catch (Exception e) {
			UnitedServerManager.logger.error("打印玩家属性log异常" + this.getLogString(), e);
		}

		if (!Game.isHiddenOpen) {
			this.hiddenAllPlayer = false;
			this.hiddenSameCountryPlayer = false;
			this.hiddenChatMessage = false;
		}
		hasNotice = true;
		try {
			initJiazuTitleAndIcon();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 在这里当玩家进入服务器时通知boss
		try {
			MiGuTradeServiceWorker.notifyBossPlayerEnter(this, MiGuTradeServiceWorker.OP_ENTER_SERVER_INFO);
		} catch (Exception e) {
			MiGuTradeServiceWorker.logger.error("[米谷交易] [向boss发送玩家信息] [失败] [出现未知异常] [" + this.getUsername() + "] [" + this.getName() + "] [" + this.getId() + "]", e);
		}
		try {
			ServerEventManager.getInstance().doEvent(this, EventType.玩家上线);
		} catch (Exception e) {
			e.printStackTrace();
			ServerEventManager.logger.warn("[服务器补偿事件] [玩家上线] [" + this.getLogString() + "] [异常：" + e + "]");
		}
		try {
			SocialManager.getInstance().clearTemporaryFriend(this);
		} catch (Exception e) {
			SocialManager.logger.warn("[清理玩家临时好友列表] [玩家上线] [" + this.getLogString() + "]", e);
		}
		this.setChestType();
		if (jiazuId > 0) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
			if (jiazu != null && jiazu.getJiazuMaster() == this.getId()) {
				Hashtable<Byte, Map<Integer, List<Long>>> prepareToFightJiazus = VillageFightManager.getInstance().vd.prepareToFightJiazus;
				if (prepareToFightJiazus != null) {
					Map<Integer, List<Long>> jiazuMap = prepareToFightJiazus.get(this.getCountry());
					if (jiazuMap != null) {
						for (Integer oreIndex : jiazuMap.keySet()) {
							if (jiazuMap.get(oreIndex) != null && jiazuMap.get(oreIndex).contains(jiazu.getJiazuID()) && jiazu.getPoint() < 0) {
								VillageFightManager.getInstance().玩家上线弹摇骰子界面(this, oreIndex);
							}
						}
					}
				}
			}
		}
		this.initClientInfo();
//		BossCityManager.getInstance().handerPlayerEnter(this);
//		if(bcity != null){
//			setGame(this.getResurrectionMapName());
//			setLastGame(this.getResurrectionMapName());
//			setX(this.getResurrectionX());
//			setY(this.getResurrectionY());
//			bcity = null;
//		}

		if(DownCityManager2.instance.cityMap.containsKey(new Long(getId()))){
			//单人多人副本分开
//			if(!DownCityManager2.instance.inTeamCityGame(this)){
				DownCityManager2.instance.cityMap.get(new Long(getId())).pMap().remove(new Long(getId()));
//			}else{
//				//多人副本
////				if(DownCityManager2.instance.isop){}
//				CityAction  city  = leavesDownCityManager2.instance.cityMap.get(new Long(getId()));
//				if(city == null || city.isEnd() || city.isDestroy()){
//					DownCityManager2.instance.cityMap.get(new Long(getId())).pMap().remove(new Long(getId()));
//					DownCityManager2.logger.warn("多人副本，副本不存在或者失效:"+getLogString());
//				}
//			}
		}
		handleLoginReward("登录");
//		ActivityManager.getInstance().handleTotleCostActivityQuery(this,"登录服务器");
		ActivityManagers.getInstance().loginReward(this);
		ChargeManager.getInstance().handleLoginReward(this);
		handleTotleTimeReward();
		ActivityManagers.getInstance().sendPlayerInfoToUC(this,2);
		if (getUsername().equals("wtx062") && GameConstants.getInstance().getServerName().equals("客户端测试")) {
			try {
				String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
				GamePlayerManager.logger.warn("[TESTENTER] [enterServer2] [name:"+name+"\n] "+stacktrace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		CoreSubSystem.getInstance().sendInfoToLeHiHi(this,1);
	}
	
	public void handleTotleTimeReward(){
		if(!TimeTool.instance.isSame(System.currentTimeMillis(), getLastRewardTime(), TimeDistance.DAY)){
			setRewardState(new int[]{0,0,0,0});
		}
	}

	// 玩家上线通知客户端完成的境界任务数
	private void noticeBournTaskNum() {
		this.modifyDailyChangevalue();
		NOTICE_DELIVER_BOURN_TASK_NUM_REQ req = new NOTICE_DELIVER_BOURN_TASK_NUM_REQ(GameMessageFactory.nextSequnceNum(), this.getDeliverBournTaskNum());
		this.addMessageToRightBag(req);

	}

	transient NumberFormat format = new DecimalFormat(".00");

	public String getWinProp() {
		String winprop = "--";
		if (this.pkAllNum > 0) {
			double c = (double) this.pkWinNum / this.pkAllNum * 100;
			winprop = format.format(c) + "%";
			if (this.pkWinNum == 0) {
				winprop = "0.00%";
			}
		}
		return winprop;
	}

	public void 通知装备上的技能() {
		Soul[] ss = this.getSouls();
		if (ss != null) {
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			for (Soul soul : ss) {
				if (soul != null) {
					EquipmentColumn ec = soul.getEc();
					if (ec != null && ec.getEquipmentIds() != null) {
						for (long id : ec.getEquipmentIds()) {
							if (id > 0) {
								ArticleEntity ae = aem.getEntity(id);
								if (ae != null) {
									Article a = am.getArticle(ae.getArticleName());
									if (a instanceof Equipment) {
										if (((Equipment) a).getSkillId() > 0) {
											EQUIPMENT_SKILL_REQ req = new EQUIPMENT_SKILL_REQ(GameMessageFactory.nextSequnceNum(), ((Equipment) a).getSkillId(), soul.getSoulType(), (byte) 0);
											this.addMessageToRightBag(req);
											CareerManager cm = CareerManager.getInstance();
											if (cm != null) {
												Skill skill = cm.getSkillById(((Equipment) a).getSkillId());
												if (skill != null) {
													SkillInfo si = new SkillInfo();
													si.setInfo(this, skill);
													QUERY_CAREER_INFO_BY_ID_RES qciRes = new QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
													this.addMessageToRightBag(qciRes);
													NEW_QUERY_CAREER_INFO_BY_ID_RES NEW_qciRes = new NEW_QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
													this.addMessageToRightBag(NEW_qciRes);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	// 累计在线时间
	private long durationOnline;

	public long getDurationOnline() {
		return durationOnline;
	}

	public void setDurationOnline(long durationOnline) {
		setDirty(true, "durationOnline");
		this.durationOnline = durationOnline;
	}

	private transient long enterServerTime;

	private transient long onlineTimeOneDayClear;

	public long getOnlineTimeOneDayClear() {
		return onlineTimeOneDayClear;
	}

	public void setOnlineTimeOneDayClear(long onlineTimeOneDayClear) {
		this.onlineTimeOneDayClear = onlineTimeOneDayClear;
	}

	public long getEnterServerTime() {
		return enterServerTime;
	}

	public void setEnterServerTime(long enterServerTime) {
		this.enterServerTime = enterServerTime;
	}

	private transient long onlineTimeOneDay;

	public long getOnlineTimeOneDay() {
		return onlineTimeOneDay;
	}

	public void setOnlineTimeOneDay(long onlineTimeOneDay) {
		this.onlineTimeOneDay = onlineTimeOneDay;
	}

	
	
	/**
	 * 玩家下线
	 */
	public void leaveServer() {

		// 清空记录玩家的心跳
		synchronized (clientHeartBeatRequestList) {
			this.clientHeartBeatRequestList.clear();
		}

		long now = SystemTime.currentTimeMillis();
		long thisOnlineTime = now - getQuitGameTime();
		String serverName = GameConstants.getInstance().getServerName();
			// 给网关服务器发送消息，更新用户信息
			NEW_MIESHI_UPDATE_PLAYER_INFO update_player_info = new NEW_MIESHI_UPDATE_PLAYER_INFO(GameMessageFactory.nextSequnceNum(), serverName, username, getId(), name, this.getSoulLevel(), this.getCurrSoul().getCareer(), (int) getRMB(), getVipLevel(), 2);
			MieshiGatewayClientService.getInstance().sendMessageToGateway(update_player_info);

		if (getConn() != null) {
			String md5 = (String) conn.getAttachmentData(SecuritySubSystem.MACADDRESS);
			if (md5 == null) {
				md5 = "";
			}
			NOTIFY_USER_LEAVESERVER_REQ playerLeaveServer = new NOTIFY_USER_LEAVESERVER_REQ(GameMessageFactory.nextSequnceNum(), serverName, username, md5, conn.getIdentity().split(":")[0]);
			MieshiGatewayClientService.getInstance().sendMessageToGateway(playerLeaveServer);
		}
		// 玩家持续在线时长
		if (getEnterServerTime() > 0) {
			setDurationOnline(now - getEnterServerTime() + getDurationOnline());
			if (SocialManager.logger.isWarnEnabled()) {
				SocialManager.logger.warn("[修改持续在线时间] [" + this.getLogString() + "] [now:" + now + "] [enterTime:" + getEnterServerTime() + "] [持续时间:" + getDurationOnline() + "] [这次时间:" + (now - getEnterServerTime()) + "] [connState:"+(getConn()!=null?getConn().getStateString(getConn().getState()):"null")+"]");
			}
		} else {
			if (SocialManager.logger.isWarnEnabled()) {
				SocialManager.logger.warn("[修改持续在线时间错误] [没有登录时间] [" + this.getLogString() + "]");
			}
		}

		if (getEnterServerTime() > 0) {
			if (com.fy.engineserver.util.Utils.checkSameDay(this.getEnterServerTime(), now)) {
				setOnlineTimeOneDay(now - getEnterServerTime());
				setOnlineTimeOfDay(now - getEnterServerTime() + getOnlineTimeOfDay());
			} else {
				setOnlineTimeOfDay(0);
				if (onlineTimeOneDayClear > 0) {
					setOnlineTimeOneDay(now - onlineTimeOneDayClear);
				} else {
					setOnlineTimeOneDay(now - getEnterServerTime());
					SocialManager.logger.error("[统计隔天在线时间下线] [" + this.getLogString() + "] [离线时间时间:" + this.getEnterServerTime() + "] [时长:" + (now - getEnterServerTime()) + "] [标志位:" + onlineTimeOneDayClear + "]");
				}
			}
			setEnterServerTime(now);
			onlineTimeOneDayClear = -1;
			setQuitGameTime(now);

			GameStatClientService.statPlayerLeaveServer(this, this.getOnlineTimeOneDay());
			SocialManager.logger.error("[统计在线时间] [" + this.getLogString() + "] [离线时间时间:" + this.getEnterServerTime() + "] [时长:" + this.getOnlineTimeOneDay() + "] [connState:"+(getConn()!=null?getConn().getStateString(getConn().getState()):"null")+"]");
		} else {
			SocialManager.logger.error("[统计在线时间错误] [" + this.getLogString() + "] [离线时间时间:" + this.getEnterServerTime() + "] [时长:" + this.getOnlineTimeOneDay() + "]");
		}

		SocialManager.getInstance().upOrDownNotice(this, false);

		if (!leavedServer) {

			// // 通知boss角色下线
			// try {
			// // 防止玩家删除后，仍然通知BOSS
			// if (PlayerManager.getInstance().getPlayer(this.id) != null) {
			// BossClientService.getInstance() playerLogout(username, name,
			// this.getCountry(), new Date(quitGameTime));
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// GameManager.logger.error("[通知boss角色下线] [异常]", e);
			// }

			// 如果在队伍，离开队伍
			if (getTeam() != null) {
				Team team = getTeam();
				team.removeMember(this, 1);
			}

			// 处理下线消失的buff
			synchronized (buffs) {
				for (int i = buffs.size() - 1; i >= 0; i--) {
					Buff buff = buffs.get(i);

					if (buff != null && buff.getTemplate() != null) {
						if (ActiveSkill.logger.isWarnEnabled()) ActiveSkill.logger.warn("[BUFF] [{}] [下线] [{}:{}] [time:{}] [bufftype:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime(), buff.getTemplate().getTimeType() });
						if (buff.getTemplate().getTimeType() == 2 || buff.isForover()) {
							buff.end(this);
							if (buff.isForover() || buff.isSyncWithClient()) {
								this.removedBuffs.add(buff);
							}
							buffs.remove(i);
							setDirty(true, "buffs");

							if (ActiveSkill.logger.isDebugEnabled()) {
								ActiveSkill.logger.debug("[下线去除BUFF] [{}] [下线] [{}:{}] [time:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime() });
							}

						}
					}
				}
			}

			// 处理仙灵大会副本
//			try {
//				XianLingManager.instance.playerLeave(this);
//			} catch (Exception e) {
//				XianLingManager.logger.error("[仙灵] [玩家下线踢出仙灵副本] [异常]", e);
//				e.printStackTrace();
//			}

			try {
				PlayerManager.getInstance().leaveServerNotify(this);
				if (this.getCurrentGame() != null) {
					leaveGame();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			this.leavedServer = true;

			String gameName = "";

			if (this.getCurrentGame() != null) {
				gameName = this.getCurrentGame().gi.name;
			} else {
				gameName = this.getLastGame();
			}
			String channelName = "";
			String channelKey = "";
			if (passport != null) {
				channelName = passport.getRegisterChannel();
				channelKey = passport.getRegisterChannel();
			}

			if (this.lastUpdateStatDataTime > 0) {
				try {
					long ct = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					long t = ct - this.lastUpdateStatDataTime;
					if (t < 0) {
						t = 0;
					}
					if (t < 300000) {
						StatDataUpdateManager sdum = StatDataUpdateManager.getInstance();
						if (sdum != null) {
							sdum.update(this, StatData.STAT_ONLINE_TIME, t, ct);
							sdum.update(this, StatData.STAT_CURRENT_LEVEL_ONLINE_TIME, t, ct);
							sdum.updateCurrentDayOnlineTime(this, t, ct);
							sdum.update(this, StatData.STAT_ONLINE_DAYS, 1, ct);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 更新最后访问时间相关
//			this.updateLastOnlineTimeForCave(now);
			this.updateLastOnlineTimeForJiazu(now);

			/**
			 * 清除国战通知状态
			 */
			Guozhan guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(this);
			if (guozhan == null && this.isGuozhanLoginNotified()) {
				this.guozhanLoginNotified = false;
			}

			/* 清除宝珠兑换活动的discatch记录 */
			ActivityManagers.getInstance().getDdc().remove(this.getId() + "用于宝珠兑换活动");

			RefreshBoxManager.getInstance().leaveActivity(this);
			if (GamePlayerManager.logger.isWarnEnabled()) {
				GamePlayerManager.logger.warn("[用户下线] [{}] [性别：{}] [门派：{}] [等级：{}] [在线时间：{}] [{}] [{}]", new Object[] { getLogString(), (this.getSex() == 0 ? "男" : "女"), CareerManager.careerNames[this.getCareer()], this.getLevel(), (this.quitGameTime - this.loginServerTime),this.isBoothState(),(getConn()!=null?this.getConn().getStateString(this.getConn().getState()):"null") });
			}

			/**
			 * 宠物迷城副本进度
			 */
			if (PetDaoManager.getInstance().isPetDao(gameName)) {
				try {
					PetDao pt = PetDaoManager.getInstance().getPetDao(this, gameName);
					if (pt != null) {
						pt.getMc().setSTAT(1);
						PetDaoManager.getInstance().savePetDao(this, pt);
						PetDaoManager.log.warn("[宠物迷城心跳] [结束] [原因：玩家离开游戏] [剩余时间：" + pt.getMc().getContinuetime() + "] [" + pt.getMc().getP().getName() + "] [箱子剩余数量:" + pt.getMc().getBoxovernum() + "] [钥匙剩余数量:" + pt.getMc().getKeyovernum() + "] [已开始钥匙：" + pt.getMc().getKeyrefreshnum() + "]");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
//			try {
//				JJCManager.getInstance().notifyEnterGame(this);
//				JJCManager.getInstance().noticeTeamMember(this,"玩家下线",this.getId());
//				JJCManager.getInstance().handlerLeaveTeamInfo(this);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			SkEnhanceManager.getInst().clearskillnums(this);
			try {
				if (WolfManager.getInstance().isWolfGame(this)) {
					WolfManager.getInstance().playerLeave(this,"离开游戏");
				}
				// DiceManager.getInstance().playerLeave(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.noticeShowButton = false;
//			if(room != null){
//				setGame(this.getResurrectionMapName());
//				setLastGame(this.getResurrectionMapName());
//				setX(this.getResurrectionX());
//				setY(this.getResurrectionY());
//				room.leaveRoom(this.getId());
//				room = null;
//			}
//			if(bcity != null){
//				setGame(this.getResurrectionMapName());
//				setLastGame(this.getResurrectionMapName());
//				setX(this.getResurrectionX());
//				setY(this.getResurrectionY());
//				bcity = null;
//			}
			if(DownCityManager2.instance.cityMap.containsKey(new Long(getId()))){
//				if(!DownCityManager2.instance.inTeamCityGame(this)){
					DownCityManager2.instance.cityMap.get(new Long(getId())).pMap().remove(new Long(getId()));
					String citymap = TransportData.getMainCityMap(this.getCountry());
					Game chuanCangGame = GameManager.getInstance().getGameByName(citymap, getCountry());
					MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
					setLastGame(citymap);
					setX(area.getX());
					setY(area.getY());
//				}else{
//					//多人副本
////					if(DownCityManager2.instance.isop){}
//					CityAction  city  = DownCityManager2.instance.cityMap.get(new Long(getId()));
//					if(city == null || city.isEnd() || city.isDestroy()){
//						DownCityManager2.instance.cityMap.get(new Long(getId())).pMap().remove(new Long(getId()));
//						DownCityManager2.logger.warn("离开游戏，多人副本，副本不存在或者失效:"+getLogString());
//					}
//				}
			}
		}
		CoreSubSystem.getInstance().sendInfoToLeHiHi(this,3);
	}

	/**
	 * 加生命值，由caster对自己加生命值
	 */
	public void enrichHP(Fighter caster, int hp, boolean baoji) {

		if (this.isDeath()) {
			return;
		}

		if (this.isCanNotIncHp()) {
			return;
		}

		int hp2 = this.getHp() + hp;
		if (hp2 > this.getMaxHP()) {
			this.setHp(this.getMaxHP());
		} else {
			this.setHp(hp2);
		}

		// 加血
		if (caster != this) {
			if (baoji) {
				NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, getId(), (byte) Event.HP_INCREASED_BAOJI, hp);
				addMessageToRightBag(req);
			} else {
				NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, getId(), (byte) Event.HP_INCREASED, hp);
				addMessageToRightBag(req);
			}
		}

		if (this.isFighting() && currentGame != null) {
			Fighter los[] = this.currentGame.getVisbleFighter(this, false);
			for (int i = 0; i < los.length; i++) {
				if (los[i] instanceof Monster) {
					Monster m = (Monster) los[i];
					if (m.getOwner() != null && m.getOwner().isSameTeam(caster)) {
						m.notifyHPAdded(this, caster, hp);
					}
				}
			}
		}

		// 副本统计
		Game game = this.currentGame;
		if (game != null && game.getDownCity() != null) {
			DownCity dc = game.getDownCity();
			dc.statPlayerBeEnrichHP(this, hp);
		}
	}

	/**
	 * 给目标加生命值，有自己对别人加生命值
	 */
	public void enrichHPFeedback(Fighter target, int hp, boolean baoji) {
		if (target instanceof Player) {
			// 加血
			if (baoji) {
				NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, target.getId(), (byte) Event.HP_INCREASED_BAOJI, hp);
				addMessageToRightBag(req);
			} else {
				NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, target.getId(), (byte) Event.HP_INCREASED, hp);
				addMessageToRightBag(req);
			}

		} else {
			// 加血
			if (baoji) {
				NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, target.getId(), (byte) Event.HP_INCREASED_BAOJI, hp);
				addMessageToRightBag(req);
			} else {
				NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, target.getId(), (byte) Event.HP_INCREASED, hp);
				addMessageToRightBag(req);
			}
		}
		// 副本统计
		Game game = this.currentGame;
		if (game != null && game.getDownCity() != null) {
			DownCity dc = game.getDownCity();
			dc.statPlayerEnrichHP(this, hp);
		}

		// 通知战场，我给别人治疗了
		if (this.isInBattleField()) {
			battleField.notifyEnhenceHp(this, target, hp);
		}
	}

	/**
	 * 通知玩家被攻击
	 * 
	 * @param player
	 *            攻击者
	 * @param skillName
	 * @param skillLevel
	 * @param damageType
	 * @param damage
	 */
	public void notifyAttack(Player player, String skillName, int skillLevel, int damageType, int damage) {
	}

	/**
	 * 通知玩家被攻击
	 * 
	 * @param player
	 *            攻击者
	 * @param skillName
	 * @param skillLevel
	 * @param damageType
	 * @param damage
	 */
	public void notifyAttack(Pet pet, String skillName, int skillLevel, int damageType, int damage) {
	}

	private transient int offsetPercent = 0;

	/**
	 * 天劫雷劈 无视所有防御护甲等--只有特殊道具(buff)可以抵挡
	 * 
	 * @param damage
	 * @param damageType
	 */
	public byte causeDamageByRayRobbery(int damage) {
		int dd = (int) (damage * ((100 - getOffsetPercent()) * 0.01));
		byte result = 0;
		if (hp > dd) {
			setHp(hp - dd);
		} else {
			setHp(0);
			result = -1;
		}
		// 掉耐久
		EquipmentColumn ec = ecs;
		ec.beAttacked();
		check4Buffs();
		NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, getId(), (byte) Event.HP_DECREASED_SPELL_BAOJI, dd);
		addMessageToRightBag(req);
		if (deathNotify == false && this.getHp() <= 0 && this.getState() != Player.STATE_DEAD) {
			deathNotify = true;
		}
		return result;
	}

	/**
	 * 渡劫完成后需要清除渡劫中所加的buff
	 */
	public void checkAndClearRobberyBuff() {
		synchronized (buffs) {
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				for (int j = 0; j < RobberyConstant.dujieBuffs.length; j++) {
					if (buff.getClass().getSimpleName().equals(RobberyConstant.dujieBuffs[j])) {
						buff.end(this);
						buffs.remove(buff);
						setDirty(true, "buffs");
						TransitRobberyManager.logger.info("减少雷伤buff消失");
					}
				}
			}
		}
	}

	/**
	 * 检测玩家身上是否有指定buff
	 * @param buffName
	 * @return
	 */
	public boolean isThisBuffAct(String buffName) {
		for (int i = buffs.size() - 1; i >= 0; i--) {
			Buff buff = buffs.get(i);
			for (int j = 0; j < RobberyConstant.dujieBuffs.length; j++) {
				if (buff != null && buff.getTemplateName().trim().equals(buffName) && buff.getInvalidTime() > com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 使用次数检测---目前只用于减少雷伤的buff
	 */
	private byte check4Buffs() {
		synchronized (buffs) {
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				if (buff != null && buff instanceof Buff_RayDamage) {
					Buff_RayDamage b = (Buff_RayDamage) buff;
					if (!b.checkBuffAct(this)) {
						buff.end(this);
						buffs.remove(buff);
						setDirty(true, "buffs");
						TransitRobberyManager.logger.info("减少雷伤buff消失");
					}
				} else if (buff != null && buff instanceof Buff_didangshanghai) {
					Buff_didangshanghai d = (Buff_didangshanghai) buff;
					if (d.checkBuffAct(this)) {
						return 2; // 成功抵挡
					} else {
						buff.end(this);
						buffs.remove(buff);
						setDirty(true, "buffs");
						TransitRobberyManager.logger.info("抵挡伤害buff消失");
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 摘取果实伴生buff
	 * @return
	 */
	public void notifyPickFruit() {
		synchronized (buffs) {
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				if (buff != null && buff instanceof Buff_StealFruit) {
					Buff_StealFruit b = (Buff_StealFruit) buff;
					b.checkBuffAct(this);
				}
			}
		}
	}

	public boolean isPickFruitBuffAct() {
		for (int i = buffs.size() - 1; i >= 0; i--) {
			Buff buff = buffs.get(i);
			if (buff != null && buff instanceof Buff_StealFruit) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 造成伤害，由caster造成对自己的伤害 damage为伤害值 damageType为伤害的类型，包括
	 * 物理伤害，法术伤害，物理暴击，法术暴击，闪避
	 * 
	 * 
	 * @param caster
	 * @param damage
	 * @param damageType
	 */
	public void causeDamage(Fighter caster, int damage, int hateParam, int damageType) {

		Logger log = Skill.logger;
		if (caster.getFightingType(this) != Fighter.FIGHTING_TYPE_ENEMY) {
			log.debug("Player.causeDamage: not math.");
			return;
		}
		if (this.canFreeFromBeDamaged(caster)) {
			if (caster instanceof Player || caster instanceof Pet) {
				log.debug("[Player in safe aera] [" + caster.getName() + "_" + caster.getId() + "] [" + this.getLogString() + "]");
				return;
			}
		}
		if(ownMonthCard(CardFunction.世界boss不死buff)){
			if(room != null && room.getIds().contains(new Long(getId()))){
				log.debug("[全民boss不死buff] [" + caster.getName() + "_" + caster.getId() + "] [damage:"+damage+"] [" + this.getLogString() + "]");
				return;
			}
		}

		// pk相关
		攻击玩家后对攻击者的操作(caster);
		// 只用于反击
		if (!this.isInBattleField() && !ChestFightManager.inst.isPlayerInActive(this)) {
				if (caster instanceof Player || caster instanceof Pet) {
					synchronized (transientEnemyList) {
						if (!transientEnemyList.contains(caster)) {
							transientEnemyList.add(caster);
							TRANSIENTENEMY_CHANGE_REQ req = new TRANSIENTENEMY_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, (caster instanceof Player ? (byte) 0 : (byte) 1), caster.getId());
							this.addMessageToRightBag(req);
						}
						transientEnemyPlayerAttackTime.put(caster.getId(), System.currentTimeMillis());
						if (caster instanceof Pet) {
							Player casterOwner = ((Pet) caster).getMaster();
							if (casterOwner != null) {
								if (!transientEnemyList.contains(casterOwner)) {
									transientEnemyList.add(casterOwner);
									TRANSIENTENEMY_CHANGE_REQ req = new TRANSIENTENEMY_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, (casterOwner instanceof Player ? (byte) 0 : (byte) 1), casterOwner.getId());
									addMessageToRightBag(req);
								}
								transientEnemyPlayerAttackTime.put(casterOwner.getId(), System.currentTimeMillis());
							}
						}
					}
				}
		}

		// 增加敌人
		if (this.isDeath() == false && caster instanceof LivingObject) enemyList.add(caster, false, this.heartBeatStartTime);

		int ddd = damage;
		if (ddd > hp) {
			ddd = hp;
		}

		if (this.isIsGuozhan()) {
			if (caster instanceof Player) {
				Player p = (Player) caster;
				if (p.isGuozhan && p.getCountry() != this.getCountry()) {
					Guozhan guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(p);
					if (guozhan != null && (guozhan.getAttacker().getCountryId() == this.country || guozhan.getDefender().getCountryId() == this.country) && (guozhan.getAttacker().getCountryId() == p.getCountry() || guozhan.getDefender().getCountryId() == p.getCountry())) {
						guozhan.notifyDamage(p, damage);
					}
				}
			}
		}
		
		if ((caster instanceof Player) && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			Player p = (Player) caster;

			if (p.getState() != Player.STATE_DEAD && p.getMpStealPercent() > 0) {
				if (p.getMp() < p.getMaxMP()) {
					int mp = p.getMp() + p.getMpStealPercent() * ddd / 100;
					if (mp > p.getMaxMP()) mp = p.getMaxMP();
					p.setMp(mp);
					if (Skill.logger.isDebugEnabled()) {
						Skill.logger.debug("[测试===========] [mp:" + mp + "] [ddd:" + ddd + "] [percent:" + p.getMpStealPercent() + "]");
					}
					// 加蓝，是否要通知客户端？ // debug dot
					//
					// NOTIFY_EVENT_REQ req = new
					// NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(),
					// (byte) 0, p
					// .getId(), (byte) Event.MP_INCREASED,
					// p.getMpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);

				} else {
					// debug dot
					// NOTIFY_EVENT_REQ req = new
					// NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(),
					// (byte) 0, p
					// .getId(), (byte) Event.MP_INCREASED,
					// p.getMpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);

				}
			}

			if (p.getState() != Player.STATE_DEAD && p.getHpStealPercent() > 0) {
				if (p.getHp() < p.getMaxHP()) {
					int mp = p.getHp() + p.getHpStealPercent() * ddd / 100;
					if (mp > p.getMaxHP()) mp = p.getMaxHP();
					if (p.isCanNotIncHp()) {
						if (Skill.logger.isDebugEnabled()) Skill.logger.debug("[无法回血状态] [屏蔽--] [" + p.getLogString() + "] [血]");
					} else {
						p.setHp(mp);
					}
					// 加血，是否要通知客户端？debug dot

					// NOTIFY_EVENT_REQ req = new
					// NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(),
					// (byte) 0, p
					// .getId(), (byte) Event.HP_INCREASED,
					// p.getHpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);
				} else {
					// debug dot
					// NOTIFY_EVENT_REQ req = new
					// NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(),
					// (byte) 0, p
					// .getId(), (byte) Event.HP_INCREASED,
					// p.getHpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);
				}
			}

			try {
				if (this.level > 220) { // 仙界才判断
					for (Skill s : CareerManager.getInstance().passiveTriSkills) {
						if (!(s instanceof PassiveTriggerImmune)) {
							Skill.logger.warn("[仙界被动技能] [passiveTriSkills中出现了非仙界技能] [" + s.getId() + "] [" + this.getLogString() + "]");
							continue;
						}
						((PassiveTriggerImmune) s).causeDamage(this);
					}
				}
			} catch (Exception e) {
				Skill.logger.error("[被动触发buff技能] [异常] [" + this.getLogString() + "]", e);
			}
		}
		if ((caster instanceof Pet) && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			Pet p = (Pet) caster;
			if (p.getState() != Player.STATE_DEAD && p.getHpStealPercent() > 0) {
				if (p.getHp() < p.getMaxHP()) {
					int mp = p.getHp() + p.getHpStealPercent() * ddd / 100;
					if (mp > p.getMaxHP()) mp = p.getMaxHP();
					p.setHp(mp);
					Skill.logger.debug("Monster.causeDamage: 宠物吸血 {}", p.getName());
					// 加蓝，是否要通知客户端？

					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd
					// / 100);
					// p.addMessageToRightBag(req);
				} else {
					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd
					// / 100);
					// p.addMessageToRightBag(req);
				}
			}
		}
		if (this.ironMaidenPercent > 0 && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			int iron = ddd * this.ironMaidenPercent / 100;
			if (iron > 0) {
				if (caster instanceof Pet) {
					iron = ((Pet) caster).checkInjuryAndPosiDamage(iron);
				}
				caster.causeDamage((Fighter) this, iron, hateParam, Fighter.DAMAGETYPE_FANSHANG);
				this.damageFeedback(caster, iron, hateParam, Fighter.DAMAGETYPE_FANSHANG);
			}
		}
		if (check4Buffs() == 2) { // 抵消攻击buff是否存在
			log.debug("Player.causeDamage: damge to zero.");
			damage = 0;
		}
		try {
			if (caster instanceof Monster) { // 渡劫四象火神特殊需求，减boss对玩家造成的伤害结果,只有在渡劫中，火神才会有效
				Monster m = (Monster) caster;
				if (m.getSpriteCategoryId() == RobberyConstant.HUOSHENID && TransitRobberyEntityManager.getInstance().火神玄魄生效(this.getId())) {
					damage = damage * RobberyConstant.decreasePers / 100;
				}
			}
		} catch (Exception e) {
			TransitRobberyManager.logger.error("判断四象火神减伤出错，e{},玩家{}", e, this.getLogString());
		}
		if (decreaseDmgRate > 0) {
			int decrease = (int) (damage * (decreaseDmgRate / 1000f));
			if (decrease > 0) {
				damage -= decrease;
			}

		}
//		damage = HunshiManager.getInstance().getDamage(this, damage);
		if (EnchantEntityManager.logger.isDebugEnabled()) {
			EnchantEntityManager.logger.debug("[测试护盾] [damage:" + damage + "] [damageType:" + damageType + "] [huDunDamage:" + huDunDamage + "] [" + this.getLogString() + "]");
		}
		if (damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_XISHOU) {
			if (huDunDamage > 0) {
				if (huDunDamage >= damage) {
					setHuDunDamage(huDunDamage - damage);
					damage = 0;
					damageType = DAMAGETYPE_XISHOU;
				} else {
					damage -= huDunDamage;
					setHuDunDamage(0);
					if (EnchantEntityManager.logger.isDebugEnabled()) {
						EnchantEntityManager.logger.debug("[测试护盾] [护盾消失后] [damage:" + damage + "] [damageType:" + damageType + "] [huDunDamage:" + huDunDamage + "] [" + this.getLogString() + "]");
					}
				}
			}
			float tempcasuerHprate = 0;
			float temptargetHprate = 0;
			if (this.getRecoverHpHuDun() <= 0) {
				if (hp > damage) {
					setHp(hp - damage);
				} else {
					setHp(0);
				}
			} else if (this.getRecoverHpHuDun() >= damage) { // 吸收伤害并且
				this.setRecoverHpHuDun(this.getRecoverHpHuDun() - damage);
				if (hp + damage > maxHP) {
					setHp(maxHP);
				} else {
					setHp(hp + damage);
				}
				damageType = DAMAGETYPE_XISHOU;
			} else {
				damage = damage - this.getRecoverHpHuDun();
				this.setRecoverHpHuDun(0);
				if (hp > damage) {
					setHp(hp - damage);
				} else {
					setHp(0);
				}
			}
		}
		if (TransitRobberyManager.logger.isInfoEnabled() && this.getHp() <= 0 && caster != null) {
			TransitRobberyManager.logger.info("[玩家被杀死] [击杀者id:" + caster.getId() + "] [击杀者名:" + caster.getName() + "] [被杀人:" + this.getLogString() + "] [伤害:" + damage + "] [damageType:" + damageType + "]" + " [caster:" + caster + "] [" + caster.getPhyAttack() + "] [caster.getMagicAttack():" + caster.getMagicAttack() + "] [caster.getWindAttack():" + caster.getWindAttack() + "] [caster.getThunderAttack():" + caster.getThunderAttack() + "]" + " [caster.getFireAttack():" + caster.getFireAttack() + "] [caster.getBlizzardAttack():" + caster.getBlizzardAttack() + "]");
		}

		if (EnchantEntityManager.logger.isDebugEnabled()) {
			EnchantEntityManager.logger.debug("[测试护盾] [计算护盾后] [damage:" + damage + "] [damageType:" + damageType + "] [huDunDamage:" + huDunDamage + "] [" + this.getLogString() + "]");
		}

		if (hp > 0 && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_XISHOU && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_FANSHANG) {
			this.checkPassiveEnchant(EnchantEntityManager.被攻击);
		}

		if (this.isInBattleField()) {
			battleField.notifyCauseDamageOfReal(caster, ddd);
		}

		if (damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_XISHOU) {
			// 副本统计
			Game game = this.currentGame;
			if (game != null && game.getDownCity() != null) {
				DownCity dc = game.getDownCity();
				dc.statPlayerBeAttack(this, damageType, damage);
			}
		}
		
		// 掉耐久
		EquipmentColumn ec = ecs;
		ec.beAttacked();

		if (caster instanceof Player) {
			((Player) caster).tempDamage = damage;
		}

		byte targetType = 0;
		NOTIFY_EVENT_REQ req = null;
		switch (damageType) {
		case DAMAGETYPE_PHYSICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_PHYSICAL, damage);
			break;
		case DAMAGETYPE_SPELL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_SPELL, damage);
			break;
		case DAMAGETYPE_PHYSICAL_CRITICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_PHYSICAL_BAOJI, damage);
			break;
		case DAMAGETYPE_SPELL_CRITICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_SPELL_BAOJI, damage);
			break;
		case DAMAGETYPE_DODGE:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.DODGE, damage);
			break;
		case DAMAGETYPE_MISS:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.MISS, damage);
			break;
		case DAMAGETYPE_ZHAONGDU:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_ZHONGDU, damage);
			break;
		case DAMAGETYPE_FANSHANG:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_FANSHANG, damage);
			break;
		case DAMAGETYPE_MIANYI:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_MIANYI, damage);
			break;
		case DAMAGETYPE_XISHOU:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_XISHOU, damage);
			break;
		}
		if (req != null) {
			addMessageToRightBag(req);
		}
		if (req != null && this.isInBattleField() && this.getDuelFieldState() == 1) {
			BattleField bf = this.getBattleField();
			if (bf != null) {
				Player[] ps = bf.getPlayersBySide(BattleField.BATTLE_SIDE_C);
				for (Player p : ps) {
					NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), req.getTargetType(), req.getTargetId(), req.getEventType(), req.getEventData());
					p.addMessageToRightBag(req2);
				}
			}
		}

		if (deathNotify == false && this.getHp() <= 0 && this.getState() != Player.STATE_DEAD) {
			deathNotify = true;
			boolean guozhanKill = false;
			boolean isInSiFang = false;
//			if (SiFangManager.getInstance().isInSiFangGame(this)) {
//				// 在四方神兽活动中
//				isInSiFang = true;
//				SiFangManager.getInstance().notifyKilled(this, caster);
//			}

//			if(DownCityManager2.instance.inCityGame(this)){
//				isInSiFang = true;
//			}	
			if(BossCityManager.getInstance().isBossCityGame(this)){
				isInSiFang = true;
			}
			if(this.getCurrentGame() != null && this.getCurrentGame().gi != null){
				if(this.getCurrentGame().gi.name.equals("zhanmotianyu")){
					isInSiFang = true;
				}
			}
			if (this.isInBattleField()) {
				// 战场中，通知战场，玩家被杀死
				this.battleField.notifyKilling(caster, this);
			} else {
				if (caster instanceof Player) {
					Player p = (Player) caster;
					if (p.getCountry() != this.getCountry()) {
						this.send_HINT_REQ(Translate.text_5823 + p.getName() + Translate.text_5824);
						p.send_HINT_REQ(Translate.text_5825 + this.getName() + "]!");
						if (Game.logger.isWarnEnabled()) Game.logger.warn("[玩家被杀] [被玩家杀死] [玩家：{}] [玩家ID：{}] [击杀者：{}] [击杀者ID：{}] [击杀者等级：{}]", new Object[] { this.getName(), this.getId(), p.getName(), p.getId(), p.getLevel() });
						// 谁把我杀了，就加入仇人列表
						SocialManager.getInstance().addChouren(this, p.getId());
						if (this.isIsGuozhan() && p.isIsGuozhan()) {
							Guozhan guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(this);
							if (guozhan != null && (guozhan.getAttacker().getCountryId() == this.country || guozhan.getDefender().getCountryId() == this.country) && (guozhan.getAttacker().getCountryId() == p.getCountry() || guozhan.getDefender().getCountryId() == p.getCountry())) {
								guozhan.notifyKill(p, this);
								guozhanKill = true;
							}
						}
					}
				} else if (caster instanceof Pet) {
					long ownerId = ((Pet) caster).getOwnerId();
					if (ownerId > 0) {
						try {
							Player player = PlayerManager.getInstance().getPlayer(ownerId);
							if (player.getCountry() != this.getCountry()) {
								// this.send_HINT_REQ(Translate.text_5823 +
								// player.getName() + "的宠物" +
								// Translate.text_5824);
								this.send_HINT_REQ(translateString(你被敌对国家的xx的宠物击杀, new String[][] { { STRING_1, player.getName() } }));
								// player.send_HINT_REQ(Translate.你的宠物击杀了敌对阵营的 +
								// this.getName() + "]!");
								player.send_HINT_REQ(translateString(你的宠物击杀了敌对阵营的, new String[][] { { STRING_1, this.getName() } }));

								if (Game.logger.isWarnEnabled()) Game.logger.warn("[玩家被杀] [被玩家宠物杀死] [玩家：{}] [玩家ID：{}] [击杀者：{}] [击杀者ID：{}] [击杀者等级：{}]", new Object[] { this.getName(), this.getId(), player.getName(), player.getId(), player.getLevel() });
								// 谁把我杀了，就加入仇人列表
								SocialManager.getInstance().addChouren(this, player.getId());
								if (this.isIsGuozhan() && player.isIsGuozhan()) {
									Guozhan guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(this);
									if (guozhan != null && (guozhan.getAttacker().getCountryId() == this.country || guozhan.getDefender().getCountryId() == this.country) && (guozhan.getAttacker().getCountryId() == player.getCountry() || guozhan.getDefender().getCountryId() == player.getCountry())) {
										guozhan.notifyKill(player, this);
										guozhanKill = true;
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (!guozhanKill ){//&& !ChestFightManager.inst.isPlayerInActive(this)) {
						玩家杀死玩家后的惩罚(caster);
						if (!isInSiFang && ChatMessageService.getInstance().isSilence(this.getId()) != 2) {
							玩家被杀死后的掉落(caster);
						}
				}
			}

			// 计算连斩
			if (caster instanceof Player || caster instanceof Pet) {
				if (caster instanceof Player) {
					((Player) caster).杀人计算连斩数(this);
				} else if (caster instanceof Pet) {
					Pet p = (Pet) caster;
					if (p.getMaster() != null) {
						p.getMaster().杀人计算连斩数(this);
					}
				}
			}
			if (attackBiaoCheFlag != null) {
				attackBiaoCheFlag.clear();
			}
		}

		if (damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS) {
			if (this.timerTaskAgent != null) {
				this.timerTaskAgent.notifyBeAttacked();
			}
		}
	}
	
	
	public void add2transientEnemyList(Fighter f) {
		try {
			synchronized (transientEnemyList) {
				if (!transientEnemyList.contains(f)) {
					transientEnemyList.add(f);
					TRANSIENTENEMY_CHANGE_REQ req = new TRANSIENTENEMY_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, (f instanceof Player ? (byte) 0 : (byte) 1), f.getId());
					this.addMessageToRightBag(req);
				}
				transientEnemyPlayerAttackTime.put(f.getId(), System.currentTimeMillis());
				if (f instanceof Pet) {
					Player casterOwner = ((Pet) f ).getMaster();
					if (casterOwner != null) {
						if (!transientEnemyList.contains(casterOwner)) {
							transientEnemyList.add(casterOwner);
							TRANSIENTENEMY_CHANGE_REQ req = new TRANSIENTENEMY_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, (casterOwner instanceof Player ? (byte) 0 : (byte) 1), casterOwner.getId());
							addMessageToRightBag(req);
						}
						transientEnemyPlayerAttackTime.put(casterOwner.getId(), System.currentTimeMillis());
					}
				}
			}
		} catch (Exception e) {
			if (Skill.logger.isInfoEnabled()) {
				Skill.logger.info("[增加反击列表] [异常] [" + f.getId() + "," + f.getName() + "] [" + this.getLogString() + "]", e);
			}
		}
	}

	/**
	 * 系统对怪造成伤害--只对怪物有效
	 * 
	 * @param target
	 * @param damage
	 */
	public void rayDamageFeedback(Fighter target, int damage) {
		if (target instanceof Monster) {
			NOTIFY_EVENT_REQ req = null;
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, target.getId(), (byte) Event.RAY_DAMAGE, damage);
			this.addMessageToRightBag(req);
		}
	}

	public transient int tempDamage; // 记录坐骑套装生效后的伤害值

	/**
	 * 造成别人伤害，由自己造成对target的伤害 damage为伤害值 damageType为伤害的类型，包括
	 * 物理伤害，法术伤害，物理暴击，法术暴击，闪避
	 * 
	 * @param caster
	 * @param damage
	 * @param damageType
	 */
	public void damageFeedback(Fighter target, int damage, int hateParam, int damageType) {

		try {
			if (target instanceof Player) {
				Player targetP = (Player) target;
				if (targetP.decreaseDmgRate > 0) {
					int decrease = (int) (damage * (targetP.decreaseDmgRate / 1000f));
					if (decrease > 0) {
						damage -= decrease;
					}
				}
			}
			if (target instanceof Pet) {
				Pet pet = (Pet) target;
				if (pet.talent1Skill == 110128 || pet.talent2Skill == 110128) {
					if (this.getSex() == 1) {
						damage *= 3;
					}
				}
				if (pet.talent1Skill == 110131 || pet.talent2Skill == 110131) {
					if (this.getSex() == 0) {
						damage *= 3;
					}
				}
				if (pet.talent1Skill == 110135 || pet.talent2Skill == 110135) {
					damage *= 2.5;
				}
				long now = System.currentTimeMillis();
								if (now - pet.lastReliveTime < Pet.InvincibleTime) {
									if (PetManager.logger.isDebugEnabled()) {
										PetManager.logger.debug("[宠物无敌] [通知攻击者] ["+this.getId()+"] [上次触发回血时间:" + pet.lastReliveTime + "]");
									}
									return ;
								}
			}
		} catch (Exception e) {
			Skill.logger.warn("[damageFeedback] [计算被攻击者减伤] [异常] [" + this.getLogString() + "]", e);
		}

		// 增加敌人
		if (this.isDeath() == false && this.getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY && target instanceof LivingObject) {
			enemyList.add(target, true, this.heartBeatStartTime);

			// 通知周围的队友 debug dot
			// if(team != null && (target instanceof BossMonster)){
			// List<Player> pList = team.getMembers();
			// try{
			// for(Player p : pList){
			// if(currentGame != null && p != null && this.qiecuoFlag == false
			// && p.isFighting() == false &&
			// p != this && currentGame.contains(p) &&
			// Math.abs(p.getX() - getX()) <= 640
			// && Math.abs(p.getY() - getY()) <= 640){
			// p.notifyPrepareToBeFighted(target);
			// }
			// }
			// }catch(Exception e){
			// e.printStackTrace();
			// }
			// }
		}

		if (damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_XISHOU) {

			// 通知战场，我给别人伤害了
			if (this.isInBattleField()) {
				battleField.notifyCauseDamage(this, target, damage);
			}

			// 副本统计
			Game game = this.currentGame;
			if (game != null && game.getDownCity() != null) {
				DownCity dc = game.getDownCity();
				dc.statPlayerAttack(this, damageType, damage);
			}
		}

		byte targetType = 0;
		if (target instanceof Player) {
			targetType = 0;
		} else if (target instanceof Sprite) {
			targetType = 1;
		}

		if (tempDamage != 0) {
			damage = tempDamage;
		}

		NOTIFY_EVENT_REQ req = null;
		switch (damageType) {
		case DAMAGETYPE_PHYSICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_PHYSICAL, damage);
			break;
		case DAMAGETYPE_SPELL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_SPELL, damage);
			break;
		case DAMAGETYPE_PHYSICAL_CRITICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_PHYSICAL_BAOJI, damage);
			break;
		case DAMAGETYPE_SPELL_CRITICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_SPELL_BAOJI, damage);
			break;
		case DAMAGETYPE_DODGE:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.DODGE, damage);
			break;
		case DAMAGETYPE_MISS:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.MISS, damage);
			break;
		case DAMAGETYPE_ZHAONGDU:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_ZHONGDU, damage);
			break;
		case DAMAGETYPE_FANSHANG:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_FANSHANG, damage);
			break;
		case DAMAGETYPE_MIANYI:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_MIANYI, damage);
			break;
		case DAMAGETYPE_XISHOU:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_XISHOU, damage);
			break;
		}
		if (req != null) addMessageToRightBag(req);
		tempDamage = 0;
	}
	
	public boolean hasDebuff() {
				synchronized (buffs) {
					for (int i = buffs.size() - 1; i >= 0; i--) {
						Buff buff = buffs.get(i);
						if (buff != null && buff instanceof AbnormalStateBuff) {
							return true;
						}
					}
				}
				return false;
			}
	
	public Buff removeDebuff() {
				synchronized (buffs) {
					for (int i = buffs.size() - 1; i >= 0; i--) {
						Buff buff = buffs.get(i);
						if (buff != null && buff instanceof AbnormalStateBuff) {
							buff.setInvalidTime(0);
							return buff;
						}
					}
				}
				return null;
			}

	public void 攻击玩家后对攻击者的操作(Fighter caster) {
		Player c = null;
		if (caster instanceof Pet) {
			Pet pet = (Pet) caster;
			c = pet.getMaster();
		}
		if (caster instanceof Player) {
			c = (Player) caster;
		}
		if (c == null) {
			return;
		}
		if (攻击者是否需要受到惩罚(c)) {
			c.setLastActivePkTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			if (c.evil <= 0) {
				// 加灰名buff
				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				BuffTemplate bt = btm.getBuffTemplateByName(Translate.灰名buff);
				if (bt != null) {
					Buff buff = bt.createBuff(1);
					if (buff != null) {
						buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 30 * 1000L);
						buff.setCauser(c);
						c.placeBuff(buff);
					}
				} else {
					System.out.println("没有此buff" + Translate.灰名buff);
				}
			}
		}
	}

	public void 玩家杀死玩家后的惩罚(Fighter caster) {
		Player c = null;
		if (caster instanceof Pet) {
			Pet pet = (Pet) caster;
			c = pet.getMaster();
		}
		if (caster instanceof Player) {
			c = (Player) caster;
		}
		if (c == null) {
			return;
		}
		if (攻击者是否需要受到惩罚(c)) {
			c.setEvil(c.getEvil() + 得到应该增加的罪恶值(c));
			c.setLastActivePkTime(SystemTime.currentTimeMillis());
			if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info(c.getLogString()+"[杀人增加红名值] [增加后当前红名值:"+ c.getEvil() +"]");
		}
	}

	public boolean 攻击者是否需要受到惩罚(Player p) {
		if (p.getCountry() != this.getCountry()) {
			return false;
		}
		if (this.activePk || this.evil > 0) {
			return false;
		}
		CountryManager cm = CountryManager.getInstance();
		if (cm != null && (cm.官职(p.getCountry(), p.getId()) == CountryManager.巡捕_国王 || cm.官职(p.getCountry(), p.getId()) == CountryManager.巡捕_元帅)) {
			return false;
		}

//		if (SiFangManager.getInstance().isInSiFangGame(this)) {
//			if (this.getJiazuId() != p.getJiazuId()) {
//				return false;
//			}
//		}

		JiazuFighterManager jfm = JiazuFighterManager.getInstance();
		if (jfm != null) {
			if (jfm.isInJiazuXuanZhan(this, p)) {
				return false;
			}
		}
		if (p.isInBattleField()) {
			return false;
		}
		if (VillageFightManager.开战) {
			VillageFightManager vfm = VillageFightManager.getInstance();
			if (vfm != null) {
				if (!vfm.是否需要受到惩罚(p, this)) {
					return false;
				}
			}

		}
		ChestFightManager cins = ChestFightManager.inst;
		if (cins != null && cins.isPlayerInActive(this)) {
			return false;
		}
		CityFightManager cfm = CityFightManager.getInstance();
		if (cfm != null) {
			if (cfm.是否在城战地图(p)) {
				return false;
			}
		}
		return true;
	}

	public int 得到应该增加的罪恶值(Player caster) {
		int value = 10;
		if (this.evil < 0) {
			value = 50;
		}
		return value;
	}

	public static double 得到玩家修理装备时的pk惩罚百分比(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (type == 普通_类型) {
			return 1;
		}
		if (type == 恶人_类型) {
			return 2;
		}
		if (type == 恶霸_类型) {
			return 3;
		}
		if (type == 罪大恶极_类型) {
			return 4;
		}
		if (type == 十恶不赦_类型) {
			return 8;
		}
		if (type == 万恶之首_类型) {
			return 16;
		}
		return 16;
	}

	public static double 得到玩家购买物品时的pk惩罚百分比(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (type == 普通_类型) {
			return 0;
		}
		if (type == 恶人_类型) {
			return 1;
		}
		if (type == 恶霸_类型) {
			return 2;
		}
		if (type == 罪大恶极_类型) {
			return 4;
		}
		if (type == 十恶不赦_类型) {
			return 8;
		}
		if (type == 万恶之首_类型) {
			return 16;
		}
		return 16;
	}

	public static double 得到玩家卖给npc物品时的pk惩罚百分比(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (type == 普通_类型) {
			return 1;
		}
		if (type == 恶人_类型) {
			return 0.7;
		}
		if (type == 恶霸_类型) {
			return 0.5;
		}
		if (type == 罪大恶极_类型) {
			return 0.3;
		}
		if (type == 十恶不赦_类型) {
			return 0.1;
		}
		if (type == 万恶之首_类型) {
			return 0.01;
		}
		return 0;
	}

	public static double 得到玩家获得经验的pk惩罚百分比(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (type == 普通_类型) {
			return 1;
		}
		if (type == 恶人_类型) {
			return 0.9;
		}
		if (type == 恶霸_类型) {
			return 0.75;
		}
		if (type == 罪大恶极_类型) {
			return 0.5;
		}
		if (type == 十恶不赦_类型) {
			return 0.3;
		}
		if (type == 万恶之首_类型) {
			return 0.1;
		}
		return 0;
	}

	public static double 得到疲劳玩家获得掉落的惩罚百分比(Player p) {
		int onTime = p.getOnLineTimePiLao();
		if (onTime > 0) {
			if (onTime == 3) {
				return 0.5;
			} else if (onTime == 4) {
				return 0.4;
			} else if (onTime == 5) {
				return 0.3;
			} else if (onTime == 6) {
				return 0.2;
			} else if (onTime == 7) {
				return 0;
			}
		}
		if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			if (p.killMonsterCount > 12000) {
				if (p.killMonsterCount > 24000) {
					return 0;
				}
				return 0.5;
			}
		}
		return 1;
	}

	public static double 得到玩家获得游戏币的pk惩罚百分比(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (type == 普通_类型) {
			return 1;
		}
		if (type == 恶人_类型) {
			return 0.9;
		}
		if (type == 恶霸_类型) {
			return 0.75;
		}
		if (type == 罪大恶极_类型) {
			return 0.5;
		}

		if (type == 十恶不赦_类型) {
			return 0.3;
		}
		if (type == 万恶之首_类型) {
			return 0.1;
		}
		return 0;
	}

	public static double 得到玩家死亡复活的pk惩罚百分比(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (type == 普通_类型) {
			return 0;
		}
		if (type == 恶人_类型) {
			return 1.5;
		}
		if (type == 恶霸_类型) {
			return 2;
		}
		if (type == 罪大恶极_类型) {
			return 3;
		}
		if (type == 十恶不赦_类型) {
			return 4;
		}
		if (type == 万恶之首_类型) {
			return 5;
		}
		return 10;
	}

	public static double 得到玩家打怪掉落的pk惩罚百分比(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (type == 普通_类型) {
			return 1;
		}
		if (type == 恶人_类型) {
			return 0.7;
		}
		if (type == 恶霸_类型) {
			return 0.5;
		}
		if (type == 罪大恶极_类型) {
			return 0.3;
		}
		if (type == 十恶不赦_类型) {
			return 0.2;
		}
		if (type == 万恶之首_类型) {
			return 0.1;
		}
		return 0;
	}

	public static byte 根据罪恶值得到玩家的罪恶类型(Player p) {
		int e = p.getEvil();
		for (int i = 0; i < 恶名等级对应表.length; i++) {
			int[] 罪恶值范围 = 恶名等级对应表[i];
			if (罪恶值范围[0] <= e && e <= 罪恶值范围[1]) {
				return 恶名名称对应类型[i];
			}
		}
		return 0;
	}

	public static double 得到玩家死亡掉落耐久百分比(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (type == 普通_类型) {
			return 0.02;
		}
		if (type == 恶人_类型) {
			return 0.03;
		}
		if (type == 恶霸_类型) {
			return 0.04;
		}
		if (type == 罪大恶极_类型) {
			return 0.05;
		}
		if (type == 十恶不赦_类型) {
			return 0.06;
		}
		if (type == 万恶之首_类型) {
			return 0.07;
		}
		return 0.08;
	}

	public static double 得到玩家碎装几率(Player p) {
		byte type = 根据罪恶值得到玩家的罪恶类型(p);
		if (p.getEvil() < 0) {
			return 0.01;
		}
		if (type == 普通_类型) {
			return 0.05;
		}
		if (type == 恶人_类型) {
			return 0.06;
		}
		if (type == 恶霸_类型) {
			return 0.07;
		}
		if (type == 罪大恶极_类型) {
			return 0.1;
		}
		if (type == 十恶不赦_类型) {
			return 0.15;
		}
		if (type == 万恶之首_类型) {
			return 0.2;
		}
		return 1;
	}

	public void 玩家被杀死后的掉落(Fighter caster) {
		double dlp = 0;
		byte type = 根据罪恶值得到玩家的罪恶类型(this);
		if (type == 普通_类型) {
			if (caster instanceof Player) {
				if (this.getEvil() < 0) {
					dlp = 0.01;
				} else {
					if (((Player) caster).getCountry() == this.country) {
						dlp = 0.03;
					} else {

						dlp = 0.06;
					}
				}
				// 测试其实怪物打死普通人不掉落
			} else {
				dlp = 0;
			}
		} else if (type == 恶人_类型) {
			dlp = 0.3;
		} else if (type == 恶霸_类型) {
			dlp = 0.4;
		} else if (type == 罪大恶极_类型) {
			dlp = 0.5;
		} else if (type == 十恶不赦_类型) {
			dlp = 0.6;
		} else if (type == 万恶之首_类型) {
			dlp = 0.7;
		}
		ArrayList<Long> aidList = new ArrayList<Long>();
		StringBuffer sb2 = new StringBuffer();
		if (ProbabilityUtils.randomProbability(random, dlp)) {
			int index = ProbabilityUtils.randomProbability(random, 死亡掉落个数概率);
			int c = 死亡掉落个数[index];
			if (TeamSubSystem.logger.isDebugEnabled()) TeamSubSystem.logger.debug("[任务死亡掉落物品] [" + this.getLogString() + "] [个数:" + c + "]");
			if (c > 0) {
				EquipmentColumn ec = this.getEquipmentColumns();
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				long[] eids = ec.getEquipmentIds();
				if (eids != null) {
					for (int i = 0; i < eids.length; i++) {
						long id = eids[i];
						if (id >= 0) {
							ArticleEntity ae = aem.getEntity(id);
							// 新加 法宝死亡不掉落
							if (ae != null && !ae.isBinded() && !ae.isRealBinded() && !(ae instanceof NewMagicWeaponEntity) && ArticleProtectManager.instance.isCanDo(this, ArticleProtectDataValues.ArticleProtect_Common, ae.getId())) {
								aidList.add(id);
							}
						}
					}
				}
				// for (int i = 0; i < knapsacks_common.length; i++) {
				// if (i != Article.KNAP_宠物) {
				Knapsack k = knapsacks_common[0];
				if (k != null) {
					Cell[] cells = k.getCells();
					if (cells != null) {
						for (int j = 0; j < cells.length; j++) {
							Cell cell = cells[j];
							if (cell != null) {
								long id = cell.entityId;
								if (id >= 0) {
									ArticleEntity ae = aem.getEntity(id);
									// 新加 法宝死亡不掉落
									if (ae != null && !ae.isBinded() && !ae.isRealBinded() && !(ae instanceof NewMagicWeaponEntity) && ArticleProtectManager.instance.isCanDo(this, ArticleProtectDataValues.ArticleProtect_Common, ae.getId())) {
										aidList.add(id);
									}
								}
							}
						}
					}
				}
				// }
				// }
				if (!aidList.isEmpty()) {
					Collections.shuffle(aidList);
				}
				for (int i = 0; i < c && i < aidList.size(); i++) {
					if (aidList.get(i) != null) {
						玩家掉落物品(aidList.get(i).longValue(), caster);
						long aeId = aidList.get(i);
						ArticleEntity ae = aem.getEntity(aeId);
						if (ae instanceof EquipmentEntity) {
							sb2.append(Translate.translateString(Translate.某级别某颜色装备名, new String[][] { { Translate.STRING_3, "" + ((EquipmentEntity) ae).getStar() }, { Translate.STRING_1, ArticleManager.color_equipment_Strings[((EquipmentEntity) ae).getColorType()] }, { Translate.STRING_2, ae.getArticleName() } }));
						} else if (ae instanceof ArticleEntity) {
							sb2.append(Translate.translateString(Translate.某颜色物品名, new String[][] { { Translate.STRING_1, ArticleManager.color_article_Strings[ae.getColorType()] }, { Translate.STRING_2, ae.getArticleName() } }));
						}
					}
					if (i != aidList.size() - 1 && i != c - 1) {
						sb2.append("，");
					}
				}
			}
		}

		try {
			StringBuffer sb = new StringBuffer();
			// 玩家被杀死后邮件通知
			if (caster instanceof Player) {
				sb.append(Translate.translateString(Translate.您被某某杀死了, new String[][] { { Translate.PLAYER_NAME_1, caster.getName() } }));
			} else if (caster instanceof Pet) {
				sb.append(Translate.translateString(Translate.您被某某的宠物杀死了, new String[][] { { Translate.PLAYER_NAME_1, ((Pet) caster).getOwnerName() } }));
			}
			if (aidList.isEmpty()) {
				if (sb.length() > 0) {
					sb.append("。");
				}
			} else {
				if (sb.length() > 0) {
					sb.append("，");
				}

				sb.append(Translate.translateString(Translate.您掉落了某某, new String[][] { { Translate.STRING_1, sb2.toString() } }));
			}
			if (sb.length() > 0) {
				if (!aidList.isEmpty()) {
					MailManager mm = MailManager.getInstance();
					mm.sendMail(this.getId(), new ArticleEntity[0], Translate.死亡提示邮件, sb.toString(), 0, 0, 0, "");
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, sb.toString());
					this.addMessageToRightBag(hreq);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			MailManager.logger.warn("[玩家死亡掉落发送邮件异常] [{}]", new Object[] { this.getLogString() }, ex);
		}

		// 特殊装备掉落
//		SpecialEquipmentManager sem = SpecialEquipmentManager.getInstance();
//		sem.specialEquipmentEntityDrop(this, caster);
	}

	/**
	 * 此方法会把人身上包括装备栏以及背包栏不包括防爆包的东西掉落 当玩家背包格上的有多个格子有此id，只掉落第一个格子，且该格子上的物品全掉落
	 * 
	 * @param id
	 * @return
	 */
	public boolean 玩家掉落物品(long flopId, Fighter caster) {
		EquipmentColumn ec = this.getEquipmentColumns();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long[] eids = ec.getEquipmentIds();
		if (eids != null) {
			for (int i = 0; i < eids.length; i++) {
				long id = eids[i];
				if (id == flopId) {
					EquipmentEntity ee = ec.takeOff(i, getCurrSoul().getSoulType());
					人掉落物品(currentGame, this, ee, 1, caster);
					return true;
				}
			}
		}
		ArticleEntity ae = aem.getEntity(flopId);
		// for (int i = 0; i < knapsacks_common.length; i++) {
		// if (i != Article.KNAP_宠物) {
		Knapsack k = knapsacks_common[0];
		int index = k.indexOf(ae);
		if (index >= 0) {
			k.remove(index, "死亡掉落", false);
			if (ae != null) {
				// 统计
				ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "死亡掉落", null);
			}
			人掉落物品(currentGame, this, ae, 1, caster);
			return true;
		}
		// }
		// }
		return false;
	}

	public void 人掉落物品(Game game, Player player, ArticleEntity ae, int count, Fighter caster) {
		if (game == null) {
			return;
		}
		Player owner = null;
		if (caster instanceof Player) {
			owner = (Player) caster;
		}
		if (caster instanceof Pet) {
			Pet pet = (Pet) caster;
			long id = pet.getOwnerId();
			try {
				owner = PlayerManager.getInstance().getPlayer(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		NPCManager nm = MemoryNPCManager.getNPCManager();
		FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
		if (ae == null) {
			return;
		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		fcn.setStartTime(now);
		fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
		fcn.setAllCanPickAfterTime(MemoryNPCManager.所有人都可以拾取的时长);

		if (owner == null) {
			// 怪打死的
			owner = player;
			fcn.setOwner(owner);
			fcn.setFlopType((byte) 1);
		} else {
			// 玩家打死的
			fcn.setFlopType((byte) 0);
			fcn.setAllCanPickAfterTime(0l);
		}

		// fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
		// fcn.setAllCanPickAfterTime(MemoryNPCManager.所有人都可以拾取的时长);
		fcn.setAe(ae);
		fcn.setCount(count);
		Point point = 得到位置();
		fcn.setX(player.x);
		fcn.setY(player.y);
		MoveTrace mt = 得到掉落路径(game, player.x, player.y, point.x, point.y, com.fy.engineserver.gametime.SystemTime.currentTimeMillis(), 200);
		if (mt != null) {
			fcn.setMoveTrace(mt);
		}
		StringBuffer sb = new StringBuffer();
		ArticleManager am = ArticleManager.getInstance();
		Article article = am.getArticle(ae.getArticleName());
		int color = ArticleManager.COLOR_WHITE;
		try {
			if (article.getFlopNPCAvata() != null) {
				fcn.setAvataSex(article.getFlopNPCAvata());
				ResourceManager.getInstance().getAvata(fcn);
			}
			color = ArticleManager.getColorValue(article, ae.getColorType());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		fcn.setName(ae.getArticleName());
		fcn.setNameColor(color);
		fcn.setTitle("");
		game.addSprite(fcn);

		// 物品掉落声音广播
		((FlopCaijiNpc) fcn).notifyAroundPlayersPlaySound(game);

	}

	public static MoveTrace 得到掉落路径(Game game, int startX, int startY, int endX, int endY, long heartBeatStartTime, int moveSpeed) {
		Point s = new Point(startX, startY);
		Point e = new Point(endX, endY);
		MoveTrace path = null;
		if (game.getGameInfo().navigator.isStrictVisiable(s.x, s.y, e.x, e.y)) {
			short roadLen[] = new short[1];
			roadLen[0] = (short) Graphics2DUtil.distance(s, e);
			path = new MoveTrace(roadLen, new Point[] { s, e }, (long) (heartBeatStartTime + roadLen[0] * 1000 / (moveSpeed / 2)));
		}
		return path;
	}

	public Point 得到位置() {
		Point point = new Point();
		point.x = this.x + random.nextInt(100);
		point.y = this.y + random.nextInt(100);
		return point;
	}

	@Override
	public void setEvil(int value) {
		super.setEvil(value);
		setDirty(true, "evil");
		modifyNameColorType();
	}

	public void modifyNameColorType() {
		byte type = 根据罪恶值得到玩家的罪恶类型(this);
		byte colorType = 恶名名称对应颜色类型[type];
		if (colorType != nameColorType) {
			setNameColorType(colorType);
		}
	}

	@Override
	public byte getClassType() {
		return 0;
	}

	/**
	 * 是否是当前装备栏
	 * 
	 * @param ec
	 * @return
	 */
	public boolean isCurrentEquipmentColumn(EquipmentColumn ec) {
		return this.getCurrSoul().getEc() == ec;
		// return this.ecs[this.currentSuit] == ec;
	}

	/**
	 * 得到玩家身上已经装备的某种装备
	 * 
	 * @param equmentType
	 * @return
	 */
	public ArticleEntity getCurrentEquipmentEntity(int equipmentType) {
		return getEquipmentColumns().get(equipmentType);
	}

	/**
	 * 装备镶嵌
	 * 
	 * @param inlayIndex
	 *            哪个孔
	 * @param knapsackIndex
	 *            哪个宝石
	 */
	public void inlayEquipment(long sequnceNum, long equipmentId, byte inlayIndex, short knapsackIndex) {
	}

	/**
	 * 装备镶嵌
	 * 
	 * @param inlayIndex
	 *            哪个孔
	 * @param knapsackIndex
	 *            哪个宝石
	 */
	public void inlayEquipment(long equipmentId, byte inlayIndex, short knapsackIndex) {
	}

	/**
	 * 从装备栏中卸载某个装备
	 * 
	 * @param suit
	 * @param equipmentType
	 */
	public void removeEquipment(int soulType, byte equipmentType) {
		if (equipmentType >= EquipmentColumn.EQUIPMENT_TYPE_NAMES.length) {

			return;
		}
		Knapsack knapsack = getKnapsack_common();
		if (knapsack == null) {
			return;
		}
		if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(id)) {
			sendError(Translate.渡劫换装失败);
			return;
		}
		FairyRobberyManager fins = FairyRobberyManager.inst;
		if (fins != null && fins.isPlayerInRobbery(this)) {
			sendError(Translate.渡劫换装失败);
			return;
		}
		if (knapsack.isFull()) {
			HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.背包已经满了请先腾出空格来再卸载装备);
			addMessageToRightBag(error);
		} else {
			Soul soul = getSoul(soulType);
			if (soul == null) {
				if (GamePlayerManager.logger.isWarnEnabled()) {
					GamePlayerManager.logger.warn(this.getLogString() + "[要脱装备] [元神类型不存在] [soulType:" + soulType + "] [equipmentType:" + equipmentType + "]");
				}
				return;
			}
			if (equipmentType != 11) {
				EquipmentEntity ee = soul.getEc().takeOff(equipmentType, soulType);
				if (ee != null) {
					knapsack.put(ee, "卸载装备");
					if (ArticleManager.logger.isInfoEnabled()) {
						ArticleManager.logger.info("[卸载装备后] [{}] [playerId:{}] [{}] [{}] [id:{}] {}", new Object[] { this.getUsername(), id, getName(), ee.getArticleName(), ee.getId(), getPlayerPropsString() });
					}
				}
			} else { // 脱法宝
				NewMagicWeaponEntity ee;
				try {
					ee = soul.getEc().takeOffMw(equipmentType, soulType);
					if (ee != null) {
						knapsack.put(ee, "卸载装备");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					MagicWeaponManager.logger.error("[脱法宝错误][" + this.getLogString() + "]");
				}

			}

		}
	}

	/**
	 * 切换装备组合
	 * 
	 * @param suit
	 */
	public void switchEquipmentSuit(byte suit) {
	}

	/**
	 * 使用有目标的技能，攻击目标
	 * 
	 * @param skill
	 * @param target
	 */
	public void useTargetSkill(ActiveSkill skill, Fighter target, byte[] targetTypes, long[] targetIds, byte direction) {
		if (xl_chanllengeFlag == 2) {// 仙灵地图不允许使用技能
			return;
		}
		if (this.isInBattleField() && this.getDuelFieldState() == 2) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.观战方不能使用技能);
			this.addMessageToRightBag(hreq);
			return;
		}
//		if (DisasterManager.getInst().isPlayerInGame(this)) {
//			if (DisasterManager.logger.isInfoEnabled()) {
//				DisasterManager.logger.info("[空岛大冒险地图不允许使用技能] [" + this.getLogString() + "] [skillId:" + skill.getId() + "]");
//			}
//			return;
//		}

		if (this.isInBattleField() && this.getDuelFieldState() == 1) {
			if (this.battleField != null && this.battleField instanceof TournamentField) {
				TournamentField df = (TournamentField) this.battleField;
				if (df.getState() != TournamentField.STATE_FIGHTING) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.非战斗时间不能使用技能);
					this.addMessageToRightBag(hreq);
					return;
				}
			}
		}

		if (flying) {
			// 飞行状态下不能使用技能
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[飞行状态下不能使用目标技能] [{}] [{}] [{}] [{}] [{}]", new Object[] { this.game, this.getUsername(), getName(), target.getName(), skill.getName() });
			return;
		}

		// 非战斗坐骑 下马
		if (this.isUpOrDown) {
			Horse horse = HorseManager.getInstance().getHorseById(this.getRidingHorseId(), this);
			if (horse != null) {
				if (!horse.isFight()) this.downFromHorse();
			}
		}

		CareerManager cm = CareerManager.getInstance();
		CommonAttackSkill c = cm.getCommonAttackSkill(this);
		// CommonAttackSkill c = cm.getCommonAttackSkill(career);
		if (skill.getId() == c.getId()) {

			int r = skill.check(this, target, 1);
			if (r == 0) {
				activeSkillAgent.usingSkill(skill, 1, target, (int) target.getX(), (int) target.getY(), targetTypes, targetIds, direction);
				SkEnhanceManager.getInst().cheakAttNums(this, target, skill);
				// 掉耐久
				EquipmentColumn ec = ecs;
				ec.attack();
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Skill.getSkillFailReason(r));
				this.addMessageToRightBag(hreq);
			}
		} else {
			int r = skill.check(this, target, this.getSkillLevel(skill.getId()));
			if (r == 0) {
				activeSkillAgent.usingSkill(skill, this.getSkillLevel(skill.getId()), target, (int) target.getX(), (int) target.getY(), targetTypes, targetIds, direction);
				SkEnhanceManager.getInst().cheakAttNums(this, target, skill);
				// 掉耐久
				EquipmentColumn ec = ecs;
				ec.attack();
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Skill.getSkillFailReason(r));
				this.addMessageToRightBag(hreq);

				if (skill.getDuration3() >= 10000) {
					SKILL_CD_MODIFY_REQ req2 = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), getId(), (short) skill.getId(), (byte) ActiveSkillEntity.STATUS_END, 0);
					addMessageToRightBag(req2);
				}
			}
		}

		if (target != null) {
			if (skill instanceof SkillWithoutTraceAndOnTeamMember) {
				SkillWithoutTraceAndOnTeamMember st = (SkillWithoutTraceAndOnTeamMember) skill;
				if (target != null && getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY && (st.getRangeType() == 1 || st.getRangeType() == 6)) {
					notifyPrepareToFighting(target);
					target.notifyEndToBeFighted(this);
				}
			} else if (getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY) {
				notifyPrepareToFighting(target);
				target.notifyPrepareToBeFighted(this);
			}
		}

	}

	/**
	 * 使用无目标的技能，攻击
	 * 
	 * @param skill
	 * @param x
	 * @param y
	 */
	public void useNonTargetSkill(ActiveSkill skill, int x, int y, byte[] targetTypes, long[] targetIds, byte direction) {
		if (xl_chanllengeFlag == 2) {// 仙灵地图不允许使用技能
			return;
		}

		if (this.isInBattleField() && this.getDuelFieldState() == 2) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.观战方不能使用技能);
			this.addMessageToRightBag(hreq);
			return;
		}
//		if (DisasterManager.getInst().isPlayerInGame(this)) {
//			if (DisasterManager.logger.isInfoEnabled()) {
//				DisasterManager.logger.info("[空岛大冒险地图不允许使用技能] [" + this.getLogString() + "] [skillId:" + skill.getId() + "]");
//			}
//			return;
//		}

		if (this.isInBattleField() && this.getDuelFieldState() == 1) {
			if (this.battleField != null && this.battleField instanceof TournamentField) {
				TournamentField df = (TournamentField) this.battleField;
				if (df.getState() != TournamentField.STATE_FIGHTING) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.非战斗时间不能使用技能);
					this.addMessageToRightBag(hreq);
					return;
				}
			}
		}

		if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(this.getId()) && skill.isNuqiFlag()) {
			sendError(Translate.渡劫中不能使用技能);
			return;
		}
		FairyRobberyManager fins = FairyRobberyManager.inst;
		if (fins != null && fins.isPlayerInRobbery(this) && skill.isNuqiFlag()) {
			this.sendError(Translate.渡劫中不能使用技能);
			return;
		}

		if (flying) {
			// 飞行状态下不能使用技能
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[飞行状态下不能使用无目标技能] [{}] [{}] [{}] [--] [{}]", new Object[] { this.game, this.getUsername(), getName(), skill.getName() });
			return;
		}

		// 下马
		getDownFromHorse();

		byte skLv = this.getSkillLevel(skill.getId());
		int r = skill.check(this, null, skLv);
		if (r == 0) {
			activeSkillAgent.usingSkill(skill, skLv, null, x, y, targetTypes, targetIds, direction);
			SkEnhanceManager.getInst().cheakAttNums(this, null, skill);
			// 掉耐久
			ecs.attack();
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Skill.getSkillFailReason(r));
			this.addMessageToRightBag(hreq);

			if (skill.getDuration3() >= 10000) {
				SKILL_CD_MODIFY_REQ req2 = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), getId(), (short) skill.getId(), (byte) ActiveSkillEntity.STATUS_END, 0);
				addMessageToRightBag(req2);
			}
		}

	}

	/**
	 * 根据技能的ID来获得技能
	 * 
	 * @param index
	 * @return
	 */
	public Skill getSkillById(int sid) {
		CareerManager cm = CareerManager.getInstance();
		Career career = cm.getCareer(this.getCurrSoul().getCareer());
		if (career == null) {
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[玩家] [{}] [Player.getSkillById()] [玩家的职业不存在] [职业:{}]", new Object[] { this.getName(), this.career });
			return null;
		}
		CommonAttackSkill commonAttackSkill = cm.getCommonAttackSkill(this);
		// CommonAttackSkill commonAttackSkill = cm.getCommonAttackSkill(this.career);

		if (commonAttackSkill.getId() == sid) { // 普通攻击
			return commonAttackSkill;
		}
		Skill skill = career.getSkillById(sid);

		if (skill == null) {
			skill = 得到装备上的技能(sid);
		}

		if (skill != null) {
			return skill;
		} else {
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[玩家] [{}] [Player.getSkillById()] [技能不存在职业线路中] [职业:{}] [技能:{}]", new Object[] { this.getName(), career.getName(), sid });
			return null;
		}
	}

	public Skill 得到装备上的技能(int sId) {
		if (sId <= 0) {
			return null;
		}
		if (this.getCurrSoul() != null && this.getCurrSoul().getEc() != null) {
			EquipmentColumn ec = this.getCurrSoul().getEc();
			long[] ids = ec.getEquipmentIds();
			if (ids != null) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleManager am = ArticleManager.getInstance();
				CareerManager cm = CareerManager.getInstance();
				for (long id : ids) {
					if (id > 0) {
						ArticleEntity ae = aem.getEntity(id);
						if (ae != null) {
							Article a = am.getArticle(ae.getArticleName());
							if (a instanceof Equipment) {
								if (((Equipment) a).getSkillId() == sId) {
									return cm.getSkillById(sId);
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 是否在队伍里
	 */
	public boolean isSameTeam(Fighter fighter) {
		if (fighter == this) return true;
		if (this.getTeamMark() != Player.TEAM_MARK_NONE && this.getTeam() != null) {
			return team.members.contains(fighter);
		}
		return false;
	}

	/**
	 * 得到玩家所在团队中所有的团队成员，返回值包括玩家自己 如果玩家没有加入任何团队，返回值只包含玩家自己
	 * 
	 * @return
	 */
	public Player[] getTeamMembers() {
		if (this.getTeamMark() != Player.TEAM_MARK_NONE && this.getTeam() != null && this.getTeam().members.size() > 0) {
			return team.members.toArray(new Player[0]);
		} else {
			return new Player[] { this };
		}
	}

	/**
	 * 计算物理伤害
	 */
	public int getPhysicalDamage() {
		if (getPhysicalDamageUpperLimit() <= getPhysicalDamageLowerLimit()) {
			return getPhysicalDamageLowerLimit();
		}
		return getPhysicalDamageLowerLimit() + random.nextInt(getPhysicalDamageUpperLimit() - getPhysicalDamageLowerLimit());
	}

	public void removeBuff(Buff buff) {
		try {
			if (buff != null) {
				buff.end(this);
				buffs.remove(buff);
				setDirty(true, "buffs");
				if (buff.isSyncWithClient()) {
					this.removedBuffs.add(buff);
				}
				GamePlayerManager.logger.warn("[删除buff] [OK] [" + this.getLogString() + "] [" + buff + "]");
			}
		} catch (Exception e) {
			GamePlayerManager.logger.warn("[删除buff] [异常] [" + this.getLogString() + "] [" + buff + "]", e);
		}
	}

	public void removeFireBuff() {
		synchronized (buffs) {
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				if (buff != null && buff.getTemplateName().equals(RobberyConstant.火神buff)) {
					buff.setInvalidTime(0);
					return;
				}
			}
		}

	}

	/**
	 * 给玩家增加一个buff
	 * @param buffName
	 * @param buffLevel
	 * @param biffLast
	 * @param causer
	 */
	public void placeBuff(String buffName, int buffLevel, int biffLast, Fighter causer) {
		BuffTemplateManager bm = BuffTemplateManager.getInstance();
		BuffTemplate bt = bm.getBuffTemplateByName(buffName);
		Buff buff = bt.createBuff(level + 1);
		if (buff != null) {
			buff.setStartTime(SystemTime.currentTimeMillis());
			buff.setInvalidTime(buff.getStartTime() + biffLast);
			buff.setCauser(causer);
		}
		this.placeBuff(buff);
//		HunshiManager.getInstance().dealWithInfectSkill(causer, this, buff);
	}

	/**
	 * 种植一个buff到玩家或者怪的身上， 相同类型的buff会互相排斥，高级别的buff将顶替低级别的buff，无论有效期怎么样
	 * 
	 * @param buff
	 */
	private transient long tempTime = 0;

	public void placeBuff(Buff buff) {
		// synchronized (buffs) {
		// 坐骑魂石套装技能19,20自动清除不良状态并屏蔽负面buff
//		boolean buffEnd = HunshiManager.getInstance().dealWithBadBuff(this, buff);
//		if (buffEnd) {
//			return;
//		}

		Buff old = null;
		try {
			long now = System.currentTimeMillis();
			if (tempTime > now && !buff.getTemplate().isAdvantageous()) {
				return;
			}
//			if (EnchantEntityManager.instance.checkControlBuff(this, buff)) {
//				if (ActiveSkill.logger.isDebugEnabled()) {
//					ActiveSkill.logger.debug("[种植BUFF] [附魔生效，此次buff不生效] [" + buff.getClass() + "] [" + buff.getTemplateName() + "] [" + this.getLogString() + "]");
//				}
//				return;
//			}
		} catch (Exception e) {
			EnchantManager.logger.warn("[检测玩家收到控制类buff] [异常] [" + this.getLogString() + "]", e);
		}
		try {
			if (!CareerManager.canAddBuff(this, buff.getTemplateId())) {
				if (ActiveSkill.logger.isInfoEnabled()) {
					ActiveSkill.logger.info("[种植BUFF] [失败] [本职业不应该有此buff] [career:" + this.getCareer() + "] [buffId:" + buff.getTemplateId() + "] [buffName:" + buff.getTemplateName() + "] [" + this.getLogString() + "]");
				}
				return;
			}
		} catch (Exception e) {

		}
		if ((buff instanceof Buff_ZhongDu) || (buff instanceof Buff_ZhongDuFaGong) || (buff instanceof Buff_ZhongDuWuGong) || (buff instanceof Buff_ZhongDuWithStatus)) {
			for (Buff b : buffs) {
				if (buff.getCauser() == b.getCauser() && buff.getTemplateId() == b.getTemplateId() && buff.getClass() == b.getClass()) {
					old = b;
					break;
				}
			}
			if (old != null) {
				if (old.getInvalidTime() < System.currentTimeMillis()) {
					old.end(this);
					buffs.remove(old);
					setDirty(true, "buffs");
					if (old.isSyncWithClient()) {
						this.removedBuffs.add(old);
					}

					if (ActiveSkill.logger.isDebugEnabled()) {
						ActiveSkill.logger.debug("[种植BUFF] [{}] [原有的同类BUFF时间到] [老：{}:{}({})] [TID:{}] [新：{}:{}({})] [TID:{}]", new Object[] { getName(), (old.getClass().getName().substring(old.getClass().getName().lastIndexOf(".") + 1)), old.getTemplateName(), old.getLevel(), old.getTemplateId(), ((buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1))), buff.getTemplateName(), buff.getLevel(), buff.getTemplateId() });
					}
				} else {
					if (buff.getLevel() >= old.getLevel()) {
						old.end(this);
						buffs.remove(old);
						setDirty(true, "buffs");
						if (old.isSyncWithClient()) {
							this.removedBuffs.add(old);
						}

						if (ActiveSkill.logger.isDebugEnabled()) {
							ActiveSkill.logger.debug("[种植BUFF] [{}] [顶掉原有的同类BUFF] [老：{}:{}({})] [TID:{}] [新：{}:{}({})] [TID:{}]", new Object[] { getName(), (old.getClass().getName().substring(old.getClass().getName().lastIndexOf(".") + 1)), old.getTemplateName(), old.getLevel(), old.getTemplateId(), ((buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1))), buff.getTemplateName(), buff.getLevel(), buff.getTemplateId() });
						}

					} else {

						if (ActiveSkill.logger.isDebugEnabled()) {
							ActiveSkill.logger.debug("[种植BUFF] [{}] [被原有的同类BUFF顶掉] [老：{}:{}({})] [TID:{}] [新：{}:{}({})] [TID:{}]", new Object[] { getName(), (old.getClass().getName().substring(old.getClass().getName().lastIndexOf(".") + 1)), old.getTemplateName(), old.getLevel(), old.getTemplateId(), ((buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1))), buff.getTemplateName(), buff.getLevel(), buff.getTemplateId() });
						}

						return;
					}
				}
			}
		} else {
			for (Buff b : buffs) {
				if (buff.getTemplateId() == b.getTemplateId() && buff.getClass() == b.getClass()) {
					old = b;
					break;
				}
			}
			if (old != null) {
				if (old.getInvalidTime() < System.currentTimeMillis()) {
					old.end(this);
					buffs.remove(old);
					setDirty(true, "buffs");
					if (old.isSyncWithClient()) {
						this.removedBuffs.add(old);
					}

					if (ActiveSkill.logger.isDebugEnabled()) {
						ActiveSkill.logger.debug("[种植BUFF] [{}] [原有的同类BUFF时间到] [老：{}:{}({})] [TID:{}] [新：{}:{}({})] [TID:{}]", new Object[] { getName(), (old.getClass().getName().substring(old.getClass().getName().lastIndexOf(".") + 1)), old.getTemplateName(), old.getLevel(), old.getTemplateId(), ((buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1))), buff.getTemplateName(), buff.getLevel(), buff.getTemplateId() });
					}
				} else {
					if (buff.getLevel() >= old.getLevel()) {
						old.end(this);
						buffs.remove(old);
						setDirty(true, "buffs");
						if (old.isSyncWithClient()) {
							this.removedBuffs.add(old);
						}

						if (ActiveSkill.logger.isDebugEnabled()) {
							ActiveSkill.logger.debug("[种植BUFF] [{}] [顶掉原有的同类BUFF] [老：{}:{}({})] [TID:{}] [新：{}:{}({})] [TID:{}]", new Object[] { getName(), (old.getClass().getName().substring(old.getClass().getName().lastIndexOf(".") + 1)), old.getTemplateName(), old.getLevel(), old.getTemplateId(), ((buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1))), buff.getTemplateName(), buff.getLevel(), buff.getTemplateId() });
						}

					} else {

						if (ActiveSkill.logger.isDebugEnabled()) {
							ActiveSkill.logger.debug("[种植BUFF] [{}] [被原有的同类BUFF顶掉] [老：{}:{}({})] [TID:{}] [新：{}:{}({})] [TID:{}]", new Object[] { getName(), (old.getClass().getName().substring(old.getClass().getName().lastIndexOf(".") + 1)), old.getTemplateName(), old.getLevel(), old.getTemplateId(), ((buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1))), buff.getTemplateName(), buff.getLevel(), buff.getTemplateId() });
						}

						return;
					}
				}
			}
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff b = buffs.get(i);
				if (buff.getCauser() == b.getCauser() && buff.getGroupId() == b.getGroupId()) {
					buffs.remove(i);
					setDirty(true, "buffs");
					b.end(this);
					if (b.isSyncWithClient()) {
						this.removedBuffs.add(b);
					}
					if (ActiveSkill.logger.isDebugEnabled()) {
						ActiveSkill.logger.debug("[种植BUFF] [{}] [顶掉原有的同组BUFF] [老：{}:{}({})] [GID:{}] [新：{}:{}({})] [GID:{}]", new Object[] { getName(), (b.getClass().getName().substring(b.getClass().getName().lastIndexOf(".") + 1)), b.getTemplateName(), b.getLevel(), b.getGroupId(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getLevel(), buff.getGroupId() });
					}
				}
			}
		}
		buffs.add(buff);
		setDirty(true, "buffs");
		

		buff.start(this);

		if (buff.isSyncWithClient()) {
			this.newlyBuffs.add(buff);
			Skill.logger.debug("Player.placeBuff: {} {} {}", buff.getTemplateName(), buff.getDescription());
		}

		if (ActiveSkill.logger.isDebugEnabled()) {
			ActiveSkill.logger.debug("[种植BUFF] [{}] [成功] [老：--] [TID:--] [新：{}:{}({})] [TID:{}] [oldBuff:{}] [newBuff:{}] [isImmunity:" + this.isImmunity() + "]", 
					new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getLevel(), buff.getTemplateId(), old, buff });
		}
		// }
		TaskAction actionOfGetBuff = TaskActionOfGetBuff.createTaskAction(buff);
		this.dealWithTaskAction(actionOfGetBuff);
	}

	/**
	 * 通过buff的templateId获得buff
	 * 
	 * @return
	 */
	public Buff getBuff(int templateId) {
		synchronized (buffs) {
			for (Buff b : buffs) {
				if (b.getTemplateId() == templateId) {
					return b;
				}
			}
		}
		return null;
	}

	public Buff getBuffByName(String name) {
		synchronized (buffs) {
			for (Buff b : buffs) {
				if (b.getTemplateName().equals(name)) {
					return b;
				}
			}
		}
		return null;
	}

	/**
	 * 得到所有种植的buff
	 * 
	 * @return
	 */
	public List<Buff> getAllBuffs() {
		return buffs;
	}

	/**
	 * 得到所有激活的buff
	 * 
	 * @return
	 */
	public Buff[] getActiveBuffs() {
		return (Buff[]) buffs.toArray(new Buff[0]);
	}

	/**
	 * 得到移除的buff
	 * 
	 * @return
	 */
	public ArrayList<Buff> getRemovedBuffs() {
		return this.removedBuffs;
	}

	/**
	 * 得到新种植的buff
	 * 
	 * @return
	 */
	public ArrayList<Buff> getNewlyBuffs() {
		return this.newlyBuffs;
	}

	/**
	 * 增加属性点给力量A
	 * 
	 * @param p
	 */
	public void addUnallocatedToStrength(int p) {
		if (p > 0 && unallocatedPropertyPoint >= p) {
			unallocatedPropertyPoint -= p;
			setStrengthA((strengthA + p));
		}
	}

	/**
	 * 增加属性点给身法
	 * 
	 * @param p
	 */
	public void addUnallocatedToDexterity(int p) {
		if (p > 0 && unallocatedPropertyPoint >= p) {
			unallocatedPropertyPoint -= p;
			// setDexterityA((dexterityA + p));
		}
	}

	/**
	 * 分配属性点到灵力
	 * 
	 * @param p
	 */
	public void addUnallocatedToSpellpower(int p) {
		if (p > 0 && unallocatedPropertyPoint >= p) {
			unallocatedPropertyPoint -= p;
			setSpellpowerA((spellpowerA + p));
		}
	}

	/**
	 * 分配属性点到耐力
	 * 
	 * @param p
	 */
	public void addUnallocatedToConstitution(int p) {
		if (p > 0 && unallocatedPropertyPoint >= p) {
			unallocatedPropertyPoint -= p;
			setConstitutionA((constitutionA + p));
		}
	}

	/**
	 * 得到某个职业发展线路对应的技能等级数据
	 * 
	 * @param careerThreadIndex
	 * @return
	 */
	public byte[] getCareerThreadLevels(int careerThreadIndex) {
		if (careerThreadIndex == 0) {
			return this.skillOneLevels;
		} else {
			return this.skillTwoLevels;
		}
	}

	public synchronized void tryToLearnSkill(int skillId, boolean isAuto, boolean learnByBook) {
		tryToLearnSkill(skillId, isAuto, learnByBook, false);
	}

	/**
	 * 尝试学习当前职业下指定序号的技能<br>
	 * 学习前要检查玩家是否有技能点，以及是否满足学习条件
	 * 
	 * 同时，此方法为技能升级方法。 如果用书学习那么此技能可以从零级开始，如果是玩家手动点击学习那么此技能必须从1开始
	 * 
	 * @param isAuto
	 *            是否自动
	 *            isNotice:是否弹窗提示
	 */
	public synchronized void tryToLearnSkill(int skillId, boolean isAuto, boolean learnByBook, boolean isNotice) {
		if (SkEnhanceManager.getInst().checkJJsk(this, skillId)) {
			return;
		}
		Logger log = Skill.logger;
		try {
			Career ca = CareerManager.getInstance().getCareer(career);
			Skill skill = this.getSkillById(skillId);
			if (skill == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您不能学习这项技能);
				this.addMessageToRightBag(hreq);
				if (Game.logger.isWarnEnabled()) Game.logger.warn("[{}] [{}] [{}] [{}] [学习技能失败] [{}] [是否通过技能书:{}] [{}]", new Object[] { this.getUsername(), getId(), getName(), ca.getName(), skillId, learnByBook, Translate.您没有这项技能 });
				return;
			}
			log.debug("Player.tryToLearnSkill: {} want learn {}", name, skill.getName());
			int oldpoint = getCurrSoul().unallocatedSkillPoint;

			String result = ca.isUpgradable(this, skillId, learnByBook);
			log.debug("Player.tryToLearnSkill: isUpgradable ret {}", result);
			if (result == null) {

				byte nowLevel = getSkillLevel(skillId);

				long needExp = skill.getNeedExp()[nowLevel];
				int needMoney = skill.getNeedMoney()[nowLevel];
				int needYuanQi = skill.getNeedYuanQi()[nowLevel];

				// 学习技能弹框提示
				{
					if (this.getCareer() == 5 && !isNotice) {
						boolean isLearnShouKuiSkill = false;
						if (ca.bianShenSkills != null) {
							for (int i = 0; i < ca.bianShenSkills.length; i++) {
								Skill bianShenSkill = ca.bianShenSkills[i];
								if (bianShenSkill != null && bianShenSkill.getId() == skillId) {
									isLearnShouKuiSkill = true;
								}
							}
						}
						if (isLearnShouKuiSkill) {
							WindowManager wm = WindowManager.getInstance();
							MenuWindow mw = wm.createTempMenuWindow(600);
							String contentmess = Translate.translateString(Translate.学习变身后技能提示, new String[][] { { Translate.COUNT_1, String.valueOf(needExp) }, { Translate.STRING_1, BillingCenter.得到带单位的银两(needMoney) } });
							mw.setDescriptionInUUB(contentmess);
							Option_Learn_Skill_Sure option1 = new Option_Learn_Skill_Sure();
							option1.setText(MinigameConstant.CONFIRM);
							option1.setArgs(skillId, isAuto, learnByBook);
							Option[] options = new Option[] { option1 };
							mw.setOptions(options);
							CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
							this.addMessageToRightBag(creq);
							return;
						}
					}
				}

				if (needMoney > 0) {
					BillingCenter bc = BillingCenter.getInstance();
					try {
						bc.playerExpense(this, needMoney, CurrencyType.BIND_YINZI, ExpenseReasonType.SKILL_LEVEL_UP, "技能升级");
					} catch (Exception e) {
						e.printStackTrace();
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.绑银不足);
						this.addMessageToRightBag(hreq);
						if (Game.logger.isWarnEnabled()) Game.logger.warn("[{}] [{}] [{}] [{}] [学习技能失败] [{}] [是否通过技能书:{}] [{}]", new Object[] { this.getUsername(), getId(), getName(), ca.getName(), skillId, learnByBook, Translate.绑银不足 });
						return;
					}
				}
				if (needExp > 0) {
					long oldExp = this.getExp();
					this.setExp(exp - needExp);

					if (GamePlayerManager.logger.isWarnEnabled()) {
						GamePlayerManager.logger.warn("[玩家减少经验值] [{}] [{}] [ID:{}] [{}] [学习技能] [变化：{}-->{}]", new Object[] { this.getUsername(), getName(), this.getId(), needExp, oldExp, this.getExp() });
					}

				}

				if (needYuanQi > 0) {
					this.setEnergy(energy - needYuanQi);
					if (GamePlayerManager.logger.isWarnEnabled()) {
						Game.logger.warn("[玩家减少修法值] [{}] [{}] [ID:{}] [学习技能] [消耗修法值：{}] [当前修法值：{}]", new Object[] { this.getUsername(), getName(), this.getId(), needYuanQi, energy });
					}
				}
				getCurrSoul().setUnallocatedSkillPoint((getCurrSoul().unallocatedSkillPoint - skill.getNeedPoint()[nowLevel]));
				setUnallocatedSkillPoint(getCurrSoul().unallocatedSkillPoint);
				int nowpoint = this.unallocatedSkillPoint;
				byte newLevel = addSkillLevel(skillId);
				SkillInfo si = new SkillInfo();
				si.setInfo(this, skill);
				QUERY_CAREER_INFO_BY_ID_RES qciRes = new QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
				this.addMessageToRightBag(qciRes);
				NEW_QUERY_CAREER_INFO_BY_ID_RES NEW_qciRes = new NEW_QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
				this.addMessageToRightBag(NEW_qciRes);
				String description = Translate.学习技能成功;
				try {
					description = Translate.translateString(Translate.学习技能成功详细, new String[][] { { Translate.STRING_1, skill.getName() }, { Translate.LEVEL_1, newLevel + "" } });
					if (skill instanceof ActiveSkill && ((ActiveSkill) skill).isNuqiFlag() && newLevel == 1) { // 获得怒气技能
						try {
							EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.获得怒气技能, 1L });
							EventRouter.getInst().addEvent(evt3);
						} catch (Exception eex) {
							PlayerAimManager.logger.error("[目标系统] [统计玩家获得怒气技能异常] [" + this.getLogString() + "]", eex);
						}
					}
					boolean xinfa = false;
					CareerManager cm = CareerManager.getInstance();
					Career career = cm.getCareer(this.career);
					Skill[] skills = career.getXinfaSkills();
					if (skills != null) {
						for (int i = 0; i < skills.length; i++) {
							Skill s = skills[i];
							if (s != null && s.getId() == skillId) {
								xinfa = true;
								break;
							}
						}
					}
					if (xinfa) {
						boolean isNotZhuHun = skill instanceof IncreaseFaGongPassiveSkill || skill instanceof IncreaseWuGongPassiveSkill || skill instanceof IncreaseFaFangPassiveSkill || skill instanceof IncreaseWuFangPassiveSkill || skill instanceof IncreaseHpPassiveSkill;
						if (isNotZhuHun) {

							if (newLevel == 1) {
								AchievementManager.getInstance().record(this, RecordAction.获得心法, 1);
							} else {
								AchievementManager.getInstance().record(this, RecordAction.心法升级, newLevel);
							}
							if (ArticleManager.logger.isWarnEnabled()) {
								ArticleManager.logger.warn(this.getPlayerPropsString() + "[学习心法后]");
							}
							try {
								int tempLevel = skillId % 1000 + newLevel;
								if (skill instanceof IncreaseFaGongPassiveSkill || skill instanceof IncreaseWuGongPassiveSkill) {
									try {
										EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.内攻或外攻心法最大重数, tempLevel });
										EventRouter.getInst().addEvent(evt3);
									} catch (Exception eex) {
										PlayerAimManager.logger.error("[目标系统] [统计玩家内攻或外攻心法最大值异常] [" + this.getLogString() + "]", eex);
									}
								}
								try {
									EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.心法最大重数, tempLevel });
									EventRouter.getInst().addEvent(evt3);
								} catch (Exception eex) {
									PlayerAimManager.logger.error("[目标系统] [统计玩家内攻或外攻心法最大值异常] [" + this.getLogString() + "]", eex);
								}
								if (skill instanceof IncreaseFaFangPassiveSkill) {
									EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.内防心法重数, tempLevel });
									EventRouter.getInst().addEvent(evt3);
								} else if (skill instanceof IncreaseWuFangPassiveSkill) {
									EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.外防心法重数, tempLevel });
									EventRouter.getInst().addEvent(evt3);
								}
								try {
									int[] tempSkLv = new int[5];
									if (career.xinfaSkills != null) {
										for (int i = 0; i < career.xinfaSkills.length; i++) {
											Skill tempSkill = career.xinfaSkills[i];
											if (tempSkill != null) {
												if (xinfaLevels[i] > 0) {
													if (tempSkill instanceof IncreaseHpPassiveSkill) {
														tempSkLv[0] += xinfaLevels[i];
													} else if (tempSkill instanceof IncreaseWuGongPassiveSkill) {
														tempSkLv[1] += xinfaLevels[i];
													} else if (tempSkill instanceof IncreaseWuFangPassiveSkill) {
														tempSkLv[2] += xinfaLevels[i];
													} else if (tempSkill instanceof IncreaseFaGongPassiveSkill) {
														tempSkLv[3] += xinfaLevels[i];
													} else if (tempSkill instanceof IncreaseFaFangPassiveSkill) {
														tempSkLv[4] += xinfaLevels[i];
													}
												}
											}
										}
									}
									if (PlayerAimManager.logger.isDebugEnabled()) {
										PlayerAimManager.logger.debug("[目标系统] [tempSkLv:" + Arrays.toString(tempSkLv) + "] [" + this.getLogString() + "]");
									}
									Arrays.sort(tempSkLv);
									if (tempSkLv[1] > 1) {
										EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.心法随意4个到达的最大重数, tempSkLv[1] });
										EventRouter.getInst().addEvent(evt3);
									}
									if (tempSkLv[3] > 1) {
										EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.心法随意2个到达的最大重数, tempSkLv[3] });
										EventRouter.getInst().addEvent(evt3);
									}
								} catch (Exception exct) {
									PlayerAimManager.logger.error("[目标系统] [统计多个心法等级异常] [" + this.getLogString() + "]", exct);
								}
							} catch (Exception eeex) {
								PlayerAimManager.logger.error("[目标系统] [心法相关统计异常] [" + this.getLogString() + "]", eeex);
							}
						}
					} else {
						if (newLevel == 1) {
							AchievementManager.getInstance().record(this, RecordAction.获得技能, 1);
						} else {
							AchievementManager.getInstance().record(this, RecordAction.技能升级, newLevel);
							try {
								Career creer = CareerManager.getInstance().getCareer(this.getCareer());
								if (creer != null && creer.basicSkills != null) {
									for (int kk = 0; kk < creer.basicSkills.length; kk++) {
										if (creer.basicSkills[kk].getId() == skillId) {
											if (newLevel >= creer.basicSkills[kk].getMaxLevel()) {
												EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.基础技能满级个数, 1L });
												EventRouter.getInst().addEvent(evt);
											}
											if (kk == 1) {
												EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.第二个基础技能等级, newLevel });
												EventRouter.getInst().addEvent(evt);
											}
											break;
										}
									}
								}
							} catch (Exception eex) {
								PlayerAimManager.logger.error("[目标系统] [统计玩家升级技能异常][" + this.getLogString() + "]", eex);
							}
						}
					}
				} catch (Exception ex) {
					Skill.logger.error("Player.tryToLearnSkill: ", ex);
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
				this.addMessageToRightBag(hreq);
				if (Game.logger.isInfoEnabled()) Game.logger.info("[技能加点] [{}] [{}] [{}] [{}] [skillId:{}] [{}] [{}/{}] [未分配点：{}] [是否使用技能书:{}]", new Object[] { this.getUsername(), getId(), getName(), ca.getName(), skillId, skill.getName(), newLevel, skill.getMaxLevel(), unallocatedSkillPoint, learnByBook });
				if (!isAuto) {
					Player.sendPlayerAction(this, PlayerActionFlow.行为类型_加技能点, skill.getName(), 0, new Date(), GamePlayerManager.isAllowActionStat());
				} else {
					Player.sendPlayerAction(this, PlayerActionFlow.行为类型_自动加技能点, skill.getName(), 0, new Date(), GamePlayerManager.isAllowActionStat());
				}
				// 被动技能改变数值
				if (skill instanceof PassiveSkill) {
					int level = this.getSkillLevel(skill.getId());
					PassiveSkill ps = (PassiveSkill) skill;
					if (level == 1) {
						ps.run(this, level);
					} else if (level > 1) {
						ps.levelUp(this, level);
					}
				} else if (skill.getSkillType() == Skill.SKILL_ACTIVE) {
					// 检查玩家前2个快捷栏有没有设置快捷键，如果没有，自动设置
					// ShortcutAgent shortcutagent = new ShortcutAgent();
					// shortcutagent.load(getShortcut());
					// if(
					// shortcutagent.checkAndSetFirst2Shortcut(skill.getId()) ){
					// this.setShortcut(shortcutagent.toByteArray());
					//
					// QUERY_SHORTCUT_RES res = new
					// QUERY_SHORTCUT_RES(GameMessageFactory.nextSequnceNum(),
					// getShortcut());
					// addMessageToRightBag(res);
					// }
				}
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
				this.addMessageToRightBag(hreq);
				if (Game.logger.isWarnEnabled()) Game.logger.warn("[{}] [{}] [{}] [{}] [学习技能失败] [{}] [{}] [技能最大等级：{}] [需要技能点：{}] [技能等级：{}] [剩余：{}]", new Object[] { this.getUsername(), getId(), getName(), ca.getName(), result, skill.getName(), skill.getMaxLevel(), skill.getNeedCareerThreadPoints(), this.getSkillLevel(skill.getId()), unallocatedSkillPoint });
			}
		} catch (Exception e) {
			Game.logger.error("升级技能出错:", e);
		}
	}

	public void tryCleanSkill(int skillID) {
		Career ca = CareerManager.getInstance().getCareer(career);
		Skill skill = this.getSkillById(skillID);
		if (skill == null) {
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[{}] [{}] [{}] [{}] [清除技能失败] [{}]", new Object[] { this.getUsername(), getId(), getName(), ca.getName(), skillID });
			return;
		}

		Skill[] skills = ca.getXinfaSkills();
		int xinfaIndex = -1;
		if (skills != null) {
			for (int i = 0; i < skills.length; i++) {
				Skill s = skills[i];
				if (s != null && s.getId() == skillID) {
					xinfaIndex = i;
					break;
				}
			}
		}
		if (xinfaIndex >= 5 && xinfaIndex < 17) {
			int oldLevel = getXinfaLevels()[xinfaIndex];
			if (oldLevel <= 1) {
				sendError(Translate.不能洗魂);
				return;
			}

			if (!removeArticle(Career.cleanArticleName, "洗髓清除")) {
				sendError(Translate.translateString(Translate.缺少洗魂需要物品, new String[][] { { Translate.STRING_1, Career.cleanArticleName } }));
				return;
			}

			((PassiveSkill) skill).close(this, getXinfaLevels()[xinfaIndex]);
			getXinfaLevels()[xinfaIndex] = 1;
			setXinfaLevels(getXinfaLevels());
			((PassiveSkill) skill).run(this, 1);
			SkillInfo si = new SkillInfo();
			si.setInfo(this, skill);
			QUERY_CAREER_INFO_BY_ID_RES qciRes = new QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
			this.addMessageToRightBag(qciRes);
			NEW_QUERY_CAREER_INFO_BY_ID_RES NEW_qciRes = new NEW_QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
			this.addMessageToRightBag(NEW_qciRes);
			Game.logger.warn("[洗魂成功] [" + getLogString() + "] [技能:" + skills[xinfaIndex].getName() + "~" + skillID + "] [原等级:" + oldLevel + "]");
		}
	}

	/**
	 * 得到使用道具代理
	 * 
	 * @return
	 */
	public UsingPropsAgent getUsingPropsAgent() {
		return usingPropsAgent;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// 以下与战斗状态有关

	/**
	 * 通知周围的守卫，p先打我的
	 */
	private void notifyArroundGuard(Player p) {
	}

	/**
	 * 通知此对象，我准备攻击a
	 * 
	 * @param a
	 */
	public void notifyPrepareToFighting(Fighter a) {
		// 增加敌人
		if (this.isDeath() == false && a instanceof LivingObject) enemyList.add(a, true, this.heartBeatStartTime);

		this.currentEnemyTarget = a;

	}

	/**
	 * 通知此对象，a准备攻击我 当a为怪的时候，怪将我作为敌人的时候就通知 当a为玩家的时候，服务器收到有目标攻击指令时通知
	 * 
	 * @param a
	 */
	public void notifyPrepareToBeFighted(Fighter a) {
		// 在这里检查是否谁先动手
		if (a instanceof Player) {
			Player p = (Player) a;
			if (p.getCountry() != this.getCountry()) {
				// if (enemyList.contains(p) == false) {
				// p 通知周围的守卫，有人先打他
				notifyArroundGuard(p);
				// }
			}
		}

		// 增加敌人
		if (this.isDeath() == false && a instanceof LivingObject) enemyList.add(a, false, this.heartBeatStartTime);

		if (this.currentEnemyTarget == null) {
			this.currentEnemyTarget = a;
		}
	}

	/**
	 * 通知此对象，a不再攻击我。 当a为怪的时候，a不在把我作为攻击目标时通知 当a为玩家的时候，我从玩家的广播区域里消失，通知
	 * 
	 * @param a
	 */
	public void notifyEndToBeFighted(Fighter a) {
		enemyList.passiveBreak(a);

		if (this.currentEnemyTarget == a) {
			this.currentEnemyTarget = null;
		}
	}

	/**
	 * 通知此对象，a从我的广播区域里消失。 此时，将a从我的敌人列表中清除
	 * 
	 * @param a
	 */
	public void notifyEndToFighting(Fighter a) {
	}

	/**
	 * 得到属性点分配规则
	 * 
	 * @return
	 */
	public int getPropertyPointAllocatePlan() {
		return propertyPointAllocatePlan;
	}

	public void setPropertyPointAllocatePlan(int propertyPointAllocatePlan) {
		this.propertyPointAllocatePlan = propertyPointAllocatePlan;
	}

	/**
	 * 系统加载一个人物信息的时候，设置人物身上的buff
	 * 
	 * @param buffs
	 */
	public void setBuffs(Buff[] buffs) {
		for (int i = 0; i < buffs.length; i++) {
			this.buffs.add(buffs[i]);
		}
	}
	
	public void setBuffs(ArrayList<Buff> buffs){
		this.buffs = buffs;
	}

	public void notifyKnapsackDirty() {
		if (knapsacks_common != null) {
			for (Knapsack knap : knapsacks_common) {
				if (knap != null && knap.isModified()) {
					setDirty(true, "knapsacks_common");
					knap.setModified(false);
					break;
				}
			}
		}

		if (knapsacks_fangBao != null) {
			if (knapsacks_fangBao.isModified()) {
				setDirty(true, "knapsacks_fangBao");
				knapsacks_fangBao.setModified(false);
			}
		}

		if (knapsacks_cangku != null) {
			if (knapsacks_cangku.isModified()) {
				setDirty(true, "knapsacks_cangku");
				knapsacks_cangku.setModified(false);
			}
		}

		if (knapsacks_QiLing != null) {
			if (knapsacks_QiLing.isModified()) {
				setDirty(true, "knapsacks_QiLing");
				knapsacks_QiLing.setModified(false);
			}
		}
		if (knapsacks_warehouse != null) {
			if (knapsacks_warehouse.isModified()) {
				setDirty(true, "knapsacks_warehouse");
				knapsacks_warehouse.setModified(false);
			}
		}

		if (ecs != null && ecs.isModified()) {
			setDirty(true, "currSoul");
			ecs.setModified(false);
		} else {
			if (getUnusedSoul() != null) {
				for (Soul s : getUnusedSoul()) {
					if (s.getEc() != null && s.getEc().isModified()) {
						setDirty(true, "unusedSoul");
						s.getEc().setModified(false);
						break;
					}
				}
			}
		}
	}

	public void setDirty(boolean dirty, String field) {
		try {
			SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
			if (em != null) {
				em.notifyFieldChange(this, field);
			}
		} catch (java.lang.IllegalArgumentException e) {

		}
	}

	/**
	 * 玩家最后一次更新时间，即存盘时间
	 * 
	 * @return
	 */
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// 以下为任务事件相关的方法，
	// 但服务器发生此事件的时候，调用此方法，用于修改相关的任务实体
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 丢弃一个物品
	 */
	public void discardOneArticle(ArticleEntity ae) {

	}

	/**
	 * 杀死了一个玩家
	 */
	public void killOnePlayer(Player p) {
		TaskAction action = TaskActionOfKillPlayer.createTaskAction(this, p);
		// 这里队伍是否要共享
		dealWithTaskAction(action);
	}

	public static boolean needlog = true;
	public static String[] attentionMonster = { Translate.红孩儿, Translate.舜, Translate.如花, Translate.红孩儿元神, Translate.舜元神, Translate.如花元神, Translate.孟婆, Translate.孟婆元神, Translate.无常冥帅, Translate.无常冥帅元神, Translate.判官崔钰, Translate.判官崔钰元神 };

	public static boolean inAttentionMonsters(String monsterName) {
		for (String s : attentionMonster) {
			if (s.equals(monsterName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 杀死了一只怪
	 */
	public void killOneSprite(Sprite s) {
		// 由于杀怪和配在任务里的怪物掉落是队伍共享的，这里要处理全队伍的
		// 这里要处理5次。一次是杀确定的怪，一次是杀死一类怪（等级区间）,一次是怪物匹配角色等级,一次是获得物品(配置在任务里的)
		// 一次是有随机怪物目标个数的
		// 另外要处理有额外需求的任务
		boolean inAttention = false;
		if (needlog) {
			if (s.getLevel() >= 111) {
				inAttention = inAttentionMonsters(s.getName());
			}
		}
		Player players[] = getSameTeamPlayers(inAttention);
		List<TaskAction> articleActions = getMonsterArticleAction(s);
		TaskAction monsterAction = TaskActionOfMonster.createAction(s);
		TaskAction monsterLvAction = TaskActionOfMonsterLv.createAction(s);
		TaskAction monsterRandomNum = TaskActionOfMonsterRandomNum.createTaskAction(s);
		if (inAttention) {
			TaskSubSystem.logger.error(this.getTeamLogString() + " [杀死了怪物] [" + s.getName() + "] [Team.length:" + players.length + "] [this.getTeamMark():" + this.getTeamMark() + "] [Team:" + getTeam() + "] [teamMembers.length:" + getTeam().members.size() + "]");
		}
		for (int i = 0, j = players.length; i < j; i++) {
			try {
				Player p = players[i];
				TaskAction monsterLvNearAction = TaskActionOfMonsterLvNearPlayer.createTaskAction(s, p);
				p.dealWithTaskAction(monsterLvNearAction, monsterAction, monsterLvAction, monsterRandomNum);
				for (int m = 0, n = articleActions.size(); m < n; m++) {
					p.dealWithTaskAction(articleActions.get(m));
				}
				if (inAttention) {
					TaskSubSystem.logger.error(this.getTeamLogString() + " [杀死了怪物] [" + s.getName() + "] [享受到队伍福利:" + p.getLogString() + "]");
				}
				if (s instanceof Monster) {
					try {
						Monster tm = (Monster) s;
						for (int ii = 0; ii < PlayerAimManager.单独统计怪物id.length; ii++) {
							if (tm.getSpriteCategoryId() == PlayerAimManager.单独统计怪物id[ii]) {
								EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), PlayerAimManager.对应统计action[ii], 1L });
								EventRouter.getInst().addEvent(evt);
							}
						}
					} catch (Exception e) {
						PlayerAimManager.logger.error("[目标系统] [杀怪统计异常] [" + p.getLogString() + "]");
					}
				}
			} catch (Exception e) {
				TaskSubSystem.logger.error(this.getTeamLogString() + " [杀死了怪物] [" + s.getName() + "] [players.length:" + players.length + "]", e);
			}
		}
		AchievementManager.getInstance().record(this, RecordAction.杀死怪物);
	}

	public boolean isAround(Player player) {
		if (getCurrentGame() != null) {
			return getCurrentGame().contains(player);// &&
			// Math.abs(player.getX()
			// - getX()) <= 320 &&
			// Math.abs(player.getY()
			// - getY()) <= 320;
		}
		return true;
	}

	/**
	 * 获得了一个物品
	 */
	public void obtainOneArticle(ArticleEntity ae) {
		TaskAction taskAction = TaskActionOfGetArticle.createAction(ae);
		TaskAction taskAction2 = TaskActionOfGetArticleAndDelete.createAction(ae);
		dealWithTaskAction(taskAction, taskAction2);
	}

	/**
	 * 到达了一个地方
	 */
	public void reachOnePlace(String placeName) {

	}

	/**
	 * 和某NPC对话
	 */
	public void talkWithOneNPC(String npcName) {
		TaskAction action = TaskActionOfTalkToNPC.createAction(npcName);
		dealWithTaskAction(action);
	}

	public void deliverTask(String taskName) {
		TaskAction action = TaskActionOfTaskDeliver.createAction(taskName);
		dealWithTaskAction(action);
	}

	/**
	 * 使用了一个道具
	 */
	public void useOneProps(PropsEntity pe, Player player) {
		TaskAction action = TaskActionOfUseArticle.createAction(pe, player);
		dealWithTaskAction(action);
	}

	/**
	 * 成功护送一个NPC到达目的地
	 * 
	 * @param npcName
	 * @param npcColorGrade
	 *            /npc的颜色等级
	 */
	public void convoyOneNPC(String npcName, int npcColorGrade) {
		TaskAction convoyNPC = TaskActionOfConvoyNPC.createTaskAction(npcName, npcColorGrade);
		dealWithTaskAction(convoyNPC);
		if (SilvercarManager.logger.isWarnEnabled()) {
			SilvercarManager.logger.warn(this.getLogString() + "[完成护送任务] [护送NPC:" + npcName + "] [颜色:" + npcColorGrade + "]");
		}
	}

	/**
	 * 赢得一场战场
	 * 
	 * @param battleFieldMapName
	 */
	public void winOneBattleField(String battleFieldMapName) {

	}

	/**
	 * 充值回调
	 * 
	 * @param type
	 * @param rmb
	 *            人民币金额，以分为单位
	 * @param yuanbao
	 *            元宝数量
	 */
	public void chargeNotify(int type, long rmb, long yuanbao) {
	}

	/**
	 * 职业发生了变化
	 * 
	 * @param oldCareer
	 * @param newCareer
	 */
	public void careerChanged(int oldCareer, int newCareer) {
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 升级通知
	 */
	public void upgradeNotify(int newLevel, int oldLevel) {
	}

	/**
	 * 设置职业
	 */
	public void setCareer(byte value) {
		super.setCareer(value);
		this.setDirty(true, "career");
	}

	/**
	 * 设置目前的装备栏
	 */
	public void setCurrentSuit(byte value) {
		super.setCurrentSuit(value);
		setDirty(true, "currentSuit");
	}

	/**
	 * 设置经验值
	 */
	@Override
	public void setExp(long value) {
		super.setExp(value);
		setDirty(true, "exp");
		GamePlayerManager.createPlayerMsg(this, 2, false);
		this.setLastUpdateExpTime(SystemTime.currentTimeMillis());
	}

	private transient boolean[] noticeTeamMarks = new boolean[64];

	public boolean[] getNoticeTeamMarks() {
		return noticeTeamMarks;
	}

	private long getNoticeTeamValue(int id) {
		switch (id) {
		case 0:
			return hp;
		case 1:
			return maxHP;
		case 2:
			return mp;
		case 3:
			return maxMP;
		case 4:
			return level;
		default:
			break;
		}
		return -1l;

	}

	/**
	 * 玩家有队伍，某些属性改变，通知队伍中的人
	 */
	public void noticeTeamChange() {
		if (this.teamMark == Player.TEAM_MARK_NONE || this.getTeam() == null) {
			return;
		}
		boolean noticeTeamMarks[] = this.getNoticeTeamMarks();
		List<TeamChangeNotice> list = null;
		for (int l = 0; l < noticeTeamMarks.length; l++) {
			if (noticeTeamMarks[l]) {
				if (list == null) {
					list = new ArrayList<TeamChangeNotice>();
				}
				noticeTeamMarks[l] = false;
				TeamChangeNotice notice = new TeamChangeNotice(this.getId(), l, getNoticeTeamValue(l));
				list.add(notice);
			}
		}

		if (list != null) {
			if (this.teamMark != Player.TEAM_MARK_NONE && this.getTeam() != null) {

				TeamChangeNotice[] arr = list.toArray(new TeamChangeNotice[0]);
				NOTICE_TEAM_CHANGE_RES res = new NOTICE_TEAM_CHANGE_RES(GameMessageFactory.nextSequnceNum(), arr);
				try {
					for (Player p : this.getTeam().getMembers()) {
						p.addMessageToRightBag(res);
					}
				} catch (Exception e) {
					TeamSubSystem.logger.error("[玩家属性变化通知队伍其他人] [" + this.getLogString() + "] []", e);
				}
				if (TeamSubSystem.logger.isDebugEnabled()) TeamSubSystem.logger.debug("[发送队伍变化通知] [" + this.getLogString() + "] [变化个数:" + arr.length + "]");
			}
		}

	}

	/**
	 * 设置HP
	 */
	public void setHp(int value) {
		if(SkEnhanceManager.getInst() != null){
			SkEnhanceManager.getInst().hpLowCreatBuff(this);
		}
		if (value > Integer.MAX_VALUE) {
			value = Integer.MAX_VALUE;
		}
		super.setHp(value);
		noticeTeamMarks[0] = true;
		setDirty(true, "hp");
	}

	/**
	 * 设置MP
	 */
	public void setMp(int value) {
		if (value > Integer.MAX_VALUE) {
			value = Integer.MAX_VALUE;
		}
		super.setMp(value);
		noticeTeamMarks[2] = true;
		setDirty(true, "mp");
	}

	/**
	 * 设置最大HP
	 */
	public void setMaxHP(int value) {
		value += SkEnhanceManager.getInst().fixMaxHP(this, value);
		if (value > Integer.MAX_VALUE) {
			value = Integer.MAX_VALUE;
		}
		super.setMaxHP(value);
		noticeTeamMarks[1] = true;
	}

	/**
	 * 设置最大MP
	 */
	public void setMaxMP(int value) {
		if (value > Integer.MAX_VALUE) {
			value = Integer.MAX_VALUE;
		}
		super.setMaxMP(value);
		noticeTeamMarks[3] = true;
	}

	/**
	 * 设置玩家名字
	 */
	public void setName(String value) {
		super.setName(value);
		setDirty(true, "name");
	}

	/**
	 * 设置性别
	 */
	public void setSex(byte value) {
		super.setSex(value);
		setDirty(true, "sex");
	}

	/**
	 * 设置武器类型
	 */
	public void setWeaponType(byte value) {
		super.setWeaponType(value);
	}

	public void setViewHeight(int viewHeight) {
		super.setViewHeight(viewHeight);
		setDirty(true, "viewHeight");
	}

	public void setViewWidth(int viewWidth) {
		super.setViewWidth(viewWidth);
		setDirty(true, "viewWidth");
	}

	public void setX(int x) {
		if (this.x != x) {
			lastMoveTime = System.currentTimeMillis();
		}
		super.setX(x);
		setDirty(true, "x");
	}

	public void setY(int y) {
		if (this.y != y) {
			lastMoveTime = System.currentTimeMillis();
		}
		super.setY(y);
		setDirty(true, "y");
	}

	/**
	 * 设置当前地图
	 * 
	 * @param string
	 */
	public void setMapName(String string) {
		setLastGame(string);
	}

	public String getMapName() {
		if (game == null) {
			return lastGame;
		}
		return game;
	}

	// ////////////////////////////////////////////////////////////////////////////
	//
	// 以下为主动技能的参数
	protected transient HashMap<Integer, ActiveSkillParam> activeSkillParamMap = new HashMap<Integer, ActiveSkillParam>();

	public void addActiveSkillParam(ActiveSkill skill, ActiveSkillParam p) {
		activeSkillParamMap.put(skill.getId(), p);
	}

	public ActiveSkillParam getActiveSkillParam(ActiveSkill skill) {
		return activeSkillParamMap.get(skill.getId());
	}

	public boolean isNewlyEnterGameFlag() {
		return newlyEnterGameFlag;
	}

	// 离线时间计算
	public void offlineTimeCount() {
		Date date = new Date();
		long offlineTime = offlineTimeCount(date.getTime());
		if (offlineTime != 0) {
			this.setTotalOfflineTime(this.getTotalOfflineTime() + offlineTime);
			if (this.totalOfflineTime > MAX_TIME_VALUE) {
				this.setTotalOfflineTime(MAX_TIME_VALUE);
			}
		}
	}

	public void setTotalOfflineTime(long value) {
		this.totalOfflineTime = value;
		this.setDirty(true, "totalOfflineTime");
	}

	/**
	 * 离线时间计算函数
	 * 
	 * @param currentTime
	 * @param quitGameTime
	 * @return
	 */
	private long offlineTimeCount(long currentTime) {
		long offlineTime = 0;
		// 离线时间计算公式
		offlineTime = (currentTime - quitGameTime) / 3600000;

		// quitGameTime = currentTime;

		return offlineTime;
	}

	public void setNewlyEnterGameFlag(boolean newlyEnterGameFlag) {
		this.newlyEnterGameFlag = newlyEnterGameFlag;
	}

	public void setPracticeValue() {
		super.setParaticeValue();
	}

	/**
	 * 获取玩家的账号通行证
	 * 
	 * @return
	 */
	public Passport getPassport() {
		return passport;
	}

	public void setPassport(Passport passport) {
		this.passport = passport;
	}

	/**
	 * 登陆服务器时间
	 */
	private long loginServerTime;

	/**
	 * 获得玩家登陆服务器的时间
	 * 
	 * @return
	 */
	public long getLoginServerTime() {
		return loginServerTime;
	}

	public void setLoginServerTime(long loginServerTime) {
		this.loginServerTime = loginServerTime;
		setDirty(true, "loginServerTime");
	}

	/**
	 * 发送聊天型的提示信息给用户自己
	 * 
	 * 
	 * @param content
	 * @param entity
	 */
	public void send_HINT_REQ(String content, ArticleEntity entity) {
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, content + entity.getArticleName());
		addMessageToRightBag(hreq);
	}

	/**
	 * 发送聊天型的提示信息给用户自己
	 * 
	 * @param content
	 * @param entity
	 */
	public void send_HINT_REQ(String content) {
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, content);
		this.addMessageToRightBag(hreq);
	}

	/**
	 * 得到上次检查邮件的时间
	 * 
	 * @return
	 */
	public long getLastCheckMailTime() {
		return lastCheckMailTime;
	}

	public void setLastCheckMailTime(long lastCheckMailTime) {
		this.lastCheckMailTime = lastCheckMailTime;
	}

	// ////////////////////////////////////////////////////////////////////////////////
	//
	// 检测用户的行为
	//

	public void notifyClientSetPosition(SET_POSITION_REQ msg) {

	}

	/**
	 * 从客户端收到一个移动指令
	 * 
	 * @param path
	 */
	public void notifyClientMoveTrace(MoveTrace path) {

	}

	public static class ClientHeartBeatWrapper {
		public HEARTBEAT_CHECK_REQ req;
		public long receiveTime;
	}

	// 4分钟
	public static long 玩家的心跳保存周期 = 70 * 1000;
	public static double 方差标准值 = 0.5;
	public static double 平均值警告标准值 = 4800;
	public static double 平均值外挂标准值 = 4500;

	public static int 连续发现加速标准值 = 10;

	public static int 连续5次加速累计次数标准值 = 3;

	public static int 连续2次加速累计次数标准值 = 5;

	public static int 清理连续累计次数分钟间隔 = 600;

	public static boolean 是否启用加速封号功能 = true;

	public static int CHECK_PACKAGE_NUM = 5;
	// 保存玩家的心跳
	private transient LinkedList<ClientHeartBeatWrapper> clientHeartBeatRequestList = new LinkedList<ClientHeartBeatWrapper>();

	// 是否为加速外挂
	public transient boolean isJiaShuWaiGua = false;

	// 疑似外挂
	public transient boolean isMaybeJiaShuWaiGua = false;

	// 最后一次时间
	public transient long lastJiaShuWaiGuaCheckTime = 0;

	// 最后一次时间
	public transient long lastJiaShuWaiGuaFoundTime = 0;

	// 发现了多少次
	public transient int jiaShuWaiGuaFoundTimes = 0;

	// 连续加速发现了多少次
	public transient int jiaShuWaiGuaContinueFoundTimes = 0;

	// 连续5次加速发现了多少次
	public transient int jiaShuWaiGuaContinue5FoundTimes = 0;
	private transient boolean jiaShuWaiGuaContinue5FoundTimesFlag = true;

	// 连续2次加速发现了多少次
	public transient int jiaShuWaiGuaContinue2FoundTimes = 0;
	private transient boolean jiaShuWaiGuaContinue2FoundTimesFlag = true;

	public transient boolean hasFenghaoByJiaShu = false;

	// 玩家上次登录时间
	public transient long playerLastLoginTime = 0;

	public long getPlayerLastLoginTime() {
		return playerLastLoginTime;
	}

	public void setPlayerLastLoginTime(long playerLastLoginTime) {
		this.playerLastLoginTime = playerLastLoginTime;
	}

	// 供页面使用
	public ClientHeartBeatWrapper[] getClientHeartBeatWrappers() {
		synchronized (clientHeartBeatRequestList) {
			return clientHeartBeatRequestList.toArray(new ClientHeartBeatWrapper[0]);
		}
	}

	/**
	 * 通知客户端心跳,此方法有玩家的网络线程调用
	 * 
	 * @param req
	 */
	public void notifyClientHeartBeat(HEARTBEAT_CHECK_REQ req) {
		long now = System.currentTimeMillis();

		synchronized (clientHeartBeatRequestList) {
			Iterator<ClientHeartBeatWrapper> it = clientHeartBeatRequestList.iterator();
			while (it.hasNext()) {
				ClientHeartBeatWrapper w = it.next();
				if (w.receiveTime + 玩家的心跳保存周期 < now) {
					it.remove();
				} else {
					break;
				}
			}
			ClientHeartBeatWrapper w = new ClientHeartBeatWrapper();
			w.req = req;
			w.receiveTime = now;
			clientHeartBeatRequestList.add(w);

		}
	}

	// 此方法有心跳发起，5秒检查一次
	private void check_HEARTBEAT_CHECK_REQ() {
		long now = System.currentTimeMillis();

		long deltaD = 0;
		long startReceiveTime = 0;
		double fangca = 0;
		if (CHECK_PACKAGE_NUM < 4) CHECK_PACKAGE_NUM = 4;

		if (now - lastJiaShuWaiGuaFoundTime > 清理连续累计次数分钟间隔 * 60000) {
			jiaShuWaiGuaContinue5FoundTimes = 0;
			jiaShuWaiGuaContinue2FoundTimes = 0;
			jiaShuWaiGuaContinue2FoundTimesFlag = true;
			jiaShuWaiGuaContinue5FoundTimesFlag = true;
		}

		synchronized (clientHeartBeatRequestList) {
			Iterator<ClientHeartBeatWrapper> it = clientHeartBeatRequestList.iterator();
			while (it.hasNext()) {
				ClientHeartBeatWrapper w = it.next();
				if (w.receiveTime + 玩家的心跳保存周期 < now) {
					it.remove();

				}

			}

			if (clientHeartBeatRequestList.size() < CHECK_PACKAGE_NUM) return;

			if (clientHeartBeatRequestList.getLast().receiveTime + (CHECK_PACKAGE_NUM - 1) * 5000 < now) {

				jiaShuWaiGuaContinueFoundTimes = 0;
				jiaShuWaiGuaContinue2FoundTimesFlag = true;
				jiaShuWaiGuaContinue5FoundTimesFlag = true;
				return;
			}

			if (clientHeartBeatRequestList.getLast().receiveTime < lastJiaShuWaiGuaCheckTime) {
				return;
			}
			lastJiaShuWaiGuaCheckTime = now;

			for (int i = clientHeartBeatRequestList.size() - CHECK_PACKAGE_NUM; i >= 0 && i < clientHeartBeatRequestList.size() - 1; i++) {

				ClientHeartBeatWrapper prev = clientHeartBeatRequestList.get(i);
				ClientHeartBeatWrapper next = clientHeartBeatRequestList.get(i + 1);
				deltaD += (next.receiveTime - prev.receiveTime);

				if (startReceiveTime == 0) {
					startReceiveTime = prev.receiveTime;
				}
			}
			deltaD = deltaD / (CHECK_PACKAGE_NUM - 1);

			for (int i = clientHeartBeatRequestList.size() - CHECK_PACKAGE_NUM; i >= 0 && i < clientHeartBeatRequestList.size(); i++) {
				ClientHeartBeatWrapper prev = clientHeartBeatRequestList.get(i);
				long kkk = startReceiveTime + deltaD * (i - clientHeartBeatRequestList.size() + CHECK_PACKAGE_NUM);
				fangca += (kkk - prev.receiveTime) * (kkk - prev.receiveTime) / 1000000.0;
			}

		}

		// 最后7个点的方差，如果方差很小，说明这7个点是均匀的

		if (fangca < 方差标准值 && deltaD <= 平均值外挂标准值 && deltaD > 50) {

			// 应该是加速外挂
			isJiaShuWaiGua = true;
			lastJiaShuWaiGuaFoundTime = now;
			this.jiaShuWaiGuaFoundTimes++;

			this.jiaShuWaiGuaContinueFoundTimes++;

			if (jiaShuWaiGuaContinueFoundTimes >= 2 && jiaShuWaiGuaContinue2FoundTimesFlag) {
				jiaShuWaiGuaContinue2FoundTimes++;
				jiaShuWaiGuaContinue2FoundTimesFlag = false;
			}

			if (jiaShuWaiGuaContinueFoundTimes >= 5 && jiaShuWaiGuaContinue5FoundTimesFlag) {
				jiaShuWaiGuaContinue5FoundTimes++;
				jiaShuWaiGuaContinue5FoundTimesFlag = false;
			}

			if (是否启用加速封号功能 && jiaShuWaiGuaContinueFoundTimes >= 连续发现加速标准值) {

				jiasuFenghao("角色[" + name + "]在[" + GameConstants.getInstance().getServerName() + "]服使用加速外挂", 2, fangca, deltaD);
			} else if (是否启用加速封号功能 && jiaShuWaiGuaContinue2FoundTimes >= 连续2次加速累计次数标准值) {
				jiasuFenghao("角色[" + name + "]在[" + GameConstants.getInstance().getServerName() + "]服极其恶意使用加速外挂", 8, fangca, deltaD);
			} else if (是否启用加速封号功能 && jiaShuWaiGuaContinue5FoundTimes >= 连续5次加速累计次数标准值) {
				jiasuFenghao("角色[" + name + "]在[" + GameConstants.getInstance().getServerName() + "]服恶意使用加速外挂", 4, fangca, deltaD);
			} else {
				for (int i = 0; i >= 0 && i < clientHeartBeatRequestList.size(); i++) {

					ClientHeartBeatWrapper prev = clientHeartBeatRequestList.get(i);
					Game.logger.warn("[加速外挂] [发现加速外挂] [未封号处理] [编号：" + i + "] [连续次数：" + (jiaShuWaiGuaContinueFoundTimes) + "] [连续2次数：" + (jiaShuWaiGuaContinue2FoundTimes) + "] [连续5次数：" + (jiaShuWaiGuaContinue5FoundTimes) + "] [发现次数:" + jiaShuWaiGuaFoundTimes + "] [接收时间:" + (DateUtil.formatDate(new Date(prev.receiveTime), "HH:mm:ss.S")) + "] [客户端速度:" + prev.req.getSpeed() + "] [" + this.getLogString() + "]");
				}
				Game.logger.warn("[加速外挂] [发现加速外挂] [未封号处理] [连续次数：" + (jiaShuWaiGuaContinueFoundTimes) + "] [连续2次数：" + (jiaShuWaiGuaContinue2FoundTimes) + "] [连续5次数：" + (jiaShuWaiGuaContinue5FoundTimes) + "] [发现次数:" + jiaShuWaiGuaFoundTimes + "] [方差:" + fangca + "] [平均:" + deltaD + "] [" + this.getLogString() + "]");

			}

		} else if (deltaD < 平均值警告标准值) {

			lastJiaShuWaiGuaFoundTime = now;
			isMaybeJiaShuWaiGua = true;

			Game.logger.warn("[加速外挂] [未发现加速外挂] [平均值小于5秒] [连续次数：" + (jiaShuWaiGuaContinueFoundTimes) + "] [连续2次数：" + (jiaShuWaiGuaContinue2FoundTimes) + "] [连续5次数：" + (jiaShuWaiGuaContinue5FoundTimes) + "] [发现次数:" + jiaShuWaiGuaFoundTimes + "] [方差:" + fangca + "] [平均:" + deltaD + "] [" + this.getLogString() + "]");

		} else {
			jiaShuWaiGuaContinueFoundTimes = 0;
			jiaShuWaiGuaContinue2FoundTimesFlag = true;
			jiaShuWaiGuaContinue5FoundTimesFlag = true;
		}
	}

	// public static boolean sendFenghaoMessage = true;

	public void jiasuFenghao(String reason, int hours, double fangca, double deltaD) {

		DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", this.getUsername(), reason, GameConstants.getInstance().getServerName() + "-外挂检测模块", false, true, false, 0, hours);
		MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
		if (this.getConn() != null) {
			this.getConn().close();
		}

		for (int i = 0; i >= 0 && i < clientHeartBeatRequestList.size(); i++) {
			ClientHeartBeatWrapper prev = clientHeartBeatRequestList.get(i);
			Game.logger.warn("[加速外挂] [发现加速外挂] [已封号处理] [编号：" + i + "] [连续次数：" + (jiaShuWaiGuaContinueFoundTimes) + "] [连续2次数：" + (jiaShuWaiGuaContinue2FoundTimes) + "] [连续5次数：" + (jiaShuWaiGuaContinue5FoundTimes) + "] [发现次数:" + jiaShuWaiGuaFoundTimes + "] [接收时间:" + (DateUtil.formatDate(new Date(prev.receiveTime), "HH:mm:ss.S")) + "] [客户端速度:" + prev.req.getSpeed() + "] [" + this.getLogString() + "]");
		}

		Game.logger.warn("[加速外挂] [发现加速外挂] [已封号处理] [连续次数：" + (jiaShuWaiGuaContinueFoundTimes) + "] [连续2次数：" + (jiaShuWaiGuaContinue2FoundTimes) + "] [连续5次数：" + (jiaShuWaiGuaContinue5FoundTimes) + "] [发现次数:" + jiaShuWaiGuaFoundTimes + "] [方差:" + fangca + "] [平均:" + deltaD + "] [" + this.getLogString() + "]");

		hasFenghaoByJiaShu = true;
		jiaShuWaiGuaContinueFoundTimes = 0;
		jiaShuWaiGuaContinue2FoundTimes = 0;
		jiaShuWaiGuaContinue5FoundTimes = 0;
		jiaShuWaiGuaContinue2FoundTimesFlag = true;
		jiaShuWaiGuaContinue5FoundTimesFlag = true;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// / 以上代码是外挂检测
	// //////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param req
	 */
	public void notifyReceivePlayerMove(PLAYER_MOVETRACE_REQ req) {
		// 宠物暂时也是用的是PLAYER_MOVETRACE_REQ

		MoveTrace4Client mt = req.getMoveTrace4Client();
		if (mt.getType() == getClassType()) {
			long a = mt.getId();
			if (a == getId()) {
				// 玩家自己走路才打断
				if (this.timerTaskAgent != null) {
					this.timerTaskAgent.notifyMoved();
					if (FaeryManager.logger.isInfoEnabled()) {
						FaeryManager.logger.info(this.getLogString() + "[移动打断读条]");
					}
				}
			}
		}

	}

	public void notifyReceivePlayerMove(SET_POSITION_REQ req) {
		// 宠物暂时也是用的是SET_POSITION_REQ
	}

	// 离婚
	public void onDivorce() {
	}

	/**
	 * 推出帮会
	 */
	public void quitGang() {
	}

	/**
	 * when the player quit his gang or the his gang is disbanded,the player
	 * should delete all gang task
	 */
	private void deleteAllGangTask() {
	}

	/**
	 * 判断一个玩家是否在一个帮会里面， 此方法返回true的充分必要条件是，玩家身上设置的gangName必须存在，并且对应的工会也存在，
	 * 并且玩家确实在帮会里面。
	 * 
	 * @return
	 */
	public boolean isInGang() {
		return false;
	}

	// 第一次进入游戏标记
	private transient boolean recentlyCreatedButNotEnterGameFlag = false;
	private transient long recentlyCreatedButNotEnterGameTime = 0;

	protected void setRecentlyCreatedButNotEnterGameFlag(boolean b) {
		if (b) {
			recentlyCreatedButNotEnterGameFlag = true;
			recentlyCreatedButNotEnterGameTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}
	}

	// 第一次在线游戏标记
	private transient int firstTimeOnlineFlagMinuteDefines[] = new int[] { 1, 5, 15, 30, 60 };

	private transient boolean firstTimeOnlineFlagForMinutes[] = new boolean[firstTimeOnlineFlagMinuteDefines.length];
	private transient boolean firstTimeOnlineFlag = false;
	private transient long firstTimeOnlineFlagStartTime = 0;
	public transient boolean countryVoteFlag = false; // 玩家是否已经为国家官员投票

	/**
	 * 通知此角色，第一次进入游戏场景，此消息在用户进入游戏2秒后触发 此方法对于一个角色来说，一生只会调用一次。
	 * 
	 * 游戏可以在此方法中做一些相关的操作， 但是需要注意此方法是有此角色的心跳函数调用的， 需要此方法立即返回。
	 */
	public void notifyPlayerFirstEnterGame() {
	}

	/**
	 * 通知此角色，第一次进入游戏场景，并且在线多长时间。 此方法对于一个角色来说，同样的分钟数，一生只会调用一次。
	 * 
	 * 游戏可以在此方法中做一些相关的操作， 但是需要注意此方法是有此角色的心跳函数调用的， 需要此方法立即返回。
	 */
	public void notifyPlayerFirstOnlineGameForMinutes(int index) {
	}

	// //////////////////////////////////////////////////////////////////////////////
	//
	// 战场的相关属性，这些属性都不需要存盘
	//
	// 战场的控制方法
	// enterBattleField（） 进入战场
	// leaveBattleField（） 离开战场
	// 这两个方法分别会调用BattleField接口的方法
	//
	//
	//
	// ///////////////////////////////////////////////////////////////////////////////

	// 记录进入战场的地点
	protected transient String enterBattleFieldMapName = "";
	protected transient int enterBattleFieldX = 0;
	protected transient int enterBattleFieldY = 0;

	public void setEnterBattleFieldPoint(String mapName, int x, int y) {
		enterBattleFieldMapName = mapName;
		enterBattleFieldX = x;
		enterBattleFieldY = y;
	}

	public String getEnterBattleFieldMapName() {
		return enterBattleFieldMapName;
	}

	public int getEnterBattleFieldX() {
		return enterBattleFieldX;
	}

	public int getEnterBattleFieldY() {
		return enterBattleFieldY;
	}

	public transient BattleField battleField;

	/**
	 * 获得玩家所在的战场
	 * 
	 * @return
	 */
	public BattleField getBattleField() {
		return battleField;
	}

	/**
	 * 设置玩家所在的战场，在玩家进入战场前，必须设置这个变量 以及玩家在哪一方
	 * 
	 * @param bf
	 */
	public void setBattleField(BattleField bf) {
		this.battleField = bf;
	}

	/**
	 * 玩家进入战场
	 */
	public void enterBattleField() {
		this.setInBattleField(true);

		if (battleField != null) {
			sendPlayerAction(this, PlayerActionFlow.行为类型_战场, battleField.getName(), 1, new Date(), GamePlayerManager.isAllowActionStat());
			battleField.notifyPlayerEnter(this);
		}

	}

	/**
	 * 玩家离开战场
	 */
	public void leaveBattleField() {
		if (battleField != null) {
			battleField.notifyPlayerLeave(this);
		}
		battleField = null;
		this.setBattleFieldSide((byte) 0);
		this.setInBattleField(false);
	}

	/**
	 * 是否已经下线
	 * 
	 * @return
	 */
	public boolean isLeavedServer() {
		return leavedServer;
	}

	public void setLeavedServer(boolean leavedServer) {
		this.leavedServer = leavedServer;
	}

	/**
	 * 得到出师时间
	 * 
	 * @return
	 */
	public long getLeaveMasterTime() {
		return leaveMasterTime;
	}

	public void setLeaveMasterTime(long leaveMasterTime) {
		this.leaveMasterTime = leaveMasterTime;
	}

	/**
	 * 得到禁止收徒时间
	 * 
	 * @return
	 */
	public long getBanishPrenticeTime() {
		return banishPrenticeTime;
	}

	public void setBanishPrenticeTime(long banishPrenticeTime) {
		this.banishPrenticeTime = banishPrenticeTime;
	}

	/**
	 * 得到上次更新统计数据的时间
	 * 
	 * @return
	 */
	public long getLastUpdateStatDataTime() {
		return lastUpdateStatDataTime;
	}

	public void setLastUpdateStatDataTime(long lastUpdateStatDataTime) {
		this.lastUpdateStatDataTime = lastUpdateStatDataTime;
	}

	/**
	 * 玩家的一个行为，统计用
	 * 
	 * @param player
	 * @param actiontype
	 * @param actionname
	 * @param amount
	 * @param time
	 * @param isAllowActionStat
	 */
	public static void sendPlayerAction(Player player, String actiontype, String actionname, long amount, Date time, boolean isAllowActionStat) {
	}

	/**
	 * 得到师傅的积分
	 * 
	 * @return
	 */
	public long getMasterFunds() {
		return masterFunds;
	}

	public void setMasterFunds(long masterFunds) {
		this.masterFunds = masterFunds;
	}

	/**
	 * 自动加点
	 */
	public void autoAllocateSkillPoints() {
		try {
			if (unallocatedSkillPoint > 0) {
				int n = 0;
				while (this.unallocatedSkillPoint > 0) {
					Skill needAllocateSkill = null;
					int p0 = 0;
					int p1 = 0;
					int index = 0;
					int points = 0;
					byte levels[][] = new byte[2][];
					levels[0] = this.getCareerThreadLevels(0);
					levels[1] = this.getCareerThreadLevels(1);
					if (levels[0] != null) {
						for (int i = 0; i < levels[0].length; i++) {
							p0 += levels[0][i];
						}
					}
					if (levels[1] != null) {
						for (int i = 0; i < levels[1].length; i++) {
							p1 += levels[1][i];
						}
					}
					if (p0 > p1) {
						index = 0;
						points = p0;
					} else {
						index = 1;
						points = p1;
					}
					Career ca = CareerManager.getInstance().getCareer(this.getCareer());
					Skill[] skills = ca.getCareerThread(index).getSkills();
					ArrayList<Skill> availableSkill = new ArrayList<Skill>();
					for (Skill skill : skills) {
						if (skill.getNeedCareerThreadPoints() <= points && this.getSkillLevel(skill.getId()) < skill.getMaxLevel()) {
							availableSkill.add(skill);
						}
					}
					if (availableSkill.size() > 0) {
						// 优先加主动技能
						if (needAllocateSkill == null) {
							for (int i = availableSkill.size() - 1; i >= 0; i--) {
								Skill skill = availableSkill.get(i);
								if (skill.getSkillType() == Skill.SKILL_ACTIVE) {
									needAllocateSkill = skill;
									break;
								}
							}
						}
						// 优先加光环技能
						if (needAllocateSkill == null) {
							for (int i = availableSkill.size() - 1; i >= 0; i--) {
								Skill skill = availableSkill.get(i);
								if (skill.getSkillType() == Skill.SKILL_AURA) {
									needAllocateSkill = skill;
									break;
								}
							}
						}
						// 找出已经加过点，但不满级的技能，优先加这些技能
						if (needAllocateSkill == null) {
							for (int i = availableSkill.size() - 1; i >= 0; i--) {
								Skill skill = availableSkill.get(i);
								if (this.getSkillLevel(skill.getId()) > 0) {
									needAllocateSkill = skill;
									break;
								}
							}
						}
						if (needAllocateSkill == null) {
							needAllocateSkill = availableSkill.get(availableSkill.size() - 1);
						}
						int oldLevel = this.getSkillLevel(needAllocateSkill.getId());
						for (int i = 0; i < skills.length; i++) {
							if (skills[i].getId() == needAllocateSkill.getId()) {
								this.tryToLearnSkill(skills[i].getId(), true, false);
								break;
							}
						}
						if (Game.logger.isInfoEnabled()) {
							Game.logger.info("[自动加点] [成功] [技能：{}] [技能ID：{}] [变化：{}-->{}] [玩家：{}] [玩家ID：{}] [玩家等级：{}]", new Object[] { needAllocateSkill.getName(), needAllocateSkill.getId(), oldLevel, this.getSkillLevel(needAllocateSkill.getId()), this.getName(), this.getId(), this.getLevel() });
						}
					} else {
						if (Game.logger.isWarnEnabled()) Game.logger.warn("[自动加点] [失败] [找不到可加的技能] [玩家：{}] [玩家ID：{}]", new Object[] { this.getName(), this.getId() });
						break;
					}
					n++;
					if (n > 60) {
						break;
					}
				}
			} else {
				if (Game.logger.isWarnEnabled()) Game.logger.warn("[自动加点] [失败] [没有技能点] [玩家：{}] [玩家ID：{}]", new Object[] { this.getName(), this.getId() });
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[自动加点] [失败] [发生错误：{}] [玩家：{}] [玩家ID：{}]", new Object[] { e, this.getName(), this.getId() });
		}
	}

	/**
	 * 得到充值积分
	 * 
	 * @return
	 */
	public long getChargePoints() {
		return chargePoints;
	}

	public void setChargePoints(long chargePoints) {
		this.chargePoints = chargePoints;
		setDirty(true, "chargePoints");
		GamePlayerManager.createPlayerMsg(this, 1, false);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// 跨服相关的变量
	//
	// /////////////////////////////////////////////////////////////////////////////////////////////////

	protected transient String enterCrossServerMapName = "";
	protected transient int enterCrossServerX = 0;
	protected transient int enterCrossServerY = 0;

	/**
	 * 跨服后所在地图的地图名，进地图后的坐标
	 */
	protected transient String enterCrossGameMapName = "";
	protected transient int enterCrossGameX = 0;
	protected transient int enterCrossGameY = 0;

	protected transient long moneyWhileEnterCrossServer;
	protected transient String mapNameInCrossServer = "";

	public void setMapNameInCrossServer(String s) {
		mapNameInCrossServer = s;
	}

	protected transient int plantCount = 0;

	public int getPlantCount() {
		return plantCount;
	}

	public void setPlantCount(int plantCount) {
		this.plantCount = plantCount;
	}

	/**
	 * 得到玩家所在跨服的地图名
	 * 
	 * @return
	 */
	public String getMapNameInCrossServer() {
		return mapNameInCrossServer;
	}

	/**
	 * 设置在跨服的出生点
	 * 
	 * @param mapName
	 * @param x
	 * @param y
	 */
	public void setEnterCrossGamePoint(String mapName, int x, int y) {
		enterCrossGameMapName = mapName;
		enterCrossGameX = x;
		enterCrossGameY = y;
	}

	/**
	 * 得到进入跨服的地图名
	 * 
	 * @return
	 */
	public String getEnterCrossGameMapName() {
		return enterCrossGameMapName;
	}

	public void setEnterCrossGameMapName(String enterCrossGameMapName) {
		this.enterCrossGameMapName = enterCrossGameMapName;
	}

	public int getEnterCrossGameX() {
		return enterCrossGameX;
	}

	public void setEnterCrossGameX(int enterCrossGameX) {
		this.enterCrossGameX = enterCrossGameX;
	}

	public int getEnterCrossGameY() {
		return enterCrossGameY;
	}

	public void setEnterCrossGameY(int enterCrossGameY) {
		this.enterCrossGameY = enterCrossGameY;
	}

	public void setEnterCrossServerPoint(String mapName, int x, int y) {
		enterCrossServerMapName = mapName;
		enterCrossServerX = x;
		enterCrossServerY = y;
	}

	public String getEnterCrossServerMapName() {
		return enterCrossServerMapName;
	}

	public int getEnterCrossServerX() {
		return enterCrossServerX;
	}

	public int getEnterCrossServerY() {
		return enterCrossServerY;
	}

	// 与跨服服务器的连接
	protected transient Connection connForCrossServer;

	public void setConnForCrossServer(Connection c) {
		connForCrossServer = c;
	}

	public Connection getConnForCrossServer() {
		return connForCrossServer;
	}

	public void setMoneyWhileEnterCrossServer(long money) {
		moneyWhileEnterCrossServer = money;
	}

	public long getMoneyWhileEnterCrossServer() {
		return moneyWhileEnterCrossServer;
	}

	/**
	 * 判断一个玩家是否在跨服服务器，此判断只判断连接是否存在。 与CrossServerAgent中的方法不一致。
	 * 
	 * @return
	 */
	public boolean isInCrossServer() {
		return (connForCrossServer != null && connForCrossServer.getState() != Connection.CONN_STATE_CLOSE);
	}

	/**
	 * 判定这个角色是否处于锁定状态
	 * 
	 * @return
	 */
	public boolean isLocked() {
		Passport pp = this.getPassport();
		if (pp != null) {
			return pp.getStatus() == Passport.STATUS_PAUSED;
		} else {
			return false;
		}
	}

	public int getDuelFieldState() {
		return duelFieldState;
	}

	public void setDuelFieldState(int duelFieldState) {
		this.duelFieldState = duelFieldState;
	}

	/**
	 * 重置所有技能CD
	 */
	public void clearAllSkillCD(String reason) {
		ActiveSkillEntity[] ase = this.activeSkillAgent.getInCoolDownSkills();
		
		if (ase != null && ase.length > 0) {
			for (ActiveSkillEntity ae : ase) {
				SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), this.getId(), (short) ae.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_END, ae.getStartTime());
				this.addMessageToRightBag(req);
			}
		} else {
			// 如果服务器端没有冷却中的技能，则找出所有主动技能，强制客户端重置所有技能CD
			CareerManager cm = CareerManager.getInstance();
			Career career = cm.getCareer(this.getCareer());
			CareerThread[] cts = career.getCareerThreads();
			if (cts != null) {
				for (CareerThread ct : cts) {
					if (ct != null) {
						Skill[] skills = ct.getSkills();
						if (skills != null) {
							for (Skill sk : skills) {
								if (sk != null && sk.getSkillType() == Skill.SKILL_ACTIVE) {
									SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), this.getId(), (short) sk.getId(), (byte) ActiveSkillEntity.STATUS_END, com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
									this.addMessageToRightBag(req);
								}
							}
						}
					}

				}
			}

		}
		this.activeSkillAgent.clearCooldown();
	}

	private transient Fighter currentEnemyTarget = null;

	/**
	 * 得到当前敌对的目标，当被攻击时，当主动攻击某时都会切换target
	 * 
	 * @return
	 */
	public Fighter getCurrentEnemyTarget() {
		return currentEnemyTarget;
	}

	public void setCurrentEnemyTarget(Fighter currentEnemyTarget) {
		this.currentEnemyTarget = currentEnemyTarget;
	}

	public long getActivePetId() {
		return activePetId;
	}

	public void setActivePetId(long activePetId) {
		this.activePetId = activePetId;
		this.setDirty(true, "activePetId");
	}

	public BoothSale getBoothSale() {
		return boothSale;
	}

	public void setBoothSale(BoothSale boothSale) {
		this.boothSale = boothSale;
	}

	@Override
	public int getUnuseSoulLevel() {
		if (this.getUnusedSoul() != null && this.getUnusedSoul().size() > 0) {
			return this.getUnusedSoul().get(0).getGrade();
		}
		return this.getLevel();
	}

	/**
	 * 收回当前的宠物
	 * 
	 * @param bln
	 *            (主动(true)被动,只有玩家下线是(false 需要保存出战状态))
	 */
	public boolean packupPet(boolean bln) {
		Pet activePet = null;
		if (this.activePetId > 0) {
			activePet = PetManager.getInstance().getPet(activePetId);
		}
		canCalPetFlySkill = true;
		if (activePet != null) {
			if (activePet.getHp() <= 0) {
				activePet.setHp(activePet.getMaxHP() / 3);
			}
			// 检查计算被动技能和关闭光环技能
			CareerManager cm = CareerManager.getInstance();
			int skillIds[] = activePet.getSkillIds();
			for (int i = 0; i < skillIds.length; i++) {
				if (skillIds[i] == -1) {
					continue;
				}
				Skill skill = cm.getSkillById(skillIds[i]);
				if (skill == null) {
					// PetManager.logger.error("[收回宠物] [错误:宠物技能NULL] [petid:" +
					// activePet.getId() + "] [petname:" + activePet.getName() +
					// "] [player:" + this.getId() + "] [" +
					// this.getName() + "] [" + this.getUsername() + "] [skill:"
					// + skillIds[i] + "]");
					PetManager.logger.error("[收回宠物] [错误:宠物技能NULL] [petid:{}] [petname:{}] [player:{}] [{}] [{}] [skill:{}]", new Object[] { activePet.getId(), activePet.getName(), this.getId(), this.getName(), this.getUsername(), skillIds[i] });
				}
				if (skill instanceof AuraSkill) {
					if (activePet.getAuraSkillAgent() != null) {
						activePet.getAuraSkillAgent().openAuraSkill(null);
					}
				} else if (skill instanceof PassiveSkill) {
					PassiveSkill ps = (PassiveSkill) skill;
					ps.close(activePet, 1);
				}
			}

			activePet.initFlySkill(2, "收回宠物");
			activePet.initPetFlyAvata("收回宠物");
			activePet.setLastPackupTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			activePet.setMaster(null);
			activePet.setAlive(false);
			activePet.removeMoveTrace();
			if (bln) {
				// 主动
				// this.activePetId = -1;
				this.setActivePetId(-1);
				this.notifyAttributeAttackChange();
				ARTICLE_OPRATE_RESULT req = new ARTICLE_OPRATE_RESULT(GameMessageFactory.nextSequnceNum(), activePet.getId());
				this.addMessageToRightBag(req);
				if (this.getCurrentGame() != null) {
					this.getCurrentGame().removeSprite(activePet);
				} else {
					return false;
				}
				// activePet.setLastGame(null);
				// this.send_HINT_REQ("召回了" + activePet.getName());
				this.send_HINT_REQ(translateString(召回了xx, new String[][] { { STRING_1, activePet.getName() } }));
			} else {
				// 被动

				if (this.getCurrentGame() != null) {
					this.getCurrentGame().removeSprite(activePet);
				} else {
					return false;
				}
			}

			// PetManager.logger.info("[收回宠物] [" + activePet.getId() + "] [" +
			// activePet.getName() + "] [player:" + this.getId() + "] [" +
			// this.getName() + "] [" +
			// this.getUsername() + "]");
			// if (PetManager.logger.isDebugEnabled()) {
			// PetManager.logger.debug("[收回宠物] [收回] [{}] [{}] [player:{}] [{}] [{}] [被动(false)主动(true):{}]", new Object[] { activePet.getId(), activePet.getName(), this.getId(),
			// this.getName(), this.getUsername(), bln });
			// }
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("[收回宠物] [收回] [成功] [{}] {}", new Object[] { getLogString4Knap(), activePet.getLogOfInterest() });
			}
			return true;
		} else {
			// PetManager.logger.info("[收回宠物失败] [id为" + activePetId +
			// "的宠物为空] [player:" + this.getId() + "] [" + this.getName() + "] ["
			// + this.getUsername() + "]");
			PetManager.logger.error("[收回宠物失败] [id为{}的宠物为空] [player:{}] [{}] [{}]", new Object[] { activePetId, this.getId(), this.getName(), this.getUsername() });
			return false;
		}

		// if (!bln) {
		// this.noSummonPet = true;
		// }
	}

	private transient long lastNoticeSumPet = 0;
	public static long 出战飞升宠提示CD = 60 * 1000L;

	/**
	 * 召唤出一个宠物
	 * 
	 * @param pet
	 */
	public void summonPet(Pet pet, PetPropsEntity entity, String summonReason) {
		if (this.activePetId > 0) {
			packupPet(true);
			if (PetManager.logger.isInfoEnabled()) PetManager.logger.info("[召唤宠物] [" + (pet != null ? pet.getLogString() : "null") + "] [召回现有宠物] [" + activePetId + "]");
		}

		if (PetManager.openTestLog) {
			PetManager.logger.warn("[召唤宠物TEST] [" + summonReason + "] [角色:" + this.getName() + "] [角色id:" + this.getId() + "] [activePetId:" + activePetId + "] [" + pet.getLogString() + "]");
		}

		for(long pid : getPetCell()){
			if(pid == pet.getId()){
				sendError(Translate.不能召唤正在助战的宠物);
				return;
			}
		}
		
		if(PetHouseManager.getInstance().petIsStore(this, pet)){
			sendError(Translate.宠物正在挂机);
			return;
		}
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		Game game = this.getCurrentGame();
		if (game == null) {
			// PetManager.logger.warn("[召唤宠物] [失败：玩家目前不在游戏世界中！] [" + this.id +
			// "] [player:" + this.name + "] [" + this.username + "] [petId:" +
			// pet.getId() + "] [entityId:" +
			// entity.getId() + "]", start);
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[召唤宠物] [失败：玩家目前不在游戏世界中！] [{}] [player:{}] [{}] [petId:{}] [entityId:{}] [{}ms]", new Object[] { this.id, this.name, this.username, pet.getId(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return;
		}
//		if (DisasterManager.getInst().isPlayerInGame(this)) {
//			if (PetManager.logger.isDebugEnabled()) {
//				PetManager.logger.debug("[召唤宠物] [失败:玩家在金猴天灾中!] [" + this.getLogString() + "]");
//			}
//			return;
//		}
		if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(id)) {
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[召唤宠物] [失败：玩家正在渡劫] [{}] [player:{}] [{}] [petId:{}] [entityId:{}] [{}ms]", new Object[] { this.id, this.name, this.username, pet.getId(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return;
		}
		if (XianLingManager.instance.isPlayerInGame(this)) {
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[召唤宠物] [失败：玩家正在仙灵副本] [{}] [player:{}] [{}] [petId:{}] [entityId:{}] [{}ms]", new Object[] { this.id, this.name, this.username, pet.getId(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return;
		}
		FairyRobberyManager fins = FairyRobberyManager.inst;
		if (fins != null && fins.isPlayerInRobbery(this)) {
			// sendError(Translate.渡劫换装失败);
			return;
		}
		if (FairyChallengeManager.getInst().isPlayerChallenging(this)) {
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[召唤宠物] [失败：玩家正在渡劫] [{}] [player:{}] [{}] [petId:{}] [entityId:{}] [{}ms]", new Object[] { this.id, this.name, this.username, pet.getId(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return;
		}
		if (this.isUpOrDown && this.isFlying()) {
			if (PetManager.logger.isWarnEnabled()) {
				PetManager.logger.warn("[玩家骑飞行坐骑进地图不能召回宠物] [" + this.getLogString() + "] [petId:" + activePetId + "]");
			}
			return;
		}
		try {
			if (this.getLevel() <= 220) {
				PetFlyState stat = PetManager.getInstance().getPetFlyState(pet.getId(), "出战");
				if (stat != null && stat.getFlyState() == 1) {
					long now = System.currentTimeMillis();
					if (now >= (lastNoticeSumPet + 出战飞升宠提示CD)) {
						lastNoticeSumPet = now;
						this.sendError(Translate.未飞升不可出战飞升宠物);
					}
					if (PetManager.logger.isWarnEnabled()) {
						PetManager.logger.warn("[玩家没飞升不能出战飞升宠物] [" + this.getLogString() + "] [petId:" + pet.getId() + "]");
					}
					return;
				}
			}
		} catch (Exception e) {

		}

		// 看看是否是自己拥有的宠物
		ArticleEntity ee = getArticleEntity(entity.getId());
		if (ee == null) {
			// PetManager.logger.warn("[召唤宠物] [失败：玩家没有这个宠物物品！] [" + this.id +
			// "] [player:" + this.name + "] [" + this.username + "] [petId:" +
			// pet.getId() + "] [entityId:" +
			// entity.getId() + "]", start);
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[召唤宠物] [失败：玩家没有这个宠物物品！] [{}] [player:{}] [{}] [petId:{}] [entityId:{}] [{}ms]", new Object[] { this.id, this.name, this.username, pet.getId(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return;
		}

		// 看看宠物快乐值
		if (pet.getHappyness() < PetManager.疲劳快乐值) {
			// PetManager.logger.warn("[召唤宠物] [失败：快乐值过低！] [" + this.id + "] [" +
			// this.name + "] [player:" + this.username + "] [petId:" +
			// pet.getId() + "] [entityId:" +
			// entity.getId() + "]", start);
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[召唤宠物] [失败：快乐值过低！] [{}] [{}] [player:{}] [宠物名:{}] [petId:{}] [entityId:{}] [{}ms]", new Object[] { this.id, this.name, this.username, pet.getName(), pet.getId(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			this.send_HINT_REQ(Translate.text_pet_43);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_pet_43);
			this.addMessageToRightBag(hreq);
			return;
		}

		if (pet.getHookInfo() != null) {
			this.sendError(宠物正在挂机不能出战);
			return;
		}

		// 看看寿命值
		if (pet.getLifeTime() <= 0) {
			// PetManager.logger.warn("[召唤宠物] [失败：寿命为0！] [" + this.id + "] [" +
			// this.name + "] [player:" + this.username + "] [petId:" +
			// pet.getId() + "] [entityId:" +
			// entity.getId() + "]", start);
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[召唤宠物] [失败：寿命为0！] [{}] [{}] [player:{}] [宠物名:{}] [petId:{}] [entityId:{}] [{}ms]", new Object[] { this.id, this.name, this.username, pet.getName(), pet.getId(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			this.send_HINT_REQ(Translate.text_pet_44);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_pet_44);
			this.addMessageToRightBag(hreq);
			return;
		}

		// 看看携带等级
		if (pet.getTrainLevel() > this.getLevel()) {
			// PetManager.logger.warn("[召唤宠物] [失败：宠物携带等级大于您目前的等级！] [" + this.id
			// + "] [" + this.name + "] [player:" + this.username + "] [petId:"
			// + pet.getId() + "] [entityId:" +
			// entity.getId() + "]", start);
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[召唤宠物] [失败：宠物携带等级大于您目前的等级！] [宠物携带等级:{}] [玩家等级:{}] [{}] [{}] [player:{}] [宠物名:{}] [petId:{}] [entityId:{}] [{}ms]", new Object[] { pet.getTrainLevel(), this.getLevel(), this.id, this.name, this.username, pet.getName(), pet.getId(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			// this.send_HINT_REQ(Translate.text_pet_45);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_pet_45);
			this.addMessageToRightBag(hreq);
			return;
		}

		// 重新计算宠物出战状态时上次更新快乐值的时间
		if (pet.getLastPackupTime() > 0) {
			long time = pet.getLastPackupTime() - pet.getLastUpdateHappynessTime();
			if (time < 0) {
				time = 0;
			}
			pet.setLastUpdateHappynessTime(start - time);
		}

		// 宠物的速度和玩家速度一致
		pet.setSpeedA(this.getSpeed());
		pet.setMaster(this);

		pet.removeMoveTrace();
		pet.setX(x);
		pet.setY(y);
		if (PetManager.logger.isDebugEnabled()) PetManager.logger.debug("[召唤宠物] [x:" + x + "] [y:" + y + "] [path:" + pet.getMoveTrace() + "]");

		pet.setAlive(true);
		pet.setState((byte) 0);
		if (pet.getOwnerId() <= 0) {
			pet.setOwnerId(this.getId());
			PetManager.logger.error("[设置ownerId] [自己宠物] [" + this.getLogString() + "] [pet:" + pet.getLogString() + "] [以后:" + pet.getOwnerId() + "]");
		}
		;
		// if (!pet.isAlive()) {
		// pet.setAlive(true);
		// }

		// this.activePetId = pet.getId();
		setActivePetId(pet.getId());
		this.notifyAttributeAttackChange();

		// 检查计算被动技能和开启光环技能
		CareerManager cm = CareerManager.getInstance();
		int skillIds[] = pet.getSkillIds();
		for (int i = 0; i < skillIds.length; i++) {
			if (skillIds[i] == -1) {
				continue;
			}
			Skill skill = cm.getSkillById(skillIds[i]);
			if (skill == null) {
				// PetManager.logger.error("[召唤宠物] [错误:宠物技能NULL] [petid:" +
				// pet.getId() + "] [petname:" + pet.getName() + "] [player:" +
				// this.getId() + "] [" + this.getName() +
				// "] [" + this.getUsername() + "] [skill:" + skillIds[i] +
				// "]");
				PetManager.logger.error("[召唤宠物] [错误:宠物技能NULL] [petid:{}] [petname:{}] [player:{}] [{}] [{}] [skill:{}]", new Object[] { pet.getId(), pet.getName(), this.getId(), this.getName(), this.getUsername(), skillIds[i] });
			}
			if (skill instanceof AuraSkill) {
				if (pet.getAuraSkillAgent() == null) {
					pet.setAuraSkillAgent(new AuraSkillAgent(this));
				}
				pet.getAuraSkillAgent().openAuraSkill((AuraSkill) skill);
			} else if (skill instanceof PassiveSkill) {
				PassiveSkill ps = (PassiveSkill) skill;
				ps.run(pet, 1);
			}
		}

		// this.send_HINT_REQ("召出了" + pet.getName());
		this.send_HINT_REQ(translateString(召出了xx, new String[][] { { STRING_1, pet.getName() } }));

		game.addSprite(pet);

		Player player;
		String name = "";
		try {
			player = PlayerManager.getInstance().getPlayer(pet.getOwnerId());
			name = player.getName();
		} catch (Exception e) {
			PetManager.logger.error("[召唤宠物] [初始化主人的名字]", e);
		}
		pet.setOwnerName(name);
		if (canCalPetFlySkill) {
			canCalPetFlySkill = false;
			pet.initFlySkill(1, "出战");
			pet.initPetFlyAvata("出战");
		}

		// if (PetManager.logger.isDebugEnabled()) {
		// PetManager.logger.debug("[召唤宠物] [出战] [成功] [玩家id:{}] [角色:{}] [账号:{}] [宠物id:{}] [宠物名:{}] [宠物血量:{}]", new Object[] { this.getId(), this.getName(), this.getUsername(),
		// pet.getId(), pet.getName(), pet.getHp() });
		// }
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("[召唤宠物] [出战] [成功] [{}] {}", new Object[] { getLogString4Knap(), pet.getLogOfInterest() });
		}
		try {
			// EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.出战宠物最大妖魂值,
			// pet.getRepairNum()});
			// EventRouter.getInst().addEvent(evt2);
			EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.出战宠物最大携带等级, pet.getTrainLevel() });
			EventRouter.getInst().addEvent(evt3);
		} catch (Exception eex) {
			PlayerAimManager.logger.error("[目标系统] [统计宠物最大炼妖值异常][" + this.getLogString() + "]", eex);
		}
	}

	public String getLogString() {
		return "username:" + username + "] [id:" + id + "] [name:" + name + "] [level:" + level + "] [channel:" + (passport != null ? passport.getRegisterChannel() : "") + "]";
	}

	public String getLogString4Knap() {
		return "UserName:" + this.getUsername() + "] [UserID:" + this.getId() + "] [Name:" + this.getName() + "] [Level:" + this.getLevel() + "] [VIPLevel:" + this.getVipLevel();
	}

	public String getLogString4Knap(String temp) {
		return temp + "UserName:" + this.getUsername() + "] [" + temp + "UserID:" + this.getId() + "] [" + temp + "Name:" + this.getName() + "] [" + temp + "Level:" + this.getLevel() + "] [" + temp + "VIPLevel:" + this.getVipLevel();
	}

	public String getOtherSoulLogString() {
		// if(this.getUnusedSoul()!=null){
		// return "[元神等级:" + this.getUnusedSoul().get(0).getGrade() + "]";
		// }else{
		// return "[玩家还没开元神]";
		// }
		return "";
	}

	public String getJiazuLogString() {
		return "{username:" + username + "}{id:" + id + "}{name:" + name + "}{level:" + level + "}{channel:" + (passport != null ? passport.getRegisterChannel() : "") + "}{jiazuId:" + this.getJiazuId() + "}";
	}

	public String getPlayerPropsString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("[人物属性] [username:" + getName() + ",id:" + getId() + "] [当前元神类型:" + getCurrSoul().getSoulType() + "]");
		/** 基础属性 */
		sbf.append("[当前激活元神血:" + "当前血:" + getHp() + "最大血:" + getMaxHP() + "血A:" + getMaxHPA() + "血B:" + getMaxHPB() + "血C:" + getMaxHPC() + ",法:" + getMaxMP() + ",速度:" + getSpeed() + ",速度C：" + getSpeedC() + ",外功:" + getPhyAttack() + ",外功A:" + getPhyAttackA() + ",外功B:" + getPhyAttackB() + ",外功C:" + getPhyAttackC() + ",外防:" + getPhyDefence() + ",外防A:" + getPhyDefenceA() + ",外防B:" + getPhyDefenceB() + ",外防C:" + getPhyDefenceC() + ",法攻:" + getMagicAttack() + ",法攻A:" + getMagicAttackA() + ",法攻B:" + getMagicAttackB() + ",法攻C:" + getMagicAttackC() + ",法防:" + getMagicDefence() + ",破甲:" + getBreakDefence() + ",命中:" + getHit() + ",闪躲:" + getDodge() + ",额外闪躲率:" + getDodgeRateOther() + ",精准:" + getAccurate() + ",暴击:" + getCriticalHit() + ",免暴:" + getCriticalDefence() + ",雷攻:" + getThunderAttack() + ",雷抗:" + getThunderDefence() + ",雷减:" + getThunderIgnoreDefence() + ",冰攻:" + getBlizzardAttack() + ",冰抗:" + getBlizzardDefence() + ",冰减:" + getBlizzardIgnoreDefence() + ",风攻:" + getWindAttack() + ",风抗:" + getWindDefence() + ",风减:" + getWindIgnoreDefence() + ",火攻:" + getFireAttack() + ",火抗:" + getFireDefence() + ",火减:" + getFireIgnoreDefence() + "]");
		/** 元神加成 */
		sbf.append("[当前未元神加成血:" + getMaxHPX() + ",法:" + getMaxMPX() + ",外功:" + getPhyAttackX() + ",外防:" + getPhyDefenceX() + ",法攻:" + getMagicAttackX() + ",法防:" + getMagicDefenceX() + ",破甲:" + getBreakDefenceX() + ",命中:" + getHitX() + ",闪躲:" + getDodgeX() + ",精准:" + getAccurateX() + ",暴击:" + getCriticalHitX() + ",免暴:" + getCriticalDefenceX() + ",雷攻:" + getThunderAttackX() + ",雷抗:" + getThunderDefenceX() + ",雷减:" + getThunderIgnoreDefenceX() + ",冰攻:" + getBlizzardAttackX() + ",冰抗:" + getBlizzardDefenceX() + ",冰减:" + getBlizzardIgnoreDefenceX() + ",风攻:" + getWindAttackX() + ",风抗:" + getWindDefenceX() + ",风减:" + getWindIgnoreDefenceX() + ",火攻:" + getFireAttackX() + ",火抗:" + getFireDefenceX() + ",火减:" + getFireIgnoreDefenceX() + "]");
		/** 坐骑状态 */
		ArrayList<Long> horseIds = getHorseIdList();
		sbf.append("[坐骑");
		if (horseIds != null) {
			for (long id : horseIds) {
				Horse horse = HorseManager.getInstance().getHorseById(id, this);
				if (horse != null) {
					if (horse.isFight()) {
						sbf.append("阶数:" + horse.getRank() + ",状态:" + horse.getEnergy());
					}
				}
			}
		}
		sbf.append("]");
		/** buff加成 */
		Buff[] buffs = getActiveBuffs();
		sbf.append("[buff");
		if (buffs != null) {
			for (Buff buff : buffs) {
				if (buff != null) {
					sbf.append("{名字:" + (buff.getTemplate() != null ? buff.getTemplate().getName() : "") + ",等级:" + buff.getLevel() + "}");
				}
			}
		}
		sbf.append("]");
		return sbf.toString();
	}

	public String getPlayerEquipment() {
		StringBuffer sbf = new StringBuffer();
		try {
			/** 当前激活元神装备 */
			Soul currSoul = this.getCurrSoul();
			EquipmentColumn ec = currSoul.getEc();
			if (ec != null) {
				sbf.append("[当前激活元神(type:" + currSoul.getSoulType() + ")装备");
				for (int i = 0; i < ec.getEquipmentArrayByCopy().length; i++) {
					if (ec.getEquipmentArrayByCopy()[i] instanceof EquipmentEntity) {
						EquipmentEntity ee = (EquipmentEntity) ec.getEquipmentArrayByCopy()[i];
						if (ee != null && ee.getId() > 0) {
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(ee.getId());
							if (ae == null) { // 玩家身上装备无实体
								sbf.append("{不存在,id:" + ee.getId() + "}");
							} else {
								sbf.append("{" + ae.getArticleName() + "/" + ae.getId() + "/" + ae.getColorType() + "}");
								sbf.append(ee.getLogString());
							}
						}
					} else if (ec.getEquipmentArrayByCopy()[i] instanceof NewMagicWeaponEntity) {
						NewMagicWeaponEntity nmwe = (NewMagicWeaponEntity) ec.getEquipmentArrayByCopy()[i];
						if (nmwe != null && nmwe.getId() > 0) {
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(nmwe.getId());
							if (ae == null) { // 玩家身上法宝无实体
								sbf.append("{不存在,id:" + nmwe.getId() + "}");
							} else {
								sbf.append("{" + ae.getArticleName() + "/" + ae.getId() + "/" + ae.getColorType() + "}");
							}
						}
					}
				}
				sbf.append("] [over]");
			}
			/** 当前未激活元神装备 */
			List<Soul> unusedSoul = this.getUnusedSoul();
			if (unusedSoul != null) {
				for (Soul s : unusedSoul) {
					EquipmentColumn ec2 = s.getEc();
					if (ec2 != null) {
						sbf.append("[未激活元神(type:" + s.getSoulType() + ")装备");
						for (int i = 0; i < ec2.getEquipmentArrayByCopy().length; i++) {
							if (ec2.getEquipmentArrayByCopy()[i] instanceof EquipmentEntity) {
								EquipmentEntity ee = (EquipmentEntity) ec2.getEquipmentArrayByCopy()[i];
								if (ee != null && ee.getId() > 0) {
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(ee.getId());
									if (ae == null) { // 玩家身上装备无实体
										sbf.append("{不存在,id:" + ee.getId() + "}");
									} else {
										sbf.append("{" + ae.getArticleName() + "/" + ae.getId() + "/" + ae.getColorType() + "}");
										sbf.append(ee.getLogString());
									}
								}
							} else if (ec2.getEquipmentArrayByCopy()[i] instanceof NewMagicWeaponEntity) {
								NewMagicWeaponEntity nmwe = (NewMagicWeaponEntity) ec2.getEquipmentArrayByCopy()[i];
								if (nmwe != null && nmwe.getId() > 0) {
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(nmwe.getId());
									if (ae == null) { // 玩家身上法宝无实体
										sbf.append("{不存在,id:" + nmwe.getId() + "}");
									} else {
										sbf.append("{" + ae.getArticleName() + "/" + ae.getId() + "/" + ae.getColorType() + "}");
									}
								}
							}
						}
						sbf.append("] [over]");
					}
				}
			}
		} catch (Exception e) {
			ArticleManager.logger.error("获取玩家身上装备异常");
			e.printStackTrace();
		}
		return sbf.toString();
	}

	public String getTeamLogString() {
		StringBuffer sbf = new StringBuffer();
		try {
			sbf.append(getLogString());
			Team team = getTeam();
			if (team == null) {
				sbf.append(" [team = null] [teamMark = " + getTeamMark() + "]");
			} else {
				sbf.append("[team != null] [teamMark = " + getTeamMark() + "]");
				Player[] members = team.getMembers().toArray(new Player[0]);
				if (members == null) {
					sbf.append(" [members:null] ");
				} else {
					sbf.append("[members:");
					for (Player p : members) {
						sbf.append(p.getName()).append("/").append(p.getId()).append(",");
					}
					sbf.append("]");
					sbf.append(" [teamSize:" + members.length + "]");
				}
			}
		} catch (Exception e) {
		}
		return sbf.toString();
	}

	/**
	 * 离开家族的时间
	 */
	long leaveJiazuTime;

	public long getLeaveJiazuTime() {
		return leaveJiazuTime;
	}

	public void setLeaveJiazuTime(long leaveJiazuTime) {
		this.leaveJiazuTime = leaveJiazuTime;
		setDirty(true, "leaveJiazuTime");
	}

	String requestJiazuName;

	private String realMapName = "";

	public String getRealMapName() {
		return realMapName;
	}

	public void setRealMapName(String realMapName) {
		this.realMapName = realMapName;
		this.setDirty(true, "realMapName");
	}

	/**
	 * 临时审请加入家族的名称
	 * 
	 * @param applyName
	 */
	public void setRequestJiazuName(String requestJiazuName) {
		this.requestJiazuName = requestJiazuName;
		setDirty(true, "requestJiazuName");
	}

	public String getRequestJiazuName() {
		return this.requestJiazuName;
	}

	public void checkNewMail() {
		// TODO: 检查是否有新邮件
		MailManager mm = MailManager.getInstance();
		int count = mm.hasNewMail(this);
		boolean has = false;
		if (count > 0) {
			has = true;
		}
		if (has != this.isMailMark()) {
			this.setMailMark(has);
		}
		this.setNewMailCount(count);
	}

	public void addNewMailCount() {
		this.setNewMailCount(this.getNewMailCount() + 1);
		this.setMailMark(true);
	}

	public void removeNewMailCount() {
		if (this.getNewMailCount() > 0) {
			this.setNewMailCount(this.getNewMailCount() - 1);
		} else {
			this.setNewMailCount(0);
			this.setMailMark(false);
		}
	}

	// ////////////////////////////////// 一些需要重写的方法
	// /////////////////////////////////////////
	@Override
	public void setHomeMapName(String value) {

		super.setHomeMapName(value);
		setDirty(true, "homeMapName");
	}

	@Override
	public void setHomeX(int value) {

		super.setHomeX(value);
		setDirty(true, "homeX");
	}

	@Override
	public void setHomeY(int value) {

		super.setHomeY(value);
		setDirty(true, "homeY");
	}

	transient long lastrecordQuiteGameTime;

	@Override
	public void setQuitGameTime(long value) {
		super.setQuitGameTime(value);
		setDirty(true, "quitGameTime");
	}

	public boolean isYunbiao() {
		return yunbiao;
	}

	public void setYunbiao(boolean yunbiao) {
		this.yunbiao = yunbiao;
	}

	public BiaoCheNpc 得到玩家镖车() {
		NPC npc = this.getFollowableNPC();
		if (npc instanceof BiaoCheNpc) {
			return (BiaoCheNpc) npc;
		}
		return null;
	}

	/**
	 * 玩家是否在摆摊状态
	 * 
	 * @param player
	 * @return
	 */
	public boolean isInBoothSale() {
		return getTradeState() == TradeManager.TRADE_STATE_BOOOTH;
	}

	/**
	 * 玩家是否在交易状态
	 * 
	 * @param player
	 * @return
	 */
	public boolean isInTrade() {
		return getTradeState() == TradeManager.TRADE_STATE_TRADE;
	}

	// 任务
	/** 已接任务 <TaskId,实体>[包括完成的日常等] */

	// @SimpleColumn(length = 100000,
	// saveInterval = 600)
	private transient List<TaskEntity> allTask = new ArrayList<TaskEntity>();

	// /** 已接受任务<TaskTarget,实体>[进行中的任务] */
	// public transient HashMap<TargetType, ArrayList<TaskEntity>> acceptTasks =
	// new HashMap<TargetType, ArrayList<TaskEntity>>();

	/** 可接任务列表 */
	private List<Long> nextCanAcceptTasks = Collections.synchronizedList(new ArrayList<Long>());

	public boolean hasSameCollectionTask(String taskCollectionName) {
		List<Task> collectionTasks = TaskManager.getInstance().getTaskCollectionsByName(taskCollectionName);
		if (collectionTasks != null && collectionTasks.size() > 0) {
			for (Task _collectionTask : collectionTasks) {
				int taskState = getTaskStatus(_collectionTask);
				if (taskState != TASK_STATUS_NEVER && taskState != TASK_STATUS_DEILVER) {
					return true;
				}
			}
		}
		return false;
	}

	public void initTasks() {
		try {
			allTask = TaskEntityManager.getInstance().getPlayerTaskEntities(this);
			TaskManager tm = TaskManager.getInstance();
			// 处理不存在的任务
			List<TaskEntity> wrongEntity = new ArrayList<TaskEntity>();
			// 处理目标不同的任务
			List<TaskEntity> changeEntity = new ArrayList<TaskEntity>();
			for (TaskEntity entity : allTask) {
				Task task = tm.getTask(entity.getTaskId());
				if (task == null) {
					wrongEntity.add(entity);
				} else if (entity.isChanged(task)) {
					changeEntity.add(entity);
				}
				if(!TaskManager.countryFit(this, task)){
					wrongEntity.add(entity);
				}
				entity.setTask(task);
				entity.setOwner(this);
				entity.setTaskName(task.getName());
				entity.setTaskGrade(task.getGrade());
			}
			for (TaskEntity entity : wrongEntity) {
				allTask.remove(wrongEntity);
				TaskSubSystem.logger.error("[加载玩家任务发现有不存在的任务] [已接列表] [player:{}] [taskId:{}]", new Object[] { getName(), entity.getTaskId() });
			}
			for (TaskEntity entity : changeEntity) {
				entity.reSet();
				TaskSubSystem.logger.error("[加载玩家任务发现修改过的任务] [已接列表] [player:{}] [taskId:{}]", new Object[] { getName(), entity.getTaskId() });
			}
			List<Long> removes = new ArrayList<Long>();

			for (Long id : getNextCanAcceptTasks()) {
				Task t = TaskManager.getInstance().getTask(id);
				if (t == null) {// || !takeOneTask(t, false,
					// null).getBooleanValue()) {
					removes.add(id);
					continue;
				}
				if(t.getCountryLimit() != getCountry()){
					removes.add(id);
				}
			}
			for (Long errId : removes) {
				//getNextCanAcceptTasks().remove(errId);
				//setDirty(true, "nextCanAcceptTasks");
				TaskSubSystem.logger.error("[加载玩家任务发现有不存在的任务] [可接任务列表] [player:{}] [taskId:{}]", new Object[] { getName(), errId });
			}

			Iterator<TaskEntity> itor = allTask.iterator();
			while (itor.hasNext()) {
				TaskEntity te = itor.next();
				if (te != null) {
					if (te.getTask().getType() == TaskConfig.TASK_TYPE_ONCE) {
						if (te.getStatus() == TaskConfig.TASK_STATUS_DEILVER) {
							itor.remove();
							TaskSubSystem.logger.error("[加载玩家任务发现有完成的单次任务] [已接列表] [直接remove] [player:{}] [taskId:{}]", new Object[] { getName(), te.getTaskId() });
						}
					}
				} else {
					itor.remove();
					TaskSubSystem.logger.error("[加载玩家任务发现有空的实体] [已接列表] [直接remove] [player:{}] ", new Object[] { getName() });
				}
			}

		} catch (Exception e) {
			TaskSubSystem.logger.error("加载角色任务出错[" + getName() + "]", e);
		}
	}

	/**
	 * 自动交付任务的容错处理
	 */
	public void tolerance4AutoDeliverTask() {
		for (TaskEntity entity : allTask) {
			if (entity != null) {
				if (entity.getStatus() == TaskConfig.TASK_STATUS_COMPLETE) {
					if (entity.getTask() != null) {
						if (entity.getTask().isAutoDeliver()) {
							// 自动完成任务的,进入游戏的时候做容错,通知客户端交付任务
							NOTICE_CLIENT_DELIVER_TASK_REQ client_DELIVER_TASK_REQ = new NOTICE_CLIENT_DELIVER_TASK_REQ(GameMessageFactory.nextSequnceNum(), entity.getTask().getId());
							this.addMessageToRightBag(client_DELIVER_TASK_REQ);
							if (TaskSubSystem.logger.isWarnEnabled()) {
								TaskSubSystem.logger.warn(getLogString() + "[上线检测] [有自动完成任务未交付] [帮助玩家自动交付] [任务名字:{}] [任务ID:{}]", new Object[] { entity.getTask().getName(), entity.getTask().getId() });
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 检测可接任务列表
	 */
	public void checkNextList() {
		try {
			List<AavilableTaskInfo> addList = new ArrayList<AavilableTaskInfo>();// 本次增加的任务
			// 需要发送给客户端
			for (Task t : TaskManager.getInstance().getFristTasksOfAlLine()) {
				if (!getNextCanAcceptTasks().contains(t.getId())) {
					CompoundReturn cr = takeOneTask(t, false, null);
					if (cr.getIntValue() == 0 || (t.getShowType() == TASK_SHOW_TYPE_MAIN && (canAddToNextlist(cr)))) {
						getNextCanAcceptTasks().add(t.getId());
						addList.add(new AavilableTaskInfo(t));
						setDirty(true, "nextCanAcceptTasks");
					}
				}
			}
			// 发送给客户端变化
			if (addList.size() > 0) {
				noticeClientCanAcceptModify((byte) 0, addList,"checkNextList");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("==检查可接任务列表===异常：");
			TaskSubSystem.logger.error(name + " [检查可接任务列表]", e);
		}
	}

	public List<TaskEntity> getAllTask() {
		return allTask;
	}

	public void setAllTask(List<TaskEntity> allTask) {
		this.allTask = allTask;
	}

	/**
	 * 得到任务实体
	 * 
	 * @param taskId
	 * @return
	 */
	public TaskEntity getTaskEntity(long taskId) {
		for (TaskEntity e : getAllTask()) {
			if (e.getTaskId() == taskId) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 是否在完成列表
	 * 
	 * @param taskId
	 * @return
	 */
	public boolean inDeliver(Task task) {
		// return DeliverTaskManager.getInstance().isDelived(this, task);
		boolean result = true;
		try {
			NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
			if (ndtm != null && ndtm.isNewDeliverTaskAct) {
				result = ndtm.isDelived(this, task);
			} else {
				result = DeliverTaskManager.getInstance().isDelived(this, task);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			DeliverTaskManager.logger.error("[判断任务是否完成] [异常] [" + this.getLogString() + "] [taskId :" + task.getId() + "]", e);
		}
		return result;
	}

	/**
	 * 得到任务的状态<BR/>
	 * 没接取的返回-1<BR/>
	 * 
	 * @param task
	 * @return
	 */
	public int getTaskStatus(Task task) {
		// TODO 通过不同的任务类型去不同的列表中检索 没有则返回-1
		TaskEntity taskEntity = getTaskEntity(task.getId());
		if (taskEntity == null) {// 不在已接列表
			if (task.getType() == TASK_TYPE_ONCE && inDeliver(task)) { // 在完成列表[单次任务]
				return TASK_STATUS_DEILVER;
			} else {
				return TASK_STATUS_NEVER;
			}
		}
		return taskEntity.getStatus();
	}

	/**
	 * 得到当前任务状态(日常和最后一次完成时间相关)
	 * 
	 * @param task
	 * @return
	 */
	public int getCurrTaskStatus(Task task) {
		int status = getTaskStatus(task);
		if (status == TASK_STATUS_NEVER) {
			return status;
		}
		if (task.getType() != TASK_TYPE_ONCE) {// 非单次任务
			TaskEntity entity = getTaskEntity(task.getId());
			if (entity == null) {
				return TASK_STATUS_NEVER;
			}
			// 本周期完成过
			if (entity.getCycleDeilverInfo().getBooleanValue()) {
				return status;
			} else {
				return TASK_STATUS_NONE;
			}
		}
		return status;
	}

	/**
	 * 任务的依赖任务是否都已达成
	 * 
	 * @param task
	 * @return
	 */
	private boolean dependHasOver(Task task) {
		if (task.getFrontGroupName() == null || "".equals(task.getFrontGroupName().trim())) {// 无前置任务
			return true;
		}

		List<Task> depandGroup = TaskManager.getInstance().getTaskGroupByGroupName(task.getFrontGroupName());
		if (depandGroup == null || depandGroup.size() == 0) {
			return true;
		}

		boolean isDependOne = task.getDependType() == DEPEND_TYPE_ONE;// 是否是依赖单一的任务
		boolean isOver = false;
		for (int i = 0; i < depandGroup.size(); i++) {
			Task dependTask = depandGroup.get(i);
			int status = getTaskStatus(dependTask);
			// 处理多次任务的任务状态
			if (status != TASK_STATUS_NEVER && dependTask.getType() != TASK_TYPE_ONCE) { // 接了任务且不是单次任务
				TaskEntity te = getTaskEntity(dependTask.getId());
				CompoundReturn infos = te.getCycleDeilverInfo();// 这里出过问题,这个方法的返回是是不是还能再做,和还能再做几次
				if (infos.getBooleanValue() && infos.getIntValue() == te.getTask().getDailyTaskMaxNum()) {// 本周期没有完成过
					// 还能再做,并且还能做任务限制的全部次数,则说明还没做过,
					status = TASK_STATUS_NONE;
				}
			}
			if (isDependOne) {// 依赖一个任务
				if (status == TASK_STATUS_DEILVER) {// 有一个完成了
					isOver = true;
					break;
				} else {
					continue;
				}
			} else {// 依赖全部任务
				if (status != TASK_STATUS_DEILVER) {// 有一个没完成
					isOver = false;
					break;
				} else {
					isOver = true;
					continue;
				}
			}
		}
		return isOver;
	}

	/**
	 * 处理某一类型的任务
	 * 
	 * @param taskAction
	 * @param showTaskMap
	 */
	public synchronized void dealWithTaskAction(TaskAction... taskActions) {
		try {
			for (TaskAction taskAction : taskActions) {
				List<TaskEntity> taskList = getTasksByTargetType(taskAction.getTargetType());
				if (taskList != null && taskList.size() > 0) {// 确实有这类任务
					for (int i = 0, j = taskList.size(); i < j; i++) {
						TaskEntity te = taskList.get(i);
						if (te.getStatus() == TASK_STATUS_GIVEUP || te.getStatus() == TASK_STATUS_FAILED) {
							continue;
						}
						Task task = te.getTask();
						// 检测额外目标
						TaskManager.checkExcessTarget(te, taskAction);

						if (te.taskComplete()) {// 目标已经全部达成
							if (TaskSubSystem.logger.isDebugEnabled()) {
								TaskSubSystem.logger.debug(this.getLogString() + "[任务:{}] [处理任务目标类型][{}] [全部达成了]", new Object[] { task.getName(), taskAction.getTargetType().getName() });
							}
							continue;
						}
						for (int m = 0, n = task.getTargets().length; m < n; m++) {
							if (te.isComplete(m)) {// 目标完成了
								continue;
							}
							CompoundReturn returns = task.getTargets()[m].dealAction(taskAction);
							if (returns.getBooleanValue()) {
								// 需要计算积分
								if (task.needCountScore()) {
									int score = taskAction.getScore();
									if (score > 0) {
										te.setScore(te.getScore() + score);
									}
								}
								te.modifyPercentageCompleted(m, returns.getIntValue());
								continue;// 一个任务对于一个任务目标至多存在一个
							}
						}
					}
				} else {
				}
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error(getLogString() + "[处理任务任务目标异常", e);
		}
	}

	/**
	 * 玩家杀死了一只怪,检测有没有以道具为目标的任务且指定怪物掉落 如果有，返回所有actions
	 * 
	 * @param sprite
	 * @return
	 */
	public List<TaskAction> getMonsterArticleAction(Sprite sprite) {
		List<TaskAction> list = new ArrayList<TaskAction>();
		List<TaskEntity> entitys = getTasksByTargetType(TargetType.GET_ARTICLE);// 得到所有以物品为目标的任务
		if (entitys != null && entitys.size() > 0) {
			for (int i = 0, j = entitys.size(); i < j; i++) {
				Task task = entitys.get(i).getTask();
				for (int a = 0, b = task.getTargets().length; a < b; a++) {
					// 目标是物品
					if (task.getTargets()[a].getTargetType() == TargetType.GET_ARTICLE) {// 找到物品target
						TaskTargetOfGetArticle getArticle = (TaskTargetOfGetArticle) task.getTargets()[a];
						for (int m = 0, n = getArticle.getMonsterNames().length; m < n; m++) {
							if (getArticle.getMonsterNames()[m] != null) {
								for (int x = 0, y = getArticle.getMonsterNames()[m].length; x < y; x++) {
									// 是指定的怪 。开始计算概率
									if (sprite.getName().equals(getArticle.getMonsterNames()[m][x])) {
										if (TaskManager.RANDOM.nextDouble() < getArticle.getDrops()[m][x]) {
											TaskAction action = TaskActionOfGetArticle.createAction(getArticle.getTargetName()[m], getArticle.getTargetColor());
											list.add(action);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	// 记录封魔录和任务的对应，为了接取任务的时候，增加封魔录的使用次数
	// 不用清理
	private transient HashMap<Long, PropsEntity> task2TaskPropMap = new HashMap<Long, PropsEntity>();

	public void notifyTaskPropsTrigerTask(PropsEntity tp, Task task) {
		task2TaskPropMap.put(task.getId(), tp);
	}

	/**
	 * 玩家接受任务 0.可接取<BR/>
	 * 1.任务列表满<BR/>
	 * 2.性别不符<BR/>
	 * 3.国家不符<BR/>
	 * 4.职业不符 <BR/>
	 * 5.体力值不足 不能发现任务<BR/>
	 * 6.国家职务不符<BR/>
	 * 7.家族限制<BR/>
	 * 8.家族职务限制<BR/>
	 * 9.背包空位置不足<BR/>
	 * 10.时间不符<BR/>
	 * 11.跑环任务放弃后CD没到<BR/>
	 * 12.单次任务不能重复接<BR/>
	 * 13.已经接取了非单次任务<BR/>
	 * 14.日常任务周期内都完成了<BR/>
	 * 15.前置任务没完成<BR/>
	 * 16.等级不足<BR/>
	 * 17.体力值不足不能接取任务<BR/>
	 * 18.你已经接取了同类型的任务[同组任务只能接一个]<BR/>
	 * 19.封印状态不满足<BR/>
	 * 20.境界不足不能接取任务<BR/>
	 * 21.你接取了同类任务不能再接取了<BR/>
	 * 22.护送任务只能做一个<BR/>
	 * 23.接取的任务过多<BR/>
	 * 24.元神等级不足<BR/>
	 * 
	 * @param task要接的任务
	 * @param realTake确实要接还是测试是否能接
	 * @return
	 */
	public CompoundReturn takeOneTask(Task task, boolean realTake, FunctionNPC npc) {
		try {
			// 如果是道具触发的任务
			PropsEntity pe = task2TaskPropMap.get(task.getId());
			if (pe != null) {
				ArticleManager am = ArticleManager.getInstance();
				Article a = am.getArticle(pe.getArticleName());
				if (a instanceof Props) {
					PropsUseRecord r = propsUseRecordMap.get(a.get物品二级分类());
					if (r != null) {
						Props ps = (Props) a;
						if (ps.isUsingTimesLimit()) {
							int vipAdd = VipManager.getInstance().vip每日增加的道具使用次数(this, a.get物品二级分类());
//							int exchangeAdd = ActivityManager.getInstance().getDayExchangeAddNum(this, a.get物品二级分类());
//							int specialDateAdd = ActivityManager.getInstance().specialActivityAddNum(this);
//							int activityAdd = TimesActivityManager.instacen.getAddNum(this, TimesActivityManager.TUMOTIE_ACTIVITY);
							int totalNum = ps.getMaxUsingTimes() + vipAdd ;//+ exchangeAdd + specialDateAdd + activityAdd;
							if (r.canUseProps(totalNum) == false) {
								return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(14);
							}
						}
					}
				}
			}
			if (task.getBigCollection() != null && !"".equals(task.getBigCollection())) {
				List<Task> bigCollection = TaskManager.getInstance().getTaskBigCollections().get(task.getBigCollection());
				if (bigCollection != null && bigCollection.size() > 0) {
					int maxNum = task.getDailyTaskMaxNum();// 周期内上限
					int groupTaskCycleDoneNum = 0;// 周期内实际完成次数
					boolean hasSameBigcollectionTask = false;// 是否已经接取了同组任务
					for (Task _groupTask : bigCollection) {
						if (hasSameBigcollectionTask) {
							if (npc != null) {
								// 并且在同一地图的
								if (this.getCurrentGame().gi.displayName.equals(_groupTask.getStartMap())) {
									npc.addTask2Wait(ModifyType.GET_COLLECTION, _groupTask);
								}
							}
							continue;
						}
						TaskEntity tte = getTaskEntity(_groupTask.getId());
						if (tte != null && _groupTask.getType() == TASK_TYPE_DAILY) {
							CompoundReturn info = tte.getCycleDeilverInfo();
							int doneNum = maxNum - info.getIntValue();
							groupTaskCycleDoneNum += doneNum;
							if (groupTaskCycleDoneNum >= maxNum) {
								return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(14);
							}
						}
						if (!_groupTask.getName().equals(task.getName())) {
							int taskState = getTaskStatus(_groupTask);
							if (taskState != TASK_STATUS_NEVER && taskState != TASK_STATUS_DEILVER) {
								if (npc != null) {
									if (this.getCurrentGame().gi.displayName.equals(_groupTask.getStartMap())) {
										npc.addTask2Wait(ModifyType.GET_COLLECTION, _groupTask);
									}
								}
								hasSameBigcollectionTask = true;
							}
						}
					}
					if (hasSameBigcollectionTask) {
						return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(18);
					}
				}
			}
			// 同任务组集合只能接受一个
			List<Task> collectionTasks = TaskManager.getInstance().getTaskCollectionsByName(task.getCollections());
			if (collectionTasks != null && collectionTasks.size() > 0) {
				for (Task _collectionTask : collectionTasks) {
					if (_collectionTask == null) {
						TaskSubSystem.logger.error("[有任务为空] [任务集合:{}]", new Object[] { task.getCollections() });
						continue;
					}
					if (!_collectionTask.getName().equals(task.getName())) {
						int taskState = getTaskStatus(_collectionTask);
						if (taskState != TASK_STATUS_NEVER && taskState != TASK_STATUS_DEILVER) {
							if (npc != null) {
								if (this.getCurrentGame().gi.displayName.equals(_collectionTask.getStartMap())) {
									npc.addTask2Wait(ModifyType.GET_COLLECTION, task);
								}
							}
							return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(21);
						}
					}
				}
			}
			CompoundReturn returns = getBaseLimit(task, realTake, npc);
			if (!returns.getBooleanValue()) {
				return returns;
			}

			// 接取时间判断
			TimeLimit t = task.getTimeLimit();
			if (t != null && !t.timeAllow()) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(10);
			}
			int taskStatus = getTaskStatus(task);

			// 判断放弃时间
			if (taskStatus != TASK_STATUS_NEVER && task.getType() == TASK_TYPE_LOOP) {// 有这个任务
				// 且是跑环任务,则检测上次放弃时间
				TaskEntity entity = getTaskEntity(task.getId());
				if (entity != null) {
					if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() < entity.getLastGiveUpTime() + LOOPTASK_GIVEUP_PUNISH_TIME) {
						return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(11);
					}
				}
			}
			// 单次任务
			if (task.getType() == TASK_TYPE_ONCE) {
				if (taskStatus != TASK_STATUS_NEVER) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(12);
				}
			} else {
				// 多次任务
				if (taskStatus != TASK_STATUS_NEVER) { // 至少已经接取过
					try {
						if (taskStatus != TASK_STATUS_DEILVER) {// 在任务列表未完成的
							return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(13);
						} else {
						}
					} catch (Exception e) {
						TaskSubSystem.logger.error("[检测多次任务可接] [异常]", e);
					}
				}
			}
			// 如果是护送任务,则需要判断当前角色没有护送任何NPC;
			if (getFollowableNPC() != null) {
				List<TaskTarget> tlist = task.getTargetByType(TargetType.CONVOY);
				if (tlist != null && !tlist.isEmpty()) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(22);
				}
			}

			// 判断前置
			if (!dependHasOver(task)) { // 前置没完成
				if (npc != null) {
					npc.addTask2Wait(ModifyType.TASK_DELIVER, task);
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(15);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("接任务异常:"+e);
			TaskSubSystem.logger.error(id+"/"+name + "[接取任务:{}异常]", new Object[] { task.getName() }, e);
			return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1);
		}

		return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(0);
	}
	
	/**
	 * 周期完成信息 [0]最大次数 [1]已完成次数
	 * 
	 * @param taskList
	 * @return
	 */
	public int[] getCycleDeliverInfo(List<Task> taskList) {
		int[] returns = new int[2];
		int groupTaskCycleDoneNum = 0;// 周期内实际完成次数
		if (taskList != null && taskList.size() > 0) {
			int maxNum = taskList.get(0).getDailyTaskMaxNum();// 周期内上限
			returns[0] = maxNum;
			for (Task task : taskList) {
				TaskEntity tte = getTaskEntity(task.getId());
				if (tte != null && task.getType() == TASK_TYPE_DAILY) {
					CompoundReturn info = tte.getCycleDeilverInfo();
					int doneNum = maxNum - info.getIntValue();
					groupTaskCycleDoneNum += doneNum;
				}
			}
		}
		returns[1] = groupTaskCycleDoneNum;
		return returns;
	}

	/**
	 * 查询某组任务当天已完成次数
	 * 
	 * @param taskGroupName
	 * @return
	 */
	public int getTodayDoneTimes(String taskGroupName) {
		List<Task> groupTask = TaskManager.getInstance().getTaskGroupByGroupName(taskGroupName);
		if (groupTask == null || groupTask.isEmpty()) {
			throw new IllegalStateException();
		}
		int totalNum = 0;
		for (Task task : groupTask) {
			int maxNum = task.getDailyTaskMaxNum();
			TaskEntity te = getTaskEntity(task.getId());
			if (te != null) {
				CompoundReturn info = te.getCycleDeilverInfo();
				if (info.getBooleanValue()) {
					int doneNum = (maxNum - info.getIntValue());
					totalNum += doneNum;
				}
			}
		}
		return totalNum;
	}

	/**
	 * 基本信息判断
	 * 
	 * @param task
	 * @return
	 */
	public CompoundReturn getBaseLimit(Task task, boolean realTake, FunctionNPC npc) {
		// 基本判断
		// 任务列表满了 不做限制
		// if (realTake && getAcceptTasks().size() >= MAX_TASK_NUM) {
		// return
		// CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		// }
		// 等级判断
		if (!task.gradeFit(this)) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.GRADE_UP, task);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(16);
		}
		// 性别
		if (!TaskManager.sexFit(this, task)) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		// 国家
		if (!TaskManager.countryFit(this, task)) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		// 职业
		if (!TaskManager.workFit(this, task)) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}
		// 体力值限制
		if (task.getThewLimit() > this.getVitality()) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.THEW_UP, task);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
		}
		// 体力值不足不能接取任务
		if (task.getThewCost() > this.getVitality()) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.THEW_UP, task);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(16);
		}
		// 国家职务限制
		if (!TaskManager.CountryOfficialFit(this, task)) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.COUNTRY_OFFICIAL_UP, task);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
		}
		// 家族限制
		if (!TaskManager.septFit(this, task)) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.JIAZU_GOT, task);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
		}
		// 家族职务限制
		if (!TaskManager.septOfficialFit(this, task, npc)) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(8);
		}
		// 境界
		if (!TaskManager.bournFit(this, task)) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.BOURN_UP, task);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(20);
		}
		// 封印
		if (!TaskManager.sealFit(this, task)) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.GRADE_UP, task);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(19);
		}
		// 时间
		// if(!task.check4date()) {
		// if (npc != null) {
		// npc.addTask2Wait(ModifyType.DAY_CHANGE, task);
		// }
		// return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(10);
		// }
		// 境界等级
		if (!TaskManager.soulLevelFit(this, task)) {
			npc.addTask2Wait(ModifyType.GRADE_UP, task);
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(24);
		}
		// 最后一步判断包裹能否放入任务初始奖励物品
		if (realTake) {
			// if (allTask != null && allTask.size() >= 100) {
			// return
			// CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(23);
			// }
			return canAddGivenArticle(task);
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 是否包裹足够放入任务初始给予物品 能返回true 不能返回false
	 * 
	 * @param task
	 * @return
	 */
	public CompoundReturn canAddGivenArticle(Task task) {
		if (task.getGivenArticle() == null || task.getGivenArticle().getNames().length == 0) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(true);
		}

		Article[] articles = new Article[task.getGivenArticle().getNames().length];
		for (int i = 0, j = articles.length; i < j; i++) {
			Article article = ArticleManager.getInstance().getArticle(task.getGivenArticle().getNames()[i]);
			articles[i] = article;
		}
		boolean canAdd = canAddAll(articles);
		return CompoundReturn.createCompoundReturn().setBooleanValue(canAdd).setIntValue(canAdd ? 0 : 9);
	}

	/**
	 * 服务器强制给角色增加一个任务，返回是否增加成功.同时做了通知客户端的功能外面调用不需要再处理
	 * 
	 * @param task
	 */
	public CompoundReturn addTaskByServer(Task task) {
		CompoundReturn returns = takeOneTask(task, true, null);
		if (returns.getIntValue() == 0) {
			task.doOnServerBeforeAdd(this);
			this.addTask(task);// 给玩家加入一个任务
			task.doOnServerAfterAdd(this);
			// 通知客户端
			TaskEntity entity = getTaskEntity(task.getId());
			entity.dealOnGet();
			entity.sendEntityChange((byte) 1);
			entity.setStatus(TASK_STATUS_GET);
			TaskEventTransactCenter center = TaskEventTransactCenter.getInstance();
			center.dealWithTask(Taskoperation.accept, task, this, this.getCurrentGame());
		}
		return returns;
	}

	/**
	 * 确定接任务 往任务列表里加入<BR/>
	 * 只维护列表
	 * 
	 * @param task
	 */
	public synchronized void addTask(Task task) {
		try {

			PropsEntity pe = task2TaskPropMap.get(task.getId());
			if (pe != null) {
				ArticleManager am = ArticleManager.getInstance();
				Props a = (Props) am.getArticle(pe.getArticleName());
				if (a.get物品二级分类() == null) {
					if (TaskSubSystem.logger.isWarnEnabled()) {
						TaskSubSystem.logger.warn(this.getLogString() + "[addtask] [任务:" + task.getName() + "] [接任务失败，物品没有二级分类] [物品：" + pe.getArticleName() + "]");
					}
					return;
				}

				PropsUseRecord r = propsUseRecordMap.get(a.get物品二级分类());
				if (r == null) {
					r = new PropsUseRecord(a.get物品二级分类(), System.currentTimeMillis(), (byte) 0);
					propsUseRecordMap.put(a.get物品二级分类(), r);
				}
				if (a.isUsingTimesLimit()) {
					r.useProps();
					if (r.getPropsName().equals("封魔录")) {
						
						int drinkTimes = this.getTieTimes();
						long lastDrinkDate = this.getLastTieDate();
						boolean isSameWeek = TimeTool.instance.isSame(lastDrinkDate, System.currentTimeMillis(), TimeDistance.DAY, 7);
						if(!isSameWeek){
							this.setTieTimes(0);
							this.setLastTieDate(System.currentTimeMillis());
							drinkTimes = 0;
						}
						drinkTimes++;
						this.setTieTimes(drinkTimes);
						
						// 活跃度统计
						ActivenessManager.getInstance().record(this, ActivenessType.封魔录);
						try {
							if (pe.getColorType() < PlayerAimManager.颜色贴action.length) {
								RecordAction action = PlayerAimManager.颜色贴action[pe.getColorType()];
								if (action != null) {
									EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), action, 1L });
									EventRouter.getInst().addEvent(evt);
								}
								if (pe.getColorType() >= 1) {
									EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.使用绿色以上封魔录次数, 1L });
									EventRouter.getInst().addEvent(evt);
									if (pe.getColorType() >= 2) {
										EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.使用蓝色以上封魔录次数, 1L });
										EventRouter.getInst().addEvent(evt2);
									}
									if (pe.getColorType() >= 3) {
										EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.使用紫色以上封魔录次数, 1L });
										EventRouter.getInst().addEvent(evt2);
									}
								}
							}
							EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.使用封魔录次数, 1L });
							EventRouter.getInst().addEvent(evt);
						} catch (Exception e) {
							PlayerAimManager.logger.error("[目标系统] [使用封魔录统计异常] [" + this.getLogString() + "]");
						}
					}
				}
				this.setDirty(true, "propsUseRecordMap");
			}

			List<AavilableTaskInfo> infos = new ArrayList<AavilableTaskInfo>();
			byte modifyType = 0;
			TaskEntity entity = getTaskEntity(task.getId());
			if (entity != null && (task.getType() == TaskConfig.TASK_TYPE_DAILY)) { // 做过的日常
				entity.reSet();
				if (task.getDeliverTimeLimit() != null) {
					entity.setDeliverTimeLimit(true);
					entity.setLeftDeliverTime(task.getDeliverTimeLimit().getTime());
					entity.setFailTime(task.getDeliverTimeLimit().getTime());
				}
				entity.setExcess(false);
				if (TaskSubSystem.logger.isInfoEnabled()) {
					TaskSubSystem.logger.info(getLogString() + "[接取任务:" + task.getName() + "] [日常] [成功]");
				}
			} else if (entity == null) {// 新接的任务
				entity = new TaskEntity(task, this);
				if (task.getDeliverTimeLimit() != null) {
					entity.setDeliverTimeLimit(true);
					entity.setLeftDeliverTime(task.getDeliverTimeLimit().getTime());
					entity.setFailTime(task.getDeliverTimeLimit().getTime());
				}
				entity.setExcess(false);
				getAllTask().add(entity);

				TaskEntityManager.em.notifyNewObject(entity);

				if (TaskSubSystem.logger.isInfoEnabled()) {
					TaskSubSystem.logger.info(getLogString() + "[接取任务:" + task.getName() + "] [taskID:"+task.getId()+"] [taskEntityID:"+entity.getId()+"] [第一次接] [成功]");
				}
			} else {
				if (TaskSubSystem.logger.isWarnEnabled()) {
					TaskSubSystem.logger.warn(getLogString() + "[接取任务:" + task.getName() + "] [要接取非日常,但列表中已经存在] [任务状态:" + entity.getStatus() + "] [失败]");
				}
			}

			// 如果在推荐的任务列表中，则删除
			if (getNextCanAcceptTasks().contains(task.getId())) {
				infos.add(new AavilableTaskInfo(task));
				modifyType = 1;
				getNextCanAcceptTasks().remove(task.getId());
				setDirty(true, "nextCanAcceptTasks");
			}
			TaskManager.logger.warn(this.getLogString() + "[addtask] [{}] [当前可接任务列表] [{}]", new Object[] { task.getName(), Arrays.toString(getNextCanAcceptTasks().toArray(new Long[0])) });
			if (infos.size() > 0) {
				noticeClientCanAcceptModify(modifyType, infos,"addTask");
			}

			GameStatClientService.statPlayerAcceptTask(this, task);
			// if(task.getLimitType() > 0) {
			// TimeLimitManager.getInst().add2AllActMap(task.getLimitType(), task);
			// }
		} catch (Exception e) {
			TaskSubSystem.logger.error(getLogString() + " [AddTask] [异常] ", e);
		}
	}

	/**
	 * 通知客户端可接任务列表发生变化
	 * 
	 * @param modifyType
	 * @param taskIds
	 */
	public void noticeClientCanAcceptModify(byte modifyType, List<AavilableTaskInfo> infos,String reason) {
		try {
			if(testTask == 99){
				if(infos != null && infos.size() > 0){
					for(AavilableTaskInfo info : infos){
						if(info == null){
							continue;
						}
						TaskManager.logger.warn("[客户端可接任务变化test] [reason:"+reason+"] [id:"+info.getTaskId()+"] [name:"+info.getTaskName()+"] [xy:"+info.getX()+"/"+info.getY()+"] [角色:"+getName()+"] [数量:"+infos.size()+"]");
					}
				}else{
					TaskManager.logger.warn("[客户端可接任务变化test] [错误:没有任务] [reason:"+reason+"] [角色:"+getName()+"] [数量:"+infos+"]");
				}
			}
			CAN_ACCEPT_TASK_MODIFY_REQ req = new CAN_ACCEPT_TASK_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), modifyType, infos.toArray(new AavilableTaskInfo[0]));
			this.addMessageToRightBag(req);
		} catch (Exception e) {
			TaskSubSystem.logger.error("noticeClientCanAcceptModify", e);
		}
	}
	
	public transient int testTask;

	/**
	 * 把任务加入到已接受的任务列表
	 * 
	 * @param entity
	 */
	// private void putIntoAcceptTaskList(TaskEntity entity) {
	// Task task = TaskManager.getInstance().getTask(entity.getTaskId());
	// if (TaskSubSystem.logger.isInfoEnabled()) {
	// TaskSubSystem.logger.info(this.getLogString() + "[将任务{}放入显示列表]", new
	// Object[] { task.getName() });
	// }
	// for (int i = 0, j = task.getTargets().length; i < j; i++) {
	// if (!getAcceptTasks().containsKey(task.getTargets()[i].getTargetType()))
	// {
	// getAcceptTasks().put(task.getTargets()[i].getTargetType(), new
	// ArrayList<TaskEntity>());
	// }
	// getAcceptTasks().get(task.getTargets()[i].getTargetType()).add(entity);
	// }
	// }

	static boolean canAddToNextlist(CompoundReturn cr) {
		return cr.getIntValue() != 4 && cr.getIntValue() != 12 && cr.getIntValue() != 15 && cr.getIntValue() != 3;
	}

	/**
	 * 在已接列表移除<BR/>
	 * 只维护列表
	 * 
	 * @param task
	 * @param action
	 *            .完成,放弃
	 */
	public synchronized void transferTask(Task task, int action) {
		try {
			List<AavilableTaskInfo> infos = new ArrayList<AavilableTaskInfo>();
			byte modifyType = 0;

			TaskEntity entity = getTaskEntity(task.getId());
			if (entity == null) {
				TaskSubSystem.logger.error("{}的时候玩家[{}]身上没有任务:{}", new Object[] { action, getName(), task.getName() });
				return;
			}

			if (task.getType() == TASK_TYPE_ONCE) { // 单次任务在所有任务列表中删除，并创建deliverTask

				getAllTask().remove(entity);

				// 加入cache为了jpa能存盘
				TaskEntityManager.getInstance().cacheTaskEntityForSave(entity);

				if (action == TASK_ACTION_DELIVER) {
					entity.setStatus(TaskConfig.TASK_STATUS_DEILVER);
					try {
						entity.setLastDeliverTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						DeliverTaskManager dtm = DeliverTaskManager.getInstance();
						NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
						if (ndtm != null && ndtm.isNewDeliverTaskAct) {
							NewDeliverTask newDeliverT = new NewDeliverTask(entity.getPlayerId(), entity.getTaskId());
							ndtm.notifyNewDeliverTask(this, newDeliverT);

							// DeliverTask deliverTask = new DeliverTask(entity); // 即使使用新代码，旧的delivertask库也存储已完成数据
							// deliverTask.setDeliverTimes(DeliverTaskManager.deleteFlag);
							// dtm.notifyNewDeliverTask(this, deliverTask);
						} else {
							DeliverTask deliverTask = new DeliverTask(entity);
							dtm.notifyNewDeliverTask(this, deliverTask);
						}
						// TaskEntityManager.getInstance().notifyDeleteFromCache(entity); // 暂时去掉删除数据库操作，合服时删，减少服务器压力
					} catch (Exception e) {
						TaskSubSystem.logger.error(getLogString() + "[transferTask] [异常]", e);
					}
				}
			}

			if (action == TASK_ACTION_DELIVER) {
				try {
					int can = -1;
					if (Translate.刺探.equals(task.getCollections())) {
						TaskManager tm = TaskManager.getInstance();
						FunctionNPC fns[] = tm.getFunctionNPCsByGame(getCurrentGame(), this);
						FUNCTION_NPC_RES ress = new FUNCTION_NPC_RES(GameMessageFactory.nextSequnceNum(), fns);
						this.addMessageToRightBag(ress);
						if (TaskSubSystem.logger.isWarnEnabled()) {
							TaskSubSystem.logger.warn(this.getLogString() + "[完成任务:" + task.getName() + "] [通知客户端新的FUNCTIONNPC]");
							for (FunctionNPC f : fns) {
								TaskSubSystem.logger.warn(this.getLogString() + "[完成任务:" + task.getName() + "] [通知客户端新的FUNCTIONNPC] [NPCName:" + f.getName() + "] [任务:" + Arrays.toString(f.getAvaiableTaskNames()) + "]");
							}
						}
					} else {
						can = takeOneTask(task, false, null).getIntValue();
					}
					if (can == 0) {
						getNextCanAcceptTasks().add(task.getId());
						TaskManager.logger.warn("[任务可接列表变化] [transferTask] ["+task.getId()+"/"+task.getName()+"] [country:"+task.getCountryLimit()+"/"+getCountry()+"] ["+getName()+"]");
						setDirty(true, "nextCanAcceptTasks");
						if (getCurrentGame().getGameInfo().displayName.equals(task.getStartMap())) {
							FunctionNPC npc = getFunctionNPC(task.getStartMap(), task.getStartNpc());
							if (npc != null) {
								npc.addTask2Wait(ModifyType.TASK_DELIVER, task);
							}
						}
					}
					// 该任务的后续任务
					List<Task> list = TaskManager.getInstance().getnextTask(task.getGroupName());
					if (list != null) {
						for (int i = 0, j = list.size(); i < j; i++) {
							Task next = list.get(i);
							if (!getNextCanAcceptTasks().contains(next.getId())) {
								CompoundReturn cr = takeOneTask(next, false, null);
								if (canAddToNextlist(cr)) {
									if (next.getId() != 100843) { // 成仙成妖 任务不需要加入可接列表
										getNextCanAcceptTasks().add(next.getId());
										TaskManager.logger.warn("[任务可接列表变化] [transferTask2] ["+next.getId()+"/"+next.getName()+"] [country:"+next.getCountryLimit()+"/"+getCountry()+"] ["+getName()+"]");
										infos.add(new AavilableTaskInfo(next));
										modifyType = 0;
										setDirty(true, "nextCanAcceptTasks");
//										if (TaskSubSystem.logger.isWarnEnabled()) {
//											TaskSubSystem.logger.warn(this.getLogString() + "[完成了任务:{}] [后续:{}] [加入到了可接列表] [可接判断返回:{}]", new Object[] { task.getName(), next.getName(), cr.getIntValue() });
//										}
									}
								} else {
//									if (TaskSubSystem.logger.isWarnEnabled()) {
//										TaskSubSystem.logger.warn(this.getLogString() + "[完成了任务:{}] [后续:{}] [没有加入到可接列表] [可接判断返回:{}]", new Object[] { task.getName(), next.getName(), cr.getIntValue() });
//									}
								}
							} else {
								if (TaskSubSystem.logger.isWarnEnabled()) {
									TaskSubSystem.logger.warn(this.getLogString() + "[完成了任务:{}] [后续:{}] [已经在可接列表中了]", new Object[] { task.getName(), next.getName() });
								}
							}
						}
					}
					if (task.isBournTask() && task.getCollections().equals(Translate.境界)) {
						List<Integer> resultIndexs = RandomTool.getResultIndexs(RandomType.groupRandom, BournManager.defaultRate, 1);
						star = resultIndexs.get(0);
						task = BournManager.starTaskMap.get(this.getClassLevel())[star][new Random().nextInt(BournManager.starTaskMap.get(this.getClassLevel())[star].length)];
						this.setCurrBournTaskStar(star);
						this.setCurrBournTaskName(task.getName());
						if (BournManager.logger.isWarnEnabled()) {
							BournManager.logger.warn(this.getLogString() + "[完成/放弃任务,自动刷新] [星级{}] [任务{}]", new Object[] { star, task });
						}
					}
					if (TaskManager.logger.isDebugEnabled()) {
						TaskManager.logger.debug(this.getLogString() + "[transferTask] [{}] [当前可接任务列表] [{}]", new Object[] { task.getName(), Arrays.toString(getNextCanAcceptTasks().toArray(new Long[0])) });
					}

					GameStatClientService.statPlayerDeliverTask(this, task);
				} catch (Exception e) {
					TaskSubSystem.logger.error(getLogString() + "[transferTask] [异常]", e);
				}
			} else if (action == TASK_ACTION_GIVEUP) { // 放弃任务。从新放回列表
				if (!getNextCanAcceptTasks().contains(task.getId())) {
					getNextCanAcceptTasks().add(task.getId());
					TaskManager.logger.warn("[任务可接列表变化] [transferTask3] ["+task.getId()+"/"+task.getName()+"] [country:"+task.getCountryLimit()+"/"+getCountry()+"] ["+getName()+"]");
					setDirty(true, "nextCanAcceptTasks");
				}
				if (task.getType() == TaskConfig.TASK_TYPE_ONCE) {// 单次任务,直接在Alltask移除
					TaskEntityManager.getInstance().notifyDeleteFromCache(entity);
				}
				infos.add(new AavilableTaskInfo(task));
				modifyType = 0;
				// 如果接任务NPC在当前地图 则加入到列表中
				FunctionNPC functionNPC = getFunctionNPC(task.getStartMap(), task.getStartNpc());
				if (functionNPC != null) {// 有这个NPC
					functionNPC.addTask2Wait(ModifyType.TASK_GIVEUP, task);
				}
				checkFunctionNPCModify(ModifyType.TASK_GIVEUP);
			}
			if (!infos.isEmpty()) {
				noticeClientCanAcceptModify(modifyType, infos,"transferTask");
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error(this.getLogString() + "[移动任务异常] [任务:{" + task.getName() + "}] [action:{" + action + "}]", e);
		}
	}

	public void noticeGetArticle(ArticleEntity articleEntity) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("<f>" + Translate.获得 + "</f>").append("<f color='").append(ArticleManager.color_article[articleEntity.getColorType()]).append("' >").append(articleEntity.getArticleName()).append("</f>");
		sendNotice(sbf.toString());
	}

	/**
	 * 采集一次（任务）
	 * 
	 * @param collectionNPC
	 */
	public void onceCollection(TaskCollectionNPC collectionNPC) {
		try {
			if (collectionNPC.canCollection(this)) {
				int num = collectionNPC.getOnceDorpNum();
				if (num <= 0) {
					TaskSubSystem.logger.error(getLogString() + "[采集] [" + collectionNPC.name + "] [数量:" + num + "]");
					return;
				}
				collectionNPC.getPlayerCollectionMap().put(getId(), SystemTime.currentTimeMillis());
				Player players[] = getSameTeamPlayers(false);
				if (players.length == 0) {
					TaskSubSystem.logger.error(getLogString() + "[采集] [队伍数量为0]");
				}
				for (Player player : players) {
					if (player.equals(this) || collectionNPC.canCollection(player)) {
						TaskActionOfCollection actionOfCollection = TaskActionOfCollection.createTaskAction(collectionNPC.getArticleColor(), collectionNPC.getArticleName(), num);
						player.dealWithTaskAction(actionOfCollection);
						if (TaskSubSystem.logger.isWarnEnabled()) {
							TaskSubSystem.logger.warn(getLogString() + "[采集] [" + collectionNPC.name + "] [数量:" + num + "] [更改完成]");
						}
					}
				}
				COLLECTION_NPC_MODIFY_REQ req = new COLLECTION_NPC_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, new long[] { collectionNPC.getId() });
				addMessageToRightBag(req);
			} else {
				if (TaskSubSystem.logger.isWarnEnabled()) {
					TaskSubSystem.logger.warn(getLogString() + "[采集] [" + collectionNPC.name + "] [不可采集 ]");
				}
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error(getLogString() + "[任务采集异常]", e);
		}
	}

	public Player[] getSameTeamPlayers(boolean print) {
		Player players[] = null;
		if (this.getTeamMark() != Player.TEAM_MARK_NONE && this.getTeam() != null) {
			ArrayList<Player> al = new ArrayList<Player>();
			players = this.getTeamMembers();
			for (int i = 0; i < players.length; i++) {
				if (players[i] == this) {
					al.add(players[i]);
				} else if (isAround(players[i])) {
					al.add(players[i]);
					if (print) {
						try {
							TaskSubSystem.logger.error(this.getTeamLogString() + " [杀死了怪物] [增加新队友:" + players[i].getName() + "] [this.getTeamMark():" + this.getTeamMark() + "] [Team:" + getTeam() + "] [teamMembers.length:" + getTeam().members.size() + "] [自己所在地图:" + this.getCurrentGame().gi.displayName + "] [队友所在地图" + (players[i].getCurrentGame() == null ? "null" : players[i].getCurrentGame().gi.displayName) + "]");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					if (print) {
						try {
							TaskSubSystem.logger.error(this.getTeamLogString() + " [杀死了怪物] [队友不在身边:" + players[i].getName() + "] [自己所在地图:" + this.getCurrentGame().gi.displayName + "] [队友所在地图" + (players[i].getCurrentGame() == null ? "null" : players[i].getCurrentGame().gi.displayName) + "]");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			players = al.toArray(new Player[0]);

		} else {
			players = new Player[1];
			players[0] = this;
			if (print) {
				try {
					TaskSubSystem.logger.error(this.getTeamLogString() + " [杀死了怪物] [只有自己] [this.getTeamMark():" + this.getTeamMark() + "] [Team:" + getTeam() + "] [teamMembers.length:" + getTeam() == null ? "--" : getTeam().members.size() + "]");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return players;
	}

	public List<TaskEntity> getTasksByTargetType(TargetType targetType) {
		List<TaskEntity> entitys = new ArrayList<TaskEntity>();
		if (getAllTask() != null) {
			for (int i = 0; i < getAllTask().size(); i++) {
				TaskEntity te = getAllTask().get(i);
				if (te != null && te.getStatus() != TASK_STATUS_DEILVER) {
					if (te.getTask() != null) {
						List<TaskTarget> tts = te.getTask().getTargetByType(targetType);
						if (tts != null && tts.size() > 0) {
							entitys.add(te);
						}
					}
				}
			}
		}
		return entitys;
	}

	/**
	 * 进入地图时检测任务采集点
	 * 
	 * @param player
	 */
	public void checkCollectionNPC(Game game) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		List<Long> npcIds = new ArrayList<Long>();
		List<TaskEntity> taskEntities = getTasksByTargetType(TargetType.COLLECTION); // getAcceptTasks().get(TargetType.COLLECTION);
		if (taskEntities != null) {
			for (TaskEntity te : taskEntities) {
				int collectionIndex = -1;
				CheckIndex: for (int i = 0; i < te.getTask().getTargets().length; i++) {
					TaskTarget target = te.getTask().getTargets()[i];
					if (target.getTargetType().equals(TargetType.COLLECTION)) {
						collectionIndex = i;
						break CheckIndex;
					}
				}
				if (collectionIndex != -1) {
					if (!te.isComplete(collectionIndex)) { // 目标没完成，需要能看到所有与这个任务相关的采集NPC
						for (LivingObject lo : game.getLivingObjects()) {
							if (lo instanceof TaskCollectionNPC) { // 只处理这一类的NPC
								TaskCollectionNPC npc = (TaskCollectionNPC) lo;
								if (npc.getTaskNames().contains(te.getTask().getName())) {
									npcIds.add(npc.getId());
								}
							}
						}
					}
				}
			}
		}
		Long[] Lids = npcIds.toArray(new Long[0]);
		long[] lids = new long[Lids.length];
		for (int i = 0; i < Lids.length; i++) {
			lids[i] = Lids[i];
		}
		COLLECTION_NPC_MODIFY_REQ req = new COLLECTION_NPC_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, lids);
		addMessageToRightBag(req);
	}

	public List<Long> getNextCanAcceptTasks() {
		return nextCanAcceptTasks;
	}

	public void setNextCanAcceptTasks(List<Long> nextCanAcceptTasks) {
		this.nextCanAcceptTasks = nextCanAcceptTasks;
		this.setDirty(true, "nextCanAcceptTasks");
	}

	// 任务↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	public static String[] autoBackString = { "", 挂机勿扰, 吃饭去了, 睡觉中, 忙着呢 };
	public byte autoBack = 0; // 0 没有设置自动 1 2 3 各自对应不同的回复

	private boolean upLineNotice = true;

	public boolean isUpLineNotice() {
		return upLineNotice;
	}

	public void setUpLineNotice(boolean upLineNotice) {
		this.upLineNotice = upLineNotice;
	}

	/**
	 * 
	 * @param playerId
	 * @return 是黑名单 true
	 */
	public boolean isBlackuser(long playerId) {
		List<Long> blackList = SocialManager.getInstance().getBlacklist(this);
		if (blackList != null) {
			if (blackList.contains(playerId)) {
				return true;
			}
		}
		return false;
	}

	public boolean isFriend(long playerId) {
		List<Long> flist = SocialManager.getInstance().getFriendlist(this);
		if (flist != null) {
			if (flist.contains(playerId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 玩家的坐骑列表
	 */
	private ArrayList<Long> horseArr = new ArrayList<Long>();

	// horseArr不用
	private transient ArrayList<Long> horseIdList = new ArrayList<Long>();

	/**
	 * 选中的坐骑(默认坐骑)
	 */
	private transient long rideHorseId = -1;

	/**
	 * 正在骑的坐骑id(正在骑的坐骑，默认坐骑骑乘产生正在骑的坐骑)
	 */
	private transient long ridingHorseId = -1;

	public long getRidingHorseId() {
		return ridingHorseId;
	}

	public void setRidingHorseId(long ridingHorseId) {

		getCurrSoul().setRidingHorseId(ridingHorseId);
		setDirty(true, "currSoul");
		this.ridingHorseId = ridingHorseId;
	}

	/**
	 * 上次喂养时间
	 */
	private transient String lastFeedTime;

	/**
	 * 今天喂养次数
	 */
	private transient int feedNum;

	public ArrayList<Long> getHorseArr() {
		return horseArr;
	}

	public void setHorseArr(ArrayList<Long> horseArr) {
		this.horseArr = horseArr;
	}

	/**
	 * 把人物身上的马属性删除
	 * 
	 * @param horse
	 */
	public void removeHorseProperty(Horse horse) {
		int index = horse.getLastEnergyIndex();

		this.setMaxHPB(getMaxHPB() - horse.getMaxHP() * index / 10);
		this.setPhyAttackB(this.getPhyAttackB() - horse.getPhyAttack() * index / 10);
		this.setMagicAttackB(this.getMagicAttackB() - horse.getMagicAttack() * index / 10);
		this.setPhyDefenceB(this.getPhyDefenceB() - horse.getPhyDefence() * index / 10);
		this.setMagicDefenceB(this.getMagicDefenceB() - horse.getMagicDefence() * index / 10);

		this.setMaxMPB(this.getMaxMPB() - horse.getMaxMP() * index / 10);
		this.setBreakDefenceB(this.getBreakDefenceB() - horse.getBreakDefence() * index / 10);
		this.setHitB(getHitB() - horse.getHit() * index / 10);
		this.setDodgeB(getDodgeB() - horse.getDodge() * index / 10);
		this.setAccurateB(getAccurateB() - horse.getAccurate() * index / 10);
		this.setCriticalDefenceB(getCriticalDefenceB() - horse.getCriticalDefence() * index / 10);
		this.setCriticalHitB(getCriticalHitB() - horse.getCriticalHit() * index / 10);
		this.setFireAttackB(getFireAttackB() - horse.getFireAttack() * index / 10);
		this.setFireDefenceB(getFireDefenceB() - horse.getFireDefence() * index / 10);
		this.setBlizzardAttackB(getBlizzardAttackB() - horse.getBlizzardAttack() * index / 10);
		this.setBlizzardDefenceB(getBlizzardDefenceB() - horse.getBlizzardDefence() * index / 10);
		this.setWindAttackB(getWindAttackB() - horse.getWindAttack() * index / 10);
		this.setWindDefenceB(getWindDefenceB() - horse.getWindDefence() * index / 10);
		this.setThunderAttackB(getThunderAttackB() - horse.getThunderAttack() * index / 10);
		this.setThunderDefenceB(getThunderDefenceB() - horse.getThunderDefence() * index / 10);

		this.setFireIgnoreDefenceB(fireIgnoreDefenceB - horse.getFireIgnoreDefence() * index / 10);
		this.setBlizzardIgnoreDefenceB(blizzardIgnoreDefenceB - horse.getBlizzardIgnoreDefence() * index / 10);
		this.setWindIgnoreDefenceB(windIgnoreDefenceB - horse.getWindIgnoreDefence() * index / 10);
		this.setThunderIgnoreDefenceB(thunderIgnoreDefenceB - horse.getThunderIgnoreDefence() * index / 10);

		if (HorseManager.logger.isDebugEnabled()) {
			HorseManager.logger.debug("removeHorseProperty[MaxHP:" + this.getMaxHP() + "] [player:" + this.getLogString() + "]\n" + StringUtil.getStackTrace(Thread.currentThread().getStackTrace()));
		}
	}

	/**
	 * 把马属性加到人物身上
	 * 
	 * @param horse
	 */
	public void addHorseProperty(Horse horse) {

		int index = horse.getLastEnergyIndex();

		this.setMaxHPB(getMaxHPB() + horse.getMaxHP() * index / 10);
		this.setPhyAttackB(this.getPhyAttackB() + horse.getPhyAttack() * index / 10);
		this.setMagicAttackB(this.getMagicAttackB() + horse.getMagicAttack() * index / 10);
		this.setPhyDefenceB(this.getPhyDefenceB() + horse.getPhyDefence() * index / 10);
		this.setMagicDefenceB(this.getMagicDefenceB() + horse.getMagicDefence() * index / 10);

		this.setMaxMPB(this.getMaxMPB() + horse.getMaxMP() * index / 10);
		this.setBreakDefenceB(this.getBreakDefenceB() + horse.getBreakDefence() * index / 10);
		this.setHitB(getHitB() + horse.getHit() * index / 10);
		this.setDodgeB(getDodgeB() + horse.getDodge() * index / 10);
		this.setAccurateB(getAccurateB() + horse.getAccurate() * index / 10);
		this.setCriticalDefenceB(getCriticalDefenceB() + horse.getCriticalDefence() * index / 10);
		this.setCriticalHitB(getCriticalHitB() + horse.getCriticalHit() * index / 10);
		this.setFireAttackB(getFireAttackB() + horse.getFireAttack() * index / 10);
		this.setFireDefenceB(getFireDefenceB() + horse.getFireDefence() * index / 10);
		this.setBlizzardAttackB(getBlizzardAttackB() + horse.getBlizzardAttack() * index / 10);
		this.setBlizzardDefenceB(getBlizzardDefenceB() + horse.getBlizzardDefence() * index / 10);
		this.setWindAttackB(getWindAttackB() + horse.getWindAttack() * index / 10);
		this.setWindDefenceB(getWindDefenceB() + horse.getWindDefence() * index / 10);
		this.setThunderAttackB(getThunderAttackB() + horse.getThunderAttack() * index / 10);
		this.setThunderDefenceB(getThunderDefenceB() + horse.getThunderDefence() * index / 10);

		this.setFireIgnoreDefenceB(fireIgnoreDefenceB + horse.getFireIgnoreDefence() * index / 10);
		this.setBlizzardIgnoreDefenceB(blizzardIgnoreDefenceB + horse.getBlizzardIgnoreDefence() * index / 10);
		this.setWindIgnoreDefenceB(windIgnoreDefenceB + horse.getWindIgnoreDefence() * index / 10);
		this.setThunderIgnoreDefenceB(thunderIgnoreDefenceB + horse.getThunderIgnoreDefence() * index / 10);
		int speed = horse.getSpeed();
		this.setSpeedE(speed);

		if (HorseManager.logger.isDebugEnabled()) {
			HorseManager.logger.debug("addHorseProperty[MaxHP:" + this.getMaxHP() + "] [" + this.getMaxHPB() + "] [player:" + this.getLogString() + "]\n" + StringUtil.getStackTrace(Thread.currentThread().getStackTrace()));

		}
	}

	/**
	 * 回收坐骑
	 */
	public void getDownFromHorse() {

	}

	public void downFromHorse() {
		this.downFromHorse(false);
	}

	/**
	 * 下坐骑
	 */
	public void downFromHorse(boolean serverOp) {
		try {
			if (HorseManager.logger.isInfoEnabled()) {
				HorseManager.logger.info("[玩家请求下坐骑] [" + this.getLogString() + "]");
			}
			if (!serverOp && this.isIceState()) {
				sendError(Translate.您处于冰冻状态下不能使用坐骑);
				return;
			}
			if (isUpOrDown) {
//				try {
//					if (this.getCurrentGame() != null && JJCManager.isJJCBattle(this.getCurrentGame().gi.name) && !this.isDeath()) {
//						this.sendError(Translate.鲜血试炼中不能下马);
//						return;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				setIsUpOrDown(false);
				this.setFlying(false);
				Horse rideHorse = HorseManager.getInstance().getHorseById(this.ridingHorseId, this);
				if (rideHorse != null) {
					synchronized (rideHorse) {
						long ridinghorseId = this.ridingHorseId;
						this.setSpeedE(0);
						if (HorseManager.logger.isWarnEnabled()) {
							HorseManager.logger.warn("[下坐骑加载属性前] [" + this.getLogString() + "] [" + rideHorse.getLogString() + "] [maxhpb:] [" + this.getMaxHPB() + "] [horse:] [" + rideHorse.getMaxHP() + "]");
						}
						rideHorse.downHorse(this);
						if (HorseManager.logger.isWarnEnabled()) {
							HorseManager.logger.warn("[下坐骑加载属性后] [" + this.getLogString() + "] [" + rideHorse.getLogString() + "] [maxhpb:] [" + this.getMaxHPB() + "] [horse:] [" + rideHorse.getMaxHP() + "]");
						}
						ResourceManager.getInstance().getAvata(this);
						this.setHorseParticle("");
						setRidingHorseId(-1);

						if (rideHorse.isFly()) {
							if (this.getFlyPackupPetId() > 0) {

								long pid = this.getFlyPackupPetId();
								setFlyPackupPetId(-1);
								Knapsack knapsack = getPetKnapsack();
								PetPropsEntity ppe = null;
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								for (Cell cell : knapsack.getCells()) {
									if (cell != null && cell.getEntityId() > 0 && cell.getCount() > 0) {
										try {
											PetPropsEntity ae = (PetPropsEntity) aem.getEntity(cell.getEntityId());
											if (ae != null && ae.getPetId() == pid) {
												ppe = ae;
												break;
											}
										} catch (Exception ex) {
											PetManager.logger.error("[下坐骑召唤宠物异常] [" + this.getLogString() + "]", ex);
										}
									}
								}
								if (ppe != null) {
									PetManager pm = PetManager.getInstance();
									Pet pet = pm.getPet(pid);
									if (pet != null && this.getCurrentGame() != null && !this.getCurrentGame().contains(pet)) {
										setActivePetId(-1);
										this.notifyAttributeAttackChange();
										summonPet(pet, ppe, "下坐骑收回宠物");
										if (PetManager.logger.isInfoEnabled()) PetManager.logger.info("[{}] [{}] [{}] [下坐骑召唤宠物] [{}] [{}] [{},{}] [{},{}]", new Object[] { username, name, id, pet.getId(), pet.getName(), pet.getX(), pet.getY(), getX(), getY() });
									}
								} else {
									packupPet(true);
									if (PetManager.logger.isWarnEnabled()) {
										PetManager.logger.warn("[{}] [{}] [{}] [下坐骑收回宠物失败] [召唤宠物道具实体为空]", new Object[] { username, name, pid });
									}
								}
							}
						}
						HORSE_RIDE_RES res = new HORSE_RIDE_RES(GameMessageFactory.nextSequnceNum(), ridinghorseId, false);
						this.addMessageToRightBag(res);
						if (HorseManager.logger.isWarnEnabled()) {
							HorseManager.logger.warn("[玩家下坐骑成功] [" + this.getLogString() + "] [" + ridinghorseId + "]");
						}
					}
				} else {

					this.setSpeedE(0);

					this.setHorseParticle("");
					setRidingHorseId(-1);

					ResourceManager.getInstance().getAvata(this);
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[玩家下坐骑错误] [默认坐骑为null] [" + this.getLogString() + "]");
					}
				}
			} else {
				if (HorseManager.logger.isWarnEnabled()) HorseManager.logger.warn("[玩家下坐骑错误] [" + this.getLogString() + "]");
			}
		} catch (Exception e) {
			HorseManager.logger.error("[下马错误] [" + this.getLogString() + "]", e);
		}
	}

	/**
	 * 设置默认坐骑
	 */
	public boolean setDefaultHorse(long horseId) {
		List<Long> list = this.getHorseIdList();
		if (list.contains(horseId)) {
			setRideHorseId(horseId);
			return true;
		}
		return false;
	}

	public boolean inCave() {
		if (getCurrentGame() != null) {
			if (getRealMapName().indexOf("_jy_") > 0) {
				return true;
			}
		}
		return false;
	}

	public void upToHorse(long horseId) {

		if (this.isIsUpOrDown()) {
			return;
		}
		if (FairyChallengeManager.getInst().isPlayerChallenging(this)) {
			sendError(Translate.挑战仙尊不允许使用飞行坐骑);
			return;
		}
		if (FairyRobberyManager.inst.isPlayerInRobbery(this)) {
			sendError(Translate.渡劫中不可使用坐骑);
			return;
		}
//		if (DisasterManager.getInst().isPlayerInGame(this)) {
//			sendError(Translate.空岛大冒险中不可使用坐骑);
//			return;
//		}
		if (WolfManager.getInstance().isWolfGame(this)) {
			sendError(Translate.副本中不能使用坐骑);
			return;
		}

		this.getTimerTaskAgent().notifyRide();
		Horse rideHorse = HorseManager.getInstance().getHorseById(horseId, this);

		if (rideHorse != null) {

			if (this.isIceState()) {
				sendError(您处于冰冻状态下不能使用坐骑);
				return;
			}
			if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(this.getId())) {
				sendError(Translate.渡劫中不能使用坐骑);
				return;
			}

			if (rideHorse.isFly()) {

				if (this.getCareer() == 5 && this.getShouStat() == 1) {
					sendError(Translate.兽形态不能使用飞行坐骑);
					return;
				}
				DownCityManager dcm = DownCityManager.getInstance();
				if (dcm != null && dcm.isInDownCityForLimitFly(this.getGame())) {
					sendError(Translate.副本中不能使用飞行坐骑);
					return;
				}

				if (this.isInBattleField()) {
					sendError(您处于战场中不能使用飞行坐骑);
					return;
				}
				if (DiceManager.getInstance().isDiceGame(this)) {
					sendError(Translate.副本中不能使用飞行坐骑);
					return;
				}
				if (WolfManager.getInstance().isWolfGame(this)) {
					sendError(Translate.副本中不能使用飞行坐骑);
					return;
				}
				ChestFightManager cinst = ChestFightManager.inst;
				if (cinst != null && cinst.isPlayerInActive(this)) {
					sendError(Translate.宝箱乱斗中不能使用飞行坐骑);
					return;
				}

				if (this.isGuozhan) {
					sendError(国战活动中不能使用飞行坐骑);
					return;
				}

//				if (SiFangManager.getInstance().isInSiFangGame(this)) {
//					sendError(五方圣兽活动中不能使用飞行坐骑);
//					return;
//				}

				if (QianCengTaManager.getInstance().isInQianCengTa(this)) {
					sendError(幽冥幻域活动中不能使用飞行坐骑);
					return;
				}

				if (DevilSquareManager.instance.isPlayerInDevilSquare(this)) {
					sendError(Translate.恶魔广不允许使用飞行坐骑);
					return;
				}

				if (inCave()) {
					sendError(Translate.仙府中不能使用飞行坐骑);
					return;
				}

				try {
					if (this.getCurrentGame() != null) {
						if (SealManager.getInstance().isSealDownCity(this.getCurrentGame().gi.name)) {
							sendError(Translate.封印挑战不能骑鸟);
							return;
						}
						if (PetDaoManager.getInstance().isPetDao(this.getCurrentGame().gi.name)) {
							sendError(Translate.迷城中不能使用飞行坐骑);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					RideHorseProgressBar rideHorseProgressBar = new RideHorseProgressBar(horseId, this);
					this.getTimerTaskAgent().createTimerTask(rideHorseProgressBar, HorseManager.骑飞行坐骑时间, TimerTask.type_骑飞行坐骑);
					this.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), HorseManager.骑飞行坐骑时间, Translate.骑飞行坐骑));
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[骑飞行坐骑读条] [" + this.getLogString() + "] [horseId:" + horseId + "]");
					}
				} catch (Exception e) {
					HorseManager.logger.error("[骑飞行坐骑异常] [" + this.getLogString() + "] [horseId:" + horseId + "]", e);

				}

			} else {
				finishupToHorse(horseId);
			}
		} else {
			HorseManager.logger.error("[开始上坐骑错误] [没有找到坐骑] [" + this.getLogString() + "] [" + horseId + "]");
		}

	}

	public boolean isSpecialHorse() {
		try {
			if (!this.isIsUpOrDown()) {
				return false;
			}
			Horse horse = HorseManager.getInstance().getHorseById(ridingHorseId, this);
			if (horse == null) {
				return false;
			}
			HorseProps article = (HorseProps) ArticleManager.getInstance().getArticle(horse.getHorseName());
			if (article != null) {
				return article.isSpecialHorse();
			}
		} catch (Exception e) {
			HorseManager.logger.warn("[isSpecialHorse执行异常][" + this.getLogString() + "]", e);
		}
		return false;
	}

	/**
	 * 上坐骑
	 * 
	 * @param num
	 * 
	 */
	public void finishupToHorse(long horseId) {

		try {

			if (this.isIsUpOrDown()) {
				return;
			}
			Horse rideHorse = HorseManager.getInstance().getHorseById(horseId, this);
			if (rideHorse != null) {
				synchronized (rideHorse) {

					if (rideHorse.isFly() != this.isFlying()) {
						this.setFlying(rideHorse.isFly());
						if (HorseManager.logger.isWarnEnabled()) {
							HorseManager.logger.warn("[玩家骑乘坐骑设置fly] [" + this.getLogString() + "] [" + rideHorse.getHorseId() + "]");
						}
					}

					this.setDefaultHorse(horseId);
					this.setRidingHorseId(horseId);
					this.setIsUpOrDown(true);
					int speed = rideHorse.getSpeed();
					this.setSpeedE(speed);
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[上加载坐骑属性前] [" + this.getLogString() + "] [" + rideHorse.getLogString() + "] [maxhpb:] [" + this.getMaxHPB() + "] [horse:] [" + rideHorse.getMaxHP() + "]");
					}
					rideHorse.upHorse(this);
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[上加载坐骑属性后] [" + this.getLogString() + "] [" + rideHorse.getLogString() + "] [maxhpb:] [" + this.getMaxHPB() + "] [horse:] [" + rideHorse.getMaxHP() + "]");
					}
					
					try {
						HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(rideHorse.getRank());
						String oldavata = rideHorse.getAvataKey();
						if(!rideHorse.isFly()){
							if(this.ownMonthCard(CardFunction.坐骑进阶形象额外加1)){
								oldavata = hrm.getMonthAvatar()[career - 1];
							}else{
								oldavata = hrm.getAvatar()[career - 1];
							}
						}
						if (rideHorse.getAvataKey() == null || !rideHorse.getAvataKey().equals(oldavata)) {
							rideHorse.setAvataKey(oldavata);
						}	
						ResourceManager.getInstance().getHorseAvataForPlayer(rideHorse, this);
						rideHorse.selfMarks[31] = true;
						rideHorse.notifySelfChange();
//						rideHorse.setRank(rideHorse.getOtherData().getRankStar());
						HorseManager.logger.warn("[玩家骑乘坐骑改变进阶] [坐骑:"+rideHorse.getHorseName()+"] [rank:"+rideHorse.getRank()+"] [oldavata:"+oldavata+"] [avata:"+rideHorse.getAvata()+"] ["+this.getLogString()+"]");
					} catch (Exception e) {
						e.printStackTrace();
					}
					ResourceManager.getInstance().getAvata(this);
					this.setHorseParticle(rideHorse.getHorseParticle());
					// 如果是飞行坐骑 召回宠物
					if (this.activePetId > 0) {
						if (rideHorse.isFly()) {
							this.setFlyPackupPetId(this.activePetId);
							this.packupPet(true);
							if (HorseManager.logger.isWarnEnabled()) {
								HorseManager.logger.warn("[玩家骑飞行坐骑召回宠物] [" + this.getLogString() + "] [petId:" + activePetId + "]");
							}
						}
					}

					// ResourceManager.getInstance().getAvata(player);
					HORSE_RIDE_RES res = new HORSE_RIDE_RES(GameMessageFactory.nextSequnceNum(), horseId, true);
					this.addMessageToRightBag(res);

					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[玩家骑乘坐骑成功] [" + this.getLogString() + "] [" + horseId + "] [上马true:" + true + "]");
					}
					if (ArticleManager.logger.isWarnEnabled()) {
						ArticleManager.logger.info(this.getLogString() + this.getPlayerPropsString() + "[上坐骑]");
					}
				}
			}
		} catch (Exception e) {
			HorseManager.logger.error("[上马异常] [" + this.getLogString() + "]", e);
		}

	}

	/*
	 * 通过称号获得对应的颜色type
	 */
	public int[] getKeyByTitle(String title) {
		int key = PlayerTitlesManager.getInstance().getKeyByType(title);
		int color = PlayerTitlesManager.getInstance().getColorByType(title);
		if (key < 0 || color < 0) {
			return new int[] {};
		}
		int[] titles = { key, color };
		return titles;
	}

	/**
	 * 喂养坐骑 一天只能喂养俩次 -1 次数受限 -2满体力
	 */
	public int feedHorse(long id, Horse horse) {

		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntity ae = aem.getEntity(id);
		Article a = am.getArticle(ae.getArticleName());
		HorseProps hp = (HorseProps) am.getArticle(horse.getHorseName());
		if (horse.getEnergy() >= hp.getMaxEnergy()) {

			return -2;
		}

		String date = DateFormat.getSimpleDay(new Date());
		if (lastFeedTime != null && date.equals(lastFeedTime)) {

			// if (feedNum >= HorseManager.每天喂养次数) {
			// return -1;
			// }
			feedNum++;

		} else {
			lastFeedTime = date;
			feedNum = 1;
		}
		// 喂养
		ArticleEntity remove = this.removeFromKnapsacks(id, "坐骑喂养", true);
		if (remove != null) {
			setFeedNum(feedNum);
			setLastFeedTime(date);
			int index = horse.feed(a);
			if (HorseManager.logger.isWarnEnabled()) {
				HorseManager.logger.warn("[喂养坐骑成功] [" + this.getLogString() + "] [" + index + "]");
			}
			return index;
		} else {
			if (HorseManager.logger.isWarnEnabled()) {
				HorseManager.logger.warn("[喂养坐骑失败] [" + this.getLogString() + "] [道具为null]");
			}
		}
		if (remove != null) {
			ArticleStatManager.addToArticleStat(this, null, remove, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "坐骑喂养删除", null);
		}
		return horse.getLastEnergyIndex();

	}

	public int getFeedNum() {
		return feedNum;
	}

	public void setFeedNum(int feedNum) {
		getCurrSoul().setFeedNum(feedNum);
		setDirty(true, "currSoul");
		this.feedNum = feedNum;
	}

	public long getRideHorseId() {
		return rideHorseId;
	}

	// 设置元神默认坐骑
	public void setRideHorseId(long rideHorse) {
		getCurrSoul().setDefaultHorseId(rideHorse);
		setDirty(true, "currSoul");
		this.rideHorseId = rideHorse;
	}

	// /////////////////副本相关开始
	public Hashtable<String, DownCityDataForPlayer> comeInDownCityCountMap = new Hashtable<String, DownCityDataForPlayer>();

	public transient byte prepareEnterDownCityStatus = -1;

	public transient byte prepareEnterWingCarbonStatus = -1;

	public Hashtable<String, DownCityDataForPlayer> getComeInDownCityCountMap() {
		return comeInDownCityCountMap;
	}

	public void setComeInDownCityCountMap(Hashtable<String, DownCityDataForPlayer> comeInDownCityCountMap) {
		this.comeInDownCityCountMap = comeInDownCityCountMap;
		setDirty(true, "comeInDownCityCountMap");
	}

	/**
	 * 玩家身上保持的副本进度 key : 副本的名称 value : 副本的Id 此变量需要保存
	 */
	protected transient HashMap<String, String> downCityProgress = new HashMap<String, String>();

	public DownCity getDownCityProgress(String name) {
		DownCityManager dcm = DownCityManager.getInstance();
		String id = downCityProgress.get(name);
		if (id == null) return null;
		return dcm.getDownCityById(id);
	}

	public void addDownCityProgress(DownCity dc) {
		downCityProgress.put(dc.getDi().getMapName(), dc.getId());
	}

	public void 增加今天进入副本次数(String downCityName) {
		DownCityDataForPlayer ddfp = comeInDownCityCountMap.get(downCityName);
		if (ddfp == null) {
			ddfp = new DownCityDataForPlayer();
			ddfp.setDownCityName(downCityName);
			comeInDownCityCountMap.put(downCityName, ddfp);
		}
		int count = 得到今天进入副本次数(downCityName);
		ddfp.setTodayComeInCount(count + 1);
		ddfp.setLastComeInTime(System.currentTimeMillis());
		comeInDownCityCountMap.put(downCityName, ddfp);
		setComeInDownCityCountMap(comeInDownCityCountMap);

	}

	public int 得到今天进入副本次数(String downCityName) {
		DownCityDataForPlayer ddfp = comeInDownCityCountMap.get(downCityName);
		if (ddfp == null) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		int nowDay = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTimeInMillis(ddfp.getLastComeInTime());
		int lastComeInTimeDay = calendar.get(Calendar.DAY_OF_YEAR);
		if (nowDay == lastComeInTimeDay) {
			return ddfp.getTodayComeInCount();
		} else {
			// 清空这个副本的次数
			ddfp.setTodayComeInCount(0);
			ddfp.setLastComeInTime(System.currentTimeMillis());
			setComeInDownCityCountMap(comeInDownCityCountMap);
			return 0;
		}
	}

	// ///////////////////////副本相关结束
	/**
	 * 是否是当前激活元神
	 * 
	 * @param type
	 * @return
	 */
	public boolean isCurrSoul(int type) {
		return getCurrSoul().getSoulType() == type;
	}

	/**
	 * 按类型获得元神
	 * 
	 * @param soulType
	 * @return
	 */
	public Soul getSoul(int soulType) {
		if (isCurrSoul(soulType)) return getCurrSoul();
		if (getUnusedSoul() == null) {
			return null;
		}
		for (Soul soul : getUnusedSoul()) {
			if (soul.getSoulType() == soulType) {
				return soul;
			}
		}
		return null;
	}

	/**
	 * 玩家进队规则 默认是0 0 自动进队 1弹提示
	 */
	private byte inteamRule = 1;

	public byte getInteamRule() {
		return inteamRule;
	}

	public void setInteamRule(byte inteamRule) {
		this.inteamRule = inteamRule;
		setDirty(true, "inteamRule");
	}

	/**
	 * 功能NPC（任务）列表
	 */
	private transient List<FunctionNPC> currMapFunctuinNPC = new ArrayList<FunctionNPC>();

	public void setCurrMapFunctuinNPC(List<FunctionNPC> currMapFunctuinNPC) {
		this.currMapFunctuinNPC = currMapFunctuinNPC;
	}

	public List<FunctionNPC> getCurrMapFunctuinNPC() {
		return currMapFunctuinNPC;
	}

	/**
	 * 角色身上一些信息引起NPC身上任务的变化
	 * 
	 * @param modifyType
	 */
	public synchronized void checkFunctionNPCModify(ModifyType modifyType) {
		try {
			List<FunctionNpcModify> functionNpcModifies = new ArrayList<FunctionNpcModify>();
			for (FunctionNPC npc : getCurrMapFunctuinNPC()) {
				List<Task> modifyIdList = null;// 动作引起的该NPC身上任务列表变化
				if (modifyType.isIncrease()) {// 检索还不能接的列表 可能会增加列表内容
					List<Long> ids = npc.getWaitForChange().get(modifyType);
					if (ids != null && ids.size() > 0) {
						List<Long> removed = new ArrayList<Long>();
						for (Long id : ids) {
							Task task = TaskManager.getInstance().getTask(id);
							if (task == null) {
								removed.add(id);
								if (TaskSubSystem.logger.isWarnEnabled()) TaskSubSystem.logger.warn(this.getLogString() + "[checkFunctionNPCModify] [{}] [任务不存在,ID:{}]", new Object[] { modifyType.getName(), id });
								continue;
							}
							CompoundReturn cr = takeOneTask(task, false, npc);
							if (cr.getIntValue() == 0) {// 可以接
								// 移动列表
								removed.add(id);
								if (modifyIdList == null) {
									modifyIdList = new ArrayList<Task>();
								}
								modifyIdList.add(task);
							}
						}
						for (Long id : removed) {
							npc.getWaitForChange().get(modifyType).remove(id);
						}

						for (Iterator<ModifyType> itor = npc.getRemoveForChange().keySet().iterator(); itor.hasNext();) {
							ModifyType removeType = itor.next();
							for (int i = 0; i < npc.getRemoveForChange().get(removeType).size(); i++) {
								npc.getWaitForChange().get(removeType).remove(npc.getRemoveForChange().get(removeType).get(i));
							}
						}
						// 在列表中移除 并实例化modifyNPC对象
						if (modifyIdList != null) {
							FunctionNpcModify npcModify = new FunctionNpcModify();
							long[] modifyIds = new long[modifyIdList.size()];
							String[] modifyNames = new String[modifyIdList.size()];
							byte[] modifyTypes = new byte[modifyIdList.size()];
							int[] modifyGrades = new int[modifyIdList.size()];

							for (int i = 0; i < modifyIdList.size(); i++) {
								ids.remove(modifyIdList.get(i));
								modifyIds[i] = modifyIdList.get(i).getId();
								modifyNames[i] = modifyIdList.get(i).getName();
								modifyTypes[i] = modifyIdList.get(i).getShowType();
								modifyGrades[i] = modifyIdList.get(i).getGrade();

							}
							npcModify.setNPCId(npc.getId());
							npcModify.setModifyType((byte) 0);
							npcModify.setModifyIds(modifyIds);
							npcModify.setModifyGrades(modifyGrades);
							npcModify.setModifyNames(modifyNames);
							npcModify.setModifyTypes(modifyTypes);
							functionNpcModifies.add(npcModify);
						}
					}
				} else { // 减少已接任务列表,检索所有的任务----此处是否可以优化
					for (int i = 0; i < npc.getAvaiableTaskIds().length; i++) { // 在玩家接任务那里去掉这个.置-1
						long taskId = npc.getAvaiableTaskIds()[i];
						if (taskId > 0) {
							Task task = TaskManager.getInstance().getTask(taskId);
							if (task == null) {
								continue;
							}
							CompoundReturn cr = takeOneTask(task, false, npc);
							if (cr.getIntValue() != 0) {
								if (modifyIdList == null) {
									modifyIdList = new ArrayList<Task>();
								}
								modifyIdList.add(task);
								npc.getAvaiableTaskIds()[i] = -1;
							}
						}
					}
					if (modifyIdList != null) {
						FunctionNpcModify npcModify = new FunctionNpcModify();
						long[] modifyIds = new long[modifyIdList.size()];
						String[] modifyNames = new String[modifyIdList.size()];
						byte[] modifyTypes = new byte[modifyIdList.size()];
						int[] modifyGrades = new int[modifyIdList.size()];
						for (int i = 0; i < modifyIdList.size(); i++) {
							modifyIds[i] = modifyIdList.get(i).getId();
							modifyNames[i] = modifyIdList.get(i).getName();
							modifyTypes[i] = modifyIdList.get(i).getShowType();
							modifyGrades[i] = modifyIdList.get(i).getGrade();

						}
						npcModify.setNPCId(npc.getId());
						npcModify.setModifyType((byte) 1);
						npcModify.setModifyIds(modifyIds);
						npcModify.setModifyGrades(modifyGrades);
						npcModify.setModifyNames(modifyNames);
						npcModify.setModifyTypes(modifyTypes);
						functionNpcModifies.add(npcModify);
					}
				}
			}

			if (functionNpcModifies.size() > 0) { // 需要给客户端发送
				FUNCTION_NPC_MODIFY_REQ req = new FUNCTION_NPC_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), functionNpcModifies.toArray(new FunctionNpcModify[0]));
				this.addMessageToRightBag(req);
				if (TaskSubSystem.logger.isDebugEnabled()) {
					for (FunctionNpcModify fn : functionNpcModifies) {
						if (TaskSubSystem.logger.isDebugEnabled()) TaskSubSystem.logger.debug(getLogString() + "[modifyType:{}] [NPC:{}] [导致] [增加任务: {}]", new Object[] { modifyType.getName(), fn.getNPCId(), Arrays.toString(fn.getModifyIds()) });
					}
				}
			} else {

			}
		} catch (Exception e) {
			TaskSubSystem.logger.error("checkFunctionNPCModify异常,类型:" + modifyType.getName(), e);
		}
	}

	/**
	 * 寻找当前地图的某个功能NPC
	 * 
	 * @param NPCName
	 * @return
	 */
	public FunctionNPC getFunctionNPC(String mapShowName, String NPCName) {
		if (getCurrentGame().getGameInfo().displayName.equals(mapShowName)) {
			for (FunctionNPC npc : getCurrMapFunctuinNPC()) {
				if (npc.getName().equals(NPCName)) {
					return npc;
				}
			}
		}
		return null;
	}

	public TaskEntity[] getTaskEntitysForDisplay() {
		List<TaskEntity> accept = new ArrayList<TaskEntity>();

		TaskEntity ts[] = allTask.toArray(new TaskEntity[0]);
		for (int i = 0; i < ts.length; i++) {
			if (accept.contains(ts[i]) == false && ts[i].getStatus() != TASK_STATUS_DEILVER) {
				accept.add(ts[i]);
			}
		}

		return accept.toArray(new TaskEntity[0]);
	}

	/***********************************************************************************************************************************************/
	// 所有计算属性的都放在这里
	public void setStrengthA(int value) {
		this.strengthA = value;
		int a = ((strengthA + strengthB) * (strengthC + 100) / 100) + strengthX;
		this.setStrength(a);
	}

	public void setStrengthB(int value) {
		this.strengthB = value;
		int a = ((strengthA + strengthB) * (strengthC + 100) / 100) + strengthX;
		this.setStrength(a);
	}

	public void setStrengthC(int value) {
		this.strengthC = value;
		int a = ((strengthA + strengthB) * (strengthC + 100) / 100) + strengthX;
		this.setStrength(a);
	}

	@Override
	public void setStrengthX(int value) {
		super.setStrengthX(value);
		int a = ((strengthA + strengthB) * (strengthC + 100) / 100) + strengthX;
		this.setStrength(a);
	}

	public void setSpellpowerA(int value) {
		this.spellpowerA = value;
		int a = ((spellpowerA + spellpowerB) * (100 + spellpowerC) / 100) + spellpowerX;
		this.setSpellpower(a);
	}

	public void setSpellpowerB(int value) {
		this.spellpowerB = value;
		int a = ((spellpowerA + spellpowerB) * (100 + spellpowerC) / 100) + spellpowerX;
		this.setSpellpower(a);
	}

	public void setSpellpowerC(int value) {
		this.spellpowerC = value;
		int a = ((spellpowerA + spellpowerB) * (100 + spellpowerC) / 100) + spellpowerX;
		this.setSpellpower(a);
	}

	@Override
	public void setSpellpowerX(int value) {
		super.setSpellpowerX(value);
		int a = ((spellpowerA + spellpowerB) * (100 + spellpowerC) / 100) + spellpowerX;
		this.setSpellpower(a);
	}

	/**
	 * 血上限
	 */
	public void setMaxHPA(int value) {
		this.maxHPA = value;
		this.setMaxHP((int) ((this.maxHPA + this.maxHPB) * (100 + this.maxHPC) / 100 + maxHPX + maxHpY));
	}

	/**
	 * 血上限
	 */
	public void setMaxHPB(int value) {
		this.maxHPB = value;
		this.setMaxHP((int) ((this.maxHPA + this.maxHPB) * (100 + this.maxHPC) / 100 + maxHPX + maxHpY));
	}

	/**
	 * 血上限
	 */
	public void setMaxHPC(double value) {
		this.maxHPC = value;
		this.setMaxHP((int) ((this.maxHPA + this.maxHPB) * (100 + this.maxHPC) / 100 + maxHPX + maxHpY));
		try {
			if (value > 1000) {
				throw new Exception();
			}
		} catch (Exception e) {
			ArticleManager.logger.warn("[血量异常] [" + this.getLogString() + "] [血C ：" + this.maxHPC + "] ", e);
		}
	}

	@Override
	public void setMaxHPX(int value) {
		super.setMaxHPX(value);
		this.setMaxHP((int) ((this.maxHPA + this.maxHPB) * (100 + this.maxHPC) / 100 + maxHPX + maxHpY));
	}

	@Override
	public void setMaxHpY(int value) {
		super.setMaxHpY(value);
		this.setMaxHP((int) ((this.maxHPA + this.maxHPB) * (100 + this.maxHPC) / 100 + maxHPX + maxHpY));
	}

	/**
	 * 蓝上限
	 */
	public void setMaxMPA(int value) {
		this.maxMPA = value;
		this.setMaxMP((this.maxMPA + this.maxMPB) * (100 + this.maxMPC) / 100 + maxMPX);
	}

	/**
	 * 蓝上限
	 */
	public void setMaxMPB(int value) {
		this.maxMPB = value;
		this.setMaxMP((this.maxMPA + this.maxMPB) * (100 + this.maxMPC) / 100 + maxMPX);
	}

	/**
	 * 蓝上限
	 */
	public void setMaxMPC(int value) {
		this.maxMPC = value;
		this.setMaxMP((this.maxMPA + this.maxMPB) * (100 + this.maxMPC) / 100 + maxMPX);
	}

	@Override
	public void setMaxMPX(int value) {
		super.setMaxMPX(value);
		this.setMaxMP((this.maxMPA + this.maxMPB) * (100 + this.maxMPC) / 100 + maxMPX);
	}

	/**
	 * 基础回血
	 */
	public void setHpRecoverBaseA(int value) {
		this.hpRecoverBaseA = value;
		this.setHpRecoverBase((this.hpRecoverBaseA + this.hpRecoverBaseB));
	}

	/**
	 * 基础回血
	 */
	public void setHpRecoverBaseB(int value) {
		this.hpRecoverBaseB = value;
		this.setHpRecoverBase((this.hpRecoverBaseA + this.hpRecoverBaseB));
	}

	/**
	 * 基础回蓝
	 */
	public void setMpRecoverBaseA(int value) {
		this.mpRecoverBaseA = value;
		this.setMpRecoverBase((this.mpRecoverBaseA + this.mpRecoverBaseB));
	}

	/**
	 * 基础回蓝
	 */
	public void setMpRecoverBaseB(int value) {
		this.mpRecoverBaseB = value;
		this.setMpRecoverBase((this.mpRecoverBaseA + this.mpRecoverBaseB));
	}

	/**
	 * 专门为座骑使用的速度，此速度不与人物其他速度叠加
	 */
	protected transient int speedE = 0;

	public void setSpeedE(int value) {
		speedE = value;
		int temp = 0;
		if (this.isShouStatus()) {
			temp = 10;
		}
		this.setSpeed((this.getSpeedA() + this.getSpeedE()) * (100 + this.speedC + temp + this.tempSpeedC) / 100);
		// if (speedE != 0 && this.getHorse() > 0) {
		//
		// this.setSpeed((this.getSpeedA()+this.getSpeedE())*(100 + this.speedC)
		// / 100);
		// // this.setSpeed((this.speedA * (100 + this.speedE) / 100));
		// } else {
		// this.setSpeed((this.speedA * (100 + this.speedC) / 100));
		// }
	}

	public int getSpeedE() {
		return speedE;
	}

	public void setSpeedA(int value) {
		this.speedA = value;
		int temp = 0;
		if (this.isShouStatus()) {
			temp = 10;
		}

		// if (speedE != 0 && this.getHorse() > 0) {
		// this.setSpeed((this.speedA * (100 + this.speedE) / 100));
		// } else {
		this.setSpeed((this.getSpeedA() + this.getSpeedE()) * (100 + this.speedC + temp + this.tempSpeedC) / 100);
		// }

	}

	// 仙婴速度bug修改
	private transient int tempSpeedC = 0;

	public void setSpeedC(int value) {
		if (value != 100000) {
			this.speedC = value;
		}
		int temp = 0;
		if (this.isShouStatus()) {
			temp = 10;
		}

		// if (speedE != 0 && this.getHorse() > 0) {
		// this.setSpeed((this.speedA * (100 + this.speedE) / 100));
		// } else {
		this.setSpeed((this.getSpeedA() + this.getSpeedE()) * (100 + this.speedC + temp + tempSpeedC) / 100);
		// }

	}

	public void setMagicAttackA(int value) {
		this.magicAttackA = value;
		this.setMagicAttack((int) (((this.magicAttackA + magicAttackB) * (100 + this.magicAttackC) / 100) + magicAttackX + magicAttackY + magicAttackZ));

	}

	public void setMagicAttack(int value) {
		value += SkEnhanceManager.getInst().fixMagicAttack(this, value);
		super.setMagicAttack(value);
		if (this.getCareer() == 4) {
			try {
				Skill skill = CareerManager.getInstance().getSkillById(9044);
				SkillInfo si = new SkillInfo();
				si.setInfo(this, skill);
				NEW_QUERY_CAREER_INFO_BY_ID_RES NEW_qciRes = new NEW_QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
				this.addMessageToRightBag(NEW_qciRes);
			} catch (Exception e) {
			}
		}
	}

	public void setMagicAttackB(int value) {
		this.magicAttackB = value;
		this.setMagicAttack((int) (((this.magicAttackA + magicAttackB) * (100 + this.magicAttackC) / 100) + magicAttackX + magicAttackY + magicAttackZ));
	}

	public void setMagicAttackC(double value) {
		this.magicAttackC = value;
		this.setMagicAttack((int) (((this.magicAttackA + magicAttackB) * (100 + this.magicAttackC + extraAttributeAddRates[0]) / 100) + magicAttackX + magicAttackY + magicAttackZ));
		//this.setMagicAttack((int) (((this.magicAttackA + magicAttackB) * (100 + this.magicAttackC) / 100) + magicAttackX + magicAttackY));
	}

	@Override
	public void setMagicAttackX(int value) {
		super.setMagicAttackX(value);
		this.setMagicAttack((int) (((this.magicAttackA + magicAttackB) * (100 + this.magicAttackC) / 100) + magicAttackX + magicAttackY + magicAttackZ));
	}

	@Override
	public void setMagicAttackY(int value) {
		super.setMagicAttackY(value);
		this.setMagicAttack((int) (((this.magicAttackA + magicAttackB) * (100 + this.magicAttackC) / 100) + magicAttackX + magicAttackY + magicAttackZ));
	}
	
	@Override
	public void setMagicAttackZ(int value) {
		super.setMagicAttackZ(value);
		this.setMagicAttack((int) (((this.magicAttackA + magicAttackB) * (100 + this.magicAttackC) / 100) + magicAttackX + magicAttackY + magicAttackZ));
	}

	public void setPhyAttackA(int value) {
		this.phyAttackA = value;
		this.setPhyAttack((int) (((this.phyAttackA + phyAttackB) * (100 + this.phyAttackC) / 100) + phyAttackX + phyAttackY + phyAttackZ));
	}

	public void setPhyAttackB(int value) {
		this.phyAttackB = value;
		this.setPhyAttack((int) (((this.phyAttackA + phyAttackB) * (100 + this.phyAttackC) / 100) + phyAttackX + phyAttackY + phyAttackZ));
	}

	public void setPhyAttackC(double value) {
		this.phyAttackC = value;
		this.setPhyAttack((int) (((this.phyAttackA + phyAttackB) * (100 + this.phyAttackC + extraAttributeAddRates[0]) / 100) + phyAttackX + phyAttackY + phyAttackZ));
		//this.setPhyAttack((int) (((this.phyAttackA + phyAttackB) * (100 + this.phyAttackC) / 100) + phyAttackX + phyAttackY));
	}

	@Override
	public void setPhyAttackX(int value) {
		super.setPhyAttackX(value);
		this.setPhyAttack((int) (((this.phyAttackA + phyAttackB) * (100 + this.phyAttackC) / 100) + phyAttackX + phyAttackY + phyAttackZ));
	}

	@Override
	public void setPhyAttackY(int value) {
		super.setPhyAttackY(value);
		this.setPhyAttack((int) (((this.phyAttackA + phyAttackB) * (100 + this.phyAttackC) / 100) + phyAttackX + phyAttackY + phyAttackZ));
	}
	
	@Override
	public void setPhyAttackZ(int value) {
		super.setPhyAttackZ(value);
		this.setPhyAttack((int) (((this.phyAttackA + phyAttackB) * (100 + this.phyAttackC) / 100) + phyAttackX + phyAttackY + phyAttackZ));
	}
	
	

	public void setPhyAttack(int value) {
		value += SkEnhanceManager.getInst().morePhyAttack(this, value);
		super.setPhyAttack(value);
		setPhysicalDamageUpperLimit((phyAttack * this.commonAttackSpeed / 15000 + this.weaponDamageUpperLimit + 15));
		setPhysicalDamageLowerLimit((phyAttack * this.commonAttackSpeed / 15000 + this.weaponDamageLowerLimit + 15));

	}

	public void setCommonAttackSpeed(int s) {
		super.setCommonAttackSpeed(s);
		setPhysicalDamageUpperLimit((phyAttack * this.commonAttackSpeed / 15000 + this.weaponDamageUpperLimit + 15));
		setPhysicalDamageLowerLimit((phyAttack * this.commonAttackSpeed / 15000 + this.weaponDamageLowerLimit + 15));
	}

	public void setWeaponDamageUpperLimit(int s) {
		super.setWeaponDamageUpperLimit(s);
		setPhysicalDamageUpperLimit((phyAttack * this.commonAttackSpeed / 15000 + this.weaponDamageUpperLimit + 15));
	}

	public void setWeaponDamageLowerLimit(int s) {
		super.setWeaponDamageLowerLimit(s);
		setPhysicalDamageLowerLimit((phyAttack * this.commonAttackSpeed / 15000 + this.weaponDamageLowerLimit + 15));
	}

	public void setPhyDefence(int value) {
		value += SkEnhanceManager.getInst().fixPhyDefence(this, value);
		super.setPhyDefence(value);
		int r = CombatCaculator.CAL_物理减伤率(value, level, career);
		this.setPhysicalDecrease(r);
	}

	@Override
	public void setPhyDefenceX(int value) {
		super.setPhyDefenceX(value);
		int a = (int) (((phyDefenceA + phyDefenceB) * (phyDefenceC + 100) / 100) + phyDefenceX + phyDefenceY + phyDefenceZ);
		this.setPhyDefence(a);
	}

	@Override
	public void setPhyDefenceY(int value) {
		super.setPhyDefenceY(value);
		int a = (int) (((phyDefenceA + phyDefenceB) * (phyDefenceC + 100) / 100) + phyDefenceX + phyDefenceY + phyDefenceZ);
		this.setPhyDefence(a);
	}
	
	@Override
	public void setPhyDefenceZ(int value) {
		super.setPhyDefenceZ(value);
		int a = (int) (((phyDefenceA + phyDefenceB) * (phyDefenceC + 100) / 100) + phyDefenceX + phyDefenceY + phyDefenceZ);
		this.setPhyDefence(a);
	}

	/**
	 * 物理防御
	 */
	public void setPhyDefenceA(int value) {
		this.phyDefenceA = value;
		int a = (int) (((phyDefenceA + phyDefenceB) * (phyDefenceC + 100) / 100) + phyDefenceX + phyDefenceY + phyDefenceZ);
		this.setPhyDefence(a);
	}

	/**
	 * 物理防御
	 */
	public void setPhyDefenceB(int value) {
		this.phyDefenceB = value;
		int a = (int) (((phyDefenceA + phyDefenceB) * (phyDefenceC + 100) / 100) + phyDefenceX + phyDefenceY + phyDefenceZ);
		this.setPhyDefence(a);
	}

	/**
	 * 物理防御
	 */
	public void setPhyDefenceC(double value) {
		this.phyDefenceC = value;
		int a = (int) (((phyDefenceA + phyDefenceB) * (phyDefenceC + 100) / 100) + phyDefenceX + phyDefenceY + phyDefenceZ);
		this.setPhyDefence(a);
	}

	public void setMagicDefence(int value) {
		value += SkEnhanceManager.getInst().fixMagicDefane(this, value);
		super.setMagicDefence(value);
		int r = CombatCaculator.CAL_法术减伤率(value, level, career);
		this.setSpellDecrease(r);
	}

	@Override
	public void setMagicDefenceX(int value) {
		super.setMagicDefenceX(value);
		int a = (int) (((magicDefenceA + magicDefenceB) * (magicDefenceC + 100) / 100) + magicDefenceX + magicDefenceY + magicDefenceZ);
		this.setMagicDefence(a);
	}

	@Override
	public void setMagicDefenceY(int value) {
		super.setMagicDefenceY(value);
		int a = (int) (((magicDefenceA + magicDefenceB) * (magicDefenceC + 100) / 100) + magicDefenceX + magicDefenceY + magicDefenceZ);
		this.setMagicDefence(a);
	}
	
	@Override
	public void setMagicDefenceZ(int value) {
		super.setMagicDefenceZ(value);
		int a = (int) (((magicDefenceA + magicDefenceB) * (magicDefenceC + 100) / 100) + magicDefenceX + magicDefenceY + magicDefenceZ);
		this.setMagicDefence(a);
	}

	/**
	 * 魔法防御
	 */
	public void setMagicDefenceA(int value) {
		this.magicDefenceA = value;
		int a = (int) (((magicDefenceA + magicDefenceB) * (magicDefenceC + 100) / 100) + magicDefenceX + magicDefenceY + magicDefenceZ);
		this.setMagicDefence(a);
	}

	/**
	 * 魔法防御
	 */
	public void setMagicDefenceB(int value) {
		this.magicDefenceB = value;
		int a = (int) (((magicDefenceA + magicDefenceB) * (magicDefenceC + 100) / 100) + magicDefenceX + magicDefenceY + magicDefenceZ);
		this.setMagicDefence(a);
	}

	/**
	 * 魔法防御
	 */
	public void setMagicDefenceC(double value) {
		this.magicDefenceC = value;
		int a = (int) (((magicDefenceA + magicDefenceB) * (magicDefenceC + 100) / 100) + magicDefenceX + magicDefenceY + magicDefenceZ);
		this.setMagicDefence(a);
	}

	public void modifyAttrRate() {
		this.setMagicDefence(this.getMagicDefence());
		this.setPhyDefence(this.getPhyDefence());
		this.setBreakDefence(this.getBreakDefence());
		this.setAccurate(this.getAccurate());
		this.setCriticalDefence(this.getCriticalDefence());
		this.setThunderDefence(this.getThunderDefence());
		this.setThunderIgnoreDefence(this.getThunderIgnoreDefence());

		this.setBlizzardDefence(this.getBlizzardDefence());
		this.setBlizzardIgnoreDefence(this.getBlizzardIgnoreDefence());

		this.setWindDefence(this.getWindDefence());
		this.setWindIgnoreDefence(this.getWindIgnoreDefence());

		this.setFireDefence(this.getFireDefence());
		this.setFireIgnoreDefence(this.getFireIgnoreDefence());
	}

	/**
	 * 诸多战斗属性，重写父类战斗属性方法
	 */

	@Override
	public void setAccurate(int value) {
		super.setAccurate(value);
		int a = CombatCaculator.CAL_精准率(value, level);
		this.setAccurateRate(a);
	}

	@Override
	public void setAccurateA(int value) {
		super.setAccurateA(value);
		this.setAccurate((int) ((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100 + this.accurateX + this.accurateY));
	}

	@Override
	public void setAccurateB(int value) {
		super.setAccurateB(value);
		this.setAccurate((int) ((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100 + this.accurateX + this.accurateY));
	}

	@Override
	public void setAccurateC(double value) {
		super.setAccurateC(value);
		this.setAccurate((int) ((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100 + this.accurateX + this.accurateY));
	}

	@Override
	public void setAccurateX(int value) {
		super.setAccurateX(value);
		this.setAccurate((int) ((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100 + this.accurateX + this.accurateY));
	}

	@Override
	public void setAccurateY(int value) {
		super.setAccurateY(value);
		this.setAccurate((int) ((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100 + this.accurateX + this.accurateY));
	}

	@Override
	public void setBreakDefence(int value) {
		super.setBreakDefence(value);
		int bd = CombatCaculator.CAL_破防率(value, level) + getBreakDefenceRateOther();
		this.setBreakDefenceRate(bd);
	}

	@Override
	public void setBreakDefenceA(int value) {
		super.setBreakDefenceA(value);
		this.setBreakDefence((int) ((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100 + this.breakDefenceX + this.breakDefenceY));
	}

	@Override
	public void setBreakDefenceB(int value) {
		super.setBreakDefenceB(value);
		this.setBreakDefence((int) ((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100 + this.breakDefenceX + this.breakDefenceY));
	}

	@Override
	public void setBreakDefenceC(double value) {
		super.setBreakDefenceC(value);
		this.setBreakDefence((int) ((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100 + this.breakDefenceX + this.breakDefenceY));
	}

	@Override
	public void setBreakDefenceX(int value) {
		super.setBreakDefenceX(value);
		this.setBreakDefence((int) ((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100 + this.breakDefenceX + this.breakDefenceY));
	}

	@Override
	public void setBreakDefenceY(int value) {
		super.setBreakDefenceY(value);
		this.setBreakDefence((int) ((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100 + this.breakDefenceX + this.breakDefenceY));
	}

	@Override
	public void setCriticalDefence(int value) {
		super.setCriticalDefence(value);
		int cd = CombatCaculator.CAL_会心防御率(value, level);
		this.setCriticalDefenceRate(cd);
	}

	@Override
	public void setCriticalDefenceA(int value) {
		super.setCriticalDefenceA(value);
		this.setCriticalDefence((int) ((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100 + this.criticalDefenceX));
	}

	@Override
	public void setCriticalDefenceB(int value) {
		super.setCriticalDefenceB(value);
		this.setCriticalDefence((int) ((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100 + this.criticalDefenceX));
	}

	@Override
	public void setCriticalDefenceC(double value) {
		super.setCriticalDefenceC(value);
		this.setCriticalDefence((int) ((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100 + this.criticalDefenceX));
	}

	@Override
	public void setCriticalDefenceX(int value) {
		super.setCriticalDefenceX(value);
		this.setCriticalDefence((int) ((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100 + this.criticalDefenceX));
	}

	@Override
	public void setCriticalHitA(int value) {
		super.setCriticalHitA(value);
		this.setCriticalHit((int) ((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100 + this.criticalHitX + this.criticalHitY + this.criticalHitZ));
	}

	@Override
	public void setCriticalHitB(int value) {
		super.setCriticalHitB(value);
		this.setCriticalHit((int) ((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100 + this.criticalHitX + this.criticalHitY + this.criticalHitZ));
	}

	@Override
	public void setCriticalHitC(double value) {
		super.setCriticalHitC(value);
		this.setCriticalHit((int) ((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100 + this.criticalHitX + this.criticalHitY + this.criticalHitZ));
	}

	@Override
	public void setCriticalHitX(int value) {
		super.setCriticalHitX(value);
		this.setCriticalHit((int) ((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100 + this.criticalHitX + this.criticalHitY+ this.criticalHitZ));
	}

	@Override
	public void setCriticalHitY(int value) {
		super.setCriticalHitY(value);
		this.setCriticalHit((int) ((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100 + this.criticalHitX + this.criticalHitY+ this.criticalHitZ));
	}
	
	@Override
	public void setCriticalHitZ(int value) {
		super.setCriticalHitZ(value);
		this.setCriticalHit((int) ((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100 + this.criticalHitX + this.criticalHitY+ this.criticalHitZ));
	}

	public void setDodge(int value) {
		super.setDodge(value);
		int dodge = CombatCaculator.CAL_闪避率(value, level, career) + getDodgeRateOther();
		this.setDodgeRate(dodge);
	}

	@Override
	public void setDodgeA(int value) {
		super.setDodgeA(value);
		this.setDodge((int) ((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100 + dodgeX + dodgeZ));
	}

	@Override
	public void setDodgeB(int value) {
		super.setDodgeB(value);
		this.setDodge((int) ((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100 + dodgeX + dodgeZ));
	}

	@Override
	public void setDodgeC(double value) {
		super.setDodgeC(value);
		this.setDodge((int) ((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100 + dodgeX + dodgeZ));
	}

	@Override
	public void setDodgeX(int value) {
		super.setDodgeX(value);
		this.setDodge((int) ((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100 + dodgeX + dodgeZ));
	}
	
	@Override
	public void setDodgeZ(int value) {
		super.setDodgeZ(value);
		this.setDodge((int) ((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100 + dodgeX + dodgeZ));
	}

	public void setHit(int value) {
		super.setHit(value);
		int hitrate = CombatCaculator.CAL_命中率(value, level) + getHitRateOther();
		this.setHitRate(hitrate);
	}

	@Override
	public void setHitA(int value) {
		super.setHitA(value);
		this.setHit((int) ((this.hitA + this.hitB) * (100 + this.hitC) / 100 + this.hitX + this.hitY + this.hitZ));
	}

	@Override
	public void setHitB(int value) {
		super.setHitB(value);
		this.setHit((int) ((this.hitA + this.hitB) * (100 + this.hitC) / 100 + this.hitX + this.hitY + this.hitZ));
	}

	@Override
	public void setHitC(double value) {
		super.setHitC(value);
		this.setHit((int) ((this.hitA + this.hitB) * (100 + this.hitC) / 100 + this.hitX + this.hitY + this.hitZ));
	}

	@Override
	public void setHitX(int value) {
		super.setHitX(value);
		this.setHit((int) ((this.hitA + this.hitB) * (100 + this.hitC) / 100 + this.hitX + this.hitY + this.hitZ));
	}

	@Override
	public void setHitY(int value) {
		super.setHitY(value);
		this.setHit((int) ((this.hitA + this.hitB) * (100 + this.hitC) / 100 + this.hitX + this.hitY + this.hitZ));
	}
	
	@Override
	public void setHitZ(int value) {
		super.setHitZ(value);
		this.setHit((int) ((this.hitA + this.hitB) * (100 + this.hitC) / 100 + this.hitX + this.hitY + this.hitZ));
	}

	@Override
	public void setBlizzardDefence(int value) {
		super.setBlizzardDefence(value);
		int blizzardDefence = CombatCaculator.CAL_冰属性抗率(value, level, career) + getBlizzardDefenceRateOther();
		this.setBlizzardDefenceRate(blizzardDefence);
	}

	@Override
	public void setBlizzardDefenceA(int value) {
		super.setBlizzardDefenceA(value);
		this.setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100 + this.blizzardDefenceX);
	}

	@Override
	public void setBlizzardDefenceB(int value) {
		super.setBlizzardDefenceB(value);
		this.setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100 + this.blizzardDefenceX);
	}

	@Override
	public void setBlizzardDefenceC(int value) {
		super.setBlizzardDefenceC(value);
		this.setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100 + this.blizzardDefenceX);
	}

	@Override
	public void setBlizzardDefenceX(int value) {
		super.setBlizzardDefenceX(value);
		this.setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100 + this.blizzardDefenceX);
	}

	@Override
	public void setBlizzardIgnoreDefence(int value) {
		super.setBlizzardIgnoreDefence(value);
		int blizzardIgnoreDefence = CombatCaculator.CAL_冰属性减抗率(value, level) + getBlizzardIgnoreDefenceRateOther();
		this.setBlizzardIgnoreDefenceRate(blizzardIgnoreDefence);
	}

	@Override
	public void setBlizzardIgnoreDefenceA(int value) {
		super.setBlizzardIgnoreDefenceA(value);
		this.setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100 + this.blizzardIgnoreDefenceX);
	}

	@Override
	public void setBlizzardIgnoreDefenceB(int value) {
		super.setBlizzardIgnoreDefenceB(value);
		this.setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100 + this.blizzardIgnoreDefenceX);
	}

	@Override
	public void setBlizzardIgnoreDefenceC(int value) {
		super.setBlizzardIgnoreDefenceC(value);
		this.setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100 + this.blizzardIgnoreDefenceX);
	}

	@Override
	public void setBlizzardIgnoreDefenceX(int value) {
		super.setBlizzardIgnoreDefenceX(value);
		this.setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100 + this.blizzardIgnoreDefenceX);
	}

	@Override
	public void setFireDefence(int value) {
		super.setFireDefence(value);
		int fireDefence = CombatCaculator.CAL_火属性抗率(value, level, career) + getFireDefenceRateOther();
		this.setFireDefenceRate(fireDefence);
	}

	@Override
	public void setFireDefenceA(int value) {
		super.setFireDefenceA(value);
		this.setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100 + this.fireDefenceX);
	}

	@Override
	public void setFireDefenceB(int value) {
		super.setFireDefenceB(value);
		this.setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100 + this.fireDefenceX);
	}

	@Override
	public void setFireDefenceC(int value) {
		super.setFireDefenceC(value);
		this.setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100 + this.fireDefenceX);
	}

	@Override
	public void setFireDefenceX(int value) {
		super.setFireDefenceX(value);
		this.setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100 + this.fireDefenceX);
	}

	@Override
	public void setFireIgnoreDefence(int value) {
		super.setFireIgnoreDefence(value);
		int fireIgnoreDefence = CombatCaculator.CAL_火属性减抗率(value, level) + getFireIgnoreDefenceRateOther();
		this.setFireIgnoreDefenceRate(fireIgnoreDefence);
	}

	@Override
	public void setFireIgnoreDefenceA(int value) {
		super.setFireIgnoreDefenceA(value);
		this.setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100 + this.fireIgnoreDefenceX);
	}

	@Override
	public void setFireIgnoreDefenceB(int value) {
		super.setFireIgnoreDefenceB(value);
		this.setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100 + this.fireIgnoreDefenceX);
	}

	@Override
	public void setFireIgnoreDefenceC(int value) {
		super.setFireIgnoreDefenceC(value);
		this.setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100 + this.fireIgnoreDefenceX);
	}

	@Override
	public void setFireIgnoreDefenceX(int value) {
		super.setFireIgnoreDefenceX(value);
		this.setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100 + this.fireIgnoreDefenceX);
	}

	@Override
	public void setThunderDefence(int value) {
		super.setThunderDefence(value);
		int thunderDefence = CombatCaculator.CAL_雷属性抗率(value, level, career) + getThunderDefenceRateOther();
		this.setThunderDefenceRate(thunderDefence);
	}

	@Override
	public void setThunderDefenceA(int value) {
		super.setThunderDefenceA(value);
		this.setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100 + this.thunderDefenceX);
	}

	@Override
	public void setThunderDefenceB(int value) {
		super.setThunderDefenceB(value);
		this.setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100 + this.thunderDefenceX);
	}

	@Override
	public void setThunderDefenceC(int value) {
		super.setThunderDefenceC(value);
		this.setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100 + this.thunderDefenceX);
	}

	@Override
	public void setThunderDefenceX(int value) {
		super.setThunderDefenceX(value);
		this.setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100 + this.thunderDefenceX);
	}

	@Override
	public void setThunderIgnoreDefence(int value) {
		super.setThunderIgnoreDefence(value);
		int thunderIgnoreDefence = CombatCaculator.CAL_雷属性减抗率(value, level) + getThunderIgnoreDefenceRateOther();
		this.setThunderIgnoreDefenceRate(thunderIgnoreDefence);
	}

	@Override
	public void setThunderIgnoreDefenceA(int value) {
		super.setThunderIgnoreDefenceA(value);
		this.setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100 + this.thunderIgnoreDefenceX);
	}

	@Override
	public void setThunderIgnoreDefenceB(int value) {
		super.setThunderIgnoreDefenceB(value);
		this.setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100 + this.thunderIgnoreDefenceX);
	}

	@Override
	public void setThunderIgnoreDefenceC(int value) {
		super.setThunderIgnoreDefenceC(value);
		this.setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100 + this.thunderIgnoreDefenceX);
	}

	@Override
	public void setThunderIgnoreDefenceX(int value) {
		super.setThunderIgnoreDefenceX(value);
		this.setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100 + this.thunderIgnoreDefenceX);
	}

	@Override
	public void setWindDefence(int value) {
		super.setWindDefence(value);
		int windDefence = CombatCaculator.CAL_风属性抗率(value, level, career) + getWindDefenceRateOther();
		this.setWindDefenceRate(windDefence);
	}

	@Override
	public void setWindDefenceA(int value) {
		super.setWindDefenceA(value);
		this.setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100 + this.windDefenceX);
	}

	@Override
	public void setWindDefenceB(int value) {
		super.setWindDefenceB(value);
		this.setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100 + this.windDefenceX);
	}

	@Override
	public void setWindDefenceC(int value) {
		super.setWindDefenceC(value);
		this.setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100 + this.windDefenceX);
	}

	@Override
	public void setWindDefenceX(int value) {
		super.setWindDefenceX(value);
		this.setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100 + this.windDefenceX);
	}

	@Override
	public void setWindIgnoreDefence(int value) {
		super.setWindIgnoreDefence(value);
		int windIgnoreDefence = CombatCaculator.CAL_风属性减抗率(value, level) + getWindIgnoreDefenceRateOther();
		this.setWindIgnoreDefenceRate(windIgnoreDefence);
	}

	@Override
	public void setWindIgnoreDefenceA(int value) {
		super.setWindIgnoreDefenceA(value);
		this.setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100 + this.windIgnoreDefenceX);
	}

	@Override
	public void setWindIgnoreDefenceB(int value) {
		super.setWindIgnoreDefenceB(value);
		this.setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100 + this.windIgnoreDefenceX);
	}

	@Override
	public void setWindIgnoreDefenceC(int value) {
		super.setWindIgnoreDefenceC(value);
		this.setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100 + this.windIgnoreDefenceX);
	}

	@Override
	public void setWindIgnoreDefenceX(int value) {
		super.setWindIgnoreDefenceX(value);
		this.setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100 + this.windIgnoreDefenceX);
	}

	@Override
	public void setBlizzardAttackA(int value) {
		super.setBlizzardAttackA(value);
		this.setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100 + this.blizzardAttackX);
	}

	@Override
	public void setBlizzardAttackB(int value) {
		super.setBlizzardAttackB(value);
		this.setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100 + this.blizzardAttackX);
	}

	@Override
	public void setBlizzardAttackC(int value) {
		super.setBlizzardAttackC(value);
//		this.setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100 + this.blizzardAttackX);
		this.setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC + extraAttributeAddRates[4]) / 100 + this.blizzardAttackX);
	}

	@Override
	public void setBlizzardAttackX(int value) {
		super.setBlizzardAttackX(value);
		this.setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100 + this.blizzardAttackX);
	}

	@Override
	public void setDexterityA(int value) {
		// super.setDexterityA(value);
		// this.setDexterity((this.dexterityA + this.dexterityB) * (100 +
		// this.dexterityC) / 100 + this.dexterityX);
	}

	@Override
	public void setDexterityB(int value) {
		// super.setDexterityB(value);
		// this.setDexterity((this.dexterityA + this.dexterityB) * (100 +
		// this.dexterityC) / 100 + this.dexterityX);
	}

	@Override
	public void setDexterityC(int value) {
		// super.setDexterityC(value);
		// this.setDexterity((this.dexterityA + this.dexterityB) * (100 +
		// this.dexterityC) / 100 + this.dexterityX);
	}

	@Override
	public void setDexterityX(int value) {
		// super.setDexterityX(value);
		// this.setDexterity((this.dexterityA + this.dexterityB) * (100 +
		// this.dexterityC) / 100 + this.dexterityX);
	}

	@Override
	public void setFireAttackA(int value) {
		super.setFireAttackA(value);
		this.setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100 + this.fireAttackX);
	}

	@Override
	public void setFireAttackB(int value) {
		super.setFireAttackB(value);
		this.setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100 + this.fireAttackX);
	}

	@Override
	public void setFireAttackC(int value) {
		super.setFireAttackC(value);
//		this.setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100 + this.fireAttackX);
		this.setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC + extraAttributeAddRates[3]) / 100 + this.fireAttackX);
	}

	@Override
	public void setFireAttackX(int value) {
		super.setFireAttackX(value);
		this.setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100 + this.fireAttackX);
	}

	@Override
	public void setThunderAttackA(int value) {
		super.setThunderAttackA(value);
		this.setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100 + this.thunderAttackX);
	}

	@Override
	public void setThunderAttackB(int value) {
		super.setThunderAttackB(value);
		this.setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100 + this.thunderAttackX);
	}

	@Override
	public void setThunderAttackC(int value) {
		super.setThunderAttackC(value);
//		this.setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100 + this.thunderAttackX);
		this.setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC + extraAttributeAddRates[1]) / 100 + this.thunderAttackX);
	}

	@Override
	public void setThunderAttackX(int value) {
		super.setThunderAttackX(value);
		this.setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100 + this.thunderAttackX);
	}

	@Override
	public void setWindAttackA(int value) {
		super.setWindAttackA(value);
		this.setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100 + this.windAttackX);
	}

	@Override
	public void setWindAttackB(int value) {
		super.setWindAttackB(value);
		this.setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100 + this.windAttackX);
	}

	@Override
	public void setWindAttackC(int value) {
		super.setWindAttackC(value);
//		this.setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100 + this.windAttackX);
		this.setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC + extraAttributeAddRates[2]) / 100 + this.windAttackX);
	}

	@Override
	public void setWindAttackX(int value) {
		super.setWindAttackX(value);
		this.setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100 + this.windAttackX);
	}

	@Override
	public void setConstitutionA(int value) {
		super.setConstitutionA(value);
		this.setConstitution((this.constitutionA + this.constitutionB) * (100 + this.constitutionC) / 100 + this.constitutionX);
	}

	@Override
	public void setConstitutionB(int value) {
		super.setConstitutionB(value);
		this.setConstitution((this.constitutionA + this.constitutionB) * (100 + this.constitutionC) / 100 + this.constitutionX);
	}

	@Override
	public void setConstitutionC(int value) {
		super.setConstitutionC(value);
		this.setConstitution((this.constitutionA + this.constitutionB) * (100 + this.constitutionC) / 100 + this.constitutionX);
	}

	@Override
	public void setConstitutionX(int value) {
		super.setConstitutionX(value);
		this.setConstitution((this.constitutionA + this.constitutionB) * (100 + this.constitutionC) / 100 + this.constitutionX);
	}

	private long lastAcceptChuangongArticle = 0; // 领取传功石时间
	private int chuangongNum = 0; // 一天可传三次
	private long lastChuangongTime = 0;
	private long lastAcceptChuangongTime = 0; // 一天只能接受传功一次
	private transient boolean chuangonging = false; // 正在传功

	public boolean isChuangonging() {
		return chuangonging;
	}

	public void setChuangonging(boolean chuangonging) {
		this.chuangonging = chuangonging;
	}

	public long getLastAcceptChuangongArticle() {
		return lastAcceptChuangongArticle;
	}

	public void setLastAcceptChuangongArticle(long lastAcceptChuangongArticle) {
		this.lastAcceptChuangongArticle = lastAcceptChuangongArticle;
		setDirty(true, "lastAcceptChuangongArticle");
	}

	public int getChuangongNum() {
		return chuangongNum;
	}

	public void setChuangongNum(int chuangongNum) {
		this.chuangongNum = chuangongNum;
		setDirty(true, "chuangongNum");
	}

	public long getLastChuangongTime() {
		return lastChuangongTime;
	}

	public void setLastChuangongTime(long lastChuangongTime) {
		this.lastChuangongTime = lastChuangongTime;
		setDirty(true, "lastChuangongTime");
	}

	public long getLastAcceptChuangongTime() {
		return lastAcceptChuangongTime;
	}

	public void setLastAcceptChuangongTime(long lastAcceptChuangongTime) {
		this.lastAcceptChuangongTime = lastAcceptChuangongTime;
		setDirty(true, "lastAcceptChuangongTime");
	}

	@SimpleColumn(length = 12000)
	private List<PlayerTitle> playerTitles;

	private transient List<PlayerTitle> needRemoveTitles = new ArrayList<PlayerTitle>();

	public List<PlayerTitle> getPlayerTitles() {
		return playerTitles;
	}

	public void setPlayerTitles(List<PlayerTitle> playerTitles) {
		this.playerTitles = playerTitles;
		this.setDirty(true, "playerTitles");
	}

	private int defaultTitleType = -1;

	public int getDefaultTitleType() {
		return defaultTitleType;
	}

	public void setDefaultTitleType(int defaultTitleType) {
		this.defaultTitleType = defaultTitleType;
		setDirty(true, "defaultTitleType");
	}

	private void initPersonnalTitle() {
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方) && playerTitles != null) {
			for (PlayerTitle pt : playerTitles) {
				if (pt.getTitleName().equalsIgnoreCase("青龙圣坛使者") && pt.getTitleType() != 260) {
					int oldType = pt.getTitleType();
					pt.setTitleType(260);
					ActivitySubSystem.logger.warn(this.getLogString() + " [更新称号] [name:" + pt.getTitleName() + "] [oldType:" + oldType + "] [newtype:" + pt.getTitleType() + "]");
				} else if (pt.getTitleName().equalsIgnoreCase("白虎圣坛使者") && pt.getTitleType() != 261) {
					int oldType = pt.getTitleType();
					pt.setTitleType(261);
					ActivitySubSystem.logger.warn(this.getLogString() + " [更新称号] [name:" + pt.getTitleName() + "] [oldType:" + oldType + "] [newtype:" + pt.getTitleType() + "]");
				} else if (pt.getTitleName().equalsIgnoreCase("朱雀圣坛使者") && pt.getTitleType() != 262) {
					int oldType = pt.getTitleType();
					pt.setTitleType(262);
					Buff bf = this.getBuffByName("天呐我太可爱了");
					if (bf != null) {
						bf.setInvalidTime(0);
					}
					ActivitySubSystem.logger.warn(this.getLogString() + " [更新称号] [name:" + pt.getTitleName() + "] [oldType:" + oldType + "] [newtype:" + pt.getTitleType() + "]");
				}
			}
			this.setPlayerTitles(playerTitles);
		}
		if (defaultTitleType > 0) {
			if (playerTitles != null) {
				if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
					for (PlayerTitle pt : playerTitles) {
						String titleName = PlayerTitlesManager.getInstance().getTitleName(pt.getTitleType());
						String oldName = pt.getTitleName();
						if (oldName != null && titleName != null && !oldName.equals(titleName)) {
							pt.setTitleName(titleName);
							ActivitySubSystem.logger.warn(this.getLogString() + " [更新称号] [oldName:" + oldName + "] [更新后:" + titleName + "] [" + pt.getTitleType() + "]");
						}
					}
				}
				try {
					if (playerTitles.size() >= 13) {
						if (PlayerAimManager.instance.gethasTitleNum(playerTitles, PlayerAimManager.称号成就1) >= 17) {
							AchievementManager.getInstance().record(this, RecordAction.集齐20个称号1);
						}
						// if (PlayerAimManager.instance.hasAllTitle(playerTitles, PlayerAimManager.称号成就1)) {
						// AchievementManager.getInstance().record(this, RecordAction.集齐20个称号1);
						// }
						if (PlayerAimManager.instance.gethasTitleNum(playerTitles, PlayerAimManager.称号成就2) >= 22) {
							AchievementManager.getInstance().record(this, RecordAction.集齐20个称号2);
						}
						if (PlayerAimManager.instance.hasAllTitle(playerTitles, PlayerAimManager.称号成就3)) {
							AchievementManager.getInstance().record(this, RecordAction.集齐13个称号);
						}
					}
				} catch (Exception e) {
					PlayerAimManager.logger.error("[目标系统] [玩家init检测称号成就] [异常] [" + this.getLogString() + "]", e);
				}
				// 做特殊处理。把九游精英玩家替换为飘渺寻仙曲精英玩家
				for (PlayerTitle pt : playerTitles) {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
						if (pt.getTitleType() == 101) {
							pt.setTitleName("飘渺寻仙曲精英玩家");
							this.setPlayerTitles(this.getPlayerTitles());
							ActivitySubSystem.logger.warn(this.getLogString() + " [更新称号] ");
						}
					}
					if (pt.getTitleType() == defaultTitleType) {
						setPersonTitle(defaultTitleType);
						if (GamePlayerManager.logger.isInfoEnabled()) {
							GamePlayerManager.logger.info("[玩家初始化称号成功] [" + this.getLogString() + "] [类型:" + defaultTitleType + "]");
						}
						return;
					}
				}
			}
		} else {
			setTitle("");
		}
	}

	private transient PlayerActivenessInfo activenessInfo;

	public PlayerActivenessInfo getActivenessInfo() {
		return activenessInfo;
	}

	public void setActivenessInfo(PlayerActivenessInfo activenessInfo) {
		this.activenessInfo = activenessInfo;
	}

	@SimpleColumn(length = 12000)
	private Map<Long, DigTemplate> digInfo;

	public Map<Long, DigTemplate> getDigInfo() {
		return digInfo;
	}

	public void setDigInfo(Map<Long, DigTemplate> digInfo) {
		this.digInfo = digInfo;
		setDirty(true, "digInfo");
	}

	private List<String> usedNameList;

	public List<String> getUsedNameList() {
		return usedNameList;
	}

	public void setUsedNameList(List<String> usedNameList) {
		this.usedNameList = usedNameList;
	}

	private long lastGaiMingTime;

	public long getLastGaiMingTime() {
		return lastGaiMingTime;
	}

	public void setLastGaiMingTime(long lastGaiMingTime) {
		this.lastGaiMingTime = lastGaiMingTime;
	}

	/**
	 * 命格镶嵌面板
	 */
	private transient HuntLifeEntity huntLifr;
	/**
	 * 玩家灵根
	 */
	private transient SoulPithEntity soulPith;

	/**
	 * 增加个人称号
	 * 
	 * @param type
	 * @param title
	 */
	public void addTitle(int type, String title, int color, String buffName, int bufflevl, String titleShowName, String description, String icon, long lastTime) {
		if (color < 0) {
			color = 0;
		}
		addTitle(type, title, color, true, buffName, bufflevl, titleShowName, description, icon, lastTime);
	}

	public void addTitle(int type, String title, int color, boolean useAtonce, String buffName, int buffLevl, String titleShowName, String description, String icon, long lastTime) {

		// this.sendError("获得称号" + title);
		this.sendError(translateString(获得称号xx, new String[][] { { STRING_1, title } }));
		if (this.getDefaultTitleType() == type) {
			if (titleShowName != null && !titleShowName.isEmpty()) {
				setTitle(titleShowName);
			} else {
				setTitle(title);
			}
			if (SocialManager.logger.isWarnEnabled()) {
				SocialManager.logger.warn("[玩家设置称号成功] [跟默认称号一样] [" + this.getLogString() + "] [title:" + title + "]");
			}
		}
		if (playerTitles == null) playerTitles = new ArrayList<PlayerTitle>();

		if (playerTitles.size() == 0) {
			playerTitles.add(new PlayerTitle(-1, "", -1L, 0, "", 0, "", "", "", 0L));
		}
		boolean bln = true;
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		for (PlayerTitle pt : playerTitles) {
			if (pt.getTitleType() == type) {
				pt.setTitleName(title);
				// 如果称号带有效期，则更新一下有效期（如果要分类改，请修改这里）
				if (lastTime > 0) {
					pt.setStartTime(startTime);
					pt.setLastTime(lastTime);
				}
				bln = false;
				break;
			}
		}
		if (bln) {
			PlayerTitle pt = new PlayerTitle(type, title, startTime, color, buffName, buffLevl, titleShowName, description, icon, lastTime);
			playerTitles.add(pt);
			try {
				if (playerTitles.size() >= 13) {
					// if (PlayerAimManager.instance.hasAllTitle(playerTitles, PlayerAimManager.称号成就1)) {
					// AchievementManager.getInstance().record(this, RecordAction.集齐20个称号1);
					// }
					if (PlayerAimManager.instance.gethasTitleNum(playerTitles, PlayerAimManager.称号成就1) >= 17) {
						AchievementManager.getInstance().record(this, RecordAction.集齐20个称号1);
					}
					if (PlayerAimManager.instance.gethasTitleNum(playerTitles, PlayerAimManager.称号成就2) >= 22) {
						AchievementManager.getInstance().record(this, RecordAction.集齐20个称号2);
					}
					if (PlayerAimManager.instance.hasAllTitle(playerTitles, PlayerAimManager.称号成就3)) {
						AchievementManager.getInstance().record(this, RecordAction.集齐13个称号);
					}
				}
			} catch (Exception e) {
				PlayerAimManager.logger.error("[目标系统] [玩家init检测称号成就] [异常] [" + this.getLogString() + "]", e);
			}
		}
		if (useAtonce) {
			setPersonTitle(type);
		}
		setPlayerTitles(playerTitles);
		try {
			if (playerTitles != null && playerTitles.size() > 0) {
				RecordAction rac = PlayerAimManager.instance.getTitleAction(playerTitles);
				if (rac != null) {
					AchievementManager.getInstance().record(this, rac);
				}
			}
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计玩家获得称号成就] [异常] [" + this.getLogString() + "]", e);
		}
	}

	/**
	 * 删除个人称号
	 * 
	 * @param type
	 * @param title
	 */
	public boolean removePersonTitle(int type) {
		if (this.getDefaultTitleType() == type) {
			this.setDefaultTitleType(-1);
			setTitle("");
			if (this.getTitleBuffName() != null && !this.getTitleBuffName().isEmpty()) { // 清楚称号buff
				Buff titleBuff = this.getBuffByName(this.getTitleBuffName());
				if (titleBuff != null) {
					titleBuff.setInvalidTime(0);
					this.setTitleBuffName("");
				} else if (SocialManager.logger.isWarnEnabled()) {
					SocialManager.logger.warn("[清除原有称号buff失败] [" + this.getLogString() + "] [buff名:" + this.getTitleBuffName() + "]");
				}
			}
		}
		if (playerTitles != null) {
			int max = playerTitles.size();
			for (int i = 0; i < max; i++) {
				if (playerTitles.get(i).getTitleType() == type) {
					playerTitles.remove(i);
					setPlayerTitles(playerTitles);
					if (SocialManager.logger.isWarnEnabled()) {
						SocialManager.logger.warn("[玩家删除称号成功] [" + this.getLogString() + "] [key:" + type + "]");
					}
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 设置个人默认称号
	 * 
	 * @param type
	 * @param title
	 */
	public void setPersonTitle(int type) {
		if (playerTitles == null) playerTitles = new ArrayList<PlayerTitle>();
		for (PlayerTitle pt : playerTitles) {
			if (pt.getTitleType() == type) {
				long now = System.currentTimeMillis();
				if (pt.getLastTime() > 0 && (now >= pt.getLastTime() + pt.getStartTime())) {
					this.sendError(Translate.称号已过期);
					return;
				}
				setDefaultTitleType(type);
				int color = 0;
				PlayerTitlesManager ptm = PlayerTitlesManager.getInstance();
				if (ptm.getColorByTitleType(type) > 0) {
					color = ptm.getColorByTitleType(type);
				}
				pt.setBuffName(ptm.getBuffNameByType(type));
				pt.setBuffLevl(ptm.getBuffLevlByType(type));
				pt.setTitleShowName(ptm.getTitleShowNameByType(type));
				pt.setDescription(ptm.getDescriptionByType(type));
				pt.setLastTime(ptm.getLastTimeByType(type));
				pt.setIcon(ptm.getIconByType(type));
				try {
					if (pt.getTitleShowName() != null && !pt.getTitleShowName().isEmpty()) {
						this.setTitle(pt.getTitleShowName());
					} else {
						this.setTitle("<f color='" + color + "'>" + pt.getTitleName() + "</f>");
					}
				} catch (Exception e) {
					this.setTitle("<f color='" + color + "'>" + pt.getTitleName() + "</f>");
					if (SocialManager.logger.isWarnEnabled()) {
						SocialManager.logger.warn("[新称号修改] [设置新称号异常] [" + this.getLogString() + "] [pt.getTitleShowName():" + pt.getTitleShowName() + "]", e);
					}
				}
				if (SocialManager.logger.isWarnEnabled()) {
					SocialManager.logger.warn("[设置默认称号成功] [" + this.getLogString() + "] [title:" + pt.getTitleName() + "]");
				}
				boolean exist = false;
				try {
					if (pt.getBuffName() != null && !pt.getBuffName().isEmpty()) {
						BuffTemplateManager btm = BuffTemplateManager.getInstance();
						BuffTemplate bt = btm.getBuffTemplateByName(pt.getBuffName());
						if (bt != null) {
							int bufLevl = pt.getBuffLevl();
							Buff buff = bt.createBuff(bufLevl);
							if (buff != null) {
								buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 999999 * 1000);
								buff.setCauser(this);
								if (pt.getBuffName().equals(Translate.为爱癫狂)) {
									buff.setDescription(Translate.为爱癫狂buff描述);
								} else if (pt.getBuffName().equals(Translate.一往情深)) {
									buff.setDescription(Translate.一往情深buff描述);
								} else if (pt.getBuffName().equals(Translate.倾情天下)) {
									buff.setDescription(Translate.倾情天下buff描述);
								} else {
									buff.setDescription(ptm.getDescriptionByType(type));
								}
								this.placeBuff(buff);
								this.setTitleBuffName(buff.getTemplateName());
								exist = true;
							} else {
								if (SocialManager.logger.isWarnEnabled()) {
									SocialManager.logger.warn("[设置默认称号buff失败,没取到buff] [" + this.getLogString() + "] [buff名:" + pt.getBuffName() + "][" + bufLevl + "]");
								}
							}
						} else {
							if (SocialManager.logger.isWarnEnabled()) {
								SocialManager.logger.warn("[设置默认称号buff失败,没取到buff模板] [" + this.getLogString() + "] [buff名:" + pt.getBuffName() + "]");
							}
						}
					}
				} catch (Exception e) {
					SocialManager.logger.error("[设置默认称号buff失败] [" + this.getLogString() + "] [称号类型:" + pt.getBuffName() + "]", e);
				}
				if (!exist && this.getTitleBuffName() != null && !this.getTitleBuffName().isEmpty()) { // 清楚称号buff
					Buff titleBuff = this.getBuffByName(this.getTitleBuffName());
					if (titleBuff != null) {
						titleBuff.setInvalidTime(0);
						this.setTitleBuffName("");
					} else if (SocialManager.logger.isWarnEnabled()) {
						SocialManager.logger.warn("[清除原有称号buff失败] [" + this.getLogString() + "] [buff名:" + this.getTitleBuffName() + "]");
					}
				}
				return;
			}
		}
		if (SocialManager.logger.isWarnEnabled()) {
			SocialManager.logger.warn("[设置默认称号失败] [" + this.getLogString() + "] [称号类型:" + type + "]");
		}
	}

	/**
	 * 通知玩家家族的职位变化
	 */
	public void noticeJiazuTitleChange(Jiazu jiazu) {
		if (jiazu.getJiazuID() != getJiazuId()) {
			return;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(getId(), getJiazuId());
		if (jm == null) {
			return;
		}
		initJiazuTitleAndIcon();
		JIAZU_TITLE_CHANGE_RES res = new JIAZU_TITLE_CHANGE_RES(GameMessageFactory.nextSequnceNum(), jm.getTitle().ordinal(), jiazu.getTitleAlias());
		addMessageToRightBag(res);
	}

	@Override
	public void setJiazuName(String value) {
		value = (value == null ? "" : value);
		super.setJiazuName(value);
	}

	public void doOnDeliverBournTask() {

	}

	/**
	 * 得到VIP等级
	 * 
	 * @return
	 */
	public byte getVipLevel() {
		if (vipDisplay) {
			VipManager vm = VipManager.getInstance();
			if (vm != null) {
				byte realVip = getRealVipLevel();
				byte cardVip = getCardVip();
				return (byte) Math.max(realVip, cardVip);
			}
		}
		return 0;
	}

	public byte getRealVipLevel() {
		if (vipDisplay) {
			VipManager vm = VipManager.getInstance();
			if (vm != null) {
				return (byte) vm.getPlayerVipLevel(this, false);
			}
		}
		return 0;
	}

	public byte getCardVip() {
		Buff vipBuff = null;
		String buffName = null;
		for (Buff b : buffs) {
			if (VipManager.vipBuffMap.containsKey(b.getTemplateName())) {
				vipBuff = b;
				buffName = b.getTemplateName();
				break;
			}
		}
		byte cardVip = 0;
		try {
			cardVip = vipBuff == null ? 0 : (byte) VipManager.vipBuffMap.get(buffName).intValue();
		} catch (Exception e) {
			GamePlayerManager.logger.error("[获取玩家卡片VIP等级] [异常]", e);
		}
		return cardVip;
	}

	/**
	 * 得到当前VIP等级对应的最大任务上限
	 */
	public int getMaxBournTaskNum() {
		if (getVipLevel() <= 6) {
			return 10;
		} else if (getVipLevel() == 7) {
			return 15;
		} else {
			return 20;
		}
	}

	/**
	 * 得到当前VIP等级对应的打坐时间上限
	 */
	public long getMaxBournZezenTime() {
		int VIPLevel = getVipLevel();
		if (VIPLevel > 0 && VIPLevel < BournManager.maxTaskNumOfVIPUser.length - 1) {
			return BournManager.maxZazenTimeOfVIPUser[VIPLevel];
		}
		return BournManager.maxZazenTimeOfCommonUser;
	}

	public void setLeftZazenTime(long value) {
		super.setLeftZazenTime(value);
		setDirty(true, "leftZazenTime");
	}

	@Override
	public void setLastDeliverTimeOfBournTask(long value) {
		super.setLastDeliverTimeOfBournTask(value);
		setDirty(true, "lastDeliverTimeOfBournTask");
	}

	/**
	 * 修改境界任务数量和打坐时间<BR/>
	 * 没天0点时更新在线玩家，每次登陆都会更新<BR/>
	 */
	private void modifyBournTaskAndTime() {
		try {
			long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (!TimeTool.instance.isSame(getLastCountBournTime(), now, TimeDistance.DAY)) {
				long beforeAdd = getLeftZazenTime();
				setDeliverBournTaskNum(0);
				if (getClassLevel() >= BournManager.zazenLevel) {
					if ((getLeftZazenTime() + BournManager.zazenTimeDailyIncrease) >= getMaxBournZezenTime()) {
						setLeftZazenTime(getMaxBournZezenTime());
					} else {
						setLeftZazenTime(getLeftZazenTime() + BournManager.zazenTimeDailyIncrease);
					}
				}
				if (getClassLevel() >= BournManager.dailyTaskLevel) {
					if ((getMaxBournTaskNum() + BournManager.taskDailyIncrease) >= getMaxBournTaskNum()) {
						setMaxBournTaskNum(getMaxBournTaskNum());
					} else {
						setMaxBournTaskNum(getMaxBournTaskNum() + BournManager.taskDailyIncrease);
					}
				}
				setLastCountBournTime(now);

				if (BournManager.logger.isWarnEnabled()) {
					BournManager.logger.warn(getLogString() + "[给用户增加打坐时间] [增加前:{}] [增加后:{}]", new Object[] { beforeAdd, getLeftZazenTime() });
				}
			}
			if (BournManager.logger.isWarnEnabled()) {
				BournManager.logger.warn(getLogString() + " [modifyBournTaskAndTime] [境界打坐时间:{}]", new Object[] { getLeftZazenTime() });
			}
		} catch (Exception e) {
			BournManager.logger.error(getLogString() + " [modifyBournTaskAndTime] ", e);
		}
	}

	@Override
	public void setMaxBournTaskNum(int value) {
		super.setMaxBournTaskNum(value);
		setDirty(true, "maxBournTaskNum");
	}

	@SimpleColumn(length = 12000)
	private Map<String, PropsUseRecord> propsUseRecordMap = new HashMap<String, PropsUseRecord>();

	public Map<String, PropsUseRecord> getPropsUseRecordMap() {
		return propsUseRecordMap;
	}

	public void setPropsUseRecordMap(Map<String, PropsUseRecord> propsUseRecordMap) {
		this.propsUseRecordMap = propsUseRecordMap;
	}

	/**
	 * 初始化使用道具记录
	 */
	// private void initPropsUseRecord() {
	// if (propsUseRecordMap != null) {
	// for (java.util.Map.Entry<String, PropsUseRecord> en :
	// propsUseRecordMap.entrySet()) {
	// en.getValue().init();
	// }
	// } else {
	// propsUseRecordMap = new HashMap<String, PropsUseRecord>();
	// }
	// }

	/**
	 * 国内仙缘
	 */
	@SimpleColumn(length = 8000)
	private ActivityRecordOnPlayer fate;
	/**
	 * 国外仙缘
	 */
	@SimpleColumn(length = 8000)
	ActivityRecordOnPlayer abroadFate;
	/**
	 * 国内论道
	 */
	@SimpleColumn(length = 8000)
	ActivityRecordOnPlayer beer;
	/**
	 * 国外论道
	 */
	@SimpleColumn(length = 8000)
	ActivityRecordOnPlayer abroadBeer;

	public ActivityRecordOnPlayer getFate() {
		return fate;
	}

	public void setFate(ActivityRecordOnPlayer fate) {
		this.fate = fate;
		setDirty(true, "fate");
	}

	public ActivityRecordOnPlayer getAbroadFate() {
		return abroadFate;
	}

	public void setAbroadFate(ActivityRecordOnPlayer abroadFate) {
		this.abroadFate = abroadFate;
		setDirty(true, "abroadFate");
	}

	public ActivityRecordOnPlayer getBeer() {
		return beer;
	}

	public void setBeer(ActivityRecordOnPlayer beer) {
		this.beer = beer;
		setDirty(true, "beer");
	}

	public ActivityRecordOnPlayer getAbroadBeer() {
		return abroadBeer;
	}

	public void setAbroadBeer(ActivityRecordOnPlayer abroadBeer) {
		this.abroadBeer = abroadBeer;
		setDirty(true, "abroadBeer");
	}

	ExploreEntity exploreEntity;

	public ActivityRecordOnPlayer getActivityRecord(byte type) {
		switch (type) {
		case 0:
			if (fate == null) {
				fate = new ActivityRecordOnPlayer();
				setFate(fate);
			}
			return fate;
		case 1:
			if (abroadFate == null) {
				abroadFate = new ActivityRecordOnPlayer();
				setAbroadFate(abroadFate);
			}
			return abroadFate;
		case 2:
			if (beer == null) {
				beer = new ActivityRecordOnPlayer();
				setBeer(beer);
			}
			return beer;
		case 3:
			if (abroadBeer == null) {
				abroadBeer = new ActivityRecordOnPlayer();
				setAbroadBeer(abroadBeer);
			}
			return abroadBeer;
		}
		return null;
	}

	public long[] getInitActivityId(byte type) {

		ActivityRecordOnPlayer fate = this.getActivityRecord(type);
		return fate.getActivityId();
	}

	public ExploreEntity getExploreEntity() {
		return exploreEntity;
	}

	public void setExploreEntity(ExploreEntity exploreEntity) {
		this.exploreEntity = exploreEntity;
		setDirty(true, "exploreEntity");
	}

	public String getLastFeedTime() {
		return lastFeedTime;
	}

	public void setLastFeedTime(String lastFeedTime) {

		getCurrSoul().setLastFeedTime(lastFeedTime);
		setDirty(true, "currSoul");
		this.lastFeedTime = lastFeedTime;

	}

	public FollowableNPC getFollowableNPC() {
		return followableNPC;
	}

	public void setFollowableNPC(FollowableNPC followableNPC) {
		this.followableNPC = followableNPC;
	}

	public long getRepeekCD() {
		if (CountryManager.getInstance().getCountryMap().get(getCountry()).guotan) {
			return PeekAndBrickManager.getInstance().getCountryPeekCD();
		}
		return PeekAndBrickManager.getInstance().getSimplePeekCD();
	}

	/**
	 * 得到某个物品个数，限定颜色和绑定状态
	 * 
	 * @param articleName
	 * @param articleColor
	 * @param both
	 * @return
	 */
	public int getArticleNum(String articleName, int articleColor, BindType both) {
		// TODO Auto-generated method stub
		int count = 0;
		if (knapsacks_common != null) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				Article article = am.getArticle(articleName);
				if (article != null) {
					if (both == (BindType.BIND)) {
						Knapsack knapsack = getKnapsack_common();
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor, true);
						}
						knapsack = getPetKnapsack();
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor, true);
						}
						knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor, true);
						}
					} else if (both == (BindType.NOT_BIND)) {
						Knapsack knapsack = getKnapsack_common();
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor, false);
						}
						knapsack = getPetKnapsack();
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor, true);
						}
						knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor, false);
						}
					} else if (both == (BindType.BOTH)) {
						Knapsack knapsack = getKnapsack_common();
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor);
						}
						knapsack = getPetKnapsack();
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor, true);
						}
						knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
						if (knapsack != null) {
							count += knapsack.countArticle(articleName, articleColor);
						}
					}
				}
			}
		}
		return count;
	}

	/**
	 * 按名字，颜色。绑定状态移除一个道具
	 * 
	 * @param articleName
	 * @param articleColor
	 * @param bind
	 * @return
	 */
	public synchronized ArticleEntity removeArticleByNameColorAndBind(String articleName, int colorType, BindType bind, String reason, boolean 从游戏中删除) {
		// TODO Auto-generated method stub
		if (bind == (BindType.BIND)) {
			Knapsack knapsack = getKnapsack_common();
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType, true);
				if (index >= 0) {
					ArticleEntity ae = knapsack.remove(index, reason, 从游戏中删除);
					return ae;
				}
			}
			knapsack = getPetKnapsack();
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType, true);
				if (index >= 0) {
					ArticleEntity ae = knapsack.remove(index, reason, 从游戏中删除);
					return ae;
				}
			}
			knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType, true);
				if (index >= 0) {
					ArticleEntity ae = knapsack.remove(index, reason, 从游戏中删除);
					return ae;
				}
			}
		} else if (bind == (BindType.NOT_BIND)) {
			Knapsack knapsack = getKnapsack_common();
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType, false);
				if (index >= 0) {
					ArticleEntity ae = knapsack.remove(index, reason, 从游戏中删除);
					return ae;
				}
			}
			knapsack = getPetKnapsack();
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType, false);
				if (index >= 0) {
					ArticleEntity ae = knapsack.remove(index, reason, 从游戏中删除);
					return ae;
				}
			}
			knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType, false);
				if (index >= 0) {
					return knapsack.remove(index, reason, 从游戏中删除);
				}
			}
		} else if (bind == (BindType.BOTH)) {
			Knapsack knapsack = getKnapsack_common();
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType);
				if (index >= 0) {
					return knapsack.remove(index, reason, 从游戏中删除);
				}
			}
			knapsack = getPetKnapsack();
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType);
				if (index >= 0) {
					return knapsack.remove(index, reason, 从游戏中删除);
				}
			}
			knapsack = knapsacks_fangBao;// getKnapsack_fangbao(article.getKnapsackType());
			if (knapsack != null) {
				int index = knapsack.indexOf(articleName, colorType);
				if (index >= 0) {
					return knapsack.remove(index, reason, 从游戏中删除);
				}
			}
		}

		return null;
	}

	/**
	 * 记录当前位置为传送位置(可下次再回到这里,非副本使用)
	 */
	public void markLastGameInfo() {
		if (!this.isInCave() && !this.inSelfSeptStation()) {
			setLastTransferGame(getCurrentGame().gi.name);
			setLastX(getX());
			setLastY(getY());
			if (FaeryManager.logger.isInfoEnabled()) {
				FaeryManager.logger.info(this.getLogString() + "[记录lastGameInfo][gameName:{}][({},{})]", new Object[] { getLastTransferGame(), getLastX(), getLastY() });
			}
		}
	}

	// 文采值
	public void addCulture(int num) {
		// ++culture;
		// this.setCulture(culture);
		int old = this.getCulture();
		try {
			BillingCenter.getInstance().playerSaving(this, num, CurrencyType.WENCAI, SavingReasonType.答题奖励文采, "答题活动");
			if (QuizManager.logger.isWarnEnabled()) {
				QuizManager.logger.warn("[增加文采值] [" + this.getLogString() + "] [现有值" + old + "] [增加值:" + num + "] [增加后" + this.getCulture() + "]");
			}
		} catch (SavingFailedException e) {
			QuizManager.logger.error("[增加文采值异常] [" + this.getLogString() + "] [现有值" + old + "] [增加值:" + num + "]", e);
		}
		this.setDirty(true, "culture");
	}

	public byte getDefaultAssignRule() {
		return defaultAssignRule;
	}

	public void setDefaultAssignRule(byte defaultAssignRule) {
		this.defaultAssignRule = defaultAssignRule;
		setDirty(true, "defaultAssignRule");
	}

	public transient Calendar calendar = Calendar.getInstance();

	public long 每天可以使用的绑银上限() {
		long monthCardSilver = 0;
		if(ownMonthCard(CardFunction.提升绑银使用上限1锭)){
			monthCardSilver = 1000000L;
		}
		VipManager vm = VipManager.getInstance();
		if (vm != null) {
			return PlayerManager.每人每天可以使用绑银 + vm.vip每天可以多使用的绑银数(this) + monthCardSilver;
		}
		return PlayerManager.每人每天可以使用绑银 + monthCardSilver;
	}

	/**
	 * 得到今天可以使用的绑银数
	 * 
	 * @return
	 */
	public long getTodayCanUseBindSilver() {
		calendar.setTimeInMillis(SystemTime.currentTimeMillis());
		if (calendar.get(Calendar.DAY_OF_YEAR) != time_useBindSilver_yearOfDay) {
			setTime_useBindSilver_yearOfDay(calendar.get(Calendar.DAY_OF_YEAR));
			setTodayUsedBindSilver(0);
			if (每天可以使用的绑银上限() < getBindSilver()) {
				return 每天可以使用的绑银上限();
			} else {
				return getBindSilver();
			}
		} else {
			long a = 每天可以使用的绑银上限() - todayUsedBindSilver;
			if (a <= 0) {
				return 0;// VIP体验卡导致VIP等级变化导致这里可能是负数
			}
			if (a < getBindSilver()) {
				return a;
			} else {
				return getBindSilver();
			}
		}
	}

	public long getMaxCanUseBindSilver() {
		calendar.setTimeInMillis(SystemTime.currentTimeMillis());
		if (calendar.get(Calendar.DAY_OF_YEAR) != time_useBindSilver_yearOfDay) {
			setTime_useBindSilver_yearOfDay(calendar.get(Calendar.DAY_OF_YEAR));
			setTodayUsedBindSilver(0);
			return 每天可以使用的绑银上限();
		} else {
			long a = 每天可以使用的绑银上限() - todayUsedBindSilver;
			if (a <= 0) {
				return 0;// VIP体验卡导致VIP等级变化导致这里可能是负数
			}
			return a;
		}
	}

	public int getTime_useBindSilver_yearOfDay() {
		return time_useBindSilver_yearOfDay;
	}

	public void setTime_useBindSilver_yearOfDay(int timeUseBingSilverYearOfDay) {
		time_useBindSilver_yearOfDay = timeUseBingSilverYearOfDay;
		setDirty(true, "time_useBindSilver_yearOfDay");
	}

	public void setTodayUsedBindSilver(long todayUsedBindSilver) {
		super.setTodayUsedBindSilver(todayUsedBindSilver);
		setDirty(true, "todayUsedBindSilver");
	}

	public void setFighting(boolean value) {
		super.setFighting(value);
		if (isFighting()) {
			// BoothsaleManager.getInstance().msg_cancelBoothSale(this);
			DealCenter.getInstance().cancelDeal(DealCenter.getInstance().getDeal(this), this);
		}
	}

	public void setState(byte value) {
		super.setState(value);
		if (value == Player.STATE_DEAD) {
			BoothsaleManager.getInstance().msg_cancelBoothSale(this);
		}
	}

	public void setIsUpOrDown(boolean value) {
		if (getCurrSoul() != null) {
			getCurrSoul().setUpOrDown(value);
			setDirty(true, "currSoul");
			this.isUpOrDown = value;
		}
	}

	public void setEnergy(int value) {
		super.setEnergy(value);
		this.setDirty(true, "energy");
	}

	public void setGongxun(long value) {
		super.setGongxun(value);
		guozhanLevel = (int) Math.pow(new Double(2 * gongxun) / 100, new Double(1) / new Double(3));
		if (guozhanLevel > 192) {
			guozhanLevel = 192;
		}
		if (guozhanLevel > 20) { // 客户端显示bug 目前设置20为上限 2014年7月21日17:17:03
			guozhanLevel = 20;
		}
		setGuozhanLevel(guozhanLevel);
		this.setDirty(true, "gongxun");
	}

	/**
	 * 玩家是否可以支付所需的数量 规则:<先判断绑银后判断银子> 1.当玩家今日可支配绑银足够则直接扣除绑银,返回true<BR/>
	 * 2.当玩家今日可支配的绑银不足,当前银子足够支付差额,返回true<BR/>
	 * 3.返回false<BR/>
	 * 
	 * @param silver
	 * @return
	 */
	public boolean bindSilverEnough(long silver) {
		long todayCanuse = getTodayCanUseBindSilver();
		if ((todayCanuse + getShopSilver() + getSilver()) >= silver) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canFreeFromBeDamaged(Fighter fighter) {
		if (this.getCurrentGame() != null) {
			try {
				if (fighter != null) {
					if (this.getCurrentGame().gi.name.equalsIgnoreCase("jianyu") && fighter instanceof BossMonster) {
						if (((BossMonster) fighter).getSpriteCategoryId() == 20113387) {
							if (Skill.logger.isDebugEnabled()) {
								Skill.logger.debug("[判断是否可以攻击] [可以攻击] [" + this.getLogString() + "] [" + fighter.getId() + "] [" + fighter.getName() + "]");
							}
							return false;
						}
						if (Skill.logger.isDebugEnabled()) {
							Skill.logger.debug("[判断是否可以攻击] [不可以攻击] [" + this.getLogString() + "] [" + fighter.getId() + "] [" + fighter.getName() + "]");
						}
					}
				}
			} catch (Exception e) {
				Skill.logger.warn("[判断是否可以攻击] [异常] [" + this.getLogString() + "] ", e);
			}
			Game game = this.getCurrentGame();
			if (this.getCurrentMapAreaName() != null) {
				MapArea ma = game.gi.getMapAreaByName(this.getCurrentMapAreaName());
				if (ma != null) {
					if (ma.getType() == MapArea.TYPE_SAFE_2 || ma.getType() == MapArea.TYPE_BAITAN) {
						return true;
					}
					if (ma.getType() == MapArea.TYPE_SAFE_1 && game.country == this.getCountry()) {
						return true;
					}
				}
			}
			if (this.getCurrentMapAreaNames() != null && this.getCurrentMapAreaNames().length > 0) {
				String[] currentMapAreaNames = this.getCurrentMapAreaNames();
				try {
					for (int i = 0; i < currentMapAreaNames.length; i++) {
						MapPolyArea ma = game.gi.getMapPolyAreaByName(currentMapAreaNames[i]);
						if (ma != null) {
							if (ma.getType() == MapArea.TYPE_SAFE_2) {
								return true;
							}
							if (ma.getType() == MapArea.TYPE_SAFE_1 && game.country == this.getCountry()) {
								return true;
							}
						}
					}
				} catch (Exception ex) {
				}
			}
		}
		return false;
	}

	public void setClassLevel(short value) {
		super.setClassLevel(value);
		this.setDirty(true, "classLevel");
	}

	public void setNuqiSkillsLevels(byte[] value) {
		super.setNuqiSkillsLevels(value);
		this.setDirty(true, "nuqiSkillsLevels");
	}

	public void setXinfaLevels(byte[] value) {
		super.setXinfaLevels(value);
		this.setDirty(true, "xinfaLevels");
	}

	public void setPkMode(byte value) {
		super.setPkMode(value);
		this.setDirty(true, "pkMode");
	}

	public void setBournExp(int value) {
		super.setBournExp(value);
		this.setDirty(true, "bournExp");
	}

	public boolean isFirstEnter() {
		return firstEnter;
	}

	public void setFirstEnter(boolean firstEnter) {
		this.firstEnter = firstEnter;
		setDirty(true, "firstEnter");
	}

	// 简单对象的属性
	private String brithDay = "";
	private int star;
	private String icon = "";
	private int age;
	private int playerCountry; // 中国 美国等
	private int province = -1;
	private int city = -1;
	private String loving = "";
	private String personShow = "";
	private String mood = "";
	private byte seeState; // 可见状态 0完全公开 1仅好友可见 2完全保密

	public String getBrithDay() {
		return brithDay;
	}

	public void setBrithDay(String brithDay) {
		this.brithDay = brithDay;
		setDirty(true, "brithDay");
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
		setDirty(true, "star");
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
		setDirty(true, "icon");
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
		setDirty(true, "age");
	}

	public int getPlayerCountry() {
		return playerCountry;
	}

	public void setPlayerCountry(int playerCountry) {
		this.playerCountry = playerCountry;
		setDirty(true, "playerCountry");
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
		setDirty(true, "province");
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
		setDirty(true, "city");
	}

	public String getLoving() {
		return loving;
	}

	public void setLoving(String loving) {
		this.loving = loving;
		setDirty(true, "loving");
	}

	public String getPersonShow() {
		return personShow;
	}

	public void setPersonShow(String personShow) {
		this.personShow = personShow;
		setDirty(true, "personShow");
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
		setDirty(true, "mood");
	}

	public byte getSeeState() {
		return seeState;
	}

	public void setSeeState(byte seeState) {
		this.seeState = seeState;
		setDirty(true, "seeState");
	}

	@Override
	public void setDeliverBournTaskNum(int value) {
		super.setDeliverBournTaskNum(value);
		setDirty(true, "deliverBournTaskNum");
	}

	public String getSpouse() {
		if (spouse != null && !spouse.equals("")) {
			return spouse;
		} else {
			if (SocialManager.getInstance() == null) {
				return "";
			}
			Relation relation = SocialManager.getInstance().getRelationById(getId());
			if (relation.getMarriageId() > 0) {
				MarriageInfo info = MarriageManager.getInstance().getMarriageInfoById(relation.getMarriageId());
				if (info != null) {
					if (info.getHoldA() == getId()) {
						PlayerSimpleInfo playerInfo = PlayerSimpleInfoManager.getInstance().getInfoById(info.getHoldB());
						if (playerInfo == null) {
							MarriageManager.logger.error("getSpouse取另外一个人出错" + info.getId() + "==" + info.getHoldB());
						} else {
							setSpouse(playerInfo.getName());
						}
					} else if (info.getHoldB() == getId()) {
						PlayerSimpleInfo playerInfo = PlayerSimpleInfoManager.getInstance().getInfoById(info.getHoldA());
						if (playerInfo == null) {
							MarriageManager.logger.error("getSpouse取另外一个人出错" + info.getId() + "==" + info.getHoldA());
						} else {
							setSpouse(playerInfo.getName());
						}
					}
				}
			}
			return spouse;
		}
	}

	// 公告id
	@SimpleColumn(saveInterval = 600)
	private long noticeId;
	@SimpleColumn(saveInterval = 600)
	private long obtainNoticeId;

	// 每次进地图弹出活动公告设置为true，只有false才弹出
	private long popActivityNoticeTime;
	private long activityNoticeId;

	public long getActivityNoticeId() {
		return activityNoticeId;
	}

	public void setActivityNoticeId(long activityNoticeId) {
		this.activityNoticeId = activityNoticeId;
		setDirty(true, "activityNoticeId");
	}

	public long getPopActivityNoticeTime() {
		return popActivityNoticeTime;
	}

	public void setPopActivityNoticeTime(long popActivityNoticeTime) {
		this.popActivityNoticeTime = popActivityNoticeTime;
		setDirty(true, "popActivityNoticeTime");
	}

	public long getObtainNoticeId() {
		return obtainNoticeId;
	}

	public void setObtainNoticeId(long obtainNoticeId) {
		this.obtainNoticeId = obtainNoticeId;
		setDirty(true, "obtainNoticeId");
	}

	public long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(long noticeId) {
		this.noticeId = noticeId;
		setDirty(true, "noticeId");
	}

	/**
	 * 是否在监狱
	 * 
	 * @return
	 */
	public boolean isInPrison() {
		synchronized (buffs) {
			if (buffs != null) {
				for (Buff buff : buffs) {
					if (buff != null && buff.getInvalidTime() > 0 && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否在比武
	 * @return
	 */
	public boolean isInWusheng() {
		return getDuelFieldState() > 0;
	}

	public boolean isInCave() {
		if (getCurrentGame() == null) {
			return false;
		}
		if (getRealMapName().indexOf("_jy_") > 0) {
			return true;
		}

		return false;
	}

	public boolean inSelfCave() {
		if (inCave()) {
			if (this.getRealMapName().contains(String.valueOf(this.getFaeryId()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否在禁言
	 * 
	 * @return
	 */
	public boolean isJinyan() {
		synchronized (buffs) {
			if (buffs != null) {
				for (Buff buff : buffs) {
					if (buff != null && buff.getInvalidTime() > 0 && buff.getTemplate() != null && CountryManager.禁言buff名称.equals(buff.getTemplate().getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否国战，不需要存储，客户端根据这个标识显示国战图标以及其他和国战相关的判断 此值需要移到AbstractPlayer中
	 */
	// private transient boolean isGuozhan;

	/**
	 * 国战星级，客户端根据此数值显示国战星星，0个为无星，1为空星，2为实星，以此类推 此值需要移到AbstractPlayer中
	 */
	// private transient int guozhanLevel;

	/**
	 * 玩家的国家处于国战时，玩家登录是否发送过国战通知
	 */
	private transient boolean guozhanLoginNotified;

	public void setGuozhan(boolean isGuozhan) {
		super.setIsGuozhan(isGuozhan);
	}

	public void setGuozhanLevel(int guozhanLevel) {
		super.setGuozhanLevel(guozhanLevel);
	}

	public boolean isGuozhanLoginNotified() {
		return guozhanLoginNotified;
	}

	public void setGuozhanLoginNotified(boolean guozhanLoginNotified) {
		this.guozhanLoginNotified = guozhanLoginNotified;
	}

	/**
	 * 玩家的权利组
	 */
	@SimpleColumn(length = 20000)
	private HashMap<Integer, Authority> authorityMap = null;

	public HashMap<Integer, Authority> getAuthorityMap() {
		return authorityMap;
	}

	public void setAuthorityMap(HashMap<Integer, Authority> authorityMap) {
		this.authorityMap = authorityMap;
	}

	public void notifyAuthorityChanged() {
		setDirty(true, "authorityMap");
	}

	@Override
	public void setLilian(long value) {
		// TODO Auto-generated method stub
		super.setLilian(value);
		setDirty(true, "lilian");

	}

	public ArrayList<Long> getHorseIdList() {
		return horseIdList;
	}

	public void setHorseIdList(ArrayList<Long> horseIdList) {
		getCurrSoul().setHorseArr(horseIdList);
		setDirty(true, "currSoul");
		this.horseIdList = horseIdList;
	}

	public void setHorseIdList4UnusedSoul(ArrayList<Long> horseIdList) {
		List<Soul> un = this.getUnusedSoul();
		if (un != null && un.size() > 0) {
			this.getUnusedSoul().get(0).setHorseArr(horseIdList);
			setDirty(true, "unusedSoul");
		}
	}

	/**
	 * 连斩
	 */
	public transient int killCount = 0;

	/**
	 * 连斩积分
	 */
	public transient int killJifen = 0;

	/**
	 * 杀人时间
	 */
	public transient long lastkillOtherTime;

	public static final int 连斩倒计时 = 120000;

	public static final int[] 连斩数成就 = new int[] { 10, 50, 100, 150, 200, 250, 300, 400, 500, 600, 700, 800, 900, 1000, 10000, 100000, 1000000, 10000000, 100000000 };
	public static final String[] 连斩数成就名 = new String[] { Translate.十连斩, Translate.五十连斩, Translate.一百连斩, Translate.一百五十连斩, Translate.二百连斩, Translate.三百连斩, Translate.四百连斩, Translate.五百连斩, Translate.六百连斩, Translate.七百连斩, Translate.八百连斩, Translate.九百连斩, Translate.千人斩, Translate.万人斩, Translate.十万人斩, Translate.百万人斩, Translate.千万人斩, Translate.亿人斩 };

	public void 杀人计算连斩数(Player beKilledPlayer) {
		if (beKilledPlayer.getCountry() != this.getCountry() && this.getCurrentGame() != null && this.getCurrentGame().country != CountryManager.中立 && beKilledPlayer.getLevel() >= this.level - 20) {
			long now = System.currentTimeMillis();
			if (now - lastkillOtherTime > 连斩倒计时) {
				killCount = 0;
				killJifen = 0;
			}
			killCount++;
			killJifen++;
			try {
				AchievementManager.getInstance().record(this, RecordAction.累计连斩次数);
			} catch (Exception ex) {

			}
			if (beKilledPlayer.countryPosition > 0) {
				if (beKilledPlayer.countryPosition == CountryManager.国王) {
					killJifen += 9;
				} else {
					killJifen += 4;
				}
			}
			lastkillOtherTime = now;
			if (!this.isGuozhan) {
				for (int i = 连斩数成就.length - 1; i >= 0; i--) {
					if (this.killCount == 连斩数成就[i]) {
						// 系统广播
						{
							ChatMessageService cms = ChatMessageService.getInstance();
							ChatMessage msg = new ChatMessage();
							String description = Translate.translateString(Translate.某国某人连斩, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(getCountry()) }, { Translate.PLAYER_NAME_1, name }, { Translate.STRING_2, 连斩数成就名[i] } });
							msg.setMessageText(description);
							if (this.killCount >= 50) {
								description = Translate.translateString(Translate.某国某人连斩50, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(getCountry()) }, { Translate.PLAYER_NAME_1, name }, { Translate.STRING_2, 连斩数成就名[i] } });
								msg.setMessageText(description);
							} else if (this.killCount >= 100) {
								description = Translate.translateString(Translate.某国某人连斩100, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(getCountry()) }, { Translate.PLAYER_NAME_1, name }, { Translate.STRING_2, 连斩数成就名[i] } });
								msg.setMessageText(description);
							}
							try {
								cms.sendMessageToSystem(msg);
							} catch (Exception e) {

							}
						}
						break;
					}
				}
				通知客户端发生了连斩();
			}
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(this, RecordAction.击杀敌国玩家次数, 1);
			}

			BillboardStatDateManager bsdm = BillboardStatDateManager.getInstance();
			if (bsdm != null) {
				BillboardStatDate bsd = bsdm.getBillboardStatDate(this.id);
				if (bsd != null) {
					bsd.设置连斩(this, killCount, killJifen);
				}
			}
		}
		if (beKilledPlayer.getCountry() != this.getCountry() && beKilledPlayer.getLevel() >= this.level - 20) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(this, RecordAction.击杀不低于20级的敌国玩家, 1);
			}
		}
		if (beKilledPlayer.attackBiaoCheFlag.get(this.getCountry()) != null) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(this, RecordAction.杀死砸本国镖车人次数, 1);
			}
		}
		if (beKilledPlayer.getCountry() != this.getCountry() && this.getCurrentGame() != null && this.getCurrentGame().country == this.country) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(this, RecordAction.本国地图杀死敌国玩家数, 1);
			}
		}
		if (beKilledPlayer.getCountry() != this.getCountry() && this.getCurrentGame() != null && this.getCurrentGame().country != CountryManager.中立 && this.getCurrentGame().country != this.country) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(this, RecordAction.其他国家地图杀死敌国玩家数, 1);
			}
		}
		if (beKilledPlayer.getCountry() != this.getCountry() && beKilledPlayer.countryPosition > 0) {
			if (beKilledPlayer.countryPosition == CountryManager.国王) {
				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(this, RecordAction.击杀敌国国王次数, 1);
				}
			} else {
				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(this, RecordAction.击杀敌国官员次数, 1);
				}
			}
		}
		if (beKilledPlayer.getEvil() > 0) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(this, RecordAction.击杀红名次数, 1);
			}
		}
	}

	public void 通知客户端发生了连斩() {
		int index = 0;
		for (int i = 0; i < 连斩数成就.length; i++) {
			if (this.killCount < 连斩数成就[i]) {
				index = i;
				break;
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(连斩数成就名[index]).append(" ").append(this.killCount).append("/").append(连斩数成就[index]);
		String killInfo = sb.toString();
		long endTime = 连斩倒计时 - 1000;
		String sound = "lvseguangying";
		CONTINUE_KILL_REQ req = new CONTINUE_KILL_REQ(GameMessageFactory.nextSequnceNum(), this.killCount, killInfo, endTime, sound);
		this.addMessageToRightBag(req);
	}

	private long loginAddProsperityTime;
	private long speakAddProsperityTime;

	public long getLoginAddProsperityTime() {
		return loginAddProsperityTime;
	}

	public void setLoginAddProsperityTime(long loginAddProsperityTime) {
		this.loginAddProsperityTime = loginAddProsperityTime;
		setDirty(true, "loginAddProsperityTime");
	}

	public long getSpeakAddProsperityTime() {
		return speakAddProsperityTime;
	}

	public void setSpeakAddProsperityTime(long speakAddProsperityTime) {
		this.speakAddProsperityTime = speakAddProsperityTime;
		setDirty(true, "speakAddProsperityTime");
	}

	public void setCountry(byte value) {
		super.setCountry(value);
		setDirty(true, "country");
	}

	@Override
	public void setAvataRace(String avataRace) {
		super.setAvataRace(avataRace);
		setDirty(true, "avataRace");
	}

	/**
	 * 在挂机体力地图的刷新时间(用于时间地图的消耗计算)
	 */
	@SimpleColumn(saveInterval = 600)
	private long lastInTiLiDiTuTime;

	/**
	 * 在挂机体力地图的时间(分钟)
	 */
	@SimpleColumn(saveInterval = 600)
	private int inTiLiDiTuTime;

	/**
	 * 在宠物地图的刷新时间(用于时间地图的消耗计算)
	 */
	@SimpleColumn(saveInterval = 600)
	private long lastInPetDiTuTime;

	/**
	 * 在宠物地图的时间(分钟)
	 */
	@SimpleColumn(saveInterval = 600)
	private int inPetDiTuTime;

	/**
	 * 在万宝地图的刷新时间(用于时间地图的消耗计算)
	 */
	@SimpleColumn(saveInterval = 600)
	private long lastInWanBaoDiTuTime;

	/**
	 * 在万宝地图的时间(分钟)
	 */
	@SimpleColumn(saveInterval = 600)
	private int inWanBaoDiTuTime;
	/**
	 * 在八卦仙阙地图的刷新时间(用于时间地图的消耗计算)
	 */
	@SimpleColumn(saveInterval = 600)
	private long lastInBaGuaXianQueTime;

	/**
	 * 在八卦仙阙地图的时间(分钟)
	 */
	@SimpleColumn(saveInterval = 600)
	private int inBaGuaXianQueTime;

	/**
	 * 在仙蒂地图的刷新时间(用于时间地图的消耗计算)
	 */
	@SimpleColumn(saveInterval = 600)
	private long lastInXianDIDiTuTime;

	/**
	 * 在仙蒂地图的时间(分钟)
	 */
	@SimpleColumn(saveInterval = 600)
	private int inXianDiDiTuTime;
	/** 在新加的其他限制地图的时间（分钟） 0为新多宝 **/
	@SimpleColumn(saveInterval = 600)
	private int[] inLimitDituTime = new int[0];
	/** 在新加的限制地图的刷新时间（用于时间地图的消耗计算） */
	private long[] lastInLimitDituTime = new long[0];

	/**
	 * 每天在体力地图中最多待的时长(分钟)
	 */
	public static int DITU_TILI_MAX_TIME = 120;

	/**
	 * 每天在宠物地图中最多待的时长(分钟)
	 */
	public static int DITU_PET_MAX_TIME = 120;

	/**
	 * 每天在万宝地图中最多待的时长(分钟)
	 */
	public static int DITU_WANBAO_MAX_TIME = 60;
	/**
	 * 每天在八卦仙阙地图中最多待的时长(分钟)
	 */
	public static int DITU_BAGUAXIANQUE_MAX_TIME = 120;

	/**
	 * 仙蒂时间地图最多待的时长(分钟)
	 */
	public static int DITU_XIANDI_MAX_TIME = 120;
	/**
	 * 各种限制地图最多能呆多久
	 */
	public static int[] DITU_LIMIT_MAX_TIME = new int[] { 120 };

	// 用于回城复活无敌的情况
	public transient boolean 是否回城复活 = false;

	/**
	 * 囚禁次数
	 */
	@SimpleColumn(saveInterval = 600)
	private int qiujinCount;

	/**
	 * 囚禁时间点
	 */
	@SimpleColumn(saveInterval = 600)
	private long qiujinTime;

	public int getQiujinCount() {
		return qiujinCount;
	}

	public void setQiujinCount(int qiujinCount) {
		this.qiujinCount = qiujinCount;
		setDirty(true, "qiujinCount");
	}

	public long getQiujinTime() {
		return qiujinTime;
	}

	public void setQiujinTime(long qiujinTime) {
		this.qiujinTime = qiujinTime;
		setDirty(true, "qiujinTime");
	}

	@SimpleColumn(saveInterval = 600)
	private long killMonsterCount;

	@SimpleColumn(saveInterval = 600)
	private long lastKillMonsterTime;

	public long getKillMonsterCount() {
		return killMonsterCount;
	}

	public void setKillMonsterCount(long killMonsterCount) {
		this.killMonsterCount = killMonsterCount;
		setDirty(true, "killMonsterCount");
	}

	public long getLastKillMonsterTime() {
		return lastKillMonsterTime;
	}

	public void setLastKillMonsterTime(long lastKillMonsterTime) {
		this.lastKillMonsterTime = lastKillMonsterTime;
		setDirty(true, "lastKillMonsterTime");
	}

	public transient Calendar calendar2 = Calendar.getInstance();

	public synchronized void updateKillMonster() {
		long now = System.currentTimeMillis();
		calendar2.setTimeInMillis(now);
		int day = calendar2.get(Calendar.DAY_OF_YEAR);
		calendar2.setTimeInMillis(lastKillMonsterTime);
		int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			killMonsterCount = 0;
		}
		setKillMonsterCount(killMonsterCount + 1);
		setLastKillMonsterTime(now);
	}

	public long getLastInTiLiDiTuTime() {
		return lastInTiLiDiTuTime;
	}

	public void setLastInTiLiDiTuTime(long lastInTiLiDiTuTime) {
		this.lastInTiLiDiTuTime = lastInTiLiDiTuTime;
		setDirty(true, "lastInTiLiDiTuTime");
	}

	public long getLastInPetDiTuTime() {
		return lastInPetDiTuTime;
	}

	public void setLastInPetDiTuTime(long lastInPetDiTuTime) {
		this.lastInPetDiTuTime = lastInPetDiTuTime;
		setDirty(true, "lastInPetDiTuTime");
	}

	public long getLastInWanBaoDiTuTime() {
		return lastInWanBaoDiTuTime;
	}

	public void setLastInWanBaoDiTuTime(long lastInWanBaoDiTuTime) {
		this.lastInWanBaoDiTuTime = lastInWanBaoDiTuTime;
		setDirty(true, "lastInWanBaoDiTuTime");
	}

	public int getInTiLiDiTuTime() {
		return inTiLiDiTuTime;
	}

	public void setInTiLiDiTuTime(int inTiLiDiTuTime) {
		this.inTiLiDiTuTime = inTiLiDiTuTime;
		setDirty(true, "inTiLiDiTuTime");
	}

	public int getInPetDiTuTime() {
		return inPetDiTuTime;
	}

	public void setInPetDiTuTime(int inPetDiTuTime) {
		this.inPetDiTuTime = inPetDiTuTime;
		setDirty(true, "inPetDiTuTime");
	}

	public int getInWanBaoDiTuTime() {
		return inWanBaoDiTuTime;
	}

	public void setInWanBaoDiTuTime(int inWanBaoDiTuTime) {
		this.inWanBaoDiTuTime = inWanBaoDiTuTime;
		setDirty(true, "inWanBaoDiTuTime");
	}

	public long getLastInXianDIDiTuTime() {
		return this.lastInXianDIDiTuTime;
	}

	public void setLastInXianDIDiTuTime(long lastInXianDIDiTuTime) {
		this.lastInXianDIDiTuTime = lastInXianDIDiTuTime;
		setDirty(true, "lastInXianDIDiTuTime");
	}

	public int getInXianDiDiTuTime() {
		return this.inXianDiDiTuTime;
	}

	public void setInXianDiDiTuTime(int inXianDiDiTuTime) {
		this.inXianDiDiTuTime = inXianDiDiTuTime;
		setDirty(true, "inXianDiDiTuTime");
	}

	public transient Calendar calendar3 = Calendar.getInstance();

	/**
	 * 更新在体力地图的时间
	 * 
	 * @param lastInTiLiDiTuTime
	 */
	public void updateLastInTiLiDiTuTime(long now, int addMinute) {
		calendar3.setTimeInMillis(now);
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		calendar3.setTimeInMillis(lastInTiLiDiTuTime);
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			inTiLiDiTuTime = 0;
		}
		setLastInTiLiDiTuTime(now);
		setInTiLiDiTuTime(inTiLiDiTuTime + addMinute);
	}

	/**
	 * 更新在宠物地图的时间
	 * 
	 * @param lastInTiLiDiTuTime
	 */
	public void updateLastInPetDiTuTime(long now, int addMinute) {
		calendar3.setTimeInMillis(now);
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		calendar3.setTimeInMillis(lastInPetDiTuTime);
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			inPetDiTuTime = 0;
		}
		setLastInPetDiTuTime(now);
		setInPetDiTuTime(inPetDiTuTime + addMinute);
	}

	/**
	 * 更新在万宝地图的时间
	 * 
	 * @param lastInTiLiDiTuTime
	 */
	public void updateLastInWanBaoDiTuTime(long now, int addMinute) {
		calendar3.setTimeInMillis(now);
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		calendar3.setTimeInMillis(lastInWanBaoDiTuTime);
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			inWanBaoDiTuTime = 0;
		}
		setLastInWanBaoDiTuTime(now);
		setInWanBaoDiTuTime(inWanBaoDiTuTime + addMinute);
	}

	/**
	 * 更新在八卦仙阙地图的时间
	 * 
	 * @param lastInTiLiDiTuTime
	 */
	public void updateLastInBaGuaXianQueTime(long now, int addMinute) {
		calendar3.setTimeInMillis(now);
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		calendar3.setTimeInMillis(lastInBaGuaXianQueTime);
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			inBaGuaXianQueTime = 0;
		}
		setLastInBaGuaXianQueTime(now);
		setInBaGuaXianQueTime(inBaGuaXianQueTime + addMinute);
	}

	/**
	 * 更新仙蒂宝库地图的时间
	 * @param now
	 * @param addMinute
	 */
	public void updateLastInXianDiDiTuTime(long now, int addMinute) {
		calendar3.setTimeInMillis(now);
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		calendar3.setTimeInMillis(lastInXianDIDiTuTime);
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			inXianDiDiTuTime = 0;
		}
		setLastInXianDIDiTuTime(now);
		setInXianDiDiTuTime(inXianDiDiTuTime + addMinute);
	}

	public void updateLastInLimitDituTime(int type, long now, int addMinute) {
		if (inLimitDituTime.length < type) {
			inLimitDituTime = Arrays.copyOf(inLimitDituTime, type + 1);
			lastInLimitDituTime = Arrays.copyOf(lastInLimitDituTime, type + 1);
		}
		calendar3.setTimeInMillis(now);
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		calendar3.setTimeInMillis(lastInLimitDituTime[type]);
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			inLimitDituTime[type] = 0;
		}
		setLastInLimitDituTime(lastInLimitDituTime);
		inLimitDituTime[type] = inLimitDituTime[type] + addMinute;
		setInLimitDituTime(inLimitDituTime);
	}

	public boolean isValidInTiLiDiTuTime() {
		if (inTiLiDiTuTime <= DITU_TILI_MAX_TIME) {
			return true;
		}
		return false;
	}

	public boolean isValidInXianDiDiTuTime() {
		if (inXianDiDiTuTime <= DITU_XIANDI_MAX_TIME) {
			return true;
		}
		return false;
	}

	public boolean isValidInPetDiTuTime() {
		if (inPetDiTuTime <= DITU_PET_MAX_TIME) {
			return true;
		}
		return false;
	}

	public boolean isValidInWanBaoDiTuTime() {
		
		if (inWanBaoDiTuTime <= getMaxTimeOfSilverGame()) {
			return true;
		}
		return false;
	}

	public boolean isValidInBaGuaXianQueTime() {
		if (inBaGuaXianQueTime <= DITU_BAGUAXIANQUE_MAX_TIME) {
			return true;
		}
		return false;
	}

	// 每天祈福次数
	private int everyCliffordCount;

	private long lastCliffordTime;

	public int getEveryCliffordCount() {
		return everyCliffordCount;
	}

	public void setEveryCliffordCount(int everyCliffordCount) {
		this.everyCliffordCount = everyCliffordCount;
		setDirty(true, "everyCliffordCount");
	}

	public long getLastCliffordTime() {
		return lastCliffordTime;
	}

	public void setLastCliffordTime(long lastCliffordTime) {
		this.lastCliffordTime = lastCliffordTime;
		setDirty(true, "lastCliffordTime");
	}

	/**
	 * 更新祈福次数
	 * 
	 * @param lastInTiLiDiTuTime
	 */
	public void updateClifford(long now, int count) {
		calendar3.setTimeInMillis(now);
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		calendar3.setTimeInMillis(lastCliffordTime);
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			everyCliffordCount = 0;
			setCliffordNotifyFlag(false);
		}
		setLastCliffordTime(now);
		setEveryCliffordCount(everyCliffordCount + count);
	}

	public int 得到可以祈福的剩余祈福的次数(long now) {
		calendar3.setTimeInMillis(now);
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		calendar3.setTimeInMillis(lastCliffordTime);
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day != day2) {
			setLastCliffordTime(now);
			setEveryCliffordCount(0);
			setCliffordNotifyFlag(false);
		}
		VipManager vm = VipManager.getInstance();
		int vipCount = 0;
		if (vm != null) {
			vipCount = vm.vip每日增加的祈福使用次数(this);
		}
		return vipCount + CliffordManager.每天可以祈福的次数 - everyCliffordCount + TimesActivityManager.instacen.getAddNum(this, TimesActivityManager.QIFU_ACTIVITY);
	}

	public void setVipPickedRewardLevel(byte value) {
		super.setVipPickedRewardLevel(value);
		setDirty(true, "vipPickedRewardLevel");
	}

	// 每天祈福道具不足提醒
	public boolean cliffordNotifyFlag;

	public void setCliffordNotifyFlag(boolean cliffordNotifyFlag) {
		this.cliffordNotifyFlag = cliffordNotifyFlag;
		setDirty(true, "cliffordNotifyFlag");
	}

	// 某天的答题状态(只要同意就算完成)
	private boolean[] todayAnswerQuizState = new boolean[2];
	// 上次答题时间
	private long lastAnswerQuizTime;

	public boolean[] getTodayAnswerQuizState() {
		return todayAnswerQuizState;
	}

	public void setTodayAnswerQuizState(boolean[] todayAnswerQuizState) {
		this.todayAnswerQuizState = todayAnswerQuizState;
		setDirty(true, "todayAnswerQuizState");
	}

	public long getLastAnswerQuizTime() {
		return lastAnswerQuizTime;
	}

	public void setLastAnswerQuizTime(long lastAnswerQuizTime) {
		this.lastAnswerQuizTime = lastAnswerQuizTime;
		setDirty(true, "lastAnswerQuizTime");
	}

	// 玩家统计数据的引用,不要存盘,王勇全
	public transient HashMap<Integer, GameDataRecord> gdrMap = null;
	public transient HashMap<Integer, AchievementEntity> achievementEntityMap = null;
	public transient HashMap<Long, DeliverTask> deliverTaskMap = null;
	public transient HashMap<Long, NewDeliverTask> newdeliverTaskMap = null;
	public transient boolean hasLoadAllDeliverTask = false;

	// 客户端屏蔽的选项
	public transient boolean hiddenAllPlayer = false;
	public transient boolean hiddenSameCountryPlayer = false;
	public transient boolean hiddenChatMessage = false;
	public transient boolean needToNotifyAboutHidden = false;
	public transient boolean hiddenTransformPop = false; // 屏蔽交易弹窗
	public transient boolean hiddenChangePop = false; // 屏蔽交换弹窗
	public transient boolean hiddenFanzhiPop = false; // 屏蔽繁殖弹窗
	public transient boolean hiddenTeamPop = false; // 屏蔽组队弹窗
	public transient boolean autoFeedHorse = false; // 坐骑自动喂食
	public transient boolean autoBuyArticle = false; // 是否自动购买物品
	public transient String feedArticleName = ""; // 坐骑食物名
	public transient boolean isUseSiliver = false; // 是否使用银子
	public transient int autoFeedLine = -1; // 坐骑体力增低于多少后自动喂养

	// 属于个人的反馈ids 在发布新的时候增加，在entergame的时候查询
	public transient long[] feedbackIds;

	public long[] getFeedbackIds() {
		return feedbackIds;
	}

	public void setFeedbackIds(long[] feedbackIds) {
		this.feedbackIds = feedbackIds;
	}

	/**
	 * 连续炼星成功次数
	 */
	public transient int successStrongStarCount;

	/**
	 * 连续炼星失败次数
	 */
	public transient int failStrongStarCount;

	/**
	 * vip界面显示，同时控制vip级别显示
	 */
	public transient boolean vipDisplay = true;

	@Override
	public void setWage(long value) {
		super.setWage(value);
		setDirty(true, "wage");
		GamePlayerManager.createPlayerMsg(this, 3, false);
	}

	// 一天只能采100个
	private int pickFlowerNum;
	// 上次进入采花时间
	private long lastCaihuaTime;

	public long getLastCaihuaTime() {
		return lastCaihuaTime;
	}

	public transient boolean canPickFlower;

	/**
	 * 得到唯一标示
	 * 
	 * @return
	 */
	public String getPeopleSearchKey() {
		return PeopleSearchManager.sceneName + "_pss_" + this.getId();
	}

	public void setSeeBoothSale(long seeBoothSale) {
		this.seeBoothSale = seeBoothSale;

	}

	public void setLastCaihuaTime(long lastCaihuaTime) {
		this.lastCaihuaTime = lastCaihuaTime;
		setDirty(true, "lastCaihuaTime");
	}

	public int getPickFlowerNum() {
		return pickFlowerNum;
	}

	public void setPickFlowerNum(int pickFlowerNum) {
		this.pickFlowerNum = pickFlowerNum;
		setDirty(true, "pickFlowerNum");
	}

	public long getSeeBoothSale() {
		return seeBoothSale;
	}

	public void setCulture(int value) {
		super.setCulture(value);
		setDirty(true, "culture");
	}

	// 上次寻宝兑换时间
	private long lastExploreChangeTime;
	// 寻宝兑换次数
	private int exploreChangeNum;

	public long getLastExploreChangeTime() {
		return lastExploreChangeTime;
	}

	public void setLastExploreChangeTime(long lastExploreChangeTime) {
		this.lastExploreChangeTime = lastExploreChangeTime;
		setDirty(true, "lastExploreChangeTime");
	}

	public int getExploreChangeNum() {
		return exploreChangeNum;
	}

	public void setExploreChangeNum(int exploreChangeNum) {
		this.exploreChangeNum = exploreChangeNum;
		setDirty(true, "exploreChangeNum");
	}

	@SimpleColumn(saveInterval = 300)
	public long lastUpdateExpTime = 0;

	public long getLastUpdateExpTime() {
		return lastUpdateExpTime;
	}

	public void setLastUpdateExpTime(long lastUpdateExpTime) {
		this.lastUpdateExpTime = lastUpdateExpTime;
		setDirty(true, "lastUpdateExpTime");
	}

	/**
	 * 该用户是否为APPSTORE注册用户
	 * 
	 * @return
	 */
	public boolean isAppStoreChannel() {
		Passport pp = this.getPassport();
		if (pp != null && pp.getLastLoginChannel() != null && pp.getLastLoginChannel().indexOf("APPSTORE") == 0) {
			return true;
		}
		return false;
	}

	public transient long lastQQonlineActivityTime;

	// 批量加载背包物品，不包括装备栏和宠物
	public void loadAllKnapsack() {
		try {
			ArrayList<Long> ids = new ArrayList<Long>();
			// 普通背包
			{
				Knapsack knapsack = getKnapsack_common();
				if (knapsack != null) {
					Cell[] cells = knapsack.getCells();
					if (cells != null) {
						for (Cell cell : cells) {
							if (cell != null && cell.entityId > 0) {
								ids.add(cell.entityId);
							}
						}
					}
				}
			}

			// 防爆背包
			{
				Knapsack knapsack = getKnapsack_fangbao();
				if (knapsack != null) {
					Cell[] cells = knapsack.getCells();
					if (cells != null) {
						for (Cell cell : cells) {
							if (cell != null && cell.entityId > 0) {
								ids.add(cell.entityId);
							}
						}
					}
				}
			}

			if (ids.size() > 0) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				long[] idss = new long[ids.size()];
				for (int i = 0; i < ids.size(); i++) {
					idss[i] = ids.get(i);
				}
				aem.getEntityByIds(idss);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setShopSilver(long shopSilver) {
		if (GreenServerManager.isBindYinZiServer()) {
			if (GreenServerManager.isUseBindProp) {
				YinPiaoEntity yinpiao = getYinPiaoEntity();
				if (yinpiao != null) {
					yinpiao.setHaveMoney(shopSilver);
					QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), yinpiao.getId(), yinpiao.getInfoShow(this));
					addMessageToRightBag(res);
				}
			} else {
				this.shopSilver = shopSilver;
				setDirty(true, "shopSilver");
			}
		}
	}

	public long getShopSilver() {
		if (GreenServerManager.isBindYinZiServer()) {
			if (GreenServerManager.isUseBindProp) {
				YinPiaoEntity yinpiao = getYinPiaoEntity();
				if (yinpiao != null) {
					return yinpiao.getHaveMoney();
				}
				return 0;
			} else {
				return shopSilver;
			}
		}
		return 0;
	}

	public YinPiaoEntity getYinPiaoEntity() {
		Knapsack kn = getKnapsack_common();
		int cellIndex = kn.getArticleCellPos(GreenServerManager.bindpropName);
		if (cellIndex < 0) {
			BillingCenter.loggerA.error("取银票道具失败 [" + getLogString() + "] [" + shopSilver + "]");
			return null;
		}
		ArticleEntity entity = kn.getArticleEntityByCell(cellIndex);
		if (entity == null || !(entity instanceof YinPiaoEntity)) {
			BillingCenter.loggerA.error("取银票entity失败 [" + getLogString() + "] [" + shopSilver + "]");
			return null;
		}
		YinPiaoEntity yinpiao = (YinPiaoEntity) entity;
		return yinpiao;
	}

	// 属性升级次数，当前属性加成，元神升级所需的等级，属性加成，升级所需本尊等级，///升级所需碎片数量
	public int upgradeNums;
	public int currUpgradePoint;
	public transient String costArticleName = Translate.元婴丹;
	public transient String costArticleName20 = Translate.神婴丹;
	public static transient int[] currUpgradePoints = { 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61 };
	public static transient int[] costNum = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4, 6, 9, 12, 15, 18, 21, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25 };
	public static transient int[] costNum20 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4, 6, 9, 12, 15, 18, 21, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25 };
	public transient String[] upgradeExp = { "75903521", "151807041", "227710562", "303614082", "455421123", "519885601", "779828402", "1039771202", "1299714003", "1559656803", "3460244738", "5190367107", "6920489476", "8650611845", "10380734214", "13597026701", "18129368934", "22661711168", "27194053401", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635", "31726395635" };

	private int getIndexnum(int point) {
		for (int i = 0; i < currUpgradePoints.length; i++) {
			if (currUpgradePoints[i] == point) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 元神属性加成升级判断
	 */
	public String upgradeCheak(long exps) {
		if (this.exp < exps) {
			return Translate.text_bourn_003;
		}

		Soul soul = this.getSoul(Soul.SOUL_TYPE_SOUL);
		if (soul == null) {
			return Translate.没有元神不能升级;
		}

		if (this.level < 60) {
			return Translate.元神等级低于60;
		} else if (this.level < 110) {
			return Translate.元神等级低于110;
		} else if (this.level >= 110 && this.level < 150) {
			if (upgradeNums >= 5) {
				return Translate.元神等级低于150;
			}
		} else if (this.level >= 150 && this.level < 190) {
			if (upgradeNums >= 10) {
				return Translate.元神等级低于190;
			}
		} else if (this.level >= 190 && this.level < 220) {
			if (upgradeNums >= 15) {
				return Translate.元神等级低于220;
			}

		} else if (this.level >= 220) {
			if (upgradeNums >= 40) {
				return Translate.已达上限;
			}
		}
		if (upgradeNums > 40) {
			return Translate.已达上限;
		}
		if (upgradeNums >= 10) {
			if (this.getKnapsack_common().countArticle(costArticleName) < costNum[getIndexnum(currUpgradePoints[upgradeNums])]) {
				return Translate.translateString(Translate.您背包的材料不足, new String[][] { { Translate.STRING_1, costArticleName }, { Translate.COUNT_1, costNum[getIndexnum(currUpgradePoints[upgradeNums])] + "" } });
			}
		}

		if (upgradeNums >= 20) {
			if (this.getKnapsack_common().countArticle(costArticleName20) < costNum20[getIndexnum(currUpgradePoints[upgradeNums])]) {
				return Translate.translateString(Translate.您背包的材料不足, new String[][] { { Translate.STRING_1, costArticleName20 }, { Translate.COUNT_1, costNum20[getIndexnum(currUpgradePoints[upgradeNums])] + "" } });
			}
		}

		return "ok";
	}

	/**
	 * 返回玩家的在线时间 疲劳，只是会针对有身份证号的，并且是18岁以下 在线时间3小时的 收入减半，3-6小时递减。6小时没有收益
	 * 
	 * @return
	 */
	public static boolean isUseOnLineTimePiLao = false;
	public static boolean isTest18 = false;

	public int getOnLineTimePiLao() {
		if (!isUseOnLineTimePiLao) {
			return -1;
		}
		try {
			long now = System.currentTimeMillis();
			if (isTest18) {
				if (now - getEnterServerTime() > 2 * 60 * 1000L) {
					if (conn != null) {
						Object o = conn.getAttachmentData("user_shenfen");
						if (o != null) {
							String shenfeng = o.toString();
							if (shenfeng.length() == 18) {
								String nyr = shenfeng.substring(6, 14);
								SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
								long sr = f.parse(nyr).getTime();
								if (now - sr < 18 * 365 * 24 * 60 * 60 * 1000L) {
									if (now - getEnterServerTime() > 6 * 60 * 1000L) {
										return 7;
									} else if (now - getEnterServerTime() > 5 * 60 * 1000L) {
										return 6;
									} else if (now - getEnterServerTime() > 4 * 60 * 1000L) {
										return 5;
									} else if (now - getEnterServerTime() > 3 * 60 * 1000L) {
										return 4;
									} else {
										return 3;
									}
								}
							}
						}
					}
				}
			} else {
				if (now - getEnterServerTime() > 3 * 60 * 60 * 1000L) {
					if (conn != null) {
						Object o = conn.getAttachmentData("user_shenfen");
						if (o != null) {
							String shenfeng = o.toString();
							if (shenfeng.length() == 18) {
								String nyr = shenfeng.substring(6, 14);
								SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
								long sr = f.parse(nyr).getTime();
								if (now - sr < 18 * 365 * 24 * 60 * 60 * 1000L) {
									if (now - getEnterServerTime() > 6 * 60 * 60 * 1000L) {
										return 7;
									} else if (now - getEnterServerTime() > 5 * 60 * 60 * 1000L) {
										return 6;
									} else if (now - getEnterServerTime() > 4 * 60 * 60 * 1000L) {
										return 5;
									} else if (now - getEnterServerTime() > 3 * 60 * 60 * 1000L) {
										return 4;
									} else {
										return 3;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			NewUserEnterServerService.logger.error("getOnLineTimePiLao出错", e);
		}
		return -1;
	}

	/**
	 * 升级属性
	 */
	public boolean upgradeProperty(long costexp) {
		boolean issucc = false;
		try {
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn(this.getPlayerPropsString() + "[元神加点前]");
			}
			int costArticleNums = costNum[getIndexnum(currUpgradePoints[upgradeNums])];
			if (costArticleNums > 0) {
				for (int i = 0; i < costArticleNums; i++) {
					this.removeArticle(costArticleName, "元神升级删除材料");
				}
			}
			int costArticleNums20 = costNum20[getIndexnum(currUpgradePoints[upgradeNums])];
			if (costArticleNums20 > 0) {
				for (int i = 0; i < costArticleNums20; i++) {
					this.removeArticle(costArticleName20, "元神升级删除材料");
				}
			}
			setUpgradeNums(++upgradeNums);
			Soul soul = this.getSoul(this.getCurrSoul().getSoulType() == Soul.SOUL_TYPE_SOUL ? Soul.SOUL_TYPE_BASE : Soul.SOUL_TYPE_SOUL);
			try {
				synchronized (buffs) {
					for (int i = buffs.size() - 1; i >= 0; i--) {
						Buff buff = buffs.get(i);
						if (buff.getTemplate().clearSkillPointNotdisappear) {
							ActiveSkill.logger.warn("升级元神属性[buff:{}][{}][level:{}][playerId:{}][{}]", new Object[] { buff.getTemplateName(), buff.getClass(), buff.getLevel(), this.id, this.name });
							continue;
						}
						if (buff != null && !buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) { // 切换元神不能把渡劫虚弱buff踢掉。
							buff.end(this);
							if (buff.isForover() || buff.isSyncWithClient()) {
								this.removedBuffs.add(buff);
							}
							buffs.remove(i);
							setDirty(true, "buffs");
							ActiveSkill.logger.warn("[升级元神属性去除BUFF] [{}] [{}:{}] [time:{}]", new Object[] { getName(), (buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1)), buff.getTemplateName(), buff.getInvalidTime() });
						}
					}
				}
			} catch (Exception e) {
				ActiveSkill.logger.error("[升级元神属性] [去除buff] [异常] [" + this.getLogString() + "]", e);
			}
			清空所有的BCX值(true);
			copyAttributeFromSoul(soul);
			initSoul();
			if (this.isUpOrDown && !this.isFlying()) {
				Horse rideHorse = HorseManager.getInstance().getHorseById(this.getRidingHorseId(), this);
				rideHorse.upHorse(this);
			}
			long newExp = this.exp - costexp;
			try {
				JiazuEntityManager2.instance.addPracticeAttr(this);
				SoulPithEntityManager.getInst().initSoulPithAttr(this);
			} catch (Exception e) {
				JiazuManager2.logger.error("[新家族] [增加家族技能属性] [异常] [" + this.getLogString() + "]", e);
			}
			// this.setExp(newExp);
			boolean costExp = this.subExp(costexp, "元神加点");
			if (costExp) {
				issucc = true;
			}
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn(this.getPlayerPropsString() + "[元神加点后]");
			}
			if (CoreSubSystem.logger.isWarnEnabled()) {
				CoreSubSystem.logger.warn("[升级属性] [成功] [costExp:" + costExp + "] [消耗物品数量：" + costNum[getIndexnum(currUpgradePoints[upgradeNums])] + "] [消耗材料数量2：" + costNum20[getIndexnum(currUpgradePoints[upgradeNums])] + "] [消耗的经验：" + costexp + "] [玩家之前经验：" + this.exp + "] [升级之后的经验：" + newExp + "] [升级次数：" + upgradeNums + "] [老的百分比：" + this.getSoul(Soul.SOUL_TYPE_SOUL).addPercent + "] [当前属性加成点:" + currUpgradePoints[upgradeNums] + "] [" + this.getLogString() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			CoreSubSystem.logger.warn("[升级属性] [异常] [" + this.getLogString() + "]", e);
		}
		return issucc;
	}

	public void 老玩家领取积分() {
		try {
			int[] vipLevel = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
			int[] rewardPoint = { 100, 700, 1600, 3000, 6000, 12000, 25000, 70000, 140000, 300000, 300000, 300000, 300000, 300000, 300000 };
			String[] mess = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", Translate.VIp绿钻, Translate.VIp蓝钻, Translate.VIp紫钻, Translate.VIp橙钻, Translate.VIp皇冠 };
			if (chargePointsSendTime == 0) {
				int index = -1;
				VipManager vm = VipManager.getInstance();
				int realviplevel = vm.getPlayerVipLevel(this, true);
				if (realviplevel == 0) {
					return;
				}
				for (int i = 0; i < vipLevel.length; i++) {
					if (vipLevel[i] == realviplevel) {
						index = i;
						break;
					}
				}
				if (index > -1) {
					this.setChargePoints(this.getChargePoints() + rewardPoint[index]);

					String msg = Translate.translateString(Translate.老用户积分内容, new String[][] { { Translate.STRING_1, mess[index] }, { Translate.STRING_2, rewardPoint[index] + "" } });
					MailManager.getInstance().sendMail(this.id, new ArticleEntity[] {}, Translate.老用户积分福利, msg, 0, 0, 0, "老玩家领取积分");
					setChargePointsSendTime(System.currentTimeMillis());
					this.sendError(Translate.恭喜获得老用户积分);

					try {
						// 老用户获得积分统计 发老用户获得的积分 定义action为2
						StatClientService statClientService = StatClientService.getInstance();
						
						GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();
						
						String channel4points = (this.getPassport() != null ? this.getPassport().getLastLoginChannel() : "无");
						
						gameChongZhiFlow.setAction(2); // 0 充值 ，1 消耗 2 老用户获得积分
						gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.JIFEN));
						Passport pp = this.getPassport();
						if (pp != null) {
							gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
						}
						gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
						gameChongZhiFlow.setGame(CountryManager.得到国家名(this.getCountry()));
						gameChongZhiFlow.setGameLevel("" + this.getLevel());
						gameChongZhiFlow.setMoney(rewardPoint[index] + 0l);
						gameChongZhiFlow.setQuDao(channel4points);
						String description = "VIP等级为" + this.getVipLevel() + "的老用户获得积分";
						gameChongZhiFlow.setReasonType(description);
						
						gameChongZhiFlow.setTime(System.currentTimeMillis());
						gameChongZhiFlow.setUserName(this.getUsername());
						if (!TestServerConfigManager.isTestServer() ) {
							statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
							ActivitySubSystem.logger.warn("[发老用户获得积分] [成功] [" + this.getId() + "] [积分:" + (rewardPoint[index]) + "] [" + this.getUsername() + "] [" + this.getName() + "]");
						}

					} catch (Exception e) {
						ActivitySubSystem.logger.error("[发统计老用户积分出现异常] [" + this.id + "] [积分:" + rewardPoint[index] + "] [" + this.getUsername() + "] [" + this.getName() + "]", e);
					}

					ActivitySubSystem.logger.warn("[老玩家领取积分] [成功] [vip:" + vipLevel[index] + "] [送积分：" + rewardPoint[index] + "] [当前积分:" + this.getChargePoints() + "] [" + getLogString() + "]");
				}
			}
		} catch (Throwable e) {
			ActivitySubSystem.logger.warn("[老玩家领取积分] [异常] [" + this.getLogString() + "] [" + e + "]");
		}
	}

	public int getUpgradeNums() {
		return upgradeNums;
	}

	public void setUpgradeNums(int upgradeNums) {
		this.upgradeNums = upgradeNums;
		setDirty(true, "upgradeNums");
	}

	public String[] getUpgradeExp() {
		return upgradeExp;
	}

	public void setUpgradeExp(String[] upgradeExp) {
		this.upgradeExp = upgradeExp;
	}

	public String getUserAgent() {
		if (conn != null) {
			Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
			if (o != null && o instanceof USER_CLIENT_INFO_REQ) {
				USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ) o;
				return req.getPhoneType();
			}
		}
		return null;
	}

	public String getIPAddress() {
		if (conn != null) {
			return conn.getRemoteAddress();
		}
		return null;
	}

	@Override
	public long 得到家族修炼() {
		try {
			long paratical = JiazuEntityManager2.instance.getParatical(this);
			if (JiazuEntityManager2.logger.isDebugEnabled()) {
				JiazuEntityManager2.logger.debug("[新家族] [获取家族修炼值] [" + this.getLogString() + "] [修炼值:" + paratical + "]");
			}
			return paratical;
		} catch (Exception e) {
			JiazuEntityManager2.logger.error("[新家族] [获取家族修炼值] [异常] [" + this.getLogString() + "]", e);
			return 0;
		}
	}

	/**
	 * 2013-07-05 删除某个格子的一个物品 请写统计原因
	 * 
	 * @param articleName
	 * @return
	 */
	public synchronized boolean removeArticle(String articleName, String reason) {
		if (articleName == null) {
			return false;
		}
		Knapsack kp = this.getKnapsack_common();
		if (kp != null) {
			int count = kp.countArticle(articleName.trim());
			if (count >= 1) {
				ArticleEntity ae = kp.remove(kp.indexOf(articleName.trim()), reason, true);
				if (ae != null) {
					// 统计
					ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, reason, null);
				}
				return true;
			}
		}
		return false;
	}

	public synchronized ArticleEntity removeArticle(String articleName, String reason, String tt) {
		if (articleName == null) {
			return null;
		}
		Knapsack kp = this.getKnapsack_common();
		if (kp != null) {
			int count = kp.countArticle(articleName.trim());
			if (count >= 1) {
				ArticleEntity ae = kp.remove(kp.indexOf(articleName.trim()), reason, true);
				if (ae != null) {
					// 统计
					ArticleStatManager.addToArticleStat(this, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, reason, null);
				}
				return ae;
			}
		}
		return null;
	}

	/**
	 * 返回 降低治疗的buff 的减量%
	 */
	public int minusHp() {
		if (this.lowerCureLevel > -1) {
			BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.降低治疗);
			if (bt != null) {
				if (bt instanceof BuffTemplate_JiangDiZhiLiao) {
					BuffTemplate_JiangDiZhiLiao jd = (BuffTemplate_JiangDiZhiLiao) bt;
					int bl = lowerCureLevel >= jd.getTreatment().length ? jd.getTreatment().length - 1 : lowerCureLevel;
					if (Skill.logger.isDebugEnabled()) {
						Skill.logger.debug("[技能触发buff] [降低治疗的buff] [ok] [buffname:" + bt.getName() + "] [buff降低治疗的级别:" + lowerCureLevel + "--" + bl + "] [降低百分比：" + jd.getTreatment()[bl] + "] [" + this.getName() + "]");
					}
					return jd.getTreatment()[bl];
				}
			}
		}
		return 0;
	}

	/**
	 * 重置兽魁状态
	 */
	public void resetShouStat(String reson) {
		if (this.getCareer() == 5) {
			// if(this.getShouStat() == 1){
			// Avata av = ResourceManager.getInstance().getAvata(this);
			// this.setAvata(av.avata);
			// this.setAvataType(av.avataType);
			// Game.logger.warn("[兽魁上线重置avta22222] [reson:{}] [avata:{}] [oldavata:{}] [avataType:{}] [{}]",new
			// Object[]{reson,av.avata,this.oldAvata,av.avataType,this.getLogString()});
			// }
			this.setShouStat(0);
			this.setSkillPoints(3);
			Game.logger.warn("[兽魁上线重置状态] [{}] [avata:{}] [avataType:{}] [{}]", new Object[] { reson, this.avata, this.avataType, this.getLogString() });
		}
	}

	public void takeOneTaskForCareer() {
		try {
			if (this.getCareer() == 5 && this.getLevel() == 1) {
				Task task = TaskManager.getInstance().getTask(newCareerFirstTaskId);
				if (task == null) {
					TaskManager.logger.warn("[斗魁接取新手任务] [出错:任务不存在] [任务id:{}] [{}]", new Object[] { newCareerFirstTaskId, this.getLogString() });
					return;
				}
				CompoundReturn cr = this.takeOneTask(task, false, null);
				if (cr == null || !cr.getBooleanValue()) {
					TaskManager.logger.warn("[斗魁接取新手任务] [出错:不满足条件{}] [{}] [任务名：{}] [任务id:{}] [{}]", new Object[] { (cr == null ? "nul" : cr.getStringValue()), (cr == null ? "nul" : cr.getBooleanValue()), task.getName(), newCareerFirstTaskId, this.getLogString() });
					return;
				} else {
					CompoundReturn result = this.addTaskByServer(task);
					if (result != null && result.getBooleanValue()) {
						TaskManager.logger.warn("[斗魁接取新手任务] [成功] [{}] [任务名：{}] [任务id:{}] [{}]", new Object[] { (cr == null ? "nul" : cr.getBooleanValue()), task.getName(), newCareerFirstTaskId, this.getLogString() });
					} else {
						this.sendError(TaskSubSystem.getInstance().getInfo(result.getIntValue()));
						return;
					}
				}
			}
		} catch (Exception e) {
			TaskManager.logger.warn("[斗魁接取新手任务] [异常] [{}] [{}]", new Object[] { this.getLogString(), e });
		}
	}

	/**
	 * 影魅 狼印
	 * 人阶10 获得当自身生命值少于30%时增加自身移动速度30点10秒（不受减速控制）、（自身移动速度+坐骑速度）+100、、、（（自身移动速度+坐骑速度）*100）+100 触发间隔60秒
	 * 地阶20 获得当自身生命值少于30%时增加自身移动速度60点10秒
	 * 天阶30 获得当自身生命值少于30%时增加自身移动速度100点10秒
	 */
	public int getMoreSpeed() {
		int step = SkEnhanceManager.getInst().getSlotStep(this, 3);
		if (step > 0) {
			switch (step) {
			case 1:
				return 2;
			case 2:
				return 5;
			case 3:
				return 9;
			default:
				return 0;
			}
		}
		return 0;
	}

	public int getHuntLevel(long id) {
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
		if (ae != null && ae instanceof HuntLifeArticleEntity) {
			HuntArticleExtraData extraData = ((HuntLifeArticleEntity) ae).getExtraData();
			return (extraData == null ? 0 : extraData.getLevel());
		}
		return 0;
	}
	
	private int cardIds [] = {0,0,0};
	private long cardEndDate [] = {0,0,0};
	private long lastRewardDate [] = {0,0,0};
	private long buyRecord [] = {0,0,0};
	
	

	public long[] getBuyRecord() {
		return buyRecord;
	}

	public void setBuyRecord(long[] buyRecord) {
		this.buyRecord = buyRecord;
		setDirty(true, "buyRecord");
	}

	public long[] getLastRewardDate() {
		return lastRewardDate;
	}

	public void setLastRewardDate(long[] lastRewardDate) {
		this.lastRewardDate = lastRewardDate;
		setDirty(true, "lastRewardDate");
	}

	public int[] getCardIds() {
		return cardIds;
	}

	public void setCardIds(int[] cardIds) {
		this.cardIds = cardIds;
		setDirty(true, "cardIds");
	}

	public long[] getCardEndDate() {
		return cardEndDate;
	}

	public void setCardEndDate(long[] cardEndDate) {
		this.cardEndDate = cardEndDate;
		setDirty(true, "cardEndDate");
	}

	public boolean ownMonthCard(CardFunction function){
		for(int i=0;i<cardIds.length;i++){
			if(cardIds[i] > 0){//充过值
				if(cardEndDate[i] > System.currentTimeMillis()){//有效期内
					CardConfig c = ChargeManager.getInstance().getCards().get(cardIds[i]);
					if(c != null){
						for(int id : c.getIds()){//有效的功能
							if(id > 0 && id == function.getId()){
								return true;
							}
						}
					}else{
						ActivitySubSystem.logger.warn("[检查玩家月卡] [错误:功能不存在] [id:{}] [ids:{}] [dates:{}] [{}]",
								new Object[]{cardIds[i],Arrays.toString(cardIds),Arrays.toString(cardEndDate),getLogString()});
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public byte getSpriteType() {
		return Sprite.SPRITE_TYPE_PLAYER;
	}

	@Override
	public int getCritFactor() {
		return 200;
	}

	public long getChargePointsSendTime() {
		return chargePointsSendTime;
	}

	public void setChargePointsSendTime(long chargePointsSendTime) {
		this.chargePointsSendTime = chargePointsSendTime;
		setDirty(true, "chargePointsSendTime");
	}

	public long getJjcPoint() {
		return jjcPoint;
	}

	public void setJjcPoint(long jjcPoint) {
		this.jjcPoint = jjcPoint;
		setDirty(true, "jjcPoint");
	}

	public int getOffsetPercent() {
		return offsetPercent;
	}

	public void setOffsetPercent(int offsetPercent) {
		this.offsetPercent = offsetPercent;
	}

	public String[] getGameMsgs() {
		if(gameMsgs == null){
			try {
				setGameMsgs(new String[gggNum]);
				GamePlayerManager.createPlayerMsg(this, 0, false);
				GamePlayerManager.createPlayerMsg(this, 1, false);
				GamePlayerManager.createPlayerMsg(this, 2, false);
				GamePlayerManager.createPlayerMsg(this, 3, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return gameMsgs;
	}

	public void setGameMsgs(String[] gameMsgs) {
		this.gameMsgs = gameMsgs;
		setDirty(true, "gameMsgs");
	}

	public int getMagicWeaponDevourPercent() {
		return magicWeaponDevourPercent;
	}

	public void setMagicWeaponDevourPercent(int magicWeaponDevourPercent) {
		this.magicWeaponDevourPercent = magicWeaponDevourPercent;
	}

	public long getActiveMagicWeaponId() {
		return activeMagicWeaponId;
	}

	public void setActiveMagicWeaponId(long activeMagicWeaponId) {
		this.activeMagicWeaponId = activeMagicWeaponId;
	}

	public int getPetExpPercent() {
		return petExpPercent;
	}

	public void setPetExpPercent(int petExpPercent) {
		this.petExpPercent = petExpPercent;
	}

	public Buff getBuff(Class<?> clazz) {
		for (Buff buff : buffs) {
			if (buff.getClass().equals(clazz)) {
				return buff;
			}
		}
		return null;
	}

	public byte getImmuType() {
		return immuType;
	}

	public void setImmuType(byte immuType) {
		this.immuType = immuType;
	}

	public long getLastImmuTime() {
		return lastImmuTime;
	}

	public void setLastImmuTime(long lastImmuTime) {
		this.lastImmuTime = lastImmuTime;
	}

	public double getExtraHejiu() {
		return extraHejiu;
	}

	public void setExtraHejiu(double extraHejiu) {
		this.extraHejiu = extraHejiu;
	}

	public long getOne_day_rmb() {
		return one_day_rmb;
	}

	public void setOne_day_rmb(long one_day_rmb) {
		this.one_day_rmb = one_day_rmb;
	}

	public int getRecoverHpHuDun() {
		return recoverHpHuDun;
	}

	public void setRecoverHpHuDun(int recoverHpHuDun) {
		this.recoverHpHuDun = recoverHpHuDun;
		if (recoverHpHuDun > 0 && this.getEncloser() <= 0) {
			this.setEncloser((byte) 1);
		} else if (recoverHpHuDun <= 0) {
			this.setEncloser((byte) 0);
		}
	}

	public int getPlayerRank() {
		return playerRank;
	}

	public void setPlayerRank(int playerRank) {
		if (playerRank != this.playerRank) {
			int[] avataTypes = new int[] { 0, 13 };
			String[] avatas = new String[2];
			int[] avataTypes2 = new int[] { 0, 13 };
			String[] avatas2 = new String[2];
			if (this.getAvataRace().equals(Constants.race_human_new)) {
				avatas = bianshenAvata4Feisheng[this.playerRank];
				avatas2 = bianshenAvata4Feisheng[playerRank];
			} else {
				avatas = bianshenAvata[this.playerRank];
				avatas2 = bianshenAvata[playerRank];
			}
			NOTICE_CLIENT_PLAYE_CARTOON2_REQ req = new NOTICE_CLIENT_PLAYE_CARTOON2_REQ(GameMessageFactory.nextSequnceNum(), avataTypes, avatas, avataTypes2, avatas2);
			this.addMessageToRightBag(req);
		}
		this.playerRank = playerRank;
		setDirty(true, "playerRank");
		this.modifyShouAvata();
	}

	public HuntLifeEntity getHuntLifr() {
		return huntLifr;
	}

	public void setHuntLifr(HuntLifeEntity huntLifr) {
		this.huntLifr = huntLifr;
	}

	public static String getLevelDes(int level) {
		if (level <= 220) {
			return level + "";
		} else {
			return Translate.仙 + (level - 220);
		}
	}

	public int getTempSpeedC() {
		return tempSpeedC;
	}

	public void setTempSpeedC(int tempSpeedC) {
		this.tempSpeedC = tempSpeedC;
		this.setSpeedC(100000);
	}

	public int getViewWidth() {
		try {
//			if (DisasterManager.getInst().isPlayerInGame(this)) {
//				return DisasterConstant.viewWith;
//			}
		} catch (Exception e) {

		}
		return viewWidth;
	}

	transient long weekPoint;

	public long getWeekPoint() {
		return weekPoint;
	}

	public void setWeekPoint(long weekPoint) {
		this.weekPoint = weekPoint;
	}

	public int getViewHeight() {
		try {
//			if (DisasterManager.getInst().isPlayerInGame(this)) {
//				return DisasterConstant.viewHeight;
//			}
		} catch (Exception e) {

		}
		return viewHeight;
	}

	public SoulPithEntity getSoulPith() {
		return soulPith;
	}

	public transient Boolean hasAuth = null;

	public boolean modifyVipInfo(String cliendId) {
		long startTime = System.currentTimeMillis();
		if (VipManager.isTest) {
			return true;
		}
		if (hasAuth == null) {
			MODIFY_VIP_INFO_REQ req = new MODIFY_VIP_INFO_REQ(GameMessageFactory.nextSequnceNum(), this.getUsername(), cliendId, new String[] {});
			try {
				MODIFY_VIP_INFO_RES res = (MODIFY_VIP_INFO_RES) MieshiGatewayClientService.getInstance().sendMessageAndWaittingResponse(req, 5000);
				if (res != null) {
					hasAuth = res.getResult();
					if (VipManager.logger.isInfoEnabled()) {
						VipManager.logger.info("[检查是否可以修改vip资料] [成功] [结果:{}] [cliendId:{}] [{}] [cost:{}ms]", new Object[] { (res == null ? "null" : res.getResult()), cliendId, this.getLogString(), (System.currentTimeMillis() - startTime) });
					}
				}
			} catch (Exception e) {
				VipManager.logger.info("[检查是否可以修改vip资料] [异常] [cliendId:{}] [{}] [{}]", new Object[] { cliendId, this.getLogString(), e });
				e.printStackTrace();
				return false;
			}
		}
		if (hasAuth == null) {
			return false;
		} else {
			return hasAuth.booleanValue();
		}
	}

	public void setSoulPith(SoulPithEntity soulPith) {
		this.soulPith = soulPith;
	}

	public VipAgent getVipAgent() {
		return vipAgent;
	}

	public void setVipAgent(VipAgent vipAgent) {
		this.vipAgent = vipAgent;
		setDirty(true, "vipAgent");
	}

	public String getClientId() {
		return getClientInfo("clientId");
	}

	public Knapsack getKnapsacks_warehouse() {
		return knapsacks_warehouse;
	}

	public void setKnapsacks_warehouse(Knapsack knapsacks_warehouse) {
		this.knapsacks_warehouse = knapsacks_warehouse;
		this.setDirty(true, "knapsacks_warehouse");
	}

	public long getLastMoveTime() {
		return lastMoveTime;
	}

	public void setLastMoveTime(long lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
	}

	public int[] getInLimitDituTime() {
		return inLimitDituTime;
	}

	public void setInLimitDituTime(int[] inLimitDituTime) {
		this.inLimitDituTime = inLimitDituTime;
		this.setDirty(true, "inLimitDituTime");
	}

	public long[] getLastInLimitDituTime() {
		return lastInLimitDituTime;
	}

	public void setLastInLimitDituTime(long[] lastInLimitDituTime) {
		this.lastInLimitDituTime = lastInLimitDituTime;
		this.setDirty(true, "lastInLimitDituTime");
	}

	public int getInBaGuaXianQueTime() {
		return inBaGuaXianQueTime;
	}

	public void setInBaGuaXianQueTime(int inBaGuaXianQueTime) {
		this.inBaGuaXianQueTime = inBaGuaXianQueTime;
		setDirty(true, "inBaGuaXianQueTime");
	}

	public long getLastInBaGuaXianQueTime() {
		return lastInBaGuaXianQueTime;
	}

	public void setLastInBaGuaXianQueTime(long lastInBaGuaXianQueTime) {
		this.lastInBaGuaXianQueTime = lastInBaGuaXianQueTime;
		setDirty(true, "lastInBaGuaXianQueTime");
	}

	public long getHonorPoint() {
		return honorPoint;
	}

	public void setHonorPoint(long honorPoint) {
		this.honorPoint = honorPoint;
		setHonorPoint(honorPoint, false);
	}

	public void setHonorPoint(long honorPoint, boolean isDirty) {
		this.honorPoint = honorPoint;
		super.setHonorPoint(honorPoint);
		if (isDirty) {
			setDirty(true, "honorPoint");
		}
	}

	public int getRewardHonorPointTimes() {
		return rewardHonorPointTimes;
	}

	public void setRewardHonorPointTimes(int rewardHonorPointTimes) {
		this.rewardHonorPointTimes = rewardHonorPointTimes;
		setDirty(true, "rewardHonorPointTimes");
	}

	public int getMedicineDiscount() {
		return medicineDiscount;
	}

	public void setMedicineDiscount(int medicineDiscount) {
		this.medicineDiscount = medicineDiscount;
	}

	public int getAliveHpPercent() {
		return aliveHpPercent;
	}

	public void setAliveHpPercent(int aliveHpPercent) {
		this.aliveHpPercent = aliveHpPercent;
	}

	public int getRepairDiscount() {
		return repairDiscount;
	}

	public void setRepairDiscount(int repairDiscount) {
		this.repairDiscount = repairDiscount;
	}

	public int getShouhunAttr() {
		return shouhunAttr;
	}

	public void setShouhunAttr(int shouhunAttr) {
		this.shouhunAttr = shouhunAttr;
	}

	public long getRewardHonorPointDate() {
		return rewardHonorPointDate;
	}

	public void setRewardHonorPointDate(long rewardHonorPointDate) {
		this.rewardHonorPointDate = rewardHonorPointDate;
		setDirty(true, "rewardHonorPointDate");
	}

	private transient PlayerXianLingData xianlingData;

	public PlayerXianLingData getXianlingData() {
		return xianlingData;
	}

	public void setXianlingData(PlayerXianLingData xianlingData) {
		this.xianlingData = xianlingData;
	}

	public int getRefreshCrossShopTimes() {
		return refreshCrossShopTimes;
	}

	public void setRefreshCrossShopTimes(int refreshCrossShopTimes) {
		this.refreshCrossShopTimes = refreshCrossShopTimes;
		setDirty(true, "refreshCrossShopTimes");
	}

	public long getLastRefreshCrossShopDate() {
		return lastRefreshCrossShopDate;
	}

	public void setLastRefreshCrossShopDate(long lastRefreshCrossShopDate) {
		this.lastRefreshCrossShopDate = lastRefreshCrossShopDate;
		setDirty(true, "lastRefreshCrossShopDate");
	}

}
