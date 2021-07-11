package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("profissional-saude")
public class ProfissionalSaudeController {
    private ProfissionalSaudeService profissionalSaudeService;

    @Autowired
    ProfissionalSaudeController(ProfissionalSaudeService profissionalSaudeService) {
        this.profissionalSaudeService = profissionalSaudeService;
    }

    @GetMapping("/form")
    public String form(Model model) {
        if (!model.containsAttribute("profissionalSaude")) {
            model.addAttribute(new ProfissionalSaude());
        }

        return "profissional-saude/form";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid ProfissionalSaude profissionalSaude, BindingResult br, RedirectAttributes ra, Model model) {

        profissionalSaude = profissionalSaudeService.verificarEdicao(profissionalSaude);
        br = profissionalSaudeService.validarDados(profissionalSaude, br);

        if (br.hasErrors()) {
            model.addAttribute("message", "Erro ao cadastrar profissional da saúde");
            model.addAttribute(profissionalSaude);
            return form(model);
        }

        profissionalSaudeService.salvar(profissionalSaude);


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
    public String editarOutroPaciente(@PathVariable Long id, Model model) {
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

    @DeleteMapping("/excluir-perfil")
    public String excluirPerfil() {
        ProfissionalSaude ps = profissionalSaudeService.buscarProfissionalPorUsuarioLogado();
        profissionalSaudeService.excluir(ps);

        return "redirect:/login";
    }
}
