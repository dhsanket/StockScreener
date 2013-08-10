Ext.define('StockBoard.store.StoreMinMax', {
    extend: 'Ext.data.Store',
    model: 'StockBoard.model.ModelMinMax',
    autoLoad: true,

    proxy: {
        type: 'ajax',
        url: 'StockExchange',
        waitMsg:'Loading..please wait..',
        extraParams: {
            stckexchange: 0
        },
        reader: {
            type: 'json',
           // totalProperty: 'totalCount',
            root: 'stockexchange',
            successProperty: 'success'
        },
     }


});
