package br.ufrn.PDSgrupo5.repository;

import br.ufrn.PDSgrupo5.model.Atendimento;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
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
            "a.horarioAtendimento.horarioInicio > CURRENT_DATE and a.horarioAtendimento.horarioInicio < ?2 and a.confirmado=true")
    List<Atendimento> buscarProximosAtendimentosProfissional(Long idProfissional, Date dataLimite);

    @Query(value="select a from Atendimento a where a.confirmado=false and a.profissionalSaude=?1")
    List<Atendimento> buscarAtendimentosAguardandoConfirmacao(ProfissionalSaude profissionalSaude);
    
    @Query(value="select a from Atendimento a where a.requerLembreteRetorno=true and a.horarioAtendimento.horarioInicio <= (CURRENT_DATE - 60)")
    List<Atendimento> buscarAtendimentosRequeremLembreteRetorno();
}
