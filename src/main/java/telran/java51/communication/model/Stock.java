package telran.java51.communication.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Document(collection = "market")
//@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Stock {
	@Id
	String id;
	String index;
	LocalDate date;
	double open;
	double high;
	double low;
	double close;
	double adjective_close;
	long volume;
	
	public Stock(String index, LocalDate date, double open, double high, double low, double close,
			double adjective_close, long volume) {
		this.index = index;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.adjective_close = adjective_close;
		this.volume = volume;
	}
	
	
}


