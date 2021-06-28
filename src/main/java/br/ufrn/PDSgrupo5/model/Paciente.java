package br.ufrn.PDSgrupo5.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "paciente")
@NoArgsConstructor
@EqualsAndHashCode
public class Paciente extends Pessoa {

	@Getter @Setter private Double altura;

	@Getter @Setter private Double peso;

//	@Getter @Setter private List<DoencaCronica> doencaCronica;

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@Getter @Setter private List<Paciente> dependentes;
}
