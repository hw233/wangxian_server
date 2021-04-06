package com.fy.engineserver.activity.extraquiz;

import java.util.Arrays;

public class RewardArticles {
	/** 物品统计名 */
	private String[] articleCNNames;
	/** 对应数量 */
	private int[] articleNums;
	
	public RewardArticles (String str) throws Exception {
		String[] st = str.split(";");
		this.articleCNNames = new String[st.length];
		this.articleNums = new int[st.length];
		for (int i=0; i<st.length; i++) {
			String[] ss = st[i].split(",");
			if (ss.length != 2) {
				throw new Exception("[奖励物品格式录入错误] [" + str + "]");
			}
			this.articleCNNames[i] = ss[0];
			this.articleNums[i] = Integer.parseInt(ss[1]);
		}
	}
	
	@Override
	public String toString() {
		return "RewardArticles [articleCNNames=" + Arrays.toString(articleCNNames) + ", articleNums=" + Arrays.toString(articleNums) + "]";
	}
	public String[] getArticleCNNames() {
		return articleCNNames;
	}
	public void setArticleCNNames(String[] articleCNNames) {
		this.articleCNNames = articleCNNames;
	}
	public int[] getArticleNums() {
		return articleNums;
	}
	public void setArticleNums(int[] articleNums) {
		this.articleNums = articleNums;
	}
	
	
}
