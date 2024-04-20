package telran.java51.communication.dto;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class IndexResponseApyAllDto {

	String source;
	LocalDate historyFrom;
	LocalDate historyTo;
	String type;
	LocalDate from;
	LocalDate to;
	Double purchaseAmount;
	Double saleAmount;
	Double income;
	Double apy;
}
