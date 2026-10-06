package br.edu.vendas.controller;
import br.edu.vendas.model.*;
import br.edu.vendas.service.ImagemService;
import jakarta.inject.*;
import jakarta.enterprise.context.SessionScoped;
import org.primefaces.event.FileUploadEvent;
import java.util.*;
@Named @SessionScoped public class ProdutoController extends CrudController<Produto>{
 @Inject ImagemService imagens;private String pendente;
 protected Class<Produto> tipo(){return Produto.class;}protected Produto criar(){return new Produto();}
 public List<Marca> getMarcas(){return service.listar(Marca.class);}
 public List<Categoria> getCategorias(){return service.listar(Categoria.class);}
 public String getPreview(){return pendente!=null?pendente:getAtual().getCaminhoImagem();}
 public void upload(FileUploadEvent evento){try(var in=evento.getFile().getInputStream()){
  String nome=imagens.salvar(in);if(pendente!=null)imagens.excluir(pendente);pendente=nome;mensagem("Imagem recebida. Salve o produto para confirmar.");
 }catch(Exception e){erro(e);}}
 @Override public void novo(){if(pendente!=null)imagens.excluir(pendente);pendente=null;super.novo();}
 @Override public void editar(Produto p){novo();super.editar(p);}
 @Override public void salvar(){String anterior=getAtual().getCaminhoImagem();try{
  if(pendente!=null)getAtual().setCaminhoImagem(pendente);service.salvar(getAtual());boolean mudou=pendente!=null;pendente=null;
  if(mudou && anterior!=null)imagens.excluir(anterior);novo();recarregar();mensagem("Produto salvo.");
 }catch(Exception e){getAtual().setCaminhoImagem(anterior);erro(e);}}
 @Override public void excluir(Produto p){try{service.excluir(p);imagens.excluir(p.getCaminhoImagem());novo();recarregar();mensagem("Produto excluído.");}catch(Exception e){erro(e);}}
}
