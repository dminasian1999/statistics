package telran.java51.communication.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.PeriodRepository;
import telran.java51.communication.dao.StockInfoRepository;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.dto.IncomeApyDto;
import telran.java51.communication.dto.IncomeIrrDto;
import telran.java51.communication.dto.ResponseApyDto;
import telran.java51.communication.dto.ResponseIrrDto;
import telran.java51.communication.dto.StatisticalData;
import telran.java51.communication.dto.CorrelationDto;
import telran.java51.communication.dto.RequestDto;
import telran.java51.communication.dto.HistoryDto;
import telran.java51.communication.dto.RequestPackageDto;
import telran.java51.communication.dto.ResponseApyAllDto;
import telran.java51.communication.dto.ResponsePeriodDto;
import telran.java51.communication.dto.ResponseValueCloseDto;
import telran.java51.communication.dto.exceptions.StockNotFoundException;
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
	final ReadWriteLock rwl = new ReentrantReadWriteLock();

	@Override
	public boolean addHistoryWithFile(String index, String csv) {

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

				if (stockInfo.getHistory() == null
						|| stockInfo.getHistory().isBefore(LocalDate.parse(fields[0]).plusDays(1))) {
					stockInfo.setHistory(
							stockRepository.findFirstByIndexIgnoreCaseOrderByDateAsc(index).getDate().minusDays(1));
				}
				rwl.writeLock().lock();
				stockRepository.save(stock);
				stockInfoRepository.save(stockInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwl.writeLock().unlock();
		}
		return true;

	}

	@Override
	public HistoryDto getTimeHistoryForIndex(String index) {
		if (!stockRepository.existsByIndexIgnoreCase(index)) {
			throw new StockNotFoundException("Stock not found: "+index);
		}
		LocalDate from = stockRepository.findFirstByIndexIgnoreCaseOrderByDateAsc(index).getDate().minusDays(1);
		LocalDate to = stockRepository.findFirstByIndexIgnoreCaseOrderByDateDesc(index).getDate().minusDays(1);
		return new HistoryDto(index, from, to);
	}

	@Override
	public List<String> getAllIndexes() {
		return stockRepository.getIndexes();
	}

	@Override
	public ResponsePeriodDto periodBeetwin(RequestDto index) {
//		// TODO plural.
//		// TODO what if to create new entitie and rep (only fild close)?
//		// TODO exceptions if .getTo() is in future	
		calculatePeriodIncomeHandler(index, index.getIndexs().get(0));
		StatisticalData st = periodRepository.calcAnalysis(index.getIndexs().get(0), index.getFrom(), index.getTo());
		return new ResponsePeriodDto(index.getFrom(), index.getTo(), index.getIndexs().get(0),
				String.valueOf(index.getQuantity() + " " + index.getType()), st.getMax(), st.getMean(), st.getMedian(),
				st.getMin(), st.getStd());
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
			System.out.println("Invalid type: " + index.getType());
			break;
		}
		return res;
	}

	@Override
	public List<ResponseValueCloseDto> getAllValueCloseBetween(RequestDto index) {
		// TODO fix times ,min,maxDates
		calculatePeriodIncomeHandler(index, index.getIndexs().get(0));
		return periodRepository
				.findByIndexIgnoreCaseAndDateOfPurchaseBetween(index.getIndexs().get(0), index.getFrom(), index.getTo())
				.map(p -> {
					String type = index.getQuantity() + " " + index.getType();
					type = index.getQuantity() > 1 ? type : type.substring(0, type.length() - 1);
					return new ResponseValueCloseDto(p.getDateOfPurchase().toLocalDate(),
							p.getDateOfSale().toLocalDate(), index.getIndexs().get(0), type,
							p.getDateOfPurchase().toLocalDate(), p.getDateOfSale().toLocalDate(), p.getPurchaseAmount(),
							p.getSaleAmount(), p.getIncome(),
							stockRepository.getClosesByIndexAndDateBetween(index.getIndexs().get(0),
									p.getDateOfPurchase().toLocalDate(), p.getDateOfSale().toLocalDate()));
				}).toList();
	}

	@Override
	public ResponsePeriodDto calcSumPackage(RequestPackageDto indexPackage) {
		RequestDto index = new RequestDto(indexPackage.getIndexs(), indexPackage.getType(), indexPackage.getQuantity(),
				indexPackage.getFrom(), indexPackage.getTo());
		indexPackage.getIndexs().forEach(i -> calculatePeriodIncomeHandler(index, i));
		double max = 0;
		double mean = 0;
		double median = 0;
		double min = 0;
		double std = 0;
		for (int i = 0; i < indexPackage.getAmount().size(); i++) {
			StatisticalData income = periodRepository.calcAnalysis(indexPackage.getIndexs().get(i), index.getFrom(),
					index.getTo(), Double.valueOf(indexPackage.getAmount().get(i)));
			max += income.getMax();
			mean += income.getMean();
			median += income.getMedian();
			min += income.getMin();
			std += income.getStd();
		}
		return new ResponsePeriodDto(indexPackage.getFrom(), indexPackage.getTo(),
				String.valueOf(indexPackage.getIndexs()), String.valueOf(index.getQuantity() + " " + index.getType()),
				max, mean, median, min, std);
	}

	@Override
	public ResponseApyDto calcIncomeWithApy(RequestDto index) {
		calculatePeriodIncomeHandler(index, index.getIndexs().get(0));
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
		return new ResponseApyDto(index.getFrom(), index.getTo(), index.getIndexs(), type, min, max);
	}

	@Override
	public List<ResponseApyAllDto> calcIncomeWithApyAllDate(RequestDto index) {
		calculatePeriodIncomeHandler(index, index.getIndexs().get(0));
		return periodRepository
				.findByIndexIgnoreCaseAndDateOfPurchaseBetween(index.getIndexs().get(0), index.getFrom(), index.getTo())
				.map(p -> {
					String type = index.getQuantity() + " " + index.getType();
					type = index.getQuantity() > 1 ? type : type.substring(0, type.length() - 1);
					return new ResponseApyAllDto(index.getIndexs().get(0), index.getFrom(), index.getTo(), type,
							p.getDateOfPurchase().toLocalDate(), p.getDateOfSale().toLocalDate(), p.getPurchaseAmount(),
							p.getSaleAmount(), p.getIncome(), p.getApy());
				}).toList();
	}

	private void calculatePeriodIncomeHandler(RequestDto index, String indexName) {
		// TODO what if year after is not available for apy
		LocalDateTime[] periodTimes = getPeriodDates(index);
		LocalDateTime firstDate = periodTimes[0];
		LocalDateTime lastDate = periodTimes[1];
		LocalDateTime lastLimit = LocalDateTime.of(index.getTo(), LocalTime.of(21, 0));
		Period period = Period.between(firstDate.toLocalDate(), lastDate.toLocalDate());
		boolean lessThanYear = period.getYears() < 0;
		PeriodStats income;
		while (firstDate.isBefore(lastLimit)) {
			if (!periodRepository.existsByIndexIgnoreCaseAndDateOfPurchaseAndDateOfSale(indexName, firstDate,
					lastDate)) {
				if (lessThanYear) {
					income = stockRepository.calcIncomeForPeriod(indexName, firstDate, lastDate);
				} else {
					double apyIncome = stockRepository.calcIncomeForPeriod(indexName, firstDate, firstDate.plusYears(1))
							.getSaleAmount();
					income = stockRepository.calcIncomeForPeriodWithApy(indexName, firstDate, lastDate, apyIncome);
				}

				periodRepository.save(income);
			}
			firstDate = firstDate.plusDays(1);
			lastDate = lastDate.plusDays(1);
		}
	}

	@Override
	public List<ResponseIrrDto> calcIncomeWithIrr(RequestDto index) {
		List<ResponseIrrDto> res = new ArrayList<>();
		index.getIndexs().forEach(i -> {
			calculatePeriodIncomeHandler(index, i);
			PeriodStats minm = periodRepository.findFirstByIndexIgnoreCaseOrderByIncomeAsc(i);
			PeriodStats maxm = periodRepository.findFirstByIndexIgnoreCaseOrderByIncomeDesc(i);
			IncomeIrrDto min = modelMapper.map(periodRepository.findFirstByIndexIgnoreCaseOrderByIncomeAsc(i),
					IncomeIrrDto.class);
			min.setDateOfPurchase(minm.getDateOfPurchase().toLocalDate());
			min.setDateOfSale(minm.getDateOfSale().toLocalDate());
			IncomeIrrDto max = modelMapper.map(periodRepository.findFirstByIndexIgnoreCaseOrderByIncomeDesc(i),
					IncomeIrrDto.class);
			max.setDateOfPurchase(maxm.getDateOfPurchase().toLocalDate());
			max.setDateOfSale(maxm.getDateOfSale().toLocalDate());
			String type = index.getQuantity() + " " + index.getType();
			type = index.getQuantity() > 1 ? type : type.substring(0, type.length() - 1);
			res.add(new ResponseIrrDto(index.getFrom(), index.getTo(), index.getIndexs(), type, min, max));
		});
		return res;
	}

	@Override
	public String calcCorrelation(CorrelationDto index) {
		List<Double> prices1 = stockRepository.getClosesByIndexAndDateBetween(index.getIndexs().get(0), index.getFrom(),
				index.getTo());

		List<Double> prices2 = stockRepository.getClosesByIndexAndDateBetween(index.getIndexs().get(1), index.getFrom(),
				index.getTo());

		if (prices1.size() != prices2.size()) {
			throw new IllegalArgumentException("Размеры списков должны совпадать");
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
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
