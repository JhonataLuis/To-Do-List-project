package br.com.toDoList.model;
/**
 * @author Jhonata
 * @version 1.0
 * @since 18/02/2025
 */

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@SequenceGenerator(name = "seq_tarefa", sequenceName = "seq_tarefa", allocationSize = 1, initialValue = 1)
public class Tarefas implements Serializable{
	/**
	 * Esta classe é utilizada para criar as tabelas no banco de dados juntamente disponibilizar os atributos
	 * para trazer cada item atraves na conexão dos atributos
	 * @param titulo
	 * @param descricao
	 * @param prioridade
	 */

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tarefa")
	private Long id;
	
	private String titulo;
	private String descricao;
	//private boolean concluido;//SIM OU NÃO
	private String prioridade;//ALTA, MÉDIA, BAIXA - SEPARAR POR COR CADA PRIORIDADE
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.S")
	private LocalDateTime dataCriacao;
	
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	//private LocalDateTime dataConclusao;
	private String categoria;/*TRABALHO, PESSOAL, ESTUDOS*/
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}
	

	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	
	
}
