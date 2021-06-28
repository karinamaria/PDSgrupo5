package br.ufrn.PDSgrupo5.repository;

import br.ufrn.PDSgrupo5.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
