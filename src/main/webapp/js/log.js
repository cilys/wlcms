function log(str){
	console.log(str);
}

function logE(err){
    //此函数不会被关闭，会打印出来
    console.log(err);
}

function logErr(err){
	//此函数不会被关闭，会打印出来
	console.log(err);
}

function al(str){
	alert(str);
}

function href(url){
	history.replaceState("", "", location.href = url);
//	location.href = url;
}

function pHref(url){
	history.replaceState("", "", window.parent.href = url);
//	window.location.href = url;
}
