package telran.java51.communication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class StatisticDto {
	Double max;
	Double min;
	Double mean;
	Double median;
	Double std;

}
