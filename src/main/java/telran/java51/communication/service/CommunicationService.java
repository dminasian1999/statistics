package telran.java51.communication.service;

import java.util.List;

import telran.java51.communication.dto.StockCorrelationDto;
import telran.java51.communication.dto.StockDto;
import telran.java51.communication.dto.StockHistoryDto;
import telran.java51.communication.dto.StockPackageDto;
import telran.java51.communication.dto.StockResponseApyAllDto;
import telran.java51.communication.dto.StockResponseApyDto;
import telran.java51.communication.dto.StockResponseIrrDto;
import telran.java51.communication.dto.StockResponsePeriodDto;
import telran.java51.communication.dto.StockResponseValueCloseDto;

public interface CommunicationService {

	boolean addHistoryWithFile(String indexForHistory,String csv);
	
	StockHistoryDto getTimeHistoryForIndex(String index);
	
	List<String> getAllIndexes();
	
	List<StockResponsePeriodDto> periodBeetwin(StockDto index);
	
	List<StockResponseValueCloseDto> getAllValueCloseBetween(StockDto index);
	
	StockResponsePeriodDto calcSumPackage(StockPackageDto indexPackage);
	
	StockResponseApyDto calcIncomeWithApy (StockDto index);
	
	StockResponseApyAllDto calcIncomeWithApyAllDate (StockDto index); 
	
	StockResponseIrrDto calcIncomeWithIrr(StockDto index);
	
	String calcCorrelation(StockCorrelationDto index);
	
	boolean deleteAllHistoryForCompany(String index);
}
