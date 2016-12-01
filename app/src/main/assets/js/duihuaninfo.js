
var vm;
var id = "";
var uid = "";
var uname="";

var need = -1;
var has = -1;
var running = false;

requirejs(['main'], function (main) {  
	
		require(['avalon','net'], function() {
	     	
	     	id = $.getUrlParam('id');
	     	uid = $.getUrlParam('uid');
		 	uname = $.getUrlParam('uname');
		 	
	     	 
	 			vm = avalon.define({
	    
		  		$id: "duihuaninfo",
		  		
		  		info:{
			  		
			  		content : ''
		  		},
		  		
		  		doDH: function()
		  		{
			  		$('#alertBG').removeClass("hidden")
		  		},
		  		
		  		alertClick:function(item)
		  		{
			  		if(running)
			  		{
				  		return;
			  		}
			  		
			  		if(item == 0)
			  		{
				  		$('#alertBG').addClass("hidden")
			  		}
			  		else
			  		{		  		
				  		$('#alertBG').addClass("hidden")
				  		dh();
			  		}
			  		
		  		},
		  		
		  		
		  		});
		  		
		  		
		  		
	  		
		  		getinfo();
		  		getUInfo();
				
				function getinfo()
		  		 {
			  		need = -1;
		  		 	var url = BaseUrl+"jifen.getproduct&id="+id;

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  			var arr = data.data.info;
		  		 		
		  		 		if(arr)
		  		 		{
			  		 		if(arr.length > 0)
			  		 		{
				  		 		var item = arr[0];
				  		 		
				  		 		if(!item.content)
				  		 		{
					  		 		item.content = '';
				  		 		}
				  		
				  		 		vm.info = item;	
				  		 		need = item["hfb"]*1;
				  		 		check();		  		 					  		 
				  		 		
			  		 		}

						}

					});
				}
				
				
				function getUInfo()
		  		 {
                    has = -1;
		  		 	var url = BaseUrl+"jifen.getUinfo&uid="+uid+"&username="+uname;

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  			var arr = data.data.info;
		  		 		
		  		 		if(arr)
		  		 		{
			  		 		if(arr.length > 0)
			  		 		{
				  		 		var item = arr[0];
				  		 		has = item["hfb"]*1;
				  		 		check();		  		 					  		 		
			  		 		}

						}

					});
				}
				
				function check()
				{
					if(need == null || has == null)
					{
						return;
					}
					
					if(need < 0 || has < 0)
					{
						return;
					}
					
					if(has < need)
					{
						$('#bugou').removeClass("hidden");
					}
					else
					{
					    $('#bugou').addClass("hidden");
					}
				}

				function dh()
				{
					if(running)
					{
						return;
					}
					
					sendMsgToAPP({'msg':'开始兑换商品','type':'2'});
					
					running = true;
					
					var url = BaseUrl+"jifen.AddDH&uid="+uid+"&username="+uname+"&id="+id;

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  			var code = data.data.code;
		  		 		
		  		 		if(code == 0)
		  		 		{
		  		 		    getinfo();
                            getUInfo();
			  		 		var id = data.data.info.id;
			  		 		sendMsgToAPP({'msg':'商品兑换成功','type':'2','id':id});
			  		 		running = false;
			  		 		return;
						}
						
						var msg = data.data.msg;
						if(msg == null)
						{
							msg = "";
						}
						
						msg = msg == "" ? "兑换失败" : msg;
						
						sendMsgToAPP({'msg':'商品兑换失败','type':'2','info':msg});
						running = false;

					});
					
					
				}

	
		
	})
	
	
     
   
});

function toGoodsCenter()
{
	sendMsgToAPP({'msg':'跳转怀府币商城','type':'3'});
}

function doDH()
{
// 	sendMsgToAPP({'msg':'跳转签到规则','type':'0'});
//$('#alertBG').removeClass("hidden")
}

function alertClick(item)
{
/*
	if(item == 0)
	{
		$('#alertBG').addClass("hidden")
	}
	else
	{
				  		
	}
*/
}




