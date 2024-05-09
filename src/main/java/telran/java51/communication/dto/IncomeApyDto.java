package telran.java51.communication.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class IncomeApyDto {
	
	LocalDate dateOfPurchase;
	Double purchaseAmount;
	LocalDate dateOfSale;
	Double saleAmount;
	Double income;
	Double apy;
}
