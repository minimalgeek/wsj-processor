jQuery.noConflict();

if (urlContains("archive-")) {
	var selector = "div#archivedArticles ul.newsItem li h2 a";
	processAllLinkBySelector(selector);
} else {
	console.log("send article data");
	var info = {
			"title": jQuery("h1.wsj-article-headline").text(),
			"rawText": jQuery("<div>").append(jQuery("div#wsj-article-wrap p").clone()).html(),
			"dateTime": (new Date(jQuery("meta[name='article.published']").attr("content"))).toJSON(),
			"url": window.location.href
	};		

	chrome.runtime.sendMessage(info);
	window.close();
}

function processAllLinkBySelector(selector) {
	var divs = jQuery(selector);
	var divsSize = divs.length;
	if (divsSize == 0) {
		window.close();
	} else {
		divs.each(function(index) {
			var productLink = jQuery(this);
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