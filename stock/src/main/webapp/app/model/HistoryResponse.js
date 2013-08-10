Ext.define('StockBoard.model.HistoryResponse', {
    extend: 'Ext.data.Model',

    fields: //['code','curdate','gainpercent','id','mktcap','name','pe','type','value1','value2','volume']
       [
         //{ name: 'id', type: 'int' },
        { name: 'histDate', type: 'auto' },
        { name: 'count', type: 'int' }
    ],
   // idProperty:'id'

});

