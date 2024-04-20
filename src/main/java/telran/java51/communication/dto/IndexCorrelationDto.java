package telran.java51.communication.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.Getter;

@Getter
public class IndexCorrelationDto {

	List<String> indexs ;
    LocalTime from;
    LocalTime to;
}
