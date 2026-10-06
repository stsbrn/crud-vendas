package br.edu.vendas.service;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.vendas.model.*;
import br.edu.vendas.repository.CatalogoRepository;
import jakarta.persistence.*;
import java.lang.reflect.*;
import java.util.*;
import org.junit.jupiter.api.*;

class CatalogoTest {
  CatalogoService service;
  CatalogoRepository repo;
  EntityManagerFactory factory;

  @BeforeEach
  void setup() throws Exception {
    factory =
        Persistence.createEntityManagerFactory(
            "vendas",
            Map.of(
                "jakarta.persistence.jdbc.url",
                "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=PostgreSQL",
                "jakarta.persistence.jdbc.user",
                "sa",
                "jakarta.persistence.jdbc.password",
                "",
                "hibernate.hbm2ddl.auto",
                "create-drop"));
    repo = new CatalogoRepository();
    Field f = CatalogoRepository.class.getDeclaredField("factory");
    f.setAccessible(true);
    f.set(repo, factory);
    service = new CatalogoService();
    service.repository = repo;
  }

  @AfterEach
  void close() {
    factory.close();
  }

  Marca marca(String n) {
    Marca m = new Marca();
    m.setNome(n);
    service.salvar(m);
    return m;
  }

  Categoria categoria(String n, Categoria pai) {
    Categoria c = new Categoria();
    c.setNome(n);
    c.setCategoriaPai(pai);
    service.salvar(c);
    return c;
  }

  @Test
  void marcaUnica() {
    marca("Samsung");
    assertThrows(IllegalArgumentException.class, () -> marca(" samsung "));
    assertEquals(1, service.listar(Marca.class).size());
  }

  @Test
  void arvoreSemCiclos() {
    Categoria a = categoria("Raiz", null), b = categoria("Filha", a), c = categoria("Neta", b);
    a.setCategoriaPai(c);
    assertThrows(IllegalArgumentException.class, () -> service.salvar(a));
    assertThrows(IllegalArgumentException.class, () -> service.excluir(b));
    assertEquals("Raiz / Filha / Neta", service.buscar(Categoria.class, c.getId()).getCaminho());
  }

  @Test
  void produtoCrudEVinculos() {
    Marca m = marca("Teste");
    Categoria c = categoria("Teste", null);
    Produto p = new Produto();
    p.setNome("Produto");
    p.setPreco(new java.math.BigDecimal("10.00"));
    p.setMarca(m);
    p.setCategoria(c);
    service.salvar(p);
    assertThrows(IllegalArgumentException.class, () -> service.excluir(m));
    assertThrows(IllegalArgumentException.class, () -> service.excluir(c));
    p.setQuantidadeEstoque(7);
    service.salvar(p);
    assertEquals(7, service.listar(Produto.class).get(0).getQuantidadeEstoque());
    service.excluir(p);
    service.excluir(c);
    service.excluir(m);
    assertTrue(service.listar(Produto.class).isEmpty());
  }

  @Test
  void valoresInvalidos() {
    Produto p = new Produto();
    p.setNome("X");
    p.setPreco(java.math.BigDecimal.ZERO);
    p.setQuantidadeEstoque(-1);
    assertThrows(IllegalArgumentException.class, () -> service.salvar(p));
  }

  @Test
  void assinaturasImagens() throws Exception {
    assertEquals(
        "png", ImagemService.extensao(new byte[] {(byte) 137, 80, 78, 71, 13, 10, 26, 10}));
    assertEquals("jpg", ImagemService.extensao(new byte[] {(byte) 255, (byte) 216, (byte) 255}));
    assertEquals("webp", ImagemService.extensao("RIFF1234WEBP".getBytes()));
    assertThrows(
        IllegalArgumentException.class, () -> ImagemService.extensao("arquivo.txt".getBytes()));
    ImagemService s = new ImagemService();
    assertNull(s.localizar("../../etc/passwd"));
    assertNotNull(s.localizar("12345678-1234-1234-1234-123456789abc.png"));
  }
}
