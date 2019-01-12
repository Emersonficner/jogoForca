package br.com.ulbra.modelo;

public enum TipoEnum {
	PALAVRA("Palavra"),
	FRASE("Frase");
	
	private TipoEnum(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
