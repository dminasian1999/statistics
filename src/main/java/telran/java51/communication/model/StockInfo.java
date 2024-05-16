package telran.java51.communication.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "indexName")
@Document(collection = "indexes")


public class StockInfo {
	@Id
	String indexName; 
	LocalDate history;
	
	public StockInfo(String indexName) {
		this.indexName = indexName;
	}
	
	
}
