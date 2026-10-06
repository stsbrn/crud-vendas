package br.edu.vendas.converter;
import br.edu.vendas.model.Categoria;import br.edu.vendas.service.CatalogoService;
import jakarta.inject.Inject;import jakarta.faces.convert.*;import jakarta.faces.context.FacesContext;import jakarta.faces.component.UIComponent;
@FacesConverter(value="categoriaConverter",managed=true) public class CategoriaConverter implements Converter<Categoria>{
 @Inject CatalogoService service;
 public Categoria getAsObject(FacesContext ctx,UIComponent c,String s){if(s==null || s.isBlank())return null;try{Categoria v=service.buscar(Categoria.class,Long.valueOf(s));if(v==null)throw new IllegalArgumentException();return v;}catch(Exception e){throw new ConverterException(new jakarta.faces.application.FacesMessage("Seleção inválida. Atualize a página."));}}
 public String getAsString(FacesContext ctx,UIComponent c,Categoria v){return v==null || v.getId()==null?"":v.getId().toString();}
}