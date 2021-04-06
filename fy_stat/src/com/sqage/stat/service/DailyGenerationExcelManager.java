package com.sqage.stat.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.commonstat.manager.Impl.ChongZhiManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.UserManagerImpl;
import com.sqage.stat.language.MultiLanguageManager;
import com.sqage.stat.model.Channel;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.timer.Executable;

public class DailyGenerationExcelManager implements Executable {

    protected static DailyGenerationExcelManager m_self = null;
	UserManagerImpl userManager=UserManagerImpl.getInstance();
    ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();;
    ChannelManager channelManager=ChannelManager.getInstance();
    DailyChongzhiManager dailyChongzhiManager = DailyChongzhiManager.getInstance();
    
    String dailyExcelBase=MultiLanguageManager.translateMap.get("dailyExcelBase");
    //String dailyExcelBase = "D:/sever/apache-tomcat-6.0.16.other/webapps/stat_common_mieshi/excel/daily";   //日报存放目录

    String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
    String dailyDay = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
    
	protected static final Log logger = LogFactory.getLog(DailyGenerationExcelManager.class);
	public static DailyGenerationExcelManager getInstance() {
			return m_self;
		}
	  
	public void init() throws Exception{
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
	
	 
	public void generateExcel(Date statdate) throws WriteException, IOException, BiffException
	{
		int rowsCount=0;
          File thePath = new File(dailyExcelBase);
          if (thePath.isDirectory() == false)// 如果该目录不存在，则生成新目录
          {
              thePath.mkdirs();
          }
          String xls= dailyExcelBase+"/"+dailyDay+"daily.xls";//日报文件名称
          String tempxls= dailyExcelBase+"/"+dailyDay+"dailytemp.xls";//日报文件名称
          
          File f=new File(xls);
          File tempfile = new File(tempxls);
  
          WritableWorkbook wwb=null;
          if(f.exists()){
				Workbook rwb = Workbook.getWorkbook(f);
                wwb = Workbook.createWorkbook(tempfile, rwb);
                rwb.close();
			
             }else{
               wwb = Workbook.createWorkbook(tempfile);
              }
          String shetname=MultiLanguageManager.translateMap.get("dailySheetName");
          if("国服日报".equals(shetname))
          {
        	  WritableSheet ws_chongzhi = wwb.createSheet("充值汇总", 0);
        	  try {
				dailyChongzhiManager.generationChongZhiDate(0,0,"充值汇总",ws_chongzhi,statdate);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	  
          }
          
              WritableSheet ws = wwb.createSheet(shetname, 0);
              
              dailyChongzhiManager.writeDaysTitle(ws,0,rowsCount,"日报汇总");  rowsCount+=2;
             List<String[]> commondatals=  getDayDailyStat(statdate);
		           for (int i=0;i<commondatals.size();i++) 
		           {
		        	   String [] dats=(String [])commondatals.get(i);
		        	   dailyChongzhiManager.writeWritableSheetSecTiTile(ws,0, rowsCount, dats[0].replace("null", ""));//日期
		        	   dailyChongzhiManager.writeWritableSheet(ws,1, rowsCount, dats[1]);    //注册人数
		        	   dailyChongzhiManager.writeWritableSheet(ws,2, rowsCount, dats[2]);    //有效注册人数
		        	   dailyChongzhiManager.writeWritableSheet(ws,3, rowsCount, dats[3]);    //未激活账号
		        	   dailyChongzhiManager.writeWritableSheet(ws,4, rowsCount, dats[4]);    //独立进入
		        	   dailyChongzhiManager.writeWritableSheet(ws,5, rowsCount, dats[5]);    //独立进入(当天)
		        	   dailyChongzhiManager.writeWritableSheet(ws,6, rowsCount, dats[6]);    //平均在线
		        	   dailyChongzhiManager.writeWritableSheet(ws,7, rowsCount, dats[7]);    //最高在线
		        	   dailyChongzhiManager.writeWritableSheet(ws,8, rowsCount, dats[8]);    //平均在线时长(分钟)
		        	   dailyChongzhiManager.writeWritableSheet(ws,9, rowsCount, dats[9]);    ///付费人数
		        	   dailyChongzhiManager.writeWritableSheet(ws,10, rowsCount, dats[10]);  //新付费人数
		        	   dailyChongzhiManager.writeWritableSheet(ws,11, rowsCount, dats[11]);  //有效用户平均在线时长(分钟)
		        	   dailyChongzhiManager.writeWritableSheet(ws,12, rowsCount, dats[12]);  //充值金额
		        	   dailyChongzhiManager.writeWritableSheet(ws,13, rowsCount, dats[13]);  //当天进入AEPU(不分区)

		        	   dailyChongzhiManager.writeWritableSheet(ws,14, rowsCount, String.valueOf(Integer.parseInt(dats[4])-Integer.parseInt(dats[5])));//独立进入老用户数
		        	   dailyChongzhiManager.writeWritableSheet(ws,15, rowsCount, dailyChongzhiManager.xiangChu(dats[9],dats[4])+" %");//付费渗透率
		        	   dailyChongzhiManager.writeWritableSheet(ws,16, rowsCount, dailyChongzhiManager.xiangChu(dats[5],dats[1])+" %");//激活率
		        	   dailyChongzhiManager.writeWritableSheet(ws,17, rowsCount, dailyChongzhiManager.xiangChu(dats[2],dats[5])+" %");//有效率
                       
                       rowsCount++;
		            }
		           rowsCount+=4;
		         
		           int column=0;
		           dailyChongzhiManager.writeXiFenTitle(ws,column,rowsCount,"分区充值",statdate,"分区名称");
		           
		             rowsCount+=3;
		             List<String[]> commonFenqudatals=  genFenQuDailyStat(statdate);
		             commonFenqudatals=dailyChongzhiManager.sortMONWYList(commonFenqudatals);
		             rowsCount=dailyChongzhiManager.writeSheetData(ws,column,rowsCount,commonFenqudatals);
				           rowsCount+=4;
				           
				           column=0;
				           dailyChongzhiManager.writeXiFenTitle_zhuce(ws,column,rowsCount,"分渠道注册",statdate);
				             rowsCount+=3;
				             List<String[]> commonQuDaodatals=  genQuDaoDailyStat(statdate);
				             commonQuDaodatals= dailyChongzhiManager.sortZHUCEList(commonQuDaodatals);
				             dailyChongzhiManager.writeSheetData_zhuce(ws,column,rowsCount,commonQuDaodatals);
						          

				             column=9;
				             rowsCount-=3;
				             dailyChongzhiManager.writeXiFenTitle(ws,column,rowsCount,"分渠道充值",statdate,"渠道名称");
				             rowsCount+=3;
				             commonQuDaodatals=  genQuDaoDailyStat(statdate);
				             commonQuDaodatals= dailyChongzhiManager.sortMONWYList(commonQuDaodatals);
				             rowsCount=dailyChongzhiManager.writeSheetData(ws,column,rowsCount,commonQuDaodatals);
						           rowsCount+=4;
						           
						           column=0;
						           dailyChongzhiManager.writeXiFenTitle(ws,column,rowsCount,"分机型充值",statdate,"机型");
						             rowsCount+=3;
						             List<String[]> commonJiXingdatals=  genJiXingDailyStat(statdate);
						             commonJiXingdatals=dailyChongzhiManager.sortMONWYList(commonJiXingdatals);
						             rowsCount= dailyChongzhiManager.writeSheetData(ws,column,rowsCount,commonJiXingdatals);
								           rowsCount+=4;
								          
								           wwb.write();
                                           wwb.close();
                                           
                                           f.delete();
                                           tempfile.renameTo(f);
                                           
	             }
	
	public void genChannelStat(Date statdate) {
		try {
			generateExcel(statdate);
			
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (BiffException e) {
			e.printStackTrace();
		}
	}
	
	ArrayList<String> dayList = new ArrayList<String>();
	/**
	 * 获取要统计的日期段。
	 * @param statdate
	 * @return
	 */
	public List getDayList(Date statdate)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(statdate);
		Date t = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date s = cal.getTime();
        String tdat=DateUtil.formatDate(t, "yyyy-MM-dd");
		String sdat=DateUtil.formatDate(s, "yyyy-MM-dd");
		
		if(tdat.equals(sdat)){
			
			cal.set(Calendar.DAY_OF_MONTH, 0);
			s = cal.getTime();
		}
		while(s.getTime() <= t.getTime() + 3600000){
			 String day = DateUtil.formatDate(s,"yyyy-MM-dd");
			 dayList.add(day);
			 s.setTime(s.getTime() + 24*3600*1000);
		      }
		
		return dayList;
	}
/**
 * 按日期获取日报信息
 * @param statdate
 * @return
 */
	public List<String[]> getDayDailyStat(Date statdate){
		
		String qudao=null;
		String fenqu=null;
		String jixing=null;
		List<String> dayList=getDayList(statdate);
		List<String[]> stat_common=new ArrayList<String[]>();
		for(int k = 0 ; k < dayList.size() ; k++){
			String _day = dayList.get(k);
			List<String[]> stat_commonList=null;
			String key="";
			if("null".equals(qudao)){key=_day+"内侧用户"+fenqu+jixing;}
			else{
			key=_day+qudao+fenqu+jixing;
			}
			if(today.compareTo(_day)>0){
			stat_commonList=userManager.getstat_common(key);
			}
			if(stat_commonList!=null&&stat_commonList.size()>0){
			stat_common.add(stat_commonList.get(0));
			}
	}
		return stat_common;
	}
	
	
	/**
	 * 各个分区日报的产生
	 */
   public List<String[]> genFenQuDailyStat(Date statdate){
		String qudao=null;
		String jixing=null;
		
		 ArrayList<String []> fenQuList = userManager.getFenQu(null);//获得现有的分区信息
		 String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
		 String _day2 = DateUtil.formatDate(new Date(statdate.getTime()-24*60*60*1000), "yyyy-MM-dd");
		 List<String[]> stat_commonList = null;
		 List<String[]> stat_commonList2 = null;
		
		 List<String[]> stat_common_twoDays = new ArrayList<String[]>();
		 
		for (int k = 0; k < fenQuList.size(); k++) {//前一天各个分区的日报
			String[] dfenqu = fenQuList.get(k);
			String fenqu = dfenqu[1];
			String key = _day + qudao + fenqu + jixing;
			String key2 = _day2 + qudao + fenqu + jixing;
			
			boolean flag=true;
			
			stat_commonList  = userManager.getstat_common(key);
			stat_commonList2 = userManager.getstat_common(key2);
			String[] stat_common=new String[34];
			if (stat_commonList != null && stat_commonList.size() > 0) {
					if(!"0".equals(stat_commonList.get(0)[4]))
					{
						stat_common[0]=stat_commonList.get(0)[0];
						stat_common[1]=stat_commonList.get(0)[1];
						stat_common[2]=stat_commonList.get(0)[2];
						stat_common[3]=stat_commonList.get(0)[3];
						stat_common[4]=stat_commonList.get(0)[4];
						stat_common[5]=stat_commonList.get(0)[5];
						stat_common[6]=stat_commonList.get(0)[6];
						stat_common[7]=stat_commonList.get(0)[7];
						stat_common[8]=stat_commonList.get(0)[8];
						stat_common[9]=stat_commonList.get(0)[9];
						stat_common[10]=stat_commonList.get(0)[10];
						stat_common[11]=stat_commonList.get(0)[11];
						stat_common[12]=stat_commonList.get(0)[12];
						stat_common[13]=stat_commonList.get(0)[13];
						stat_common[14]=stat_commonList.get(0)[14];
						stat_common[15]=stat_commonList.get(0)[15];
						stat_common[16]=stat_commonList.get(0)[16];
					} else{flag=false;}
			} else{flag=false;}
			if (stat_commonList2 != null && stat_commonList2.size() > 0) {
				if(!"0".equals(stat_commonList2.get(0)[4]))
				{
					stat_common[17]=stat_commonList2.get(0)[0];
					stat_common[18]=stat_commonList2.get(0)[1];
					stat_common[19]=stat_commonList2.get(0)[2];
					stat_common[20]=stat_commonList2.get(0)[3];
					stat_common[21]=stat_commonList2.get(0)[4];
					stat_common[22]=stat_commonList2.get(0)[5];
					stat_common[23]=stat_commonList2.get(0)[6];
					stat_common[24]=stat_commonList2.get(0)[7];
					stat_common[25]=stat_commonList2.get(0)[8];
					stat_common[26]=stat_commonList2.get(0)[9];
					stat_common[27]=stat_commonList2.get(0)[10];
					stat_common[28]=stat_commonList2.get(0)[11];
					stat_common[29]=stat_commonList2.get(0)[12];
					stat_common[30]=stat_commonList2.get(0)[13];
					stat_common[31]=stat_commonList2.get(0)[14];
					stat_common[32]=stat_commonList2.get(0)[15];
					stat_common[33]=stat_commonList2.get(0)[16];
				}
		   } 
			if(flag){stat_common_twoDays.add(stat_common);}
		}
	   return stat_common_twoDays;
	}
   /**
    * 各个渠道日报的产生
    */
   public List<String[]> genQuDaoDailyStat(Date statdate){
	   String fenqu=null;
	   String jixing=null;
	   
	   List<Channel> channelList = channelManager.getChannels();//渠道信息
	   String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
	   String _day2 = DateUtil.formatDate(new Date(statdate.getTime()-24*60*60*1000), "yyyy-MM-dd");
	   
	    List<String[]> stat_common_twoDays = new ArrayList<String[]>();
		List<String[]> stat_commonList = null;
		List<String[]> stat_commonList2 = null;
		
		String key  = "";
		String key2 = "";
		
		
		for (int k = 0; k < channelList.size(); k++) {
			Channel dqudao = channelList.get(k);
			String qudao = dqudao.getKey();
			if(MultiLanguageManager.translateMap.get(qudao)==null){
			 boolean flag=true;
			 String[] stat_common=new String[34];
			if ("null".equals(qudao)) {
				key  = _day  + "内侧用户" + fenqu + jixing;
				key2 = _day2 + "内侧用户" + fenqu + jixing;
			} else {
				key  = _day  + qudao + fenqu + jixing;
				key2 = _day2 + qudao + fenqu + jixing;
			}
			stat_commonList  = userManager.getstat_common(key);
			stat_commonList2 = userManager.getstat_common(key2);
			if (stat_commonList != null && stat_commonList.size() > 0) {
				if(!"0".equals(stat_commonList.get(0)[4]))
				{
					stat_common[0]=stat_commonList.get(0)[0];
					stat_common[1]=stat_commonList.get(0)[1];
					stat_common[2]=stat_commonList.get(0)[2];
					stat_common[3]=stat_commonList.get(0)[3];
					stat_common[4]=stat_commonList.get(0)[4];
					stat_common[5]=stat_commonList.get(0)[5];
					stat_common[6]=stat_commonList.get(0)[6];
					stat_common[7]=stat_commonList.get(0)[7];
					stat_common[8]=stat_commonList.get(0)[8];
					stat_common[9]=stat_commonList.get(0)[9];
					stat_common[10]=stat_commonList.get(0)[10];
					stat_common[11]=stat_commonList.get(0)[11];
					stat_common[12]=stat_commonList.get(0)[12];
					stat_common[13]=stat_commonList.get(0)[13];
					stat_common[14]=stat_commonList.get(0)[14];
					stat_common[15]=stat_commonList.get(0)[15];
					stat_common[16]=stat_commonList.get(0)[16];
				}else{ flag=false;}
			} else{ flag=false;}
			
			if (stat_commonList2 != null && stat_commonList2.size() > 0) {
				if(!"0".equals(stat_commonList2.get(0)[4]))
				{
					stat_common[17]=stat_commonList2.get(0)[0];
					stat_common[18]=stat_commonList2.get(0)[1];
					stat_common[19]=stat_commonList2.get(0)[2];
					stat_common[20]=stat_commonList2.get(0)[3];
					stat_common[21]=stat_commonList2.get(0)[4];
					stat_common[22]=stat_commonList2.get(0)[5];
					stat_common[23]=stat_commonList2.get(0)[6];
					stat_common[24]=stat_commonList2.get(0)[7];
					stat_common[25]=stat_commonList2.get(0)[8];
					stat_common[26]=stat_commonList2.get(0)[9];
					stat_common[27]=stat_commonList2.get(0)[10];
					stat_common[28]=stat_commonList2.get(0)[11];
					stat_common[29]=stat_commonList2.get(0)[12];
					stat_common[30]=stat_commonList2.get(0)[13];
					stat_common[31]=stat_commonList2.get(0)[14];
					stat_common[32]=stat_commonList2.get(0)[15];
					stat_common[33]=stat_commonList2.get(0)[16];
				}else{ flag=false;}
			} else{ flag=false;}
			if(flag){ stat_common_twoDays.add(stat_common);}
			}
		}
		
		 String[] MultiQuDaos={"安拓1","N多","P9P9","weiyun_MIESHI","爱贝","爱皮","淡鼎","点金","金立","琵琶网","苹果园","三星","万普","野火","易蛙","易蛙ios01","51","360","553sdk用户","anzhi","dcn用户","duoku用户","feiliu用户","gfan_MIESHI用户","jiyou","LEXUN用户","PPZHUSHOU","SHENGDA","smtmobi用户","tongbu","UC用户","xiaomiyx_MIESHI用户"};
		 for(int k = 0; k <MultiQuDaos.length; k++)
		 {
			 String qudao = MultiLanguageManager.translateMap.get(MultiQuDaos[k]);
			  key  = _day + MultiQuDaos[k] + fenqu + jixing;
			  key2 = _day2 + MultiQuDaos[k] + fenqu + jixing;
				stat_commonList  = userManager.getstat_common(key);
				stat_commonList2 = userManager.getstat_common(key2);
				String[] stat_common=new String[34];
				boolean flag=true;
				
				if (stat_commonList != null && stat_commonList.size() > 0) {
					if(!"0".equals(stat_commonList.get(0)[4]))
					{
						stat_common[0]=stat_commonList.get(0)[0];
						stat_common[1]=stat_commonList.get(0)[1];
						stat_common[2]=stat_commonList.get(0)[2];
						stat_common[3]=stat_commonList.get(0)[3];
						stat_common[4]=stat_commonList.get(0)[4];
						stat_common[5]=stat_commonList.get(0)[5];
						stat_common[6]=stat_commonList.get(0)[6];
						stat_common[7]=stat_commonList.get(0)[7];
						stat_common[8]=stat_commonList.get(0)[8];
						stat_common[9]=stat_commonList.get(0)[9];
						stat_common[10]=stat_commonList.get(0)[10];
						stat_common[11]=stat_commonList.get(0)[11];
						stat_common[12]=stat_commonList.get(0)[12];
						stat_common[13]=stat_commonList.get(0)[13];
						stat_common[14]=stat_commonList.get(0)[14];
						stat_common[15]=stat_commonList.get(0)[15];
						stat_common[16]=stat_commonList.get(0)[16];
					}else{flag=false;}
				} else{
					flag=false;
				}
				
				if (stat_commonList2 != null && stat_commonList2.size() > 0) {
					if(!"0".equals(stat_commonList2.get(0)[4]))
					{
						stat_common[17]=stat_commonList2.get(0)[0];
						stat_common[18]=stat_commonList2.get(0)[1];
						stat_common[19]=stat_commonList2.get(0)[2];
						stat_common[20]=stat_commonList2.get(0)[3];
						stat_common[21]=stat_commonList2.get(0)[4];
						stat_common[22]=stat_commonList2.get(0)[5];
						stat_common[23]=stat_commonList2.get(0)[6];
						stat_common[24]=stat_commonList2.get(0)[7];
						stat_common[25]=stat_commonList2.get(0)[8];
						stat_common[26]=stat_commonList2.get(0)[9];
						stat_common[27]=stat_commonList2.get(0)[10];
						stat_common[28]=stat_commonList2.get(0)[11];
						stat_common[29]=stat_commonList2.get(0)[12];
						stat_common[30]=stat_commonList2.get(0)[13];
						stat_common[31]=stat_commonList2.get(0)[14];
						stat_common[32]=stat_commonList2.get(0)[15];
						stat_common[33]=stat_commonList2.get(0)[16];
					}
				} 
				if(flag){ stat_common_twoDays.add(stat_common); }
		 }
		return stat_common_twoDays;
	}
   /**
    * 各个机型日报的产生
    */
   public  List<String[]> genJiXingDailyStat(Date statdate){

	   String fenqu=null;
	   String qudao=null;
	   String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
	   String _day2 = DateUtil.formatDate(new Date(statdate.getTime()-24*60*60*1000), "yyyy-MM-dd");
	   String [] jiXings={"Android","IOS"};
	    List<String[]> stat_common_twoDays = new ArrayList<String[]>();
		List<String[]> stat_commonList = null;
		List<String[]> stat_commonList2 = null;
	
		
		
		for (int k = 0; k < jiXings.length+1; k++) {
			String jixing=null;
			if(k < jiXings.length){//这种情况处理的是所有条件都选择“全部”的情况。
			 jixing = jiXings[k];
			}
			boolean flag=true;
			String key  = _day  + qudao + fenqu + jixing;
			String key2 = _day2 + qudao + fenqu + jixing;
			stat_commonList  = userManager.getstat_common(key);
			stat_commonList2 = userManager.getstat_common(key2);
			String[] stat_common=new String[34];
			if (stat_commonList != null && stat_commonList.size() > 0) {
				stat_common[0]=stat_commonList.get(0)[0];
				stat_common[1]=stat_commonList.get(0)[1];
				stat_common[2]=stat_commonList.get(0)[2];
				stat_common[3]=stat_commonList.get(0)[3];
				stat_common[4]=stat_commonList.get(0)[4];
				stat_common[5]=stat_commonList.get(0)[5];
				stat_common[6]=stat_commonList.get(0)[6];
				stat_common[7]=stat_commonList.get(0)[7];
				stat_common[8]=stat_commonList.get(0)[8];
				stat_common[9]=stat_commonList.get(0)[9];
				stat_common[10]=stat_commonList.get(0)[10];
				stat_common[11]=stat_commonList.get(0)[11];
				stat_common[12]=stat_commonList.get(0)[12];
				stat_common[13]=stat_commonList.get(0)[13];
				stat_common[14]=stat_commonList.get(0)[14];
				stat_common[15]=stat_commonList.get(0)[15];
				stat_common[16]=stat_commonList.get(0)[16];
			} else{ flag=false;}
			
			if (stat_commonList2 != null && stat_commonList2.size() > 0) {
				stat_common[17]=stat_commonList2.get(0)[0];
				stat_common[18]=stat_commonList2.get(0)[1];
				stat_common[19]=stat_commonList2.get(0)[2];
				stat_common[20]=stat_commonList2.get(0)[3];
				stat_common[21]=stat_commonList2.get(0)[4];
				stat_common[22]=stat_commonList2.get(0)[5];
				stat_common[23]=stat_commonList2.get(0)[6];
				stat_common[24]=stat_commonList2.get(0)[7];
				stat_common[25]=stat_commonList2.get(0)[8];
				stat_common[26]=stat_commonList2.get(0)[9];
				stat_common[27]=stat_commonList2.get(0)[10];
				stat_common[28]=stat_commonList2.get(0)[11];
				stat_common[29]=stat_commonList2.get(0)[12];
				stat_common[30]=stat_commonList2.get(0)[13];
				stat_common[31]=stat_commonList2.get(0)[14];
				stat_common[32]=stat_commonList2.get(0)[15];
				stat_common[33]=stat_commonList2.get(0)[16];			} 
			
			if(flag){stat_common_twoDays.add(stat_common);}
		}
		
		return stat_common_twoDays;
   }
   
   
public static void main(String[] args) throws ParseException
   {
	DailyGenerationExcelManager s=new DailyGenerationExcelManager();
//	   Date statdate = new SimpleDateFormat("yyyy-MM-dd").parse("2013-05-01"); 
//	   s.getDayList(statdate);
	  
	
//	try {
//		   InternetAddress[] to=InternetAddress.parse("huangqing@sqage.com");
//		   InternetAddress[] cc=InternetAddress.parse("303018973@qq.com");
//		   InternetAddress[] bcc=InternetAddress.parse("303018973@qq.com");
//		   String[] attachments={"D:\\sss.txt"};
//		   
//		JavaMailUtils.sendMail("smtp.sqage.com", to, cc, bcc, "日报", "尊敬的介运恒，这是一个测试冰冰发","text/html;charset=gbk", attachments, "jieyunheng@sqage.com", "jieyunheng", "987uio");
//		//JavaMailUtils.sendMail(smtpHost, to, cc, bcc, subject, message, attachments, from, username, password);
//	   
//	   } catch (Exception e) {
//		e.printStackTrace();
//	}
	   

   }



}
