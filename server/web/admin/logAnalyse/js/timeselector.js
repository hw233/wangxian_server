//add by lili 2009-04-17
function initYearMonthDay() 
{ 

        var form1=document.forms[0]; 
        timeC = new Date(); 
        var curYear = timeC.getFullYear() ; 
                  
        form1.sYear.options[0] = new Option(curYear-1,curYear-1); 
        form1.eYear.options[0] = new Option(curYear-1,curYear-1); 
              
        form1.sYear.options[1] = new Option(curYear,curYear); 
		 form1.sYear.options[1].selected = true; 
        form1.eYear.options[1] = new Option(curYear,curYear); 
		 form1.eYear.options[1].selected = true; 
      
                    
        form1.sYear.options[2] = new Option(curYear+1,curYear+1); 
        form1.eYear.options[2] = new Option(curYear+1,curYear+1);      
       
        initMonch(); 

        initDaych(); 
		
}  
//add by lili 2009-04-15
function initYear() 
{ 

        var form1=document.forms[0]; 
        timeC = new Date(); 
        var curYear = timeC.getFullYear() ; 
                  
        form1.sYear.options[0] = new Option(curYear-1,curYear-1); 
        form1.eYear.options[0] = new Option(curYear-1,curYear-1); 
              
        form1.sYear.options[1] = new Option(curYear,curYear); 
		 form1.sYear.options[1].selected = true; 
        form1.eYear.options[1] = new Option(curYear,curYear); 
		 form1.eYear.options[1].selected = true; 
      
                    
        form1.sYear.options[2] = new Option(curYear+1,curYear+1); 
        form1.eYear.options[2] = new Option(curYear+1,curYear+1);      
       
        initMonch(); 

        initDaych(); 
		
		initHourch();
		
}  
function initMonch() 
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
function initDaych() 
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
function initHourch() 
{ 

        var form1=document.forms[0]; 
        var day=new Date(); 
        var curHour=day.getHours(); 
		 for (var i=0;i<24;i++){ 	
                form1.sHour.options[i] = new Option(i, i); 
          
        } 
		
        for (var i=0;i<25;i++){ 	
          
                form1.eHour.options[i] = new Option(i, i); 
        } 
        form1.sHour.options[0].selected = true; 
        form1.eHour.options[curHour+1].selected = true; 

}
//add by lili
function initHour() 
{ 
        var form1=document.forms[0]; 
        var day=new Date(); 
        var curHour=day.getHours(); 
  
		 for (var i=0;i<24;i++){ 	
                form1.sHour.options[i] = new Option(i, i); 
          
        } 
        for (var i=0;i<24;i++){ 	
          
                form1.eHour.options[i] = new Option(i, i); 
        } 
        form1.sHour.options[0].selected = true; 
        form1.eHour.options[curHour+1].selected = true; 
        initMinute();
        initSecond();
        
}
function initMinute() 
{ 

        var form1=document.forms[0]; 
        var day=new Date(); 
        var curMinutes=day.getMinutes(); 
    
	for (var i=0;i<60;i++){ 	
                form1.sMinutes.options[i] = new Option(i, i); 
          
        } 
        for (var i=0;i<60;i++){ 	
          
                form1.eMinutes.options[i] = new Option(i, i); 
        } 
        form1.sMinutes.options[0].selected = true; 
        form1.eMinutes.options[curMinutes+1].selected = true; 

}
//ÐÂÔö´øÃë
function initHour1() 
{ 

        var form1=document.forms[0]; 
        var day=new Date(); 
        var curHour=day.getHours(); 
		 for (var i=0;i<24;i++){ 	
                form1.sHour.options[i] = new Option(i, i); 
          
        } 
		
        for (var i=0;i<24;i++){ 	
          
                form1.eHour.options[i] = new Option(i, i); 
        } 
        form1.sHour.options[0].selected = true; 
        form1.eHour.options[curHour+1].selected = true; 
        
        initMinute();
	initSecond();
}

function initSecond() 
{ 

        var form1=document.forms[0]; 
        	
        form1.sSecond.options[0] = new Option(00, '00'); 
        
        form1.eSecond.options[0] = new Option(00, '00'); 
        form1.eSecond.options[1] = new Option(59, '59');
       
        form1.sSecond.options[0].selected = true; 
        form1.eSecond.options[0].selected = true; 

}
function subm_second()
{
  var thisform = document.forms[0];
	
  sTime = thisform.sHour.value +":" +
	        thisform.sMinutes.value +":00";
						
  eTime = thisform.eHour.value +":" +
	        thisform.eMinutes.value +":00";
			 
	 thisform.StartTime.value = sTime;
	 thisform.EndTime.value = eTime;
	  return true;
}
function subm_second1()
{
  var thisform = document.forms[0];
	
  sTime = thisform.sHour.value +":" +
	        thisform.sMinutes.value +":00";
						
  eTime = thisform.eHour.value +":" +
	        thisform.eMinutes.value +":00";
			 
	 thisform.StartTime.value = sTime;
	 thisform.EndTime.value = eTime;
	
}
function subm_second2()
{
  var thisform = document.forms[0];
	
  sTime = thisform.sHour.value +":" +
	        thisform.sMinutes.value +":" + 
	        thisform.sSecond.value;
						
  eTime = thisform.eHour.value +":" +
	        thisform.eMinutes.value +":" + 
	        thisform.eSecond.value;
			 
	 thisform.StartTime.value = sTime;
	 thisform.EndTime.value = eTime;

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
	 thisform.StartTime.value = sTime;
	 thisform.EndTime.value = eTime;
	  return true;
    
}
function subyearmonthday()
{
	var thisform = document.forms[0];
	
    sTime = thisform.sYear.value +"-" +
	        thisform.sMonth.value +"-" + 
	        thisform.sDay.value ;
	       
     eTime = thisform.eYear.value +"-" + 
	         thisform.eMonth.value +"-" +
			 thisform.eDay.value ;

	 thisform.StartTime.value = sTime;
	 thisform.EndTime.value = eTime;

	  
	  return true;
    
}
