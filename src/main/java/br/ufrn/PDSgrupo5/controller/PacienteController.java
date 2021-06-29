package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
        model.addAttribute(new Paciente());
        return "paciente/form";
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid @RequestBody Paciente paciente, BindingResult br, RedirectAttributes ra){
        ModelAndView modelAndView = new ModelAndView(new RedirectView("paciente/form", true));

        br = pacienteService.validarPaciente(paciente, br);

        if(br.hasErrors()){
            ra.addFlashAttribute("org.springframework.validation.BindingResult.Paciente", br);
            ra.addFlashAttribute(paciente);
        }

        return modelAndView;
    }
}
