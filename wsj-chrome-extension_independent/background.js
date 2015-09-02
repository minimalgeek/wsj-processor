// omnibox

chrome.omnibox.onInputChanged.addListener(function(text, suggest) {
    suggest([
		{
			content: text + " today", 
			description: "Download all the articles on this day"
		},
		{
			content: text + " all", 
			description: "Download all the articles"
		},
		{
			content: text + " stop", 
			description: "Empty the queue, and stop downloading"
		}
    ]);
});

chrome.omnibox.onInputEntered.addListener(function(text) {
    if (text == "today") {
		downloader.todayData();
	} else if (text == "all") {
		downloader.allData();
	} else if (text == "stop") {
		clearInterval(downloader.timer);
		downloader.timer = null;
	}
});

// message listener

chrome.runtime.onMessage.addListener(
	function(request, sender, sendResponse) {
		console.log("Request received from " + sender.tab.url);
		if (request.method == "openURL") {
			downloader.queue(request.url);
		} else {
			contentSender.sendContent(request);
		}
	}
);

// set up alarm code to trigger the todayData function periodically

/*
chrome.runtime.onInstalled.addListener(function () {
	console.log("Alarm created");
    chrome.alarms.create("todayData", {
		delayInMinutes : 0.0,
		periodInMinutes : 1.0
	});
});
*/

chrome.alarms.onAlarm.addListener(function(alarm) {
	console.log("Fired alarm: " + alarm.name + ", scheduled time: " + alarm.scheduledTime);
    if (alarm.name == "todayData") {
		downloader.todayData();
    }
});


// window opener object

var downloader = {
	stack : [], 
    timer : null,
	allData : function(windowDoc) {
		window.open("http://www.independent.co.uk/article-archive/");
	},
	todayData : function(windowDoc) {
		var today = new Date();
		window.open(
			"http://www.independent.co.uk/dayinapage/?day=" + today.getDate() + 
			"&month=" + (today.getMonth()+1) + 
			"&year=" + today.getFullYear());
	},
	process : function() {
		chrome.windows.getAll({populate : true}, function (window_list) {
			var func = function() {
				if (window_list.length < 5) {
					var item = downloader.stack.shift();
					var win = window.open(item, "_blank");
					if(!win){
						//Broswer has blocked it
						alert("Please allow popups for this site");
					}
					if (downloader.stack.length === 0) {
						clearInterval(downloader.timer);
						downloader.timer = null;
					}
				}
			};
			
			setTimeout(func, downloader.randomIntForClick());
		});
	},
	queue : function(item) {
		console.log("Push url to queue: " + item);
		downloader.stack.push(item);
		if (this.timer === null) {
			this.timer = setInterval(this.process, 1500);
		}
	},
	randomIntFromInterval : function(min,max) {
		return Math.floor(Math.random()*(max-min+1)+min);
	},
	randomIntForClick : function() {
		return this.randomIntFromInterval(0, 1000);
	}
};

// REST content sender

var contentSender = {
	restURL: 'http://localhost:8080/save-article',
	sendContent: function(info) {
		console.log("Sending article to server...");

		$.ajax({
			url:this.restURL,
			type:"POST",
			data:JSON.stringify(info),
			contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (msg) {
               console.log("Success: " + msg);
            },
            error: function (errormsg) {
				console.log("Success: " + errormsg);
            }
			});
	}
};