package telran.java51.communication.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Document(collection = "market")
@EqualsAndHashCode(of = "index")
public class Stock {
	
	@Id
	String index;
	@Setter
	String name;
	
	List<StockInfo> history ;
	
	
	
	public boolean addHistory(StockInfo history) {
		return this.history.add(history);
	}



	public Stock(String index) {
		this.index = index;
		history =  new ArrayList<>();
	}

}
