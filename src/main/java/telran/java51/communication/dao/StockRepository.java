package telran.java51.communication.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.model.Stock;

public interface StockRepository extends CrudRepository<Stock, String > {
	
	boolean existsByIndexIgnoreCase(String index);
	Stock findFirstByIndexIgnoreCaseOrderByDateAsc(String index);
	Stock findFirstByIndexIgnoreCaseOrderByDateDesc(String index);

	
}
