Ext.define('StockBoard.view.StockLogWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.stockwindow',
	width: 570,
	title: 'Stock Log (Last 10 Days)',
	layout: 'fit',
	requires:[
		       	'StockBoard.view.StockLogGrid'
		       	],
    initComponent: function() {
		Ext.apply(this, {
			items: [{
				xtype:'stockloggrid'
			}]

		});

		this.callParent(arguments);
	}
});