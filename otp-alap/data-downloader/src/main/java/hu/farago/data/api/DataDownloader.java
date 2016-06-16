package hu.farago.data.api;

import hu.farago.data.utils.URLUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class DataDownloader<T> {

	protected static final int PAGE_SIZE = 3;
	protected List<String> indexes;

	protected abstract Logger getLogger();
	protected abstract String buildUrl(String index, int pageIndex);
	protected abstract boolean notLastPage(String siteContent);
	protected abstract List<T> processDocument(String index, Document document);
	
	public void clean() {
		
	}
	
	public Map<String, List<T>> parseAll(int pageIdx)
			throws Exception {

		Map<String, List<T>> insiderList = Maps.newHashMap();

		for (int i = pageIdx * PAGE_SIZE; i < (pageIdx + 1) * PAGE_SIZE && i < indexes.size(); i++) {
			String index = indexes.get(i);
			try {
				insiderList.put(index, collectAllDataForIndex(index));
				getLogger().info(index + " processed");
			} catch (Exception e) {
				getLogger().error(e.getMessage(), e);
			}
		}

		return insiderList;
	}
	
	public int pages() {
		if (indexes.size() % PAGE_SIZE == 0) {
			return (int) (indexes.size() / PAGE_SIZE);
		} else {
			return (int) (indexes.size() / PAGE_SIZE) + 1;
		}
	}
	
	public List<String> getIndexes() {
		return indexes;
	}
	
	protected double tryToParseDouble(DecimalFormat format, String text) {
		try {
			return format.parse(text).doubleValue();
		} catch (Exception e) {
			getLogger().error(e.getMessage(), e);
			return 0.0;
		}
	}
	
	protected void readFileFromPathAndFillIndexes(String path) {
		File file = new File(path);

		try {
			indexes = FileUtils.readLines(file, Charset.forName("UTF-8"));
		} catch (IOException e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	public List<T> collectAllDataForIndex(String index)
			throws IOException, SAXException, TikaException, ParseException {
		List<T> dataList = Lists.newArrayList();
		boolean jumpToNext = true;
		int pageIndex = 0;

		while (jumpToNext) {
			String urlStr = buildUrl(index, ++pageIndex);
			getLogger().info("Processing page: " + urlStr);
			String siteContent = URLUtils.getHTMLContentOfURL(urlStr);
			Document document = Jsoup.parse(siteContent);

			jumpToNext = notLastPage(siteContent);
			dataList.addAll(processDocument(index, document));
		}
		return dataList;
	}

	
}
