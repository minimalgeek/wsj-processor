package hu.farago.data.semantic.calc;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TfIdfCalculator implements Serializable {

	private static final long serialVersionUID = -8055162793845452681L;

	private class DocWithWordSet implements Serializable {

		private static final long serialVersionUID = 3433143570937953539L;

		public List<String> words;
		public Set<String> uniqueWords;
		
		public DocWithWordSet(List<String> words, Set<String> uniqueWords) {
			super();
			this.words = words;
			this.uniqueWords = uniqueWords;
		}
		
	}
	
	private List<DocWithWordSet> docs;
	private Set<String> words;
	private Map<String, Double> idf;
	private int minOccurance;

	public TfIdfCalculator(List<List<String>> strDocs, int minOccurance) {
		this.docs = Lists.newArrayList();
		for (List<String> doc : strDocs) {
			this.docs.add(new DocWithWordSet(doc, new HashSet<String>(doc)));
		}
		this.minOccurance = minOccurance;
		createRelevanWordSet();
		idf = Maps.newHashMap();
		for (String word : this.words) {
			idf.put(word, idf(word));
		}
	}

	private void createRelevanWordSet() {
		this.words = new HashSet<String>();
		for (DocWithWordSet doc : this.docs) {
			words.addAll(doc.uniqueWords);
		}

		Map<String, Integer> histogram = Maps.newHashMap(Maps.asMap(this.words,
				new Function<String, Integer>() {
					@Override
					public Integer apply(String input) {
						return 0;
					}
				}));
		for (DocWithWordSet doc : docs) {
			for (String word : doc.uniqueWords) {
				histogram.put(word, histogram.get(word) + 1);
			}
		}

		this.words = histogram.entrySet().stream()
				.filter(mapEntry -> mapEntry.getValue() >= this.minOccurance)
				.map(Map.Entry::getKey).collect(Collectors.toSet());
	}

	private double tf(List<String> doc, String term) {
		double result = 0;

		for (String word : doc) {
			if (term.equalsIgnoreCase(word))
				result++;
		}

		return result / doc.size();
	}

	private double idf(String term) {
		double n = 0;
		for (DocWithWordSet doc : docs) {
			if (doc.uniqueWords.contains(term)) {
				n++;
			}
		}
		return log2(docs.size() / n);
	}

	private double log2(double numb) {
		return Math.log(numb) / Math.log(2);
	}

	private double tfIdf(List<String> doc, String term) {
		return tf(doc, term) * idf.get(term);
	}
	
	public RealMatrix calculateTfIdf() {
		RealMatrix matrix = new Array2DRowRealMatrix(words.size(), docs.size());

		int row = 0, column = 0;
		for (DocWithWordSet doc : docs) {
			row = 0;
			for (String word : words) {
				matrix.setEntry(row, column, tfIdf(doc.words, word));
				row++;
			}
			column++;
		}
		return matrix;
	}

	public RealMatrix calculateTfIdfVector(List<String> newDoc) {
		RealMatrix matrix = new Array2DRowRealMatrix(1, words.size());

		int column = 0;
		for (String word : words) {
			matrix.setEntry(0, column, tfIdf(newDoc, word));
			column++;
		}
		return matrix;
	}

}
