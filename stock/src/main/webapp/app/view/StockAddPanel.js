Ext.define('StockBoard.view.StockAddPanel', {
	extend: 'Ext.form.Panel',
	alias: 'widget.stockaddpanel',
	width: 660,
	layout: 'fit',
	autoScroll:true,

	//requires:['Ext.form.field.File'],
    initComponent: function() {
		Ext.apply(this, {
				bodyStyle: 'padding: 10px;',
				layout:'vbox',
				//height:'230px',
				border:0,
				/*items: [{
					 height:'170px',
					 flex:1,
					 border:0,
					 padding:'10 px',*/
					/* fieldLabel:'Stock Exchange',
					 name:'stockexchange',
					 store:['NYSE','BSE'],
					 xtype:'combo',
					 displayField: 'value',
					 valueField: 'id',
					 forceSelection: true,
					 editable: false,
					 allowBlank:false,*/
					items:[{
						fieldLabel:'Stock Exchange',
						 name:'stockexchange',
						 store:['NYSE','BSE'],
						 xtype:'combo',
						 displayField: 'value',
						 value: 'NYSE',
						 valueField:'value',
						 forceSelection: true,
						 editable: false,
						 pack:'center'
						 //allowBlank:false
					//}]
				},{
					layout:'hbox',
					 flex:1,
					 border:0,

				//	items:[{

						 //padding:'10 px',
						items:[{
							//border:0,
							xtype: 'textfield',
							name: 'fileName',
							//id:'fileName',
							//emptyText: 'Select a File to Upload',
							fieldLabel: 'File',
							flex:1,
							//buttonText: '',
							//buttonConfig: {
								//iconCls: 'upload-icon'
									//},
					listeners: {                                                    // Listener added here with change event
			            		   'change': function(field, newVal, oldVal){
			            		   var form = this.up('form').getForm();
			            		   fields = form.getFields();
			            		   if(newVal==""){
			            			   Ext.each(fields.items, function (f)
				            				   {
			            				   f.inputEl.dom.disabled = false;
			            				   });
			            		   }else{
			            			   Ext.each(fields.items, function (f)
				            				   {
			            				   if(f.name!="fileName"){
			            				   f.inputEl.dom.disabled = true; }});
			            		   }

			            		   }
				            }
					 //}]
				 },{ height:'170px',
					 flex:1,
					 border:0,

					items:[{
						xtype:'label',
						text:'  OR  ',
						flex:1
						//padding:'5 px',
						//border:0
					}]
				 },{
					 height:'170px',
					 flex:1,
					 border:0,
					 items:[{
						 xtype: 'textfield',
				            name: 'id',
				            readOnly:true,
				            fieldLabel: 'Id'
				        },
						   {
							 fieldLabel:'Stock Symbol',
							 xtype:'textfield',
							 //id:'code',
							 name:'code',
							// allowBlank:false,
							 maxLength:60
						 },{
							 fieldLabel:'Stock name',
							 xtype:'textfield',
							 name:'name',
							 //id:'name'//,
							 //vtype:'alphaSpace'
						 }, {
								   xtype:'checkbox',
								   fieldLabel:'Is Active',
								   name:'isactive',
								  // id:'isactive',
								   inputValue: true,
								   uncheckedValue: false,
								   checked:true
	                    }]
				 }
	            ]

				}]
				/*items: [{
					xtype:'form',
					 height:'200px',
					 autoScroll:true,
					 flex:1,
					 border:0,
					items:[{
			            xtype: 'textfield',
			            name: 'fileName',
			            fieldLabel: 'File Name',
			            	 listeners: {                                                    // Listener added here with change event
			            		   'change': function(field, newVal, oldVal){
			            		   var form = this.up('form').getForm();
			            		   fields = form.getFields();
			            		   if(newVal==""){
			            			   Ext.each(fields.items, function (f)
				            				   {
			            				   f.inputEl.dom.disabled = false;
			            				   });
			            		   }else{
			            			   Ext.each(fields.items, function (f)
				            				   {
			            				   if(f.name!="fileName"){
			            				   f.inputEl.dom.disabled = true; }});
			            		   }

			            		   }
			            	 }
			        },/*{
						xtype: 'filefield',
			            id: 'file',
			            emptyText: 'Select a file',
			            fieldLabel: 'File',
			            name: 'fileName',
			            labelWidth: 50,
			            msgTarget: 'side',
			            allowBlank: false,
			            anchor: '100%',
			            buttonText: 'Select a File...'

					},*//*{
			            xtype: 'textfield',
			            name: 'id',
			            readOnly:true,
			            fieldLabel: 'Id'
			        },
					   {
						 fieldLabel:'Stock Symbol',
						 xtype:'textfield',
						 //id:'code',
						 name:'code',
						// allowBlank:false,
						 maxLength:60
					 },{
						 fieldLabel:'Stock name',
						 xtype:'textfield',
						 name:'name',
						 //id:'name'//,
						 //vtype:'alphaSpace'
					 }, {
							   xtype:'checkbox',
							   fieldLabel:'Is Active',
							   name:'isactive',
							  // id:'isactive',
							   inputValue: true,
							   uncheckedValue: false,
							   checked:true

					 }]
				 }
	            ]*/
			,buttons:[{
            	text:'Save',
            	pack:'center',
            	//align:'middle',
            	handler:function(){
            		var form=this.up('form').getForm();
            		var win=this.up('window');
            		var chckValid=0;
            		var insertFlag=2;
            		if(form.findField('fileName').getValue()==""){
            			insertFlag=2;
           			 	}else{
           			 	insertFlag=1;
           			 	}
            		if((insertFlag==2)&&(form.findField('code').getValue()=="")){
            			Ext.Msg.alert('Error','Please insert either filename or stock symbol');
            			chckValid=1;
            		}
            		if(chckValid==0){
            		form.submit({
           			 url:'History',

           			params: {insertFlag:insertFlag},
           			//params: {insertFlag:1,fileName:"C://Users/saranya/Downloads/NYSE_20130705.csv"},
           			 success: function(form, action) {
                         Ext.Msg.alert('Success', action.result.msg);
                         Ext.MessageBox.confirm('Confirm' , 'Do you want to insert history data?', function(btn){
								if(btn == 'yes'){
									var store = Ext.data.StoreManager.lookup("StoreHistory");
						    		 store.proxy.extraParams.insertFlag=1;
						    		 store.load();
									 Ext.data.StoreManager.lookup('StoreStock').load();


						    		}
						        }
						        );
                         if(win)//during edit mode there will be no window
                        	 win.close();
                     },
                     failure: function(form, action) {
                      var msg = action.result.msg;
                         Ext.Msg.alert('Failed', msg);
                     }
           		 });
            		}
            	}
            }]

				//}]
		});



		this.callParent(arguments);
	}
});