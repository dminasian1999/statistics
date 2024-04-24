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
import telran.java51.communication.dto.IndexCorrelationDto;
import telran.java51.communication.dto.IndexDto;
import telran.java51.communication.dto.IndexHistoryDto;
import telran.java51.communication.dto.IndexPackageDto;
import telran.java51.communication.dto.IndexResponseApyAllDto;
import telran.java51.communication.dto.IndexResponseApyDto;
import telran.java51.communication.dto.IndexResponseIrrDto;
import telran.java51.communication.dto.IndexResponsePeriodDto;
import telran.java51.communication.dto.IndexResponseValueCloseDto;
import telran.java51.communication.service.CommunicationService;


@RestController
@RequestMapping("/communication")
@RequiredArgsConstructor
public class CommunicationController  {

	final CommunicationService communicationService;
	
	@PostMapping("/parser/{indexForHistory}/{csv}")
	public boolean addHistoryWithFile() {
		return communicationService.addHistoryWithFile();
	}

	@GetMapping("/parser/{indexForHistory}")
	public IndexHistoryDto getTimeHistoryForIndex(@PathVariable String indexForHistory) {
		return communicationService.getTimeHistoryForIndex();
	}

	@GetMapping("/index")
	public List<String> getAllIndexes() {
		return communicationService.getAllIndexes();
	}

	@PostMapping("/index")
	public List<IndexResponsePeriodDto> periodBeetwin(@RequestBody IndexDto index) {
		return communicationService.periodBeetwin(index);
	}

	@PostMapping("/data")
	public List<IndexResponseValueCloseDto> getAllValueCloseBetween(@RequestBody IndexDto index) {
		return communicationService.getAllValueCloseBetween(index);
	}

	@PostMapping("/index/sum")
	public IndexResponsePeriodDto calcSumPackage(@RequestBody IndexPackageDto indexPackage) {
		return communicationService.calcSumPackage(indexPackage);
	}

	@PostMapping("/index/apy")
	public IndexResponseApyDto calcIncomeWithApy(@RequestBody IndexDto index) {
		return communicationService.calcIncomeWithApy(index);
	}

	@PostMapping("/index/apy_all")
	public IndexResponseApyAllDto calcIncomeWithApyAllDate(@RequestBody IndexDto index) {
		return communicationService.calcIncomeWithApyAllDate(index);
	}

	@PostMapping("/index/irr")
	public IndexResponseIrrDto calcIncomeWithIrr(@RequestBody IndexDto index) {
		return communicationService.calcIncomeWithIrr(index);
	}

	@PostMapping("/index/correlation")
	public String calcCorrelation(@RequestBody IndexCorrelationDto index) {
		return communicationService.calcCorrelation(index);
	}

	@DeleteMapping("/index/{index}")
	public boolean deleteAllHistoryForCompany(@PathVariable String index) {
		return communicationService.deleteAllHistoryForCompany(index);
	}

}
