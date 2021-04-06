package com.fy.engineserver.sprite.pet2;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.fy.gamebase.monitor.JspProc;
import com.fy.gamebase.monitor.JspRouter;

/**
 * 
 * create on 2013年8月24日
 */
public class Pet2dataProc  implements JspProc {

	@Override
	public void proc(Writer out, Map<String, String[]> request)
			throws IOException {
		String action = JspRouter.getString(request, "action");
		out.append("action is "+action);
		if(action == null){
			return;
		}else if(action.isEmpty()){
			out.append("空的action");
			return;
		}
		if(action.equals("dumpConf")){
			dumpConf(out);
		}else{
			out.append("未知的action:");
			out.append(action);
		}
	}

	public void dumpConf(Writer out) {
		
	}

}
