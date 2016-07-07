package hu.farago.data.semantic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.DocumentVectorBuilder;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.matrix.TfIdfTransform;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;

@Component
public class SemanticParser {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SemanticParser.class);

	public static class BuildSemanticSpaceParameter {
		public List<File> documents;
		public int dimensions;
		public int tokenLimit;
		public String spaceFile;

		public BuildSemanticSpaceParameter(List<File> documents,
				int dimensions, int tokenLimit, String spaceFile) {
			this.documents = documents;
			this.dimensions = dimensions;
			this.tokenLimit = tokenLimit;
			this.spaceFile = spaceFile;
		}
	}

	public SemanticSpace buildSemanticSpace(
			BuildSemanticSpaceParameter parameterObject) throws Exception {

		setProperties(parameterObject);
		List<BufferedReader> bufferedReaders = Lists.newArrayList();
		convertFilesToBufferedReaders(parameterObject.documents,
				bufferedReaders);

		if (parameterObject.dimensions <= 0) {
			parameterObject.dimensions = parameterObject.documents.size() - 1;
		}
		LatentSemanticAnalysis lsa = new LatentSemanticAnalysis(true,
				parameterObject.dimensions, new TfIdfTransform(),
				SVD.getFastestAvailableFactorization(), false,
				new StringBasisMapping());

		for (BufferedReader document : bufferedReaders) {
			lsa.processDocument(document);
		}

		lsa.processSpace(System.getProperties());
		SemanticSpaceIO.save(lsa, parameterObject.spaceFile);

		return lsa;
	}

	public double countSimilarity(BuildSemanticSpaceParameter parameter)
			throws Exception {
		setProperties(parameter);
		SemanticSpace space = SemanticSpaceIO.load(parameter.spaceFile);
		// SemanticSpace after =
		// buildSemanticSpace(Lists.newArrayList(document), dimensions,
		// tokenLimit);
		
		DocumentVectorBuilder builder = new DocumentVectorBuilder(space);
		DoubleVector vector = new SparseHashDoubleVector(parameter.dimensions);
		for (File document : parameter.documents) {
			vector = builder.buildVector(new BufferedReader(new StringReader(
					FileUtils.readFileToString(document, "UTF-8"))), vector);
		}

		return vector.magnitude();
		/*
		Set<String> words = space.getWords();
		double sum = 0.0;
		Iterator<String> i = words.iterator();

		while (i.hasNext()) {
			String word = i.next();
			sum += Similarity.getSimilarity(Similarity.SimType.COSINE,
					space.getVector(word), vector);
		}
		return sum / words.size();
		*/
	}

	private void setProperties(BuildSemanticSpaceParameter parameterObject) {
		Properties props = System.getProperties();
		props.setProperty(IteratorFactory.TOKEN_COUNT_LIMIT_PROPERTY,
				parameterObject.tokenLimit + "");
		props.setProperty(IteratorFactory.STEMMER_PROPERTY,
				EnglishStemmerWithPatternRemover.class.getName());
		IteratorFactory.setProperties(props);
	}
	
	private void convertFilesToBufferedReaders(List<File> documents,
			List<BufferedReader> bufferedReaders) throws IOException {
		for (File file : documents) {
			// StringBuilder builder = new StringBuilder();
			//
			// Stemmer s = new EnglishStemmer();
			// Iterator<String> it =
			// IteratorFactory.tokenize(FileUtils.readFileToString(file,
			// "UTF-8"));
			// while (it.hasNext()) {
			// String token =
			// StringUtils.removePattern(StringUtils.lowerCase(it.next()),
			// "[^\\w]");
			// String stemmedToken = s.stem(token);
			// builder.append(stemmedToken);
			// builder.append('\n');
			// }

			bufferedReaders.add(new BufferedReader(new StringReader(FileUtils
					.readFileToString(file, "UTF-8"))));
		}
	}
}
