<%@page contentType="text/html;charset=utf-8"%><%@page import="java.lang.management.*"%><%!
	protected static ThreadMXBean oMBean = null;
	static{
		oMBean = ManagementFactory.getThreadMXBean();
	}
	
	String getThreadGroupInfo(String uri,ThreadGroup group, boolean bDump, long lThreadID)
	{
		StringBuffer rc = new StringBuffer();
		rc.append("<BR><B>");
		rc.append("Thread Group: " + group.getName());
		rc.append("</B> : ");
		rc.append("max priority:" + group.getMaxPriority() + ", demon:" + group.isDaemon() + ", threads:" + group.activeCount());
		rc.append("<blockquote>");
		Thread threads[] = new Thread[group.activeCount()];
		group.enumerate(threads, false);
		for (int i = 0; i < threads.length && threads[i] != null; i++)
		{
			ThreadInfo oInfo = oMBean.getThreadInfo(threads[i].getId());
			rc.append("<B>");
			rc.append("<a href='"+uri+"&thread_id="+threads[i].getId()+"'>Thread(" + threads[i].getId() + "): " + threads[i].getName()+"</a>");
			rc.append(" : ");
			rc.append("<font color=\"blue\">" + oInfo.getThreadState() + "</font></B>");
			rc.append("<input type=button onclick='stopit(\""+threads[i].getName()+"\")' value='stop'><input type=button onclick='suspendit(\""+threads[i].getName()+"\")' value='suspend'><input type=button onclick='resumeit(\""+threads[i].getName()+"\")' value='resume'><input type=button onclick='interruptit(\""+threads[i].getName()+"\")' value='interrupt'>");
			rc.append("<BR>");
			if (bDump || threads[i].getId() == lThreadID)
			{
				rc.append("BlockedTime:"+oInfo.getBlockedTime() + ", BlockedCount:" + oInfo.getBlockedCount() + ", WaitedTime:" + oInfo.getWaitedTime() + ", WaitedCount:" + oInfo.getWaitedCount()).append("<BR>");
				StackTraceElement[] oTraceArr = threads[i].getStackTrace();
				int iLength = oTraceArr == null ? 0 : oTraceArr.length;
				for (int j = 0; j < iLength; j++)
				{
					rc.append("at:" + oTraceArr[j]);
					rc.append("<BR>");
				}
			}
		}
		ThreadGroup groups[] = new ThreadGroup[group.activeGroupCount()];
		group.enumerate(groups, false);
		for (int i = 0; i < groups.length && groups[i] != null; i++)
		{
			rc.append(getThreadGroupInfo(uri,groups[i], bDump, lThreadID));
		}
		rc.append("</blockquote>");
		return rc.toString();
	}
	
	public String showThreads(String uri,boolean bDump, long lThreadID)
	{
	// Get the root thread group
		ThreadGroup root = Thread.currentThread().getThreadGroup();
		while (root.getParent() != null)
		{
			root = root.getParent();
		}
		// I'm not sure why what gets reported is off by +1, 
		// but I'm adjusting so that it is consistent with the display
		int activeThreads = root.activeCount()-1;
		// I'm not sure why what gets reported is off by -1
		// but I'm adjusting so that it is consistent with the display
		int activeGroups = root.activeGroupCount()+1;
		String rc = "<b>Peak Threads:</b> "+oMBean.getPeakThreadCount()+"<br>"+"<b>Total Threads:</b> "+activeThreads+"<br>"+"<b>Total Thread Groups:</b> "+activeGroups+"<br>"+getThreadGroupInfo(uri,root, bDump, lThreadID);
		return rc;
	}
%><%
	Runtime r = Runtime.getRuntime();
	long max = r.maxMemory();
	long total = r.totalMemory();
	long free = r.freeMemory();
	
	out.println("max:" + com.xuanzhi.tools.text.StringUtil.addcommas(max) + "  total:"+com.xuanzhi.tools.text.StringUtil.addcommas(total)+"  free:"+com.xuanzhi.tools.text.StringUtil.addcommas(free)+"<br/>");
	
	String s = request.getParameter("s");
	String n = request.getParameter("n");
	if (s!= null && n != null)
	{
	      ThreadGroup root= Thread.currentThread().getThreadGroup();
	      while (root.getParent() != null) {
	         root = root.getParent();
	      }
	      Thread threads[]= new Thread[root.activeCount()];
	      root.enumerate(threads, true);
	      for (int i= 0; i < threads.length && threads[i] != null; i++) {
	      	if (threads[i].getName().equals(n))
	      	{
				if (s.equals("1"))	//stop
				{
					threads[i].stop();
					out.print("SUCCESSFUL STOP "+n+"<br>");
				}else if (s.equals("2")) //suspend
				{
					threads[i].suspend();
					out.print("SUCCESSFUL SUSPEND " + n + "<br>");
				}else if (s.equals("3")) //resume
				{
					threads[i].resume();
					out.print("SUCCESSFUL SUSPEND " + n + "<br>");
				}else if (s.equals("4")) //interrupt
				{
					threads[i].interrupt();
					out.print("SUCCESSFUL SUSPEND "+ n + "<br>");
				}	      		
	      	}
	      }
	}
	else 
	{
		s = request.getParameter("passwd");
		if(s == null) s = "";
		if(s.equals("haha1029") == false)
		{
			return;
		}
	}
	String strDump = request.getParameter("dump_info");
	boolean bDump = (strDump != null && strDump.equals("yes"));
	long lThreadID = -1;
	try
	{
		lThreadID = Long.parseLong(request.getParameter("thread_id"));
	}
	catch (Exception e)
	{}
	out.print(showThreads(request.getRequestURI()+"?s=&n=" ,bDump, lThreadID));   
%>
<script>
function stopit(n)
{
	document.location = "threadDump_1_5.jsp?s=1&n="+n;
}
function suspendit(n)
{
	document.location = "threadDump_1_5.jsp?s=2&n="+n;
}
function resumeit(n)
{
	document.location = "threadDump_1_5.jsp?s=3&n="+n;
}
function interruptit(n)
{
	document.location = "threadDump_1_5.jsp?s=4&n="+n;
}
</script>

