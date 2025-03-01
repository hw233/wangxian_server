package com.fy.engineserver.achievement;

import com.fy.engineserver.achievement.AchievementConf.RecordType;

public enum RecordAction {

	等级(1, "等级", RecordType.记录最大值),
	境界(2, "境界", RecordType.记录最大值),
	获得技能(3, "获得技能", RecordType.累加),
	技能升级(4, "技能升级", RecordType.记录最大值),
	获得心法(5, "获得心法", RecordType.累加),
	心法升级(6, "心法升级", RecordType.记录最大值),
	开启元神(7, "开启元神", RecordType.只记录第一次),
	元神等级(8, "元神等级", RecordType.记录最大值),
	论道完成次数(9, "论道完成次数", RecordType.累加),
	仙缘完成次数(10, "仙缘完成次数", RecordType.累加),
	寻宝次数(11, "寻宝次数", RecordType.累加),
	指定藏宝图寻宝(12, "指定藏宝图寻宝", RecordType.累加),
	开启古董(13, "开启古董", RecordType.累加),
	获得银子(14, "获得银子", RecordType.记录最大值),
	喝酒次数(15, "喝酒次数", RecordType.累加),
	喝白色品质以上酒次数(16, "喝白色品质以上酒次数", RecordType.累加),
	喝紫酒次数(17, "喝紫酒次数", RecordType.累加),
	喝橙酒次数(18, "喝橙酒次数", RecordType.累加),
	获得青龙圣地(19, "获得青龙圣地", RecordType.只记录第一次),
	获得白虎圣地(20, "获得白虎圣地", RecordType.只记录第一次),
	获得朱雀圣地(21, "获得朱雀圣地", RecordType.只记录第一次),
	获得玄武圣地(22, "获得玄武圣地", RecordType.只记录第一次),
	获得麒麟圣地(23, "获得麒麟圣地", RecordType.只记录第一次),
	获得5种圣地(24, "获得5种圣地", RecordType.累加),
	获得村战胜利(25, "获得村战胜利", RecordType.累加),
	获得城市争夺战胜利(26, "获得城市争夺战胜利", RecordType.累加),
	参加武斗次数(27, "参加武斗次数", RecordType.累加),
	武斗获胜次数(28, "武斗获胜次数", RecordType.累加),
	武斗连胜次数(29, "武斗连胜次数", RecordType.设置),					
	武斗前50名次数(30, "武斗前50名次数", RecordType.累加),
	武斗前10名次数(31, "武斗前10名次数", RecordType.累加),
	武斗冠军次数(32, "武斗冠军次数", RecordType.累加),
	武斗亚军次数(33, "武斗亚军次数", RecordType.累加),
	武斗季军次数(34, "武斗季军次数", RecordType.累加),
	千层塔活动次数(35, "千层塔活动次数", RecordType.累加),
	千层塔完成各道(36, "千层塔完成各道", RecordType.累加),
	世界BOSS(37, "参加一次世界BOSS", RecordType.只记录第一次),
	答题全部正确次数(38, "答题全部正确次数", RecordType.累加),
	文采值累计(39, "文采值累计", RecordType.累加),
	刺探完成次数(40, "刺探完成次数", RecordType.累加),
	刺探紫BUFF次数(41, "刺探紫BUFF次数", RecordType.累加),
	刺探橙BUFF次数(42, "刺探橙BUFF次数", RecordType.累加),
	偷砖紫BUFF次数(43, "偷砖紫BUFF次数", RecordType.累加),
	偷砖橙BUFF次数(44, "偷砖橙BUFF次数", RecordType.累加),
	个人运镖紫BUFF次数(45, "个人运镖紫BUFF次数", RecordType.累加),
	个人运镖橙BUFF次数(46, "个人运镖橙BUFF次数", RecordType.累加),
	镖局押镖次数(47, "镖局押镖次数", RecordType.累加),
	累计祈福次数(48, "累计祈福次数", RecordType.累加),
	祈福全开次数(49, "祈福全开次数", RecordType.累加),
	获得庄园(50, "获得庄园", RecordType.只记录第一次),
	仙府升级建筑次数(51, "仙府升级建筑次数", RecordType.累加),
	仙府开垦土地次数(52, "仙府开垦土地次数", RecordType.累加),
	仙府主建筑达到等级(53, "仙府主建筑达到等级", RecordType.记录最大值),
	仙府全部建筑达到等级(54, "仙府全部建筑达到等级", RecordType.记录最大值),
	使用高级鲁班令次数(55, "使用高级鲁班令次数", RecordType.累加),
	收获自己农作物个数(56, "收获自己农作物个数", RecordType.累加),
	种植农作物次数(57, "种植农作物次数", RecordType.累加),
	偷取农作物个数(58, "偷取农作物个数", RecordType.累加),
	偷取橙色果实次数(59, "偷取橙色果实次数", RecordType.累加),
	获得鲜花数量(60, "获得鲜花数量", RecordType.累加),
	获得糖果数量(61, "获得糖果数量", RecordType.累加),
	求婚被拒绝次数(62, "求婚被拒绝次数", RecordType.累加),
	传功次数(63, "传功次数", RecordType.累加),
	被传功次数(64, "被传功次数", RecordType.累加),
	拜师次数(65, "拜师次数", RecordType.累加),
	叛师次数(66, "叛师次数", RecordType.累加),
	收徒次数(67, "收徒次数", RecordType.累加),
	出师次数(68, "出师次数", RecordType.累加),
	结义次数(69, "结义次数", RecordType.累加),
	结义人数(70, "结义人数", RecordType.记录最大值),
	结婚次数(71, "结婚次数", RecordType.累加),
	离婚次数(72, "离婚次数", RecordType.累加),
	结婚规模(73, "结婚规模", RecordType.记录最大值),
	好友数量(74, "好友数量", RecordType.累加),
	仇人数量(75, "仇人数量", RecordType.累加),
	原地复活次数(76, "原地复活次数", RecordType.累加),
	击杀敌国玩家次数(77, "击杀敌国玩家次数", RecordType.累加),
	击杀敌国国王次数(78, "击杀敌国国王次数", RecordType.累加),
	击杀红名次数(79, "击杀红名次数", RecordType.累加),
	被关监狱次数(80, "被关监狱次数", RecordType.累加),
	被禁言次数(81, "被禁言次数", RecordType.累加),
	加入家族次数(82, "加入家族次数", RecordType.累加),
	家族到达等级(83, "家族到达等级", RecordType.记录最大值),
	加入宗派次数(84, "加入宗派次数", RecordType.累加),
	组队次数(85, "组队次数", RecordType.累加),
	彩世次数(86, "彩世次数", RecordType.累加),
	孵化宠物次数(87, "孵化宠物次数", RecordType.累加),
	宠物获得百里挑一次数(88, "宠物获得百里挑一次数", RecordType.累加),
	宠物获得千载难逢次数(89, "宠物获得千载难逢次数", RecordType.累加),
	宠物炼妖次数(90, "宠物炼妖次数", RecordType.累加),
	宠物炼妖属性满值个数(91, "宠物炼妖属性满值个数", RecordType.记录最大值),
	宠物幻化次数(92, "宠物幻化次数", RecordType.累加),
	宠物最大星数(93, "宠物最大星数", RecordType.记录最大值),
	宠物合体次数(94, "宠物合体次数", RecordType.累加),
	宠物技能个数(95, "宠物技能个数", RecordType.记录最大值),
	宠物繁殖次数(96, "宠物繁殖次数", RecordType.累加),
	连任国王次数(97, "连任国王次数", RecordType.设置),
	国战胜利次数(98, "国战胜利次数", RecordType.累加),
	拍卖次数(99, "拍卖次数", RecordType.累加),
	竞拍次数(100, "竞拍次数", RecordType.累加),
	求购次数(101, "求购次数", RecordType.累加),
	快速出售次数(102, "快速出售次数", RecordType.累加),
	收取邮件次数(103, "收取邮件次数", RecordType.累加),
	扩展背包格数(104, "扩展背包格数", RecordType.累加),
	扩展仓库格数(105, "扩展仓库格数", RecordType.累加),
	获得防爆背包格数(106, "获得防爆背包格数", RecordType.记录最大值),
	获得地榜传说装备个数(107, "获得地榜传说装备个数", RecordType.只记录第一次),
	获得一套完美紫(108, "获得一套完美紫", RecordType.只记录第一次),
	获得一套橙装(109, "获得一套橙装", RecordType.只记录第一次),
	获得一套完美橙(110, "获得一套完美橙", RecordType.只记录第一次),
	获得天榜传说装备个数(111, "获得天榜传说装备个数", RecordType.累加),
	获得星级套的星级(112, "获得星级套的星级", RecordType.记录最大值),
	连续炼星成功次数(113, "连续炼星成功次数", RecordType.设置),
	连续炼星失败次数(114, "连续炼星失败次数", RecordType.设置),
	合成装备次数(115, "合成装备次数", RecordType.累加),
	鉴定装备次数(116, "鉴定装备次数", RecordType.累加),		
	铭刻装备次数(117, "铭刻装备次数", RecordType.累加),		
	合成宝石等级(118, "合成宝石等级", RecordType.记录最大值),
	获得坐骑阶数(119, "获得坐骑阶数", RecordType.记录最大值),
	获得坐骑装备套数(120, "获得坐骑装备套数", RecordType.只记录第一次),
	完成任务个数(121, "完成任务个数", RecordType.累加),
	完成日常任务次数(122, "完成日常任务次数", RecordType.累加),
	进入昆华古城(123, "进入昆华古城次数", RecordType.只记录第一次),
	进入昆仑圣殿(124, "进入昆仑圣殿次数", RecordType.只记录第一次),
	探索所有地图(125, "探索所有地图", RecordType.只记录第一次),
	获得飞行坐骑次数(126, "获得飞行坐骑次数", RecordType.累加),
	VIP等级(127, "VIP等级", RecordType.记录最大值),
	杀死怪物(128, "杀死怪物", RecordType.累加),
	炼星次数(129, "炼星次数", RecordType.累加),
	获得怒气技能(130, "获得怒气技能", RecordType.累加),									
	全身装备最大星级(131, "全身装备最大星级", RecordType.记录最大值),
	宠物高级炼妖次数(132, "宠物高级炼妖次数", RecordType.累加),
	宠物变异等级(133, "宠物变异等级", RecordType.记录最大值),
	宠物对应性格技能数量(134, "宠物对应性格技能数量", RecordType.记录最大值),
	使用秒时间次数(135, "使用秒时间次数", RecordType.累加),
	获得碧虚青鸾次数(136, "获得碧虚青鸾次数", RecordType.累加),
	宗派内家族上限(137, "宗派内家族上限", RecordType.累加),
	偷砖完成次数(138, "偷砖完成次数", RecordType.累加),
	运镖完成次数(139, "运镖完成次数", RecordType.累加),
	获得八爪鱼坐骑次数(140, "获得八爪鱼坐骑次数", RecordType.累加),
	获得莲花坐骑次数(141, "获得莲花坐骑次数", RecordType.只记录第一次),
	获得飞扇坐骑次数(142, "获得飞扇坐骑次数", RecordType.只记录第一次),
	获得葫芦坐骑次数(143, "获得葫芦坐骑次数", RecordType.只记录第一次),
	获得飞龙坐骑次数(144, "获得飞龙坐骑次数", RecordType.只记录第一次),
	获得飞剑坐骑次数(145, "获得飞剑坐骑次数", RecordType.只记录第一次),
	获得青龙宠物(146, "获得青龙宠物", RecordType.只记录第一次),
	获得玄武宠物(147, "获得玄武宠物", RecordType.只记录第一次),
	获得朱雀宠物(148, "获得朱雀宠物", RecordType.只记录第一次),
	获得白虎宠物(149, "获得白虎宠物", RecordType.只记录第一次),
	获得麒麟宠物(150, "获得麒麟宠物", RecordType.只记录第一次),
	获得所有圣兽(151, "获得所有圣兽", RecordType.累加),
	击杀敌国官员次数(152, "击杀敌国官员次数", RecordType.累加),
	通关地狱道次数(153, "通关地狱道次数", RecordType.累加),
	通关饿鬼道次数(154, "通关饿鬼道次数", RecordType.累加),
	通关畜生道次数(155, "通关畜生道次数", RecordType.累加),
	通关修罗道次数(156, "通关修罗道次数", RecordType.累加),
	通关人间道次数(157, "通关人间道次数", RecordType.累加),
	通关天道次数(158, "通关天道次数", RecordType.累加),
	七十不算容易(159, "七十不算容易", RecordType.只记录第一次),
	一百一要用点时间(160, "一百一要用点时间", RecordType.只记录第一次),
	一百八要花大功夫(161, "一百八要花大功夫", RecordType.只记录第一次),
	昆仑我第一(162, "昆仑我第一", RecordType.只记录第一次),
	万法我第一(163, "万法我第一", RecordType.只记录第一次),
	九州我第一(164, "九州我第一", RecordType.只记录第一次),
	三界最快修罗(165, "三界最快修罗", RecordType.只记录第一次),
	三界最快影魅(166, "三界最快影魅", RecordType.只记录第一次),
	三界最快九黎(167, "三界最快九黎", RecordType.只记录第一次),
	三界最快仙心(168, "三界最快仙心", RecordType.只记录第一次),
	获得棉花糖果数量(169, "获得棉花糖果数量", RecordType.累加),
	世界击杀次数(170, "世界击杀次数", RecordType.累加),
	WBoss周榜伤害最高(171, "WBoss周榜伤害最高", RecordType.只记录第一次),
	WBoss周榜击杀最高(172, "WBoss周榜击杀最高", RecordType.只记录第一次),
	渡劫重数(173, "渡劫重数", RecordType.记录最大值),
	大师技能最大重数(174, "大师技能最大重数", RecordType.记录最大值),			
	装备一个法宝(175, "装备一个法宝", RecordType.只记录第一次),			
	法宝神识(176, "法宝神识", RecordType.累加),						
	法宝加持(177, "法宝加持", RecordType.累加),						
	激活法宝隐藏属性(178, "激活法宝隐藏属性", RecordType.累加),				
	获得一套绿装(179, "获得一套绿装", RecordType.只记录第一次),
	获得一套蓝色以上装(180, "获得一套蓝色以上装", RecordType.只记录第一次),
	刷新法宝隐藏属性次数(181, "刷新法宝隐藏属性次数", RecordType.累加),
	杀死千年狐妖(182, "杀死千年狐妖", RecordType.只记录第一次),			
	穿戴一件武器(183, "穿戴一件武器", RecordType.只记录第一次),			
	喝白酒次数(184, "喝白酒次数", RecordType.累加),					
	喝蓝酒次数(185, "喝蓝酒次数", RecordType.累加),					
	杀死九转元胎(186, "杀死游荡野鬼", RecordType.只记录第一次),				
	穿一件蓝色以上装备(187, "穿一件蓝色以上装备", RecordType.只记录第一次),					
	第二个基础技能等级(188, "第二个基础技能等级", RecordType.记录最大值),					
	宠物鉴定次数(189, "宠物鉴定次数", RecordType.累加),					
	装备修理次数(190, "装备修理次数", RecordType.累加),					
	签到次数(191, "签到次数", RecordType.累加),					
	使用白色封魔录次数(192, "使用白色封魔录次数", RecordType.累加),				
	使用蓝色封魔录次数(193, "使用蓝色封魔录次数", RecordType.累加),				
	使用封魔录次数(194, "使用封魔录次数", RecordType.累加),				
	基础技能满级个数(195, "基础技能满级个数", RecordType.累加),				
	镶嵌宝石次数(196, "镶嵌宝石次数", RecordType.累加),				
	活跃商城购买道具次数(197, "活跃商城购买道具次数", RecordType.累加),				
	喝绿色以上酒次数(198, "喝绿色以上酒次数", RecordType.累加),				
	使用绿色以上封魔录次数(199, "使用绿色以上封魔录次数", RecordType.累加),				
	穿戴一套61级以上紫装(200, "穿戴一套61级以上紫装", RecordType.只记录第一次),				
	摆摊次数(201, "摆摊次数", RecordType.累加),				
	国内寻宝次数(202, "国内寻宝次数", RecordType.累加),				
	国外寻宝次数(203, "国外寻宝次数", RecordType.累加),				
	国内仙缘次数(204, "国内仙缘次数", RecordType.累加),				
	国外仙缘次数(205, "国外仙缘次数", RecordType.累加),				
	国内论道次数(206, "国内论道次数", RecordType.累加),				
	国外论道次数(207, "国外论道次数", RecordType.累加),
	使用蓝色以上封魔录次数(208, "使用蓝色以上封魔录次数", RecordType.累加),
	穿戴一套81级以上完美紫装(209, "穿戴一套81级以上完美紫装", RecordType.只记录第一次),
	元神穿戴一套41级以上装备(210, "元神穿戴一套41级以上装备", RecordType.只记录第一次),
	喝蓝色以上酒次数(211, "喝蓝色以上酒次数", RecordType.累加),
	本尊穿戴一套101以上橙装(212, "本尊穿戴一套101以上橙装", RecordType.只记录第一次),		
	使用高级鉴定次数(213, "使用高级鉴定次数", RecordType.累加),		
	穿戴一件N星以上武器(214, "穿戴一件N星以上武器", RecordType.记录最大值),		
	内攻或外攻心法最大重数(215, "内攻或外攻心法最大重数", RecordType.记录最大值),		
	进入110级恶魔城堡次数(217, "进入110级恶魔城堡次数", RecordType.累加),		
	进入150级恶魔城堡次数(218, "进入150级恶魔城堡次数", RecordType.累加),		
	进入190级恶魔城堡次数(219, "进入190级恶魔城堡次数", RecordType.累加),		
	进入220级恶魔城堡次数(220, "进入220级恶魔城堡次数", RecordType.累加),		
	喝紫色以上酒次数(221, "喝紫色以上酒次数", RecordType.累加),		
	使用紫色以上封魔录次数(222, "使用紫色以上封魔录次数", RecordType.累加),		
	大师技能总重数(223, "大师技能总重数", RecordType.累加),		
	宠物进阶次数(224, "宠物进阶次数", RecordType.累加),		
	宠物技能抽取次数(225, "宠物技能抽取次数", RecordType.累加),		
	购买单月礼包次数(226, "购买单月礼包次数", RecordType.累加),								
	宠物最大阶数(227, "宠物最大阶数", RecordType.记录最大值),		
	宠物天赋技能最大数(228, "宠物天赋技能最大数", RecordType.记录最大值),		
	宠物最大妖魂值(229, "宠物最大妖魂值", RecordType.记录最大值),		
	羽化装备次数(230, "羽化装备次数", RecordType.累加),									
	宠物进阶到终阶次数(231, "宠物进阶到终阶次数", RecordType.累加),		
	飞升(232, "飞升", RecordType.只记录第一次),		
	获得一只拥有先天技能的宠物(233, "获得一只拥有先天技能的宠物", RecordType.只记录第一次),		
	宠物携带等级升至90级次数(234, "宠物携带等级升至90级次数", RecordType.累加),		
	宠物岛击杀圣兽精英(235, "宠物岛击杀圣兽精英", RecordType.只记录第一次),						
	完成平定四方次数(236, "完成平定四方次数", RecordType.累加),								
	福地洞天击杀宝怪(237, "福地洞天击杀宝怪", RecordType.只记录第一次),							
	领取家族工资次数(238, "领取家族工资次数", RecordType.累加),		
	合成一瓶蓝酒(239, "合成一瓶蓝酒", RecordType.只记录第一次),								
	家族喝酒次数(240, "家族喝酒次数", RecordType.只记录第一次),								
	内防心法重数(241, "内防心法重数", RecordType.记录最大值),		
	外防心法重数(242, "外防心法重数", RecordType.记录最大值),		
	内防和外防心法都达到50重(243, "内防和外防心法都达到50重", RecordType.只记录第一次),		
	镶嵌一颗3级宝石(244, "镶嵌一颗3级宝石", RecordType.只记录第一次),							
	完成斩妖除魔次数(245, "完成斩妖除魔次数", RecordType.累加),								
	完成采花大盗次数(246, "完成采花大盗次数", RecordType.累加),								
	宠物携带等级升至135级次数(247, "宠物携带等级升至135级次数", RecordType.累加),		
	使用橙色封魔录次数(248, "使用橙色封魔录次数", RecordType.累加),							
	宠物携带等级升至180级次数(249, "宠物携带等级升至180级次数", RecordType.累加),
	宠物携带等级升至220级次数(250, "宠物携带等级升至220级次数", RecordType.累加),
	击杀boss增长天(251, "击杀boss增长天", RecordType.只记录第一次),
	击杀boss广目天(252, "击杀boss广目天", RecordType.只记录第一次),
	击杀boss多闻天(253, "击杀boss多闻天", RecordType.只记录第一次),
	击杀boss持国天(254, "击杀boss持国天", RecordType.只记录第一次),
	击杀boss月宫天(255, "击杀boss月宫天", RecordType.只记录第一次),
	击杀boss金刚密迹(256, "击杀boss金刚密迹", RecordType.只记录第一次),
	击杀boss大自在天(257, "击杀boss大自在天", RecordType.只记录第一次),
	击杀boss摩利支天(258, "击杀boss摩利支天", RecordType.只记录第一次),
	击杀boss功德天(259, "击杀boss功德天", RecordType.只记录第一次),
	击杀boss功驮天(260, "击杀boss功驮天", RecordType.只记录第一次),
	击杀boss日宫天(261, "击杀boss日宫天", RecordType.只记录第一次),
	击杀boss大梵天(262, "击杀boss大梵天", RecordType.只记录第一次),
	击杀所有修仙界世界boss(263, "击杀所有修仙界世界boss", RecordType.只记录第一次),
	参加一次世界boss活动(264, "参加一次世界boss活动", RecordType.只记录第一次),				
	元神穿戴一套61级以上紫装(265, "元神穿戴一套61级以上紫装", RecordType.只记录第一次),			
	穿戴10件铭刻装备(266, "穿戴10件铭刻装备", RecordType.记录最大值),						
	本尊穿戴一套141级以上的完美橙装备(269, "本尊穿戴一套141级以上的完美橙装备", RecordType.只记录第一次),
	穿戴1套装备到6星以上(270, "穿戴1套装备到6星以上", RecordType.只记录第一次),					
	元神穿戴一套121级橙色以上装备(275, "元神穿戴一套121级橙色以上装备", RecordType.只记录第一次),		
	本尊穿戴一套181级以上完美橙装备(279, "本尊穿戴一套181级以上完美橙装备", RecordType.只记录第一次),	
	成功穿戴1套装备到7星以上(280, "成功穿戴1套装备到7星以上", RecordType.只记录第一次),			
	通关幽冥幻域阳清普通模式(281, "通关幽冥幻域阳清（普通模式）", RecordType.累加),					
	通关幽冥幻域阳清困难模式(282, "通关幽冥幻域阳清（困难模式）", RecordType.累加),					
	通关幽冥幻域洪荒普通模式(283, "通关幽冥幻域阳清（普通模式）", RecordType.累加),					
	通关幽冥幻域洪荒困难模式(284, "通关幽冥幻域阳清（困难模式）", RecordType.累加),					
	通关幽冥幻域混沌普通模式(285, "通关幽冥幻域阳清（普通模式）", RecordType.累加),					 
	
	通关幽冥幻域混沌困难模式(286, "通关幽冥幻域阳清（困难模式）", RecordType.累加),					
	通关幽冥幻域玄天普通(273, "通关幽冥幻域玄天普通", RecordType.累加),							
	通关幽冥幻域玄天困难(274, "通关幽冥幻域玄天困难", RecordType.累加),							
	通关幽冥幻域阴浊普通模式(277, "通关幽冥幻域阴浊（普通模式）", RecordType.累加),					
	通关幽冥幻域阴浊困难模式(278, "通关幽冥幻域阴浊（困难模式）", RecordType.累加),					
	通天黄泉普通(292, "通天黄泉普通", RecordType.累加),								
	通天黄泉困难(293, "通天黄泉困难", RecordType.累加),								
	通关玉虚迷境本尊(271, "通关玉虚迷境(本尊)", RecordType.累加),							
	通关玉虚迷境元神(272, "通关玉虚迷境(元神)", RecordType.累加),							
	通关太虚幻境本尊(267, "通关太虚幻境(本尊)", RecordType.累加),							
	通关太虚幻境元神(268, "通关太虚幻境(元神)", RecordType.累加),							
	本尊穿戴一套211级以上完美橙装备(287, "本尊穿戴一套211级以上完美橙装备", RecordType.只记录第一次),	
	元神穿戴一套211级以上完美橙装备(288, "元神穿戴一套211级以上完美橙装备", RecordType.只记录第一次),	
	成功穿戴1套装备到17星以上(289, "成功穿戴1套装备到17星以上", RecordType.只记录第一次),			
	成功穿戴随意4件装备到4星(290, "成功穿戴随意4件装备到4星", RecordType.只记录第一次),			
	成功穿戴1套装备到1星以上(291, "成功穿戴1套装备到1星以上", RecordType.只记录第一次),			
	注魂次数(295, "注魂次数", RecordType.累加),									
	任意三个注魂等级(296, "任意三个注魂等级", RecordType.记录最大值),							
	装备一个蓝色器灵(297, "装备一个蓝色器灵", RecordType.只记录第一次),
	装备一个紫色器灵(298, "装备一个紫色器灵", RecordType.只记录第一次),
	装备一个橙色器灵(299, "装备一个橙色器灵", RecordType.只记录第一次),
	成功穿戴随意4件装备到5星(301, "成功穿戴随意4件装备到5星", RecordType.只记录第一次),			
	心法最大重数(302, "心法最大重数", RecordType.记录最大值),
	创建战队次数(303, "创建战队次数", RecordType.累加),									//	
	练满一个蓝色器灵(304, "练满一个蓝色器灵", RecordType.只记录第一次),
	练满一个紫色器灵(305, "练满一个紫色器灵", RecordType.只记录第一次),
	练满一个橙色器灵(306, "练满一个橙色器灵", RecordType.只记录第一次),
	成功穿戴随意5件装备到12星(307, "成功穿戴随意5件装备到12星", RecordType.只记录第一次),			
	镶嵌器灵4个的装备个数(308, "镶嵌器灵4个的装备个数", RecordType.记录最大值),					
	心法随意2个到达的最大重数(309, "心法随意2个到达的最大重数", RecordType.记录最大值),				
	心法随意4个到达的最大重数(310, "心法随意4个到达的最大重数", RecordType.记录最大值),				
	镶嵌满一身宝石等级(311, "镶嵌满一身宝石等级", RecordType.记录最大值),
	进入玲珑塔(312, "进入玲珑塔", RecordType.只记录第一次),
	身上装备宝石数量(313, "身上装备宝石数量", RecordType.记录最大值),
	出战宠物最大携带等级(314, "出战宠物最大携带等级", RecordType.记录最大值),						
	坐骑升级最大阶数(315, "坐骑升级最大阶数", RecordType.记录最大值),					
	坐骑升级最大血脉阶(316, "坐骑升级最大血脉阶", RecordType.记录最大值),						
	击杀不低于20级的敌国玩家(317, "击杀不低于20级的敌国玩家", RecordType.累加),						
	连续获得武圣争夺战第一名次数(318, "连续获得武圣争夺战第一名次数", RecordType.设置),							
	杀死不低于自身5级怪物数量(319, "杀死不低于自身5级怪物数量", RecordType.累加),						
	使用仙装图纸升级仙装次数(320, "使用仙装图纸升级仙装次数", RecordType.累加),								
	合成套装属性装备次数(321, "合成套装属性装备次数", RecordType.累加),									
	获得金宝蛇皇(322, "获得金宝蛇皇", RecordType.只记录第一次),									
	获得黑雾(323, "获得黑雾", RecordType.记录最大值),							
	发送邮件次数(324, "发送邮件次数", RecordType.累加),									
	坐骑拥有2级技能个数(325, "坐骑拥有2级技能个数", RecordType.记录最大值),				
	坐骑拥有3级技能个数(326, "坐骑拥有3级技能个数", RecordType.记录最大值),				
	宠物拥有2级基础技能个数(327, "宠物拥有2级基础技能个数", RecordType.记录最大值),		
	宠物拥有3级基础技能个数(328, "宠物拥有3级基础技能个数", RecordType.记录最大值),		
	法宝星级(329, "法宝星级", RecordType.记录最大值),							
	法宝羽化成功次数(330, "法宝羽化成功次数", RecordType.累加),									
	装备紫色以上器灵件数(331, "装备紫色以上器灵件数", RecordType.记录最大值),			
	装备橙色以上器灵件数(332, "装备橙色以上器灵件数", RecordType.记录最大值),			
	接受并完成仙缘或论道任务次数(333, "接受并完成仙缘或论道任务次数", RecordType.累加),				
	本国地图杀死敌国玩家数(334, "本国地图杀死敌国玩家数", RecordType.累加),				
	其他国家地图杀死敌国玩家数(335, "其他国家地图杀死敌国玩家数", RecordType.累加),				
	杀死砸本国镖车人次数(336, "杀死砸本国镖车人次数", RecordType.累加),						
	完成家族任务次数(337, "完成家族任务次数", RecordType.累加),							
	监狱中消除红名时间(338, "监狱中消除红名时间", RecordType.累加),				//				单位记录为分钟数	
	集齐20个称号1(339, "集齐20个称号1", RecordType.只记录第一次),				
	集齐20个称号2(340, "集齐20个称号2", RecordType.只记录第一次),				
	集齐13个称号(341, "集齐13个称号", RecordType.只记录第一次),				
	通天黄泉深渊(342, "通天黄泉深渊", RecordType.只记录第一次),							
	通关幽冥幻域玄天深渊(343, "通关幽冥幻域玄天深渊", RecordType.只记录第一次),							
	通关幽冥幻域阴浊深渊模式(344, "通关幽冥幻域阴浊深渊模式", RecordType.只记录第一次),							
	通关幽冥幻域阳清深渊模式(345, "通关幽冥幻域阳清深渊模式", RecordType.只记录第一次),								
	通关幽冥幻域洪荒深渊模式(346, "通关幽冥幻域洪荒深渊模式", RecordType.只记录第一次),								
	通关幽冥幻域混沌深渊模式(347, "通关幽冥幻域混沌深渊模式", RecordType.只记录第一次),
	羽化装备成功次数(348, "羽化装备成功次数", RecordType.累加),							
	杀死精英怪(349, "杀死精英怪", RecordType.累加),							
	杀死境界怪物(350, "杀死境界怪物", RecordType.累加),							
	击杀漏网的世界BOSS(351, "击杀漏网的世界BOSS", RecordType.累加),
	深渊幽冥幻域(352,"通过所有深渊幽冥幻域",RecordType.只记录第一次),					
	使用图纸制造翅膀次数(353,"使用图纸制造翅膀次数",RecordType.累加),				// 未做
	集齐十个翅膀(354,"集齐十个翅膀",RecordType.只记录第一次),					// 未做
	随意10个家族修炼最高等级(355,"随意10个家族修炼最高等级",RecordType.记录最大值),					
	杀死固定20只怪(356,"杀死固定20只怪",RecordType.累加),						
	累计连斩次数(357,"累计连斩次数",RecordType.累加),							
	极限任务组1(358,"极限任务组1",RecordType.累加),								
	极限任务组2(359,"极限任务组2",RecordType.累加),								
	极限任务组3(360,"极限任务组3",RecordType.累加),								
	极限任务组4(361,"极限任务组4",RecordType.累加),								
	极限任务组5(362,"极限任务组5",RecordType.累加),							
	极限任务组6(363,"极限任务组6",RecordType.累加),							
	宠物拥有2级基础技能数2(364, "宠物拥有2级基础技能数2", RecordType.记录最大值),		
	宠物拥有3级基础技能数2(365, "宠物拥有3级基础技能数2", RecordType.记录最大值),
	武器附魔次数(366, "武器附魔次数", RecordType.累加),
	戒指附魔次数(367, "戒指附魔次数", RecordType.累加),
	头盔附魔次数(368, "头盔附魔次数", RecordType.累加),
	胸甲附魔次数(369, "胸甲附魔次数", RecordType.累加),
	武器附魔到max(370, "武器附魔到max", RecordType.只记录第一次),
	戒指附魔到max(371, "戒指附魔到max", RecordType.只记录第一次),
	头盔附魔到max(372, "头盔附魔到max", RecordType.只记录第一次),
	胸甲附魔到max(373, "胸甲附魔到max", RecordType.只记录第一次),
	三界最快兽魁(374, "三界最快兽魁", RecordType.只记录第一次),
	仙丹修炼次数(375, "仙丹修炼次数", RecordType.累加),										
	参加宝箱大乱斗次数(376, "参加宝箱大乱斗次数", RecordType.累加),
	使用易筋丹使宠物灵性达到50(377, "使用易筋丹使宠物灵性达到50", RecordType.只记录第一次),
	使用易筋丹使宠物灵性达到85(378, "使用易筋丹使宠物灵性达到85", RecordType.只记录第一次),
	使用易筋丹使宠物灵性达到100(379, "使用易筋丹使宠物灵性达到100", RecordType.只记录第一次),
	宠物学习一个飞升技能(380, "宠物学习一个飞升技能", RecordType.只记录第一次),
	使用易筋丹使宠物灵性达到110(381, "使用易筋丹使宠物灵性达到110", RecordType.只记录第一次),
	使用返生丹次数(382, "使用返生丹次数", RecordType.累加),		
	完成仙界渡劫层数(383, "完成仙界渡劫层数", RecordType.记录最大值),		
	任务拜访玄武祖师(384, "任务拜访玄武祖师", RecordType.只记录第一次),		
	任务拜访无上玉皇(385, "任务拜访无上玉皇", RecordType.只记录第一次),		
	任务拜访北斗星君(386, "任务拜访北斗星君", RecordType.只记录第一次),		
	任务拜访林大华(387, "任务拜访林大华", RecordType.只记录第一次),			 
	任务暗杀赵公明(388, "任务暗杀赵公明", RecordType.只记录第一次),			
	元婴修炼等级(389, "元婴修炼等级", RecordType.记录最大值),				
	元婴天赋总数(390, "元婴天赋总数", RecordType.记录最大值),				
	昆仑第一个到达仙40的玩家(391, "昆仑第一个到达仙40的玩家", RecordType.只记录第一次),
	万法第一个到达仙40的玩家(392, "万法第一个到达仙40的玩家", RecordType.只记录第一次),
	九州第一个到达仙40的玩家(393, "九州第一个到达仙40的玩家", RecordType.只记录第一次),
	仙界第一修罗(394, "仙界第一修罗", RecordType.只记录第一次),
	仙界第一影魅(395, "仙界第一影魅", RecordType.只记录第一次),
	仙界第一仙心(396, "仙界第一仙心", RecordType.只记录第一次),
	仙界第一九黎(397, "仙界第一九黎", RecordType.只记录第一次),
	仙界第一兽魁(398, "仙界第一兽魁", RecordType.只记录第一次),
	击杀仙界俩boss(399, "击杀仙界俩boss", RecordType.累加);		
	
	

	private int type;
	private String name;
	private RecordType recordType;

	private RecordAction(int type, String name, RecordType recordType) {
		this.type = type;
		this.name = name;
		this.recordType = recordType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public RecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}

}
