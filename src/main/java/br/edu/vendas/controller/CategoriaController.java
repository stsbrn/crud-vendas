package br.edu.vendas.controller;
import br.edu.vendas.model.*;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
@Named @SessionScoped public class CategoriaController extends CrudController<Categoria> {
 protected Class<Categoria> tipo(){return Categoria.class;} protected Categoria criar(){return new Categoria();}
}