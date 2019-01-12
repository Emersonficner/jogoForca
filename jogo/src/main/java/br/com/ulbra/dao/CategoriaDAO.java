package br.com.ulbra.dao;

import java.io.Serializable;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import br.com.ulbra.modelo.Categoria;

public class CategoriaDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject
	EntityManager em;
	
	private DAO<Categoria> dao;
	
	@PostConstruct
	void init() {
		this.dao = new DAO<Categoria>(this.em, Categoria.class);
	}
	
	public List<Categoria> listaTodos(){
		return dao.listaTodos();
	}
	
	public Categoria buscaPorId(Integer categoriaId) {
		return this.dao.buscaPorId(categoriaId);
	}

	public void adiciona(Categoria categoria) {
		this.dao.adiciona(categoria);
	}

	public void atualiza(Categoria categoria) {
		this.dao.atualiza(categoria);
	}

	public void remove(Categoria categoria) {
		this.dao.remove(categoria);
	}
	}
	
	
	
	
	
	
	
	
	

