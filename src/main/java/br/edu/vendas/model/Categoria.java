package br.edu.vendas.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;
@Entity @Table(name="tb_categoria")
public class Categoria implements Registro {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;
@NotBlank @Size(max=120) @Column(nullable=false,length=120)
private String nome;
@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="categoria_id")
private Categoria categoriaPai;
@OneToMany(mappedBy="categoriaPai") private List<Categoria> subcategorias = new ArrayList<>();
public List<Categoria> getSubcategorias(){return subcategorias;}
public String getCaminho(){ return categoriaPai==null ? nome : categoriaPai.getCaminho()+" / "+nome; }
public Long getId(){return id;}
public void setId(Long valor){this.id=valor;}
public String getNome(){return nome;}
public void setNome(String valor){this.nome=valor;}
public Categoria getCategoriaPai(){return categoriaPai;}
public void setCategoriaPai(Categoria valor){this.categoriaPai=valor;}
@Override public boolean equals(Object o){return this==o || (o!=null && getClass()==o.getClass() && id!=null && id.equals(((Registro)o).getId()));}
@Override public int hashCode(){return getClass().hashCode();}
}
