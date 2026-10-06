package br.edu.vendas.controller;
import br.edu.vendas.model.*;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
@Named @SessionScoped public class MarcaController extends CrudController<Marca> {
 protected Class<Marca> tipo(){return Marca.class;} protected Marca criar(){return new Marca();}
}