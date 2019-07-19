/**
 * isModal 为true时用模态框显示
 * 默认为false 使用toastr显示
 * v2.0改版，模态脱离bootstrap
 */
$.okMsg = function (msg,isModal){
	isModal = isModal ? isModal : false;
	if(isModal){
		//  模态框显示信息
	    var that = $("<div>");
		this.each(function(){
			showMsgWithModal(that,msg);
		}
		);
	}else{
	//  toastr显示信息
		showMsgWithToast({
			title : '',
			msg : msg,
			type : 'success',
			position : 'toast-top-center',
			time : 3000
		});
	}
};
	

$.errMsg = function (msg,isModal){
	isModal = isModal ? isModal : false;
	if(isModal){
		  //	模态框显示信息
		var that = $("<div>");
	    this.each(function(){
	    	showErrorWithModal(that,msg);
	    });
	}else{
		//  toastr显示信息
		showMsgWithToast({
			title : '',
			msg : msg,
			type : 'error',
			position : 'toast-top-center',
			time : 5000
		});
	}	
};
/* 通过toastr显示消息
 * example:
 * showMsgWithToast({
 *  title : '', //标题
	msg : msg,  //显示的消息
	type : 'success', //消息类型   默认为info类型
	positon : 'toast-top-right', //显示toast的位置，默认为顶部右边
	time : 5000   //显示的时长 ，默认为5秒
});

*/
/*
显示位置   positon
toast-top-left  顶端左边
toast-top-right    顶端右边
toast-top-center  顶端中间
toast-top-full-width 顶端，宽度铺满整个屏幕
toast-botton-right  底部右边
toast-bottom-left	底部左边
toast-bottom-center 底部中间
toast-bottom-full-width 底部，宽度铺满整个屏幕
*/
/*  
消息类型  type
info  常规消息提示，默认背景为浅蓝色 
success  成功消息提示，默认背景为浅绿色
warning  警告消息提示，默认背景为橘黄色 
error   错误消息提示，默认背景为浅红色 
*/

function showMsgWithToast(settings) {
	var title = "";
	var msg = settings.msg ? (settings.msg) : '';
	var position = settings.position ? (settings.position) : 'toast-top-right';
	var type = settings.type ? (settings.type) : 'info';
	var time = settings.time ? (settings.time) : 5000;
	var title = settings.title ? (settings.title) : '';
	
	toastr.options = {
	  "closeButton": true,  //是否显示关闭按钮
	  "debug": false,     //是否显示关闭按钮
	  "newestOnTop": false,  //最新消息出现的位置（默认在顶端）
	  "progressBar": false,  //是否显示进度条
	  "rtl": false,
	  "positionClass": position, //弹出窗的位置
	  "preventDuplicates": false,  //阻止多个弹窗
	  "onclick": null,
	  "showDuration": 300,    //显示的动画时间
	  "hideDuration": 1000,   //消失的动画时间
	  "timeOut": time,		//展现时间
	  "extendedTimeOut": 1000,  //加长展示时间
	  "showEasing": "swing",  //显示时的动画缓冲方式
	  "hideEasing": "linear",  //消失时的动画缓冲方式
	  "showMethod": "fadeIn",  //显示时的动画方式
	  "hideMethod": "fadeOut"  //消失时的动画方式
	}
  toastr[type](msg, title); 

}


/**
 * 通过模态框显示普通信息
 * @param that
 * @param msg
 * @return
 */
function showMsgWithModal(that,msg){
	var ele = "";
	ele=ele+('<div class="modal fade" id="msgDialog_msg" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">');
	ele=ele+('<div class="modal-dialog" >');
	ele=ele+('<div class="modal-content">');
	ele=ele+('<div class="modal-header">');
	ele=ele+('<button type="button" class="close" msg="close" data-dismiss="modal" aria-hidden="true">');
	ele=ele+('&times;');
	ele=ele+('</button>');
	ele=ele+('<h4 class="modal-title" id="myModalLabel">');
	ele=ele+('正确信息');
	ele=ele+('</h4>');
	ele=ele+('</div>');
	ele=ele+('<div class="modal-body">');
	ele=ele+(msg);
	ele=ele+('</div>');
	ele=ele+('<div class="modal-footer">');
	ele=ele+('<button type="button" class="btn btn-primary" msg="close" data-dismiss="modal">');
	ele=ele+('确认');
	ele=ele+('</button>');
	ele=ele+('</div>');
	ele=ele+('</div>');
	ele=ele+('</div>');
	ele=ele+('</div>');
	$(that).html("");
	$(that).append(ele);
	$(document).on("click","[msg='close']",function(){
		$('#msgDialog_msg').modal('hide');
	});
	
	$('#msgDialog_msg').modal('show');
	return that;	
}
/**
 * 通过模态框显示错误信息
 * @param that
 * @param msg
 * @return
 */
function showErrorWithModal(that,msg) {
	var ele="";
	ele=ele+('<div class="modal fade" id="msgDialog_error" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">');
	ele=ele+('<div class="modal-dialog"  >');
	ele=ele+('<div class="modal-content">');
	ele=ele+('<div class="modal-header">');
	ele=ele+('<button type="button" class="close" msg="close" data-dismiss="modal" aria-hidden="true">');
	ele=ele+('&times;');
	ele=ele+('</button>');
	ele=ele+('<h4 class="modal-title" id="myModalLabel">');
	ele=ele+('错误信息');
	ele=ele+('</h4>');
	ele=ele+('</div>');
	ele=ele+('<div class="modal-body">');
	ele=ele+(msg);
	ele=ele+('</div>');
	ele=ele+('<div class="modal-footer">');
	ele=ele+('<button type="button" class="btn btn-primary" msg="close" data-dismiss="modal">');
	ele=ele+('确认');
	ele=ele+('</button>');
	ele=ele+('</div>');
	ele=ele+('</div>');
	ele=ele+('</div>');
	ele=ele+('</div>');
	$(that).html("");
	$(that).append(ele);
	$(document).on("click","[msg='close']",function(){
		$('#msgDialog_error').modal('hide');
	});
	$('#msgDialog_error').modal('show');
	return that;	
}
/**
 * 
 * 可选参数： title,
 * {按钮一文本：btn1
 * 按钮二文本：btn2
 * 消息文本: msg
 * 按钮一事件：funbtn1
 * 按钮二事件：funbtn2
 * }
 * 默认按钮一关闭
 * 用法
 * $("#id").askDialog({
 * 		title:"你的标题",
 * 		no:"按钮一文本",
 * 		yes:"按钮二文本",
 * 		nofun:"按钮一方法",
 *      yesfun:"按钮二方法"
 * }); 
 * */
//example
//$("#msgDialog").askDialog({
//	yes:'好的',
//	no:'不要啊',
//	title:'你的标题',
//	msg:'很好',
// timer :3000,
//	yesfun:function(){
//		alert('是的');
//	},
//	nofun:function (){
//		alert('不！');
//	}	
//});
$.askDialog = function (data){
	var title=data.title?(data.title):'询问';
	var no=data.no?(data.no):'取消';
	var yes=data.yes?(data.yes):'确定';
	var msg=data.msg?(data.msg):'';
	var timer=data.timer?(data.timer):false;
	var timerCount=data.timerCount?(data.timerCount):5;
	$("#msgDialog_ask").remove();

	var ele="";
	ele=ele+('<div class="modal fade" id="msgDialog_ask" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">');
	ele=ele+('<div class="modal-dialog"  >');
	ele=ele+('<div class="modal-content">');
	ele=ele+('<div class="modal-header">');
	ele=ele+('<button type="button" class="close" msg="closeask" data-dismiss="modal" aria-hidden="true">');
	ele=ele+('&times;');
	ele=ele+('</button>');
	ele=ele+('<h4 class="modal-title" id="myModalLabel">');
	ele=ele+(title);
	ele=ele+('</h4>');
	ele=ele+('</div>');
	ele=ele+('<div class="modal-body">');
	ele=ele+(msg);
	ele=ele+('</div>');
	ele=ele+('<div class="modal-footer">');
	
	ele=ele+('<button type="button" name="nobtn" class="btn btn-default"  data-dismiss="modal">');
	ele=ele+(no);//默认的
	ele=ele+('</button>');
	
	ele=ele+('<button type="button" name="yesbtn" class="btn btn-primary"  data-dismiss="modal">');
	ele=ele+(yes);//确认
	ele=ele+('</button>');
	
	ele=ele+('</div>');
	ele=ele+('</div>');
	ele=ele+('</div>');
	ele=ele+('</div>');
	//var div=$("<div>").append(ele);
	$("body").append(ele);
	$("#msgDialog_ask [name='nobtn']").click(function(){
		$('#msgDialog_ask').modal('hide');
		if (data.nofun){
			data.nofun();
		  }
	});
	$("#msgDialog_ask [name='yesbtn']").click(function(){
		$('#msgDialog_ask').modal('hide');
		if (data.yesfun){
			data.yesfun();  
		}
	});
	$('#msgDialog_ask').modal('show');

	if(timer){
		clearInterval(timerKey);
		var i=timerCount;
		$("#msgDialog_ask [name='yesbtn']").prop("disabled",true);
		$("#msgDialog_ask [name='yesbtn']").addClass("disable_a_ol");
		$("#msgDialog_ask [name='yesbtn']").text(yes+"("+i+")");
		timerKey = setInterval (function(){
			i=i-1;
			$("#msgDialog_ask [name='yesbtn']").text(yes+"("+i+")");
			if(i==0){
				$("#msgDialog_ask [name='yesbtn']").text(yes);
				$("#msgDialog_ask [name='yesbtn']").prop("disabled",false);
				$("#msgDialog_ask [name='yesbtn']").removeClass("disable_a_ol");
				clearInterval(timerKey);
			}
		},1000);
	}
	return this;
};
var timerKey;