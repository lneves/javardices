var kpi = new SAPO.Component.Kpi({
	collection: 'realtime'
});

var TIME_INTERVAL = 900000; //15 minutes

var QUEUE_PREFIX = "queue://";
var AGENT_PREFIX = "agent://";
var TOPIC_PREFIX = "topic://";

var SPARKLINE_ARGUMENTS= 
{
	width:'200px', 
	height:'90px', 
	lineColor:'#336699', 
	fillColor:'', 
	lineWidth:'3', 
	spotColor:'#990000', 
	minSpotColor:'#990000', 
	maxSpotColor:'#990000'
};

var data = new Array();

function main(){

	var f_agents = function() 
	{
		setAgentsInfo(
		{
			domain: 'broker.bk.sapo.pt',
			last: true, 
			tseries: [
			{
				name: 'Agents',
				metric: 'n_status',
				agg: 'sum'
			}]
		},
		"#status-box",
		"#alertAgentsDown");
	}

	refreshAllGraphsMain();
	setInterval(refreshAllGraphsMain, 5000);
	f_agents();
	setInterval(f_agents, 5100);
	queuesInformation();
	setInterval(queuesInformation, 5200);
}

/*
* GRAPHS - MAIN
*/

function refreshAllGraphsMain()
{
	var time = new Date().getTime();
	//graph queue size
	refreshGraph(
	{
		domain: 'broker.bk.sapo.pt',
		begin: new Date(time - TIME_INTERVAL), 
		end: new Date(time), 
		tseries: [
		{
			name: 'Total Queue Size',
			metric: 'n_queue_size',
			agg: 'sum'
		}]
	},
	"#img_queue_size_rate", 
	"#queue_size_rate", 
	undefined
	)
	//graph input
	refreshGraph( 	
	{
		domain: 'broker.bk.sapo.pt',
		begin: new Date(time - TIME_INTERVAL), 
		end: new Date(time), 
		tseries: [
		{
			name: 'Total Queue Size',
			metric: 'n_input_rate',
			agg: 'sum'
		}]
	},
	"#img_input_rate", 
	"#count_input_rate", 
	"m/s"
	)
	//graph output
	refreshGraph( 	
	{
		domain: 'broker.bk.sapo.pt',
		begin: new Date(time - TIME_INTERVAL), 
		end: new Date(time), 
		tseries: [
		{
			name: 'Total Queue Size',
			metric: 'n_output_rate',
			agg: 'sum'
		}]
	},
	"#img_output_rate", 
	"#count_output_rate", 
	"m/s"
	)
	//graph errors
	refreshGraph( 	
	{
		domain: 'broker.bk.sapo.pt',
		begin: new Date(time - TIME_INTERVAL), 
		end: new Date(time), 
		tseries: [
		{
			name: 'Total Queue Size',
			metric: 'n_error_rate',
			agg: 'sum'
		}]
	},
	"#img_error_rate", 
	"#count_error_rate", 
	"e/s"
	)

}

function refreshGraph(query, graphPlaceHolder, legendPlaceHolder, unit)
{
	kpi.requestTimeSeriesData(query, function(response, tserieOptions) 
	{
		var response_array = getArrayFromResponseData(response);

		drawGraph(
			response_array.values, 
			graphPlaceHolder, 
			legendPlaceHolder, 
			response_array.values[response_array.values.length - 1], 
			response_array.min, 
			response_array.max,
			unit 
			);

	});

}

function drawGraph(values, graphPlaceHolder, legendPlaceHolder, latest, min, max, unit)
{
	var s_unit = "";
	if(typeof(unit)!=undefined && unit!=null)
	{
		s_unit="&nbsp;" + unit;
	}

	$(graphPlaceHolder).sparkline(values, SPARKLINE_ARGUMENTS);
	$(legendPlaceHolder).html("<p><span class='mvalue-latest'>" + latest + s_unit + "</span></p><p><span class='mlabel'>Min: </span><span class='mvalue'>" + min + "</span>;<span class='mlabel'> Max: </span><span class='mvalue'>" + max + "</span></p>");

}
/*
 * Transform the response into a single array
 */
 function getArrayFromResponseData(response)
 {
	
	var max = 0, min = 0;
	var total_values = [];

	for (var i in response.data) 
	{
		response.data[i][1] > max ? max = response.data[i][1] : max;
		response.data[i][1] < min || min == 0 ? min = response.data[i][1] : min;
		total_values.push(response.data[i][1]);
	}

	return {
		min: min,
		max: max,
		values: total_values
	};
 }

 function setAgentsInfo(agentQuery, pannel, label)
 {
	kpi.requestTimeSeriesData(agentQuery, function(response, tserieOptions) 
	{

		var agentsDown = response.data[0][1];

		if(agentsDown == 0) 
		{
			$(pannel).hide();
		}
		else
		{
			var msg = "";
			if(agentsDown != 0)
				msg += agentsDown + (agentsDown == 1 ? " agent" : " agents") + " down!\n";
			
			$(label).text(msg);
			$(pannel).show();
		}
	});
 }

/*
* QUEUEs in MAIN
*/
function queuesInformation()
{
	var query = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_queue_size',
			group: [{ 
				agg: "sum",
				field: "s_destination_name", 
				sort: "desc",
				limit: 10
			}]
		}]	
	};

	kpi.requestSummaryData(query, function(response, tserieOptions){
		setQueueInfo(response, $('#queue_size'));
	});

}

// function errorsInformation()
// {
// 	var query = {
// 			domain: 'broker.bk.sapo.pt',
// 			last: true, 
// 			summary: [{
// 				name: 'Error Rate',
// 				metric: 'n_error_rate',
// 				group: [{ 
// 					agg: "sum",
// 					field: "s_destination_name", 
// 					sort: "desc",
// 					limit: 10
// 				}],
// 			}]	
// 		};
// 	kpi.requestSummaryData(query, function(response, tserieOptions){
// 		console.log(response);
// 		setErrorInfo(response, $('#errors'));
// 	});
// }

var previousAllQueuesGeneralInfo = new Object();
var previousGeneralQueueInfo = new Object();
var previousQueueInfo = new Object();


function setQueueInfo(queueInfo, panel)
{
	var newContent = "";

	if (queueInfo == unde)
	{
		newContent = "<td class=\"oddrow\" colspan=\"3\">No information available.</td>";
	}
	else
	{
		var group = queueInfo.group.s_destination_name;
		var keys = [];
		for(var k in group)
		{
			keys.push(k);
		}

		var content = "";

		for(var i = 0; i != keys.length; ++i)
		{
			var queueName = removePrefix(keys[i], QUEUE_PREFIX);
			var encQueueName = encodeURIComponent(queueName);
			var strCount = group[keys[i]];
			var count = parseFloat(strCount);
			
			var previousValue = previousQueueInfo[queueName];
			var pic = getLocalPic(previousValue, count);
			previousQueueInfo[queueName] = count;
			
			var rowClass =  ( ((i+1)%2) == 0) ? "evenrow" : "oddrow";
			content = content + "<tr class=\"" + rowClass +"\"><td><a href='./queue.html?queuename="+encQueueName+"'>"+ queueName+ "</a></td><td class=\"countrow\">" +  count + "</td><td><img src='"+ pic + "' /></td></tr>";

		}

		newContent = content;
	}
	panel.html(newContent);
}

// function setErrorInfo(errorInfo, panel)
// {
// 	var newContent = "";
// 	if (errorInfo.length == 0)
// 	{
//         	newContent = "<td class=\"oddrow\" colspan=\"3\">No information available.</td>";
//   	}
// 	else
// 	{
// 		var group = errorInfo.group.s_destination_name;
// 		var keys = [];
// 		for(var k in group)
// 		{
// 			keys.push(k);
// 		}
// 		for(var i = 0; i != errorInfo.length; ++i)
// 		{
// 			var shortMessage = group[keys[i]].shortMessage;
// 			var encShortMessage = encodeURIComponent(shortMessage);
// 			var count = errorInfo[i].count;
// 			var previousValue = previousSysMsgInfo[shortMessage];
// 			var pic = getLocalPic(previousValue, count);
// 			previousSysMsgInfo[shortMessage] = count;
// 			var rowClass =  ( ((i+1)%2) == 0) ? "evenrow" : "oddrow";
// 			newContent = newContent + "<tr class=\"" + rowClass +"\"><td><a href='./faulttype.html?type="+encShortMessage+"'>"+ shortMessage+ "</a></td><td class=\"countrow\">" +  count + "</td><td><img src='"+ pic + "' /></td></tr>";
// 		}
// 	}
// 	panel.html(newContent);
// }


//
// QUEUE PAGE
//
function queueMonitorizationInit() 
{
	var queueName = SAPO.Utility.Url.getQueryString()['queuename'];
	var qnPanel =  jQuery('#queue_name'); 

	var encQueueName = encodeURIComponent(queueName);

	var countPanel = jQuery('#queue_msg_count');

	if (queueName == undefined)
	{
		qnPanel.html("<b>Queue name not specified</b>");
		return;
	}
	qnPanel.text(queueName);

	var f_rates_all = function() {

		var time = new Date().getTime();

		refreshGraph(
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_queue_size',
				agg: 'sum',
				filter: {
					s_destination_name:"\"" + QUEUE_PREFIX + queueName + "\""
				}
			}]
		},
		"#img_queue_size_rate", 
		"#queue_size_rate", 
		undefined
		);

		refreshGraph(
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_output_rate',
				agg: 'sum',
				filter: {
					s_destination_name:"\"" + QUEUE_PREFIX + queueName + "\""
				}
			}]
		},
		"#img_input_rate", 
		"#count_input_rate", 
		"m/s"
		);

		refreshGraph(
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_output_rate',
				agg: 'sum',
				filter: {
					s_destination_name:"\"" + QUEUE_PREFIX + queueName + "\""
				}
			}]
		},
		"#img_output_rate", 
		"#count_output_rate", 
		"m/s"
		);
	}

	f_rates_all();
	setInterval(f_rates_all, 5000);
	getGeneralQueueInfo();
	setInterval(getGeneralQueueInfo, 6000);

}

function setGeneralQueueInfo(queueGeneralInfo,  panel)
{
	var count = 0;
	var newContent = "";
	if (queueGeneralInfo == undefined)
	{
		newContent = "<p>No information available.</P>";
	}
	else
	{	
		var k = 0;
		for(var i in queueGeneralInfo)
		{
			var agentname = i;		

			var prevQueueInfo = previousGeneralQueueInfo[i];

			if( prevQueueInfo === undefined)
			{	
				prevQueueInfo = new Object();
				previousGeneralQueueInfo[agentname] = prevQueueInfo;
			}
			
			var agentCount = parseFloat(queueGeneralInfo[i].queueSize);
			var pic = getLocalPic(prevQueueInfo.queueSize, agentCount);
			prevQueueInfo.queueSize = agentCount;
			
			var rowClass =  ++k % 2 == 0 ? "evenrow" : "oddrow";

			newContent = newContent + "<tr class=\"" + rowClass +"\"><td><a href='./agent.html?agentname="+ agentname+ "'>" + queueGeneralInfo[i].agentName + "</a></td><td>" + agentCount + "</td><td><img src='" + pic + "' /></td>";

			var inputRate = round(parseFloat(queueGeneralInfo[i].inputRate));
			newContent = newContent + "<td style='padding-left:2em'>" + inputRate + "</td>";
			
			var outputRate = round(parseFloat(queueGeneralInfo[i].outputRate));
			newContent = newContent + "<td style='padding-left:2em'>" + outputRate + "</td>";

			var redeliveredRate = round(parseFloat(queueGeneralInfo[i].redeliveredRate));
			newContent = newContent + "<td style='padding-left:2em'>" + redeliveredRate + "</td>";

			newContent = newContent + "<td>" + parseFloat(queueGeneralInfo[i].subscriptions) + "</td></tr>";
		}
	}

	panel.html(newContent);
}

function getGeneralQueueInfo(){

	var auxResp = {};
	var queueName = SAPO.Utility.Url.getQueryString()['queuename'];
	var panel = $('#general_queue_information');

	var querySize = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_queue_size',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
			filter: {
				s_subject_type:'queue' ,
				s_destination_name:"\"" + QUEUE_PREFIX + queueName + "\""
			}
		}]
	};

	var queryInput = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_input_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
			filter: {
				s_subject_type:'queue' ,
				s_destination_name:"\"" + QUEUE_PREFIX + queueName + "\""
			}
		}]
	};

	var queryOutput = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_output_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
			filter: {
				s_subject_type:'queue' ,
				s_destination_name:"\"" + QUEUE_PREFIX + queueName + "\""
			}
		}]
	};

	var queryRedelivery = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_redelivered_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
			filter: {
				s_subject_type:'queue' ,
				s_destination_name:"\"" + QUEUE_PREFIX + queueName + "\""
			}
		}]
	};

	var querySubscriptions = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_subscriptions',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
			filter: {
				s_subject_type:'queue' ,
				s_destination_name:"\"" + QUEUE_PREFIX + queueName + "\""
			}
		}]
	};

	kpi.requestSummaryData(querySize, function(response, tserieOptions){
		var aux = response.group.s_agent_host;
		for(var i in aux){
			auxResp[i] = { queueSize : aux[ i ]};
			auxResp[i]['agentName'] = i; 
		}
		kpi.requestSummaryData(queryInput, function(response, tserieOptions){
			var aux = response.group.s_agent_host;
			for(var i in aux){
				auxResp[i]['inputRate'] =  aux[ i ];
			}
			kpi.requestSummaryData(queryOutput, function(response, tserieOptions){
				var aux = response.group.s_agent_host;
				for(var i in aux){
					auxResp[i]['outputRate'] =  aux[ i ];
				}
				kpi.requestSummaryData(querySubscriptions, function(response, tserieOptions){
					var aux = response.group.s_agent_host;
					for(var i in aux){
						auxResp[i]['subscriptions'] =  aux[ i ];
					}
					kpi.requestSummaryData(queryRedelivery, function(response, tserieOptions){
						var aux = response.group.s_agent_host;
						for(var i in aux){
							auxResp[i]['redeliveredRate'] =  aux[ i ];
						}
						setGeneralQueueInfo(auxResp, panel);
					});
				});
			});
		});
	});
}

//
//ALL QUEUES
//

function allQueuesInformationInit()
{
	var infoPanel = jQuery('#queues_info');
	infoPanel.html("<tr><td colspan='9' class='oddrow'>Please wait...</td></tr>");

	getAllQueuesInformation();
	setInterval(getAllQueuesInformation, 6000);
}

function getAllQueuesInformation()
{
	var auxResp = {};
	var panel = $('#queues_info');

	var querySize = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_queue_size',
			group: [{ 
				agg: "sum",
				field: "s_destination_name", 
			}],
			filter: {
				s_subject_type:'queue'
			}
		}]
	};

	var queryInput = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_input_rate',
			group: [{ 
				agg: "sum",
				field: "s_destination_name",
			}],
			filter: {
				s_subject_type:'queue'
			}
		}]
	};

	var queryOutput = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_output_rate',
			group: [{ 
				agg: "sum",
				field: "s_destination_name", 
			}],
			filter: {
				s_subject_type:'queue'
			}
		}]
	};

	var queryExpired = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_expired_rate',
			group: [{ 
				agg: "sum",
				field: "s_destination_name", 
			}],
			filter: {
				s_subject_type:'queue'
			}
		}]
	};

	var queryRedelivery = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_redelivered_rate',
			group: [{ 
				agg: "sum",
				field: "s_destination_name", 
			}],
			filter: {
				s_subject_type:'queue'
			}
		}]
	};

	var querySubscriptions = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Queues Info',
			metric: 'n_subscriptions',
			group: [{ 
				agg: "sum",
				field: "s_destination_name", 
			}],
			filter: {
				s_subject_type:'queue'
			}
		}]
	};

	kpi.requestSummaryData(querySize, function(response, tserieOptions){
		var aux = response.group.s_destination_name;
		for(var i in aux){
			auxResp[i] = { queueSize : aux[ i ]};
			auxResp[i]['agentName'] = i; 
		}
		kpi.requestSummaryData(queryInput, function(response, tserieOptions){
			var aux = response.group.s_destination_name;
			for(var i in aux){
				auxResp[i]['inputRate'] =  aux[ i ];
			}
			kpi.requestSummaryData(queryOutput, function(response, tserieOptions){
				var aux = response.group.s_destination_name;
				for(var i in aux){
					auxResp[i]['outputRate'] =  aux[ i ];
				}
				kpi.requestSummaryData(querySubscriptions, function(response, tserieOptions){
					var aux = response.group.s_destination_name;
					for(var i in aux){
						auxResp[i]['expiredRate'] =  aux[ i ];
					}
					kpi.requestSummaryData(querySubscriptions, function(response, tserieOptions){
						var aux = response.group.s_destination_name;
						for(var i in aux){
							auxResp[i]['subscriptions'] =  aux[ i ];
						}
						kpi.requestSummaryData(queryRedelivery, function(response, tserieOptions){
							var aux = response.group.s_destination_name;
							for(var i in aux){
								auxResp[i]['redeliveredRate'] =  aux[ i ];
							}
							setAllQueueGeneralInfo(auxResp, panel);
						});
					});
				});
			});
		});
	});
}

function setAllQueueGeneralInfo(queueGeneralInfo,  panel)
{
	var count = 0;
	var newContent = "";
	if (queueGeneralInfo == undefined)
	{
       	newContent = "<tr><td colspan='9' class='oddrow'>No information available.</td></tr><p>No information available.</P>";
  	}
	else
	{
		var k = 0;
		for(var i in queueGeneralInfo)
		{
			var queueName = removePrefix(i , QUEUE_PREFIX);
			var encQueueName = encodeURIComponent(queueName);

			var prevQueueInfo = previousGeneralQueueInfo[queueName];

			if( prevQueueInfo === undefined)
			{	
				prevQueueInfo = new Object();
				previousGeneralQueueInfo[queueName] = prevQueueInfo;
			}
		
			var queueSize = parseFloat(queueGeneralInfo[i].queueSize);
			var pic = getLocalPic(prevQueueInfo.queueSize, queueSize);
			prevQueueInfo.queueSize = queueSize;
			
			var rowClass =  ( ++k  % 2 == 0) ? "evenrow" : "oddrow";

			newContent = newContent + "<tr class=\"" + rowClass +"\"><td><a href='./queue.html?queuename="+ encQueueName+ "'>" + queueName + "</a></td><td>" + queueSize + "</td><td><img src='" + pic + "' /></td>";


			var inputRate = round(parseFloat(queueGeneralInfo[i].inputRate));
			newContent = newContent + "<td style='padding-left:2em'>" + inputRate + "</td>";
	
			var outputRate = round(parseFloat(queueGeneralInfo[i].outputRate));
			newContent = newContent + "<td style='padding-left:2em'>" + outputRate + "</td>";

			var expiredRate = round(parseFloat(queueGeneralInfo[i].expiredRate));
			newContent = newContent + "<td style='padding-left:2em'>" + expiredRate + "</td>";

			var redeliveredRate = round(parseFloat(queueGeneralInfo[i].redeliveredRate));
			newContent = newContent + "<td style='padding-left:2em'>" + redeliveredRate + "</td>";

			var subscriptions = round(parseFloat(queueGeneralInfo[i].subscriptions));
			newContent = newContent + "<td style='padding-left:2em'>" + subscriptions + "</td></tr>";
		}
	}

	panel.html(newContent);
}



//
//ALL AGENT
//
function allAgentInit()
{
	var panel = jQuery('#agents');
	panel.html("<tr><td colspan='10' class='oddrow'>Please wait...</td></tr>");

	getAllAgentGeneralInfo();
	setInterval(getAllAgentGeneralInfo, 5000);
}

//all agent get info
function getAllAgentGeneralInfo(){

	var auxResp = {};
	var panel = $('#agents');

	var querySize = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_queue_size',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
		}]
	};

	var queryInput = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_agent_input_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
		}]
	};

	var queryOutput = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_agent_output_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
		}]
	};

	var queryStatus = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_status',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
		}]
	};

	var queryErrors = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_error_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
		}]
	};

	kpi.requestSummaryData(querySize, function(response, tserieOptions){
		var aux = response.group.s_agent_host;
		for(var i in aux){
			auxResp[i] = { queueSize : aux[ i ]};
		}
		kpi.requestSummaryData(queryInput, function(response, tserieOptions){
			var aux = response.group.s_agent_host;
			for(var i in aux){
				if(auxResp[i] != undefined)
					auxResp[i]['inputRate'] =  aux[ i ];
				else
					auxResp[i] = { inputRate : aux[i]};
			}
			kpi.requestSummaryData(queryOutput, function(response, tserieOptions){
				var aux = response.group.s_agent_host;
				for(var i in aux){
					if(auxResp[i] != undefined)
						auxResp[i]['outputRate'] =  aux[ i ];
					else
						auxResp[i] = { outputRate : aux[i]};
				}
				kpi.requestSummaryData(queryErrors, function(response, tserieOptions){
					var aux = response.group.s_agent_host;
					for(var i in aux){
						if(auxResp[i] != undefined)
							auxResp[i]['errorRate'] =  aux[ i ];
						else
							auxResp[i] = { errorRate : aux[i]};
					}
					kpi.requestSummaryData(queryStatus, function(response, tserieOptions){
						var aux = response.group.s_agent_host;
						for(var i in aux){
							if(auxResp[i] != undefined)
								auxResp[i]['status'] =  aux[ i ];
							else
								auxResp[i] = { status : aux[i]};
						}
						setAllAgentGeneralInfo(auxResp, panel);
					});
				});
			});
		});
	});
}
//all agents set to table
function setAllAgentGeneralInfo(agentGeneralInfo,  panel)
{
	var count = 0;
	var newContent = "";
	if (agentGeneralInfo.length == 0)
	{
		newContent = "<tr><td colspan='10' class='oddrow'>No information available.</td></tr>";
	}
	else
	{
		var k = 0;
		for(var i in agentGeneralInfo)
		{
			var agentname = i;		
			var rowClass =  (( (++k) % 2) == 0) ? "evenrow" : "oddrow";

			newContent = newContent + "<tr class=\"" + rowClass +"\"><td><a href='./agent.html?agentname="+ agentname+ "'>" + agentname + "</a></td>";

			newContent = newContent + "<td>" + ( parseFloat(agentGeneralInfo[i].status) == 0 ? "OK" : "Down" ) + "</td>";

			var inputRate = round(parseFloat(agentGeneralInfo[i].inputRate));
			newContent = newContent + "<td style='padding-left:2em'>" + inputRate + "</td><td style='padding-right:2em'></td>";
			
			var outputRate = round(parseFloat(agentGeneralInfo[i].outputRate));
			newContent = newContent + "<td style='padding-left:2em'>" + outputRate + "</td><td style='padding-right:2em'></td>";

			var errorRate = round(parseFloat(agentGeneralInfo[i].errorRate));
			newContent = newContent + "<td style='padding-left:2em'>" + errorRate + "</td><td style='padding-right:2em'></td>";

			var totalQueueCount = round(parseFloat(agentGeneralInfo[i].queueSize));
			newContent = newContent + "<td>" + ( isNaN(totalQueueCount) ? 0 : totalQueueCount ) + "</td></tr>";
		}
	}
	panel.html(newContent);
}

//
// AGENT PAGE
//
function agentMonitorizationInit() 
{
	var idPanel = jQuery('#host_name');
	var agentname = SAPO.Utility.Url.getQueryString()['agentname'];
	if (agentname == null)
	{
		idPanel.html("<b>Agent name not specified</b>");
		return;
	}
	idPanel.text(agentname);

	var f_rates_all = function() {
		
		var time = new Date().getTime();

		refreshGraph(
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_queue_size',
				agg: 'sum',
				filter: {
					s_agent_host: "\"" + agentname + "\""
				}
			}]
		},
		"#img_queue_size_rate", 
		"#queue_size_rate", 
		undefined
		);

		refreshGraph(
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_output_rate',
				agg: 'sum',
				filter: {
					s_agent_host:"\"" + agentname + "\""
				}
			}]
		},
		"#img_input_rate", 
		"#count_input_rate", 
		"m/s"
		);

		refreshGraph(
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_output_rate',
				agg: 'sum',
				filter: {
					s_agent_host:"\"" + agentname + "\""
				}
			}]
		},
		"#img_output_rate", 
		"#count_output_rate", 
		"m/s"
		);

		refreshGraph( 	
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_error_rate',
				agg: 'sum',
				filter: {
					s_agent_host:"\"" + agentname + "\""
				}
			}]
		},
		"#img_error_rate", 
		"#count_error_rate", 
		"e/s"
		)
	}

	f_rates_all();
	setInterval(f_rates_all, 5000);


	var panel = jQuery('#agents');
	panel.html("<tr><td colspan='10' class='oddrow'>Please wait...</td></tr>");

	getAgentGeneralInfo();
	setInterval(getAgentGeneralInfo, 5000);

}


function getAgentGeneralInfo(){

	var agentName = SAPO.Utility.Url.getQueryString()['agentname'];
	var querySize = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Queue Info',
			metric: 'n_queue_size',
			group: [{ 
				agg: "sum",
				field: "s_destination_name", 
			}],
			filter: {
				s_subject_type: "queue",
				s_agent_host:"\"" + agentName + "\""
			}
		}]
	};

	var queryStatus = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_status',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
			filter: {
				s_agent_host:"\"" + agentName + "\""
			}
		}]
	};

	var queryErrors = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info Errors',
			metric: 'n_error_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host", 
			}],
			filter: {
				s_agent_host:"\"" + agentName + "\""
			}
		}]
	};

	var querySubscriptions = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_subscriptions',
			group: [{ 
				agg: "sum",
				field: "s_destination_name", 
			}],
			filter: {
				s_agent_host:"\"" + agentName + "\""
			}
		}]
	};

	kpi.requestSummaryData(querySize, function(response, tserieOptions){
		var aux = response.group.s_destination_name;
		setAgentQueueInfo(aux, $('#queue_size'));
	});

	var panel = $('#misc_info');
	var auxMisc = {};

	kpi.requestSummaryData(queryStatus, function(response, tserieOptions){
		var aux = response.group.s_agent_host;
		for(var i in aux){
			auxMisc['status'] =  aux[ i ];
		}
		kpi.requestSummaryData(queryErrors, function(response, tserieOptions){
			var aux = response.group.s_agent_host;
			for(var i in aux){
				auxMisc['errorRate'] =  aux[ i ];
			}
			setMiscAgentInfo(auxMisc, panel);
		});
	});

	var auxResp = {};

	kpi.requestSummaryData(querySubscriptions, function(response, tserieOptions){
		var aux = response.group.s_destination_name;
		for(var i in aux){
			auxResp[i] =  aux[i] ;
		}
		setAgentSubscriptionInfo(auxResp, $('#subscriptions'));
	});
	
}


//misc agent info
function setMiscAgentInfo(miscInfo, panel)
{
	var newContent = "";
	if (miscInfo == null || miscInfo == '')
	{
        	newContent = "<p>No information available.</P>";
  	}
	else
	{
		var i = 1;
		var rowClass =  ( ((i++)%2) == 0) ? "evenrow" : "oddrow";
		newContent = newContent + "<tr class=\"" + rowClass +"\"><td>Status</td><td style='padding-right:2em'>" + ( miscInfo['status'] == 0 ? 'OK' : 'Down' ) +"</td></tr>";

//		rowClass =  ( ((i++)%2) == 0) ? "evenrow" : "oddrow";
//		newContent = newContent + "<tr class=\"" + rowClass +"\"><td>TCP Connections</td><td style='padding-right:2em'>" + round(parseFloat(miscInfo[0].tcpConnections), 0) +"</td></tr>";

//		rowClass =  ( ((i++)%2) == 0) ? "evenrow" : "oddrow";
//		newContent = newContent + "<tr class=\"" + rowClass +"\"><td>Legacy TCP Connections</td><td style='padding-right:2em'>" + round(parseFloat(miscInfo[0].tcpLegacyConnections), 0) +"</td></tr>";

//		rowClass =  ( ((i++)%2) == 0) ? "evenrow" : "oddrow";
//		newContent = newContent + "<tr class=\"" + rowClass +"\"><td>SSL Connections</td><td style='padding-right:2em'>" + round(parseFloat(miscInfo[0].ssl), 0) +"</td></tr>";

//		rowClass =  ( ((i++)%2) == 0) ? "evenrow" : "oddrow";
//		newContent = newContent + "<tr class=\"" + rowClass +"\"><td>Dropbox</td><td style='padding-right:2em'>" + round(parseFloat(miscInfo[0].dropboxCount), 0) +"</td></tr>";

		rowClass =  ( ((i++)%2) == 0) ? "evenrow" : "oddrow";
		newContent = newContent + "<tr class=\"" + rowClass +"\"><td>Fault Rate</td><td style='padding-right:2em'>" + round(parseFloat(miscInfo['errorRate'])	) +"</td></tr>";

//		rowClass =  ( ((i++)%2) == 0) ? "evenrow" : "oddrow";
//		newContent = newContent + "<tr class=\"" + rowClass +"\"><td>Failed Sys Msg</td><td style='padding-right:2em'>" +  round(parseFloat(miscInfo[0].pendingAckSystemMsg), 0) +"</td></tr>";
	}
	panel.html(newContent);
}


var previousAgentQueueCount = new Object();

//agent info - set to queue table
function setAgentQueueInfo(queueInfo, panel)
{
	var newContent = "";

	if (queueInfo == undefined)
	{
        	newContent = "<p>No information available.</P>";
  	}
	else
	{
		var k = 0;
		for(var i in queueInfo)
		{
			var queueName = removePrefix(i, QUEUE_PREFIX);
			var encQueueName = encodeURIComponent(queueName);
			
			var queueCount = parseFloat(queueInfo[i]);
			
			var previous = previousAgentQueueCount[queueName];
			var pic = getLocalPic(previous, queueCount);
			previousAgentQueueCount[queueName] = queueCount;	

			var rowClass =  ( ((++k + 1 ) % 2 ) == 0) ? "evenrow" : "oddrow";
			newContent = newContent + "<tr class=\"" + rowClass +"\"><td style='padding-left:2em'><a href='./queue.html?queuename="+encQueueName+"'>"+ queueName+ "</a></td><td style='padding-right:2em'>" + queueCount +"</td><td style='padding-right:2em'><img src='" + pic + "' /></td></tr>";
		}
	}
	panel.html(newContent);
}

//agent info - subcriptions
function setAgentSubscriptionInfo(subscriptionsInfo, panel)
{
	var newContent = "";
	if (subscriptionsInfo == undefined)
	{
        	newContent = "<p>No information available.</P>";
  	}
	else
	{	
		var k = 0;

		for(var i in subscriptionsInfo)
		{
			var subscriptionName = i;
			var encSubscriptionName = encodeURIComponent(subscriptionName);
			var count = parseFloat(subscriptionsInfo[i]);
			var isTopic = isPrefix(subscriptionName, TOPIC_PREFIX);
			
			subscriptionName = removePrefix(subscriptionName, isTopic ? TOPIC_PREFIX : QUEUE_PREFIX);
			
			var rowClass =  ( ((++k )%2) == 0) ? "evenrow" : "oddrow";
			if(isTopic)
			{
				newContent = newContent + "<tr class=\"" + rowClass +"\"><td style='padding-left:2em'><a href='./topic.html?subscriptionname=" + encSubscriptionName + "'>" + subscriptionName + "</td><td style='padding-right:2em'>TOPIC</td><td style='padding-right:2em'>" +  count + "</td></tr>";
			}
			else
			{
				newContent = newContent + "<tr class=\"" + rowClass +"\"><td style='padding-left:2em'><a href='./queue.html?queuename="+encSubscriptionName+"'>"+ subscriptionName+ "</a></td><td style='padding-right:2em'>QUEUE</td><td style='padding-right:2em'>" + count +"</td></tr>";	
			}
		}
	}
	panel.html(newContent);
}


//
// ALL TOPICS PAGE
//
function allTopicsMonitorizationInit()
{
	var infoPanel = jQuery('#topics');
	infoPanel.html("<tr><td colspan='3' class='oddrow'>Please wait...</td></tr>");

	getAllTopicGeneralInfo();
	setInterval(getAllTopicGeneralInfo, 6000);
}

function getAllTopicGeneralInfo()
{

	var querySubscriptions = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_subscriptions',
			group: [{ 
				agg: "sum",
				field: "s_destination_name",
				sort: 'desc' 
			}],
		}]
	};

	var queryOutput = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_output_rate',
			group: [{ 
				agg: "sum",
				field: "s_destination_name"
			}],
		}]
	};

	var auxResp = {};

	kpi.requestSummaryData(queryOutput, function(response, tserieOptions){
		var aux = response.group.s_destination_name;
		for(var i in aux){
			auxResp[i] =  {output : aux [i] } ;
		}
		kpi.requestSummaryData(querySubscriptions	, function(response, tserieOptions){
			var aux = response.group.s_destination_name;
			
			for(var i in aux){
				auxResp[i]['subscriptions'] =  aux[i] ;
			}
			setAllTopicGeneralInfo(auxResp, $('#topics'));
		});
	});


}

function setAllTopicGeneralInfo(topicsInfo, panel)
{
	var newContent = "";

	if (topicsInfo == undefined)
	{
        newContent = "<tr><td colspan='9' class='oddrow'>No information available.</td></tr>";
  	}
	else
	{
		var k = 0;

		for(var i in topicsInfo)
		{
			var subscriptionName = i;
			var count = parseFloat(topicsInfo[i].subscriptions);
			var outputRate = round(parseFloat(topicsInfo[i].output));
			var isTopic = isPrefix(subscriptionName, TOPIC_PREFIX);

			if(isTopic){	
				subscriptionName = removePrefix(subscriptionName, isTopic ? TOPIC_PREFIX : QUEUE_PREFIX);
				var encSubscriptionName = encodeURIComponent(subscriptionName);
			
				var rowClass =  ( (++k % 2) == 0) ? "evenrow" : "oddrow";

				newContent = newContent + "<tr class=\"" + rowClass +"\"><td><a href='./topic.html?subscriptionname=" + encSubscriptionName + "'>" + subscriptionName + "</a></td><td style='padding-right:2em'>" + outputRate + "</td><td style='padding-right:2em'>" + count +"</td></tr>";
		    }
		}
	}

	panel.html(newContent);
}


//
// TOPIC PAGE
//
function topicMonitorizationInit() 
{
	var subscriptionname = SAPO.Utility.Url.getQueryString()['subscriptionname'];
	var tnPanel =  jQuery('#topic_name'); 
	var encSubscriptionName = encodeURIComponent(subscriptionname);

	if (subscriptionname == undefined)
	{
		tnPanel.html("<b>Queue name not specified</b>");
		return;
	}
	tnPanel.text(subscriptionname);
	var panel = jQuery('#general_topic_information');
	panel.html("<tr><td colspan='5' class='oddrow'>Please wait...</td></tr>");

	var f_rates_all = function() {

		var time = new Date().getTime();

		refreshGraph(
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_output_rate',
				agg: 'sum',
				filter: {
					s_destination_name: "\"" + TOPIC_PREFIX + subscriptionname + "\""
				}
			}]
		},
		"#img_output_rate", 
		"#count_output_rate", 
		"m/s"
		);

		refreshGraph(
		{
			domain: 'broker.bk.sapo.pt',
			begin: new Date(time - TIME_INTERVAL), 
			end: new Date(time), 
			tseries: [
			{
				name: 'Total Queue Size',
				metric: 'n_discarded_rate',
				agg: 'sum',
				filter: {
					s_destination_name: "\"" + TOPIC_PREFIX  + subscriptionname + "\""
				}
			}]
		},
		"#img_discarded_rate", 
		"#discarded_size_rate", 
		"m/s"
		);
	}
	
	f_rates_all();
	setInterval(f_rates_all, 5000);
	getGeneralTopicInfo();
	setInterval(getGeneralTopicInfo, 6000);
}

function getGeneralTopicInfo(){

	var subscriptionname = SAPO.Utility.Url.getQueryString()['subscriptionname'];

	var querySubscriptions = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_subscriptions',
			group: [{ 
				agg: "sum",
				field: "s_agent_host",
			}],
			filter: {
				s_destination_name: "\"" + TOPIC_PREFIX + subscriptionname + "\""
			}
		}]
	};

	var queryOutput = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_output_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host"
			}],
			filter: {
				s_destination_name: "\"" + TOPIC_PREFIX + subscriptionname + "\""
			}
		}]
	};

	var queryDiscarded = {
		domain: 'broker.bk.sapo.pt',
		last: true, 
		summary: [{
			name: 'Agent Info',
			metric: 'n_discarded_rate',
			group: [{ 
				agg: "sum",
				field: "s_agent_host"
			}],
			filter: {
				s_destination_name: "\"" + TOPIC_PREFIX + subscriptionname + "\""
			}
		}]
	};

	var auxResp = {};

	kpi.requestSummaryData(queryOutput, function(response, tserieOptions){
		var aux = response.group.s_agent_host;
		for(var i in aux){
			auxResp[i] =  {outputRate : aux [i] } ;
		}
		kpi.requestSummaryData(queryDiscarded, function(response, tserieOptions){
			var aux = response.group.s_agent_host;
			for(var i in aux){
				auxResp[i]['discardedRate'] =  aux [i] ;
			}
			kpi.requestSummaryData(querySubscriptions	, function(response, tserieOptions){
				var aux = response.group.s_agent_host;
				for(var i in aux){
					auxResp[i]['subscriptions'] =  aux[i] ;
				}
				setGeneralTopicInfo(auxResp, $('#general_topic_information'));
			});
		});
	});


}

function setGeneralTopicInfo(topicGeneralInfo,  panel)
{
	var newContent = "";
	if (topicGeneralInfo == undefined)
	{
        	newContent = "<tr><td colspan='5' class='oddrow'>No information available.</td></tr>";
  	}
	else
	{
		var k = 0;
		for(var i in topicGeneralInfo)
		{
			var agentname = i;		

			var rowClass =  ( (++k % 2) == 0) ? "evenrow" : "oddrow";

			newContent = newContent + "<tr class=\"" + rowClass +"\"><td><a href='./agent.html?agentname="+ agentname+ "'>" + agentname + "</a></td>";
	
			var outputRate = round(parseFloat(topicGeneralInfo[i].outputRate));
			newContent = newContent + "<td style='padding-left:2em'>" + outputRate + "</td>";

			var discardedRate = round(parseFloat(topicGeneralInfo[i].discardedRate));
			newContent = newContent + "<td style='padding-left:2em'>" + discardedRate + "</td>";

			newContent = newContent + "<td>" + parseFloat(topicGeneralInfo[i].subscriptions) + "</td></tr>";
		}
	}

	panel.html(newContent);
}