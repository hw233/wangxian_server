
  
//***********默认设置定义.*********************
  var tPopWait=50;        //停留tWait豪秒后显示提示。
  var tPopShow=16000;        //显示tShow豪秒后关闭提示
  var showPopStep=20;
  var popOpacity=95;
  var tfontcolor="#000000";
  var tbgcolor="#000000";
  var tbordercolor="#666666";

  //***************内部变量定义*****************
  var pltsPop=null;
  var pltsoffsetX = 6; // 弹出窗口位于鼠标左侧或者右侧的距离；3-12 合适
  var pltsoffsetY = 6; // 弹出窗口位于鼠标下方的距离；3-12 合适
  var pltsPopbg="#FF0099"; //背景色
  var pltsPopfg="#880000"; //前景色
  var pltsTitle="";
  //document.write('<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>');
  function pltsinits()
  {
  document.onmouseover = plts;
  document.onmousemove = moveToMouseLoc;
  }
  function plts()
  { var o=event.srcElement;
  if(o.alt!=null && o.alt!=""){o.dypop=o.alt;o.alt=""};
  if(o.title!=null && o.title!=""){o.dypop=o.title;o.title=""};
  pltsPop=o.dypop;
  if(pltsPop!=null&&pltsPop!=""&&typeof(pltsPop)!="undefined"&&pltsTipLayer!=null)
  {
  pltsTipLayer.style.left=-1000;
  pltsTipLayer.style.display='';
  var Msg=pltsPop.replace(/\n/g,"<br>");
  Msg=Msg.replace(/\0x13/g,"<br>");
  var re=/\{(.[^\{]*)\}/ig;
  if(!re.test(Msg))pltsTitle="";
  else{
  re=/\{(.[^\{]*)\}(.*)/ig;
  pltsTitle=Msg.replace(re,"$1")+" ";
  re=/\{(.[^\{]*)\}/ig;
  Msg=Msg.replace(re,"");
  Msg=Msg.replace("<br>","");}
  var attr=(document.location.toString().toLowerCase().indexOf("list.asp")>0?"nowrap":"");
  var content =
  '<table style="FILTER:alpha(opacity=90) shadow(color=#bbbbbb,direction=135);"  id=toolTipTalbe border=0><tr><td  width="100%"><table  cellspacing="0" cellpadding="0" style="width:100%" height=22>'+
  '<tr id=pltsPoptop class=td11 style="display:none"><td height=18 valign=bottom bgcolor=#ACDCDC><b><div id=topleft align=left>'+pltsTitle+'</div><div id=topright align=right style="display:none">'+pltsTitle+'</font></b></td></tr>'+
  '<tr><td "+attr+" class=bg_td11 bgcolor=#EBECF5  valign=top style="padding-left:14px;padding-right:14px;padding-top: 6px;padding-bottom:6px;line-height:135%">'+Msg+'</td></tr>'+
  '<tr id=pltsPopbot style="display:none"><th  height=18 valign=bottom><b><div id=botleft align=left>'+pltsTitle+'</div><div id=botright align=right style="display:none">'+pltsTitle+'</font></b></th></tr>'+
  '</table></td></tr></table>';
  pltsTipLayer.innerHTML=content;
  toolTipTalbe.style.width=Math.min(pltsTipLayer.clientWidth,document.documentElement.clientWidth/2.2);
  moveToMouseLoc();
  return true;
  }
  else
  {
	  if(pltsTipLayer!=null){
		  pltsTipLayer.innerHTML='';
		  pltsTipLayer.style.display='none';
	  }
  return true;
  }
  }

  function moveToMouseLoc()
  {
  if(pltsTipLayer!=null&&pltsTipLayer.innerHTML=='')return true;
  var MouseX=event.x;
  var MouseY=event.y;

  //pltsTipLayer.style.left=MouseX+pltsoffsetX+document.body.scrollLeft+popLeftAdjust;
  //pltsTipLayer.style.top=MouseY+pltsoffsetY+document.body.scrollTop+popTopAdjust;
  
  function mousePosition(ev){
	    if(ev.pageX || ev.pageY){
	     return {x:ev.pageX, y:ev.pageY};
	      }
	    var xValue=0;
	    var yValue=0;
	    if(pltsTipLayer != null){
		    if((ev.clientX+pltsTipLayer.clientWidth)>document.documentElement.clientWidth){
		    	xValue = ev.clientX + document.documentElement.scrollLeft - document.documentElement.clientLeft-pltsTipLayer.clientWidth;
		    }else{
		    	xValue = ev.clientX + document.documentElement.scrollLeft - document.documentElement.clientLeft;
		    }
		    if((ev.clientY+pltsTipLayer.clientHeight)>document.documentElement.clientHeight){
		    	yValue = ev.clientY + document.documentElement.scrollTop  - document.documentElement.clientTop-pltsTipLayer.clientHeight;
		    }else{
		    	yValue = ev.clientY + document.documentElement.scrollTop  - document.documentElement.clientTop;
		    }
	    }
	     return {
	    	 x:xValue,
	         y:yValue
	       };
	}

	function mouseMove(ev){
	    ev = ev || window.event;
	   var mousePos = mousePosition(ev);
	   //悬浮框的位置
	   if(pltsTipLayer != null){
		  pltsTipLayer.style.left=mousePos.x;
		  pltsTipLayer.style.top=mousePos.y;
	   }
	}
	document.onmousemove = mouseMove;
  
  return true;
  }
  pltsinits();
