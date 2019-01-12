package br.com.ulbra.modelo;

public enum NivelEnum {
	FACIL("Fácil"),	
	MEDIO("Médio"),
	DIFICIL("Difícil");
	
	private String descricao;	

	NivelEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
}
