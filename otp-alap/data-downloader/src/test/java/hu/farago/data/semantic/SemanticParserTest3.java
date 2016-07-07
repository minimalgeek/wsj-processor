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

public class SemanticParserTest3 extends AbstractRootTest {

	private static final String ARTICLETEST_TXT = "articletest.txt";
	private static final String IBMTEST_TXT = "ibmtest.txt";
	
	private static final String DIR_PATH = "semantic_data_tech/";

	@Autowired
	private SemanticParser parser;
	
	private static File goodTestFile;
	private static File badTestFile;
	
	@Value("${semantic.parser.sspacefile.article}")
	private String articleSpace;
	
	private static List<File> articleCorpus = Lists.newArrayList();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		URL url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + "article/");
		
		articleCorpus.addAll(
				FileUtils.listFiles(
						FileUtils.toFile(url), 
						FileFilterUtils.trueFileFilter(), 
						DirectoryFileFilter.DIRECTORY));
		
		goodTestFile = FileUtils.toFile(Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + ARTICLETEST_TXT));
		
		badTestFile = FileUtils.toFile(Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + IBMTEST_TXT));
	}

	@Test
	public void testBuildSemanticSpace() throws Exception {
		parser.buildSemanticSpace(new BuildSemanticSpaceParameter(articleCorpus, 2, 10, articleSpace));
	}
	
	@Test
	public void testCountSimilarity() throws Exception {
		double articleValue = parser.countSimilarity(new BuildSemanticSpaceParameter(Lists.newArrayList(goodTestFile), 1, 10, articleSpace));
		double ibmValue = parser.countSimilarity(new BuildSemanticSpaceParameter(Lists.newArrayList(badTestFile), 1, 10, articleSpace));
		
		System.out.println("Article value: " + articleValue);
		System.out.println("IBM value: " + ibmValue);
	}

}
