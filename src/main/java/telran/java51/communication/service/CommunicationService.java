package telran.java51.communication.service;

import java.util.List;

import telran.java51.communication.dto.CorrelationDto;
import telran.java51.communication.dto.RequestDto;
import telran.java51.communication.dto.HistoryDto;
import telran.java51.communication.dto.RequestPackageDto;
import telran.java51.communication.dto.ResponseApyAllDto;
import telran.java51.communication.dto.ResponseApyDto;
import telran.java51.communication.dto.ResponseIrrDto;
import telran.java51.communication.dto.ResponsePeriodDto;
import telran.java51.communication.dto.ResponseValueCloseDto;

public interface CommunicationService {

	boolean addHistoryWithFile(String indexForHistory,String csv);
	
	HistoryDto getTimeHistoryForIndex(String index);
	
	List<String> getAllIndexes();
	
	List<ResponsePeriodDto> periodBeetwin(RequestDto index);
	
	List<ResponseValueCloseDto> getAllValueCloseBetween(RequestDto index);
	
	ResponsePeriodDto calcSumPackage(RequestPackageDto indexPackage);
	
	ResponseApyDto calcIncomeWithApy (RequestDto index);
	
	List<ResponseApyAllDto>  calcIncomeWithApyAllDate (RequestDto index); 
	
	 List<ResponseIrrDto>  calcIncomeWithIrr(RequestDto index);
	
	String calcCorrelation(CorrelationDto index);
	
	boolean deleteAllHistoryForCompany(String index);
}
