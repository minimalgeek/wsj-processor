package hu.farago.data;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import hu.farago.data.config.AppConfig;
import hu.farago.data.config.MongoContext;

@ActiveProfiles(AppConfig.APPLICATION_TEST)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoContext.class }, 
					  loader = AnnotationConfigContextLoader.class)
public abstract class AbstractRootTest {
	
	protected DateTime getNewDate(int year, int month, int day) {
		return new DateTime(year, month, day, 0, 0);
	}

}
