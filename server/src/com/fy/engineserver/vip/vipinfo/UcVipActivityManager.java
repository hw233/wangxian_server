package com.fy.engineserver.vip.vipinfo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;

//注意 这里需要是异步操作
public class UcVipActivityManager {
	
	public DiskCache disk = ActivityManager.getInstance().disk;
	public String key_prefix = "ucvip_";
	
	protected static UcVipActivityManager self;
	public boolean isRunning = false;
	private Thread sendMailThread = null;
	
	private final Queue<MailInfo> infoQueue = new ConcurrentLinkedQueue<MailInfo>();

	public static UcVipActivityManager getInstance() {
		if (self == null) {
			synchronized (UcVipActivityManager.class) {
				if (self == null)
					self = new UcVipActivityManager();
			}
		}
		return self;
	}
	
	private UcVipActivityManager()
	{
		init();
	}
	
	private void init()
	{
		isRunning = true;
//		sendMailThread = new Thread(new MailSender(),"UCVIP-SENDMAIL-THREAD");
		//启动一个线程，无限循环取出数据 然后走逻辑
//		sendMailThread.start();
	}
	
	class MailInfo
	{
		public Article article;
		public ArticleEntity articleEntity;
		public String[] infos;
	}
	
	class MailSender implements Runnable
	{
		@Override
		public void run() {
			while(isRunning)
			{
				try
				{
					Thread.sleep(500);
					MailInfo mailInfo = null;
					while((mailInfo = infoQueue.poll()) != null)
					{
						doSendMail4UcVip(mailInfo.infos,mailInfo.article,mailInfo.articleEntity);
					}
				}
				catch(Exception e)
				{
					ActivitySubSystem.logger.debug("["+Thread.currentThread().getName()+"] [发送ucvip礼包] [失败] [出现异常]",e);
				}
			}
			
			
		}
	}
	

	
	
	//发邮件
	public  void sendUcVipMail4EnterServer(String articleName,Player player,String username) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		String[] infos = new String[6];
		infos[0] = username;
		infos[1] = player.getId()+"";
		infos[2] = player.getName();
		infos[3] = GameConstants.getInstance().getServerName();
		infos[4]  =  player.getVipLevel()+"";
		infos[5] = player.getLevel()+"";
		
		//拼装
		MailInfo mailInfo = new MailInfo();
		
		
		Article a = ArticleManager.getInstance().getArticle(articleName);
		if(a == null)
		{
			ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [失败] [无此物品]  ["+articleName+"] ["+player.getId()+"] ["+player.getName()+"] ["+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return;
		}
		ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
		if(ae == null)
		{
			ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [失败] [创建道具实体失败] ["+articleName+"] ["+a.getColorType()+"] ["+player.getId()+"] ["+player.getName()+"] ["+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return;
		}
		
		
		mailInfo.article = a;
		mailInfo.articleEntity =ae;
		mailInfo.infos = infos;
		//放入队列
		infoQueue.add(mailInfo);
		ActivitySubSystem.logger.debug("[UCVIP进入游戏服礼物赠送] [添加了礼包发送信息] ["+articleName+"] ["+player.getUsername()+"] ["+player.getName()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
	}

	/**
	 * @param articleName
	 * @param player
	 */
	public void doSendMail4UcVip(String[] infos, Article a,ArticleEntity ae) {
		
		long startTime = System.currentTimeMillis();
		try
		{
			String username = infos[0];
			String playerId = infos[1]+"";
			if(canSendMail(username,playerId))
			{
				//先插入记录，再发礼包
				BossClientService bossClientService = BossClientService.getInstance();
				/**
				 * 			String[] infos =  req.getInfos();

				String username = infos[0];	
				String playerId = infos[1];
				String playerName = infos[2];
				String serverName = infos[3];
				String playerLevel = infos[4];
				String vipLevel = infos[5];

				 */
				int result = bossClientService.createUcVipRecord(infos);
				if(result != 0)
				{
					ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [失败] [创建发送记录失败] ["+a.getName()+"] ["+playerId+"] ["+infos[2]+"] ["+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
					return;
				}
				disk.put(key_prefix+playerId, System.currentTimeMillis());
				
				MailManager.getInstance().sendMail(Long.parseLong(playerId), new ArticleEntity[] { ae }, new int[] { 1 }, "恭迎贵宾", "尊敬的贵宾仙友，您的到来为整个飘渺寻仙曲仙界增添了光彩，赠予您珍稀[贵宾锦囊]，开启后可收获神力道具，助您仙途如虎添翼！请在附件查收。", 0, 0, 0, "在线奖励");
				Player player = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
				player.sendError("您已获得贵宾锦囊。");
				ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [成功] [ok] ["+a.getName()+"] ["+playerId+"] ["+infos[2]+"] ["+username+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
			}
			else
			{
				ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [失败] [已经发送过这个礼包或者用户不是ucvip] ["+a.getName()+"] ["+playerId+"] ["+infos[2]+"] ["+username+"] [送礼包的时间:"+ disk.get(key_prefix+infos[1])+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
			}
		
		}
		catch(Exception e)
		{
			ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [失败] [出现异常] ["+a.getName()+"] ["+infos[1]+"] ["+infos[2]+"] ["+infos[0]+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]",e);
		}
	}
	
	public  boolean isUC(String  username) throws Exception
	{
		Passport p = BossClientService.getInstance().getPassportByUserName(username);
		if(p.getRegisterChannel().toLowerCase().startsWith("uc"))
		{
			return true;
		}
		return false;
	}
	
	public  boolean canSendMail(String username,String playerId)
	{
		if(StringUtils.isEmpty(username))
		{
			return false;
		}
		
		if(StringUtils.isEmpty(playerId))
		{
			return false;
		}
		
		if(disk.get(key_prefix+playerId) != null)
		{
			return false;
		}
		BossClientService bossClientService = BossClientService.getInstance();
		String[] infos = new String[2];
		infos[0] = username;
		infos[1] = playerId+"";
		return bossClientService.canSendMail4UCVip(infos);
	}
	
	
	
}
