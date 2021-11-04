package com.dev.virtualstore.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dev.virtualstore.modelos.ItensCompra;
import com.dev.virtualstore.modelos.Produto;
import com.dev.virtualstore.repositorios.ProdutoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CarrinhoControle {

	private List<ItensCompra> itensCompra = new ArrayList<ItensCompra>();

	@Autowired
	private ProdutoRepositorio produtoRepositorio;

	@GetMapping("/carrinho")
	public ModelAndView chamarCarrinho() {
		ModelAndView mv = new ModelAndView("cliente/carrinho");
		mv.addObject("listaItens", this.itensCompra);
		return mv;
	}

	@GetMapping("/alterarQuantidade/{id}/{acao}")
	public String alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
		for (ItensCompra it : this.itensCompra) {
			if (it.getProduto().getId().equals(id)) {
				if (acao.equals(1))
					it.setQuantidade(it.getQuantidade() + 1);
				else if (acao.equals(0) && it.getQuantidade() > 1)
					it.setQuantidade(it.getQuantidade() - 1);
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
				break;
			}
		}

		if (!controle) {
			item.setProduto(p);
			item.setValorUnitario(p.getValorVenda());
			item.setQuantidade(item.getQuantidade()+1);
			item.setValorTotal(item.getQuantidade()*item.getValorUnitario());
	
			this.itensCompra.add(item);
		}

		return "redirect:/carrinho";
	}
}
