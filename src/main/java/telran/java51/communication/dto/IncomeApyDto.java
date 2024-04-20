package telran.java51.communication.dto;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class IncomeApyDto {
	
	LocalDate dateOfPurchase;
	Double purchaseAmount;
	LocalDate dateOfSale;
	Double saleAmount;
	Double income;
	Double apy;
}
