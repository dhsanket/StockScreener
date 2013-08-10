Ext.define('StockBoard.model.StockResponse', {
    extend: 'Ext.data.Model',

    fields: //['code','curdate','gainpercent','id','mktcap','name','pe','type','value1','value2','volume']
       [ { name: 'id', type: 'int' },
        { name: 'name', type: 'auto' },
        { name: 'curdate', type: 'auto' },
        { name: 'price', type: 'auto' },
        { name: 'code', type: 'auto' },
        { name: 'volume', type: 'auto' },
        { name: 'mktcap', type: 'auto' },
        { name: 'pe', type: 'auto' },
        { name: 'gainpercent', type: 'auto' },
        { name: 'value1', type: 'auto' },
        { name: 'value2', type: 'auto' },
        { name: 'rank', type: 'int' },
        { name: 'sector', type: 'int' },
        { name: 'isactive', type: 'boolean' },
        { name:'stockexchange', type: 'auto'},
        { name:'startdate', type: 'auto'},
        { name:'enddate', type: 'auto'}
    ],
    idProperty:'id'

});

