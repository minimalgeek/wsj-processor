package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.MacroManRepository;
import hu.farago.data.model.entity.mongo.MacroMan;
import hu.farago.data.semantic.SimpleSemanticParser.SemanticSpace;
import hu.farago.data.semantic.SimpleSemanticParser.SemanticSpaceParameter;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public class SimpleSemanticParserTest3 extends AbstractRootTest {

	@Autowired
	private SimpleSemanticParser parser;
	
	@Autowired
	private SemanticSpaceIO spaceIO;
	
	@Autowired
	private MacroManRepository repository;

	private List<List<String>> corpus;

	@Before
	public void setUpBefore() throws Exception {
		corpus = Lists.newArrayList();
		for (MacroMan macro : repository.findAll()) {
			corpus.add(macro.tone.stemmedText);
		}
	}

	@Test
	public void testBuildSemanticSpace() throws Exception {
		SemanticSpace space = parser.buildSemanticSpace(new SemanticSpaceParameter(5, 20, corpus));
		spaceIO.save(space);
	}
	
	@Test
	public void testCountSimilarity() throws Exception {
//		RealMatrix ret = parser.query(parser.buildSemanticSpace(new SemanticSpaceParameter(5, 5, corpus)), testFile);
//		System.out.println("Similarity: " + ret.toString());
	}

}
