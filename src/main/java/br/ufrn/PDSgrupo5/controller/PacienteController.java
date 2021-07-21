package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoRegistro;
import br.ufrn.PDSgrupo5.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.model.Atendimento;
import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.service.AtendimentoService;
import br.ufrn.PDSgrupo5.service.HorarioAtendimentoService;
import br.ufrn.PDSgrupo5.service.PacienteService;
import br.ufrn.PDSgrupo5.service.ProfissionalSaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("paciente")
public class PacienteController {
    private PacienteService pacienteService;
    private ProfissionalSaudeService profissionalSaudeService;
    private AtendimentoService atendimentoService;
    private HorarioAtendimentoService horarioAtendimentoService;

    @Autowired
    public PacienteController(PacienteService pacienteService, ProfissionalSaudeService profissionalSaudeService,
    						  AtendimentoService atendimentoService, HorarioAtendimentoService horarioAtendimentoService){
        this.pacienteService = pacienteService;
        this.profissionalSaudeService = profissionalSaudeService;
        this.atendimentoService = atendimentoService;
        this.horarioAtendimentoService = horarioAtendimentoService;
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
            paciente = pacienteService.verificarEdicao(paciente);
            pacienteService.validarPaciente(paciente, br);

            pacienteService.salvarPaciente(paciente);
        }catch(ValidacaoException validacaoException){
            model.addAttribute("message", "Erro ao salvar paciente");
            model.addAttribute(paciente);
            return form(model);
        }

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
    
    @GetMapping("/visualizarProfissional/{id}")
    public String visualizarProfissional(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("profissional", profissionalSaudeService.buscarProfissionalPorId(id));
    	model.addAttribute("horariosAtendimento", profissionalSaudeService.buscarHorariosAtendimentoLivres(id));
    	model.addAttribute("atendimento", new Atendimento());
    	
    	return "paciente/formAtendimento";
    }
    
    @PostMapping("/agendarAtendimento")
    public String agendarAtendimento(@RequestParam("horarioAtendimentoId") Long idHorario, @RequestParam("profissionalId") Long idProfissional,
    							     @Valid Atendimento atendimento) {
    	atendimento.setPaciente(pacienteService.buscarPacientePorUsuarioLogado());
    	atendimento.setProfissionalSaude(profissionalSaudeService.buscarProfissionalPorId(idProfissional));
    	
    	horarioAtendimentoService.buscarHorarioPorId(idHorario).setLivre(false);
    	atendimento.setHorarioatendimento(horarioAtendimentoService.buscarHorarioPorId(idHorario));
    	
    	atendimentoService.salvar(atendimento);
    	
    	return "redirect:/dashboard";
    }
}
