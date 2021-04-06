package com.fy.engineserver.billboard;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.dbpool.DataSourceManager;
import com.xuanzhi.tools.timer.Executable;

public class BillboardExecutable implements Executable, Runnable {

//	static Logger log=Logger.getLogger("Billboard");
public	static Logger log = LoggerFactory.getLogger("Billboard");
	
	Billboards[] billboards;
	
	boolean isAlive;
	
	public void execute(String[] arg0) {
		// TODO Auto-generated method stub
		Thread th=new Thread(this);
		th.setName(Translate.text_2308);
		if(!this.isAlive){
			this.isAlive=true;
			th.start();
		}
		if(log.isInfoEnabled()){
			log.info("[日常更新排行榜] [开始日常更新]");
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Connection con=null;
		this.billboards=BillboardManager.getInstance().billboards;
//		BillboardManager.getInstance().playerNames.clear();
//		BillboardManager.getInstance().gangNames.clear();
		try{
			if(this.billboards!=null){
				for(int i=0;i<this.billboards.length;i++){
					if(this.billboards[i]!=null){
						con=this.getConnection();
						this.billboards[i].update(con);
						try{
							con.close();
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				}
				BillboardManager.self.maker.outputBillboard(this.billboards);
			}
			if(log.isInfoEnabled()){
//				log.info("[日常更新排行榜] [成功] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
				if(log.isInfoEnabled())
					log.info("[日常更新排行榜] [成功] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
			}
		}catch(Exception e){
			e.printStackTrace();
			if(log.isInfoEnabled())
				log.info("[日常更新排行榜] [失败] [发生错误] [错误："+e+"]",e);
		}finally{
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.isAlive=false;
		}
	}
	
	private Connection getConnection() throws Exception {
		return DataSourceManager.getInstance().getConnection();
	}

}
