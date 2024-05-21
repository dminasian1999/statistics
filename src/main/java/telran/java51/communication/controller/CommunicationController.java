package telran.java51.communication.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dto.CorrelationDto;
import telran.java51.communication.dto.RequestDto;
import telran.java51.communication.dto.HistoryDto;
import telran.java51.communication.dto.RequestPackageDto;
import telran.java51.communication.dto.ResponseApyAllDto;
import telran.java51.communication.dto.ResponseApyDto;
import telran.java51.communication.dto.ResponseIrrDto;
import telran.java51.communication.dto.ResponsePeriodDto;
import telran.java51.communication.dto.ResponseValueCloseDto;
import telran.java51.communication.service.CommunicationService;


@RestController
@RequestMapping("/communication")
@RequiredArgsConstructor
@CrossOrigin
public class CommunicationController  {

	final CommunicationService communicationService;
	
	@PostMapping("/parser/{index}/{csv}")
	public boolean addHistoryWithFile(@PathVariable String index, @PathVariable String csv) {
		return communicationService.addHistoryWithFile(index,csv);
	}

	@GetMapping("/index/{index}")
	public HistoryDto getTimeHistoryForIndex(@PathVariable String index) {
		return communicationService.getTimeHistoryForIndex(index);
	}

	@GetMapping("/index")
	public List<String> getAllIndexes() {
		return communicationService.getAllIndexes();
	}

	@PostMapping("/index")
	public List<ResponsePeriodDto> periodBeetwin(@RequestBody RequestDto index) {
		return communicationService.periodBeetwin(index);
	}

	@PostMapping("/data")
	public List<ResponseValueCloseDto> getAllValueCloseBetween(@RequestBody RequestDto index) {
		return communicationService.getAllValueCloseBetween(index);
	}

	@PostMapping("/index/sum")
	public ResponsePeriodDto calcSumPackage(@RequestBody RequestPackageDto indexPackage) {
		return communicationService.calcSumPackage(indexPackage);
	}

	@PostMapping("/index/apy")
	public ResponseApyDto calcIncomeWithApy(@RequestBody RequestDto index) {
		return communicationService.calcIncomeWithApy(index);
	}

	@PostMapping("/index/apy_all")
	public List<ResponseApyAllDto>  calcIncomeWithApyAllDate(@RequestBody RequestDto index) {
		return communicationService.calcIncomeWithApyAllDate(index);
	}

	@PostMapping("/index/irr")
	public  List<ResponseIrrDto>  calcIncomeWithIrr(@RequestBody RequestDto index) {
		return communicationService.calcIncomeWithIrr(index);
	}

	@PostMapping("/index/correlation")
	public String calcCorrelation(@RequestBody CorrelationDto index) {
		return communicationService.calcCorrelation(index);
	}

	@DeleteMapping("/index/{index}")
	public boolean deleteAllHistoryForCompany(@PathVariable String index) {
		return communicationService.deleteAllHistoryForCompany(index);
	}

}
