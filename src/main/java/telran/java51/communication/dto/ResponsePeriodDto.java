package telran.java51.communication.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponsePeriodDto {

	LocalDate from;
	LocalDate to;
	String source;
	String type;
	Double max;
	Double mean;
	Double median;
	Double min;
	Double std;

}
