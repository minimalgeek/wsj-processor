package hu.farago.data.sandp;

import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpiceHttpMethodBuilder {

	@Value("${spice.indices.url}")
	private String spiceIndicesUrl;
	
	@Value("${spice.indices.100}")
	private String spiceIndices100;
	
	@Value("${spice.indices.400}")
	private String spiceIndices400;
	
	@Value("${spice.indices.500}")
	private String spiceIndices500;
	
	@Value("${spice.indices.600}")
	private String spiceIndices600;
	
	public HttpMethod buildMethod(SAndPGroup group) {
		
		HttpMethod method = new PostMethod(spiceIndicesUrl);
		
		switch (group) {
		case SP100:
			method.setQueryString(spiceIndices100);
			break;
		case SP400:
			method.setQueryString(spiceIndices400);
			break;
		case SP500:
			method.setQueryString(spiceIndices500);
			break;
		case SP600:
			method.setQueryString(spiceIndices600);
			break;
		default:
			break;
		}
		
		return method;
	}
	
}
