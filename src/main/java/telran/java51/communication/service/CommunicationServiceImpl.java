package telran.java51.communication.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.dto.StockCorrelationDto;
import telran.java51.communication.dto.StockDto;
import telran.java51.communication.dto.StockHistoryDto;
import telran.java51.communication.dto.StockInfoDto;
import telran.java51.communication.dto.StockPackageDto;
import telran.java51.communication.dto.StockResponseApyAllDto;
import telran.java51.communication.dto.StockResponseApyDto;
import telran.java51.communication.dto.StockResponseIrrDto;
import telran.java51.communication.dto.StockResponsePeriodDto;
import telran.java51.communication.dto.StockResponseValueCloseDto;
import telran.java51.communication.dto.exceptions.StockNotFoundException;
import telran.java51.communication.model.Stock;
import telran.java51.communication.model.StockInfo;

@Service
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

	final ModelMapper modelMapper;
	final StockRepository stockRepository;
	static ObjectMapper mapper;
	
	@Override
	synchronized public boolean  addHistoryWithFile(String indexForHistory,String csv) {
		try(BufferedReader br = new BufferedReader(new FileReader(csv))){
			String line;
            br.readLine();
            Stock stock = stockRepository.findById(indexForHistory).orElse(new Stock(indexForHistory));
            
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                
                
                StockInfo stockInfo = new StockInfo(LocalDate.parse(fields[0]).plusDays(1), Double.parseDouble(fields[1]), Double.parseDouble(fields[2]),
                		Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5]), Long.parseLong(fields[6]));
                
                stock.addHistory(stockInfo);
                stockRepository.save(stock);
            }
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public StockHistoryDto getTimeHistoryForIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StockResponsePeriodDto> periodBeetwin(StockDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StockResponseValueCloseDto> getAllValueCloseBetween(StockDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StockResponsePeriodDto calcSumPackage(StockPackageDto indexPackage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StockResponseApyDto calcIncomeWithApy(StockDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StockResponseApyAllDto calcIncomeWithApyAllDate(StockDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StockResponseIrrDto calcIncomeWithIrr(StockDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String calcCorrelation(StockCorrelationDto index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteAllHistoryForCompany(String index) {
		// TODO Auto-generated method stub
		return false;
	}

}
