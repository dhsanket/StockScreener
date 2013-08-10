Ext.define('StockBoard.view.FileUpload', {
	extend: 'Ext.form.Panel',
	alias: 'widget.fileupload',
	width: 570,
	layout: 'fit',
	autoScroll:true,
	//requires:['Ext.form.field.File'],
    initComponent: function() {
		Ext.apply(this, {
				bodyStyle: 'padding: 10px;',
				layout:'hbox',
				height:'230px',

				items: [{
					xtype: 'filefield',
					name: 'fileName',
					fieldLabel: 'File',
					labelWidth: 50,
					msgTarget: 'side',
					allowBlank: false,
					anchor: '100%',
					buttonText: 'Select a File...'
					}],
					buttons: [{
					text: 'Upload',
					handler: function() {
					var form = this.up('form').getForm();
					if(form.isValid()){
					form.submit({
					url: 'History',
					params: {insertFlag:1},
					waitMsg: 'Uploading your file...',
					success: function(fp, o) {
					Ext.Msg.alert('Success', 'Your file has been uploaded.');
					}
					});
					}
					}
					}]
		});

		this.callParent(arguments);
	}
});