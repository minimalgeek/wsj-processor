package hu.farago.data.service;

import hu.farago.data.edgar.EdgarDownloader;
import hu.farago.data.model.dao.mongo.EdgarDataRepository;
import hu.farago.data.model.entity.mongo.EdgarData;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class EdgarDownloadService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EdgarDownloadService.class);

	@Autowired
	private EdgarDownloader edgarDownloader;
	@Autowired
	private EdgarDataRepository edgarRepository;	

	@RequestMapping(value = "/collectGroupContent", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> collectGroupContent() {
		LOGGER.info("collectGroupContent");
		List<String> ret = Lists.newArrayList();
		try {
			edgarRepository.deleteAll();
			edgarDownloader.clean();
			for (int i = 0; i < edgarDownloader.pages(); i++) {
				Map<String, List<EdgarData>> map = edgarDownloader.parseAll(i);
				
				for (Map.Entry<String, List<EdgarData>> entry : map.entrySet()) {
					edgarRepository.save(entry.getValue());
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
			edgarDownloader.clean();
			List<EdgarData> list = edgarDownloader.collectAllDataForIndex(index);
			
			// remove older entries
			edgarRepository.delete(edgarRepository.findByTradingSymbol(index));
			edgarRepository.save(list);
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
