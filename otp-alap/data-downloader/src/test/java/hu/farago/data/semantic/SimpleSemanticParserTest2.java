package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.semantic.SimpleSemanticParser.SemanticSpaceParameter;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public class SimpleSemanticParserTest2 extends AbstractRootTest {

	private static final String TXT_2015_12 = "2015-12.txt";

	private static final String DIR_PATH = "semantic_data/";

	@Autowired
	private SimpleSemanticParser parser;

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
		
		
		url = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(DIR_PATH + TXT_2015_12);

		testFile = FileUtils.toFile(url);
	}
	
	@Test
	public void testCountSimilarity() throws Exception {
		RealMatrix ret = parser.query(parser.buildSemanticSpace(new SemanticSpaceParameter(corpus, 5, 5)), testFile);
		System.out.println("Similarity: " + ret.toString());
	}

}
