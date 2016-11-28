
var vm;
var id = "";


requirejs(['main'], function (main) {  
	
		require(['avalon','net'], function() {
	     	
	     	id = $.getUrlParam('id');
		 	
	     	 
	 			vm = avalon.define({
	    
		  		$id: "success",
		  		
		  		info:{},
		  				
		  		
		  		});
		  		
		  		getinfo();
	
				function getinfo()
		  		 {
			  		 
		  		 	var url = BaseUrl+"Jifen.getDhinfo&id="+id;

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  			var arr = data.data.info;
		  		 		
		  		 		if(arr)
		  		 		{
			  		 		if(arr.length > 0)
			  		 		{
				  		 		var item = arr[0];				  		 		
				  		 		vm.info = item;		
			  		 		}

						}

					});
				}

	})
	
	
     
   
});





