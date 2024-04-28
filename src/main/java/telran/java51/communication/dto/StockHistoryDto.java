package telran.java51.communication.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class StockHistoryDto {
	String source ;
    LocalDate fromData;
    LocalDate toData;
}
