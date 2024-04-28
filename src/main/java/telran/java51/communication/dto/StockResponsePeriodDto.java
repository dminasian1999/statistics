package telran.java51.communication.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.Getter;

@Getter
public class StockResponsePeriodDto {

	LocalTime from;
	LocalTime to;
	List<String> source;
	String type;
	Integer max;
	Integer mean;
	Integer median;
	Integer min;
	Integer std;

}
