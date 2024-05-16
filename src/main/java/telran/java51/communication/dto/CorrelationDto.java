package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class CorrelationDto  {

	List<String> indexs;
    LocalDate from;
    LocalDate to;
}
