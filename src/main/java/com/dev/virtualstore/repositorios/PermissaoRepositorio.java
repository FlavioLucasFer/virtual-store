package com.dev.virtualstore.repositorios;

import com.dev.virtualstore.modelos.Permissao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissaoRepositorio extends JpaRepository<Permissao, Long> {
	
}
