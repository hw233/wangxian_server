window.onload=function(){
	var noItems = document.getElementById("noitems");
	if(noItems!= null){
	    document.getElementById("items_page").style.display = "none" ;
	}
}

function displaytagURL() 
{ 
	var pagenum = document.getElementById('tz').value;
	var dpatt = /^\d+$/;
	if(!dpatt.test(pagenum)){
		alert("页码只能是数字");
		return;
	}
	var totalPageNum = document.getElementById('totalPageNum').value;
	if(parseInt(pagenum) > totalPageNum || parseInt(pagenum)<=0){
		alert("页码必须为大于0且不超过总页数的整数");
		return;
	}
	var pattern = /d-(\d)+-p=/;
/*	var arrayOf_table_Tag = document.getElementsByTagName('table'); 
	for (var i=0; i<arrayOf_table_Tag.length; i++){ 
		if(arrayOf_table_Tag[i].id.indexOf('nm')==0) //form的名字，这里是我的项目中的命名规则，csXXXXList,这里要按照你的项目的命名规则来修改
		{ 
			var charArray = "x-"+arrayOf_table_Tag[i].id; 
			var checkSum = 17; 
			for(var j = 0; j < charArray.length; j++){ 
				checkSum = 3 * checkSum + charArray.charCodeAt(j); 
			} 
			checkSum &=8388607; //对form的名字的转换，这里可以在displaytag源码中看到
*/			var parameterIdentifier = "d-5461-p="; 
			var arrayOf_a_Tag = document.getElementsByTagName('a'); 
			for (var k=0; k<arrayOf_a_Tag.length; k++){ 
				if(arrayOf_a_Tag[k].title.indexOf('跳转到页面')==0){ 
					var url = arrayOf_a_Tag[k].href; 
					var splitUrl = url.split("&"); 
					for(var l=0;l<splitUrl.length;l++){ 
						if(/*splitUrl[l].indexOf(parameterIdentifier)!=-1*/pattern.test(splitUrl[l])){ 
							var url1 = (url.split("?"))[1].split("&");
							for(var n=0; n<url1.length; n++){
								if(pattern.test(url1[n])){
									parameterIdentifier=url1[n].substring(0,url1[n].indexOf("=")+1);
								}
							}
							if(l==0){
								splitUrl[l]="?"+parameterIdentifier+document.getElementById('tz').value;
							}else{
								splitUrl[l]=parameterIdentifier+document.getElementById('tz').value;
							} 
                        } 
					} 
					var realURL =''; 
					for(m=0;m<splitUrl.length;m++){ 
						if(m!=splitUrl.length-1){
							realURL+=splitUrl[m]+'&';
						}else{
							realURL+=splitUrl[m];
						} 
					} 
					window.location.href=realURL; 
					break; 
					return; 
				} 
			} 
		/*} 
	} */
}

function changePageSize(){
	var pattern = /d-(\d)+-p=/;
	var parameterIdentifier = "d-5461-p="; 
	var parameterPageSize = "pageSize=";
	var arrayOf_a_Tag = document.getElementsByTagName('a'); 
	for (var k=0; k<arrayOf_a_Tag.length; k++){ 
		if(arrayOf_a_Tag[k].title.indexOf('跳转到页面')==0){ 
			var url = arrayOf_a_Tag[k].href; 
			var splitUrl = url.split("&"); 
			for(var l=0;l<splitUrl.length;l++){ 
				if(/*splitUrl[l].indexOf(parameterIdentifier)!=-1*/pattern.test(splitUrl[l])){ 
					var url1 = (url.split("?"))[1].split("&");
					for(var n=0; n<url1.length; n++){
						if(pattern.test(url1[n])){
							parameterIdentifier=url1[n].substring(0,url1[n].indexOf("=")+1);
						}
					}
					if(l==0){
						splitUrl[l]="?"+parameterIdentifier+"1";
					}else{
						splitUrl[l]=parameterIdentifier+"1";
					} 
	            }
				if(splitUrl[l].indexOf(parameterPageSize)!=-1){
					var pageSize = splitUrl[l].split("=");
					var size = $("select[name='pageSize']").val();
					splitUrl[l]=pageSize[0]+"="+size;
				}
			}
				
			var realURL =''; 
			for(m=0;m<splitUrl.length;m++){ 
				if(m!=splitUrl.length-1){
					realURL+=splitUrl[m]+'&';
				}else{
					realURL+=splitUrl[m];
				} 
			} 
			if(url.indexOf(parameterPageSize)==-1){
				var size = $("select[name='pageSize']").val();
				realURL+='&'+parameterPageSize+size;
			}
			window.location.href=realURL; 
			break; 
			return; 
		} 
	} 
}

//弹出层操作后返回上一页
function goHistoryAfterPopup(page){
	var pattern = /d-(\d)+-p=/;
	var parameterIdentifier = "d-5461-p="; 
	var arrayOf_a_Tag = document.getElementsByTagName('a'); 
	for (var k=0; k<arrayOf_a_Tag.length; k++){ 
		if(arrayOf_a_Tag[k].title.indexOf('跳转到页面')==0){ 
			var url = arrayOf_a_Tag[k].href; 
			var splitUrl = url.split("&"); 
			for(var l=0;l<splitUrl.length;l++){ 
				if(/*splitUrl[l].indexOf(parameterIdentifier)!=-1*/pattern.test(splitUrl[l])){ 
					var url1 = (url.split("?"))[1].split("&");
					for(var n=0; n<url1.length; n++){
						if(pattern.test(url1[n])){
							parameterIdentifier=url1[n].substring(0,url1[n].indexOf("=")+1) + page;
							break;
						}
					}
					
				} 
			} 
			return parameterIdentifier; 
		} 
	} 
}