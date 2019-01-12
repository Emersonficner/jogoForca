package br.com.ulbra.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ulbra.tx.Transacional;
import br.com.ulbra.dao.UsuarioDAO;
import br.com.ulbra.modelo.Usuario;

@Named
@ViewScoped
public class CadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();

	@Inject
	UsuarioDAO dao;

	@Inject
	FacesContext context;

	public Usuario getUsuario() {
		return usuario;
	}

	@Transacional
	public String gravar() {
		boolean existe = dao.existe(this.usuario, false);
		if(existe){
			context.getExternalContext().getFlash().setKeepMessages(true);
			context.addMessage(null, new FacesMessage("Usuario já esta cadastrado"));
			return "";
		}
		if (this.usuario.getId() == null) {
			usuario.setPontos(0);
			this.dao.adiciona(this.usuario);
		}
		this.usuario = new Usuario();

		return "login?faces-redirect=true";
	}
	
	
	
	
	
	
	
	
	
	
	
}
