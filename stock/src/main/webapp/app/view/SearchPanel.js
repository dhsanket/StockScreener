Ext.define("StockBoard.view.SearchPanel", {
    extend: 'Ext.form.Panel',
    alias:'widget.searchpanel',
    initComponent: function() {
		Ext.apply(this, {
			border: false,
			//frame:true,
			//split:true,
		    /*fieldDefaults:{
		    	msgTarget:'side',
		    	labelWidth:100,
		    	bodyStyle: 'padding: 5px;',
		    },*/
			items:[{
		    items:[{
    	xtype:'multislider',
    	fieldLabel:'Volume',
    	name:'volume',
    	id:'volume',
        minValue:0,
        maxValue:1000000,
        values:[0,10000000],
        constrainThumbs: true,
        width:300,


    },{
    	xtype:'multislider',
    	fieldLabel:'Market Captial',
    	name:'marketcap',
    	id:'marketcap',
    	values:[0,1000000000000],
        minValue: 0,
        maxValue: 1000000000000,
        constrainThumbs: true,
        width:300
    },{
    	xtype:'multislider',
    	fieldLabel:'P/E',
    	name:'pe',
    	id:'pe',
    	values:[0,100],
        minValue: 0,
        maxValue: 100,
        constrainThumbs: true,
        width:300
    },{
    	xtype:'combo',
		fieldLabel:'Stock Exchange',
		//triggerAction : 'all',
		editable : false,
		//allowBlank:false,
	    store:Ext.data.StoreManager.lookup('StoreExchange'),
	   //store:["NYSE","LSE"],
	    valueField: 'stockexchange',
	    displayField: 'stockexchange',
		mode:'local',
		name:'stockexchange',
		//id:'stockexchange',
		//allowBlank:false
    }],},{
    buttonAlign:'center',
    buttons:[{
    	text:'Reset',
    	handler: function() {
            this.up('form').getForm().reset();
            var store = Ext.data.StoreManager.lookup('StoreMinMax');

      		store.load();
      		store.on('load', function(store, records) {
      		    for (var i = 0; i < records.length; i++) {
      		    	if(i==0){
      		    if(records[i].get('maxpe')!=records[i].get('minpe')){
          		    Ext.getCmp('pe').maxValue=records[i].get('maxpe');
              		Ext.getCmp('pe').minValue=records[i].get('minpe');
              		Ext.getCmp('pe').setValue([records[i].get('minpe'),records[i].get('maxpe')]);
          		    }
          		  if(records[i].get('minvolume')!=records[i].get('maxvolume')){
              		 Ext.getCmp('volume').maxValue=records[i].get('maxvolume');
               		Ext.getCmp('volume').minValue=records[i].get('minvolume');
               		Ext.getCmp('volume').setValue([records[i].get('minvolume'),records[i].get('maxvolume')]);
          		 }
          		  if(records[i].get('minmrktcap')!=records[i].get('maxmrktcap')){
               		 Ext.getCmp('marketcap').maxValue=records[i].get('maxmrktcap');
               		Ext.getCmp('marketcap').minValue=records[i].get('minmrktcap');
               		Ext.getCmp('marketcap').setValue([records[i].get('minmrktcap'),records[i].get('maxmrktcap')]);
          		  }
      		    	}
      		    };
      		});
        }
    },{
    	text:'Search',
    	handler:function(){

    		var form = this.up('form').getForm();
    		var storesector=Ext.data.StoreManager.lookup('StoreStock');
    		var sector=1;
    		if(storesector.getAt(0)==undefined){
    			sector=1;
    		}else{
    		 sector=storesector.getAt(0).get('sector');
    		}
    		 var volume = form.findField('volume');
             var volumevalues = "";
             for (var i = 0; i < volume.thumbs.length; i++) {
            	 volumevalues+=volume.thumbs[i].value + ' ';
             }
             var marketcap = form.findField('marketcap');
             var mcapvalues = "";
             for (var i = 0; i < marketcap.thumbs.length; i++) {
            	 mcapvalues+=marketcap.thumbs[i].value + ' ';
             }
             var pe = form.findField('pe');
             var pevalues = "";
             for (var i = 0; i < pe.thumbs.length; i++) {
            	 pevalues+=pe.thumbs[i].value + ' ';
             }
            var stockexchange = form.findField('stockexchange').getValue();
             var store = Ext.data.StoreManager.lookup("StoreStock");
     		store.proxy.extraParams.volume=volumevalues;
     		store.proxy.extraParams.marketcap=mcapvalues;
     		store.proxy.extraParams.pe=pevalues;
     		store.proxy.extraParams.sector=sector;
     		store.proxy.extraParams.stockexchange=stockexchange;
     		store.proxy.extraParams.srchFlag=1;
     		store.load();
    	}
    },{
    	text:'Log',
    	handler:function(){
    		var store = Ext.data.StoreManager.lookup("StoreHistory");
    		store.proxy.extraParams.insertFlag=0;
    		store.load();
    		var stockLogWindow= new StockBoard.view.StockLogWindow();
    		stockLogWindow.show();
    	}
    },{
    	text:'Populate History Data',
    	handler:function(){
    		var store = Ext.data.StoreManager.lookup("StoreHistory");
    		store.proxy.extraParams.insertFlag=1;
    		store.load();
            Ext.MessageBox.show({
                msg: 'Saving your data, please wait...',
                progressText: 'Saving...',
                width:300,
                wait:true,
                waitConfig: {interval:20000},
                animateTarget: 'InsertHistory'
             });
             setTimeout(function(){
                 Ext.MessageBox.hide();
                // Ext.example.msg('Done', 'data saved!');
                 }, 8000);

    	}
    },{
    		text:'Add Stock Symbol',
        	handler:function(){

        		var stockWindow= new StockBoard.view.StockWindow();
        		stockWindow.show();
    	}
    },{
    	text:'Refresh',
    	handler:function(){
    		var store = Ext.data.StoreManager.lookup("StoreStock");
    		store.proxy.extraParams.srchFlag=0;
    		store.load();
    	}
   /* },{
    	text:'InsertStockSymbolFromFile',
    	handler:function(){
    		//var store = Ext.data.StoreManager.lookup("StoreHistory");
    		//store.proxy.extraParams.insertFlag=2;
    		//store.load();
    		var filewindow= new StockBoard.view.FileUploadWindow();
    		filewindow.show();
	}*/
    }],

         }],

    });
		this.callParent(arguments);
	},
          listeners:
      	{render:function(){
      		var store = Ext.data.StoreManager.lookup('StoreMinMax');

      		store.load();
      		store.on('load', function(store, records) {
      		    for (var i = 0; i < records.length; i++) {
      		    	if(i==0){
      		    console.log(records[i].get('minpe'));
      		    if(records[i].get('maxpe')!=records[i].get('minpe')){
      		    Ext.getCmp('pe').maxValue=records[i].get('maxpe');
          		Ext.getCmp('pe').minValue=records[i].get('minpe');
          		Ext.getCmp('pe').setValue([records[i].get('minpe'),records[i].get('maxpe')]);
      		    }
      		  if(records[i].get('minvolume')!=records[i].get('maxvolume')){
          		 Ext.getCmp('volume').maxValue=records[i].get('maxvolume');
           		Ext.getCmp('volume').minValue=records[i].get('minvolume');
           		Ext.getCmp('volume').setValue([records[i].get('minvolume'),records[i].get('maxvolume')]);
      		 }
      		  if(records[i].get('minmrktcap')!=records[i].get('maxmrktcap')){
           		 Ext.getCmp('marketcap').maxValue=records[i].get('maxmrktcap');
           		Ext.getCmp('marketcap').minValue=records[i].get('minmrktcap');
           		Ext.getCmp('marketcap').setValue([records[i].get('minmrktcap'),records[i].get('maxmrktcap')]);
      		  }
      		    	}
      		    };
      		});

      		}
      	}

});