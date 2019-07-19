

/**
 * value 转hex
 * 
 * @param value
 * @param row
 * @param index
 * @returns
 */
function intToHexFormatter(value,row,index){
	if (value==null||""==value){
		return ""
	}
	return value.toString(16).toUpperCase();
}

/**
 * timestamp转yyyyMMdd
 * @param value
 * @param row
 * @param index
 * @returns {String}
 */
function timestampToyyyyMMddFormatter(value,row,index){
	if (value==null){
		return "--";
	}
	var date = new Date(value);
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	
}

/**
 *timeStamp转 yyyyMMddHHmmss
 * @param value
 * @param row
 * @param index
 * @returns
 */
function timestampToyyyyMMddHHmmssFormatter(value,row,index){
	if (value==null){
		return "--";
	}
	var date = new Date( parseInt(value));
	return date.format("yyyy-MM-dd HH:mm:ss");
}
/**
 *timeStamp转 yyyyMMdd
 * @param value
 * @param row
 * @param index
 * @returns
 */
function timestampToyyyyMMddFormatter(value,row,index){
	if (value==null){
		return "--";
	}
	var date = new Date(parseInt(value));
	return date.format("yyyy-MM-dd");
}

/**
 * date转yyyyMMdd
 * @param date
 * @returns {String}
 */
function dateToyyyyMMddFormatter(date){
	if (date==null){
		return "--";
	}
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}

/**
 * 字符串转date
 * @param s
 * @returns {Date}
 */
function yyyyMMddToDateParser(s){
	if (!s) return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
}