package com.dev.virtualstore.servico;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class RelatorioServico {
	@Autowired
	private DataSource dataSource;

	public void emitir(HttpServletResponse response, String file) throws SQLException, JRException, IOException {
		InputStream jasperFile = this.getClass().getResourceAsStream("/relatorios/"+file+".jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource.getConnection());

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline;filename=relatorioclientes.pdf");

		OutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	}
}
