package br.edu.vendas.controller;

import br.edu.vendas.model.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class CategoriaController extends CrudController<Categoria> {
  protected Class<Categoria> tipo() {
    return Categoria.class;
  }

  protected Categoria criar() {
    return new Categoria();
  }
}
