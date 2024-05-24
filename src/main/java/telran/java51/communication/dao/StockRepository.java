
package telran.java51.communication.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.model.PeriodStats;
import telran.java51.communication.model.Stock;

public interface StockRepository extends CrudRepository<Stock, String> {

	boolean existsByIndexIgnoreCase(String index);

	Stock findFirstByIndexOrderByDateAsc(String index);

	Stock findFirstByIndexOrderByDateDesc(String index);

	@Aggregation({ "{$match: { index: ?0, date: { $gte: ?1, $lt: ?2 } } }", "{$sort: { date: 1 } }",
		  "{$group:{ _id: '$close'}}"})
	List<Double> getClosesByIndexAndDateBetween(String index, LocalDate from, LocalDate to);

	@Aggregation({ "{$match: { index: ?0, date: { $gte: ?1, $lte: ?2 } } }", "{$sort: { date: 1 } }",
		 "{$group:{ _id: null, first: {$first: '$close'}, last:{$last: '$close'}}}",
		 "{$project:{index: ?0, type: ?3, dateOfPurchase: ?1, purchaseAmount:'$first',dateOfSale: ?2,saleAmount: '$last', income:{$subtract: ['$last','$first']}}}" })
	PeriodStats calcIncomeForPeriod(String index, LocalDate from, LocalDate to, String type);

	@Aggregation({ "{$match: { index: ?0, date: { $gte: ?1, $lte: ?2 } } }", "{$sort: { date: 1 } }",
		"{$group:{ _id: null, first: {$first: '$close'}, last:{$last: '$close'}}}",
		"{$project:{index: ?0 ,type: ?4, dateOfPurchase: ?1, purchaseAmount:'$first',dateOfSale: ?2,saleAmount: '$last', income:{$subtract: ['$last','$first']} ,apy:{$subtract:[{$pow:[{$divide:[?3,'$first']}, 1]}, 1]}}}" })
	PeriodStats calcIncomeForPeriodWithApy(String index, LocalDate from, LocalDate to, double apyYear, String type);

	void deleteAllByIndexIgnoreCase(String index);
}
