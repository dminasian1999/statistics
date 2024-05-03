package telran.java51.communication.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.IndexRepository;
import telran.java51.communication.dao.StockRepository;
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
import telran.java51.communication.model.Index;
import telran.java51.communication.model.Stock;

@Service
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

	final ModelMapper modelMapper;
	final StockRepository stockRepository;
	final IndexRepository indexRepository;

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
				if (!indexRepository.existsById(indexForHistory)) {
					indexRepository.save(new Index(indexForHistory));
				}
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
		return StreamSupport.stream(indexRepository.findAll().spliterator(), false).map(i -> i.getIndex())
				.collect(Collectors.toList());
		// TODO find mongoDB query solution
	}

	@Override
	public StockResponsePeriodDto periodBeetwin(StockDto index) {
		// TODO plural
		// TODO try to calculate in db by aggregate and so change tree to array
		// TODO multiple threads ?
		// TODO exceptions if .getTo() is in future
		// TODO should we divide total result to corresponding periods and return
		// average results of each field as result?
		TreeSet<Double> closingPrices = new TreeSet<>();
		closingPrices = (TreeSet<Double>) stockRepository
				.findByIndexAndDateBetween(index.getIndexs().get(0), index.getFrom().minusDays(1),index.getTo().plusDays(2))
				.map(s -> s.getClose()).collect(Collectors.toCollection(TreeSet::new));
		double max = Collections.max(closingPrices);
		double min = Collections.min(closingPrices);
		double mean = closingPrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
		double median = calculateMedian(closingPrices);
		double std = calculateStandardDeviation(closingPrices, mean);
		return new StockResponsePeriodDto(index.getFrom(), index.getTo(), index.getIndexs().get(0), index.getType(),max, mean, median, min, std);
	}

	private double calculateMedian(TreeSet<Double> closingPrices) {
		if (closingPrices.size() % 2 != 0) {
			return closingPrices.ceiling(closingPrices.first());
		} else {
			double firstMiddle = closingPrices.ceiling(closingPrices.first());
			double secondMiddle = closingPrices.floor(closingPrices.last());
			return (firstMiddle + secondMiddle) / 2.0;
		}
	}

	private double calculateStandardDeviation(TreeSet<Double> closingPrices, double mean) {
		double sumSquaredDifferences = closingPrices.stream().mapToDouble(v -> {
			double diff = v - mean;
			return diff * diff;
		}).sum();
		return Math.sqrt(sumSquaredDifferences / closingPrices.size());
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
