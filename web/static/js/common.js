
//-------------------------------------------------窗口大小start-----------------------------------
var resizeFunction=[];
//监听窗口大小变化
window.onresize = function(){
	setTimeout(executeResizeFunction,900);
}
function executeResizeFunction(){
	for (var i in resizeFunction ){
		try{
			resizeFunction[i]();
		}catch(e){
			console.log(e);
		}
	}
}

function addResizeFunction(fun){
	if (fun==null){
		return ;
	}
	for(var i in resizeFunction){
		if (resizeFunction[i].toString() ==fun.toString()){
			return ;
		};
	}
	resizeFunction.push(fun);
}
//-----------------------------------------------窗口大小end------------------------------------------------
//--------------------------------------------------ajax start------------------------------------------------------
var _ajax = $.ajax;
$.ajax = function(data) {
	data.type   = data.type   ? data.type   : "post";
	data.async  = data.async!=undefined ? data.async : true;
	data.processData  = data.processData!=undefined ? data.processData : true;
	data.contentType  = data.contentType!=undefined ? data.contentType : "application/x-www-form-urlencoded; charset=UTF-8";
	data.Dialog = data.Dialog!=undefined ? data.Dialog: true;

	_ajax({
		type:data.type,
		url:data.url,					
		data:data.data,
		async:data.async,
		processData:data.processData,
		contentType:data.contentType,
		error:function(json){//40114
			try{
				$.errMsg("连接服务器失败！");
			}catch(e){
				alert("连接服务器失败！");
			}
			if(data.error && typeof(data.error) == "function"){ 
				_processDone(data.error,json);

			}
		},
		success:function(json){
			if (json.msgCode>=90000){
				if (json.msgCode==90004||json.msgCode==90005){
					alert(getMsg(json.msgCode));
					top.location.href=GVAR.ctx+"/user/login/page";
					return;
				}
				if (json.msgCode==90008){
					//alert(getMsg(json.msgCode));
					///return;
					top.location.href=GVAR.ctx+"/user/logOut";
					return;
				}
			}
			if(data.Dialog){
				if(json.msgCode>40000){
					try{
						$.errMsg(json.msg);
					}catch(e){
						alert(json.msg);
					}
				}else if(json.msgCode<40000&&json.msgCode>10000){
					try{
						$.okMsg(json.msg);
					}catch(e){
						alert(json.msg);
					}
				}
			}
			if(data.success && typeof(data.success) == "function"){ 
				if(json.msgCode>=40000){
					//_processDone(data.success,json);
 				}else{
 					_processDone(data.success,json);
 					
 				}
			}else if(data.success && data.success instanceof Array){
				if(json.msgCode>=40000){//处理失败
					_processDone(data.success[1],json);
				}else{
					_processDone(data.success[0],json);
					
				}
			}
		}
	});	
};
function _processDone(fun,json){
	if (fun){
		try{
			fun(json);
		}catch(e){
			console.error(e);
		}
	}
}

//----------------------------------------------------ajax end--------------------------------------------------------
//-------------------------------------------------------------------------------load static start-----------------------------------------------

function loadJs() {
	loadJsA(arguments,0);
	function loadJsA(data,index){
		if(data.length == index+1){
			jQuery.getScript(GVAR.ctx+data[index]);
		}else{
			jQuery.getScript(GVAR.ctx+data[index],function(){
				index=index+1;
				loadJsA(data,index);
			});
		}
	}
}

function loadCss(url) {
	var link = document.createElement("link");
	link.type = "text/css";
	link.rel = "stylesheet";
	link.href = GVAR.ctx+url;
	document.getElementsByTagName("head")[0].appendChild(link);
}


//-------------------------------------------------------------------------------load static end-----------------------------------------------


//-------------------------------------------------------------------------------utils start-----------------------------------------------

function isNull(str){
	if (!str||str==null){
		return true;
	}
	return false;
}

function isEmpty(str){
	if (!str||str==null||$.trim(str)===''){
		return true;
	}
	return false;
}
 


function getMsg(key){
	if(SYS.MSG[key]){
		return SYS.MSG[key];
	}else{
		if(key>40000){
			return "系统繁忙！";
		}else{
			return "操作成功！";
		}
	}
}
jQuery.fn.shake = function(intShakes, intDistance, intDuration) {
	this.each(function() {
		var jqNode = $(this);
		jqNode.stop(true);
		jqNode.css({
			position : 'relative'
		});
		for ( var x = 1; x <= intShakes; x++) {
			jqNode.animate({
				left : (intDistance * -1)
			}, (((intDuration / intShakes) / 4))).animate({
				left : intDistance
			}, ((intDuration / intShakes) / 2)).animate({
				left : 0
			}, (((intDuration / intShakes) / 4)));
		}
	});
	return this;
};
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
function cmp( x, y ) {  
    // If both x and y are null or undefined and exactly the same  
    if ( x === y ) {  
        return true;  
    }  
    // If they are not strictly equal, they both need to be Objects  
    if ( ! ( x instanceof Object ) || ! ( y instanceof Object ) ) {  
        return false;  
    }  
    // They must have the exact same prototype chain, the closest we can do is  
    // test the constructor.  
    if ( x.constructor !== y.constructor ) {  
        return false;  
    }  
    for ( var p in x ) {  
        // Inherited properties were tested using x.constructor === y.constructor  
        if ( x.hasOwnProperty( p ) ) {  
            // Allows comparing x[ p ] and y[ p ] when set to undefined  
            if ( ! y.hasOwnProperty( p ) ) {  
                return false;  
            }  
            // If they have the same strict value or identity then they are equal  
            if ( x[ p ] === y[ p ] ) {  
                continue;  
            }  
            // Numbers, Strings, Functions, Booleans must be strictly equal  
            if ( typeof( x[ p ] ) !== "object" ) {  
                return false;  
            }  
            // Objects and Arrays must be tested recursively  
            if ( ! Object.equals( x[ p ],  y[ p ] ) ) {  
                return false;  
            }  
        }  
    }  
    for ( p in y ) {  
        // allows x[ p ] to be set to undefined  
        if ( y.hasOwnProperty( p ) && ! x.hasOwnProperty( p ) ) {  
            return false;  
        }  
    }  
    return true;  
};  

function deepCopy(obj){
    if(typeof obj != 'object'){
        return obj;
    }
    var newobj = {};
    for ( var attr in obj) {
        newobj[attr] = deepCopy(obj[attr]);
    }
    return newobj;
}

String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length){
		return false;
	}
	if(this.substr(0,str.length)==str){
		return true;
	}else{
		return false;
	}
	return true;
}
//-------------------------------------------------------------------------------utils end-----------------------------------------------




var SYS={
	MSG:{
		"10000":"操作成功",
		"10001":"操作成功",
		"10009":"请输入验证码",
		"10010":"发货成功",
		"10020":"添加成功",
		"10021":"删除成功",
		"10025":"修改密码成功",
		"10030":"删除成功",
		"40000":"操作失败",
		"40001":"操作失败!!",
		"40010":"账户不存在或者密码错误",
		"40011":"操作过于频繁",
		"40012":"账户不能为空",
		"40013":"账户格式不正确",
		"40014":"验证码为空",
		"40015":"验证码不正确",
		"40016":"验证码超时，请重新输入",
		"40017":"密码不能为空",
		"40018":"密码格式不正确",
		"40019":"旧密码错误",
		"40030":"订单信息有误",
		"40031":"该商品不能再次录音",
		"40032":"录音文件不符合要求，需要15秒内的录音",
		"40033":"文件体积过大，不能超出3M",
		"40034":"转码失败",
		"40035":"录音无效，请重新录音",
		"40040":"地址信息不能为空",
		"40050":"该账户已存在",
		"40060":"名称不能为空",
		"40061":"商品描述不能为空",
		"40062":"属性名不能为空",
		"40063":"已存在相同的属性名",
		"40064":"该属性存在子属性值，请先删除子属性",
		"40065":"该属性存在引用的正在售卖的商品，不能删除",
		"40066":"未定义属性",
		"40067":"存在属性未定义属性值",
		"40068":"请先下架该商品",
		"40079":"请上传主图片",
		"40080":"请上传商品描述图片",
		"40081":"请上传商品轮播图片",
		"40100":"库存不足",
		"40101":"地址有误",
		"40102":"地址不能为空",
		"40103":"数量不能为空",
		"40104":"购买商品不能为空",
		"40105":"匿名选择有误",
		"40111":"商品名称不能为空！",
		"40112":"商品图片不能为空！",
		"40113":"商品原价不能为空！",
		"40114":"商品售价不能为空！",
		"40115":"修改失败",
		"40125":"获取微信支付交易号失败",
		"40126":"微信支付失败",
		"40127":"订单已经过期",
		"40128":"订单已经被支付",
		"40155":"该订单不能删除",
		"40156":"违法删除订单",
		"90001":"系统繁忙",
		"90002":"token失效",
		"90004":"用户未登录",
		"90005":"异地登录",
		"90006":"数据格式错误",
		"90007":"权限不足",
		"90008":"未分配任何权限！"

	},
	REGEX:{
		//账号正则表达式 不是用户名，不是用户名，不是用户名
		REGEX_ACCOUNT : /^[a-zA-Z0-9]{5,13}$/,
		//密码正则表达式 6到16位数 符号，数字，英文 3选2
		//REGEX_PASSWORD : /^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)([0-9a-zA-Z]{6,16})$/,
		REGEX_PASSWORD: /^([0-9a-zA-Z]{6,16})$/,
		//@Field REGEX_IS_SIGNED_INTEGER : 是否为数字，包含带加号或者减号 的正则表达式
		REGEX_IS_SIGNED_INTEGER: /^[-\+]?\d+$/,
		//银行账户，数字
		REGEX_BANK_ACCOUNT:/^(\d{16}|\d{19})$/,
		//邮政编码
		REGEX_POSTCODE:/^[1-9][0-9]{5}$/,
		//手机号码
		REGEX_MOBILE:/^1\d{10}$/,
		REGEX_IS_INTEGER: /^\d+$/,
		//固话
		REGEX_TELEPHONE:/^([0-9]{3,4}-)?[0-9]{7,8}$/,
		REGEX_YYYY_MM:/^[0-9]{4}-((0[1-9])|[1][0-2])$/,
		REGEX_HEX_STRING:/^[A-Fa-f0-9]+$/
		
	}
};


///
jQuery.extend({
	handle :function(data,success,fail ){
		if (data.msgCode>=90000){
			//出现未登录的情况才需要跳转
			if (data.msgCode==90002||data.msgCode==90006){
				alert(data.msg);
				top.location.href=decodeURI(data.url);
				return;
			}	
		}
		if(data.msgCode>=40000){
			if (fail){
				fail();
			}
		}else{
			if (success){
				success();
			}
		}
	}
});


function isIE(){
	return navigator.appName == "Microsoft Internet Explorer" ? true :false;
}

function spHTML5(){
	return typeof(Worker) != "undefined" ? true : false;
}

