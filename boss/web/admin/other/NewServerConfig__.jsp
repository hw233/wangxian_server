
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <script language="javascript"> 
    	function selectall(selectids){ 
    		var a = document.getElementsByTagName("input"); 
   			for (var i=0; i<a.length; i++) {
   				if (a[i].type == "checkbox") {
   					if(a[i].checked==true){ 
//    	   					alert("a[i].type:"+a[i].type);
   	   					if(a[i].id == selectids.value){
   	   						a[i].checked = false;
   	   					}
   	   				}else if(a[i].checked==false){ 
   	  					if(a[i].id == selectids.value){
   	  						a[i].checked = true;
   	  					}
   	   	    		} 
   				}
   			}
    	} 
    </script>
    <table>
	<form>
<% 
		out.print("<tr>");
		out.print("<th>91、UC不可见</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='91uc'></td>");
		out.print("<td><div id='http://116.213.192.232:8001/gm' style='display:none;'>雄霸天下</div><input type='checkbox' id='91uc' name='server' value='http://116.213.192.232:8001/gm'/>雄霸天下</td>");
		out.print("<td><div id='http://116.213.204.140:8001/gm' style='display:none;'>月满乾坤</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.140:8001/gm'/>月满乾坤</td>");
		out.print("<td><div id='http://116.213.204.132:8001/gm' style='display:none;'>勇者无敌</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.132:8001/gm'/>勇者无敌</td>");
		out.print("<td><div id='http://116.213.204.112:8002/gm' style='display:none;'>傲视群雄</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.112:8002/gm'/>傲视群雄</td>");
		out.print("<td ><div id='http://116.213.204.114:8001/gm' style='display:none;'>傲视遮天</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.114:8001/gm'/>傲视遮天</td>");
		out.print("<td ><div id='http://117.121.9.205:8002/gm' style='display:none;'>虎啸山河</div><input type='checkbox' id='91uc' name='server' value='http://117.121.9.205:8002/gm'/>虎啸山河</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>腾讯</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='tx'></td>");
		out.print("<td ><div id='http://117.135.149.27:20026/gm' style='display:none;'>太虚幻境</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20026/gm'/>太虚幻境</td>");
		out.print("<td ><div id='http://117.135.149.27:20014/gm' style='display:none;'>幽冥山谷</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20014/gm'/>幽冥山谷</td>");
		out.print("<td ><div id='http://117.135.149.27:20020/gm' style='display:none;'>昆仑圣殿</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20020/gm'/>昆仑圣殿</td>");
		out.print("<td ><div id='http://117.135.149.27:20184/gm' style='display:none;'>霸气乾坤</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20184/gm'/>霸气乾坤</td>");
		out.print("<td ><div id='http://117.135.130.45:8802/gm' style='display:none;'>烟雨青山</div><input type='checkbox' id='tx' name='server' value='http://117.135.130.45:8802/gm'/>烟雨青山</td>");
		out.print("<td ><div id='http://117.135.130.45:8801/gm' style='display:none;'>仙山琼阁</div><input type='checkbox' id='tx' name='server' value='http://117.135.130.45:8801/gm'/>仙山琼阁</td>");
		out.print("<td ><div id='http://117.135.130.25:8801/gm' style='display:none;'>霸气无双</div><input type='checkbox' id='tx' name='server' value='http://117.135.130.25:8801/gm'/>霸气无双</td>");
		out.print("<td ><div id='http://117.135.149.97:8801/gm' style='display:none;'>华山之巅</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.97:8801/gm'/>华山之巅</td>");
		out.print("<td ><div id='http://182.254.148.114:8002/gm' style='display:none;'>神龙摆尾</div><input type='checkbox' id='tx' name='server' value='http://182.254.148.114:8002/gm'/>神龙摆尾</td>");
		out.print("<td ><div id='http://117.135.130.112:8801/gm' style='display:none;'>柳暗花明</div><input type='checkbox' id='tx' name='server' value='http://117.135.130.112:8801/gm'/>柳暗花明</td>");
		out.print("<td ><div id='http://117.135.149.108:8801/gm' style='display:none;'>众仙归来</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.108:8801/gm'/>众仙归来</td>");
		out.print("<td ><div id='http://117.135.149.108:8802/gm' style='display:none;'>三界奇缘</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.108:8802/gm'/>三界奇缘</td>");
		out.print("<td ><div id='http://117.135.130.25:8801/gm' style='display:none;'>战无不胜</div><input type='checkbox' id='tx' name='server' value='http://117.135.130.25:8801/gm'/>战无不胜</td>");
		out.print("<td ><div id='http://117.135.149.27:20014/gm' style='display:none;'>河东狮吼</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20014/gm'/>河东狮吼</td>");
		out.print("<td ><div id='http://182.254.133.91:8001/gm' style='display:none;'>步罡踏斗</div><input type='checkbox' id='tx' name='server' value='http://182.254.133.91:8001/gm'/>步罡踏斗</td>");
		out.print("<td ><div id='http://182.254.148.114:8001/gm' style='display:none;'>万象更新</div><input type='checkbox' id='tx' name='server' value='http://182.254.148.114:8001/gm'/>万象更新</td>");
		out.print("</tr>");
		out.print("<th>91专服</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='91'></td>");
		out.print("<td ><div id='http://116.213.204.163:8002/gm' style='display:none;'>瑞草溪亭</div><input type='checkbox' id='91' name='server' value='http://116.213.204.163:8002/gm'/>瑞草溪亭</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>UC专服</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='uc'></td>");
		out.print("<td ><div id='http://117.121.22.6:8001/gm' style='display:none;'>瑶林琼树</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.6:8001/gm'/>瑶林琼树</td>");
		out.print("<td ><div id='http://116.213.204.111:8003/gm' style='display:none;'>混元塑骨</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.111:8003/gm'/>混元塑骨</td>");
		out.print("<td ><div id='http://116.213.204.113:8002/gm' style='display:none;'>雪飘无垠</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.113:8002/gm'/>雪飘无垠</td>");
		out.print("<td ><div id='http://116.213.204.111:8001/gm' style='display:none;'>流虹千转</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.111:8001/gm'/>流虹千转</td>");
		out.print("<td ><div id='http://117.121.22.16:8001/gm' style='display:none;'>风和日丽</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.16:8001/gm'/>风和日丽</td>");
		out.print("<td ><div id='http://117.121.22.7:8002/gm' style='display:none;'>勇夺桂冠</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.7:8002/gm'/>勇夺桂冠</td>");
		out.print("<td ><div id='http://116.213.204.140:8001/gm' style='display:none;'>佳人如梦</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.140:8001/gm'/>佳人如梦</td>");
		out.print("<td ><div id='http://117.121.22.6:8003/gm' style='display:none;'>流星追月</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.6:8003/gm'/>流星追月</td>");
		out.print("<td ><div id='http://116.213.204.116:8001/gm' style='display:none;'>凤骨龙姿</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.116:8001/gm'/>凤骨龙姿</td>");
		out.print("<td ><div id='http://116.213.192.226:8001/gm' style='display:none;'>灵宝异彩</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.18:8003/gm'/>灵宝异彩</td>");
		out.print("<td ><div id='http://116.213.204.115:8002/gm' style='display:none;'>千年凤鸣</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.115:8002/gm'/>千年凤鸣</td>");
		out.print("<td ><div id='http://116.213.204.100:8002/gm' style='display:none;'>白水鉴心</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.100:8002/gm'/>白水鉴心</td>");
		out.print("<td ><div id='http://116.213.204.164:8001/gm' style='display:none;'>玉柱擎天</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.164:8001/gm'/>玉柱擎天</td>");
		out.print("<td ><div id='http://116.213.204.116:8002/gm' style='display:none;'>雾暗云深</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.116:8002/gm'/>雾暗云深</td>");
		out.print("<td ><div id='http://117.121.22.3:8002/gm' style='display:none;'>仙露明珠</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.3:8002/gm'/>仙露明珠</td>");
		out.print("<td ><div id='http://117.121.22.17:8002/gm' style='display:none;'>一掷乾坤</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.17:8002/gm'/>一掷乾坤</td>");
		out.print("<td ><div id='http://116.213.204.142:8002/gm' style='display:none;'>步月登云</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.142:8002/gm'/>步月登云</td>");
		out.print("<td ><div id='http://116.213.204.142:8002/gm' style='display:none;'>文武之道</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.142:8002/gm'/>文武之道</td>");
		out.print("<td ><div id='http://116.213.204.144:8001/gm' style='display:none;'>天下风云</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.144:8001/gm'/>天下风云</td>");
		out.print("<td ><div id='http://117.121.22.3:8003/gm' style='display:none;'>杏花微雨</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.3:8003/gm'/>杏花微雨</td>");
		out.print("<td ><div id='http://116.213.204.112:8002/gm' style='display:none;'>杏雨梨云</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.112:8002/gm'/>杏雨梨云</td>");
		out.print("<td ><div id='http://117.121.22.15:8002/gm' style='display:none;'>逢山开道</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.15:8002/gm'/>逢山开道</td>");
		out.print("<td ><div id='http://117.121.22.13:8002/gm' style='display:none;'>百战乾坤</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.13:8002/gm'/>百战乾坤</td>");
		out.print("<td ><div id='http://116.213.204.111:8002/gm' style='display:none;'>星桥火树</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.111:8002/gm'/>星桥火树</td>");
		out.print("<td ><div id='http://117.121.22.18:8003/gm' style='display:none;'>云屯星聚</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.18:8003/gm'/>云屯星聚</td>");
		out.print("<td ><div id='http://116.213.204.140:8003/gm' style='display:none;'>富于春秋</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.140:8003/gm'/>富于春秋</td>");
		out.print("<td ><div id='http://117.121.22.2:8003/gm' style='display:none;'>风行电掣</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.2:8003/gm'/>风行电掣</td>");
		out.print("<td ><div id='http://116.213.204.141:8002/gm' style='display:none;'>桃花依旧</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.141:8002/gm'/>桃花依旧</td>");
		out.print("<td ><div id='http://117.121.22.4:8003/gm' style='display:none;'>天下归心</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.4:8003/gm'/>天下归心</td>");
		out.print("<td ><div id='http://117.121.22.5:8001/gm' style='display:none;'>品竹调丝</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.5:8001/gm'/>品竹调丝</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>苹果专服<font color='app'></th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='app'></td>");
		out.print("<td ><div id='http://117.121.22.3:8001/gm' style='display:none;'>春风得意</div><input type='checkbox' id='app' name='server' value='http://117.121.22.3:8001/gm'/>春风得意</td>");
		out.print("<td ><div id='http://116.213.192.230:8002/gm' style='display:none;'>天下无双</div><input type='checkbox' id='app' name='server' value='http://116.213.192.230:8002/gm'/>天下无双</td>");
		out.print("<td ><div id='http://116.213.204.116:8002/gm' style='display:none;'>仙子乱尘</div><input type='checkbox' id='app' name='server' value='http://116.213.204.116:8002/gm'/>仙子乱尘</td>");
		out.print("<td ><div id='http://116.213.204.136:8001/gm' style='display:none;'>幻灵仙境</div><input type='checkbox' id='app' name='server' value='http://116.213.204.136:8001/gm'/>幻灵仙境</td>");
		out.print("<td ><div id='http://116.213.204.167:8001/gm' style='display:none;'>星陨神阁</div><input type='checkbox' id='app' name='server' value='http://116.213.204.167:8001/gm'/>星陨神阁</td>");
		out.print("<td ><div id='http://116.213.204.100:8002/gm' style='display:none;'>秋兰飘香</div><input type='checkbox' id='app' name='server' value='http://116.213.204.100:8002/gm'/>秋兰飘香</td>");
		out.print("<td ><div id='http://116.213.204.144:8002/gm' style='display:none;'>断魂憶梦</div><input type='checkbox' id='app' name='server' value='http://116.213.204.144:8002/gm'/>断魂憶梦</td>");
		out.print("<td ><div id='http://116.213.204.132:8002/gm' style='display:none;'>天悬星河</div><input type='checkbox' id='app' name='server' value='http://116.213.204.132:8002/gm'/>天悬星河</td>");
		out.print("<td ><div id='http://116.213.205.21:8003/gm' style='display:none;'>万兽神鼎</div><input type='checkbox' id='app' name='server' value='http://116.213.205.21:8003/gm'/>万兽神鼎</td>");
		out.print("<td ><div id='http://116.213.204.144:8001/gm' style='display:none;'>六指圣龙</div><input type='checkbox' id='app' name='server' value='http://116.213.204.144:8001/gm'/>六指圣龙</td>");
		out.print("<td ><div id='http://116.213.204.102:8003/gm' style='display:none;'>行兵布阵</div><input type='checkbox' id='app' name='server' value='http://116.213.204.102:8003/gm'/>行兵布阵</td>");
		out.print("<td ><div id='http://117.121.22.18:8001/gm' style='display:none;'>携云握雨</div><input type='checkbox' id='app' name='server' value='http://117.121.22.18:8001/gm'/>携云握雨</td>");
		out.print("<td ><div id='http://116.213.204.143:8001/gm' style='display:none;'>万兽灵火</div><input type='checkbox' id='app' name='server' value='http://116.213.204.143:8001/gm'/>万兽灵火</td>");
		out.print("<td ><div id='http://116.213.204.115:8003/gm' style='display:none;'>凤凰涅槃</div><input type='checkbox' id='app' name='server' value='http://116.213.204.115:8003/gm'/>凤凰涅槃</td>");
		out.print("<td ><div id='http://117.121.22.8:8001/gm' style='display:none;'>神功圣化</div><input type='checkbox' id='app' name='server' value='http://117.121.22.8:8001/gm'/>神功圣化</td>");
		out.print("<td ><div id='http://117.121.22.18:8002/gm' style='display:none;'>视险如夷</div><input type='checkbox' id='app' name='server' value='http://117.121.22.18:8002/gm'/>视险如夷</td>");
		out.print("<td ><div id='http://117.121.22.5:8001/gm' style='display:none;'>翩然归来</div><input type='checkbox' id='app' name='server' value='http://117.121.22.5:8001/gm'/>翩然归来</td>");
		out.print("<td ><div id='http://116.213.204.113:8001/gm' style='display:none;'>碎琼乱玉</div><input type='checkbox' id='app' name='server' value='http://116.213.204.113:8001/gm'/>碎琼乱玉</td>");
		out.print("<td ><div id='http://117.121.22.4:8003/gm' style='display:none;'>追波逐浪</div><input type='checkbox' id='app' name='server' value='http://117.121.22.4:8003/gm'/>追波逐浪</td>");
		out.print("<td ><div id='http://117.121.22.7:8001/gm' style='display:none;'>皓月千里</div><input type='checkbox' id='app' name='server' value='http://117.121.22.7:8001/gm'/>皓月千里</td>");
		out.print("<td ><div id='http://116.213.204.116:8001/gm' style='display:none;'>气贯长虹</div><input type='checkbox' id='app' name='server' value='http://116.213.204.116:8001/gm'/>气贯长虹</td>");
		out.print("<td ><div id='http://116.213.205.21:8001/gm' style='display:none;'>大梵天宫</div><input type='checkbox' id='app' name='server' value='http://116.213.205.21:8001/gm'/>大梵天宫</td>");
		out.print("<td ><div id='http://117.121.22.6:8003/gm' style='display:none;'>龙舞皇图</div><input type='checkbox' id='app' name='server' value='http://117.121.22.6:8003/gm'/>龙舞皇图</td>");
		out.print("<td ><div id='http://116.213.204.100:8003/gm' style='display:none;'>众虎同心</div><input type='checkbox' id='app' name='server' value='http://116.213.204.100:8003/gm'/>众虎同心</td>");
		out.print("<td ><div id='http://117.121.22.2:8002/gm' style='display:none;'>元丹境界</div><input type='checkbox' id='app' name='server' value='http://117.121.22.2:8002/gm'/>元丹境界</td>");
		out.print("<td ><div id='http://116.213.204.167:8002/gm' style='display:none;'>月宫桂香</div><input type='checkbox' id='app' name='server' value='http://116.213.204.167:8002/gm'/>月宫桂香</td>");
		out.print("<td ><div id='http://116.213.204.144:8002/gm' style='display:none;'>继古开今</div><input type='checkbox' id='app' name='server' value='http://116.213.204.144:8002/gm'/>继古开今</td>");
		out.print("<td ><div id='http://116.213.204.142:8001/gm' style='display:none;'>势不可挡</div><input type='checkbox' id='app' name='server' value='http://116.213.204.142:8001/gm'/>势不可挡</td>");
		out.print("<td ><div id='http://116.213.204.116:8003/gm' style='display:none;'>重温旧梦</div><input type='checkbox' id='app' name='server' value='http://116.213.204.116:8003/gm'/>重温旧梦</td>");
		out.print("<td ><div id='http://116.213.204.144:8003/gm' style='display:none;'>无上宝玉</div><input type='checkbox' id='app' name='server' value='http://116.213.204.144:8003/gm'/>无上宝玉</td>");
		out.print("<td ><div id='http://117.121.22.4:8002/gm' style='display:none;'>天降鸿福</div><input type='checkbox' id='app' name='server' value='http://117.121.22.4:8002/gm'/>天降鸿福</td>");
		out.print("<td ><div id='http://117.121.22.16:8002/gm' style='display:none;'>逆鳞之舞</div><input type='checkbox' id='app' name='server' value='http://117.121.22.16:8002/gm'/>逆鳞之舞</td>");
		out.print("<td ><div id='http://116.213.204.99:8001/gm' style='display:none;'>洪荒妖瞳</div><input type='checkbox' id='app' name='server' value='http://116.213.204.99:8001/gm'/>洪荒妖瞳</td>");
		out.print("<td ><div id='http://117.121.22.14:8001/gm' style='display:none;'>春山如笑</div><input type='checkbox' id='app' name='server' value='http://117.121.22.14:8001/gm'/>春山如笑</td>");
		out.print("<td ><div id='http://117.121.22.6:8002/gm' style='display:none;'>云梦闲情</div><input type='checkbox' id='app' name='server' value='http://117.121.22.6:8002/gm'/>云梦闲情</td>");
		out.print("<td ><div id='http://116.213.204.142:8003/gm' style='display:none;'>柔情侠骨</div><input type='checkbox' id='app' name='server' value='http://116.213.204.142:8003/gm'/>柔情侠骨</td>");
		out.print("<td ><div id='http://116.213.204.164:8002/gm' style='display:none;'>风刀霜剑</div><input type='checkbox' id='app' name='server' value='http://116.213.204.164:8002/gm'/>风刀霜剑</td>");
		out.print("<td ><div id='http://117.121.22.13:8003/gm' style='display:none;'>夏炉冬扇</div><input type='checkbox' id='app' name='server' value='http://117.121.22.13:8003/gm'/>夏炉冬扇</td>");
		out.print("<td ><div id='http://116.213.204.141:8003/gm' style='display:none;'>雪满长空</div><input type='checkbox' id='app' name='server' value='http://116.213.204.141:8003/gm'/>雪满长空</td>");
		out.print("<td ><div id='http://117.121.22.6:8003/gm' style='display:none;'>收聚神光</div><input type='checkbox' id='app' name='server' value='http://117.121.22.6:8003/gm'/>收聚神光</td>");
		out.print("<td ><div id='http://117.121.22.7:8003/gm' style='display:none;'>云心鹤眼</div><input type='checkbox' id='app' name='server' value='http://117.121.22.7:8003/gm'/>云心鹤眼</td>");
		out.print("<td ><div id='http://117.121.22.15:8003/gm' style='display:none;'>西山晴雪</div><input type='checkbox' id='app' name='server' value='http://117.121.22.15:8003/gm'/>西山晴雪</td>");
		out.print("<td ><div id='http://117.121.22.17:8003/gm' style='display:none;'>灵山胜境</div><input type='checkbox' id='app' name='server' value='http://117.121.22.17:8003/gm'/>灵山胜境</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>当乐、UC不可见</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='dluc'></td>");
		out.print("<td ><div id='http://116.213.192.231:8001/gm' style='display:none;'>万世神兵</div><input type='checkbox' id='dluc' name='server' value='http://116.213.192.231:8001/gm'/>万世神兵</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>全混</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='quan'></td>");
		out.print("<td><div id='http://117.121.22.5:8003/gm' style='display:none;'>剑气箫心</div><input type='checkbox' id='quan' name='server' value='http://117.121.22.5:8003/gm'/>剑气箫心</td>");
		out.print("<td><div id='http://117.121.9.197:8003/gm' style='display:none;'>皎阳似火</div><input type='checkbox' id='quan' name='server' value='http://117.121.9.197:8003/gm'/>皎阳似火</td>");
		out.print("<td><div id='http://116.213.205.20:8002/gm' style='display:none;'>圣帝明王</div><input type='checkbox' id='quan' name='server' value='http://116.213.205.20:8002/gm'/>圣帝明王</td>");
		out.print("<td><div id='http://117.121.9.199:8003/gm' style='display:none;'>森罗鬼尊</div><input type='checkbox' id='quan' name='server' value='http://117.121.9.199:8003/gm'/>森罗鬼尊</td>");
		out.print("<td><div id='http://117.121.9.208:8001/gm' style='display:none;'>仙凡逍遥</div><input type='checkbox' id='quan' name='server' value='http://117.121.9.208:8001/gm'/>仙凡逍遥</td>");
		out.print("<td><div id='http://117.121.9.204:8002/gm' style='display:none;'>醉舞狂歌</div><input type='checkbox' id='quan' name='server' value='http://117.121.9.204:8002/gm'/>醉舞狂歌</td>");
		out.print("<td><div id='http://117.121.9.198:8002/gm' style='display:none;'>云锦天章</div><input type='checkbox' id='quan' name='server' value='http://117.121.9.198:8002/gm'/>云锦天章</td>");
		out.print("<td><div id='http://117.121.9.207:8003/gm' style='display:none;'>渊渟岳立</div><input type='checkbox' id='quan' name='server' value='http://117.121.9.207:8003/gm'/>渊渟岳立</td>");
		out.print("</tr>");
		out.print("<th>绿色全混</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='green'></td>");
		out.print("</tr>");
		out.print("<th>移动专服</th>");
		out.print("<td><div id='http://116.213.204.112:8003/gm' style='display:none;'>蓬莱仙境</div><input type='checkbox' id='server' name='server' value='http://116.213.204.112:8003/gm'/>蓬莱仙境</td>");
		out.print("<th><font color='red'>UC看不见</font></th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='uckbj'></td>");
		out.print("<td ><div id='http://117.121.9.204:8003/gm' style='display:none;'>玉树琼枝</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.204:8003/gm'/>玉树琼枝</td>");
		out.print("<td ><div id='http://116.213.205.18:8002/gm' style='display:none;'>淹旬旷月</div><input type='checkbox' id='uckbj' name='server' value='http://116.213.205.18:8002/gm'/>淹旬旷月</td>");
		out.print("<td ><div id='http://117.121.17.68:8001/gm' style='display:none;'>所向皆靡</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.17.68:8001/gm'/>所向皆靡</td>");
		out.print("<td ><div id='http://117.121.9.208:8002/gm' style='display:none;'>逾闲荡检 </div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.208:8002/gm'/>逾闲荡检</td>");
		out.print("<td ><div id='http://117.121.9.196:8002/gm' style='display:none;'>万象森罗 </div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.196:8002/gm'/>万象森罗</td>");
		out.print("<td ><div id='http://117.121.9.209:8001/gm' style='display:none;'>潜德秘行 </div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.209:8001/gm'/>潜德秘行</td>");
		out.print("<td ><div id='http://117.121.9.206:8002/gm' style='display:none;'>望风而逃 </div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.206:8002/gm'/>望风而逃</td>");
		out.print("<td ><div id='http://117.121.9.199:8003/gm' style='display:none;'>独霸一方 </div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.199:8003/gm'/>独霸一方</td>");
		out.print("<td ><div id='http://117.121.9.197:8001/gm' style='display:none;'>情比金坚</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.197:8001/gm'/>情比金坚</td>");
		out.print("<td ><div id='http://117.121.9.197:8002/gm' style='display:none;'>余韵流风</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.197:8002/gm'/>余韵流风</td>");
		out.print("<td ><div id='http://117.121.9.206:8001/gm' style='display:none;'>云海仙域</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.206:8001/gm'/>云海仙域</td>");
		out.print("<td ><div id='http://117.121.9.207:8002/gm' style='display:none;'>腾云驾雾</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.207:8002/gm'/>腾云驾雾</td>");
		out.print("<td ><div id='http://117.121.9.198:8001/gm' style='display:none;'>法天象地</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.198:8001/gm'/>法天象地</td>");
		out.print("<td ><div id='http://117.121.9.195:8001/gm' style='display:none;'>雕栏玉砌</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.195:8001/gm'/>雕栏玉砌</td>");
		out.print("<td ><div id='http://117.121.22.19:8001/gm' style='display:none;'>风月无涯</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.22.19:8001/gm'/>风月无涯</td>");
		out.print("<td ><div id='http://117.121.22.19:8002/gm' style='display:none;'>冰魂雪魄</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.22.19:8002/gm'/>冰魂雪魄</td>");
		out.print("<td ><div id='http://116.213.205.18:8001/gm' style='display:none;'>月夜花朝</div><input type='checkbox' id='uckbj' name='server' value='http://116.213.205.18:8001/gm'/>月夜花朝</td>");
		out.print("<td ><div id='http://116.213.205.19:8003/gm' style='display:none;'>星月相伴</div><input type='checkbox' id='uckbj' name='server' value='http://116.213.205.19:8003/gm'/>星月相伴</td>");
		out.print("<td ><div id='http://116.213.205.20:8001/gm' style='display:none;'>诚至金开</div><input type='checkbox' id='uckbj' name='server' value='http://116.213.205.20:8001/gm'/>诚至金开</td>");
		out.print("<td ><div id='http://117.121.9.194:8001/gm' style='display:none;'>福寿康宁</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.194:8001/gm'/>福寿康宁</td>");
		out.print("<td ><div id='http://117.121.9.208:8002/gm' style='display:none;'>仙姿玉质</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.208:8002/gm'/>仙姿玉质</td>");
		out.print("<td ><div id='http://117.121.9.205:8003/gm' style='display:none;'>跃马弯弓</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.205:8003/gm'/>跃马弯弓</td>");
		out.print("<td ><div id='http://117.121.9.204:8001/gm' style='display:none;'>青竹丹枫</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.204:8001/gm'/>青竹丹枫</td>");
		out.print("<td ><div id='http://116.213.205.19:8002/gm' style='display:none;'>暗香疏影</div><input type='checkbox' id='uckbj' name='server' value='http://116.213.205.19:8002/gm'/>暗香疏影</td>");
		out.print("<td ><div id='http://117.121.9.209:8003/gm' style='display:none;'>红尘百戏</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.209:8003/gm'/>红尘百戏</td>");
		out.print("<td ><div id='http://117.121.9.198:8003/gm' style='display:none;'>冰壶秋月</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.198:8003/gm'/>冰壶秋月</td>");
		out.print("<td ><div id='http://117.121.9.196:8002/gm' style='display:none;'>正明公道</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.196:8002/gm'/>正明公道</td>");
		out.print("<td ><div id='http://117.121.9.194:8002/gm' style='display:none;'>空城旧梦</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.194:8002/gm'/>空城旧梦</td>");
		out.print("<td ><div id='http://117.121.9.205:8001/gm' style='display:none;'>云中仙鹤</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.205:8001/gm'/>云中仙鹤</td>");
		out.print("<td ><div id='http://117.121.9.195:8002/gm' style='display:none;'>金鸡迎福</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.195:8002/gm'/>金鸡迎福</td>");
		out.print("<td ><div id='http://117.121.9.208:8003/gm' style='display:none;'>渔海樵山</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.208:8003/gm'/>渔海樵山</td>");
		out.print("<td ><div id='http://117.121.9.196:8003/gm' style='display:none;'>风清月白</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.196:8003/gm'/>风清月白</td>");
		out.print("<td ><div id='http://117.121.9.207:8002/gm' style='display:none;'>凤髓龙肝</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.207:8002/gm'/>凤髓龙肝</td>");
		out.print("<td ><div id='http://117.121.9.199:8002/gm' style='display:none;'>星梦高飞</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.199:8002/gm'/>星梦高飞</td>");
		out.print("<td ><div id='http://117.121.9.204:8003/gm' style='display:none;'>黄龙吐翠</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.204:8003/gm'/>黄龙吐翠</td>");
		out.print("<td ><div id='http://117.121.9.196:8001/gm' style='display:none;'>破浪乘风</div><input type='checkbox' id='uckbj' name='server' value='http://117.121.9.196:8001/gm'/>破浪乘风</td>");
		
		out.print("</tr>");
		out.print("</tr>");
		out.print("<th><font color='red'>win专服</font></th>");
		out.print("<td><div id='http://116.213.205.18:8003/gm' style='display:none;'>群雄风云</div><input type='checkbox' id='server' name='server' value='http://116.213.205.18:8003/gm'/>群雄风云</td>");
		out.print("</tr>");
		out.print("<th><font color='red'>国际服</font></th>");
		out.print("<td><div id='http://116.213.204.115:8001/gm' style='display:none;'>幽恋蝶谷</div><input type='checkbox' id='server' name='server' value='http://116.213.204.115:8001/gm'/>幽恋蝶谷</td>");
		out.print("</tr>");
		out.print("</tr>");
		out.print("<th><font color='red'>测试服</font></th>");
		out.print("<td><div id='http://116.213.192.203:8080/gm' style='display:none;'>国内本地测试</div><input type='checkbox' id='server' name='server' value='http://116.213.192.203:8080/gm'/>国内本地测试</td>");
		out.print("<td><div id='http://124.248.40.18:8082/gm' style='display:none;'>pan2</div><input type='checkbox' id='server' name='server' value='http://124.248.40.18:8082/gm'/>pan2</td>");
		out.print("<td><div id='http://124.248.40.18:18818/gm' style='display:none;'>潘多拉星</div><input type='checkbox' id='server' name='server' value='http://124.248.40.18:18818/gm'/>潘多拉星</td>");
		out.print("</tr>");
		
		out.print("<font color='red'><hr></font></tr>");
		out.print("<th><font color='red'>给选中的渠道玩家发公告</font></th>");
		out.print("<td>APP<input type='checkbox' id='qudao' name='qudao' value='APPSTORE_MIESHI'/></td><td>APP国际<input type='checkbox' id='qudao1' name='qudao1' value='APPSTOREGUOJI_MIESHI'/></td><td>此功能先别用</td>");
		out.print("</tr>");
		
 %>	
 
 	 </form>
</table>

