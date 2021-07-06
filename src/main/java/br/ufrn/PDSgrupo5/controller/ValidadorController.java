package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.PessoaService;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;
import br.ufrn.PDSgrupo5.service.ValidadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("validador")
public class ValidadorController {
    private PessoaService pessoaService;

    private ProfissionalSaudeService profissionalSaudeService;

    private ValidadorService validadorService;

    @Autowired
    public ValidadorController(PessoaService pessoaService, ProfissionalSaudeService ps,
                               ValidadorService validadorService){
        this.pessoaService = pessoaService;
        this.profissionalSaudeService = ps;
        this.validadorService = validadorService;
    }

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute(pessoaService.buscarPessoaPorUsuarioLogado());
        model.addAttribute("profissionais", profissionalSaudeService.listarProfissionaisStatusLegalizacao(false));
        return "";
    }

    @GetMapping("/visualizarProfissional/{id}")
    public String visualizarProfissional(@PathVariable Long id, Model model){
        model.addAttribute("profissional",profissionalSaudeService.buscarProfissioalPorId(id));

        return "";
    }

    @PostMapping("/salvarProfissional")
    public String editarProfissionalSaude(@Valid ProfissionalSaude profissionalSaude, BindingResult br,
                                          Model model){
        if(br.hasErrors()){
            model.addAttribute("message", "Não foi possível salvar as alterações.");
            return "";
        }
        validadorService.salvarEdicaoProfissionalSaude(profissionalSaude);
        return "";
    }
}
