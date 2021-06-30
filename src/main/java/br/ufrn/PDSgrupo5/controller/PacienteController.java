package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.handler.UsuarioHelper;
import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.model.Usuario;
import br.ufrn.PDSgrupo5.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@Controller
@RequestMapping("paciente")
public class PacienteController {
    private PacienteService pacienteService;

    private UsuarioHelper usuarioHelper;

    @Autowired
    public PacienteController(PacienteService pacienteService, UsuarioHelper usuarioHelper){
        this.pacienteService = pacienteService;
        this.usuarioHelper = usuarioHelper;
    }

    @GetMapping
    public String salvar(){
        return "";
    }

    @GetMapping("/form")
    public String form(Model model){
        if(!model.containsAttribute("paciente")){
            model.addAttribute(new Paciente());
        }
        return "paciente/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Paciente paciente, BindingResult br, RedirectAttributes ra, Model model){

        br = pacienteService.validarPaciente(paciente, br);

        if(br.hasErrors()){
            model.addAttribute("message", "Erro ao salvar paciente");
            model.addAttribute(paciente);
            return form(model);
        }
        
        pacienteService.salvarNovoPaciente(paciente);

        return "redirect:paciente/login";
    }

    @PostMapping("/salvarEdicao")
    public String salvarEdicao(@Valid Paciente paciente, BindingResult br, RedirectAttributes ra, Model model){

        br = pacienteService.validarPaciente(paciente, br);

        if(br.hasErrors()){
            model.addAttribute("message", "Erro ao editar paciente");
            model.addAttribute(paciente);
            return editar(model);
        }
        pacienteService.salvar(paciente);

        return "redirect:paginaprincipallogado";
    }

    //o usuário edita seu próprio cadastro
    @GetMapping("/editar")
    public String editar(Model model){
        model.addAttribute(pacienteService.buscarPacientePorUsuarioLogado());
        return form(model);
    }

    //validador pode editar
    @GetMapping("/editarOutroUsuario/{id}")
    public String editarOutroPaciente(@PathVariable Long id, Model model){
        model.addAttribute(pacienteService.buscarPacientePorUsuario(id));
        return form(model);
    }

    @GetMapping("/perfil")
    public String visualizarPerfil(Model model){
        model.addAttribute(pacienteService.buscarPacientePorUsuarioLogado());
        return "paginadevisualizacaoPerfil";
    }

    @DeleteMapping("/excluirPerfil")
    public String excluirPerfil(){
        Paciente paciente = pacienteService.buscarPacientePorUsuarioLogado();
        paciente.setAtivo(false);
        pacienteService.salvar(paciente);

        return "/login";
    }

}
