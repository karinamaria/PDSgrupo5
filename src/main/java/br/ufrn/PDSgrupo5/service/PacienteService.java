package br.ufrn.PDSgrupo5.service;

import br.ufrn.PDSgrupo5.handler.UsuarioHelper;
import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.model.Pessoa;
import br.ufrn.PDSgrupo5.model.Usuario;
import br.ufrn.PDSgrupo5.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Service
public class PacienteService {
    private PacienteRepository pacienteRepository;

    private PessoaService pessoaService;

    private UsuarioService usuarioService;

    private UsuarioHelper usuarioHelper;

    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, PessoaService pessoaService,
                           UsuarioService usuarioService, UsuarioHelper usuarioHelper){
        this.pacienteRepository = pacienteRepository;
        this.pessoaService = pessoaService;
        this.usuarioService = usuarioService;
        this.usuarioHelper = usuarioHelper;
    }
    public Paciente salvar(Paciente paciente){
        return pacienteRepository.save(paciente);
    }

    public void salvarNovoPaciente(Paciente paciente){
        paciente.setAtivo(true);
        paciente.getPessoa().setUsuario(usuarioService.prepararUsuarioParaCriacao(paciente.getPessoa().getUsuario()));
        salvar(paciente);
    }

    public BindingResult validarPaciente(Paciente paciente, BindingResult br){
        if(!pessoaService.ehCpfValido(paciente.getPessoa().getCpf())){
            br.rejectValue("pessoa.cpf", "", "CPF inválido");
        }
        Pessoa pessoa = pessoaService.buscarPessoaPorCpf(paciente.getPessoa().getCpf());
        if(Objects.nonNull(pessoa)){
            if(pessoa.getId() != paciente.getPessoa().getId()){
                br.rejectValue("pessoa.cpf", "","CPF já pertence a outra pessoa");
            }
        }
        if(!paciente.getPessoa().getEmail().matches("^(.+)@(.+)$")){
            br.rejectValue("pessoa.email", "","E-mail inválido");
        }
        if(usuarioService.loginJaExiste(paciente.getPessoa().getUsuario())){
            br.rejectValue("pessoa.usuario.login", "","Login já existe");
        }
        pessoa = pessoaService.buscarPessoaPorEmail(paciente.getPessoa().getEmail());
        if(Objects.nonNull(pessoa)){
            if(pessoa.getId() != paciente.getPessoa().getId()){
                br.rejectValue("pessoa.email", "","Email já pertence a outra pessoa");
            }
        }
        return br;
    }

    public Paciente buscarPacientePorUsuarioLogado(){
        return pacienteRepository.findPacienteByUsuario(usuarioHelper.getUsuarioLogado());
    }
    public Paciente buscarPacientePorUsuario(Long id){
        Usuario usuario = usuarioService.buscarUsuarioPeloId(id);
        return pacienteRepository.findPacienteByUsuario(usuario);
    }
}
