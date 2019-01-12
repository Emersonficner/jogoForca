package br.com.ulbra.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ulbra.dao.UsuarioDAO;
import br.com.ulbra.modelo.Usuario;
import br.com.ulbra.tx.Transacional;

@Named
@SessionScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	UsuarioDAO dao;
	
	@Inject
	FacesContext context;

	private Usuario usuario;
	
	@PostConstruct
	private void init(){
		Usuario usr = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
		usuario = dao.buscaUsuario(usr);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Transacional
	public void gravaPontos() {
		if (usuario.getId() != null) {
			this.dao.atualiza(usuario);			
		}
		usuario = dao.buscaUsuario(usuario);		
	}

}
