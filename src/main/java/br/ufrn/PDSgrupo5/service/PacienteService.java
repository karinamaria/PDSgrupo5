package br.ufrn.PDSgrupo5.service;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.exception.AcessoNegadoException;
import br.ufrn.PDSgrupo5.exception.ValidacaoException;
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

    /**
     * Valida os dados do paciente
     * @param paciente objeto que será validado
     * @param br onde os erros relativos a entidade `Paciente`são acumulados
     * @return um BindingResult que contém os erros, caso existam
     */
    public void validarPaciente(Paciente paciente, BindingResult br) throws ValidacaoException{
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
        if(paciente.getPessoa().getUsuario().getLogin().length() < 5){
            br.rejectValue("pessoa.usuario.login", "", "O login deve ter pelo menos cinco caracteres");
        }
        pessoa = pessoaService.buscarPessoaPorEmail(paciente.getPessoa().getEmail());
        if(Objects.nonNull(pessoa)){
            if(pessoa.getId() != paciente.getPessoa().getId()){
                br.rejectValue("pessoa.email", "","Email já pertence a outra pessoa");
            }
        }

        if(br.hasErrors()){
            throw new ValidacaoException(br);
        }
    }

    public Paciente buscarPacientePorUsuarioLogado(){
        return pacienteRepository.findPacienteByUsuario(usuarioHelper.getUsuarioLogado());
    }

    public void excluirPaciente(Paciente paciente){
        pacienteRepository.delete(paciente);
    }

    public Paciente buscarPacientePorUsuario(Long id){
        Usuario usuario = usuarioService.buscarUsuarioPeloId(id);
        return pacienteRepository.findPacienteByUsuario(usuario);
    }

    /**
     * Se for uma edição do paciente, o método carrega o tipo do papel e a senha
     * @param paciente que está sendo editado
     * @return paciente
     */
    public Paciente verificarEdicao(Paciente paciente) {
        Paciente paciente1 = buscarPacientePorUsuarioLogado();
        //nenhum atributo do usuário será modificado na edição
        paciente.getPessoa().setUsuario(paciente1.getPessoa().getUsuario());

        paciente.setId(paciente1.getId());
        paciente.getPessoa().setId(paciente1.getPessoa().getId());

        if(Objects.isNull(paciente1.getPessoa().getEndereco())){
            paciente.getPessoa().setEndereco(null);
        }else{
            paciente.getPessoa().getEndereco().setId(paciente1.getPessoa().getEndereco().getId());
        }

        return paciente;
    }

    /**
     * Verifica se o usuário está editando o próprio cadastro
     * @param paciente Paciente que será editado
     * @throws AcessoNegadoException exceção lançada caso a edição não for permitida
     */
    public void verificarPermissao(Paciente paciente) throws AcessoNegadoException {
        if(paciente.getId() == null){ //eh usuário novo
            return;
        }

        Paciente pacienteLogado = buscarPacientePorUsuarioLogado();

        if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel() == EnumTipoPapel.VALIDADOR
            || paciente.getId() == null){
            return;
        }

        if( paciente.getId() != pacienteLogado.getId() || paciente.getPessoa().getId() != paciente.getPessoa().getId()
            || paciente.getPessoa().getUsuario().getId() != paciente.getPessoa().getUsuario().getId()){
            throw new AcessoNegadoException("Você não tem permissão para editar esse usuário");
        }
    }
}
