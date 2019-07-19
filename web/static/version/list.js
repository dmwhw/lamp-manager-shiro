
var versionList={

		btnFormatter:function (val,row,index){
			return "<a href='#' onclick='versionList.viewFirmFiles("+row.id+","+row.status+")'>显示固件列表</a>"+
			(row.status==0?"&nbsp; <a href='#' onclick='versionList.viewFirmFiles("+row.id+")'>发布</a>":"");
			
		},
		updateTypeFormatter:function (val,row,index){
			if (row.type==0){
				return "无需更新";
			}else if (row.type==1){
				return "可选更新";

			}else if (row.type==2){
				return "建议更新";

			}else if (row.type==4){
				return "公测版本";
			}
		},
		statusFormatter:function (val,row,index){
			if (row.status==0){
				return "提交";
			}else if (row.status==1){
				return "已发布";

			}
		},
		//////////////////////////////////////////////////////////////////////////
		viewFirmFiles:function (versionId,status){	
			$("#firmFileListWindow #versionId").val(versionId);
			$("#firmFileListWindow #status").val(status);

			$('#firmFileListWindow').window({
				title:"固件文件列表",
			    modal:true
			});

			versionList.refreshFirmFileList();
		},
 
		 

		//初始化表格
		initDataGrid:function (){
			$('#versionListTab').datagrid({
			     pageList:[10,15,20,25,30],
			     pageSize:20,
			     pagination:true,
			     loadMsg:"加载中..."
			});
			$('#versionListTab').datagrid('getPager').pagination({
			    layout:['list','sep','first','prev','sep',$('#versionListTab #p-style').val(),'sep','next','last','sep','refresh','info']
			});
			 
			$('#firmFileTab').datagrid({
			     pageList:[10,15,20,25,30],
			     pageSize:20,
			     pagination:true,
			     loadMsg:"加载中...",
			     columns:[[
					{field:'versionId',title:'版本id' },
			        {field:'name',title:'名称' },
			        {field:'firmwareFileType',title:'fileType(hex)' ,formatter:intToHexFormatter},
			        {field:'firmwareFileIndex',title:'fileIndex(hex)' ,formatter:intToHexFormatter},
			        {field:'checkSum',title:'checksum(hex)',formatter:intToHexFormatter },
			        {field:'fileLength',title:'大小(byte)', formatter:intToHexFormatter},
			        {field:'flashAddr',title:'写入地址(hex)',formatter:intToHexFormatter },
			        {field:'uploadDate',title:'上传时间',formatter:timestampToyyyyMMddHHmmssFormatter},
			        {field:'userId',title:'userId' },
			        {field:'remark',title:'备注',width:20}
			    ]]
			});
			$('#firmFileTab').datagrid('getPager').pagination({
			    layout:['list','sep','first','prev','sep',$('#firmFileTab #p-style').val(),'sep','next','last','sep','refresh','info']
			});


		},

		 
		//刷新版本列表pageBean
		refreshVersionList : function (){
			var queryParams = {};
			if($("#start").val())
			queryParams.start = $("#start").val();
			if($("#end").val())
			queryParams.end = $("#end").val();
			if($("#versionListToolbar #status").val())
			queryParams.status = $("#status").val();
			$('#versionListTab').datagrid({
				url : GVAR.ctx + "api/web/software-version/pagebean",
				pagePosition:$('#versionListTab #p-pos').val(),
				queryParams: queryParams,
				fitColumns:true,
			    emptyMsg:'无数据显示',
			    columns:[[
			   	        {field:'id',title:'版本id' },
			   	        {field:'modelNoId',title:'型号id'  },
			   	        {field:'versionName',title:'版本名称'  },
			   	        {field:'mainVersion',title:'主版本'  },
			   	        {field:'subVersion',title:'子版本'  },
			   	        {field:'androidMinVersion',title:'安卓最低版本'  },
			   	        {field:'iosMinVersion',title:'IOS最低版本'  },
			   	        {field:'type',title:'更新选项',formatter: versionList.updateTypeFormatter  },
			   	        {field:'status',title:'状态',formatter:versionList.statusFormatter },
			   	        {field:'postTime',title:'提交时间' ,formatter:timestampToyyyyMMddHHmmssFormatter},
			   	     	{field:'publishDate',title:'发布日' ,formatter:timestampToyyyyMMddFormatter },
			   	 		{field:'_operate',title:'操作',formatter: versionList.btnFormatter  },
			   	  		{field:'remark',title:'版本说明'  }
		 	   	    ]]
			});

		},

		//刷新固件文件列表
		refreshFirmFileList : function (){
			var queryParams = {};
			queryParams.versionId = $("#firmFileListWindow #versionId").val();
		 	var toolbar=null;
			if (''+$("#firmFileListWindow #status").val()!='1'){
				toolbar=versionList.firmFiletoolbar
			}
		 	$('#firmFileTab').datagrid({
				toolbar : toolbar,
			    emptyMsg:'无数据显示',
				url : GVAR.ctx + "api/web/firm-file/pagebean",
				pagePosition:$('#firmFileListWindow #p-pos').val(),
				queryParams: queryParams,
			});

		},
		fnAddFirmFile:function(){
			$(addFirmFileForm).form("enableValidation");
			var result=$(addFirmFileForm).form("validate");
			if (result){
				var formdata=new FormData(addFirmFileForm);
				 $.ajax({
						type:"POST",
						url:   GVAR.ctx+"api/web/firm-file/add",					
						data: formdata,
						async:true,
						contentType: false,
						processData: false,
						error:function(request){
							
						},
						success:function(data){
							versionList.refreshFirmFileList();
							$(addFirmFileForm)[0].reset();
							$("#addFirmFileDialog").dialog('close');
							
						}
					
					});
			}
			
		},
		fnEditFirmFile:function(){
			$(addFirmFileForm).form("enableValidation");
			var result=$(editFirmFileForm).form("validate");
			if (result){
				var formdata=new FormData(editFirmFileForm);
				 $.ajax({
						type:"POST",
						url:   GVAR.ctx+"api/web/firm-file/edit",					
						data: formdata,
						async:true,
						contentType: false,
						processData: false,
						error:function(request){
							
						},
						success:function(data){
							versionList.refreshFirmFileList();
							$(editFirmFileForm)[0].reset();
							$("#editFirmFileDialog").dialog('close');
							
						}
					
					});
			}
			
		},
		//删除版本
		fnDelVersionDialog : function(){
			var rowData=$("#versionListTab").datagrid('getSelected');
			if (rowData ){
				if (rowData.status!=0){
					$.errMsg("该版本非提交状态，不可删除");
					return ;
				}
				$.messager.confirm({
					title: '询问',
					msg: '确定要删除版本'+(rowData.versionName?('['+rowData.versionName+"]"):'')+'吗?',
					fn: function(r){
						if (r){
							 $.ajax({
										type:"POST",
										url:   GVAR.ctx+"api/web/software-version/del",					
										data: {versionId:rowData.id},
										async:true,
										error:function(request){
											
										},
										success:function(data){
											versionList.refreshVersionList();
										}
									
									});
						}
					}
				});
			}
		},
		uiLoadDeviceNoList : /**
		 * 获取版本列表，作为comobobox输入
		 */
		function(comboboxId){
			 $.ajax({
					type:"POST",
					url:   GVAR.ctx+"api/web/device-no/list",					
					async:true,
					error:function(request){
						
					},
					success:function(data){
						var list=data.data.list;
						var size=data.data.size;
						if (size>0){
							$(comboboxId).combobox({
								valueField:'id',
								textField:'deviceName',
								data: list
							});
						}
					}
				
				});
		},
		uiLoadAppVersionList : /**
			 * 获取APP版本，作为comobobox输入
			 */
			function(iosComoboBoxId,androidComoboBoxId){
				 $.ajax({
						type:"POST",
						url:   GVAR.ctx+"/api/web/app-version/lists-by-system",					
						async:true,
						error:function(request){
							
						},
						success:function(data){
							var ios=data.data.ios;
							var android=data.data.android;
							
							$(iosComoboBoxId).combobox({
								valueField:'id',
								textField:'versionName',
								data: ios
							});
							$(androidComoboBoxId).combobox({
								valueField:'id',
								textField:'versionName',
								data: android
							});
						
						}
					
					});
			},
		
		
		//-----------------------------------------------firmFileListWindowToolbar start ------------------------
		//是否加载了版本添加框、版本编辑框的选择内容
		isVersionComboboxLoad:false,
		uiInitVersionCombobox:
		/**
		 * 加载了版本添加框、版本编辑框的选择内容
		 */
		function(){
			if (!this.isVersionComboboxLoad){
				//请求台灯型号
				//请求安卓版本
				//请求苹果版本
				this.uiLoadDeviceNoList("#addVersionDialog #modelNoId");
				this.uiLoadAppVersionList("#addVersionDialog  #iosMinVersion","#addVersionDialog #androidMinVersion");

			}
		},
		uiShowEditVersionDialog : function(){
			this.uiInitVersionCombobox();
			var rowData=$("#versionListTab").datagrid('getSelected');
			if (rowData){
				$(editVersionForm)[0].reset();
				$("#editVersionDialog").dialog({
					title: '编辑版本',
					iconCls:'icon-add',
					closed: true,
					modal: true
				});
				console.log(rowData);
				$("#editVersionDialog #versionId").val(rowData.id);
				$("#editVersionDialog #modelNoId").combobox('setValue',rowData.modelNoId);
				$("#editVersionDialog #name_").textbox('setValue',rowData.versionName);
				$("#editVersionDialog #mainVersion").numberspinner('setValue',rowData.mainVersion);
				$("#editVersionDialog #mainVersion").numberspinner('setValue',rowData.mainVersion);
				$("#editVersionDialog #subVersion").numberspinner('setValue',rowData.subVersion);
				$("#editVersionDialog #androidMinVersion").combobox('setValue',rowData.androidMinVersion);
				$("#editVersionDialog #iosMinVersion").combobox('setValue',rowData.iosMinVersion);
				$("#editVersionDialog #type").combobox('setValue',rowData.type);
				$("#editVersionDialog #remark").textbox('setValue',rowData.remark);

				$("#editVersionDialog").dialog('open');

			}else{
				$.errMsg ("请选择版本");
			}

		},
		uiShowAddVersionDialog : function(){
			this.uiInitVersionCombobox();

			$(addVersionForm)[0].reset();
			$("#addVersionDialog").dialog({
			    title: '增加版本',
			    iconCls:'icon-edit',
			    closed: true,
			    modal: true
			});
			$("#addVersionDialog").dialog('open');

		},
		uiShowAddFirmFileDialog : function (){
			addFirmFileForm.reset();
			$(addFirmFileForm).form("disableValidation");
			$("#addFirmFileDialog").dialog({
			    title: '增加固件文件',
			    iconCls:'icon-add',
			    closed: true,
			    modal: true
			});
			$("#addFirmFileDialog  #versionId").val($("#firmFileListWindow #versionId").val());
			$("#addFirmFileDialog").dialog('open');
			//$(addFirmFileForm).form("disableValidation");

		},
		uiShowEditFirmFileDialog:function (){
			
			var rowData=$("#firmFileTab").datagrid('getSelected');
			if (rowData){
				$("#editFirmFileDialog").dialog({
				    title: '编辑固件文件',
				    iconCls:'icon-add',
				    closed: true,
				    modal: true
				});
				$(editFirmFileForm)[0].reset();
				$("#editFirmFileDialog #firmFileId").val(rowData.id);

				$("#editFirmFileDialog #versionId").val(rowData.versionId);
				$("#editFirmFileDialog #name_edit").textbox( "setValue",rowData.name );
				$("#editFirmFileDialog #fileIndex_edit").textbox("setValue",intToHexFormatter(rowData.firmwareFileIndex,null,null ));
				$("#editFirmFileDialog #fileType_edit").textbox("setValue",intToHexFormatter(rowData.firmwareFileType,null,null ));
				$("#editFirmFileDialog #flashAddr_edit").textbox("setValue",intToHexFormatter(rowData.flashAddr,null,null ));
				$("#editFirmFileDialog #remark_edit").textbox("setValue",rowData.remark);
				$("#editFirmFileDialog").dialog('open');
				
			}
			
		},
		uiShowDelFirmFileConfirmDialog:function(){
			var rowData=$("#firmFileTab").datagrid('getSelected');
			if (rowData){
				$.messager.confirm({
					title: '询问',
					msg: '确定要删除当前文件'+(rowData.name?('['+rowData.name+"]"):'')+'吗',
					fn: function(r){
						if (r){
							 $.ajax({
										type:"POST",
										url:   GVAR.ctx+"api/web/firm-file/del",					
										data: {firmFileId:rowData.id},
										async:true,
										error:function(request){
											
										},
										success:function(data){
											versionList.refreshFirmFileList();
										}
									
									});
						}
					}
				});
			}
		},
		//------------------------------ui----------------
		//改变表格宽高
		domresize:function (){
			$('#mainContent').tabs('resize',{ 
			 	height:$("#mainContent").height(),
				width:$("#mainContent").width()
			});
				
			$('#versionListTab').datagrid('resize',{ 
				height:$("#mainContent").height()-$("#mainContent").children(":first").height()-5,
				width:$("#mainContent").width()
			});
		 }
	
// 		,
// 		firmFiletoolbar :
// 			[
// 			  {text:'Add',id:"firmFileAddBtn",iconCls:'icon-add', handler: function(){versionList.uiShowAddFirmFileDialog()} }, 
// 			  {text:'Edit',id:"firmFileEditBtn",iconCls:'icon-edit',handler: function(){versionList.uiShowEditFirmFileDialog()}},
// 			  {text:'删除',id:"firmFileDelBtn", iconCls:'icon-remove',handler: function(){versionList.uiShowDelFirmFileConfirmDialog()}}
// 			]
};

versionList.firmFiletoolbar =
	[
	  {text:'Add',id:"firmFileAddBtn",iconCls:'icon-add', handler: versionList.uiShowAddFirmFileDialog   }, 
	  {text:'Edit',id:"firmFileEditBtn",iconCls:'icon-edit',handler: versionList.uiShowEditFirmFileDialog},
	  {text:'删除',id:"firmFileDelBtn", iconCls:'icon-remove',handler: versionList.uiShowDelFirmFileConfirmDialog}
	];
	


versionList.initDataGrid();
versionList.refreshVersionList();
addResizeFunction(versionList.domresize);
//-----------------------------------------------firmFileListWindowToolbar start ------------------------

