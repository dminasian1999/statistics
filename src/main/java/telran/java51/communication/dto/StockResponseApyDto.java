package telran.java51.communication.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseApyDto {
	
	LocalDate from;
	LocalDate to;
	String source;
	String type;
	IncomeApyDto minIncome;
	IncomeApyDto maxIncome;
}
