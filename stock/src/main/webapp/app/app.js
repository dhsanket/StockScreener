/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when it performs code generation tasks such as generating new
    models, controllers or views and when running "sencha app upgrade".

    Ideally changes to this file would be limited and most work would be done
    in other places (such as Controllers). If Sencha Cmd cannot merge your
    changes and its generated code, it will produce a "merge conflict" that you
    will need to resolve manually.
*/

// DO NOT DELETE - this directive is required for Sencha Cmd packages to work.
//@require @packageOverrides

Ext.application({
    name: 'StockBoard',

    models:[
        'StockResponse','ModelSymbol','ModelMinMax','ModelExchange'
    ],
    stores:[
           'StoreStock','StoreHistory','StoreSymbol','StoreExchange','StoreMinMax'],
    views: [
        'Main',
        'SearchPanel',
        'Viewport',
        'SectorGrid',
        'StockSymbGrid',
        'StockLogWindow',
        'StockWindow',
       // 'StockSymbolWindow',
        'FileUploadWindow'
    ],

    controllers: [
        'Main'
    ],
    init:function(){
    	this.control({
    		 'viewport > panel' : {
                 render : this.onPanelRendered
             }
              });

    },
    launch:function(){
    	console.log('The launch panel was rendered  from app');
    	var store=Ext.data.StoreManager.lookup('StoreSymbol');
    	store.load();
    	//alert("Loading...");
    	Ext.MessageBox.show({
            msg: 'Loading...',
            progressText: 'Loading...',
            width:300,
            wait:true,
            waitConfig: {interval:500},
            animateTarget: ''
         });
         setTimeout(function(){
             Ext.MessageBox.hide();
          //   Ext.data.StoreManager.lookup('StoreStock').load();
         	if(store.getAt(0)==undefined){
        		var stockWindow= new StockBoard.view.StockWindow();
        		stockWindow.show();

        	}else{

        	  Ext.MessageBox.confirm('Confirm' , 'Do you want to insert history data?', function(btn){
    				if(btn == 'yes'){
    					var store = Ext.data.StoreManager.lookup("StoreHistory");
    		    		 store.proxy.extraParams.insertFlag=1;
    		    		 store.load();
    				//	 Ext.data.StoreManager.lookup('StoreStock').load();


    		    		}
    		        }
    		        );

        	}
            // Ext.example.msg('Done', 'data saved!');
             }, 1000);


    },

    onPanelRendered : function() {
        //just a console log to show when the panel si rendered
        console.log('The panel was rendered  from app');
    },

    autoCreateViewport: true
});
