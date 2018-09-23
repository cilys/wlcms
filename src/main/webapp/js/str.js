function strIsEmpty(str){
	return (str == null || str == "" || str == undefined || str == "undefined" || str.length == 0 || str.indexOf("null") > -1);
}

function strFomcat(str){
	return strIsEmpty(str) ? "" : str;
}

function strFomcatUserName(realName, userName){
	var r = strFomcat(realName);
	
	return strIsEmpty(r) ? userName : r;
}

function fomcatMsg(str){
	var s = getRecordContent(str);
	log("发布内容：" + s);
	if(s.length > 30){
		return s = s.substring(0, 30) + "...";
	}else{
		return s;
	}
}

function getRecordContent(str){
	var s = strFomcat(str);
	// if(s.length > 0){
	// 	return decodeURI(s);
	// }
	return s;
}

function fomcatRecordContent(str){
	var s = getRecordContent(str);
	log("发布内容：" + s);
	if(s.length > 12){
		return s = s.substring(0, 12) + "...";
	}else{
		return s;
	}
}

function fomcatEnable(str){
	if(strIsEmpty(str)){
		return "禁用";
	}
	
	if(str == "0"){
		return "可用";
	}
	
	return "禁用";
}

function fomcatSex(str){
	if(strIsEmpty(str)){
		return "保密";
	}
	if(str == "0"){
		return "未知";
	}
	if(str == "1"){
		return "男";
	}
	if(str == "2"){
		return "女";
	}
	return "保密";
}