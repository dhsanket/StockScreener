Ext.define('StockBoard.store.StoreSymbol', {
    extend: 'Ext.data.Store',
    model: 'StockBoard.model.ModelSymbol',
    autoLoad: true,
    pageSize: 50,
    autoLoad: {start: 0, limit: 50},
    proxy: {
        type: 'ajax',
        url: 'StockSymbol',
        waitMsg:'Loading..please wait..',
        extraParams: {
            sector: 1
        },
        reader: {
            type: 'json',
            totalProperty: 'totalCount',
            root: 'count',
            successProperty: 'success',
            messageProperty: 'message'
        },
     }


});
