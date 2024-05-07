package telran.java51.communication.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.CrudRepository;

import lombok.Synchronized;
import telran.java51.communication.dto.IncomeApyDto;
import telran.java51.communication.model.Stock;

public interface StockRepository extends CrudRepository<Stock, String> {

	boolean existsByIndexIgnoreCase(String index);

	Stock findFirstByIndexIgnoreCaseOrderByDateAsc(String index);

	Stock findFirstByIndexIgnoreCaseOrderByDateDesc(String index);

	@Aggregation("{ $group: { _id: '$index' }}")
	Stream<String> findDistinctByIndex();

	
//	@Aggregation({
//	    "{$match: { index: ?0, date: { $gte: ?1, $lte: ?2 } } }",
//	    "{$group:{ _id: null , max: {$max: '$close'}, min: {$min: '$close'}, mean: {$avg: '$close'}, median:{$median:{input: '$close', method: 'approximate'}} ,std:{$stdDevSamp: '$close'} }}"
//	})
//	StatisticDto findByIndexAndDateBetween(String index, LocalDate from, LocalDate to);
	
	@Aggregation({
	    "{$match: { index: ?0, date: { $gte: ?1, $lte: ?2 } } }",
	    "{$group:{ _id: '$close'}}"
	})
	List<Double> findByIndexAndDateBetween2(String index, LocalDate from, LocalDate to);
	
	@Aggregation({
	    "{$match: { index: ?0, date: { $gte: ?1, $lte: ?2 } } }",
	    "{$group:{ _id: null, first: {$first: '$close'}, last:{$last: '$close'}}}",
	    "{$project:{dateOfPurchase: ?1, purchaseAmount:'$first',dateOfSale: ?2,saleAmount: '$last' ,income:{$subtract:[{$pow:[{$divide:['$last','$first']}, ?3]}, 1]}}}"
	})
	 IncomeApyDto calcIncomeForPeriod(String index, LocalDateTime from, LocalDateTime to, int power);
}
