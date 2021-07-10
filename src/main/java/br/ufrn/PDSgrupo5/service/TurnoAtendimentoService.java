package br.ufrn.PDSgrupo5.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.model.TurnoAtendimento;

@Service
public class TurnoAtendimentoService {
	private ProfissionalSaudeService profissionalSaudeService;
	private DataHoraService dataHoraService;
	
	@Autowired
	public TurnoAtendimentoService(ProfissionalSaudeService profissionalSaudeService, DataHoraService dataHoraService) {
		this.profissionalSaudeService = profissionalSaudeService;
		this.dataHoraService = dataHoraService;
	}
	
	public ProfissionalSaude salvar(TurnoAtendimento ta) {
		return profissionalSaudeService.adicionarTurnoAtendimento(ta);
	}
	
	public String validarTurno(TurnoAtendimento ta) {
		if(ta.getHorarioInicio().compareTo(ta.getHorarioFim()) >= 0) {
			System.out.println("Dif: " + ta.getHorarioInicio().compareTo(ta.getHorarioFim()));
			return "Turno inválido. O horário de início deve ser anterior ao de fim e eles não podem ser iguais.";
		}
		
		List<TurnoAtendimento> turnos = profissionalSaudeService.buscarTurnosAtendimento();
		for(TurnoAtendimento turno : turnos ) {
			if( turnosTemChoqueHorario(ta, turno) ) {
				return "Choque de horário entre turnos. Verifique seus turnos já cadastrados.";
			}
		}
		
		return null;
	}
	
	public TurnoAtendimento converterParaTurnoAtendimento(Date horarioInicio, Date horarioFim) {	
		TurnoAtendimento ta = new TurnoAtendimento();
    	ta.setHorarioInicio(horarioInicio);
    	ta.setHorarioFim(horarioFim);
    	ta.setDiaSemana(dataHoraService.getDiaSemana(horarioInicio));
    	
		return ta;
	}
	
	public boolean turnosTemChoqueHorario(TurnoAtendimento turnoA, TurnoAtendimento turnoB) {
		boolean temChoque = true;
		
		//já foi testado fora do método que início do horário é anterior ao fim do horário
		if( turnoA.getHorarioFim().before(turnoB.getHorarioInicio()) || turnoA.getHorarioInicio().after(turnoB.getHorarioFim()) ) {
			temChoque = false;
		}
		
		return temChoque;
	}
}
