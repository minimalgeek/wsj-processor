package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.semantic.SemanticParser.BuildSemanticSpaceParameter;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;

public class SemanticParserTest2 extends AbstractRootTest {

	private static final String IBMTEST_TXT = "ibmtest.txt";
	private static final String DIR_PATH = "semantic_data_tech/";

	@Autowired
	private SemanticParser parser;
	
	@Value("${semantic.parser.sspacefile.ibm}")
	private String ibmSpace;
	
	@Value("${semantic.parser.sspacefile.apple}")
	private String appleSpace;
	
	private static List<File> ibmCorpus = Lists.newArrayList();
	private static List<File> appleCorpus = Lists.newArrayList();
	
	private static File testFile;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		URL url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + "ibm/");
		
		ibmCorpus.addAll(
				FileUtils.listFiles(
						FileUtils.toFile(url), 
						FileFilterUtils.trueFileFilter(), 
						DirectoryFileFilter.DIRECTORY));
		url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + "apple/");
		
		appleCorpus.addAll(
				FileUtils.listFiles(
						FileUtils.toFile(url), 
						FileFilterUtils.trueFileFilter(), 
						DirectoryFileFilter.DIRECTORY));
		
		url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + IBMTEST_TXT);

		testFile = FileUtils.toFile(url);
	}

	@Test
	public void testBuildSemanticSpace() throws Exception {
		parser.buildSemanticSpace(new BuildSemanticSpaceParameter(ibmCorpus, 10, 20, ibmSpace));
		parser.buildSemanticSpace(new BuildSemanticSpaceParameter(appleCorpus, 10, 20, appleSpace));
	}
	
	@Test
	public void testCountSimilarity() throws Exception {
		double ibmValue = parser.countSimilarity(new BuildSemanticSpaceParameter(Lists.newArrayList(testFile), 1, 30, ibmSpace));
		double appleValue = parser.countSimilarity(new BuildSemanticSpaceParameter(Lists.newArrayList(testFile), 1, 30, appleSpace));
		
		System.out.println("IBM similarity: " + ibmValue);
		System.out.println("Apple similarity: " + appleValue);
		Assert.assertTrue(ibmValue < appleValue);
	}

}
