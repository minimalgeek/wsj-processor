package hu.farago.data.semantic.calc;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class TfIdfCalculator {

	private List<List<String>> docs;
	private Set<String> words;
	private Map<String, Double> idf;
	private int minOccurance;

	public TfIdfCalculator(List<List<String>> docs, int minOccurance) {
		this.docs = docs;
		this.minOccurance = minOccurance;
		createRelevanWordSet(docs);
		idf = Maps.newHashMap();
		for (String word : this.words) {
			idf.put(word, idf(word));
		}
	}

	private void createRelevanWordSet(List<List<String>> docs) {
		this.words = new HashSet<String>();
		for (List<String> doc : docs) {
			words.addAll(doc);
		}

		Map<String, Integer> histogram = Maps.newHashMap(Maps.asMap(this.words,
				new Function<String, Integer>() {
					@Override
					public Integer apply(String input) {
						return 0;
					}
				}));
		for (List<String> doc : docs) {
			for (String word : doc) {
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
		for (List<String> doc : docs) {
			for (String word : doc) {
				if (term.equalsIgnoreCase(word)) {
					n++;
					break;
				}
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
		for (List<String> doc : docs) {
			row = 0;
			for (String word : words) {
				matrix.setEntry(row, column, tfIdf(doc, word));
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
