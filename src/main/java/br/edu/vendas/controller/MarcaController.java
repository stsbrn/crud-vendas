package br.edu.vendas.controller;

import br.edu.vendas.model.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class MarcaController extends CrudController<Marca> {
  protected Class<Marca> tipo() {
    return Marca.class;
  }

  protected Marca criar() {
    return new Marca();
  }
}
