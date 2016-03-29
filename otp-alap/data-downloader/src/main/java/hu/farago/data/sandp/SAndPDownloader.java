package hu.farago.data.sandp;

import hu.farago.data.model.entity.mongo.SAndPIndex;
import hu.farago.data.utils.URLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.exception.TikaException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

@Component
public class SAndPDownloader {

	@Value("${sandp.url}")
	private String sAndPURL;
	
	@Value("${sandp.urlRoot}")
	private String sAndPURLRoot;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SAndPDownloader.class);
	
	private PDFTextStripper stripper;
	
	@PostConstruct
	private void postConstruct() {
		try {
			stripper = new PDFTextStripper();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public List<SAndPIndex> processFirstPage() throws IOException, SAXException, TikaException {
		
		String siteContent = URLUtils.getHTMLContentOfURL(sAndPURL);
		Document document = Jsoup.parse(siteContent);
		
		return collectChanges(document);
		
	}
	
	private List<SAndPIndex> collectChanges(Document document) {
		List<SAndPIndex> ret = Lists.newArrayList();
		
		Element container = document.getElementsByClass("result-container-mid").first();
		Elements articles = container.getElementsByClass("result");
		
		for (Element article : articles) {
			try {
				String pdfURL = sAndPURLRoot + article.getElementsByTag("a").first().attr("href");
				SAndPIndex sAndPIndex = processPDF(pdfURL);
				ret.add(sAndPIndex);
			} catch (Exception e) {
				LOGGER.error("Failed to process: (" + article.text() + ")", e);
			}
		}
		
		return ret;
	}
	
	private SAndPIndex processPDF(String pdfURL) throws MalformedURLException, IOException {
		SAndPIndex ret = null;
		InputStream in = null;
		
		try {
			in = new URL(pdfURL).openStream();
			PDDocument doc = PDDocument.load(in);
			
			String pdfContent = stripper.getText(doc);
			
			LOGGER.info("PDF content: " + pdfContent);
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		return ret;
	}
}
