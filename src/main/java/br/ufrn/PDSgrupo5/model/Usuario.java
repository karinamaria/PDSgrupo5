package br.ufrn.PDSgrupo5.model;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoPapel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
@NoArgsConstructor
@EqualsAndHashCode
public class Usuario extends EntidadeAbstrata {

	@Getter @Setter
	private String login;

	@Getter @Setter
	private String senha;

	@Enumerated(EnumType.STRING)
	@Getter @Setter private EnumTipoPapel enumTipoPapel;

}
