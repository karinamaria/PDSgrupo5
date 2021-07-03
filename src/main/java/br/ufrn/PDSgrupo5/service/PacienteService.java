package br.ufrn.PDSgrupo5.service;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.exception.NegocioException;
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

    public void salvarPaciente(Paciente paciente){
        if(paciente.getId() == null){
            paciente.setAtivo(true);
            paciente.getPessoa().setUsuario(usuarioService.prepararUsuarioParaCriacao(paciente.getPessoa().getUsuario()));
        }
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
        Paciente p = pacienteRepository.findPacienteByUsuario(usuarioHelper.getUsuarioLogado());
        return pacienteRepository.findPacienteByUsuario(usuarioHelper.getUsuarioLogado());
    }
    public Paciente buscarPacientePorUsuario(Long id){
        Usuario usuario = usuarioService.buscarUsuarioPeloId(id);
        return pacienteRepository.findPacienteByUsuario(usuario);
    }

    public Paciente verificarEdicao(Paciente paciente) {
        if(paciente.getId() == null){//não eh edição
            return paciente;
        }
        Paciente paciente1 = pacienteRepository.findById(paciente.getId()).get();
        paciente.getPessoa().getUsuario().setEnumTipoPapel(paciente1.getPessoa().getUsuario().getEnumTipoPapel());
        paciente.getPessoa().getUsuario().setSenha(paciente1.getPessoa().getUsuario().getSenha());

        if(Objects.isNull(paciente.getPessoa().getEndereco())){
            paciente.getPessoa().setEndereco(null);
        }

        return paciente1;
    }

    public void verificarPermissao(Paciente paciente) throws NegocioException{
        Paciente pacienteLogado = buscarPacientePorUsuarioLogado();

        if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel() == EnumTipoPapel.VALIDADOR
            || paciente.getId() == null){
            return;
        }

        if( paciente.getId() != pacienteLogado.getId() || paciente.getPessoa().getId() != paciente.getPessoa().getId()
            || paciente.getPessoa().getUsuario().getId() != paciente.getPessoa().getUsuario().getId()){
            throw new NegocioException("Você não tem permissão para editar esse usuário");
        }
    }
}
