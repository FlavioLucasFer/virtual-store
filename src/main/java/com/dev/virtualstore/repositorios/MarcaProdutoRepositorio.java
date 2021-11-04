package com.dev.virtualstore.repositorios;

import com.dev.virtualstore.modelos.MarcaProduto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaProdutoRepositorio extends JpaRepository<MarcaProduto, Long> {
	
}
