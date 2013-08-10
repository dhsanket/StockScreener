Ext.define('StockBoard.store.StoreHistory', {
    extend: 'Ext.data.Store',
    model: 'StockBoard.model.HistoryResponse',
    autoLoad: false,
   // pageSize: 10,
 
    proxy: {
        type: 'ajax',
        url: 'History',
       
        reader: {
            type: 'json',
            //totalProperty: 'totalCount',
            root: 'count',
            successProperty: 'success'
        },
     }
    
 
});