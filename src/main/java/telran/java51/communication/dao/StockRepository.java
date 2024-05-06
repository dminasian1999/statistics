package telran.java51.communication.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.dto.StatisticDto;
import telran.java51.communication.model.Stock;

public interface StockRepository extends CrudRepository<Stock, String> {

	boolean existsByIndexIgnoreCase(String index);

	Stock findFirstByIndexIgnoreCaseOrderByDateAsc(String index);

	Stock findFirstByIndexIgnoreCaseOrderByDateDesc(String index);

	@Aggregation("{ $group: { _id: '$index' }}")
	Stream<String> findDistinctByIndex();

	@Aggregation({
	    "{$match: { index: ?0, date: { $gte: ?1, $lte: ?2 } } }",
	    "{$group:{ _id: null , max: {$max: '$close'}, min: {$min: '$close'}, mean: {$avg: '$close'}, median:{$median:{input: '$close', method: 'approximate'}} ,std:{$stdDevSamp: '$close'} }}"
	})
	StatisticDto findByIndexAndDateBetween(String index, LocalDate from, LocalDate to);

	@Aggregation({
	    "{$match: { index: ?0, date: { $gte: ?1, $lte: ?2 } } }",
	    "{$group:{ _id: '$close'} }"
	})
	Stream<Double> findByIndexAndDateBetween111(String index, LocalDate from, LocalDate to);
}
