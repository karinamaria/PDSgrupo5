package br.ufrn.PDSgrupo5.enumeration;

public enum EnumTipoPapel {
	PACIENTE("Paciente"), PROFISSIONAL_SAUDE ("Profissional da saúde"), TELEFONE("Telefone");

	private String descricao;

	private EnumTipoPapel(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
