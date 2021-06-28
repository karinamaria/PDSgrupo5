package br.ufrn.PDSgrupo5.model;

import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.util.List;

@ApiIgnore
@Entity
@Table(name = "paciente")
public class Paciente extends Pessoa {

	private Double altura;

	private Double peso;

//	private List<DoencaCronica> doencaCronica;

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Paciente> dependentes;

	public Paciente() {
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public List<Paciente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(List<Paciente> dependentes) {
		this.dependentes = dependentes;
	}
}
