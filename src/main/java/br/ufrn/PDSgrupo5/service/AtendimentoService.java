package br.ufrn.PDSgrupo5.service;

import br.ufrn.PDSgrupo5.model.Atendimento;
import br.ufrn.PDSgrupo5.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.repository.AtendimentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {
    private PacienteService pacienteService;

    private ProfissionalSaudeService profissionalSaudeService;

    private AtendimentoRepository atendimentoRepository;

    private HorarioAtendimentoService horarioAtendimentoService;

    public AtendimentoService(PacienteService pacienteService, ProfissionalSaudeService psService,
                              AtendimentoRepository atendimentoRepository){
        this.pacienteService = pacienteService;
        this.profissionalSaudeService = psService;
        this.atendimentoRepository = atendimentoRepository;
    }
    
    public Atendimento salvar(Atendimento a) {
    	return atendimentoRepository.save(a);
    }

    public List<Atendimento> buscarProximosAtendimentosPaciente(){
        Long idPacienteLogado = pacienteService.buscarPacientePorUsuarioLogado().getId();

        return atendimentoRepository.buscarProximosAtendimentosPaciente(idPacienteLogado, somarQuinzeDiasADataAtual());
    }

    public List<Atendimento> buscarProximosAtendimentosProfissional(){
        Long idProfissionalLogado = profissionalSaudeService.buscarProfissionalPorUsuarioLogado().getId();

        return atendimentoRepository.buscarProximosAtendimentosProfissional(idProfissionalLogado, somarQuinzeDiasADataAtual());
    }

    public List<Atendimento> buscarAtendimentosAguardandoConfirmacao(){
        ProfissionalSaude profissionalSaude = profissionalSaudeService.buscarProfissionalPorUsuarioLogado();

        return atendimentoRepository.buscarAtendimentosAguardandoConfirmacao(profissionalSaude);
    }

    public Date somarQuinzeDiasADataAtual(){
        LocalDate dataMaisQuinzeDias = LocalDate.now().plusDays(14);

        return Date.from(dataMaisQuinzeDias.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void aceitarRecusarAtendimento(Long id, boolean autorizacao) {
        Atendimento atendimento = atendimentoRepository.getById(id);

        if(autorizacao){//atendimento autorizado
            atendimento.setConfirmado(true);
            atendimentoRepository.save(atendimento);
        }else{
            HorarioAtendimento horarioAtendimento = atendimento.getHorarioAtendimento();
            horarioAtendimento.setLivre(true);
            horarioAtendimentoService.salvarHorario(horarioAtendimento);//salvar horário de atendimento
            atendimentoRepository.delete(atendimento);
        }
    }
    
    public List<Atendimento> buscarAtendimentosRequeremLembreteRetorno(){
        List<Atendimento> atendimentos = atendimentoRepository.buscarAtendimentosRequeremLembreteRetorno();

        //retornar apenas os atendimentos de pacientes que ainda não marcaram o retorno
        return   atendimentos.stream()
                .filter(a -> pacienteNaoMarcouRetorno(a))
                .collect(Collectors.toList());
    	//return atendimentoRepository.buscarAtendimentosRequeremLembreteRetorno();
    }

    /**
     * Verifica se o paciente não marcou retorno ao profissional da saúde
     * @param a atendimento que foi realizado há 60 dias ou mais.
     * @return true: se o paciente não marcou retorno; false: caso contrário.
     */
    public Boolean pacienteNaoMarcouRetorno(Atendimento a){
        List<Atendimento> todosProximosAtendimentos = atendimentoRepository.buscarTodosProximosAtendimentosPaciente(a.getPaciente().getId());

        return todosProximosAtendimentos == null? true : todosProximosAtendimentos.stream()
                .filter(atendimento -> atendimento.getProfissionalSaude().equals(a.getProfissionalSaude()))
                .count() == 0;

        //return !todosProximosAtendimentos.contains(a.getProfissionalSaude());
    }
}
