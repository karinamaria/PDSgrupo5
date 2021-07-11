package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.exception.NegocioException;
import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.PacienteService;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {
    private PacienteService pacienteService;

    private ProfissionalSaudeService profissionalSaudeService;

    @Autowired
    public HomeController(PacienteService pacienteService, ProfissionalSaudeService profissionalSaudeService){
        this.pacienteService = pacienteService;
        this.profissionalSaudeService = profissionalSaudeService;
    }

    @RequestMapping("/dashboard")
    public String dashBoard(Model model){
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
            br = pacienteService.validarPaciente(paciente, br);

            if(br.hasErrors()){
                ra.addFlashAttribute("org.springframework.validation.BindingResult.paciente", br);
                ra.addFlashAttribute("message", "Erro ao salvar paciente");
                ra.addFlashAttribute("paciente",paciente);
                ra.addFlashAttribute("active_tab","paciente");
                return "redirect:/login";
            }
            pacienteService.salvarPaciente(paciente);

        }catch(NegocioException ne){
            return "";//o usuário não tem permissão para editar outro candidato. Apresente página de erro
        }

        return "redirect:/login";
    }

    @PostMapping("/novo-profissional/salvar")
    public String novoProfissionalSaude(@Valid ProfissionalSaude profissionalSaude, BindingResult br, RedirectAttributes ra){
        try{
            profissionalSaudeService.verificarPermissao(profissionalSaude);
            br = profissionalSaudeService.validarDados(profissionalSaude, br);

            if(br.hasErrors()) {
                ra.addFlashAttribute("org.springframework.validation.BindingResult.paciente", br);
                ra.addFlashAttribute("message", "Erro ao salvar profissional da saúde");
                ra.addFlashAttribute("profissionalSaude",profissionalSaude);
                ra.addFlashAttribute("active_tab","profissional");
                return "redirect:/login";
            }

            profissionalSaudeService.cadastrar(profissionalSaude);
        }catch(NegocioException ne){
            return "";
        }

        return "redirect:/login";
    }
}
