package com.fy.engineserver.vip.vipinfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_OPTION_SELECT_REQ;
import com.fy.engineserver.message.NEW_QUERY_WINDOW_REQ;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.vip.Option_VipInfo_Cancel;
import com.fy.engineserver.vip.Option_VipInfo_OK;
import com.fy.engineserver.vip.VipManager;
import com.fy.boss.client.BossClientService;
import com.fy.boss.transport.BossServerService;
import com.xuanzhi.boss.game.GameConstants;

public class Record4OrderManager {
	
	public static boolean isRun = true;
	public static boolean isRunning = false;
	public static long TimeOutValue = 1000l * 50;
	public static Map<String,VipInfo4Order> menuWindowCache = new ConcurrentHashMap<String, VipInfo4Order>();
	public static Record4OrderManager self = new Record4OrderManager();


	public static Record4OrderManager getInstance()
	{
		if(self == null)
		{
			self = new Record4OrderManager();
		}

		return self;
	}


	public void gatherInfo4Order(Player player)
	{
		long startTime = System.currentTimeMillis();
		try
		{
			//调用bossClientService的判断是否弹出窗口的方法
			BossClientService bossClientService = BossClientService.getInstance();
			//获取player上的信息 用以判断是否弹出窗口给vip用户
			String[] infos = new String[7];
			infos[0] = player.getVipLevel()+"";
			infos[1] = player.getUsername();
			infos[2] = PlatformManager.getInstance().getPlatform().getPlatformName();
			infos[3] = player.getName();
			infos[4] = player.getLevel()+"";
			infos[5] = CountryManager.getInstance().getCountryByCountryId(player.getCountry()).getName();
			infos[6] = GameConstants.getInstance().getServerName();

			MenuWindow4Order mw = Record4OrderManager.createMenuWindow((int)Record4OrderManager.TimeOutValue);
			//创建一个确认按钮和取消按钮
			Option_VipInfo_OK optionOk = new Option_VipInfo_OK();
			optionOk.setOType(Option.OPTION_TYPE_SERVER_FUNCTION);
			Option_VipInfo_Cancel optionCancel = new Option_VipInfo_Cancel();
			optionCancel.setOType(Option.OPTION_TYPE_SERVER_FUNCTION);
			optionOk.setText(com.fy.engineserver.datasource.language.Translate.确定);
			optionCancel.setText(com.fy.engineserver.datasource.language.Translate.取消);

			mw.setOptions(new Option[]{optionOk,optionCancel});
			VipInfo4Order vipInfo = new VipInfo4Order();
			vipInfo.menuWindow = mw;
			vipInfo.infos = infos;
			menuWindowCache.put(mw.getWinId()+"", vipInfo);
			//创建标题
			String title = "录入信息";
			//创建desc
			String desc = "亲爱的玩家您好：恭喜您成为飘渺寻仙曲贵宾用户，我们诚邀您完善信息，只需要动动手指就能获得专属客服经理哟~快快来完善资料吧，感谢您一直以来对飘渺寻仙曲的支持与热爱！";
			String[] inputTitles = new String[]{"您的姓名:","QQ号码:","联系方式:"};
			byte[] inputTypes = new byte[]{1,1,1};
			byte[] maxLength = new byte[]{20,20,20};
			String[] defaultContent = new String[]{""};
			byte[] png = new byte[]{0};


			NEW_QUERY_WINDOW_REQ req = new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getWinId(), title,desc,inputTitles,inputTypes,maxLength,defaultContent,png,mw.getOptions());
			//发给客户端弹出窗口信息
			player.addMessageToRightBag(req, "");

			if(VipManager.logger.isInfoEnabled())
			{
				VipManager.logger.info("[弹出窗口获取联系信息] [向客户端发起弹窗请求] [成功] ["+player.getLogString()+"] [menuWindowId:"+mw.getWinId()+"] [country:"+infos[5]+"] [servername:"+infos[6]+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}

		}
		catch(Exception e)
		{
			VipManager.logger.error("[弹出窗口获取联系信息] [错误] [出现异常] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}

	public static MenuWindow4Order createMenuWindow (int time) {
		MenuWindow4Order mw = new MenuWindow4Order();
		mw.setWinId(Integer.parseInt((System.currentTimeMillis()+"").substring(4)));
		mw.setDestoryTime(System.currentTimeMillis() + time);
		return mw;
	}

	private void checkWindowCache()
	{
		for(String key : menuWindowCache.keySet())
		{
			VipInfo4Order vipInfo = menuWindowCache.get(key);
			if(vipInfo != null)
			{
				String[] infos = vipInfo.infos;
				MenuWindow4Order menuWindow = vipInfo.menuWindow;
				if(menuWindow.getDestoryTime()  <= System.currentTimeMillis())
				{
					menuWindowCache.remove(key);
					if(VipManager.logger.isInfoEnabled())
					{
						VipManager.logger.info("[弹出窗口获取联系信息] [清理过期窗口缓存] [成功] [menuWindowId:"+menuWindow.getWinId()+"] [viplevel:"+(infos[0])+"] [username:"+(infos[1])+"] [name:"+infos[3]+"] [level:"+infos[4]+"] [country:"+infos[5]+"] [servername:"+infos[6]+"]");
					}
				}
			}
		}
	}
	
	private int validateInput(String[] infos)
	{
		int i = 0;
		for(String str : infos)
		{
			if(str.trim().length() == 0 )
			{
				return i;
			}
			i++;
		}
		
		return 100;
	}
	
	private void sendHint(String message,Player player,String buttonName)
	{
		if(player == null)
		{
			VipManager.logger.error("[发送提示信息] [失败] [角色对象为空] ["+message.getClass().getName()+"]");
			return;
		}
		
		MenuWindow4Order mw = createMenuWindow((int)Record4OrderManager.TimeOutValue);
		//创建一个确认按钮和取消按钮
		Option optionOk = new Option();
		optionOk.setOType(Option.OPTION_TYPE_SERVER_FUNCTION);
		
		if(buttonName == null || buttonName.trim().length() == 0)
		{
			buttonName = "返回";
		}
		
		optionOk.setText(buttonName);

		mw.setOptions(new Option[]{optionOk});
		String title = "";
		//创建desc
		String desc = message;
		String[] inputTitles = new String[]{""};
		byte[] inputTypes = new byte[0];
		byte[] maxLength = new byte[0];
		String[] defaultContent = new String[]{""};
		byte[] png = new byte[]{0};


		NEW_QUERY_WINDOW_REQ req = new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getWinId(), title,desc,inputTitles,inputTypes,maxLength,defaultContent,png,mw.getOptions());
		//发给客户端弹出窗口信息
		player.addMessageToRightBag(req, "");
		
		
		return;
	}
	
	//
	public void handle_NEW_OPTION_SELECT_REQ(NEW_OPTION_SELECT_REQ req,Player player) {
		long startTime = System.currentTimeMillis();
		try
		{

			VipInfo4Order vipInfo = menuWindowCache.get(req.getWId()+"");
		
			
			//根据请求携带的winid查找到对应的窗口 同时检查过期的窗口并清除
			if(vipInfo != null)
			{
				if(req.getIndex() == 0) //按下确认按钮
				{
					String[] infos = vipInfo.infos;
					//validate输入
					int isValid = validateInput(req.getInputText());
						//发送提示信息
					if(isValid != 100)
					{
						String message = "";
						message = "提交信息不完整";
							
						sendHint(message,player,null);
						return;
					}
					else
					{
						String[] vipRecords = new String[]{
								req.getInputText()[0],
								req.getInputText()[1],
								req.getInputText()[2],
								infos[0],
								infos[1],
								infos[2],
								infos[3],
								infos[4],
								infos[5],
								infos[6],
								"2"
						};
						
						
						int result = BossClientService.getInstance().createNewVipRecord(vipRecords);
						if(result == 0)
						{
							sendHint("信息提交成功，感谢您对游戏的支持！",player,"确定");
							
							player.getVipAgent().setLastRecordtime(System.currentTimeMillis());
							player.setVipAgent(player.getVipAgent());
							if(VipManager.logger.isInfoEnabled())
							{
								VipManager.logger.info("[弹出窗口获取联系信息] [成功] [recordTime] [menuWindowId:"+req.getWId()+"] [viplevel:"+(infos[0])+"] [username:"+(infos[1])+"] [name:"+infos[3]+"] [level:"+infos[4]+"] [country:"+infos[5]+"] [servername:"+infos[6]+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
							}
						}
						else
						{
							VipManager.logger.error("[弹出窗口获取联系信息] [失败] [menuWindowId:"+req.getWId()+"] [viplevel:"+(infos[0])+"] [username:"+(infos[1])+"] [name:"+infos[3]+"] [level:"+infos[4]+"] [country:"+infos[5]+"] [servername:"+infos[6]+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
						}
						
					}
				}
				else
				{
					String[] infos = vipInfo.infos;
					if(VipManager.logger.isInfoEnabled())
					{
						VipManager.logger.info("[弹出窗口获取联系信息] [失败] [用户点击取消按钮取消操作] [menuWindowId:"+req.getWId()+"] [viplevel:"+(infos[0])+"] [username:"+(infos[1])+"] [name:"+infos[3]+"] [level:"+infos[4]+"] [country:"+infos[5]+"] [servername:"+infos[6]+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
					}
				}
				
				menuWindowCache.remove(req.getWId());
				return;
				
			}
		}
		catch(Exception e)
		{
			VipManager.logger.error("[收集玩家信息] [错误] [出现异常]",e);
		}
		finally
		{
			checkWindowCache();
		}
	}


}


class MenuWindow4Order
{
	private int winId;

	// 窗口的标题
	private String title;

	// 窗口的描述，UUB格式的
	private String descriptionInUUB;

	// 窗口中的选项
	private Option options[] = new Option[0];

	// png图片数据
	private byte[] png = new byte[0];

	//销毁时间
	private long destoryTime;

	public void setWinId(int winId) {
		this.winId = winId;
	}

	public int getWinId() {
		return winId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescriptionInUUB(String descriptionInUUB) {
		this.descriptionInUUB = descriptionInUUB;
	}

	public String getDescriptionInUUB() {
		return descriptionInUUB;
	}

	public void setOptions(Option options[]) {
		this.options = options;
	}

	public Option[] getOptions() {
		return options;
	}

	public void setPng(byte[] png) {
		this.png = png;
	}

	public byte[] getPng() {
		return png;
	}

	public void setDestoryTime(long destoryTime) {
		this.destoryTime = destoryTime;
	}

	public long getDestoryTime() {
		return destoryTime;
	}

}




class VipInfo4Order
{
	public MenuWindow4Order menuWindow;
	public String[] infos;
}
