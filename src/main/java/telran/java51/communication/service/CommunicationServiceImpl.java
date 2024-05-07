package telran.java51.communication.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.dto.IncomeApyDto;
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
import telran.java51.communication.model.Stock;

@Service
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

	final ModelMapper modelMapper;
	final StockRepository stockRepository;

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
		return stockRepository.findDistinctByIndex().toList();
	}

	@Override
	public StockResponsePeriodDto periodBeetwin(StockDto index) {
		// TODO plural
		// TODO multiple threads ?
		// TODO exceptions if .getTo() is in future	
//		StatisticDto st = stockRepository.findByIndexAndDateBetween(index.getIndexs().get(0),
//				index.getFrom().minusDays(1), index.getTo().plusDays(1));
//		return new StockResponsePeriodDto(index.getFrom(), index.getTo(), index.getIndexs().get(0),String.valueOf(index.getQuantity()+" "+index.getType()) ,
//				st.getMax(), st.getMean(), st.getMedian(), st.getMin(), st.getStd());
		return null;
	}

//	private double calculateIncome(double first, double last, int power) {
//		double s = last/first;
//        double result = Math.pow(s, 1.0 / power);
//        return result - 1;
//	}

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
            res[1] = res[0].plusYears(index.getQuantity()*10);
            break;
        case "years":
            res[1] = res[0].plusYears(index.getQuantity());
            break;
        case "centuries":
            res[1] = res[0].plusYears(index.getQuantity()*10*10);
            break;
        default:
            System.out.println("Invalid type: " + index.getType());
            break;
		}
		res[1] = res[1].plusDays(1); 
		return res;
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
		//TODO get id from db
		LocalDateTime[] periodTimes = getPeriodDates(index);
		LocalDateTime firstDate = periodTimes[0];
		LocalDateTime lastDate = periodTimes[1];
		TreeMap<Double, IncomeApyDto> statistics = new TreeMap<>();
		while(firstDate.isBefore(periodTimes[1])) {
			IncomeApyDto income =  stockRepository.calcIncomeForPeriod(index.getIndexs().get(0),firstDate,lastDate,1);
			statistics.put(income.getIncome(),income);
			System.out.println(income);
			firstDate = firstDate.plusDays(1);
			lastDate = lastDate.plusDays(1);
		}
		return new StockResponseApyDto(index.getFrom(), index.getTo().minusDays(1), index.getIndexs().get(0), index.getType(), statistics.firstEntry().getValue(), statistics.lastEntry().getValue());
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
