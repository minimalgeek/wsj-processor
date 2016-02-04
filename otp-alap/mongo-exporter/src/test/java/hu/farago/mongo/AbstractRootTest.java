package hu.farago.mongo;

import hu.farago.mongo.config.AppConfig;
import hu.farago.mongo.config.MongoContext;
import hu.farago.mongo.config.WebMvcConfig;

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
@ContextConfiguration(classes = { AppConfig.class, WebMvcConfig.class, MongoContext.class }, 
	loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public abstract class AbstractRootTest {

	@Before
	public void before() {

	}

	@After
	public void after() {

	}
}
