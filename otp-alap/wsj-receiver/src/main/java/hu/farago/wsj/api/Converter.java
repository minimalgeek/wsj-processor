package hu.farago.wsj.api;

public interface Converter<A, B> {

	A convertFrom(B obj);
	
	B convertTo(A obj);
	
}
