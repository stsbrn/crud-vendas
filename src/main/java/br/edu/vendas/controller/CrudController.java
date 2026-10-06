package br.edu.vendas.controller;
import br.edu.vendas.model.*;
import br.edu.vendas.service.CatalogoService;
import jakarta.inject.Inject;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;
public abstract class CrudController<T extends Registro> implements Serializable {
 @Inject protected CatalogoService service;
 protected T atual;private List<T> lista;
 protected abstract Class<T> tipo();protected abstract T criar();
 public T getAtual(){if(atual==null)atual=criar();return atual;}
 public List<T> getLista(){if(lista==null)recarregar();return lista;}
 public void recarregar(){try{lista=service.listar(tipo());}catch(Exception e){lista=new ArrayList<>();erro(e);}}
 public void novo(){atual=criar();}
 public void editar(T item){atual=item;}
 public void cancelar(){novo();recarregar();}
 public void salvar(){try{service.salvar(getAtual());novo();recarregar();mensagem("Registro salvo.");}catch(Exception e){erro(e);}}
 public void excluir(T item){try{service.excluir(item);novo();recarregar();mensagem("Registro excluído.");}catch(Exception e){erro(e);}}
 protected void mensagem(String s){FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(s));}
 protected void erro(Exception e){System.getLogger(getClass().getName()).log(System.Logger.Level.ERROR,"Operação falhou",e);String s=e instanceof IllegalArgumentException?e.getMessage():"Não foi possível concluir. Confira o banco e os vínculos do registro.";FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Atenção",s));}
}
