package telran.java51.communication.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.Getter;

@Getter
public class IndexPackageDto {

	List<String> indexs;
	List<Integer> amount;
	LocalTime from;
	LocalTime to;
	String type;
	Integer quantity;

}
