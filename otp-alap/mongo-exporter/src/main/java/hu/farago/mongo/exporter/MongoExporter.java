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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class MongoExporter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MongoExporter.class);

	@Value("${exporter.path}")
	private String exporterPath;

	@Value("${exporter.earningsCallPath}")
	private String exporterEarningsCallPath;

	private static final int PAGESIZE = 5;

	@Autowired
	private EarningsCallRepository earningsCallRepository;

	public void exportEarningsCall() throws IOException {

		int count = (int) earningsCallRepository.count();
		int pageSize = calculateMaxPages(count);
		for (int i = 0; i < pageSize; i++) {
			Page<EarningsCall> calls = earningsCallRepository
					.findAll(new PageRequest(i, PAGESIZE));
			Map<String, List<EarningsCallDTO>> indexList = Maps.newHashMap();

			for (EarningsCall call : calls) {
				if (call.hTone == null || call.tone == null) {
					continue;
				}

				if (indexList.containsKey(call.tradingSymbol)) {
					indexList.get(call.tradingSymbol).add(
							new EarningsCallDTO(call));
				} else {
					List<EarningsCallDTO> list = Lists.newArrayList();
					list.add(new EarningsCallDTO(call));
					indexList.put(call.tradingSymbol, list);
				}
			}

			CSVUtils<EarningsCallDTO> utils = new CSVUtils<EarningsCallDTO>(
					exporterPath + exporterEarningsCallPath);
			utils.writeInsiderDataToCSVFiles(indexList);
		}
	}

	private int calculateMaxPages(int size) {
		if (size % PAGESIZE == 0) {
			return size / PAGESIZE;
		} else {
			return (size / PAGESIZE) + 1;
		}
	}

}
