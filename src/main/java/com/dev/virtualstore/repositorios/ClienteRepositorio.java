package com.dev.virtualstore.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.virtualstore.modelos.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
	
}
