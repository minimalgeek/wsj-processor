package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.vector.VectorIO;

public class SemanticParserTest extends AbstractRootTest {

	private static final String DIR_PATH = "semantic_data/";

	@Autowired
	private SemanticParser parser;

	private static List<File> corpus;
	private static File testFile;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		corpus = Lists.newArrayList();
		for (int year = 2010; year <= 2015; year++) {
			for (int month = 1; month <= 12; month++) {
				URL url = Thread
						.currentThread()
						.getContextClassLoader()
						.getResource(
								DIR_PATH + year + "-"
										+ StringUtils.leftPad(month + "", 2, '0')
										+ ".txt");

				corpus.add(FileUtils.toFile(url));
			}
		}
		
		URL url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + "2015-12.txt");

		testFile = FileUtils.toFile(url);
	}

	@Test
	public void testBuildSemanticSpace() throws Exception {
		SemanticSpace space = parser.buildSemanticSpace(corpus, 50);
	}
	
	@Test
	public void testCountSimilarity() throws Exception {
		double ret = parser.countSimilarity(testFile, 1);
		System.out.println("Similarity: " + ret);
	}

}
