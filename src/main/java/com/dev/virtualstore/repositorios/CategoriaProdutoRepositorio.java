package com.dev.virtualstore.repositorios;

import com.dev.virtualstore.modelos.CategoriaProduto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaProdutoRepositorio extends JpaRepository<CategoriaProduto, Long> {
	
}
