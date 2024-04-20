package telran.java51.communication.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.Getter;

@Getter
public class IndexResponseApyDto {
	
	LocalTime from;
	LocalTime to;
	List<String> source;
	String type;
	IncomeApyDto minIncome;
	IncomeApyDto maxIncome;
}
