package hu.farago.data.semantic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.text.EnglishStemmer;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.text.Stemmer;

@Component
public class SemanticParser {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SemanticParser.class);
	
	@Value("${semantic.parser.sspacefile}")
	private String sspaceFile;

	public SemanticSpace buildSemanticSpace(List<File> documents, int dimensions)
			throws Exception {
		
		List<BufferedReader> bufferedReaders = Lists.newArrayList();
		convertFilesToBufferedReaders(documents, bufferedReaders);
		
		if (dimensions <= 0) {
			dimensions = documents.size()-1;
		}
		LatentSemanticAnalysis lsa = new LatentSemanticAnalysis(
				true, 
				dimensions, 
				new LogEntropyTransform(),
	            SVD.getFastestAvailableFactorization(), 
	            false, 
	            new StringBasisMapping());

		for (BufferedReader document : bufferedReaders) {
			lsa.processDocument(document);
		}
		 
		lsa.processSpace(System.getProperties());
		SemanticSpaceIO.save(lsa, sspaceFile);
		
		return lsa;
	}
	
	public double countSimilarity(File document, int dimensions) throws Exception {
		SemanticSpace before = SemanticSpaceIO.load(sspaceFile);
		SemanticSpace after = buildSemanticSpace(Lists.newArrayList(document), dimensions);
		
	    Set<String> words = after.getWords();
	    double sum=0.0;
	    Iterator<String> i = words.iterator();
	    
	    while(i.hasNext()){
	        String word = i.next();
	        sum += Similarity.getSimilarity(Similarity.SimType.COSINE,
	                                        before.getVector(word),
	                                        after.getVector(word));
	    }
	    return sum/words.size();
	}

	private void convertFilesToBufferedReaders(List<File> documents,
			List<BufferedReader> bufferedReaders) throws IOException {
		for (File file : documents) {
//			StringBuilder builder = new StringBuilder();
//			
//			Stemmer s = new EnglishStemmer();
//			Iterator<String> it = IteratorFactory.tokenize(FileUtils.readFileToString(file, "UTF-8"));
//		    while (it.hasNext()) {
//		        String token = StringUtils.removePattern(StringUtils.lowerCase(it.next()), "[^\\w]");
//		        String stemmedToken = s.stem(token); 
//		        builder.append(stemmedToken);
//		        builder.append('\n');
//		    }
			
			bufferedReaders.add(new BufferedReader(new StringReader(FileUtils.readFileToString(file, "UTF-8"))));
		}
	}
}
