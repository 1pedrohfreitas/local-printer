package dev.pedrohfreitas.localprinter.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pedrohfreitas.localprinter.dtos.ResponseDTO;
import dev.pedrohfreitas.localprinter.services.PrinterService;

@RestController
@RequestMapping(value = "/local/printer")
public class PrinterController {
	
	private final PrinterService printerService;

	public PrinterController(PrinterService printerService) {
		this.printerService = printerService;
	}
	
	@GetMapping(path = "/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseDTO<Page<String>>> getPrinters(Pageable pageable) {
		if (pageable == null) {
			pageable = PageRequest.of(0, 100);
		}
		List<String> printers = printerService.getPrinters();
		Page<String> printersPage = new PageImpl<String>(printers, pageable, printers.size());
		return new ResponseDTO<Page<String>>().ok(printersPage, null);
	}
	
	@PostMapping(path = "/print", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseDTO<String>> print(@RequestBody String body) {
		printerService.printTempFile(body);
		return new ResponseDTO<String>().ok("OK", null);
	}
	
	
}
