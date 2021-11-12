package com.dev.virtualstore.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.virtualstore.modelos.Papel;

public interface PapelRepositorio extends JpaRepository<Papel, Long> {
	
}
