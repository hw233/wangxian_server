 function openScript(url, width, height){
 var Win = window.open(url,"openScript",'width=' + width + ',height=' + height + ',resizable=1,scrollbars=yes,menubar=no,status=yes' );}

var bMoveable=true;
var _VersionInfo="Version:2.0"

var strFrame;
document.writeln('<iframe id=meizzDateLayer Author=wayx frameborder=0 style="position: absolute; width: 144px; height: 173px; z-index: 9998; display:none"></iframe>');
strFrame='<style>';
strFrame+='INPUT.button{BORDER-RIGHT: #448FEB 1px solid;BORDER-TOP: #448FEB 1px solid;BORDER-LEFT: #448FEB 1px solid;';
strFrame+='BORDER-BOTTOM: #448FEB 1px solid;BACKGROUND-COLOR: #fff8ec;font-family:宋体;}';
strFrame+='TD{FONT-SIZE: 12px;font-family:宋体;}';
strFrame+='</style>';
strFrame+='<scr' + 'ipt>';
strFrame+='var datelayerx,datelayery; /*存放日历控件的鼠标位置*/';
strFrame+='var bDrag; /*标记是否开始拖动*/';
strFrame+='function document.onmousemove() /*在鼠标移动事件中，如果开始拖动日历，则移动日历*/';
strFrame+='{if(bDrag && window.event.button==1)';
strFrame+=' {var DateLayer=parent.document.all.meizzDateLayer.style;';
strFrame+='  DateLayer.posLeft += window.event.clientX-datelayerx;/*由于每次移动以后鼠标位置都恢复为初始的位置，因此写法与div中不同*/';
strFrame+='  DateLayer.posTop += window.event.clientY-datelayery;}}';
strFrame+='function DragStart()  /*开始日历拖动*/';
strFrame+='{var DateLayer=parent.document.all.meizzDateLayer.style;';
strFrame+=' datelayerx=window.event.clientX;';
strFrame+=' datelayery=window.event.clientY;';
strFrame+=' bDrag=true;}';
strFrame+='function DragEnd(){  /*结束日历拖动*/';
strFrame+=' bDrag=false;}';
strFrame+='</scr' + 'ipt>';
strFrame+='<div style="z-index:9999;position: absolute; left:0; top:0;" onselectstart="return false"><span id=tmpSelectYearLayer Author=wayx style="z-index: 9999;position: absolute;top: 3; left: 19;display: none"></span>';
strFrame+='<span id=tmpSelectMonthLayer Author=wayx style="z-index: 9999;position: absolute;top: 3; left: 78;display: none"></span>';
strFrame+='<table border=1 cellspacing=0 cellpadding=0 width=142 height=173 bordercolor=#448FEB bgcolor=#448FEB Author="wayx">';
strFrame+='  <tr Author="wayx"><td width=142 height=23 Author="wayx" bgcolor=#FFFFFF><table border=0 cellspacing=1 cellpadding=0 width=140 Author="wayx" height=23>';
strFrame+='      <tr align=center Author="wayx"><td width=16 align=center bgcolor=#448FEB style="font-size:12px;cursor: hand;color: #ffffff" ';
strFrame+='        onclick="parent.meizzPrevM()" title="向前翻 1 月" Author=meizz><b Author=meizz><';
strFrame+='        </td><td width=60 align=center style="background-color:#c5dfff;font-size:12px;cursor:default" Author=meizz ';
strFrame+='onmouseover="style.color=\'#333\';style.backgroundColor=\'#63A5F7\'" onmouseout="style.backgroundColor=\'#c5dfff\'" ';
strFrame+='onclick="parent.tmpSelectYearInnerHTML(this.innerText.substring(0,4))" title="点击这里选择年份"><span Author=meizz id=meizzYearHead></span></td>';
strFrame+='<td width=48 align=center style="background-color:#c5dfff;font-size:12px;cursor:default" Author=meizz onmouseover="style.color=\'#333\';style.backgroundColor=\'#63A5F7\'" ';
strFrame+=' onmouseout="style.backgroundColor=\'#c5dfff\'" onclick="parent.tmpSelectMonthInnerHTML(this.innerText.length==3?this.innerText.substring(0,1):this.innerText.substring(0,2))"';
strFrame+='        title="点击这里选择月份"><span id=meizzMonthHead Author=meizz></span></td>';
strFrame+='        <td width=16 bgcolor=#448FEB align=center style="font-size:12px;cursor: hand;color: #ffffff" ';
strFrame+='         onclick="parent.meizzNextM()" title="向后翻 1 月" Author=meizz><b Author=meizz>></td></tr>';
strFrame+='    </table></td></tr>';
strFrame+='  <tr Author="wayx"><td width=142 height=18 Author="wayx">';
strFrame+='<table border=1 cellspacing=0 cellpadding=0 bgcolor=#448FEB ' + (bMoveable? 'onmousedown="DragStart()" onmouseup="DragEnd()"':'');
strFrame+=' BORDERCOLORLIGHT=#448FEB BORDERCOLORDARK=#FFFFFF width=140 height=20 Author="wayx" style="cursor:' + (bMoveable ? 'move':'default') + '">';
strFrame+='<tr Author="wayx" align=center valign=bottom><td style="font-size:12px;color:#FFFFFF" Author=meizz>日</td>';
strFrame+='<td style="font-size:12px;color:#FFFFFF" Author=meizz>一</td><td style="font-size:12px;color:#FFFFFF" Author=meizz>二</td>';
strFrame+='<td style="font-size:12px;color:#FFFFFF" Author=meizz>三</td><td style="font-size:12px;color:#FFFFFF" Author=meizz>四</td>';
strFrame+='<td style="font-size:12px;color:#FFFFFF" Author=meizz>五</td><td style="font-size:12px;color:#FFFFFF" Author=meizz>六</td></tr>';
strFrame+='</table></td></tr><!-- Author:F.R.Huang(meizz) http://www.meizz.com/ mail: meizz@hzcnc.com 2002-10-8 -->';
strFrame+='  <tr Author="wayx"><td width=142 height=120 Author="wayx">';
strFrame+='    <table border=1 cellspacing=2 cellpadding=0 BORDERCOLORLIGHT=#448FEB BORDERCOLORDARK=#FFFFFF bgcolor=#fff8ec width=140 height=120 Author="wayx">';
var n=0; for (j=0;j<5;j++){ strFrame+= ' <tr align=center Author="wayx">'; for (i=0;i<7;i++){
strFrame+='<td width=20 height=15 id=meizzDay'+n+' style="font-size:12px" Author=meizz onclick=parent.meizzDayClick(this.innerText,0)></td>';n++;}
strFrame+='</tr>';}
strFrame+='      <tr align=center Author="wayx">';
for (i=35;i<39;i++)strFrame+='<td width=20 height=15 id=meizzDay'+i+' style="font-size:12px" Author=wayx onclick="parent.meizzDayClick(this.innerText,0)"></td>';
strFrame+='        <td colspan=3 align=right ><span onclick=parent.closeLayer() style="font-size:12px;cursor: hand"';
strFrame+='         ><u>关闭</u></span> </td></tr>';
strFrame+='    </table></td></tr><tr Author="wayx"><td Author="wayx">';
strFrame+='</td></tr></table></div>';

window.frames.meizzDateLayer.document.writeln(strFrame);
window.frames.meizzDateLayer.document.close();

var outObject;
var outButton; 
var outDate="";
var odatelayer=window.frames.meizzDateLayer.document.all;
function selectDate(tt,obj)
{
 document.getElementById('meizzDateLayer2').style.display="none";
 if (arguments.length >  2){alert("对不起!传入本控件的参数太多!");return;}
 if (arguments.length == 0){alert("对不起!您没有传回本控件任何参数!");return;}
 var dads  = document.all.meizzDateLayer.style;
 var th = tt;
 var ttop  = tt.offsetTop; 
 var thei  = tt.clientHeight;
 var tleft = tt.offsetLeft;  
 var ttyp  = tt.type;
 while (tt = tt.offsetParent){ttop+=tt.offsetTop; tleft+=tt.offsetLeft;}
 dads.top  = (ttyp=="image")? ttop+thei : ttop+thei+6;
 dads.left = tleft;
 outObject = (arguments.length == 1) ? th : obj;
 outButton = (arguments.length == 1) ? null : th;
 var reg = /^(\d+)-(\d{1,2})-(\d{1,2})$/; 
 var r = outObject.value.match(reg); 
 if(r!=null){
  r[2]=r[2]-1; 
  var d= new Date(r[1], r[2],r[3]); 
  if(d.getFullYear()==r[1] && d.getMonth()==r[2] && d.getDate()==r[3]){
   outDate=d; 
  }
  else outDate="";
   meizzSetDay(r[1],r[2]+1);
 }
 else{
  outDate="";
  meizzSetDay(new Date().getFullYear(), new Date().getMonth() + 1);
 }
 dads.display = '';

 event.returnValue=false;
}

var MonHead = new Array(12);        
    MonHead[0] = 31; MonHead[1] = 28; MonHead[2] = 31; MonHead[3] = 30; MonHead[4]  = 31; MonHead[5]  = 30;
    MonHead[6] = 31; MonHead[7] = 31; MonHead[8] = 30; MonHead[9] = 31; MonHead[10] = 30; MonHead[11] = 31;

var meizzTheYear=new Date().getFullYear(); 
var meizzTheMonth=new Date().getMonth()+1; 
var meizzWDay=new Array(39);               

function document.onclick()
{ 
  with(window.event)
  { if (srcElement.getAttribute("Author")==null && srcElement != outObject)
    closeLayer();
  }
}

function document.onkeyup() 
  {
    if (window.event.keyCode==27){
  if(outObject)outObject.blur();
  closeLayer();
 }
 else if(document.activeElement)
  if(document.activeElement.getAttribute("Author")==null && document.activeElement != outObject && document.activeElement != outButton)
  {
   closeLayer();
  }
  }

function meizzWriteHead(yy,mm) 
  {
 odatelayer.meizzYearHead.innerText  = yy + " 年";
    odatelayer.meizzMonthHead.innerText = mm + " 月";
  }

function tmpSelectYearInnerHTML(strYear)
{
  if (strYear.match(/\D/)!=null){alert("年份输入参数不是数字!");return;}
  var m = new Date().getFullYear();
  if (m < 1000 || m > 9999) {alert("年份值不在 1000 到 9999 之间!");return;}
  var n = m - 100;
  if (n < 1000) n = 1000;
  if (n + 120 > 9999) n = 9974;
  var s = "<select Author=meizz name=tmpSelectYear style='font-size: 12px' "
     s += "onblur='document.all.tmpSelectYearLayer.style.display=\"none\"' "
     s += "onchange='document.all.tmpSelectYearLayer.style.display=\"none\";"
     s += "parent.meizzTheYear = this.value; parent.meizzSetDay(parent.meizzTheYear,parent.meizzTheMonth)'>\r\n";
  var selectInnerHTML = s;
  for (var i = n; i < n + 120; i++)
  {
    if (i == m)
       {selectInnerHTML += "<option Author=wayx value='" + i + "' selected>" + i + "年" + "</option>\r\n";}
    else {selectInnerHTML += "<option Author=wayx value='" + i + "'>" + i + "年" + "</option>\r\n";}
  }
  selectInnerHTML += "</select>";
  odatelayer.tmpSelectYearLayer.style.display="";
  odatelayer.tmpSelectYearLayer.innerHTML = selectInnerHTML;
  odatelayer.tmpSelectYear.focus();
}

function tmpSelectMonthInnerHTML(strMonth)
{
  if (strMonth.match(/\D/)!=null){alert("月份输入参数不是数字!");return;}
  var m = (strMonth) ? strMonth : new Date().getMonth() + 1;
  var s = "<select Author=meizz name=tmpSelectMonth style='font-size: 12px' "
     s += "onblur='document.all.tmpSelectMonthLayer.style.display=\"none\"' "
     s += "onchange='document.all.tmpSelectMonthLayer.style.display=\"none\";"
     s += "parent.meizzTheMonth = this.value; parent.meizzSetDay(parent.meizzTheYear,parent.meizzTheMonth)'>\r\n";
  var selectInnerHTML = s;
  for (var i = 1; i < 13; i++)
  {
    if (i == m)
       {selectInnerHTML += "<option Author=wayx value='"+i+"' selected>"+i+"月"+"</option>\r\n";}
    else {selectInnerHTML += "<option Author=wayx value='"+i+"'>"+i+"月"+"</option>\r\n";}
  }
  selectInnerHTML += "</select>";
  odatelayer.tmpSelectMonthLayer.style.display="";
  odatelayer.tmpSelectMonthLayer.innerHTML = selectInnerHTML;
  odatelayer.tmpSelectMonth.focus();
}

function closeLayer()
  {
    document.all.meizzDateLayer.style.display="none";
	document.all.meizzDateLayer2.style.display="none";
  }

function IsPinYear(year)
  {
    if (0==year%4&&((year%100!=0)||(year%400==0))) return true;else return false;
  }

function GetMonthCount(year,month)
  {
    var c=MonHead[month-1];if((month==2)&&IsPinYear(year)) c++;return c;
  }
function GetDOW(day,month,year)
  {
    var dt=new Date(year,month-1,day).getDay()/7; return dt;
  }

function meizzPrevM()
  {
    if(meizzTheMonth>1){meizzTheMonth--}else{meizzTheYear--;meizzTheMonth=12;}
    meizzSetDay(meizzTheYear,meizzTheMonth);
  }
function meizzNextM()
  {
    if(meizzTheMonth==12){meizzTheYear++;meizzTheMonth=1}else{meizzTheMonth++}
    meizzSetDay(meizzTheYear,meizzTheMonth);
  }

function meizzSetDay(yy,mm)
{
  meizzWriteHead(yy,mm);
  meizzTheYear=yy;
  meizzTheMonth=mm;
  
  for (var i = 0; i < 39; i++){meizzWDay[i]=""};
  var day1 = 1,day2=1,firstday = new Date(yy,mm-1,1).getDay(); 
  for (i=0;i<firstday;i++)meizzWDay[i]=GetMonthCount(mm==1?yy-1:yy,mm==1?12:mm-1)-firstday+i+1
  for (i = firstday; day1 < GetMonthCount(yy,mm)+1; i++){meizzWDay[i]=day1;day1++;}
  for (i=firstday+GetMonthCount(yy,mm);i<39;i++){meizzWDay[i]=day2;day2++}
  for (i = 0; i < 39; i++)
  { var da = eval("odatelayer.meizzDay"+i) 
    if (meizzWDay[i]!="")
      { 
  da.borderColorLight="#448FEB";
  da.borderColorDark="#FFFFFF";
  if(i<firstday) 
  {
   da.innerHTML="<font color=gray>" + meizzWDay[i] + "</font>";
   da.title=(mm==1?12:mm-1) +"月" + meizzWDay[i] + "日";
   da.onclick=Function("meizzDayClick(this.innerText,-1)");
   if(!outDate)
    da.style.backgroundColor = ((mm==1?yy-1:yy) == new Date().getFullYear() && 
     (mm==1?12:mm-1) == new Date().getMonth()+1 && meizzWDay[i] == new Date().getDate()) ?
             "#FFD700":"#C5DFFF";
   else
   {
    da.style.backgroundColor =((mm==1?yy-1:yy)==outDate.getFullYear() && (mm==1?12:mm-1)== outDate.getMonth() + 1 && 
    meizzWDay[i]==outDate.getDate())? "#00ffff" :
    (((mm==1?yy-1:yy) == new Date().getFullYear() && (mm==1?12:mm-1) == new Date().getMonth()+1 && 
    meizzWDay[i] == new Date().getDate()) ? "#FFD700":"#C5DFFF");
    if((mm==1?yy-1:yy)==outDate.getFullYear() && (mm==1?12:mm-1)== outDate.getMonth() + 1 && 
    meizzWDay[i]==outDate.getDate())
    {
     da.borderColorLight="#FFFFFF";
     da.borderColorDark="#448FEB";
    }
   }
  }
  else if (i>=firstday+GetMonthCount(yy,mm))
  {
   da.innerHTML="<font color=gray>" + meizzWDay[i] + "</font>";
   da.title=(mm==12?1:mm+1) +"月" + meizzWDay[i] + "日";
   da.onclick=Function("meizzDayClick(this.innerText,1)");
   if(!outDate)
    da.style.backgroundColor = ((mm==12?yy+1:yy) == new Date().getFullYear() && 
     (mm==12?1:mm+1) == new Date().getMonth()+1 && meizzWDay[i] == new Date().getDate()) ?
             "#FFD700":"#C5DFFF";
   else
   {
    da.style.backgroundColor =((mm==12?yy+1:yy)==outDate.getFullYear() && (mm==12?1:mm+1)== outDate.getMonth() + 1 && 
    meizzWDay[i]==outDate.getDate())? "#00ffff" :
    (((mm==12?yy+1:yy) == new Date().getFullYear() && (mm==12?1:mm+1) == new Date().getMonth()+1 && 
    meizzWDay[i] == new Date().getDate()) ? "#FFD700":"#C5DFFF");
    if((mm==12?yy+1:yy)==outDate.getFullYear() && (mm==12?1:mm+1)== outDate.getMonth() + 1 && 
    meizzWDay[i]==outDate.getDate())
    {
     da.borderColorLight="#FFFFFF";
     da.borderColorDark="#448FEB";
    }
   }
  }
  else
  {
   da.innerHTML="" + meizzWDay[i] + "";
   da.title=mm +"月" + meizzWDay[i] + "日";
   da.onclick=Function("meizzDayClick(this.innerText,0)"); 
   if(!outDate)
    da.style.backgroundColor = (yy == new Date().getFullYear() && mm == new Date().getMonth()+1 && meizzWDay[i] == new Date().getDate())?
     "#FFD700":"#C5DFFF";
   else
   {
    da.style.backgroundColor =(yy==outDate.getFullYear() && mm== outDate.getMonth() + 1 && meizzWDay[i]==outDate.getDate())?
     "#00ffff":((yy == new Date().getFullYear() && mm == new Date().getMonth()+1 && meizzWDay[i] == new Date().getDate())?
     "#FFD700":"#C5DFFF");
    //将选中的日期显示为凹下去
    if(yy==outDate.getFullYear() && mm== outDate.getMonth() + 1 && meizzWDay[i]==outDate.getDate())
    {
     da.borderColorLight="#FFFFFF";
     da.borderColorDark="#448FEB";
    }
   }
  }
        da.style.cursor="hand"
      }
    else{da.innerHTML="";da.style.backgroundColor="";da.style.cursor="default"}
  }
}

function meizzDayClick(n,ex,f)  
{
  var yy=meizzTheYear;
  var mm = parseInt(meizzTheMonth)+ex; 
 if(mm<1){
  yy--;
  mm=12+mm;
 }
 else if(mm>12){
  yy++;
  mm=mm-12;
 }
 
  if (mm < 10){mm = "0" + mm;}
  if (outObject)
  {
    if (!n) {
      return;}
    if ( n < 10){n = "0" + n;}
	if (f=="time")
	{
		outObject.value = n;
	}else{
	    outObject.value= yy + "-" + mm + "-" + n ; 
	}
    closeLayer(); 
  }
  else {closeLayer(); alert("您所要输出的控件对象并不存在!");}
}

var strFrame2 = "";
document.writeln('<iframe id=meizzDateLayer2 Author=wayx frameborder=0 style="position: absolute; width: 144px; height: 200px; z-index: 9999; display:none"></iframe>');
strFrame2+='<scr' + 'ipt>';
strFrame2+='var datelayerx,datelayery; /*存放日历控件的鼠标位置*/';
strFrame2+='var bDrag; /*标记是否开始拖动*/';
strFrame2+='function document.onmousemove() /*在鼠标移动事件中，如果开始拖动日历，则移动日历*/';
strFrame2+='{if(bDrag && window.event.button==1)';
strFrame2+=' {var DateLayer=parent.document.all.meizzDateLayer2.style;';
strFrame2+='  DateLayer.posLeft += window.event.clientX-datelayerx;/*由于每次移动以后鼠标位置都恢复为初始的位置，因此写法与div中不同*/';
strFrame2+='  DateLayer.posTop += window.event.clientY-datelayery;}}';
strFrame2+='function DragStart()  /*开始日历拖动*/';
strFrame2+='{var DateLayer=parent.document.all.meizzDateLayer2.style;';
strFrame2+=' datelayerx=window.event.clientX;';
strFrame2+=' datelayery=window.event.clientY;';
strFrame2+=' bDrag=true;}';
strFrame2+='function DragEnd(){  /*结束日历拖动*/';
strFrame2+=' bDrag=false;}';
strFrame2+='</scr' + 'ipt>';
strFrame2+='<div style="z-index:9999;position: absolute; left:0; top:0; background-color:#e9f3ff; border:2px solid #448feb;padding:1px height:200px;height:200px; width:144px;text-align:center;font-size:12px;" onselectstart="return false">';
strFrame2+='<h1 Author="wayx" style="text-align:left;background-color:#448feb;font-size:12px;font-weight:normal;color:#fff;height:22px;line-height:22px;margin:0px;padding:0px 5px 0px 5px;cursor:' + (bMoveable ? 'move':'default') + '" '+(bMoveable? 'onmousedown="DragStart()" onmouseup="DragEnd()"':'')+'><a href=javascript:void(0) style=\'float:right;text-decoration:none;color:#fff\' onclick=parent.closeLayer()>&times;</a>选择时间</h1>';
strFrame2+='<br><span id="nowhour"></span>';
strFrame2+='</div>';
strFrame2+='<scr' + 'ipt>';
strFrame2+='setInterval("RealTime()",1000);';
strFrame2+='function RealTime(){';
strFrame2+='	var d = new Date();';
strFrame2+='	var hour = d.getHours();';
strFrame2+='	var minute = d.getMinutes();';
strFrame2+='	var second = d.getSeconds();';
strFrame2+='	if(hour<10) hour=0+hour.toString();';
strFrame2+='	if(minute<10) minute=0+minute.toString();';
strFrame2+='	if(second<10) second=0+second.toString();';
strFrame2+='	document.getElementById(\'realtime\').value = hour + ":" + minute + ":" + second;';
strFrame2+='}';
strFrame2+=' function getTime() {';
strFrame2+='    var sTime;';
strFrame2+='    sTime = document.all.hour.value + ":" + document.all.minute.value + ":" + document.all.second.value;';
strFrame2+='	parent.meizzDayClick(sTime,0,\'time\');';
strFrame2+='	parent.closeLayer();';
strFrame2+=' }';

strFrame2+='var timestr;';
strFrame2+='timestr = "<div id=TimeLayer style=\'background-color:#E9F3FF;\'>";';
strFrame2+='timestr+="<div style=\'margin-bottom:10px;\'>小时:<select id=hour name=hour>";';
strFrame2+='var d = new Date();';
strFrame2+=' for (var i=0; i<24;  i++){';
strFrame2+='	i<10?j=0+i.toString():j=i;';
strFrame2+='		if (d.getHours().toString()==i)';
strFrame2+='		{';
strFrame2+='			timestr+="<OPTION VALUE= " + j + " selected>" + j + "</option>";';
strFrame2+='		}else{';
strFrame2+='			timestr+="<OPTION VALUE= " + j + ">" + j + "</option>";';
strFrame2+='		}';
strFrame2+='	 }';
strFrame2+='	timestr+="</select></div><div style=\'margin-bottom:10px;\'>分钟:<select id=minute  name=minute class=smallSel>";';
strFrame2+='	 for (var i=0; i<60;  i++){';
strFrame2+='		i<10?j=0+i.toString():j=i;';
strFrame2+='		if (d.getMinutes().toString()==i)';
strFrame2+='		{';
strFrame2+='			 timestr+="<OPTION VALUE= " + j + " selected>" + j + "</option>";';
strFrame2+='		}else{';
strFrame2+='			 timestr+="<OPTION VALUE= " + j + ">" + j + "</option>";';
strFrame2+='		}';
strFrame2+='	}';
strFrame2+='	timestr+="</select></div><div style=\'margin-bottom:10px;\'>秒钟:<select id=second name=second class=smallSel>";';
strFrame2+='	 for (var i=0; i<60;  i++){';
strFrame2+=' 		i<10?j=0+i.toString():j=i;';
strFrame2+='		if (d.getSeconds().toString() == i)';
strFrame2+='		{';
strFrame2+='			timestr+="<OPTION VALUE= " + j + " selected>" + j + "</option>";';
strFrame2+='		}else{';
strFrame2+='			timestr+="<OPTION VALUE= " + j + ">" + j + "</option>";';
strFrame2+='		}';
strFrame2+='	}';
strFrame2+='	timestr+="</select></div><div><input type=button id=realtime onclick=parent.meizzDayClick(this.value,0,\'time\') value=\'12:34:56\'></div>";';
strFrame2+='	timestr+="<div style=\'margin:10px;\'><a href=javascript:void(0) onclick=getTime() style=color:#000>确定</a>&nbsp;&nbsp;&nbsp;&nbsp;";';
strFrame2+='	timestr+="<a href=javascript:void(0) onclick=parent.closeLayer() style=color:#000>取消</a></div>";';
strFrame2+='	timestr+="</div>";';
strFrame2+='	document.getElementById("nowhour").innerHTML = timestr;';
strFrame2+='</scr' + 'ipt>';

window.frames.meizzDateLayer2.document.writeln(strFrame2);
window.frames.meizzDateLayer2.document.close(); 

function selectTime(tt,obj)
{
 document.getElementById('meizzDateLayer').style.display="none";
 if (arguments.length >  2){alert("对不起!传入本控件的参数太多!");return;}
 if (arguments.length == 0){alert("对不起!您没有传回本控件任何参数!");return;}
 var dads  = document.all.meizzDateLayer2.style;
 var th = tt;
 var ttop  = tt.offsetTop; 
 var thei  = tt.clientHeight;
 var tleft = tt.offsetLeft; 
 var ttyp  = tt.type;       
 while (tt = tt.offsetParent){ttop+=tt.offsetTop; tleft+=tt.offsetLeft;}
 dads.top  = (ttyp=="image")? ttop+thei : ttop+thei+6;
 dads.left = tleft;
 outObject = (arguments.length == 1) ? th : obj;
 outButton = (arguments.length == 1) ? null : th;
 dads.display = '';
 event.returnValue=false;
}
