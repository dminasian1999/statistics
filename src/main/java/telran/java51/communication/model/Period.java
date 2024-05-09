package telran.java51.communication.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Document(collection = "periods")
//@EqualsAndHashCode(of =  {"index","dateOfPurchase" })
public class Period {
	
	@Id
	String id;
	String index;
	LocalDate dateOfPurchase;
	Double purchaseAmount;
	LocalDate dateOfSale;
	Double saleAmount;
	Double income;
	Double apy;

}
