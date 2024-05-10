package telran.java51.communication.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Document(collection = "periods")
public class PeriodStats {
	
	@Id
	String id;
	String index;
	LocalDateTime dateOfPurchase;
	Double purchaseAmount;
	LocalDateTime dateOfSale;
	Double saleAmount;
	Double income;
	Double apy;

}
