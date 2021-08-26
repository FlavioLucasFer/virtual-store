package com.dev.virtualstore.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.virtualstore.modelos.EntradaItens;

public interface EntradaItensRepositorio extends JpaRepository<EntradaItens, Long> {
	
}
