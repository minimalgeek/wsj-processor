package hu.farago.data.semantic;

import hu.farago.data.semantic.calc.CosineSimilarity;
import hu.farago.data.semantic.calc.TfIdfCalculator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import edu.ucla.sspace.text.IteratorFactory;

@Component
public class SimpleSemanticParser {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleSemanticParser.class);
	private static final EnglishStemmerWithPatternRemover stemmer = new EnglishStemmerWithPatternRemover();

	public static class SemanticSpaceParameter {
		public List<File> documents;
		public int dimensions;
		public int dimensionsForIndexing;
		public int minimalTokenOccurance;

		public SemanticSpaceParameter(List<File> documents, int dimensions,
				int minimalTokenOccurance) {
			this.documents = documents;
			this.dimensions = dimensions;
			this.dimensionsForIndexing = dimensions - 1;
			this.minimalTokenOccurance = minimalTokenOccurance;
		}
	}

	public static class SemanticSpace {
		public RealMatrix termSpace;
		public RealMatrix diagonalMiddle;
		public RealMatrix docSpace;

		public SingularValueDecomposition originalDecomposition;
		public TfIdfCalculator tfIdfCalculator;
		public SemanticSpaceParameter spaceParameter;
	}

	public SemanticSpace buildSemanticSpace(
			SemanticSpaceParameter spaceParameter) throws IOException {
		List<List<String>> docTerms = Lists.newArrayList();
		for (File file : spaceParameter.documents) {
			docTerms.add(tokenizeAndStemTextFile(file));
		}

		TfIdfCalculator calculator = new TfIdfCalculator(docTerms,
				spaceParameter.minimalTokenOccurance);
		SingularValueDecomposition svd = new SingularValueDecomposition(
				calculator.calculateTfIdf());

		SemanticSpace space = new SemanticSpace();
		
		space.originalDecomposition = svd;
		space.tfIdfCalculator = calculator;
		space.spaceParameter = spaceParameter;
		
		space.termSpace = svd.getU().getSubMatrix(0,
				svd.getU().getRowDimension() - 1, 0,
				spaceParameter.dimensionsForIndexing);
		space.diagonalMiddle = svd.getS().getSubMatrix(0,
				spaceParameter.dimensionsForIndexing, 0,
				spaceParameter.dimensionsForIndexing);
		space.docSpace = svd.getV().getSubMatrix(0,
				spaceParameter.dimensionsForIndexing, 0,
				svd.getV().getColumnDimension() - 1);
		return space;
	}

	private List<String> tokenizeAndStemTextFile(File file)
			throws IOException {
		String doc = FileUtils.readFileToString(file, "UTF-8");
		List<String> tokens = Lists.newArrayList(IteratorFactory.tokenize(doc));
		List<String> stemmedTokens = Lists.newArrayList();
		for (String token : tokens) {
			stemmedTokens.add(stemmer.stem(token));
		}
		return stemmedTokens;
	}

	public RealMatrix query(SemanticSpace space, File file) throws IOException {

		RealMatrix diagonalMiddleInverse = new LUDecomposition(
				space.diagonalMiddle).getSolver().getInverse();
		RealMatrix queryVector = space.tfIdfCalculator.calculateTfIdfVector(tokenizeAndStemTextFile(file));
		// RealMatrix truncatedQuery = queryVector.getSubMatrix(0, 0, 0, space.spaceParameter.dimensionsForIndexing);
		RealMatrix query = queryVector.multiply(space.termSpace).multiply(diagonalMiddleInverse);
		LOGGER.info("Query:" + query.toString());
		
		CosineSimilarity similarity = new CosineSimilarity();
		RealMatrix docSimilarity = new Array2DRowRealMatrix(space.docSpace.getColumnDimension(), 1);
		for (int i = 0; i < space.docSpace.getColumnDimension(); i++) {
			double sim = similarity.computeSimilarity(query.transpose(), space.docSpace.getColumnMatrix(i));
			docSimilarity.setEntry(i, 0, sim);
		}
		
		return docSimilarity;
	}
}
