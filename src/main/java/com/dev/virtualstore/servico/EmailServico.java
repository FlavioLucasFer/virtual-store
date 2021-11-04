package com.dev.virtualstore.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServico {
	@Autowired
	private JavaMailSender mailSender;

	public void enviar(String destinatario, String assunto, String mensagem) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	
			simpleMailMessage.setFrom("virtualstorees@gmail.com");
			simpleMailMessage.setTo(destinatario);
			simpleMailMessage.setSubject(assunto);
			simpleMailMessage.setText(mensagem);

			this.mailSender.send(simpleMailMessage);
		} catch (Exception e) {
			throw e;
		}
	}
}
