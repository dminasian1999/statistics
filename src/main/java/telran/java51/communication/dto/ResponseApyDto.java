package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApyDto {
	
	LocalDate from;
	LocalDate to;
	List<String> source;
	String type;
	IncomeApyDto minIncome;
	IncomeApyDto maxIncome;
}
