Ext.define('StockBoard.view.StockSymbolWindow', {
  //extend : 'Ext.window.Window',
  extend: 'Ext.form.Panel',
  alias : 'widget.stocksymbolwindow',
  //title : 'Stock Symbol',
  requires : [ 'StockBoard.view.StockSymbGrid' ],
  width : 610,
  initComponent : function() {
    Ext.apply(this, {
      items : [ {
        xtype : 'form',
        flex : 1,
				layout : 'vbox',
				bodyStyle : 'padding: 10px;',
				items : [ {
					layout : 'hbox',
					border : 0,
					width : 600,
					items : [ {
						//{
						//flex : 0.05,

						border : 0,
						items :  [{
							fieldLabel:'Stock Exchange',
						 name:'stockexchange',
						 store:['NYSE','NASDAQ','LSE'],
						 xtype:'combo',
						 displayField: 'value',
						 value: 'NYSE',
						 valueField:'value',
						 forceSelection: true,
						 editable: false,
						 pack:'center',

						} ]
					}]

				}, {
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
				}
			]

			,
			buttons : [ {
				text : 'Save',
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
           			waitMsg:"Loading...",
           			//params: {insertFlag:1,fileName:"C://Users/saranya/Downloads/NYSE_20130705.csv"},
           			 success: function(form, action) {". ;m/"
                         Ext.Msg.alert('Success', action.result.msg);
                         Ext.data.StoreManager.lookup('StoreSymbol').load();
                         form.findField('name').setReadOnly(false);
    					form.findField('code').setReadOnly(false);
    					form.findField('fileName').setReadOnly(false);
    					form.findField('stockexchange').setReadOnly(false);
                         form.reset();
                        /* Ext.MessageBox.confirm('Confirm' , 'Do you want to insert history data?', function(btn){
								if(btn == 'yes'){
									var store = Ext.data.StoreManager.lookup("StoreHistory");
						    		 store.proxy.extraParams.insertFlag=1;
						    		 store.load();
									 Ext.data.StoreManager.lookup('StoreSymbol').load();


						    		}
						        }
						        );
                         if(win)//during edit mode there will be no window
                        	 win.close();*/
                     },
                     failure: function(form, action) {
                      var msg = action.result.msg;
                         Ext.Msg.alert('Failed', msg);
                     }
           		 });
            		}
            	}
			}, {
				text:'Clear',
				handler: function() {
					var frm=this.up('form').getForm();
					frm.findField('name').setReadOnly(false);
					frm.findField('code').setReadOnly(false);
					frm.findField('fileName').setReadOnly(false);
					frm.findField('stockexchange').setReadOnly(false);
		            frm.reset();
		        }
			},{
				text : 'Close',
				scope:this,
				handler:this.close,

			} ],
			},{
				xtype:'stocksymbgrid'
			} ],
			listeners:{

		        close:function(){
		        	 if(this.up('window'))
						 this.up('window').close();

		        	Ext.MessageBox.confirm('Confirm' , 'Do you want to insert history data?', function(btn){
						if(btn == 'yes'){
							var store = Ext.data.StoreManager.lookup("StoreHistory");
				    		 store.proxy.extraParams.insertFlag=1;
				    		 store.load();
				    		 Ext.MessageBox.show({
				                 msg: 'Saving your data, please wait...',
				                 progressText: 'Saving...',
				                 width:300,
				                 wait:true,
				                 waitConfig: {interval:20000},
				                 animateTarget: ''
				              });
				              setTimeout(function(){
				                  Ext.MessageBox.hide();
				                  Ext.data.StoreManager.lookup('StoreStock').load();
				                 // Ext.example.msg('Done', 'data saved!');
				                  }, 8000);


							// if(this.up('window'))
//								 this.up('window').close();

				    		}else{
				    			Ext.data.StoreManager.lookup('StoreStock').load();
				    		}
				        }
				        );


		        }
		    }
		});

		this.callParent(arguments);
	}
});