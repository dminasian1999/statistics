package telran.java51.communication.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.Getter;

@Getter
public class StockDto {

	List<String> indexs;
	String type;
	Integer quantity;
	LocalTime from;
	LocalTime to;
}
