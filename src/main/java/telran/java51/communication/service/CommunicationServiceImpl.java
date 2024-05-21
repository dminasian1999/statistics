package telran.java51.communication.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.PeriodRepository;
import telran.java51.communication.dao.StockInfoRepository;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.dto.CorrelationDto;
import telran.java51.communication.dto.HistoryDto;
import telran.java51.communication.dto.IncomeApyDto;
import telran.java51.communication.dto.IncomeIrrDto;
import telran.java51.communication.dto.RequestDto;
import telran.java51.communication.dto.RequestPackageDto;
import telran.java51.communication.dto.ResponseApyAllDto;
import telran.java51.communication.dto.ResponseApyDto;
import telran.java51.communication.dto.ResponseIrrDto;
import telran.java51.communication.dto.ResponsePeriodDto;
import telran.java51.communication.dto.ResponseValueCloseDto;
import telran.java51.communication.dto.exceptions.ArgumentsNotMatchingException;
import telran.java51.communication.dto.exceptions.OutOfBoundaryException;
import telran.java51.communication.dto.exceptions.StockNotFoundException;
import telran.java51.communication.dto.exceptions.TypeNotFoundException;
import telran.java51.communication.model.PeriodStats;
import telran.java51.communication.model.Stock;
import telran.java51.communication.model.StockInfo;

@Service
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

	final ModelMapper modelMapper;
	final StockRepository stockRepository;
	final PeriodRepository periodRepository;
	final StockInfoRepository stockInfoRepository;
	ExecutorService executor;


	@Override
	public boolean addHistoryWithFile(String index, String csv) {
		// TODO false name exeption,no duplicates

		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			StockInfo stockInfo = stockInfoRepository.findById(index)
					.orElse(stockInfoRepository.save(new StockInfo(index)));
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] fields = line.split(",");
				Stock stock = new Stock(index, LocalDate.parse(fields[0]).plusDays(1), Double.parseDouble(fields[1]),
						Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]),
						Double.parseDouble(fields[5]), Long.parseLong(fields[6]));
				stockRepository.save(stock);
				stockInfoRepository.save(stockInfo);
				if (stockInfo.getHistory() == null
						|| stockInfo.getHistory().isBefore(LocalDate.parse(fields[0]).plusDays(1))) {
					stockInfo.setHistory(stockRepository.findFirstByIndexIgnoreCaseOrderByDateAsc(index).getDate());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public HistoryDto getTimeHistoryForIndex(String index) {
		checkIndex(index);
//		LocalDate from = stockRepository.findFirstByIndexIgnoreCaseOrderByDateAsc(index).getDate().minusDays(1);
		LocalDate from = stockInfoRepository.findById(index).orElse(null).getHistory();
		LocalDate to = stockRepository.findFirstByIndexIgnoreCaseOrderByDateDesc(index).getDate().minusDays(1);
		return new HistoryDto(index, from, to);
	}

	@Override
	public List<String> getAllIndexes() {
		return stockInfoRepository.getIndexes();
	}

	@Override
	public List<ResponsePeriodDto> periodBeetwin(RequestDto index) {
		return index.getIndexs().stream().map(i -> {
			calculatePeriodIncomeHandler(index, i);
			ResponsePeriodDto st = periodRepository.calcAnalysis(i, index.getFrom(), index.getTo());
			return new ResponsePeriodDto(index.getFrom(), index.getTo(), index.getIndexs().get(0),
					getPeriodType(index.getType(), index.getQuantity()), st.getMax(), st.getMean(), st.getMedian(),
					st.getMin(), st.getStd());
		}).toList();
	}

	@Override
	public List<ResponseValueCloseDto> getAllValueCloseBetween(RequestDto index) {
		calculatePeriodIncomeHandler(index, index.getIndexs().get(0));
		return periodRepository.findByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetween(index.getIndexs().get(0),
				getPeriodType(index.getType(), index.getQuantity()),
				LocalDateTime.of(index.getFrom(), LocalTime.of(21, 00)),
				LocalDateTime.of(index.getTo(), LocalTime.of(21, 00))).map(p -> {
					return new ResponseValueCloseDto(index.getFrom(), index.getTo(), index.getIndexs().get(0),
							p.getType(), p.getDateOfPurchase().toLocalDate(), p.getDateOfSale().toLocalDate(),
							p.getPurchaseAmount(), p.getSaleAmount(), p.getIncome(),
							stockRepository.getClosesByIndexAndDateBetween(index.getIndexs().get(0),
									p.getDateOfPurchase(), p.getDateOfSale()));
				}).toList();
	}

	@Override
	public ResponsePeriodDto calcSumPackage(RequestPackageDto indexPackage) {
		if (indexPackage.getIndexs().size() != indexPackage.getAmount().size()) {
			throw new ArgumentsNotMatchingException("Arguments are not matching!");
		}
		RequestDto index = new RequestDto(indexPackage.getIndexs(), indexPackage.getType(), indexPackage.getQuantity(),
				indexPackage.getFrom(), indexPackage.getTo());
		indexPackage.getIndexs().forEach(i -> checkIndex(i));
		indexPackage.getIndexs().forEach(i -> calculatePeriodIncomeHandler(index, i));
		double max = 0;
		double mean = 0;
		double median = 0;
		double min = 0;
		double std = 0;
		for (int i = 0; i < indexPackage.getAmount().size(); i++) {
			ResponsePeriodDto income = periodRepository.calcAnalysis(indexPackage.getIndexs().get(i), index.getFrom(),
					index.getTo(), Double.valueOf(indexPackage.getAmount().get(i)));
			max += income.getMax();
			mean += income.getMean();
			median += income.getMedian();
			min += income.getMin();
			std += income.getStd();
		}
		return new ResponsePeriodDto(indexPackage.getFrom(), indexPackage.getTo(),
				String.valueOf(indexPackage.getIndexs()), getPeriodType(index.getType(), index.getQuantity()), max,
				mean, median, min, std);
	}

	@Override
	public ResponseApyDto calcIncomeWithApy(RequestDto index) {
		calculatePeriodIncomeHandler(index, index.getIndexs().get(0));
		String type = getPeriodType(index.getType(), index.getQuantity());
		PeriodStats minm = periodRepository.findFirstByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByIncomeAsc(
				index.getIndexs().get(0), type, LocalDateTime.of(index.getFrom(), LocalTime.of(21, 00)),
				LocalDateTime.of(index.getTo(), LocalTime.of(21, 00)));
		PeriodStats maxm = periodRepository.findFirstByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByIncomeDesc(
				index.getIndexs().get(0), type,LocalDateTime.of(index.getFrom(), LocalTime.of(21, 00)),
				LocalDateTime.of(index.getTo(), LocalTime.of(21, 00)));
		
		IncomeApyDto min = modelMapper.map(minm, IncomeApyDto.class);
		min.setDateOfPurchase(minm.getDateOfPurchase().toLocalDate());
		min.setDateOfSale(minm.getDateOfSale().toLocalDate());

		IncomeApyDto max = modelMapper.map(maxm, IncomeApyDto.class);
		max.setDateOfPurchase(maxm.getDateOfPurchase().toLocalDate());
		max.setDateOfSale(maxm.getDateOfSale().toLocalDate());
		return new ResponseApyDto(index.getFrom(), index.getTo(), index.getIndexs(), type, min, max);
	}

	@Override
	public List<ResponseApyAllDto> calcIncomeWithApyAllDate(RequestDto index) {
		calculatePeriodIncomeHandler(index, index.getIndexs().get(0));
		return periodRepository.findByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetween(index.getIndexs().get(0),
				getPeriodType(index.getType(), index.getQuantity()),
				LocalDateTime.of(index.getFrom(), LocalTime.of(21, 00)),
				LocalDateTime.of(index.getTo(), LocalTime.of(21, 00))).map(p -> {
					return new ResponseApyAllDto(index.getIndexs().get(0), index.getFrom(), index.getTo(), p.getType(),
							p.getDateOfPurchase().toLocalDate(), p.getDateOfSale().toLocalDate(), p.getPurchaseAmount(),
							p.getSaleAmount(), p.getIncome(), p.getApy());
				}).toList();
	}

	@Override
	public List<ResponseIrrDto> calcIncomeWithIrr(RequestDto index) {
		List<ResponseIrrDto> res = new ArrayList<>();
		index.getIndexs().forEach(i -> {
			calculatePeriodIncomeHandler(index, i);
			String type = index.getQuantity() + " " + index.getType();
			type = index.getQuantity() > 1 ? type : type.substring(0, type.length() - 1);
			PeriodStats minm = periodRepository
					.findFirstByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByIncomeAsc(
							i, type,LocalDateTime.of(index.getFrom(), LocalTime.of(21, 00)),
							LocalDateTime.of(index.getTo(), LocalTime.of(21, 00)));
			PeriodStats maxm = periodRepository
					.findFirstByIndexIgnoreCaseAndTypeAndDateOfPurchaseBetweenOrderByIncomeDesc(
							i, type,LocalDateTime.of(index.getFrom(), LocalTime.of(21, 00)),
							LocalDateTime.of(index.getTo(), LocalTime.of(21, 00)));
			IncomeIrrDto min = modelMapper.map(minm, IncomeIrrDto.class);
			min.setDateOfPurchase(minm.getDateOfPurchase().toLocalDate());
			min.setDateOfSale(minm.getDateOfSale().toLocalDate());
			IncomeIrrDto max = modelMapper.map(maxm, IncomeIrrDto.class);
			max.setDateOfPurchase(maxm.getDateOfPurchase().toLocalDate());
			max.setDateOfSale(maxm.getDateOfSale().toLocalDate());
			res.add(new ResponseIrrDto(index.getFrom(), index.getTo(), index.getIndexs(), type, min, max));
		});
		return res;
	}

	@Override
	public String calcCorrelation(CorrelationDto index) {
		List<Double> prices1 = stockRepository.getClosesByIndexAndDateBetween(index.getIndexs().get(0),
				LocalDateTime.of(index.getFrom(), LocalTime.of(21, 00)),
				LocalDateTime.of(index.getTo(), LocalTime.of(21, 00)));

		List<Double> prices2 = stockRepository.getClosesByIndexAndDateBetween(index.getIndexs().get(1),
				LocalDateTime.of(index.getFrom(), LocalTime.of(21, 00)),
				LocalDateTime.of(index.getTo(), LocalTime.of(21, 00)));

		if (prices1.size() != prices2.size()) {
			throw new IllegalArgumentException("Sizes must match!");
		}

		int n = prices1.size();
		double[] returns1 = new double[n - 1];
		double[] returns2 = new double[n - 1];

		for (int i = 1; i < n; i++) {
			returns1[i - 1] = (prices1.get(i) - prices1.get(i - 1)) / prices1.get(i - 1);
			returns2[i - 1] = (prices2.get(i) - prices2.get(i - 1)) / prices2.get(i - 1);
		}

		double mean1 = calculateMean(returns1);
		double mean2 = calculateMean(returns2);

		double covariance = 0.0;
		double variance1 = 0.0;
		double variance2 = 0.0;

		for (int i = 0; i < returns1.length; i++) {
			double deviation1 = returns1[i] - mean1;
			double deviation2 = returns2[i] - mean2;
			covariance += deviation1 * deviation2;
			variance1 += deviation1 * deviation1;
			variance2 += deviation2 * deviation2;
		}

		covariance /= (returns1.length - 1);
		variance1 /= (returns1.length - 1);
		variance2 /= (returns1.length - 1);

		double stddev1 = Math.sqrt(variance1);
		double stddev2 = Math.sqrt(variance2);

		double correlation = covariance / (stddev1 * stddev2);

		if (correlation == 1.0) {
			return "Perfect positive correlation";
		} else if (correlation > 0.8) {
			return "Very strong positive correlation";
		} else if (correlation > 0.5) {
			return "Strong positive correlation";
		} else if (correlation > 0.2) {
			return "Moderate positive correlation";
		} else if (correlation > -0.2) {
			return "Weak or no correlation";
		} else if (correlation > -0.5) {
			return "Moderate negative correlation";
		} else if (correlation > -0.8) {
			return "Strong negative correlation";
		} else if (correlation > -1.0) {
			return "Very strong negative correlation";
		} else {
			return "Perfect negative correlation";
		}
	}

	private double calculateMean(double[] values) {
		double sum = 0.0;
		for (double value : values) {
			sum += value;
		}
		return sum / values.length;

	}

	@Override
	public boolean deleteAllHistoryForCompany(String index) {
		try {
			periodRepository.deleteAllByIndexIgnoreCase(index);
			stockRepository.deleteAllByIndexIgnoreCase(index);
			stockInfoRepository.deleteById(index);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void checkIndex(String index) {
		if (!stockInfoRepository.existsById(index)) {
			throw new StockNotFoundException("Stock not found: " + index);
		}
	}
	private void calculatePeriodIncomeHandler(RequestDto index, String indexName) {
		checkIndex(indexName);
		LocalDateTime[] periodTimes = getPeriodDates(index);
		LocalDateTime firstDate = periodTimes[0];
		LocalDateTime lastDate = periodTimes[1];
		LocalDateTime lastLimit = LocalDateTime.of(index.getTo(), LocalTime.of(21, 0));
		ckeckTimeBoundary(indexName, lastDate, lastLimit, firstDate);
		String type = getPeriodType(index.getType(), index.getQuantity());
		int size = 0;
		for (firstDate = periodTimes[0]; firstDate.isBefore(lastLimit); firstDate = firstDate.plusDays(1)) size++;
		Calculator[] calcs = new Calculator[size];
		for (firstDate = periodTimes[0]; firstDate.isBefore(lastLimit); firstDate = firstDate.plusDays(1),lastDate = lastDate.plusDays(1)) {
			calcs[--size]= new Calculator(stockRepository, periodRepository, indexName, type, firstDate, lastDate);
		}	
	    executor = Executors.newWorkStealingPool();
		for (int i = 0; i < size; i++) {
			executor.execute(calcs[i]);
		}
		try {
			executor.shutdown();
			executor.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void ckeckTimeBoundary(String indexName, LocalDateTime lastDate, LocalDateTime lastLimit,
			LocalDateTime firstDate) {
		LocalDate lastestDate = stockRepository.findFirstByIndexIgnoreCaseOrderByDateDesc(indexName).getDate()
				.minusDays(1);
		LocalDate oldestDate = stockInfoRepository.findById(indexName).orElse(null).getHistory().minusDays(1);
		if (lastDate.toLocalDate().isAfter(lastestDate) || lastLimit.toLocalDate().isAfter(lastestDate)) {
			throw new OutOfBoundaryException("Lastest date for '" + indexName + "' is: " + lastestDate + "");
		}
		if (firstDate.toLocalDate().isBefore(oldestDate)) {
			throw new OutOfBoundaryException("Oldest date for '" + indexName + "' is: " + oldestDate + "");
		}
		if (lastDate.toLocalDate().isBefore(firstDate.toLocalDate())) {
			throw new OutOfBoundaryException("Reversed dates!");
		}
	}

	private LocalDateTime[] getPeriodDates(RequestDto index) {
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
			throw new TypeNotFoundException("Invalid type: " + index.getType());
		}
		return res;
	}

	private String getPeriodType(String periodType, int quantity) {
		String type = quantity + " " + periodType;
		return quantity > 1 ? type : type.substring(0, type.length() - 1);
	}
}
