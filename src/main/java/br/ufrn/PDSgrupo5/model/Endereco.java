package br.ufrn.PDSgrupo5.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "endereco")
@NoArgsConstructor
@EqualsAndHashCode
public class Endereco extends EntidadeAbstrata {

	@Getter @Setter
	private String cidade;

	@Getter @Setter
	private String bairro;

	@Getter @Setter
	private String rua;

	@Getter @Setter
	private Integer numero;

	@Getter @Setter
	private String complemento;

	@Getter @Setter
	private String cep;

	@Getter @Setter
	private String pais;

}
