package hu.farago.data.semantic;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.springframework.stereotype.Component;

import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;
import com.apporiented.algorithm.clustering.SingleLinkageStrategy;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;

@Component
public class ClusterMaker {

	public Cluster cluster(String[] names, double[][] distances) {
		ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
		Cluster cluster = alg.performClustering(distances, names,
				new SingleLinkageStrategy());
		
		exportClusterToPNG(cluster);
		
		return cluster;
	}

	private void exportClusterToPNG(Cluster cluster) {
		DendrogramPanel dp = new DendrogramPanel();
		dp.setModel(cluster);
		
		JFrame frame = new JFrame("FrameDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(dp, BorderLayout.CENTER);
		frame.setBounds(0, 0, 1280, 800);
		frame.setVisible(true);
		
		BufferedImage bi = new BufferedImage(frame.getSize().width, frame.getSize().height, BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics();
		frame.paint(g);
		g.dispose();
		try{ImageIO.write(bi,"png",new File("C:\\DEV\\temp\\cluster.png"));}catch (Exception e) {}
	}

}
