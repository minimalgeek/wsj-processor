if (urlContains("dayinapage")) {
	var selector = "div.channel div.section ul li a";
	processAllLinkBySelector(selector);
} else if (urlContains("article-archive") || urlContains("archivemonth")) {
	var selector = "div.row div.column ul li a";
	processAllLinkBySelector(selector);
} else {
	var info;
	console.log("send article data");
	if (urlContains("i100")) {
		jQuery.noConflict();
		info = {
			"title": document.title,
			"rawText": jQuery("div.body").html(),
			"dateTime": jQuery("div#article-container article").attr("data-releasedate"),
			"url": window.location.href
		};		
	} else {
		info = {
			"title": document.title,
			"rawText": $("<div>").append($(".storyContent.articleContent").clone()).html(),
			"dateTime": (new Date($('meta[property="article:published_time"]').attr('content'))).toJSON(),
			"url": window.location.href
		};
	}

	chrome.runtime.sendMessage(info);
	window.close();
}

function processAllLinkBySelector(selector) {
	var divs = $(selector);
	var divsSize = divs.length;
	if (divsSize == 0) {
		window.close();
	} else {
		divs.each(function(index) {
			var productLink = $(this);
			openLinkAndClose(productLink, index, divsSize);
		});
	}
}

function openLinkAndClose(productLink, index, divsSize) {
	if (index == divsSize - 1) {
		openLink(productLink);
		window.close();
	} else {
		openLink(productLink);
	}
}

function urlContains(str) {
	return window.location.href.indexOf(str) > -1;
}

function openLink(productLink) {
	var info = {
		"method" : "openURL",
		"url": productLink.attr("href")
	};
	chrome.runtime.sendMessage(info);
}