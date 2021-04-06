function ajax(url){	
	
	

	this.url=url;
	this.input=null;

	try{
		this.aObject = window.XMLHttpRequest ? new XMLHttpRequest(): new ActiveXObject("Microsoft.XMLHTTP");
	}
	catch (e){
		this.aObject = false;
	}

	

	this.addParam = function(name,value){
		if(!this.input)
			this.input=new Array();
		this.input[this.input.length]=name+'='+value;
	}

	this.addJson = function(json){
		this.input=json;
	}

	this.post=function(runfun){

		
		if(!this.aObject){
			alert("Browser doesn't support ajax");
			return;
		}

		this.aObject.open("POST",this.url,true); 

		//定义传输的文件HTTP头信息

		this.aObject.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk"); 
		if(typeof(this.input)=='string')
			this.aObject.send(this.input);
		else
			this.aObject.send(this.input.join('&'));
		
		var obj=this;
		this.aObject.onreadystatechange=function(){obj.callBack(runfun)};

	}
	
	//以GET方式，发送数据
	this.get=function (runfun){
		
		
		if(!this.aObject){
			alert("Browser doesn't support ajax");
			return;
		}
		
		if(typeof(this.input)=='string')
			var input=this.input;
		else
			var input=this.input.join('&');
        
         
		this.aObject.open("GET",this.url+'?'+input+"&callback=?",true); 
		
		 
		this.aObject.send(null);

		var obj=this;
		this.aObject.onreadystatechange=function(){obj.callBack(runfun)};

	};

	this.callBack=function(runfun){
		 
		var bool=(this.aObject.readyState == 4 && this.aObject.status == 200);
		 
		if(bool){
		 
			runfun(this.aObject.responseText);
		}
	};

}
/**
* Create and return a "version 4" RFC-4122 UUID string.
*/
function randomUUID() {
  var s = [], itoh = '0123456789ABCDEF';
 
  // Make array of random hex digits. The UUID only has 32 digits in it, but we
  // allocate an extra items to make room for the '-'s we'll be inserting.
  for (var i = 0; i <36; i++) s[i] = Math.floor(Math.random()*0x10);
 
  // Conform to RFC-4122, section 4.4
  s[14] = 4;  // Set 4 high bits of time_high field to version
  s[19] = (s[19] & 0x3) | 0x8;  // Specify 2 high bits of clock sequence
 
  // Convert to hex chars
  for (var i = 0; i <36; i++) s[i] = itoh[s[i]];
 
  // Insert '-'s
  s[8] = s[13] = s[18] = s[23] = '-';
 
  return s.join('');
}
function dataSave(k,v,d) { 
		var date = new Date();
		date.setTime(date.getTime()+((d?d:365*10)*24*60*60*1000));
		document.cookie = k+"="+v+"; expires="+date.toGMTString()+"; path=/";
}
function dataLoad(k,a) {
		var nameeq = k+"=";
		var ca = document.cookie.split(";");
		var rt;
		for (var i=0;i<ca.length;i++) {
			var c=ca[i];
			while (c.charAt(0)==' ') c=c.substring(1,c.length);
			if (c.indexOf(nameeq)==0) {
				rt=c.substring(nameeq.length,c.length);
				if (a&&a.number) return rt*1; else return rt;
				if (a&&a.number) return rt*1; else return rt;
			}
		}
		return null;
} 
 
function getDeviceUUID(){
	    if(dataLoad("san-DeviceUUID") != null) {
			return dataLoad("san-DeviceUUID");
		}else{
			var uuid = randomUUID();
			dataSave("san-DeviceUUID",uuid);
			return uuid;
		}
}
function isHaveDeiceUUID(){
	 if(dataLoad("san-DeviceUUID") != null) {
	 	return true;
	 }else{
	 	return false;
	 }
}

