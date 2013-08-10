Ext.define('StockBoard.view.StockSymbGrid', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.stocksymbgrid',
	border: false,
	requires:['StockBoard.store.StoreStock'],
	//autoscroll:true,
	height:	200,
	initComponent: function() {
		Ext.apply(this, {
			store: Ext.data.StoreManager.lookup('StoreSymbol'),
			autoScroll: true,
			//height:'230px',
			stateful: true,
	        collapsible: true,
	        //multiSelect: true,
			//collapsible: true,
			//layout:'fit',
			//id:'sectorgrid',
			columns: [{
				text:'Id',
				dataIndex:'id',
				flex:1
			},{
				text: 'Stock symbol',
				dataIndex:'code',

				//renderer: Ext.util.Format.dateRenderer('m/d/Y'),
				flex:1
			},{
				text: 'Stock name',
				dataIndex:'name',
				flex:1
			},{

				text: 'Status',
				dataIndex:'isactive',
				flex:1
			}]
		,
			dockedItems: [{
				xtype: 'pagingtoolbar',
				store:  Ext.data.StoreManager.lookup('StoreSymbol'),
				dock: 'bottom',
				displayInfo: true,
				displayMsg: 'Displaying stocks order {0} - {1} of {2}',
				emptyMsg: "No stocks to display",
				pageSize:50
			}],listeners: {
				itemdblclick: function(dv, record, item, index, e) {
					//var stockwin= this.;
					//var stockWindow= new StockBoard.view.StockWindow();
					var frm=this.up('form').getForm();
					frm.loadRecord(record);
					frm.findField('name').setReadOnly(true);
					frm.findField('code').setReadOnly(true);
					frm.findField('fileName').setReadOnly(true);
					frm.findField('stockexchange').setReadOnly(true);
					//stockWindow.down('stocksymbolwindow').loadRecord(record);

					/*stockWindow.down('stocksymbolwindow').query('.field').forEach(function(field) {
						alert(field.name);
						if(field.name=="code" || field.name=="name" || field.name=="fileName" || field.name=="stockexchange"){
			            if (field.inputEl) {
			                field.inputEl.dom.setAttribute('readOnly', true);
			            } else {
			                field.readOnly = true;
			            }
						}
						if(field.name=="fileName" || field.name=="stockexchange"){
							 if (field.inputEl) {
							 field.inputEl.dom.setAttribute('Visible', false);
							 }else{
							field.Visible=false;
							 }
						}
			        });
*/
				}
			},


		});
		this.callParent(arguments);
	}

});