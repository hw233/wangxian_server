<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page
	language="java"
	contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta
	http-equiv="Content-Type"
	content="text/html; charset=utf-8"
>
<title>删除玩家令旗</title>
<%
	String playerNames[] = {"EVO安少","魔比","萬中之明1","銀子","戰神龍之王","快樂吊卡手","情洛莎","伊伊","夜夜萌","陌小猪","秀智","光龍","一夫當關","詩情","幻星龍","夜夜愛","真龍-光龍","基爺","★靖少★","暮成雪","黃阿威","沙紅燕","淨琉璃","上等茶","張天師","黑心鬼","我就是我","aszxy","wei0928","凌小晞、","請勿挑戰神","阿拉伯阿拉","武君羅喉","機車大哥","荊靜薇","足奇麥","闇月一殺戮","甜蜜蜜","太極","機巴忠","可汗","煞星狼","山東","鴉魔狩","紫@月","阿露","千錘百鍊","舞旋子","日曜","紫玉娥","讓ㄧ下","*淨琉璃*","30678","豬鼻雞=_=","保","不要一直看","天下之魂","赢仙仙","～神＊武～","ady88","傅說木偶","三隻小熊","夜夜呱","crazy幻影","GG88","Mr.zeng","sss","Tinalin","噯戀you","闇焱","寒冰°~","林爺","美魔女王","默默不得語","秋哥","軟Q","殺破狼","小風信子","雪兒","眼鏡兄","*~舖起卡~*","9重天","hm136","ottotsai","蒼紫楓","魑珸鬱","熾魔燄火","大丹","梵帝","戤","古惑女","鬼妹","海觴君","虹儀","咖痲乙","怒刀騰","品","太陽神","兔","勿忘我","逍遙法外","小文","欣妤","妖艷女王","足享"};
	int removeNums[] = {341,224,195,172,153,112,76,74,61,56,50,49,43,42,40,40,39,34,33,27,24,22,21,21,21,20,20,19,18,18,18,16,16,14,14,13,12,12,11,10,10,10,10,10,10,9,9,9,8,8,6,5,5,5,4,4,4,4,3,3,3,3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	if (playerNames.length != removeNums.length) {
		out.print("玩家数量和要删除的数量不匹配！");
		return;
	}
	String articleName = "連登令旗";
	Player p = null;
	PlayerManager pm = PlayerManager.getInstance();
	for (int i = 0; i < playerNames.length; i++) {
		try {
			p = pm.getPlayer(playerNames[i]);
			int leftRemoveNum = removeNums[i];
			if (p != null) {
				//删除背包
				Knapsack knapsack = p.getKnapsack_common();
				int 实际删除数_背包 = 0;
				int 实际删除数_防爆 = 0;
				int 实际删除数_仓库 = 0;
				for (Cell cell : knapsack.getCells()) {
					if (leftRemoveNum <= 0) {
						break;
					}
					long eId = cell.getEntityId();
					ArticleEntity ae = ArticleEntityManager
							.getInstance().getEntity(eId);
					if (ae != null) {
						if (ae.getArticleName().equals(articleName)) {
							int num = cell.getCount();
							int cellLeft = num;
							if (leftRemoveNum >= num) {
								cellLeft = 0;
								leftRemoveNum -= num;
								实际删除数_背包 += num;
							} else {
								cellLeft -= leftRemoveNum;
								leftRemoveNum = 0;
							}
							if (cellLeft == 0) {
								cell.setCount(0);
								cell.setEntityId(-1);
							} else {
								cell.setCount(cellLeft);
							}
							knapsack.setModified(true);
						}
					}
				}
				out.print(playerNames[i]+"从【背包】删除"+articleName+",需要删除"+leftRemoveNum+"个，实际删除了："+实际删除数_背包+"<br>");
				//删除防爆背包
				knapsack = p.getKnapsack_fangbao();
				for (Cell cell : knapsack.getCells()) {
					if (leftRemoveNum <= 0) {
						break;
					}
					long eId = cell.getEntityId();
					ArticleEntity ae = ArticleEntityManager
							.getInstance().getEntity(eId);
					if (ae != null) {
						if (ae.getArticleName().equals(articleName)) {
							int num = cell.getCount();
							int cellLeft = num;
							if (leftRemoveNum >= num) {
								cellLeft = 0;
								leftRemoveNum -= num;
								实际删除数_防爆 += num;
							} else {
								cellLeft -= leftRemoveNum;
								leftRemoveNum = 0;
							}
							if (cellLeft == 0) {
								cell.setCount(0);
								cell.setEntityId(-1);
							} else {
								cell.setCount(cellLeft);
							}
							knapsack.setModified(true);
						}
					}
				}
				out.print(playerNames[i]+"从【防爆背包】删除"+articleName+",需要删除"+leftRemoveNum+"个，实际删除了："+实际删除数_防爆+"<br>");
				//删除仓库
				knapsack = p.getKnapsacks_cangku();
				for (Cell cell : knapsack.getCells()) {
					if (leftRemoveNum <= 0) {
						break;
					}
					long eId = cell.getEntityId();
					ArticleEntity ae = ArticleEntityManager
							.getInstance().getEntity(eId);
					if (ae != null) {
						if (ae.getArticleName().equals(articleName)) {
							int num = cell.getCount();
							int cellLeft = num;
							if (leftRemoveNum >= num) {
								cellLeft = 0;
								leftRemoveNum -= num;
								实际删除数_仓库 += num;
							} else {
								cellLeft -= leftRemoveNum;
								leftRemoveNum = 0;
							}
							if (cellLeft == 0) {
								cell.setCount(0);
								cell.setEntityId(-1);
							} else {
								cell.setCount(cellLeft);
							}
							knapsack.setModified(true);
						}
					}
				}
				out.print(playerNames[i]+"从【仓库】删除"+articleName+",需要删除"+leftRemoveNum+"个，实际删除了："+实际删除数_仓库+"<br>");
				out.print("============"+playerNames[i]+"删除"+articleName+"完毕,需要删除"+leftRemoveNum+"个，一共删除了："+(实际删除数_仓库+实际删除数_背包+实际删除数_防爆)+"==============<br>");
				ActivitySubSystem.logger.warn("[台湾删除] ["+articleName+"] [成功] [角色名："
						+ playerNames[i] + "] [需要删除数："+leftRemoveNum+"] [实际删除数:"+(实际删除数_仓库+实际删除数_背包+实际删除数_防爆)+"]");
			}
		} catch (Exception e) {
			out.print("获得玩家异常：" + playerNames[i] + "<br>");
			ActivitySubSystem.logger.warn("[台湾删除令旗] [获得玩家异常] [角色名："
					+ playerNames[i] + "] [异常:" + e + "]");
			continue;
		}
	}
%>

</body>
</html>
