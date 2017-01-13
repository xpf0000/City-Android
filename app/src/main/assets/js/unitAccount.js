
var vm;
var header = "";
var type = "";
var nick = "";
var openid = "";

//requirejs(['main'], function (main) {
//
//		require(['avalon','net'], function() {
	     		 
	     		header = $.getUrlParam('header');
	     		type = $.getUrlParam('type');
	     		nick = $.getUrlParam('nick');
	     		openid = $.getUrlParam('openid');
	     		 
	 			vm = avalon.define({
	    
		  		$id: "unitLogin",
		  		
		  		header : "",
		  		type : "",
		  		nick : "",
		  		
		  		});
		  		
		  		vm.header = header;
		  		vm.type = type;
		  		vm.nick = nick;
		  					  		
  				
//	});
//
//
//});


function show(header,type,nick)
{
	vm.header = header;
	vm.type = type;
	vm.nick = nick;

}


function toRegist()
{
	sendMsgToAPP({'msg':'跳转注册页面','type':'5','bindtype':vm.type});
}

function toUnit()
{
	sendMsgToAPP({'msg':'跳转绑定现有帐号页面','type':'5','openid':openid,'bindtype':vm.type});
}







