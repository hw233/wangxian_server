package com.fy.boss.platform.xiaomi.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fy.boss.platform.xiaomi.common.ParamEntry;

public class ParamUtil {
	/**
	 * 获取得到排序好的查询字符串
	 * @param params 请求参数
	 * @param isContainSignature 是否包含signature参数
	 * @return
	 */
	public static  String getSortQueryString(List<ParamEntry> params) throws Exception {
		Collections.sort( params, new Comparator<ParamEntry>(){
			@Override
			public int compare( ParamEntry object1, ParamEntry object2 ){
					return object1.getKey().compareTo( object2.getKey());
			}
		});
		StringBuffer sb = new StringBuffer();
		for(ParamEntry pe:params){
			sb.append(pe.getKey()).append("=").append(pe.getValue()).append("&");
		}
		
		String text = sb.toString();
		if(text.endsWith("&")) {
			text=text.substring(0,text.length()-1);
		}
		return text;
	}
}
