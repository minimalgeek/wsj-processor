package hu.farago.data.semantic.calc;

import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Implements Cosine Similarity for a term document matrix. A o B = x1*x2 +
 * y1*y2 dist(A,0) = sqrt((xa-x0)^2 + (ya-y0)^2) == |A| Therefore: sim(A,B) =
 * cos t = A o B/|A|x|B|
 * 
 * @author Sujit Pal
 * @version $Revision: 21 $
 */
public class CosineSimilarity {

	public double computeSimilarity(RealMatrix sourceDoc, RealMatrix targetDoc) {
		if (sourceDoc.getRowDimension() != targetDoc.getRowDimension()
				|| sourceDoc.getColumnDimension() != targetDoc
						.getColumnDimension()
				|| sourceDoc.getColumnDimension() != 1) {
			throw new IllegalArgumentException(
					"Matrices are not column matrices or not of the same size");
		}
		double[] source = sourceDoc.getColumn(0);
		double[] target = targetDoc.getColumn(0);
		
		double dotProduct = dot(source, target);
		double distance = norm(source) * norm(target);
		return dotProduct / distance;
	}

	private double dot(double[] source, double[] target) {
		double sum = 0.0;
		for (int i = 0; i < source.length; i++) {
			sum += source[i]*target[i];
		}
		return sum;
	}
	
	private double norm(double[] vector) {
		double sum = 0.0;
		for (double num : vector) {
			sum += Math.pow(num, 2);
		}
		return Math.sqrt(sum);
	}
}
