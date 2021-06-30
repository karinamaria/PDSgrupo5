package br.ufrn.PDSgrupo5.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;

@Controller
@RequestMapping("profissional-saude")
public class ProfissionalSaudeController {
	private ProfissionalSaudeService profissionalSaudeService;
	
	@Autowired
	ProfissionalSaudeController(ProfissionalSaudeService profissionalSaudeService){
		this.profissionalSaudeService = profissionalSaudeService;
	}
	
	@GetMapping("/form")
	public String form(Model model) {
		if(model.containsAttribute("profissionalSaude")) {
			model.addAttribute(new ProfissionalSaude());
		}
		
		return "profissional-saude/form";
	}
	
	@PostMapping("/cadastrar")
	public String cadastrar(@Valid ProfissionalSaude profissionalSaude, BindingResult br, RedirectAttributes ra, Model model) {
		br = profissionalSaudeService.validarProfissionalSaude(profissionalSaude, br);
		
		if(br.hasErrors()) {
			model.addAttribute("message", "Erro ao cadastrar profissional da saúde");
			model.addAttribute(profissionalSaude);
			return form(model);
		}
		
		profissionalSaudeService.cadastrarProfissional(profissionalSaude);
		return "redirect:/profissional-saude/login" + profissionalSaude.getId();
	}
}
