package br.edu.vendas.converter;

import br.edu.vendas.model.Marca;
import br.edu.vendas.service.CatalogoService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.*;
import jakarta.inject.Inject;

@jakarta.enterprise.context.Dependent
@FacesConverter(value = "marcaConverter", managed = true)
public class MarcaConverter implements Converter<Marca> {
  @Inject CatalogoService service;

  public Marca getAsObject(FacesContext ctx, UIComponent c, String s) {
    if (s == null || s.isBlank()) return null;
    try {
      Marca v = service.buscar(Marca.class, Long.valueOf(s));
      if (v == null) throw new IllegalArgumentException();
      return v;
    } catch (Exception e) {
      throw new ConverterException(
          new jakarta.faces.application.FacesMessage("Seleção inválida. Atualize a página."));
    }
  }

  public String getAsString(FacesContext ctx, UIComponent c, Marca v) {
    return v == null || v.getId() == null ? "" : v.getId().toString();
  }
}
