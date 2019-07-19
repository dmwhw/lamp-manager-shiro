$.extend($.fn.validatebox.defaults.rules, {
    equals: {    
        validator: function(value,param){    
            return value == $(param[0]).val();    
        },    
        message: '值不相等'   
    },  
    onlyHex:{
        validator:function(value,param){
           
            return SYS.REGEX.REGEX_HEX_STRING.test(value);
        },
        message:  '只能输入16进制字符串'
    },
    requireFile:{
        validator:function(value,param){
            return value&&value!='';
        },
        message:  '请上传文件'
    }
});