package telran.java51.communication.dto;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class IncomeIrrDto {

	String index;
	LocalDate dateOfPurchase;
	Double purchaseAmount;
	LocalDate dateOfSale;
	Double saleAmount;
	Double income;
	Double irr;
}
