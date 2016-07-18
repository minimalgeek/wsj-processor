package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.semantic.SimpleSemanticParser.SemanticSpace;
import hu.farago.data.semantic.SimpleSemanticParser.SemanticSpaceParameter;
import hu.farago.data.semantic.calc.TfIdfCalculator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public class SimpleSemanticParserTest1 extends AbstractRootTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleSemanticParserTest1.class);

	private static final String GOODTEST_TXT = "goodtest.txt";
	private static final String BADTEST_TXT = "badtest.txt";
	private static final String DIR_PATH = "semantic_data_tech/";
	private static File goodTestFile;
	private static File badTestFile;
	private static List<File> articleCorpus = Lists.newArrayList();

	@Autowired
	private SimpleSemanticParser parser;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(DIR_PATH + "article/");

		articleCorpus
				.addAll(FileUtils.listFiles(FileUtils.toFile(url),
						FileFilterUtils.trueFileFilter(),
						DirectoryFileFilter.DIRECTORY));

		goodTestFile = FileUtils.toFile(Thread.currentThread()
				.getContextClassLoader().getResource(DIR_PATH + GOODTEST_TXT));

		badTestFile = FileUtils.toFile(Thread.currentThread()
				.getContextClassLoader().getResource(DIR_PATH + BADTEST_TXT));
	}

	@Test
	public void testTfIdfCalculation() {
		List<String> doc1 = Arrays.asList("Lorem", "ipsum", "dolor", "ipsum",
				"sit", "ipsum");
		List<String> doc2 = Arrays.asList("Vituperata", "incorrupte", "at",
				"ipsum", "pro", "quo");
		List<String> doc3 = Arrays.asList("Has", "persius", "disputationi",
				"id", "simul", "ipsum");
		List<String> doc4 = Arrays.asList("Has", "persiussos", "disputationi",
				"id", "simul");
		List<List<String>> documents = Arrays.asList(doc1, doc2, doc3, doc4);

		TfIdfCalculator calc = new TfIdfCalculator(documents, 2);
		RealMatrix matrix = calc.calculateTfIdf();
		Assert.assertNotNull(matrix);
		System.out.println(matrix.toString());
	}

	@Test
	public void testBuildSemanticSpace() throws IOException {
		SemanticSpace space = parser
				.buildSemanticSpace(new SemanticSpaceParameter(articleCorpus,
						3, 2));
		Assert.assertNotNull(space);
	}

	@Test
	public void testQuery() throws IOException {
		SemanticSpace space = parser
				.buildSemanticSpace(new SemanticSpaceParameter(articleCorpus,
						3, 2));

		RealMatrix similarity = parser.query(space, goodTestFile);
		LOGGER.info("Good file similarity:" + similarity.toString());

		similarity = parser.query(space, badTestFile);
		LOGGER.info("Bad file similarity:" + similarity.toString());
	}

	@Test
	public void testCluster() throws IOException {
		SemanticSpace space = parser
				.buildSemanticSpace(new SemanticSpaceParameter(articleCorpus,
						5, 2));
		parser.cluster(space);
	}
}
