package hu.farago.data.api;

import hu.farago.data.model.entity.mongo.UrlBasedEntity;
import hu.farago.data.utils.URLUtils;

import java.math.BigInteger;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.google.common.collect.Lists;

public abstract class UrlBasedDownloader<T extends UrlBasedEntity> {

	private static final int PAGE_SIZE = 50;

	protected abstract Logger getLogger();

	protected abstract List<String> buildUrls() throws Exception;

	protected abstract T buildDocument(Document document);

	protected abstract MongoRepository<T, BigInteger> getRepository();

	public void downloadAndSaveAll() throws Exception {
		getRepository().deleteAll();
		List<T> temp = Lists.newArrayList();

		int idx = 0;
		for (String url : buildUrls()) {
			getLogger().info("Download content from: " + url);

			String siteContent = null;
			try {
				siteContent = URLUtils.getHTMLContentOfURL(url);
			} catch (Exception e) {
				getLogger().info("URL is not valid, skip this: " + url);
				getLogger().error(e.getMessage(), e);
			}

			if (siteContent != null) {
				Document document = Jsoup.parse(siteContent);

				try {
					T doc = buildDocument(document);
					doc.url = url;
					temp.add(doc);
				} catch (Exception e) {
					getLogger().error("Error in: " + url);
					getLogger().error(e.getMessage(), e);
				}
			}

			idx++;
			if (idx >= PAGE_SIZE) {
				idx = 0;
				getRepository().save(temp);
				temp.clear();
			}
		}

		if (idx != 0) {
			getRepository().save(temp);
		}
	}

}
