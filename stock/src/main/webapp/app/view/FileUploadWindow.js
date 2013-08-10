Ext.define('StockBoard.view.FileUploadWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.fileuploadwindow',
	width: 570,
	title: 'Add/Edit Stock',
	layout: 'fit',
	requires:[
		       	'StockBoard.view.FileUpload'
		       	],
    initComponent: function() {
		Ext.apply(this, {
			items: [{
				xtype:'fileupload'
			}]

		});

		this.callParent(arguments);
	}
});