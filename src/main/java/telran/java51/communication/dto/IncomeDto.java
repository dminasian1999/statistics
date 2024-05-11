package telran.java51.communication.dto;

import java.time.LocalDate;

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
public class IncomeDto {

//	Double income;
	LocalDate from;
	LocalDate to;
    String source;
    String type;
    double max;
    double mean;
    double median;
    double min;
    double std;

}
