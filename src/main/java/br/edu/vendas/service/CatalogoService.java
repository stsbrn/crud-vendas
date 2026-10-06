package br.edu.vendas.service;

import br.edu.vendas.model.*;
import br.edu.vendas.repository.CatalogoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.validation.Validation;
import java.util.*;

@ApplicationScoped
public class CatalogoService {
  @Inject CatalogoRepository repository;

  public <T extends Registro> List<T> listar(Class<T> tipo) {
    return repository.listar(tipo);
  }

  public <T extends Registro> T buscar(Class<T> tipo, Long id) {
    return repository.buscar(tipo, id);
  }

  public <T extends Registro> void salvar(T item) {
    if (item instanceof Marca m) m.setNome(m.getNome() == null ? null : m.getNome().trim());
    if (item instanceof Categoria c) c.setNome(c.getNome() == null ? null : c.getNome().trim());
    if (item instanceof Produto p) p.setNome(p.getNome() == null ? null : p.getNome().trim());
    try (var vf = Validation.buildDefaultValidatorFactory()) {
      var erros = vf.getValidator().validate(item);
      if (!erros.isEmpty())
        throw new IllegalArgumentException(erros.iterator().next().getMessage());
    }
    repository.executar(
        em -> {
          if (item instanceof Marca m) {
            long n =
                em.createQuery(
                        "select count(m) from Marca m where lower(trim(m.nome))=:nome and (:id is"
                            + " null or m.id<>:id)",
                        Long.class)
                    .setParameter("nome", m.getNome().toLowerCase(Locale.ROOT))
                    .setParameter("id", m.getId())
                    .getSingleResult();
            if (n > 0) throw new IllegalArgumentException("Já existe uma marca com esse nome.");
          }
          if (item instanceof Categoria c) validarPai(em, c);
          if (item instanceof Produto p) {
            Marca m = em.find(Marca.class, p.getMarca().getId());
            Categoria c = em.find(Categoria.class, p.getCategoria().getId());
            if (m == null || c == null)
              throw new IllegalArgumentException("Marca ou categoria não existe mais.");
            p.setMarca(m);
            p.setCategoria(c);
          }
          if (item.getId() == null) em.persist(item);
          else em.merge(item);
          return null;
        });
  }

  private void validarPai(EntityManager em, Categoria c) {
    Categoria pai = c.getCategoriaPai();
    Set<Long> vistos = new HashSet<>();
    if (pai != null) {
      pai = em.find(Categoria.class, pai.getId());
      if (pai == null) throw new IllegalArgumentException("Categoria pai não existe mais.");
    }
    for (Categoria atual = pai; atual != null; atual = atual.getCategoriaPai()) {
      if (Objects.equals(c.getId(), atual.getId()) || !vistos.add(atual.getId()))
        throw new IllegalArgumentException(
            "Uma categoria não pode ser filha de si mesma ou de uma descendente.");
    }
    c.setCategoriaPai(pai);
  }

  public void excluir(Registro item) {
    repository.executar(
        em -> {
          if (item instanceof Categoria
              && em.createQuery(
                          "select count(c) from Categoria c where c.categoriaPai.id=:id",
                          Long.class)
                      .setParameter("id", item.getId())
                      .getSingleResult()
                  > 0)
            throw new IllegalArgumentException("Remova ou mova as subcategorias antes de excluir.");
          if (!(item instanceof Produto)) {
            String campo = item instanceof Marca ? "marca" : "categoria";
            if (em.createQuery(
                        "select count(p) from Produto p where p." + campo + ".id=:id", Long.class)
                    .setParameter("id", item.getId())
                    .getSingleResult()
                > 0)
              throw new IllegalArgumentException(
                  "Este registro está vinculado a produtos e não pode ser excluído.");
          }
          Object salvo = em.find(item.getClass(), item.getId());
          if (salvo != null) em.remove(salvo);
          return null;
        });
  }
}
