package telran.java51.communication.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@Setter
@Getter
@ToString

public class StockInfo {

	LocalDate date;
	double open;
	double high;
	double low;
	double close;
	double adjective_close;
	long volume;
}
