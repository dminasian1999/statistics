package telran.java51.communication.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	LocalDateTime dateOfPurchase;
	Double purchaseAmount;
	LocalDateTime dateOfSale;
	Double saleAmount;
	Double income;
	Double apy;

}
