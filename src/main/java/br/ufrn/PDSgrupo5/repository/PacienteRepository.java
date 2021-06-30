package br.ufrn.PDSgrupo5.repository;

import br.ufrn.PDSgrupo5.model.Paciente;
import br.ufrn.PDSgrupo5.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Query(value="SELECT p FROM Paciente p WHERE p.pessoa.usuario=?1")
    Paciente findPacienteByUsuario(Usuario usuario);
}
