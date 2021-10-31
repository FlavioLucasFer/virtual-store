package com.dev.virtualstore.controle;

import java.util.Optional;

import javax.validation.Valid;

import com.dev.virtualstore.modelos.MarcaProduto;
import com.dev.virtualstore.repositorios.MarcaProdutoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MarcaProdutoControle {
	@Autowired
	private MarcaProdutoRepositorio repositorio;

	@GetMapping("/administrativo/marcasProduto/cadastrar")
	public ModelAndView cadastrar(MarcaProduto marcaProduto) {
		ModelAndView mv = new ModelAndView("/administrativo/produtos/marcas/cadastro");
		mv.addObject("marcaProduto", marcaProduto);

		return mv;
	}

	@GetMapping("/administrativo/marcasProduto/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/produtos/marcas/lista");
		mv.addObject("listaMarcasProduto", this.repositorio.findAll());

		return mv;
	}

	@GetMapping("/administrativo/marcasProduto/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<MarcaProduto> marcaProduto = this.repositorio.findById(id);

		return cadastrar(marcaProduto.get());
	}

	@GetMapping("/administrativo/marcasProduto/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<MarcaProduto> marcaProduto = this.repositorio.findById(id);
		this.repositorio.delete(marcaProduto.get());

		return listar();
	}

	@PostMapping("/administrativo/marcasProduto/salvar")
	public ModelAndView salvar(@Valid MarcaProduto marcaProduto, BindingResult result) {
		if (result.hasErrors()) {
			return cadastrar(marcaProduto);
		}

		this.repositorio.saveAndFlush(marcaProduto);

		return this.listar();
	}
}
