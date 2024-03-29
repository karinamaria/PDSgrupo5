package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.enumeration.EnumSituacaoProfissionalSaude;
import br.ufrn.PDSgrupo5.service.PessoaService;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;
import br.ufrn.PDSgrupo5.service.ValidadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("profissional",profissionalSaudeService.buscarProfissionalPorId(id));

        return "";
    }

    @PostMapping("/salvarProfissional")
    public String editarProfissionalSaude(@RequestParam("profissionalId") Long id,
                                          @RequestParam("autorizacao") boolean autorizacao, @RequestParam("justificativa") EnumSituacaoProfissionalSaude justificativa){

        validadorService.salvarEdicaoProfissionalSaude(id, autorizacao, justificativa);

        return "redirect:/dashboard";
    }
}
