package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class StockResponseIrrDto {

	LocalDate from;
	LocalDate to;
	List<String> source;
	String type;
	IncomeIrrDto minIncome;
	IncomeIrrDto maxIncome;
}
