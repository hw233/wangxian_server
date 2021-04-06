package com.sqage.stat.language;

import java.util.HashMap;

import com.xuanzhi.tools.text.StringUtil;
/**
 * @author
 */
public class Translate{
	
	public static String redcolor = "FF0000";
	
	public static String male = "男";
	public static String female = "女";
	public static String unknown = "未知";
	public static String no = "无";
	public static String detail = "详情";
	
	public static String 银子类道具="碎银子','银两','银块','银条','银锭','银砖','普通红包','白银红包','黄金红包','白金红包','钻石红包','镖银(白)','镖银(绿)','镖银(蓝)','镖银(紫)','镖银(橙)','镖金(白)','镖金(绿)','镖金(蓝)','镖金(紫)','镖金(橙)','镖玉(白)','镖玉(绿)','镖玉(蓝)','镖玉(紫)','镖玉(橙)";
	public static String 宠物蛋="地狱狼王蛋','青丘妖狐蛋','绝世花妖蛋','霸刀魔王蛋','黑风熊精蛋','金甲游神蛋','擎天牛神蛋','六耳猕猴蛋','麒麟圣祖蛋','白虎战神蛋','青龙斗神蛋','玄武水神蛋','朱雀火神蛋','九阴魔蝎蛋','青翼蝠王蛋','乱舞蝶妖蛋','罗刹鬼王蛋','嗜血剑魂蛋','千年参妖蛋','雷霆天尊蛋','大龙人蛋";
	public static String 飞行坐骑="爱的炫舞','浪漫今生','碧虚青鸾','八卦仙蒲','梦瞳仙鹤','乾坤葫芦','金极龙皇','焚焰火扇','深渊魔章','碧虚青鸾(1天)','碧虚青鸾(7天)','碧虚青鸾(体验)";
	public static String 重要碎片="青鸾翎羽','朱雀火羽','战火残片','才子印记','武圣印记','武圣印记碎片','青龙精魂','朱雀精魂','白虎精魂','玄武精魂','麒麟精魂";
	public static String 万灵榜装备="装备名称','猛志固常在','五岳倒为轻','日曜贪狼盔','兽面吞火铠','血魂肩铠','紫铖护手','紫铖战靴','紫铖腰带','天启戒','王天古玉','太极图','天地为一朝','蚩尤瀑布碎','火曜破军盔','魔麟饕餮铠','皇鳞肩铠','遁龙护手','遁龙战靴','遁龙腰带','暝天戒','辟邪石玉','破天图','彩凤双飞翼','风萧易水寒','东极炫金罩','东极炫金衫','东极炫金肩','东极炫金手','东极炫金鞋','赤血','噬灵戒','浮世印','转空','明月照九州','承影无形杀','青冥海狱罩','青冥海狱衫','青冥海狱肩','青冥海狱手','天妖灭魂鞋','残雪','刺尨戒','苍天印','玄曦','点睛龙破壁','玄天斩灵洛','小北炎极头','小北炎极袄','小北炎极垫肩','离火手套','离火轻履','离火坠饰','火熔晶','火熔项链','逆央镜','泼墨碧丹青','血晶摩诃惑','海水江崖头','海水江崖袄','海水江崖垫肩','玄冰手套','玄冰轻履','玄冰坠饰','婆罗戒','婆罗项链','玄冰境','一岁一枯荣','本来无一物','乾坤地理蛇兜','乾坤地理裙','千幻蛛巾','千幻蝎腕','千幻道履','千幻腰带','封魔戒','落魂钟','毒王鼎','凤歌楚狂人','醉古堂剑扫','九黎太虚蛇兜','九黎太虚衫','蛟龙蛛巾','蛟龙蝎腕','蛟龙木履','蛟龙腰带','万魔戒','坤木蛊','星罗盘','朱雀斧','武圣禅杖','战火甲胄','朱雀刺','武圣轮','战火衫','朱雀笔','武圣剑','战火袄','朱雀幡','武圣杖','战火蟒衣";
	public static String 酒="杏花村','屠苏酒','妙沁药酒";
	public static String 屠魔帖="屠魔帖●降魔','屠魔帖●逍遥','屠魔帖●霸者','屠魔帖●朱雀','屠魔帖●水晶','屠魔帖●倚天','屠魔帖●青虹','屠魔帖●赤霄','屠魔帖●震天','屠魔帖●天罡";
	public static String 古董="古董";

	
	
	
	/**
	 * 根据传入的字符串数组组成这些key和value的Map
	 * 传进一段字符串s，根据map的key对字符串里的字符进行匹配，匹配上后就用map的value进行替换，返回替换后的字符串
	 * @param s
	 * @param keyValues String keyValues[][] = new String[][]{{"PLAYER1","米佳"},{"LEVEL1","41"},{"ARTICLE1","馒头"}...}
	 * @return
	 * @throws Exception
	 */
	public static String translateString(String s, String[][] keyValues) {
		long start = System.currentTimeMillis();
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			if(keyValues != null && keyValues.length > 0){
				for(int i = 0; i < keyValues.length; i++){
					map.put(keyValues[i][0], keyValues[i][1]);
				}
			}
			StringBuffer sb = new StringBuffer();
			char cs[] = s.toCharArray();
			char dota = '@';
			int firstDota = -1;
			for( int i=0;i< cs.length;i++){
				if(firstDota< 0){
					if( cs[i] == dota){
						firstDota = i;
					}else{
						sb.append(cs[i]);
					}
				}else{
					if( cs[i] == dota){					
						String key = new String(cs,firstDota,i-firstDota+1);
						String value = (String)map.get(key);
						if(value!= null){
							sb.append(value);
							firstDota = -1;
						}else{
							sb.append(key);
							firstDota = i;
						}
					}
				}
			}
			if( firstDota>=0 && firstDota< cs.length){
				sb.append(new String(cs,firstDota,cs.length-firstDota));
			}
			
			return sb.toString();
		} catch(Exception e) {
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<keyValues.length; i++) {
				sb.append("{" + StringUtil.arrayToString(keyValues[i], ",")  + "}");
			}
			return "";
		}
	}
	
	public static void main(String[] args) {
		String test="玩家@userName@的仙果被@friendName@偷走了，@userName@欲哭无泪啊……";
		try {
			
//			String rr = translateString(test, new String[][]{{"@playername@","米加"},{"@num@","100"},{"@articlename@","拉面"}});
			String rr = translateString(test, new String[][]{{"@userName@", "2222"},{"@friendName@","11111"}});
			System.out.println(rr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
