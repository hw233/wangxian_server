package com.fy.engineserver.articleEnchant.client;


public class CompaModel implements Comparable<CompaModel>{
	
	public long articleIds ;
	public int articleTypes;
	public int enchantStatus;
	public String description1;
	public String description2 ;
	
	public CompaModel(long articleIds, int articleTypes, int enchantStatus, String description1, String description2) {
		super();
		this.articleIds = articleIds;
		this.articleTypes = articleTypes;
		this.enchantStatus = enchantStatus;
		this.description1 = description1;
		this.description2 = description2;
	}
	

	@Override
	public String toString() {
		return "CompaModel [articleIds=" + articleIds + ", articleTypes=" + articleTypes + ", enchantStatus=" + enchantStatus + ", description1=" + description1 + ", description2=" + description2 + "]";
	}




	/**
	 * 排序规则 以后看情况再改
	 */
	@Override
	public int compareTo(CompaModel o) {
		// TODO Auto-generated method stub
		if (this.articleIds > 0 && o.articleIds > 0) {
			if (this.enchantStatus <=0 && o.enchantStatus > 0) {
				return 1;
			}
			return 0;
		}
		if (this.articleIds > 0) {		
			return -1;
		}else if (o.articleIds > 0) {
			return 1;
		}
		return 0;
	}

}
