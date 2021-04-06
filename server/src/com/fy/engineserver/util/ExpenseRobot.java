package com.fy.engineserver.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.timer.Executable;

/**
 * 消费机器人，测试
 * 
 *
 */
public class ExpenseRobot implements Runnable,Executable {
//	protected static final Log logger = LogFactory.getLog(ExpenseRobot.class);
public	static Logger logger = LoggerFactory.getLogger(ExpenseRobot.class);
	
	private static boolean running;
	
	private long period = 50;
	
	public void execute(String args[]) {
		if(!running && args.length > 0) {
			running = true;
			Thread t = new Thread(this, "ExpenseRobot");
			t.start();
		}
	}
	
	public void run() {
		try {
			if(logger.isInfoEnabled())
				logger.info("[开始模拟]");
			PlayerManager pmanager = PlayerManager.getInstance();
			ShopManager shopManager = ShopManager.getInstance();
			Shop shop = shopManager.getShop(Translate.text_6080,null);
			Goods goods[] = shop.getGoods(null);
			Random ran = new Random();
			int expensed = 0;
			while(true) {
				try {
					long ransleep = period + ran.nextInt(50);
					Thread.sleep(ransleep);
//					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//					Player player = pmanager.getRandomPlayer();
//					int gindex = ran.nextInt(goods.length);
//					int amount = ran.nextInt(5)+1;
//					Goods good = goods[gindex];
//					boolean succ = shop.buyGoods(player, gindex, amount);
//					expensed++;
//					logger.info("[模拟消费] ["+(succ?"成功":"失败")+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+shop.getName()+"] ["+good.getArticleName()+"] [数量:"+amount+"] [expense:"+good.getYuanboPrice()*amount+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)+"ms]");
				} catch(Throwable e) {
					e.printStackTrace();
					logger.error("[模拟消费发生异常] [失败] ["+expensed+"]", e);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("[模拟消费发生异常] [失败]", e);
		}
	}
}
