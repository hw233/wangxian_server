<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="org.apache.commons.fileupload.ParameterParser"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.io.DataInputStream"%>
<%
		String contentType = request.getContentType();
		out.print("contentType:"+contentType);
		if(contentType != null && contentType.contains("multipart"))
		{/* 
			DataInputStream dataInputStream = new DataInputStream(request.getInputStream());
			
			int n = 0;
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while((n = dataInputStream.read()) != -1)
			{
				
				arrayOutputStream.write(n);
				
			}
			arrayOutputStream.flush();
			out.print("<textarea>"+arrayOutputStream.toString()+"<textarea/><br/>");
			arrayOutputStream.close();
			dataInputStream.close(); */
			
			
			/*  ParameterParser parser = new ParameterParser();
	         parser.setLowerCaseNames(true);
	            // Parameter parser can handle null input
	         Map<String, String> params = parser.parse("Content-Disposition: form-data; name=\"isDo\"", ';');
	            
	        out.println("demo parser:"+params);     */
			
			
			/* ServletFileUpload servletFileUpload = new ServletFileUpload(); 
			FileItemFactory fileItemFactory = new DiskFileItemFactory();
			servletFileUpload.setFileItemFactory(fileItemFactory);
			Map<String, List<FileItem>> map = servletFileUpload.parseParameterMap(request);
			out.print("map size:"+map.size());
			Iterator<String> iterator = map.keySet().iterator();
			
			while(iterator.hasNext())
			{
				String key = iterator.next();
				out.println("key:"+key+"<br/>");
				
				List<FileItem> lst = map.get(key);
				
				Iterator<FileItem> it = lst.iterator();
				while(it.hasNext())
				{
					FileItem fileItem = it.next();
					out.println("fileitem  classname:"+fileItem.getClass().getCanonicalName()+"<br/>");
					out.println("fileitem fieldName:"+fileItem.getFieldName()+":value:"+fileItem.getString()+"<br/>");
					
				}
				
				
			} */
			
			byte CR = 0x0D;

		    /**
		     * The Line Feed ASCII character value.
		     */
		    byte LF = 0x0A;

		    /**
		     * The dash (-) ASCII character value.
		     */
		   	byte DASH = 0x2D;
			
			
		  	 byte[] HEADER_SEPARATOR = {CR, LF, CR, LF};

		    /**
		     * A byte sequence that that follows a delimiter that will be
		     * followed by an encapsulation (<code>CRLF</code>).
		     */
		  	byte[] FIELD_SEPARATOR = {CR, LF};

		    /**
		     * A byte sequence that that follows a delimiter of the last
		     * encapsulation in the stream (<code>--</code>).
		     */
		   	byte[] STREAM_TERMINATOR = {DASH, DASH};

		    /**
		     * A byte sequence that precedes a boundary (<code>CRLF--</code>).
		     */
		  	byte[] BOUNDARY_PREFIX = {CR, LF, DASH, DASH};
			
			//首先要确认边界是什么
	        ParameterParser parser = new ParameterParser();
	        parser.setLowerCaseNames(true);
	        // Parameter parser can handle null input
	        Map<String, String> params = parser.parse(contentType, new char[] {';', ','});
	        String boundaryStr = params.get("boundary");

	        byte[] boundary;
	        try {
	            boundary = boundaryStr.getBytes("ISO-8859-1");
	        } catch (UnsupportedEncodingException e) {
	            boundary = boundaryStr.getBytes(); // Intentionally falls back to default charset
	        }
			
	        out.print("boundarystr:"+boundaryStr+"<br/>");
	        
	        //构造出一个实际流中的分隔符 
	        byte[] modifiedboundary = new byte[boundary.length + BOUNDARY_PREFIX.length];
	        System.arraycopy(BOUNDARY_PREFIX, 0, modifiedboundary, 0,
	                BOUNDARY_PREFIX.length);
	        
	        System.arraycopy(boundary, 0, modifiedboundary, BOUNDARY_PREFIX.length,
	                boundary.length);
	        
	        //最开始的分隔符是没有CRLF的
	        byte[]startBoundary = new byte[modifiedboundary.length-2];
	        System.arraycopy(modifiedboundary, 2, startBoundary, 0, modifiedboundary.length-2);
	        
	        
	        //准备一个大的二进制数组存储所有content内容
	        
	       	int contentTotalBytes = request.getContentLength();
	        byte contentBinArray[] = new byte[contentTotalBytes];
	        int i=0;
	        int j=0;
	        
	        out.println("contentlength:"+contentTotalBytes);
	        for(;i < contentTotalBytes;i += j)
	        {
	            try
	            {
	                j = request.getInputStream().read(contentBinArray,i,contentTotalBytes - i);
	            }
	            catch(Exception exception)
	            {
	               exception.printStackTrace();
	            }
	        }
	        
	        out.println("content:<textarea>"+new String(contentBinArray,"utf-8")+"</textarea>");
	        
	      //找到所有name中的位置 直接去取名字 然后crlfcrlf 然后到下一个crlf之间的就是值
	     String content = new String(contentBinArray,"utf-8");
	     int startpos = content.indexOf("orderid");
	   	 int endpos = content.indexOf('\r', startpos+"orderid".length());
	     while(content.charAt(endpos) == '\r' || content.charAt(endpos) == '\n')
	     {
	    	 endpos++;
	     }
		
	     String value = "";
	     while(content.charAt(endpos) != '\r' && content.charAt(endpos) != '\n')
	     {
	    	 value+=content.charAt(endpos);
	    	 endpos++;
	     }
	     out.print("value:"+value);
			
		}
%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>删除通行证</title>
</head>
<body>
	<form enctype="multipart/form-data" method="post">
	<input type="hidden" name="isDo" value="true" />
	<input type="text" name="orderid" value="sadaf"/>
	<input type="text" name="aaaa" value="dsaf"/>
	<input type="submit" name="aaaa" value="commit"/>
	
	</form>
</body>
</html>