if (typeof (SAPO) === 'undefined') {
    window.SAPO = {};
}

/*
{
    domain: "adw.sapo.pt",
    begin: 1320141600000,
    end: 1322611200000,
    tseries: [{
        name: "Serie A",
        metric: "n_metric_a",
        agg: "sum",
        ds: "gauge",
        period: "day",
        diff: false,
        filter: {
            s_foo: "bar"
        },
        onload: function () {}
    }, {
        name: "Serie B",
        metric: "n_metric_b",
        agg: "sum",
        ds: "gauge",
        period: "day",
        diff: false,
        onload: function () {}
    }],
    summary: [{
        name: "summary A",
        metric: "n_metric_a",
        filter: {
            s_foo: "bar"
        },
        group: [{
            agg: "sum",
            field: "s_foo",
            sort: "desc"
        }, {
            agg: "sum",
            field: "s_bar",
            sort: "desc"
        }, ]
        onload: function () {}
    }, {
        name: "summary A",
        metric: "n_metric_a",
        agg: "sum",
        filter: {
            s_foo: "bar"
        },
        group: [{
            agg: "sum",
            field: "s_foo",
            sort: "desc"
        }, {
            agg: "sum",
            field: "s_foo",
            sort: "desc"
        }, ]
        onload: function () {}
    }]
}
*/

if (typeof (SAPO.Component) == 'undefined') {
    SAPO.namespace('Component');
}

SAPO.Component.Kpi = function () {
    this.init(arguments[0] || {});
}

SAPO.Component.Kpi.prototype = {

    tseriesEndpoint: 'http://services.bk.sapo.pt/Analytics/KPI/Timeserie',
    summaryEndpoint: 'http://services.bk.sapo.pt/Analytics/KPI/Summary',
    collection: 'generic',
	ssoToken: '',


    init: function () {
        var options = SAPO.extendObj({
            collection: 'generic',
            debug: false
        }, arguments[0] || {});

        if (!this.isBlank(options.collection)) {
            this.collection = options.collection;
        }
        if (!this.isBlank(options.ssoToken)) {
            this.ssoToken = options.ssoToken;
        }    
    },

    requestTimeSeriesData: function (dataOptions, callback) {

        'use strict';

        for (var i = 0; i < dataOptions.tseries.length; i++) {

            var tserieOptions = dataOptions.tseries[i];

            var ds = (typeof tserieOptions.ds == "undefined") ? 'gauge' : tserieOptions.ds,
                diff = (typeof tserieOptions.diff == "undefined") ? false : tserieOptions.diff,
                filters = tserieOptions.filter,
                tsUrl = this.tseriesEndpoint + '?';

            tsUrl += 'collection=' + this.collection ;
			tsUrl += '&domain=' + dataOptions.domain;
            tsUrl += '&metric=' + tserieOptions.metric;

            if (dataOptions.last) {
                tsUrl += '&last=true';
            } else {
                tsUrl += '&begin=' + this.isoDateString(dataOptions.begin);
                tsUrl += '&end=' + this.isoDateString(dataOptions.end);
            }



            tsUrl += '&agg=' + tserieOptions.agg;
            tsUrl += '&ds=' + ds;
            tsUrl += '&diff=' + diff;

            if (!this.isBlank(tserieOptions.period)) {
                tsUrl += '&group.period=' + tserieOptions.period;
            }

            if (!this.isBlank(this.ssoToken)) {
                tsUrl += '&SSOToken=' + this.ssoToken;
            }

            if (filters) {
                for (var f in filters) {
                    if ( (this.isBlank(filters[f])) || ("()" == filters[f]) ) {
                        continue;
                    }
 
                    tsUrl += '&f=' + f + ':' + filters[f];
                }
            }

            var ajaxSettings = {
                dataType: 'jsonp',
                success: function (response) {
                    if (typeof (tserieOptions.onload) === 'function') {
                        tserieOptions.onload(response);
                    }

                    callback(response, tserieOptions);
                }
            };

            jQuery.ajax(tsUrl, ajaxSettings);
        }
    },

    requestSummaryData: function (dataOptions, callback) {

        for (var i = 0; i < dataOptions.summary.length; i++) {

            var summaryOptions = dataOptions.summary[i],
                filters = summaryOptions.filter,
                groups = summaryOptions.group,
                summaryUrl = this.summaryEndpoint + '?';


            summaryUrl += 'collection=' + this.collection ;
			summaryUrl += '&domain=' + dataOptions.domain;
            summaryUrl += '&metric=' + summaryOptions.metric;

            if (dataOptions.last) {
                summaryUrl += '&last=true';
            } else {
                summaryUrl += '&begin=' + this.isoDateString(dataOptions.begin);
                summaryUrl += '&end=' + this.isoDateString(dataOptions.end);
            }
            if (filters) {
                for (var f in filters) {
                    if ( (this.isBlank(filters[f])) || ("()" == filters[f]) ) {
                        continue;
                    }
                    summaryUrl += '&f=' + f + ':' + filters[f];
                }
            }

            if (groups) {
                for (var i = 0; i < groups.length; i++) {
                    var g = groups[i],
                        summary_group = g.agg + ':' + g.field;

                    if (g.sort) {
                        summary_group += ':' + g.sort;
                    }

                    if (g.limit) {
                        summary_group += ':' + g.limit;
                    }
                    if (g.offset) {
                        summary_group += ':' + g.offset;
                    }

                    summaryUrl += '&group=' + summary_group;
                }
            }

            if (!this.isBlank(this.ssoToken)) {
                summaryUrl += '&SSOToken=' + this.ssoToken;
            }

            var ajaxSettings = {
                dataType: 'jsonp',
                success: function (response) {

                    if (typeof (summaryOptions.onload) === 'function') {
                        summaryOptions.onload(response);
                    }
                    callback(response, summaryOptions);
                }
            };
            jQuery.ajax(summaryUrl, ajaxSettings);
        }
    },

    isoDateString: function (d) {
        function pad(n) {
            return n < 10 ? '0' + n : n
        }

        return d.getUTCFullYear() + '-' + pad(d.getUTCMonth() + 1) + '-' + pad(d.getUTCDate()) + 'T' + pad(d.getUTCHours()) + ':' + pad(d.getUTCMinutes()) + ':' + pad(d.getUTCSeconds()) + '.' + d.getMilliseconds() + 'Z';
    },

    isBlank: function (str) {
        return (!str || /^\s*$/.test(str));
    }
}
