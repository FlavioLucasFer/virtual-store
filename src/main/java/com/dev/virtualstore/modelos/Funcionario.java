package com.dev.virtualstore.modelos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name="funcionario")
public class Funcionario implements Serializable {
	public Funcionario() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotEmpty(message = "Nome é obrigatório")
	private String nome;
	private Double salarioBruto;
	@Temporal(TemporalType.DATE)
	private Date dataEntrada;
	@Temporal(TemporalType.DATE)
	private Date dataSaida;
	private String cargo;
	@ManyToOne
	private Cidade cidade;
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String uf;
	private String cep;
	@NotEmpty(message = "Email é obrigatório")
	@Email(message = "Email inválido")
	private String email;
	private String senha;
	private String codigoRecuperacao;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCodigoRecuperacao = new Date();

	@CPF(message = "CPF inválido")
	@Column(length = 14)
	private String cpf;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Double getSalarioBruto() {
		return salarioBruto;
	}
	
	public void setSalarioBruto(Double salarioBruto) {
		this.salarioBruto = salarioBruto;
	}
	
	public Date getDataEntrada() {
		return dataEntrada;
	}
	
	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}
	
	public Date getDataSaida() {
		return dataSaida;
	}
	
	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}
	
	public String getCargo() {
		return cargo;
	}
	
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	public Cidade getCidade() {
		return cidade;
	}
	
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	
	public String getLogradouro() {
		return logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getComplemento() {
		return complemento;
	}
	
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public String getUf() {
		return uf;
	}
	
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = new BCryptPasswordEncoder().encode(senha);
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		if (cpf != null && cpf.trim().length() == 0)
			this.cpf = null;
		else
			this.cpf = cpf;
	}

	public String getCodigoRecuperacao() {
		return codigoRecuperacao;
	}

	public void setCodigoRecuperacao(String codigoRecuperacao) {
		this.codigoRecuperacao = codigoRecuperacao;
	}

	public Date getDataCodigoRecuperacao() {
		return dataCodigoRecuperacao;
	}

	public void setDataCodigoRecuperacao(Date dataCodigoRecuperacao) {
		this.dataCodigoRecuperacao = dataCodigoRecuperacao;
	}
}
