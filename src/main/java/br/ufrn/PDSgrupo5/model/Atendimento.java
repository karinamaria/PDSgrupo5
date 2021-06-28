package br.ufrn.PDSgrupo5.model;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoAtendimento;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "atendimento")
@NoArgsConstructor
@EqualsAndHashCode
public class Atendimento extends EntidadeAbstrata {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "horario", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	@Getter @Setter private Date horario;

	@Getter @Setter private Integer duracao;

	@Getter @Setter private Double preco;

	@Getter @Setter private String titulo;

	@Getter @Setter private String descricao;

	@Getter @Setter private String status;

	@OneToOne
	@Getter @Setter private Paciente paciente;

	@OneToOne(cascade = CascadeType.ALL)
	@Getter @Setter private ProfissionalSaude profissionalSaude;

	@Column(name="tipo_atendimento")
	@Enumerated(EnumType.STRING)
	@Getter @Setter private EnumTipoAtendimento enumTipoAtendimento;

}
