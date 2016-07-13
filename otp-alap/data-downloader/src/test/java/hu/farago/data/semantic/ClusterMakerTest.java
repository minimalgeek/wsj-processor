package hu.farago.data.semantic;

import hu.farago.data.config.AbstractRootTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.apporiented.algorithm.clustering.Cluster;

public class ClusterMakerTest extends AbstractRootTest {

	@Autowired
	private ClusterMaker clustering;
	
	@Test
	public void clusterTest() {
		
		String[] names = new String[] { "O1", "O2", "O3", "O4", "O5", "O6", "07" };
		double[][] distances = new double[][] { 
		    { 0, 1, 9, 7, 11, 14, 1 },
		    { 1, 0, 4, 3, 8, 10, 0 }, 
		    { 9, 4, 0, 9, 2, 8, 3 },
		    { 7, 3, 9, 0, 6, 13, 0 }, 
		    { 11, 8, 2, 6, 0, 10, 6 },
		    { 14, 10, 8, 13, 10, 0, 0 },
		    { 14, 10, 8, 13, 10, 0, 9 }};
		
		Cluster cluster = clustering.cluster(names, distances);
		Assert.assertNotNull(cluster);
	}
	
}
