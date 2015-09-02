jQuery.noConflict();

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

// window opener object

var downloader = {
	stack : [], 
    timer : null,
	allData : function(windowDoc) {
		var year, month, day;
		for (year = 2013; year <= 2013; year++) {
			for (month = 8; month <= 12; month++) {
				for (day = 1; day <= this.daysInMonth(month-1, year); day++) {
					this.queue(
						"http://online.wsj.com/public/page/archive-" + year +
						"-" + month +
						"-" + day + 
						".html"
					);
				}
			}
		}
	},
	todayData : function(windowDoc) {
		var today = new Date();
		window.open(
			"http://online.wsj.com/public/page/archive-" + today.getFullYear() +
			"-" + (today.getMonth()+1) +
			"-" + today.getDate() + 
			".html");
	},
	process : function() {
		// chrome.windows.getAll({populate : true}, function (window_list) {
			// if (window_list.length < 5) {
				var func = function() {				
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
				};
				
				setTimeout(func, downloader.randomIntForClick());
			// }
		// });
	},
	queue : function(item) {
		console.log("Push url to queue: " + item);
		downloader.stack.push(item);
		if (this.timer === null) {
			this.timer = setInterval(this.process, 4000);
		}
	},
	randomIntFromInterval : function(min,max) {
		return Math.floor(Math.random()*(max-min+1)+min);
	},
	randomIntForClick : function() {
		return this.randomIntFromInterval(0, 1000);
	},
	daysInMonth: function(iMonth, iYear){
		return 32 - new Date(iYear, iMonth, 32).getDate();
	}
};

// REST content sender

var contentSender = {
	restURL: 'http://localhost:8080/save-article',
	sendContent: function(info) {
		console.log("Sending article to server...");

		jQuery.ajax({
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