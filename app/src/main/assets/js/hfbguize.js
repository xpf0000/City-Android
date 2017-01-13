var id = "";

//requirejs(['main'], function (main) {
//
//		require(['avalon','net'], function() {
	     			 
	     		id = $.getUrlParam('id');
	     			 
	 			var vm = avalon.define({
	    
		  		$id: "guize",
		  		
		  		info:{
			  		content:"",
		  		},
		  		
		  		});
		  		
		  		getinfo();
	
				function getinfo()
		  		 {
			  		 
		  		 	var url = BaseUrl+"News.getArticle&id="+id;

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
		  		
		  		
  				
//	});
//
//
//});





