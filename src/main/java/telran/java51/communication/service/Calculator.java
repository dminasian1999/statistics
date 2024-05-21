package telran.java51.communication.service;

import java.time.LocalDateTime;
import java.time.Period;

import telran.java51.communication.dao.PeriodRepository;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.model.PeriodStats;

public class Calculator implements Runnable {
	final StockRepository stockRepository;
	final PeriodRepository periodRepository;
	String indexName;
	String type;
	LocalDateTime firstDate;
	LocalDateTime lastDate;
	Thread thread;
	PeriodStats periodStats;

	public Calculator(StockRepository stockRepository, PeriodRepository periodRepository, String indexName, String type,
			LocalDateTime firstDate, LocalDateTime lastDate) {
		thread = new Thread(this);
		this.stockRepository = stockRepository;
		this.periodRepository = periodRepository;
		this.indexName = indexName;
		this.type = type;
		this.firstDate = firstDate;
		this.lastDate = lastDate;
	}

//	public void CalculatorStart() {
//		thread.start();
//	}

//	public void join() {
//		try {
//			thread.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public void run() {
		if (!periodRepository.existsByIndexIgnoreCaseAndDateOfPurchaseAndType(indexName, firstDate, type)) {
			periodStats = stockRepository.calcIncomeForPeriod(indexName, firstDate, lastDate, type);
			if (Period.between(firstDate.toLocalDate(), lastDate.toLocalDate()).getYears() > 0) {
				double apyIncome = stockRepository
						.calcIncomeForPeriod(indexName, firstDate, firstDate.plusYears(1), type).getSaleAmount();
				periodStats = stockRepository.calcIncomeForPeriodWithApy(indexName, firstDate, lastDate, apyIncome,
						type);
			}
			periodRepository.save(periodStats);
		}
	}

}
