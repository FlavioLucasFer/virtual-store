package com.dev.virtualstore.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dev.virtualstore.modelos.Cliente;
import com.dev.virtualstore.modelos.Compra;
import com.dev.virtualstore.modelos.ItensCompra;
import com.dev.virtualstore.modelos.Produto;
import com.dev.virtualstore.repositorios.ClienteRepositorio;
import com.dev.virtualstore.repositorios.ProdutoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CarrinhoControle {

	private List<ItensCompra> itensCompra = new ArrayList<ItensCompra>();
	private Compra compra = new Compra();
	private Cliente cliente;

	@Autowired
	private ClienteRepositorio repositorioCliente;

	@Autowired
	private ProdutoRepositorio produtoRepositorio;

	private void calcularTotal() {
		this.compra.setValorTotal(.0);
		for (ItensCompra it : this.itensCompra) {
			this.compra.setValorTotal(this.compra.getValorTotal() + it.getValorTotal());
		}
	}

	private ItensCompra calcularValorTotalItem(ItensCompra item) {
		item.setValorTotal(.0);
		item.setValorTotal(item.getValorTotal() + (item.getQuantidade() * item.getValorUnitario()));
		return item;
	}

	private void buscarUsuarioLogado() {
		Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
		if (!(autenticado instanceof AnonymousAuthenticationToken)) {
			String email = autenticado.getName();
			cliente = repositorioCliente.buscarClienteEmail(email).get(0);
		}
	}

	@GetMapping("/carrinho")
	public ModelAndView chamarCarrinho() {
		ModelAndView mv = new ModelAndView("cliente/carrinho");
		calcularTotal();
		mv.addObject("compra", this.compra);
		mv.addObject("listaItens", this.itensCompra);
		return mv;
	}

	@GetMapping("/finalizar")
	public ModelAndView finalizarCompra() {
		buscarUsuarioLogado();
		ModelAndView mv = new ModelAndView("cliente/finalizar");
		calcularTotal();
		mv.addObject("compra", this.compra);
		mv.addObject("listaItens", this.itensCompra);
		mv.addObject("cliente", this.cliente);
		return mv;
	}

	@GetMapping("/alterarQuantidade/{id}/{acao}")
	public String alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
		for (ItensCompra it : this.itensCompra) {
			if (it.getProduto().getId().equals(id)) {
				if (acao.equals(1)) {
					it.setQuantidade(it.getQuantidade() + 1);
					it = calcularValorTotalItem(it);
				}
				else if (acao.equals(0) && it.getQuantidade() > 1) {
					it.setQuantidade(it.getQuantidade() - 1);
					it = calcularValorTotalItem(it);
				}
				break;
			}
		}

		return "redirect:/carrinho";
	}

	@GetMapping("/removerProduto/{id}")
	public String removerProdutoCarrinho(@PathVariable Long id) {
		for (ItensCompra it : this.itensCompra) {
			if (it.getProduto().getId().equals(id)) {
				itensCompra.remove(it);

				break;
			}
		}

		return "redirect:/carrinho";
	}

	@GetMapping("/adicionarCarrinho/{id}")
	public String adicionarCarrinho(@PathVariable Long id) {
		Optional<Produto> produto = this.produtoRepositorio.findById(id);
		Produto p = produto.get();
		ItensCompra item = new ItensCompra();

		boolean controle = false;
		for (ItensCompra it: this.itensCompra) {
			if (it.getProduto().getId().equals(p.getId())) {
				controle = true;
				it.setQuantidade(it.getQuantidade()+1);
				it = calcularValorTotalItem(it);
				break;
			}
		}

		if (!controle) {
			item.setProduto(p);
			item.setValorUnitario(p.getValorVenda());
			item.setQuantidade(item.getQuantidade()+1);
			item = calcularValorTotalItem(item);
	
			this.itensCompra.add(item);
		}

		return "redirect:/carrinho";
	}
}
