package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import edu.ucla.sspace.common.SemanticSpace;

public class SemanticParserTest2 extends AbstractRootTest {

	private static final String DIR_PATH = "semantic_data_tech/";

	@Autowired
	private SemanticParser parser;

	private static List<File> corpus;
	private static File testFile;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		corpus = Lists.newArrayList();
		
		URL url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH);
		
		corpus.addAll(
				FileUtils.listFiles(
						FileUtils.toFile(url), 
						FileFilterUtils.trueFileFilter(), 
						DirectoryFileFilter.DIRECTORY));
		
		url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + "ibmtest.txt");

		testFile = FileUtils.toFile(url);
	}

	@Test
	public void testBuildSemanticSpace() throws Exception {
		SemanticSpace space = parser.buildSemanticSpace(corpus, 20);
	}
	
	@Test
	public void testCountSimilarity() throws Exception {
		double ret = parser.countSimilarity(testFile, 1);
		System.out.println("Similarity: " + ret);
	}

}
