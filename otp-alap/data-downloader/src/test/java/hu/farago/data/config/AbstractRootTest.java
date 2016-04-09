package hu.farago.data.config;

import java.util.Calendar;
import java.util.Date;

import hu.farago.data.config.AppConfig;
import hu.farago.data.config.WebMvcConfig;
import hu.farago.data.config.MongoContext;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles(AppConfig.APPLICATION_TEST)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebMvcConfig.class,
		PersistenceContext.class, MongoContext.class }, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public abstract class AbstractRootTest {
	
	protected Date getNewDate(int year, int month, int day) {
		Calendar date = Calendar.getInstance();
		date.set(year, month, day);
		date = DateUtils.truncate(date, Calendar.DATE);
		return date.getTime();
	}

}
