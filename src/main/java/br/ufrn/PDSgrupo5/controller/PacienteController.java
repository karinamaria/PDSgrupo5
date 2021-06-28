package br.ufrn.PDSgrupo5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("paciente")
public class PacienteController {

    @GetMapping
    public String salvar(){
        return "";
    }
}
