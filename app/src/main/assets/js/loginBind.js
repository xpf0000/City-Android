
var vm;
var type = "";
var openid = "";

//requirejs(['main'], function (main) {
//
//		require(['avalon','net'], function() {
	     		 
	     		type = $.getUrlParam('type');
	     		openid = $.getUrlParam('openid');
	     		
	 			vm = avalon.define({
	    
		  		$id: "loginBind",
		  		
		  		account : "",
		  		pass : "",
		  		typestr:"",
		  		
		  		});
		  		
		  		if(type == "1")
		  		{
			  		vm.typestr = "新浪微博";
		  		}
		  		else if(type == "2")
		  		{
			  		vm.typestr = "微信";
		  		}
		  		else if(type == "3")
		  		{
			  		vm.typestr = "QQ";
		  		}
		  					  		
  				
//	});
//
//
//});

function doLogin()
{
	if(vm.account.trim().length == 0 || vm.pass.trim().length == 0)
	{
		return;
	}
	
	sendMsgToAPP({'msg':'绑定登录','type':'6','account':vm.account,'pass':vm.pass,'openid':openid,'bindtype':type});
}

function toFindPass()
{
	sendMsgToAPP({'msg':'跳转找回密码页面','type':'6'});
}







