package br.ufrn.PDSgrupo5.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.exception.NegocioException;
import br.ufrn.PDSgrupo5.handler.UsuarioHelper;
import br.ufrn.PDSgrupo5.model.Pessoa;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.model.Usuario;
import br.ufrn.PDSgrupo5.repository.ProfissionalSaudeRepository;

@Service
public class ProfissionalSaudeService {
	private ProfissionalSaudeRepository profissionalSaudeRepository;
	
	private PessoaService pessoaService;
	
	private UsuarioService usuarioService;
	
	private UsuarioHelper usuarioHelper;
	
	@Autowired
	public ProfissionalSaudeService(ProfissionalSaudeRepository profissionalSaudeRepository,
									PessoaService pessoaService, UsuarioService usuarioService,
									UsuarioHelper usuarioHelper) {
		this.profissionalSaudeRepository = profissionalSaudeRepository;
		this.pessoaService = pessoaService;
		this.usuarioService = usuarioService;
		this.usuarioHelper = usuarioHelper;
	}
	
	public ProfissionalSaude salvar(ProfissionalSaude ps) {
		return profissionalSaudeRepository.save(ps);
	}
	
	public void cadastrar(ProfissionalSaude ps) {
		ps.setAtivo(true);
		ps.getPessoa().setUsuario(usuarioService.prepararUsuarioParaCriacao(ps.getPessoa().getUsuario()));
		ps.getPessoa().getUsuario().setEnumTipoPapel(EnumTipoPapel.PROFISSIONAL_SAUDE);
		salvar(ps);
	}
	
	public BindingResult validarDados(ProfissionalSaude ps, BindingResult br) {
		if(!pessoaService.ehCpfValido(ps.getPessoa().getCpf())) {
			br.rejectValue("pessoa.cpf", "", "CPF inválido");
		}
		Pessoa pessoa = pessoaService.buscarPessoaPorCpf(ps.getPessoa().getCpf());
		if(Objects.nonNull(pessoa)) {
			if(pessoa.getId() != ps.getPessoa().getId()) {
				br.rejectValue("pessoa.cpf", "", "CPF já pertence a outro usuário");
			}
		}
		
		if(!ps.getPessoa().getEmail().matches("^(.+)@(.+)$")){
            br.rejectValue("pessoa.email", "","E-mail inválido");
        }
		
        if(usuarioService.loginJaExiste(ps.getPessoa().getUsuario())){
            br.rejectValue("pessoa.usuario.login", "","Login já existe");
        }
        
        pessoa = pessoaService.buscarPessoaPorEmail(ps.getPessoa().getEmail());
        if(Objects.nonNull(pessoa)){
            if(pessoa.getId() != ps.getPessoa().getId()){
                br.rejectValue("pessoa.email", "","E-mail já pertence a outro usuário");
            }
        }
        
        ProfissionalSaude profissional = buscarProfissionalPorNumeroRegistro(ps.getNumeroRegistro());
		if(Objects.nonNull(profissional)) {
			if(profissional.getId() != ps.getId()) {
				br.rejectValue("numeroRegistro", "", "Registro profissional já pertence a outro usuário");
			}
		}
		
		return br;
	}
	
	public ProfissionalSaude verificarEdicao(ProfissionalSaude ps) {
        ProfissionalSaude psAux = buscarProfissionalPorUsuarioLogado();
		//nenhum atributo do usuário será modificado na edição
		ps.getPessoa().setUsuario(psAux.getPessoa().getUsuario());

		ps.setId(psAux.getId());
		ps.getPessoa().setId(psAux.getPessoa().getId());

        if(Objects.isNull(ps.getPessoa().getEndereco())){
            ps.getPessoa().setEndereco(null);
        }else{
        	ps.getPessoa().getEndereco().setId(psAux.getPessoa().getEndereco().getId());
		}

        return ps;
	}

	public List<ProfissionalSaude> listarTodosProfissionais(){
		return profissionalSaudeRepository.findAll();
	}
	
	public void excluir(ProfissionalSaude ps) {
		profissionalSaudeRepository.delete(ps);
	}
	
	public ProfissionalSaude buscarProfissionalPorNumeroRegistro(Long numeroRegistro) {
		return profissionalSaudeRepository.findByNumeroRegistro(numeroRegistro);
	}
	
	public ProfissionalSaude buscarProfissionalPorUsuarioLogado() {
		return profissionalSaudeRepository.findByUsuario(usuarioHelper.getUsuarioLogado());
	}
	
	public ProfissionalSaude buscarProfissionalPorUsuario(Long id){
        Usuario usuario = usuarioService.buscarUsuarioPeloId(id);
        return profissionalSaudeRepository.findByUsuario(usuario);
    }

    public List<ProfissionalSaude> listarProfissionaisStatusLegalizacao(boolean legalizado){
		return profissionalSaudeRepository.findAllByLegalizado(legalizado);
	}

	public ProfissionalSaude buscarProfissioalPorId(Long id){
		return profissionalSaudeRepository.findById(id).orElse(null);
	}

	public void verificarPermissao(ProfissionalSaude ps) throws NegocioException{
		if(ps.getId() == null){ //eh usuário novo
			return;
		}

		ProfissionalSaude psLogado = buscarProfissionalPorUsuarioLogado();

		if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel() == EnumTipoPapel.VALIDADOR
				|| ps.getId() == null){
			return;
		}

		if( ps.getId() != psLogado.getId() || ps.getPessoa().getId() != psLogado.getPessoa().getId()
				|| ps.getPessoa().getUsuario().getId() != psLogado.getPessoa().getUsuario().getId()){
			throw new NegocioException("Você não tem permissão para editar esse usuário");
		}
	}
}
