package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.exception.NegocioException;
import br.ufrn.PDSgrupo5.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.handler.UsuarioHelper;
import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.AtendimentoService;
import br.ufrn.PDSgrupo5.service.PacienteService;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class HomeController {
    private PacienteService pacienteService;

    private ProfissionalSaudeService profissionalSaudeService;

    private AtendimentoService atendimentoService;

    private UsuarioHelper usuarioHelper;

    @Autowired
    public HomeController(PacienteService pacienteService, ProfissionalSaudeService profissionalSaudeService,
                         AtendimentoService atendimento, UsuarioHelper usuarioHelper){
        this.pacienteService = pacienteService;
        this.profissionalSaudeService = profissionalSaudeService;
        this.atendimentoService = atendimento;
        this.usuarioHelper = usuarioHelper;
    }

    @RequestMapping("/dashboard")
    public String dashBoard(Model model){
        if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel().equals(EnumTipoPapel.VALIDADOR)){
            model.addAttribute("profissionais", profissionalSaudeService.listarProfissionaisStatusLegalizacao(false));
        }
        else if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel().equals(EnumTipoPapel.PACIENTE)){
            model.addAttribute("profissionais", profissionalSaudeService.listarProfissionaisStatusLegalizacao(true));
            model.addAttribute("proximosAtendimentos", atendimentoService.buscarProximosAtendimentosPaciente());
        }
        else{
            model.addAttribute("atendimentosStatusPendente", atendimentoService.buscarAtendimentosAguardandoConfirmacao());
            model.addAttribute("proximosAtendimentos", atendimentoService.buscarProximosAtendimentosProfissional());
        }
        return "dashboard";
    }

    @RequestMapping("/login")
    public String login(Model model){

        if(!model.containsAttribute("paciente")){
            model.addAttribute("paciente",new Paciente());
        }
        if(!model.containsAttribute("profissionalSaude")){
            model.addAttribute("profissionalSaude",new ProfissionalSaude());
        }

        if(!model.containsAttribute("active_tab")){
            model.addAttribute("active_tab",null);
        }

        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/novo-paciente/salvar")
    public String novoPaciente(@Valid Paciente paciente, BindingResult br, RedirectAttributes ra){
        try{
            pacienteService.verificarPermissao(paciente);
            pacienteService.validarPaciente(paciente, br);
            pacienteService.salvarPaciente(paciente);
            ra.addFlashAttribute("active_tab",null); //ir para página de login
        }catch(NegocioException ne){
            return "";//o usuário não tem permissão para editar outro candidato. Apresente página de erro
        }catch(ValidacaoException validacaoException){
            ra.addFlashAttribute("org.springframework.validation.BindingResult.paciente", validacaoException.getBindingResult());
            ra.addFlashAttribute("message", "Erro ao salvar paciente");
            ra.addFlashAttribute("paciente",paciente);
            ra.addFlashAttribute("active_tab","paciente");
        }

        return "redirect:/login";
    }

    @PostMapping("/novo-profissional/salvar")
    public String novoProfissionalSaude(@Valid ProfissionalSaude profissionalSaude, BindingResult br, RedirectAttributes ra){
        try{
            profissionalSaudeService.verificarPermissao(profissionalSaude);
            profissionalSaudeService.validarDados(profissionalSaude, br);
            profissionalSaudeService.salvarProfissional(profissionalSaude);
            ra.addFlashAttribute("active_tab",null);
        }catch(NegocioException ne){
            return "";//usuário não tem permissão para edição
        }catch(ValidacaoException validacaoException){
            ra.addFlashAttribute("org.springframework.validation.BindingResult.profissionalSaude", validacaoException.getBindingResult());
            ra.addFlashAttribute("message", "Erro ao salvar profissional da saúde");
            ra.addFlashAttribute("profissionalSaude",profissionalSaude);
            ra.addFlashAttribute("active_tab","profissional");
        }

        return "redirect:/login";
    }
}
