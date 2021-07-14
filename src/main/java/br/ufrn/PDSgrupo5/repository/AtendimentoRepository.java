package br.ufrn.PDSgrupo5.repository;

import br.ufrn.PDSgrupo5.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    @Query(value="select a from Atendimento a where a.paciente.id=?1 and " +
            "a.horarioAtendimento.horarioInicio > CURRENT_DATE and a.horarioAtendimento.horarioInicio < ?2")
    List<Atendimento> buscarProximosAtendimentosPaciente(Long idPaciente, Date dataLimite);

    @Query(value="select a from Atendimento a where a.profissionalSaude.id=?1 and " +
            "a.horarioAtendimento.horarioInicio > CURRENT_DATE and a.horarioAtendimento.horarioInicio < ?2")
    List<Atendimento> buscarProximosAtendimentosProfissional(Long idProfissional, Date dataLimite);
}
