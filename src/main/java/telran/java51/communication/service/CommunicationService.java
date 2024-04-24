package telran.java51.communication.service;

import java.util.List;

import telran.java51.communication.dto.IndexCorrelationDto;
import telran.java51.communication.dto.IndexDto;
import telran.java51.communication.dto.IndexHistoryDto;
import telran.java51.communication.dto.IndexPackageDto;
import telran.java51.communication.dto.IndexResponseApyAllDto;
import telran.java51.communication.dto.IndexResponseApyDto;
import telran.java51.communication.dto.IndexResponseIrrDto;
import telran.java51.communication.dto.IndexResponsePeriodDto;
import telran.java51.communication.dto.IndexResponseValueCloseDto;

public interface CommunicationService {

	boolean addHistoryWithFile();
	
	IndexHistoryDto getTimeHistoryForIndex();
	
	List<String> getAllIndexes();
	
	List<IndexResponsePeriodDto> periodBeetwin(IndexDto index);
	
	List<IndexResponseValueCloseDto> getAllValueCloseBetween(IndexDto index);
	
	IndexResponsePeriodDto calcSumPackage(IndexPackageDto indexPackage);
	
	IndexResponseApyDto calcIncomeWithApy (IndexDto index);
	
	IndexResponseApyAllDto calcIncomeWithApyAllDate (IndexDto index); 
	
	IndexResponseIrrDto calcIncomeWithIrr(IndexDto index);
	
	String calcCorrelation(IndexCorrelationDto index);
	
	boolean deleteAllHistoryForCompany(String index);
}
