package hu.farago.data.semantic;

import hu.farago.data.semantic.calc.CosineSimilarity;
import hu.farago.data.semantic.calc.TfIdfCalculator;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apporiented.algorithm.clustering.Cluster;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.ucla.sspace.text.IteratorFactory;

@Component
public class SimpleSemanticParser {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleSemanticParser.class);
	private static final EnglishStemmerWithPatternRemover stemmer = new EnglishStemmerWithPatternRemover();

	@Autowired
	private ClusterMaker clusterMaker;

	public static class SemanticSpaceParameter {
		public List<File> documents;
		public List<List<String>> documentStrings;
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

		public SemanticSpaceParameter(int dimensions,
				int minimalTokenOccurance, List<List<String>> documentStrings) {
			this.documentStrings = documentStrings;
			this.dimensions = dimensions;
			this.dimensionsForIndexing = dimensions - 1;
			this.minimalTokenOccurance = minimalTokenOccurance;
		}
	}

	public static class SemanticSpace implements Serializable {

		private static final long serialVersionUID = -4385228509384397672L;

		public RealMatrix termSpace;
		public RealMatrix diagonalMiddle;
		public RealMatrix docSpace;

		public transient SingularValueDecomposition originalDecomposition;
		public TfIdfCalculator tfIdfCalculator;
		public transient SemanticSpaceParameter spaceParameter;
	}

	public SemanticSpace buildSemanticSpace(
			SemanticSpaceParameter spaceParameter) throws IOException {
		List<List<String>> stemmedDocumentList = null;
		if (spaceParameter.documentStrings != null) {
			stemmedDocumentList = spaceParameter.documentStrings;
		} else {
			stemmedDocumentList = Lists.newArrayList();
			for (File file : spaceParameter.documents) {
				stemmedDocumentList.add(tokenizeAndStemTextFile(file));
			}
		}

		TfIdfCalculator calculator = new TfIdfCalculator(stemmedDocumentList,
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

		/*
		LOGGER.info("Term space matrix after dimension reduction");
		RealMatrixUtils.printMatrix(space.termSpace.getData());

		LOGGER.info("Diagonal matrix after dimension reduction");
		RealMatrixUtils.printMatrix(space.diagonalMiddle.getData());

		LOGGER.info("Document space matrix after dimension reduction");
		RealMatrixUtils.printMatrix(space.docSpace.getData());
		*/

		return space;
	}

	private List<String> tokenizeAndStemTextFile(File file) throws IOException {
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
		RealMatrix queryVector = space.tfIdfCalculator
				.calculateTfIdfVector(tokenizeAndStemTextFile(file));
		// LOGGER.info("Query vector: " + queryVector.toString());
		// RealMatrix truncatedQuery = queryVector.getSubMatrix(0, 0, 0,
		// space.spaceParameter.dimensionsForIndexing);
		RealMatrix query = queryVector.multiply(space.termSpace).multiply(
				diagonalMiddleInverse);

		CosineSimilarity similarity = new CosineSimilarity();
		RealMatrix docSimilarity = new Array2DRowRealMatrix(
				space.docSpace.getColumnDimension(), 1);
		for (int i = 0; i < space.docSpace.getColumnDimension(); i++) {
			double sim = similarity.computeSimilarity(query.transpose(),
					space.docSpace.getColumnMatrix(i));
			docSimilarity.setEntry(i, 0, sim);
		}

		return docSimilarity;
	}

	public Cluster cluster(SemanticSpace space) {
		if (space.spaceParameter.documents == null) {
			return null;
		}
		
		List<String> names = space.spaceParameter.documents.stream()
				.map(x -> x.getName())
				.collect(Collectors.toCollection(ArrayList<String>::new));

		CosineSimilarity similarity = new CosineSimilarity();
		RealMatrix docSimilarity = new Array2DRowRealMatrix(
				space.docSpace.getColumnDimension(),
				space.docSpace.getColumnDimension());
		for (int i = 0; i < docSimilarity.getColumnDimension(); i++) {
			for (int j = 0; j < docSimilarity.getColumnDimension(); j++) {
				double sim = similarity.computeSimilarity(
						space.docSpace.getColumnMatrix(i),
						space.docSpace.getColumnMatrix(j));
				docSimilarity.setEntry(i, j, 5 * sim + 5);
			}
		}

		return clusterMaker.cluster(names.toArray(new String[0]),
				docSimilarity.getData());
	}

	public HashMap<List<Boolean>, List<Integer>> dumbCluster(
			SemanticSpace space, int depth) {
		HashMap<Integer, List<Boolean>> clusterMap = Maps.newHashMap();

		for (int i = 0; i < depth; i++) {
			double[] row = space.docSpace.getRow(i);
			int docIdx = 0;
			for (double value : row) {
				boolean positive = value > 0.0;
				if (clusterMap.containsKey(docIdx)) {
					clusterMap.get(docIdx).add(positive);
				} else {
					clusterMap.put(docIdx, Lists.newArrayList(positive));
				}
				docIdx++;
			}
		}

		HashMap<List<Boolean>, List<Integer>> cluster = Maps.newHashMap();
		for (Map.Entry<Integer, List<Boolean>> entry : clusterMap.entrySet()) {
			if (cluster.containsKey(entry.getValue())) {
				cluster.get(entry.getValue()).add(entry.getKey());
			} else {
				cluster.put(entry.getValue(),
						Lists.newArrayList(entry.getKey()));
			}
		}

		LOGGER.info(cluster.toString());
		return cluster;
	}
}
