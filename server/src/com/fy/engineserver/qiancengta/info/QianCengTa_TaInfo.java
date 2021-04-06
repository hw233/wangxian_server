package com.fy.engineserver.qiancengta.info;

import java.util.ArrayList;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 千层塔 塔的数据
 * 
 *
 */
public class QianCengTa_TaInfo {

	//塔的基础数据  普通 困难 深渊
	public static QianCengTa_TaInfo taInfo;
	public static QianCengTa_TaInfo hardTaInfo;
	public static QianCengTa_TaInfo gulfTaInfo;
	
	public static long[] commonCost = new long[] {0, 50000, 100000, 150000, 200000, 250000, 300000, 350000};
	public static long[] hardCost = new long[] {0, 50000, 100000, 150000, 200000, 250000, 300000, 350000};
	public static long[] gulfCost = new long[] {0, 50000, 100000, 150000, 200000, 250000, 300000, 350000};
	
	public static int[] QianCengHideRandom1 = new int[]{336, 336, 336, 336, 336, 336};	//基础几率
	public static int[] QianCengHideRandom2 = new int[]{0, 0, 0, 0, 0, 0};			//每层叠加的几率
	
	public static String[] QianCengMapNames = new String[]{"qiancengtayiceng","qiancengtaerceng","qiancengtasanceng","qiancengtasiceng","qiancengtawuceng","qiancengtaliuceng"};
	
	public static String[] QianCengNanduNames = new String[]{Translate.text_qiancengta_普通, Translate.text_qiancengta_困难, Translate.text_qiancengta_深渊};
	
	public static int[] QianCengTaLevel = new int[]{111,131,151,171,191,191};
	
	public static int HideRandom = 10000;			//正常应该是万分之
	
	public QianCengTa_TaInfo(){
		for (int i = 0; i < daoCount; i++) {
			QianCengTa_DaoInfo daoInfo = new QianCengTa_DaoInfo();
			daoInfo.setDaoIndex(i);
			daoList.add(daoInfo);
			QianCengTa_CengInfo hiddenCeng = new QianCengTa_CengInfo();
			daoInfo.setHiddenCeng(hiddenCeng);
			hiddenCeng.setCengIndex(-1);
			for (int j = 0; j < cengCount; j++) {
				QianCengTa_CengInfo cengInfo = new QianCengTa_CengInfo();
				cengInfo.setCengIndex(j);
				daoInfo.getCengList().add(cengInfo);
			}
		}
	}
	
	public static int daoCount = 6;
	
	public static int cengCount = 25;
	
	//每次刷新需要的银子数，单位为文
	public long[] costSilver;
	
	public long getCostSilver(int num) {
		if (num >= costSilver.length){
			return -1;
		}
		return costSilver[num];
	}
	
	//塔里道的列表，从低级到高级
	public ArrayList<QianCengTa_DaoInfo> daoList = new ArrayList<QianCengTa_DaoInfo>();
	
	public QianCengTa_CengInfo getCengInfo(int daoIndex, int cengIndex){
		if (cengIndex < 0) {
			return daoList.get(daoIndex).getHiddenCeng();
		}else {
			return daoList.get(daoIndex).getCengList().get(cengIndex);
		}
	}
}
