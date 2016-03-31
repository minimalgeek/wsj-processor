package hu.farago.data.sandp;

import hu.farago.data.model.entity.mongo.SAndPIndex;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.Event;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;


//@Component
//public class SAndPRSSDownloader {
//	
//	private static final String ADD_ON = " Add On ";
//	private static final String DROP_ON = " Drop On ";
//
//	@Value("${sandp.url.100}")
//	private String sAndPURL100;
//	
//	@Value("${sandp.url.400}")
//	private String sAndPURL400;
//	
//	@Value("${sandp.url.500}")
//	private String sAndPURL500;
//	
//	@Value("${sandp.url.600}")
//	private String sAndPURL600;
//	
//	@Value("${sandp.url.1500}")
//	private String sAndPURL1500;
//	
//	private SyndFeedInput input;
//	
//	@PostConstruct
//	private void postConstruct() {
//		input = new SyndFeedInput();
//	}
//
//	public List<SAndPIndex> downloadAll() throws IllegalArgumentException, FeedException, IOException {
//		List<SAndPIndex> ret = Lists.newArrayList();
//		
//		processURL(sAndPURL100, ret, SAndPGroup.SP100);
//		processURL(sAndPURL400, ret, SAndPGroup.SP400);
//		processURL(sAndPURL500, ret, SAndPGroup.SP500);
//		processURL(sAndPURL600, ret, SAndPGroup.SP600);
//		processURL(sAndPURL1500, ret, SAndPGroup.SP1500);
//
//		return ret;
//	}
//	
//	private void processURL(String urlStr, List<SAndPIndex> ret, SAndPGroup group) throws IllegalArgumentException, FeedException, IOException {
//		URL url = new URL(urlStr);
//		SyndFeed feed = input.build(new XmlReader(url));
//		
//		List<SyndEntry> entries = feed.getEntries();
//		
//		for (SyndEntry entry : entries) {
//			SAndPIndex sAndPIndex = null;
//			if (entry.getTitle().contains(ADD_ON)) {
//				sAndPIndex = new SAndPIndex();
//				sAndPIndex.company = StringUtils.substringAfter(entry.getTitle(), ADD_ON);
//				
//				SAndPOperation operation = new SAndPOperation();
//				operation.event = Event.ADD;
//				connectIndexAndOperation(group, entry, sAndPIndex, operation);
//			} else if (entry.getTitle().contains(DROP_ON)) {
//				sAndPIndex = new SAndPIndex();
//				sAndPIndex.company = StringUtils.substringAfter(entry.getTitle(), DROP_ON);
//				
//				SAndPOperation operation = new SAndPOperation();
//				operation.event = Event.DROP;
//				connectIndexAndOperation(group, entry, sAndPIndex, operation);
//			}
//			
//			if (sAndPIndex != null) {
//				ret.add(sAndPIndex);
//			}
//		}
//	}
//
//	private void connectIndexAndOperation(SAndPGroup group, SyndEntry entry,
//			SAndPIndex sAndPIndex, SAndPOperation operation) {
//		operation.indexGroup = group;
//		operation.eventDate = new DateTime(entry.getPublishedDate()).withZoneRetainFields(DateTimeZone.UTC);
//		operation.GUID = entry.getUri();
//		sAndPIndex.operations.add(operation);
//	}
//	
//}
