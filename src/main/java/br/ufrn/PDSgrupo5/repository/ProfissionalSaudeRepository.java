package br.ufrn.PDSgrupo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;

public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {
	ProfissionalSaude findByNumeroRegistro(Long numeroRegistro);
}
