package br.com.ulbra.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ulbra.dao.UsuarioDAO;
import br.com.ulbra.modelo.Categoria;
import br.com.ulbra.modelo.Usuario;
import br.com.ulbra.tx.Transacional;

@Named
@ViewScoped
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();
	
	@Inject
	UsuarioDAO dao;
	
	@Inject
	FacesContext context;
	
	public Usuario getUsuario() {
		return usuario;
	}

	public String efetuaLogin() {
		System.out.println("fazendo login do usuario "
				+ this.usuario.getEmail());

		boolean existe = dao.existe(this.usuario);
		if (existe) {
			context.getExternalContext().getSessionMap().put("usuarioLogado", this.usuario);
			return "jogo?faces-redirect=true";
		}

		context.getExternalContext().getFlash().setKeepMessages(true);
		context.addMessage(null, new FacesMessage("Usuario nao encontrado"));

		return "login?faces-redirect=true";
	}

	public String deslogar() {
		context.getExternalContext().getSessionMap().remove("usuarioLogado");
		return "login?faces-redirect=true";
	}
	@Transacional
	public String gravar() {
		System.out.println("cadastrando ");

		if (this.usuario.getId() == null) {
			this.dao.adiciona(this.usuario);
		} 

		this.usuario = new Usuario();

		return "login?faces-redirect=true";
	}
	
	public List<Usuario> getUsuarios() {
		return this.dao.lista();
	}
}
