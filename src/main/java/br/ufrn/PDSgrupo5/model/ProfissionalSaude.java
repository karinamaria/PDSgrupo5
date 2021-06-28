package br.ufrn.PDSgrupo5.model;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoRegistro;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "profissional_saude")
@NoArgsConstructor
@EqualsAndHashCode
public class ProfissionalSaude extends Pessoa {

	@Getter @Setter private Long numeroRegistro;

	@Column(name="data_aprovacao_registro")
	@Getter @Setter private Date dataAprovacaoRegistro;

	@Getter @Setter private String descricao;

	@Column(name="tipo_registro")
	@Enumerated(EnumType.STRING)
	@Getter @Setter private EnumTipoRegistro enumTipoRegistro;

	@Column(name="turno_atendimento")
	@OneToMany(cascade = CascadeType.ALL)
	@Getter @Setter private List<TurnoAtendimento> turnoAtendimento;

}
