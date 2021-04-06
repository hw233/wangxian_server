package com.sqage.stat.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.commonstat.manager.Impl.ChongZhiManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.OnLineUsersCountManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.UserManagerImpl;
import com.sqage.stat.language.MultiLanguageManager;
import com.sqage.stat.model.Channel;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.timer.Executable;

public class DailyGenerationManager implements Executable {

    protected static DailyGenerationManager m_self = null;
    
    UserManagerImpl userManager=UserManagerImpl.getInstance();
    PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
    OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
    ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
    ChannelManager cmanager = ChannelManager.getInstance();
    List<Channel> channelList = cmanager.getChannels();//渠道信息
    //ArrayList<String []> fenQuList = userManager.getFenQu(null);//获得现有的分区信息
    ArrayList<String []> fenQuList = userManager.getFenQuByStatus("0");
    
    String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
    
	protected static final Log logger = LogFactory.getLog(DailyGenerationManager.class);
	public static DailyGenerationManager getInstance() {
			return m_self;
		}
	  
	public void initialize() throws Exception{
		m_self = this;
		logger.info("["+DailyGenerationManager.class.getName()+"] [initialized]");
	}
	
	@Override
	public void execute(String[] args) {
		if(args.length == 0 || args[0].trim().length() == 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -1);
			genChannelStat(cal.getTime());
		} else if(args.length == 1) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(args[0]));
			genChannelStat(cal.getTime());
		} else if(args.length == 2) {
			String startdate = args[0];
			String enddate = args[1];
			Calendar cal = Calendar.getInstance();
			Date starttime = DateUtil.parseDate(startdate, "yyyy-MM-dd");
			cal.setTime(starttime);
			while(!DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd").equals(enddate)) {
				genChannelStat(cal.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			genChannelStat(cal.getTime());
		}
	}
	
	public void genChannelStat(Date statdate) {
		genFenQuDailyStat(statdate);
		genQuDaoDailyStat(statdate);
		genJiXingDailyStat(statdate);
		genMutiQuDaoDailyStat(statdate);
		
	}
	
	
	/**
	 * 各个分区日报的产生
	 */
   public void genFenQuDailyStat(Date statdate){
		String qudao=null;
		String jixing=null;
		
		for (int k = 0; k < fenQuList.size(); k++) {
			String[] dfenqu = fenQuList.get(k);
			String fenqu = dfenqu[1];
			String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
			List<String[]> stat_commonList = null;
			String key = "";
			if ("null".equals(qudao)) {
				key = _day + "内侧用户" + fenqu + jixing;
			} else {
				key = _day + qudao + fenqu + jixing;
			}
			if (today.compareTo(_day) > 0) {
				stat_commonList = userManager.getstat_common(key);
			}
			String[] stat_common = new String[17];

			if (stat_commonList != null && stat_commonList.size() > 0) {//如果目前日报表里没有这条记录，就查询原始数据产生这个记录，并存到日报表里。
			} else if(today.compareTo(_day)> 0) {
				stat_common[0] = key;
				Long registUserCount = userManager.getRegistUerCount(_day, _day, qudao, null, jixing);
				if (registUserCount != null) {
					stat_common[1] = registUserCount.toString();
				} else {
					stat_common[1] = "0";
				}

				Long reg_LoginUserCount = playGameManager.getReg_LoginUserCount(_day, _day, qudao, fenqu, jixing);// 当天注册，在分区道登陆的用户
				if (reg_LoginUserCount != null) {
					stat_common[5] = reg_LoginUserCount.toString();
				} else {
					stat_common[5] = "0";
				}

				Long youXiaoUserCount = userManager.getYouXiaoUerCount(_day, _day, qudao, fenqu, jixing);
				if (youXiaoUserCount != null) {
					stat_common[2] = youXiaoUserCount.toString();
				} else {
					stat_common[2] = "0";
				}

				Long zhuCeNOCreatPlayer = 0L;
				Long reg_LoginUserCount_all = playGameManager.getReg_LoginUserCount(_day, _day, qudao, null, jixing);// 注册并登陆的用户，不区分区
				if (registUserCount != null && reg_LoginUserCount_all != null) {
					zhuCeNOCreatPlayer = registUserCount - reg_LoginUserCount_all;
					stat_common[3] = zhuCeNOCreatPlayer.toString();
				} else {
					stat_common[3] = "0";
				}

				Long enterGameUserCount = playGameManager.getEnterGameUserCount(_day, _day, qudao, fenqu, jixing);
				if (enterGameUserCount != null) {
					stat_common[4] = enterGameUserCount.toString();
				} else {
					stat_common[4] = "0";
				}

				Long avgOnlineUserCount = onLineUsersCountManager.getAvgOnlineUserCount(_day, _day, qudao, fenqu, jixing);
				if (avgOnlineUserCount != null) {
					stat_common[6] = avgOnlineUserCount.toString();
				} else {
					stat_common[6] = "0";
				}

				Long maxOnlineUserCount = onLineUsersCountManager.getMaxOnlineUserCount(_day, _day, qudao, fenqu, jixing);
				if (maxOnlineUserCount != null) {
					stat_common[7] = maxOnlineUserCount.toString();
				} else {
					stat_common[7] = "0";
				}

				Long avgOnLineTime = playGameManager.getAvgOnLineTime(_day, _day, qudao, fenqu, jixing);
				if (avgOnLineTime != null) {
					Long fenzhong = avgOnLineTime / 60000;
					stat_common[8] = fenzhong.toString();
				} else {
					stat_common[8] = "0";
				}

				Long chongZhiUserCount = chongZhiManager.getChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
				if (chongZhiUserCount != null) {
					stat_common[9] = chongZhiUserCount.toString();
				} else {
					stat_common[9] = "0";
				}
				Long unAheadChongZhiUserCount = chongZhiManager.getUnAheadChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
				if (unAheadChongZhiUserCount != null) {
					stat_common[10] = unAheadChongZhiUserCount.toString();
				} else {
					stat_common[10] = "0";
				}

				Long youXiaoUerAVGOnLineTime = userManager.getYouXiaoUerAVGOnLineTime(_day, _day, qudao, fenqu, jixing);
				// Long
				// chongzhi_LoginUserCount=chongZhiManager.getChongZhi_LoginUserCount(_day,_day,qudao,fenqu,null);
				if (youXiaoUerAVGOnLineTime != null) {
					Long fenzheng = youXiaoUerAVGOnLineTime / 60000;
					stat_common[11] = fenzheng.toString();
				} else {
					stat_common[11] = "0";
				}

				Long chongZhiCount = chongZhiManager.getChongZhiCount(_day, _day, qudao, fenqu, jixing);// 分区充值金额
				if (chongZhiCount != null) {
					stat_common[12] = chongZhiCount.toString();
				} else {
					stat_common[12] = "0";
				}

				Long chongZhiCount_all = chongZhiManager.getChongZhiCount(_day, _day, qudao, null, jixing);// 全部充值金额，不分区
				if (reg_LoginUserCount != null && chongZhiCount_all != null && reg_LoginUserCount != 0) {
					Long zhuceAEPU = chongZhiCount_all / reg_LoginUserCount;
					stat_common[13] = zhuceAEPU.toString();
				} else {
					stat_common[13] = "0";
				}

				if (chongZhiCount != null && chongZhiUserCount != null && chongZhiUserCount != 0) {
					Long fufeiAEPU = chongZhiCount / chongZhiUserCount;
					stat_common[14] = fufeiAEPU.toString();
				} else {
					stat_common[14] = "0";
				}

				if (enterGameUserCount != null && enterGameUserCount != 0) {// 付费渗透率=  付费人数/独立进入
					Long shenTouLv = (chongZhiUserCount*1000) / enterGameUserCount;
					stat_common[15] = shenTouLv.toString();
				} else {
					stat_common[15] = "0";
				}

				 Long enterGameUserCount_PAICHONG=playGameManager.getEnterGameUserCount_paichong(_day,_day,qudao,fenqu,jixing);
				  if(enterGameUserCount_PAICHONG!=null){
				  stat_common[16]=enterGameUserCount_PAICHONG.toString();
				}else{ stat_common[16]="0";}
				  
				
			userManager.addstat_common(_day, stat_common);//存入数据库
			}
		}
	   
	}
   /**
    * 各个渠道日报的产生
    */
   public void genQuDaoDailyStat(Date statdate){
	   String fenqu=null;
	   String jixing=null;
		for (int k = 0; k < channelList.size(); k++) {
			Channel dqudao = channelList.get(k);
			String qudao = dqudao.getKey();
			String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
			List<String[]> stat_commonList = null;
			String key = "";
			if ("null".equals(qudao)) {
				key = _day + "内侧用户" + fenqu + jixing;
			} else {
				key = _day + qudao + fenqu + jixing;
			}
			if (today.compareTo(_day) > 0) {
				stat_commonList = userManager.getstat_common(key);
			}
			String[] stat_common = new String[17];
			if (stat_commonList != null && stat_commonList.size() > 0) {
			} else  if(today.compareTo(_day)> 0) {
				stat_common[0] = key;
				Long registUserCount = userManager.getRegistUerCount(_day, _day, qudao, null, jixing);
				if (registUserCount != null) {
					stat_common[1] = registUserCount.toString();
				} else {
					stat_common[1] = "0";
				}

				Long reg_LoginUserCount = playGameManager.getReg_LoginUserCount(_day, _day, qudao, fenqu, jixing);// 当天注册，在分区道登陆的用户
				if (reg_LoginUserCount != null) {
					stat_common[5] = reg_LoginUserCount.toString();
				} else {
					stat_common[5] = "0";
				}

				Long youXiaoUserCount = userManager.getYouXiaoUerCount(_day, _day, qudao, fenqu, jixing);
				if (youXiaoUserCount != null) {
					stat_common[2] = youXiaoUserCount.toString();
				} else {
					stat_common[2] = "0";
				}

				Long zhuCeNOCreatPlayer = 0L;
				Long reg_LoginUserCount_all = playGameManager.getReg_LoginUserCount(_day, _day, qudao, null, jixing);// 注册并登陆的用户，不区分区
				if (registUserCount != null && reg_LoginUserCount_all != null) {
					zhuCeNOCreatPlayer = registUserCount - reg_LoginUserCount_all;
					stat_common[3] = zhuCeNOCreatPlayer.toString();
				} else {
					stat_common[3] = "0";
				}

				Long enterGameUserCount = playGameManager.getEnterGameUserCount(_day, _day, qudao, fenqu, jixing);
				if (enterGameUserCount != null) {
					stat_common[4] = enterGameUserCount.toString();
				} else {
					stat_common[4] = "0";
				}

				Long avgOnlineUserCount = onLineUsersCountManager.getAvgOnlineUserCount(_day, _day, qudao, fenqu, jixing);
				if (avgOnlineUserCount != null) {
					stat_common[6] = avgOnlineUserCount.toString();
				} else {
					stat_common[6] = "0";
				}

				Long maxOnlineUserCount = onLineUsersCountManager.getMaxOnlineUserCount(_day, _day, qudao, fenqu, jixing);
				if (maxOnlineUserCount != null) {
					stat_common[7] = maxOnlineUserCount.toString();
				} else {
					stat_common[7] = "0";
				}

				Long avgOnLineTime = playGameManager.getAvgOnLineTime(_day, _day, qudao, fenqu, jixing);
				if (avgOnLineTime != null) {
					Long fenzhong = avgOnLineTime / 60000;
					stat_common[8] = fenzhong.toString();
				} else {
					stat_common[8] = "0";
				}

				Long chongZhiUserCount = chongZhiManager.getChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
				if (chongZhiUserCount != null) {
					stat_common[9] = chongZhiUserCount.toString();
				} else {
					stat_common[9] = "0";
				}
				Long unAheadChongZhiUserCount = chongZhiManager.getUnAheadChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
				if (unAheadChongZhiUserCount != null) {
					stat_common[10] = unAheadChongZhiUserCount.toString();
				} else {
					stat_common[10] = "0";
				}

				Long youXiaoUerAVGOnLineTime = userManager.getYouXiaoUerAVGOnLineTime(_day, _day, qudao, fenqu, jixing);
				if (youXiaoUerAVGOnLineTime != null) {
					Long fenzheng = youXiaoUerAVGOnLineTime / 60000;
					stat_common[11] = fenzheng.toString();
				} else {
					stat_common[11] = "0";
				}

				Long chongZhiCount = chongZhiManager.getChongZhiCount(_day, _day, qudao, fenqu, jixing);// 分区充值金额
				if (chongZhiCount != null) {
					stat_common[12] = chongZhiCount.toString();
				} else {
					stat_common[12] = "0";
				}

				Long chongZhiCount_all = chongZhiManager.getChongZhiCount(_day, _day, qudao, null, jixing);// 全部充值金额，不分区
				if (reg_LoginUserCount != null && chongZhiCount_all != null && reg_LoginUserCount != 0) {
					Long zhuceAEPU = chongZhiCount_all / reg_LoginUserCount;
					stat_common[13] = zhuceAEPU.toString();
				} else {
					stat_common[13] = "0";
				}

				if (chongZhiCount != null && chongZhiUserCount != null && chongZhiUserCount != 0) {
					Long fufeiAEPU = chongZhiCount / chongZhiUserCount;
					stat_common[14] = fufeiAEPU.toString();
				} else {
					stat_common[14] = "0";
				}
				
				if (enterGameUserCount != null && enterGameUserCount != 0) {// 付费渗透率=  付费人数/独立进入
						Long shenTouLv = chongZhiUserCount / enterGameUserCount;
						stat_common[15] = shenTouLv.toString();
					} else {
						stat_common[15] = "0";
					}
				  
				    Long enterGameUserCount_PAICHONG=playGameManager.getEnterGameUserCount_paichong(_day,_day,qudao,fenqu,jixing);
				if(enterGameUserCount_PAICHONG!=null){
				  stat_common[16]=enterGameUserCount_PAICHONG.toString();
				}else{ stat_common[16]="0";}
				  
			  userManager.addstat_common(_day, stat_common);//存入数据库
			}
		}
	}
   
   public void genMutiQuDaoDailyStat(Date statdate){
	   String fenqu=null;
	   String jixing=null;
	   String[] MultiQuDaos={"安拓1","N多","P9P9","weiyun_MIESHI","爱贝","爱皮","淡鼎","点金","金立","琵琶网","苹果园","三星","万普","野火","易蛙","易蛙ios01","51","360","553sdk用户","anzhi","dcn用户","duoku用户","feiliu用户","gfan_MIESHI用户","jiyou","LEXUN用户","PPZHUSHOU","SHENGDA","smtmobi用户","tongbu","UC用户","xiaomiyx_MIESHI用户"};
		for (int k = 0; k <MultiQuDaos.length; k++) {
			String qudao = MultiLanguageManager.translateMap.get(MultiQuDaos[k]);
			String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
			List<String[]> stat_commonList = null;
			String key = "";
			if (qudao!=null) {
				key = _day + MultiQuDaos[k] + fenqu + jixing;
			}
			if (today.compareTo(_day) > 0) {
				stat_commonList = userManager.getstat_common(key);
			}
			String[] stat_common = new String[17];
			if (stat_commonList != null && stat_commonList.size() > 0) {
			} else  if(today.compareTo(_day)> 0) {
				stat_common[0] = key;
				Long registUserCount = userManager.getRegistUerCount(_day, _day, qudao, null, jixing);
				if (registUserCount != null) {
					stat_common[1] = registUserCount.toString();
				} else {
					stat_common[1] = "0";
				}

				Long reg_LoginUserCount = playGameManager.getReg_LoginUserCount(_day, _day, qudao, fenqu, jixing);// 当天注册，在分区道登陆的用户
				if (reg_LoginUserCount != null) {
					stat_common[5] = reg_LoginUserCount.toString();
				} else {
					stat_common[5] = "0";
				}

				Long youXiaoUserCount = userManager.getYouXiaoUerCount(_day, _day, qudao, fenqu, jixing);
				if (youXiaoUserCount != null) {
					stat_common[2] = youXiaoUserCount.toString();
				} else {
					stat_common[2] = "0";
				}

				Long zhuCeNOCreatPlayer = 0L;
				Long reg_LoginUserCount_all = playGameManager.getReg_LoginUserCount(_day, _day, qudao, null, jixing);// 注册并登陆的用户，不区分区
				if (registUserCount != null && reg_LoginUserCount_all != null) {
					zhuCeNOCreatPlayer = registUserCount - reg_LoginUserCount_all;
					stat_common[3] = zhuCeNOCreatPlayer.toString();
				} else {
					stat_common[3] = "0";
				}

				Long enterGameUserCount = playGameManager.getEnterGameUserCount(_day, _day, qudao, fenqu, jixing);
				if (enterGameUserCount != null) {
					stat_common[4] = enterGameUserCount.toString();
				} else {
					stat_common[4] = "0";
				}

				Long avgOnlineUserCount = onLineUsersCountManager.getAvgOnlineUserCount(_day, _day, qudao, fenqu, jixing);
				if (avgOnlineUserCount != null) {
					stat_common[6] = avgOnlineUserCount.toString();
				} else {
					stat_common[6] = "0";
				}

				Long maxOnlineUserCount = onLineUsersCountManager.getMaxOnlineUserCount(_day, _day, qudao, fenqu, jixing);
				if (maxOnlineUserCount != null) {
					stat_common[7] = maxOnlineUserCount.toString();
				} else {
					stat_common[7] = "0";
				}

				Long avgOnLineTime = playGameManager.getAvgOnLineTime(_day, _day, qudao, fenqu, jixing);
				if (avgOnLineTime != null) {
					Long fenzhong = avgOnLineTime / 60000;
					stat_common[8] = fenzhong.toString();
				} else {
					stat_common[8] = "0";
				}

				Long chongZhiUserCount = chongZhiManager.getChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
				if (chongZhiUserCount != null) {
					stat_common[9] = chongZhiUserCount.toString();
				} else {
					stat_common[9] = "0";
				}
				Long unAheadChongZhiUserCount = chongZhiManager.getUnAheadChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
				if (unAheadChongZhiUserCount != null) {
					stat_common[10] = unAheadChongZhiUserCount.toString();
				} else {
					stat_common[10] = "0";
				}

				Long youXiaoUerAVGOnLineTime = userManager.getYouXiaoUerAVGOnLineTime(_day, _day, qudao, fenqu, jixing);
				if (youXiaoUerAVGOnLineTime != null) {
					Long fenzheng = youXiaoUerAVGOnLineTime / 60000;
					stat_common[11] = fenzheng.toString();
				} else {
					stat_common[11] = "0";
				}

				Long chongZhiCount = chongZhiManager.getChongZhiCount(_day, _day, qudao, fenqu, jixing);// 分区充值金额
				if (chongZhiCount != null) {
					stat_common[12] = chongZhiCount.toString();
				} else {
					stat_common[12] = "0";
				}

				Long chongZhiCount_all = chongZhiManager.getChongZhiCount(_day, _day, qudao, null, jixing);// 全部充值金额，不分区
				if (reg_LoginUserCount != null && chongZhiCount_all != null && reg_LoginUserCount != 0) {
					Long zhuceAEPU = chongZhiCount_all / reg_LoginUserCount;
					stat_common[13] = zhuceAEPU.toString();
				} else {
					stat_common[13] = "0";
				}

				if (chongZhiCount != null && chongZhiUserCount != null && chongZhiUserCount != 0) {
					Long fufeiAEPU = chongZhiCount / chongZhiUserCount;
					stat_common[14] = fufeiAEPU.toString();
				} else {
					stat_common[14] = "0";
				}
				
				if (enterGameUserCount != null && enterGameUserCount != 0) {// 付费渗透率=  付费人数/独立进入
						Long shenTouLv = chongZhiUserCount / enterGameUserCount;
						stat_common[15] = shenTouLv.toString();
					} else {
						stat_common[15] = "0";
					}
				  
				    Long enterGameUserCount_PAICHONG=playGameManager.getEnterGameUserCount_paichong(_day,_day,qudao,fenqu,jixing);
				if(enterGameUserCount_PAICHONG!=null){
				  stat_common[16]=enterGameUserCount_PAICHONG.toString();
				}else{ stat_common[16]="0";}
				  
			  userManager.addstat_common(_day, stat_common);//存入数据库
			}
		}
	}
   /**
    * 各个机型日报的产生
    */
   public void genJiXingDailyStat(Date statdate){

	   String fenqu=null;
	   String qudao=null;
	   
	   String [] jiXings={"Android","IOS"};
		for (int k = 0; k < jiXings.length+1; k++) {
			String jixing=null;
			if(k < jiXings.length){//这种情况处理的是所有条件都选择“全部”的情况。
			 jixing = jiXings[k];
			}
			
			String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
			List<String[]> stat_commonList = null;
			String key = "";
			if ("null".equals(qudao)) {
				key = _day + "内侧用户" + fenqu + jixing;
			} else {
				key = _day + qudao + fenqu + jixing;
			}
			if (today.compareTo(_day) > 0) {
				stat_commonList = userManager.getstat_common(key);
			}
			String[] stat_common = new String[17];
			if (stat_commonList != null && stat_commonList.size() > 0) {
			} else  if(today.compareTo(_day)> 0){
				stat_common[0] = key;
				Long registUserCount = userManager.getRegistUerCount(_day, _day, qudao, null, jixing);
				if (registUserCount != null) {
					stat_common[1] = registUserCount.toString();
				} else {
					stat_common[1] = "0";
				}

				Long reg_LoginUserCount = playGameManager.getReg_LoginUserCount(_day, _day, qudao, fenqu, jixing);// 当天注册，在分区道登陆的用户
				if (reg_LoginUserCount != null) {
					stat_common[5] = reg_LoginUserCount.toString();
				} else {
					stat_common[5] = "0";
				}

				Long youXiaoUserCount = userManager.getYouXiaoUerCount(_day, _day, qudao, fenqu, jixing);
				if (youXiaoUserCount != null) {
					stat_common[2] = youXiaoUserCount.toString();
				} else {
					stat_common[2] = "0";
				}

				Long zhuCeNOCreatPlayer = 0L;
				Long reg_LoginUserCount_all = playGameManager.getReg_LoginUserCount(_day, _day, qudao, null, jixing);// 注册并登陆的用户，不区分区
				if (registUserCount != null && reg_LoginUserCount_all != null) {
					zhuCeNOCreatPlayer = registUserCount - reg_LoginUserCount_all;
					stat_common[3] = zhuCeNOCreatPlayer.toString();
				} else {
					stat_common[3] = "0";
				}

				Long enterGameUserCount = playGameManager.getEnterGameUserCount(_day, _day, qudao, fenqu, jixing);
				if (enterGameUserCount != null) {
					stat_common[4] = enterGameUserCount.toString();
				} else {
					stat_common[4] = "0";
				}

				Long avgOnlineUserCount = onLineUsersCountManager.getAvgOnlineUserCount(_day, _day, qudao, fenqu, jixing);
				if (avgOnlineUserCount != null) {
					stat_common[6] = avgOnlineUserCount.toString();
				} else {
					stat_common[6] = "0";
				}

				Long maxOnlineUserCount = onLineUsersCountManager.getMaxOnlineUserCount(_day, _day, qudao, fenqu, jixing);
				if (maxOnlineUserCount != null) {
					stat_common[7] = maxOnlineUserCount.toString();
				} else {
					stat_common[7] = "0";
				}

				Long avgOnLineTime = playGameManager.getAvgOnLineTime(_day, _day, qudao, fenqu, jixing);
				if (avgOnLineTime != null) {
					Long fenzhong = avgOnLineTime / 60000;
					stat_common[8] = fenzhong.toString();
				} else {
					stat_common[8] = "0";
				}

				Long chongZhiUserCount = chongZhiManager.getChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
				if (chongZhiUserCount != null) {
					stat_common[9] = chongZhiUserCount.toString();
				} else {
					stat_common[9] = "0";
				}
				Long unAheadChongZhiUserCount = chongZhiManager.getUnAheadChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
				if (unAheadChongZhiUserCount != null) {
					stat_common[10] = unAheadChongZhiUserCount.toString();
				} else {
					stat_common[10] = "0";
				}

				Long youXiaoUerAVGOnLineTime = userManager.getYouXiaoUerAVGOnLineTime(_day, _day, qudao, fenqu, jixing);
				if (youXiaoUerAVGOnLineTime != null) {
					Long fenzheng = youXiaoUerAVGOnLineTime / 60000;
					stat_common[11] = fenzheng.toString();
				} else {
					stat_common[11] = "0";
				}

				Long chongZhiCount = chongZhiManager.getChongZhiCount(_day, _day, qudao, fenqu, jixing);// 分区充值金额
				if (chongZhiCount != null) {
					stat_common[12] = chongZhiCount.toString();
				} else {
					stat_common[12] = "0";
				}

				Long chongZhiCount_all = chongZhiManager.getChongZhiCount(_day, _day, qudao, null, jixing);// 全部充值金额，不分区
				if (reg_LoginUserCount != null && chongZhiCount_all != null && reg_LoginUserCount != 0) {
					Long zhuceAEPU = chongZhiCount_all / reg_LoginUserCount;
					stat_common[13] = zhuceAEPU.toString();
				} else {
					stat_common[13] = "0";
				}

				if (chongZhiCount != null && chongZhiUserCount != null && chongZhiUserCount != 0) {
					Long fufeiAEPU = chongZhiCount / chongZhiUserCount;
					stat_common[14] = fufeiAEPU.toString();
				} else {
					stat_common[14] = "0";
				}
				
				if (enterGameUserCount != null && enterGameUserCount != 0) {// 付费渗透率=  付费人数/独立进入
						Long shenTouLv = chongZhiUserCount / enterGameUserCount;
						stat_common[15] = shenTouLv.toString();
					} else {
						stat_common[15] = "0";
					}
				  
				  
			    Long enterGameUserCount_PAICHONG=playGameManager.getEnterGameUserCount_paichong(_day,_day,qudao,fenqu,jixing);
				if(enterGameUserCount_PAICHONG!=null){
				  stat_common[16]=enterGameUserCount_PAICHONG.toString();
				}else{ stat_common[16]="0";}
				
				
				userManager.addstat_common(_day, stat_common);//存入数据库
			}
		}
   }

}
