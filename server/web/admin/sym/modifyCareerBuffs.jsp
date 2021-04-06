<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.jiazu2.manager.JiazuManager2"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
<title>test</title>
</head>
<body>

<%
if (JiazuManager2.instance.translate.get(3336) != null) {
	out.println("已经刷过此页面！！！！！");
	return ;
}
JiazuManager2.instance.translate.put(3336, "buffYemian");
Runnable r = new Runnable(){
	public void run(){
		Game.logger.warn("*********************************************************");
		Game.logger.warn("************************[检测玩家buff线程] [开始]*****************");
		Game.logger.warn("*********************************************************");
		Map<Byte, Integer[]> careerBuffs = new HashMap<Byte, Integer[]>();
		careerBuffs.put((byte)1, new Integer[]{1380,1492,1473});			//斗罗，  狂印  血印
		careerBuffs.put((byte)2, new Integer[]{1382,1556});			//鬼煞    熊印  狼印
		careerBuffs.put((byte)3, new Integer[]{1381,1361});			//灵尊    灵印  炎印
		careerBuffs.put((byte)4, new Integer[]{1384,1555});			//巫皇   骨印  妖印
		while (true) {
			try {
				if (JiazuManager2.instance.translate.get(35558) != null) {
					break;
				}
				Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
				for (Player p : players) {
					if (p.getLevel() >= 60) {
						Soul base = p.getSoul(Soul.SOUL_TYPE_BASE);
						Soul soul = p.getSoul(Soul.SOUL_TYPE_SOUL);
						if (base != null && soul != null && base.getCareer() != soul.getCareer() && p.getBuffs().size() > 0) {//只检查本尊和元神不同职业的
							try {
								for (Buff b : p.getBuffs()) {
									if (!canAddBuff(p.getCareer(), b.getTemplateId(), careerBuffs)) {
										b.setInvalidTime(0);			//删除不应该有的buff
									}
								}
							} catch (Exception e2) {
								
							}
						}
					}
				}
			} catch (Exception e) {
				
			}
			try {
				Thread.sleep(60000);
			} catch (Exception e) {
				
			}
		}
		Game.logger.warn("*********************************************************");
		Game.logger.warn("************************[检测玩家buff线程] [停止]*****************");
		Game.logger.warn("*********************************************************");
	}
};
Thread t = new Thread(r,"检查玩家buff线程");
t.start();
%>
<%!
	public boolean canAddBuff(byte career, int buffId, Map<Byte, Integer[]> careerBuffs) {
		Iterator<Byte> ite = careerBuffs.keySet().iterator();
		while(ite.hasNext()) {
			byte key = ite.next();
			if (key == career) {
				continue;
			}
			Integer[] buffs = careerBuffs.get(key);
			for (Integer buff : buffs) {
				if (buff == buffId) {
					return false;
				}
			}
		}
		return true;
	}

%>
 