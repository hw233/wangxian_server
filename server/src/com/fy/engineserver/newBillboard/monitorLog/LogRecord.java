package com.fy.engineserver.newBillboard.monitorLog;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardsManager;


//活动日志记录
public class LogRecord {

	//没有过期 true
	public boolean effected = true;
	
	//每个类不同
	public String[] dateString = {};
	public String[] platform = {};
	
	
	//根据上边得到
	public long[] times = {};
	
	//true 生效  false 实效
	public boolean[] state = {};
	
	public boolean isMatchTime(){
		return true;
//		return effected;
	}
	
	public void 打印日志(Billboard billboard){
		long nowTime = System.currentTimeMillis();
//		for(int i= 0;i<state.length;i++){
//			if(state[i]){
//				if(nowTime >= times[i]){
//					state[i] = false;
//					//打印
//					打印(billboard,platform[i],dateString[i]);
//				}
//			}
//		}
		打印(billboard,"","");
		BillboardsManager.logger.warn("[打印日志时间] ["+this.getClass()+"] ["+(System.currentTimeMillis() - nowTime)+"ms]");
	}
	
	public void 打印(Billboard billboard,String platForm,String dateString){
		

	}
	
	
	public void init()throws Exception{
		if(dateString.length > 0){
			effected = true;
			if(dateString.length == platform.length){
				times = new long[dateString.length];
				state = new boolean[dateString.length];
				long nowTime = System.currentTimeMillis();
				int j = 0;
				for(int i= 0;i<dateString.length;i++){
					Date date = LogRecordManager.sdf.parse(dateString[i]);
					long time = date.getTime();
					times[i] = time;
					if(nowTime > time){
						state[i] = false;
						++j;
						BillboardsManager.logger.error("[查看排行榜活动失效] ["+dateString[i]+"] ["+platform[i]+"]");
					}else{
						state[i] = true;
						BillboardsManager.logger.error("[查看排行榜活动生效] ["+dateString[i]+"] ["+platform[i]+"]");
					}
				}
				if(j == dateString.length){
					effected = false;
					BillboardsManager.logger.error("[查看排行榜活动完全失效] ["+this.getClass()+"]");
				}
			}else{
				throw new RuntimeException(this.getClass()+"初始化错误，给定时间跟平台不符合");
			}
		}else{
			BillboardsManager.logger.error("[查看排行榜活动配置错误] [没有时间] ["+this.getClass()+"]");
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		String dateSt = "2012-06-14 21:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date date = sdf.parse(dateSt);
		System.out.println(date.getTime());
		
		System.out.println(System.currentTimeMillis());
		
	}
	
}
