/*
 * Gijgo JavaScript Library v1.9.13
 * http://gijgo.com/
 *
 * Copyright 2014, 2019 gijgo.com
 * Released under the MIT license
 */
var gj = {};

gj.widget = function () {
    var self = this;

    self.xhr = null;

    self.generateGUID = function () {
        function s4() {
            return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
        }
        return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
    };

    self.mouseX = function (e) {
        if (e) {
            if (e.pageX) {
                return e.pageX;
            } else if (e.clientX) {
                return e.clientX + (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
            } else if (e.touches && e.touches.length) {
                return e.touches[0].pageX;
            } else if (e.changedTouches && e.changedTouches.length) {
                return e.changedTouches[0].pageX;
            } else if (e.originalEvent && e.originalEvent.touches && e.originalEvent.touches.length) {
                return e.originalEvent.touches[0].pageX;
            } else if (e.originalEvent && e.originalEvent.changedTouches && e.originalEvent.changedTouches.length) {
                return e.originalEvent.touches[0].pageX;
            }
        }
        return null;
    };

    self.mouseY = function (e) {
        if (e) {
            if (e.pageY) {
                return e.pageY;
            } else if (e.clientY) {
                return e.clientY + (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
            } else if (e.touches && e.touches.length) {
                return e.touches[0].pageY;
            } else if (e.changedTouches && e.changedTouches.length) {
                return e.changedTouches[0].pageY;
            } else if (e.originalEvent && e.originalEvent.touches && e.originalEvent.touches.length) {
                return e.originalEvent.touches[0].pageY;
            } else if (e.originalEvent && e.originalEvent.changedTouches && e.originalEvent.changedTouches.length) {
                return e.originalEvent.touches[0].pageY;
            }
        }
        return null;
    };
};

gj.widget.prototype.init = function (jsConfig, type) {
    var option, clientConfig, fullConfig;

    this.attr('data-type', type);
    clientConfig = $.extend(true, {}, this.getHTMLConfig() || {});
    $.extend(true, clientConfig, jsConfig || {});
    fullConfig = this.getConfig(clientConfig, type);
    this.attr('data-guid', fullConfig.guid);
    this.data(fullConfig);

    // Initialize events configured as options
    for (option in fullConfig) {
        if (gj[type].events.hasOwnProperty(option)) {
            this.on(option, fullConfig[option]);
            delete fullConfig[option];
        }
    }

    // Initialize all plugins
    for (plugin in gj[type].plugins) {
        if (gj[type].plugins.hasOwnProperty(plugin)) {
            gj[type].plugins[plugin].configure(this, fullConfig, clientConfig);
        }
    }

    return this;
};

gj.widget.prototype.getConfig = function (clientConfig, type) {
    var config, uiLibrary, iconsLibrary, plugin;

    config = $.extend(true, {}, gj[type].config.base);

    uiLibrary = clientConfig.hasOwnProperty('uiLibrary') ? clientConfig.uiLibrary : config.uiLibrary;
    if (gj[type].config[uiLibrary]) {
        $.extend(true, config, gj[type].config[uiLibrary]);
    }

    iconsLibrary = clientConfig.hasOwnProperty('iconsLibrary') ? clientConfig.iconsLibrary : config.iconsLibrary;
    if (gj[type].config[iconsLibrary]) {
        $.extend(true, config, gj[type].config[iconsLibrary]);
    }

    for (plugin in gj[type].plugins) {
        if (gj[type].plugins.hasOwnProperty(plugin)) {
            $.extend(true, config, gj[type].plugins[plugin].config.base);
            if (gj[type].plugins[plugin].config[uiLibrary]) {
                $.extend(true, config, gj[type].plugins[plugin].config[uiLibrary]);
            }
            if (gj[type].plugins[plugin].config[iconsLibrary]) {
                $.extend(true, config, gj[type].plugins[plugin].config[iconsLibrary]);
            }
        }
    }

    $.extend(true, config, clientConfig);

    if (!config.guid) {
        config.guid = this.generateGUID();
    }

    return config;
}

gj.widget.prototype.getHTMLConfig = function () {
    var result = this.data(),
        attrs = this[0].attributes;
    if (attrs['width']) {
        result.width = attrs['width'].value;
    }
    if (attrs['height']) {
        result.height = attrs['height'].value;
    }
    if (attrs['value']) {
        result.value = attrs['value'].value;
    }
    if (attrs['align']) {
        result.align = attrs['align'].value;
    }
    if (result && result.source) {
        result.dataSource = result.source;
        delete result.source;
    }
    return result;
};

gj.widget.prototype.createDoneHandler = function () {
    var $widget = this;
    return function (response) {
        if (typeof (response) === 'string' && JSON) {
            response = JSON.parse(response);
        }
        gj[$widget.data('type')].methods.render($widget, response);
    };
};

gj.widget.prototype.createErrorHandler = function () {
    var $widget = this;
    return function (response) {
        if (response && response.statusText && response.statusText !== 'abort') {
            alert(response.statusText);
        }
    };
};

gj.widget.prototype.reload = function (params) {
    var ajaxOptions, result, data = this.data(), type = this.data('type');
    if (data.dataSource === undefined) {
        gj[type].methods.useHtmlDataSource(this, data);
    }
    $.extend(data.params, params);
    if ($.isArray(data.dataSource)) {
        result = gj[type].methods.filter(this);
        gj[type].methods.render(this, result);
    } else if (typeof(data.dataSource) === 'string') {
        ajaxOptions = { url: data.dataSource, data: data.params };
        if (this.xhr) {
            this.xhr.abort();
        }
        this.xhr = $.ajax(ajaxOptions).done(this.createDoneHandler()).fail(this.createErrorHandler());
    } else if (typeof (data.dataSource) === 'object') {
        if (!data.dataSource.data) {
            data.dataSource.data = {};
        }
        $.extend(data.dataSource.data, data.params);
        ajaxOptions = $.extend(true, {}, data.dataSource); //clone dataSource object
        if (ajaxOptions.dataType === 'json' && typeof(ajaxOptions.data) === 'object') {
            ajaxOptions.data = JSON.stringify(ajaxOptions.data);
        }
        if (!ajaxOptions.success) {
            ajaxOptions.success = this.createDoneHandler();
        }
        if (!ajaxOptions.error) {
            ajaxOptions.error = this.createErrorHandler();
        }
        if (this.xhr) {
            this.xhr.abort();
        }
        this.xhr = $.ajax(ajaxOptions);
    }
    return this;
}

gj.documentManager = {
    events: {},

    subscribeForEvent: function (eventName, widgetId, callback) {
        if (!gj.documentManager.events[eventName] || gj.documentManager.events[eventName].length === 0) {
            gj.documentManager.events[eventName] = [{ widgetId: widgetId, callback: callback }];
            $(document).on(eventName, gj.documentManager.executeCallbacks);
        } else if (!gj.documentManager.events[eventName][widgetId]) {
            gj.documentManager.events[eventName].push({ widgetId: widgetId, callback: callback });
        } else {
            throw 'Event ' + eventName + ' for widget with guid="' + widgetId + '" is already attached.';
        }
    },

    executeCallbacks: function (e) {
        var callbacks = gj.documentManager.events[e.type];
        if (callbacks) {
            for (var i = 0; i < callbacks.length; i++) {
                callbacks[i].callback(e);
            }
        }
    },

    unsubscribeForEvent: function (eventName, widgetId) {
        var success = false,
            events = gj.documentManager.events[eventName];
        if (events) {
            for (var i = 0; i < events.length; i++) {
                if (events[i].widgetId === widgetId) {
                    events.splice(i, 1);
                    success = true;
                    if (events.length === 0) {
                        $(document).off(eventName);
                        delete gj.documentManager.events[eventName];
                    }
                }
            }
        }
        if (!success) {
            throw 'The "' + eventName + '" for widget with guid="' + widgetId + '" can\'t be removed.';
        }
    }
};

/**
 * @widget Tree
 * @plugin Base
 */
gj.tree = {
   plugins: {}
};

gj.tree.config = {
   base: {

       params: {},

       /** When this setting is enabled the content of the tree will be loaded automatically after the creation of the tree.
        * @type boolean
        * @default true
        * @example disabled <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         autoLoad: false
        *     });
        *     tree.reload(); //call .reload() explicitly in order to load the data in the tree
        * </script>
        * @example enabled <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         autoLoad: true
        *     });
        * </script>
        */
       autoLoad: true,

       /** The type of the node selection.<br/>
        * If the type is set to multiple the user will be able to select more then one node in the tree.
        * @type (single|multiple)
        * @default single
        * @example Single.Selection <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         selectionType: 'single'
        *     });
        * </script>
        * @example Multiple.Selection <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         selectionType: 'multiple'
        *     });
        * </script>
        */
       selectionType: 'single',

       /** This setting enable cascade selection and unselection of children
        * @type boolean
        * @default false
        * @example Sample <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         cascadeSelection: true
        *     });
        * </script>
        */
       cascadeSelection: false,

       /** The data source of tree.
        * @additionalinfo If set to string, then the tree is going to use this string as a url for ajax requests to the server.<br />
        * If set to object, then the tree is going to use this object as settings for the <a href="http://api.jquery.com/jquery.ajax/" target="_new">jquery ajax</a> function.<br />
        * If set to array, then the tree is going to use the array as data for tree nodes.
        * @type (string|object|array)
        * @default undefined
        * @example Local.DataSource <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: [ { text: 'foo', children: [ { text: 'bar' } ] } ]
        *     });
        * </script>
        * @example Remote.DataSource <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get'
        *     });
        * </script>
        */
       dataSource: undefined,

       /** Primary key field name.
        * @type string
        * @default undefined
        * @example defined <!-- tree -->
        * <p>Select a node to see the key.</p>
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         primaryKey: 'id',
        *         dataSource: [ { id: 101, text: 'foo', children: [ { id: 202, text: 'bar' } ] } ],
        *         select: function (e, node, id) {
        *             alert('Your key is ' + id);
        *         }
        *     });
        * </script>
        * @example undefined <!-- tree -->
        * <p>Select a node to see the key.</p>
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: [ { id: 101, text: 'foo', children: [ { id: 202, text: 'bar' } ] } ],
        *         select: function (e, node, id) {
        *             alert('Your key is ' + id);
        *         }
        *     });
        * </script>
        */
       primaryKey: undefined,

       /** Text field name.
        * @type string
        * @default 'text'
        * @example sample <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         textField: 'newTextName',
        *         dataSource: [ { newTextName: 'foo', children: [ { newTextName: 'bar' } ] } ]
        *     });
        * </script>
        */
       textField: 'text',

       /** Children field name.
        * @type string
        * @default 'children'
        * @example Custom.FieldName <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         childrenField: 'myChildrenNode',
        *         dataSource: [ { text: 'foo', myChildrenNode: [ { text: 'bar' } ] } ]
        *     });
        * </script>
        */
       childrenField: 'children',

       /** The name of the field that indicates if the node has children. Shows expand icon if the node has children.
        * @type string
        * @default 'hasChildren'
        * @example Custom.FieldName <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     var continents, countries, states, tree;
        *     continents = [
        *         { id: 1, anyChildren: true, text: 'Asia', type: 'continent' },
        *         { id: 2, anyChildren: true, text: 'North America', type: 'continent' },
        *         { id: 3, anyChildren: false, text: 'South America', type: 'continent' }
        *     ];
        *     countries = [
        *         { id: 1, anyChildren: false, continent: 'Asia', text: 'China', type: 'country' },
        *         { id: 2, anyChildren: false, continent: 'Asia', text: 'Japan', type: 'country' },
        *         { id: 3, anyChildren: true, continent: 'North America', text: 'USA', type: 'country' },
        *         { id: 4, anyChildren: false, continent: 'North America', text: 'Canada', type: 'country' }
        *     ];
        *     states = [
        *         { id: 1, country: 'USA', text: 'California', type: 'state' },
        *         { id: 2, country: 'USA', text: 'Florida', type: 'state' }
        *     ];
        *     tree = $('#tree').tree({
        *         hasChildrenField: 'anyChildren',
        *         dataSource: continents
        *     });
        *     tree.on('expand', function (e, $node, id) {
        *         var i, children, record = tree.getDataById(id);
        *         if (tree.getChildren($node).length === 0) {
        *             if (record.type === 'continent') {
        *                 children = $.grep(countries, function (i) { return i.continent === record.text; });
        *                 for (i = 0; i < children.length; i++) {
        *                     tree.addNode(children[i], $node);
        *                 }
        *             } else if (record.type === 'country') {
        *                 children = $.grep(states, function (i) { return i.country === record.text; });
        *                 for (i = 0; i < children.length; i++) {
        *                     tree.addNode(children[i], $node);
        *                 }
        *             }
        *         }
        *     });
        * </script>
        */
       hasChildrenField: 'hasChildren',

       /** Image css class field name.
        * @type string
        * @default 'imageCssClass'
        * @example Default.Name <!-- bootstrap, tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         uiLibrary: 'bootstrap',
        *         dataSource: [ { text: 'folder', imageCssClass: 'glyphicon glyphicon-folder-close', children: [ { text: 'file', imageCssClass: 'glyphicon glyphicon-file' } ] } ]
        *     });
        * </script>
        * @example Custom.Name <!-- tree  -->
        * <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         imageCssClassField: 'faCssClass',
        *         dataSource: [ { text: 'folder', faCssClass: 'fa fa-folder', children: [ { text: 'file', faCssClass: 'fa fa-file' } ] } ]
        *     });
        * </script>
        */
       imageCssClassField: 'imageCssClass',

       /** Image url field name.
        * @type string
        * @default 'imageUrl'
        * @example Default.HTML.Field.Name <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: [ { text: 'World', imageUrl: 'http://gijgo.com/content/icons/world-icon.png', children: [ { text: 'USA', imageUrl: 'http://gijgo.com/content/icons/usa-oval-icon.png' } ] } ]
        *     });
        * </script>
        * @example Custom.HTML.Field.Name <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         imageUrlField: 'icon',
        *         dataSource: [ { text: 'World', icon: 'http://gijgo.com/content/icons/world-icon.png', children: [ { text: 'USA', icon: 'http://gijgo.com/content/icons/usa-oval-icon.png' } ] } ]
        *     });
        * </script>
        */
       imageUrlField: 'imageUrl',

       /** Image html field name.
        * @type string
        * @default 'imageHtml'
        * @example Default.HTML.Field.Name <!-- materialicons, tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: [ { text: 'folder', imageHtml: '<i class="material-icons">folder</i>', children: [ { text: 'file', imageHtml: '<i class="material-icons">insert_drive_file</i>' } ] } ]
        *     });
        * </script>
        * @example Custom.HTML.Field.Name <!-- materialicons, tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         imageHtmlField: 'icon',
        *         dataSource: [ { text: 'folder', icon: '<i class="material-icons">folder</i>', children: [ { text: 'file', icon: '<i class="material-icons">insert_drive_file</i>' } ] } ]
        *     });
        * </script>
        */
       imageHtmlField: 'imageHtml',

       /** Disabled field name. Assume that the item is not disabled if not set.
        * @type string
        * @default 'disabled'
        * @example Default.Value <!-- checkbox, tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         checkboxes: true,
        *         dataSource: [ { text: 'foo', children: [
        *                 { text: 'bar', disabled: true, children: [ { text: 'sub-bar' } ] },
        *                 { text: 'bar2', disabled: false }
        *             ] }
        *         ]
        *     });
        * </script>
        * @example Custom.Value <!-- checkbox, tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         checkboxes: true,
        *         disabledField: 'disabledState',
        *         dataSource: [ { text: 'foo', children: [
        *                 { text: 'bar', disabledState: true, children: [ { text: 'sub-bar' } ] },
        *                 { text: 'bar2', disabledState: false }
        *             ] }
        *         ]
        *     });
        * </script>
        * @example Bootstrap <!-- bootstrap, checkbox, tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         uiLibrary: 'bootstrap',
        *         checkboxes: true,
        *         dataSource: [ { text: 'foo', children: [
        *                 { text: 'bar', disabled: true, children: [ { text: 'sub-bar' } ] },
        *                 { text: 'bar2', disabled: false }
        *             ] }
        *         ]
        *     });
        * </script>
        * @example Bootstrap.4 <!-- bootstrap4, checkbox, tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         uiLibrary: 'bootstrap4',
        *         checkboxes: true,
        *         dataSource: [ { text: 'foo', children: [
        *                 { text: 'bar', disabled: true, children: [ { text: 'sub-bar' } ] },
        *                 { text: 'bar2', disabled: false }
        *             ] }
        *         ]
        *     });
        * </script>
        */
       disabledField: 'disabled',

       /** Width of the tree.
        * @type number
        * @default undefined
        * @example JS.Config <!-- bootstrap, tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'bootstrap',
        *         border: true
        *     });
        * </script>
        * @example HTML.Config <!-- bootstrap, tree -->
        * <div id="tree" width="500" data-source="/Locations/Get" data-ui-library="bootstrap" data-border="true"></div>
        * <script>
        *     $('#tree').tree();
        * </script>
        */
       width: undefined,

       /** When this setting is enabled the content of the tree will be wrapped by borders.
        * @type boolean
        * @default false
        * @example Material.Design.True <!-- checkbox, tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         border: true,
        *         checkboxes: true
        *     });
        * </script>
        * @example Material.Design.False <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         border: false
        *     });
        * </script>
        * @example Bootstrap.3.True <!-- bootstrap, tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'bootstrap',
        *         border: true
        *     });
        * </script>
        * @example Bootstrap.3.False <!-- bootstrap, tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'bootstrap',
        *         border: false
        *     });
        * </script>
        * @example Bootstrap.4.True <!-- bootstrap4, tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'bootstrap4',
        *         border: true
        *     });
        * </script>
        * @example Bootstrap.4.False <!-- bootstrap4, tree -->
        * <div id="tree"></div>
        * <script>
        *     $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'bootstrap4',
        *         border: false
        *     });
        * </script>
        */
       border: false,

       /** The name of the UI library that is going to be in use.
        * @additionalinfo The css file for bootstrap should be manually included if you use bootstrap.
        * @type (materialdesign|bootstrap|bootstrap4)
        * @default materialdesign
        * @example MaterialDesign <!-- tree, checkbox -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'materialdesign',
        *         checkboxes: true
        *     });
        * </script>
        * @example Bootstrap.3 <!-- bootstrap, tree, checkbox -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'bootstrap',
        *         checkboxes: true
        *     });
        * </script>
        * @example Bootstrap.4 <!-- bootstrap4, tree, checkbox -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'bootstrap4',
        *         checkboxes: true
        *     });
        * </script>
        */
       uiLibrary: 'materialdesign',

       /** The name of the icons library that is going to be in use. Currently we support Material Icons, Font Awesome and Glyphicons.
        * @additionalinfo If you use Bootstrap 3 as uiLibrary, then the iconsLibrary is set to Glyphicons by default.<br/>
        * If you use Material Design as uiLibrary, then the iconsLibrary is set to Material Icons by default.<br/>
        * The css files for Material Icons, Font Awesome or Glyphicons should be manually included to the page where the grid is in use.
        * @type (materialicons|fontawesome|glyphicons)
        * @default 'materialicons'
        * @example Base.Theme.Material.Icons <!-- tree -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         iconsLibrary: 'materialicons'
        *     });
        * </script>
        * @example Bootstrap.4.Font.Awesome <!-- bootstrap4, fontawesome, tree, checkbox -->
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         width: 500,
        *         uiLibrary: 'bootstrap4',
        *         iconsLibrary: 'fontawesome',
        *         checkboxes: true
        *     });
        * </script>
        */
       iconsLibrary: 'materialicons',

       autoGenId: 1,

       autoGenFieldName: 'autoId_b5497cc5-7ef3-49f5-a7dc-4a932e1aee4a',

       indentation: 24,

       style: {
           wrapper: 'gj-unselectable',
           list: 'gj-list gj-list-md',
           item: undefined,
           active: 'gj-list-md-active',
           leafIcon: undefined,
           border: 'gj-tree-md-border'
       },

       icons: {
           /** Expand icon definition.
            * @alias icons.expand
            * @type String
            * @default '<i class="gj-icon chevron-right" />'
            * @example Plus.Minus.Icons <!-- materialicons, tree -->
            * <div id="tree"></div>
            * <script>
            *     var tree = $('#tree').tree({
            *         dataSource: '/Locations/Get',
            *         icons: { 
            *             expand: '<i class="material-icons">add</i>',
            *             collapse: '<i class="material-icons">remove</i>'
            *         }
            *     });
            * </script>
            */
           expand: '<i class="gj-icon chevron-right" />',

           /** Collapse icon definition.
            * @alias icons.collapse
            * @type String
            * @default '<i class="gj-icon chevron-down" />'
            * @example Plus.Minus.Icons <!-- materialicons, tree -->
            * <div id="tree"></div>
            * <script>
            *     var tree = $('#tree').tree({
            *         dataSource: '/Locations/Get',
            *         icons: { 
            *             expand: '<i class="material-icons">add</i>',
            *             collapse: '<i class="material-icons">remove</i>'
            *         }
            *     });
            * </script>
            */
           collapse: '<i class="gj-icon chevron-down" />'
       }
   },

   bootstrap: {
       style: {
           wrapper: 'gj-unselectable gj-tree-bootstrap-3',
           list: 'gj-list gj-list-bootstrap list-group',
           item: 'list-group-item',
           active: 'active',
           border: 'gj-tree-bootstrap-border'
       },
       iconsLibrary: 'glyphicons'
   },

   bootstrap4: {
       style: {
           wrapper: 'gj-unselectable gj-tree-bootstrap-4',
           list: 'gj-list gj-list-bootstrap',
           item: 'list-group-item',
           active: 'active',
           border: 'gj-tree-bootstrap-border'
       },
       icons: {
           expand: '<i class="gj-icon plus" />',
           collapse: '<i class="gj-icon minus" />'
       }
   },

   materialicons: {
       style: {
           expander: 'gj-tree-material-icons-expander'
       }
   },

   fontawesome: {
       style: {
           expander: 'gj-tree-font-awesome-expander'
       },
       icons: {
           expand: '<i class="fa fa-plus" aria-hidden="true"></i>',
           collapse: '<i class="fa fa-minus" aria-hidden="true"></i>'
       }
   },

   glyphicons: {
       style: {
           expander: 'gj-tree-glyphicons-expander'
       },
       icons: {
           expand: '<span class="glyphicon glyphicon-plus" />',
           collapse: '<span class="glyphicon glyphicon-minus" />'
       }
   }
};
/**
 * @widget Tree
 * @plugin Base
 */
gj.tree.events = {

   /**
    * Event fires when the tree is initialized
    * @event initialized
    * @param {object} e - event data
    * @example Event.Sample <!-- tree -->
    * <button id="reload" class="gj-button-md">Reload</button>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         initialized: function (e) {
    *             alert('initialized is fired.');
    *         }
    *     });
    *     $('#reload').on('click', function() { 
    *         tree.reload(); 
    *     });
    * </script>
    */
   initialized: function ($tree) {
       $tree.triggerHandler('initialized');
   },

   /**
    * Event fired before data binding takes place.
    * @event dataBinding
    * @param {object} e - event data
    * @example Event.Sample <!-- tree -->
    * <div id="tree"></div>
    * <script>
    *     $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         dataBinding: function (e) {
    *             alert('dataBinding is fired.');
    *         }
    *     });
    * </script>
    */
   dataBinding: function ($tree) {
       $tree.triggerHandler('dataBinding');
   },

   /**
    * Event fires after the loading of the data in the tree.
    * @event dataBound
    * @param {object} e - event data
    * @example Event.Sample <!-- tree -->
    * <div id="tree"></div>
    * <script>
    *     $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         dataBound: function (e) {
    *             alert('dataBound is fired.');
    *         }
    *     });
    * </script>
    */
   dataBound: function ($tree) {
       $tree.triggerHandler('dataBound');
   },

   /**
    * Event fires after selection of tree node.
    * @event select
    * @param {object} e - event data
    * @param {object} node - the node as jquery object
    * @param {string} id - the id of the record
    * @example Event.Sample <!-- tree -->
    * <p>Select tree node in order to fire the event.</p>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    *     tree.on('select', function (e, node, id) {
    *         alert('select is fired for node with id=' + id);
    *     });
    * </script>
    */
   select: function ($tree, $node, id) {
       return $tree.triggerHandler('select', [$node, id]);
   },

   /**
    * Event fires on un selection of tree node
    * @event unselect
    * @param {object} e - event data
    * @param {object} node - the node as jquery object
    * @param {string} id - the id of the record
    * @example Event.Sample <!-- tree -->
    * <p>Select/Unselect tree node in order to fire the event.</p>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    *     tree.on('unselect', function (e, node, id) {
    *         alert('unselect is fired for node with id=' + id);
    *     });
    * </script>
    */
   unselect: function ($tree, $node, id) {
       return $tree.triggerHandler('unselect', [$node, id]);
   },

   /**
    * Event fires before node expand.
    * @event expand
    * @param {object} e - event data
    * @param {object} node - the node as jquery object
    * @param {string} id - the id of the record
    * @example Event.Sample <!-- tree -->
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    *     tree.on('expand', function (e, node, id) {
    *         alert('expand is fired.');
    *     });
    * </script>
    */
   expand: function ($tree, $node, id) {
       return $tree.triggerHandler('expand', [$node, id]);
   },

   /**
    * Event fires before node collapse.
    * @event collapse
    * @param {object} e - event data
    * @param {object} node - the node as jquery object
    * @param {string} id - the id of the record
    * @example Event.Sample <!-- tree -->
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    *     tree.on('collapse', function (e, node, id) {
    *         alert('collapse is fired.');
    *     });
    * </script>
    */
   collapse: function ($tree, $node, id) {
       return $tree.triggerHandler('collapse', [$node, id]);
   },

   /**
    * Event fires on enable of tree node.
    * @event enable
    * @param {object} e - event data
    * @param {object} node - the node as jquery object
    * @example Event.Sample <!-- tree -->
    * <button onclick="tree.enable(northAmerica, false)" class="gj-button-md">Enable North America</button>
    * <button onclick="tree.disable(northAmerica, false)" class="gj-button-md">Disable North America</button>
    * <br/><br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree, northAmerica;
    *     tree = $('#tree').tree({
    *         primaryKey: 'ID',
    *         dataBound: function () {
    *             northAmerica = tree.getNodeByText('North America');
    *         }
    *     });
    *     tree.on('enable', function (e, node) {
    *         alert(node.text() + ' is enabled.');
    *     });
    * </script>
    */
   enable: function ($tree, $node) {
       return $tree.triggerHandler('enable', [$node]);
   },

   /**
    * Event fires on disable of tree node.
    * @event disable
    * @param {object} e - event data
    * @param {object} node - the node as jquery object
    * @example Event.Sample <!-- tree -->
    * <button onclick="tree.enable(northAmerica, false)" class="gj-button-md">Enable North America</button>
    * <button onclick="tree.disable(northAmerica, false)" class="gj-button-md">Disable North America</button>
    * <br/><br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree, northAmerica;
    *     tree = $('#tree').tree({
    *         primaryKey: 'ID',
    *         dataBound: function () {
    *             northAmerica = tree.getNodeByText('North America');
    *         }
    *     });
    *     tree.on('disable', function (e, node) {
    *         alert(node.text() + ' is disabled.');
    *     });
    * </script>
    */
   disable: function ($tree, $node) {
       return $tree.triggerHandler('disable', [$node]);
   },

   /**
    * Event fires before tree destroy
    * @event destroying
    * @param {object} e - event data
    * @example Event.Sample <!-- tree -->
    * <button onclick="tree.destroy()" class="gj-button-md">Destroy</button>
    * <br/><br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    *     tree.on('destroying', function (e) {
    *         alert('destroying is fired.');
    *     });
    * </script>
    */
   destroying: function ($tree) {
       return $tree.triggerHandler('destroying');
   },

   /**
    * Event fires when the data is bound to node.
    * @event nodeDataBound
    * @param {object} e - event data
    * @param {object} node - the node as jquery object
    * @param {string} id - the id of the record
    * @param {object} record - the data of the node record
    * @example Event.Sample <!-- tree -->
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    *     tree.on('nodeDataBound', function (e, node, id, record) {
    *         if ((parseInt(id, 10) % 2) === 0) {
    *             node.css('background-color', 'red');
    *         }
    *     });
    * </script>
    */
   nodeDataBound: function ($tree, $node, id, record) {
       return $tree.triggerHandler('nodeDataBound', [$node, id, record]);
   }
}
/*global gj $*/
gj.tree.methods = {

   init: function (jsConfig) {
       gj.widget.prototype.init.call(this, jsConfig, 'tree');

       gj.tree.methods.initialize.call(this);

       if (this.data('autoLoad')) {
           this.reload();
       }
       return this;
   },

   initialize: function () {
       var data = this.data(),
           $root = $('<ul class="' + data.style.list + '"/>');
       this.empty().addClass(data.style.wrapper).append($root);
       if (data.width) {
           this.width(data.width);
       }
       if (data.border) {
           this.addClass(data.style.border);
       }
       gj.tree.events.initialized(this);
   },

   useHtmlDataSource: function ($tree, data) {
       data.dataSource = [];
   },

   render: function ($tree, response) {
       var data;
       if (response) {
           if (typeof (response) === 'string' && JSON) {
               response = JSON.parse(response);
           }
           data = $tree.data();
           data.records = response;
           if (!data.primaryKey) {
               gj.tree.methods.genAutoId(data, data.records);
           }
           gj.tree.methods.loadData($tree);
       }
       return $tree;
   },

   filter: function ($tree) {
       return $tree.data().dataSource;
   },

   genAutoId: function (data, records) {
       var i;
       for (i = 0; i < records.length; i++) {
           records[i][data.autoGenFieldName] = data.autoGenId++;
           if (records[i][data.childrenField] && records[i][data.childrenField].length) {
               gj.tree.methods.genAutoId(data, records[i][data.childrenField]);
           }
       }
   },

   loadData: function ($tree) {
       var i,
           records = $tree.data('records'),
           $root = $tree.children('ul');

       gj.tree.events.dataBinding($tree);
       $root.off().empty();
       for (i = 0; i < records.length; i++) {
           gj.tree.methods.appendNode($tree, $root, records[i], 1);
       }
       gj.tree.events.dataBound($tree);
   },

   appendNode: function ($tree, $parent, nodeData, level, position) {
       var i, $node, $newParent, $span, $img,
           data = $tree.data(),
           id = data.primaryKey ? nodeData[data.primaryKey] : nodeData[data.autoGenFieldName];
           $node = $('<li data-id="' + id + '" data-role="node" />').addClass(data.style.item),
           $wrapper = $('<div data-role="wrapper" />'),
           $expander = $('<span data-role="expander" data-mode="close"></span>').addClass(data.style.expander),
           $display = $('<span data-role="display">' + nodeData[data.textField] + '</span>'),
           hasChildren = typeof (nodeData[data.hasChildrenField]) !== 'undefined' && nodeData[data.hasChildrenField].toString().toLowerCase() === 'true',
           disabled = typeof (nodeData[data.disabledField]) !== 'undefined' && nodeData[data.disabledField].toString().toLowerCase() === 'true';

       if (data.indentation) {
           $wrapper.append('<span data-role="spacer" style="width: ' + (data.indentation * (level - 1)) + 'px;"></span>');
       }

       if (disabled) {
           gj.tree.methods.disableNode($tree, $node);
       } else {
           $expander.on('click', gj.tree.methods.expanderClickHandler($tree));
           $display.on('click', gj.tree.methods.displayClickHandler($tree));
       }
       $wrapper.append($expander);
       $wrapper.append($display);
       $node.append($wrapper);

       if (position) {
           $parent.find('li:eq(' + (position - 1) + ')').before($node);
       } else {
           $parent.append($node);
       }

       if (data.imageCssClassField && nodeData[data.imageCssClassField]) {
           $span = $('<span data-role="image"><span class="' + nodeData[data.imageCssClassField] + '"></span></span>');
           $span.insertBefore($display);
       } else if (data.imageUrlField && nodeData[data.imageUrlField]) {
           $span = $('<span data-role="image"></span>');
           $span.insertBefore($display);
           $img = $('<img src="' + nodeData[data.imageUrlField] + '"></img>');
           $img.attr('width', $span.width()).attr('height', $span.height());
           $span.append($img);
       } else if (data.imageHtmlField && nodeData[data.imageHtmlField]) {
           $span = $('<span data-role="image">' + nodeData[data.imageHtmlField] + '</span>');
           $span.insertBefore($display);
       }

       if ((nodeData[data.childrenField] && nodeData[data.childrenField].length) || hasChildren) {
           $expander.empty().append(data.icons.expand);
           $newParent = $('<ul />').addClass(data.style.list).addClass('gj-hidden');
           $node.append($newParent);

           if (nodeData[data.childrenField] && nodeData[data.childrenField].length) {
               for (i = 0; i < nodeData[data.childrenField].length; i++) {
                   gj.tree.methods.appendNode($tree, $newParent, nodeData[data.childrenField][i], level + 1);
               }
           }
       } else {
           data.style.leafIcon ? $expander.addClass(data.style.leafIcon) : $expander.html('&nbsp;');
       }

       gj.tree.events.nodeDataBound($tree, $node, nodeData.id, nodeData);
   },

   expanderClickHandler: function ($tree) {
       return function (e) {
           var $expander = $(this),
               $node = $expander.closest('li');
           if ($expander.attr('data-mode') === 'close') {
               $tree.expand($node);
           } else {
               $tree.collapse($node);
           }
       }
   },

   expand: function ($tree, $node, cascade) {
       var $children, i,
           $expander = $node.find('>[data-role="wrapper"]>[data-role="expander"]'),
           data = $tree.data(),
           id = $node.attr('data-id'),
           $list = $node.children('ul');
       if (gj.tree.events.expand($tree, $node, id) !== false && $list && $list.length) {
           $list.show();
           $expander.attr('data-mode', 'open');
           $expander.empty().append(data.icons.collapse);
           if (cascade) {
               $children = $node.find('ul>li');
               for (i = 0; i < $children.length; i++) {
                   gj.tree.methods.expand($tree, $($children[i]), cascade);
               }
           }
       }
       return $tree;
   },

   collapse: function ($tree, $node, cascade) {
       var $children, i,
           $expander = $node.find('>[data-role="wrapper"]>[data-role="expander"]'),
           data = $tree.data(),
           id = $node.attr('data-id'),
           $list = $node.children('ul');
       if (gj.tree.events.collapse($tree, $node, id) !== false && $list && $list.length) {
           $list.hide();
           $expander.attr('data-mode', 'close');
           $expander.empty().append(data.icons.expand);
           if (cascade) {
               $children = $node.find('ul>li');
               for (i = 0; i < $children.length; i++) {
                   gj.tree.methods.collapse($tree, $($children[i]), cascade);
               }
           }
       }
       return $tree;
   },

   expandAll: function ($tree) {
       var i, $nodes = $tree.find('ul>li');
       for (i = 0; i < $nodes.length; i++) {
           gj.tree.methods.expand($tree, $($nodes[i]), true);
       }
       return $tree;
   },

   collapseAll: function ($tree) {
       var i, $nodes = $tree.find('ul>li');
       for (i = 0; i < $nodes.length; i++) {
           gj.tree.methods.collapse($tree, $($nodes[i]), true);
       }
       return $tree;
   },

   displayClickHandler: function ($tree) {
       return function (e) {
           var $display = $(this),
               $node = $display.closest('li'),
               cascade = $tree.data().cascadeSelection;
           if ($node.attr('data-selected') === 'true') {
               gj.tree.methods.unselect($tree, $node, cascade);
           } else {
               if ($tree.data('selectionType') === 'single') {
                   gj.tree.methods.unselectAll($tree);
               }
               gj.tree.methods.select($tree, $node, cascade);
           }
       }
   },

   selectAll: function ($tree) {
       var i, $nodes = $tree.find('ul>li');
       for (i = 0; i < $nodes.length; i++) {
           gj.tree.methods.select($tree, $($nodes[i]), true);
       }
       return $tree;
   },

   select: function ($tree, $node, cascade) {
       var i, $children, data = $tree.data();
       if ($node.attr('data-selected') !== 'true' && gj.tree.events.select($tree, $node, $node.attr('data-id')) !== false) {
           $node.addClass(data.style.active).attr('data-selected', 'true');
           if (cascade) {
               $children = $node.find('ul>li');
               for (i = 0; i < $children.length; i++) {
                   gj.tree.methods.select($tree, $($children[i]), cascade);
               }
           }
       }
   },
   
   unselectAll: function ($tree) {
       var i, $nodes = $tree.find('ul>li');
       for (i = 0; i < $nodes.length; i++) {
           gj.tree.methods.unselect($tree, $($nodes[i]), true);
       }
       return $tree;
   },

   unselect: function ($tree, $node, cascade) {
       var i, $children, data = $tree.data();
       if ($node.attr('data-selected') === 'true' && gj.tree.events.unselect($tree, $node, $node.attr('data-id')) !== false) {
           $node.removeClass($tree.data().style.active).removeAttr('data-selected');
           if (cascade) {
               $children = $node.find('ul>li');
               for (i = 0; i < $children.length; i++) {
                   gj.tree.methods.unselect($tree, $($children[i]), cascade);
               }
           }
       }
   },

   getSelections: function ($list) {
       var i, $node, children,
           result = [],
           $nodes = $list.children('li');
       if ($nodes && $nodes.length) {
           for (i = 0; i < $nodes.length; i++) {
               $node = $($nodes[i]);
               if ($node.attr('data-selected') === 'true') {
                   result.push($node.attr('data-id'));
               } else if ($node.has('ul')) {
                   children = gj.tree.methods.getSelections($node.children('ul'));
                   if (children.length) {
                       result = result.concat(children);
                   }
               }
           }
       }

       return result;
   },

   getDataById: function ($tree, id, records) {
       var i, data = $tree.data(), result = undefined;
       for (i = 0; i < records.length; i++) {
           if (data.primaryKey && records[i][data.primaryKey] == id) {
               result = records[i];
               break;
           } else if (records[i][data.autoGenFieldName] == id) {
               result = records[i];
               break;
           } else if (records[i][data.childrenField] && records[i][data.childrenField].length) {
               result = gj.tree.methods.getDataById($tree, id, records[i][data.childrenField]);
               if (result) {
                   break;
               }
           }
       }
       return result;
   },

   getDataByText: function ($tree, text, records) {
       var i, id,
           result = undefined,
           data = $tree.data();
       for (i = 0; i < records.length; i++) {
           if (text === records[i][data.textField]) {
               result = records[i];
               break;
           } else if (records[i][data.childrenField] && records[i][data.childrenField].length) {
               result = gj.tree.methods.getDataByText($tree, text, records[i][data.childrenField]);
               if (result) {
                   break;
               }
           }
       }
       return result;
   },

   getNodeById: function ($list, id) {
       var i, $node,
           $result = undefined,
           $nodes = $list.children('li');
       if ($nodes && $nodes.length) {
           for (i = 0; i < $nodes.length; i++) {
               $node = $($nodes[i]);
               if (id == $node.attr('data-id')) {
                   $result = $node;
                   break;
               } else if ($node.has('ul')) {
                   $result = gj.tree.methods.getNodeById($node.children('ul'), id);
                   if ($result) {
                       break;
                   }
               }
           }
       }
       return $result;
   },

   getNodeByText: function ($list, text) {
       var i, $node,
           $result = undefined,
           $nodes = $list.children('li');
       if ($nodes && $nodes.length) {
           for (i = 0; i < $nodes.length; i++) {
               $node = $($nodes[i]);
               if (text === $node.find('>[data-role="wrapper"]>[data-role="display"]').text()) {
                   $result = $node;
                   break;
               } else if ($node.has('ul')) {
                   $result = gj.tree.methods.getNodeByText($node.children('ul'), text);
                   if ($result) {
                       break;
                   }
               }
           }
       }
       return $result;
   },

   addNode: function ($tree, nodeData, $parent, position) {
       var level, record, data = $tree.data();

       if (!$parent || !$parent.length) {
           $parent = $tree.children('ul');
           $tree.data('records').push(nodeData);
       } else {
           if ($parent[0].tagName.toLowerCase() === 'li') {
               if ($parent.children('ul').length === 0) {
                   $parent.find('[data-role="expander"]').empty().append(data.icons.collapse);
                   $parent.append($('<ul />').addClass(data.style.list));
               }
               $parent = $parent.children('ul');
           }
           record = $tree.getDataById($parent.parent().data('id'));
           if (!record[data.childrenField]) {
               record[data.childrenField] = [];
           }
           record[data.childrenField].push(nodeData);
       }
       level = $parent.parentsUntil('[data-type="tree"]', 'ul').length + 1;
       if (!data.primaryKey) {
           gj.tree.methods.genAutoId(data, [nodeData]);
       }

       gj.tree.methods.appendNode($tree, $parent, nodeData, level, position);

       return $tree;
   },

   remove: function ($tree, $node) {
       gj.tree.methods.removeDataById($tree, $node.attr('data-id'), $tree.data('records'));
       $node.remove();
       return $tree;
   },

   removeDataById: function ($tree, id, records) {
       var i, data = $tree.data();
       for (i = 0; i < records.length; i++) {
           if (data.primaryKey && records[i][data.primaryKey] == id) {
               records.splice(i, 1);
               break;
           } else if (records[i][data.autoGenFieldName] == id) {
               records.splice(i, 1);
               break;
           } else if (records[i][data.childrenField] && records[i][data.childrenField].length) {
               gj.tree.methods.removeDataById($tree, id, records[i][data.childrenField]);
           }
       }
   },

   update: function ($tree, id, newRecord) {
       var data = $tree.data(),
           $node = $tree.getNodeById(id),
           oldRecord = $tree.getDataById(id);
       oldRecord = newRecord;
       $node.find('>[data-role="wrapper"]>[data-role="display"]').html(newRecord[data.textField]);
       gj.tree.events.nodeDataBound($tree, $node, id, newRecord);
       return $tree;
   },

   getChildren: function ($tree, $node, cascade) {
       var result = [], i, $children,
           cascade = typeof (cascade) === 'undefined' ? true : cascade;

       if (cascade) {
           $children = $node.find('ul li');
       } else {
           $children = $node.find('>ul>li');
       }

       for (i = 0; i < $children.length; i++) {
           result.push($($children[i]).data('id'));
       }

       return result;
   },

   enableAll: function ($tree) {
       var i, $children = $tree.find('ul>li');
       for (i = 0; i < $children.length; i++) {
           gj.tree.methods.enableNode($tree, $($children[i]), true);
       }
       return $tree;
   },

   enableNode: function ($tree, $node, cascade) {
       var i, $children,
           $expander = $node.find('>[data-role="wrapper"]>[data-role="expander"]'),
           $display = $node.find('>[data-role="wrapper"]>[data-role="display"]'),
           cascade = typeof (cascade) === 'undefined' ? true : cascade;

       $node.removeClass('disabled');
       $expander.on('click', gj.tree.methods.expanderClickHandler($tree));
       $display.on('click', gj.tree.methods.displayClickHandler($tree));
       gj.tree.events.enable($tree, $node);
       if (cascade) {
           $children = $node.find('ul>li');
           for (i = 0; i < $children.length; i++) {
               gj.tree.methods.enableNode($tree, $($children[i]), cascade);
           }
       }
   },

   disableAll: function ($tree) {
       var i, $children = $tree.find('ul>li');
       for (i = 0; i < $children.length; i++) {
           gj.tree.methods.disableNode($tree, $($children[i]), true);
       }
       return $tree;
   },

   disableNode: function ($tree, $node, cascade) {
       var i, $children,
           $expander = $node.find('>[data-role="wrapper"]>[data-role="expander"]'),
           $display = $node.find('>[data-role="wrapper"]>[data-role="display"]'),
           cascade = typeof (cascade) === 'undefined' ? true : cascade;

       $node.addClass('disabled');
       $expander.off('click');
       $display.off('click');
       gj.tree.events.disable($tree, $node);
       if (cascade) {
           $children = $node.find('ul>li');
           for (i = 0; i < $children.length; i++) {
               gj.tree.methods.disableNode($tree, $($children[i]), cascade);
           }
       }
   },

   destroy: function ($tree) {
       var data = $tree.data();
       if (data) {
           gj.tree.events.destroying($tree);
           $tree.xhr && $tree.xhr.abort();
           $tree.off();
           $tree.removeData();
           $tree.removeAttr('data-type');
           $tree.removeClass().empty();
       }
       return $tree;
   },

   pathFinder: function (data, list, id, parents) {
       var i, result = false;

       for (i = 0; i < list.length; i++) {
           if (list[i].id == id) {
               result = true;
               break;
           } else if (gj.tree.methods.pathFinder(data, list[i][data.childrenField], id, parents)) {
               parents.push(list[i].data[data.textField]);
               result = true;
               break;
           }
       }

       return result;
   }
}
/**
 * @widget Tree
 * @plugin Base
 */
gj.tree.widget = function ($element, jsConfig) {
   var self = this,
       methods = gj.tree.methods;

   /**
    * Reload the tree.
    * @method
    * @param {object} params - Params that needs to be send to the server. Only in use for remote data sources.
    * @return jQuery object
    * @example Method.Sample <!-- tree -->
    * <button onclick="tree.reload()" class="gj-button-md">Click to load</button>
    * <br/><br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         autoLoad: false
    *     });
    * </script>
    * @example Search <!-- tree -->
    * <input type="text" id="query" /> <button onclick="Search()">Search</button>
    * <br/><br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get'
    *     });
    *     function Search() {
    *         tree.reload({ query: $('#query').val() });
    *     }
    * </script>
    */
   self.reload = function (params) {
       return gj.widget.prototype.reload.call(this, params);
   };

   /**
    * Render data in the tree
    * @method
    * @param {object} response - An object that contains the data that needs to be loaded in the tree.
    * @fires dataBinding, dataBound
    * @return tree
    * @example sample <!-- tree -->
    * <div id="tree"></div>
    * <script>
    *     var tree, onSuccessFunc;
    *     onSuccessFunc = function (response) {
    *         //you can modify the response here if needed
    *         tree.render(response);
    *     };
    *     tree = $('#tree').tree({
    *         dataSource: { url: '/Locations/Get', success: onSuccessFunc }
    *     });
    * </script>
    */
   self.render = function (response) {
       return methods.render(this, response);
   };

   /**
    * Add node to the tree.
    * @method
    * @param {object} data - The node data.
    * @param {object} parentNode - Parent node as jquery object.
    * @param {Number} position - Position where the new node need to be added. 
    * @return jQuery object
    * @example Append.ToRoot <!-- tree -->
    * <button onclick="append()" class="gj-button-md">Append To Root</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    *     function append() {
    *         tree.addNode({ text: 'New Node' });
    *     }
    * </script>
    * @example Append.Parent <!-- tree -->
    * <button onclick="append()" class="gj-button-md">Append To Asia</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var parent, tree = $('#tree').tree();
    *     tree.on('dataBound', function () {
    *         parent = tree.getNodeByText('Asia');
    *         tree.off('dataBound');
    *     });
    *     function append() {
    *         tree.addNode({ text: 'New Node' }, parent);
    *     }
    * </script>
    * @example Bootstrap <!-- bootstrap, tree -->
    * <button onclick="append()" class="btn btn-default">Append To Asia</button>
    * <br/><br/>
    * <div id="tree" data-source="/Locations/Get" data-ui-library="bootstrap"></div>
    * <script>
    *     var parent, tree = $('#tree').tree();
    *     tree.on('dataBound', function () {
    *         parent = tree.getNodeByText('Asia');
    *         tree.off('dataBound');
    *     });
    *     function append() {
    *         tree.addNode({ text: 'New Node' }, parent);
    *     }
    * </script>
    * @example Prepend <!-- tree -->
    * <button onclick="append()" class="gj-button-md">Prepend in Asia</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var parent, tree = $('#tree').tree();
    *     tree.on('dataBound', function () {
    *         parent = tree.getNodeByText('Asia');
    *         tree.off('dataBound');
    *     });
    *     function append() {
    *         tree.addNode({ text: 'New Node' }, parent, 1);
    *     }
    * </script>
    * @example Position <!-- tree -->
    * <button onclick="append()" class="gj-button-md">Append to Asia as second</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var parent, tree = $('#tree').tree();
    *     tree.on('dataBound', function () {
    *         parent = tree.getNodeByText('Asia');
    *         tree.off('dataBound');
    *     });
    *     function append() {
    *         tree.addNode({ text: 'New Node' }, parent, 2);
    *     }
    * </script>
    */
   self.addNode = function (data, $parentNode, position) {
       return methods.addNode(this, data, $parentNode, position);
   };

   /**
    * Remove node from the tree.
    * @method
    * @param {object} node - The node as jQuery object
    * @return jQuery object
    * @example Method.Sample <!-- tree -->
    * <button onclick="remove()" class="gj-button-md">Remove USA</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get'
    *     });
    *     function remove() {
    *         var node = tree.getNodeByText('USA');
    *         tree.removeNode(node);
    *     }
    * </script>
    */
   self.removeNode = function ($node) {
       return methods.remove(this, $node);
   };

   /**
    * Update node from the tree.
    * @method
    * @param {string} id - The id of the node that needs to be updated
    * @param {object} record - The node as jQuery object
    * @return jQuery object
    * @example Method.Sample <!-- tree -->
    * <input type="text" id="nodeName" />
    * <button onclick="save()" class="gj-button-md">Save</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         primaryKey: 'id',
    *         dataSource: '/Locations/Get'
    *     });
    *     tree.on('select', function (e, node, id) {
    *         $('#nodeName').val(tree.getDataById(id).text);
    *     });
    *     function save() {
    *         var id = tree.getSelections()[0],
    *             record = tree.getDataById(id);
    *         record.text = $('#nodeName').val();
    *         tree.updateNode(id, record);
    *     }
    * </script>
    */
   self.updateNode = function (id, record) {
       return methods.update(this, id, record);
   };

   /**
    * Destroy the tree.
    * @method
    * @return jQuery object
    * @example Method.Sample <!-- tree -->
    * <button onclick="tree.destroy()" class="gj-button-md">Destroy</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get'
    *     });
    * </script>
    */
   self.destroy = function () {
       return methods.destroy(this);
   };

   /**
    * Expand node from the tree.
    * @method
    * @param {object} node - The node as jQuery object
    * @param {boolean} cascade - Expand all children
    * @return jQuery object
    * @example Method.Sample <!-- tree -->
    * <button onclick="expand()" class="gj-button-md">Expand Asia</button>
    * <button onclick="collapse()" class="gj-button-md">Collapse Asia</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get'
    *     });
    *     function expand() {
    *         var node = tree.getNodeByText('Asia');
    *         tree.expand(node);
    *     }
    *     function collapse() {
    *         var node = tree.getNodeByText('Asia');
    *         tree.collapse(node);
    *     }
    * </script>
    * @example Cascade <!-- tree -->
    * <button onclick="expand()" class="gj-button-md">Expand North America</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get'
    *     });
    *     function expand() {
    *         var node = tree.getNodeByText('North America');
    *         tree.expand(node, true);
    *     }
    * </script>
    */
   self.expand = function ($node, cascade) {
       return methods.expand(this, $node, cascade);
   };

   /**
    * Collapse node from the tree.
    * @method
    * @param {object} node - The node as jQuery object
    * @param {boolean} cascade - Collapse all children
    * @return jQuery object
    * @example Method.Sample <!-- tree -->
    * <button onclick="expand()" class="gj-button-md">Expand Asia</button>
    * <button onclick="collapse()" class="gj-button-md">Collapse Asia</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get'
    *     });
    *     function expand() {
    *         var node = tree.getNodeByText('Asia');
    *         tree.expand(node);
    *     }
    *     function collapse() {
    *         var node = tree.getNodeByText('Asia');
    *         tree.collapse(node);
    *     }
    * </script>
    * @example Cascade <!-- tree -->
    * <button onclick="collapse()" class="gj-button-md">Collapse North America</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get'
    *     });
    *     function collapse() {
    *         var node = tree.getNodeByText('North America');
    *         tree.collapse(node, true);
    *     }
    * </script>
    */
   self.collapse = function ($node, cascade) {
       return methods.collapse(this, $node, cascade);
   };

   /**
    * Expand all tree nodes
    * @method
    * @return jQuery object
    * @example Sample <!-- tree -->
    * <button onclick="tree.expandAll()" class="gj-button-md">Expand All</button>
    * <button onclick="tree.collapseAll()" class="gj-button-md">Collapse All</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    * </script>
    */
   self.expandAll = function () {
       return methods.expandAll(this);
   };

   /**
    * Collapse all tree nodes
    * @method
    * @return jQuery object
    * @example Sample <!-- tree -->
    * <button onclick="tree.expandAll()" class="gj-button-md">Expand All</button>
    * <button onclick="tree.collapseAll()" class="gj-button-md">Collapse All</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree();
    * </script>
    */
   self.collapseAll = function () {
       return methods.collapseAll(this);
   };

   /**
    * Return node data by id of the record.
    * @method
    * @param {string|number} id - The id of the record that needs to be returned
    * @return object
    * @example sample <!-- tree -->
    * <button id="btnGetData" class="gj-button-md">Get Data</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         primaryKey: 'id' //define the name of the column that you want to use as ID here.
    *     });
    *     $('#btnGetData').on('click', function () {
    *         var data = tree.getDataById(9);
    *         alert('The population of ' + data.text + ' is ' + data.population);
    *     });
    * </script>
    */
   self.getDataById = function (id) {
       return methods.getDataById(this, id, this.data('records'));
   };

   /**
    * Return node data by text.
    * @method
    * @param {string} text - The text of the record that needs to be returned
    * @return object
    * @example sample <!-- tree -->
    * <button id="btnGetData" class="gj-button-md">Get Data</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *     });
    *     $('#btnGetData').on('click', function () {
    *         var data = tree.getDataByText('California');
    *         alert('The population of California is ' + data.population);
    *     });
    * </script>
    */
   self.getDataByText = function (text) {
       return methods.getDataByText(this, text, this.data('records'));
   };

   /**
    * Return node by id of the record.
    * @method
    * @param {string} id - The id of the node that needs to be returned
    * @return jQuery object
    * @example sample <!-- tree -->
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         primaryKey: 'id' //define the name of the column that you want to use as ID here.
    *     });
    *     tree.on('dataBound', function() {
    *         var node = tree.getNodeById('1');
    *         node.css('background-color', 'red');
    *     });
    * </script>
    */
   self.getNodeById = function (id) {
       return methods.getNodeById(this.children('ul'), id);
   };

   /**
    * Return node by text.
    * @method
    * @param {string} text - The text in the node that needs to be returned
    * @return jQuery object
    * @example sample <!-- tree -->
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get'
    *     });
    *     tree.on('dataBound', function() {
    *         var node = tree.getNodeByText('Asia');
    *         node.css('background-color', 'red');
    *     });
    * </script>
    */
   self.getNodeByText = function (text) {
       return methods.getNodeByText(this.children('ul'), text);
   };

   /**
    * Return an array with all records presented in the tree.
    * @method
    * @return Array
    * @example sample <!-- tree -->
    * <button onclick="alert(JSON.stringify(tree.getAll()))" class="gj-button-md">Get All Data</button>
    * <button onclick="tree.addNode({ text: 'New Node' });" class="gj-button-md">Add New Node</button>
    * <br/>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: [ { text: 'foo', children: [ { text: 'bar' } ] } ]
    *     });
    * </script>
    */
   self.getAll = function () {
       return this.data('records');
   };

   /**
    * Select node from the tree.
    * @method
    * @param {Object} node - The node as jquery object.
    * @return jQuery Object
    * @example Select.Method <!-- tree -->
    * <button onclick="tree.select(northAmerica)" class="gj-button-md">Select North America</button>
    * <button onclick="tree.unselect(northAmerica)" class="gj-button-md">Unselect North America</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree, northAmerica;
    *     tree = $('#tree').tree({
    *         primaryKey: 'id',
    *         dataBound: function () {
    *             northAmerica = tree.getNodeByText('North America');
    *         },
    *         select: function (e, node, id) {
    *             alert('select is fired for node with id=' + id);
    *         }
    *     });
    * </script>
    */
   self.select = function ($node) {
       return methods.select(this, $node);
   };

   /**
    * Unselect node from the tree.
    * @method
    * @param {Object} node - The node as jquery object.
    * @return jQuery Object
    * @example UnSelect.Method <!-- tree -->
    * <button onclick="tree.select(northAmerica)" class="gj-button-md">Select North America</button>
    * <button onclick="tree.unselect(northAmerica)" class="gj-button-md">Unselect North America</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree, northAmerica;
    *     tree = $('#tree').tree({
    *         primaryKey: 'id',
    *         dataBound: function () {
    *             northAmerica = tree.getNodeByText('North America');  
    *         },
    *         unselect: function (e, node, id) {
    *             alert('unselect is fired for node with id=' + id);
    *         }
    *     });
    * </script>
    */
   self.unselect = function ($node) {
       return methods.unselect(this, $node);
   };

   /**
    * Select all tree nodes
    * @method
    * @return jQuery object
    * @example Sample <!-- tree -->
    * <button onclick="tree.selectAll()" class="gj-button-md">Select All</button>
    * <button onclick="tree.unselectAll()" class="gj-button-md">Unselect All</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         selectionType: 'multiple'
    *     });
    *     tree.on('dataBound', function() {
    *         tree.expandAll();
    *     });
    * </script>
    */
   self.selectAll = function () {
       return methods.selectAll(this);
   };

   /**
    * Unselect all tree nodes
    * @method
    * @return jQuery object
    * @example Sample <!-- tree -->
    * <button onclick="tree.selectAll()" class="gj-button-md">Select All</button>
    * <button onclick="tree.unselectAll()" class="gj-button-md">Unselect All</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         selectionType: 'multiple'
    *     });
    *     tree.on('dataBound', function() {
    *         tree.expandAll();
    *     });
    * </script>
    */
   self.unselectAll = function () {
       return methods.unselectAll(this);
   };

   /**
    * Return an array with the ids of the selected nodes.
    * @method
    * @return array
    * @example Sample <!-- tree -->
    * <button id="btnShowSelection" class="gj-button-md">Show Selections</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         selectionType: 'multiple'
    *     });
    *     $('#btnShowSelection').on('click', function () {
    *         var selections = tree.getSelections();
    *         selections && selections.length && alert(selections.join());
    *     });
    * </script>
    */
   self.getSelections = function () {
       return methods.getSelections(this.children('ul'));
   };

   /**
    * Return an array with the ids of all children.
    * @method
    * @param {Object} node - The node as jquery object.
    * @param {Boolean} cascade - Include all nested children. Set to true by default.
    * @return array
    * @example Cascade.True <!-- tree -->
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         dataBound: function () {
    *             var node = tree.getNodeByText('North America'),
    *                 children = tree.getChildren(node);
    *             alert(children.join());
    *         }
    *     });
    * </script>
    * @example Cascade.False <!-- tree -->
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         dataBound: function () {
    *             var node = tree.getNodeByText('North America'),
    *                 children = tree.getChildren(node, false);
    *             alert(children.join());
    *         }
    *     });
    * </script>
    */
   self.getChildren = function ($node, cascade) {
       return methods.getChildren(this, $node, cascade);
   };

   /**
    * Return an array with the names of all parents.
    * @method
    * @param {String} id - The id of the target node
    * @return array
    * @example sample <!-- tree -->
    * Location: <div id="location" style="display: inline-block;"></div>
    * <div id="tree"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         dataSource: '/Locations/Get',
    *         select: function (e, node, id) {
    *             var parents = tree.parents(id);
    *             $('#location').text(parents.join(' / ') + ' / ' + tree.getDataById(id).text);
    *         }
    *     });
    * </script>
    */
   self.parents = function (id) {
       var parents = [], data = this.data();
       methods.pathFinder(data, data.records, id, parents);
       return parents.reverse();
   };

   /**
    * Enable node from the tree.
    * @method
    * @param {Object} node - The node as jquery object.
    * @param {Boolean} cascade - Enable all children. Set to true by default.
    * @return jQuery Object
    * @example Material.Design <!-- checkbox, tree -->
    * <button onclick="tree.enable(northAmerica)" class="gj-button-md">Enable North America (Cascade)</button>
    * <button onclick="tree.disable(northAmerica)" class="gj-button-md">Disable North America (Cascade)</button>
    * <button onclick="tree.enable(northAmerica, false)" class="gj-button-md">Enable North America (Non-Cascade)</button>
    * <button onclick="tree.disable(northAmerica, false)" class="gj-button-md">Disable North America (Non-Cascade)</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree, northAmerica;
    *     tree = $('#tree').tree({
    *         checkboxes: true,
    *         primaryKey: 'ID',
    *         dataBound: function () {
    *             northAmerica = tree.getNodeByText('North America');
    *         }
    *     });
    * </script>
    * @example Bootstrap <!-- bootstrap, checkbox, tree -->
    * <button onclick="tree.enable(northAmerica)" class="btn btn-default">Enable North America (Cascade)</button>
    * <button onclick="tree.disable(northAmerica)" class="btn btn-default">Disable North America (Cascade)</button>
    * <button onclick="tree.enable(northAmerica, false)" class="btn btn-default">Enable North America (Non-Cascade)</button>
    * <button onclick="tree.disable(northAmerica, false)" class="btn btn-default">Disable North America (Non-Cascade)</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree, northAmerica;
    *     tree = $('#tree').tree({
    *         checkboxes: true,
    *         primaryKey: 'ID',
    *         uiLibrary: 'bootstrap',
    *         dataBound: function () {
    *             northAmerica = tree.getNodeByText('North America');
    *         }
    *     });
    * </script>
    * @example Bootstrap.4 <!-- bootstrap4, fontawesome, checkbox, tree -->
    * <button onclick="tree.enable(northAmerica)" class="btn btn-default">Enable North America (Cascade)</button>
    * <button onclick="tree.disable(northAmerica)" class="btn btn-default">Disable North America (Cascade)</button>
    * <button onclick="tree.enable(northAmerica, false)" class="btn btn-default">Enable North America (Non-Cascade)</button>
    * <button onclick="tree.disable(northAmerica, false)" class="btn btn-default">Disable North America (Non-Cascade)</button>
    * <br/><br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree, northAmerica;
    *     tree = $('#tree').tree({
    *         checkboxes: true,
    *         primaryKey: 'ID',
    *         uiLibrary: 'bootstrap4',
    *         iconsLibrary: 'fontawesome',
    *         dataBound: function () {
    *             northAmerica = tree.getNodeByText('North America');
    *         }
    *     });
    * </script>
    */
   self.enable = function ($node, cascade) {
       return methods.enableNode(this, $node, cascade);
   };

   /**
    * Enable all nodes from the tree.
    * @method
    * @return jQuery Object
    * @example Sample <!-- checkbox, tree -->
    * <button onclick="tree.enableAll()" class="gj-button-md">Enable All</button>
    * <button onclick="tree.disableAll()" class="gj-button-md">Disable All</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         checkboxes: true
    *     });
    * </script>
    */
   self.enableAll = function () {
       return methods.enableAll(this);
   };

   /**
    * Disable node from the tree.
    * @method
    * @param {Object} node - The node as jquery object.
    * @param {Boolean} cascade - Disable all children. Set to true by default.
    * @return jQuery Object
    * @example Sample <!-- checkbox, tree -->
    * <button onclick="tree.enable(northAmerica)" class="gj-button-md">Enable North America (Cascade)</button>
    * <button onclick="tree.disable(northAmerica)" class="gj-button-md">Disable North America (Cascade)</button>
    * <button onclick="tree.enable(northAmerica, false)" class="gj-button-md">Enable North America (Non-Cascade)</button>
    * <button onclick="tree.disable(northAmerica, false)" class="gj-button-md">Disable North America (Non-Cascade)</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree, northAmerica;
    *     tree = $('#tree').tree({
    *         checkboxes: true,
    *         primaryKey: 'ID',
    *         dataBound: function () {
    *             northAmerica = tree.getNodeByText('North America');
    *         }
    *     });
    * </script>
    */
   self.disable = function ($node, cascade) {
       return methods.disableNode(this, $node, cascade);
   };

   /**
    * Disable all nodes from the tree.
    * @method
    * @return jQuery Object
    * @example Sample <!-- checkbox, tree -->
    * <button onclick="tree.enableAll()" class="gj-button-md">Enable All</button>
    * <button onclick="tree.disableAll()" class="gj-button-md">Disable All</button>
    * <br/>
    * <div id="tree" data-source="/Locations/Get"></div>
    * <script>
    *     var tree = $('#tree').tree({
    *         checkboxes: true
    *     });
    * </script>
    */
   self.disableAll = function () {
       return methods.disableAll(this);
   };

   $.extend($element, self);
   if ('tree' !== $element.attr('data-type')) {
       methods.init.call($element, jsConfig);
   }

   return $element;
};

gj.tree.widget.prototype = new gj.widget();
gj.tree.widget.constructor = gj.tree.widget;

(function ($) {
   $.fn.tree = function (method) {
       var $widget;
       if (this && this.length) {
           if (typeof method === 'object' || !method) {
               return new gj.tree.widget(this, method);
           } else {
               $widget = new gj.tree.widget(this, null);
               if ($widget[method]) {
                   return $widget[method].apply(this, Array.prototype.slice.call(arguments, 1));
               } else {
                   throw 'Method ' + method + ' does not exist.';
               }
           }
       }
   };
})(jQuery);
/** 
* @widget Tree 
* @plugin Checkboxes
*/
gj.tree.plugins.checkboxes = {
   config: {
       base: {
           /** Add checkbox for each node, if set to true.
             * @type Boolean
             * @default undefined
             * @example Material.Design <!-- checkbox, tree -->
             * <div id="tree"></div>
             * <script>
             *     var tree = $('#tree').tree({
             *         dataSource: '/Locations/Get',
             *         checkboxes: true
             *     });
             * </script>
             * @example Bootstrap.3 <!-- bootstrap, checkbox, tree -->
             * <div id="tree"></div>
             * <script>
             *     var tree = $('#tree').tree({
             *         dataSource: '/Locations/Get',
             *         checkboxes: true,
             *         uiLibrary: 'bootstrap'
             *     });
             * </script>
             * @example Bootstrap.4 <!-- bootstrap4, checkbox, tree -->
             * <div id="tree"></div>
             * <script>
             *     var tree = $('#tree').tree({
             *         dataSource: '/Locations/Get',
             *         checkboxes: true,
             *         uiLibrary: 'bootstrap4'
             *     });
             * </script>
             */
           checkboxes: undefined,

           /** Name of the source field, that indicates if the checkbox is checked.
            * @type string
            * @default 'checked'
            * @example Custom.Name <!-- checkbox, tree -->
            * <div id="tree"></div>
            * <script>
            *     var tree = $('#tree').tree({
            *         checkboxes: true,
            *         checkedField: 'checkedFieldName',
            *         dataSource: [ { text: 'foo', checkedFieldName: false, children: [ { text: 'bar', checkedFieldName: true }, { text: 'bar2', checkedFieldName: false } ] }, { text: 'foo2', children: [ { text: 'bar2' } ] } ]
            *     });
            * </script>
            */
           checkedField: 'checked',

           /** This setting enable cascade check and uncheck of children
            * @type boolean
            * @default true
            * @example False.Remote.DataSource <!-- checkbox, tree -->
            * <div id="tree"></div>
            * <script>
            *     var tree = $('#tree').tree({
            *         checkboxes: true,
            *         dataSource: '/Locations/Get',
            *         cascadeCheck: false
            *     });
            *     tree.on('dataBound', function() {
            *         tree.expandAll();
            *     });
            * </script>
            * @example False.Local.DataSource <!-- checkbox, tree -->
            * <div id="tree"></div>
            * <script>
            *     var tree = $('#tree').tree({
            *         checkboxes: true,
            *         dataSource: [ { text: 'foo', checked: true, children: [ { text: 'bar', checked: true }, { text: 'bar2', checked: false } ] }, { text: 'foo2', checked: true, children: [ { text: 'bar2', checked: false } ] } ],
            *         cascadeCheck: false
            *     });
            * </script>
            * @example True <!-- checkbox, tree -->
            * <div id="tree"></div>
            * <script>
            *     var tree = $('#tree').tree({
            *         checkboxes: true,
            *         dataSource: '/Locations/Get',
            *         cascadeCheck: true
            *     });
            *     tree.on('dataBound', function() {
            *         tree.expandAll();
            *     });
            * </script>
            */
           cascadeCheck: true,
       }
   },

   private: {
       dataBound: function ($tree) {
           var $nodes;
           if ($tree.data('cascadeCheck')) {
               $nodes = $tree.find('li[data-role="node"]');
               $.each($nodes, function () {
                   var $node = $(this),
                       state = $node.find('[data-role="checkbox"] input[type="checkbox"]').checkbox('state');
                   if (state === 'checked') {
                       gj.tree.plugins.checkboxes.private.updateChildrenState($node, state);
                       gj.tree.plugins.checkboxes.private.updateParentState($node, state);
                   }
               });
           }
       },

       nodeDataBound: function ($tree, $node, id, record) {
           var data, $expander, $checkbox, $wrapper, disabled;

           if ($node.find('> [data-role="wrapper"] > [data-role="checkbox"]').length === 0) {
               data = $tree.data();
               $expander = $node.find('> [data-role="wrapper"] > [data-role="expander"]');
               $checkbox = $('<input type="checkbox"/>');
               $wrapper = $('<span data-role="checkbox"></span>').append($checkbox);
               disabled = typeof (record[data.disabledField]) !== 'undefined' && record[data.disabledField].toString().toLowerCase() === 'true';

               $checkbox = $checkbox.checkbox({
                   uiLibrary: data.uiLibrary,
                   iconsLibrary: data.iconsLibrary,
                   change: function (e, state) {
                       gj.tree.plugins.checkboxes.events.checkboxChange($tree, $node, record, $checkbox.state());
                   }
               });
               disabled && $checkbox.prop('disabled', true);
               record[data.checkedField] && $checkbox.state('checked');
               $checkbox.on('click', function (e) {
                   var $node = $checkbox.closest('li'),
                       state = $checkbox.state();
                   if (data.cascadeCheck) {
                       gj.tree.plugins.checkboxes.private.updateChildrenState($node, state);
                       gj.tree.plugins.checkboxes.private.updateParentState($node, state);
                   }
               });
               $expander.after($wrapper);
           }
       },

       updateParentState: function ($node, state) {
           var $parentNode, $parentCheckbox, $siblingCheckboxes, allChecked, allUnchecked, parentState;

           $parentNode = $node.parent('ul').parent('li');
           if ($parentNode.length === 1) {
               $parentCheckbox = $node.parent('ul').parent('li').find('> [data-role="wrapper"] > [data-role="checkbox"] input[type="checkbox"]');
               $siblingCheckboxes = $node.siblings().find('> [data-role="wrapper"] > span[data-role="checkbox"] input[type="checkbox"]');
               allChecked = (state === 'checked');
               allUnchecked = (state === 'unchecked');
               parentState = 'indeterminate';
               $.each($siblingCheckboxes, function () {
                   var state = $(this).checkbox('state');
                   if (allChecked && state !== 'checked') {
                       allChecked = false;
                   }
                   if (allUnchecked && state !== 'unchecked') {
                       allUnchecked = false;
                   }
               });
               if (allChecked && !allUnchecked) {
                   parentState = 'checked';
               }
               if (!allChecked && allUnchecked) {
                   parentState = 'unchecked';
               }
               $parentCheckbox.checkbox('state', parentState);
               gj.tree.plugins.checkboxes.private.updateParentState($parentNode, $parentCheckbox.checkbox('state'));
           }
       },

       updateChildrenState: function ($node, state) {
           var $childrenCheckboxes = $node.find('ul li [data-role="wrapper"] [data-role="checkbox"] input[type="checkbox"]');
           if ($childrenCheckboxes.length > 0) {
               $.each($childrenCheckboxes, function () {
                   $(this).checkbox('state', state);
               });
           }
       },

       update: function ($tree, $node, state) {
           var checkbox = $node.find('[data-role="checkbox"] input[type="checkbox"]').first();
           $(checkbox).checkbox('state', state);
           if ($tree.data().cascadeCheck) {
               gj.tree.plugins.checkboxes.private.updateChildrenState($node, state);
               gj.tree.plugins.checkboxes.private.updateParentState($node, state);
           }
       }
   },

   public: {

       /** Get ids of all checked nodes
        * @method
        * @return Array
        * @example Base.Theme <!-- checkbox, tree -->
        * <button id="btnGet" class="gj-button-md">Get Checked Nodes</button>
        * <div id="tree"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         dataSource: '/Locations/Get',
        *         checkboxes: true
        *     });
        *     $('#btnGet').on('click', function() {
        *         var result = tree.getCheckedNodes();
        *         alert(result.join());
        *     });
        * </script>
        */
       getCheckedNodes: function () {
           var result = [],
               checkboxes = this.find('li [data-role="checkbox"] input[type="checkbox"]');
           $.each(checkboxes, function () {
               var checkbox = $(this);
               if (checkbox.checkbox('state') === 'checked') {
                   result.push(checkbox.closest('li').data('id'));
               }
           });
           return result;
       },

       /**
        * Check all tree nodes
        * @method
        * @return tree as jQuery object
        * @example Sample <!-- checkbox, tree -->
        * <button onclick="tree.checkAll()" class="gj-button-md">Check All</button>
        * <button onclick="tree.uncheckAll()" class="gj-button-md">Uncheck All</button>
        * <br/><br/>
        * <div id="tree" data-source="/Locations/Get"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         checkboxes: true
        *     });
        *     tree.on('dataBound', function() {
        *         tree.expandAll();
        *     });
        * </script>
        */
       checkAll: function () {
           var $checkboxes = this.find('li [data-role="checkbox"] input[type="checkbox"]');
           $.each($checkboxes, function () {
               $(this).checkbox('state', 'checked');
           });
           return this;
       },

       /**
        * Uncheck all tree nodes
        * @method
        * @return tree as jQuery object
        * @example Sample <!-- checkbox, tree -->
        * <button onclick="tree.checkAll()" class="gj-button-md">Check All</button>
        * <button onclick="tree.uncheckAll()" class="gj-button-md">Uncheck All</button>
        * <br/><br/>
        * <div id="tree" data-source="/Locations/Get"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         checkboxes: true
        *     });
        *     tree.on('dataBound', function() {
        *         tree.expandAll();
        *     });
        * </script>
        */
       uncheckAll: function () {
           var $checkboxes = this.find('li [data-role="checkbox"] input[type="checkbox"]');
           $.each($checkboxes, function () {
               $(this).checkbox('state', 'unchecked');
           });
           return this;
       },

       /**
        * Check tree node.
        * @method
        * @param {object} node - The node as jQuery object
        * @return tree as jQuery object
        * @example Sample <!-- checkbox, tree -->
        * <button onclick="tree.check(tree.getNodeByText('China'))" class="gj-button-md">Check China</button>
        * <br/>
        * <div id="tree" data-source="/Locations/Get"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         checkboxes: true
        *     });
        *     tree.on('dataBound', function() {
        *         tree.expandAll();
        *     });
        * </script>
        */
       check: function ($node) {
           gj.tree.plugins.checkboxes.private.update(this, $node, 'checked');
           return this;
       },

       /**
        * Uncheck tree node.
        * @method
        * @param {object} node - The node as jQuery object
        * @return tree as jQuery object
        * @example Sample <!-- checkbox, tree -->
        * <button onclick="tree.uncheck(tree.getNodeByText('China'))" class="gj-button-md">UnCheck China</button>
        * <br/>
        * <div id="tree" data-source="/Locations/Get"></div>
        * <script>
        *     var tree = $('#tree').tree({
        *         checkboxes: true
        *     });
        *     tree.on('dataBound', function() {
        *         tree.expandAll();
        *         tree.check(tree.getNodeByText('China'));
        *     });
        * </script>
        */
       uncheck: function ($node) {
           gj.tree.plugins.checkboxes.private.update(this, $node, 'unchecked');
           return this;
       }
   },

   events: {
       /**
        * Event fires when the checkbox state is changed.
        * @event checkboxChange
        * @param {object} e - event data
        * @param {object} $node - the node object as jQuery element
        * @param {object} record - the record data
        * @param {string} state - the new state of the checkbox
        * @example Event.Sample <!-- checkbox, tree -->
        * <div id="tree" data-source="/Locations/Get" data-checkboxes="true"></div>
        * <script>
        *     var tree = $('#tree').tree();
        *     tree.on('checkboxChange', function (e, $node, record, state) {
        *         alert('The new state of record ' + record.text + ' is ' + state);
        *     });
        * </script>
        */
       checkboxChange: function ($tree, $node, record, state) {
           return $tree.triggerHandler('checkboxChange', [$node, record, state]);
       }
   },

   configure: function ($tree) {
       if ($tree.data('checkboxes') && gj.checkbox) {
           $.extend(true, $tree, gj.tree.plugins.checkboxes.public);
           $tree.on('nodeDataBound', function (e, $node, id, record) {
               gj.tree.plugins.checkboxes.private.nodeDataBound($tree, $node, id, record);
           });
           $tree.on('dataBound', function () {
               gj.tree.plugins.checkboxes.private.dataBound($tree);
           });
           $tree.on('enable', function (e, $node) {
               $node.find('>[data-role="wrapper"]>[data-role="checkbox"] input[type="checkbox"]').prop('disabled', false);
           });
           $tree.on('disable', function (e, $node) {
               $node.find('>[data-role="wrapper"]>[data-role="checkbox"] input[type="checkbox"]').prop('disabled', true);
           });
       }
   }
};

/**
* @widget Tree
* @plugin DragAndDrop
*/
gj.tree.plugins.dragAndDrop = {
	config: {
		base: {
			/** Enables drag and drop functionality for each node.
             * @type Boolean
             * @default undefined
             * @example Material.Design <!-- draggable, droppable, tree -->
             * <h3>Drag and Drop Tree Nodes</h3>
             * <div id="tree"></div>
             * <script>
             *     $('#tree').tree({
             *         dataSource: '/Locations/Get',
             *         dragAndDrop: true
             *     });
             * </script>
             * @example Bootstrap.3 <!-- bootstrap, draggable, droppable, tree -->
             * <div class="container">
             *     <h3>Drag and Drop Tree Nodes</h3>
             *     <div id="tree"></div>
             * </div>
             * <script>
             *     $('#tree').tree({
             *         dataSource: '/Locations/Get',
             *         dragAndDrop: true,
             *         uiLibrary: 'bootstrap'
             *     });
             * </script>
             * @example Bootstrap.4 <!-- bootstrap4, draggable, droppable, tree -->
             * <div class="container">
             *     <h3>Drag and Drop Tree Nodes</h3>
             *     <div id="tree"></div>
             * </div>
             * <script>
             *     $('#tree').tree({
             *         dataSource: '/Locations/Get',
             *         dragAndDrop: true,
             *         uiLibrary: 'bootstrap4'
             *     });
             * </script>
             */
			dragAndDrop: undefined,

			style: {
			    dragEl: 'gj-tree-drag-el gj-tree-md-drag-el',
               dropAsChildIcon: 'gj-cursor-pointer gj-icon plus',
			    dropAbove: 'gj-tree-drop-above',
			    dropBelow: 'gj-tree-drop-below'
			}
       },

       bootstrap: {
           style: {
               dragEl: 'gj-tree-drag-el gj-tree-bootstrap-drag-el',
               dropAsChildIcon: 'glyphicon glyphicon-plus',
               dropAbove: 'drop-above',
               dropBelow: 'drop-below'
           }
       },

       bootstrap4: {
           style: {
               dragEl: 'gj-tree-drag-el gj-tree-bootstrap-drag-el',
               dropAsChildIcon: 'gj-cursor-pointer gj-icon plus',
               dropAbove: 'drop-above',
               dropBelow: 'drop-below'
           }
       }
	},

	private: {
	    nodeDataBound: function ($tree, $node) {
	        var $wrapper = $node.children('[data-role="wrapper"]'),
   	        $display = $node.find('>[data-role="wrapper"]>[data-role="display"]');
           if ($wrapper.length && $display.length) {
               $display.on('mousedown', gj.tree.plugins.dragAndDrop.private.createNodeMouseDownHandler($tree));
               $display.on('mousemove', gj.tree.plugins.dragAndDrop.private.createNodeMouseMoveHandler($tree, $node, $display));
               $display.on('mouseup', gj.tree.plugins.dragAndDrop.private.createNodeMouseUpHandler($tree));
		    }
       },

       createNodeMouseDownHandler: function ($tree) {
           return function (e) {
               $tree.data('dragReady', true);
           }
       },

       createNodeMouseUpHandler: function ($tree) {
           return function (e) {
               $tree.data('dragReady', false);
           }
       },

	    createNodeMouseMoveHandler: function ($tree, $node, $display) {
           return function (e) {
               if ($tree.data('dragReady')) {
                   var data = $tree.data(), $dragEl, $wrapper, offsetTop, offsetLeft;

                   $tree.data('dragReady', false);
                   $dragEl = $display.clone().wrap('<div data-role="wrapper"/>').closest('div')
                       .wrap('<li class="' + data.style.item + '" />').closest('li')
                       .wrap('<ul class="' + data.style.list + '" />').closest('ul');
                   $('body').append($dragEl);
                   $dragEl.attr('data-role', 'draggable-clone').addClass('gj-unselectable').addClass(data.style.dragEl);
                   $dragEl.find('[data-role="wrapper"]').prepend('<span data-role="indicator" />');
                   $dragEl.draggable({
                       drag: gj.tree.plugins.dragAndDrop.private.createDragHandler($tree, $node, $display),
                       stop: gj.tree.plugins.dragAndDrop.private.createDragStopHandler($tree, $node, $display)
                   });
                   $wrapper = $display.parent();
                   offsetTop = $display.offset().top;
                   offsetTop -= parseInt($wrapper.css("border-top-width")) + parseInt($wrapper.css("margin-top")) + parseInt($wrapper.css("padding-top"));
                   offsetLeft = $display.offset().left;
                   offsetLeft -= parseInt($wrapper.css("border-left-width")) + parseInt($wrapper.css("margin-left")) + parseInt($wrapper.css("padding-left"));
                   offsetLeft -= $dragEl.find('[data-role="indicator"]').outerWidth(true);
                   $dragEl.css({
                       position: 'absolute', top: offsetTop, left: offsetLeft, width: $display.outerWidth(true)
                   });
                   if ($display.attr('data-droppable') === 'true') {
                       $display.droppable('destroy');
                   }
                   gj.tree.plugins.dragAndDrop.private.getTargetDisplays($tree, $node, $display).each(function () {
                       var $dropEl = $(this);
                       if ($dropEl.attr('data-droppable') === 'true') {
                           $dropEl.droppable('destroy');
                       }
                       $dropEl.droppable();
                   });
                   gj.tree.plugins.dragAndDrop.private.getTargetDisplays($tree, $node).each(function () {
                       var $dropEl = $(this);
                       if ($dropEl.attr('data-droppable') === 'true') {
                           $dropEl.droppable('destroy');
                       }
                       $dropEl.droppable();
                   });
                   $dragEl.trigger('mousedown');
               }
		    };
	    },

	    getTargetDisplays: function ($tree, $node, $display) {
	        return $tree.find('[data-role="display"]').not($display).not($node.find('[data-role="display"]'));
	    },

	    getTargetWrappers: function ($tree, $node) {
	        return $tree.find('[data-role="wrapper"]').not($node.find('[data-role="wrapper"]'));
	    },

	    createDragHandler: function ($tree, $node, $display) {
	        var $displays = gj.tree.plugins.dragAndDrop.private.getTargetDisplays($tree, $node, $display),
               $wrappers = gj.tree.plugins.dragAndDrop.private.getTargetWrappers($tree, $node),
	            data = $tree.data();
	        return function (e, offset, mousePosition) {
	            var $dragEl = $(this), success = false;
	            $displays.each(function () {
	                var $targetDisplay = $(this),
	                    $indicator;
	                if ($targetDisplay.droppable('isOver', mousePosition)) {
	                    $indicator = $dragEl.find('[data-role="indicator"]');
	                    data.style.dropAsChildIcon ? $indicator.addClass(data.style.dropAsChildIcon) : $indicator.text('+');
	                    success = true;
	                    return false;
	                } else {
	                    $dragEl.find('[data-role="indicator"]').removeClass(data.style.dropAsChildIcon).empty();
                   }
	            });
	            $wrappers.each(function () {
	                var $wrapper = $(this),
                       $indicator, middle;
	                if (!success && $wrapper.droppable('isOver', mousePosition)) {
	                    middle = $wrapper.position().top + ($wrapper.outerHeight() / 2);
	                    if (mousePosition.y < middle) {
	                        $wrapper.addClass(data.style.dropAbove).removeClass(data.style.dropBelow);
	                    } else {
	                        $wrapper.addClass(data.style.dropBelow).removeClass(data.style.dropAbove);
	                    }
	                } else {
	                    $wrapper.removeClass(data.style.dropAbove).removeClass(data.style.dropBelow);
	                }
	            });
	        };
       },

	    createDragStopHandler: function ($tree, $sourceNode, $sourceDisplay) {
	        var $displays = gj.tree.plugins.dragAndDrop.private.getTargetDisplays($tree, $sourceNode, $sourceDisplay),
               $wrappers = gj.tree.plugins.dragAndDrop.private.getTargetWrappers($tree, $sourceNode),
	            data = $tree.data();
	        return function (e, mousePosition) {
               var success = false, record, $targetNode, $sourceParentNode, parent;
	            $(this).draggable('destroy').remove();
	            $displays.each(function () {
	                var $targetDisplay = $(this), $ul;
	                if ($targetDisplay.droppable('isOver', mousePosition)) {
	                    $targetNode = $targetDisplay.closest('li');
	                    $sourceParentNode = $sourceNode.parent('ul').parent('li');
	                    $ul = $targetNode.children('ul');
	                    if ($ul.length === 0) {
	                        $ul = $('<ul />').addClass(data.style.list);
	                        $targetNode.append($ul);
	                    }
	                    if (gj.tree.plugins.dragAndDrop.events.nodeDrop($tree, $sourceNode.data('id'), $targetNode.data('id'), $ul.children('li').length + 1) !== false) {
                           $ul.append($sourceNode);

                           //BEGIN: Change node position inside the backend data
                           record = $tree.getDataById($sourceNode.data('id'));
                           gj.tree.methods.removeDataById($tree, $sourceNode.data('id'), data.records);
                           parent = $tree.getDataById($ul.parent().data('id'));
                           if (parent[data.childrenField] === undefined) {
                               parent[data.childrenField] = [];
                           }
                           parent[data.childrenField].push(record);
                           //END

	                        gj.tree.plugins.dragAndDrop.private.refresh($tree, $sourceNode, $targetNode, $sourceParentNode);
	                    }
	                    success = true;
	                    return false;
	                }
	                $targetDisplay.droppable('destroy');
	            });
	            if (!success) {
	                $wrappers.each(function () {
	                    var $targetWrapper = $(this), prepend, orderNumber, sourceNodeId;
	                    if ($targetWrapper.droppable('isOver', mousePosition)) {
	                        $targetNode = $targetWrapper.closest('li');
	                        $sourceParentNode = $sourceNode.parent('ul').parent('li');
	                        prepend = mousePosition.y < ($targetWrapper.position().top + ($targetWrapper.outerHeight() / 2));
	                        sourceNodeId = $sourceNode.data('id');
	                        orderNumber = $targetNode.prevAll('li:not([data-id="' + sourceNodeId + '"])').length + (prepend ? 1 : 2);
                           if (gj.tree.plugins.dragAndDrop.events.nodeDrop($tree, sourceNodeId, $targetNode.parent('ul').parent('li').data('id'), orderNumber) !== false) {
                               //BEGIN: Change node position inside the backend data
                               record = $tree.getDataById($sourceNode.data('id'));
                               gj.tree.methods.removeDataById($tree, $sourceNode.data('id'), data.records);
                               $tree.getDataById($targetNode.parent().data('id'))[data.childrenField].splice($targetNode.index() + (prepend ? 0 : 1), 0, record);
                               //END

	                            if (prepend) {
                                   $sourceNode.insertBefore($targetNode);
	                            } else {
	                                $sourceNode.insertAfter($targetNode);
                               }

                               gj.tree.plugins.dragAndDrop.private.refresh($tree, $sourceNode, $targetNode, $sourceParentNode);
	                        }
	                        return false;
	                    }
	                    $targetWrapper.droppable('destroy');
	                });
               }
	        }
	    },

	    refresh: function ($tree, $sourceNode, $targetNode, $sourceParentNode) {
	        var data = $tree.data();
	        gj.tree.plugins.dragAndDrop.private.refreshNode($tree, $targetNode);
	        gj.tree.plugins.dragAndDrop.private.refreshNode($tree, $sourceParentNode);
	        gj.tree.plugins.dragAndDrop.private.refreshNode($tree, $sourceNode);
	        $sourceNode.find('li[data-role="node"]').each(function () {
	            gj.tree.plugins.dragAndDrop.private.refreshNode($tree, $(this));
	        });
	        $targetNode.children('[data-role="wrapper"]').removeClass(data.style.dropAbove).removeClass(data.style.dropBelow);
       },

	    refreshNode: function ($tree, $node) {
	        var $wrapper = $node.children('[data-role="wrapper"]'),
	            $expander = $wrapper.children('[data-role="expander"]'),
	            $spacer = $wrapper.children('[data-role="spacer"]'),
	            $list = $node.children('ul'),
               data = $tree.data(),
	            level = $node.parentsUntil('[data-type="tree"]', 'ul').length;

	        if ($list.length && $list.children().length) {
	            if ($list.is(':visible')) {
	                $expander.empty().append(data.icons.collapse);
	            } else {
	                $expander.empty().append(data.icons.expand);
	            }
	        } else {
	            $expander.empty();
	        }
	        $wrapper.removeClass(data.style.dropAbove).removeClass(data.style.dropBelow);

	        $spacer.css('width', (data.indentation * (level - 1)));
	    }
	},

	public: {
	},

	events: {
	    /**
        * Event fires when the node is dropped.
        * @event nodeDrop
        * @param {object} e - event data
        * @param {string} id - the id of the record
        * @param {object} parentId - the id of the new parend node
        * @param {object} orderNumber - the new order number
        * @example Event.Sample <!-- draggable, droppable, tree -->
        * <div id="tree" data-source="/Locations/Get" data-drag-and-drop="true"></div>
        * <script>
        *     var tree = $('#tree').tree();
        *     tree.on('nodeDrop', function (e, id, parentId, orderNumber) {
        *         var node = tree.getDataById(id),
        *             parent = parentId ? tree.getDataById(parentId) : {};
        *         if (parent.text === 'North America') {
        *             alert('Can\'t add children to North America.');
        *             return false;
        *         } else {
        *             alert(node.text + ' is added to ' + parent.text + ' as ' + orderNumber);
        *             return true;
        *         }
        *     });
        * </script>
        */
	    nodeDrop: function ($tree, id, parentId, orderNumber) {
	        return $tree.triggerHandler('nodeDrop', [id, parentId, orderNumber]);
       }
   },

	configure: function ($tree) {
		$.extend(true, $tree, gj.tree.plugins.dragAndDrop.public);
		if ($tree.data('dragAndDrop') && gj.draggable && gj.droppable) {
			$tree.on('nodeDataBound', function (e, $node) {
				gj.tree.plugins.dragAndDrop.private.nodeDataBound($tree, $node);
			});
		}
	}
};

/** 
* @widget Tree 
* @plugin Lazy Loading
*/
gj.tree.plugins.lazyLoading = {
   config: {
       base: {

           paramNames: {

               /** The name of the parameter that is going to send the parent identificator.
                * Lazy Loading needs to be enabled in order this parameter to be in use.
                * @alias paramNames.parentId
                * @type string
                * @default "parentId"
                */
               parentId: 'parentId'
           },

           /** Enables lazy loading
             * @type Boolean
             * @default false
             * @example Material.Design <!-- tree -->
             * <div id="tree"></div>
             * <script>
             *     $('#tree').tree({
             *         dataSource: '/Locations/LazyGet',
             *         primaryKey: 'id',
             *         lazyLoading: true
             *     });
             * </script>
             */
           lazyLoading: false
       }
   },

   private: {
       nodeDataBound: function ($tree, $node, id, record) {
           var data = $tree.data(),
               $expander = $node.find('> [data-role="wrapper"] > [data-role="expander"]');

           if (record.hasChildren) {
               $expander.empty().append(data.icons.expand);
           }
       },

       createDoneHandler: function ($tree, $node) {
           return function (response) {
               var i, $expander, $list, data = $tree.data();
               if (typeof (response) === 'string' && JSON) {
                   response = JSON.parse(response);
               }
               if (response && response.length) {
                   $list = $node.children('ul');
                   if ($list.length === 0) {
                       $list = $('<ul />').addClass(data.style.list);
                       $node.append($list);
                   }
                   for (i = 0; i < response.length; i++) {
                       $tree.addNode(response[i], $list);
                   }
                   $expander = $node.find('>[data-role="wrapper"]>[data-role="expander"]'),
                   $expander.attr('data-mode', 'open');
                   $expander.empty().append(data.icons.collapse);
                   gj.tree.events.dataBound($tree);
               }
           };
       },

       expand: function ($tree, $node, id) {
           var ajaxOptions, data = $tree.data(), params = {},
               $children = $node.find('>ul>li');

           if (!$children || !$children.length) {
               if (typeof (data.dataSource) === 'string') {
                   params[data.paramNames.parentId] = id;
                   ajaxOptions = { url: data.dataSource, data: params };
                   if ($tree.xhr) {
                       $tree.xhr.abort();
                   }
                   $tree.xhr = $.ajax(ajaxOptions).done(gj.tree.plugins.lazyLoading.private.createDoneHandler($tree, $node)).fail($tree.createErrorHandler());
               }
           }
       }
   },

   public: {},

   events: {},

   configure: function ($tree, fullConfig, clientConfig) {
       if (clientConfig.lazyLoading) {
           $tree.on('nodeDataBound', function (e, $node, id, record) {
               gj.tree.plugins.lazyLoading.private.nodeDataBound($tree, $node, id, record);
           });
           $tree.on('expand', function (e, $node, id) {
               gj.tree.plugins.lazyLoading.private.expand($tree, $node, id);
           });
       }
   }
};

/* global window alert jQuery */
/** 
* @widget Checkbox 
* @plugin Base
*/
gj.checkbox = {
   plugins: {}
};

gj.checkbox.config = {
   base: {
       /** The name of the UI library that is going to be in use. Currently we support only Material Design and Bootstrap. 
        * @additionalinfo The css files for Bootstrap should be manually included to the page if you use bootstrap as uiLibrary.
        * @type string (materialdesign|bootstrap|bootstrap4)
        * @default 'materialdesign'
        * @example Material.Design <!-- checkbox  -->
        * <input type="checkbox" id="checkbox"/><br/><br/>
        * <button onclick="$chkb.state('checked')" class="gj-button-md">Checked</button>
        * <button onclick="$chkb.state('unchecked')" class="gj-button-md">Unchecked</button>
        * <button onclick="$chkb.state('indeterminate')" class="gj-button-md">Indeterminate</button>
        * <button onclick="$chkb.prop('disabled', false)" class="gj-button-md">Enable</button>
        * <button onclick="$chkb.prop('disabled', true)" class="gj-button-md">Disable</button>
        * <script>
        *     var $chkb = $('#checkbox').checkbox({
        *         uiLibrary: 'materialdesign'
        *     });
        * </script>
        * @example Bootstrap.3 <!-- bootstrap, checkbox -->
        * <div class="container-fluid" style="margin-top:10px">
        *     <input type="checkbox" id="checkbox"/><br/><br/>
        *     <button onclick="$chkb.state('checked')" class="btn btn-default">Checked</button>
        *     <button onclick="$chkb.state('unchecked')" class="btn btn-default">Unchecked</button>
        *     <button onclick="$chkb.state('indeterminate')" class="btn btn-default">Indeterminate</button>
        *     <button onclick="$chkb.prop('disabled', false)" class="btn btn-default">Enable</button>
        *     <button onclick="$chkb.prop('disabled', true)" class="btn btn-default">Disable</button>
        * </div>
        * <script>
        *     var $chkb = $('#checkbox').checkbox({
        *         uiLibrary: 'bootstrap'
        *     });
        * </script>
        * @example Bootstrap.4 <!-- bootstrap4, checkbox -->
        * <div class="container-fluid" style="margin-top:10px">
        *     <input type="checkbox" id="checkbox"/><br/><br/>
        *     <button onclick="$chkb.state('checked')" class="btn btn-default">Checked</button>
        *     <button onclick="$chkb.state('unchecked')" class="btn btn-default">Unchecked</button>
        *     <button onclick="$chkb.state('indeterminate')" class="btn btn-default">Indeterminate</button>
        *     <button onclick="$chkb.prop('disabled', false)" class="btn btn-default">Enable</button>
        *     <button onclick="$chkb.prop('disabled', true)" class="btn btn-default">Disable</button>
        * </div>
        * <script>
        *     var $chkb = $('#checkbox').checkbox({
        *         uiLibrary: 'bootstrap4'
        *     });
        * </script>
        */
       uiLibrary: 'materialdesign',
       
       /** The name of the icons library that is going to be in use. Currently we support Material Icons, Font Awesome and Glyphicons.
        * @additionalinfo If you use Bootstrap 3 as uiLibrary, then the iconsLibrary is set to Glyphicons by default.<br/>
        * If you use Material Design as uiLibrary, then the iconsLibrary is set to Material Icons by default.<br/>
        * The css files for Material Icons, Font Awesome or Glyphicons should be manually included to the page where the grid is in use.
        * @type (materialicons|fontawesome|glyphicons)
        * @default 'materialicons'
        * @example Bootstrap.4.FontAwesome <!-- bootstrap4, checkbox, fontawesome -->
        * <div class="container-fluid" style="margin-top:10px">
        *     <input type="checkbox" id="checkbox"/><br/><br/>
        *     <button onclick="$chkb.state('checked')" class="btn btn-default">Checked</button>
        *     <button onclick="$chkb.state('unchecked')" class="btn btn-default">Unchecked</button>
        *     <button onclick="$chkb.state('indeterminate')" class="btn btn-default">Indeterminate</button>
        *     <button onclick="$chkb.prop('disabled', false)" class="btn btn-default">Enable</button>
        *     <button onclick="$chkb.prop('disabled', true)" class="btn btn-default">Disable</button>
        * </div>
        * <script>
        *     var $chkb = $('#checkbox').checkbox({
        *         uiLibrary: 'bootstrap4',
        *         iconsLibrary: 'fontawesome'
        *     });
        * </script>
        */
       iconsLibrary: 'materialicons',

       style: {
           wrapperCssClass: 'gj-checkbox-md',
           spanCssClass: undefined
       }
       
   },

   bootstrap: {
       style: {
           wrapperCssClass: 'gj-checkbox-bootstrap'
       },
       iconsLibrary: 'glyphicons'
   },

   bootstrap4: {
       style: {
           wrapperCssClass: 'gj-checkbox-bootstrap gj-checkbox-bootstrap-4'
       },
       iconsLibrary: 'materialicons'
   },

   materialicons: {
       style: {
           iconsCssClass: 'gj-checkbox-material-icons',
           spanCssClass: 'gj-icon'
       }
   },

   glyphicons: {
       style: {
           iconsCssClass: 'gj-checkbox-glyphicons',
           spanCssClass: ''
       }
   },

   fontawesome: {
       style: {
           iconsCssClass: 'gj-checkbox-fontawesome',
           spanCssClass: 'fa'
       }
   }
};

gj.checkbox.methods = {
   init: function (jsConfig) {
       var $chkb = this;

       gj.widget.prototype.init.call(this, jsConfig, 'checkbox');
       $chkb.attr('data-checkbox', 'true');

       gj.checkbox.methods.initialize($chkb);

       return $chkb;
   },

   initialize: function ($chkb) {
       var data = $chkb.data(), $wrapper, $span;

       if (data.style.wrapperCssClass) {
           $wrapper = $('<label class="' + data.style.wrapperCssClass + ' ' + data.style.iconsCssClass + '"></label>');
           if ($chkb.attr('id')) {
               $wrapper.attr('for', $chkb.attr('id'));
           }
           $chkb.wrap($wrapper);
           $span = $('<span />');
           if (data.style.spanCssClass) {
               $span.addClass(data.style.spanCssClass);
           }
           $chkb.parent().append($span);
       }
   },

   state: function ($chkb, value) {
       if (value) {
           if ('checked' === value) {
               $chkb.prop('indeterminate', false);
               $chkb.prop('checked', true);
           } else if ('unchecked' === value) {
               $chkb.prop('indeterminate', false);
               $chkb.prop('checked', false);
           } else if ('indeterminate' === value) {
               $chkb.prop('checked', true);
               $chkb.prop('indeterminate', true);
           }
           gj.checkbox.events.change($chkb, value);
           return $chkb;
       } else {
           if ($chkb.prop('indeterminate')) {
               value = 'indeterminate';
           } else if ($chkb.prop('checked')) {
               value = 'checked';
           } else {
               value = 'unchecked';
           }
           return value;
       }
   },

   toggle: function ($chkb) {
       if ($chkb.state() == 'checked') {
           $chkb.state('unchecked');
       } else {
           $chkb.state('checked');
       }
       return $chkb;
   },

   destroy: function ($chkb) {
       if ($chkb.attr('data-checkbox') === 'true') {
           $chkb.removeData();
           $chkb.removeAttr('data-guid');
           $chkb.removeAttr('data-checkbox');
           $chkb.off();
           $chkb.next('span').remove();
           $chkb.unwrap();
       }
       return $chkb;
   }
};

gj.checkbox.events = {
   /**
    * Triggered when the state of the checkbox is changed
    *
    * @event change
    * @param {object} e - event data
    * @param {string} state - the data of the checkbox
    * @example sample <!-- checkbox -->
    * <input type="checkbox" id="checkbox"/>
    * <script>
    *     var chkb = $('#checkbox').checkbox({
    *         change: function (e) {
    *             alert('State: ' + chkb.state());
    *         }
    *     });
    * </script>
    */
   change: function ($chkb, state) {
       return $chkb.triggerHandler('change', [state]);
   }
};


gj.checkbox.widget = function ($element, jsConfig) {
   var self = this,
       methods = gj.checkbox.methods;

   /** Toogle the state of the checkbox.
    * @method
    * @fires change
    * @return checkbox as jquery object
    * @example sample <!-- checkbox -->
    * <button onclick="$chkb.toggle()" class="gj-button-md">toggle</button>
    * <hr/>
    * <input type="checkbox" id="checkbox"/>
    * <script>
    *     var $chkb = $('#checkbox').checkbox();
    * </script>
    */
   self.toggle = function () {
       return methods.toggle(this);
   };

   /** Return state or set state if you pass parameter.
    * @method
    * @fires change
    * @param {string} value - State of the checkbox. Accept only checked, unchecked or indeterminate as values.
    * @return checked|unchecked|indeterminate|checkbox as jquery object
    * @example sample <!-- checkbox -->
    * <button onclick="$chkb.state('checked')" class="gj-button-md">Set to checked</button>
    * <button onclick="$chkb.state('unchecked')" class="gj-button-md">Set to unchecked</button>
    * <button onclick="$chkb.state('indeterminate')" class="gj-button-md">Set to indeterminate</button>
    * <button onclick="alert($chkb.state())" class="gj-button-md">Get state</button>
    * <hr/>
    * <input type="checkbox" id="checkbox"/>
    * <script>
    *     var $chkb = $('#checkbox').checkbox();
    * </script>
    */
   self.state = function (value) {
       return methods.state(this, value);
   };

   /** Remove checkbox functionality from the element.
    * @method
    * @return checkbox as jquery object
    * @example sample <!-- checkbox -->
    * <button onclick="$chkb.destroy()" class="gj-button-md">Destroy</button>
    * <input type="checkbox" id="checkbox"/>
    * <script>
    *     var $chkb = $('#checkbox').checkbox();
    * </script>
    */
   self.destroy = function () {
       return methods.destroy(this);
   };

   $.extend($element, self);
   if ('true' !== $element.attr('data-checkbox')) {
       methods.init.call($element, jsConfig);
   }

   return $element;
};

gj.checkbox.widget.prototype = new gj.widget();
gj.checkbox.widget.constructor = gj.checkbox.widget;

(function ($) {
   $.fn.checkbox = function (method) {
       var $widget;
       if (this && this.length) {
           if (typeof method === 'object' || !method) {
               return new gj.checkbox.widget(this, method);
           } else {
               $widget = new gj.checkbox.widget(this, null);
               if ($widget[method]) {
                   return $widget[method].apply(this, Array.prototype.slice.call(arguments, 1));
               } else {
                   throw 'Method ' + method + ' does not exist.';
               }
           }
       }
   };
})(jQuery);
/* global window alert jQuery */
/** 
* @widget Editor
* @plugin Base
*/
gj.editor = {
   plugins: {},
   messages: {}
};

gj.editor.config = {
   base: {

       /** The height of the editor. Numeric values are treated as pixels.
        * @type number|string
        * @default 300
        * @example sample <!-- editor -->
        * <textarea id="editor"></textarea>
        * <script>
        *     $('#editor').editor({ height: 400 });
        * </script>
        */
       height: 300,

       /** The width of the editor. Numeric values are treated as pixels.
        * @type number|string
        * @default undefined
        * @example JS <!-- editor -->
        * <textarea id="editor"></textarea>
        * <script>
        *     $('#editor').editor({ width: 900 });
        * </script>
        * @example HTML <!-- editor -->
        * <div id="editor" width="900"></div>
        * <script>
        *     $('#editor').editor();
        * </script>
        */
       width: undefined,

       /** The name of the UI library that is going to be in use. Currently we support only Material Design and Bootstrap. 
        * @additionalinfo The css files for Bootstrap should be manually included to the page if you use bootstrap as uiLibrary.
        * @type string (materialdesign|bootstrap|bootstrap4)
        * @default 'materialdesign'
        * @example Material.Design <!-- editor, materialicons  -->
        * <textarea id="editor"></textarea>
        * <script>
        *     $('#editor').editor({ uiLibrary: 'materialdesign' });
        * </script>
        * @example Bootstrap.3 <!-- bootstrap, editor -->
        * <textarea id="editor"></textarea>
        * <script>
        *     $('#editor').editor({
        *         uiLibrary: 'bootstrap'
        *     });
        * </script>
        * @example Bootstrap.4 <!-- bootstrap4, editor -->
        * <textarea id="editor"></textarea>
        * <script>
        *     $('#editor').editor({
        *         uiLibrary: 'bootstrap4'
        *     });
        * </script>
        */
       uiLibrary: 'materialdesign',

       /** The name of the icons library that is going to be in use. Currently we support Material Icons and Font Awesome.
        * @additionalinfo If you use Bootstrap as uiLibrary, then the iconsLibrary is set to font awesome by default.<br/>
        * If you use Material Design as uiLibrary, then the iconsLibrary is set to Material Icons by default.<br/>
        * The css files for Material Icons or Font Awesome should be manually included to the page where the grid is in use.
        * @type (materialicons|fontawesome)
        * @default 'materialicons'
        * @example Bootstrap.4.FontAwesome <!-- bootstrap4, fontawesome, editor -->
        * <textarea id="editor"></textarea>
        * <script>
        *     $('#editor').editor({
        *         uiLibrary: 'bootstrap4',
        *         iconsLibrary: 'fontawesome'
        *     });
        * </script>
        * @example Bootstrap.3.FontAwesome <!-- bootstrap, fontawesome, editor -->
        * <textarea id="editor"></textarea>
        * <script>
        *     $('#editor').editor({
        *         uiLibrary: 'bootstrap',
        *         iconsLibrary: 'fontawesome'
        *     });
        * </script>
        */
       iconsLibrary: 'materialicons',

       /** The language that needs to be in use.
        * @type string
        * @default 'en-us'
        * @example French <!-- editor -->
        * <script src="../../dist/modular/editor/js/messages/messages.fr-fr.js"></script>
        * <div id="editor">Hover buttons in the toolbar in order to see localized tooltips</div>
        * <script>
        *     $("#editor").editor({
        *         locale: 'fr-fr'
        *     });
        * </script>
        * @example German <!-- editor -->
        * <script src="../../dist/modular/editor/js/messages/messages.de-de.js"></script>
        * <div id="editor">Hover <b><u>buttons</u></b> in the toolbar in order to see localized tooltips</div>
        * <script>
        *     $("#editor").editor({
        *         locale: 'de-de'
        *     });
        * </script>
        */
       locale: 'en-us',

       buttons: undefined,

       style: {
           wrapper: 'gj-editor gj-editor-md',
           buttonsGroup: 'gj-button-md-group',
           button: 'gj-button-md',
           buttonActive: 'active'
       }
   },

   bootstrap: {
       style: {
           wrapper: 'gj-editor gj-editor-bootstrap',
           buttonsGroup: 'btn-group',
           button: 'btn btn-default gj-cursor-pointer',
           buttonActive: 'active'
       }
   },

   bootstrap4: {
       style: {
           wrapper: 'gj-editor gj-editor-bootstrap',
           buttonsGroup: 'btn-group',
           button: 'btn btn-outline-secondary gj-cursor-pointer',
           buttonActive: 'active'
       }
   },

   materialicons: {
       icons: {
           bold: '<i class="gj-icon bold" />',
           italic: '<i class="gj-icon italic" />',
           strikethrough: '<i class="gj-icon strikethrough" />',
           underline: '<i class="gj-icon underlined" />',

           listBulleted: '<i class="gj-icon list-bulleted" />',
           listNumbered: '<i class="gj-icon list-numbered" />',
           indentDecrease: '<i class="gj-icon indent-decrease" />',
           indentIncrease: '<i class="gj-icon indent-increase" />',

           alignLeft: '<i class="gj-icon align-left" />',
           alignCenter: '<i class="gj-icon align-center" />',
           alignRight: '<i class="gj-icon align-right" />',
           alignJustify: '<i class="gj-icon align-justify" />',

           undo: '<i class="gj-icon undo" />',
           redo: '<i class="gj-icon redo" />'
       }
   },

   fontawesome: {
       icons: {
           bold: '<i class="fa fa-bold" aria-hidden="true"></i>',
           italic: '<i class="fa fa-italic" aria-hidden="true"></i>',
           strikethrough: '<i class="fa fa-strikethrough" aria-hidden="true"></i>',
           underline: '<i class="fa fa-underline" aria-hidden="true"></i>',

           listBulleted: '<i class="fa fa-list-ul" aria-hidden="true"></i>',
           listNumbered: '<i class="fa fa-list-ol" aria-hidden="true"></i>',
           indentDecrease: '<i class="fa fa-indent" aria-hidden="true"></i>',
           indentIncrease: '<i class="fa fa-outdent" aria-hidden="true"></i>',

           alignLeft: '<i class="fa fa-align-left" aria-hidden="true"></i>',
           alignCenter: '<i class="fa fa-align-center" aria-hidden="true"></i>',
           alignRight: '<i class="fa fa-align-right" aria-hidden="true"></i>',
           alignJustify: '<i class="fa fa-align-justify" aria-hidden="true"></i>',

           undo: '<i class="fa fa-undo" aria-hidden="true"></i>',
           redo: '<i class="fa fa-repeat" aria-hidden="true"></i>'
       }
   }
};

gj.editor.methods = {
   init: function (jsConfig) {
       gj.widget.prototype.init.call(this, jsConfig, 'editor');
       this.attr('data-editor', 'true');
       gj.editor.methods.initialize(this);
       return this;
   },

   initialize: function ($editor) {
       var self = this, data = $editor.data(),
           $group, $btn, wrapper, $body, $toolbar;

       $editor.hide();

       if ($editor[0].parentElement.attributes.role !== 'wrapper') {
           wrapper = document.createElement('div');
           wrapper.setAttribute('role', 'wrapper');
           $editor[0].parentNode.insertBefore(wrapper, $editor[0]);
           wrapper.appendChild($editor[0]);
       }

       gj.editor.methods.localization(data);
       $(wrapper).addClass(data.style.wrapper);
       if (data.width) {
           $(wrapper).width(data.width);
       }

       $body = $(wrapper).children('div[role="body"]');
       if ($body.length === 0) {
           $body = $('<div role="body"></div>');
           $(wrapper).append($body);
           if ($editor[0].innerText) {
               $body[0].innerHTML = $editor[0].innerText;
           }
       }
       $body.attr('contenteditable', true);
       $body.on('keydown', function (e) {
           var key = event.keyCode || event.charCode;
           if (gj.editor.events.changing($editor) === false && key !== 8 && key !== 46) {
               e.preventDefault();
           }
       });
       $body.on('mouseup keyup mouseout cut paste', function (e) {
           self.updateToolbar($editor, $toolbar);
           gj.editor.events.changed($editor);
           $editor.html($body.html());
       });

       $toolbar = $(wrapper).children('div[role="toolbar"]');
       if ($toolbar.length === 0) {
           $toolbar = $('<div role="toolbar"></div>');
           $body.before($toolbar);

           for (var group in data.buttons) {
               $group = $('<div />').addClass(data.style.buttonsGroup);
               for (var btn in data.buttons[group]) {
                   $btn = $(data.buttons[group][btn]);
                   $btn.on('click', function () {
                       gj.editor.methods.executeCmd($editor, $body, $toolbar, $(this));
                   });
                   $group.append($btn);
               }
               $toolbar.append($group);
           }
       }

       $body.height(data.height - gj.core.height($toolbar[0], true));
   },

   localization: function (data) {
       var msg = gj.editor.messages[data.locale];
       if (typeof (data.buttons) === 'undefined') {
           data.buttons = [
               [
                   '<button type="button" class="' + data.style.button + '" title="' + msg.bold + '" role="bold">' + data.icons.bold + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.italic + '" role="italic">' + data.icons.italic + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.strikethrough + '" role="strikethrough">' + data.icons.strikethrough + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.underline + '" role="underline">' + data.icons.underline + '</button>'
               ],
               [
                   '<button type="button" class="' + data.style.button + '" title="' + msg.listBulleted + '" role="insertunorderedlist">' + data.icons.listBulleted + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.listNumbered + '" role="insertorderedlist">' + data.icons.listNumbered + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.indentDecrease + '" role="outdent">' + data.icons.indentDecrease + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.indentIncrease + '" role="indent">' + data.icons.indentIncrease + '</button>'
               ],
               [
                   '<button type="button" class="' + data.style.button + '" title="' + msg.alignLeft + '" role="justifyleft">' + data.icons.alignLeft + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.alignCenter + '" role="justifycenter">' + data.icons.alignCenter + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.alignRight + '" role="justifyright">' + data.icons.alignRight + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.alignJustify + '" role="justifyfull">' + data.icons.alignJustify + '</button>'
               ],
               [
                   '<button type="button" class="' + data.style.button + '" title="' + msg.undo + '" role="undo">' + data.icons.undo + '</button>',
                   '<button type="button" class="' + data.style.button + '" title="' + msg.redo + '" role="redo">' + data.icons.redo + '</button>'
               ]
           ];
       }
   },

   updateToolbar: function ($editor, $toolbar) {
       var data = $editor.data();
       $buttons = $toolbar.find('[role]').each(function() {
           var $btn = $(this),
               cmd = $btn.attr('role');

           if (cmd && document.queryCommandEnabled(cmd) && document.queryCommandValue(cmd) === "true") {
               $btn.addClass(data.style.buttonActive);
           } else {
               $btn.removeClass(data.style.buttonActive);
           }
       });
   },

   executeCmd: function ($editor, $body, $toolbar, $btn) {
       $body.focus();
       document.execCommand($btn.attr('role'), false);
       gj.editor.methods.updateToolbar($editor, $toolbar);
   },

   content: function ($editor, html) {
       var $body = $editor.parent().children('div[role="body"]');
       if (typeof (html) === "undefined") {
           return $body.html();
       } else {
           return $body.html(html);
       }
   },

   destroy: function ($editor) {
       var $wrapper;
       if ($editor.attr('data-editor') === 'true') {
           $wrapper = $editor.parent();
           $wrapper.children('div[role="body"]').remove();
           $wrapper.children('div[role="toolbar"]').remove();
           $editor.unwrap();
           $editor.removeData();
           $editor.removeAttr('data-guid');
           $editor.removeAttr('data-editor');
           $editor.off();
           $editor.show();
       }
       return $editor;
   }
};

gj.editor.events = {

   /**
    * Event fires before change of text in the editor.
    *
    * @event changing
    * @param {object} e - event data
    * @example MaxLength <!-- editor -->
    * <textarea id="editor"></textarea>
    * <script>
    *     var editor = $('#editor').editor();
    *     editor.on('changing', function (e) {
    *         return $(e.target).text().length < 3;
    *     });
    * </script>
    */
   changing: function ($editor) {
       return $editor.triggerHandler('changing');
   },

   /**
    * Event fires after change of text in the editor.
    *
    * @event changed
    * @param {object} e - event data
    * @example sample <!-- editor -->
    * <textarea id="editor"></textarea>
    * <script>
    *     $('#editor').editor({
    *         changed: function (e) {
    *             alert('changed is fired');
    *         }
    *     });
    * </script>
    */
   changed: function ($editor) {
       return $editor.triggerHandler('changed');
   }
};

gj.editor.widget = function ($element, jsConfig) {
   var self = this,
       methods = gj.editor.methods;

   /** Get or set html content in the body.
    * @method
    * @param {string} html - The html content that needs to be set.
    * @return string | editor
    * @example Get <!-- editor, materialicons -->
    * <button class="gj-button-md" onclick="alert($editor.content())">Get Content</button>
    * <hr/>
    * <div id="editor">My <b>content</b>.</div>
    * <script>
    *     var $editor = $('#editor').editor();
    * </script>
    * @example Set <!-- editor, materialicons -->
    * <button class="gj-button-md" onclick="$editor.content('<h1>new value</h1>')">Set Content</button>
    * <hr/>
    * <textarea id="editor"></textarea>
    * <script>
    *     var $editor = $('#editor').editor();
    * </script>
    */
   self.content = function (html) {
       return methods.content(this, html);
   };

   /** Remove editor functionality from the element.
    * @method
    * @return jquery element
    * @example sample <!-- editor, materialicons -->
    * <button class="gj-button-md" onclick="editor.destroy()">Destroy</button><br/>
    * <textarea id="editor"></textarea>
    * <script>
    *     var editor = $('#editor').editor();
    * </script>
    */
   self.destroy = function () {
       return methods.destroy(this);
   };

   $.extend($element, self);
   if ('true' !== $element.attr('data-editor')) {
       methods.init.call($element, jsConfig);
   }

   return $element;
};

gj.editor.widget.prototype = new gj.widget();
gj.editor.widget.constructor = gj.editor.widget;

(function ($) {
   $.fn.editor = function (method) {
       var $widget;
       if (this && this.length) {
           if (typeof method === 'object' || !method) {
               return new gj.editor.widget(this, method);
           } else {
               $widget = new gj.editor.widget(this, null);
               if ($widget[method]) {
                   return $widget[method].apply(this, Array.prototype.slice.call(arguments, 1));
               } else {
                   throw 'Method ' + method + ' does not exist.';
               }
           }
       }
   };
})(jQuery);
gj.editor.messages['en-us'] = {
   bold: 'Bold',
   italic: 'Italic',
   strikethrough: 'Strikethrough',
   underline: 'Underline',
   listBulleted: 'List Bulleted',
   listNumbered: 'List Numbered',
   indentDecrease: 'Indent Decrease',
   indentIncrease: 'Indent Increase',
   alignLeft: 'Align Left',
   alignCenter: 'Align Center',
   alignRight: 'Align Right',
   alignJustify: 'Align Justify',
   undo: 'Undo',
   redo: 'Redo'
};
