package br.com.ulbra.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ulbra.dao.CategoriaDAO;
import br.com.ulbra.modelo.Categoria;
import br.com.ulbra.tx.Transacional;



@Named
@ViewScoped
public class CategoriaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Categoria categoria = new Categoria();

	private Integer categoriaId;

	@Inject
	private CategoriaDAO dao;

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	public void carregarCategoriaPelaId() {
		this.categoria = this.dao.buscaPorId(categoriaId);
	}

	@Transacional
	public String gravar() {
		System.out.println("Gravando autor " + this.categoria.getCategoria());

		if (this.categoria.getId() == null) {
			this.dao.adiciona(this.categoria);
		} else {
			this.dao.atualiza(this.categoria);
		}

		this.categoria = new Categoria();

		return "palavra?faces-redirect=true";
	}

	@Transacional
	public void remover(Categoria categoria) {
		System.out.println("Removendo autor " + categoria.getCategoria());
		this.dao.remove(categoria);
	}

	public List<Categoria> getCategorias() {
		return this.dao.listaTodos();
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}
