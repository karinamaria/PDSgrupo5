package br.ufrn.PDSgrupo5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;

@Controller
@RequestMapping("profissional-saude")
public class ProfissionalSaudeController {
	
	@Autowired
	private ProfissionalSaudeService profissionalSaudeService;
	
	//Processamento formulário cadastro profissional de sáude
	@PostMapping
	public String create(@RequestBody ProfissionalSaude entityProfissionalSaude) {
		ProfissionalSaude profissionalSaude = null;
		
		try {
			profissionalSaude = profissionalSaudeService.save(entityProfissionalSaude);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/profissional-saude/" + profissionalSaude.getId();
	}
}
