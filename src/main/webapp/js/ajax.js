function post(url, data, sus, err){
	$.ajax({
		type:"POST",
		url:url,
		data:data,
		async:true,
		dataType: 'json',
		beforeSend:function(request){
			request.setRequestHeader("osType", "1");
		},
		success: function(re){
			sus(re);
		},
		error: function(er){
			err(er)
		}
	})
}