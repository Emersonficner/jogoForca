package br.com.ulbra.bean;

import java.io.Serializable;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ulbra.dao.JogoDAO;
import br.com.ulbra.modelo.Jogo;
import br.com.ulbra.modelo.TipoEnum;
import br.com.ulbra.tx.Transacional;

@Named
@ViewScoped
public class JogoFormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	JogoDAO dao;
	private List<Jogo> jogos;
	@Inject
	FacesContext context;
	
	private Jogo jogo=new Jogo();
	private Integer jogoId;
	@Transacional
	public String gravar() {
		if(jogo.getDescricao()!=null){
			jogo.setTipo(verificaFraseOuPalavra(jogo.getDescricao()));
			jogo.setDescricao(jogo.getDescricao().toUpperCase());
		}
		if (this.jogo.getId() == null) {			
			this.dao.adiciona(this.jogo);
		} else {
			this.dao.atualiza(this.jogo);
		}

		this.jogo = new Jogo();

		return "cadastroJogo?faces-redirect=true";
	}

	public Jogo getJogo() {
		return jogo; 
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}
	public void carregarjogoPelaId() {
		this.jogo = this.dao.buscaPorId(jogoId);
	}
	public List<Jogo> getJogos() {

		if (this.jogos == null) {
			this.jogos = this.dao.listaTodos();
		}
		return jogos;
	}	
	public void carregar(Jogo jogo) {
		this.jogo = this.dao.buscaPorId(jogo.getId());
	}
	@Transacional
	public void remover(Jogo jogo) {
		System.out.println("Removendo Jogo");
		this.dao.remove(jogo);
		this.jogos = this.dao.listaTodos();
	}
	
	private TipoEnum verificaFraseOuPalavra(String descricaoJogo) {
		// Se tiver epaço em branco vamos considerar que é uma frase
		descricaoJogo.trim();
		String[] array = descricaoJogo.split(" ");
		if (array.length > 1) {
			return TipoEnum.FRASE;
		} else {
			return TipoEnum.PALAVRA;
		}
	}	
}
