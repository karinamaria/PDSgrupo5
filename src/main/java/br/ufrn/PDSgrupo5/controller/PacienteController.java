package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoRegistro;
import br.ufrn.PDSgrupo5.exception.NegocioException;
import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.PacienteService;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.validation.Valid;

@Controller
@RequestMapping("paciente")
public class PacienteController {
    private PacienteService pacienteService;
    private ProfissionalSaudeService profissionalSaudeService;

    @Autowired
    public PacienteController(PacienteService pacienteService, ProfissionalSaudeService profissionalSaudeService){
        this.pacienteService = pacienteService;
        this.profissionalSaudeService = profissionalSaudeService;
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
            return "";
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
    
    @GetMapping("/buscarProfissional")
    public ModelAndView buscarProfissional() {
    	ModelAndView mv = new ModelAndView("paciente/buscarProfissional");
    	List<ProfissionalSaude> profissionais = profissionalSaudeService.listarProfissionaisStatusLegalizacao(true);
    	mv.addObject("listaProfissionais", profissionais);
    	return mv;
    }
    
    @PostMapping("/buscar")
    public ModelAndView buscar(@RequestParam("nomeProfissional") String nome, @RequestParam("categoriaProfissional") String categoria) {
    	ModelAndView mv = new ModelAndView("paciente/buscarProfissional");
    	if(nome.equals("")) {
    		nome = null;
    	}
    	
    	List<ProfissionalSaude> profissionais;
    	if(categoria.equals("Todos")){
    		profissionais = profissionalSaudeService.buscarPorFiltro(true, nome, null);
    	} else {
    		profissionais = profissionalSaudeService.buscarPorFiltro(true, nome, EnumTipoRegistro.valueOf(categoria));
    	}
    	
    	mv.addObject("listaProfissionais", profissionais);
    	return mv;
    }
}
