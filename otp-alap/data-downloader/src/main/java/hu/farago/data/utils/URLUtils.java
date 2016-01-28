package hu.farago.data.utils;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class URLUtils {

	public static final String UTF_8 = "UTF-8";
	
	public static String getContentOfURL(String url) throws IOException, SAXException, TikaException {
		ContentHandler handler = new BodyContentHandler(-1);
		String rawText = IOUtils.toString(new URL(url), UTF_8);
        new HtmlParser().parse(IOUtils.toInputStream(rawText), handler, new Metadata(), new ParseContext());
        String plainText = StringUtils.normalizeSpace(handler.toString());
		return plainText;
	}
	
	public static String getHTMLContentOfURL(String url) throws IOException, SAXException, TikaException {
		String rawText = IOUtils.toString(new URL(url), UTF_8);
		return rawText;
	}
	
	public static String getContentOfHTMLContent(String content) throws IOException, SAXException, TikaException {
		ContentHandler handler = new BodyContentHandler(-1);
        new HtmlParser().parse(IOUtils.toInputStream(content), handler, new Metadata(), new ParseContext());
        String plainText = StringUtils.normalizeSpace(handler.toString());
		return plainText;
	}
	
}
