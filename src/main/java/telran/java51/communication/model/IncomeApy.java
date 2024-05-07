package telran.java51.communication.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IncomeApy {

	LocalDate dateOfPurchase;
	Double purchaseAmount;
	LocalDate dateOfSale;
	Double saleAmount;
	Double income;
	Double apy;

}
