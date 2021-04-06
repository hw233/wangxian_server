package com.fy.engineserver.billboard;

import com.fy.engineserver.datasource.language.Translate;

public class BillboardType {
	public static final byte TYPE_ITEM=0;
	
	public static final byte TYPE_PLAYER=1;
	
	public static final byte TYPE_GANG=2;
	
	public static final byte TYPE_CITY=3;
	
	public static final byte TYPE_PET=4;
	
	public static final String[] TYPES={Translate.text_4,Translate.text_2317,Translate.text_1037,Translate.text_2318,Translate.text_2319};
	
	public static String getType(byte type){
		if(type<0||type>=BillboardType.TYPES.length){
			return null;
		}
		return BillboardType.TYPES[type];
	}
	
	public static byte getTypeIndex(String type){
		byte index=-1;
		for(int i=0;i<BillboardType.TYPES.length;i++){
			if(BillboardType.TYPES[i].equals(type)){
				index=(byte)i;
			}
		}
		return index;
	}
}
