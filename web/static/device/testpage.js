var testPage = {
    /**
     * 图像的websocket传输对象
     */
    ws: null,
    /**
     * 初始化表格
     */
    initDataGrid: function () {
        $('#deviceTestListTab').datagrid({
            pageList: [10, 15, 20, 25, 30],
            pageSize: 20,
            pagination: true,
            loadMsg: "加载中...",
            onBeforeLoad: function () {
                var a = arguments;
            },
            columns: [[
                {field: 'id', title: 'id'},
                {field: 'deviceNickName', title: '设备名称'},
                {field: 'snNo', title: '序列号'},
                {field: 'mac', title: 'Mac地址'},
                {field: 'deviceUuid', title: 'deviceUuid'},

                {field: 'isOnLine', title: '在线状态', formatter: testPage.uiOnLineFormatter, align: 'center'},
                {field: 'status', title: '测试状态', formatter: testPage.uiTestStatusFormatter},
                {field: 'lastTestDate', title: '上次测试时间', formatter: timestampToyyyyMMddHHmmssFormatter},
                {field: 'testDoneDate', title: '测试完成时间', formatter: timestampToyyyyMMddHHmmssFormatter},
                {field: 'remark', title: '备注'}
            ]]
        });
        $('#deviceTestListTab').datagrid('getPager').pagination({
            layout: ['list', 'sep', 'first', 'prev', 'sep', $('#versionListTab #p-style').val(), 'sep', 'next', 'last', 'sep', 'refresh', 'info']
        });


    },
    /**
     * 输出结果
     */
    fnExportResult: function () {
        var queryParams = {};
        if ($("#deviceTestListTabToolBar #start").val())
            queryParams.start = $("#deviceTestListTabToolBar #start").val();
        if ($("#deviceTestListTabToolBar #end").val())
            queryParams.end = $("#deviceTestListTabToolBar #end").val();
        if ($("#deviceTestListTabToolBar #status").val())
            queryParams.status = $("#deviceTestListTabToolBar #status").val();
        if ($("#deviceTestListTabToolBar #deviceNickName").val())
            queryParams.deviceNickName = $("#deviceTestListTabToolBar #deviceNickName").val();
        var params = Object.keys(queryParams).map(function (key) {
            // body...
            return encodeURIComponent(key) + "=" + encodeURIComponent(queryParams[key]);
        }).join("&");
        var url = GVAR.ctx + "api/web/device-test/export?a=1&" + params;
        var form = $("<form></form>").attr("action", url).attr("method", "post");
        form.appendTo('body').submit().remove();
    },
    /**
     * 修改音量
     * @param thiz
     * @param flag
     */
    fnAddVoiceLevel: function (thiz, flag) {
        var parentTD = $(thiz).parent().parent();
        var data = $(parentTD).data("data");
        var postData = {};
        var voiceLevel;
        var dom;
        if (flag == 'add') {
            dom = $(thiz).next();
            voiceLevel = parseInt($(dom).html());
            voiceLevel++;
        } else {
            dom = $(thiz).prev();
            voiceLevel = parseInt($(dom).html());
            voiceLevel--;
        }
        if (voiceLevel > 5) {
            voiceLevel = 5;
        }
        if (voiceLevel < 0) {
            voiceLevel = 0;
        }
        $(dom).html(voiceLevel)
        postData.voiceLevel = voiceLevel;
        postData.deviceUuid = data.deviceUuid;
        $(thiz).linkbutton('disable');

        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/adjust-voice",
            data: postData,
            async: true,
            error: function (request) {
                $(thiz).linkbutton('enable');

            },
            success: [function (data) {

                $(thiz).linkbutton('enable');
                $(parentTD).find("#testPlane").show();

            }, function (data) {
                $(thiz).linkbutton('enable');
                $(parentTD).find("#testPlane").show();


            }]

        });
    },
    /**
     * 修改亮度
     * @param thiz
     * @param flag
     */
    fnchangeBrightnessLevel: function (thiz, flag) {
        var parentTD = $(thiz).parent().parent();
        var data = $(thiz).parent().parent().data("data");
        var postData = {};
        var level;
        var dom;
        if (flag == 'add') {
            dom = $(thiz).next();
            level = parseInt($(dom).html());
            level++;
        } else {
            dom = $(thiz).prev();
            level = parseInt($(dom).html());
            level--;
        }
        if (level > 6) {
            level = 6;
        }
        if (level < 0) {
            level = 0;
        }
        $(dom).html(level)
        postData.brightnessLevel = level;
        postData.deviceUuid = data.deviceUuid;
        $(thiz).linkbutton('disable');

        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/adjust-brightness",
            data: postData,
            async: true,
            error: function (request) {
                $(thiz).linkbutton('enable');

            },
            success: [function (data) {
                $(parentTD).find("#testPlane").show();

                $(thiz).linkbutton('enable');


            }, function (data) {
                $(parentTD).find("#testPlane").show();

                $(thiz).linkbutton('enable');


            }]

        });
    },
    /**
     * 修改灯光的色温
     * @param thiz
     * @param flag
     */
    fnchangeLightMode: function (thiz, flag) {
        var data = $(thiz).parent().parent().data("data");
        var parentTD = $(thiz).parent().parent();

        var postData = {};
        var level;
        var dom;
        if (flag == 'add') {
            dom = $(thiz).next();
            level = parseInt($(dom).html());
            level++;
        } else {
            dom = $(thiz).prev();
            level = parseInt($(dom).html());
            level--;
        }
        if (level > 8) {
            level = 8;
        }
        if (level < 0) {
            level = 0;
        }
        $(dom).html(level)
        postData.lightMode = level;
        postData.deviceUuid = data.deviceUuid;
        $(thiz).linkbutton('disable');

        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/adjust-lightMode",
            data: postData,
            async: true,
            error: function (request) {
                $(thiz).linkbutton('enable');

            },
            success: [function (data) {
                $(thiz).linkbutton('enable');
                $(parentTD).find("#testPlane").show();


            }, function (data) {
                $(thiz).linkbutton('enable');
                $(parentTD).find("#testPlane").show();


            },
            ]

        });
    },
    /**
     * 刷新测试设备列表
     */
     refreshDeviceTestList: function () {
        var queryParams = {};
        if ($("#deviceTestListTabToolBar #start").val())
            queryParams.start = $("#deviceTestListTabToolBar #start").val();
        if ($("#deviceTestListTabToolBar #end").val())
            queryParams.end = $("#deviceTestListTabToolBar #end").val();
        if ($("#deviceTestListTabToolBar #status").val())
            queryParams.status = $("#deviceTestListTabToolBar #status").val();
        if ($("#deviceTestListTabToolBar #deviceNickName").val())
            queryParams.deviceNickName = $("#deviceTestListTabToolBar #deviceNickName").val();

        $('#deviceTestListTab').datagrid({
            url: GVAR.ctx + "api/web/device-test/pagebean",
            pagePosition: $('#versionListTab #p-pos').val(),
            queryParams: queryParams,
            fitColumns: true,
            emptyMsg: '无数据显示'
        });
        $('#deviceTestListTab').datagrid('hideColumn', 'id');


    },
    /**
     * 发送语音
     * @param thiz
     */
    fnSendVoice: function (thiz) {
        var parentTD = $(thiz).parent().parent();
        var btn = $(thiz);
        var id = $(thiz).attr("id");
        var data = $(thiz).parent().parent().data("data");
        var index = $(thiz).parent().parent().data("index");
        var postData = {};
        postData.deviceUuid = data.deviceUuid;
        postData.index = index;
        $(thiz).linkbutton({"text": "发送中..."});
        $(thiz).linkbutton('disable');
        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/sendVoice",
            data: postData,
            async: true,
            error: function (request) {
                $(thiz).linkbutton('enable');
                $(thiz).linkbutton({"text": "发送语音"});

            },
            success: [function (data) {
                $(thiz).linkbutton('enable');
                $(thiz).linkbutton({"text": "发送语音"});
                $(parentTD).find("#testPlane").show();

            }, function (data) {
                $(thiz).linkbutton('enable');
                $(thiz).linkbutton({"text": "发送语音"});
                $(parentTD).find("#testPlane").show();

            }

            ]
        });
    },
    fnPlayInternalVoice: function (thiz, index) {
        var parentTD = $(thiz).parent().parent();
        var btn = $(thiz);
        var id = $(thiz).attr("id");
        var data = $(thiz).parent().parent().data("data");
        var postData = {};
        postData.deviceUuid = data.deviceUuid;
        postData.index = index;
        $(thiz).linkbutton('disable');
        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/send-internal-Voice",
            data: postData,
            async: true,
            error: function (request) {
                $(thiz).linkbutton('enable');

            },
            success: [function (data) {
                $(thiz).linkbutton('enable');
                $(parentTD).find("#testPlane").show();

            }, function (data) {
                $(thiz).linkbutton('enable');
                $(parentTD).find("#testPlane").show();

            }

            ]
        });
    },
    /**
     * 获取状态
     * @param thiz
     */
    fnGetStatus: function (thiz) {
        var data = $(thiz).parent().data("data");
        var postData = {};

        postData.deviceUuid = data.deviceUuid;

        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/status",
            data: postData,
            async: true,
            error: function (request) {

            },
            success: [function (data) {
                alert(data.data.status);


            }, function (data) {


            },
            ]

        });
    },
    fnPlayVoice: function (thiz) {
        var player = $(thiz).prev();
        $(player)[0].play();
    },
    /**
     * 处理 websocket数据。图片的进度
     * @param data
     */
    fnHandleWsMsg: function (data) {
        var imgs = $("#fnTestWindowTable #Imgprogress");
        var progressData = $.parseJSON(data.data);

        $(imgs).each(function () {
            var deviceData = $(this).parent().parent().data("data");
            if (deviceData && progressData[deviceData.deviceUuid]) {
                var progress = progressData[deviceData.deviceUuid];
                $(this).html(Math.floor(progress.percent * 100) + "%");
            }
        });
    },
    /**
     * 截图
     * @param thiz
     */
    fnCapture: function (thiz) {
        var parentTD = $(thiz).parent().parent();
        var data = $(thiz).parent().parent().data("data");
        var postData = {};
        var timerObj = setInterval(function () {
            testPage.ws.checkPicStatus();
            console.log("-----");
        }, 800);
        postData.deviceUuid = data.deviceUuid;
        $(thiz).attr({src: GVAR.ctx + "images/loading.gif"});
        $(thiz).unbind("click").removeAttr("onclick");
        console.log("kaishi");
        $(thiz).next().hide();
        $(thiz).next().next().html("0%");
        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/capture",
            data: postData,
            async: true,
            error: function (request) {
                $(thiz).attr({src: GVAR.ctx + "images/loaderror.gif"});
                $(thiz).click(function () {
                    testPage.fnCapture(this);
                });
                clearInterval(timerObj);
            },
            success: [function (data) {
                console.log("requestdone");
                $(thiz).click(function () {
                    testPage.fnCapture(this);
                });
                $(thiz).attr({src: "data:image/png;base64," + data.data.img});
                $(thiz).next().show();
                $(thiz).next().next().html("");
                clearTimeout(timerObj);
                $(parentTD).find("#testPlane").show();

            }, function (data) {
                console.log("requestdone");
                $(thiz).click(function () {
                    testPage.fnCapture(this);
                });
                $(thiz).attr({src: GVAR.ctx + "images/loaderror.gif"});
                $(thiz).next().next().html("");
                clearInterval(timerObj);
                $(parentTD).find("#testPlane").show();

            }

            ]
        });
    },
    fnGetPositionStatus:function(thiz){
        var parentTD = $(thiz).parent();
        var deviceData = $(thiz).parent().parent().data("data");
        
        $(thiz).linkbutton('disable');
        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/config-position-status",
            data: "deviceUuid="+deviceData.deviceUuid ,
            async: true,
            error: function (request) {
                $(thiz).linkbutton('enable');

            },
            success: [function (data) {
                $(thiz).linkbutton('enable');
                $(parentTD).find("#testPlane").show();
                alert(data.data.returnMsg);
            }, function (data) {
                $(thiz).linkbutton('enable');
                $(parentTD).find("#testPlane").show();

            }

            ]
        });
    },
     fnSetPositionStatus:function(thiz){
         var parentTD = $(thiz).parent();
         var deviceData = $(thiz).parent().parent().data("data");
         var forms=$(parentTD).find("form");
         
         $(thiz).linkbutton('disable');
         $.ajax({
             type: "POST",
             url: GVAR.ctx + "api/web/device-test/config-position",
             data: $(forms).serialize()+"&deviceUuid="+deviceData.deviceUuid ,
             async: true,
             error: function (request) {
                 $(thiz).linkbutton('enable');

             },
             success: [function (data) {
                 $(thiz).linkbutton('enable');
                 $(parentTD).find("#testPlane").show();
                 alert(data.data.returnMsg);
             }, function (data) {
                 $(thiz).linkbutton('enable');
                 $(parentTD).find("#testPlane").show();

             }

             ]
         });
     },   
    /**
     * 处理高温log
     * @param msg
     */
    fnHandleLogData: function (msg) {
        var text = $("#showLogDialog #log").textbox('getText') + $.parseJSON(msg.data).log;
        $("#showLogDialog #log").textbox('setText', text);
    },
    /**
     *  LogSocket断开触发
     * @param msg
     */
    fnLogLostConnection: function (msg) {
        $("#showLogDialog #state").html('[断开,重新打开对话框]');
        ;
    },
    /**
     * 查看台灯压测log
     * @param thiz
     */
     fnGetTestLog: function (thiz) {
        var parentTD = $(thiz).parent().parent();
        var deviceData = $(thiz).parent().parent().data("data");
        $("#showLogDialog #log").textbox('setText', "");
        $("#showLogDialog #state").html('');
        ;
        var onopen = function () {
            socket.send(JSON.stringify(gsonData));

        }
        var socket = WebSocketUtils.createSocket("ws://" + GVAR.serverName + ":" + GVAR.serverPort + "/" + GVAR.contextPath + "/device-test/stress-test", onopen, testPage.fnHandleLogData, testPage.fnLogLostConnection);
        var gsonData = {};
        gsonData.deviceUuid = deviceData.deviceUuid;
        $("#showLogDialog").dialog({
            close: true,
            modal: true,
            title: "日志-" + deviceData.deviceNickName,
            onClose: function () {
                socket.close();
                $(parentTD).find("#testPlane").show();

            }
        });
        $("#showLogDialog").dialog('open');


    },
    /**
     * 更改台灯备注
     */
    fnChangeRemark: function () {
        var data = {};
        data.id = editRemarkForm.id.value;
        data.remark = $("#editRemarkDialog #remark").textbox('getValue');
        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/changremark",
            data: data,
            async: true,
            error: function (request) {

            },
            success: [function (data) {
                $("#editRemarkDialog").dialog('close');
                testPage.refreshDeviceTestList();
            }, function (data) {

            }

            ]
        });
    },
    /**
     * 提交结果
     * @param thiz
     * @param flag
     */
    fnPostTestResult: function (thiz, flag) {
         var postData={};
         if(flag==0){
             postData.remark=$("#abnormalRemarkDialog #remark").textbox('getValue');
             postData.deviceId=$("#abnormalRemarkDialog #deviceId").val( );
             postData.subjectCode=  $("#abnormalRemarkDialog #subjectCode").val();
         }else{
             postData.deviceId=$(thiz).parent().parent().parent().data("data").deviceId;
             postData.subjectCode=  $(thiz).parent().parent().attr("code") ;

         }
        postData.status=flag;

        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/test-result",
            data: postData,
            async: true,
            error: function (request) {

            },
            success: [function (data) {
                 var parentTD;
                if (flag==0){
                    var id= $("#abnormalRemarkDialog #inputBtnId").val();
                    $("#"+id).parent().parent().hide();
                    parentTD=  $("#"+id).parent().parent().parent();
                    $("#abnormalRemarkDialog").dialog('close');
                    $(parentTD).css({"background-color":"#FFE4DD"});

                }else{
                    $(thiz).parent().parent().hide();
                    parentTD=$(thiz).parent().parent().parent();
                    $(parentTD).css({"background-color":"#F1FFDD"});
                }
                var date=new Date();
                $(parentTD).find("#lastTestResult").html("正常?:"+(flag==1?"√":"×")+" 时间:"+date.format('MM-dd HH:mm'));
            }, function (data) {
                var parentTD;
                if (flag==0){
                    var id= $("#abnormalRemarkDialog #inputBtnId").val();
                    $("#"+id).parent().parent().hide();
                    $("#abnormalRemarkDialog").dialog('close');
                    parentTD=  $("#"+id).parent().parent().parent();
                    $(parentTD).css({"background-color":"#FFE4DD"});

                }else{
                    $(thiz).parent().parent().hide();
                    parentTD=$(thiz).parent().parent().parent();
                    $(parentTD).css({"background-color":"#F1FFDD"});

                }
                $(parentTD).find("#lastTestResult").html("正常?:"+(flag==1?"√":"×")+" 时间:"+date.format('MM-dd HH:mm'));

            }

            ]
        });
    },
    /**
     * 显示不正常原因窗口
     * @param thiz
     */
    uiShowAbnormalRemarkDialog: function (thiz,id) {
        var parentTD = $(thiz).parent().parent().parent();
        var data = $(parentTD).data("data");
        var deviceId = data.deviceId;
        $("#abnormalRemarkDialog #deviceId").val(deviceId);
        $("#abnormalRemarkDialog #inputBtnId").val(id);
        $("#abnormalRemarkDialog #subjectCode").val($(thiz).parent().parent().attr("code"));

        $("#abnormalRemarkDialog #remark").textbox('setValue', "");
        $("#abnormalRemarkDialog").dialog({
            "modal": true,
            title: "填写不正常的原因",
            close: "true"
        }).dialog("open");
    },
    uiOpenPicInNewWindow: function (thiz) {
        var dialog = $("#picDialog");
        var pic = $(thiz).prev();
        var imgs = $(dialog).find("img");
        $(dialog).find("img").attr({src: $(pic).attr("src")});
        $(dialog).dialog({
            close: true,
            modal: true
        });
        $(dialog).dialog('open');
    },
    uiShowEditRemarkDialog: function () {
        var rowDatas = $("#deviceTestListTab").datagrid('getSelections');
        if (rowDatas && rowDatas.length > 1) {
            $.errMsg("只能选择一个进行修改备注");
            return;
        }
        var rowData = $("#deviceTestListTab").datagrid('getSelected');
        if (rowData) {
            $("#editRemarkDialog").dialog({
                title: '修改备注',
                iconCls: 'icon-edit',
                closed: true,
                modal: true
            });
            $("#editRemarkDialog [name='id']").val(rowData.id);
            $("#editRemarkDialog #remark").textbox("setValue", rowData.remark);
            $("#editRemarkDialog").dialog('open');
        }
    },
    /**
     *询问是否测试完毕，无问题
     */
    uiShowMarkTestDoneConfirm: function () {
        var rowDatas = $("#deviceTestListTab").datagrid('getSelections');
        if (!rowDatas || rowDatas.length == 0) {
            $.errMsg("请选择设备");
            return;
        }
        var list = "";
        var idList = [];
        for (var i = 0; i < rowDatas.length; i++) {
            list += "No:" + (i + 1) + "[name:" + rowDatas[i].deviceNickName + ",mac:" + rowDatas[i].mac + "]" + "<br>";
            idList.push(rowDatas[i].id);
        }
        $.messager.confirm('Confirm', '标记已测后，将不能再次测试！确认吗？<br>' + list,
            function (r) {
                if (r) {
                    $.ajax({
                        type: "POST",
                        url: GVAR.ctx + "api/web/device-test/confirm-test-done",
                        data: {"ids": idList},
                        async: true,
                        error: function (request) {

                        },
                        success: [function (data) {
                            testPage.refreshDeviceTestList();
                        }, function (data) {

                        }

                        ]
                    });
                }
            });
    },
    /**
     * 显示测试页面
     */
    uiShowTestWindow: function () {
        var rowDatas = $("#deviceTestListTab").datagrid('getSelections');
        if (!rowDatas || rowDatas.length == 0) {
            $.errMsg("请选择要测试的设备");
            return;
        }
        if (rowDatas && rowDatas.length > 5) {
            $.errMsg("只能选择最多五个进行测试");
            return;

        }


        $("#fnTestWindow").window({
            title: '测试功能',
            iconCls: 'icon-edit',
            closed: true,
            modal: true,
            maximized:true,
            left: "0%",
            top: "0%",
            onMinimize: function () {
                //最下化移动到右下角并折叠
                $('#fnTestWindow').window('move', {
                    left: "58%",
                    top: "97%"
                }).window('collapse').window('open');
            },
            onOpen: function () {

            },
            onClose: function () {
                if (testPage.ws != null) {
                    testPage.ws.close();
                    testPage.ws = null;
                }
            }
        });
        if (!testPage.ws) {
            testPage.ws = WebSocketUtils.createSocket("ws://" + GVAR.serverName + ":" + GVAR.serverPort + "/" + GVAR.contextPath + "/device-test/capture-status", null, testPage.fnHandleWsMsg);
        }
        var wsData = {};
        wsData.deviceUuids = [];



        for (var i = 0; i < rowDatas.length; i++) {
            wsData.deviceUuids.push(rowDatas[i].deviceUuid);
        }
        this.ws.checkPicStatus = function () {
            testPage.ws.send(JSON.stringify(wsData));

        };
        //clear
        var trs = "";
        var divs = $("#testTemplate").children("div");
        for (var i = 0; i < divs.length + 1; i++) {
            trs += "<tr></tr>";
        }
        $("#fnTestWindowTable").html($("<tbody>").append(trs));
        var totalWidth = rowDatas.length * 192 + 30;
        //insert head
        var head;
        var line1;
        var line2;
        var line3;
        var line4;
        var line5;
        var line6;
        var line7;
        var line8;

        //获取模板
        var templates = [];
        var lines = [];
        //模板
        var temp1 = divs.eq(0).html();
        var temp2 = divs.eq(1).html();
        var temp3 = divs.eq(2).html();
        var temp4 = divs.eq(3).html();
        var temp5 = divs.eq(4).html();
        var temp6 = divs.eq(5).html();
        var temp7 = divs.eq(6).html();
        var temp8 = divs.eq(7).html();

        var tableTrs = $("#fnTestWindowTable tbody").children("tr");

        for (var i = 0; i < rowDatas.length; i++) {//设备循环
            
        	var devicename = (!rowDatas[i].deviceNickName || rowDatas[i].deviceNickName == "") ? ("no name") : rowDatas[i].deviceNickName;
            head = $("<td>").html($("<input type='text' id='nickNameInput'>").val(devicename)).data("data", rowDatas[i]).data("index", i);
            head.append($("<a href='#' onclick='testPage.fnChangeNickName(this)'>修改别名</a>"));
            head.append($("<a href='#' onclick='testPage.fnGetStatus(this)'>&nbsp;获取状态</a>"));

            line1 = ($("<td>").html($(temp1))).data("data", rowDatas[i]).data("index", i);
            line1 = testPage.uiHandleHtml(line1, i);


            line2 = ($("<td>").html($(temp2))).data("data", rowDatas[i]).data("index", i);
            $(line2).find("audio").attr({src: GVAR.ctx + "voice/L" + (i + 1) + ".mp3"});
            line2 = testPage.uiHandleHtml(line2, i);

            line3 = ($("<td>").html($(temp3))).data("data", rowDatas[i]).data("index", i);
            line3 = testPage.uiHandleHtml(line3, i);


            line4 = ($("<td>").html($(temp4))).data("data", rowDatas[i]).data("index", i);
            line4 = testPage.uiHandleHtml(line4, i);


            line5 = ($("<td>").html($(temp5))).data("data", rowDatas[i]).data("index", i);
            line5 = testPage.uiHandleHtml(line5, i);

            line6 = ($("<td>").html($(temp6))).data("data", rowDatas[i]).data("index", i);
            line6 = testPage.uiHandleHtml(line6, i);

            line7 = ($("<td>").html($(temp7))).data("data", rowDatas[i]).data("index", i);
            line7 = testPage.uiHandleHtml(line7, i);
           
            line8 = ($("<td>").html($(temp8))).data("data", rowDatas[i]).data("index", i);
            line8 = testPage.uiHandleHtml(line8, i);

            //
            tableTrs.eq(0).append(head);
            tableTrs.eq(1).append(line1);
            tableTrs.eq(2).append(line2);
            tableTrs.eq(3).append(line3);
            tableTrs.eq(4).append(line4);
            tableTrs.eq(5).append(line5);
            tableTrs.eq(6).append(line6);
            tableTrs.eq(7).append(line7);
            tableTrs.eq(8).append(line8);

        }


        $("#fnTestWindow [name='id']").val(rowDatas[0].id);
        $("#fnTestWindow [name='deviceUuid']").val(rowDatas[0].deviceUuid);
        $("#fnTestWindow").window(
            {"width"
                : totalWidth});
        $("#fnTestWindow").window('open');
        //获取测试结果
         var postData ={};
        postData.deviceIds=[];
        for (var i = 0; i < rowDatas.length; i++) {
            postData.deviceIds.push(rowDatas[i].deviceId);
        }
        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/test-subject-result",
            data: postData,
            async: true,
            error: function (request) {

            },
            success: [function (resp) {
                console.log(resp);
                var data=resp.data;
                $("#fnTestWindow td").each(function(){
                    var td=$(this);
                    var rowData=$(td).data("data");
                    if (!rowData||!data.result[rowData.deviceId]){
                        return ;
                    }
                    var testResult=data.result[rowData.deviceId];
                    for(var index in testResult){
                        var rs=testResult[index];
                        if (rs.subjectCode==$(td).find("#testPlane").attr("code")){
                            var html="";
                            var flag=rs.status;
                            var date=new Date(rs.lastTestTime);
                            html= "正常?:"+(flag==1?"√":"×")+" 时间:"+date.format('MM-dd HH:mm')
                            $(td).find("#lastTestResult") .html(html);
                            $(td).css({"background-color":flag==0?"#FFE4DD":"#F1FFDD"});
                            break;
                        }
                    }

                });

            }, function (resp) {

            }

            ]
        });


    },
    /**
     * 按钮id处理工具
     * @param htmlDom
     * @param index
     * @returns {jQuery|HTMLElement}
     */
     uiHandleHtml: function (htmlDom, index) {
        var code = $(htmlDom).find("#testPlane").attr("code");
        $(htmlDom).find(".lb").each(function () {
            var id = $(this).attr("id");
            $(this).removeAttr("id").attr({id: code + "_" + id + index});
            $(this).linkbutton({});
        });
        $(htmlDom).find(".numberRange").each(function () {
            var id = $(this).attr("id");
            $(this).removeAttr("id").attr({id: code + "_" + id + index});
            $(this).numberspinner({});
        });
        
        return $(htmlDom);
    },
    /**
     * 修改昵称
     * @param thiz
     */
    fnChangeNickName: function (thiz) {
        var postData = {};
        var parentTD = $(thiz).parent();
        var data = $(parentTD).data("data");
        postData.id = data.id;
        postData.newName = $(parentTD).find("#nickNameInput").val();

        $.ajax({
            type: "POST",
            url: GVAR.ctx + "api/web/device-test/rename",
            data: postData,
            async: true,
            error: function (request) {

            },
            success: [function (data) {
                console.log("requestdone");

            }, function (data) {

            }

            ]
        });


    },
    /***
     * 显示备注台灯修改框
     */

    uiShowTestSubjectResult:function(){
        var rowDatas = $("#deviceTestListTab").datagrid('getSelections');
        if (rowDatas && rowDatas.length > 1) {
            $.errMsg("只能选择一个进行修改备注");
            return;
        }
        var rowData = $("#deviceTestListTab").datagrid('getSelected');
        if (rowData) {
            $("#editRemarkDialog").dialog({
                title: '修改备注',
                iconCls: 'icon-edit',
                closed: true,
                modal: true
            });
            $("#editRemarkDialog [name='id']").val(rowData.id);
            $("#editRemarkDialog #remark").textbox("setValue", rowData.remark);
            $("#editRemarkDialog").dialog('open');
        }
    },
    //---------------------------------------------------------------格式化工具start----------------------------------------------------------------------
    uiOnLineFormatter: function (value, row, index) {
        if (value == 1) {
            return "<span style='background-color:green'>在线</span>";

        } else {
            return "<span style='background-color:red'>离线</span>";
        }
    },
    /***
     * 格式化工具
     */
    uiTestStatusFormatter: function (value, row, index) {
        if (value == 1) {
            return "已测试";

        } else {
            return "未测试";
        }
    }
    //---------------------------------------------------------------格式化工具end---------------------------------------------------------------------

};
testPage.initDataGrid();
testPage.refreshDeviceTestList();