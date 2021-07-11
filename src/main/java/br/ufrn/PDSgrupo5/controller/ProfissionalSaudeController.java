package br.ufrn.PDSgrupo5.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import br.ufrn.PDSgrupo5.exception.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.model.TurnoAtendimento;
import br.ufrn.PDSgrupo5.service.DataHoraService;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;
import br.ufrn.PDSgrupo5.service.TurnoAtendimentoService;

@Controller
@RequestMapping("profissional-saude")
public class ProfissionalSaudeController {
	private ProfissionalSaudeService profissionalSaudeService;
	private TurnoAtendimentoService turnoAtendimentoService;
	private DataHoraService dataHoraService;
	
	@Autowired
	ProfissionalSaudeController(ProfissionalSaudeService profissionalSaudeService, TurnoAtendimentoService turnoAtendimentoService,
								DataHoraService dataHoraService){
		this.profissionalSaudeService = profissionalSaudeService;
		this.turnoAtendimentoService = turnoAtendimentoService;
		this.dataHoraService = dataHoraService;
	}
	
	@GetMapping("/form")
	public String form(Model model) {
		if(!model.containsAttribute("profissionalSaude")) {
			model.addAttribute(new ProfissionalSaude());
		}
		
		return "profissional-saude/form";
	}
	
	@PostMapping("/cadastrar")
	public String cadastrar(@Valid ProfissionalSaude profissionalSaude, BindingResult br, Model model) {
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
	
    @GetMapping("/editar")
    public String editar(Model model){
        model.addAttribute(profissionalSaudeService.buscarProfissionalPorUsuarioLogado());
        return form(model);
    }
    
	@GetMapping("/perfil")
    public String visualizarPerfil(Model model){
        model.addAttribute(profissionalSaudeService.buscarProfissionalPorUsuarioLogado());
        return "paginadevisualizacaoPerfil";
    }

    @DeleteMapping("/excluirPerfil")
    public String excluirPerfil(){
        ProfissionalSaude ps = profissionalSaudeService.buscarProfissionalPorUsuarioLogado();
        ps.setAtivo(false);
        profissionalSaudeService.salvar(ps);

        return "/login";
    }
    
    @GetMapping("/formTurnoAtendimento")
    public String formTurnoAtendimento(Model model) {
    	return "profissional-saude/formTurnoAtendimento";
    }
    
    @PostMapping("/addTurnoAtendimento")
    public String addTurnoAtendimento(@RequestParam("data") String data, @RequestParam("horaInicio") String horaInicio,
    								  @RequestParam("horaFim") String horaFim, Model model) {
    	
		try {
			Date horarioInicio = dataHoraService.converterParaDate(data, horaInicio);
			Date horarioFim = dataHoraService.converterParaDate(data, horaFim);
			TurnoAtendimento ta = turnoAtendimentoService.converterParaTurnoAtendimento(horarioInicio, horarioFim);
			
			String erro = turnoAtendimentoService.validarTurno(ta);
			if(Objects.nonNull(erro)) {
				model.addAttribute("mensagemErro", erro);
				return formTurnoAtendimento(model);
			}
			
			profissionalSaudeService.adicionarTurnoAtendimento(ta);
			
		} catch (ParseException e) {
			return "redirect:/profissional-saude/error";
		}
    	
    	return "redirect:/profissional-saude/principal";
    }
}
