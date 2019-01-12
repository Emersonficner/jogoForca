package br.com.ulbra.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.primitives.Chars;

import br.com.ulbra.dao.CategoriaDAO;
import br.com.ulbra.dao.JogoDAO;
import br.com.ulbra.modelo.Categoria;
import br.com.ulbra.modelo.Jogo;
import br.com.ulbra.modelo.NivelEnum;
import br.com.ulbra.modelo.TipoEnum;

@Named
@ViewScoped
public class JogoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	private JogoDAO jogoDAO;
	
	@Inject
	UsuarioBean usuarioBean;
	
	@Inject
	private CategoriaDAO categoriaDAO;
	
	FacesContext context = FacesContext.getCurrentInstance();
		
	private List<SelectItem> listaNivel;
	private List<SelectItem> listaCategoria;
	private List<SelectItem> listaTipo;
	private Categoria categoria;
	private TipoEnum tipo;
	private NivelEnum nivel;
	private List<Categoria> categorias;
	private Jogo jogo;
	private List<Jogo> jogos = new LinkedList<>();
	private boolean disabled = false;
	private List<Character> charList;
	private List<Character> charListCompleto;
	private List<Character> charListEscolhido= new LinkedList<>();
	private Character letra;
	private Integer pontos=0;
	private Integer erros=0;
	
	
	@PostConstruct
	void init(){
		//inicia apenas com carregamento de tela: eventos ajax por exemplo botão não executa esse método
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		categorias = categoriaDAO.listaTodos();
		
	}
	
	public void novoJogo(){
		charListEscolhido= new LinkedList<>();
		jogos = jogoDAO.busca(categoria, tipo);
		if(jogos==null || jogos.isEmpty()){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nehum jogo cadastrado com essas opções!", "Nehum jogo cadastrado com essas opções!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
		disabled = true;
		pontos=0;
    	erros=0;
		jogo = jogos.get(sorteia(jogos.size()));
		charListCompleto= Chars.asList(jogo.getDescricao().toCharArray());
		charList = new ArrayList<>(charListCompleto.size());
		for (Character c : charListCompleto) {
			char vazio = ' ';
			charList.add(vazio);
		}
		}
	}
	
	public void verifica() {
		letra= Character.toUpperCase(letra);
		if (verificaLetraExiste()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Você já digitou essa letra!",
					"Você já digitou essa letra!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else if(verificaJogoTerminou()){
			encerraJogo();
		}else {// adiciona letra
			if(!verificaAcertouLetra()){
				erros++;
			}
			charListEscolhido.add(letra);
			for (int i = 0; i < charListCompleto.size(); i++) {
				if (charListCompleto.get(i).equals(letra)) {
					charList.remove(i);
					charList.add(i, letra);
					pontos+=20;
				}
			}
		}
		if(verificaJogoTerminou()){
			encerraJogo();       
		}
		if(erros.intValue()==8){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Inicie novo jogo!", "Inicie novo jogo!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            disabled=false;
            gravaPontos();
		}
		letra= null;
	}

	private void encerraJogo() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Parabéns você acertou!", "Parabéns você acertou!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		if(TipoEnum.FRASE.equals(jogo.getTipo())){
			pontos+=200;
		}else if(TipoEnum.PALAVRA.equals(jogo.getTipo())){
			pontos+=100;
		}
		disabled=false;
		gravaPontos();
	}

	private void gravaPontos() {
		usuarioBean.getUsuario().setPontos(usuarioBean.getUsuario().getPontos()+pontos);
		usuarioBean.gravaPontos();
	}
	
	private int sorteia(int size){
		return new Random().nextInt(size);
	}
	
	public List<SelectItem> getListaCategoria(){
		if(listaCategoria==null){
			listaCategoria = new LinkedList<>();
			for(Categoria c : categorias){
				listaCategoria.add(new SelectItem(c, c.getCategoria()));
			}
		}
		return listaCategoria;
	}
	
	public List<SelectItem> getListaTipo(){
		if(listaTipo==null){
		 	listaTipo = new LinkedList<>();
			for(TipoEnum c : TipoEnum.values()){
				listaTipo.add(new SelectItem(c.name(), c.getDescricao()));
			}
		}
		return listaTipo;
	}
	public List<SelectItem> getListaNivel(){
		if(listaNivel==null){
			listaNivel = new LinkedList<>();
			for(NivelEnum n : NivelEnum.values()){
				listaNivel.add(new SelectItem(n,n.getDescricao()));
			}
		}
		return listaNivel;
		
	}
	
	private boolean verificaJogoTerminou(){
		for (int i = 0; i < charListCompleto.size(); i++) {
			if(!charListCompleto.get(i).equals(charList.get(i))){
				return false;
			}
		}
		return true;
	}
	
	private boolean verificaLetraExiste(){
		return charListEscolhido.contains(letra);
	}
	
	private boolean verificaAcertouLetra(){
		return charListCompleto.contains(letra);
	}
	
	public List<Character> getCharListEscolhido() {
		return charListEscolhido;
	}

	public void setCharListEscolhido(List<Character> charListEscolhido) {
		this.charListEscolhido = charListEscolhido;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public TipoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoEnum tipo) {
		this.tipo = tipo;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<Character> getCharList() {
		return charList;
	}

	public void setCharList(List<Character> charList) {
		this.charList = charList;
	}

	public Character getLetra() {
		return letra;
	}

	public void setLetra(Character letra) {
		this.letra = letra;
	}

	public NivelEnum getNivel() {
		return nivel;
	}

	public void setNivel(NivelEnum nivel) {
		this.nivel = nivel;
	}

	public Integer getPontos() {
		return pontos;
	}

	public void setPontos(Integer pontos) {
		this.pontos = pontos;
	}

	public Integer getErros() {
		return erros;
	}

	public void setErros(Integer erros) {
		this.erros = erros;
	}

	public Jogo getJogo() {
		return jogo;
	}
	
	
}
