package hu.farago.mongo.flatten;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AggregateRunner {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AggregateRunner.class);

	@Value("${aggregate.path}")
	private String aggregatePath;

	@Value("${aggregate.mongoPath}")
	private String aggregateMongoPath;

	@Value("${mongo.host}")
	private String mongoHost;

	@Value("${mongo.port}")
	private String mongoPort;

	@Value("${mongo.db}")
	private String mongoDB;

	public void executeScript(String scriptPath) throws IOException,
			InterruptedException {
		Runtime rt = Runtime.getRuntime();

		StringBuilder fullCommand = new StringBuilder();
		fullCommand.append("\"");
		fullCommand.append(aggregateMongoPath);
		fullCommand.append("mongo.exe");
		fullCommand.append("\" ");
		fullCommand.append(mongoHost);
		fullCommand.append(":");
		fullCommand.append(mongoPort);
		fullCommand.append("/");
		fullCommand.append(mongoDB);
		fullCommand.append(" ");
		fullCommand.append(scriptPath);

		Process pr = rt.exec(fullCommand.toString());

		pr.waitFor();

		if (pr.getErrorStream() != null) {
			LOGGER.error(IOUtils.toString(pr.getErrorStream()));
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				pr.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			LOGGER.info(line);
		}

	}

	public void runAllScriptsInDirectory() throws IOException,
			InterruptedException {
		Collection<File> files = FileUtils.listFiles(
				FileUtils.getFile(aggregatePath), new String[] { "js" }, true);

		for (File file : files) {
			String path = file.getPath();
			LOGGER.info("Running: " + path);
			executeScript(path);
		}
	}
}
