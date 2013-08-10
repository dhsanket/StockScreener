Ext.define('StockBoard.model.ModelMinMax', {
    extend: 'Ext.data.Model',

    fields: //['code','curdate','gainpercent','id','mktcap','name','pe','type','value1','value2','volume']
       [  { name: 'minpe', type: 'auto' },
          { name: 'maxpe', type: 'auto' },
          { name: 'minvolume', type: 'auto' },
          { name: 'maxvolume', type: 'auto' },
          { name: 'minmrktcap', type: 'auto' },
          { name: 'maxmrktcap', type: 'auto' },
          { name:'stockexchange', type: 'auto'}
    ]

});

