package com.dev.virtualstore.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dev.virtualstore.modelos.EntradaItens;
import com.dev.virtualstore.modelos.EntradaProduto;
import com.dev.virtualstore.modelos.Produto;
import com.dev.virtualstore.repositorios.EntradaProdutoRepositorio;
import com.dev.virtualstore.repositorios.EntradaItensRepositorio;
import com.dev.virtualstore.repositorios.FuncionarioRepositorio;
import com.dev.virtualstore.repositorios.ProdutoRepositorio;

@Controller
public class EntradaProdutoControle {
	private List<EntradaItens> listaEntrada = new ArrayList<EntradaItens>();
	
	@Autowired
	private EntradaProdutoRepositorio entradaProdutoRepositorio;
	
	@Autowired
	private EntradaItensRepositorio entradaItensRepositorio;
	
	@Autowired
	private FuncionarioRepositorio funcionarioRepositorio;
	
	@Autowired
	private ProdutoRepositorio produtoRepositorio;
	
	@GetMapping("/administrativo/entrada/cadastrar")
	public ModelAndView cadastrar(EntradaProduto entrada, EntradaItens entradaItens) {
		ModelAndView mv = new ModelAndView("/administrativo/entrada/cadastro");
		mv.addObject("entrada", entrada);
		mv.addObject("listaEntradaItens", this.listaEntrada);
		mv.addObject("entradaItens", entradaItens);
		mv.addObject("listaFuncionarios", funcionarioRepositorio.findAll());
		mv.addObject("listaProdutos", produtoRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/administrativo/entrada/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/entrada/lista");
		mv.addObject("listaEntradasProdutos", this.entradaProdutoRepositorio.findAll());
		
		return mv;
	}
	
//	@GetMapping("/administrativo/entrada/editar/{id}")
//	public ModelAndView editar(@PathVariable("id") Long id) {
//		Optional<EntradaProduto> cidade = this.entradaProdutoRepositorio.findById(id);
//		
//		return cadastrar(cidade.get());
//	}
	
//	@GetMapping("/administrativo/entrada/remover/{id}")
//	public ModelAndView remover(@PathVariable("id") Long id) {
//		Optional<EntradaProduto> cidade = this.entradaProdutoRepositorio.findById(id);
//		this.entradaProdutoRepositorio.delete(cidade.get());
//		
//		return listar();
//	}
	
	@PostMapping("/administrativo/entrada/salvar")
	public ModelAndView salvar(String acao, EntradaProduto entrada, EntradaItens entradaItens) {
		if (acao.equals("itens")) {
			this.listaEntrada.add(entradaItens);
		} else if (acao.equals("salvar")) {
			this.entradaProdutoRepositorio.saveAndFlush(entrada);
			
			for (EntradaItens item : this.listaEntrada) {
				item.setEntrada(entrada);
				this.entradaItensRepositorio.saveAndFlush(item);
				Optional<Produto> prod = this.produtoRepositorio.findById(item.getProduto().getId());
				Produto produto = prod.get();
				produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + item.getQuantidade());
				produto.setValorVenda(item.getValorVenda());
				this.produtoRepositorio.saveAndFlush(produto);
				this.listaEntrada = new ArrayList<>();
			}
		
			return cadastrar(new EntradaProduto(), new EntradaItens());
		}
		
		System.out.println(this.listaEntrada.size());
	
		
		return cadastrar(entrada, new EntradaItens());
	}
}
