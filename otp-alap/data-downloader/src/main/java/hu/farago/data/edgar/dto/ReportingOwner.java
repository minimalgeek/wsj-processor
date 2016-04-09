package hu.farago.data.edgar.dto;

public class ReportingOwner {

	public ReportingOwnerId reportingOwnerId;
	public ReportingOwnerAddress reportingOwnerAddress;
	public ReportingOwnerRelationship reportingOwnerRelationship;

	public static class ReportingOwnerId {
		public String rptOwnerCik;
		public String rptOwnerName;
	}

	public static class ReportingOwnerAddress {
		public String rptOwnerStreet1;
		public String rptOwnerStreet2;
		public String rptOwnerCity;
		public String rptOwnerState;
		public int rptOwnerZipCode; 
		public String rptOwnerStateDescription;
	}

	public static class ReportingOwnerRelationship {
		public boolean isDirector;
		public boolean isOfficer;
		public boolean isTenPercentOwner;
		public boolean isOther;
		public String officerTitle;
	}
}
