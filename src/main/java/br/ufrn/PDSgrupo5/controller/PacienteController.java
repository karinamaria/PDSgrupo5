package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.exception.NegocioException;
import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String salvar(@Valid Paciente paciente, BindingResult br, RedirectAttributes ra, Model model){
        try{
            pacienteService.verificarPermissao(paciente);
            br = pacienteService.validarPaciente(paciente, br);

            if(br.hasErrors()){
                model.addAttribute("message", "Erro ao salvar paciente");
                model.addAttribute(paciente);
                return form(model);
            }
            paciente = pacienteService.verificarEdicao(paciente);
            pacienteService.salvarPaciente(paciente);

        }catch(NegocioException ne){
            return "/error/403.html";
        }
        //se for edição, deve retornar para página diferente
        return "redirect:/login";
    }

    //o usuário edita seu próprio cadastro
    @GetMapping("/editar")
    public String editar(Model model){
        model.addAttribute(pacienteService.buscarPacientePorUsuarioLogado());
        return form(model);
    }

    //usuário com papel "validador" pode editar qualquer paciente
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
