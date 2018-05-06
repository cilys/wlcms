$(document).ready(function(){
	if(unlogin()){}
	
	var startAllAppoint = 0;	//开始页数
	var pageSize = 10;	//每页显示数据条数
	var currentPage = 1;	//当前页数
	var totalPage = 0;			//数据总条数
	
	getUsers("");
	
	function getUsers(status){
		post(getHost() + "sysUser/getUsers", 
			{pageNumber:currentPage, pageSize:pageSize, status:status},
		function success(res){
			new Toast({message: res.msg}).show();
			
			if(res.code == '0'){
				setDataToView(res.data.list);
				totalPage = res.data.totalPage;
				currentPage = res.data.pageNumber;
				toPage();
			}
		}, function error(err){
			new Toast({message: "获取用户列表失败"}).show();
		});
	}
	
	function setDataToView(data){
		var s = "<tr>" + 
					"<th>用户名</th>" + 
					"<th>真实姓名</th>" + 
					"<th>性别</th>" + 
					"<th>手机号码</th>" + 
					"<th>状态</th>" + 
					"<th>注册时间</th>" + 
					"<th>操作</th>" + 
				"</tr>";
				
	
				
		$.each(data, function(v, o) {
			s+='<tr><td>' + o.userName + '</td>';
			s+='<td>' + strFomcat(o.realName) + '</td>';
			s+='<td>' + fomcatSex(o.sex) + '</td>';
			s+='<td>' + strFomcat(o.phone) + '</td>';
			s+='<td>' + fomcatEnable(o.status) + '</td>';
			s+='<td>' + strFomcat(o.createTime) + '</td>';
			s+="<td><div class='layui-btn-group'>" +
						"<button id='btn_user_edit' data-user-id=" + o.userId + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#xe642;</i>" +
						"</button>" +
						"<button class='layui-btn layui-btn-primary layui-btn-sm' id='btn_user_del' data-user-id=" + o.userId + ">" +
							"<i class='layui-icon'>&#xe640;</i>" + "</button></div></td></tr>";
		});
		
		if(data.length > 0){
			$("#t_customerInfo").html(s);
			
			$("#t_customerInfo #btn_user_edit").on("click", function(){
				var userId = $(this).attr("data-user-id");
				new Toast({message: "编辑：" + userId}).show();		
			});
			$("#t_customerInfo #btn_user_del").on("click", function(){
				var userId = $(this).attr("data-user-id");
				delUser(userId);	
			});
			
		}else{
			$("#paged").hide();
			$("#t_customerInfo").html("<br/><span style='width:10%;height:30px;display:block;margin:0 auto;'>暂无数据</span>");
		}
	}
	
	function toPage(){
   		layui.use(['form', 'laypage', 'layedit','layer', 'laydate'], function() {
			var form = layui.form(),
				layer = layui.layer,
				layedit = layui.layedit,
				laydate = layui.laydate,
				laypage = layui.laypage;

			//调用分页
			  laypage({
			    cont: 'paged'
			    ,pages: totalPage //得到总页数，在layui2.X中使用count替代了，并且不是使用"总页数"，而是"总记录条数"
			    ,curr: currentPage
			    ,skip: true
			    ,jump: function(obj, first){
			    	
			    	currentPage = obj.curr;
			    	startAllAppoint = (obj.curr-1)*pageSize;
			      //document.getElementById('biuuu_city_list').innerHTML = render(obj, obj.curr);
				    if(!first){ //一定要加此判断，否则初始时会无限刷新
				      	getUsers("");//一定要把翻页的ajax请求放到这里，不然会请求两次。
				          //location.href = '?page='+obj.curr;
				    }
			   	}
			  });
			
			
		});
   	};
   	
   	$("#clearSearch").click(function(){
   		var searchValue = $("#selectValue").val();
   		$("#selectValue").val("");
   		if(searchValue.length > 0){
	   		currentPage = 1;
	   		getUsers("");
   		}
   	});
   	
   	var addBoxIndex = -1;
	$('#add').on('click', function() {
		if(addBoxIndex !== -1)
			return;
		//本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
		$.get('temp/edit-form.html', null, function(form) {
			addBoxIndex = layer.open({
				type: 1,
				title: '编辑用户',
				content: form,
				shadeClose: true,
				shade: 0.5,
				offset: ['5%', '15%'],
				area: ['70%', '90%'],
				zIndex: 19950924,
				maxmin: false,
				
				full: function(elem) {
					var win = window.top === window.self ? window : parent.window;
					$(win).on('resize', function() {
						var $this = $(this);
						elem.width($this.width()).height($this.height()).css({
							top: 0,
							left: 0
						});
						elem.children('div.layui-layer-content').height($this.height() - 95);
					});
				},
				success: function(layero, index) {
					//弹出窗口成功后渲染表单
					var form = layui.form();
					form.render();
					form.on('submit(addUser)', function(data){
						post(getHost() + "sysUser/addUser", {
							userName: $("#userName_id").val(),
							pwd: $("#pwd_id").val(),
							realName: $("#realName_id").val(),
							phone: $("#phone_id").val(),
							idCard: $("#idCard_id").val(),
							address: $("#address_id").val(),
							status: $("#status_id").prop('checked') ? 0 : 1,
							sex: $('input:radio[name="sex"]:checked').val() == '男' ? 0 : 1
						}, function success(res){
							new Toast({message: res.msg}).show();
							
							if(res.code == 0){
								layer.closeAll();
								return true;
							}else{
								
							}
						}, function error(err){
							new Toast({message: "添加用户失败"}).show();
							layer.closeAll();
						});
						
						return false;
					});
				},
				end: function() {
					addBoxIndex = -1;
				}
			});
		});
	});
	
	function delUser(userId){
		post(getHost() + "sysUser/delUser",
		{userId: userId},
		function success(res){
			new Toast({message: res.msg}).show();
			if(res.code == '0'){
				getUsers();
			}
		}, function error(err){
			new Toast({message: '删除用户失败，请重试...'}).show();
		});
	}
	
	$("#selectButton").click(function(){
		var searchValue = $("#selectValue").val();
   		
   		if(searchValue.length > 0){
			currentPage = 1;
			post(getHost() + "user/search", 
			{
				pageNumber:currentPage, 
				pageSize:pageSize,
				searchText: $("#selectValue").val()
			},function success(res){
				new Toast({message: res.msg}).show();
				
				if(res.code == '0'){
					setDataToView(res.data.list);
					totalPage = res.data.totalPage;
					currentPage = res.data.pageNumber;
					toPage();
				}
			}, function error(err){
				new Toast({message: "查询用户失败..."}).show();
				logErr("查询用户失败..." + err);
			});
		}else{
			new Toast({message: "请输入搜索内容"}).show();
		}
	});
	
	$("#a_reload").click(function(){
   		var searchValue = $("#selectValue").val();
   		$("#selectValue").val("");
   		
	   		currentPage = 1;
	   		getUsers("");
   		
   	});
	
//	layui.use(['layer', 'form'], function() {
//		var layer = layui.layer,
//			
//		form = layui.form();
//			
//		form.on('submit(search)',function(data){
//			currentPage = 1;
//			post(getHost() + "user/search", 
//			{
//				pageNumber:currentPage, 
//				pageSize:pageSize,
//				searchText: $("#selectValue").val()
//			},function success(res){
//				new Toast({message: res.msg}).show();
//				
//				if(res.code == '0'){
//					setDataToView(res.data.list);
//					totalPage = res.data.totalPage;
//					currentPage = res.data.pageNumber;
//					toPage();
//				}
//			}, function error(err){
//				new Toast({message: "查询用户失败..."}).show();
//				logErr("查询用户失败..." + err);
//			});
//			
//			return false;
//		});
//	});
});