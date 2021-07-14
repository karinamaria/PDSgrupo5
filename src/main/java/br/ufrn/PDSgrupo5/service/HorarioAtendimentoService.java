package br.ufrn.PDSgrupo5.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.ufrn.PDSgrupo5.repository.HorarioAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.model.HorarioAtendimento;

@Service
public class HorarioAtendimentoService {
	private ProfissionalSaudeService profissionalSaudeService;
	private DataHoraService dataHoraService;
	private HorarioAtendimentoRepository horarioAtendimentoRepository;
	
	@Autowired
	public HorarioAtendimentoService(ProfissionalSaudeService profissionalSaudeService, DataHoraService dataHoraService) {
		this.profissionalSaudeService = profissionalSaudeService;
		this.dataHoraService = dataHoraService;
	}
	
	public ProfissionalSaude salvar(HorarioAtendimento ha) {
		return profissionalSaudeService.adicionarHorarioAtendimento(ha);
	}
	
	public String validarHorario(HorarioAtendimento ha) {
		if(ha.getHorarioInicio().compareTo(ha.getHorarioFim()) >= 0) {
			return "Horário inválido. A hora de início deve ser anterior a de fim e elas não podem ser iguais.";
		}
		
		List<HorarioAtendimento> horarios = profissionalSaudeService.buscarHorariosAtendimento();
		for(HorarioAtendimento horario : horarios ) {
			if( horariosTemChoque(ha, horario) ) {
				return "Choque entre horários. Verifique seus horários já cadastrados.";
			}
		}
		
		return null;
	}
	
	public HorarioAtendimento converterParaHorarioAtendimento(Date horarioInicio, Date horarioFim) {	
		HorarioAtendimento ha = new HorarioAtendimento();
    	ha.setHorarioInicio(horarioInicio);
    	ha.setHorarioFim(horarioFim);
    	ha.setDiaSemana(dataHoraService.getDiaSemana(horarioInicio));
    	
		return ha;
	}
	
	public boolean horariosTemChoque(HorarioAtendimento horarioA, HorarioAtendimento horarioB) {
		boolean temChoque = true;
		
		//já foi testado fora do método que início do horário é anterior ao fim do horário
		if( horarioA.getHorarioFim().before(horarioB.getHorarioInicio()) || horarioA.getHorarioInicio().after(horarioB.getHorarioFim()) ) {
			temChoque = false;
		}
		
		return temChoque;
	}

	public void excluirHorario(Long idHorario){
		HorarioAtendimento hr = horarioAtendimentoRepository.findById(idHorario).get();
		if(Objects.nonNull(hr)){
			horarioAtendimentoRepository.delete(hr);
		}
	}
}
