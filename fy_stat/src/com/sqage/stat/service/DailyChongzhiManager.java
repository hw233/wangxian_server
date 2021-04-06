package com.sqage.stat.service;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xuanzhi.tools.text.DateUtil;

public class DailyChongzhiManager {
	
	protected static DailyChongzhiManager m_self = null;
	protected static final Log logger = LogFactory.getLog(DailyChongzhiManager.class);
	public static DailyChongzhiManager getInstance() {
			return m_self;
		}
	public void init() throws Exception{
		m_self = this;
		logger.info("["+DailyChongzhiManager.class.getName()+"] [initialized]");
	}
	

	
	 
	 public void generationChongZhiDate(int column,int rowsCount,String title,WritableSheet ws,Date statdate) throws RowsExceededException, WriteException, SQLException
	 {
		String fenQuArray[][]={ {"国服充值"},{"台湾充值"},{"腾讯充值"},{"韩国充值"}  } ;
		String statday= DateUtil.formatDate(statdate,"yyyy-MM-dd");
		String preday= DateUtil.formatDate(new Date(statdate.getTime()-24*60*60*1000),"yyyy-MM-dd");
		String dayList[] ={statday,preday};
		ArrayList<String []> ls=getChongzhiByFenfu(statdate);
		
		ws.mergeCells(column, rowsCount, column+6, rowsCount);
        ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
        writeWritableSheetFirstTiTile(ws,column++, rowsCount++, title);//写题目
      
        for(int col =0;col<dayList.length;col++)
		{
        	//ws.mergeCells(column+(col+0)*3+1, rowsCount, column+(col+1)*3, rowsCount);
            ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
        	writeWritableSheetSecTiTile(ws,column+col+1, rowsCount, dayList[col]);
        	
        	writeWritableSheetSecTiTile(ws,column+col+2, rowsCount, "收入增减");
        	writeWritableSheetSecTiTile(ws,column+col+3, rowsCount, "增幅");
        	writeWritableSheetSecTiTile(ws,column+col+4, rowsCount, "占比");
		}
		
        rowsCount+=1;
		for(int col2=0;col2<ls.size();col2++)
		{
			String [] moneys=ls.get(col2);
			 writeWritableSheetSecTiTile(ws,column, rowsCount, moneys[0]);
			 writeWritableSheetSecTiTile(ws,column+1, rowsCount, moneys[2]);
			 writeWritableSheetSecTiTile(ws,column+2, rowsCount, moneys[4]);
			 
			 Long increase=Long.parseLong(moneys[2])-Long.parseLong(moneys[4]);
			 long rate=0L;
			 if(moneys[4]!=null&&!"0".equals(moneys[4])) {  rate =(increase*100)/Long.parseLong(moneys[4]); }
			 writeWritableSheetSecTiTile(ws,column+3, rowsCount, increase.toString());
			 writeWritableSheetSecTiTile(ws,column+4, rowsCount++, rate+"%");
			 
		}
		
	 }
	
	
	public ArrayList<String []> getChongzhiByFenfu(Date statdate) throws SQLException
   {
        String dbArray[][]={
                   {"国服充值","stat_mieshi","stat_mieshi"},      
                   {"台湾充值","STAT_TAIWAN","STAT_TAIWAN"},
                   {"腾讯充值","STAT_MIESHI_QQ","STAT_MIESHI_QQ"},
                   {"韩国充值","stat_hanguo","stat_hanguo"}
                   } ;
        ArrayList<String[]> ls = new ArrayList<String[]>();
     
			for (int col = 0; col < dbArray.length; col++) {
				ArrayList<String[]> ls_temp = new ArrayList<String[]>();
				String[] strs = new String[5];
				ls_temp = getChongZhiJinE(statdate, dbArray[col][1], dbArray[col][2]);
				for (int m = 0; m < ls_temp.size(); m++) {
                  
					String[] strs_temp = ls_temp.get(m);
					if(m%2==0){
					    strs[0] = dbArray[col][0];
					    strs[1] = strs_temp[0];
					    strs[2] = strs_temp[1];
					}else{
						strs[3] = strs_temp[0];
						strs[4] = strs_temp[1];
					}
				}
				ls.add(strs);
			}
		
		return ls;
	}
	
	
	public ArrayList<String []> getChongZhiJinE(Date statdate,String dbUseName,String dbPsw) {
		
		String day = DateUtil.formatDate(statdate,"yyyy-MM-dd");
		String preday = DateUtil.formatDate(new Date(statdate.getTime()-24*60*60*1000),"yyyy-MM-dd");
		ArrayList<String []> ls=new ArrayList<String []> ();
		Connection conn=null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.142.188:1521:ora11g",dbUseName,dbPsw);
// Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.142.188:1521:ora11g","stat_hanguo","stat_hanguo");
//                                                                                            stat_mieshi
			
			String sql="select to_char(c.time,'YYYY-MM-DD') statdate,sum(c.money) money from stat_chongzhi c  " +
					" where c.time between to_date('"+preday+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+day+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +
					" group by to_char(c.time,'YYYY-MM-DD') order by  to_char(c.time,'YYYY-MM-DD') ";
			ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			
			while (rs.next()) {
				String [] strs=new String[2];
				strs[0]=rs.getString("statdate");
				strs[1]=rs.getString("money");
				ls.add(strs);
			}
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally{
		try {
			if(ps!=null)
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   }
	return ls;
}
	
	
	
	/**
	 * 根据DAU对ls中的数据进行排序
	 * @param ls
	 * @return
	 */
	public List<String[]> sortDAUList(List<String[]> ls)
	{
		//最正统的做法是自定义一个比较器，如下所示
		  class ListComparator implements Comparator {
		    public int compare(Object o1, Object o2) {
		    String[] c1 = (String[]) o1;
		    String[] c2 = (String[]) o2;
		    int num1=Integer.parseInt(c1[4]);
		    int num2= Integer.parseInt(c2[4]);
		    if (num1<num2) {
		     return 1;
		    }
		  return -1;
		 }
		  }
		  ListComparator articleComparator = new ListComparator();
		  Collections.sort(ls, articleComparator);
		return ls;
	}
	
	/**
	 * 根据注册人数对ls中的数据进行排序
	 * @param ls
	 * @return
	 */
	public List<String[]> sortZHUCEList(List<String[]> ls)
	{
		//最正统的做法是自定义一个比较器，如下所示
		  class ListComparator implements Comparator {
		    public int compare(Object o1, Object o2) {
		    String[] c1 = (String[]) o1;
		    String[] c2 = (String[]) o2;
		    int num1=Integer.parseInt(c1[1]);
		    int num2= Integer.parseInt(c2[1]);
		    if (num1<num2) {
		     return 1;
		    }
		  return -1;
		 }
		  }
		  ListComparator articleComparator = new ListComparator();
		  Collections.sort(ls, articleComparator);
		return ls;
	}
	
	/**
	 * 根据充值金额对ls中的数据进行排序
	 * @param ls
	 * @return
	 */
	public List<String[]> sortMONWYList(List<String[]> ls)
	{
		//最正统的做法是自定义一个比较器，如下所示
		  class ListComparator implements Comparator {
		    public int compare(Object o1, Object o2) {
		    String[] c1 = (String[]) o1;
		    String[] c2 = (String[]) o2;
		    int num1=Integer.parseInt(c1[12]);
		    int num2= Integer.parseInt(c2[12]);
		    if (num1<num2) {
		     return 1;
		    }
		  return -1;
		 }
		  }
		  ListComparator articleComparator = new ListComparator();
		  Collections.sort(ls, articleComparator);
		return ls;
	}
	
	
	
	/**
	 * 向电子表格的某个表格里写数据
	 * @param ws  name of the sheet to be written
	 * @param column    column number
	 * @param row     row number
	 * @param content   content
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	 public void writeWritableSheet(WritableSheet ws,int column,int row,String content) throws RowsExceededException, WriteException
	 {
		 
		 Label labelC = new jxl.write.Label(column, row, content);
         ws.addCell(labelC);
	 }
	 
	 /**
	  * 一级标题
	  * @param ws
	  * @param column
	  * @param row
	  * @param content
	  * @throws RowsExceededException
	  * @throws WriteException
	  */
	 public void writeWritableSheetFirstTiTile(WritableSheet ws,int column,int row,String content) throws RowsExceededException, WriteException
	 {
		 
		 WritableFont font1= new WritableFont(WritableFont.TIMES,16,WritableFont.BOLD);
			 WritableCellFormat format1=new WritableCellFormat(font1);

			//把水平对齐方式指定为居中
			 format1.setAlignment(jxl.format.Alignment.CENTRE);

//			 //把垂直对齐方式指定为居中
//			 format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		 
		 Label labelC = new jxl.write.Label(column, row, content,format1);
         ws.addCell(labelC);
	 }
	 
	 /**
	  * 二级级标题
	  * @param ws
	  * @param column
	  * @param row
	  * @param content
	  * @throws RowsExceededException
	  * @throws WriteException
	  */
	 public void writeWritableSheetSecTiTile(WritableSheet ws,int column,int row,String content) throws RowsExceededException, WriteException
	 {
		 
		 WritableFont font1= new WritableFont(WritableFont.TIMES,12,WritableFont.BOLD);
			 WritableCellFormat format1=new WritableCellFormat(font1);

			//把水平对齐方式指定为居中
			 format1.setAlignment(jxl.format.Alignment.CENTRE);

//			 //把垂直对齐方式指定为居中
//			 format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		 
		 Label labelC = new jxl.write.Label(column, row, content,format1);
         ws.addCell(labelC);
	 }
	 
	 public void writeDaysTitle(WritableSheet ws,int column,int rows,String title) throws RowsExceededException, WriteException
	 {
		 
		 ws.mergeCells(column, rows, 17, rows);
         ws.setRowView(rows,500);//第一行 高设为 500
         
		 writeWritableSheetFirstTiTile(ws,column, rows++, title);//写题目
		 writeWritableSheetSecTiTile(ws,column++, rows, "日期");
		 writeWritableSheetSecTiTile(ws,column++, rows, "注册人数");
		 writeWritableSheetSecTiTile(ws,column++, rows, "有效注册人数");
		 writeWritableSheetSecTiTile(ws,column++, rows, "未激活账号");
		 writeWritableSheetSecTiTile(ws,column++, rows, "独立进入");
		 writeWritableSheetSecTiTile(ws,column++, rows, "独立进入(当天)");
		 writeWritableSheetSecTiTile(ws,column++, rows, "平均在线");
		 writeWritableSheetSecTiTile(ws,column++, rows, "最高在线");
		 writeWritableSheetSecTiTile(ws,column++, rows, "平均在线时长(分钟)");
		 writeWritableSheetSecTiTile(ws,column++, rows, "付费人数");
		 writeWritableSheetSecTiTile(ws,column++, rows, "新付费人数");
		 writeWritableSheetSecTiTile(ws,column++, rows, "有效用户平均在线时长(分钟)");
		 writeWritableSheetSecTiTile(ws,column++, rows, "充值金额");
		 writeWritableSheetSecTiTile(ws,column++, rows, "当天进入ARPU(不分区)");
         
		 writeWritableSheetSecTiTile(ws,column++, rows, "独立进入老用户");
		 writeWritableSheetSecTiTile(ws,column++, rows, "付费渗透率");
		 writeWritableSheetSecTiTile(ws,column++, rows, "激活率");
		 writeWritableSheetSecTiTile(ws,column++, rows, "有效率");
         
	 }
	 /**
	  * 细分标题
	  * @param ws
	  * @param column
	  * @param rowsCount
	  * @param title
	  * @throws RowsExceededException
	  * @throws WriteException
	  */
	 public void writeXiFenTitle(WritableSheet ws,int column,int rowsCount,String title,Date dt,String columnName) throws RowsExceededException, WriteException
	 {
		 ws.mergeCells(column, rowsCount, column+13, rowsCount);
         ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
         //ws.setColumnView(rowsCount,30);//第rowsCount行 宽设为 200
         writeWritableSheetFirstTiTile(ws,column, rowsCount++, title);//写题目
         
         ws.mergeCells(column+1, rowsCount, column+4, rowsCount);
         ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
         writeWritableSheetFirstTiTile(ws,column+1, rowsCount, DateUtil.formatDate(dt,"yyyy-MM-dd"));//写题目
         
         ws.mergeCells(column+5, rowsCount, column+8, rowsCount);
         ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
         writeWritableSheetFirstTiTile(ws,column+5, rowsCount, DateUtil.formatDate(new Date(dt.getTime()-24*60*60*1000),"yyyy-MM-dd"));//写题目
         
         ws.mergeCells(column+9, rowsCount, column+13, rowsCount);
         ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
         writeWritableSheetFirstTiTile(ws,column+9, rowsCount++, "对比");//写题目
         
        
         writeWritableSheetSecTiTile(ws,column++, rowsCount,columnName);
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费人数");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "充值金额");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费ARPU");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费渗透率");
        
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费人数");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "充值金额");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费ARPU");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费渗透率");
         
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "充值人数增减");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费金额增减");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费ARPU增减");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "付费率增减");
         
	 }
	 
	 /**
	  * 渠道注册细分标题
	  * @param ws
	  * @param column
	  * @param rowsCount
	  * @param title
	  * @throws RowsExceededException
	  * @throws WriteException
	  */
	 public void writeXiFenTitle_zhuce(WritableSheet ws,int column,int rowsCount,String title,Date dt) throws RowsExceededException, WriteException
	 {
		 ws.mergeCells(column, rowsCount, column+7, rowsCount);
         ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
         writeWritableSheetFirstTiTile(ws,column, rowsCount++, title);//写题目
         
         ws.mergeCells(column+1, rowsCount, column+3, rowsCount);
         ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
         writeWritableSheetFirstTiTile(ws,column+1, rowsCount, DateUtil.formatDate(dt,"yyyy-MM-dd"));//写题目
         
         ws.mergeCells(column+4, rowsCount, column+5, rowsCount);
         ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
         writeWritableSheetFirstTiTile(ws,column+4, rowsCount, DateUtil.formatDate(new Date(dt.getTime()-24*60*60*1000),"yyyy-MM-dd"));//写题目
         
         ws.mergeCells(column+6, rowsCount, column+6, rowsCount);
         ws.setRowView(rowsCount,500);//第rowsCount行 高设为500
         writeWritableSheetFirstTiTile(ws,column+6, rowsCount++, "对比");//写题目
         
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "渠道");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "注册");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "独立进入当天");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "激活率");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "注册");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "激活率");
         writeWritableSheetSecTiTile(ws,column++, rowsCount, "注册增减");
	 }
	 /**
	  * 向表格中写数据。
	  * @param ws
	  * @param column
	  * @param rowsCount
	  * @param title
	  * @throws RowsExceededException
	  * @throws WriteException
	  */
	 public int writeSheetData(WritableSheet ws,int column,int rowsCount,List<String[]> data) throws RowsExceededException, WriteException
	 {
	           for (int i=0;i<data.size();i++) 
	           {
	        	   String [] dats=(String [])data.get(i);
	        	   if(dats[0]==null){
	        		   if(logger.isDebugEnabled()){ logger.debug("向电子表格中写数据，表格中有null  dats[0]:"+dats[0]);}
	        	        continue;
	        	   }
	        	   if(!"0".equals(dats[12])||!"0".equals(dats[29])){
	        	   int col=column;
	        	   writeWritableSheetSecTiTile(ws,col++, rowsCount, "".endsWith(dats[0].substring(10).replace("null", "")) ? "全部":dats[0].substring(10).replace("null", ""));
	        	   
                   writeWritableSheet(ws,col++, rowsCount, dats[9]);///付费人数
                   writeWritableSheet(ws,col++, rowsCount, dats[12]);//充值金额
                   writeWritableSheet(ws,col++, rowsCount, dats[14]);
                    String shenTouRate1=xiangChu(dats[9],dats[4]);
                   writeWritableSheet(ws,col++, rowsCount, shenTouRate1+" %");//付费渗透率
                   
                   writeWritableSheet(ws,col++, rowsCount, dats[26]);///付费人数
                   writeWritableSheet(ws,col++, rowsCount, dats[29]);//充值金额
                   writeWritableSheet(ws,col++, rowsCount, dats[31]);//当天进入AEPU(不分区)
                    String shenTouRate2=xiangChu(dats[26],dats[21]);
                   writeWritableSheet(ws,col++, rowsCount, shenTouRate2+" %");//付费渗透率
                   
                   writeWritableSheet(ws,col++, rowsCount, Long.toString(Long.parseLong(dats[9]==null? "0":dats[9])-Long.parseLong(dats[26]==null? "0":dats[26])));
                   writeWritableSheet(ws,col++, rowsCount, Long.toString(Long.parseLong(dats[12]==null? "0":dats[12])-Long.parseLong(dats[29]==null? "0":dats[29])));
                   writeWritableSheet(ws,col++, rowsCount, Long.toString(Long.parseLong(dats[14]==null? "0":dats[14])-Long.parseLong(dats[31]==null? "0":dats[31])));
                  
                   DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
                   writeWritableSheet(ws,col++, rowsCount, df.format(Float.parseFloat(shenTouRate1)-Float.parseFloat(shenTouRate2))+" %");
		             
                    rowsCount++;
	        	   }
	            }
	           return rowsCount;
	 }
	 
	 /**
	  * 向表格中写注册数据。
	  * @param ws
	  * @param column
	  * @param rowsCount
	  * @param title
	  * @throws RowsExceededException
	  * @throws WriteException
	  */
	 public int writeSheetData_zhuce(WritableSheet ws,int column,int rowsCount,List<String[]> data) throws RowsExceededException, WriteException
	 {
	           for (int i=0;i<data.size();i++) 
	           {
	        	   String [] dats=(String [])data.get(i);
	        	   if(dats[0]==null){
	        		   if(logger.isDebugEnabled()){ logger.debug("向电子表格中写数据，表格中有null  dats[0]:"+dats[0]);}
	        	        continue;
	        	   }
	        	   if(!"0".equals(dats[12])||!"0".equals(dats[29])){
	        	   int col=column;
	        	   writeWritableSheetSecTiTile(ws,col++, rowsCount, dats[0].substring(10).replace("null", ""));//注册人数
	        	   writeWritableSheetSecTiTile(ws,col++, rowsCount, dats[1]);//注册人数
	        	   writeWritableSheetSecTiTile(ws,col++, rowsCount, dats[5]);//独立进入当天
	        	   writeWritableSheet(ws,col++, rowsCount, xiangChu(dats[5],dats[1])+" %");//激活率
	        	   
	        	   writeWritableSheetSecTiTile(ws,col++, rowsCount, dats[18]);//注册人数
	        	   //writeWritableSheetSecTiTile(ws,col++, rowsCount, dats[22]);//独立进入当天
	        	   writeWritableSheet(ws,col++, rowsCount, xiangChu(dats[22],dats[18])+" %");//激活率
                   writeWritableSheet(ws,col++, rowsCount, Long.toString(Long.parseLong(dats[1]==null? "0":dats[1])-Long.parseLong(dats[18]==null? "0":dats[18])));
                    rowsCount++;
	        	   }
	            }
	           return rowsCount;
	 }
	
	 
	 /**
		 * 两个字符串相除，如果参数不合法，返回0；  返回结构为百分率，但是没有加百分号。
		 * @param beiChuShu
		 * @param chuShu
		 * @return
		 */
		public String xiangChu(String beiChuShu,String chuShu)
		{
			if(beiChuShu==null||chuShu==null||"0".equals(chuShu))
				return "0";
			float size = Float.parseFloat(beiChuShu)*100/Float.parseFloat(chuShu);
			   DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
			   String filesize = df.format(size);//返回的是String类型的
			   return filesize;
			
		}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
