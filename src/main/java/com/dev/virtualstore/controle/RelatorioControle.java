package com.dev.virtualstore.controle;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.dev.virtualstore.servico.RelatorioServico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import net.sf.jasperreports.engine.JRException;

@Controller
public class RelatorioControle {
	@Autowired
	private RelatorioServico relatorioServico;

	@GetMapping("/listagemClientes")
	public void listagemClientes(HttpServletResponse response) throws SQLException, JRException, IOException {
		relatorioServico.emitir(response, "clientes");
	}

	@GetMapping("/listagemVendas")
	public void listagemVendas(HttpServletResponse response) throws SQLException, JRException, IOException {
		relatorioServico.emitir(response, "vendas");
	}
}
