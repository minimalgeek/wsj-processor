package hu.farago.data.service;

import hu.farago.data.insider.InsiderDownloader;
import hu.farago.data.insider.InsiderFileUtils;
import hu.farago.data.insider.InsiderGroupDownloader;
import hu.farago.data.model.dao.mongo.InsiderDataGroupRepository;
import hu.farago.data.model.dao.mongo.InsiderDataRepository;
import hu.farago.data.model.entity.mongo.InsiderData;
import hu.farago.data.model.entity.mongo.InsiderDataGroup;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class InsiderTradingDownloadService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InsiderTradingDownloadService.class);

	@Autowired
	private InsiderDownloader insiderTradingParser;
	@Autowired
	private InsiderGroupDownloader insiderGroupParser;
	@Autowired
	private InsiderFileUtils insiderFileUtils;
	@Autowired
	private InsiderDataRepository insiderDataDao;
	@Autowired
	private InsiderDataGroupRepository insiderDataGroupDao;

	@Scheduled(cron = "0 0 12 * * ?") // every day at 12:00
	public void scheduledCollectContent() {
		//collectContent();
	}

	@RequestMapping(value = "/collectContent", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> collectContent() {
		LOGGER.info("collectContent");
		List<String> ret = Lists.newArrayList();
		try {
			insiderDataDao.deleteAll();
			for (int i = 0; i < insiderTradingParser.pages(); i++) {
				Map<String, List<InsiderData>> map = insiderTradingParser.parseAll(i);
				
				insiderFileUtils.writeInsiderDataToCSVFiles(map);
				for (Map.Entry<String, List<InsiderData>> entry : map.entrySet()) {
					insiderDataDao.save(entry.getValue());
				}
				
				ret.addAll(map.keySet());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return ret;
	}
	
	@RequestMapping(value = "/collectGroupContent", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> collectGroupContent() {
		LOGGER.info("collectGroupContent");
		List<String> ret = Lists.newArrayList();
		try {
			insiderDataGroupDao.deleteAll();
			insiderGroupParser.clean();
			for (int i = 0; i < insiderTradingParser.pages(); i++) {
				Map<String, List<InsiderDataGroup>> map = insiderGroupParser.parseAll(i);
				
				for (Map.Entry<String, List<InsiderDataGroup>> entry : map.entrySet()) {
					insiderDataGroupDao.save(entry.getValue());
				}
				ret.addAll(map.keySet());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return ret;
	}
	
	@RequestMapping(value = "/collectGroupContentFor/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public String collectGroupContentFor(@PathVariable("id") String index) {
		LOGGER.info("collectGroupContentFor");
		
		StringBuilder ret = new StringBuilder();

		try {
			insiderGroupParser.clean();
			List<InsiderDataGroup> list = insiderGroupParser.collectAllDataForIndex(index);
			
			// remove older entries
			insiderDataGroupDao.delete(insiderDataGroupDao.findByIssuerTradingSymbol(index));
			insiderDataGroupDao.save(list);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ret.append(e.getMessage());
		}
		
		if (ret.length() == 0) {
			ret.append("success");
		}
		
		return ret.toString();
	}

}
