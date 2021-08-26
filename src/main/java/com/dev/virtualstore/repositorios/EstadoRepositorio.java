package com.dev.virtualstore.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.virtualstore.modelos.Estado;

public interface EstadoRepositorio extends JpaRepository<Estado, Long> {
	
}
