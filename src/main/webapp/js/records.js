$(document).ready(function(){
	if(unlogin()){}
	
	var startAllAppoint = 0;	//开始页数
	var pageSize = 5;	//每页显示数据条数
	var currentPage = 1;	//当前页数
	var totalPage = 0;			//数据总条数
	var recordStatus = "";
	
	getRecords(recordStatus);
	
	function getRecords(recordStatus){
		post(getHost() + "sysUser/getRecords", 
			{pageNumber:currentPage, 
				pageSize:pageSize, recordStatus:recordStatus},
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
					"<th style='width:150px'>物料编码</th>" + 
					"<th style='width:120px'>物料名称</th>" + 
					"<th style='width:36px'>等级</th>" + 
					"<th style='width:30px'>状态</th>" + 
					"<th style='width:180px'>详情</th>" + 
					"<th style='width:120px'>发布者</th>" + 
					"<th style='width:130px'>更新时间</th>" + 
					"<th style='width:180px'>操作</th>" + 
				"</tr>";
				
	
				
		$.each(data, function(v, o) {
			s+='<tr><td>' + strFomcat(o.recordNum) + '</td>';
			s+='<td>' + strFomcat(o.recordName) + '</td>';
			s+='<td>' + strFomcat(o.recordLevel) + '</td>';
			if(strFomcat(o.recordStatus) == '0'){
				s+="<td><button id='btn_status_edit' data-real-name=" + o.recordName + " data-user-status=" + o.recordStatus + " data-user-id=" + o.recordId + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#xe605;</i>" +
						"</button>" +
						"</td>";
			}else{
				s+="<td><button id='btn_status_edit' data-real-name=" + o.recordName + " data-user-status=" + o.recordStatus + " data-user-id=" + o.recordId + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#x1006;</i>" +
						"</button>" +
						"</td>";
			}
			s+='<td>' + fomcatRecordContent(o.recordContent) + '</td>';
			s+='<td>' + strFomcat(o.userName) + '</td>';
			s+='<td>' + strFomcat(o.updateTime) + '</td>';
			s+="<td><div class='layui-btn-group'>" +
						"<button id='btn_show_img' data-imgs=" + o.recordImgUrl + " data-record-name='" + o.recordName + "' data-record-num=" + o.recordNum + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#xe60d;</i>" +
						"</button>" +
						"<button id='btn_user_info' data-record-id='" + o.recordId + "' data-record-content='" + getRecordContent(o.recordContent) + "' data-record-name=''" + o.recordName + "' data-record-num=" + o.recordNum + " class='layui-btn layui-btn-primary layui-btn-sm'>" +
							"<i class='layui-icon'>&#xe63c;</i>" +
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
				var recordId = $(this).attr("data-record-id");
				
				showRecordContent(recordId, recordName, recordNum, recordContent);
			});
			
			$("#t_customerInfo #btn_user_del").on("click", function(){
				var recordId = $(this).attr("data-record-id");
				delRecord(recordId);	
			});
			
			$("#t_customerInfo #btn_show_img").on("click", function(){
				var imgUrls = $(this).attr("data-imgs");
				var recordName = $(this).attr("data-record-name");
				var recordNum = $(this).attr("data-record-num");
				showImgsDialog(recordName, recordNum, imgUrls);	
			});
			
			$("#t_customerInfo #btn_status_edit").on("click", function(){
				var status = $(this).attr("data-user-status");
				var realName = $(this).attr("data-real-name");
				var userId = $(this).attr("data-user-id");
				enableRecordDialog(status == "1" ? "0" : "1", realName, userId);
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
				      	getRecords(recordStatus);//一定要把翻页的ajax请求放到这里，不然会请求两次。
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
	
	function showRecordContent(recordId, recordName, recordNum, recordContent){
		/*layui.use(['layer', 'form'], function() {
			var layer = layui.layer;
			layer.open({
	      		type: 1,
	      		title: recordName + '[' + recordNum + ']',
	      		area: ['600px', '360px'],
	      		shadeClose: true, //点击遮罩关闭
	      		content: "<div style='padding:20px;'><p>" + recordContent + "</p></div>"
	    	});
		});*/
		
		layer.prompt({title: (recordName + '[' + recordNum + ']'), formType: 2,
					area: ['600px', '360px'],
					value: recordContent}, function(text, index){
		    layer.close(index);
//		    sendSSystemMsg(userId, text);
			updateRecordContent(recordId, text);
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
	   		getRecords(recordStatus);
   		}
   	});
   	$("#a_reload").click(function(){
   		var searchValue = $("#selectValue").val();
   		$("#selectValue").val("");
   		
	   		currentPage = 1;
	   		getRecords(recordStatus);
   		
   	});
   	
   	function showImgsDialog(recordName, recordNum, imgs){
   		if(imgs != null && imgs != "" && imgs != "undefinded" && imgs != "null"){
   			var imgArray = new Array();
   			imgArray = imgs.split(",");
   			
   			var divs = "<div class='layui-carousel'><div carousel-item=''>";
   			for(var i = 0; i < imgArray.length; i++){
   				divs += "<div><img src=" + (getImgBaseUrl() + imgArray[i]) + "></div>";
   			}
   			divs += "</div></div>"
   			
   			layui.use("layer", function(){
				layui.layer.open({
					title:"图片浏览：" + recordName + "[" + recordNum + "]",
					type:1,
					content:"<div style='padding: 10px 10px;'>" + divs + "</div>",
					
					
					area: ['960px', '640px'],
					shadeClose:true,
					yes:function(){
						layui.layer.closeAll();
					}
				});
			});
   		}else{
   			new Toast({message: "该发布没有图片"}).show();	
   		}   		
   	}
   	
   	$("#btn_record_all").click(function(){
		$("#btn_record_all").removeClass("layui-btn-primary");
		$("#btn_record_enable").removeClass("layui-btn-primary");
		$("#btn_record_disable").removeClass("layui-btn-primary");
		
		$("#btn_record_enable").addClass("layui-btn-primary");
		$("#btn_record_disable").addClass("layui-btn-primary");
		recordStatus = "";
		getRecords(recordStatus);
	});
	$("#btn_record_enable").click(function(){
		$("#btn_record_all").removeClass("layui-btn-primary");
		$("#btn_record_enable").removeClass("layui-btn-primary");
		$("#btn_record_disable").removeClass("layui-btn-primary");
		
		$("#btn_record_all").addClass("layui-btn-primary");
		$("#btn_record_disable").addClass("layui-btn-primary");
		recordStatus = "0";
		getRecords(recordStatus);
	});
	$("#btn_record_disable").click(function(){
		$("#btn_record_all").removeClass("layui-btn-primary");
		$("#btn_record_enable").removeClass("layui-btn-primary");
		$("#btn_record_disable").removeClass("layui-btn-primary");
		
		$("#btn_record_enable").addClass("layui-btn-primary");
		$("#btn_record_all").addClass("layui-btn-primary");
		recordStatus = "1";
		getRecords(recordStatus);
	});
	
	function enableRecordDialog(status, realName, recordId){
		layui.use("layer", function(){
			layui.layer.open({
				title:(status == '0' ? "启用" : "禁用"),
				type:1,
				content:"<div style='padding: 20px 80px;'>是否" + (status == '0' ? "启用" : "禁用") + realName + "</div>",
				btn:"重置",
				btnAlign:"c",
				shade:0,
				yes:function(){
					layui.layer.closeAll();
					updateStatus(status, recordId);
				}
			});
		});
	}
	function updateStatus(status, recordId){
		post(getHost() + "sysUser/updateRecordStatus", 
			{recordId: recordId,
			recordStatus: status
			},
			function success(res){
				new Toast({message: res.msg}).show();
				currentPage = 1;
	   			getRecords(recordStatus);
			}, function error(){
				new Toast({message: "操作失败，请重试..."}).show();
		});
	}
	
	function updateRecordContent(recordId, recordContent){
		post(getHost() + "sysUser/updateContent",
		{recordId: recordId, recordContent: recordContent}, function success(res){
			new Toast({message: res.msg}).show();
		}, function error(err){
			new Toast({message: "更新失败，请重试..."}).show();
		});
	}
});