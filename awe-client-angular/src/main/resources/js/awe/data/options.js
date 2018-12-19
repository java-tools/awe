import { aweApplication } from "./../awe";

// Application options
aweApplication.factory('Options',
  ['AweUtilities',
  function (utilities) {
    // Define actions
    var Options = {
      // Application default $settings
      settings: {
        values: {
          // Paths
          pathServer: "",
          initialURL: "",
          // Globals
          language: null,
          theme: "default",
          screen: "",
          charset: "UTF-8",
          applicationName: "AWE (Almis Web Engine)",
          serverActionKey: "serverAction",
          targetActionKey: "targetAction",
          screenKey: "screen",
          optionKey: "option",
          dataSuffix: ".data",
          homeScreen: "home",
          recordsPerPage: 30,
          pixelsPerCharacter: 7,
          defaultComponentSize: "md",
          shareSessionInTabs: false,
          reloadCurrentScreen: false,
          suggestTimeout: 200,
          // Connection
          connectionProtocol: "AJAX",
          connectionTransport: "websocket",
          connectionBackup: "streaming",
          connectionTimeout: 300000,
          connectionId: "s",
          cometUID: utilities.getUID(),
          // Upload / Download
          uploadIdentifier: 'u',
          uploadMaxSize: 500 * 1024 * 1024,
          downloadIdentifier: 'd',
          addressIdentifier: 'address',
          // Security
          passwordPattern: ".*",
          minlengthPassword: 4,
          encodeTransmission: false,
          encodeKey: "p",
          tokenKey: "t",
          // Debug
          actionsStack: 0,
          debug: "INFO",
          // Screen loading
          loadingTimeout: 20000,
          // Help
          helpTimeout: 1000,
          // Messages
          messageTimeout: {
            info: 2000,
            error: 0,
            validate: 2000,
            help: 4000,
            warning: 4000,
            ok: 2000,
            wrong: 0,
            chat: 0
          },
          // Numeric options
          numericOptions: {
            aSep: ",",
            dGroup: '3',
            aDec: ".",
            aSign: '',
            pSign: 's',
            vMin: "-9999999999.99",
            vMax: "9999999999.99",
            mDec: null,
            mRound: "S",
            aPad: false,
            wEmpty: 'empty'
          },
          // Pivot options
          pivotOptions: {
            numGroup: 5000
          },
          // Chart options
          chartOptions: {
            limitPointsSerie: 1000000
          }
        },
        definition: {
          // Paths
          pathServer: 'string',
          initialURL: 'string',
          // Globals
          language: 'string',
          theme: 'string',
          screen: 'string',
          charset: 'string',
          applicationName: 'string',
          serverActionKey: 'string',
          targetActionKey: 'string',
          screenKey: 'string',
          optionKey: 'string',
          homeScreen: 'string',
          recordsPerPage: 'int',
          pixelsPerCharacter: 'int',
          defaultComponentSize: 'string',
          dataSuffix: 'string',
          shareSessionInTabs: 'boolean',
          reloadCurrentScreen: 'boolean',
          suggestTimeout: 'int',
          // Connection
          connectionProtocol: 'string',
          connectionTransport: 'string',
          connectionBackup: 'string',
          connectionTimeout: 'int',
          connectionId: 'string',
          // Upload / Download
          uploadIdentifier: 'string',
          downloadIdentifier: 'string',
          addressIdentifier: 'string',
          // Security
          passwordPattern: 'string',
          minlengthPassword: 'int',
          encodeTransmission: 'boolean',
          encodeKey: 'string',
          tokenKey: 'string',
          // Debug
          actionsStack: 'int',
          debug: 'string',
          // Help
          helpTimeout: 'int',
          // Messages
          messageTimeout: {
            info: 'int',
            error: 'int',
            validate: 'int',
            help: 'int',
            warning: 'int',
            ok: 'int',
            wrong: 'int',
            chat: 'int'
          },
          // Numeric options
          numericOptions: {
            aSep: 'string',
            dGroup: 'string',
            aDec: 'string',
            aSign: 'string',
            pSign: 'string',
            vMin: 'string',
            vMax: 'string',
            mDec: 'string',
            mRound: 'string',
            aPad: 'boolean',
            wEmpty: 'string'
          },
          // Pivot options
          pivotOptions: {
            numGroup: 'int'
          },
          // Screen loading
          loadingTimeout: 'int',
          // Chart render options
          chartOptions: {
            limitPointsSerie: 'int'
          }
        }
      },
      // Spinner definitions
      spin: {
        big: {
          // The number of lines to draw
          lines: 13,
          // The length of each line
          length: 20,
          // The line thickness
          width: 8,
          // The radius of the inner circle
          radius: 30,
          // Corner roundness (0..1)
          corners: 1,
          // The rotation offset
          rotate: 0,
          // 1: clockwise, -1: counterclockwise
          direction: 1,
          // #rgb or #rrggbb or array of colors
          color: '#000',
          // Rounds per second
          speed: 1,
          // Afterglow percentage
          trail: 60,
          // Whether to render a shadow
          shadow: true,
          // Whether to use hardware acceleration
          hwaccel: true,
          // The CSS class to assign to the spinner
          className: 'spinner',
          // The z-index (defaults to 2000000000)
          zIndex: 1010,
          // Top position relative to parent
          top: '50%',
          // Left position relative to parent
          left: '50%'
        },
        medium: {
          // The number of lines to draw
          lines: 11,
          // The length of each line
          length: 5,
          // The line thickness
          width: 1,
          // The radius of the inner circle
          radius: 4,
          // Corner roundness (0..1)
          corners: 1,
          // The rotation offset
          rotate: 0,
          // 1: clockwise, -1: counterclockwise
          direction: 1,
          // #rgb or #rrggbb or array of colors
          color: '#666',
          // Rounds per second
          speed: 1,
          // Afterglow percentage
          trail: 80,
          // Whether to render a shadow
          shadow: false,
          // Whether to use hardware acceleration
          hwaccel: true,
          // The CSS class to assign to the spinner
          className: 'spinner',
          // The z-index (defaults to 2000000000)
          zIndex: 100,
          // Top position relative to parent
          top: '50%',
          // Left position relative to parent
          left: '50%'
        },
        small: {
          // The number of lines to draw
          lines: 9,
          // The length of each line
          length: 3,
          // The line thickness
          width: 1,
          // The radius of the inner circle
          radius: 2,
          // Corner roundness (0..1)
          corners: 1,
          // The rotation offset
          rotate: 0,
          // 1: clockwise, -1: counterclockwise
          direction: 1,
          // #rgb or #rrggbb or array of colors
          color: '#666',
          // Rounds per second
          speed: 1,
          // Afterglow percentage
          trail: 80,
          // Whether to render a shadow
          shadow: false,
          // Whether to use hardware acceleration
          hwaccel: true,
          // The CSS class to assign to the spinner
          className: 'spinner',
          // The z-index (defaults to 2000000000)
          zIndex: 100,
          // Top position relative to parent
          top: '9px',
          // Left position relative to parent
          left: '8px'
        }
      },
      // Default grid values
      grid: {
        /* Dynamic load url  */
        url: null,
        /* Edition URL  */
        editurl: "clientArray",
        /* Footing layer name  */
        pager: "",
        /* Report title  */
        caption: "",
        /* Show row numbers  */
        rownumbers: true,
        /* Loading data type  */
        datatype: "local",
        /* Array with column titles  */
        colNames: [],
        /*
         * Array que describe el modelo de las columnas. Contiene los siguientes datos:
         * - name: Es el nombre de la columna (No tiene por que ser el nombre de la columna en la base de datos).
         * - index: Es el nombre que se le pasa al servidor para ordenar la tabla. Es el nombre de la columna en la
         * base de datos.
         * - width: Es el ancho de la columna en pixeles.
         * - align: Es la alineacion de la columna.
         * - sortable: Indica si se puede realizar ordenacion por la columna en cuestion o no.
         * Ejemplo: colModel:[{name:'id',index:'id', width:75},{name:'note',index:'note', width:150, align:"right", sortable:false}]
         */
        colModel: [],
        /*
         * Group header model
         * Ejemplo: headerModel:[{startColumnName: 'amount', numberOfColumns: 3, titleText: '<em>Price</em>'},{startColumnName: 'closed', numberOfColumns: 2, titleText: 'Shiping'}]
         */
        headerModel: [],
        /* Requested page number  */
        page: 1,
        /* Total pages number  */
        lastpage: 1,
        /* Show pagination buttons  */
        pgbuttons: true,
        /* Show page number input  */
        pginput: true,
        /* Show alternate rows faded (pijama)  */
        altRows: false,
        /* Autowidth */
        autowidth: false,
        /* Table height  */
        height: "100%",
        /* Show total records  */
        viewrecords: true,
        /* Allow cell editing  */
        cellEdit: false,
        /* Stores operation type in each row if true and send them as rowTypeName defines */
        sendOperations: false,
        /* Stores operation type in each row if true and send them as rowTypeName defines */
        deepempty: false,
        /* Name to send operation type */
        rowTypeName: "RowTyp",
        /* Name to send operation icon */
        rowTypeIcon: "RowIco",
        /* Row actions list */
        rowActions: {
          INSERT: "INSERT",
          UPDATE: "UPDATE",
          DELETE: "DELETE",
          NONE: "NONE"
        },
        /* Selected row postname */
        rowPostName: "_sel",
        /*
         * Envio de datos de Celda. Si esta a "remote" se envia una llamada ajax para guardar los datos
         * modificados usando el atributo de jqGrid "cellurl", si esta como "clientArray" no se envia llamada
         * ajax y el contenido se puede obtener via el metodo de jqGrid "getChangedCells"
         */
        cellsubmit: "clientArray",
        /*
         * Control de la ui cuando se esta usando el ajax. Puede ser:
         * - disable: Deshabilita el indicador de progreso.
         * - enable (default): Habilita el mensaje de cargando.
         * - block: Habilita un barra de progreso en funcion de las css que se hayan indicado
         * para "div.loadingui". Deshabilita mientras tanto la paginacion, la ordenacion
         * y cualquier accion de la barra de herramientas si las hay.
         */
        loadui: "disable",
        /* Form submit method (POST|GET)  */
        mtype: "post",
        /* Allow row multiselect  */
        multiselect: false,
        /* Allow column multisort */
        multiSort: true,
        /* Selected row list  */
        selectedRowList: [],
        /* Elements per page  */
        rowNum: 0,
        /* Show calculated totals  */
        footerrow: false,
        /*
         * Personalizar el nombre de las variables que se envian via ajax. Los valores por defecto para estos campos
         * son "page", "rows", "sidx", y "sord". Por ejemplo: con el setting prmNames: {sort: "mysort"} cambiaremos
         * el nombre de la variable de ordenacion de "sidx" a "mysort". La cadena que se enviara al servidor sera:
         *
         * myurl.php?page=1&rows=10&mysort=myindex&sord=asc
         * en lugar de
         * myurl.php?page=1&rows=10&sidx=myindex&sord=asc
         */
        prmnames: {
          page: "page",
          rows: "rows",
          sort: "sort_id",
          order: "sort_direction",
          search: "search",
          nd: "nd"
        },
        /* Function on load finish  */
        onload: function () {
        },
        /* Array with row per page list [10,20,30]  */
        rowList: [],
        /* Stored widget list */
        storedWidgets: [],
        /* Show a button with search options  */
        showSearch: false,
        /* Shows if the table is visible or not */
        visible: true,
        /* Set last row style */
        totalize: "",
        /* Loading message */
        loading: null,
        /* Grid is being loaded */
        isLoading: false,
        /* Grid is doing a lookup */
        lookup: false,
        /* Lookup check interval */
        lookupInterval: 1000,
        /* Empty data */
        emptyData: {
          total: "0",
          page: "0",
          records: "0",
          rows: []
        },
        /* Autorefresh */
        autorefresh: 0,
        /* Sort */
        sort: [],
        /* Order */
        order: [],
        // Column reordering
        sortable: true,
        // Use fa icons
        fontAwesomeIcons: true,
        // Improve performance
        // This mode has some limitations - it is not possible to use
        // treeGrid, subGrid and afterInsertRow (event)
        gridview: true,
        // JSON format
        localReader: {
          root: "rows",
          page: "page",
          total: "total",
          records: "records",
          cell: "cell",
          id: "id"
        }
      }
    };
    return Options;
  }]
);