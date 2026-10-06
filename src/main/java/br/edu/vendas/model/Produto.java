package br.edu.vendas.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;
@Entity @Table(name="tb_produto")
public class Produto implements Registro {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;
@NotBlank @Size(max=160) @Column(nullable=false,length=160)
private String nome;
@Column(columnDefinition="text") @Size(max=10000)
private String descricao;
@NotNull @DecimalMin(value="0",inclusive=false) @Digits(integer=10,fraction=2) @Column(nullable=false,precision=12,scale=2)
private BigDecimal preco;
@NotNull @Min(0) @Column(name="quantidade_estoque",nullable=false)
private Integer quantidadeEstoque = 0;
@NotNull @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="marca_id",nullable=false)
private Marca marca;
@NotNull @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="categoria_id",nullable=false)
private Categoria categoria;
@Column(name="caminho_imagem",length=80)
private String caminhoImagem;
public Long getId(){return id;}
public void setId(Long valor){this.id=valor;}
public String getNome(){return nome;}
public void setNome(String valor){this.nome=valor;}
public String getDescricao(){return descricao;}
public void setDescricao(String valor){this.descricao=valor;}
public BigDecimal getPreco(){return preco;}
public void setPreco(BigDecimal valor){this.preco=valor;}
public Integer getQuantidadeEstoque(){return quantidadeEstoque;}
public void setQuantidadeEstoque(Integer valor){this.quantidadeEstoque=valor;}
public Marca getMarca(){return marca;}
public void setMarca(Marca valor){this.marca=valor;}
public Categoria getCategoria(){return categoria;}
public void setCategoria(Categoria valor){this.categoria=valor;}
public String getCaminhoImagem(){return caminhoImagem;}
public void setCaminhoImagem(String valor){this.caminhoImagem=valor;}
@Override public boolean equals(Object o){return this==o || (o!=null && getClass()==o.getClass() && id!=null && id.equals(((Registro)o).getId()));}
@Override public int hashCode(){return getClass().hashCode();}
}
