package br.ufrn.PDSgrupo5.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "turno_atendimento")
public class TurnoAtendimento extends EntidadeAbstrata{

	@Column(name="dia_semana")
	private String diaSemana;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "horario_inicio", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date horarioInicio;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "horario_fim", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date horarioFim;

	public TurnoAtendimento() {
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Date getHorarioInicio() {
		return horarioInicio;
	}

	public void setHorarioInicio(Date horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public Date getHorarioFim() {
		return horarioFim;
	}

	public void setHorarioFim(Date horarioFim) {
		this.horarioFim = horarioFim;
	}
}
