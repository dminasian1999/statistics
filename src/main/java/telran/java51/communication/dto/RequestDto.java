package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class RequestDto {

	List<String> indexs;
	String type;
	Integer quantity;
	LocalDate from;
	LocalDate to;
}
