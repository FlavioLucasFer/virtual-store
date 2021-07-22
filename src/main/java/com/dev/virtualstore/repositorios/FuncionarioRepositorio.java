package com.dev.virtualstore.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.virtualstore.modelos.Funcionario;

public interface FuncionarioRepositorio extends JpaRepository<Funcionario, Long> {

}
