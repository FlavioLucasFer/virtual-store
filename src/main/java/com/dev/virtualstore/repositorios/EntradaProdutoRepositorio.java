package com.dev.virtualstore.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.virtualstore.modelos.EntradaProduto;

public interface EntradaProdutoRepositorio extends JpaRepository<EntradaProduto, Long> {
	
}
