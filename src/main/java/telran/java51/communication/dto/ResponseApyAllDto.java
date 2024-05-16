package telran.java51.communication.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApyAllDto {

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
