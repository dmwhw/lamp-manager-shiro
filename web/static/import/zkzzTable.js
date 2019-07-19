jQuery.fn.createTable = function(data,form){
	zkzzTable.createTable(this,data,form);
	$(this).data("value",data);
	$(this).data("form",form);
};

jQuery.fn.tableLoading = function(){
	zkzzTable.tableLoading(this);
};

var zkzzTable = {
		defaultValue : "--",
		$:function(ele){   
			if(typeof(ele)=="object")   
				return ele;   
			else if(typeof(ele)=="string"||typeof(ele)=="number")   
				return $(ele.toString());   
			return ele;   
		},    
		getValOfJson : function(json,key){
			if(key.indexOf(".") > 0){
				var keys = key.split(".");
				for(var i = 0; i<keys.length;i++){
					json = json[keys[i]];
					//if(!json&&json!=0){
					if(json == undefined){
						return this.defaultValue;
					}
				}
				return json;
			}
			//if(!json[key]&&json[key]!=0){
			if(json[key] == undefined){
				return this.defaultValue;
			}
			return json[key];
		},
		htmlEncode : function(str){
			return $('<span/>').text(str).html();
		},
		getValOfFormat : function(col,row,zFormat,index,data){
			if(zFormat){
				return eval(zFormat+"(col,row,index,data)");
			}
			return col;
		},
		cheakWidth : function(col,ltd){
			var sp = $("<span style='display: none;'>"+col+"</span>");
			ltd.append(sp);
			spw = sp.width()+1;
			sp.remove();
			return spw+1;
		},
		getToolStyle : function(ztf,content){
			//col = '<a style="padding: 0px;" data-toggle="tooltip" data-html="false" data-placement="bottom" title="'+ztf+'">'+temp+'</a>';
			return '<a class="zkzzTip" onmouseover="tipsBox.start(this)" tips="'+encodeURI(ztf)+'">'+content+'</a>';
		},
		getTooltip : function(col,row,zTooltipFormat,index,data,zTooltip,ltd,zTooltipDis){
			if(zTooltip){
				var ztf = this.getTooltipFormat(col, row, zTooltipFormat, index, data);
				if(zTooltip=="auto"){
					this.autoTooltip(col, ztf, zTooltip, ltd, zTooltipDis);
					return this.calculationTooltip(col, ztf, ltd, zTooltipDis);
				}else if(col.length > zTooltip || zTooltipDis){
					var content;
					if(col.length > zTooltip){
						content = col.substring(0, zTooltip-3)+"...";
					}else{
						content = col;
					}
					col = this.getToolStyle(ztf, content);
					return col;
				}
			}
			return col;
		},
		getTooltipFormat : function(col,row,zTooltipFormat,index,data){
			if(zTooltipFormat){
				return eval(zTooltipFormat+"(col,row,index,data)");
			}else{
				return col;
			}
		},
		calculationTooltip : function(col,ztf,ltd,zTooltipDis){
			var thw = ltd.width();
			var spw = this.cheakWidth(col,ltd);
			if(thw < spw || zTooltipDis){
				var content;
				if(thw < spw){
					content = col + "...";
				}else{
					content = col;
				}
				while(thw < spw && thw !=0 ){
					content = content.substring(0,content.length-4)+"...";
					spw = this.cheakWidth(content,ltd);
				}
				col = this.getToolStyle(ztf, content);
			}
			return col;
		},
		autoTooltip : function(col,ztf,zTooltip,ltd,zTooltipDis){
			var oldonresize = window.onresize;
			window.onresize = function(){
				if(oldonresize){
					oldonresize();
				}
				if(ltd.resizeTimer){
					clearTimeout(ltd.resizeTimer);  
				}
				ltd.resizeTimer = setTimeout(function(){
					zkzzTable.autoTooltip2(col,ztf,ltd,zTooltipDis);
				},10);  
			};
		},
		autoTooltip2 : function(col,ztf,ltd,zTooltipDis){
			col = this.calculationTooltip(col,ztf,ltd,zTooltipDis);
			ltd.html(col);
			this.activeTooltip(ltd);
		},
		activeTooltip : function(ltd){
			var tooltops = ltd.find("[data-toggle='tooltip']");
			if(tooltops.length > 0){
				try{
					tooltops.tooltip();
				}catch(e){
					console.log(e);
				}
			}
		},
		createTbody : function(table){
			var thead = table.find("thead");
			var tbody = table.find("tbody");
			if(tbody.length==0){
				thead.after("<tbody></tbody>");
				tbody = table.find("tbody");
			}
			return tbody;
		},
		createTable : function (table,data,form){
			data = this.htmlEncode(JSON.stringify(data));
			data = JSON.parse(data);
			var table = this.$(table);
			var form = this.$(form);
			var thead = table.find("thead");
			var th = table.find("th");
			var rowNum = th.length;
			var tbody = this.createTbody(table,tbody);
			var thzcolNames = [];
			var thzFormats = [];
			var thzEvents = [];
			var thzTooltips = [];
			var thzTooltipsDis = [];
			var thzTooltipsFormat = [];
			for(var i = 0; i < th.length; i++){
				thzcolNames.push($(th[i]).attr("zcolName"));
				thzFormats.push($(th[i]).attr("zFormat"));
				thzEvents.push($(th[i]).attr("zEvents"));
				thzTooltips.push($(th[i]).attr("zTooltip"));//auto
				thzTooltipsDis.push($(th[i]).attr("zTooltipDis"));//显示
				thzTooltipsFormat.push($(th[i]).attr("zTooltipFormat"));
			}
			tbody.html("");
			for(var i = 0; i<data.list.length; i++){
				tbody.append("<tr></tr>");
				var ltr = tbody.find("tr:last");
				for(var j = 0; j < th.length; j++){
					var cVal = "";
					ltr.append("<td></td>");
					var ltd = ltr.find("td:last");
					var ele = "";
					if(thzcolNames[j]){
						cVal = this.getValOfJson(data.list[i],thzcolNames[j]);
						var cFVal = this.getValOfFormat(cVal, data.list[i],thzFormats[j],i,data);
						cFVal = this.getTooltip(cFVal,data.list[i],thzTooltipsFormat[j],i,data,thzTooltips[j],ltd,thzTooltipsDis[j]);
						ele = ele + cFVal;
					}
					ltd.append(ele);
					this.activeTooltip(ltd);
					this.tableEvents(thzEvents[j],ltr,cVal,data.list[i],i,data);
				}
			}
			if(data.list.length == 0){
				this.tableDataNA(table);
			}
			this.createPageBar(table,data,form);
		},
		tableLoading : function(table,title){
			title = title ? title : "加载中..";
			var table = this.$(table);
			var tbody = this.createTbody(table,tbody);
			var th = table.find("th");
			var rowNum = th.length;
			var ele = "";
			ele = ele + "<tr>";
			ele = ele + "<td colspan=\""+rowNum+"\">";
			ele = ele + '<div class="none-data">';
			ele = ele + "<strong></strong><span>"+title+"</span>";
			ele = ele + "</div>";
			ele = ele + "</td>";
			ele = ele + "</tr>";
			tbody.html(ele);
		},
		tableDataNA : function(table,title){
			title = title ? title : "没有数据";
			var table = this.$(table);
			var tbody = this.createTbody(table,tbody);
			var th = table.find("th");
			var rowNum = th.length;
			var ele = "";
			ele = ele + "<tr>";
			ele = ele + "<td colspan=\""+rowNum+"\">";
			ele = ele + '<div class="none-data">';
			ele = ele + "<span>"+title+"</span>";
			ele = ele + "</div>";
			ele = ele + "</td>";
			ele = ele + "</tr>";
			tbody.html(ele);
		},
		tableEvents : function(event,ltr,col,row,index,data){
			if(event){
				var events = eval(event);
				if(events){
					for (var key in events) {
						for(var ob in events[key]){
							var tag = ltr.find("td:last "+ob);
							var fn = events[key][ob];
							if(typeof(fn)=="function"){
								var uuid=UUID.prototype.createUUID();
								window[uuid] = events[key][ob];
								tag.bind(key,{"uuid":uuid,"col":col,"row":row,"index":index,"data":data},zkzzTable.tempFn);
							}else{
								var fun2 = new Function("eval("+events[key][ob]+"("+col+"))");
								tag.bind(key,fun2);
							}
						}
					}
				}
			}
		},
		tempFn : function(event){
			var data = event.data;
			window[data.uuid](data.col,data.row,data.index,data.data);
		},
		createPageBar : function(table,data,form){
			var ele = '<div class="table-footer">';
			ele = ele + '<div class="btn-group btn-group-sm pull-right">';
			ele = ele + '<button id="pageBarPageLeft" type="button" class="btn btn-default"><span class="glyphicon glyphicon-chevron-left"></span></button>';
			ele = ele + '<button id="pageBarPageRight" type="button" class="btn btn-default"><span class="glyphicon glyphicon-chevron-right"></span></button>';
			ele = ele + '</div>';
			ele = ele + '<div class="pull-right table-prompt">';
			ele = ele + '第'+(data.pageStart)+'-'+data.pageEnd+'项，共'+data.totalPage+'页';
			ele = ele + '</div>';
			ele = ele + '<div class="pull-right table-jump">';
			ele = ele + '转到<input id="pageBarPageIndex" type="text" value="'+data.pageIndex+'">页';
			ele = ele + '</div>';
			ele = ele + '<div class="pull-right table-pages">';
			ele = ele + '显示行数：';
			ele = ele + '<select id="pageBarPageSize">';
			ele = ele + '<option value ="20">20</option>';
			ele = ele + '<option value ="25">25</option>';
			ele = ele + '<option value ="30">30</option>';
			ele = ele + '<option value ="35">35</option>';
			ele = ele + '</select>';
			ele = ele + '</div>';
			ele = ele + '</div>';
			if(table.parent("div").next().hasClass("table-footer")){
				table.parent("div").next().remove();
			}
			table.parent("div").after(ele);
			var pageBar = table.parent("div").next();
			pageBar.find("#pageBarPageIndex").bind("keydown",function(e){
				if(e.keyCode==13){
					var pageIndex = pageBar.find("#pageBarPageIndex").val();
					var pageSize = pageBar.find("#pageBarPageSize").val();
					zkzzTable.submit(form, pageIndex, pageSize);
				}
			});
			pageBar.find("#pageBarPageSize").bind("change",function(){
				var pageSize = pageBar.find("#pageBarPageSize").val();
				var pageIndex = pageBar.find("#pageBarPageIndex").val();
				zkzzTable.submit(form, pageIndex, pageSize);
			});
			pageBar.find("#pageBarPageLeft").bind("click",function(){
				var pageSize = pageBar.find("#pageBarPageSize").val();
				var pageIndex = pageBar.find("#pageBarPageIndex").val();
				zkzzTable.submit(form, Number(pageIndex)-1, pageSize);
			});
			pageBar.find("#pageBarPageRight").bind("click",function(){
				var pageSize = pageBar.find("#pageBarPageSize").val();
				var pageIndex = pageBar.find("#pageBarPageIndex").val();
				zkzzTable.submit(form, Number(pageIndex)+1, pageSize);
			});
			pageBar.find("option[value='"+data.pageSize+"']").prop("selected",true);
			if(data.pageIndex==data.totalPage){
				pageBar.find("#pageBarPageRight").prop("disabled",true);
			}
			if(data.pageIndex==1){
				pageBar.find("#pageBarPageLeft").prop("disabled",true);
			}
		},
		formGetReady : function(form,pageIndex,pageSize){
			var oPageIndex = form.find("[name='pageIndex']");
			var oPageSize = form.find("[name='pageSize']");
			if(oPageIndex.length==1){
				oPageIndex.val(pageIndex);
			}else{
				form.append("<input name='pageIndex' id='pageIndex' type='hidden' value='"+pageIndex+"'>");
			}
			if(oPageSize.length==1){
				oPageSize.val(pageSize);
			}else{
				form.append("<input name='pageSize' id='pageSize' type='hidden' value='"+pageSize+"'>");	
			}
		},
		submit : function(form,pageIndex,pageSize){
			if(typeof(form)=="function"){
				form(pageIndex,pageSize);
			}else{
				zkzzTable.formGetReady(form,pageIndex,pageSize);
				form.submit();
			}
		},
		setDefaultValue : function(defaultValue){
			this.defaultValue = defaultValue;
		}
};

