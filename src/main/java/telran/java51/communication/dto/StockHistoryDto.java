package telran.java51.communication.dto;

import java.time.LocalTime;

import lombok.Getter;

@Getter
public class StockHistoryDto {
	String source ;
    LocalTime fromData;
    LocalTime toData;
}
