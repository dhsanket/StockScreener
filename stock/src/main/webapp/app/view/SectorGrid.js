Ext.define('StockBoard.view.SectorGrid', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.sectorgrid',
	title:'Stock Details',
	border: false,
	//id:'sectorgrid',
	requires:['StockBoard.store.StoreStock'],
	initComponent: function() {
		Ext.apply(this, {
			store: Ext.data.StoreManager.lookup('StoreStock'),
			autoScroll: true,
			autoheight:true,
			stateful: true,
	        collapsible: true,
	        multiSelect: true,
	      /*  viewConfig: {
	            getRowClass: function(record, index) {
	                var c = record.get('mktcap');
	                var temp=c/1000000;
	                 if (c > 0) {
	                	// alert(parseInt(c));
	                	record.set('mktcap',temp);

	                }
	            },
	            markDirty:false
	        },*/

			//collapsible: true,
			//layout:'fit',
			//id:'sectorgrid',
			columns: [{
				text:'Date',
				dataIndex:'curdate',
				//renderer: Ext.util.Format.dateRenderer('m/d/Y'),
				flex:1
			},{
				text: 'Name',
				dataIndex:'name',
				flex:1
			},{
				text: 'Ticker',
				dataIndex:'code',
				flex:1
			},{
				text: 'Gain/Loss %',
				dataIndex:'gainpercent',
				renderer: Ext.util.Format.numberRenderer('0.000'),
				flex:1
			},{
				text: 'Rank',
				dataIndex:'rank',
				flex:1
			},{
				text:'Current Price',
				dataIndex:'price',
				flex:1
			},
			{
				text:'Volume',
				dataIndex:'volume',
				flex:1
			},{
				text: 'Market Cap',
				dataIndex:'mktcap',
				//name:'mktcap',
				flex:1


				//renderer:this.formatmcap
			},{
				text: 'P/E',
				dataIndex:'pe',
				renderer: Ext.util.Format.numberRenderer('0.000'),
				flex:1
			},{
				text:'Start Price',
				dataIndex:'value2',
				//text: 'Price Sold',
				//dataIndex:'value1',
				renderer: Ext.util.Format.numberRenderer('0.000'),
				flex:1
			},{
				//text: 'Purchase Price',
				//dataIndex:'value2',
				text:'End Price',
				dataIndex:'value1',
				renderer: Ext.util.Format.numberRenderer('0.000'),
				flex:1
			},{
				text: 'Stock Exchange',
				dataIndex:'stockexchange',
				flex:1
			}]
		,
			dockedItems: [{
				xtype: 'pagingtoolbar',
				store:  Ext.data.StoreManager.lookup('StoreStock'),
				dock: 'bottom',
				displayInfo: true,
				pageSize:50,
				displayMsg: 'Displaying stocks order {0} - {1} of {2}'
			}],
			listeners: {
		        afterrender: function( eOpts )
		        {
		        	var store = Ext.data.StoreManager.lookup('StoreStock');
		        	var tooltip="";

		      		store.on('load', function(store, records) {
		      		    for (var i = 0; i < records.length; i++) {
		      		    	if(i==0){
		      		    console.log(records[i].get('startdate'));
		      		  tooltip="StartDate:"+records[i].get('startdate')+"<br>"+"EndDate:"+records[i].get('enddate');
		      		    	}
		      		    }
		      		});

		          view = this.getView();
		          view.tip = Ext.create('Ext.tip.ToolTip', {
		            target: view.el,
		            delegate: view.itemSelector,
		            trackMouse: true,
		            renderTo: Ext.getBody(),
		            listeners: {
		              beforeshow: function updateTipBody(tip) {
		              tip.update(tooltip);
		            }
		          }
		        });
		      }
		    }
		});

		this.callParent(arguments);
	}

});