package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.MacroManRepository;
import hu.farago.data.model.entity.mongo.MacroMan;
import hu.farago.data.semantic.SimpleSemanticParser.SemanticSpace;
import hu.farago.data.semantic.SimpleSemanticParser.SemanticSpaceParameter;

import java.util.List;

import org.junit.Ignore;
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

	@Test
	@Ignore("It's really time consuming")
	public void testBuildSemanticSpace() throws Exception {
		List<List<String>> corpus = Lists.newArrayList();
		
		for (MacroMan macro : repository.findAll()) {
			corpus.add(macro.tone.stemmedText);
		}
		
		SemanticSpace space = parser.buildSemanticSpace(new SemanticSpaceParameter(5, 20, corpus));
		spaceIO.save(space);
	}
	
	@Test
	public void testCountSimilarity() throws Exception {
//		RealMatrix ret = parser.query(parser.buildSemanticSpace(new SemanticSpaceParameter(5, 5, corpus)), testFile);
//		System.out.println("Similarity: " + ret.toString());
	}

	@Test
	public void testReadSemanticSpace() {
		SemanticSpace space = spaceIO.load();
		//parser.query(space, );
	}
	
}
