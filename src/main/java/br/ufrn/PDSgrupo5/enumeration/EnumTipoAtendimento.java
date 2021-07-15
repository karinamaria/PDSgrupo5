package br.ufrn.PDSgrupo5.enumeration;

public enum EnumTipoAtendimento {

	DOMICILIO("em domicilio"), CONSULTORIO ("consultório"), TELEFONE("telefone");

	private String descricao;

	private EnumTipoAtendimento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
