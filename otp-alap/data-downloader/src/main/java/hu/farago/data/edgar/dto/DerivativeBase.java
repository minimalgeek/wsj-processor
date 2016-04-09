package hu.farago.data.edgar.dto;

public abstract class DerivativeBase {

	public StringValue securityTitle;
	public DateTimeValue transactionDate;
	public DateTimeValue deemedExecutionDate;
	public TransactionCoding transactionCoding;
	public StringValue transactionTimeliness;
	public TransactionAmounts transactionAmounts;
	public PostTransactionAmounts postTransactionAmounts;
	public OwnershipNature ownershipNature;
	
	public static class OwnershipNature {
		public StringValue directOrIndirectOwnership;
	}
	
	public static class PostTransactionAmounts {
		public DoubleValue sharesOwnedFollowingTransaction;
	}
	
	public static class TransactionCoding {
		public int transactionFormType;
		public String transactionCode;
		public int equitySwapInvolved;
	}
	
	public static class TransactionAmounts {
		public DoubleValue transactionShares;
		public DoubleValue transactionPricePerShare;
		public StringValue transactionAcquiredDisposedCode;
	}
}
