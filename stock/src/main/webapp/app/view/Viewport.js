Ext.define('StockBoard.view.Viewport', {
    extend: 'Ext.container.Viewport',
    requires:[
        'Ext.tab.Panel',
        'Ext.layout.container.Border',
        'StockBoard.view.SectorGrid'
    ],

    layout: {
        type: 'border'
    },

    items: [{
        region: 'north',
        xtype: 'searchpanel',
        title: 'Search'
    },{
        region: 'center',
        xtype: 'tabpanel',
        items:[{
        	xtype:'sectorgrid',
            title: '1: Day -3 To Yesterday',
            name:'1',
            value:'1',
            tabConfig:{
            tooltip:'firsttab'
            }

        },{
        	xtype:'sectorgrid',
            title: '2: Day -7 To Day -3',
            name:'2',
            value:'2',
           // id:'2'

        },{
        	xtype:'sectorgrid',
            title: '3:Day -14 To Day -7',
            name:'3'
        },{
        	xtype:'sectorgrid',
            title: '4:Day -31 To Day -14',
            name:'4'
        },{
        	xtype:'sectorgrid',
            title: '5: Current Month',
            name:'5'
        },{
        	xtype:'sectorgrid',
            title: '6: Month -1',
            name:'6'
        },{
        	xtype:'sectorgrid',
            title: '7: Month -2',
            name:'7'
        },{
        	xtype:'sectorgrid',
            title: '8: Month -2  To Yesterday',
            name:'8'
        },{
        	xtype:'sectorgrid',
            title: '9: Last 3 Month',
            name:'9'
        },{
        	xtype:'sectorgrid',
            title: '10: Last 6 Month',
            name:'10'
        },{
        	xtype:'sectorgrid',
            title: '11: Last 12 Month',
            name:'11'
        }],
        listeners: {
            'tabchange': function(tabPanel, tab){
            	var store = Ext.data.StoreManager.lookup("StoreStock");
            	//alert(store.getAt(0).get('startdate'));
        		store.proxy.extraParams.sector=tab.name;
        		//tab.setToolTip(new ToolTipConfig("Information", "Prints the current document"));
        		//tab.setToolTip("checkkkkkkkk");
        		store.load();
            }
        }
    }]

});
