package telran.java51.communication.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.dto.IncomeDto;
import telran.java51.communication.model.PeriodStats;

public interface PeriodRepository extends CrudRepository<PeriodStats, String> {

	Stream<PeriodStats> findByIndexIgnoreCaseAndDateOfPurchaseBetween(String index, LocalDate from, LocalDate to);

	boolean existsByIndexIgnoreCaseAndDateOfPurchaseAndDateOfSale(String index, LocalDateTime dateOfPurchase,LocalDateTime dateOfSale);

	PeriodStats findFirstByIndexIgnoreCaseOrderByIncomeAsc(String index);

	PeriodStats findFirstByIndexIgnoreCaseOrderByIncomeDesc(String index);
	
//	@Aggregation({
//	    "{$match: { index: ?0, dateOfPurchase: { $gte: ?1, $lte: ?2 } } }",
//	    "{$group:{ _id: null , max: {$max: '$income'}, min: {$min: '$income'}, mean: {$avg: '$income'}, median:{$median:{input: '$income', method: 'approximate'}} ,std:{$stdDevSamp: '$income'} }}"
//	})
//	IncomeDto findByIndexAndDateBetweenPeriod(String index, LocalDate from, LocalDate to);
	
	@Aggregation({
	    "{$match: { index: { $in: ?0 }, dateOfPurchase: { $gte: ?1, $lte: ?2 } } }",
	    "{$group:{ _id: null , max: {$max: '$income'}, min: {$min: '$income'}, mean: {$avg: '$income'}, median:{$median:{input: '$income', method: 'approximate'}} ,std:{$stdDevSamp: '$income'} }}",
	})
	IncomeDto findByIndexAndDateBetweenPeriod(List<String> indexes, LocalDate from, LocalDate to);

}
