package com.dev.virtualstore.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.virtualstore.modelos.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {
	List<Produto> findAllByDescricao(String descricao);
	@Query("FROM Produto p WHERE p.categoria.id = ?1")
	List<Produto> findAllByCategoria(Long categoriaId);
	@Query("FROM Produto p WHERE p.marca.id = ?1")
	List<Produto> findAllByMarca(Long marcaId);
}
