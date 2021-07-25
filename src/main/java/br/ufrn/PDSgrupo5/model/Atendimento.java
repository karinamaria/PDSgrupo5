package br.ufrn.PDSgrupo5.model;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoAtendimento;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "atendimento")
public class Atendimento extends EntidadeAbstrata {
	
	@OneToOne(cascade = CascadeType.ALL)
	private HorarioAtendimento horarioAtendimento;

	private String titulo;

	private String descricao;

	@Column(columnDefinition = "boolean default false")
	private Boolean confirmado = false;

	@OneToOne
	private Paciente paciente;

	@OneToOne(cascade = CascadeType.ALL)
	private ProfissionalSaude profissionalSaude;

	@Column(name="tipo_atendimento")
	@Enumerated(EnumType.STRING)
	private EnumTipoAtendimento enumTipoAtendimento;
	
	@Column(name="requer_lembrete_retorno")
	private Boolean requerLembreteRetorno = true;

	public Atendimento() {
	}

	public HorarioAtendimento getHorarioAtendimento() {
		return horarioAtendimento;
	}

	public void setHorarioatendimento(HorarioAtendimento horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getConfirmado() {
		return confirmado;
	}

	public void setConfirmado(Boolean status) {
		this.confirmado = status;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public ProfissionalSaude getProfissionalSaude() {
		return profissionalSaude;
	}

	public void setProfissionalSaude(ProfissionalSaude profissionalSaude) {
		this.profissionalSaude = profissionalSaude;
	}

	public EnumTipoAtendimento getEnumTipoAtendimento() {
		return enumTipoAtendimento;
	}

	public void setEnumTipoAtendimento(EnumTipoAtendimento enumTipoAtendimento) {
		this.enumTipoAtendimento = enumTipoAtendimento;
	}
	
	public Boolean getRequerLembreteRetorno() {
		return requerLembreteRetorno;
	}
	
	public void setRequerLembreteRetorno(Boolean requerLembreteRetorno) {
		this.requerLembreteRetorno = requerLembreteRetorno;
	}
}
