package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.exception.NegocioException;
import br.ufrn.PDSgrupo5.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.AtendimentoService;
import br.ufrn.PDSgrupo5.service.DataHoraService;
import br.ufrn.PDSgrupo5.service.HorarioAtendimentoService;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;

@Controller
@RequestMapping("profissional-saude")
public class ProfissionalSaudeController {
	private ProfissionalSaudeService profissionalSaudeService;
	private HorarioAtendimentoService horarioAtendimentoService;
	private DataHoraService dataHoraService;
	private AtendimentoService atendimentoService;
	
	@Autowired
	ProfissionalSaudeController(ProfissionalSaudeService profissionalSaudeService, HorarioAtendimentoService horarioAtendimentoService,
								DataHoraService dataHoraService, AtendimentoService atendimentoService){
		this.profissionalSaudeService = profissionalSaudeService;
		this.horarioAtendimentoService = horarioAtendimentoService;
		this.dataHoraService = dataHoraService;
		this.atendimentoService = atendimentoService;
	}
	
	@GetMapping("/form")
	public String form(Model model) {
		if(!model.containsAttribute("profissionalSaude")) {
			model.addAttribute(new ProfissionalSaude());
		}
		
		return "profissional-saude/form";
	}
	
    @PostMapping("/salvar")
    public String salvar(@Valid ProfissionalSaude profissionalSaude, BindingResult br, RedirectAttributes ra, Model model) {
        try{
            profissionalSaude = profissionalSaudeService.verificarEdicao(profissionalSaude);
            profissionalSaudeService.validarDados(profissionalSaude, br);

            profissionalSaudeService.salvarProfissional(profissionalSaude);
        }catch (ValidacaoException validacaoException){
            model.addAttribute("message", "Erro ao cadastrar profissional da saúde");
            model.addAttribute(profissionalSaude);
            return form(model);
        }
        return "redirect:/dashboard";
    }

    //o usuário edita seu próprio cadastro
    @GetMapping("/editar")
    public String editar(Model model) {
        model.addAttribute(profissionalSaudeService.buscarProfissionalPorUsuarioLogado());
        return "profissional-saude/form";
    }

    //usuário com papel "validador" pode editar qualquer profissional da saúde
    @GetMapping("/editar-usuario/{id}")
    public String editarOutroProfissional(@PathVariable Long id, Model model) {
        model.addAttribute(profissionalSaudeService.buscarProfissionalPorUsuario(id));
        return form(model);
    }

    /**
     * O profissional pode visualizar seu próprio perfil
     *
     * @param model
     * @return
     */
    @GetMapping("/perfil")
    public String visualizarPerfil(Model model) {
        model.addAttribute(profissionalSaudeService.buscarProfissionalPorUsuarioLogado());
        return "paginadevisualizacaoPerfil";
    }
    
    @DeleteMapping("/excluirPerfil")
    public String excluirPerfil(){
        ProfissionalSaude ps = profissionalSaudeService.buscarProfissionalPorUsuarioLogado();
        profissionalSaudeService.excluir(ps);

        return "redirect:/login";
    }
    
    @GetMapping("/horariosAtendimento")
    public String horariosAtendimento(Model model) {
    	ProfissionalSaude ps = profissionalSaudeService.buscarProfissionalPorUsuarioLogado();
    	model.addAttribute("horariosAtendimento", ps.getHorarioAtendimento());
    	return "profissional-saude/horariosAtendimento";
    }
    
    @PostMapping("/addHorarioAtendimento")
    public String addHorarioAtendimento(@RequestParam("data") String data, @RequestParam("horaInicio") String horaInicio,
    								  @RequestParam("horaFim") String horaFim, Model model) {
    	
		try {
			Date horarioInicio = dataHoraService.converterParaDate(data, horaInicio);
			Date horarioFim = dataHoraService.converterParaDate(data, horaFim);
			HorarioAtendimento ha = horarioAtendimentoService.converterParaHorarioAtendimento(horarioInicio, horarioFim);
			
			horarioAtendimentoService.validarHorario(ha);

			profissionalSaudeService.adicionarHorarioAtendimento(ha);
			
		} catch (ParseException e) {
			return "redirect:/profissional-saude/error";
		} catch (NegocioException ne){
            model.addAttribute("mensagemErro", ne.getMessage());
            return horariosAtendimento(model);
        }
    	
    	return "redirect:/profissional-saude/horariosAtendimento";
    }

    @GetMapping("/excluirHorarioAtendimento")
    public String excluirHorarioAtendimento(@RequestParam("idHorarioAtendimento") Long idHorarioAtendimento){
        profissionalSaudeService.excluirHorarioAtendimento(idHorarioAtendimento);

        return "redirect:/profissional-saude/horariosAtendimento";
    }

    @PostMapping("/aceitarRecusarAtendimento")
    public String aceitarRecusarAtendimento(@RequestParam("atendimentoId") Long id,
                                            @RequestParam("autorizacaoAtendimento") boolean autorizacao){
        atendimentoService.aceitarRecusarAtendimento(id, autorizacao);
        return "redirect:/dashboard";
    }
}
