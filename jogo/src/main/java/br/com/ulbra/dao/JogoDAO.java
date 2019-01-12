package br.com.ulbra.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.ulbra.modelo.Categoria;
import br.com.ulbra.modelo.Jogo;
import br.com.ulbra.modelo.TipoEnum;
import br.com.ulbra.modelo.Usuario;

public class JogoDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	EntityManager em;
	

	private DAO<Jogo> dao;

	@PostConstruct
	void init() {
		this.dao = new DAO<Jogo>(this.em, Jogo.class);
	}

	public List<Jogo> listaTodos() {
		return dao.listaTodos();
	}

	public List<Jogo> busca(Categoria categoria, TipoEnum tipo){
		StringBuilder sb = new StringBuilder();
		sb.append("select j from Jogo j ");
		if(categoria!=null || tipo!=null)
			sb.append(" where ");
		if(categoria!=null){
			sb.append(" j.categoria.id= :pCategoria ");
		}
		if(categoria!=null && tipo!=null)
			sb.append(" and ");
		if(tipo!=null){
			sb.append(" j.tipo= :pTipo ");
		}
		TypedQuery<Jogo> query = em.createQuery(sb.toString(), Jogo.class);
		if(categoria!=null){
			query.setParameter("pCategoria", categoria.getId());
		}
		if(tipo!=null){
			query.setParameter("pTipo", tipo);
		}
		try {
			return query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}
	
	public Jogo buscaPorId(Integer jogoId) {
		return this.dao.buscaPorId(jogoId);
	}
	public void adiciona(Jogo jogo) {
		this.dao.adiciona(jogo);
	}

	public void atualiza(Jogo jogo) {
		this.dao.atualiza(jogo);
	}

	public void remove(Jogo jogo) {
		this.dao.remove(jogo);
	}
	
}
