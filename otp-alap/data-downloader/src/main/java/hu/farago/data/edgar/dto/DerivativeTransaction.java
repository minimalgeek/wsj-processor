package hu.farago.data.edgar.dto;


public class DerivativeTransaction extends DerivativeBase {

	public DoubleValue conversionOrExercisePrice;
	public DateTimeValue exerciseDate;
	public DateTimeValue expirationDate;
	public UnderlyingSecurity underlyingSecurity;
	
	public static class UnderlyingSecurity {
		public StringValue underlyingSecurityTitle;
		public DoubleValue underlyingSecurityShares;
	}
}
