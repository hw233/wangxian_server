// ����ѡ��
// By Ziyue(http://www.web-v.com/)
var months = new Array("һ��", "����", "����", "����", "����", "����", "����", "����", "����", "ʮ��", "ʮһ��", "ʮ����"); 
var daysInMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31); 
var days = new Array("��","һ", "��", "��", "��", "��", "��"); 
var today; 

document.writeln("<div id='Calendar' style='position:absolute; z-index:1; visibility: hidden; filter:\"progid:DXImageTransform.Microsoft.Shadow(direction=135,color=#999999,strength=3)\"'></div>");

function getDays(month, year)
{ 
    //�������δ������жϵ�ǰ�Ƿ�������� 
    if (1 == month) 
        return ((0 == year % 4) && (0 != (year % 100))) || (0 == year % 400) ? 29 : 28; 
    else 
        return daysInMonth[month]; 
} 

function getToday() 
{ 
    //�õ��������,��,�� 
    this.now = new Date(); 
    this.year = this.now.getFullYear(); 
    this.month = this.now.getMonth(); 
    this.day = this.now.getDate(); 
}

function getStringDay(str) 
{ 
    //�õ���������,��,��
    var str=str.split("-")
    
    this.now = new Date(parseint(str[0]),parseint(str[1])-1,parseint(str[2])); 
    this.year = this.now.getFullYear(); 
    this.month = this.now.getMonth(); 
    this.day = this.now.getDate(); 
}

function newCalendar() { 
    var parseYear = parseInt(document.all.Year.options[document.all.Year.selectedIndex].value); 
    var newCal = new Date(parseYear, document.all.Month.selectedIndex, 1); 
    var day = -1; 
    var startDay = newCal.getDay(); 
    var daily = 0; 
    
    if ((today.year == newCal.getFullYear()) &&(today.month == newCal.getMonth())) 
        day = today.day; 
        
    var tableCal = document.all.calendar; 
    var intDaysInMonth =getDays(newCal.getMonth(), newCal.getFullYear());
     
    for (var intWeek = 1; intWeek < tableCal.rows.length;intWeek++) 
        for (var intDay = 0;intDay < tableCal.rows[intWeek].cells.length;intDay++) 
        { 
            var cell = tableCal.rows[intWeek].cells[intDay]; 
            if ((intDay == startDay) && (0 == daily)) 
                daily = 1; 
                
            if(day==daily) //���죬���ý����Class 
            {
                cell.style.background='#6699CC';
                cell.style.color='#FFFFFF';
                //cell.style.fontWeight='bold';
            }
            else if(intDay==6) //���� 
                cell.style.color='green'; 
            else if (intDay==0) //���� 
                cell.style.color='red';
            
            if ((daily > 0) && (daily <= intDaysInMonth)) 
            { 
                cell.innerText = daily; 
                daily++; 
            } 
            else 
                cell.innerText = ""; 
        } 
} 

function GetDate(InputBox)
{ 
    var sDate; 
    //��δ��봦����������� 
    if (event.srcElement.tagName == "TD") 
        if (event.srcElement.innerText != "") 
        { 
            sDate = document.all.Year.value + "-" + document.all.Month.value + "-" + event.srcElement.innerText;
            eval("document.all."+InputBox).value=sDate;
            HiddenCalendar();
        } 
} 

function HiddenCalendar()
{
    //�ر�ѡ�񴰿�
    document.all.Calendar.style.visibility='hidden';
}

function ShowCalendar(InputBox)
{
    var x,y,intLoop,intWeeks,intDays;
    var DivContent;
    var year,month,day;
    var o=eval("document.all."+InputBox);
    var thisyear; //����Ľ������
    
    thisyear=new getToday();
    thisyear=thisyear.year;
    
    today = o.value;
    if(isDate(today))
        today = new getStringDay(today);
    else
        today = new getToday(); 
    
    //��ʾ��λ��
    x=o.offsetLeft;
    y=o.offsetTop;
    while(o=o.offsetParent)
    {
        x+=o.offsetLeft;
        y+=o.offsetTop;
    }
    document.all.Calendar.style.left=x+2;
    document.all.Calendar.style.top=y+20;
    document.all.Calendar.style.visibility="visible";
    
    //���濪ʼ���������(border-color:#9DBAF7)
    DivContent="<table border='0' cellspacing='0' style='border:1px solid #0066FF; background-color:#EDF2FC'>";
    DivContent+="<tr>";
    DivContent+="<td style='border-bottom:1px solid #0066FF; background-color:#C7D8FA'>";
    
    //��
    DivContent+="<select name='Year' id='Year' onChange='newCalendar()' style='font-family:Verdana; font-size:12px'>";
    for (intLoop = thisyear - 35; intLoop < (thisyear + 20); intLoop++) 
        DivContent+="<option value= " + intLoop + " " + (today.year == intLoop ? "Selected" : "") + ">" + intLoop + "</option>"; 
    DivContent+="</select>";
    
    //��
    DivContent+="<select name='Month' id='Month' onChange='newCalendar()' style='font-family:Verdana; font-size:12px'>";
    for (intLoop = 0; intLoop < months.length; intLoop++) 
        DivContent+="<option value= " + (intLoop + 1) + " " + (today.month == intLoop ? "Selected" : "") + ">" + months[intLoop] + "</option>"; 
    DivContent+="</select>";
    
    DivContent+="</td>";
    
    DivContent+="<td style='border-bottom:1px solid #0066FF; background-color:#C7D8FA; font-weight:bold; font-family:Wingdings 2,Wingdings,Webdings; font-size:16px; padding-top:2px; color:#4477FF; cursor:hand' align='center' title='�ر�' onClick='javascript:HiddenCalendar()'>S</td>";
    DivContent+="</tr>";
     
    DivContent+="<tr><td align='center' colspan='2'>";
    DivContent+="<table id='calendar' border='0' width='100%'>";
    
    //����
    DivContent+="<tr>";
    for (intLoop = 0; intLoop < days.length; intLoop++) 
        DivContent+="<td align='center' style='font-size:12px'>" + days[intLoop] + "</td>"; 
    DivContent+="</tr>";
    
    //��
    for (intWeeks = 0; intWeeks < 6; intWeeks++)
    { 
        DivContent+="<tr>"; 
        for (intDays = 0; intDays < days.length; intDays++) 
            DivContent+="<td onClick='GetDate(\"" + InputBox + "\")' style='cursor:hand; border-right:1px solid #BBBBBB; border-bottom:1px solid #BBBBBB; color:#215DC6; font-family:Verdana; font-size:12px' align='center'></td>"; 
        DivContent+="</tr>"; 
    } 
    DivContent+="</table></td></tr></table>";

    document.all.Calendar.innerHTML=DivContent;
    newCalendar();
}

function isDate(dateStr)
{ 
    var datePat = /^(\d{4})(\-)(\d{1,2})(\-)(\d{1,2})$/;
    var matchArray = dateStr.match(datePat);
    if (matchArray == null) return false; 
    var month = matchArray[3];
    var day = matchArray[5]; 
    var year = matchArray[1]; 
    if (month < 1 || month > 12) return false; 
    if (day < 1 || day > 31) return false; 
    if ((month==4 || month==6 || month==9 || month==11) && day==31) return false; 
    if (month == 2)
    {
        var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)); 
        if (day > 29 || (day==29 && !isleap)) return false; 
    } 
    return true;
}
