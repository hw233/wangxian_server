package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.Transaction_FaceDao;
import com.sqage.stat.commonstat.entity.Transaction_Face;
import com.sqage.stat.commonstat.entity.Transfer_Platform;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class Transaction_FaceDaoImpl implements Transaction_FaceDao {
	static Logger logger = Logger.getLogger(Transaction_FaceDaoImpl.class);
	DataSourceManager dataSourceManager;

	@Override
	public void addList(ArrayList<Transaction_Face> transaction_FaceList) {

        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_TRANSACTION_FACE(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					"ZHENYING," +
					
					"FUSERNAME," +
					"FQUDAO," +
					"FDAOJU," +
					"FLEVEL," +
					"FVIP,"+
					"FMONEY,"+
					
					"TOLEVEL,"+
					"TOVIP,"+
					"TOMONEY,"+
					"TOUSERNAME,"+
					"TOQUDAO,"+
					"TODAOJU"+
					
					") values (SEQ_TRANSACTION_FACE_ID.NEXTVAL,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?)");

		    int i = -1;
			int count=0;
		for(Transaction_Face transaction_Face : transaction_FaceList){
			
			ps.setTimestamp(1, new Timestamp(transaction_Face.getCreateDate()));
			ps.setString(2, transaction_Face.getFenQu());
			ps.setString(3, transaction_Face.getZhenYing());
			
			ps.setString(4, transaction_Face.getFuserName());
			ps.setString(5, transaction_Face.getFquDao());
			ps.setString(6, transaction_Face.getFdaoJu());
			ps.setString(7, transaction_Face.getFlevel());
			ps.setString(8, transaction_Face.getFvip());
			ps.setLong(9, transaction_Face.getFmoney());
			
			ps.setString(10, transaction_Face.getToLevel());
			ps.setString(11, transaction_Face.getToVip());
			ps.setLong(12, transaction_Face.getToMoney());
			ps.setString(13, transaction_Face.getToUserName());
			ps.setString(14, transaction_Face.getToquDao());
			ps.setString(15, transaction_Face.getTodaoJu());
			
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%600==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			
			if(logger.isDebugEnabled()){logger.debug("开始批量插入面对面交易"+transaction_FaceList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("插入面对面交易数据库异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[添加面对面交易回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[添加面对面交易信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public ArrayList<String[]> getTransaction_Face(String sql, String[] columusEnums) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[columusEnums.length];
				for(int i=0;i<columusEnums.length;i++){
					vales[i]=rs.getString(columusEnums[i]);
				}
				resultlist.add(vales);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultlist;
    }
	
	
	
	@Override
	public void addTransferFormList(ArrayList<Transfer_Platform> transferPlatformList) {

        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			if(logger.isDebugEnabled()){ logger.debug("插入平台交易"+transferPlatformList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
			
		    String sql = " merge into STAT_TRANSFER_PLATFORM t " +
		    		" using (select ? as newid from dual) newData " +
		    		" ON (t.id||t.servername=newData.newid) " +
		    		" WHEN NOT MATCHED THEN " +
		    		" INSERT VALUES (?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?,  ?,?) " +
		    		" WHEN MATCHED THEN " +
		    		" UPDATE SET t.RESPONSERESULT= ?,t.trademoney=?,t.Column1=?";
			ps = con.prepareStatement(sql);
		    int i = -1;
			int count=0;
		for(Transfer_Platform tpform : transferPlatformList){
			
			ps.setString(1, tpform.getId()+tpform.getServerName());
			ps.setString(2, tpform.getId());
			
			ps.setString(3, tpform.getArticleId());
			ps.setString(4, tpform.getArticleName());
			ps.setString(5, tpform.getArticleColor());
			ps.setString(6, tpform.getCellIndex());
			ps.setString(7, tpform.getGoodsCount());
			ps.setTimestamp(8, new Timestamp(tpform.getCreateTime()));
			ps.setLong(9, tpform.getPayMoney());
			
			
			ps.setString(10, tpform.getSellPassportId());
			ps.setString(11, tpform.getSellPassportName());
			ps.setString(12, tpform.getSellPassportId());
			ps.setString(13, tpform.getSellPlayerName());
			ps.setString(14, tpform.getSellPassportChannel());
			ps.setString(15, tpform.getSellPlayerLevel());
			ps.setString(16, tpform.getSellVipLevel());
			
			ps.setString(17, tpform.getTradeType());
			ps.setString(18, tpform.getResponseResult());
			ps.setLong(19, tpform.getTradeMoney());
			
			ps.setString(20, tpform.getBuyPlayerId());
			ps.setString(21, tpform.getBuyPlayerName());
			ps.setString(22, tpform.getBuyPassportId());
			ps.setString(23, tpform.getBuyPassportName());
			ps.setString(24, tpform.getBuyPlayerLevel());
			ps.setString(25, tpform.getBuyPlayerVipLevel());
			ps.setString(26, tpform.getBuyPassportChannel());
			ps.setString(27, tpform.getServerName());
          
			ps.setString(28, tpform.getColumn1());
			ps.setString(29, tpform.getColumn2());
			ps.setString(30, tpform.getColumn3());
			ps.setString(31, tpform.getColumn4());
			
			ps.setString(32, tpform.getArticleSalePaySilver());
	        ps.setString(33, tpform.getCancelSaleSilver());
			
			ps.setString(34, tpform.getResponseResult());
			
			ps.setLong(35, tpform.getTradeMoney());
			ps.setString(36, tpform.getColumn1());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%600==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			
			if(logger.isDebugEnabled()){logger.debug("开始批量插入平台交易"+transferPlatformList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("插入平台交易数据库异常",e);
			//System.out.println("插入平台交易数据库异常"+e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[添加平台交易回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[添加平台交易信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	@Override
	public void addTransferFormList_2(ArrayList<Transfer_Platform> transferPlatformList) {


        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			if(logger.isDebugEnabled()){ logger.debug("插入平台交易交易成功"+transferPlatformList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
			
		    String sql = " merge into STAT_TRANSFER_PLATFORM t " +
		    		" using (select ? as newid from dual) newData " +
		    		" ON (t.id||t.servername=newData.newid) " +
		    		" WHEN NOT MATCHED THEN " +
		    		" INSERT VALUES (?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?,  ?,?) " +
		    		" WHEN MATCHED THEN " +
		    		" UPDATE SET t.RESPONSERESULT= ?,t.trademoney=?,t.Column1=?," +
		    		" t.BUYPLAYERID=?,BUYPLAYERNAME=?,BUYPASSPORTID=?,BUYPASSPORTNAME=?,BUYPLAYERLEVEL=?,BUYPLAYERVIPLEVEL=?,BUYPASSPORTCHANNEL=?,t.Column2=?";

			ps = con.prepareStatement(sql);
		    int i = -1;
			int count=0;
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Transfer_Platform tpform : transferPlatformList){
			
			if(logger.isDebugEnabled()){ logger.info("[交易平台信息交易成功上报] " + tpform.toString() + " [" + (System.currentTimeMillis() - now) + "ms]");}
			ps.setString(1, tpform.getId()+tpform.getServerName());
			ps.setString(2, tpform.getId());
			
			ps.setString(3, tpform.getArticleId());
			ps.setString(4, tpform.getArticleName());
			ps.setString(5, tpform.getArticleColor());
			ps.setString(6, tpform.getCellIndex());
			ps.setString(7, tpform.getGoodsCount());
			ps.setTimestamp(8, new Timestamp(tpform.getCreateTime()));
			ps.setLong(9, tpform.getPayMoney());
			
			
			ps.setString(10, tpform.getSellPassportId());
			ps.setString(11, tpform.getSellPassportName());
			ps.setString(12, tpform.getSellPassportId());
			ps.setString(13, tpform.getSellPlayerName());
			ps.setString(14, tpform.getSellPassportChannel());
			ps.setString(15, tpform.getSellPlayerLevel());
			ps.setString(16, tpform.getSellVipLevel());
			
			ps.setString(17, tpform.getTradeType());
			ps.setString(18, tpform.getResponseResult());
			ps.setLong(19, tpform.getTradeMoney());
			
			ps.setString(20, tpform.getBuyPlayerId());
			ps.setString(21, tpform.getBuyPlayerName());
			ps.setString(22, tpform.getBuyPassportId());
			ps.setString(23, tpform.getBuyPassportName());
			ps.setString(24, tpform.getBuyPlayerLevel());
			ps.setString(25, tpform.getBuyPlayerVipLevel());
			ps.setString(26, tpform.getBuyPassportChannel());
			ps.setString(27, tpform.getServerName());
          
			ps.setString(28, tpform.getColumn1());
			ps.setString(29, tpform.getColumn2());
			ps.setString(30, tpform.getColumn3());
			ps.setString(31, tpform.getColumn4());
			
			ps.setString(32, tpform.getArticleSalePaySilver());
	        ps.setString(33, tpform.getCancelSaleSilver());
			ps.setString(34, tpform.getResponseResult());
			ps.setLong(35, tpform.getTradeMoney());
			ps.setString(36, tpform.getColumn1());
			
			ps.setString(37, tpform.getBuyPlayerId());
			ps.setString(38, tpform.getBuyPlayerName());
			ps.setString(39, tpform.getBuyPassportId());
			ps.setString(40, tpform.getBuyPassportName());
			ps.setString(41, tpform.getBuyPlayerLevel());
			ps.setString(42, tpform.getBuyPlayerVipLevel());
			ps.setString(43, tpform.getBuyPassportChannel());
			
			Long trancationTime = tpform.getColumn2()==null? System.currentTimeMillis():Long.parseLong(tpform.getColumn2());
			Date tranTime= new Date(trancationTime);
			
			ps.setString(44,sf.format(tranTime));
			
			//System.out.println("tpform"+count+tpform.toString());
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%600==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			
			if(logger.isDebugEnabled()){logger.debug("开始批量插入平台交易交易成功"+transferPlatformList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("插入平台交易交易成功数据库异常",e);
			//System.out.println("插入平台交易数据库异常"+e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[添加平台交易交易成功回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[添加平台交易交易成功信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	@Override
	public void addTransferFormList_3(ArrayList<Transfer_Platform> transferPlatformList) {


        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			if(logger.isDebugEnabled()){ logger.debug("插入平台交易撤单"+transferPlatformList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
			
		    String sql = " merge into STAT_TRANSFER_PLATFORM t " +
		    		" using (select ? as newid from dual) newData " +
		    		" ON (t.id||t.servername=newData.newid) " +
		    		" WHEN NOT MATCHED THEN " +
		    		" INSERT VALUES (?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?,  ?,?) " +
		    		" WHEN MATCHED THEN " +
		    		" UPDATE SET t.RESPONSERESULT= ?,t.trademoney=?,t.Column1=?,t.CANCELSALESILVER=?";
			ps = con.prepareStatement(sql);
		    int i = -1;
			int count=0;
		for(Transfer_Platform tpform : transferPlatformList){
			
			ps.setString(1, tpform.getId()+tpform.getServerName());
			ps.setString(2, tpform.getId());
			
			ps.setString(3, tpform.getArticleId());
			ps.setString(4, tpform.getArticleName());
			ps.setString(5, tpform.getArticleColor());
			ps.setString(6, tpform.getCellIndex());
			ps.setString(7, tpform.getGoodsCount());
			ps.setTimestamp(8, new Timestamp(tpform.getCreateTime()));
			ps.setLong(9, tpform.getPayMoney());
			
			
			ps.setString(10, tpform.getSellPassportId());
			ps.setString(11, tpform.getSellPassportName());
			ps.setString(12, tpform.getSellPassportId());
			ps.setString(13, tpform.getSellPlayerName());
			ps.setString(14, tpform.getSellPassportChannel());
			ps.setString(15, tpform.getSellPlayerLevel());
			ps.setString(16, tpform.getSellVipLevel());
			
			ps.setString(17, tpform.getTradeType());
			ps.setString(18, tpform.getResponseResult());
			ps.setLong(19, tpform.getTradeMoney());
			
			ps.setString(20, tpform.getBuyPlayerId());
			ps.setString(21, tpform.getBuyPlayerName());
			ps.setString(22, tpform.getBuyPassportId());
			ps.setString(23, tpform.getBuyPassportName());
			ps.setString(24, tpform.getBuyPlayerLevel());
			ps.setString(25, tpform.getBuyPlayerVipLevel());
			ps.setString(26, tpform.getBuyPassportChannel());
			ps.setString(27, tpform.getServerName());
          
			ps.setString(28, tpform.getColumn1());
			ps.setString(29, tpform.getColumn2());
			ps.setString(30, tpform.getColumn3());
			ps.setString(31, tpform.getColumn4());
			
			ps.setString(32, tpform.getArticleSalePaySilver());
	        ps.setString(33, tpform.getCancelSaleSilver());
			ps.setString(34, tpform.getResponseResult());
			ps.setLong(35, tpform.getTradeMoney());
			ps.setString(36, tpform.getColumn1());
			
			ps.setString(37, tpform.getCancelSaleSilver());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%600==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			
			if(logger.isDebugEnabled()){logger.debug("开始批量插入平台交易撤单"+transferPlatformList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("插入平台交易数据库撤单异常",e);
			//System.out.println("插入平台交易数据库异常"+e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[添加平台交易撤单回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[添加平台交易撤单信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}
	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

}
