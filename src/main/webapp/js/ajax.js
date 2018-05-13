function post(url, data, sus, err){
	$.ajax({
		type:"POST",
		url:url,
		data:data,
		async:true,
		dataType: 'json',
		beforeSend:function(request){
			request.setRequestHeader("osType", "1");
			request.setRequestHeader("userId", window.localStorage.getItem("userId"));
			request.setRequestHeader("token", window.localStorage.getItem("token"));
		},
		success: function(re){
			if(login(re)){
				sus(re);
			}else{
				log("未登陆或登陆已过期")
				pHref("./login.html");
			}
		},
		error: function(er){
			err(er)
		}
	})
}