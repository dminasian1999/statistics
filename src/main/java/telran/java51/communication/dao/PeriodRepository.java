
package telran.java51.communication.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.dto.ResponsePeriodDto;
import telran.java51.communication.model.PeriodStats;

public interface PeriodRepository extends CrudRepository<PeriodStats, String> {

//	@Aggregation({ "{$match: { index: ?0, type: ?1, date: { $gte: ?2, $lte: ?3 } } }", 
//		   "{$sort: { date: -1 } }"
//
//		   })
	Stream<PeriodStats> findByIndexAndTypeAndDateOfPurchaseBetweenOrderByDateOfPurchaseAsc(String index, String type, LocalDate from, LocalDate to);
//	Stream<PeriodStats> findByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByDateOfPurchase(String index, String type, LocalDateTime from, LocalDateTime to);

	
	boolean existsByIndexIgnoreCaseAndDateOfPurchaseAndType(String index, LocalDate dateOfPurchase,String type);
	
	PeriodStats findFirstByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByIncomeAsc(String index, String type, LocalDateTime from, LocalDateTime to);
	PeriodStats findFirstByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByIncomeDesc(String index, String type,LocalDateTime from, LocalDateTime to);

	@Aggregation({ "{$match: { index: ?0, dateOfPurchase: { $gte: ?1, $lte: ?2 } } }",

			"{$group:{ _id: null , max: {$max: '$income'}, min: {$min: '$income'}, mean: {$avg: '$income'}, median:{$median:{input: '$income', method: 'approximate'}} ,std:{$stdDevSamp: '$income'} }}" })
	ResponsePeriodDto calcAnalysis(String index, LocalDate from, LocalDate to);

	@Aggregation({ "{$match: { index: ?0, dateOfPurchase: { $gte: ?1, $lte: ?2 } } }",

			"{$group:{ _id: null , max: {$max: '$income'}, min: {$min: '$income'}, mean: {$avg: '$income'}, median:{$median:{input: '$income', method: 'approximate'}} ,std:{$stdDevSamp: '$income'} }}" ,
			"{$project  :{ max: {$multiply: ['$max', ?3]}, min: {$multiply: ['$min', ?3]}, mean: {$multiply: ['$mean', ?3]},  median: '$median',   std: {$multiply: ['$std', ?3]} }}"})
	ResponsePeriodDto calcAnalysis(String index, LocalDate from, LocalDate to,  Double amount);
	
	void deleteAllByIndexIgnoreCase(String index);

}
