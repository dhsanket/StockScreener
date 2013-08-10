Ext.define('StockBoard.view.StockWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.stockwindow',
	width: 660,
	autoheight:true,
	title: 'Add/Edit Stock',
	layout: 'fit',
	requires:[
		       	'StockBoard.view.StockSymbolWindow'
		       	],
    initComponent: function() {
		Ext.apply(this, {
			bodyStyle: 'padding: 10px;',
			//layout:'hbox',
			items:[{

				xtype:'stocksymbolwindow',
				flex:1
			}]

		});

		this.callParent(arguments);
	}
});