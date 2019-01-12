package br.com.ulbra.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.ulbra.modelo.Usuario;

public class UsuarioDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	EntityManager em;

	private DAO<Usuario> dao;

	@PostConstruct
	void init() {
		this.dao = new DAO<Usuario>(this.em, Usuario.class);
	}

	public boolean existe(Usuario usuario) {
		return existe(usuario, true);
	}
	
	public boolean existe(Usuario usuario, boolean senha) {
		StringBuilder sb = new StringBuilder();
		sb.append("select u from Usuario u " + " where u.email = :pEmail ");
		if(senha)
			sb.append(" and u.senha = :pSenha ");
		TypedQuery<Usuario> query = em.createQuery(sb.toString(), Usuario.class);

		query.setParameter("pEmail", usuario.getEmail());
		if(senha)
			query.setParameter("pSenha", usuario.getSenha());
		try {
			Usuario resultado = query.getSingleResult();
			if (resultado != null && resultado.getId() != null) {
				return true;
			}
		} catch (NoResultException ex) {
			return false;
		}
		return false;
	}

	public Usuario buscaUsuario(Usuario usuario) {

		TypedQuery<Usuario> query = em.createQuery(
				" select u from Usuario u " + " where u.email = :pEmail and u.senha = :pSenha", Usuario.class);

		query.setParameter("pEmail", usuario.getEmail());
		query.setParameter("pSenha", usuario.getSenha());
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public void adiciona(Usuario usuario) {
		this.dao.adiciona(usuario);
	}

	public void atualiza(Usuario usuario) {
		this.dao.atualiza(usuario);
	}

	public List<Usuario> lista() {

		TypedQuery<Usuario> query = em.createQuery(
				" select u from Usuario u order by u.pontos desc", Usuario.class);

		try {
			return query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
