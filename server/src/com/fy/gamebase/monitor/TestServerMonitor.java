package com.fy.gamebase.monitor;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

public class TestServerMonitor implements MILinkProc {
	public static void main(String[] args) throws IOException {
		TestServerMonitor ts = new TestServerMonitor();
		ServerMonitor inst = ServerMonitor.getInst();
		MonitorItem mi = inst.addInfo(ts, "绿色服务器", true);
		mi.addLink("xxx=1&yyy=2", "点我");
		StringWriter w = new StringWriter();
		inst.dumpServerInfo(w);
		System.out.println(w.toString());
		//
		w.getBuffer().setLength(0);
		ServletRequest sr;
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("mappingKey2link", new String[]{"1001"});
		map.put("xxx", new String[]{"666"});
		inst.doRequest(map, w);
		System.out.println(w.toString());
	}

	@Override
	public String getName() {
		return "测试用例";
	}

	@Override
	public void procClick(MILink link, Map<String, String[]> params, Writer out)
			throws IOException {
		out.append("测试成功"+link.href+" "+link.showText);
	}
}
