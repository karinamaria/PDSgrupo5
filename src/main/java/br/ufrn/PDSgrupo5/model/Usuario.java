package br.ufrn.PDSgrupo5.model;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoPapel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario extends EntidadeAbstrata {
	@Column(unique = true)
	private String login;

	@JsonIgnore
	private String senha;

	@Column(name="papel")
	@Enumerated(EnumType.STRING)
	private EnumTipoPapel enumTipoPapel;

	public Usuario() {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public EnumTipoPapel getEnumTipoPapel() {
		return enumTipoPapel;
	}

	public void setEnumTipoPapel(EnumTipoPapel enumTipoPapel) {
		this.enumTipoPapel = enumTipoPapel;
	}
}
