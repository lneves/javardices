
//
// UTILS
//

function getLocalPic(oldValue, newValue)
{
	var tendencyPic = "images/trend_flat.gif";
	if( oldValue !== undefined)
	{
		tendencyPic = (oldValue == newValue)? "images/trend_flat.gif" : (newValue > oldValue)? "images/trend_up_bad.gif" : "images/trend_down_good.gif";
	}
	return tendencyPic;
}

function removePrefix(string, prefix)
{
	if(isPrefix(string, prefix))
	{	
		return string.substring(prefix.length);
	}
	return string;
}

function isPrefix(string, prefix)
{
	return string.match("^"+prefix)==prefix;
}

function round(value, precision)
{
	if(value == 0) return 0;
	
	var mult = 1;
	
	if(typeof(precision)!=undefined && precision!=null)
	{
		mult = precision;
	}

	if(precision == 0)
	{
		Math.round(value);
	}
	
	var factor = 10;	
	for(var i = 1;  mult > i; ++i)
	{
		factor = factor * 10;
	}
	
	
	return Math.round(value * factor) / factor;
}

function parseISO8601(str) {
 // we assume str is a UTC date ending in 'Z'

 var parts = str.split('T'),
 dateParts = parts[0].split('-'),
 timeParts = parts[1].split('Z'),
 timeSubParts = timeParts[0].split(':'),
 timeSecParts = timeSubParts[2].split('.'),
 timeHours = Number(timeSubParts[0]),
 _date = new Date;

 _date.setUTCFullYear(Number(dateParts[0]));
 _date.setUTCMonth(Number(dateParts[1])-1);
 _date.setUTCDate(Number(dateParts[2]));
 _date.setUTCHours(Number(timeHours));
 _date.setUTCMinutes(Number(timeSubParts[1]));
 _date.setUTCSeconds(Number(timeSecParts[0]));
 if (timeSecParts[1]) _date.setUTCMilliseconds(Number(timeSecParts[1]));

 // by using setUTC methods the date has already been converted to local time(?)
 return _date;
}

function getHumanTextDiff2(date)
{
	var res = jQuery.timeFormat(date.getTime());

	return res;
}

function getHumanTextDiff(date)
{
	var nowMillis = new Date().getTime();
	var dateMillis = date.getTime();

	var str = "";

	var tDif = nowMillis - dateMillis;

	var tDifMillis = tDif % 1000;

	var tDifSec = Math.floor(tDif / (1000)) % 60;

	var tDifMin = Math.floor(tDif / (1000 * 60)) % (60 * 60);

	var tDifHours = Math.floor(tDif / (1000 * 60 * 60 )) % (60 * 60 * 60);

	if( (tDifHours % 24) != 0 )
	{
		str += (tDifHours % 24) +  " hours";
	}
	if( tDifMin != 0 )
	{
		str = (str == "") ? str : str + " and "; 
		str += " " + tDifMin +  " minutes";
	}
	
	if( tDifSec != 0 )
	{
		str = (str == "") ? str : str + " and "; 
		str += " " + tDifSec + " seconds";
	}

	if( str === "" && tDifMillis != 0 )
	{
		//str = (str == "") ? str : str + " and "; 
		str += tDifMillis + " milliseconds";
	}

	if(str === "")
	{
		return date.toString();
	}
	
	return str;
}
/*
function getHumanTextDiff(date)
{
	var now = new Date();
	var dDif = new Date( now - date);

	var tzOffsetHours = 0; //(now.getTimezoneOffset() != 0) ? (now.getTimezoneOffset() / 60) : 0;

	var str = "";

	if( ((dDif.getHours() + tzOffsetHours) % 24) != 0 )
	{
		str += ((dDif.getHours() + tzOffsetHours) % 24) +  " hours";
	}
	if( dDif.getMinutes() != 0 )
	{
		str = (str == "") ? str : str + " and "; 
		str += " " + dDif.getMinutes() +  " minutes";
	}
	
	if( dDif.getSeconds() != 0 )
	{
		str = (str == "") ? str : str + " and "; 
		str += " " + dDif.getSeconds() + " seconds";
	}

	if( str === "" && dDif.getMilliseconds() != 0 )
	{
		//str = (str == "") ? str : str + " and "; 
		str += dDif.getMilliseconds() + " milliseconds";
	}

	if(str === "")
	{
		return date.toString();
	}
	
	return str;
}
*/

/*
	Circular queue
*/


function test_circular_queue()
{
	var cq = new CircularQueue(); 
	cq.init(5);
	
	var counter = 0;
	cq.add(counter++);
	console.log('showing after: ' + counter);
	showAllElements(cq);
	cq.add(counter++);
	
	console.log('showing after: ' + counter);
	showAllElements(cq);

	cq.add(counter++);
	cq.add(counter++);
	cq.add(counter++);

	console.log('showing after: ' + counter);
	showAllElements(cq);

	cq.add(counter++);
	cq.add(counter++);
	cq.add(counter++);
	cq.add(counter++);

	console.log('showing after: ' + counter);
	showAllElements(cq);

	cq.add(counter++);
	cq.add(counter++);
	cq.add(counter++);
	cq.add(counter++);

	console.log('showing after: ' + counter);
	showAllElements(cq);

//	var count = cq.size();

//	var value = cq.get(2);
}

function showAllElements(circular_queue)
{
	var size = circular_queue.size();
	for(var i = 0; i != size; ++i)
	{
		console.log(circular_queue.get(i));
	}
}

function ImageMetadata()
{
	var _min;
	var _max;
	var _circularQueue;
	
	this.init = function(min, max)
	{
		this._min = min;
		this._max = max;
	}
	
	this.getMin = function()
	{
		return this._min;
	}
	
	this.setMin = function(min)
	{
		this._min = min;
	}

	this.getMax = function()
	{
		return this._max;
	}

	this.setMax = function(max)
	{
		this._max = max;
	}

	this.getCircularQueue = function()
	{
		return this._circularQueue;
	}

	this.setCircularQueue = function(circularQueue)
	{
		this._circularQueue = circularQueue;
	}
}

function CircularQueue()
{
	var _buffer;
	var _next;
	var _size;
	var _elementCount;
	this.init = function(size)
	{
		this._buffer = new Object();
		this._next = 0;
		this._size = size;
		this._elementCount = 0;
	}
	this.add = function(value)
	{
		this._buffer[this._next] =  value;
		if(this._elementCount < this._size)
		{
			++this._elementCount;
		}
		this._next = (this._next +1 ) % this._size;
	}
	this.size = function()
	{
		return this._elementCount;
	}
	this.get = function(index)
	{
		return  this._buffer[ ( ((this._count < this._size) ? 0 : this._next ) + index ) % this._elementCount];
	}
}
/*
	Circular queue end
*/
