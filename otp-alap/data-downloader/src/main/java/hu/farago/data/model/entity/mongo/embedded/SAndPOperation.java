package hu.farago.data.model.entity.mongo.embedded;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SAndPOperation {
	
	public SAndPGroup indexGroup;
	public Event event;
	public DateTime eventDate;
	
	@Override
	public boolean equals(Object obj) {
		SAndPOperation other = (SAndPOperation) obj;
		
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(eventDate.withZone(DateTimeZone.UTC), other.eventDate.withZone(DateTimeZone.UTC));
		builder.append(indexGroup.name, other.indexGroup.name);
		
		return builder.isEquals();
	}
	
	public static enum Event {
		ADD("Add"), DROP("Drop"); //, DIVIDEND("Dividend"), MERGER("Merger/Acquisition"), SPIN_OFF("Spin-Off");
		
		private String name;
		
		Event(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static Event getByName(String str) {
			for (Event event : Event.values()) {
				if (event.name.equals(str)) {
					return event;
				}
			}
			
			return null;
		}
	}
	
	public static enum SAndPGroup {
		
		SP100("S&P 100"), SP400("S&P 400"), SP500("S&P 500"), SP600("S&P 600");
		
		private String name;
		
		SAndPGroup(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static SAndPGroup getByName(String str) {
			for (SAndPGroup event : SAndPGroup.values()) {
				if (event.name.equals(str)) {
					return event;
				}
			}
			
			return null;
		}
	}
}
