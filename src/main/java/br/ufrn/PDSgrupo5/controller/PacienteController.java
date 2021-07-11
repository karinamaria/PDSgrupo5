package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("paciente")
public class PacienteController {
    private PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService){
        this.pacienteService = pacienteService;
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
    public String salvar(@Valid Paciente paciente, BindingResult br, Model model){

        paciente = pacienteService.verificarEdicao(paciente);

        br = pacienteService.validarPaciente(paciente, br);

        if(br.hasErrors()){
            model.addAttribute("message", "Erro ao salvar paciente");
            model.addAttribute(paciente);
            return form(model);
        }
        pacienteService.salvarPaciente(paciente);

        return "redirect:/dashboard";
    }

    //o usuário edita seu próprio cadastro
    @GetMapping("/editar")
    public String editar(Model model){
        model.addAttribute(pacienteService.buscarPacientePorUsuarioLogado());
        return "paciente/form";
    }

    @GetMapping("/perfil")
    public String visualizarPerfil(Model model){
        model.addAttribute(pacienteService.buscarPacientePorUsuarioLogado());
        return "paginadevisualizacaoPerfil";
    }

    @DeleteMapping("/excluirPerfil")
    public String excluirPerfil(){
        Paciente paciente = pacienteService.buscarPacientePorUsuarioLogado();

        pacienteService.excluirPaciente(paciente);

        return "redirect:/login";
    }

}
