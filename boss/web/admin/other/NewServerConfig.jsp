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
		out.print("<th>91不可见</th>");
		out.print("<td ><div id='http://116.213.192.212:8002/gm' style='display:none;'>云波鬼岭</div><input type='checkbox' id='server' name='server' value='http://116.213.192.212:8002/gm'/>云波鬼岭</td>");
		out.print("<td ><div id='http://116.213.192.226:8001/gm' style='display:none;'>黄金海岸</div><input type='checkbox' id='server' name='server' value='http://116.213.192.226:8001/gm'/>黄金海岸</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>91、UC不可见</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='91uc'></td>");
		out.print("<td ><div id='http://116.213.192.229:8001/gm' style='display:none;'>蓬莱仙阁</div></div><input type='checkbox' id='91uc' name='server' value='http://116.213.192.229:8001/gm'/>蓬莱仙阁</td>");
		out.print("<td ><div id='http://116.213.192.244:8001/gm' style='display:none;'>乾元金光</div><input type='checkbox' id='91uc' name='server' value='http://116.213.192.244:8001/gm'/>乾元金光</td>");
		out.print("<td ><div id='http://116.213.193.68:8001/gm' style='display:none;'>北冥佛光</div><input type='checkbox' id='91uc' name='server' value='http://116.213.193.68:8001/gm'/>北冥佛光</td>");
		out.print("<td ><div id='http://116.213.193.71:8001/gm' style='display:none;'>七宝莲台</div><input type='checkbox' id='91uc' name='server' value='http://116.213.193.71:8001/gm'/>七宝莲台</td>");
		out.print("<td ><div id='http://116.213.204.70:8001/gm' style='display:none;'>明空梵天</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.70:8001/gm'/>明空梵天</td>");
		out.print("<td ><div id='http://116.213.204.82:8001/gm' style='display:none;'>冲霄云楼</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.82:8001/gm'/>冲霄云楼</td>");
		out.print("<td ><div id='http://211.151.127.230:8001/gm' style='display:none;'>王者之域</div><input type='checkbox' id='91uc' name='server' value='http://211.151.127.230:8001/gm'/>王者之域</td>");
		out.print("<td ><div id='http://211.151.127.232:8001/gm' style='display:none;'>独战群神</div><input type='checkbox' id='91uc' name='server' value='http://211.151.127.232:8001/gm'/>独战群神</td>");
		out.print("<td ><div id='http://116.213.192.246:8001/gm' style='display:none;'>傲剑凌云</div><input type='checkbox' id='91uc' name='server' value='http://116.213.192.246:8001/gm'/>傲剑凌云</td>");
		out.print("<td ><div id='http://116.213.204.111:8001/gm' style='display:none;'>百花仙谷</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.111:8001/gm'/>百花仙谷</td>");
		out.print("<td><div id='http://116.213.204.80:8001/gm' style='display:none;'>金蛇圣殿</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.80:8001/gm'/>金蛇圣殿</td>");
		out.print("<td><div id='http://116.213.192.232:8001/gm' style='display:none;'>雄霸天下</div><input type='checkbox' id='91uc' name='server' value='http://116.213.192.232:8001/gm'/>雄霸天下</td>");
		out.print("<td><div id='http://116.213.204.69:8001/gm' style='display:none;'>仙界至尊</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.69:8001/gm'/>仙界至尊</td>");
		out.print("<td><div id='http://116.213.204.140:8001/gm' style='display:none;'>月满乾坤</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.140:8001/gm'/>月满乾坤</td>");
		out.print("<td><div id='http://116.213.204.132:8001/gm' style='display:none;'>勇者无敌</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.132:8001/gm'/>勇者无敌</td>");
		out.print("<td><div id='http://116.213.204.112:8002/gm' style='display:none;'>傲视群雄</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.112:8002/gm'/>傲视群雄</td>");
		out.print("<td ><div id='http://116.213.204.134:8001/gm' style='display:none;'>问鼎江湖</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.134:8001/gm'/>问鼎江湖</td>");
		out.print("<td ><div id='http://116.213.192.248:8002/gm' style='display:none;'>笑傲江湖</div><input type='checkbox' id='91uc' name='server' value='http://116.213.192.248:8002/gm'/>笑傲江湖</td>");
		out.print("<td ><div id='http://116.213.204.164:8001/gm' style='display:none;'>鱼跃龙门</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.164:8001/gm'/>鱼跃龙门</td>");
		out.print("<td ><div id='http://116.213.192.244:8002/gm' style='display:none;'>龙翔凤舞</div><input type='checkbox' id='91uc' name='server' value='http://116.213.192.244:8002/gm'/>龙翔凤舞</td>");
		out.print("<td ><div id='http://116.213.204.71:8002/gm' style='display:none;'>华夏战神</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.71:8002/gm'/>华夏战神</td>");
		out.print("<td ><div id='http://116.213.204.176:8001/gm' style='display:none;'>启天之路</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.176:8001/gm'/>启天之路</td>");
		out.print("<td ><div id='http://116.213.204.145:8001/gm' style='display:none;'>上善若水</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.145:8001/gm'/>上善若水</td>");
		out.print("<td ><div id='http://116.213.204.165:8001/gm' style='display:none;'>春暖花开</div><input type='checkbox' id='91uc' name='server' value='http://116.213.204.165:8001/gm'/>春暖花开</td>");
		out.print("<td ><div id='http://117.121.22.4:8001/gm' style='display:none;'>桃花仙岛</div><input type='checkbox' id='91uc' name='server' value='http://117.121.22.4:8001/gm'/>桃花仙岛</td>");
		out.print("<td ><div id='http://117.121.22.15:8001/gm' style='display:none;'>花好月圆</div><input type='checkbox' id='91uc' name='server' value='http://117.121.22.15:8001/gm'/>花好月圆</td>");
		
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>腾讯</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='tx'></td>");
		out.print("<td ><div id='http://117.135.149.27:20026/gm' style='display:none;'>太虚幻境</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20026/gm'/>太虚幻境</td>");
		out.print("<td ><div id='http://117.135.149.27:20014/gm' style='display:none;'>幽冥山谷</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20014/gm'/>幽冥山谷</td>");
		out.print("<td ><div id='http://117.135.149.27:20020/gm' style='display:none;'>昆仑圣殿</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20020/gm'/>昆仑圣殿</td>");
		out.print("<td ><div id='http://117.135.149.27:20032/gm' style='display:none;'>凌霄宝殿</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20032/gm'/>凌霄宝殿</td>");
		out.print("<td ><div id='http://117.135.149.27:20184/gm' style='display:none;'>霸气乾坤</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20184/gm'/>霸气乾坤</td>");
		out.print("<td ><div id='http://117.135.149.27:20178/gm' style='display:none;'>烟雨青山</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.27:20178/gm'/>烟雨青山</td>");
		out.print("<td ><div id='http://117.135.130.45:8801/gm' style='display:none;'>仙山琼阁</div><input type='checkbox' id='tx' name='server' value='http://117.135.130.45:8801/gm'/>仙山琼阁</td>");
		out.print("<td ><div id='http://117.135.130.25:8801/gm' style='display:none;'>霸气无双</div><input type='checkbox' id='tx' name='server' value='http://117.135.130.25:8801/gm'/>霸气无双</td>");
		out.print("<td ><div id='http://117.135.149.97:8801/gm' style='display:none;'>华山之巅</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.97:8801/gm'/>华山之巅</td>");
		out.print("<td ><div id='http://117.135.149.99:8801/gm' style='display:none;'>神龙摆尾</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.99:8801/gm'/>神龙摆尾</td>");
		out.print("<td ><div id='http://117.135.130.112:8801/gm' style='display:none;'>柳暗花明</div><input type='checkbox' id='tx' name='server' value='http://117.135.130.112:8801/gm'/>柳暗花明</td>");
		out.print("<td ><div id='http://117.135.149.108:8801/gm' style='display:none;'>众仙归来</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.108:8801/gm'/>众仙归来</td>");
		out.print("<td ><div id='http://117.135.149.108:8802/gm' style='display:none;'>三界奇缘</div><input type='checkbox' id='tx' name='server' value='http://117.135.149.108:8802/gm'/>三界奇缘</td>");
		out.print("</tr>");
		out.print("<th>91专服</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='91'></td>");
		out.print("<td ><div id='http://116.213.192.242:8001/gm' style='display:none;'>金霞玉鼎</div><input type='checkbox' id='91' name='server' value='http://116.213.192.242:8001/gm'/>金霞玉鼎</td>");
		out.print("<td ><div id='http://116.213.192.245:8002/gm' style='display:none;'>海天佛国</div><input type='checkbox' id='91' name='server' value='http://116.213.192.245:8002/gm'/>海天佛国</td>");
		out.print("<td ><div id='http://116.213.193.69:8001/gm' style='display:none;'>百花深谷</div><input type='checkbox' id='91' name='server' value='http://116.213.193.69:8001/gm'/>百花深谷</td>");
		out.print("<td ><div id='http://116.213.193.66:8001/gm' style='display:none;'>东极仙岛</div><input type='checkbox' id='91' name='server' value='http://116.213.193.66:8001/gm'/>东极仙岛</td>");
		out.print("<td ><div id='http://116.213.204.70:8002/gm' style='display:none;'>摘星楼台</div><input type='checkbox' id='91' name='server' value='http://116.213.204.70:8002/gm'/>摘星楼台</td>");
		out.print("<td ><div id='http://116.213.204.85:8001/gm' style='display:none;'>幽灵山庄</div><input type='checkbox' id='91' name='server' value='http://116.213.204.85:8001/gm'/>幽灵山庄</td>");
		out.print("<td ><div id='http://116.213.204.81:8003/gm' style='display:none;'>斩龙神台</div><input type='checkbox' id='91' name='server' value='http://116.213.204.81:8003/gm'/>斩龙神台</td>");
		out.print("<td ><div id='http://211.151.127.231:8001/gm' style='display:none;'>惊天战神</div><input type='checkbox' id='91' name='server' value='http://211.151.127.231:8001/gm'/>惊天战神</td>");
		out.print("<td ><div id='http://116.213.192.243:8001/gm' style='display:none;'>岚境仙音</div><input type='checkbox' id='91' name='server' value='http://116.213.192.243:8001/gm'/>岚境仙音</td>");
		out.print("<td ><div id='http://116.213.192.211:8001/gm' style='display:none;'>伏魔幻境</div><input type='checkbox' id='91' name='server' value='http://116.213.192.211:8001/gm'/>伏魔幻境</td>");
		out.print("<td ><div id='http://116.213.204.98:8001/gm' style='display:none;'>六道轮回</div><input type='checkbox' id='91' name='server' value='http://116.213.204.98:8001/gm'/>六道轮回</td>");
		out.print("<td ><div id='http://116.213.204.112:8001/gm' style='display:none;'>太极玄天</div><input type='checkbox' id='91' name='server' value='http://116.213.204.112:8001/gm'/>太极玄天</td>");
		out.print("<td ><div id='http://116.213.204.66:8002/gm' style='display:none;'>狂龙魔窟</div><input type='checkbox' id='91' name='server' value='http://116.213.204.66:8002/gm'/>狂龙魔窟</td>");
		out.print("<td ><div id='http://116.213.192.213:8001/gm' style='display:none;'>日光峡谷</div><input type='checkbox' id='91' name='server' value='http://116.213.192.213:8001/gm'/>日光峡谷</td>");
		out.print("<td ><div id='http://116.213.204.102:8001/gm' style='display:none;'>四海之巅</div><input type='checkbox' id='91' name='server' value='http://116.213.204.102:8001/gm'/>四海之巅</td>");
		out.print("<td ><div id='http://116.213.192.216:8001/gm' style='display:none;'>壮气凌云</div><input type='checkbox' id='91' name='server' value='http://116.213.192.216:8001/gm'/>壮气凌云</td>");
		out.print("<td ><div id='http://116.213.204.116:8001/gm' style='display:none;'>江山如画</div><input type='checkbox' id='91' name='server' value='http://116.213.204.116:8001/gm'/>江山如画</td>");
		out.print("<td ><div id='http://116.213.204.130:8001/gm' style='display:none;'>碧海青天</div><input type='checkbox' id='91' name='server' value='http://116.213.204.130:8001/gm'/>碧海青天</td>");
		out.print("<td ><div id='http://116.213.204.133:8001/gm' style='display:none;'>侠骨柔肠</div><input type='checkbox' id='91' name='server' value='http://116.213.204.133:8001/gm'/>侠骨柔肠</td>");
		out.print("<td ><div id='http://116.213.204.98:8002/gm' style='display:none;'>凌霜傲雪</div><input type='checkbox' id='91' name='server' value='http://116.213.204.98:8002/gm'/>凌霜傲雪</td>");
		out.print("<td ><div id='http://116.213.193.66:8002/gm' style='display:none;'>仙岛秘境</div><input type='checkbox' id='91' name='server' value='http://116.213.193.66:8002/gm'/>仙岛秘境</td>");
		out.print("<td ><div id='http://116.213.204.68:8002/gm' style='display:none;'>拨云揽月</div><input type='checkbox' id='91' name='server' value='http://116.213.204.68:8002/gm'/>拨云揽月</td>");
		out.print("<td ><div id='http://116.213.204.162:8001/gm' style='display:none;'>马踏千里</div><input type='checkbox' id='91' name='server' value='http://116.213.204.162:8001/gm'/>马踏千里</td>");
		out.print("<td ><div id='http://116.213.204.173:8001/gm' style='display:none;'>游云惊龙</div><input type='checkbox' id='91' name='server' value='http://116.213.204.173:8001/gm'/>游云惊龙</td>");
		out.print("<td ><div id='http://116.213.204.144:8001/gm' style='display:none;'>仗剑天涯</div><input type='checkbox' id='91' name='server' value='http://116.213.204.144:8001/gm'/>仗剑天涯</td>");
		out.print("<td ><div id='http://116.213.204.177:8001/gm' style='display:none;'>金风玉露</div><input type='checkbox' id='91' name='server' value='http://116.213.204.177:8001/gm'/>金风玉露</td>");
		out.print("<td ><div id='http://116.213.192.214:8002/gm' style='display:none;'>鸟语花香</div><input type='checkbox' id='91' name='server' value='http://116.213.192.214:8002/gm'/>鸟语花香</td>");
		out.print("<td ><div id='http://117.121.22.5:8001/gm' style='display:none;'>桃李春风</div><input type='checkbox' id='91' name='server' value='http://117.121.22.5:8001/gm'/>桃李春风</td>");
		out.print("<td ><div id='http://116.213.204.83:8002/gm' style='display:none;'>洪荒古殿</div><input type='checkbox' id='91' name='server' value='http://116.213.204.83:8002/gm'/>洪荒古殿</td>");
		out.print("<td ><div id='http://116.213.192.213:8002/gm' style='display:none;'>镜花水月</div><input type='checkbox' id='91' name='server' value='http://116.213.192.213:8002/gm'/>镜花水月</td>");
		out.print("<td ><div id='http://116.213.204.167:8001/gm' style='display:none;'>琼华烬染</div><input type='checkbox' id='91' name='server' value='http://116.213.204.167:8001/gm'/>琼华烬染</td>");
		out.print("<td ><div id='http://211.151.127.228:8002/gm' style='display:none;'>桂殿兰宫</div><input type='checkbox' id='91' name='server' value='http://211.151.127.228:8002/gm'/>桂殿兰宫</td>");
		out.print("<td ><div id='http://116.213.204.143:8002/gm' style='display:none;'>轮回之剑</div><input type='checkbox' id='91' name='server' value='http://116.213.204.143:8002/gm'/>轮回之剑</td>");
		out.print("<td ><div id='http://116.213.204.166:8001/gm' style='display:none;'>仙露琼浆</div><input type='checkbox' id='91' name='server' value='http://116.213.204.166:8001/gm'/>仙露琼浆</td>");
		out.print("<td ><div id='http://116.213.204.102:8002/gm' style='display:none;'>程门立雪</div><input type='checkbox' id='91' name='server' value='http://116.213.204.102:8002/gm'/>程门立雪</td>");
		out.print("<td ><div id='http://211.151.127.231:8002/gm' style='display:none;'>天上人间</div><input type='checkbox' id='91' name='server' value='http://211.151.127.231:8002/gm'/>天上人间</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>UC专服</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='uc'></td>");
		out.print("<td ><div id='http://116.213.192.247:8002/gm' style='display:none;'>普陀潮音</div><input type='checkbox' id='uc' name='server' value='http://116.213.192.247:8002/gm'/>普陀潮音</td>");
		out.print("<td ><div id='http://116.213.192.228:8001/gm' style='display:none;'>鹊桥仙境</div><input type='checkbox' id='uc' name='server' value='http://116.213.192.228:8001/gm'/>鹊桥仙境</td>");
		out.print("<td ><div id='http://116.213.192.231:8002/gm' style='display:none;'>瑶台仙宫</div><input type='checkbox' id='uc' name='server' value='http://116.213.192.231:8002/gm'/>瑶台仙宫</td>");
		out.print("<td ><div id='http://116.213.192.242:8002/gm' style='display:none;'>紫阳青峰</div><input type='checkbox' id='uc' name='server' value='http://116.213.192.242:8002/gm'/>紫阳青峰</td>");
		out.print("<td ><div id='http://116.213.192.247:8001/gm' style='display:none;'>崆峒灵宝</div><input type='checkbox' id='uc' name='server' value='http://116.213.192.247:8001/gm'/>崆峒灵宝</td>");
		out.print("<td ><div id='http://116.213.193.72:8001/gm' style='display:none;'>万佛朝宗</div><input type='checkbox' id='uc' name='server' value='http://116.213.193.72:8001/gm'/>万佛朝宗</td>");
		out.print("<td ><div id='http://116.213.204.66:8001/gm' style='display:none;'>瀛台仙山</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.66:8001/gm'/>瀛台仙山</td>");
		out.print("<td ><div id='http://116.213.204.72:8002/gm' style='display:none;'>无量净土</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.72:8002/gm'/>无量净土</td>");
		out.print("<td ><div id='http://116.213.204.84:8001/gm' style='display:none;'>金宫银坊</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.84:8001/gm'/>金宫银坊</td>");
		out.print("<td ><div id='http://211.151.127.228:8001/gm' style='display:none;'>遨游四海</div><input type='checkbox' id='uc' name='server' value='http://211.151.127.228:8001/gm'/>遨游四海</td>");
		out.print("<td ><div id='http://211.151.127.229:8001/gm' style='display:none;'>傲视三界</div><input type='checkbox' id='uc' name='server' value='http://211.151.127.229:8001/gm'/>傲视三界</td>");
		out.print("<td ><div id='http://211.151.127.233:8001/gm' style='display:none;'>霸气纵横</div><input type='checkbox' id='uc' name='server' value='http://211.151.127.233:8001/gm'/>霸气纵横</td>");
		out.print("<td ><div id='http://116.213.192.248:8001/gm' style='display:none;'>龙脉之地</div><input type='checkbox' id='uc' name='server' value='http://116.213.192.248:8001/gm'/>龙脉之地</td>");
		out.print("<td ><div id='http://116.213.192.214:8001/gm' style='display:none;'>九霄之巅</div><input type='checkbox' id='uc' name='server' value='http://116.213.192.214:8001/gm'/>九霄之巅</td>");
		out.print("<td ><div id='http://116.213.192.227:8001/gm' style='display:none;'>游龙引凤</div><input type='checkbox' id='uc' name='server' value='http://116.213.192.227:8001/gm'/>游龙引凤</td>");
		out.print("<td ><div id='http://116.213.204.100:8001/gm' style='display:none;'>百炼成仙</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.100:8001/gm'/>百炼成仙</td>");
		out.print("<td ><div id='http://116.213.204.113:8001/gm' style='display:none;'>龙飞凤舞</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.113:8001/gm'/>龙飞凤舞</td>");
		out.print("<td ><div id='http://116.213.204.101:8001/gm' style='display:none;'>通天浮屠</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.101:8001/gm'/>通天浮屠</td>");
		out.print("<td ><div id='http://116.213.204.114:8001/gm' style='display:none;'>逍遥神殿</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.114:8001/gm'/>逍遥神殿</td>");
		out.print("<td ><div id='http://116.213.204.115:8001/gm' style='display:none;'>纵横天下</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.115:8001/gm'/>纵横天下</td>");
		out.print("<td ><div id='http://116.213.204.71:8001/gm' style='display:none;'>叱咤风云</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.71:8001/gm'/>叱咤风云</td>");
		out.print("<td ><div id='http://211.151.127.234:8001/gm' style='display:none;'>鹏程万里</div><input type='checkbox' id='uc' name='server' value='http://211.151.127.234:8001/gm'/>鹏程万里</td>");
		out.print("<td ><div id='http://116.213.204.131:8001/gm' style='display:none;'>仙山云海</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.131:8001/gm'/>仙山云海</td>");
		out.print("<td ><div id='http://116.213.204.142:8001/gm' style='display:none;'>唯我独尊</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.142:8001/gm'/>唯我独尊</td>");
		out.print("<td ><div id='http://116.213.204.113:8002/gm' style='display:none;'>谁与争锋</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.113:8002/gm'/>谁与争锋</td>");
		out.print("<td><div id='http://116.213.204.143:8001/gm' style='display:none;'>逆转乾坤</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.143:8001/gm'/>逆转乾坤</td>");
		out.print("<td><div id='http://116.213.204.67:8002/gm' style='display:none;'>独闯天涯</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.67:8002/gm'/>独闯天涯</td>");
		out.print("<td><div id='http://116.213.204.163:8001/gm' style='display:none;'>龙啸九州</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.163:8001/gm'/>龙啸九州</td>");
		out.print("<td><div id='http://116.213.204.174:8001/gm' style='display:none;'>龙腾虎跃</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.174:8001/gm'/>龙腾虎跃</td>");
		out.print("<td><div id='http://116.213.193.71:8002/gm' style='display:none;'>踞虎盘龙</div><input type='checkbox' id='uc' name='server' value='http://116.213.193.71:8002/gm'/>踞虎盘龙</td>");
		out.print("<td><div id='http://116.213.204.111:8002/gm' style='display:none;'>九仙揽月</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.111:8002/gm'/>九仙揽月</td>");
		out.print("<td><div id='http://116.213.204.146:8001/gm' style='display:none;'>蜂飞蝶舞</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.146:8001/gm'/>蜂飞蝶舞</td>");
		out.print("<td><div id='http://116.213.204.101:8002/gm' style='display:none;'>江山美人</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.101:8002/gm'/>江山美人</td>");
		out.print("<td ><div id='http://116.213.204.178:8001/gm' style='display:none;'>千娇百媚</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.178:8001/gm'/>千娇百媚</td>");
		out.print("<td ><div id='http://116.213.204.114:8002/gm' style='display:none;'>浩瀚乾坤</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.114:8002/gm'/>浩瀚乾坤</td>");
		out.print("<td ><div id='http://116.213.204.135:8001/gm' style='display:none;'>千秋北斗</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.135:8001/gm'/>千秋北斗</td>");
		out.print("<td ><div id='http://116.213.204.131:8002/gm' style='display:none;'>绝代风华</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.131:8002/gm'/>绝代风华</td>");
		out.print("<td ><div id='http://117.121.22.6:8001/gm' style='display:none;'>瑶林琼树</div><input type='checkbox' id='uc' name='server' value='http://117.121.22.6:8001/gm'/>瑶林琼树</td>");
		out.print("<td ><div id='http://116.213.204.168:8001/gm' style='display:none;'>众生皆拜</div><input type='checkbox' id='uc' name='server' value='http://116.213.204.168:8001/gm'/>众生皆拜</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>苹果专服<font color='app'></th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='app'></td>");
		out.print("<td ><div id='http://116.213.193.67:8001/gm' style='display:none;'>西方灵山</div><input type='checkbox' id='app' name='server' value='http://116.213.193.67:8001/gm'/>西方灵山</td>");
		out.print("<td ><div id='http://116.213.192.245:8001/gm' style='display:none;'>飞瀑龙池</div><input type='checkbox' id='app' name='server' value='http://116.213.192.245:8001/gm'/>飞瀑龙池</td>");
		out.print("<td ><div id='http://116.213.192.228:8002/gm' style='display:none;'>玉幡宝刹</div><input type='checkbox' id='app' name='server' value='http://116.213.192.228:8002/gm'/>玉幡宝刹</td>");
		out.print("<td ><div id='http://116.213.192.215:8002/gm' style='display:none;'>问天灵台</div><input type='checkbox' id='app' name='server' value='http://116.213.192.215:8002/gm'/>问天灵台</td>");
		out.print("<td ><div id='http://116.213.193.70:8001/gm' style='display:none;'>雪域冰城</div><input type='checkbox' id='app' name='server' value='http://116.213.193.70:8001/gm'/>雪域冰城</td>");
		out.print("<td ><div id='http://116.213.204.67:8001/gm' style='display:none;'>白露横江</div><input type='checkbox' id='app' name='server' value='http://116.213.204.67:8001/gm'/>白露横江</td>");
		out.print("<td ><div id='http://116.213.204.72:8001/gm' style='display:none;'>左岸花海</div><input type='checkbox' id='app' name='server' value='http://116.213.204.72:8001/gm'/>左岸花海</td>");
		out.print("<td ><div id='http://116.213.204.68:8001/gm' style='display:none;'>裂风峡谷</div><input type='checkbox' id='app' name='server' value='http://116.213.204.68:8001/gm'/>裂风峡谷</td>");
		out.print("<td ><div id='http://116.213.204.83:8001/gm' style='display:none;'>右道长亭</div><input type='checkbox' id='app' name='server' value='http://116.213.204.83:8001/gm'/>右道长亭</td>");
		out.print("<td ><div id='http://116.213.204.99:8001/gm' style='display:none;'>永安仙城</div><input type='checkbox' id='app' name='server' value='http://116.213.204.99:8001/gm'/>永安仙城</td>");
		out.print("<td ><div id='http://116.213.192.230:8001/gm' style='display:none;'>霹雳霞光</div><input type='checkbox' id='app' name='server' value='http://116.213.192.230:8001/gm'/>霹雳霞光</td>");
		out.print("<td ><div id='http://116.213.204.103:8001/gm' style='display:none;'>对酒当歌</div><input type='checkbox' id='app' name='server' value='http://116.213.204.103:8001/gm'/>对酒当歌</td>");
		out.print("<td ><div id='http://116.213.204.141:8001/gm' style='display:none;'>独霸一方</div><input type='checkbox' id='app' name='server' value='http://116.213.204.141:8001/gm'/>独霸一方</td>");
		out.print("<td ><div id='http://116.213.204.99:8002/gm' style='display:none;'>独步天下</div><input type='checkbox' id='app' name='server' value='http://116.213.204.99:8002/gm'/>独步天下</td>");
		out.print("<td ><div id='http://116.213.193.72:8002/gm' style='display:none;'>飞龙在天</div><input type='checkbox' id='app' name='server' value='http://116.213.193.72:8002/gm'/>飞龙在天</td>");
		out.print("<td ><div id='http://116.213.204.175:8001/gm' style='display:none;'>九霄龙吟</div><input type='checkbox' id='app' name='server' value='http://116.213.204.175:8001/gm'/>九霄龙吟</td>");
		out.print("<td ><div id='http://116.213.204.100:8002/gm' style='display:none;'>万象更新</div><input type='checkbox' id='app' name='server' value='http://116.213.204.100:8002/gm'/>万象更新</td>");
		out.print("<td ><div id='http://117.121.22.3:8001/gm' style='display:none;'>春风得意</div><input type='checkbox' id='app' name='server' value='http://117.121.22.3:8001/gm'/>春风得意</td>");
		out.print("<td ><div id='http://116.213.192.230:8002/gm' style='display:none;'>天下无双</div><input type='checkbox' id='app' name='server' value='http://116.213.192.230:8002/gm'/>天下无双</td>");
		out.print("<td ><div id='http://116.213.204.116:8002/gm' style='display:none;'>仙子乱尘</div><input type='checkbox' id='app' name='server' value='http://116.213.204.116:8002/gm'/>仙子乱尘</td>");
		out.print("<td ><div id='http://116.213.204.136:8001/gm' style='display:none;'>幻灵仙境</div><input type='checkbox' id='app' name='server' value='http://116.213.204.136:8001/gm'/>幻灵仙境</td>");
		out.print("<td ><div id='http://116.213.204.179:8001/gm' style='display:none;'>梦倾天下</div><input type='checkbox' id='app' name='server' value='http://116.213.204.179:8001/gm'/>梦倾天下</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>当乐、UC不可见</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='dluc'></td>");
		out.print("<td ><div id='http://116.213.192.212:8001/gm' style='display:none;'>云海冰岚</div><input type='checkbox' id='dluc' name='server' value='http://116.213.192.212:8001/gm'/>云海冰岚</td>");
		out.print("<td ><div id='http://116.213.192.215:8001/gm' style='display:none;'>无极冰原</div><input type='checkbox' id='dluc' name='server' value='http://116.213.192.215:8001/gm'/>无极冰原</td>");
		out.print("<td ><div id='http://116.213.192.226:8002/gm' style='display:none;'>福地洞天</div><input type='checkbox' id='dluc' name='server' value='http://116.213.192.226:8002/gm'/>福地洞天</td>");
		out.print("<td ><div id='http://116.213.192.231:8001/gm' style='display:none;'>峨嵋金顶</div><input type='checkbox' id='dluc' name='server' value='http://116.213.192.231:8001/gm'/>峨嵋金顶</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<th>全混</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='quan'></td>");
		out.print("<td><div id='http://116.213.192.194:8001/gm' style='display:none;'>风雪之巅</div><input type='checkbox' id='quan' name='server' value='http://116.213.192.194:8001/gm'/>风雪之巅</td>");
		out.print("<td><div id='http://116.213.192.195:8001/gm' style='display:none;'>桃源仙境</div><input type='checkbox' id='quan' name='server' value='http://116.213.192.195:8001/gm'/>桃源仙境</td>");
		out.print("<td><div id='http://116.213.192.196:8001/gm' style='display:none;'>飘渺王城</div><input type='checkbox' id='quan' name='server' value='http://116.213.192.196:8001/gm'/>飘渺王城</td>");
		out.print("<td><div id='http://116.213.192.197:8001/gm' style='display:none;'>千年古城</div><input type='checkbox' id='quan' name='server' value='http://116.213.192.197:8001/gm'/>千年古城</td>");
		out.print("<td><div id='http://116.213.142.181:8001/gm' style='display:none;'>燃烧圣殿</div><input type='checkbox' id='quan' name='server' value='http://116.213.142.181:8001/gm'/>燃烧圣殿</td>");
		out.print("<td><div id='http://116.213.142.181:8002/gm' style='display:none;'>太华神山</div><input type='checkbox' id='quan' name='server' value='http://116.213.142.181:8002/gm'/>太华神山</td>");
		out.print("<td><div id='http://116.213.192.210:8001/gm' style='display:none;'>东海龙宫</div><input type='checkbox' id='quan' name='server' value='http://116.213.192.210:8001/gm'/>东海龙宫</td>");
		out.print("<td><div id='http://116.213.192.210:8002/gm' style='display:none;'>炼狱绝地</div><input type='checkbox' id='quan' name='server' value='http://116.213.192.210:8002/gm'/>炼狱绝地</td>");
		out.print("</tr>");
		out.print("<th>绿色全混</th>");
		out.print("<td><input type = 'button' onclick='selectall(this)' value='green'></td>");
		out.print("<td><div id='http://117.121.22.14:8001/gm' style='display:none;'>万里苍穹</div><input type='checkbox' id='green' name='server' value='http://117.121.22.14:8001/gm'/>万里苍穹</td>");
		out.print("<td><div id='http://117.121.22.13:8001/gm' style='display:none;'>飘渺仙道</div><input type='checkbox' id='green' name='server' value='http://117.121.22.13:8001/gm'/>飘渺仙道</td>");
		out.print("<td><div id='http://116.213.204.162:8002/gm' style='display:none;'>修罗转生</div><input type='checkbox' id='green' name='server' value='http://116.213.204.162:8002/gm'/>修罗转生</td>");
		out.print("<td><div id='http://116.213.192.246:8002/gm' style='display:none;'>琼楼金阙</div><input type='checkbox' id='green' name='server' value='http://116.213.192.246:8002/gm'/>琼楼金阙</td>");
		out.print("<td ><div id='http://117.121.22.19:8001/gm' style='display:none;'>海纳百川</div><input type='checkbox' id='green' name='server' value='http://117.121.22.19:8001/gm'/>海纳百川</td>");
		out.print("<td ><div id='http://211.151.127.229:8002/gm' style='display:none;'>盛世欢歌</div><input type='checkbox' id='green' name='server' value='http://211.151.127.229:8002/gm'/>盛世欢歌</td>");
		out.print("</tr>");
		out.print("<th>移动专服</th>");
		out.print("<td><div id='http://116.213.193.70:8002/gm' style='display:none;'>蓬莱仙境</div><input type='checkbox' id='server' name='server' value='http://116.213.193.70:8002/gm'/>蓬莱仙境</td>");
		out.print("</tr>");
		out.print("<th><font color='red'>测试服</font></th>");
		out.print("<td><div id='http://116.213.142.189:8080/gm' style='display:none;'>巍巍昆仑</div><input type='checkbox' id='server' name='server' value='http://116.213.142.189:8080/gm'/>巍巍昆仑</td>");
		out.print("<td><div id='http://124.248.40.18:8082/gm' style='display:none;'>pan2</div><input type='checkbox' id='server' name='server' value='http://124.248.40.18:8082/gm'/>pan2</td>");
		out.print("</tr>");
		
 %>	
 	 </form>
</table>