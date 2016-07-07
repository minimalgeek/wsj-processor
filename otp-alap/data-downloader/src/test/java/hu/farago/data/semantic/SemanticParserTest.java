package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.semantic.SemanticParser.BuildSemanticSpaceParameter;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;

public class SemanticParserTest extends AbstractRootTest {

	private static final String TXT_2015_12 = "2015-12.txt";

	private static final String DIR_PATH = "semantic_data/";

	@Autowired
	private SemanticParser parser;
	
	@Value("${semantic.parser.sspacefile.oil}")
	private String oilSpace;

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
						FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter(TXT_2015_12)), 
						DirectoryFileFilter.DIRECTORY));
		
		
		// TODO not good test file, because it is in the corpus
		url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + TXT_2015_12);

		testFile = FileUtils.toFile(url);
	}

	@Test
	public void testBuildSemanticSpace() throws Exception {
		parser.buildSemanticSpace(new BuildSemanticSpaceParameter(corpus, 50, 100, oilSpace));
	}
	
	@Test
	public void testCountSimilarity() throws Exception {
		double ret = parser.countSimilarity(new BuildSemanticSpaceParameter(Lists.newArrayList(testFile), 1, 100, oilSpace));
		System.out.println("Similarity: " + ret);
	}

}
