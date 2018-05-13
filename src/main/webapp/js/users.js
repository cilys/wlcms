$(document).ready(function(){
	if(unlogin()){}
	
	var startAllAppoint = 0;	//开始页数
	var pageSize = 5;	//每页显示数据条数
	var currentPage = 1;	//当前页数
	var totalPage = 0;			//数据总条数
	var userStatus = "";	//用户状态，0可用，1禁用，""全部用户
	
	getUsers(userStatus);
	
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
					"<th style='width:150px'>用户名</th>" + 
					"<th style='width:120px'>真实姓名</th>" + 
					"<th style='width:36px'>性别</th>" + 
					"<th style='width:90px'>手机号码</th>" + 
					"<th style='width:150px'>身份证号</th>" + 
					"<th style='width:30px'>状态</th>" + 
					"<th style='width:130px'>注册时间</th>" + 
					"<th>操作</th>" + 
				"</tr>";
				
	
				
		$.each(data, function(v, o) {
			s+='<tr><td>' + o.userName + '</td>';
			s+='<td>' + strFomcat(o.realName) + '</td>';
			s+='<td>' + fomcatSex(o.sex) + '</td>';
			s+='<td>' + strFomcat(o.phone) + '</td>';
			s+='<td>' + strFomcat(o.idCard) + '</td>';
			if(strFomcat(o.status) == '0'){
				s+="<td><button id='btn_status_edit' data-real-name=" + o.realName + " data-user-status=" + o.status + " data-user-id=" + o.userId + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#xe605;</i>" +
						"</button>" +
						"</td>";
			}else{
				s+="<td><button id='btn_status_edit' data-real-name=" + o.realName + " data-user-status=" + o.status + " data-user-id=" + o.userId + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#x1006;</i>" +
						"</button>" +
						"</td>";
			}
			
			s+='<td>' + strFomcat(o.createTime) + '</td>';
			s+="<td><div class='layui-btn-group'>" +
						"<button id='btn_user_edit' data-real-name=" + o.realName + " data-user-id=" + o.userId + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#x1002;</i>" +
						"</button>" +
						"<button class='layui-btn layui-btn-primary layui-btn-sm' id='btn_user_del' data-user-id=" + o.userId + ">" +
							"<i class='layui-icon'>&#xe640;</i>" + "</button></div></td></tr>";
		});
		
		if(data.length > 0){
			$("#t_customerInfo").html(s);
			
			$("#t_customerInfo #btn_user_edit").on("click", function(){
				var realName = $(this).attr("data-real-name");
				var userId = $(this).attr("data-user-id");
				resetPwdDialog(realName, userId);		
			});
			$("#t_customerInfo #btn_user_del").on("click", function(){
				var userId = $(this).attr("data-user-id");
				delUser(userId);	
			});
			$("#t_customerInfo #btn_status_edit").on("click", function(){
				var status = $(this).attr("data-user-status");
				var realName = $(this).attr("data-real-name");
				var userId = $(this).attr("data-user-id");
				enableUserDialog(status == "1" ? "0" : "1", realName, userId);
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
				      	getUsers(userStatus);//一定要把翻页的ajax请求放到这里，不然会请求两次。
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
	   		getUsers(userStatus);
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
							sex: $('input:radio[name="sex"]:checked').val() == '男' ? 1 : 2
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
				getUsers(userStatus);
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
	   		getUsers(userStatus);
   		
   	});
	
	function resetPwdDialog(realName, userId){
		layui.use("layer", function(){
			layui.layer.open({
				title:"重置密码",
				type:1,
				content:"<div style='padding: 20px 80px;'>是否重置" + realName + "密码？</div>",
				btn:"重置密码",
				btnAlign:"c",
				shade:0,
				yes:function(){
					layui.layer.closeAll();
					resetPwd(realName, userId);
				}
			});
		});
	}
	
	function resetPwd(userId){
		post(getHost() + "sysUser/updateUserInfo", 
			{userId: userId,
			pwd: "123456"
			},
			function success(res){
				new Toast({message: res.msg}).show();	
			}, function error(){
				new Toast({message: "重置密码失败，请重试..."}).show();
		});
	}
	
	function enableUserDialog(status, realName, userId){
		layui.use("layer", function(){
			layui.layer.open({
				title:(status == '0' ? "启用用户" : "禁用用户"),
				type:1,
				offset:"t",
				content:"<div style='padding: 20px 80px;'>是否" + (status == '0' ? "启用" : "禁用") + realName + "</div>",
				btn:"重置",
				btnAlign:"c",
				shade:0,
				yes:function(){
					layui.layer.closeAll();
					updateStatus(status, userId);
				}
			});
		});
	}
	function updateStatus(status, userId){
		post(getHost() + "sysUser/updateUserInfo", 
			{userId: userId,
			status: status
			},
			function success(res){
				new Toast({message: res.msg}).show();
				currentPage = 1;
	   			getUsers(userStatus);
			}, function error(){
				new Toast({message: "操作失败，请重试..."}).show();
		});
	}
	
	$("#btn_user_all").click(function(){
		$("#btn_user_all").removeClass("layui-btn-primary");
		$("#btn_user_enable").removeClass("layui-btn-primary");
		$("#btn_user_disable").removeClass("layui-btn-primary");
		
		$("#btn_user_enable").addClass("layui-btn-primary");
		$("#btn_user_disable").addClass("layui-btn-primary");
		userStatus = "";
		getUsers(userStatus);
	});
	$("#btn_user_enable").click(function(){
		$("#btn_user_all").removeClass("layui-btn-primary");
		$("#btn_user_enable").removeClass("layui-btn-primary");
		$("#btn_user_disable").removeClass("layui-btn-primary");
		
		$("#btn_user_all").addClass("layui-btn-primary");
		$("#btn_user_disable").addClass("layui-btn-primary");
		userStatus = "0";
		getUsers(userStatus);
	});
	$("#btn_user_disable").click(function(){
		$("#btn_user_all").removeClass("layui-btn-primary");
		$("#btn_user_enable").removeClass("layui-btn-primary");
		$("#btn_user_disable").removeClass("layui-btn-primary");
		
		$("#btn_user_enable").addClass("layui-btn-primary");
		$("#btn_user_all").addClass("layui-btn-primary");
		userStatus = "1";
		getUsers(userStatus);
	});
});