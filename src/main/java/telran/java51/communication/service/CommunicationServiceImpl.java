package telran.java51.communication.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.PeriodRepository;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.dto.IncomeApyDto;
import telran.java51.communication.dto.IncomeDto;
import telran.java51.communication.dto.StockCorrelationDto;
import telran.java51.communication.dto.StockDto;
import telran.java51.communication.dto.StockHistoryDto;
import telran.java51.communication.dto.StockPackageDto;
import telran.java51.communication.dto.StockResponseApyAllDto;
import telran.java51.communication.dto.StockResponseApyDto;
import telran.java51.communication.dto.StockResponseIrrDto;
import telran.java51.communication.dto.StockResponsePeriodDto;
import telran.java51.communication.dto.StockResponseValueCloseDto;
import telran.java51.communication.dto.exceptions.StockNotFoundException;
import telran.java51.communication.model.PeriodStats;
import telran.java51.communication.model.Stock;

@Service
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

	final ModelMapper modelMapper;
	final StockRepository stockRepository;
	final PeriodRepository periodRepository;

	@Override
	public boolean addHistoryWithFile(String indexForHistory, String csv) {
		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] fields = line.split(",");
				Stock stock = new Stock(indexForHistory, LocalDate.parse(fields[0]).plusDays(1),
						Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), Double.parseDouble(fields[3]),
						Double.parseDouble(fields[4]), Double.parseDouble(fields[5]), Long.parseLong(fields[6]));
				stockRepository.save(stock);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public StockHistoryDto getTimeHistoryForIndex(String index) {
		if (!stockRepository.existsByIndexIgnoreCase(index)) {
			throw new StockNotFoundException();
		}
		LocalDate from = stockRepository.findFirstByIndexIgnoreCaseOrderByDateAsc(index).getDate().minusDays(1);
		LocalDate to = stockRepository.findFirstByIndexIgnoreCaseOrderByDateDesc(index).getDate().minusDays(1);
		return new StockHistoryDto(index, from, to);
	}

	@Override
	public List<String> getAllIndexes() {
		return stockRepository.getIndexes().toList();
	}

	@Override
	public StockResponsePeriodDto periodBeetwin(StockDto index) {
//		// TODO plural
//		// TODO exceptions if .getTo() is in future	
		calculatePeriodIncomeHandler(index,index.getIndexs().get(0));
		IncomeDto st =  periodRepository.calcStats(index.getIndexs().get(0), index.getFrom(), index.getTo());
		return new StockResponsePeriodDto(index.getFrom(), index.getTo(), index.getIndexs().get(0),String.valueOf(index.getQuantity()+" "+index.getType()) ,
				st.getMax(), st.getMean(), st.getMedian(), st.getMin(), st.getStd());
	}

	private LocalDateTime[] getPeriodDates(StockDto index) {
		LocalDateTime[] res = new LocalDateTime[2];
		res[0] = LocalDateTime.of(index.getFrom(), LocalTime.of(21, 0));
		switch (index.getType()) {
		case "days":
			res[1] = res[0].plusDays(index.getQuantity());
			break;
		case "weeks":
			res[1] = res[0].plusWeeks(index.getQuantity());
			break;
		case "months":
			res[1] = res[0].plusMonths(index.getQuantity());
			break;
		case "decades":
			res[1] = res[0].plusYears(index.getQuantity() * 10);
			break;
		case "years":
			res[1] = res[0].plusYears(index.getQuantity());
			break;
		case "centuries":
			res[1] = res[0].plusYears(index.getQuantity() * 10 * 10);
			break;
		default:
			System.out.println("Invalid type: " + index.getType());
			break;
		}
		return res;
	}

	@Override
	public List<StockResponseValueCloseDto> getAllValueCloseBetween(StockDto index) {
		//TODO fix times ,min,maxDates
		calculatePeriodIncomeHandler(index,index.getIndexs().get(0));		
		return periodRepository.findByIndexIgnoreCaseAndDateOfPurchaseBetween(index.getIndexs().get(0), index.getFrom(), index.getTo())
				.map(p -> {
	                String type = index.getQuantity() + " " + index.getType();
	                type = index.getQuantity() > 1 ? type : type.substring(0, type.length() - 1);
	                return new StockResponseValueCloseDto(
	                        p.getDateOfPurchase().toLocalDate(),
	                        p.getDateOfSale().toLocalDate(),
	                        index.getIndexs().get(0),
	                        type,
	                        p.getDateOfPurchase().toLocalDate(),
	                        p.getDateOfSale().toLocalDate(),
	                        p.getPurchaseAmount(),
	                        p.getSaleAmount(),
	                        p.getIncome(),
	                        stockRepository.getClosesByIndexAndDateBetween(index.getIndexs().get(0), p.getDateOfPurchase().toLocalDate(), p.getDateOfSale().toLocalDate())
	                );
	            })
				.toList();
	}

	@Override
	public StockResponsePeriodDto calcSumPackage(StockPackageDto indexPackage) {
		StockDto index = new StockDto(indexPackage.getIndexs(), indexPackage.getType(), indexPackage.getQuantity(), indexPackage.getFrom(), indexPackage.getTo());
		indexPackage.getIndexs().stream().forEach(i-> calculatePeriodIncomeHandler(index,i));		
		double max =0;
		double mean =0;
		double median=0;
		double min =0;
		double std =0; 
		for (int i = 0; i < indexPackage.getAmount().size(); i++) {
			IncomeDto income =periodRepository.calcStatsSum(indexPackage.getIndexs().get(i) , index.getFrom(), index.getTo(),Double.valueOf(indexPackage.getAmount().get(i)));
			 max +=  income.getMax();
			 mean += income.getMean();
			 median += income.getMedian();
			 min +=  income.getMin();
			 std += income.getStd();			
		}	
		return new StockResponsePeriodDto(indexPackage.getFrom(), indexPackage.getTo(), String.valueOf(indexPackage.getIndexs()), String.valueOf(index.getQuantity()+" "+index.getType()) ,
				max, mean, median, min, std);
	}

	@Override
	public StockResponseApyDto calcIncomeWithApy(StockDto index) {
		calculatePeriodIncomeHandler(index,index.getIndexs().get(0));
		PeriodStats minm = periodRepository.findFirstByIndexIgnoreCaseOrderByIncomeAsc(index.getIndexs().get(0));
		PeriodStats maxm = periodRepository.findFirstByIndexIgnoreCaseOrderByIncomeDesc(index.getIndexs().get(0));
		IncomeApyDto min = modelMapper.map(minm, IncomeApyDto.class);
		min.setDateOfPurchase(minm.getDateOfPurchase().toLocalDate());
		min.setDateOfSale(minm.getDateOfSale().toLocalDate());
		IncomeApyDto max = modelMapper.map(maxm, IncomeApyDto.class);
		max.setDateOfPurchase(maxm.getDateOfPurchase().toLocalDate());
		max.setDateOfSale(maxm.getDateOfSale().toLocalDate());
		String type = index.getQuantity() + " " + index.getType();
		type = index.getQuantity() > 1 ? type : type.substring(0, type.length() - 1);
		return new StockResponseApyDto(index.getFrom(), index.getTo(), index.getIndexs().get(0), type, min, max);
	}

	@Override
	public List<StockResponseApyAllDto>  calcIncomeWithApyAllDate(StockDto index) {
		calculatePeriodIncomeHandler(index,index.getIndexs().get(0));
		return periodRepository.findByIndexIgnoreCaseAndDateOfPurchaseBetween(index.getIndexs().get(0), index.getFrom(), index.getTo())
	            .map(p -> {
	                String type = index.getQuantity() + " " + index.getType();
	                type = index.getQuantity() > 1 ? type : type.substring(0, type.length() - 1);
	                return new StockResponseApyAllDto(
	                        index.getIndexs().get(0),
	                        index.getFrom(),
	                        index.getTo(),
	                        type,
	                        p.getDateOfPurchase().toLocalDate(),
	                        p.getDateOfSale().toLocalDate(),
	                        p.getPurchaseAmount(),
	                        p.getSaleAmount(),
	                        p.getIncome(),
	                        p.getApy()
	                );
	            })
	            .toList();
	}

	private void calculatePeriodIncomeHandler(StockDto index,String indexName) {
		//TODO what if year after is not available for apy
		LocalDateTime[] periodTimes = getPeriodDates(index);
		LocalDateTime firstDate = periodTimes[0];
		LocalDateTime lastDate = periodTimes[1];
		LocalDateTime lastLimit = LocalDateTime.of(index.getTo(), LocalTime.of(21, 0));
		Period period = Period.between(firstDate.toLocalDate(), lastDate.toLocalDate());
        boolean lessThanYear = period.getYears() < 0;           
		PeriodStats income;
		while (firstDate.isBefore(lastLimit)) {
			if (!periodRepository.existsByIndexIgnoreCaseAndDateOfPurchaseAndDateOfSale(indexName,firstDate, lastDate)) {
				if (lessThanYear) {
					income = stockRepository.calcIncomeForPeriod(indexName, firstDate, lastDate);
		        } else {
		        	double apyIncome = stockRepository.calcIncomeForPeriod(indexName, firstDate, firstDate.plusYears(1)).getSaleAmount();
					income = stockRepository.calcIncomeForPeriodWithApy(indexName, firstDate, lastDate, apyIncome);
		        }
				
				periodRepository.save(income);
			}
			firstDate = firstDate.plusDays(1);
			lastDate = lastDate.plusDays(1);
		}
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
		try {
			periodRepository.deleteAllByIndexIgnoreCase(index);
			stockRepository.deleteAllByIndexIgnoreCase(index);
			return true;
		} catch (Exception e) {
			return false;
		} 
	}

}
