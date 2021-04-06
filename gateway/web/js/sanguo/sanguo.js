	function checkRegister(){
		 if(window.document.fm.username.value.length<1){  
		    alert("请您填写用户通行证!");  
		 	document.fm.username.focus();  
		    return false;
		    }  
		 if(window.document.fm.password.value.length<1){  
		    alert("请您填写密码!");  
		 	document.fm.password.focus();  
		    return false;
		    }
		/* if(window.document.fm.passwordAgain.value.length<1){  
		    alert("请您填写二次密码!");  
		 	document.fm.passwordAgain.focus();  
		    return false;
			}

		if(window.document.fm.password.value != window.document.fm.passwordAgain.value){
		 	alert("您填写的两次密码不一致，请重新填写!");  
		 	document.fm.password.focus();  
		    return false;
			}  
			
		  if(window.document.fm.email.value.length<1){  
		    alert("请您填写邮箱号!");  
		 	document.fm.email.focus();  
		    return false;
			}
		    */
		    //验证用户名字符合法性  
		    var regx=/^\w{5,17}$/;  
		 if(!regx.test(document.fm.username.value)){  
		    alert("用户名长度在6~18之间，且只能包含字符、数字和下划线组成");  
		    document.fm.username.focus();  
		    return false;  
		 }  
		    //验证密码合法性  
		    var regxpwd=/^[a-zA-Z0-9]\w{5,17}$/;  
		 if(!regxpwd.test(document.fm.password.value)){  
		    alert("密码长度在6~18之间，且只能包含字符、数字和下划线组成");  
		    document.fm.password.focus();  
		    return false;  
		 }
		 /*
		 	var tegxeml = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		 if(!tegxeml.test(document.fm.email.value)){  
		    alert("email有误，请重新输入");  
		    document.fm.email.focus();  
		    return false;  
		 }		 */
		 window.document.fm.submit();  
		 return true; 
	}
	
	
		function checkLogin(){
		 if(window.document.fm.username.value.length<1){  
		    alert("请您填写用户通行证!");  
		 	document.fm.username.focus();  
		    return false;
		    }  
		 if(window.document.fm.password.value.length<1){  
		    alert("请您填写密码!");  
		 	document.fm.password.focus();  
		    return false;
		    }
		    
		    //验证用户名字符合法性  
		    var regx=/^\w{5,17}$/;  
		 if(!regx.test(document.fm.username.value)){  
		    alert("用户名长度在6~18之间，且只能包含字符、数字和下划线组成");  
		    document.fm.username.focus();  
		    return false;  
		 }  
		    //验证密码合法性  
		    var regxpwd=/^\w{5,17}$/;  
		 if(!regxpwd.test(document.fm.password.value)){  
		    alert("密码长度在6~18之间，且只能包含字符、数字和下划线组成");  
		    document.fm.password.focus();  
		    return false;  
		 }

		 window.document.fm.submit();  
		 return true; 
	}

	
		function checkUpdate(){
		 if(window.document.fm.username.value.length<1){  
		    alert("请您填写用户通行证!");  
		 	document.fm.username.focus();  
		    return false;
		    }  
		 if(window.document.fm.password.value.length<1){  
		    alert("请您填写密码!");  
		 	document.fm.password.focus();  
		    return false;
		    }
			
		 if(window.document.fm.newPassword.value.length<1){  
		    alert("请您填写新密码!");  
		 	document.fm.newPassword.focus();  
		    return false;
			}

		 if(window.document.fm.newPasswordAgain.value.length<1){  
		    alert("请您再次填入新密码!");  
		 	document.fm.newPasswordAgain.focus();  
		    return false;
			}
			
		  if(window.document.fm.newPassword.value != window.document.fm.newPasswordAgain.value){
		 	alert("您填写的两次新密码不一致，请重新填写!");  
		 	document.fm.password.focus();  
		    return false;
			}  
		    
		    //验证用户名字符合法性  
		    var regx=/^\w+$/;  
		 if(!regx.test(document.fm.username.value)){  
		    alert("用户名只能由數字、26個英文字母或者下劃線組成的字符串");  
		    document.fm.operator_name.focus();  
		    return false;  
		 }  
		    //验证密码合法性  
		    var regxpwd=/^[a-zA-Z]\w{5,17}$/;  
		 if(!regxpwd.test(document.fm.newPassword.value)){  
		    alert("密码只能以字母開頭，長度在6~18之间，且只能包含字符、數字和下劃線组成");  
		    document.fm.newPassword.focus();  
		    return false;  
		 }
		 //btnclick();
		 window.document.fm.submit();  
		 return true; 
	}


function getCookie(c_name)
{
    if (document.cookie.length>0)
    {
        var c_start=document.cookie.indexOf(c_name + "=");
        if (c_start!=-1)
        {
        	
            c_start=c_start + c_name.length+1;
            c_end=document.cookie.indexOf(";",c_start);
            if (c_end==-1) c_end=document.cookie.length;
            return unescape(document.cookie.substring(c_start,c_end));
        }
    }
    return "";
}

function setCookie(c_name,value,expiredays)
{
    var exdate=new Date();
    exdate.setDate(exdate.getDate()+expiredays);
    document.cookie=document.cookie+c_name+ "=" +escape(value)+((expiredays==null) ? "":";expires="+exdate.toUTCString());
}

//首先将天数转换为有效的日期，然后，将 cookie 名称、值及其过期日期存入 document.cookie 对象。

function checkCookie()
{
	alert("document.cookie:"+document.cookie);
    var username=getCookie('username');
    if (username!=null && username!="")
    {
        document.getElementById("username").value =username;
    }

}

function btnclick()
{
    var username=document.getElementById ("username").value ;
    setCookie('username',username,365);
}