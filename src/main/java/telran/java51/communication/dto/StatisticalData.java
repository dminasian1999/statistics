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
public class StatisticalData {

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
