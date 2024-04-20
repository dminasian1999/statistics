package telran.java51.communication.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.Getter;

@Getter
public class IndexResponseValueCloseDto {

	LocalTime from;
	LocalTime to;
	String source;
	String type;
	LocalTime minDate;
	LocalTime maxDate;
	Double startClose;
	Double endClose;
	Double valueClose;
	List<Double> listClose;
}
