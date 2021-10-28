package com.dev.virtualstore.controle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AutencicacaoControle {
	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView("/login"); 

		return mv;
	}
	
	@GetMapping("/negado")
	public ModelAndView negado() {
		ModelAndView mv = new ModelAndView("/negado"); 
	
		return mv;
	}
}
