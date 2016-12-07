package hu.farago.data.seekingalpha;

import org.jsoup.nodes.Document;

public class ProcessFirstNArticleParameter {
	public String index;
	public Document document;
	public int count;

	public ProcessFirstNArticleParameter(String index) {
		this.index = index;
		this.count = 1;
	}
	
	public ProcessFirstNArticleParameter(String index, int count) {
		this.index = index;
		this.count = count;
	}
	
	public ProcessFirstNArticleParameter(String index, Document document, int count) {
		this.index = index;
		this.document = document;
		this.count = count;
	}
}