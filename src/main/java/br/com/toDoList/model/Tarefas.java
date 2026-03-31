package br.com.toDoList.model;
/**
 * @author Jhonata
 * @version 1.0
 * @since 18/02/2025
 */


import java.time.LocalDateTime;



import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name = "tasks")
@SequenceGenerator(name = "seq_tarefa", sequenceName = "seq_tarefa", allocationSize = 1, initialValue = 1)
public class Tarefas{
	/**
	 * Esta classe é utilizada para criar as tabelas no banco de dados juntamente disponibilizar os atributos
	 * para trazer cada item atraves na conexão dos atributos
	 * @param titulo
	 * @param descricao
	 * @param prioridade
	 */

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tarefa")
	private Long id;
	
	@Column(nullable = false)
	private String titulo;

	@Column(columnDefinition = "TEXT")
	private String descricao;

	@Enumerated(EnumType.STRING)
	private TaskStatus status;

	//private TaskPriority priority;

	private boolean concluido;//SIM OU NÃO
	private String prioridade;//ALTA, MÉDIA, BAIXA - SEPARAR POR COR CADA PRIORIDADE
	
	@Column(name = "due_date")
	private LocalDateTime dueDate; // Data de Vencimento

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;
	
	@Column(name = "data_criacao")
	@CreationTimestamp
	private LocalDateTime dataCriacao; // Armazena a data e hora da criação automaticamente

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	//private LocalDateTime dataConclusao;
	private String categoria;/*TRABALHO, PESSOAL, ESTUDOS*/

	@PrePersist
	protected void onCreate(){
		dataCriacao = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate(){
		updatedAt = LocalDateTime.now();
	}
	
	
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

	public boolean isConcluido() {
		return concluido;
	}

	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	
}

enum TaskStatus {
	PENDING, IN_PROGRESS, COMPLETED, CANCELLED
}

enum TaskPriority {
	LOW, MEDIUM, HIGH
}
