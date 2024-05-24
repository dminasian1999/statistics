
package telran.java51.communication.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.model.StockInfo;

public interface StockInfoRepository extends CrudRepository<StockInfo, String>  {
	
	@Aggregation("{ $group: { _id: '$_id' }}")
	List<String> getIndexes();
	
}
