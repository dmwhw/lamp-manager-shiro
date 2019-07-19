//初始化渲染菜单
function _initMenu(menus){
	var _menus=deepCopy(menus);
	if (!_menus){
		return ;
	}
	//先来个sort排序。
	var sortMenus=[];
	for (var key in _menus){
		var menu=_menus[key];
		if (sortMenus.length==0){
			sortMenus.push(menu);
			continue;
		}
		//
		for (var i=0;i<sortMenus.length;i++){
			var pm=sortMenus[i];
			if (menu.sort>=pm.sort){
				sortMenus.push(menu);
				break;
			}
		}
	}
	
	//获取parentMenu先。
	var parentMenu=[];
	for (var i=0;i<sortMenus.length;i++){
		var menu=sortMenus[i];
		if (menu.parentId+""=="0"){
			parentMenu.push(menu);
		}
		
	}
	
	//parent去查找是否有相关的menu
	//var html=$("<a/>").attr;
	for (var i=0;i<parentMenu.length;i++){
		var pm=parentMenu[i];
		var parentHtml=$("<li/>")
			.attr({"data-options":"state:'open'"})
			.append($("<span>").html(pm.name));
		var parentUL=$("<ul/>");
		for(var j=0;j<sortMenus.length;j++){
			var subMenu=sortMenus[j];
			if (subMenu.parentId!=pm.id){
				continue;
			}
			var a=$("<a/>").attr({"href":"javascript:void(0);"})
				.data("menu",JSON.stringify(subMenu))
				.html(subMenu.name)
				.attr({"onclick":"openOrShow('"+subMenu.id+"');return false;"});
			var li=$("<li/>").append(
								$("<span/>").append(
													a
													)
								);
			parentUL.append(li);
			sortMenus.splice(j--,1);
			
		}
		parentHtml.append(parentUL);
		$("#menuContent_").append(parentHtml);
	}
	//
	//$("#menuContent_").html(html);
}

//-----------------------------tab-----------------------------------------------
function _getMenuTabTitle(menuId){
	var menu=GVAR.menus[menuId];
	return menu.name+"-"+GVAR.menus[""+menu.parentId].name;
}

function openOrShow(menuId){
	var menu=GVAR.menus[menuId];
	var menuName=_getMenuTabTitle(menuId);
	//tab.通过menu id去
	if ($('#mainContent').tabs('exists',menuName)){
		$('#mainContent').tabs('select',menuName);
	} else {
		try{
			ajaxGetTabContent(menu.url,menuName);
		}catch(e){
			console.log(e);
		}
	}

}

function ajaxGetTabContent(url,menuName){
	  $.ajax({
			type : "get",
			url : GVAR.ctx+url + ".html",
			async:true,
			cache:true,
			error:function(request){
				//$.errorDialog("无法读取页面:" + fullUrl + ".html");
			},
			success:function(data){
				$('#mainContent').tabs('add',{
					title:menuName,
					content:data,
					closable:true,
					 tools:[{
					        iconCls:'icon-mini-refresh',
					        handler:function(){
					        	 reloadTabContent(url,menuName);
					        }
					    }]
				});
			}
	    });
		
}


function reloadTabContent(url,menuName){
	  $.ajax({
			type : "get",
			url : GVAR.ctx+url + ".html",
			async:true,
			cache:true,
			error:function(request){
				//$.errorDialog("无法读取页面:" + fullUrl + ".html");
			},
			success:function(data){
			    $('#mainContent').tabs("select", menuName);
			    var tab = $('#mainContent').tabs('getSelected'); // get selected panel
			    $('#mainContent').tabs('update', {
			        tab: tab,
			        options: {
			            title: menuName,
			            content:data
			        }
			    });
			}
	    });

}
//--------------------------------------------------------------------------------------------
$.ajaxSetup({
	  cache: true
	});


_initMenu(GVAR.menus);
//url
var url2Menu={}
for (var key in GVAR.menus){
	var menu=GVAR.menus[key];
	url2Menu[menu.url]=menu;
}
GVAR.url2Menu=url2Menu;

