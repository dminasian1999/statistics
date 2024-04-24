package telran.java51.communication.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "Indexes")
@EqualsAndHashCode(of = "name")
public class Index {
	
	@Id
	String name;
	@Setter
	LocalDate lastUpDate;
	@Setter
	Double purchaseAmount;
	@Setter
	Double saleAmount;

}
