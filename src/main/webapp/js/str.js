function strIsEmpty(str){
	return (str == null || str == "" || str == undefined || str == "undefined" || str.length == 0 || str.indexOf("null") > -1);
}

function strFomcat(str){
	return strIsEmpty(str) ? "" : str;
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
		return "未知";
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
	return "未知";
}