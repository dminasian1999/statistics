package telran.java51.communication.service.task;

import java.time.LocalDate;

import telran.java51.communication.dao.StockInfoRepository;
import telran.java51.communication.dao.StockRepository;
import telran.java51.communication.model.Stock;
import telran.java51.communication.model.StockInfo;

public class IndexPharser implements Runnable {
	final StockRepository stockRepository;
	final StockInfoRepository stockInfoRepository;
	String line;
	Thread thread;
	String index;
	StockInfo stockInfo;
	
	public IndexPharser(String line, String index, StockRepository stockRepository, StockInfoRepository stockInfoRepository, StockInfo stockInfo) {
		thread = new Thread(this);
		this.line = line;
		this.index = index;
		this.stockRepository = stockRepository;
		this.stockInfoRepository = stockInfoRepository;
		this.stockInfo = stockInfo;
	}
	
	public void IndexPharserStart() {
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
		String[] fields = line.split(",");
		Stock stock = new Stock(index+fields[0], index, LocalDate.parse(fields[0]),Double.parseDouble(fields[1]),
				Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]),
				Double.parseDouble(fields[5]), Long.parseLong(fields[6]));
		stockRepository.save(stock);
		stockInfoRepository.save(stockInfo);
		if (stockInfo.getHistory() == null
				|| stockInfo.getHistory().isBefore(LocalDate.parse(fields[0]).plusDays(1))) {
			stockInfo.setHistory(stockRepository.findFirstByIndexOrderByDateAsc(index).getDate());
		}

	}

}
