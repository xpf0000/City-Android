
var vm;

var uid = "";
var uname = "";
var ordernumber = "";

requirejs(['main'], function (main) {  
	
		require(['avalon','net'], function() {
	     		
	     		uid = $.getUrlParam('uid');
	     		uname = $.getUrlParam('uname');
	     		ordernumber = $.getUrlParam('ordernumber');
	     		 
	 			vm = avalon.define({
	    
		  		$id: "success",
		  		
		  		info:{},
		  		
		  		});
		  		
		  		getinfo();
	
				function getinfo()
		  		 {
			  		 
		  		 	var url = BaseUrl+"hyk.getOrderInfo&uid="+uid+"&username="+uname+"&ordernumber="+ordernumber;

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  			var arr = data.data.info;
		  		 		
		  		 		if(arr)
		  		 		{
			  		 		if(arr.length > 0)
			  		 		{
				  		 		var item = arr[0];	
				  		 		if(item.pay_time == null)
				  		 		{
					  		 		item.pay_time = "";
				  		 		}
				  		 		else
				  		 		{
					  		 		item.pay_time = $.myTime.UnixToDateFormat(item.pay_time,"yyyy-MM-dd HH:mm:ss");
				  		 		}
				  				  		 		
				  		 		vm.info = item;		
			  		 		}

						}

					});
				}
		  		
		  		
  				
	});
	
   
});





