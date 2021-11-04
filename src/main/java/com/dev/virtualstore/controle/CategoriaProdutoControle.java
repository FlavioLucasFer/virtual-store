package com.dev.virtualstore.controle;

import java.util.Optional;

import javax.validation.Valid;

import com.dev.virtualstore.modelos.CategoriaProduto;
import com.dev.virtualstore.repositorios.CategoriaProdutoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CategoriaProdutoControle {
	@Autowired
	private CategoriaProdutoRepositorio repositorio;

	@GetMapping("/administrativo/categoriasProduto/cadastrar")
	public ModelAndView cadastrar(CategoriaProduto categoriaProduto) {
		ModelAndView mv = new ModelAndView("/administrativo/produtos/categorias/cadastro");
		mv.addObject("categoriaProduto", categoriaProduto);

		return mv;
	}

	@GetMapping("/administrativo/categoriasProduto/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/produtos/categorias/lista");
		mv.addObject("listaCategoriasProduto", this.repositorio.findAll());

		return mv;
	}

	@GetMapping("/administrativo/categoriasProduto/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<CategoriaProduto> categoriaProduto = this.repositorio.findById(id);

		return cadastrar(categoriaProduto.get());
	}

	@GetMapping("/administrativo/categoriasProduto/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<CategoriaProduto> categoriaProduto = this.repositorio.findById(id);
		this.repositorio.delete(categoriaProduto.get());

		return listar();
	}

	@PostMapping("/administrativo/categoriasProduto/salvar")
	public ModelAndView salvar(@Valid CategoriaProduto categoriaProduto, BindingResult result) {
		if (result.hasErrors()) {
			return cadastrar(categoriaProduto);
		}

		this.repositorio.saveAndFlush(categoriaProduto);

		return this.listar();
	}
}
