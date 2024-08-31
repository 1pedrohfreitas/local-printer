package dev.pedrohfreitas.localprinter.services;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	
	private static final String ARQUIVO_JASPER_STRING = "report/pedido_report.jrxml";
	
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
			PDDocument documento;
			try {
				PrintService servico = PrintServiceLookup.lookupDefaultPrintService();
				documento = PDDocument.load(new File(file));
				PrinterJob job = PrinterJob.getPrinterJob();
				  job.setPageable(new PDFPageable(documento));
				  
				  job.setPrintService(servico);
				  job.print();
				  documento.close();
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
	}
	
	public static String timestampToStr(String pattern, Timestamp valor) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(Date.from(valor.toInstant()));
	}
	public String geraRelatorio(String nomeRelatorio, String conteudo) {
//		Parametros parametros = parametroService.getParametros();
		String nomeArquivo = String.format("%s.pdf",nomeRelatorio);
		File pastaPedidos = new File("relatorios");
		if (!pastaPedidos.exists()) {
		    pastaPedidos.mkdirs();
		}
		nomeArquivo = pastaPedidos.getPath() + File.separator + nomeArquivo;
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("CONTEUDO", conteudo);

		buildJarperReport(nomeArquivo, params);
		
//		if(parametros.isImprimir()) {
			print(nomeArquivo);			
//		}
		
		return nomeRelatorio;
	}

	
//	public String geraPedido(PedidoDTO pedidoDTO) {
//
//		String nomeArquivo = String.format("%d_pedido.pdf",pedidoDTO.getId());
//		File pastaPedidos = new File("pedidos");
//		if (!pastaPedidos.exists()) {
//		    pastaPedidos.mkdirs();
//		}
//		nomeArquivo = pastaPedidos.getPath() + File.separator + nomeArquivo;
//
//		Map<String, Object> params = new LinkedHashMap<>();
//		params.put("CONTEUDO", pedidoDTO.getPedidoRelatorio());
//		buildJarperReport(nomeArquivo, params);
//		
//			print(nomeArquivo);			
//
//		return "Imprimindo";
//	}
	
	private void buildJarperReport(String nomeArquivo, Map<String, Object> params) {
		Resource resource = new ClassPathResource(ARQUIVO_JASPER_STRING);
		try {
			File arquivo = new File(nomeArquivo);
			if(arquivo.exists()) {
				if (arquivo.delete()) {
	                System.out.println("Arquivo excluído com sucesso.");
	            } else {
	                System.out.println("Falha ao excluir o arquivo.");
	            }
			}
			
			InputStream arquivoJarper = resource.getInputStream();
			JasperReport jr = JasperCompileManager.compileReport(arquivoJarper);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jr, params,  new JRBeanCollectionDataSource(Arrays.asList("item")));
			
			JRPdfExporter jrPdfExporter = new JRPdfExporter();
			jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			
			jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(nomeArquivo));
			jrPdfExporter.exportReport();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	private void excluindoArquivoPdf(String nomeArquivo) {
		File arquivo = new File(nomeArquivo);
		if(arquivo.exists()) {
			if (arquivo.delete()) {
                System.out.println("Arquivo excluído com sucesso.");
            } else {
                System.out.println("Falha ao excluir o arquivo.");
            }
		}
	}
	
}
