package telran.java51.communication.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;


import telran.java51.communication.model.Stock;


public interface StockRepository extends CrudRepository<Stock, String > {
	
	boolean existsByIndexIgnoreCase(String index);
	Stock findFirstByIndexIgnoreCaseOrderByDateAsc(String index);
	Stock findFirstByIndexIgnoreCaseOrderByDateDesc(String index);
	
    Stream<Stock> findByIndexAndDateBetween(String index, LocalDate from, LocalDate to);
	
//	Set<Stock> findDistinctByOrderByIndexAsc();
	
}
