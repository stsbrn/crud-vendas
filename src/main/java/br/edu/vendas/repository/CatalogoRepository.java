package br.edu.vendas.repository;

import br.edu.vendas.model.*;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import java.util.*;
import java.util.function.Function;

@ApplicationScoped
public class CatalogoRepository {
  private EntityManagerFactory factory;

  private synchronized EntityManagerFactory factory() {
    if (factory == null) {
      Map<String, Object> p = new HashMap<>();
      p.put(
          "jakarta.persistence.jdbc.url",
          env("VENDAS_DB_URL", "jdbc:postgresql://localhost:5432/vendas"));
      p.put("jakarta.persistence.jdbc.user", env("VENDAS_DB_USER", "postgres"));
      p.put("jakarta.persistence.jdbc.password", env("VENDAS_DB_PASSWORD", "postgres"));
      factory = Persistence.createEntityManagerFactory("vendas", p);
    }
    return factory;
  }

  private String env(String k, String d) {
    return System.getenv().getOrDefault(k, d);
  }

  public <T> T executar(Function<EntityManager, T> acao) {
    EntityManager em = factory().createEntityManager();
    try {
      em.getTransaction().begin();
      T r = acao.apply(em);
      em.getTransaction().commit();
      return r;
    } catch (RuntimeException e) {
      if (em.getTransaction().isActive()) em.getTransaction().rollback();
      throw e;
    } finally {
      em.close();
    }
  }

  public <T extends Registro> List<T> listar(Class<T> tipo) {
    return executar(
        em -> {
          String q = "select e from " + tipo.getSimpleName() + " e";
          if (tipo == Produto.class) q += " join fetch e.marca join fetch e.categoria";
          List<T> lista = em.createQuery(q + " order by e.nome", tipo).getResultList();
          for (T item : lista) {
            if (item instanceof Categoria c) c.getCaminho();
            if (item instanceof Produto p) p.getCategoria().getCaminho();
          }
          return lista;
        });
  }

  public <T extends Registro> T buscar(Class<T> tipo, Long id) {
    return executar(
        em -> {
          T e = em.find(tipo, id);
          if (e instanceof Categoria c) c.getCaminho();
          return e;
        });
  }

  @PreDestroy
  public void fechar() {
    if (factory != null) factory.close();
  }
}
