var otherMac = {
    // 初始化表格
    initUI : function() {
        $('#otherMacListTab').datagrid({
            pagination : false,
            loadMsg : "加载中...",

            onBeforeLoad : function() {
            },
            columns : [ [ {
                field : 'mac',
                title : 'mac地址'
            }, {
                field : 'time',
                title : '出现时间',
                formatter : timestampToyyyyMMddHHmmssFormatter
            }, {
                field : 'ip',
                title : 'ip'
            } ] ]
        });
        $.ajax({
            type : "POST",
            url : GVAR.ctx + "api/web/device-no/list",
            async : true,
            error : function(request) {

            },
            success : [ function(data) {
                var dataList = data.data.list;
                var size = data.data.size;
                $('#otherMacChooseModelNoCombobox').combobox({
                    valueField : 'deviceCode',
                    textField : 'deviceName',
                    data : dataList
                });

            }, function(data) {

            }, ]

        });

    },
    macs : null,
    uiShowImportConfirm : function() {
        var rowDatas = $("#otherMacListTab").datagrid('getSelections');
        if (!rowDatas || rowDatas.length == 0) {
            $.errMsg("请选择设备");
            return;
        }
        var list = "";
        var idList = [];
        for (var i = 0; i < rowDatas.length; i++) {
            list += "No:" + (i + 1) + "[ mac:" + rowDatas[i].mac + "]" + "<br>";
            idList.push(rowDatas[i].mac);
        }
        $.messager.confirm('Confirm', '确定要导入吗<br>' + list, function(r) {
            if (r) {
                // 选择型号
                // showComboxBox
                otherMac.macs = idList;
                $("#otherMacChooseModelNoDialog").dialog({
                    "modal" : true,
                    "close" : true,
                    title : "选择台灯的型号",
                });
                $("#otherMacChooseModelNoDialog").dialog('open');
            }
        });
    },
    fnAddMacs : function() {
        var deviceCode = $('#otherMacChooseModelNoCombobox').combobox(
                'getValue');
        if (deviceCode == null) {
            $.errMsg("请选择导入的设备的台灯型号！");
            return;
        }
        $.ajax({
            type : "POST",
            url : GVAR.ctx + "api/web/other-mac/import",
            async : true,
            data : {
                "macs" : otherMac.macs,
                "deviceCode" : deviceCode
            },
            error : function(request) {

            },
            success : [ function(data) {
                $("#otherMacChooseModelNoDialog").dialog('close');
                otherMac.fnRefreshData();
            }, function(data) {

            }, ]

        });
    },
    uiShowDelMacConfirm : function() {
        var rowDatas = $("#otherMacListTab").datagrid('getSelections');
        if (!rowDatas || rowDatas.length == 0) {
            $.errMsg("请选择设备");
            return;
        }
        var list = "";
        var idList = [];
        for (var i = 0; i < rowDatas.length; i++) {
            list += "No:" + (i + 1) + "[ mac:" + rowDatas[i].mac + "]" + "<br>";
            idList.push(rowDatas[i].mac);
        }
        $.messager.confirm('Confirm', '确定要删除吗吗<br>' + list, function(r) {
            if (r) {

                $.ajax({
                    type : "POST",
                    url : GVAR.ctx + "api/web/other-mac/del",
                    async : true,
                    data : {
                        "macs" : idList
                    },
                    error : function(request) {

                    },
                    success : [ function(data) {
                        otherMac.fnRefreshData();

                    }, function(data) {

                    }, ]

                });
            }
        });
    },
    fnRefreshData : function() {
        $.ajax({
            type : "POST",
            url : GVAR.ctx + "api/web/other-mac/list",
            async : true,
            error : function(request) {

            },
            success : [ function(data) {
                $('#otherMacListTab').datagrid({
                    fitColumns : true,
                    emptyMsg : '无数据显示',
                    data : data.data.list
                });

            }, function(data) {

            }, ]

        });

    }

};
otherMac.initUI();
otherMac.fnRefreshData();
