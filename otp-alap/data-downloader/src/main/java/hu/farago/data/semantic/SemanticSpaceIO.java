package hu.farago.data.semantic;

import hu.farago.data.semantic.SimpleSemanticParser.SemanticSpace;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SemanticSpaceIO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SemanticSpaceIO.class);

	@Value("${semantic.file}")
	private String semanticFile;
	
	public void save(SemanticSpace space) {
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try{
		    fout = new FileOutputStream(semanticFile, true);
		    oos = new ObjectOutputStream(fout);
		    oos.writeObject(space);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		} finally {
		    IOUtils.closeQuietly(oos);
		}
	}
	
	public SemanticSpace load() {
		ObjectInputStream ois = null;
		FileInputStream fin = null;
		try {
		    fin = new FileInputStream(semanticFile);
		    ois = new ObjectInputStream(fin);
		    SemanticSpace readCase = (SemanticSpace) ois.readObject();
		    return readCase;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(ois);
		}
		return null;
	}
	
}
