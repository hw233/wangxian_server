<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>功能模块管理</title>
	<link rel="stylesheet" href="../css/style.css" />
	<script type="text/javascript" src="jquery.js"></script>
	<script type="text/javascript" src="json2.js"></script>
<script type="text/javascript">
   // document.domain="221.179.174.53:8888/game_boss";
   /* function check(){
    alert(1);
     var rid= document.getElementById("rid").value;
     var mid= document.getElementById("mid").value;
     //che.window.check(rid,mid);
      
		 $.post('http://221.179.174.53:8888/game_boss/check.sq',
			     {rid:rid,
				  mid:mid
				 },
				 function callback(json){
					alert(json);
					//var res =JSON.parse(decodeURI(json));
					//alert(res);
			        //alert(res.result);
				  },
				  "String");
	    var qsData = {'rid':rid,'mid':mid};
	   $.ajax({
		async:false,
		url: 'http://221.179.174.53:8888/game_boss/check.sq?action=getmsg&callback=?',
		type: "GET",
		dataType: 'jsonp',
		jsonp: 'callback',
		data: qsData,
		timeout: 5000,
		beforeSend: function(){
        alert("beforeSend");
  		},
		success: function (json) {
			alert(json);
			var res =JSON.parse(decodeURI(json));
			alert(res);
			alert(res.result);
		},
		complete: function(XMLHttpRequest, textStatus){
			alert("结束");
		},
		error: function(xhr){
		alert(" 请求出错(请检查相关度网络状况.)"+xhr.status+" ******** "+xhr.readySatate);
				}
	   });
	 
	   $.getJSON("http://221.179.174.53:8888/game_boss/check.sq?mid="+mid+"&rid="+rid+"&jsoncallback=?",function(json){
   					alert(json);
					//var res =JSON.parse(decodeURI(json));
					//alert(res);
			       // alert(res.result);
      });
	  }*/
	  function check(){
	  //alert(1);
	     var xmlHttp;     
         try{   
             // Firefox, Opera 8.0+, Safari   
             xmlHttp=new XMLHttpRequest();  
         }catch (e){     // Internet Explorer   
              try{  
                    xmlHttp=new ActiveXObject("Msxml2.XMLHTTP"); 
                 }catch (e){   
                 try{  
                     xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");  
                    }catch (e){    
                       alert("不支持AJAX");     
                      return false;    
                      }        
                }     
           }   
            var rid= document.getElementById("rid").value;
            var mid= document.getElementById("mid").value;
            var str = "http://221.179.174.53:8888/game_boss/check.sq";
            alert(xmlHttp);
             xmlHttp.onreadystatechange=function(){  
              if(xmlHttp.readyState==4){   
              alert(1);
                  //document.myForm.name.value=xmlHttp.ResponseText;   
                  alert(xmlHttp.responseText);
                 }  
                }
           xmlHttp.open("GET",str,true);
           xmlHttp.send(null);
	  
	  }	  
</script>
	</head>
	<body bgcolor="#FFFFFF">
		<!-- <iframe  width=0 height=0 src='http://221.179.174.53:8888/game_boss/gm/testCheck.html' id='che' ></iframe> -->
		<h1 align="d">
			测试检查权限
		</h1>
		<form>
			<table>
				<tr>
					<th>
						角色ID
					</th>
					<td>
						<input name='rid' id='rid' type='text' />
					</td>
				</tr>
				<tr>
					<th>
						模块ID
					</th>
					<td>
						<input name='mid' id='mid' type='text' />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="button" onclick="check();" value="检测" />
					</td>
				</tr>
			</table>

		</form>

	</body>
</html>
