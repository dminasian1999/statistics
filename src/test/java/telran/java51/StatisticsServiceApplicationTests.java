package telran.java51;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import telran.java51.communication.dao.PeriodRepository;
import telran.java51.communication.dao.StockInfoRepository;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.dto.exceptions.StockNotFoundException;
import telran.java51.communication.model.Stock;
import telran.java51.communication.service.CommunicationService;
import telran.java51.communication.service.CommunicationServiceImpl;

@SpringBootTest
@ActiveProfiles("test")

class StatisticsServiceApplicationTests {
	CommunicationService communication;
	ModelMapper modelMapper;
	@Mock
	StockRepository stockRepository ;
	PeriodRepository periodRepository;
	StockInfoRepository stockInfoRepository;
	ReadWriteLock rwl = new ReentrantReadWriteLock();

	@BeforeEach
    void setUp() throws Exception {
		communication =  new CommunicationServiceImpl(modelMapper, stockRepository, periodRepository, stockInfoRepository);
		List<Stock> stocks = (Arrays.asList(
			new Stock("FIRST", LocalDate.of(1999, 10, 21), 0.0, 0.0, 0.0, 10.0, 0.0, 0),
			new Stock("SECOND", LocalDate.of(2000, 10, 21), 0.0, 0.0, 0.0, 20.0, 0.0, 0),
			new Stock("THIRD", LocalDate.of(2001, 10, 21), 0.0, 0.0, 0.0, 30.0, 0.0, 0),
			new Stock("FORTH", LocalDate.of(2002, 10, 21), 0.0, 0.0, 0.0, 40.0, 0.0, 0),
			new Stock("FIFTH", LocalDate.of(2003, 10, 21),  0.0, 0.0, 0.0, 50.0, 0.0, 0)
			));
		stocks.forEach(t -> stockRepository.save(t));		
		List<String > indexes = stocks.stream().map(t -> t.getIndex()).toList();
		when(stockRepository.getIndexes()).thenReturn(indexes);
        Pattern pattern = Pattern.compile("FIRST|SECOND|THIRD|FOURTH|FIFTH", Pattern.CASE_INSENSITIVE);
		when(stockRepository.existsByIndexIgnoreCase(argThat(arg -> pattern.matcher(arg).matches()))).thenReturn(true);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testAddHistoryWithFile() throws Exception {
		// Test implementation
	}

	@Test
	void testGetTimeHistoryForIndex() {
		//TODO to be continued
        StockNotFoundException exception =  assertThrows(StockNotFoundException.class, ()->communication.getTimeHistoryForIndex("SIXTH"));
		assertEquals("Stock not found: " + "SIXTH", exception.getMessage());
	}

	@Test
	void testGetAllIndexes() {	
		String [] expected = {"FIRST", "SECOND","THIRD", "FORTH", "FIFTH"};
		String[] actual = communication.getAllIndexes().stream()
							.toArray(String[]::new);		
		assertArrayEquals(expected,actual);
	}

	@Test
	void testPeriodBeetwin() {
		// Test implementation
	}

	@Test
	void testGetAllValueCloseBetween() {
		// Test implementation
	}

	@Test
	void testCalcSumPackage() {
		// Test implementation
	}

	@Test
	void testCalcIncomeWithApy() {
		// Test implementation
	}

	@Test
	void testCalcIncomeWithApyAllDate() {
		// Test implementation
	}

	@Test
	void testCalcIncomeWithIrr() {
		// Test implementation
	}

	@Test
	void testCalcCorrelation() {
		// Test implementation
	}

	@Test
	void testDeleteAllHistoryForCompany() {
		// Test implementation
	}

}
