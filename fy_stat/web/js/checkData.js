//没有超过返回true，超过返回false
function CheckLength(strInput,varMaxLength)
{       
	//字符串的实际长度
	var varRealLength = 0;
	
	//字符串长度最小单位,英文字符所占位数
	var varCount = 1;
	
	//中文字符集
	var strRegx = "/[\u4e00-\u9fa5]/"; 

	//逐个字符校验
	for (var i = 0; i < strInput.length; i++) 
	{
		re = eval(strRegx);
		if(re.test(strInput.charAt(i)) == true)                               
		{
			//如果是中文，所占位数为2
			varCount = 2;
		}
		else
		{
			//如果是英文，所占位数为1
			varCount = 1;
		}
		varRealLength += varCount;
	}
	//alert(varRealLength);
	//判断
	if(varMaxLength >= varRealLength)
	{
		return true;
	}
	else
	{
		return false;
	}           
}

//判断输入内容是否全部都是空格
//全部都是空格返回true，不全是返回false
function CheckBlank(strInput)
{		
	var re = / /g;
	var resultStr = strInput.replace(re,"");
	
	if(resultStr == "")
	{
		return true;
	}
	else
	{
		return false;
	}
}

//判断是否为整型，返回bool
function IsInteger(string ,sign) 
{  
	var integer; 
	if ((sign!=null) && (sign!='-') && (sign!='+')) 
	{ 
		alert('IsInter(string,sign)的参数出错：nsign为null或"-"或"+"'); 
		return false; 
	} 
	integer = parseInt(string); 
	if (isNaN(integer)) 
	{ 
		return false; 
	} 
	else if (integer.toString().length==string.length) 
	{  
		if ((sign==null) || (sign=='-' && integer<0) || (sign=='+' && integer>0)) 
		{ 
			return true; 
		} 
		else 
			return false;  
	} 
	else 
		return false; 
}

//短日期(形如: 2003-12-18)，返回bool
function IsDate(DateString) 
{ 
	if (DateString==null) return false; 
	var Dilimeter = '-'; 
	var tempy=''; 
	var tempm=''; 
	var tempd=''; 
	var tempArray; 
	if (DateString.length<8 && DateString.length>10) 
		return false;    
	tempArray = DateString.split(Dilimeter); 
	if (tempArray.length!=3) 
		return false; 
	if (tempArray[0].length==4) 
	{ 
		tempy = tempArray[0]; 
		tempm = tempArray[1]; 
		tempd = tempArray[2]; 
	} 
	else 
	{ 
		return false; 
	} 
	if(!IsInteger(tempy) || !IsInteger(tempm) || !IsInteger(tempd))
		return false; 
	
	var tDateString = tempy + '/'+tempm + '/'+tempd+' 8:0:0';//加八小时是因为我们处于东八区 
	var tempDate = new Date(tDateString); 
	if (isNaN(tempDate)) 
	{
		return false; 
	}
	if (((tempDate.getUTCFullYear()).toString()==tempy) && (tempDate.getMonth()==parseInt(tempm)-1) && (tempDate.getDate()==parseInt(tempd))) 
	{ 
		return true; 
	} 
	else 
	{ 
		return false; 
	} 
}
//比较两个日期字符串,endTime 大于等于 beginTime 返回true; 否则返回false;
function CompareTime(beginDate,endDate)
{
	var tBeginDate = beginDate.replace("-","/")+' 8:0:0';//加八小时是因为我们处于东八区
	tBeginDate = tBeginDate.replace("-","/");	
	var tEndDate = endDate.replace("-","/")+' 8:0:0';
	tEndDate = tEndDate.replace("-","/");
	
	var tbegin = new Date(tBeginDate); 
	var tend = new Date(tEndDate); 
	if (isNaN(tbegin) || isNaN(tend)) 
	{
		return false; 
	}
	else
	{
		if(tend < tbegin)
			return false;
	}
	return true;
}

//判断图片文件类型是否为gif、jpg，返回bool
function CheckPicFile(url) 
{  
	var extType = url.substring(url.indexOf(".")+1,url.length);
	extType = extType.toLowerCase();
	if(extType != "gif" && extType != "jpg")
	{
		return true;
	}
	else
	{
		return false;
	}
}

//判断图片文件类型是否为gif、jpg、png，返回bool
function CheckImgFile(url) 
{  
	var extType = url.substring(url.lastIndexOf(".")+1,url.length);
	extType = extType.toLowerCase();
	if(extType != "gif" && extType != "jpg" && extType != "png")
	{
		return true;
	}
	else
	{
		return false;
	}
}
//判断图片文件类型是否为gif、jpg  +  Flash文件类型是否swf，返回bool
function CheckPicAddFlashFile(url) 
{  
	var extType = url.substring(url.indexOf(".")+1,url.length);
	extType = extType.toLowerCase();
	if(extType != "gif" && extType != "jpg" && extType != "swf")
	{
		return true;
	}
	else
	{
		return false;
	}
}

//判断影音文件类型是否为mp3、wma、wmv、asf、rm、rmb、mpg、avi、swf，返回bool
function CheckMediaFile(url) 
{  
	var extType = url.substring(url.lastIndexOf(".")+1,url.length);
	extType = extType.toLowerCase();
	if(extType != "mp3" && extType != "wma" && extType != "wmv" && extType != "asf" && extType != "rm" && extType != "rmb" && extType != "mpg" && extType != "avi" && extType != "swf")
	{
		return true;
	}
	else
	{
		return false;
	}
}
//判断视频文件类型是否为wmv、asf、rm、rmvb、mpg、avi，返回bool
function CheckVideoFile(url) 
{  
	var extType = url.substring(url.lastIndexOf(".")+1,url.length);
	extType = extType.toLowerCase();
	if(extType != "wmv" && extType != "asf" && extType != "rm" && extType != "rmvb" && extType != "mpg" && extType != "avi")
	{
		return true;
	}
	else
	{
		return false;
	}
}

// 判断电子邮件是否格式正确

function is_email(object_name)
{
	var string;
	string=new String(object_name);
	var len=string.length;
	
	if(len==0)
	{
		return true;
	}
	else
	{			
		if (string.indexOf("@",1)==-1||string.indexOf(".",1)==-1||string.length<7)
		{
			return false;
		}

		if (string.charAt(len-1)=="."||string.charAt(len-1)=="@")
		{
			return false;
		}
		return true;
	}
}

//判断是否为中文，是返回true,否则返回false
function IsChinese(strInput)
{
	//中文字符集
	var strRegx = "/[\u4e00-\u9fa5]/"; 
	//逐个字符校验
	for (var i = 0; i < strInput.length; i++) 
	{
		re = eval(strRegx);
		if(re.test(strInput.charAt(i)) == true)
		{
			return true;
		}
	}
	
	return false
}

//去左空格;
function LTrim(s){
    return s.replace( /^\\s*/, "");
}
//去右空格;
function RTrim(s){
    return s.replace( /\\s*$/, "");
}
//去左右空格;
function Trim(s){
    return RTrim(LTrim(s));
}
function IsMail(strInput)//email不能为空或格式不正确！
{
	var tmp_str = Trim(strInput);
	var patrn=/^([a-zA-Z0-9._-]+)@([a-zA-Z0-9_-]+)(\.[a-zA-Z0-9_-]+)(\.*[a-zA-Z0-9_-]*)$/; 
	if(!patrn.exec(tmp_str)){
		return false;
	}	
	return true;
}

//手机号不能为空或格式不正确
function IsMobile(strInput)//(以13开头或者15开头的11数字)
{
	var tmp_str = Trim(strInput);
	//var patrn=/^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;//(以13开头的11数字)
	//var patrn=/^13\d{9}|159\d{8}|153\d{8}$/;//(以13开头，以159开头，以153开头的11数字)
	var patrn=/^(13|15)\d{9}$/;//(以13开头或者15开头的11数字)
	if(tmp_str.length != 11)
		return false;
	
	if(!patrn.exec(tmp_str)){
		return false;
	}else{
		return true;
	}
}

//是否为英文字符串BEGIN
function IsCharsInBagEn (s, bag) 
{ 
	var i,c; 
	for (i = 0; i < s.length; i++) 
	{ 
		c = s.charAt(i);//字符串s中的字符 
		if (bag.indexOf(c) <0) 
		return c; 
	} 
	return ""; 
} 
function IsEnglish(s) 
{ 
	var errorChar; 
	var badChar = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; 
	errorChar = IsCharsInBagEn( s, badChar) 
	if (errorChar != "" ) 
		return false; 
		
	return true; 
} 
//是否为英文字符串END

//用户输入用户名字符是否合适，只能输入字母、数字、下划线
function IsCharacterTrue(txtUserName)
{
	var checkOKM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
	var checkStrM = txtUserName;
	var allValidM = true;
	for (i = 0;  i < checkStrM.length;  i++)
	{
		ch = checkStrM.charAt(i);
		for (j = 0;  j < checkOKM.length;  j++)
		if (ch == checkOKM.charAt(j))
			break;
		if (j == checkOKM.length)
		{
			allValidM = false;
			break;
		}
	}
	return allValidM;
}