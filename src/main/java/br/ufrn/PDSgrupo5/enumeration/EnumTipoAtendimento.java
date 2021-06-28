package br.ufrn.PDSgrupo5.enumeration;

public enum EnumTipoAtendimento {

	DOMICILIO("Em domicilío"), CONSULTORIO ("Consultório"), TELEFONE("Telefone");

	private String descricao;

	private EnumTipoAtendimento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
