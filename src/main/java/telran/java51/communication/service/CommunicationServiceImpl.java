package telran.java51.communication.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.CommunicationRepository;
import telran.java51.communication.dto.IndexCorrelationDto;
import telran.java51.communication.dto.IndexDto;
import telran.java51.communication.dto.IndexHistoryDto;
import telran.java51.communication.dto.IndexPackageDto;
import telran.java51.communication.dto.IndexResponseApyAllDto;
import telran.java51.communication.dto.IndexResponseApyDto;
import telran.java51.communication.dto.IndexResponseIrrDto;
import telran.java51.communication.dto.IndexResponsePeriodDto;
import telran.java51.communication.dto.IndexResponseValueCloseDto;

@Service
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

	final ModelMapper modelMapper;
	final CommunicationRepository communicationRepository;
	
	@Override
	public boolean addHistoryWithFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IndexHistoryDto getTimeHistoryForIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IndexResponsePeriodDto> periodBeetwin(IndexDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IndexResponseValueCloseDto> getAllValueCloseBetween(IndexDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexResponsePeriodDto calcSumPackage(IndexPackageDto indexPackage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexResponseApyDto calcIncomeWithApy(IndexDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexResponseApyAllDto calcIncomeWithApyAllDate(IndexDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexResponseIrrDto calcIncomeWithIrr(IndexDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String calcCorrelation(IndexCorrelationDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteAllHistoryForCompany(String index) {
		// TODO Auto-generated method stub
		return false;
	}

}
