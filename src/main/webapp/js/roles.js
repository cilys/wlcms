$(document).ready(function(){
	
	
	getRoles();
	
	function getRoles(){
		post(
			getHost() + "role/getRoles",
			{},
			function success(res){
				log("获取角色列表：" + res.msg);
				layui.use('layer', function(){
					layer.msg("获取角色列表：" + res.msg);
				});
				
				
				if(res.code == 0){
					addRolesToTable(res.data.list);
				}
			}, function error(err){
				log("获取角色列表失败：" + err);
				layui.use('layer', function(){
					layer.msg("获取角色列表失败：" + err);
				});
			}
		)
	}
	
	function addRolesToTable(res){
		layui.use('table', function(){
			var table = layui.table;
			
			table.render({
				elem: '#id_table_roles',
				cols:[[
					{field: 'roleName', title:'角色名称'},
					{field:'createTime', title:'创建时间'},
					{field:'updateTime', title:'更新时间'}
				]],
				data:res
			});
		});
	}
});