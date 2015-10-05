package hu.farago.wsj.contentcollector;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ContentCollectorService {

	@Scheduled(cron = "0 0/30 * * * ?")
	public void collectContent() {
		
	}
	
}
