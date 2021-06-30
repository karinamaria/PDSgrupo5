package br.ufrn.PDSgrupo5.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.ufrn.PDSgrupo5.model.Pessoa;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.repository.ProfissionalSaudeRepository;

@Service
public class ProfissionalSaudeService {
	private ProfissionalSaudeRepository profissionalSaudeRepository;
	
	private PessoaService pessoaService;
	
	private UsuarioService usuarioService;
	
	@Autowired
	public ProfissionalSaudeService(ProfissionalSaudeRepository profissionalSaudeRepository,
									PessoaService pessoaService, UsuarioService usuarioService) {
		this.profissionalSaudeRepository = profissionalSaudeRepository;
		this.pessoaService = pessoaService;
		this.usuarioService = usuarioService;
	}
	
	public ProfissionalSaude salvar(ProfissionalSaude ps) {
		return profissionalSaudeRepository.save(ps);
	}
	
	public void cadastrarProfissional(ProfissionalSaude ps) {
		ps.setAtivo(true);
		ps.getPessoa().setUsuario(usuarioService.prepararUsuarioParaCriacao(ps.getPessoa().getUsuario()));
		salvar(ps);
	}
	
	public BindingResult validarProfissionalSaude(ProfissionalSaude ps, BindingResult br) {
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
		
		return br;
	}
	
	public List<ProfissionalSaude> listarTodosProfissionais(){
		return profissionalSaudeRepository.findAll();
	}
	
	public void excluir(ProfissionalSaude ps) {
		profissionalSaudeRepository.delete(ps);
	}
	
	public ProfissionalSaude buscarProfissionalPorNumeroRegistro(ProfissionalSaude ps) {
		return profissionalSaudeRepository.findByNumeroRegistro(ps.getNumeroRegistro());
	}
}
