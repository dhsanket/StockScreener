Ext.define('StockBoard.view.StockLogGrid', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.stockloggrid',
	border: false,
	requires:['StockBoard.store.StoreHistory'],
	initComponent: function() {
		Ext.apply(this, {
			store: Ext.data.StoreManager.lookup('StoreHistory'),
			autoScroll: true,
			autoheight:true,
			//stateful: true,
	       // collapsible: true,
	        //multiSelect: true,
			//collapsible: true,
			//layout:'fit',
			//id:'sectorgrid',
			columns: [{
				text: 'History Date',
				dataIndex:'histDate',

				//renderer: Ext.util.Format.dateRenderer('m/d/Y'),
				flex:1
			},{
				text: 'Count',
				dataIndex:'count',
				flex:1

			}]

		});
		this.callParent(arguments);
	}
});