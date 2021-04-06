	
	//给url地址增加时间戳，不读取缓存    
	function convertURL(url) {    
	        //获取时间戳
	        var timstamp = (new Date()).valueOf();    
	        //将时间戳信息拼接到url上    
	        //url = "AJAXServer"
	        if (url.indexOf("?") >= 0) {
	            url = url + "&t=" + timstamp;    
	        } else {
	            url = url + "?t=" + timstamp;    
	        }
	        return url;
	}
	
	function query(){

		obj = document.getElementById('gmSelect'); 
		var index = obj.selectedIndex; // 选中索引
		var str = obj.options[index].text; // 选中文本

		document.getElementById('gm').value = str;

		document.feedback.submit();

	}
	
	
	function submit(bln){

		 obj = document.getElementById('gmSelect'); 
		 var index = obj.selectedIndex; // 选中索引
		 var str = obj.options[index].text; // 选中文本

		 document.getElementById('gm').value = str;
		 
		
		var numSt = document.getElementById("hiddenNum").value;
		var num = parseInt(numSt);
		if(bln){
			document.getElementById("hiddenNum").value = (num-1);
			document.feedback.submit();

		}else{
			document.getElementById("hiddenNum").value = (num+1);
			document.feedback.submit();

		}

	}
	
	function showOne(){
		document.getElementById('small').style.display='block';
		document.getElementById('fade').style.display='block';
		
	}

	function closeWindow(){
		document.getElementById('small').style.display='none';
		document.getElementById('fade').style.display='none';
	}
	
	
	function ajax(path,id){
		
		document.getElementById('ajaxfeedbackId').value = id;
		var xmlHttp;
		try
		   {
		  // Firefox, Opera 8.0+, Safari
		   xmlHttp=new XMLHttpRequest();
		   }
		catch (e)
		   {
		 // Internet Explorer
		  try
		     {
		     xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
		     }
		  catch (e)
		     {
		     try
		        {
		        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		        }
		     catch (e)
		        {
		        alert("您的浏览器不支持AJAX！");
		        return false;
		        }
		     }
		   }
		
		xmlHttp.onreadystatechange=function()
		{
		if(xmlHttp.readyState==4)
		  {
			showOne();
			var str = xmlHttp.responseText;
			var a = str.split("||");
			document.getElementById("left").innerHTML =a[0];
			document.getElementById("right").innerHTML = a[1];
		  }
		}
		var url = path+"/admin/feedback/ajaxQueryFeedback.jsp?id="+id;
		xmlHttp.open("GET",convertURL(url),true);
		xmlHttp.send(null);
		
	}
	
	
	function sendjudge(){
		document.getElementById("feedbackId2").value = document.getElementById("ajaxfeedbackId").value;
		document.judge.submit();
	}
	
	function closeFeedback(){
		document.getElementById("feedbackId3").value = document.getElementById("ajaxfeedbackId").value;
		document.closeFeedback.submit();
	}
	
	function replyPlayer(){
		document.getElementById("feedbackId1").value = document.getElementById("ajaxfeedbackId").value;
		document.reply.submit();
	}