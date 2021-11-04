package com.dev.virtualstore.controle;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dev.virtualstore.modelos.Cliente;
import com.dev.virtualstore.repositorios.CidadeRepositorio;
import com.dev.virtualstore.repositorios.ClienteRepositorio;

@Controller
public class ClienteControle {
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired 
	private CidadeRepositorio cidadeRepositorio;
	
	@GetMapping("/cliente/cadastrar")
	public ModelAndView cadastrar(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cliente/cadastrar");
		mv.addObject("cliente", cliente);
		mv.addObject("listaCidades", cidadeRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/cliente/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Cliente> cliente = this.clienteRepositorio.findById(id);
		
		return cadastrar(cliente.get());
	}
	
	@PostMapping("/cliente/salvar")
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result) {
		if (result.hasErrors()) {
			return cadastrar(cliente);
		}
		
		this.clienteRepositorio.saveAndFlush(cliente);
		
		return this.cadastrar(new Cliente());
	}
}
