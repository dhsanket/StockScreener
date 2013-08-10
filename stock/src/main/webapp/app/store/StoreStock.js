Ext.define('StockBoard.store.StoreStock', {
    extend: 'Ext.data.Store',
    model: 'StockBoard.model.StockResponse',
    autoLoad: true,
    pageSize: 50,

    proxy: {
        type: 'ajax',
        url: 'StockData',
        waitMsg:'Loading..please wait..',
        extraParams: {
            sector: 1
        },
        reader: {
            type: 'json',
            totalProperty: 'totalCount',
            root: 'sectors',
            successProperty: 'success'
        },
     }


});
