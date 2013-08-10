Ext.define('StockBoard.model.ModelSymbol', {
    extend: 'Ext.data.Model',

    fields: //['code','curdate','gainpercent','id','mktcap','name','pe','type','value1','value2','volume']
       [ { name: 'id', type: 'int' },
        { name: 'name', type: 'auto' },
        { name: 'code', type: 'auto' },
        { name: 'isactive', type: 'boolean' },
        { name:'stockexchange', type: 'auto'}
    ],
    idProperty:'id'

});

