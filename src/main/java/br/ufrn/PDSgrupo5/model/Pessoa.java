package br.ufrn.PDSgrupo5.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
public class Pessoa extends EntidadeAbstrata {

	@Getter @Setter private String nome;

	@Getter @Setter
	private String cpf;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "data_nascimento", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	@Getter @Setter private Date dataNascimento;

	@Getter @Setter
	private char sexo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	@Getter @Setter private Usuario usuario;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id", referencedColumnName = "id")
	@Getter @Setter private Endereco endereco;

}
