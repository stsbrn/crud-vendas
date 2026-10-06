package br.edu.vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "tb_marca")
public class Marca implements Registro {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 120)
  @Column(nullable = false, unique = true, length = 120)
  private String nome;

  @Size(max = 1000)
  @Column(length = 1000)
  private String descricao;

  @NotNull
  @Column(nullable = false)
  private Boolean ativo = true;

  public Long getId() {
    return id;
  }

  public void setId(Long valor) {
    this.id = valor;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String valor) {
    this.nome = valor;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String valor) {
    this.descricao = valor;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean valor) {
    this.ativo = valor;
  }

  @Override
  public boolean equals(Object o) {
    return this == o
        || (o != null
            && getClass() == o.getClass()
            && id != null
            && id.equals(((Registro) o).getId()));
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
