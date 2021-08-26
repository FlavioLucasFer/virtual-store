package com.dev.virtualstore.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.virtualstore.modelos.Cidade;

public interface CidadeRepositorio extends JpaRepository<Cidade, Long> {
	
}
