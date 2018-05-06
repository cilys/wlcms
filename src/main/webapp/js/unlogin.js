function login(res){
	if(res == 'undefined'){
		pHref("./login.html");
		return false;
	}
	var code = res.status;

	if(code == "1002"){
		//1002用户未登录
		clearCookie();
		pHref("./login.html");
		return false;
	}else if(code == "1003"){
		//1003登录已过期
		clearCookie();
		pHref("./login.html");
		
		return false;
	}else{
		return true;
	}
}

function loginInfoEmpty(){
//	var uid = $.cookie('userId');
//	var token = $.cookie("token");

	var uid = window.localStorage.getItem("userId");
	var token = window.localStorage.getItem("token");
	
	log("userId = " + uid + "<--->token = " + token);

	if(uid == null || uid == "undefined" || uid == "" || uid == "null"
		|| token == null || token == "undefined" || token == "" || token == "null"){
			
		clearCookie();
		
		return true;
	}
		return false;
	
}

function unlogin(){
		pHref("./login.html");
//		href("./login.html");
}

function clearCookie(){
//	$.cookie("userId", null);
//	$.cookie("token", null);
	
	window.localStorage.removeItem("token");
	window.localStorage.removeItem("userId");

}

function saveCookie(token, userId){
//	$.cookie('token', token, {expires: 1, path: '/'});
//	$.cookie('userId', userId, {expires: 1, path: '/'});

	window.localStorage.setItem("token", token);
	window.localStorage.setItem("userId", userId);
}
