package com.dev.virtualstore.controle;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dev.virtualstore.modelos.Funcionario;
import com.dev.virtualstore.repositorios.FuncionarioRepositorio;
import com.dev.virtualstore.servico.EmailServico;
import com.dev.virtualstore.repositorios.CidadeRepositorio;

@Controller
public class FuncionarioControle {
	@Autowired
	private FuncionarioRepositorio funcionarioRepositorio;
	
	@Autowired
	private CidadeRepositorio cidadeRepositorio;

	@Autowired
	private EmailServico emailServico;
	
	@GetMapping("/administrativo/funcionarios/cadastrar")
	public ModelAndView cadastrar(Funcionario funcionario) {
		ModelAndView mv = new ModelAndView("/administrativo/funcionarios/cadastro");
		mv.addObject("funcionario", funcionario);
		mv.addObject("listaCidades", cidadeRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/administrativo/funcionarios/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/funcionarios/lista");
		mv.addObject("listaFuncionarios", this.funcionarioRepositorio.findAll());
		
		return mv;
	}
	
	@GetMapping("/administrativo/funcionarios/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Funcionario> funcionario = this.funcionarioRepositorio.findById(id);
		
		return cadastrar(funcionario.get());
	}
	
	@GetMapping("/administrativo/funcionarios/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Funcionario> funcionario = this.funcionarioRepositorio.findById(id);
		this.funcionarioRepositorio.delete(funcionario.get());
		
		return listar();
	}
	
	@PostMapping("/administrativo/funcionarios/salvar")
	public ModelAndView salvar(@Valid Funcionario funcionario, BindingResult result) {
		if (result.hasErrors()) {
			return cadastrar(funcionario);
		}

		String senha = this.gerarSenhaAleatoria();

		System.out.println("senha: "+senha);

		funcionario.setSenha(senha);
		this.funcionarioRepositorio.saveAndFlush(funcionario);

		System.out.println("senha: " + senha);

		this.enviarEmailEmOutraThread(funcionario.getEmail(), "Senha de acesso ao sistema",
				"Seja bem vindo à Virtual Store " + funcionario.getNome() + "!"
						+ "\nPara sua segurança geramos uma senha aleatória, acreditamos que isso é mais seguro para você. "
						+ "\nSua senha de acesso ao sistema é: " + senha);

		System.out.println("senha: " + senha);
		
		return this.listar();
	}

	private void enviarEmailEmOutraThread(String destinatario, String assunto, String mensagem) {
		new Thread() {

			@Override
			public void run() {
				emailServico.enviar(destinatario, assunto, mensagem);
			}
		}.start();
	}

	private String gerarSenhaAleatoria() {
		byte[] bytearray;
		bytearray = new byte[256];
		String mystring;
		StringBuffer thebuffer;
		String theAlphaNumericS;
		int i = 10;

		new Random().nextBytes(bytearray);

		mystring = new String(bytearray, Charset.forName("UTF-8"));

		thebuffer = new StringBuffer();

		theAlphaNumericS = mystring.replaceAll("[^A-Z0-9]", "");

		for (int m = 0; m < theAlphaNumericS.length(); m++) {

			if (Character.isLetter(theAlphaNumericS.charAt(m)) && (i > 0)
					|| Character.isDigit(theAlphaNumericS.charAt(m)) && (i > 0)) {

				thebuffer.append(theAlphaNumericS.charAt(m));
				i--;
			}
		}

		return thebuffer.toString();
	}

	private String gerarCodigoRecuperacaoSenha() {
		String codigo = "";

		Random random = new Random();

		for (int i = 0; i < 3; i++) {
			codigo += (char)(random.nextInt(26) + 'A');
		}

		codigo += '-';

		for (int i = 0; i < 5; i++) {
			codigo += random.nextInt(9);
		}

		return codigo;
	}

	@PostMapping("login/recuperarSenha/enviarCodigo")
	public String enviarCodigoRecuperacaoSenha(@RequestParam("email") String email, Model model) throws Exception {
		try {
			Funcionario funcionario = this.funcionarioRepositorio.findByEmail(email);

			if (funcionario == null)
				throw new Exception("Não encontramos um funcionário cadastrado em nossa base com o email informado.");
			
			funcionario.setCodigoRecuperacao(this.gerarCodigoRecuperacaoSenha());
			funcionario.setDataCodigoRecuperacao(new Date());

			this.emailServico.enviar(email, "Recuperação de senha", "Olá " + funcionario.getNome() + "!"
					+ "\nEste é seu código de recuperação de senha: " + funcionario.getCodigoRecuperacao());
			this.funcionarioRepositorio.save(funcionario);
		} catch (Exception e) {
			throw e;
		}

		return "login";
	}

	@PostMapping("login/recuperarSenha/confirmarCodigo")
	public String confirmarCodigoRecuperacao(@RequestParam("email") String email, @RequestParam("codigo") String codigo, Model model) throws Exception {
		try {
			Funcionario funcionario = this.funcionarioRepositorio.findByEmail(email);
			
			if ((new Date().getTime() - funcionario.getDataCodigoRecuperacao().getTime()) > 600000)
				throw new Exception("Seu código de recuperação expirou. Códigos de recuperação são válidos por 10 minutos!");

			else if (!funcionario.getCodigoRecuperacao().equals(codigo))
				throw new Exception("Código de recuperação inválido!");

			String novaSenha = this.gerarSenhaAleatoria();
			funcionario.setSenha(novaSenha);
			funcionario.setCodigoRecuperacao(null);
			funcionario.setDataCodigoRecuperacao(null);

			this.funcionarioRepositorio.save(funcionario);

			this.emailServico.enviar(email, "Senha redefinida", "Olá " + funcionario.getNome() + "!"
					+ "\nSua senha foi redefinida. Sua nova senha é: " + novaSenha);
		} catch (Exception e) {
			throw e;
		}
		
		return "login";
	}
}
