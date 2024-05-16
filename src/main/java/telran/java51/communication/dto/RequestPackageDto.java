package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class RequestPackageDto {

	List<String> indexs;
	List<Integer> amount;
	LocalDate from;
	LocalDate to;
	String type;
	Integer quantity;

}
