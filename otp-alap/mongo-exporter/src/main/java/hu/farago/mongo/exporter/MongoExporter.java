package hu.farago.mongo.exporter;

import hu.farago.mongo.exporter.dto.EarningsCallDTO;
import hu.farago.mongo.model.dao.mongo.EarningsCallRepository;
import hu.farago.mongo.model.entity.mongo.EarningsCall;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class MongoExporter {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MongoExporter.class);

	@Value("${exporter.path}")
	private String exporterPath;
	
	@Value("${exporter.mongoBinPath}")
	private String exporterMongoBinPath;
	
	@Value("${exporter.earningsCallPath}")
	private String exporterEarningsCallPath;
	
	@Autowired
	private EarningsCallRepository earningsCallRepository;
	
	public void executeCommand(String command) throws IOException {
		Runtime rt = Runtime.getRuntime();              
	    Process pr = rt.exec(command);
	    
	    if (pr.getErrorStream() != null) {
	    	LOGGER.error(pr.getErrorStream().toString());
	    }
	    
	}

	public void exportEarningsCall() throws IOException {
		
		List<EarningsCall> calls = earningsCallRepository.findAll();
		Map<String, List<EarningsCallDTO>> indexList = Maps.newHashMap();
		
		for (EarningsCall call : calls) {
			if (call.hTone == null || call.tone == null) {
				continue;
			}
			
			if (indexList.containsKey(call.tradingSymbol)) {
				indexList.get(call.tradingSymbol).add(new EarningsCallDTO(call));
			} else {
				List<EarningsCallDTO> list = Lists.newArrayList();
				list.add(new EarningsCallDTO(call));
				indexList.put(call.tradingSymbol, list);
			}
		}
		
		CSVUtils<EarningsCallDTO> utils = new CSVUtils<EarningsCallDTO>(exporterPath + exporterEarningsCallPath);
		utils.writeInsiderDataToCSVFiles(indexList);
	}
	
	
}
