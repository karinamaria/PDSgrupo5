package br.ufrn.PDSgrupo5.controller;

import javax.validation.Valid;

import br.ufrn.PDSgrupo5.exception.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		if(!model.containsAttribute("profissionalSaude")) {
			model.addAttribute(new ProfissionalSaude());
		}
		
		return "profissional-saude/form";
	}
	
	@PostMapping("/cadastrar")
	public String cadastrar(@Valid ProfissionalSaude profissionalSaude, BindingResult br, RedirectAttributes ra, Model model) {
		try{
			profissionalSaudeService.verificarPermissao(profissionalSaude);
			br = profissionalSaudeService.validarDados(profissionalSaude, br);

			if(br.hasErrors()) {
				model.addAttribute("message", "Erro ao cadastrar profissional da saúde");
				model.addAttribute(profissionalSaude);
				return form(model);
			}

			profissionalSaudeService.cadastrar(profissionalSaude);
		}catch(NegocioException ne){
			return "";
		}

		return "redirect:/login";
	}
	
	//o usuário edita seu próprio cadastro
    @GetMapping("/editar")
    public String editar(Model model){
        model.addAttribute(profissionalSaudeService.buscarProfissionalPorUsuarioLogado());
        return form(model);
    }

    //usuário com papel "validador" pode editar qualquer profissional da saúde
    @GetMapping("/editar-usuario/{id}")
    public String editarOutroPaciente(@PathVariable Long id, Model model){
        model.addAttribute(profissionalSaudeService.buscarProfissionalPorUsuario(id));
        return form(model);
    }

    @GetMapping("/perfil")
    public String visualizarPerfil(Model model){
        model.addAttribute(profissionalSaudeService.buscarProfissionalPorUsuarioLogado());
        return "paginadevisualizacaoPerfil";
    }

    @DeleteMapping("/excluir-perfil")
    public String excluirPerfil(){
        ProfissionalSaude ps = profissionalSaudeService.buscarProfissionalPorUsuarioLogado();
        ps.setAtivo(false);
        profissionalSaudeService.salvar(ps);

        return "/login";
    }
}
