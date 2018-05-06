$(document).ready(function(){
	if(unlogin()){}
	
	var startAllAppoint = 0;	//开始页数
	var pageSize = 2;	//每页显示数据条数
	var currentPage = 1;	//当前页数
	var totalPage = 0;			//数据总条数
	
	getRecords();
	
	function getRecords(){
		post(getHost() + "sysUser/getRecords", 
			{pageNumber:currentPage, pageSize:pageSize},
		function success(res){
			new Toast({message: res.msg}).show();
			
			if(res.code == '0'){
				setDataToView(res.data.list);
				totalPage = res.data.totalPage;
				currentPage = res.data.pageNumber;
				toPage();
			}
		}, function error(err){
			new Toast({message: "获取发布列表失败"}).show();
		});
	}
	
	function setDataToView(data){
		var s = "<tr>" + 
					"<th>物料编码</th>" + 
					"<th>物料名称</th>" + 
					"<th>等级</th>" + 
					"<th>图片</th>" + 
					"<th>详情</th>" + 
					"<th>发布者</th>" + 
					"<th>更新时间</th>" + 
					"<th>操作</th>" + 
				"</tr>";
				
	
				
		$.each(data, function(v, o) {
			s+='<tr><td>' + strFomcat(o.recordNum) + '</td>';
			s+='<td>' + strFomcat(o.recordName) + '</td>';
			s+='<td>' + strFomcat(o.recordLevel) + '</td>';
			s+='<td>' + strFomcat(null) + '</td>';
			s+='<td>' + strFomcat(o.recordContent) + '</td>';
			s+='<td>' + strFomcat(o.userName) + '</td>';
			s+='<td>' + strFomcat(o.updateTime) + '</td>';
			s+="<td><div class='layui-btn-group'>" +
						"<button id='btn_user_info' data-record-content=" + o.recordContent + " data-record-name=" + o.recordName + " data-record-num=" + o.recordNum + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#xe642;</i>" +
						"</button>" +
						"<button class='layui-btn layui-btn-primary layui-btn-sm' id='btn_user_del' data-record-id=" + o.recordId + ">" +
							"<i class='layui-icon'>&#xe640;</i>" + "</button></div></td></tr>";
		});
		
		if(data.length > 0){
			$("#t_customerInfo").html(s);
			
			$("#t_customerInfo #btn_user_info").on("click", function(){
				var recordName = $(this).attr("data-record-name");
				var recordNum = $(this).attr("data-record-num");
				var recordContent = $(this).attr("data-record-content");
				
				showRecordContent(recordName, recordNum, recordContent);
			});
			
			$("#t_customerInfo #btn_user_del").on("click", function(){
				var recordId = $(this).attr("data-record-id");
				delRecord(recordId);	
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
				      	getRecords();//一定要把翻页的ajax请求放到这里，不然会请求两次。
				          //location.href = '?page='+obj.curr;
				    }
			   	}
			  });
		});
   };
	
	function delRecord(recordId){
		post(getHost() + "record/del",
		{recordId: recordId},
		function success(res){
			new Toast({message: res.msg}).show();
			if(res.code == '0'){
				getUsers();
			}
		}, function error(err){
			new Toast({message: '删除发布失败，请重试...'}).show();
		});
	}
	
	function showRecordContent(recordName, recordNum, recordContent){
		layui.use(['layer', 'form'], function() {
			var layer = layui.layer;
			layer.open({
	      		type: 1,
	      		title: recordName + '[' + recordNum + ']',
	      		area: ['600px', '360px'],
	      		shadeClose: true, //点击遮罩关闭
	      		content: "<div style='padding:20px;'>" + recordContent + "</div>"
	    	});
		});
	}
	
	$("#selectButton").click(function(){
		currentPage = 1;
		var searchValue = $("#selectValue").val();
		if(searchValue.length > 0){
			post(getHost() + "record/searchRecords",
			{
				searchText: $("#selectValue").val(),
				pageNumber:currentPage, 
				pageSize:pageSize,
			},
			function success(res){
				new Toast({message: res.msg}).show();
				if(res.code == '0'){
					setDataToView(res.data.list);
					totalPage = res.data.totalPage;
					currentPage = res.data.pageNumber;
					toPage();
				}
			}, function error(err){
				new Toast({message: "查询发布信息失败..."}).show();
				logErr("查询发布信息失败..." + err);
			});
		}else{
			new Toast({message: "请输入搜索内容"}).show()
		}
	});
	
	$("#clearSearch").click(function(){
   		var searchValue = $("#selectValue").val();
   		$("#selectValue").val("");
   		if(searchValue.length > 0){
	   		currentPage = 1;
	   		getRecords();
   		}
   	});
   	$("#a_reload").click(function(){
   		var searchValue = $("#selectValue").val();
   		$("#selectValue").val("");
   		
	   		currentPage = 1;
	   		getRecords();
   		
   	});
   	
});