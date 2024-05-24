package telran.java51.communication.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "periods")
public class PeriodStats {
	
	
	String id;
	String index;
	LocalDate dateOfPurchase;
	double purchaseAmount;
	LocalDate dateOfSale;
	double saleAmount;
	double income;
	
	String type;
	
	Double apy;
	Double irr;

}