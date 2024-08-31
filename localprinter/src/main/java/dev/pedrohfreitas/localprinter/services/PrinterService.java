package dev.pedrohfreitas.localprinter.services;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class PrinterService {
	
	public List<String> getPrinters(){
		List<String> printersNames = new ArrayList<>();
//		PrinterJob printerJob = PrinterJob.getPrinterJob();
        javax.print.PrintService[] printServices = PrinterJob.lookupPrintServices();
        if (printServices.length != 0) {
            for (javax.print.PrintService printService : printServices) {
            	printersNames.add(printService.getName());
            }
        }
		return printersNames;
	}
	
	public void print(String file) {
		try {
			PDDocument document;
			try {
				PrintService servico = PrintServiceLookup.lookupDefaultPrintService();
				document = PDDocument.load(new File(file));
				PrinterJob job = PrinterJob.getPrinterJob();
				  job.setPageable(new PDFPageable(document));
				  job.setPrintService(servico);
				  job.print();
				  document.close();
			} catch (InvalidPasswordException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (PrinterException e) {
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		eraserTempFile(file);
	}
	
	public static String getNow() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHMMss");
		return dateFormat.format(Date.from(Instant.now()));
	}
	
	public String printTempFile(String content) {
		String now = getNow();
		String tempFileName = String.format("%s.pdf",now);
		File tempFiles = new File("tempFiles");
		if (!tempFiles.exists()) {
			tempFiles.mkdirs();
		}
		
		tempFileName = tempFiles.getPath() + File.separator + tempFileName;

		Map<String, Object> params = new LinkedHashMap<>();
		params.put("CONTEUDO", content);
		buildJarperReport(tempFileName, params);
		
		print(tempFileName);			
		return "OK";
	}
	
	private void buildJarperReport(String tempFileName, Map<String, Object> params) {
		Resource resource = new ClassPathResource("report/report.jrxml");
		try {
			File tempFile = new File(tempFileName);
			InputStream jarperFile = resource.getInputStream();
			JasperReport jr = JasperCompileManager.compileReport(jarperFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jr, params,  new JRBeanCollectionDataSource(Arrays.asList("item")));
			
			JRPdfExporter jrPdfExporter = new JRPdfExporter();
			jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			
			jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(tempFile));
			jrPdfExporter.exportReport();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	private void eraserTempFile(String tempFileName) {
		File tempFile = new File(tempFileName);
		if(tempFile.exists()) {
			tempFile.delete();
		}
	}
	
}
