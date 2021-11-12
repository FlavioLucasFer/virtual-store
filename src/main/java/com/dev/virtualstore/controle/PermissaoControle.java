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

import com.dev.virtualstore.modelos.Permissao;
import com.dev.virtualstore.repositorios.PermissaoRepositorio;
import com.dev.virtualstore.repositorios.FuncionarioRepositorio;
import com.dev.virtualstore.repositorios.PapelRepositorio;

@Controller
public class PermissaoControle {
	@Autowired
	private PermissaoRepositorio permissaoRepositorio;
	
	@Autowired
	private FuncionarioRepositorio funcionarioRepositorio;

	@Autowired
	private PapelRepositorio papelRepositorio;
	
	@GetMapping("/administrativo/permissoes/cadastrar")
	public ModelAndView cadastrar(Permissao permissao) {
		ModelAndView mv = new ModelAndView("/administrativo/permissoes/cadastro");
		mv.addObject("permissao", permissao);
		mv.addObject("listaFuncionarios", funcionarioRepositorio.findAll());
		mv.addObject("listaPapeis", papelRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/administrativo/permissoes/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/permissoes/lista");
		mv.addObject("listaPermissoes", this.permissaoRepositorio.findAll());
		
		return mv;
	}
	
	@GetMapping("/administrativo/permissoes/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Permissao> permissao = this.permissaoRepositorio.findById(id);
		
		return cadastrar(permissao.get());
	}
	
	@GetMapping("/administrativo/permissoes/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Permissao> permissao = this.permissaoRepositorio.findById(id);
		this.permissaoRepositorio.delete(permissao.get());
		
		return listar();
	}
	
	@PostMapping("/administrativo/permissoes/salvar")
	public ModelAndView salvar(@Valid Permissao permissao, BindingResult result) {
		if (result.hasErrors()) {
			return cadastrar(permissao);
		}
		
		this.permissaoRepositorio.saveAndFlush(permissao);
		
		return this.listar();
	}
}
