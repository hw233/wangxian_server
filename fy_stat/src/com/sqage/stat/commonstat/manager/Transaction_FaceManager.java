package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import com.sqage.stat.commonstat.entity.Transaction_Face;
import com.sqage.stat.commonstat.entity.Transfer_Platform;

public interface Transaction_FaceManager {
	
	public void addList(ArrayList<Transaction_Face> transaction_FaceList);
	
	public ArrayList<String[]> getTransaction_Face(String sql, String[] columusEnums);
	/**
	 * 平台交易购买成功以外的消息入数据库的处理
	 * @param transfer_PlatformList
	 */
	public void addTransferFormList(ArrayList<Transfer_Platform> transfer_PlatformList);
	/**
	 * 平台交易购买成功时的消息入数据库的处理
	 * @param transfer_PlatformList
	 */
	public void addTransferFormList_2(ArrayList<Transfer_Platform> transfer_PlatformList);
	
	
	/**
	 * 平台交易购买撤单的消息入数据库的处理
	 * @param transfer_PlatformList
	 */
	public void addTransferFormList_3(ArrayList<Transfer_Platform> transfer_PlatformList);
	
	
}
