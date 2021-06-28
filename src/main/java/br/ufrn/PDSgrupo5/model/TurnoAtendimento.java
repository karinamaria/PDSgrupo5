package br.ufrn.PDSgrupo5.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "turno_atendimento")
@NoArgsConstructor
@EqualsAndHashCode
public class TurnoAtendimento extends EntidadeAbstrata{

	@Column(name="dia_semana")
	@Getter @Setter private String diaSemana;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "horario_inicio", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	@Getter @Setter private Date horarioInicio;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "horario_fim", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	@Getter @Setter private Date horarioFim;
}
