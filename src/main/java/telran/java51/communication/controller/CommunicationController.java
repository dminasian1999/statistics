package telran.java51.communication.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dto.StockCorrelationDto;
import telran.java51.communication.dto.StockDto;
import telran.java51.communication.dto.StockHistoryDto;
import telran.java51.communication.dto.StockPackageDto;
import telran.java51.communication.dto.StockResponseApyAllDto;
import telran.java51.communication.dto.StockResponseApyDto;
import telran.java51.communication.dto.StockResponseIrrDto;
import telran.java51.communication.dto.StockResponsePeriodDto;
import telran.java51.communication.dto.StockResponseValueCloseDto;
import telran.java51.communication.service.CommunicationService;


@RestController
@RequestMapping("/communication")
@RequiredArgsConstructor
public class CommunicationController  {

	final CommunicationService communicationService;
	
	@PostMapping("/parser/{index}/{csv}")
	public boolean addHistoryWithFile(@PathVariable String index, @PathVariable String csv) {
		return communicationService.addHistoryWithFile(index,csv);
	}

	@GetMapping("/index/{index}")
	public StockHistoryDto getTimeHistoryForIndex(@PathVariable String index) {
		return communicationService.getTimeHistoryForIndex(index);
	}

	@GetMapping("/index")
	public List<String> getAllIndexes() {
		return communicationService.getAllIndexes();
	}

	@PostMapping("/index")
	public StockResponsePeriodDto periodBeetwin(@RequestBody StockDto index) {
		return communicationService.periodBeetwin(index);
	}

	@PostMapping("/data")
	public List<StockResponseValueCloseDto> getAllValueCloseBetween(@RequestBody StockDto index) {
		return communicationService.getAllValueCloseBetween(index);
	}

	@PostMapping("/index/sum")
	public StockResponsePeriodDto calcSumPackage(@RequestBody StockPackageDto indexPackage) {
		return communicationService.calcSumPackage(indexPackage);
	}

	@PostMapping("/index/apy")
	public StockResponseApyDto calcIncomeWithApy(@RequestBody StockDto index) {
		return communicationService.calcIncomeWithApy(index);
	}

	@PostMapping("/index/apy_all")
	public StockResponseApyAllDto calcIncomeWithApyAllDate(@RequestBody StockDto index) {
		return communicationService.calcIncomeWithApyAllDate(index);
	}

	@PostMapping("/index/irr")
	public StockResponseIrrDto calcIncomeWithIrr(@RequestBody StockDto index) {
		return communicationService.calcIncomeWithIrr(index);
	}

	@PostMapping("/index/correlation")
	public String calcCorrelation(@RequestBody StockCorrelationDto index) {
		return communicationService.calcCorrelation(index);
	}

	@DeleteMapping("/index/{index}")
	public boolean deleteAllHistoryForCompany(@PathVariable String index) {
		return communicationService.deleteAllHistoryForCompany(index);
	}

}
