function IsDate(DateString , Dilimeter)
{
if (DateString==null) return false;
if (Dilimeter=='' || Dilimeter==null)
Dilimeter = '-';
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
tempd = tempArray[2];
}
else
{
tempy = tempArray[2];
tempd = tempArray[1];
}
tempm = tempArray[1];
var tDateString = tempy + '/'+tempm + '/'+tempd+' 8:0:0';//加八小时是因为我们处于东八区
var tempDate = new Date(tDateString);
if (isNaN(tempDate))
return false;


if (tempm=='07') {
  tempm='7';
}
if (tempm=='08') {
  tempm='8';
}
if (tempm=='09') {
  tempm='9';
}
if (tempd=='07') {
  tempd='7';
}
if (tempd=='08') {
  tempd='8';
}
if (tempd=='09') {
  tempd='9';
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


function IsDateTime(str){ 
var reg = /^(d+)-(d{1,2})-(d{1,2}) (d{1,2}):(d{1,2}):(d{1,2})$/; 
var r = str.match(reg); 
if(r==null)return false; 
r[2]=r[2]-1; 
var d= new Date(r[1], r[2],r[3], r[4],r[5], r[6]); 
if(d.getFullYear()!=r[1])return false; 
if(d.getMonth()!=r[2])return false; 
if(d.getDate()!=r[3])return false; 
if(d.getHours()!=r[4])return false; 
if(d.getMinutes()!=r[5])return false; 
if(d.getSeconds()!=r[6])return false; 
return true; 
} 



 function subm()
  {
    var thisform = document.forms[0];
	
	
	 
	
    sTime = thisform.sYear.value +"-" +
	        thisform.sMonth.value +"-" + 
	        thisform.sDay.value +" " +
	        thisform.sHour.value + ":00:00";
			
	 if (thisform.eHour.value == 24 )
	 {
	   eTime = thisform.eYear.value +"-" + 
	         thisform.eMonth.value +"-" +
			 thisform.eDay.value +" " + 
			 "23:59:59"; 
	   } 
	   else
	   { 				
     eTime = thisform.eYear.value +"-" + 
	         thisform.eMonth.value +"-" +
			 thisform.eDay.value +" " + 
			 thisform.eHour.value + ":00:00";
		}	 
	 thisform.StartDate.value = sTime;
	 thisform.EndDate.value = eTime;

	  
	  return true;
      
  }
  
  
  
 function subm_date()
  {
    var thisform = document.forms[0];
	
	
	 
	
    sTime = thisform.sYear.value +"-" +
	        thisform.sMonth.value +"-" + 
	        thisform.sDay.value ;
	       
     eTime = thisform.eYear.value +"-" + 
	         thisform.eMonth.value +"-" +
			 thisform.eDay.value ;

	 thisform.StartDate.value = sTime;
	 thisform.EndDate.value = eTime;

	  
	  return true;
      
  }
function populate(type)  
{ 
	var objYear, objDay, objMonth; 
	var frm = document.forms[0]; 
	if (type == 1){ 
		objYear = frm.sYear; 
		objMonth = frm.sMonth; 
		objDay = frm.sDay; 
	} 
	else{ 
		objYear = frm.eYear; 
                objMonth = frm.eMonth; 
                objDay = frm.eDay; 
	} 
        timeA = new Date(objYear.options[objYear.selectedIndex].text, objMonth.options[objMonth.selectedIndex].value,1); 
        timeDifference = timeA - 86400000; 
        timeB = new Date(timeDifference);
        var daysInMonth = timeB.getDate();
        for (var i = 0; i < objDay.length; i++)
                objDay.options[i] = null;
        for ( i = 0; i < daysInMonth; i++)
                objDay.options[i] = new Option(i+1, i+1);
        objDay.options[0].selected = true;
 }
 
function initDate() 
{ 

        var form1=document.forms[0]; 
        timeC = new Date(); 
        var curYear = timeC.getFullYear() ; 
       
       form1.sYear.options[0] = new Option(curYear-1,curYear-1); 
	//	 form1.sYear.options[0].selected = true; 
        form1.eYear.options[0] = new Option(curYear-1,curYear-1); 
	//	 form1.eYear.options[0].selected = true;
        
                
        form1.sYear.options[1] = new Option(curYear,curYear); 
		 form1.sYear.options[1].selected = true; 
        form1.eYear.options[1] = new Option(curYear,curYear); 
		 form1.eYear.options[1].selected = true; 
      
                    
        form1.sYear.options[2] = new Option(curYear+1,curYear+1); 
	//	 form1.sYear.options[1].selected = true; 
        form1.eYear.options[2] = new Option(curYear+1,curYear+1); 
	//	 form1.eYear.options[1].selected = true;       
       
        initMon(); 

        initDay(); 
				
} 
function initYear() 
{ 

        var form1=document.forms[0]; 
        timeC = new Date(); 
        var curYear = timeC.getFullYear() ; 
       
       form1.sYear.options[0] = new Option(curYear-1,curYear-1); 
	//	 form1.sYear.options[0].selected = true; 
        form1.eYear.options[0] = new Option(curYear-1,curYear-1); 
	//	 form1.eYear.options[0].selected = true;
        
                
        form1.sYear.options[1] = new Option(curYear,curYear); 
		 form1.sYear.options[1].selected = true; 
        form1.eYear.options[1] = new Option(curYear,curYear); 
		 form1.eYear.options[1].selected = true; 
      
                    
        form1.sYear.options[2] = new Option(curYear+1,curYear+1); 
	//	 form1.sYear.options[1].selected = true; 
        form1.eYear.options[2] = new Option(curYear+1,curYear+1); 
	//	 form1.eYear.options[1].selected = true;       
       
        initMon(); 

        initDay(); 
		
		initHour();
		
} 

function initMon() 
{ 
        var form1=document.forms[0]; 
        var day=new Date(); 
        var curMon=day.getMonth(); 
        for (var i=0;i<12;i++){ 
				form1.sMonth.options[i] = new Option(i+1, i+1); 
                form1.eMonth.options[i] = new Option(i+1, i+1); 
        } 
        form1.sMonth.options[curMon].selected = true; 
        form1.eMonth.options[curMon].selected = true; 
		
} 
function initDay() 
{ 
        var form1=document.forms[0]; 
        var day=new Date(); 
        var curDay = day.getDate (); 
        for (var i=0;i<31;i++){ 
                form1.sDay.options[i] = new Option(i+1, i+1); 
                form1.eDay.options[i] = new Option(i+1, i+1); 
        } 
        form1.sDay.options[curDay-1].selected = true;  //today 
        form1.eDay.options[curDay-1].selected = true;  //today 
	
} 
function initHour() 
{ 

        var form1=document.forms[0]; 
        var day=new Date(); 
        var curHour=day.getHours(); 
        // dawning  add 24:00 2004.4.23
        // for (var i=0;i<24;i++){ 
		 for (var i=0;i<24;i++){ 	
                form1.sHour.options[i] = new Option(i, i); 
          
        } 
		
        for (var i=0;i<25;i++){ 	
          
                form1.eHour.options[i] = new Option(i, i); 
        } 
        form1.sHour.options[0].selected = true; 
        form1.eHour.options[curHour+1].selected = true; 

}


function initDay2() 
{ 
        var form1=document.forms[0]; 
        var day=new Date(); 
        var curDay = day.getDate (); 
        for (var i=0;i<31;i++){ 
                form1.sDay.options[i] = new Option(i+1, i+1); 
                form1.eDay.options[i] = new Option(i+1, i+1); 
        } 
        form1.sDay.options[0].selected = true;  //  first day
        form1.eDay.options[curDay-1].selected = true;  //today 
	
} 
  
function initYear2() 
{ 

        var form1=document.forms[0]; 
        timeC = new Date(); 
        var curYear = timeC.getFullYear() ; 
        
                
        form1.sYear.options[0] = new Option(curYear-1,curYear-1); 
	//	 form1.sYear.options[0].selected = true; 
        form1.eYear.options[0] = new Option(curYear-1,curYear-1); 
	//	 form1.eYear.options[0].selected = true;
        
                
        form1.sYear.options[1] = new Option(curYear,curYear); 
		 form1.sYear.options[1].selected = true; 
        form1.eYear.options[1] = new Option(curYear,curYear); 
		 form1.eYear.options[1].selected = true; 
      
                    
        form1.sYear.options[2] = new Option(curYear+1,curYear+1); 
	//	 form1.sYear.options[1].selected = true; 
        form1.eYear.options[2] = new Option(curYear+1,curYear+1); 
	//	 form1.eYear.options[1].selected = true;      
       
        initMon(); 

        initDay2(); 
		
		initHour();
		
}  
  
  
function initYear3() 
{

        var form1=document.forms[0]; 
        timeC = new Date(); 
        var curYear = timeC.getFullYear() ; 
       
       form1.sYear.options[0] = new Option(curYear-1,curYear-1); 
	//	 form1.sYear.options[0].selected = true; 
        form1.eYear.options[0] = new Option(curYear-1,curYear-1); 
	//	 form1.eYear.options[0].selected = true;
        
                
        form1.sYear.options[1] = new Option(curYear,curYear); 
		 form1.sYear.options[1].selected = true; 
        form1.eYear.options[1] = new Option(curYear,curYear); 
		 form1.eYear.options[1].selected = true; 
      
                    
        form1.sYear.options[2] = new Option(curYear+1,curYear+1); 
	//	 form1.sYear.options[1].selected = true; 
        form1.eYear.options[2] = new Option(curYear+1,curYear+1); 
	//	 form1.eYear.options[1].selected = true;       
       
        initMon(); 

        initDay2(); 
		
	
	
}
  
//------------------------------------------------------------
function initYear4() 
{

        var form1=document.forms[0]; 
        timeC = new Date(); 
        var curYear = timeC.getFullYear() ; 
       
       form1.sYear.options[0] = new Option(curYear-1,curYear-1); 
               
        form1.sYear.options[1] = new Option(curYear,curYear);
        
		 form1.sYear.options[1].selected = true; 
		        
        form1.sYear.options[2] = new Option(curYear+1,curYear+1);
         initMon1(); 
       
	
} 

function initMon1() 
{ 
        var form1=document.forms[0]; 
        var day=new Date(); 
        var curMon=day.getMonth(); 
        for (var i=0;i<12;i++){ 
				form1.sMonth.options[i] = new Option(i+1, i+1); 
                } 
        form1.sMonth.options[curMon].selected = true; 
      	
} 

function submitym()
  {
    var thisform = document.forms[0];
	
    sTime = thisform.sYear.value +"-" +
	        thisform.sMonth.value ;
	               
	 thisform.StartDate.value = sTime;

	  return true;
      
  } 
//-----------------------------------------------------------

function changeitem(selection1, selection2) {
	var users = selection1;
	var engineer = selection2;
	
	var k = selection2.options.length;

	var length = selection1.options.length;    

	for(i = 0; i < length; i ++) {
        //alert("hello " + i);
		if (selection1.options[i].selected){
			//array[k]=users.options[i];
             
			selection2.options.length = selection2.options.length + 1;
			selection2.options[k].text=selection1.options[i].text;
            selection2.options[k].value=selection1.options[i].value;		
            k ++;
            selection1.options[i] = null;
            //alert("end");
            i--;
            length--;
		}
	}
    //history.go(0);
   
	
}


function trim(s){ 
return s.replace(/^\s+|\s+$/, ''); 
} 

function ltrim(s){ 
return s.replace(/^\s+/, ''); 
} 


function rtrim(s){ 
return s.replace(/\s+$/, ''); 
} 

/*
function trim(s) 
{
  // Remove leading spaces and carriage returns
  
  while ((s.substring(0,1) == ' ') || (s.substring(0,1) == '\n') || (s.substring(0,1) == '\r'))
  {
    s = s.substring(1,s.length);
  }

  // Remove trailing spaces and carriage returns

  while ((s.substring(s.length-1,s.length) == ' ') || (s.substring(s.length-1,s.length) == '\n') || (s.substring(s.length-1,s.length) == '\r'))
  {
    s = s.substring(0,s.length-1);
  }
  return s;
}
*/



  //功能：扩展日期函数，支持YYYY-MM-DD或YYYY-MMDD hh:mm:ss字符串格式
  //返回：如果正确，则返回javascript中支持UTC日期格式，如果错误，则返回false  
  //日期：2004-12-15
  //举例： var myDate = Date_Ex("2004-12-21 23:01:00"); //返回正确的日期
  //       var myDate = Date_Ex("2004-12-21");     //返回正确的日期
  //       var myDate = Date_Ex("2004-23-12 12:60:29");//返回false，且提示日期或时间超出有效范围
  function Date_Ex(value1)
  {
 var strDate = value1;
 if (strDate.length == 0)
  return false;

 //先判断是否为短日期格式：YYYY-MM-DD，如果是，将其后面加上00:00:00，转换为YYYY-MM-DD hh:mm:ss格式
 var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})/;   //短日期格式的正则表达式
 var r = strDate.match(reg);

 if (r != null)   //说明strDate是短日期格式，改造成长日期格式
   strDate = strDate + " 00:00:00";
 
 reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})/;
 r = strDate.match(reg);
 if (r == null)
 {
  alert("你输入的日期格式有误，正确格式为：2004-12-01 或 2004-12-01 12:23:45");
  return false;
 }

 var d = new Date(r[1], r[3]-1,r[4],r[5],r[6],r[7]);
 if (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]&&d.getHours()==r[5]&&d.getMinutes()==r[6]&&d.getSeconds()== r[7])
 {
  return d;
 }
 else
 {
  alert("你输入的日期或时间超出有效范围，请仔细检查！");
  return false;
 }
  }

