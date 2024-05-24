package telran.java51.communication.service.task;

import java.time.LocalDate;
import java.time.Period;

import telran.java51.communication.dao.PeriodRepository;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.model.PeriodStats;

public class Calculator implements Runnable {
	final StockRepository stockRepository;
	final PeriodRepository periodRepository;
	String indexName;
	String type;
	LocalDate firstDate;
	LocalDate lastDate;
//	LocalDateTime firstDate;
//	LocalDateTime lastDate;
	Thread thread;
	PeriodStats periodStats = new PeriodStats();

	public Calculator(StockRepository stockRepository, PeriodRepository periodRepository, String indexName, String type,
			LocalDate firstDate, LocalDate lastDate) {
//		public Calculator(StockRepository stockRepository, PeriodRepository periodRepository, String indexName, String type,
//				LocalDateTime firstDate, LocalDateTime lastDate) {
		thread = new Thread(this);
//		periodStats.setId(indexName+firstDate+type);
		this.stockRepository = stockRepository;
		this.periodRepository = periodRepository;
		this.indexName = indexName;
		this.type = type;
		this.firstDate = firstDate;
		this.lastDate = lastDate;
	}

	public void CalculatorStart() {
		thread.start();
	}
	
	public void join() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (!periodRepository.existsByIndexIgnoreCaseAndDateOfPurchaseAndType(indexName, firstDate, type)) {
			periodStats = stockRepository.calcIncomeForPeriod(indexName, firstDate, lastDate, type);
			if (Period.between(firstDate, lastDate).getYears() > 0) {
				double apyIncome = stockRepository
						.calcIncomeForPeriod(indexName, firstDate, firstDate.plusYears(1), type).getSaleAmount();
				periodStats = stockRepository.calcIncomeForPeriodWithApy(indexName, firstDate, lastDate, apyIncome,
						type);
			}
			periodStats.setId(indexName+firstDate+type);

			periodRepository.save(periodStats);
		}
	}

}
