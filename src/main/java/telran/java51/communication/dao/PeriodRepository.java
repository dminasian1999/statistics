package telran.java51.communication.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.model.IncomeApy;
import telran.java51.communication.model.Stock;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;


public interface PeriodRepository extends CrudRepository<IncomeApy, String> {

	//TODO fix this
	 boolean existsByIndexAndDateOfPurchaseAndDateOfSale(String index, LocalDateTime firstDate, LocalDateTime lastDate);
	 
	 IncomeApy findFirstByIndexIgnoreCaseOrderByIncomeAsc(String index);

	 IncomeApy findFirstByIndexIgnoreCaseOrderByIncomeDesc(String index);
}
