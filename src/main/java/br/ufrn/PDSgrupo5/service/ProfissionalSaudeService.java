package br.ufrn.PDSgrupo5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.repository.ProfissionalSaudeRepository;

@Service
@Transactional (readOnly = true)
public class ProfissionalSaudeService {
	
	private ProfissionalSaudeRepository profissionalSaudeRepository;
	
	@Autowired
	public ProfissionalSaudeService(ProfissionalSaudeRepository profissionalSaudeRepository) {
		this.profissionalSaudeRepository = profissionalSaudeRepository;
	}
	
	public List<ProfissionalSaude> findAll(){
		return profissionalSaudeRepository.findAll();
	}
	
	@Transactional (readOnly = false)
	public ProfissionalSaude save(ProfissionalSaude entity) throws Exception {
		ProfissionalSaude aux = profissionalSaudeRepository.findByNumeroRegistro(entity.getNumeroRegistro());
		if(aux != null) {
			throw new Exception("Já existe cadastro com esse número de registro profissional");
		}
		
		return profissionalSaudeRepository.save(entity);
	}
	
	@Transactional (readOnly = false)
	public void delete(ProfissionalSaude entity) {
		profissionalSaudeRepository.delete(entity);
	}
}
