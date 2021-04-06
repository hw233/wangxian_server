package com.fy.gamegateway.thirdpartner.congzhi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.JsonUtil;
/**
 * 
 * 
 *
 */
public class CongZhiQueryService {
	static Logger logger = Logger.getLogger(CongZhiQueryService.class);
	public static String url = "http://116.213.192.219:8813/stat_common_mieshi/query_congzhi.jsp";
	
	/**
	 * 
	 * 请求 http://116.213.142.188:8813/stat_common_mieshi/a.jsp?day=2012-08-16&username=wangyugang
	 * 
	 * 返回JSON串，也就是CongZhiRecord数组
	 * {
	 * 	[{
	 * 		"username":"wangyugang",
	 * 		"money":41800,
	 * 		"level":94,
	 * 		"fenqu":"",
	 * 		"channel":"",
	 * 		"congzhiType":"",
	 * 		"congzhiCardType":"",
	 * 		"congzhiTime":"",
	 * 		"platform":""
	 * 	},{
	 * 		"username":"wangyugang",
	 * 		"money":41800,
	 * 		"level":94,
	 * 		"fenqu":"",
	 * 		"channel":"",
	 * 		"congzhiType":"",
	 * 		"congzhiCardType":"",
	 * 		"congzhiTime":"",
	 * 		"platform":""
	 * 	},{
	 * 		"username":"wangyugang",
	 * 		"money":41800,
	 * 		"level":94,
	 * 		"fenqu":"",
	 * 		"channel":"",
	 * 		"congzhiType":"",
	 * 		"congzhiCardType":"",
	 * 		"congzhiTime":"",
	 * 		"platform":""

	 * 	}]
	 * }
	 * 
	 * 
	 * @param day
	 * @param username
	 * @return
	 */
	public static List<CongZhiRecord> query(String day,String username){
		ArrayList<CongZhiRecord> al = new ArrayList<CongZhiRecord>();
		
		long now = System.currentTimeMillis();
		
		String u = url+"?day="+day+"&username="+username;
		
		String jsonStr = null;
		try{
			byte bytes[] = HttpUtils.webGet(new java.net.URL(u), null, 5000, 5000);
			
			jsonStr = new String(bytes);
			
			ArrayList<CongZhiRecord> result = (ArrayList<CongZhiRecord>)JsonUtil.objectFromJson(jsonStr,new TypeReference<ArrayList<CongZhiRecord>>(){});
			
			if(result != null){
				al = result;
			}
			
			if(logger.isInfoEnabled()){
				logger.info("[查询充值请求] [成功] [day:"+day+"] [username:"+username+"] [result:"+al.size()+"] [content:"+jsonStr+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
				for(int i = 0 ; i < al.size() ; i++){
					CongZhiRecord r = al.get(i);
					logger.info("[显示查询结果] [day:"+day+"] [username:"+username+"] [index:"+i+"/"+al.size()+"] [money:"+r.money+"] [level:"+r.level+"] [channel:"+r.channel+"] [fenqu:"+r.fenqu+"] [congzhiType:"+r.congzhiType+"] [congzhiTime:"+r.congzhiTime+"] [congzhiCardType:"+r.congzhiCardType+"] [platform:"+r.platform+"]");
				}
			}
			
		}catch(Exception e){
			logger.warn("[查询充值请求] [失败] [day:"+day+"] [username:"+username+"] [result:"+al.size()+"] [content:"+jsonStr+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		}
		
		return al;
	}
}
