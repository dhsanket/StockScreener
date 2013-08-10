Ext.define('StockBoard.store.StoreExchange', {
    extend: 'Ext.data.Store',
    model: 'StockBoard.model.ModelExchange',
   autoLoad: true,

    proxy: {
        type: 'ajax',
        url: 'StockExchange',
        extraParams: {
            stckexchange: 1
        },
        reader: {
            type: 'json',
           // totalProperty: 'totalCount',
            root: 'data',
            successProperty: 'success'
        }
     }


});
