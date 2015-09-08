package hu.farago.wsj.parse;

import hu.farago.wsj.model.entity.sql.Article;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Parses a given article's raw html text to a plain text for further processing. 
 * It uses Apache TIKA.
 * @author Balázs
 */
@Service
public class ArticleParser {
	
	public void parseArticleAndSetPlainText(Article article) throws IOException, SAXException, TikaException {
		ContentHandler handler = new BodyContentHandler();
        new HtmlParser().parse(IOUtils.toInputStream(article.getRawText()), handler, new Metadata(), new ParseContext());
        String plainText = StringUtils.normalizeSpace(handler.toString());
        
        article.setPlainText(plainText);
	}
	
}
