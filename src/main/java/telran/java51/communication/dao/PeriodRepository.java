package telran.java51.communication.dao;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.model.IncomeApy;


public interface PeriodRepository extends CrudRepository<IncomeApy, String> {

	//TODO fix this
//	@Aggregation({
//	    "{$match: { $elemMatch: { index: ?0, dateOfPurchase: ISODate({$toString: ?1}), dateOfSale: ISODate({$toString: ?2}) }}}",
//	    "{$group: { _id: null, count: { 'exists' } }}"
//	})
	
//	@Query("{'index': ?0, 'dateOfPurchase': ?1, 'dateOfSale': ?2}")	
    boolean existsByIndexIgnoreCaseAndDateOfPurchaseAndDateOfSale(String index, LocalDateTime dateOfPurchase, LocalDateTime dateOfSale);

	 
	 IncomeApy findFirstByIndexIgnoreCaseOrderByIncomeAsc(String index);

	 IncomeApy findFirstByIndexIgnoreCaseOrderByIncomeDesc(String index);
}
