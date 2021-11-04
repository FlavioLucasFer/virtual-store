package com.dev.virtualstore.modelos;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "produtos")
public class Produto implements Serializable {

	public Produto() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String descricao;
	private Double valorVenda;
	@ManyToOne
	private CategoriaProduto categoria;
	@ManyToOne
	private MarcaProduto marca;
	private Double quantidadeEstoque=0.;
	@OneToMany(mappedBy = "produto")
	private List<FotoProduto> fotos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(Double valorVenda) {
		this.valorVenda = valorVenda;
	}

	public CategoriaProduto getCategoria() {
		if (categoria == null)
			return new CategoriaProduto();

		return categoria;
	}

	public void setCategoria(CategoriaProduto categoria) {
		this.categoria = categoria;
	}

	public MarcaProduto getMarca() {
		if (marca == null)
			return new MarcaProduto();

		return marca;
	}

	public void setMarca(MarcaProduto marca) {
		this.marca = marca;
	}

	public Double getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Double quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public List<FotoProduto> getFotos() {
		return fotos;
	}
}