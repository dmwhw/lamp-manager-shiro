
function WebSocketInstance(){};
function WebSocketUtils(){};
WebSocketInstance.prototype.send=function (message,callback){
	var that=this;
	this.waitForConnection(function () {
		that.ws.send(message);
	        if (typeof callback !== 'undefined') {
	          callback();
	        }
	    },1000);
};
WebSocketInstance.prototype.close=function (){
	this.ws.close();
};

WebSocketInstance.prototype.waitForConnection = function (callback, interval) {
    if (this.ws.readyState === 1) {
        callback();
    } else {
        var that = this;
        // optional: implement backoff for interval here
        setTimeout(function () {
            that.waitForConnection(callback, interval);
        }, interval);
    }
};


/**
 * param
 * url:
 * onopen
 * onmessage
 * onclose
 * onerror
 * 
 */
WebSocketUtils.createSocket =function(){
	 if (!arguments||arguments.length<=0){
		 return null;
	 }
	var webSocketInstance= new WebSocketInstance();
    var websocket = null;

    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket(arguments[0]);
    }
    else{
        alert('Not support websocket');
        return null;
    }
	if (arguments.length>1&& arguments[1]&& arguments[1]&&typeof(arguments[1])=='function'){
		websocket.onopen=arguments[1];
	}
	if (arguments.length>2&& arguments[2]&& arguments[2]&&typeof(arguments[2])=='function'){
		websocket.onmessage=arguments[2];
	}
	if (arguments.length>3&& arguments[3]&& arguments[3]&&typeof(arguments[3])=='function'){
		websocket.onclose=arguments[3];
	}
	if (arguments.length>4&& arguments[4]&& arguments[4]&&typeof(arguments[4])=='function'){
		websocket.onerror=arguments[4];
	}
	WebSocketInstance.prototype.ws=websocket;
	return webSocketInstance;
}

