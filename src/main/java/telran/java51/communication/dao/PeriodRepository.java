package telran.java51.communication.dao;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import telran.java51.communication.model.Period;

public interface PeriodRepository extends CrudRepository<Period, String> {


	boolean existsByIndexIgnoreCaseAndDateOfPurchaseAndDateOfSale(String index, LocalDateTime dateOfPurchase,
			LocalDateTime dateOfSale);

	Period findFirstByIndexIgnoreCaseOrderByIncomeAsc(String index);

	Period findFirstByIndexIgnoreCaseOrderByIncomeDesc(String index);
}
