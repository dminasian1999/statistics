package telran.java51.communication.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.dto.ResponsePeriodDto;
import telran.java51.communication.model.PeriodStats;

public interface PeriodRepository extends CrudRepository<PeriodStats, String> {

	Stream<PeriodStats> findByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetween(String index, String type, LocalDateTime from, LocalDateTime to);

	boolean existsByIndexIgnoreCaseAndDateOfPurchaseAndType(String index, LocalDateTime dateOfPurchase,
			String type);
//	boolean existsByIndexIgnoreCaseAndDateOfPurchaseAndDateOfSale(String index, LocalDateTime dateOfPurchase,
//			LocalDateTime dateOfSale);
//	Stream<PeriodStats> findByIndexIgnoreCaseAndDateOfPurchaseAndDateOfSale(String index, LocalDateTime dateOfPurchase,
//			LocalDateTime dateOfSale);
	
//	boolean existsByIndexIgnoreCaseAndDateOfPurchaseAndType(String index, LocalDateTime dateOfPurchase,
//			String type);
	

	PeriodStats findFirstByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByIncomeAsc(String index, String type, LocalDate from, LocalDate to);
	PeriodStats findFirstByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByIncomeDesc(String index, String type,LocalDate from, LocalDate to);

	@Aggregation({ "{$match: { index: ?0, dateOfPurchase: { $gte: ?1, $lte: ?2 } } }",
			"{$group:{ _id: null , max: {$max: '$income'}, min: {$min: '$income'}, mean: {$avg: '$income'}, median:{$median:{input: '$income', method: 'approximate'}} ,std:{$stdDevSamp: '$income'} }}" })
	ResponsePeriodDto calcAnalysis(String index, LocalDate from, LocalDate to);

	@Aggregation({ "{$match: { index: ?0, dateOfPurchase: { $gte: ?1, $lte: ?2 } } }",
			"{$group:{ _id: null , max: {$max: '$income'}, min: {$min: '$income'}, mean: {$avg: '$income'}, median:{$median:{input: '$income', method: 'approximate'}} ,std:{$stdDevSamp: '$income'} }}" ,
			"{$project  :{ max: {$multiply: ['$max', ?3]}, min: {$multiply: ['$min', ?3]}, mean: {$multiply: ['$mean', ?3]},  median: '$median',   std: {$multiply: ['$std', ?3]} }}"})
	ResponsePeriodDto calcAnalysis(String index, LocalDate from, LocalDate to,  Double amount);
	
	void deleteAllByIndexIgnoreCase(String index);

}
